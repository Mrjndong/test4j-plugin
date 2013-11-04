package org.jtester.wiki.ui;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressAdapter;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.resources.PluginResources;
import org.jtester.utils.FolderUtils;
import org.jtester.wiki.assistor.HtmlHelper;
import org.jtester.wiki.assistor.HtmlToFitUtil;
import org.jtester.wiki.assistor.WidgetHelper;

public final class EditedHtmlPage extends ViewPart {
	private EditedWikiPage editor;
	private Button saveBtn;
	private Button editBtn;
	private Button cancelBtn;
	private FormToolkit toolkit;
	private Browser browser;

	public EditedHtmlPage(EditedWikiPage editor) {
		this.editor = editor;
	}

	public void createPartControl(final Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
		parent.setLayout(new GridLayout(1, true));

		Composite toolbar = this.toolkit.createComposite(parent);
		this.createToolbar(toolbar);

		this.browser = new Browser(parent, SWT.NORMAL);
		this.browser.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		this.toolkit.adapt(this.browser, true, true);
		this.browser.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
				| VERTICAL_ALIGN_FILL));

		this.browser.addProgressListener(new ProgressAdapter() {
			public void completed(ProgressEvent event) {
				if (saveBtn.getEnabled()) {
					String context = editor.getDocumentText();
					String html = WidgetHelper.getHtmlBody(context);
					html = HtmlHelper.encapsulateTextArea(html);

					browser.execute(String.format("setEditContent(\"%s\");", html));
					browser.setCursor(null);
				}
			}
		});
	}

	private void createToolbar(Composite toolbar) {
		toolbar.setLayoutData(new GridData(HORIZONTAL_ALIGN_FILL | GRAB_HORIZONTAL));
		toolbar.setLayout(new GridLayout(6, false));

		this.editBtn = createButton(toolbar, "edit", ISharedImages.IMG_ETOOL_PRINT_EDIT, new EditAction());
		this.saveBtn = createButton(toolbar, "save", ISharedImages.IMG_ETOOL_SAVE_EDIT, new SaveAction());
		this.cancelBtn = createButton(toolbar, "cancel", ISharedImages.IMG_TOOL_UNDO, new CancelAction());
		this.saveBtn.setEnabled(false);
		this.cancelBtn.setEnabled(false);
	}

	private Button createButton(Composite parent, String label, String image, SelectionListener listener) {
		Button button = this.toolkit.createButton(parent, PluginResources.getResourceString("WikiBrowser." + label),
				SWT.PUSH);
		button.setLayoutData(new GridData(1));
		button.setToolTipText(PluginResources.getResourceString("WikiBrowser." + label + "Tooltip"));
		button.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(image));
		button.addSelectionListener(listener);
		return button;
	}

	/**
	 * 重画wiki html viewer
	 * 
	 */
	public void redrawTextSync() {
		String wiki = editor.getDocumentText();
		String html = WidgetHelper.getHtmlWithCss(wiki);

		this.browser.setText(html);
	}

	public void setFocus() {
		this.browser.setFocus();
	}

	public void dispose() {
		this.browser.dispose();
		this.toolkit.dispose();
	}

	private final class EditAction extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			saveBtn.setEnabled(true);
			cancelBtn.setEnabled(true);
			editBtn.setEnabled(false);

			browser.setUrl(FolderUtils.USER_HOME + File.separatorChar + "EditDbFit.html");
		}
	}

	private final class SaveAction extends SelectionAdapter {
		public void widgetSelected(SelectionEvent ent) {
			saveBtn.setEnabled(false);
			cancelBtn.setEnabled(false);
			editBtn.setEnabled(true);

			Object o = browser.evaluate("return getEditContent();");
			String html = o == null ? null : o.toString();
			browser.back();
			// browser.refresh();
			browser.setText(html);
			saveEditWiki(html);
			redrawTextSync();
		}

		/**
		 * 保存编辑完毕的wiki文件
		 * 
		 * @param html
		 */
		private void saveEditWiki(String html) {
			if (html == null) {
				return;
			}
			try {
				String wiki = HtmlToFitUtil.toFitWiki(html, editor.getEncodingCharacter());
				wiki = wiki.replaceAll("(\n){3,}", "\n\n");
				editor.setDocumentText(wiki);
				editor.redrawTextAsync();
			} catch (Exception e) {
				PluginLogger.log(e);
				PluginLogger.log("rendered html:\n" + html);
			}
		}
	}

	private final class CancelAction extends SelectionAdapter {
		public void widgetSelected(SelectionEvent ent) {
			saveBtn.setEnabled(false);
			cancelBtn.setEnabled(false);
			editBtn.setEnabled(true);

			browser.back();
			redrawTextSync();
		}
	}
}