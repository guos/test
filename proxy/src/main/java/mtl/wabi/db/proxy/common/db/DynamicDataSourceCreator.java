
package mtl.wabi.db.proxy.common.db;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.common.DBConstants;
import mtl.wabi.db.proxy.pojo.DataSourceProperty;
import mtl.wabi.db.proxy.pojo.HikariCpConfig;

@Slf4j
@Component
public class DynamicDataSourceCreator {
	/**
	 * JNDI data source finder
	 */
	private static final JndiDataSourceLookup JNDI_DATA_SOURCE_LOOKUP = new JndiDataSourceLookup();

	private Method createMethod;
	private Method typeMethod;
	private Method urlMethod;
	private Method usernameMethod;
	private Method passwordMethod;
	private Method driverClassNameMethod;
	private Method buildMethod;

	/**
	 * hikari check
	 */
	private Boolean hikariExists = false;

	public DynamicDataSourceCreator() {
		Class<?> builderClass = null;
		try {
			builderClass = Class.forName("org.springframework.boot.jdbc.DataSourceBuilder");
		} catch (Exception e) {
			try {
				builderClass = Class.forName("org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder");
			} catch (Exception e1) {
			}
		}
		try {
			createMethod = builderClass.getDeclaredMethod("create");
			typeMethod = builderClass.getDeclaredMethod("type", Class.class);
			urlMethod = builderClass.getDeclaredMethod("url", String.class);
			usernameMethod = builderClass.getDeclaredMethod("username", String.class);
			passwordMethod = builderClass.getDeclaredMethod("password", String.class);
			driverClassNameMethod = builderClass.getDeclaredMethod("driverClassName", String.class);
			buildMethod = builderClass.getDeclaredMethod("build");
		} catch (Exception e) {
			log.error("exception for DataSource creator:{} ", e);
		}

		try {
			Class.forName(DBConstants.HIKARI_DATASOURCE);
			hikariExists = true;
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * create datasource
	 *
	 * @param dataSourceProperty datasource info
	 * @return Datasource
	 */
	public DataSource createDataSource(DataSourceProperty dataSourceProperty) {

		log.info("use datasource info [{}] to create datasource", dataSourceProperty);

		// if jndi
		String jndiName = dataSourceProperty.getJndiName();
		if (jndiName != null && !jndiName.isEmpty()) {
			return createJNDIDataSource(jndiName);
		}
		Class<? extends DataSource> type = dataSourceProperty.getType();
		if (type == null) {
			log.info("There is no datasource type configed");
			if (hikariExists) {
				log.info("Default Create hikari data source");
				return createHikariDataSource(dataSourceProperty);
			}
		} else if (DBConstants.HIKARI_DATASOURCE.equals(type.getName())) {
			log.info("Create hikari data source");
			return createHikariDataSource(dataSourceProperty);
		}
		log.info("use default basice datasource");
		return createBasicDataSource(dataSourceProperty);
	}

	/**
	 * basice data source
	 * 
	 * @param dataSourceProperty
	 * @return
	 */
	public DataSource createBasicDataSource(DataSourceProperty dataSourceProperty) {
		log.info("Create the basic data source use property: {}", dataSourceProperty);
		try {
			Object o1 = createMethod.invoke(null);
			Object o2 = typeMethod.invoke(o1, dataSourceProperty.getType());
			Object o3 = urlMethod.invoke(o2, dataSourceProperty.getUrl());
			Object o4 = usernameMethod.invoke(o3, dataSourceProperty.getUsername());
			Object o5 = passwordMethod.invoke(o4, dataSourceProperty.getPassword());
			Object o6 = driverClassNameMethod.invoke(o5, dataSourceProperty.getDriverClassName());
			return (DataSource) buildMethod.invoke(o6);
		} catch (Exception e) {
			throw new RuntimeException("create basice dataSource failed");
		}
	}

	/**
	 * create JNDI dataSource
	 * 
	 * @param jndiName
	 * @return
	 */
	public DataSource createJNDIDataSource(String jndiName) {
		return JNDI_DATA_SOURCE_LOOKUP.getDataSource(jndiName);
	}

	/**
	 * create hikariDataSource
	 */
	public DataSource createHikariDataSource(DataSourceProperty dataSourceProperty) {
		HikariCpConfig hikariCpConfig = dataSourceProperty.getHikari();
		HikariConfig config = hikariCpConfig.toHikariConfig(hikariCpConfig);
		log.info("hikaricp config is {}", hikariCpConfig);
		config.setUsername(dataSourceProperty.getUsername());
		config.setPassword(dataSourceProperty.getPassword());
		config.setJdbcUrl(dataSourceProperty.getUrl());
		config.setDriverClassName(dataSourceProperty.getDriverClassName());
		config.setPoolName(dataSourceProperty.getPoolName());
		log.info("Create dataSource with config[{},{},{},{}]", config.getJdbcUrl(), config.getUsername(),
				config.getPassword(), config.getDriverClassName());
		DataSource source = new HikariDataSource(config);
		// log.info("After create the dataSource info is [{},{},{},{}]",);
		return source;
	}
}
