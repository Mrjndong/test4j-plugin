package org.jtester.plugin.preference;

import static org.eclipse.swt.layout.GridData.GRAB_HORIZONTAL;
import static org.eclipse.swt.layout.GridData.GRAB_VERTICAL;
import static org.eclipse.swt.layout.GridData.HORIZONTAL_ALIGN_FILL;
import static org.eclipse.swt.layout.GridData.VERTICAL_ALIGN_FILL;
import static org.jtester.plugin.JTesterPreferenceInitializer.DBFIT_RESOURCE;
import static org.jtester.plugin.JTesterPreferenceInitializer.JAVA_TYPE;
import static org.jtester.plugin.JTesterPreferenceInitializer.OTHER;
import static org.jtester.plugin.JTesterPreferenceInitializer.URL;
import static org.jtester.plugin.resources.PluginResources.getResourceString;

import java.util.HashMap;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

public class EditorColours {
	private static final String[][] COLOUR_LIST = { { getResourceString("WikiSyntaxPreferencePage.URL"), URL },
			{ getResourceString("WikiSyntaxPreferencePage.DbFitResource"), DBFIT_RESOURCE },
			{ getResourceString("WikiSyntaxPreferencePage.JavaType"), JAVA_TYPE },
			{ getResourceString("WikiSyntaxPreferencePage.Other"), OTHER } };
	private List colors;
	private ColorEditor fgColorEditor;
	private Button fgBold;
	private RGB[] currentColours = new RGB[COLOUR_LIST.length];
	private HashMap<String, String> currentBold = new HashMap<String, String>();
	private final IPreferenceStore preferenceStore;

	public EditorColours(Composite parent, IPreferenceStore preferenceStore) {
		this.preferenceStore = preferenceStore;
		createControl(parent);
		load();
	}

	private IPreferenceStore getPreferenceStore() {
		return this.preferenceStore;
	}

	private void createControl(Composite parent) {
		Composite colorComposite = new Composite(parent, SWT.NORMAL);
		colorComposite.setLayout(new GridLayout());
		colorComposite.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
				| VERTICAL_ALIGN_FILL));

		Label label = new Label(colorComposite, 16384);
		label.setText(getResourceString("WikiSyntaxPreferencePage.keyword.color"));
		label.setLayoutData(new GridData(768));

		Composite editorComposite = new Composite(colorComposite, SWT.NORMAL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		editorComposite.setLayout(layout);
		GridData gd = new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL | VERTICAL_ALIGN_FILL);
		editorComposite.setLayoutData(gd);

		this.colors = new List(editorComposite, 2564);
		gd = new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL | VERTICAL_ALIGN_FILL);
		this.colors.setLayoutData(gd);

		Composite stylesComposite = new Composite(editorComposite, SWT.NORMAL);
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 2;
		stylesComposite.setLayout(layout);
		stylesComposite.setLayoutData(new GridData(GRAB_VERTICAL | GRAB_HORIZONTAL | HORIZONTAL_ALIGN_FILL
				| VERTICAL_ALIGN_FILL));

		label = new Label(stylesComposite, 16384);
		label.setText(getResourceString("WikiSyntaxPreferencePage.color"));
		gd = new GridData();
		gd.horizontalAlignment = 1;
		label.setLayoutData(gd);

		this.fgColorEditor = new ColorEditor(stylesComposite);

		Button fgColorButton = this.fgColorEditor.getButton();
		gd = new GridData(768);
		gd.horizontalAlignment = 1;
		fgColorButton.setLayoutData(gd);
		fgColorButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int i = EditorColours.this.colors.getSelectionIndex();
				EditorColours.this.currentColours[i] = EditorColours.this.fgColorEditor.getColorValue();
			}
		});
		label = new Label(stylesComposite, 16384);
		label.setText(getResourceString("WikiSyntaxPreferencePage.bold"));
		gd = new GridData();
		gd.horizontalAlignment = 1;
		label.setLayoutData(gd);

		this.fgBold = new Button(stylesComposite, 32);
		gd = new GridData(768);
		gd.horizontalAlignment = 1;
		this.fgBold.setLayoutData(gd);

		this.fgBold.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int i = EditorColours.this.colors.getSelectionIndex();
				String key = EditorColours.COLOUR_LIST[i][1] + "_style";
				String value = (EditorColours.this.fgBold.getSelection()) ? "bold" : "normal";
				EditorColours.this.currentBold.put(key, value);
			}
		});
		this.colors.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				EditorColours.this.handleSyntaxColorListSelection();
			}
		});
	}

	private void handleSyntaxColorListSelection() {
		int i = this.colors.getSelectionIndex();
		this.fgColorEditor.setColorValue(this.currentColours[i]);
		this.fgBold.setSelection(getPreferenceStore().getString(COLOUR_LIST[i][1] + "_style").indexOf("bold") >= 0);
	}

	private void load() {
		this.colors.removeAll();
		for (int i = 0; i < COLOUR_LIST.length; ++i) {
			this.colors.add(COLOUR_LIST[i][0]);
			this.currentColours[i] = PreferenceConverter.getColor(getPreferenceStore(), COLOUR_LIST[i][1]
					+ "_foreground");
		}
		this.colors.select(0);
		handleSyntaxColorListSelection();
	}

	public void loadDefault() {
		this.colors.removeAll();
		for (int i = 0; i < COLOUR_LIST.length; ++i) {
			this.colors.add(COLOUR_LIST[i][0]);
			this.currentColours[i] = PreferenceConverter.getDefaultColor(getPreferenceStore(), COLOUR_LIST[i][1]
					+ "_foreground");
		}
		this.colors.select(0);
		handleSyntaxColorListSelection();
	}

	public void store() {
		for (int i = 0; i < this.currentColours.length; ++i) {
			String key = COLOUR_LIST[i][1] + "_foreground";
			PreferenceConverter.setValue(getPreferenceStore(), key, this.currentColours[i]);
		}
		for (String key : this.currentBold.keySet())
			getPreferenceStore().setValue(key, (String) this.currentBold.get(key));
	}
}