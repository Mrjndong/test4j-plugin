package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DataSetView;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

public class CopyAsJsonDataMenu extends BaseMenuItem {

    public CopyAsJsonDataMenu(IViewSite viewSite) {
        super(viewSite);
    }

    @Override
    protected void executeHandler(ExecutionEvent event) {
        DataSetView.copyAsJson(null);
    }

    @Override
    protected String getMenuText() {
        return PluginMessages.DATABASE_SELECT_AS_JSON;
    }

    @Override
    protected String getMenuIcon() {
        return IconResources.COPY_JSON_DATA;
    }
}
