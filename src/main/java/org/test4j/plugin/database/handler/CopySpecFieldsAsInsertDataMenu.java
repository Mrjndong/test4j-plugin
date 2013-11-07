package org.test4j.plugin.database.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DataSetView;
import org.test4j.plugin.database.ui.wizard.SelectColumnsDialog;
import org.test4j.plugin.database.ui.wizard.SelectColumnsDialog.CopyAs;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 复制指定列为插入数据
 * 
 * @author darui.wudr
 */
public class CopySpecFieldsAsInsertDataMenu extends BaseMenuItem {

    public CopySpecFieldsAsInsertDataMenu(IViewSite viewSite) {
        super(viewSite);
    }

    @Override
    protected void executeHandler(ExecutionEvent event) {
        List<String> columns = DataSetView.getInstance().getVisibleColumns();
        if (columns == null || columns.size() == 0) {
            return;
        }
        SelectColumnsDialog dialog = new SelectColumnsDialog(this.workbenchPartSite.getShell(), columns,
                CopyAs.InsertJavaMap);
        dialog.open();
    }

    @Override
    protected String getMenuText() {
        return PluginMessages.DATABASE_SELECT_FIELDS_AS_INSERT_DATA;
    }

    @Override
    protected String getMenuIcon() {
        return IconResources.COPY_INSERT_DATA;
    }

}
