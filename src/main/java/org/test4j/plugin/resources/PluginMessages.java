package org.test4j.plugin.resources;

import org.eclipse.osgi.util.NLS;

public class PluginMessages extends NLS {
    public static String        PREFERENCE_TITLE;

    /**
     * 序列db连接串
     */
    public static String        SEQUENCE_DB_URL;
    /**
     * 序列db用户名
     */
    public static String        SEQUENCE_DB_USR;
    /**
     * 序列db密 码
     */
    public static String        SEQUENCE_DB_PWD;

    public static String        CODE_PATH_MESSAGE;
    public static String        RESOURCES_PATH_MESSAGE;
    public static String        TEST_CODE_PATH_MESSAGE;
    public static String        TEST_RESOURCES_PATH_MESSAGE;

    public static String        SORRY_NOT_JAVA_PROJECT;
    public static String        TEST4J_PATH_SETTING;

    /**
     * 刷新树状视图
     */
    public static String        DB_TREE_REFRESH;
    /**
     * 选中数据库
     */
    public static String        DB_CONNECTION_SELECT;
    /**
     * 关闭数据库连接
     */
    public static String        DB_CONNECTION_CLOSE;
    /**
     * 新建数据库连接
     */
    public static String        DB_CONNECTION_NEW;
    /**
     * 编辑数据库连接
     */
    public static String        DB_CONNECTION_EDIT;
    /**
     * 删除数据库连接
     */
    public static String        DB_CONNECTION_REMOVE;
    /**
     * 连接数据库
     */
    public static String        DB_CONNECTION_DOING;
    /**
     * 选中一个表
     */
    public static String        DB_TABLE_SELECT;
    /**
     * 表查询
     */
    public static String        DB_TABLE_QUERY;

    /**
     * select count(*)
     */
    public static String        DB_TABLE_COUNT;

    /**
     * 刷新查询视图
     */
    public static String        DATABASE_REFRESH_VIEW;
    /**
     * 复制为插入数据
     */
    public static String        DATABASE_SELECT_AS_INSERT_DATA;
    /**
     * 复制为验证数据
     */
    public static String        DATABASE_SELECT_AS_CHECK_DATA;
    /**
     * 复制指定字段为插入数据
     */
    public static String        DATABASE_SELECT_FIELDS_AS_INSERT_DATA;
    /**
     * 复制指定字段为验证数据
     */
    public static String        DATABASE_SELECT_FIELDS_AS_CHECK_DATA;
    /**
     * 将wiki转换为java代码
     */
    public static String        WIKI_CONVERT_TO_JAVA;

    // ===following for ibatis
    /**
     * 生成Domain Sqlmap
     */
    public static String        DOMAIN_GENERATE_SQLMAP;

    /**
     * 错误文件后缀
     */
    public static String        DOMAIN_GENERATE_SQLMAP_ERR_EXT;

    /**
     * 创建sqlmap文件对话框标题
     */
    public static String        DOMAIN_GENERATE_SQLMAP_TITLE;
    /**
     * 创建sqlmap文件对话框描述
     */
    public static String        DOMAIN_GENERATE_SQLMAP_DESC;

    /**
     * 生成Domain Entity
     */
    public static String        DOMAIN_GENERATE_ENTITY;

    /**
     * 错误文件后缀
     */
    public static String        DOMAIN_GENERATE_ENTITY_ERR_EXT;

    /**
     * 创建sqlmap文件对话框标题
     */
    public static String        DOMAIN_GENERATE_ENTITY_TITLE;
    /**
     * 创建sqlmap文件对话框描述
     */
    public static String        DOMAIN_GENERATE_ENTITY_DESC;

    private static final String BUNDLE_NAME = "org.test4j.plugin.resources.PluginMessages"; //$NON-NLS-1$

    public PluginMessages() {

    }

    static {
        NLS.initializeMessages(BUNDLE_NAME, PluginMessages.class);
    }

}
