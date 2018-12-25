package mtl.wabi.db.proxy.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class DBCache {
	
    private static Map<String, DataSource> dataSourceMap=new ConcurrentHashMap<>();
    
	/**
	 * 获取当前所有的数据源
	 *
	 * @return 当前所有数据源
	 */
	public static Map<String, DataSource> getCurrentDataSources() {
		return dataSourceMap;
	}

	/**
	 * 获取数据源
	 *
	 * @param ds 数据源名称
	 * @return 数据源
	 */
	public static DataSource getDataSource(String ds) {
        log.info("Try to get the data source [{}]",ds);
		return dataSourceMap.get(ds);

	}

	/**
	 * 添加数据源
	 *
	 * @param ds         数据源名称
	 * @param dataSource 数据源
	 */
	public static synchronized void addDataSource(String ds, DataSource dataSource) {
		dataSourceMap.put(ds, dataSource);
		log.info("动态数据源-创建 {} 成功", ds);
	}

	/**
	 * 删除数据源
	 *
	 * @param ds 数据源名称
	 */
	public static synchronized void removeDataSource(String ds) {
		if (dataSourceMap.containsKey(ds)) {
			dataSourceMap.remove(ds);
			log.info("动态数据源-删除 {} 成功", ds);
		} else {
			log.warn("动态数据源-未找到 {} 数据源");
		}
	}

}
