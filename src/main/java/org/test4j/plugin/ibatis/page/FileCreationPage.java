/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package org.test4j.plugin.ibatis.page;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.test4j.plugin.database.ui.tree.BaseTreeNode;

/**
 * 类FileCreationPage.java的实现描述：TODO 类实现描述
 * 
 * @author spark 2012-1-13 下午2:30:13
 */
public abstract class FileCreationPage extends WizardNewFileCreationPage {
	/**
	 * @param pageName
	 * @param selection
	 */
	public FileCreationPage(String pageName, IStructuredSelection selection) {
		super(pageName, selection);
	}

	protected BaseTreeNode model;

	/**
	 * check file extension if correct.
	 */
	protected boolean validateFilename(String ext, String errTip) {
		if (getFileName() != null && getFileName().endsWith(ext)) {
			return true;
		}
		setErrorMessage(errTip);
		return false;
	}

	/**
	 * This method will be invoked, when the "Finish" button is pressed.
	 * 
	 * @see FileCreationWizard#performFinish()
	 */
	public boolean finish() {
		IFile file = createNewFile();
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		if (file != null && page != null) {
			try {
				IDE.openEditor(page, file, true);
			} catch (PartInitException e) {
				e.printStackTrace();
				return false;
			}
		}
		if (file != null) {
			return true;
		} else
			return false;
	}
}
