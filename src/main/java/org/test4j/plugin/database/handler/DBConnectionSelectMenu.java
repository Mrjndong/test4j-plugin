package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.DatabaseNode;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

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
