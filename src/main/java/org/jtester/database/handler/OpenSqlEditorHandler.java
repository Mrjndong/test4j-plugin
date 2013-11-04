package org.jtester.database.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jtester.database.assistor.SQLEditorInput;
import org.jtester.database.ui.SQLEditor;
import org.jtester.plugin.helper.PluginLogger;

public class OpenSqlEditorHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return null;
		}
		try {
			page.openEditor(new SQLEditorInput(), SQLEditor.ID);
		} catch (PartInitException e) {
			PluginLogger.openError(e);
		}
		return null;
	}
}
