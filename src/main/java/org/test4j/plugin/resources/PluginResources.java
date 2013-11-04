package org.test4j.plugin.resources;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.osgi.util.NLS;

public class PluginResources {
    public static final String RESOURCE_BUNDLE = "org.test4j.plugin.resources.PluginResources";

    public PluginResources() {

    }

    public static String WIKI_TEXT_VIEWER_TITLE;
    public static String WIKI_HTML_VIEWER_TITLE;
    public static String WIKI_TEST_VIEWER_TITLE;

    static {
        NLS.initializeMessages(RESOURCE_BUNDLE, PluginResources.class);
    }

    public static String getResourceString(String key) {
        try {
            return getResourceBundle().getString(key);
        } catch (MissingResourceException localMissingResourceException) {
            return "!" + key + "!";
        }
    }

    public static ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);
        }
        return resourceBundle;
    }

    private static ResourceBundle resourceBundle = null;
}
