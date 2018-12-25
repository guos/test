package mtl.wabi.db.proxy.pojo;

import org.springframework.util.DigestUtils;

import mtl.wabi.db.proxy.common.DBUtils;

public class Source {

	/**
	 * tell which type of DB, DB2,MySQL
	 */
	private String type;

	private String username;
	/**
	 * password is encrypted
	 */
	private String password;

	/**
	 * the ip of the db
	 */
	private String ip;
	/**
	 * the port of the db
	 */
	private int port;

	/**
	 * which database,
	 */
	private String db;

	// --------------------------follow are not must input,app will auto get it

	/**
	 * the name of the dataSource
	 */
	private String name;
	private String jdbcUrl;
	private String driverClass;

	// ---------------------------------------------------------------------------
	public String getName() {
		if (name == null) {
			String key=username + db + port + ip + type.toUpperCase();
			name =DigestUtils.md5DigestAsHex(key.getBytes());
		}
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

		// need decrypte
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
		
		if(jdbcUrl==null) {
			//use the type to judge the url
			//eg, oracal, db2 use which type of url, use String.format 
			
			
			
		}
		
		
		
		
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

		if (driverClass == null) {
			// if user not input the driverClass, try to get it from type.
			driverClass = DBUtils.getDriverFromType(type);
		}

		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

}
