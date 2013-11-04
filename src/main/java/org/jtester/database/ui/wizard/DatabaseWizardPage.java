package org.jtester.database.ui.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jtester.database.meta.DatabaseMeta;
import org.jtester.database.meta.DatabaseType;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.plugin.helper.PluginLogger;
import org.test4j.tools.commons.StringHelper;

/**
 * 数据库链接参数设置页面
 * 
 * @author darui.wudr
 * 
 */
public class DatabaseWizardPage extends WizardPage {
	private Text name;
	private Combo dbType;
	private Text schema;
	private Text username;
	private Text password;
	private Combo url;
	private Combo driver;
	private Text driverJar;
	private Button showTables;
	private Button showViews;

	private DatabaseMeta databaseMeta = null;

	private FileDialog jarDialog;

	private boolean isEdit;

	public DatabaseWizardPage(String pageName, DatabaseMeta databaseMeta, boolean isEdit) {
		super(pageName);
		this.databaseMeta = databaseMeta == null ? new DatabaseMeta() : databaseMeta;
		this.isEdit = isEdit;
	}

	private void initDialog() {
		jarDialog = new FileDialog(getContainer().getShell(), SWT.OPEN);
		jarDialog.setFilterExtensions(new String[] { "*.jar", "*.zip", "*.*" });
		jarDialog.setFilterNames(new String[] { "Jar Files (*.jar)", "Zip Files (*.zip)", "All Files (*.*)" });
	}

	public void createControl(Composite parent) {
		this.initDialog();

		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);
		layout.verticalSpacing = 9;

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;

		this.name = this.createText(container, "*Name", gd);
		this.name.setText(this.databaseMeta.getManulName());

		this.dbType = this.createCombo(container, "*DB Type", DatabaseType.types(), gd);
		this.dbType.setText(this.databaseMeta.getDatabaseTypeStr());

		this.schema = this.createText(container, "Schema", gd);
		this.schema.setText(this.databaseMeta.getSchema());

		this.username = this.createText(container, "*Username", gd);
		this.username.setText(this.databaseMeta.getUsername());

		this.password = this.createText(container, "Password", gd);
		this.password.setEchoChar('*');
		this.password.setText(this.databaseMeta.getPassword());

		this.url = this.createCombo(container, "*URL", DatabaseNode.URL_ARRAY, gd);
		this.url.setText(this.databaseMeta.getUrl());

		this.driver = this.createCombo(container, "*Driver", DatabaseNode.DRIVER_ARRAY, gd);
		this.driver.setText(this.databaseMeta.getDriverClazz());

		this.driverJar = this.createJars(container);
		this.driverJar.setText(this.databaseMeta.getDriverJars());

		this.showTables = new Button(container, SWT.CHECK);
		this.showTables.setText("Show Tables");
		this.showTables.setSelection(this.databaseMeta.isShowTables());

		this.showViews = new Button(container, SWT.CHECK);
		this.showViews.setText("Show Views");
		this.showViews.setSelection(this.databaseMeta.isShowViews());

		this.setControl(container);
		this.setPageComplete(true);
	}

	/**
	 * 创建一行编辑框
	 * 
	 * @param parent
	 * @param capital
	 * @param gd
	 * @return
	 */
	private Text createText(Composite parent, String capital, GridData gd) {
		Label label = new Label(parent, SWT.NULL);
		label.setText(capital);
		Text text = new Text(parent, SWT.BORDER | SWT.SINGLE);
		text.setLayoutData(gd);
		return text;
	}

	private Text createJars(Composite parent) {
		Label label = new Label(parent, SWT.NULL);
		label.setText("*Driver JARs");
		Text text = new Text(parent, SWT.BORDER | SWT.MULTI);
		GridData area = new GridData(GridData.FILL_BOTH);
		area.heightHint = 60;
		text.setLayoutData(area);

		Button button = new Button(parent, SWT.PUSH);
		button.setText("..");
		GridData btnGd = new GridData(18, 20);
		btnGd.verticalAlignment = SWT.END;
		button.setLayoutData(btnGd);

		button.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				DatabaseWizardPage.this.getDialogFileName();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}

		});
		return text;
	}

	/**
	 * 返回文件对话框选择的文件名称
	 * 
	 * @return
	 */
	private String getDialogFileName() {
		String filename = jarDialog.open();
		if (filename != null) {
			String jars = driverJar.getText().trim();
			if ("".equals(jars)) {
				jars = filename;
			} else {
				jars += "\n" + filename;
			}
			driverJar.setText(jars);
		}
		return filename;
	}

	private Combo createCombo(Composite parent, String capital, String[] items, GridData gd) {
		Label label = new Label(parent, SWT.NULL);
		label.setText(capital);
		Combo combo = new Combo(parent, SWT.NULL);
		combo.setItems(items);
		combo.setLayoutData(gd);
		return combo;
	}

	public boolean performFinish() {
		if (this.databaseMeta == null) {
			this.databaseMeta = new DatabaseMeta();
			isEdit = false;
		}
		this.databaseMeta.setManulName(name.getText().trim());
		this.databaseMeta.setDatabaseType(dbType.getText());
		this.databaseMeta.setSchema(schema.getText().trim());
		this.databaseMeta.setUsername(username.getText().trim());
		this.databaseMeta.setPassword(password.getText());// Don't trim
															// password!
		this.databaseMeta.setUrl(url.getText().trim());
		this.databaseMeta.setDriverClazz(driver.getText().trim());
		this.databaseMeta.setDriverJars(driverJar.getText().trim());
		this.databaseMeta.setShowTables(showTables.getSelection());
		this.databaseMeta.setShowViews(showViews.getSelection());

		if (this.databaseMeta.getDatabaseType() == null && StringHelper.isBlankOrNull(dbType.getText()) == false) {
			PluginLogger.openError("DbType can only be:" + StringHelper.merger(DatabaseType.types(), ';'));
			return false;
		}
		if (this.databaseMeta.validate() == false) {
			PluginLogger.openError("Required field missing");
			return false;
		}

		if (isEdit) {
			DatabaseView.refresh();
		} else {
			DatabaseView.addConnection(this.databaseMeta);
		}
		return true;
	}
}
