package mtl.wabi.db.proxy.web.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mtl.wabi.db.proxy.common.ContentCache;
import mtl.wabi.db.proxy.common.DBUtils;
import mtl.wabi.db.proxy.pojo.DBRequest;
import mtl.wabi.db.proxy.pojo.DBResponse;
import mtl.wabi.db.proxy.service.DBProxyService;
import mtl.wabi.db.proxy.service.DBWorker;

@RestController
@RequestMapping("db")
public class DBProxyController {
	@Autowired
	DBProxyService service;
	
	private static Integer  poolSize=20;


	ExecutorService executorService = Executors.newFixedThreadPool(poolSize);

	@PostMapping("exec")
	public DBResponse execute(@RequestBody DBRequest request) {
		request.setId(DBUtils.getNum());
		DBResponse response = new DBResponse();
		response.setId(request.getId());
		executorService.submit(new DBWorker(service, request));
		ContentCache.getResultMap().put(request.getId(), response);
		return response;
	}

	@GetMapping("result/{id}")
	public DBResponse getResult(@PathVariable Long id) {

		DBResponse result = ContentCache.getResultMap().get(id);
		if (result == null) {
			result = new DBResponse();
			result.setId(id);
			result.setStatus("No success id["+id+"]'s result");
		}
		return result;
	}

}
