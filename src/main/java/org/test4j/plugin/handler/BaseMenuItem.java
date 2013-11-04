package org.test4j.plugin.handler;

import java.util.Collections;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.handlers.IHandlerService;
import org.test4j.plugin.helper.PluginLogger;
import org.test4j.plugin.resources.IconResources;

public abstract class BaseMenuItem extends ContributionItem {
	protected final IWorkbenchPartSite workbenchPartSite;
	protected IHandler handler;

	protected BaseMenuItem(IWorkbenchPartSite workbenchPartSite) {
		this.workbenchPartSite = workbenchPartSite;
		this.handler = newHandler();
	}

	private MenuItem menuItem;

	/**
	 * 填充菜单
	 */
	@Override
	public void fill(Menu menu, int index) {
		this.menuItem = new MenuItem(menu, SWT.NONE, index);
		menuItem.setText(this.getMenuText());
		menuItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				runExecutionEvent();
			}
		});

		String icon = this.getMenuIcon();
		Image image = IconResources.getImage(icon);
		if (menuItem != null) {
			menuItem.setImage(image);
			menuItem.setEnabled(true);
		}
	}

	/**
	 * 触发菜单命令的执行入口<br>
	 * 一般是由菜单 SelectionListener 触发
	 */
	private void runExecutionEvent() {
		try {
			final IHandlerService handlerService = (IHandlerService) workbenchPartSite
					.getService(IHandlerService.class);
			IEvaluationContext evaluationContext = handlerService.createContextSnapshot(true);
			ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null, evaluationContext);
			handler.execute(event);
		} catch (ExecutionException e) {
			PluginLogger.log(e);
		}
	}

	/**
	 * 菜单命令执行调用点
	 * 
	 * @param event
	 */
	protected abstract void executeHandler(ExecutionEvent event);

	/**
	 * 返回菜单命令的文本名称
	 * 
	 * @return
	 */
	protected abstract String getMenuText();

	/**
	 * 返回菜单命令的图标名称
	 * 
	 * @return
	 */
	protected abstract String getMenuIcon();

	/**
	 * 创建对应的菜单操作handler处理器
	 * 
	 * @return
	 */
	private AbstractHandler newHandler() {
		AbstractHandler handler = new AbstractHandler() {
			public Object execute(ExecutionEvent event) throws ExecutionException {
				executeHandler(event);
				return null;
			}
		};
		return handler;
	}
}