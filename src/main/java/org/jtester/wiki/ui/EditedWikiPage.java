package org.jtester.wiki.ui;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.jtester.database.assistor.WikiToDataMap;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.helper.ColourManager;
import org.jtester.plugin.helper.PluginHelper;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.helper.PreferenceHelper;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;
import org.jtester.wiki.assistor.WikiConfiguration;
import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.assistor.WikiHelper;

public final class EditedWikiPage extends TextEditor {
	private ColourManager colourManager;

	public static final String PART_ID = EditedWikiPage.class.getName();
	public static final String CONTEXT_MENU_ID = PART_ID + ".ContextMenu";
	public static final String WIKI_TEMP_FOLDER = "wiki";
	public static final String WIKI_TEMP_PROJECT = "wiki_temp";

	private Color backgroundColor;

	private WikiFile wikiFile;

	public EditedWikiPage() {
		super();
		this.colourManager = new ColourManager();
		super.setSourceViewerConfiguration(new WikiConfiguration(this));
		super.setDocumentProvider(new FileDocumentProvider());
		super.setEditorContextMenuId(CONTEXT_MENU_ID);
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		this.setBackgroundColor();
		this.setWordWrap();
	}

	/**
	 * 设置editor背景色
	 */
	private void setBackgroundColor() {
		RGB rgb = null;
		if (PreferenceHelper.isSysWikiEditBgColor()) {
			rgb = null;
		} else {
			rgb = PreferenceHelper.wikiEditorBgColor();
		}
		this.backgroundColor = colourManager.getColor(rgb);
		super.getSourceViewer().getTextWidget().setBackground(this.backgroundColor);
	}

	private void setWordWrap() {
		ISourceViewer viewer = this.getSourceViewer();
		if (viewer != null) {
			viewer.getTextWidget().setWordWrap(PreferenceHelper.wordWrap());
		}
	}

	public void dispose() {
		colourManager.dispose();
		super.dispose();
	}
	
	/**
	 * 返回editor的文档器
	 * 
	 * @return
	 */
	public IDocument getDocument() {
		IEditorInput input = super.getEditorInput();
		return super.getDocumentProvider().getDocument(input);
	}

	@Override
	public FileEditorInput getEditorInput() {
		return (FileEditorInput) super.getEditorInput();
	}

	public int getOffset(int line) throws BadLocationException {
		return getDocumentProvider().getDocument(getEditorInput()).getLineOffset(line);
	}

	public ColourManager getColourManager() {
		return colourManager;
	}

	/**
	 * 获得编辑器中的文本内容
	 * 
	 * @return
	 */
	public String getDocumentText() {
		ISourceViewer viewer = super.getSourceViewer();
		if (viewer != null && viewer.getDocument() != null) {
			return viewer.getDocument().get();
		} else {
			return "";
		}
	}

	/**
	 * 设置编辑器中的文本内容
	 * 
	 * @return
	 */
	public void setDocumentText(String content) {
		ISourceViewer viewer = super.getSourceViewer();
		if (viewer != null && viewer.getDocument() != null) {
			viewer.getDocument().set(content);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.editors.text.TextEditor#doSetInput(org.eclipse.ui.IEditorInput
	 * )
	 */
	protected void doSetInput(IEditorInput input) throws CoreException {
		super.doSetInput(input);
		try {
			IFile file = ((IFileEditorInput) input).getFile();
			this.wikiFile = new WikiFile(file);
		} catch (Exception e) {
			PluginLogger.log("Error whilst setting editor input", e);
		}
	}

	/**
	 * 通知异步重画text内容
	 */
	public void redrawTextAsync() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				ISourceViewer viewer = EditedWikiPage.this.getSourceViewer();
				if (viewer != null && !viewer.getTextWidget().isDisposed()) {
					viewer.invalidateTextPresentation();
				}
			}
		});
	}

	public WikiFile getContext() {
		return this.wikiFile;
	}

	/**
	 * 返回正在编辑的wiki file
	 * 
	 * @return
	 */
	public IFile getEditFile() {
		return this.wikiFile.getWikiFile();
	}

	public String getFileName() {
		return getEditFile().getName();
	}

	/**
	 * 是否允许编辑{@inheritDoc}
	 */
	public boolean isEditable() {
		IEditorInput editorInput = super.getEditorInput();
		if (editorInput instanceof FileEditorInput) {
			IFile file = ((FileEditorInput) editorInput).getFile();
			return !WikiHelper.isTempWiki(file);
		}
		return true;
	}

	/**
	 * 获得的wiki editor所关联的project<br>
	 * 
	 * @return context.workingLocation.project
	 */
	public IProject getProject() {
		if (wikiFile == null) {
			return null;
		}
		return wikiFile.getProject();
	}

	public String getEncodingCharacter() {
		IFile file = this.wikiFile.getWikiFile();
		try {
			return file.getCharset();
		} catch (CoreException e) {
			PluginLogger.log(e);
			return "utf-8";
		}
	}

	@Override
	protected void editorContextMenuAboutToShow(IMenuManager menu) {
		super.editorContextMenuAboutToShow(menu);
		IEditorSite editorSite = this.getEditorSite();
		menu.add(new BaseMenuItem(editorSite) {
			@Override
			protected void executeHandler(ExecutionEvent event) {
				String wiki = EditedWikiPage.this.getDocumentText();
				try {
					String java = WikiToDataMap.covert(wiki);
					if (java == null || "".equals(java)) {
						java = "//Nothing to convert.";
					}
					PluginHelper.copyToClipBoard(java);
				} catch (Exception e) {
					StringWriter writer = new StringWriter();
					e.printStackTrace(new PrintWriter(writer));
					PluginHelper.copyToClipBoard(writer.toString());
				}
			}

			@Override
			protected String getMenuText() {
				return PluginMessages.WIKI_CONVERT_TO_JAVA;
			}

			@Override
			protected String getMenuIcon() {
				return IconResources.CONVERT_TO_JAVA;
			}
		});
	}
}