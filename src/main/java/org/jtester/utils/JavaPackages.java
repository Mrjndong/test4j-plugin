package org.jtester.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

/**
 * java 工程中所有的packages工具类
 * 
 * @author darui.wudr
 * 
 */
public class JavaPackages {// implements IResourceChangeListener
	private static Map<IJavaProject, List<String>> packages = new HashMap<IJavaProject, List<String>>();

	public static List<String> getPackages(IJavaProject project) {
		List<String> list = packages.get(project);
		if (list == null) {
			try {
				list = loadPackages(project);
				packages.put(project, list);
			} catch (JavaModelException e) {
				throw new RuntimeException(e);
			}
		}
		return list;
	}

	private static synchronized List<String> loadPackages(final IJavaProject project) throws JavaModelException {
		List<String> list = new ArrayList<String>();
		IPackageFragment[] packageFragments = project.getPackageFragments();
		for (IPackageFragment _package : packageFragments) {
			if (!_package.isDefaultPackage()) {
				list.add(_package.getElementName());
			}
		}
		return list;
	}
}
