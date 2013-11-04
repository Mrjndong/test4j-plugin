package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.test4j.plugin.database.ui.DataSetView;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.MessageView;
import org.test4j.plugin.database.ui.SQLEditor;
import org.test4j.plugin.helper.PluginLogger;

public abstract class OpenViewHandler extends AbstractHandler {
	protected IWorkbenchWindow window;

	public Object execute(ExecutionEvent event) throws ExecutionException {
		this.window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}
		try {
			String viewID = this.getViewID();
			page.showView(viewID);
		} catch (PartInitException e) {
			PluginLogger.openError(e);
		}
		return null;
	}

	/**
	 * 返回要打开的视图标识号
	 * 
	 * @return
	 */
	protected abstract String getViewID();

	public static class OpenDatabaseViewHandler extends OpenViewHandler {
		@Override
		protected String getViewID() {
			return DatabaseView.ID;
		}
	}

	public static class OpenMessageViewHandler extends OpenViewHandler {
		@Override
		protected String getViewID() {
			return MessageView.ID;
		}
	}

	public static class OpenResultViewHandler extends OpenViewHandler {

		@Override
		protected String getViewID() {
			return DataSetView.ID;
		}
	}

	public static class OpenSqlEditorHandler extends OpenViewHandler {
		@Override
		protected String getViewID() {
			return SQLEditor.ID;
		}
	}
}
