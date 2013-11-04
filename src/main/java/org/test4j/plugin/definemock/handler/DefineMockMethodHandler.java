package org.test4j.plugin.definemock.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IType;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.test4j.plugin.definemock.ui.facade.EditorPartFacade;
import org.test4j.plugin.helper.PluginLogger;

public class DefineMockMethodHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editorPart = HandlerUtil.getActiveEditor(event);

		IFile ifile = (IFile) editorPart.getEditorInput().getAdapter(IFile.class);
		if (ifile.getName().endsWith(".java") == false) {
			return null;
		}
		EditorPartFacade facade = new EditorPartFacade(editorPart);
		try {
			IType type = facade.getFirstNonAnonymousMethodSurroundingCursorPosition();
			System.out.println(type);
			return null;
		} catch (Exception e) {
			PluginLogger.log(e.getMessage(), 1);
			return null;
		}
	}

	// private void executeJumpAction(ICompilationUnit compilationUnit, IMethod
	// methodUnderCursorPosition) {
	//
	//
	// TypeFacade typeFacade = TypeFacade.createFacade(compilationUnit);
	// IMember memberToJump =
	// typeFacade.getOneCorrespondingMember(methodUnderCursorPosition, true,
	// extendedSearch,
	// "Jump to...");
	// if (memberToJump != null) {
	// jumpToMember(memberToJump);
	// }
	// }
}
