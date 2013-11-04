package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

/**
 * 切换数据库连接
 * 
 * @author darui.wudr
 * 
 */
public class DBConnectionSelectMenu extends BaseMenuItem {

	public DBConnectionSelectMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseNode selectedNode = DatabaseView.getSelectedDatabase();
		DatabaseView.setActiveDatabase(selectedNode);
		DatabaseView.expandDatabaseNode(selectedNode);
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_CONNECTION_SELECT;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.SELECTED_DATABASE_ICON;
	}

}
