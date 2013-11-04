package org.jtester.database.ui.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;
import org.jtester.database.meta.DatabaseType;

/**
 * 数据库连接视图树状节点基类
 * 
 * @author darui.wudr
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class BaseTreeNode implements IAdaptable {
	protected String name;
	protected List<BaseTreeNode> children = new ArrayList<BaseTreeNode>();
	protected BaseTreeNode parent;

	protected boolean expanded;

	public BaseTreeNode(String name) {
		this.name = name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public BaseTreeNode getParent() {
		return parent;
	}

	public Object getAdapter(Class key) {
		return null;
	}

	public final void addChild(BaseTreeNode child) {
		children.add(child);
		child.parent = this;
	}

	public final void removeChild(BaseTreeNode child) {
		children.remove(child);
		child.parent = null;
	}

	/**
	 * 刷新树结构
	 * 
	 * @param databaseModel
	 * @param refresh
	 */
	public final void refresh() {
		// If expanded once and no refresh, don't repopulate
		InvisibleNode irm = getInvisibleRootModel();
		if (irm == null) {
			return;
		}
		clear();
		populate();
		this.expanded = true;
	}

	/**
	 * 清除子节点
	 */
	public void clear() {
		if (children != null) {
			children.clear();// children = new ArrayList<Model>();
		}
	}

	public final BaseTreeNode[] getChildren() {
		if (children == null) {
			return new BaseTreeNode[0];
		} else {
			return (BaseTreeNode[]) children.toArray(new BaseTreeNode[children.size()]);
		}
	}

	public BaseTreeNode getChildByName(String name) {
		BaseTreeNode[] children = getChildren();
		for (int i = 0; i < children.length; i++) {
			if (children[i].getName().equalsIgnoreCase(name)) {
				return children[i];
			}
		}
		return null;
	}

	public List<BaseTreeNode> getChildrenList() {
		return children;
	}

	public boolean hasChildren() {
		return children.size() > 0;
	}

	public String toString() {
		return getName();
	}

	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * Returns the type.
	 * 
	 * @return int
	 */
	public abstract NodeType getNodeType();

	public DatabaseType getDatabaseType() {
		DatabaseNode connModel = this.getRootDatabaseModel();
		DatabaseType databaseType = connModel.getDatabaseMeta().getDatabaseType();

		return databaseType;
	}

	/**
	 * 返回树中根节点ConnectionModel
	 * 
	 * @param model
	 * @return
	 */
	public DatabaseNode getRootDatabaseModel() {
		BaseTreeNode model = this;
		while (!(model instanceof DatabaseNode) && model != null) {
			model = model.getParent();
		}
		return (DatabaseNode) model;
	}

	/**
	 * 返回树形控件的的根节点
	 * 
	 * @param model
	 * @return
	 */
	public InvisibleNode getInvisibleRootModel() {
		BaseTreeNode[] children = this.getChildren();
		if (children.length == 1 && (children[0] instanceof InvisibleNode)) {
			InvisibleNode irm = (InvisibleNode) children[0];
			return irm;
		} else {
			return null;
		}
	}

	/**
	 * 根据选中的内容构造select语句<br>
	 * 同时根据databaseType加上限定
	 * 
	 * @return
	 */
	public abstract String buildQuery(IStructuredSelection selection);

	public abstract void populate();

	/**
	 * 返回当前节点的图标
	 * 
	 * @return
	 */
	public abstract Image getImage();

	/**
	 * 返回当前节点的名称
	 * 
	 * @return
	 */
	public abstract String getText();

	/**
	 * 返回当前节点的java名称
	 * 
	 * @return
	 */
	public abstract String getJavaName();

	/**
	 * Converts a database name to a Java variable name.
	 */
	public static String dbNameToVarName(String columnName) {
		if (columnName == null)
			return null;

		StringBuilder fieldName = new StringBuilder(columnName.length());

		boolean toUpper = false;
		for (int i = 0; i < columnName.length(); i++) {
			char ch = columnName.charAt(i);
			if (ch == '_') {
				toUpper = true;
			} else if (toUpper) {
				fieldName.append(Character.toUpperCase(ch));
				toUpper = false;
			} else {
				fieldName.append(Character.toLowerCase(ch));
			}
		}

		return fieldName.toString();
	}

	/**
	 * Changes the first letter of the passed String to upper case.
	 */
	public static String upperFirstChar(String string) {
		if (string == null)
			return null;
		if (string.length() <= 1)
			return string.toLowerCase();
		StringBuilder sb = new StringBuilder(string);

		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}

	/**
	 * Changes the first letter of the passed String to upper case.
	 */
	public static String lowerFirstChar(String string) {
		if (string == null)
			return null;
		if (string.length() <= 1)
			return string.toLowerCase();
		StringBuilder sb = new StringBuilder(string);

		sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
		return sb.toString();
	}
}
