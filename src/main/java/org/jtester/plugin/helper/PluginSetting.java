package org.jtester.plugin.helper;

import org.jtester.JTesterActivator;

public class PluginSetting {
	public final static String PLUGIN_ID = "org.jtester.plugin";

	/**
	 * 数据库连接url
	 */
	public static final String DATABASE_URL = "database_url";
	/**
	 * 数据库连接用户名称
	 */
	public static final String DATABASE_USR = "database_usr";
	/**
	 * 数据库连接用户密码
	 */
	public static final String DATABASE_PWD = "database_pwd";

	public static final String SOURCE_PATH = "source_path";
	public static final String RESOURCES_PATH = "resources_path";
	public static final String TEST_SOURCE_PATH = "test_source_path";
	public static final String TEST_RESOURCES_PATH = "test_resources_path";

	public final static String JTESTER_CT_SERVER_PORT = "org.jtester.ct.port";
	public final static String TEST_CLASSNAME_ARRAY = "org.jtester.ct.classes";

	public final static String CT_LAUNCHCONFIG = "org.jtester.ct.JTesterLaunchConfigurationDelegate";

	public static final String DEFAULT_CHARSET = "UTF8";

	public static final String WORD_WRAP = "WordWrap";
	public static final String SHOW_BROWSER_IN_EDITOR_WHEN_OPENING = "ShowBrowserInEditorWhenOpening";
	public static final String WIKI_FILE_EXTENSION = ".wiki";

	public static final String EXCLUDES_FILE = "wiki.exclude";
	public static final String[] URL_PREFIXES = { "http", "https", "ftp", "sftp", "mailto", "file", "news", "gopher",
			"telnet", "afp", "nfs", "smb" };

	public static final String SUFFIX_FOREGROUND = "_foreground";
	public static final String SUFFIX_BACKGROUND = "_background";
	public static final String SUFFIX_STYLE = "_style";

	public static final String RENDER_FULLY_QUALIFIED_TYPE_NAMES = "renderFullyQualifiedTypeNames";
	public static final String WIKI_HREF = "http://--wiki/";

	static final String WIKI_WORD_PATTERN = "([A-Z][a-z]+){2,}[0-9]*";
	static final String TWIKI_WORD_PATTERN = "[A-Z]+[a-z]+[A-Z]+\\w*";
	static final String ESCAPED_TWIKI_WORD_PATTERN = "!(\\[\\[)?[A-Z]+[a-z]+[A-Z]+\\w*(\\]\\])?";

	public static final String DOMAIN_SQLMAP_TEMPLATE = "Domain.SqlmapTemplate";
	public static final String DOMAIN_ENTITY_TEMPLATE = "Domain.EntityTemplate";

	public static final String DOMAIN_SQLMAP_ENCONDING = "Domain.SqlmapEncoding";
	public static final String DOMAIN_ENTITY_ENCONDING = "Domain.EntityEncoding";

	public static final String DOMAIN_SQLMAP_PATH = "Domain.SqlmapPath";
	public static final String DOMAIN_ENTITY_PATH = "Domain.EntityPath";

	/**
	 * 获取ct plugin的extension id<br>
	 * org.jtester.ui.[extension id]
	 * 
	 * @param extension
	 * @return
	 */
	public static String id(String extension) {
		return PLUGIN_ID + "." + extension;
	}

	public static final String METHOD_NAME = id("methodName");
	public static final String CLASS_NAME = id("className");

	public static String getSourcePath() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(SOURCE_PATH);
	}

	public static String getReSourcePath() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(RESOURCES_PATH);
	}

	public static String getTestSourcePath() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(TEST_SOURCE_PATH);
	}

	public static String getTestReSourcePath() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(TEST_RESOURCES_PATH);
	}

	/**
	 * 返回存放测试序列的数据库url
	 * 
	 * @return
	 */
	public static String getDbUrl() {
		// "jdbc:mysql://localhost/jtester-tutorial?characterEncoding=UTF8";
		return JTesterActivator.getDefault().getPreferenceStore().getString(DATABASE_URL);
	}

	/**
	 * 返回存放测试序列的数据库用户名称
	 * 
	 * @return
	 */
	public static String getDbUsr() {
		// return "root";
		return JTesterActivator.getDefault().getPreferenceStore().getString(DATABASE_USR);
	}

	/**
	 * 返回存放测试序列的数据库用户密码
	 * 
	 * @return
	 */
	public static String getDbPwd() {
		// return "password";
		return JTesterActivator.getDefault().getPreferenceStore().getString(DATABASE_PWD);
	}

	//
	/**
	 * 返回存储的Sqlmap模板
	 * 
	 * @return
	 */
	public static String getDomainSqlmapTemplate() {
		// return "root";
		return JTesterActivator.getDefault().getPreferenceStore().getString(DOMAIN_SQLMAP_TEMPLATE);
	}

	/**
	 * 返回存储的Sqlmap模板
	 * 
	 * @return
	 */
	public static String getDomainEntityTemplate() {
		// return "root";
		return JTesterActivator.getDefault().getPreferenceStore().getString(DOMAIN_ENTITY_TEMPLATE);
	}

	public static String geDomainSqlmapPath() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(DOMAIN_SQLMAP_PATH);
	}

	public static String geDomainEntityPath() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(DOMAIN_ENTITY_PATH);
	}

	/**
	 * 返回存储的Sqlmap Encoding
	 * 
	 * @return
	 */
	public static String getDomainSqlmapEncoding() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(DOMAIN_SQLMAP_ENCONDING);
	}

	/**
	 * 返回存储的Entity Encoding
	 * 
	 * @return
	 */
	public static String getDomainEntityEncoding() {
		return JTesterActivator.getDefault().getPreferenceStore().getString(DOMAIN_ENTITY_ENCONDING);
	}
}