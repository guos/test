package mtl.wabi.db.proxy.web.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.common.DBConstants;
import mtl.wabi.db.proxy.pojo.DBRequest;
import mtl.wabi.db.proxy.pojo.DBResponse;
import mtl.wabi.db.proxy.pojo.Source;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Slf4j
public class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		DBRequest dbRequest = new DBRequest();
		Source source = new Source();
		source.setDb("h2test");
		source.setIp("localhost");
		//source.setPort(3306);
		source.setUsername("sa");		
		source.setType(DBConstants.H2);
		source.setJdbcUrl("jdbc:h2:mem:h2test");
		dbRequest.setSource(source);
		dbRequest.setSql("select * from person");
		DBResponse response = this.restTemplate.postForObject("http://localhost:" + port + "/db/exec", dbRequest,
				DBResponse.class);
		assertEquals(DBConstants.PROC, response.getStatus());
		assertTrue(response.getCsvURL()==null);
         log.info("response is {} ",response);
         Thread.sleep(3000);
		DBResponse response2=this.restTemplate.getForObject("http://localhost:" + port + "/db/result/"+response.getId(), DBResponse.class);
			log.info("result {}",response2);	

	}
}
