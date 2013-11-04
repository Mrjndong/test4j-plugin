package org.jtester.database.assistor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 将wiki内容转换为DataMap
 * 
 * @author darui.wudr
 * 
 */
public class WikiToDataMap {
	public static String covert(String wiki) {
		String[] lines = wiki.split("\n");
		StringBuffer map = new StringBuffer();

		for (int count = 0; count < lines.length;) {
			String line = toLowerCase(lines[count]);
			try {
				if (line.startsWith("|query|")) {
					count = getQueryMap(lines, count, map);
				} else if (line.startsWith("|insert|")) {
					count = getInsertMap(lines, count, map);
				} else {
					count++;
				}
			} catch (Exception e) {
				throw new RuntimeException("illegal wiki context.", e);
			}
		}

		return map.toString();
	}

	/**
	 * 将字母转为小写，且过滤所有空格
	 * 
	 * @param line
	 * @return
	 */
	private static String toLowerCase(String line) {
		String value = line.replaceAll("\\s+", "");
		return value.toLowerCase();
	}

	private static int getQueryMap(String[] lines, int start, StringBuffer map) {
		String query = lines[start];
		query = query.split("\\|")[2].trim();

		QueryMeta meta = QueryMeta.getQueryMeta(query);

		Map<Integer, String> datas = new LinkedHashMap<Integer, String>();
		int index = start + 1;
		for (; index < lines.length; index++) {
			String line = lines[index];
			if ("".endsWith(line.trim())) {
				break;
			} else {
				putItemsToMap(line, datas);
			}
		}
		map.append("db.table(\"").append(meta.tableName).append("\")");
		if ("".equals(meta.where)) {
			map.append(".query()");
		} else {
			map.append(".queryWhere(\"").append(meta.where).append("\")");
		}
		map.append(".reflectionEqMap(").append(index - start - 2).append(" ,");
		setStringFromDatas(datas, map);
		map.append(");\n");
		return index;
	}

	private static int getInsertMap(String[] lines, int start, StringBuffer map) {
		String insert = lines[start];
		String table = insert.split("\\|")[2].trim();

		Map<Integer, String> datas = new LinkedHashMap<Integer, String>();
		int index = start + 1;
		for (; index < lines.length; index++) {
			String line = lines[index];
			if ("".endsWith(line.trim())) {
				break;
			} else {
				putItemsToMap(line, datas);
			}
		}
		map.append("db.table(\"").append(table).append("\").clean().insert(").append(index - start - 2).append(" ,");
		setStringFromDatas(datas, map);
		map.append(");\n");
		return index;
	}

	private static void setStringFromDatas(Map<Integer, String> map, StringBuffer buff) {
		buff.append("new DataMap(){\n");
		buff.append("\t{\n");
		for (String value : map.values()) {
			buff.append("\t\tthis.put(").append(value).append(");\n");
		}
		buff.append("\t}\n");
		buff.append("}");
	}

	/**
	 * 将单行wiki数据存储到map中
	 * 
	 * @param line
	 * @param map
	 */
	private static void putItemsToMap(String line, Map<Integer, String> map) {
		String[] items = line.trim().split("\\|");
		for (int pos = 1; pos < items.length; pos++) {
			String item = items[pos].trim();
			if ("<null>".equals(item)) {
				item = null;
			} else {
				item = "\"" + item + "\"";
			}
			String value = map.get(pos);
			if (value == null) {
				map.put(pos, item.toUpperCase());
			} else {
				map.put(pos, value + ", " + item);
			}
		}
	}

	static class QueryMeta {
		String tableName;
		String where;

		public static QueryMeta getQueryMeta(String select) {
			String toLowerQuery = select.toLowerCase();
			int fromPos = toLowerQuery.indexOf("from");
			int wherePos = toLowerQuery.indexOf("where");

			QueryMeta meta = new QueryMeta();
			meta.tableName = select.substring(fromPos + 4, wherePos > 0 ? wherePos : select.length()).trim();
			meta.where = wherePos > 0 ? select.substring(wherePos + 5).trim() : "";

			return meta;
		}
	}
}
