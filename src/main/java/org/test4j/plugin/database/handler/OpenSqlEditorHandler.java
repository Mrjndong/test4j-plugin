package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.test4j.plugin.database.assistor.SQLEditorInput;
import org.test4j.plugin.database.ui.SQLEditor;
import org.test4j.plugin.helper.PluginLogger;

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
