package org.jtester.plugin.preference;

import static org.jtester.plugin.JTesterPreferenceInitializer.AbstractTextEditor_Color_Background;
import static org.jtester.plugin.JTesterPreferenceInitializer.AbstractTextEditor_Color_Background_SystemDefault;
import static org.jtester.plugin.resources.PluginResources.getResourceString;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.jtester.plugin.helper.PreferenceHelper;


public class BackgroundColourEditor {
	private Button bgDefault;
	private ColorEditor bgColorEditor;
	private Button bgCustom;
	private Composite colorComposite;
	private final IPreferenceStore preferenceStore;

	public BackgroundColourEditor(Composite parent, IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
		createControl(parent);
	}

	private IPreferenceStore getPreferenceStore() {
		return this.preferenceStore;
	}

	private void createControl(Composite parent) {
		this.colorComposite = new Composite(parent, SWT.NORMAL);
		this.colorComposite.setLayout(new GridLayout());
		GridData gd = new GridData();
		gd.horizontalSpan = 1;
		this.colorComposite.setLayoutData(gd);

		Group backgroundComposite = new Group(this.colorComposite, 16);
		backgroundComposite.setLayout(new RowLayout());
		backgroundComposite.setText(getResourceString("WikiSyntaxPreferencePage.backgroundColor"));

		SelectionAdapter backgroundSelectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean custom = BackgroundColourEditor.this.bgCustom.getSelection();
				BackgroundColourEditor.this.bgColorEditor.getButton().setEnabled(custom);
			}
		};
		this.bgDefault = new Button(backgroundComposite, SWT.LEAD | SWT.RADIO);// 16400
																				// 100,0000,0001,0000
		this.bgDefault.setText(getResourceString("WikiSyntaxPreferencePage.systemDefault"));
		this.bgDefault.addSelectionListener(backgroundSelectionListener);

		this.bgCustom = new Button(backgroundComposite, SWT.LEAD | SWT.RADIO);
		this.bgCustom.setText(getResourceString("WikiSyntaxPreferencePage.custom"));
		this.bgCustom.addSelectionListener(backgroundSelectionListener);

		this.bgColorEditor = new ColorEditor(backgroundComposite);

		setSelection(PreferenceHelper.isSysWikiEditBgColor());
	}

	private void setSelection(boolean systemDefault) {
		RGB rgb = PreferenceHelper.wikiEditorBgColor();
		this.bgColorEditor.setColorValue(rgb);
		this.bgDefault.setSelection(systemDefault);
		this.bgCustom.setSelection(!(systemDefault));
		this.bgColorEditor.getButton().setEnabled(!(systemDefault));
	}

	public void loadDefault() {
		setSelection(PreferenceHelper.isSysWikiEditBgColor());
		this.bgColorEditor.setColorValue(PreferenceConverter.getDefaultColor(getPreferenceStore(),
				AbstractTextEditor_Color_Background));
	}

	public void store() {
		PreferenceConverter.setValue(getPreferenceStore(), AbstractTextEditor_Color_Background, this.bgColorEditor
				.getColorValue());
		getPreferenceStore().setValue(AbstractTextEditor_Color_Background_SystemDefault, this.bgDefault.getSelection());
	}
}