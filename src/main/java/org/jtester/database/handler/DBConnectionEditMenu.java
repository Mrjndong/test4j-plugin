package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewSite;
import org.jtester.database.meta.DatabaseMeta;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.database.ui.wizard.DatabaseWizard;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;
import org.jtester.plugin.resources.PluginMessages;

public class DBConnectionEditMenu extends BaseMenuItem {

	public DBConnectionEditMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		DatabaseNode databaseNode = (DatabaseNode) DatabaseView.getTreeViewer().getSelectedModel();
		DatabaseMeta databaseMeta = databaseNode.getDatabaseMeta();
		DatabaseWizard wizard = new DatabaseWizard(databaseMeta, true);

		WizardDialog dialog = new WizardDialog(this.workbenchPartSite.getShell(), wizard);
		dialog.open();
	}

	@Override
	protected String getMenuText() {
		return PluginMessages.DB_CONNECTION_EDIT;
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.DISCONNECTED_ICON;
	}
}
