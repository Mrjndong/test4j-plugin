package org.jtester.plugin;

import static org.jtester.plugin.helper.PluginSetting.SHOW_BROWSER_IN_EDITOR_WHEN_OPENING;

import java.io.IOException;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.jtester.JTesterActivator;
import org.jtester.ibatis.assistor.VelocityUtil;
import org.jtester.plugin.helper.PluginSetting;

public class JTesterPreferenceInitializer extends AbstractPreferenceInitializer {
	private IPreferenceStore store;

	public JTesterPreferenceInitializer() {
		this.store = JTesterActivator.preference();
	}

	public void initializeDefaultPreferences() {
		store.setDefault(PluginSetting.DATABASE_URL, "jdbc:mysql://127.0.0.1/jtester?characterEncoding=UTF8");
		store.setDefault(PluginSetting.DATABASE_USR, "root");
		store.setDefault(PluginSetting.DATABASE_USR, "password");

		PreferenceConverter.setDefault(this.store, AbstractTextEditor_Color_Background, Display.getDefault()
				.getSystemColor(SWT.COLOR_LIST_BACKGROUND).getRGB());
		this.store.setDefault(AbstractTextEditor_Color_Background_SystemDefault, true);

		setDefault(this.store, URL, URL_DEFAULT_COLOUR, STYLE_NORMAL);
		setDefault(this.store, DBFIT_RESOURCE, DBFIT_RESOURCE_DEFAULT_COLOUR, STYLE_BOLD);
		setDefault(this.store, JAVA_TYPE, JAVA_TYPE_DEFAULT_COLOUR, STYLE_NORMAL);
		setDefault(this.store, OTHER, OTHER_DEFAULT_COLOUR, STYLE_NORMAL);

		this.store.setDefault("WordWrap", false);
		this.store.setDefault(SHOW_BROWSER_IN_EDITOR_WHEN_OPENING, false);
		this.store.setDefault("renderFullyQualifiedTypeNames", true);
		
		store.setDefault(PluginSetting.DOMAIN_SQLMAP_ENCONDING, CommonConstants.UTF8_ENCODING);
        store.setDefault(PluginSetting.DOMAIN_ENTITY_ENCONDING, CommonConstants.GBK_ENCODING);
		try {
            store.setDefault(PluginSetting.DOMAIN_SQLMAP_TEMPLATE, VelocityUtil.readTemplate(CommonConstants.DOMAIN_SQLMAP_TEMPLATE));
            store.setDefault(PluginSetting.DOMAIN_ENTITY_TEMPLATE, VelocityUtil.readTemplate(CommonConstants.DOMAIN_ENTITY_TEMPLATE));
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

	private void setDefault(IPreferenceStore store, String constant, String color, String style) {
		store.setDefault(constant + "_foreground", color);
		store.setDefault(constant + "_style", style);
	}

	public final static String AbstractTextEditor_Color_Background_SystemDefault = "AbstractTextEditor.Color.Background.SystemDefault";
	public final static String AbstractTextEditor_Color_Background = "AbstractTextEditor.Color.Background";

	public static final String URL = "url";
	public static final String DBFIT_RESOURCE = "dbfitResource";
	public static final String JAVA_TYPE = "javaType";
	public static final String OTHER = "other";

	public static final String OTHER_DEFAULT_COLOUR = "0,0,0";
	public static final String DBFIT_RESOURCE_DEFAULT_COLOUR = "192,128,32";
	public static final String JAVA_TYPE_DEFAULT_COLOUR = "128,0,128";
	public static final String URL_DEFAULT_COLOUR = "0,0,255";
	// public static final String WIKI_URL_DEFAULT_COLOUR = "98,0,175";
	// public static final String NEW_WIKI_NAME_DEFAULT_COLOUR = "255,140,255";
	// public static final String WIKI_NAME_DEFAULT_COLOUR = "0,140,255";
	public static final String STYLE_NORMAL = "normal";
	public static final String STYLE_BOLD = "bold";
}