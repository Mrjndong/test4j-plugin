package org.test4j.plugin.helper;

import java.net.URL;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.test4j.plugin.Test4JActivator;

public class PluginHelper {
	public static String NEWLINE = System.getProperty("line.separator");

	/**
	 * 返回图标的url
	 * 
	 * @param name
	 * @return
	 */
	public static URL iconURL(String name) {
		return Test4JActivator.getDefault().getBundle().getEntry("icons/" + name);
	}

	/**
	 * bundle的路径
	 * 
	 * @return
	 */
	public static String pluginBundleDirectory() {
		return Test4JActivator.getDefault().pluginBundleDirectory();
	}

	/**
	 * 返回颜色值
	 * 
	 * @param color
	 * @return
	 */
	public static Color getColor(int color) {
		return Display.getCurrent().getSystemColor(color);
	}

	/**
	 * 列出所有扩展点(仅用于编程时咨询)
	 */
	public static void printAllExtendsionPoint() {
		IExtensionPoint[] points = Platform.getExtensionRegistry().getExtensionPoints();
		for (IExtensionPoint point : points) {
			System.out.println(point.getUniqueIdentifier());
		}
	}

	/**
	 * 包文本message存到黏贴板中
	 * 
	 * @param message
	 */
	public static void copyToClipBoard(String message) {
		Display display = Test4JActivator.getActiveWorkbenchShell().getDisplay();
		Clipboard cb = new Clipboard(display);
		TextTransfer textTransfer = TextTransfer.getInstance();
		cb.setContents(new Object[] { message }, new Transfer[] { textTransfer });
	}

	/**
	 * 为视图创建菜单和工具条
	 * 
	 * @param site
	 * @param toolbarAction
	 * @param menuAction
	 */
	public static void createMenuAndToolBar(IViewSite site, Action[] toolbarAction, Action[] menuAction) {
		IActionBars bars = site.getActionBars();
		IToolBarManager toolbarManager = bars.getToolBarManager();
		IMenuManager menuManager = bars.getMenuManager();
		boolean first = true;
		for (Action action : toolbarAction) {
			toolbarManager.add(action);
			if (first) {
				first = false;
			} else {
				menuManager.add(new Separator());
			}
			menuManager.add(action);
		}
		for (Action action : menuAction) {
			if (first) {
				first = false;
			} else {
				menuManager.add(new Separator());
			}
			menuManager.add(action);
		}
	}

	/**
	 * 打开视图或者设置焦点到视图
	 * 
	 * @param viewId
	 */
	public static void showView(String viewId) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (page == null) {
			return;
		}
		try {
			page.showView(viewId);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public final static String JAVA_NATURE = "org.eclipse.jdt.core.javanature";

	/**
	 * 是否是java project
	 * 
	 * @param project
	 * @return
	 * @throws CoreException
	 */
	public static boolean isJavaProject(IProject project) throws CoreException {
		return project.hasNature(JAVA_NATURE);
	}
}
