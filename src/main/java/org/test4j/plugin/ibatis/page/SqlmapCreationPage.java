/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package org.test4j.plugin.ibatis.page;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.test4j.plugin.CommonConstants;
import org.test4j.plugin.Test4JActivator;
import org.test4j.plugin.database.ui.tree.BaseTreeNode;
import org.test4j.plugin.helper.PluginSetting;
import org.test4j.plugin.ibatis.assistor.VelocityUtil;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 类SqlmapCreationPage.java的实现描述：TODO 类实现描述
 * 
 * @author spark 2012-1-13 上午10:58:38
 */
public class SqlmapCreationPage extends FileCreationPage {

	// 定义缺省的文件扩展名
	private static final String DEFAULT_EXTENSION = CommonConstants.XML_EXTENSION;

	/**
	 * Create a new wizard page instance.
	 * 
	 * @param workbench
	 *            the current workbench
	 * @param selection
	 *            the current object selection
	 * @see ShapesCreationWizard#init(IWorkbench, IStructuredSelection)
	 */
	public SqlmapCreationPage(IStructuredSelection selection, BaseTreeNode model) {
		super(PluginMessages.DOMAIN_GENERATE_SQLMAP, selection);
		this.model = model;
		// 设置向导页的标题和描述
		setTitle(PluginMessages.DOMAIN_GENERATE_SQLMAP_TITLE);
		setDescription(PluginMessages.DOMAIN_GENERATE_SQLMAP_DESC);
	}

	public void createControl(Composite parent) {
		super.createControl(parent);
		// 设置向导的完成按钮是否可用
		setPageComplete(validatePage());
		String initPath = PluginSetting.geDomainSqlmapPath();
		if (initPath != null && !"".equals(initPath)) {
			IPath path = new Path(initPath);
			setContainerFullPath(path);
		}
	}

	protected InputStream getInitialContents() {
		try {
			String templateContext = PluginSetting.getDomainSqlmapTemplate();
			return VelocityUtil.getMergeOutput(templateContext, model, PluginSetting.getDomainSqlmapEncoding());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	protected boolean validatePage() {
		return super.validatePage()
				&& validateFilename(DEFAULT_EXTENSION, PluginMessages.DOMAIN_GENERATE_SQLMAP_ERR_EXT);
	}

	public boolean finish() {
		boolean isFinished = super.finish();
		if (isFinished) {
			// 记录存储路径
			Test4JActivator.getDefault().getPreferenceStore()
					.setValue(PluginSetting.DOMAIN_SQLMAP_PATH, getContainerFullPath().toString());
		}
		return isFinished;
	}

}
