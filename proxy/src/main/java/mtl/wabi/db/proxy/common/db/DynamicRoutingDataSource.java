
package mtl.wabi.db.proxy.common.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.common.DBCache;
import mtl.wabi.db.proxy.common.DBConstants;
import mtl.wabi.db.proxy.pojo.DataSourceProperty;

@Slf4j
@Component
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
	@Autowired
	DynamicDataSourceCreator creator;
	@Autowired
	DataSourceProperty property;

	@Override
	protected DataSource determineDataSource() {
		log.info("Now look at the current Routing data source");
		String key = DynamicDataSourceContextHolder.getDataSourceLookupKey();
		log.info("Got the key [{}]", key);
		DataSource source = null;
		if (key == null) {
			// means first time,we use h2 db
			source = initDataSource();
			key = DBConstants.DEFAULT_DB;
		} else {
			source = DBCache.getDataSource(key);
		}

		log.info("found data source [{}],with key[{}]", source, key);
		return source;
	}

	private DataSource initDataSource() {

		log.info("init h2");
		DataSource dataSource = creator.createDataSource(property);
		DBCache.addDataSource(DBConstants.DEFAULT_DB, dataSource);
		return dataSource;

	}

}