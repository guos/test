package mtl.wabi.db.proxy.common;

public class DBConstants {

	public final static String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
	public final static String ORACAL_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public final static String H2_DRIVER="org.h2.Driver";
	public final static String ORACAL_URL = "jdbc:oracle:thin:@";// jdbc:oracle:thin:@localhost:1522:orcl
	public final static String MYSQL = "MYSQL";
	public final static String MSSQL = "MSSQL";
	public final static String ORACAL = "ORACAL";
	public final static String H2 = "H2";
	public final static String DB2 = "DB2";
	public final static String END = "\r\n";
	public final static String COMMA = ",";
	public final static String SUCCESS = "Success";
	public final static String FAIL = "Fail";
	public final static String PROC="Processing";
	public final static String DEFAULT_DB="h2";

	/**
	 * the web path for user download file
	 */
	public final static String DOWNLOAD_PATH = "files/";
	/**
	 * the file store path
	 */
	public final static String FILE_PATH = "static/" + DOWNLOAD_PATH;

	/**
	 * HikariCp数据源
	 */
	public static final String HIKARI_DATASOURCE = "com.zaxxer.hikari.HikariDataSource";
}
