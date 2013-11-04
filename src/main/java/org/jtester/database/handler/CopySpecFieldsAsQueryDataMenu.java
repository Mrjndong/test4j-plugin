package org.jtester.database.handler;

import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DataSetView;
import org.jtester.database.ui.wizard.SelectColumnsDialog;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

/**
 * 复制指定列为插入数据
 * 
 * @author darui.wudr
 * 
 */
public class CopySpecFieldsAsQueryDataMenu extends BaseMenuItem {

	public CopySpecFieldsAsQueryDataMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		List<String> columns = DataSetView.getInstance().getVisibleColumns();
		if (columns == null || columns.size() == 0) {
			return;
		}
		SelectColumnsDialog dialog = new SelectColumnsDialog(this.workbenchPartSite.getShell(), columns, false);
		dialog.open();
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DATABASE_SELECT_FIELDS_AS_CHECK_DATA;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.COPY_CHECK_DATA;
	}

}
