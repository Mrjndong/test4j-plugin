package org.jtester.database.handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.ui.IViewSite;
import org.jtester.database.assistor.SQLUtils;
import org.jtester.database.ui.DataSetView;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.helper.PluginHelper;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;
import org.test4j.tools.commons.StringHelper;

public class DBTableQueryMenu extends BaseMenuItem {

	public DBTableQueryMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseNode activeDatabaseNode = DatabaseView.getActiveDatabase();

		if (activeDatabaseNode == null) {
			PluginLogger.openInformation(PluginMessages.DB_CONNECTION_SELECT);
			return;
		}

		String query = DatabaseView.getTreeViewer().buildQueryStatment();
		if (StringHelper.isBlankOrNull(query)) {
			PluginLogger.openInformation(PluginMessages.DB_TABLE_SELECT);
			return;
		}
		try {
			PluginHelper.showView(DataSetView.ID);

			ResultSet rs = DatabaseView.getSelectedDatabase().executeQuery(query);

			List<String> columnNames = SQLUtils.getColumnNames(rs);
			List<Map<String, String>> datas = SQLUtils.getQueryDataSet(columnNames, rs, 100);

			DataSetView.getInstance().reFillDataSet(query, columnNames, datas);
		} catch (SQLException e) {
			PluginLogger.openError(e);
		}
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_TABLE_QUERY;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.RUN_ICON;
	}
}
