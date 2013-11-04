package org.jtester.jspec.assistor;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class JSpecInfoDialog {
	public void show(String title, String message) {
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		MessageDialog.openInformation(shell, title, message);
	}
}