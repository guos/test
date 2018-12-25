package mtl.wabi.db.proxy;

import java.io.File;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;
import mtl.wabi.db.proxy.common.DBCache;
import mtl.wabi.db.proxy.common.DBConstants;
import mtl.wabi.db.proxy.common.db.DynamicDataSourceCreator;
import mtl.wabi.db.proxy.pojo.DataSourceProperty;

@SpringBootApplication
@Slf4j
public class ProxyApplication {
	@Autowired
	DataSourceProperty property;
	@Autowired
	DynamicDataSourceCreator creator;

	public static void main(String[] args) {
		initFilePath();
		SpringApplication.run(ProxyApplication.class, args);
	}

	private static void initFilePath() {
		File file = new File(DBConstants.FILE_PATH);
		if (!file.exists()) {
			log.info("the file path [{}] not exist, so create it", file.getAbsolutePath());
			file.mkdirs();
		}
	}

	@Bean
	public void dy() {

		log.info("init h2");
		DataSource dataSource = creator.createDataSource(property);
		DBCache.addDataSource("h2", dataSource);
		

	}

}
