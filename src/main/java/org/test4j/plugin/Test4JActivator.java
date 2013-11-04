package org.test4j.plugin;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.test4j.plugin.helper.PluginLogger;

public class Test4JActivator extends AbstractUIPlugin {

    private static Test4JActivator plugin;

    private String                 bundleDirectory;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        PluginLogger.log(IStatus.INFO, "starting " + this.getBundle().toString());
        this.getPreferenceStore().setDefault("testByDefault", true);

        this.bundleDirectory = FileLocator.toFileURL(context.getBundle().getEntry("/")).getPath();

        // IconResources.registerIcon(getImageRegistry());
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        super.stop(context);
        plugin = null;
    }

    public static Test4JActivator getDefault() {
        return plugin;
    }

    public static IPreferenceStore preference() {
        return plugin.getPreferenceStore();
    }

    public String pluginBundleDirectory() {
        return bundleDirectory;
    }

    public static Shell getActiveWorkbenchShell() {
        IWorkbenchWindow workBenchWindow = getActiveWorkbenchWindow();
        if (workBenchWindow == null)
            return null;
        return workBenchWindow.getShell();
    }

    public static IWorkbenchWindow getActiveWorkbenchWindow() {
        if (plugin == null)
            return null;
        IWorkbench workBench = plugin.getWorkbench();
        if (workBench == null)
            return null;
        return workBench.getActiveWorkbenchWindow();
    }
}
