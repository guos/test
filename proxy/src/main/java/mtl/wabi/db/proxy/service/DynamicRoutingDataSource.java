
package mtl.wabi.db.proxy.service;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {


	/**
	 * 所有数据库
	 */
	private Map<String, DataSource> dataSourceMap = new LinkedHashMap<>();

	@Override
	public DataSource determineCurrentLookupKey() {
		return getDataSource(DynamicDataSourceContextHolder.getDataSourceLookupKey());
	}

	/**
	 * 获取当前所有的数据源
	 *
	 * @return 当前所有数据源
	 */
	public Map<String, DataSource> getCurrentDataSources() {
		return dataSourceMap;
	}

	/**
	 * 获取数据源
	 *
	 * @param ds 数据源名称
	 * @return 数据源
	 */
	public DataSource getDataSource(String ds) {

		return dataSourceMap.get(ds);

	}

	/**
	 * 添加数据源
	 *
	 * @param ds         数据源名称
	 * @param dataSource 数据源
	 */
	public synchronized void addDataSource(String ds, DataSource dataSource) {
		dataSourceMap.put(ds, dataSource);
		log.info("动态数据源-加载 {} 成功", ds);
	}

	/**
	 * 删除数据源
	 *
	 * @param ds 数据源名称
	 */
	public synchronized void removeDataSource(String ds) {
		if (dataSourceMap.containsKey(ds)) {
			dataSourceMap.remove(ds);
			log.info("动态数据源-删除 {} 成功", ds);
		} else {
			log.warn("动态数据源-未找到 {} 数据源");
		}
	}

}