package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.wizard.DatabaseWizard;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 新建数据库连接
 * 
 * @author darui.wudr
 * 
 */
public class DBConnectionNewMenu extends BaseMenuItem {
	public DBConnectionNewMenu(final IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseWizard wizard = new DatabaseWizard();
		WizardDialog dialog = new WizardDialog(this.workbenchPartSite.getShell(), wizard);
		dialog.open();
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_CONNECTION_NEW;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.DISCONNECTED_ICON;
	}
}
