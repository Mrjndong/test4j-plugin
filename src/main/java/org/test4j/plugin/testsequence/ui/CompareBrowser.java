package org.test4j.plugin.testsequence.ui;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.ViewPart;
import org.test4j.plugin.testsequence.assistor.HtmlHelper;
import org.test4j.plugin.testsequence.assistor.MethodSequence;

public class CompareBrowser extends ViewPart {
    public static final String    ID = "org.test4j.plugin.testsequence.CompareBrowser";

    private Browser               browser;

    private static CompareBrowser instance;

    public CompareBrowser() {
        CompareBrowser.instance = this;
    }

    public synchronized static CompareBrowser getInstance() {
        if (instance == null) {
            instance = new CompareBrowser();
        }
        return instance;
    }

    @Override
    public void createPartControl(Composite parent) {
        this.browser = new Browser(parent, SWT.NORMAL);
        this.browser.setData(FormToolkit.KEY_DRAW_BORDER, FormToolkit.TEXT_BORDER);
        this.browser.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
                | VERTICAL_ALIGN_FILL));
        this.browser.setText("test");
    }

    @Override
    public void setFocus() {
        this.browser.setFocus();
    }

    public void compareTwoFile(MethodSequence sequence, String lFile, String rFile) {
        String html = "";
        if (lFile == null || rFile == null) {
            StringBuffer buf = new StringBuffer();
            buf.append("<p>you unselect compare file.</p>");
            buf.append("<p>left file: ").append(lFile == null ? "Unselected" : lFile).append("</p>");
            buf.append("<p>right file: ").append(rFile == null ? "Unselected" : rFile).append("</p>");
            html = buf.toString();
        } else {
            String lContext = this.readSequenceFromFile(sequence, lFile);
            String rContext = this.readSequenceFromFile(sequence, rFile);
            html = HtmlHelper.getCompareHtml(lContext, rContext);
        }
        this.browser.setText(html);
    }

    private String readSequenceFromFile(MethodSequence sequence, String filename) {
        if (filename.startsWith("db://")) {
            return sequence.readSequenceFromRemote(filename);
        } else {
            return sequence.readSequenceFromLocal(filename);
        }
    }
}
