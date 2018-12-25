package mtl.wabi.db.proxy.common;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class DBUtils {

	
	private static final AtomicLong NUM = new AtomicLong(System.currentTimeMillis());

	public static String getDriverFromType(String type) {
		String dbTypeString = type.toUpperCase();
        String driver=DBConstants.MYSQL_DRIVER;
		switch (dbTypeString) {

		case DBConstants.MYSQL:
                 
			break;
		case DBConstants.H2:
            driver=DBConstants.H2_DRIVER;
			break;
		default:

		}

		return driver;
	}

	public static String writeDataToCSV(List<Map<String, Object>> result, Long id) throws IOException {

		Path path = Paths.get(DBConstants.FILE_PATH + id + ".csv");
		
		BufferedWriter writer = Files.newBufferedWriter(path);
		// write the header
		writeCSVHeder(writer,result);
		// write the content
		StringBuffer sBuffer = new StringBuffer();
		for (Map<String, Object> map : result) {
			// one map is one line
			for (Object obj : map.values()) {
				sBuffer.append(DBConstants.COMMA).append(obj);
			}
			//for end it need tell next is a new line
			sBuffer.append(DBConstants.END);
			// remove the first ,
			writer.append(sBuffer.substring(1));
			// clear data inside
			sBuffer.delete(0, sBuffer.length());
		}
		writer.flush();
		writer.close();
		return DBConstants.DOWNLOAD_PATH + path.getFileName();
	}

	/**
	 * write the header, the header is from the map's key
	 * 
	 * @param writer
	 * @param result
	 * @throws IOException
	 */
	public static void writeCSVHeder(BufferedWriter writer, List<Map<String, Object>> result) throws IOException {

		if (result.size() > 0) {
			StringBuffer stringBuffer = new StringBuffer();
			Map<String, Object> map = result.get(0);
			for (String key : map.keySet()) {
				stringBuffer.append(DBConstants.COMMA).append(key);
			}
			//line end
			stringBuffer.append(DBConstants.END);
			writer.append(stringBuffer.substring(1));
		}
	}

	/**
	 * get an id
	 * 
	 * @return
	 */
	public static Long getNum() {
		return NUM.getAndIncrement();
	}

}
