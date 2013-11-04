package org.test4j.plugin.testsequence.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.test4j.plugin.definemock.ui.facade.EditorPartFacade;
import org.test4j.plugin.helper.PluginLogger;
import org.test4j.plugin.testsequence.assistor.MethodSequence;
import org.test4j.plugin.testsequence.assistor.SelectSequenceDialog;
import org.test4j.plugin.testsequence.ui.CompareBrowser;

public class TestSequenceCompareHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

		EditorPartFacade facade = new EditorPartFacade(editorPart);
		MethodSequence sequence = facade.getTestMethodSequence();
		if (sequence == null) {
			return null;
		}
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		SelectSequenceDialog dialog = new SelectSequenceDialog(shell, sequence);
		dialog.open();
		String lFile = dialog.getlFile();
		String rFile = dialog.getRFile();

		// show in editor
		this.openCompareView(event);
		CompareBrowser.getInstance().compareTwoFile(sequence, lFile, rFile);
		return null;
	}

	private void openCompareView(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		IWorkbenchPage page = window.getActivePage();
		if (page == null) {
			return;
		}
		try {
			page.showView(CompareBrowser.ID);
		} catch (PartInitException e) {
			PluginLogger.openError(e);
		}
	}
}
