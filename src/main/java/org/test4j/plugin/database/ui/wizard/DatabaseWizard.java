package org.test4j.plugin.database.ui.wizard;

import java.io.File;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.test4j.plugin.Test4JActivator;
import org.test4j.plugin.database.meta.DatabaseMeta;
import org.test4j.plugin.helper.PluginLogger;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 数据库连接向导
 * 
 * @author darui.wudr
 * 
 */
public class DatabaseWizard extends Wizard {
	DatabaseWizardPage databasePage;
	private DatabaseMeta databaseMeta;
	private boolean isEdit = false;

	public DatabaseWizard(DatabaseMeta databaseMeta, boolean isEdit) {
		super.setWindowTitle(isEdit ? PluginMessages.DB_CONNECTION_EDIT : PluginMessages.DB_CONNECTION_NEW);
		this.databaseMeta = databaseMeta;
		this.isEdit = isEdit;
	}

	public DatabaseWizard() {
		super.setWindowTitle(PluginMessages.DB_CONNECTION_NEW);
		this.databaseMeta = new DatabaseMeta();
		this.isEdit = false;
	}

	public boolean performFinish() {
		return databasePage.performFinish();
	}

	@Override
	public void addPages() {
		this.databasePage = new DatabaseWizardPage(PluginMessages.DB_CONNECTION_EDIT, this.databaseMeta, this.isEdit);
		super.addPage(databasePage);
	}

	/**
	 * 根据properties文件中的配置项打开数据库连接向导
	 * 
	 * @param filename
	 */
	public static void newDatabaseWizard(String filename) {
		File file = new File(filename);
		if (file.exists() == false) {
			PluginLogger.openInformation("file " + filename + " not found!");
			return;
		}
		try {
			DatabaseMeta databaseMeta = new DatabaseMeta(filename);
			DatabaseWizard wizard = new DatabaseWizard(databaseMeta, false);

			WizardDialog dialog = new WizardDialog(Test4JActivator.getActiveWorkbenchShell(), wizard);
			dialog.open();
		} catch (Exception e) {
			PluginLogger.log(e);
			PluginLogger.openError(e);
		}
	}
}
