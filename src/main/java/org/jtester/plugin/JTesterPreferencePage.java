package org.jtester.plugin;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;
import static org.jtester.plugin.helper.PluginSetting.DATABASE_PWD;
import static org.jtester.plugin.helper.PluginSetting.DATABASE_URL;
import static org.jtester.plugin.helper.PluginSetting.DATABASE_USR;
import static org.jtester.plugin.helper.PluginSetting.RENDER_FULLY_QUALIFIED_TYPE_NAMES;
import static org.jtester.plugin.helper.PluginSetting.RESOURCES_PATH;
import static org.jtester.plugin.helper.PluginSetting.SHOW_BROWSER_IN_EDITOR_WHEN_OPENING;
import static org.jtester.plugin.helper.PluginSetting.SOURCE_PATH;
import static org.jtester.plugin.helper.PluginSetting.TEST_RESOURCES_PATH;
import static org.jtester.plugin.helper.PluginSetting.TEST_SOURCE_PATH;
import static org.jtester.plugin.helper.PluginSetting.WORD_WRAP;
import static org.jtester.plugin.resources.PluginMessages.CODE_PATH_MESSAGE;
import static org.jtester.plugin.resources.PluginMessages.RESOURCES_PATH_MESSAGE;
import static org.jtester.plugin.resources.PluginMessages.SEQUENCE_DB_PWD;
import static org.jtester.plugin.resources.PluginMessages.SEQUENCE_DB_URL;
import static org.jtester.plugin.resources.PluginMessages.SEQUENCE_DB_USR;
import static org.jtester.plugin.resources.PluginMessages.TEST_CODE_PATH_MESSAGE;
import static org.jtester.plugin.resources.PluginMessages.TEST_RESOURCES_PATH_MESSAGE;
import static org.jtester.plugin.resources.PluginResources.getResourceString;
import static org.jtester.plugin.helper.PluginSetting.DOMAIN_ENTITY_ENCONDING;
import static org.jtester.plugin.helper.PluginSetting.DOMAIN_ENTITY_TEMPLATE;
import static org.jtester.plugin.helper.PluginSetting.DOMAIN_SQLMAP_ENCONDING;
import static org.jtester.plugin.helper.PluginSetting.DOMAIN_SQLMAP_TEMPLATE;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jtester.JTesterActivator;
import org.jtester.ibatis.ui.MultiLineTextFieldEditor;
import org.jtester.plugin.preference.BackgroundColourEditor;
import org.jtester.plugin.preference.EditorColours;
import org.jtester.plugin.preference.TabFolderLayout;
import org.jtester.plugin.resources.PluginResources;

