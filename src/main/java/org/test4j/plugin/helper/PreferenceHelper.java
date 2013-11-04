package org.test4j.plugin.helper;

import static org.test4j.plugin.Test4JPreferenceInitializer.AbstractTextEditor_Color_Background;
import static org.test4j.plugin.Test4JPreferenceInitializer.AbstractTextEditor_Color_Background_SystemDefault;
import static org.test4j.plugin.helper.PluginSetting.SHOW_BROWSER_IN_EDITOR_WHEN_OPENING;

import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.test4j.plugin.Test4JActivator;

/**
 * plugin系统属性工具类
 * 
 * @author darui.wudr
 */
public class PreferenceHelper {
    /**
     * 是否自动分行
     * 
     * @return
     */
    public static boolean wordWrap() {
        return Test4JActivator.preference().getBoolean("WordWrap");
    }

    /**
     * 是否由系统来设置wiki editor的背景色
     * 
     * @return
     */
    public static boolean isSysWikiEditBgColor() {
        return Test4JActivator.preference().getBoolean(AbstractTextEditor_Color_Background_SystemDefault);
    }

    /**
     * wiki editor的背景色
     * 
     * @return
     */
    public static RGB wikiEditorBgColor() {
        return PreferenceConverter.getColor(Test4JActivator.preference(), AbstractTextEditor_Color_Background);
    }

    /**
     * 获取文本的前景色
     * 
     * @param key 文本类型
     * @return
     */
    public static RGB textForeground(String key) {
        RGB rgb = PreferenceConverter.getColor(Test4JActivator.preference(), key + "_foreground");
        return rgb;
    }

    /**
     * 获取文本的style
     * 
     * @param key 文本类型
     * @return
     */
    public static String textStyle(String key) {
        String style = Test4JActivator.preference().getString(key + "_style");
        return style;
    }

    /**
     * 是否缺省打开html浏览器界面
     * 
     * @return
     */
    public static boolean showHtmlBrowserDefault() {
        return Test4JActivator.preference().getBoolean(SHOW_BROWSER_IN_EDITOR_WHEN_OPENING);
    }
}
