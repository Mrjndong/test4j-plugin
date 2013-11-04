package org.jtester.plugin.helper;

import static org.jtester.plugin.helper.PluginSetting.PLUGIN_ID;

import java.util.ArrayList;
import java.util.List;

import org.jtester.JTesterActivator;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class PluginLogger {
	public static void log(String title, String message, Throwable e) {
		log(message, e);
		log(title, message);
	}

	public static void log(String message) {
		log(IStatus.OK, message, null);
	}

	public static void log(String message, int level) {
		log(level, message, null);
	}

	public static void log(String message, Throwable e) {
		if (message == null) {
			message = (e.getLocalizedMessage() != null) ? e
					.getLocalizedMessage() : e.getClass().getName();
		}
		log(IStatus.ERROR, message, e);
	}

	public static void log(Throwable e) {
		log(IStatus.ERROR, "Internal error", e);
	}

	private static ILog pluginLog = null;

	public static void log(int severity, String message) {
		log(severity, message, null);
	}

	public static void log(int severity, String message, Throwable exception) {
		Status status = new Status(severity, PluginSetting.PLUGIN_ID, 0,
				message, exception);
		if (pluginLog == null) {
			pluginLog = JTesterActivator.getDefault().getLog();
		}
		pluginLog.log(status);
	}

	public static void log(String title, String message) {
		try {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getShell();
			MessageDialog.openError(shell, title, message);
		} catch (RuntimeException e) {
			log(e.getLocalizedMessage(), e);
		}
	}

	public static void log(IStatus status) {
		JTesterActivator.getDefault().getLog().log(status);
	}

	public static void log(List<Throwable> errors) {
		if (errors.isEmpty()) {
			return;
		}
		IStatus errorStatus = null;
		if (errors.size() == 1) {
			errorStatus = new Status(IStatus.ERROR, PLUGIN_ID,
					"Error updating markers", errors.get(0));
		} else {
			List<IStatus> children = new ArrayList<IStatus>();
			for (Throwable error : errors) {
				IStatus status = new Status(IStatus.ERROR, PLUGIN_ID,
						"Error updating markers", error);
				children.add(status);
			}
			errorStatus = new MultiStatus(PLUGIN_ID, IStatus.OK,
					children.toArray(new IStatus[0]),
					"Errors updating markers", new Exception());
		}
		log(errorStatus);
	}

	public static void openInformation(String message) {
		MessageDialog.openInformation(JTesterActivator.getActiveWorkbenchShell(),
				"jtester plugin", message);
	}

	public static void openError(String message) {
		MessageDialog.openError(JTesterActivator.getActiveWorkbenchShell(), "jtester plugin",
				message);
	}

	public static void openError(Throwable e) {
		log(e);
		String message = "exception is null";
		if (e != null) {
			message = e.getClass().getName() + ":" + e.getLocalizedMessage();
		}
		MessageDialog.openError(JTesterActivator.getActiveWorkbenchShell(), "jtester plugin",
				message);
	}

	public static void openWarning(String message) {
		MessageDialog.openWarning(JTesterActivator.getActiveWorkbenchShell(), "jtester plugin",
				message);
	}

}
