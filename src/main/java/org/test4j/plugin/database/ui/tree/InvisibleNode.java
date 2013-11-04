package org.test4j.plugin.database.ui.tree;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author darui.wudr
 * 
 */
public class InvisibleNode extends BaseTreeNode {

	public InvisibleNode() {
		super("InvisibleModel");
	}

	public InvisibleNode(String s) {
		super(s);
	}

	@Override
	public NodeType getNodeType() {
		return NodeType.Invisible;
	}

	@Override
	public String buildQuery(IStructuredSelection selection) {
		throw new RuntimeException("can't be triggered");
	}

	@Override
	public Image getImage() {
		return null;
	}

	@Override
	public String getText() {
		return this.name;
	}

	@Override
	public void populate() {
		// do nothing
	}

	@Override
	public String getJavaName() {
		return upperFirstChar(dbNameToVarName(getName()));
	}
}
