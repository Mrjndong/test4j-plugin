package org.jtester.database.ui.tree;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DataSetView;
import org.jtester.plugin.handler.MenuFactory;
import org.jtester.plugin.helper.PluginHelper;

public class DataSetTable extends TableViewer {
	public static String MENU_ID = "#" + DataSetView.ID;

	private final IViewSite viewSite;

	private final Table table;

	private List<String> columnNames;

	public DataSetTable(final Composite parent, final IViewSite viewSite) {
		super(parent, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		this.viewSite = viewSite;
		this.table = this.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		this.setLabelProvider(new DataSetLabelProvider());
		this.setContentProvider(new ArrayContentProvider());

		Menu menu = this.createContextMenu();
		this.getControl().setMenu(menu);
		this.table.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
			}

			public void keyReleased(KeyEvent e) {
				int curr = e.keyCode;
				if ((curr == 'C' || curr == 'c') && e.stateMask == SWT.CTRL) {
					String wiki = DataSetTable.this.copyAsDbFit();
					PluginHelper.copyToClipBoard(wiki.toString());
				}
			}
		});
	}

	/**
	 * 填充数据
	 * 
	 * @param header
	 *            表格头
	 * @param data
	 *            具体数据
	 */
	public void fillData(List<String> columnNames, List<Map<String, String>> datas) {
		this.table.clearAll();
		TableColumn[] tableColumns = this.table.getColumns();
		for (TableColumn tableColumn : tableColumns) {
			tableColumn.dispose();
		}
		this.table.redraw();

		this.columnNames = columnNames;
		for (String name : columnNames) {
			TableColumn tableColumn = new TableColumn(this.table, SWT.CENTER);
			tableColumn.setText(name);
			tableColumn.setWidth(100);
		}
		this.setInput(datas);
	}

	/**
	 * 创建上下文菜单
	 * 
	 * @return
	 */
	private Menu createContextMenu() {
		MenuManager menuMgr = new MenuManager("JTester Menu", MENU_ID);
		this.viewSite.registerContextMenu(menuMgr, this);

		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager menuMgr) {
				if (DataSetTable.this.columnNames == null) {
					return;
				}

				menuMgr.add(MenuFactory.refreshData(viewSite));
				menuMgr.add(new Separator());
				menuMgr.add(MenuFactory.copyAsInsertData(viewSite));
				menuMgr.add(MenuFactory.copyAsQueryJava(viewSite));
				menuMgr.add(new Separator());
				menuMgr.add(MenuFactory.copySpecFieldsAsInsertJava(viewSite));
				menuMgr.add(MenuFactory.copySpecFieldsAsQueryJava(viewSite));
				// menuMgr.add(new Separator());
				// menuMgr.add(MenuFactory.generateData(viewSite));
			}
		});

		Menu menu = menuMgr.createContextMenu(this.getControl());
		return menu;
	}

	/**
	 * 将表格中的数据复制为dbfit数据
	 * 
	 * @param table
	 */
	public String copyAsDbFit() {
		return this.copyAsDbFit(this.columnNames);
	}

	/**
	 * 将表格中的数据复制为java的map代码
	 * 
	 * @return
	 */
	public String copyAsJavaMap() {
		return this.copyAsJavaMap(this.columnNames);
	}

	/**
	 * 将表格中的数据复制为java的map代码
	 * 
	 * @param table
	 */
	public String copyAsJavaMap(List<String> columns) {
		Map<String, String> datas = new LinkedHashMap<String, String>();
		TableItem[] items = this.table.getSelection();
		for (TableItem item : items) {
			for (String column : columns) {
				String text = this.getValueByColumnName(item, column, false);
				String value = datas.get(column);
				if (value == null) {
					datas.put(column, "\"" + text + "\"");
				} else {
					datas.put(column, value + ", \"" + text + "\"");
				}
			}
		}

		StringBuffer map = new StringBuffer("new DataMap(){\n");
		map.append("\t{\n");
		for (String column : datas.keySet()) {
			String value = datas.get(column);
			map.append(String.format("\t\tthis.put(\"%s\", %s);\n", column, value));
		}
		map.append("\t}\n");
		map.append("}");

		return map.toString();
	}

	/**
	 * 表格中选中的行数
	 * 
	 * @return
	 */
	public int getSelectedRows() {
		TableItem[] items = this.table.getSelection();
		return items.length;
	}

	/**
	 * 将表格中的数据复制为dbfit数据
	 * 
	 * @param table
	 */
	public String copyAsDbFit(List<String> columns) {
		StringBuffer wiki = new StringBuffer("|");
		for (String column : columns) {
			wiki.append(column);
			wiki.append("|");
		}

		TableItem[] items = this.table.getSelection();
		for (TableItem item : items) {
			wiki.append("\n|");
			for (String column : columns) {
				String text = this.getValueByColumnName(item, column, true);
				wiki.append(text);
				wiki.append("|");
			}
		}
		return wiki.toString();
	}

	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * 根据列名称获取对应列的内容
	 * 
	 * @param item
	 * @param columnName
	 * @return
	 */
	private String getValueByColumnName(TableItem item, String columnName, boolean isWiki) {
		int index = 0;
		for (String column : this.columnNames) {
			if (column.equals(columnName)) {
				break;
			}
			index++;
		}
		if (index >= this.columnNames.size()) {
			return "";
		}
		String text = item.getText(index);
		if (isWiki && (text.contains("\n") || text.contains("\r"))) {
			return "!-" + text + "-!";
		} else {
			return text;
		}
	}

	public class DataSetLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@SuppressWarnings("unchecked")
		public String getColumnText(Object element, int columnIndex) {
			if (DataSetTable.this.columnNames == null) {
				return "";
			}
			Map<String, String> map = (Map<String, String>) element;

			String key = DataSetTable.this.columnNames.get(columnIndex);
			String data = map.get(key);
			return data;
		}
	}
}
