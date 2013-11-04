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
import org.test4j.plugin.ibatis.page.EntityCreationPage;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 类NewConfigFileWizard.java的实现描述：TODO 类实现描述
 * 
 * @author spark 2012-1-13 上午11:36:35
 */
public class DomainEntityWizard extends Wizard implements INewWizard {
	private BaseTreeNode model;
	private IStructuredSelection selection;
	private EntityCreationPage page;

	public DomainEntityWizard(BaseTreeNode model) {
		setWindowTitle(PluginMessages.DOMAIN_GENERATE_ENTITY_TITLE);
		this.model = model;

	}

	@Override
	public void addPages() {
		if (selection == null) {
			selection = new StructuredSelection();
		}
		page = new EntityCreationPage(selection, model);
		if (model != null) {
			page.setFileName(model.getJavaName() + CommonConstants.ENTITY_EXTENSION + CommonConstants.JAVA_EXTENSION);
		}
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		return page.finish();
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}
}
