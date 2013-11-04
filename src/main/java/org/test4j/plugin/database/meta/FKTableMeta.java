package org.test4j.plugin.database.meta;

/**
 * 外键关联的表信息
 * 
 * @author darui.wudr
 * 
 */
public class FKTableMeta {
	private String tableName;

	private String columnPkName;

	public FKTableMeta(String tableName, String columnPkName) {
		this.tableName = tableName;
		this.columnPkName = columnPkName;
	}

	public String getTableName() {
		return tableName;
	}

	public String getColumnPkName() {
		return columnPkName;
	}
}
