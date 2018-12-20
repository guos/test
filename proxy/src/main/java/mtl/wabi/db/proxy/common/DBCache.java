package mtl.wabi.db.proxy.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mtl.wabi.db.proxy.pojo.DBResponse;

public class DBCache {
	private static Map<String, DBResponse> resultMap = new ConcurrentHashMap<>();

	public static Map<String, DBResponse> getResultMap() {
		return resultMap;
	}



}
