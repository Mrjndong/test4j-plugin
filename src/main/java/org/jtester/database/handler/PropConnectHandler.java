package org.jtester.database.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jtester.JTesterActivator;
import org.jtester.database.ui.wizard.DatabaseWizard;

/**
 * 从资源文件创建数据库连接
 * 
 * @author darui.wudr
 * 
 */
public class PropConnectHandler extends AbstractHandler {
	private FileDialog propDialog;

	public PropConnectHandler() {
		propDialog = new FileDialog(JTesterActivator.getActiveWorkbenchShell(), SWT.OPEN);
		propDialog.setFilterExtensions(new String[] { "*.properties", "*.*" });
		propDialog.setFilterNames(new String[] { "Properties Files (*.properties)", "All Files (*.*)" });
	}

	public Object execute(ExecutionEvent event) throws ExecutionException {
		String filename = propDialog.open();
		DatabaseWizard.newDatabaseWizard(filename);
		return null;
	}
}
