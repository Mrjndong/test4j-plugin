package org.test4j.plugin.database.handler;

import java.sql.SQLException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.MessageView;
import org.test4j.plugin.database.ui.SQLEditor;
import org.test4j.plugin.database.ui.tree.DatabaseNode;
import org.test4j.plugin.helper.PluginLogger;
import org.test4j.plugin.resources.PluginMessages;

public class ExecuteSqlHandler extends AbstractHandler {
	private static final boolean newTab = false;

	protected IWorkbenchWindow window;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		DatabaseNode activeDatabase = DatabaseView.getActiveDatabase();
		if (activeDatabase == null) {
			PluginLogger.openWarning(PluginMessages.DB_CONNECTION_SELECT);
			return null;
		}

		final SQLEditor editor = (SQLEditor) window.getActivePage().getActiveEditor();
		if (editor == null) {
			MessageView.addMessage("No active editor");
			return null;
		}
		try {
			String sql = editor.getSelectedText();
			if (sql.equals("")) {
				sql = editor.getText();
			}
			if (sql == null || sql.trim().length() == 0) {
				return null;
			}

			activeDatabase.executeQuery(sql, newTab);
		} catch (SQLException e) {
			PluginLogger.openError(e);
		}
		return null;
	}
}
