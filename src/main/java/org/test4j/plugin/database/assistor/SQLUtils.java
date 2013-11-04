package org.test4j.plugin.database.assistor;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.BaseTreeNode;
import org.test4j.plugin.database.ui.tree.ColumnNode;
import org.test4j.plugin.database.ui.tree.DatabaseNode;
import org.test4j.plugin.database.ui.tree.TableNode;

/**
 * 
 * @author darui.wu
 * 
 */
public class SQLUtils {
	/**
	 * This method return the Class of a specific SQL Type, defined in
	 * java.sql.Types.
	 * 
	 * @param type
	 *            SQL Type.
	 * @return The Class relative to the SQL Type.
	 */
	public static Class<?> getColumnClass(int type) {
		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			return String.class;

		case Types.BIT:
		case Types.BOOLEAN:
			return boolean.class;

		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
			return Integer.class;

		case Types.BIGINT:
			return Long.class;

		case Types.FLOAT:
		case Types.DOUBLE:
		case Types.DECIMAL:
		case Types.NUMERIC:
		case Types.REAL:
			return Double.class;

		case Types.DATE:
			return java.util.Date.class;

		case Types.TIMESTAMP:
		case Types.TIME:
			return java.sql.Timestamp.class;

		default:
			return Object.class;

			/*
			 * types not recognized yet case Types.ARRAY : case Types.BINARY :
			 * case Types.BLOB : case Types.CLOB : case Types.DATALINK : case
			 * Types.DISTINCT : case Types.JAVA_OBJECT : case
			 * Types.LONGVARBINARY : case Types.NULL : case Types.OTHER : case
			 * Types.REF : case Types.STRUCT : case Types.VARBINARY :
			 */
		}
	}

	public static TableNode getTableModelByName(String tableName) {
		tableName = tableName.trim();
		DatabaseNode connModel = DatabaseView.getActiveDatabase();

		List<BaseTreeNode> children = connModel.getChildrenList();
		for (BaseTreeNode model : children) {
			if (model.getName().equalsIgnoreCase(tableName)) {
				return (TableNode) model;
			}
		}
		return null;
	}

	public static ColumnNode getColumnModelByName(TableNode table, String columnName) {
		columnName = columnName.trim();
		List<BaseTreeNode> children = table.getChildrenList();
		for (BaseTreeNode model : children) {
			if (model.getName().equalsIgnoreCase(columnName)) {
				return (ColumnNode) model;
			}
		}
		return null;
	}

	public static String findTableName(String query) {
		char[] array = query.substring(query.toLowerCase().indexOf("from") + 4).trim().toCharArray();
		String tableName = "";
		for (char ch : array) {
			if (ch == ' ' || ch == ',' || ch == '\t') {
				break;
			} else {
				tableName += ch;
			}
		}

		return tableName;
	}

	/**
	 * 返回查询结果集的字段名称
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<String> getColumnNames(ResultSet rs) throws SQLException {
		ResultSetMetaData meta = rs.getMetaData();

		List<String> columnNames = new ArrayList<String>();
		int count = meta.getColumnCount();
		for (int index = 1; index <= count; index++) {
			String name = meta.getColumnName(index);
			columnNames.add(name);
		}
		return columnNames;
	}

	/**
	 * 返回查询结果数据
	 * 
	 * @param columns
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, String>> getQueryDataSet(List<String> columns, ResultSet rs, int limitedCount)
			throws SQLException {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();

		int count = 0;
		while (rs.next()) {
			count++;
			if (count > limitedCount) {
				break;
			}
			Map<String, String> data = new HashMap<String, String>();
			for (String column : columns) {
				String value = rs.getString(column);
				data.put(column, value);
			}
			datas.add(data);
		}
		rs.close();

		return datas;
	}
}