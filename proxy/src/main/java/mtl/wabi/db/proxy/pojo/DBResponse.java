package mtl.wabi.db.proxy.pojo;

import lombok.Data;

@Data
public class DBResponse {
	/**
	 * the request id
	 */
	private String id;
	/**
	 * the status of the execute.
	 * 
	 */
	private String status;
	/**
	 * the csv path which can be download by wget.
	 */
	private String csvURL;
	/**
	 * time in mills second to tell how long the sql executed.
	 */
	private String execTime;

}
