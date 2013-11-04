package org.jtester.database.handler;

import java.sql.SQLException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.MessageView;
import org.jtester.database.ui.tree.BaseTreeNode;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.database.ui.tree.TableNode;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class DBTableCountQueryMenu extends BaseMenuItem {

	public DBTableCountQueryMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseNode databaseModel = DatabaseView.getActiveDatabase();

		if (databaseModel == null) {
			PluginLogger.openInformation(PluginMessages.DB_CONNECTION_SELECT);
			return;
		}

		try {
			BaseTreeNode model = DatabaseView.getTreeViewer().getSelectedModel();
			if (model != null && model instanceof TableNode) {
				TableNode tableModel = (TableNode) model;
				String query = "SELECT count(*) FROM " + tableModel.getName();
				databaseModel.executeQuery(query);
			} else {
				MessageView.addMessage(PluginMessages.DB_TABLE_SELECT);
			}
		} catch (SQLException e) {
			PluginLogger.openError(e);
		}
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_TABLE_COUNT;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.RUN_ICON;
	}
}
