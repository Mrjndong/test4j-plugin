package org.jtester.plugin.resources.prop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.jtester.plugin.resources.PropertiesFile;

public abstract class AbstractPropertyFile implements PropertiesFile {
	private Properties innerPropertyFile = null;

	public AbstractPropertyFile() {
		this.innerPropertyFile = null;
	}

	protected Properties getPropertyFile() {
		return this.innerPropertyFile;
	}

	protected void setPropertyFile(Properties file) {
		this.innerPropertyFile = file;
	}

	@SuppressWarnings("rawtypes")
	public Map<String, String> getAllProperties() {
		if (this.innerPropertyFile == null) {
			return null;
		}
		Map<String, String> result = new HashMap<String, String>();
		if (this.innerPropertyFile.entrySet().size() != 0) {
			for (Iterator ite = this.innerPropertyFile.entrySet().iterator(); ite.hasNext();) {
				Map.Entry currentProperty = (Map.Entry) ite.next();

				result.put((String) currentProperty.getKey(), (String) currentProperty.getValue());
			}
		}
		return result;
	}

	public String getProperty(String pKey) {
		if (this.innerPropertyFile == null) {
			return null;
		}
		if (this.innerPropertyFile.containsKey(pKey)) {
			return this.innerPropertyFile.getProperty(pKey).trim();
		}
		return null;
	}
}