package org.jtester.plugin.resources;

import java.util.Map;

public abstract interface PropertiesFile {
	public abstract Map<String, String> getAllProperties();

	public abstract String getProperty(String paramString);
}
