/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package org.test4j.plugin.ibatis.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.test4j.plugin.CommonConstants;
import org.test4j.plugin.database.ui.tree.BaseTreeNode;
import org.test4j.plugin.ibatis.page.SqlmapCreationPage;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 类DomainSqlmapWizard.java的实现描述：TODO 类实现描述
 * 
 * @author spark 2012-1-13 上午10:39:46
 */
public class DomainSqlmapWizard extends Wizard implements INewWizard {
	private BaseTreeNode model;
	private IStructuredSelection selection;

	SqlmapCreationPage page1;

	public DomainSqlmapWizard(BaseTreeNode model) {
		super();
		setWindowTitle(PluginMessages.DOMAIN_GENERATE_SQLMAP_TITLE);
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return page1.finish();
	}

	public void addPages() {
		if (selection == null) {
			selection = new StructuredSelection();
		}
		page1 = new SqlmapCreationPage(selection, model);
		if (model != null) {
			page1.setFileName(model.getJavaName() + CommonConstants.ENTITY_EXTENSION + CommonConstants.XML_EXTENSION);
		}
		addPage(page1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}
