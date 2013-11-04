package org.test4j.plugin.resources.prop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class SystemPropertyFile extends AbstractPropertyFile {
	protected SystemPropertyFile() {
	}

	public SystemPropertyFile(String fileUri) {
		if (fileUri == null) {
			return;
		}
		InputStream inputFile = null;
		try {
			inputFile = new FileInputStream(fileUri);
		} catch (FileNotFoundException localFileNotFoundException) {
		}
		setPropertyFile(inputFile);
	}

	public SystemPropertyFile(InputStream inputFile) {
		setPropertyFile(inputFile);
	}

	public void setPropertyFile(InputStream inputFile) {
		if (inputFile != null)
			try {
				Properties file = new Properties();
				file.load(inputFile);
				super.setPropertyFile(file);
			} catch (IOException localIOException) {
			}
	}
}