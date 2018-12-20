
package mtl.wabi.db.proxy.service;

import java.lang.reflect.Method;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.pojo.DataSourceProperty;
import mtl.wabi.db.proxy.pojo.HikariCpConfig;


@Slf4j
public class DynamicDataSourceCreator {


    /**
     * HikariCp数据源
     */
    private static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
    /**
     * JNDI数据源查找
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
     * 是否存在hikari
     */
    private Boolean hikariExists = false;


    @Setter
    private HikariCpConfig hikariGlobalConfig;

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
            e.printStackTrace();
        }
     
        try {
            Class.forName(HIKARI_DATASOURCE);
            hikariExists = true;
        } catch (ClassNotFoundException e) {
        }
    }

    /**
     * 创建数据源
     *
     * @param dataSourceProperty 数据源信息
     * @return 数据源
     */
    public DataSource createDataSource(DataSourceProperty dataSourceProperty) {
        //如果是jndi数据源
        String jndiName = dataSourceProperty.getJndiName();
        if (jndiName != null && !jndiName.isEmpty()) {
            return createJNDIDataSource(jndiName);
        }
        Class<? extends DataSource> type = dataSourceProperty.getType();
        if (type == null) {
          if (hikariExists) {
                return createHikariDataSource(dataSourceProperty);
            }
        } else if (HIKARI_DATASOURCE.equals(type.getName())) {
            return createHikariDataSource(dataSourceProperty);
        }
        return createBasicDataSource(dataSourceProperty);
    }

    /**
     * 创建基础数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源
     */
    public DataSource createBasicDataSource(DataSourceProperty dataSourceProperty) {
        try {
            Object o1 = createMethod.invoke(null);
            Object o2 = typeMethod.invoke(o1, dataSourceProperty.getType());
            Object o3 = urlMethod.invoke(o2, dataSourceProperty.getUrl());
            Object o4 = usernameMethod.invoke(o3, dataSourceProperty.getUsername());
            Object o5 = passwordMethod.invoke(o4, dataSourceProperty.getPassword());
            Object o6 = driverClassNameMethod.invoke(o5, dataSourceProperty.getDriverClassName());
            return (DataSource) buildMethod.invoke(o6);
        } catch (Exception e) {
            throw new RuntimeException("多数据源创建数据源失败");
        }
    }

    /**
     * 创建JNDI数据源
     *
     * @param jndiName jndi数据源名称
     * @return 数据源
     */
    public DataSource createJNDIDataSource(String jndiName) {
        return JNDI_DATA_SOURCE_LOOKUP.getDataSource(jndiName);
    }

  

    /**
     * 创建Hikari数据源
     *
     * @param dataSourceProperty 数据源参数
     * @return 数据源

     */
    public DataSource createHikariDataSource(DataSourceProperty dataSourceProperty) {
        HikariCpConfig hikariCpConfig = dataSourceProperty.getHikari();
        HikariConfig config = hikariCpConfig.toHikariConfig(hikariGlobalConfig);
        config.setUsername(dataSourceProperty.getUsername());
        config.setPassword(dataSourceProperty.getPassword());
        config.setJdbcUrl(dataSourceProperty.getUrl());
        config.setDriverClassName(dataSourceProperty.getDriverClassName());
        config.setPoolName(dataSourceProperty.getPoolName());
        return new HikariDataSource(config);
    }
}
