package org.test4j.plugin.testsequence.assistor;

import static org.test4j.plugin.resources.PluginResources.getResourceString;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class SelectSequenceDialog extends TitleAreaDialog {

	private Button[] radios = new Button[2];

	private List lList;

	private List rList;

	private MethodSequence sequence;

	public SelectSequenceDialog(Shell parentShell, MethodSequence sequence) {
		super(parentShell);
		super.setTitle("compare two test sequence");
		this.sequence = sequence;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Control contents = super.createDialogArea(parent);
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, true));

		new Label(container, SWT.NULL).setText(getResourceString("SelectSequenceDialog.LeftFile"));
		radios[0] = new Button(container, SWT.RADIO);
		radios[0].setSelection(true);
		radios[0].setText(getResourceString("SelectSequenceDialog.LocalFile"));
		radios[0].setBounds(10, 5, 75, 30);

		new Label(container, SWT.NULL).setText(getResourceString("SelectSequenceDialog.RightFile"));

		radios[1] = new Button(container, SWT.RADIO);
		radios[1].setText(getResourceString("SelectSequenceDialog.RemoteFile"));
		radios[1].setBounds(10, 30, 75, 30);

		new Label(container, SWT.NULL);
		Button refresh = new Button(container, SWT.PUSH);
		refresh.setText(getResourceString("Refresh"));
		refresh.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				SelectSequenceDialog.this.refreshSequenceList(e);
			}
		});

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 250;
		gd.heightHint = 150;
		this.lList = new List(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		this.lList.setLayoutData(gd);
		this.rList = new List(container, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
		this.rList.setLayoutData(gd);

		String[] items = this.sequence.getLocalSequences();
		for (String item : items) {
			this.lList.add(item);
		}

		return contents;
	}

	/**
	 * 刷新列表中的文件
	 * 
	 * @param e
	 */
	private void refreshSequenceList(SelectionEvent e) {
		boolean checked = radios[0].getSelection();
		String[] items = null;
		if (checked) {
			items = this.sequence.getLocalSequences();
		} else {
			items = this.sequence.getRemoteSequences();
		}
		this.rList.removeAll();
		for (String item : items) {
			this.rList.add(item);
		}
	}

	private String lFile = null;
	private String rFile = null;

	@Override
	protected void okPressed() {
		String[] lFiles = this.lList.getSelection();
		if (lFiles != null && lFiles.length > 0) {
			lFile = lFiles[0];
		}
		String[] rFiles = this.rList.getSelection();
		if (rFiles != null && rFiles.length > 0) {
			rFile = rFiles[0];
		}
		super.okPressed();
	}

	public String getRFile() {
		return rFile;
	}

	public String getlFile() {
		return lFile;
	}
}
