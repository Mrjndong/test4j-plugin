package org.jtester.plugin.preference;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TestTreeContentProvider implements ITreeContentProvider {
	private final TestTreeNode root;

	public TestTreeContentProvider(final TestTreeNode root) {
		this.root = root;
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	public Object[] getChildren(Object parentElement) {
		TestTreeNode element = (TestTreeNode) parentElement;
		return element.getChildren();
	}

	public Object getParent(Object child) {
		TestTreeNode element = (TestTreeNode) child;
		return element.getParent();
	}

	public boolean hasChildren(Object parent) {
		TestTreeNode element = (TestTreeNode) parent;
		return element.hasChildren();
	}

	public Object[] getElements(Object inputElement) {
		return this.root.getChildren();
	}
}
