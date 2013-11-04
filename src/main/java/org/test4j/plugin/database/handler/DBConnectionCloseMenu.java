package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.DatabaseNode;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

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
