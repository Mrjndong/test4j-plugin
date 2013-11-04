package org.jtester.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	/**
	 * 加载资源文件
	 * 
	 * @param filename
	 * @return
	 */
	public static Properties loadProperties(String filename) {
		File file = new File(filename);
		if (file.exists() == false) {
			throw new RuntimeException("can't find file[" + filename + "]");
		}
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(file));
			return p;
		} catch (IOException e) {
			String error = "read properties from file[" + filename + "] error.";
			throw new RuntimeException(error, e);
		}
	}
}
