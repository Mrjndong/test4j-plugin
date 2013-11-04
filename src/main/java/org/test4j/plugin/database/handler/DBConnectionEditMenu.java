package org.test4j.plugin.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.meta.DatabaseMeta;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.DatabaseNode;
import org.test4j.plugin.database.ui.wizard.DatabaseWizard;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.resources.IconResources;
import org.test4j.plugin.resources.PluginMessages;

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
