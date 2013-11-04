package org.jtester.database.assistor;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorActionBarContributor;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.part.EditorActionBarContributor;

/**
 * Insert the type's description here.
 * 
 * @see EditorActionBarContributor EditorActionBarContributor
 */
public class SQLEditorContributor extends TextEditorActionContributor {
//	protected RetargetTextEditorAction fContentAssistProposal;
//	protected RetargetTextEditorAction fContentAssistTip;

	/**
	 * The constructor.
	 */
	public SQLEditorContributor() {
		super();

//		fContentAssistProposal = new RetargetTextEditorAction(SQLMessages.getResourceBundle(), "ContentAssistProposal.");
//		fContentAssistTip = new RetargetTextEditorAction(SQLMessages.getResourceBundle(), "ContentAssistTip.");
	}

	/**
	 * @see IEditorActionBarContributor#init(IActionBars)
	 */
	public void init(IActionBars bars) {
		super.init(bars);

		IMenuManager menuManager = bars.getMenuManager();
		IMenuManager editMenu = menuManager.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if (editMenu != null) {
			editMenu.add(new Separator());
//			editMenu.add(fContentAssistProposal);
		}

		IToolBarManager toolBarManager = bars.getToolBarManager();
		if (toolBarManager != null) {
			toolBarManager.add(new Separator());
		}
	}

//	private void doSetActiveEditor(IEditorPart part) {
//		super.setActiveEditor(part);
//
////		ITextEditor editor = null;
////		if (part instanceof ITextEditor)
////			editor = (ITextEditor) part;
//
////		fContentAssistProposal.setAction(getAction(editor, "ContentAssistProposal"));
////		fContentAssistTip.setAction(getAction(editor, "ContentAssistTip"));
//	}

//	/**
//	 * @see IEditorActionBarContributor#setActiveEditor(IEditorPart)
//	 */
//	public void setActiveEditor(IEditorPart part) {
//		super.setActiveEditor(part);
//		doSetActiveEditor(part);
//	}
//
//	/**
//	 * @see IEditorActionBarContributor#dispose()
//	 */
//	public void dispose() {
//		doSetActiveEditor(null);
//		super.dispose();
//	}
}
