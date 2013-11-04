package org.test4j.plugin.database.ui.tree;

import static org.test4j.plugin.resources.IconResources.TABLE_ICON;
import static org.test4j.plugin.resources.IconResources.VIEW_ICON;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.test4j.plugin.database.meta.ColumnMeta;
import org.test4j.plugin.database.meta.TableMeta;
import org.test4j.plugin.database.ui.MessageView;
import org.test4j.plugin.resources.IconResources;

/**
 * 表结构model
 * 
 * @author darui.wudr
 * 
 */
public class TableNode extends BaseTreeNode {
	private TableMeta tableMeta;

	// private List<ColumnNode> primaryKeys;

	public TableNode(String tableName, TableMeta tableMeta) {
		super(tableName);
		this.tableMeta = tableMeta;
	}

	/**
	 * To check if the table the columns are loaded in memory
	 */
	public void loadColumns() {
		if (!this.expanded) {
			this.refresh();
		}
	}

	public void setExpanded(boolean b) {
		this.expanded = b;
	}

	/**
	 * 列出表结构所有的自段
	 * 
	 * @param tableModel
	 */
	public void populate() {
		try {
			List<ColumnMeta> columns = this.tableMeta.getColumns();
			for (ColumnMeta column : columns) {
				ColumnNode columnModel = new ColumnNode(column);

				this.addChild(columnModel);
			}
		} catch (Exception e) {
			MessageView.addMessage(e.getMessage());
		}
	}

	public static class ViewModel extends TableNode {
		public ViewModel(String tableName, TableMeta tableMeta) {
			super(tableName, tableMeta);
		}

		@Override
		public Image getImage() {
			return IconResources.getImage(VIEW_ICON);
		}
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.Table;
	}

	@Override
	public String buildQuery(IStructuredSelection selection) {
		String select = "SELECT * FROM " + this.getName();
		return this.getDatabaseType().limitQuery(select, "");
	}

	@Override
	public Image getImage() {
		return IconResources.getImage(TABLE_ICON);
	}

	@Override
	public String getText() {
		return this.name;
	}

	@Override
	public String getJavaName() {
		return upperFirstChar(dbNameToVarName(getName()));
	}
}