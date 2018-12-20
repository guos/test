package mtl.wabi.db.proxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import mtl.wabi.db.proxy.pojo.DBRequest;
import mtl.wabi.db.proxy.pojo.DBResponse;

@Service
public class DBProxyServer {

	DynamicDataSourceCreator dataSourceCreator;
	DynamicRoutingDataSource routingDataSource;
	@Autowired
	JdbcTemplate template;
	
	public DBResponse exec(DBRequest request) {
		String ds="";
		routingDataSource.getDataSource(ds);
		
		DynamicDataSourceContextHolder.setDataSourceKey(ds);
		
		
	}
	
	
}
