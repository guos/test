
package mtl.wabi.db.proxy.pojo;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class DataSourceProperty {

	/**
	 * pool name ,default is get from application.properties
	 */
	private String poolName;
	/**
	 * pool type ,most time not use, will be used for future
	 */
	private Class<? extends DataSource> type;
	/**
	 * JDBC driver
	 */
	private String driverClassName;
	/**
	 * JDBC url 地址
	 */
	private String url;
	/**
	 * JDBC 用户名
	 */
	private String username;
	/**
	 * JDBC 密码
	 */
	private String password;
	/**
	 * jndi数据源名称(设置即表示启用)
	 */
	private String jndiName;

	/**
	 * HikariCp参数配置
	 */

	private HikariCpConfig hikari;
	public String toString() {

		return "driverClassName:" + driverClassName + " url: " + url + " username:" + username + " pwd:****" + " properties: " + hikari.getConnectionTimeout();
	}

}
