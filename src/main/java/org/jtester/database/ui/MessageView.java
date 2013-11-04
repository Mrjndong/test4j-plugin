package org.jtester.database.ui;

import java.text.DateFormat;
import java.util.Date;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

/**
 * Insert the type's description here.
 * 
 * @see ViewPart
 */
public class MessageView extends ViewPart {
	public static String ID = "org.jtester.database.ui.MessageView";
	private static MessageView instance;

	private ListViewer listView;

	public MessageView() {
		instance = this;
	}

	public static synchronized MessageView getInstance() {
		if (instance == null) {
			instance = new MessageView();
		}
		return instance;
	}

	public void clearMessages() {
		this.listView.setInput(null);
	}

	/**
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite parent) {
		this.listView = new ListViewer(parent, SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);
		this.listView.setContentProvider(new ArrayContentProvider());

		createContextMenu();
		createToolbarButtons();
	}

	private void createContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager m) {
				menuMgr.add(new Separator("edit"));
				menuMgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});
		Menu menu = menuMgr.createContextMenu(listView.getControl());
		listView.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, listView);
	}

	private void createToolbarButtons() {
		IToolBarManager toolBarMgr = getViewSite().getActionBars().getToolBarManager();
		toolBarMgr.add(new GroupMarker("edit"));
	}

	private void addMessageItem(String message) {
		String time = DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date());
		message = time + " " + message;

		this.listView.add(message);
	}

	public static void addMessage(String message) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(ID, null, IWorkbenchPage.VIEW_CREATE);
		} catch (PartInitException e) {
			throw new RuntimeException(e);
		}
		MessageView.getInstance().addMessageItem(message);
	}

	public static void addMessage(Throwable e) {
		if (e != null) {
			addMessage(e.getClass().getName() + " : " + e.getMessage());
		} else {
			addMessage("throwable is null");
		}
	}

	/**
	 * @see ViewPart#setFocus
	 */
	public void setFocus() {
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		this.listView = null;
	}
}