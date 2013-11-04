package org.test4j.plugin.database.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;
import org.test4j.plugin.database.assistor.SQLPartitionScanner;
import org.test4j.plugin.database.assistor.SQLSourceViewerConfiguration;
import org.test4j.plugin.helper.ColourManager;

/**
 * SQL Editor
 * 
 * @author darui.wudr
 * 
 */
public class SQLEditor extends TextEditor {
	public static String ID = "org.test4j.plugin.database.ui.SQLEditor";
	public static String MENU_ID = "#" + ID;

	private ColourManager colourManager;

	private FileDocumentProvider documentProvider;

	public SQLEditor() {
		super();
		this.colourManager = new ColourManager();
		this.setSourceViewerConfiguration(new SQLSourceViewerConfiguration(colourManager));
		this.documentProvider = new SQLFileDocumentProvider();
		this.setDocumentProvider(this.documentProvider);
		this.setEditorContextMenuId(MENU_ID);
	}

	@Override
	protected void editorContextMenuAboutToShow(final IMenuManager menuMgr) {
		menuMgr.setRemoveAllWhenShown(true);

		menuMgr.add(new Separator("edit"));
		menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		
		super.editorContextMenuAboutToShow(menuMgr);
	}

	public String getSelectedText() {
		String selected = getSourceViewer().getTextWidget().getSelectionText();

		return selected;
	}

	public String getText() {
		String text = getSourceViewer().getTextWidget().getText();

		return text;
	}

	public void dispose() {
		colourManager.dispose();
		super.dispose();
	}

	/**
	 * @see EditorPart#isDirty
	 */
	public boolean isDirty() {
		if (getEditorInput() instanceof FileEditorInput) {
			return super.isDirty();
		}
		return false;
	}

	/**
	 * @see EditorPart#isSaveAsAllowed
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
}

class SQLFileDocumentProvider extends FileDocumentProvider {
	@Override
	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new FastPartitioner(new SQLPartitionScanner(), new String[] {
					SQLPartitionScanner.SQL_KEYWORD, SQLPartitionScanner.SQL_COMMENT });
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}
