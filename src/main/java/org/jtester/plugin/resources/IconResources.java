package org.jtester.plugin.resources;

import static org.jtester.plugin.helper.PluginSetting.PLUGIN_ID;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * plugin用到的图标工具类
 * 
 * @author darui.wudr
 * 
 */
public class IconResources {
	public static String HR_ICON = "icons/hr.jpg";
	public static String WIKI_ICON = "icons/wiki.png";
	public static String RESOUCE_PERSPECT_ICON = "icons/resource_persp.gif";
	public static String LINK_ICON = "icons/links_view.gif";
	public static String PACKAGE_ICON = "icons/package_obj.gif";
	public static String CLAZZ_ICON = "icons/class_obj.gif";
	public static String INTERFACE_ICON = "icons/int_obj.gif";

	public static String CONNECTED_ICON = "icons/connected_icon.gif";
	public static String SELECTED_CONNECTED_ICON = "icons/selected_connected_icon.gif";
	public static String DISCONNECTED_ICON = "icons/disconnected_icon.gif";
	public static String EXECUTE_ICON = "icons/execute_icon.gif";
	public static String REMOVE_ICON = "icons/remove_icon.gif";
	public static String DATABASE_ICON = "icons/database_icon.gif";
	public static String SELECTED_DATABASE_ICON = "icons/selected_database_icon.gif";
	public static String TABLE_ICON = "icons/table_icon.gif";
	public static String VIEW_ICON = "icons/view_icon.gif";
	public static String COLUMN_ICON = "icons/column_icon.gif";
	public static String REFRESH_ICON = "icons/refresh.gif";
	public static String RUN_ICON = "icons/run.gif";
	public static String SQLEDITOR_ICON = "icons/sqleditor.gif";
	public static String PK_ICON = "icons/pk.gif";
	public static String FK_ICON = "icons/fk.gif";
	public static String INSERT_ICON = "icons/insert.gif";
	public static String COPY_ICON = "icons/copy.gif";
	public static String COPY_INSERT_DATA = "icons/data_insert.gif";
	public static String COPY_CHECK_DATA = "icons/data_check.gif";
	public static String IMPORT_ICON = "icons/import.gif";
	public static String EXPORT_ICON = "icons/export.gif";

	public static String CONVERT_TO_JAVA = "icons/android.png";

	public static final String[] PLUGIN_ICONS = { HR_ICON, WIKI_ICON, RESOUCE_PERSPECT_ICON, // <br>
			LINK_ICON, PACKAGE_ICON, CLAZZ_ICON, INTERFACE_ICON, CONNECTED_ICON, /** <br> */
			SELECTED_CONNECTED_ICON, DISCONNECTED_ICON, EXECUTE_ICON, REMOVE_ICON, DATABASE_ICON, /** <br> */
			SELECTED_DATABASE_ICON, TABLE_ICON, VIEW_ICON, COLUMN_ICON, REFRESH_ICON, /** <br> */
			RUN_ICON, SQLEDITOR_ICON, PK_ICON, FK_ICON, INSERT_ICON, /** <br> */
			COPY_ICON, IMPORT_ICON, EXPORT_ICON, /** <br> */
			COPY_INSERT_DATA, COPY_CHECK_DATA, CONVERT_TO_JAVA /** <br> */
	};

	public final static Map<String, ImageDescriptor> imageDescriptons = new HashMap<String, ImageDescriptor>();
	/**
	 * 注册系统要用到的图标
	 */
	static {
		for (String icon : PLUGIN_ICONS) {
			ImageDescriptor descriptor = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, icon);
			imageDescriptons.put(icon, descriptor);
		}
	}

	public static Image getImage(String image) {
		ImageDescriptor descriptor = imageDescriptons.get(image);
		if (descriptor != null) {
			return descriptor.createImage();
		} else {
			return null;
		}
	}

	public static ImageDescriptor getImageDescriptor(String image) {
		ImageDescriptor descriptor = imageDescriptons.get(image);
		return descriptor;
	}
}
