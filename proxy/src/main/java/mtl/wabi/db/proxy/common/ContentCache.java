package mtl.wabi.db.proxy.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import mtl.wabi.db.proxy.pojo.DBResponse;

public class ContentCache {
    private static Map<Long, DBResponse> resultMap = new ConcurrentHashMap<>();
    
	public static Map<Long, DBResponse> getResultMap() {
		return resultMap;
	}
}
