package org.test4j.plugin.database.ui;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.test4j.plugin.database.ui.tree.DataSetTable;
import org.test4j.plugin.helper.PluginHelper;

/**
 * 查询数据列表
 * 
 * @see ViewPart
 */
public class DataSetView extends ViewPart {
    public static String       ID  = "org.test4j.plugin.database.ui.DataSetView";
    private static DataSetView instance;

    private DataSetTable       dataTable;

    private String             sql = null;

    public DataSetView() {
        instance = this;
    }

    public static synchronized DataSetView getInstance() {
        if (instance == null) {
            instance = new DataSetView();
        }
        return instance;
    }

    @Override
    public void createPartControl(Composite parent) {
        this.dataTable = new DataSetTable(parent, this.getViewSite());
    }

    /**
     * 重新填充table区域的数据
     * 
     * @param rs
     * @param query
     * @param newTab
     * @throws SQLException
     */
    public void reFillDataSet(String sql, List<String> columnNames, List<Map<String, String>> datas)
            throws SQLException {
        this.sql = sql;
        // this.tableName = tableName;
        this.dataTable.fillData(columnNames, datas);
    }

    public List<String> getVisibleColumns() {
        return this.dataTable.getColumnNames();
    }

    /**
     * 将数据库数据拷贝为java插入代码或验证代码
     * 
     * @param isInsert
     */
    public static void copyAsJavaMap(List<String> columns, boolean isInsert) {
        StringBuffer buff = new StringBuffer();

        String tableName = DataSetView.getInstance().getCurrTableName();
        String map = "";
        if (columns == null || columns.size() == 0) {
            map = DataSetView.getInstance().dataTable.copyAsJavaMap();
        } else {
            map = DataSetView.getInstance().dataTable.copyAsJavaMap(columns);
        }

        int count = DataSetView.getInstance().dataTable.getSelectedRows();
        if (isInsert) {
            buff.append(String.format("db.table(\"%s\").clean().insert(%d, %s);\n", tableName, count, map));
        } else {
            buff.append(String.format("db.table(\"%s\").query().reflectionEqMap(%d, %s, EqMode.IGNORE_ORDER);",
                    tableName, count, map));
        }
        PluginHelper.copyToClipBoard(buff.toString());
    }

    /**
     * 复制为Json形式的数据
     * 
     * @param columns
     */
    public static void copyAsJson(List<String> columns) {
        String json = "";
        if (columns == null || columns.size() == 0) {
            json = DataSetView.getInstance().dataTable.copyAsJson(null);
        } else {
            json = DataSetView.getInstance().dataTable.copyAsJson(columns);
        }
        PluginHelper.copyToClipBoard(json);
    }

    private static Pattern pattern = Pattern.compile(".*FROM\\s+([\\w_]+).*");

    String getCurrTableName() {
        if (this.sql == null || "".equals(this.sql)) {
            return "Your Table";
        }
        Matcher matcher = pattern.matcher(this.sql.toUpperCase());
        if (matcher.find()) {
            String tablename = matcher.group(1);
            return tablename;
        }
        return "Your Table";
    }

    @Override
    public void setFocus() {
    }
}
