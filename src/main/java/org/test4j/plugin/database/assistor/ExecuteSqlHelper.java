package org.test4j.plugin.database.assistor;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.test4j.plugin.database.ui.DataSetView;
import org.test4j.plugin.database.ui.MessageView;
import org.test4j.plugin.helper.PluginHelper;

public final class ExecuteSqlHelper {
	public static String NEWLINE = System.getProperty("line.separator");

	/**
	 * 允许查出的最大记录数
	 */
	public static final int LIMIT_COUNT = 2000;

	/**
	 * 从文件执行sql
	 * 
	 * @param conn
	 * @param file
	 * @return
	 */
	public static int executeFromFile(final Connection conn, File file) {
		if (file == null || file.exists() == false) {
			return 0;
		}

		try {
			// 从文件中读出sql语句执行,TODO
			int count = executeQuery(conn, "todo ");// TODO
			return count;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 执行sql语句
	 * 
	 * @param conn
	 * @param sql
	 * @param newTab
	 * @return
	 * @throws SQLException
	 */
	public static int executeQuery(Connection conn, String sql) throws SQLException {
		int updateRow = 0;

		String[] statements = parseSQL(sql);
		if (statements == null || statements.length == 0) {
			return -1;
		}
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			for (String statement : statements) {
				updateRow += executeQuery(conn, stmt, statement);
			}
		} catch (SQLException e) {
			MessageView.addMessage(e.getMessage());
			throw e;
		} finally {
			close(stmt);
		}
		return updateRow;
	}

	/**
	 * 执行单条sql语句
	 * 
	 * @param conn
	 * @param stmt
	 * @param statement
	 * @return
	 * @throws SQLException
	 */
	private static int executeQuery(Connection conn, Statement stmt, String statement) throws SQLException {
		if (statement != null && statement.trim().startsWith("@")) {
			File file = new File(statement.trim().substring(1));
			return executeFromFile(conn, file);
		}

		boolean flag = stmt.execute(statement);
		if (flag) {
			ResultSet rs = stmt.getResultSet();
			PluginHelper.showView(DataSetView.ID);
			List<String> columnNames = SQLUtils.getColumnNames(rs);
			List<Map<String, String>> datas = SQLUtils.getQueryDataSet(columnNames, rs, 100);
			DataSetView.getInstance().reFillDataSet(statement, columnNames, datas);

			return 0;
		} else {
			int updateCount = stmt.getUpdateCount();
			MessageView.addMessage(updateCount + " records effected: " + statement);
			return updateCount;
		}
	}

	/**
	 * 分解sql语句为单条可执行的sql语句集合,并过滤注释
	 * 
	 * @param sql
	 * @return
	 */
	public static String[] parseSQL(String sql) {
		char[] chars = sql.toCharArray();

		char stringChar = 'x';
		boolean inComment = false;

		StringBuffer sb = new StringBuffer();
		List<String> statements = new ArrayList<String>();

		for (int i = 0; i < chars.length; i++) {
			if (inComment == false) {
				// if in comment and escaping ' or ", via \' \", skip
				if (stringChar != 'x' && chars[i] == '\\') {
					sb.append(chars[i]);
					int nextChar = i + 1;
					if (chars[nextChar] == '\'' || chars[nextChar] == '"') {
						sb.append(chars[i]);
						continue;
					}
				}

				if (chars[i] == '\'' || chars[i] == '"') {
					if (chars[i] == stringChar) {
						stringChar = 'x';
						sb.append(chars[i]);
					} else {
						stringChar = chars[i];
						sb.append(chars[i]);
					}
					continue;
				}

				if (chars[i] == '-' && chars[i + 1] == '-') {
					inComment = true;
					i++;
					continue;
				}
			}

			// if in comment and end of line, get out of comment
			if (inComment == true && chars[i] == Character.LINE_SEPARATOR) {
				inComment = false;
				continue;
			}

			if (inComment == true || chars[i] == '\r' || chars[i] == '\n') {
				if (chars[i] == '\n') {
					sb.append(" ");
				}
				continue;
			}

			// if end of statement
			if (chars[i] == ';') {
				statements.add(sb.toString());
				sb = new StringBuffer();
				continue;
			}

			if (inComment == false) {
				sb.append(chars[i]);
			}
		}

		if (statements.size() == 0) {
			statements.add(sb.toString());
		}

		String[] stmts = new String[statements.size()];
		statements.toArray(stmts);
		return stmts;
	}

	public final static void close(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			MessageView.addMessage(e.getMessage());
		} finally {
			conn = null;
		}
	}

	public final static void close(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException e) {
			MessageView.addMessage(e.getMessage());
		} finally {
			rs = null;
		}
	}

	public final static void close(Statement stmt) {
		if (stmt == null) {
			return;
		}
		try {
			stmt.close();
		} catch (SQLException e) {
			MessageView.addMessage(e.getMessage());
		} finally {
			stmt = null;
		}
	}
}
