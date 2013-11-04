package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.MessageView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class DBConnectionDoingMenu extends BaseMenuItem {

	public DBConnectionDoingMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		doConnection();
	}

	/**
	 * 执行数据库连接
	 */
	public static void doConnection() {
		DatabaseNode databaseNode = DatabaseView.getSelectedDatabase();
		if (databaseNode.isConnected()) {
			return;
		}
		try {
			DatabaseView.connect(databaseNode);
			DatabaseView.setActiveDatabase(databaseNode);
			DatabaseView.expandDatabaseNode(databaseNode);
		} catch (ClassNotFoundException e) {
			String error = "Error loading driver, please check driver jar and driver class." + e.getMessage();
			PluginLogger.openError(e);
			MessageView.addMessage(error);
		} catch (Throwable e) {
			e.printStackTrace();
			PluginLogger.openError(e);
			MessageView.addMessage(e);
		}
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_CONNECTION_DOING;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.CONNECTED_ICON;
	}
}
