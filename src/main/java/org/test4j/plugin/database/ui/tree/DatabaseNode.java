package org.test4j.plugin.database.ui.tree;

import static org.test4j.plugin.resources.IconResources.CONNECTED_ICON;
import static org.test4j.plugin.resources.IconResources.DISCONNECTED_ICON;
import static org.test4j.plugin.resources.IconResources.SELECTED_CONNECTED_ICON;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.test4j.plugin.database.assistor.ExecuteSqlHelper;
import org.test4j.plugin.database.meta.DatabaseMeta;
import org.test4j.plugin.database.meta.TableMeta;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.TableNode.ViewModel;
import org.test4j.plugin.resources.IconResources;

/**
 * 数据库连接基本信息
 * 
 * @author darui.wudr
 * 
 */
public class DatabaseNode extends BaseTreeNode {
	public DatabaseNode(String name) {
		super(name);
		this.databaseMeta = new DatabaseMeta();
	}

	public DatabaseNode(String name, DatabaseMeta databaseMeta) {
		super(name);
		this.databaseMeta = databaseMeta;
	}

	public static final String[] URL_ARRAY = { "jdbc:oracle:thin:@localhost:1521:", "jdbc:mysql://localhost/" };
	public static final String[] DRIVER_ARRAY = { "com.mysql.jdbc.Driver", "oracle.jdbc.driver.OracleDriver" };

	private DatabaseMeta databaseMeta;

	private Connection conn = null;

	@Override
	public String getName() {
		return name;
	}

	/**
	 * 数据库是否处于连接状态
	 * 
	 * @return
	 */
	public boolean isConnected() {
		return this.databaseMeta.isConnected();
	}

	public DatabaseMeta getDatabaseMeta() {
		return databaseMeta;
	}

	/**
	 * 验证参数的有效性
	 * 
	 * @return
	 */
	public final boolean validate() {
		return this.databaseMeta.validate();
	}

	/**
	 * 根据参数连接数据库
	 * 
	 * @throws Exception
	 */
	public final void connection() throws Exception {
		this.conn = this.databaseMeta.getConnection();
	}

	/**
	 * 断开连接
	 */
	public void disConnection() {
		ExecuteSqlHelper.close(conn);
		conn = null;
		this.clear();
	}

	/**
	 * 列出所有的数据库实例
	 */
	public void populate() {
		if (this.conn == null) {
			return;
		}
		if (this.databaseMeta.isShowTables()) {
			List<String> tables = databaseMeta.getTableNames(new String[] { "TABLE" });

			for (String table : tables) {
				TableMeta tableMeta = new TableMeta(table, this.databaseMeta);
				TableNode tableModel = new TableNode(table, tableMeta);
				tableModel.addChild(new InvisibleNode("NONE"));

				this.addChild(tableModel);
			}
		}
		if (this.databaseMeta.isShowViews()) {
			List<String> tables = databaseMeta.getTableNames(new String[] { "VIEW" });

			for (String table : tables) {
				TableMeta tableMeta = new TableMeta(table, this.databaseMeta);
				ViewModel viewModel = new ViewModel(table, tableMeta);
				viewModel.addChild(new InvisibleNode("NONE"));

				this.addChild(viewModel);
			}
		}
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.Database;
	}

	@Override
	public String buildQuery(IStructuredSelection selection) {
		throw new RuntimeException("can't be triggered");
	}

	@Override
	public Image getImage() {
		if (this == DatabaseView.getActiveDatabase()) {
			return IconResources.getImage(SELECTED_CONNECTED_ICON);
		}

		if (this.databaseMeta.isConnected()) {
			return IconResources.getImage(CONNECTED_ICON);
		} else {
			return IconResources.getImage(DISCONNECTED_ICON);
		}
	}

	@Override
	public String getText() {
		return this.name;
	}

	/**
	 * 执行sql语句，将结果展现到ResultView视图
	 * 
	 * @param query
	 * @param newTab
	 * @return
	 * @throws SQLException
	 */
	public int executeQuery(String query, boolean newTab) throws SQLException {
		Connection conn = this.databaseMeta.getConnection();
		return ExecuteSqlHelper.executeQuery(conn, query);
	}

	/**
	 * 执行sql语句，返回结果集
	 * 
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String query) throws SQLException {
		Statement stmt = this.databaseMeta.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(query);

		return rs;
	}

	@Override
	public String getJavaName() {
		return upperFirstChar(dbNameToVarName(getName()));
	}
}
