package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DataSetView;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

public class CopyAsCheckDataMenu extends BaseMenuItem {

	public CopyAsCheckDataMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DataSetView.copyAsJavaMap(null, false);
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DATABASE_SELECT_AS_CHECK_DATA;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.COPY_CHECK_DATA;
	}
}
