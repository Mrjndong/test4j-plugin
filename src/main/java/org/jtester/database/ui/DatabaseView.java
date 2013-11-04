package org.jtester.database.ui;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.part.ViewPart;
import org.jtester.database.assistor.SQLEditorInput;
import org.jtester.database.meta.DatabaseMeta;
import org.jtester.database.ui.tree.BaseTreeNode;
import org.jtester.database.ui.tree.DatabaseNode;
import org.jtester.database.ui.tree.DatabaseTree;
import org.jtester.database.ui.wizard.DatabaseWizard;

/**
 * 数据库连接管理视图
 * 
 * @author darui.wudr
 * 
 */
public class DatabaseView extends ViewPart {
	public static String ID = "org.jtester.database.ui.DatabaseView";
	public static String MENU_ID = "#" + ID;

	private static DatabaseView instance;

	public DatabaseTree treeViewer;

	/**
	 * 处于活动状态的数据库节点
	 */
	private static DatabaseNode activeDatabase;

	/**
	 * 返回当前处于活动状态的数据库节点
	 * 
	 * @return
	 */
	public static DatabaseNode getActiveDatabase() {
		return DatabaseView.activeDatabase;
	}

	/**
	 * 返回当前选中状态的数据库节点
	 * 
	 * @return
	 */
	public static DatabaseNode getSelectedDatabase() {
		BaseTreeNode node = getInstance().treeViewer.getSelectedModel();
		return node.getRootDatabaseModel();
	}

	/**
	 * 切换当前活动数据库节点
	 * 
	 * @param databaseNode
	 */
	public static void setActiveDatabase(DatabaseNode databaseNode) {
		DatabaseView.activeDatabase = databaseNode;
	}

	/**
	 * 展开指定是数据库节点
	 * 
	 * @param databaseNode
	 */
	public static void expandDatabaseNode(DatabaseNode databaseNode) {
		getInstance().treeViewer.collapseToLevel(databaseNode, TreeViewer.ALL_LEVELS);
		getInstance().treeViewer.refresh();
		getInstance().treeViewer.expandToLevel(databaseNode, 1);
	}

	public static void disconnect(final DatabaseNode databaseNode) {
		databaseNode.disConnection();
		if (databaseNode.equals(DatabaseView.activeDatabase)) {
			DatabaseView.activeDatabase = null;
		}
	}

	/**
	 * 连接指定的数据库节点
	 * 
	 * @param databaseNode
	 * @throws Exception
	 */
	public static void connect(DatabaseNode databaseNode) throws Exception {
		databaseNode.connection();
		databaseNode.populate();
		DatabaseView.activeDatabase = databaseNode;

		// Find buffer editor
		IEditorPart bufferEditor = null;
		IEditorReference[] editorRefs = DatabaseView.getInstance().getViewSite().getPage().getEditorReferences();
		for (IEditorReference editorRef : editorRefs) {
			IEditorPart editorPart = editorRef.getEditor(false);
			if (editorPart != null && editorPart.getEditorInput() instanceof SQLEditorInput) {
				bufferEditor = editorPart;
				break;
			}
		}

		if (bufferEditor != null) {
			SQLEditor sqlEditor = (SQLEditor) bufferEditor;
			sqlEditor.setFocus();
		}
	}

	public DatabaseView() {
		instance = this;
	}

	public synchronized static DatabaseView getInstance() {
		if (instance == null) {
			instance = new DatabaseView();
		}
		return instance;
	}

	@Override
	public void createPartControl(Composite parent) {
		treeViewer = new DatabaseTree(parent, this.getViewSite());

		DropTarget target = new DropTarget(treeViewer.getControl(), DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_DEFAULT);
		target.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		final Transfer transfer = FileTransfer.getInstance();
		target.addDropListener(new DropTargetAdapter() {
			@Override
			public void drop(DropTargetEvent event) {
				DatabaseView.this.drop(event, transfer);
			}
		});
	}

	/**
	 * 处理讲资源文件拖到数据库连接视图的事件
	 * 
	 * @param event
	 * @param transfer
	 */
	private void drop(final DropTargetEvent event, final Transfer transfer) {
		if (transfer.isSupportedType(event.currentDataType) == false) {
			return;
		}
		String[] files = (String[]) event.data;
		if (files == null || files.length != 1) {
			return;
		}
		String filename = files[0];
		if (filename.toLowerCase().endsWith(".properties")) {
			DatabaseWizard.newDatabaseWizard(filename);
			MessageView.addMessage("new connection from " + filename);
		}
	}

	/**
	 * 在connection视图中增加一条连接
	 * 
	 * @param connectionModel
	 */
	public static void addConnection(DatabaseMeta databaseMeta) {
		DatabaseNode databaseNode = new DatabaseNode(databaseMeta.getManulName(), databaseMeta);
		DatabaseTree.invisibleRootModel.addChild(databaseNode);
		refresh();
	}

	/**
	 * 刷新树结构
	 */
	public static void refresh() {
		getInstance().treeViewer.refresh();
	}

	/**
	 * 刷新选中节点
	 */
	public void refreshSelectedModel() {
		BaseTreeNode model = treeViewer.getSelectedModel();

		treeViewer.collapseToLevel(model, 1);
		treeViewer.refresh();
		treeViewer.expandToLevel(model, 1);
	}

	/**
	 * @see ViewPart#setFocus
	 */
	public void setFocus() {
	}

	public static final DatabaseTree getTreeViewer() {
		return getInstance().treeViewer;
	}
}