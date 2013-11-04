package org.jtester.database.meta;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class TableMeta {
	private DatabaseMeta databaseMeta;

	private String tableName;

	public TableMeta(String tableName, DatabaseMeta databaseMeta) {
		this.tableName = tableName;
		this.databaseMeta = databaseMeta;
	}

	/**
	 * 返回数据库指定表的字段列表
	 * 
	 * @param databaseMeta
	 * @return
	 * @throws Exception
	 */
	public List<ColumnMeta> getColumns() throws Exception {
		Connection conn = databaseMeta.getConnection();

		DatabaseMetaData dbmd = conn.getMetaData();

		// Verify if the database has catalogs
		String catalog = databaseMeta.getCatalog();
		String schema = databaseMeta.getSchema();
		ResultSet resultSet = dbmd.getColumns(catalog, schema, this.tableName, null);

		List<ColumnMeta> columnMetas = new ArrayList<ColumnMeta>();
		while (resultSet.next()) {
			ColumnMeta columnMeta = new ColumnMeta(this, resultSet);

			columnMetas.add(columnMeta);
		}
		resultSet.close();

		this.setPrimaryKeys(columnMetas, dbmd, catalog, schema);
		this.setForeignKeys(columnMetas, dbmd, catalog, schema);
		return columnMetas;
	}

	/**
	 * 设置主键
	 * 
	 * @param columns
	 * @param dbmd
	 * @param catalog
	 * @param schema
	 * @throws SQLException
	 */
	private void setPrimaryKeys(List<ColumnMeta> columns, DatabaseMetaData dbmd, String catalog, String schema)
			throws SQLException {
		// -- PRIMARY KEYS --
		ResultSet rsPk = dbmd.getPrimaryKeys(catalog, schema, this.tableName);
		while (rsPk.next()) {
			String pkName = (String) rsPk.getString("COLUMN_NAME");
			for (ColumnMeta column : columns) {
				if (column.getColumnName().equalsIgnoreCase(pkName)) {
					column.setPrimaryKey(true);
				}
			}
		}
		rsPk.close();
	}

	private void setForeignKeys(List<ColumnMeta> columns, DatabaseMetaData dbmd, String catalog, String schema)
			throws SQLException {
		// -- Foreign KEYS --
		ResultSet rsFK = dbmd.getImportedKeys(catalog, schema, this.tableName);
		while (rsFK.next()) {
			String pkTable = (String) rsFK.getString("PKTABLE_NAME");
			String pkColumn = (String) rsFK.getString("PKCOLUMN_NAME");
			String fkColumn = (String) rsFK.getString("FKCOLUMN_NAME");

			for (ColumnMeta column : columns) {
				if (column.getColumnName().equalsIgnoreCase(fkColumn)) {
					column.setForeignKey(true);
					column.setFkTableMeta(new FKTableMeta(pkTable, pkColumn));
				}
			}
		}
		rsFK.close();
	}
}
