package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class DBConnectionCloseMenu extends BaseMenuItem {

	public DBConnectionCloseMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseNode databaseNode = DatabaseView.getSelectedDatabase();
		if (databaseNode.isConnected() == false) {
			return;
		}
		DatabaseView.disconnect(databaseNode);
		DatabaseView.refresh();
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_CONNECTION_CLOSE;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.DISCONNECTED_ICON;
	}

}
