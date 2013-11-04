package org.jtester.jspec.ui;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.jtester.jspec.assistor.ScenarioCompare;

public class JSpecHtmlViewer extends ViewPart {
	private JSpecEditor editor;
	private Browser browser;

	public JSpecHtmlViewer(JSpecEditor editor) {
		this.editor = editor;
	}

	public void createPartControl(final Composite parent) {
		parent.setLayout(new GridLayout(1, true));

		this.browser = new Browser(parent, SWT.NORMAL);
		this.browser.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
		this.browser.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
				| VERTICAL_ALIGN_FILL));
	}

	/**
	 * 重画jspec viewer
	 */
	public void redrawTextSync() {
		String story = editor.getDocumentText();
		String html = ScenarioCompare.getCompareHtml(story);

		this.browser.setText(html);
	}

	public void setFocus() {
		this.browser.setFocus();
	}

	public void dispose() {
		this.browser.dispose();
	}
}
