package org.jtester.wiki.ui;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.jtester.plugin.helper.PreferenceHelper;
import org.jtester.plugin.resources.PluginResources;

/**
 * wiki多页面浏览编辑器<Br>
 * o wiki文本编辑器<br>
 * o html浏览器<br>
 * o 测试结果
 * 
 * @author darui.wudr
 * 
 */
public final class WikiBrowser extends MultiPageEditorPart {
	private EditedWikiPage editedWikiPage;
	private EditedHtmlPage editedHtmlPage;
	private TestedHtmlPage testedHtmlPage;

	private int editedWikiPageIndex;
	private int editedHtmlPageIndex;
	private int testedHtmlPageIndex;

	private boolean isPageModified;

	public WikiBrowser() {
	}

	/**
	 * 依次创建下列editor<br>
	 * o wiki text page <br>
	 * o wiki html page <br>
	 * o wiki syntax page <br>
	 * o 设置active page <br>
	 * <br>{@inheritDoc}
	 */
	@Override
	protected void createPages() {
		this.createEditedWikiPage();
		this.createEditedHtmlPage();
		this.createTestedHtmlPage();

		if (PreferenceHelper.showHtmlBrowserDefault()) {
			super.setActivePage(this.editedHtmlPageIndex);
		} else {
			super.setActivePage(this.editedWikiPageIndex);
		}
		IEditorInput input = getEditorInput();
		super.setPartName(input.getName());
		super.setTitleToolTip(input.getToolTipText());
	}

	/**
	 * 创建编辑wiki文件的tab页
	 */
	private void createEditedWikiPage() {
		try {
			this.editedWikiPage = new EditedWikiPage();
			this.editedWikiPageIndex = super.addPage(this.editedWikiPage, super.getEditorInput());
			super.setPageText(this.editedWikiPageIndex, PluginResources.WIKI_TEXT_VIEWER_TITLE);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * 创建编辑（浏览）html内容的tab页
	 */
	private void createEditedHtmlPage() {
		final Composite parent = new Composite(super.getContainer(), SWT.NORMAL);
		parent.setLayout(new FillLayout());

		this.editedHtmlPage = new EditedHtmlPage(this.editedWikiPage);
		this.editedHtmlPage.createPartControl(parent);
		this.editedHtmlPageIndex = super.addPage(parent);
		super.setPageText(this.editedHtmlPageIndex, PluginResources.WIKI_HTML_VIEWER_TITLE);
	}

	/**
	 * 创建浏览测试结果的tab页
	 */
	private void createTestedHtmlPage() {
		final Composite parent = new Composite(super.getContainer(), SWT.NORMAL);
		parent.setLayout(new FillLayout());

		this.testedHtmlPage = new TestedHtmlPage(this.editedWikiPage);
		this.testedHtmlPage.createPartControl(parent);
		this.testedHtmlPageIndex = super.addPage(parent);
		super.setPageText(this.testedHtmlPageIndex, PluginResources.WIKI_TEST_VIEWER_TITLE);
	}

	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	/**
	 * 保存wiki内容<br>{@inheritDoc}
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.getEditor(0).doSave(monitor);
	}

	/**
	 * wiki文件另存为<br>{@inheritDoc}
	 */
	@Override
	public void doSaveAs() {
		IEditorPart editor = super.getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}

	/**
	 * 转到marker标记对应的行
	 * 
	 * @param marker
	 */
	public void gotoMarker(IMarker marker) {
		super.setActivePage(0);
		IDE.gotoMarker(super.getEditor(0), marker);
	}

	/**
	 * viewer改变时重画wiki html browser
	 */
	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == this.editedHtmlPageIndex) {
			this.editedHtmlPage.redrawTextSync();
		}
		super.pageChange(newPageIndex);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	public EditedWikiPage getEditor() {
		return this.editedWikiPage;
	}
}