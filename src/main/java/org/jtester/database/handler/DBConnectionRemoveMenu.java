package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.database.ui.tree.DatabaseTree;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class DBConnectionRemoveMenu extends BaseMenuItem {

	public DBConnectionRemoveMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseNode selectedDatabaseNode = (DatabaseNode) DatabaseView.getTreeViewer().getSelectedModel();

		DatabaseTree.invisibleRootModel.removeChild(selectedDatabaseNode);
		if (selectedDatabaseNode == DatabaseView.getActiveDatabase()) {
			DatabaseView.setActiveDatabase(null);
		}
		DatabaseView.refresh();
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_CONNECTION_REMOVE;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.REMOVE_ICON;
	}

}
