package mtl.wabi.db.proxy.pojo;

import lombok.Data;
import mtl.wabi.db.proxy.common.DBConstants;

@Data
public class DBResponse {
	/**
	 * the request id
	 */
	private Long id;
	/**
	 * the status of the execute.
	 * 
	 */
	private String status=DBConstants.PROC;
	/**
	 * the csv path which can be download by wget.
	 */
	private String csvURL;
	/**
	 * time in mills second to tell how long the sql executed.
	 */
	private long execTime;

}
