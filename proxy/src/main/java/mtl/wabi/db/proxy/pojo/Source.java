package mtl.wabi.db.proxy.pojo;

public class Source {
	/**
	 * the name of the dataSource
	 */
	private String name;

	private String username;
	/**
	 * password is encrypted
	 */
	private String password;
	/**
	 * tell which type of DB
	 */
	private String type;
	private String jdbcUrl;
	private String ip;
	private int port;
	private String db;
	private String driverClass;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		
		//need decrypte 
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getJdbcUrl() {
		return jdbcUrl;
	}

	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	/**
	 * the driver class should be auto detected from the user input, but we also
	 * support user input the driverClasss directly to support some database we
	 * never know,but most time this fields is null
	 * 
	 * @return
	 */
	public String getDriverClass() {
       
		if(driverClass==null) {
			//if user not input the driverClass, try to get it from type.
			
			
			
		}
		
		
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	

	
	
	

}
