package org.test4j.plugin.resources.prop;

import java.io.InputStream;


public class ClassPathPropertiesFile extends SystemPropertyFile {
	public ClassPathPropertiesFile(String pAddress) {
		if ((pAddress == null) || (pAddress.trim().length() == 0)) {
			return;
		}
		InputStream inputFile = getClass().getClassLoader().getResourceAsStream(pAddress);
		if (inputFile != null)
			super.setPropertyFile(inputFile);
	}
}