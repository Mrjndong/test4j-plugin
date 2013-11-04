package org.jtester.database.meta;

import java.util.ArrayList;
import java.util.List;

public enum DatabaseType {
	MYSQL {
		@Override
		public String limitQuery(String select, String orderBy) {
			return String.format("%s %s limit 0,200 ", select, orderBy);
		}
	},
	ORACLE {
		@Override
		public String limitQuery(String select, String orderBy) {
			return String.format("%s where rownum <= 200 %s", select, orderBy);
		}
	};

	/**
	 * 给select * from table加上limit限制
	 * 
	 * @param query
	 * @return
	 */
	public abstract String limitQuery(String select, String orderBy);

	public static String[] types() {
		List<String> types = new ArrayList<String>();
		for (DatabaseType type : DatabaseType.values()) {
			types.add(type.name());
		}
		return types.toArray(new String[0]);
	}

	public static DatabaseType dbType(String value) {
		if (value == null) {
			return null;
		}
		return DatabaseType.valueOf(value.trim().toUpperCase());
	}
}
