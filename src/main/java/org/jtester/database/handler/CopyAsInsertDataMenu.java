package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DataSetView;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class CopyAsInsertDataMenu extends BaseMenuItem {

	public CopyAsInsertDataMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DataSetView.copyAsJavaMap(null, true);
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DATABASE_SELECT_AS_INSERT_DATA;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.COPY_INSERT_DATA;
	}
}
