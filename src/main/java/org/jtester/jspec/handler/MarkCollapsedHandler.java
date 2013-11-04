package org.jtester.jspec.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jtester.jspec.ui.JSpecBrowser;
import org.jtester.jspec.ui.JSpecEditor;

public class MarkCollapsedHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		JSpecEditor editor = this.currentEditor(event);
		if (editor == null) {
			return null;
		}
		editor.toggleFolding(true);
		return null;
	}

	protected JSpecEditor currentEditor(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		final IEditorPart editor = window.getActivePage().getActiveEditor();
		if (editor instanceof JSpecEditor) {
			return (JSpecEditor) editor;
		} else if (editor instanceof JSpecBrowser) {
			return ((JSpecBrowser) editor).getJspecEditor();
		}
		return null;
	}
}
