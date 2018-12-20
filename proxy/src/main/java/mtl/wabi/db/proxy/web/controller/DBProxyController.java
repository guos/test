package mtl.wabi.db.proxy.web.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.coyote.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mtl.wabi.db.proxy.common.DBCache;
import mtl.wabi.db.proxy.pojo.DBRequest;
import mtl.wabi.db.proxy.pojo.DBResponse;

@RestController
@RequestMapping("db")
public class DBProxyController {

	@PostMapping("exec")
	public DBResponse execute(@RequestBody DBRequest request) {
		DBResponse response = new DBResponse();

		return response;

	}

	@GetMapping("result/{id}")
	public DBResponse getResult(@PathVariable String id) {

		DBResponse result = DBCache.getResultMap().get(id);
		if (result == null) {
			result = new DBResponse();
			result.setId(id);
			result.setStatus("");
		}

		return result;

	}

}
