package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class RefreshDataMenu extends BaseMenuItem {

	public RefreshDataMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DATABASE_REFRESH_VIEW;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.REFRESH_ICON;
	}

}
