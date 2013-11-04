package org.jtester.jspec.ui;

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
import org.jtester.jspec.JSpecProp;

/**
 * wiki多页面浏览编辑器<Br>
 * o wiki文本编辑器<br>
 * o html浏览器<br>
 * o 测试结果
 * 
 * @author darui.wudr
 * 
 */
public final class JSpecBrowser extends MultiPageEditorPart {
	private JSpecEditor jspecEditor;
	private JSpecHtmlViewer jspecViewer;

	private int jspecEditorPageIndex;
	private int editedHtmlPageIndex;
	private int jspecViewerPageIndex;

	private boolean isPageModified;

	public JSpecBrowser() {
	}

	public JSpecEditor getJspecEditor() {
		return jspecEditor;
	}

	/**
	 * 依次创建editor<br>
	 * <br>
	 * {@inheritDoc}
	 */
	@Override
	protected void createPages() {
		this.createJSpecEditorPage();
		this.createJSpecHtlmViewerPage();
		super.setActivePage(this.editedHtmlPageIndex);

		IEditorInput input = getEditorInput();
		super.setPartName(input.getName());
		super.setTitleToolTip(input.getToolTipText());
	}

	/**
	 * 创建编辑wiki文件的tab页
	 */
	private void createJSpecEditorPage() {
		try {
			this.jspecEditor = new JSpecEditor();
			this.jspecEditorPageIndex = super.addPage(this.jspecEditor, super.getEditorInput());
			super.setPageText(this.jspecEditorPageIndex, JSpecProp.JSPEC_EDITOR_NAME);
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus());
		}
	}

	/**
	 * 创建浏览测试结果的tab页
	 */
	private void createJSpecHtlmViewerPage() {
		final Composite parent = new Composite(super.getContainer(), SWT.NORMAL);
		parent.setLayout(new FillLayout());

		this.jspecViewer = new JSpecHtmlViewer(this.jspecEditor);
		this.jspecViewer.createPartControl(parent);
		this.jspecViewerPageIndex = super.addPage(parent);
		super.setPageText(this.jspecViewerPageIndex, JSpecProp.JSPEC_COMPARE_VIEWER);
	}

	public boolean isDirty() {
		return isPageModified || super.isDirty();
	}

	/**
	 * 保存wiki内容<br>
	 * {@inheritDoc}
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		super.getEditor(0).doSave(monitor);
	}

	/**
	 * wiki文件另存为<br>
	 * {@inheritDoc}
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

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == this.editedHtmlPageIndex) {
			this.jspecViewer.redrawTextSync();
		}
		super.pageChange(newPageIndex);
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}
}