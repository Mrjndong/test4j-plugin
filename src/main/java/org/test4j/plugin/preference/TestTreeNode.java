package org.test4j.plugin.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;

public class TestTreeNode {
	private IJavaElement element;
	private List<TestTreeNode> children = new ArrayList<TestTreeNode>();
	private TestTreeNode parent = null;
	private String name;
	private boolean real;

	public TestTreeNode() {
	}

	private TestTreeNode(IJavaElement paramIJavaElement) {
		this.element = paramIJavaElement;
		this.real = true;
		this.name = paramIJavaElement.getPath().removeFirstSegments(1)
				.toString();
	}

	private TestTreeNode(IJavaElement paramIJavaElement, TestTreeNode parent) {
		this(paramIJavaElement);
		this.parent = parent;
	}

	public TestTreeNode(IJavaElement paramIJavaElement, String paramString,
			boolean paramBoolean) {
		this.element = paramIJavaElement;
		this.name = paramString;
		this.real = paramBoolean;
	}

	/**
	 * 新增一个子节点，并返回这个子节点,real=true<br>
	 * 如果子节点已经存在，则返回已经存在的子节点
	 * 
	 * @param element
	 * @return
	 */
	public TestTreeNode add(IJavaElement element) {
		for (TestTreeNode treeNode : this.children) {
			if (treeNode.element.equals(element)) {
				return treeNode;
			}
		}
		TestTreeNode result = new TestTreeNode(element, this);
		this.children.add(result);
		return result;
	}

	/**
	 * 新增一个子节点，并返回这个子节点<br>
	 * 如果子节点已经存在，则更新子节点的信息并返回这个节点
	 * 
	 * @param element
	 * @param name
	 * @param real
	 * @return
	 */
	public TestTreeNode add(IJavaElement element, String name, boolean real) {
		for (TestTreeNode treenode : this.children) {
			if (name == null || name.equals(treenode.name) == false) {
				continue;
			}
			treenode.real = real || treenode.real;
			if (treenode.element == null) {
				treenode.element = element;
			}
			if (treenode.parent == null) {
				treenode.parent = this;
			}
			return treenode;
		}

		TestTreeNode result = new TestTreeNode(element, name, real);
		result.parent = this;
		this.children.add(result);
		return result;
	}

	/**
	 * 增加一串子节点，并返回最后一个新增的子节点
	 * 
	 * @param element
	 * @param segments
	 * @param real
	 * @return
	 */
	public TestTreeNode addToTree(final IPackageFragmentRoot root,
			final IJavaElement element, final boolean real) {
		String packageName = element.getElementName();
		if (packageName.contains(".") == false) {
			TestTreeNode rootNode = this.add(root);
			return rootNode.add(element, packageName, real);
		} else {
			String subPackName = packageName.substring(0,
					packageName.lastIndexOf('.'));
			IJavaElement parentPackage = root.getPackageFragment(subPackName);
			TestTreeNode parentNode = addToTree(root, parentPackage, false);
			String nodeName = packageName.replaceAll("[^\\.]*\\.", "");
			return parentNode.add(element, nodeName, real);
		}
	}

	public Object[] getChildren() {
		return this.children.toArray();
	}

	public Object getParent() {
		return this.parent;
	}

	public boolean hasChildren() {
		return !this.children.isEmpty();
	}

	/**
	 * 清理没有测试类的节点
	 * 
	 * @return
	 */
	public TestTreeNode clearPackages() {
		if (this.isRoot() || this.moreThenOneNode()) {
			List<TestTreeNode> newChildren = new ArrayList<TestTreeNode>();
			for (TestTreeNode child : this.children) {
				newChildren.add(child.clearPackages());
			}
			this.children = newChildren;
			return this;
		} else if (this.children.size() == 1) {
			TestTreeNode child = this.children.iterator().next();
			child.name = this.name + "." + child.name;
			return child.clearPackages();
		} else {
			return this;
		}
	}

	private boolean isRoot() {
		return this.parent == null;
	}

	private boolean moreThenOneNode() {
		return this.real || this.children.size() > 1;
	}

	/**
	 * 检出树中所有的节点
	 * 
	 * @return
	 */
	public List<TestTreeNode> getNodes() {
		List<TestTreeNode> results = getDescendents(this);
		results.add(this);
		return results;
	}

	/**
	 * 检出指定节点的所有子孙节点(不包含自身)
	 * 
	 * @param node
	 * @return
	 */
	public final static List<TestTreeNode> getDescendents(TestTreeNode node) {
		List<TestTreeNode> nodes = new ArrayList<TestTreeNode>();
		for (TestTreeNode child : node.children) {
			nodes.add(child);
			nodes.addAll(getDescendents(child));
		}
		return nodes;
	}

	public IJavaElement getElement() {
		return element;
	}

	public String getElementName() {
		return this.element.getClass().getName();
	}

	public String getName() {
		return name;
	}

	/**
	 * 创建项目project测试类的package继承树
	 * 
	 * @param project
	 * @return
	 */
	public static TestTreeNode createTestTree(IJavaProject project) {
		TestTreeNode tree = new TestTreeNode();
		// Set<IType> classes = TestFinder.findAllTest(project, null);
		// for (IType test : classes) {
		// IPackageFragment pack = (IPackageFragment)
		// test.getAncestor(IJavaElement.PACKAGE_FRAGMENT);
		// IPackageFragmentRoot root = (IPackageFragmentRoot)
		// test.getAncestor(IJavaElement.PACKAGE_FRAGMENT_ROOT);
		// tree.addToTree(root, pack, true);
		// }
		// tree.clearPackages();
		return tree;
	}
}
