package org.test4j.plugin.testsequence.handler;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.test4j.plugin.definemock.ui.facade.EditorPartFacade;
import org.test4j.plugin.helper.PluginLogger;
import org.test4j.plugin.testsequence.assistor.MethodSequence;

public class TestSequenceOpenHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

		EditorPartFacade facade = new EditorPartFacade(editorPart);
		MethodSequence sequence = facade.getTestMethodSequence();
		if (sequence == null) {
			return null;
		}
		File file = sequence.getLastSequenceFile();
		if (file == null) {
			return null;
		}

		try {
			IFileStore fileStore = EFS.getLocalFileSystem().getStore(file.toURI());
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
			IWorkbenchPage page = window.getActivePage();
			IDE.openEditorOnFileStore(page, fileStore);
		} catch (PartInitException e) {
			PluginLogger.log(e);
		}
		return null;
	}
}
