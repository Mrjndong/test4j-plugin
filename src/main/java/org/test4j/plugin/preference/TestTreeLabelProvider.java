package org.test4j.plugin.preference;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.test4j.plugin.helper.PluginHelper;
import org.eclipse.jface.viewers.LabelProvider;

public class TestTreeLabelProvider extends LabelProvider {
	private Map<ImageDescriptor, Image> imageCache;

	public TestTreeLabelProvider() {
		this.imageCache = new HashMap<ImageDescriptor, Image>();
	}

	public String getText(Object object) {
		TestTreeNode node = (TestTreeNode) object;
		IJavaElement element = node.getElement();
		if (element instanceof IPackageFragmentRoot) {
			IPackageFragmentRoot root = (IPackageFragmentRoot) element;
			return root.getPath().removeFirstSegments(1).toString();
		}
		return node.getName() == null || node.getName().length() == 0 ? "(default package)" : node.getName();
	}

	public Image getImage(Object element) {
		URL url;
		TestTreeNode node = (TestTreeNode) element;
		ImageDescriptor descriptor = null;
		if (node.getElement() instanceof IPackageFragmentRoot) {
			url = PluginHelper.iconURL("packagefolder_obj.gif");
			descriptor = ImageDescriptor.createFromURL(url);
		} else if (node.getElement() instanceof IPackageFragment) {
			url = PluginHelper.iconURL("package_obj.gif");
			descriptor = ImageDescriptor.createFromURL(url);
		} else {
			String nodetype = node.getElement() == null ? "[null]" : node.getElementName();
			throw new Error("Unknown element type,node.element=" + nodetype);
		}
		Image image = (Image) this.imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			this.imageCache.put(descriptor, image);
		}
		return image;
	}

	public void dispose() {
		for (Image each : this.imageCache.values())
			each.dispose();
		this.imageCache.clear();
	}
}
