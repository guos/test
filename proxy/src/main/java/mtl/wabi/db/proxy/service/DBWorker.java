package mtl.wabi.db.proxy.service;

import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.common.ContentCache;
import mtl.wabi.db.proxy.pojo.DBRequest;
import mtl.wabi.db.proxy.pojo.DBResponse;
@Slf4j
public class DBWorker implements Runnable {
	DBProxyService service;
	DBRequest request;

	public DBWorker(DBProxyService service, DBRequest request) {
		this.service = service;
		this.request = request;
	}

	@Override
	public void run() {
		log.info("start execute the query");
		long start=System.currentTimeMillis();
		DBResponse response = service.exec(request);
		long end=System.currentTimeMillis();
		response.setExecTime(end-start);
		log.info("finished execute,result is {}",response);
		ContentCache.getResultMap().put(response.getId(), response);

	}

}
