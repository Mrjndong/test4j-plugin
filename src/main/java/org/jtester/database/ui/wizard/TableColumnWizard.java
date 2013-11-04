package org.jtester.database.ui.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.BaseTreeNode;
import org.jtester.database.ui.tree.ColumnNode;
import org.jtester.database.ui.tree.InvisibleNode;
import org.jtester.database.ui.tree.TableNode;

/**
 * 表结构向导
 * 
 * @author darui.wudr
 * 
 */
public abstract class TableColumnWizard extends Wizard {

	protected TableNode tableModel = null;

	protected BaseTreeNode[] columns = null;

	/**
	 * @param model
	 */
	public TableColumnWizard(BaseTreeNode model) {
		if (model instanceof TableNode) {
			initTableModel((TableNode) model);
		} else if (model instanceof ColumnNode) {
			initColumnModel((ColumnNode) model);
		}
	}

	/**
	 * @param model
	 */
	private void initTableModel(TableNode tableModel) {
		this.tableModel = tableModel;

		this.columns = tableModel.getChildren();
		// expand
		if (columns != null && columns.length > 0 && columns[0] instanceof InvisibleNode) {
			tableModel.refresh();
		}
	}

	/**
	 * @param model
	 */
	private void initColumnModel(ColumnNode columnModel) {

		this.tableModel = (TableNode) columnModel.getParent();

		initSelectedColumns();
	}

	/**
	 * Get the selection
	 */
	private void initSelectedColumns() {
		List<ColumnNode> selectedColumns = new ArrayList<ColumnNode>();
		StructuredSelection selection = (StructuredSelection) DatabaseView.getTreeViewer().getSelection();
		Iterator<?> iter = selection.iterator();
		while (iter.hasNext()) {
			selectedColumns.add((ColumnNode) iter.next());
		}

		if (selectedColumns.size() > 0) {
			this.columns = selectedColumns.toArray(new ColumnNode[0]);
		}
	}
}
