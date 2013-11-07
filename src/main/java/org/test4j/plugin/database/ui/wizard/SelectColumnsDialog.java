package org.test4j.plugin.database.ui.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.test4j.plugin.database.ui.DataSetView;

/**
 * 选择数据列向导
 * 
 * @author darui.wudr
 */
public class SelectColumnsDialog extends TitleAreaDialog {

    private CopyAs                   copyAs;

    private List<String>             columns;

    private List<BooleanFieldEditor> checks = new ArrayList<BooleanFieldEditor>();

    public SelectColumnsDialog(Shell parentShell, List<String> columns, CopyAs copyAs) {
        super(parentShell);
        super.setTitle("Select Columns Wizard");
        this.columns = columns;
        this.copyAs = copyAs;
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Control contents = super.createDialogArea(parent);
        if (columns == null || columns.size() == 0) {
            return contents;
        }

        GridLayout layout = GridLayoutFactory.fillDefaults().numColumns(4).margins(10, 10).spacing(5, 5).create();
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(layout);

        for (String column : columns) {
            Composite checkContainer = new Composite(container, SWT.NULL);
            BooleanFieldEditor check = new BooleanFieldEditor(column, column, checkContainer);
            this.checks.add(check);
        }
        return contents;
    }

    @Override
    protected void okPressed() {
        List<String> valids = new ArrayList<String>();
        for (BooleanFieldEditor check : checks) {
            if (check.getBooleanValue()) {
                String column = check.getLabelText();
                valids.add(column);
            }
        }
        switch (this.copyAs) {
            case InsertJavaMap:
                DataSetView.copyAsJavaMap(valids, true);
                break;
            case CheckJavaMap:
                DataSetView.copyAsJavaMap(valids, false);
                break;
            default:
                DataSetView.copyAsJson(valids);
        }
        super.okPressed();
    }

    public static enum CopyAs {
        InsertJavaMap,

        CheckJavaMap,

        Json;
    }
}
