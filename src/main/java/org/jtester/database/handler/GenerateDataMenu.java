package org.jtester.database.handler;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewSite;
import org.jtester.database.ui.DatabaseView;
import org.jtester.database.ui.tree.BaseTreeNode;
import org.jtester.database.ui.tree.ColumnNode;
import org.jtester.database.ui.tree.TableNode;
import org.jtester.database.ui.wizard.InsertWizard;
import org.jtester.plugin.handler.BaseMenuItem;
import org.jtester.plugin.resources.IconResources;

/**
 * 生成随机数据
 * 
 * @author darui.wudr
 * 
 */
public class GenerateDataMenu extends BaseMenuItem {

	public GenerateDataMenu(IViewSite viewSite) {
		super(viewSite);
	}

	@Override
	protected void executeHandler(ExecutionEvent event) {
		BaseTreeNode model = DatabaseView.getTreeViewer().getSelectedModel();

		if (model instanceof TableNode || model instanceof ColumnNode) {
			InsertWizard wizard = new InsertWizard(model);

			WizardDialog dialog = new WizardDialog(workbenchPartSite.getShell(), wizard);
			dialog.open();
		}
	}

	@Override
	protected String getMenuText() {
		return "生成数据";//
	}

	@Override
	protected String getMenuIcon() {
		return IconResources.INSERT_ICON;
	}

}
