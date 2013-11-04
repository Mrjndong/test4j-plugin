package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

public class DBTreeRefreshMenu extends BaseMenuItem {

	public DBTreeRefreshMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseView.getInstance().refreshSelectedModel();
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_TREE_REFRESH;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.REFRESH_ICON;
	}

}
