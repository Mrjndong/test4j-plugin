/*
 * Copyright 2012 Alibaba.com All right reserved. This software is the
 * confidential and proprietary information of Alibaba.com ("Confidential
 * Information"). You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you entered
 * into with Alibaba.com.
 */
package org.test4j.plugin.ibatis.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.BaseTreeNode;
import org.test4j.plugin.database.ui.tree.TableNode;
import org.test4j.plugin.handler.BaseMenuItem;
import org.test4j.plugin.ibatis.wizard.DomainSqlmapWizard;
import org.test4j.plugin.resources.PluginMessages;

/**
 * 类DomainSqlmapMenu.java的实现描述：TODO 类实现描述
 * 
 * @author spark 2012-1-12 下午4:27:10
 */
public class DomainSqlmapMenu extends BaseMenuItem {

    public DomainSqlmapMenu(IViewSite viewSite) {
        super(viewSite);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.test4j.plugin.handler.BaseMenuItem#executeHandler(org.eclipse.core
     * .commands.ExecutionEvent)
     */
    @Override
    protected void executeHandler(ExecutionEvent event) {
        BaseTreeNode model = DatabaseView.getTreeViewer().getSelectedModel();
        if (model instanceof TableNode) {
            DomainSqlmapWizard wizard = new DomainSqlmapWizard(model);
            WizardDialog dialog = new WizardDialog(workbenchPartSite.getShell(), wizard);
            dialog.open();
        }
    }

    /*
     * (non-Javadoc)
     * @see org.test4j.plugin.handler.BaseMenuItem#getMenuText()
     */
    @Override
    protected String getMenuText() {
        return PluginMessages.DOMAIN_GENERATE_SQLMAP;
    }

    /*
     * (non-Javadoc)
     * @see org.test4j.plugin.handler.BaseMenuItem#getMenuIcon()
     */
    @Override
    protected String getMenuIcon() {
        return null;
    }
}
