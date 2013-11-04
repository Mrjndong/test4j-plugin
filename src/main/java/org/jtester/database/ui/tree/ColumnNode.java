package org.jtester.database.ui.tree;

import static org.jtester.plugin.resources.IconResources.COLUMN_ICON;
import static org.jtester.plugin.resources.IconResources.FK_ICON;
import static org.jtester.plugin.resources.IconResources.PK_ICON;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.jtester.database.meta.ColumnMeta;
import org.jtester.plugin.resources.IconResources;

/**
 * 字段信息
 * 
 * @author darui.wudr
 * 
 */
public class ColumnNode extends BaseTreeNode {
	private ColumnMeta columnMeta;

	public ColumnNode(ColumnMeta columnMeta) {
		super(columnMeta.getColumnName());
		this.columnMeta = columnMeta;
	}

	public ColumnMeta getColumnMeta() {
		return columnMeta;
	}

	public void setColumnMeta(ColumnMeta columnMeta) {
		this.columnMeta = columnMeta;
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.Column;
	}

	@Override
	public String buildQuery(IStructuredSelection selection) {
		Iterator<?> iter = selection.iterator();
		String first = null;
		String columns = "";
		while (iter.hasNext()) {
			columns += first == null ? "" : ",";
			columns += iter.next();
			if (first == null) {
				first = columns;
			}
		}
		String tableName = this.getParent().getName();
		String select = String.format("SELECT %s FROM %s", columns, tableName);
		String orderBy = "ORDER BY " + first;
		return this.getDatabaseType().limitQuery(select, orderBy);
	}

	@Override
	public Image getImage() {
		if (columnMeta.isPrimaryKey()) {
			return IconResources.getImage(PK_ICON);
		} else if (columnMeta.isForeignKey()) {
			return IconResources.getImage(FK_ICON);
		} else {
			return IconResources.getImage(COLUMN_ICON);
		}
	}

	@Override
	public String getText() {
		return columnMeta.getColumnName();
	}

	@Override
	public void populate() {
		// do nothing
	}
	
	@Override
	public String getJavaName() {
	    return lowerFirstChar(dbNameToVarName(columnMeta.getColumnName()));
	}
}