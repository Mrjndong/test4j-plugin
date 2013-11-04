package org.test4j.plugin.database.meta;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.test4j.plugin.database.assistor.SQLUtils;

/**
 * 数据库列元数据
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public class ColumnMeta {
	private final TableMeta tableMeta;

	public ColumnMeta(TableMeta tableMeta) {
		this.tableMeta = tableMeta;
	}

	/**
	 * 列名称
	 */
	private String columnName;
	/**
	 * 列类型
	 */
	private int sqlType;
	/**
	 * 类型名称
	 */
	private String typeName;
	/**
	 * 对应的java类型
	 */
	private Class javaType;
	/**
	 * 列长度
	 */
	private int columnSize;
	/**
	 * 精度
	 */
	private int decimalDigits;
	/**
	 * 是否允许为空
	 */
	private boolean isNullable;
	/**
	 * 是否主键
	 */
	private boolean primaryKey = false;
	/**
	 * 是否外键
	 */
	private boolean foreignKey = false;

	private FKTableMeta fkTableMeta = null;

	public ColumnMeta(TableMeta tableMeta, ResultSet column) {
		this(tableMeta);
		try {
			this.setColumnName(column.getString("COLUMN_NAME"));
			this.setSqlType(column.getInt("DATA_TYPE"));
			this.setTypeName(column.getString("TYPE_NAME"));
			this.setJavaType(column.getType());
			this.setColumnSize(column.getInt("COLUMN_SIZE"));
			this.setDecimalDigits(column.getInt("DECIMAL_DIGITS"));
			this.setNullable(column.getString("IS_NULLABLE"));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Class getJavaType() {
		return javaType;
	}

	public void setJavaType(Class javaType) {
		this.javaType = javaType;
	}

	public void setJavaType(int javaType) {
		this.javaType = SQLUtils.getColumnClass(javaType);
	}

	public int getColumnSize() {
		return columnSize;
	}

	public void setColumnSize(int columnSize) {
		this.columnSize = columnSize;
	}

	public int getDecimalDigits() {
		return decimalDigits;
	}

	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}

	public boolean isNullable() {
		return isNullable;
	}

	public void setNullable(boolean isNullable) {
		this.isNullable = isNullable;
	}

	public void setNullable(String isNullable) {
		this.isNullable = "NO".equalsIgnoreCase(isNullable) == false;
	}

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isForeignKey() {
		return foreignKey;
	}

	public void setForeignKey(boolean foreignKey) {
		this.foreignKey = foreignKey;
	}

	public FKTableMeta getFkTableMeta() {
		return fkTableMeta;
	}

	public void setFkTableMeta(FKTableMeta fkTableMeta) {
		this.fkTableMeta = fkTableMeta;
	}

	public TableMeta getTableMeta() {
		return tableMeta;
	}

	public boolean isStringClass() {
		return this.getJavaType() == String.class;
	}

	// public String getText() {
	// int decimalDigits = this.getDecimalDigits();
	// String size = decimalDigits > 0 ? "(" + this.columnSize + "," +
	// decimalDigits + ")" : String
	// .valueOf(this.columnSize);
	//
	// String fk = this.getFkTable() != null ? "(" + this.getFkTable() + ")" :
	// "";
	//
	// String text = this.getName() + fk + " (" + this.getTypeName() + ", " +
	// size
	// + ((!this.isNullable()) ? ",NOT NULL" : "") + ")";
	// return text;
	// }
}
