package org.jtester.database.ui.wizard;

import java.io.IOException;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jtester.database.ui.tree.BaseTreeNode;
import org.jtester.database.ui.tree.ColumnNode;
import org.jtester.database.ui.tree.InvisibleNode;
import org.jtester.plugin.helper.PluginLogger;

/**
 * 插入数据向导
 * 
 * @author darui.wudr
 * 
 */
public class InsertWizard extends TableColumnWizard {
	private InsertPage insertPage;

	/**
	 * @param model
	 */
	public InsertWizard(BaseTreeNode model) {
		super(model);
		setWindowTitle("Insert Wizard");
	}

	public boolean performFinish() {
		try {
			return insertPage.performFinish();
		} catch (Exception e) {
			PluginLogger.openError(e);
		}
		return false;
	}

	public void addPages() {
		insertPage = new InsertPage("Insert");
		addPage(insertPage);
	}

	class InsertPage extends WizardPage {
		private Text[] texts = null;

		public InsertPage(String pageName) {
			super(pageName);
		}

		public void createControl(Composite parent) {
			Composite container = new Composite(parent, SWT.NULL);
			GridLayout gridLayout = new GridLayout();
			gridLayout.numColumns = 2;
			container.setLayout(gridLayout);

			GridData data = null;

			if (columns != null && columns.length > 0 && columns[0] instanceof InvisibleNode) {
				tableModel.refresh();
				columns = tableModel.getChildren();
			}

			texts = new Text[columns.length];

			for (int i = 0; i < columns.length; i++) {
				ColumnNode column = (ColumnNode) columns[i];
				new Label(container, SWT.PUSH).setText(column.getName());

				texts[i] = new Text(container, SWT.BORDER);
				data = new GridData();
				data.widthHint = 150;
				texts[i].setLayoutData(data);
			}

			setControl(container);
			setPageComplete(true);
		}

		public boolean performFinish() throws IOException {

			return false;
		}
	}
}