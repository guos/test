package mtl.wabi.db.proxy.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.common.DBCache;
import mtl.wabi.db.proxy.common.DBConstants;
import mtl.wabi.db.proxy.common.DBUtils;
import mtl.wabi.db.proxy.common.db.DynamicDataSourceContextHolder;
import mtl.wabi.db.proxy.common.db.DynamicDataSourceCreator;
import mtl.wabi.db.proxy.pojo.DBRequest;
import mtl.wabi.db.proxy.pojo.DBResponse;
import mtl.wabi.db.proxy.pojo.DataSourceProperty;
import mtl.wabi.db.proxy.pojo.Source;

@Service
@Slf4j
public class DBProxyService {
	@Autowired
	DynamicDataSourceCreator dataSourceCreator;	
	@Autowired
	DataSourceProperty dataSourceProperty;
	@Autowired
	JdbcTemplate template;

	public DBResponse exec(DBRequest request) {
		String ds = request.getSource().getName();
		DBResponse response = new DBResponse();
		response.setId(request.getId());
		response.setStatus(DBConstants.FAIL);
		DataSource dataSource = DBCache.getDataSource(ds);
		if (dataSource == null) {
			// create one and put it in the map
			//DataSourceProperty dataSourceProperty = new DataSourceProperty();
			Source dataPro = request.getSource();
			dataSourceProperty.setDriverClassName(dataPro.getDriverClass());
			dataSourceProperty.setUrl(dataPro.getJdbcUrl());
			dataSourceProperty.setUsername(dataPro.getUsername());
			dataSourceProperty.setPassword(dataPro.getPassword());
			dataSourceProperty.setPoolName(ds);
			dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
			DBCache.addDataSource(ds, dataSource);
		}

		DynamicDataSourceContextHolder.setDataSourceLookupKey(ds);
		List<Map<String, Object>> result = template.queryForList(request.getSql());
		String repString = null;
		try {
			log.info("write result[{}] rows to csv",result.size());
			repString = DBUtils.writeDataToCSV(result, request.getId());
		} catch (IOException e) {
			log.error("write data to csv for {} have problem {} ", request.getId(), e.getMessage());
		}
		if (repString != null) {
			response.setStatus(DBConstants.SUCCESS);
			response.setCsvURL(repString);
		}
		DynamicDataSourceContextHolder.clearDataSourceLookupKey();
		return response;

	}

}
