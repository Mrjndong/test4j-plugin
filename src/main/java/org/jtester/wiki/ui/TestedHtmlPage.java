package org.jtester.wiki.ui;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
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
import org.jtester.wiki.assistor.WikiHelper;

public class TestedHtmlPage extends ViewPart {
	protected final EditedWikiPage editor;
	private Browser browser;

	private FormToolkit toolkit;

	public TestedHtmlPage(final EditedWikiPage editor) {
		this.editor = editor;
	}

	@Override
	public void createPartControl(Composite parent) {
		this.toolkit = new FormToolkit(parent.getDisplay());
		parent.setLayout(new GridLayout(1, true));

		Composite toolbar = this.toolkit.createComposite(parent);
		this.createButtons(toolbar);

		this.browser = new Browser(parent, SWT.NORMAL);
		this.browser.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		this.toolkit.adapt(this.browser, true, true);
		this.browser.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
				| VERTICAL_ALIGN_FILL));

		this.refresh();
	}

	private void createButtons(Composite toolbar) {
		toolbar.setLayoutData(new GridData(HORIZONTAL_ALIGN_FILL | GRAB_HORIZONTAL));
		toolbar.setLayout(new GridLayout(6, false));

		createButton(toolbar, "refresh", ISharedImages.IMG_TOOL_REDO, new RefreshAction());
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

	public void refresh() {
		if (this.browser == null) {
			return;
		}
		String html = "";
		try {
			File test = WikiHelper.getDBFitTestHtml(this.editor.getProject(), this.editor.getEditFile());
			if (test == null) {
				html = "wiki tested result doesn't exist,wikifile:" + this.editor.getEditFile().getName();
			} else {
				html = FolderUtils.readFile(test);
			}
		} catch (Throwable e) {
			html = "read file context error," + e.getMessage();
		}
		try {
			this.browser.setText(html);
		} catch (Exception e) {
			PluginLogger.log("Unable to load syntax", e);
			this.browser.setText(e.getLocalizedMessage());
		}
	}

	@Override
	public void setFocus() {
		this.browser.setFocus();
	}

	private final class RefreshAction extends SelectionAdapter {
		public void widgetSelected(SelectionEvent e) {
			TestedHtmlPage.this.refresh();
		}
	}
}