public class JTesterPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	public JTesterPreferencePage() {
		super(GRID);
		setDescription(getResourceString("JTesterPreferencePage.description"));
		setPreferenceStore(JTesterActivator.getDefault().getPreferenceStore());
	}

	private BackgroundColourEditor backgroundColourEditor;
	private EditorColours editorColours;

	public boolean performOk() {
		this.backgroundColourEditor.store();
		this.editorColours.store();
		return super.performOk();
	}

	protected void performDefaults() {
		this.backgroundColourEditor.loadDefault();
		this.editorColours.loadDefault();
		super.performDefaults();
	}

	protected void addField(FieldEditor editor) {
		super.addField(editor);
	}

	@Override
	public void createFieldEditors() {
		Composite composite = new Composite(getFieldEditorParent(), SWT.NORMAL);
		// GridLayout layout = new GridLayout();
		// layout.marginHeight = 0;
		// layout.marginWidth = 0;
		composite.setLayout(new FillLayout());
		composite.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
				| VERTICAL_ALIGN_FILL));

		TabFolder folder = new TabFolder(composite, SWT.NORMAL);

		createDatabaseSetting(folder);
		createWikiEditorPreferences(folder);
		createDomainPreferences(folder);

		// folder.setLayout(new FillLayout());
		GridLayoutFactory.fillDefaults().generateLayout(folder);

		Dialog.applyDialogFont(composite);
	}

	/**
	 * 创建wiki编辑器的参数设置
	 * 
	 * @param parent
	 */
	private void createWikiEditorPreferences(TabFolder tabFolder) {
		Composite tabDbFit = createTab(tabFolder, "JTesterPreferences.WikiEditorTitle");

		Composite composite = new Composite(tabDbFit, SWT.NORMAL);
		GridLayout basicLayout = new GridLayout();
		basicLayout.numColumns = 2;
		composite.setLayout(basicLayout);

		this.backgroundColourEditor = new BackgroundColourEditor(composite, getPreferenceStore());
		this.editorColours = new EditorColours(composite, getPreferenceStore());

		addField(new BooleanFieldEditor(SHOW_BROWSER_IN_EDITOR_WHEN_OPENING,
				PluginResources.getResourceString(SHOW_BROWSER_IN_EDITOR_WHEN_OPENING), composite));

		addField(new BooleanFieldEditor(RENDER_FULLY_QUALIFIED_TYPE_NAMES,
				PluginResources.getResourceString(RENDER_FULLY_QUALIFIED_TYPE_NAMES), composite));
		addField(new BooleanFieldEditor(WORD_WRAP, PluginResources.getResourceString(WORD_WRAP), composite));
	}

	/**
	 * 设置continuous testing参数设置面板
	 * 
	 * @param tabFolder
	 */
	private void createDatabaseSetting(TabFolder tabFolder) {
		Composite tab = createTab(tabFolder, "JTesterPreferences.JTesterSettings");

		// addField(new BooleanFieldEditor(AUTO_RUN_CT, AUTO_JTESTER_CT,
		// tabCt));
		// addField(new BooleanFieldEditor(RUN_ALL_TEST, RUN_ALL_TEST_LABEL,
		// tabCt));

		/**
		 * SEQUENCE数据库属性
		 */
		addField(new StringFieldEditor(DATABASE_URL, SEQUENCE_DB_URL, tab));
		addField(new StringFieldEditor(DATABASE_USR, SEQUENCE_DB_USR, tab));
		addField(new StringFieldEditor(DATABASE_PWD, SEQUENCE_DB_PWD, tab));
		/**
		 * 测试目录和源码目录设置
		 */
		addField(new StringFieldEditor(SOURCE_PATH, CODE_PATH_MESSAGE, tab));
		addField(new StringFieldEditor(RESOURCES_PATH, RESOURCES_PATH_MESSAGE, tab));
		addField(new StringFieldEditor(TEST_SOURCE_PATH, TEST_CODE_PATH_MESSAGE, tab));
		addField(new StringFieldEditor(TEST_RESOURCES_PATH, TEST_RESOURCES_PATH_MESSAGE, tab));
	}

	/**
	 * 创建domain的参数设置
	 * 
	 * @param parent
	 */
	private void createDomainPreferences(TabFolder tabFolder) {
		Composite tabDomain = createTab(tabFolder, "JTesterPreferences.DomainTitle");

		Composite composite = new Composite(tabDomain, SWT.NONE);
		GridLayout basicLayout = new GridLayout();
		basicLayout.numColumns = 2;
		composite.setLayout(basicLayout);

		addField(new StringFieldEditor(DOMAIN_SQLMAP_ENCONDING,
				PluginResources.getResourceString(DOMAIN_SQLMAP_ENCONDING), composite));
		addField(new MultiLineTextFieldEditor(DOMAIN_SQLMAP_TEMPLATE,
				PluginResources.getResourceString(DOMAIN_SQLMAP_TEMPLATE), composite));

		addField(new StringFieldEditor(DOMAIN_ENTITY_ENCONDING,
				PluginResources.getResourceString(DOMAIN_ENTITY_ENCONDING), composite));
		addField(new MultiLineTextFieldEditor(DOMAIN_ENTITY_TEMPLATE,
				PluginResources.getResourceString(DOMAIN_ENTITY_TEMPLATE), composite));
	}

	private Composite createTab(TabFolder folder, String titleKey) {
		TabItem item = new TabItem(folder, SWT.NORMAL);
		item.setText(getResourceString(titleKey));
		Composite c = new Composite(folder, SWT.NORMAL);
		c.setLayout(new TabFolderLayout());
		c.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL | VERTICAL_ALIGN_FILL));
		item.setControl(c);
		return c;
	}

	public void init(IWorkbench workbench) {
	}
}