package mtl.wabi.db.proxy.pojo;

import lombok.Data;

@Data
public class DBRequest {
	String id;
	int timeout;
	Source source;
	String sql;
	

}
