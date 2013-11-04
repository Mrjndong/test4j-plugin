package org.test4j.plugin.database.ui.tree;

import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewSite;
import org.test4j.plugin.database.handler.DBConnectionDoingMenu;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.handler.MenuFactory;

/**
 * 数据库结构树状图
 * 
 * @author darui.wudr
 */
public class DatabaseTree extends TreeViewer {
    public static String        MENU_ID            = "#" + DatabaseView.ID;

    public static InvisibleNode invisibleRootModel = new InvisibleNode("invisible model");

    private final IViewSite     viewSite;

    /**
     * 数据库连接树状图
     * 
     * @param parent
     * @param view
     */
    public DatabaseTree(final Composite parent, final IViewSite viewSite) {
        super(parent);
        this.viewSite = viewSite;

        this.setContentProvider(new ITreeContentProvider() {
            public void dispose() {
            }

            public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            }

            public Object[] getElements(Object inputElement) {
                return getChildren(inputElement);
            }

            public Object[] getChildren(Object parentElement) {
                if (parentElement instanceof BaseTreeNode) {
                    return ((BaseTreeNode) parentElement).getChildren();
                }
                return new Object[0];
            }

            public Object getParent(Object element) {
                if (element instanceof BaseTreeNode) {
                    return ((BaseTreeNode) element).getParent();
                } else {
                    return null;
                }
            }

            public boolean hasChildren(Object element) {
                if (element instanceof BaseTreeNode) {
                    return ((BaseTreeNode) element).hasChildren();
                } else {
                    return false;
                }
            }
        });
        this.setLabelProvider(new LabelProvider() {
            @Override
            public Image getImage(Object model) {
                Image image = ((BaseTreeNode) model).getImage();
                return image;
            }

            @Override
            public String getText(Object model) {
                String text = ((BaseTreeNode) model).getText();
                return text;
            }
        });
        this.setInput(invisibleRootModel);

        this.addTreeListener(new ITreeViewerListener() {
            public void treeCollapsed(TreeExpansionEvent event) {
            }

            public void treeExpanded(TreeExpansionEvent event) {
                BaseTreeNode node = (BaseTreeNode) event.getElement();
                if (node == null) {
                    return;
                }
                DatabaseTree.this.setTreeBusy(false);
                DatabaseTree.this.expandNode(node);
            }

        });
        this.addDoubleClickListener(new IDoubleClickListener() {
            public void doubleClick(DoubleClickEvent event) {
                BaseTreeNode node = (BaseTreeNode) ((StructuredSelection) DatabaseTree.this.getSelection())
                        .getFirstElement();
                if (node == null) {
                    return;
                }
                if (node.getNodeType() == NodeType.Database) {
                    DBConnectionDoingMenu.doConnection();
                }
                DatabaseTree.this.expandNode(node);
            }
        });

        Menu menu = this.createContextMenu();
        this.getControl().setMenu(menu);
    }

    /**
     * 创建上下文菜单
     * 
     * @return
     */
    private Menu createContextMenu() {
        MenuManager menuMgr = new MenuManager("Test4J Menu", MENU_ID);
        this.viewSite.registerContextMenu(menuMgr, this);

        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager menuMgr) {
                final BaseTreeNode currSelModel = DatabaseView.getTreeViewer().getSelectedModel();

                if (currSelModel == null) {
                    menuMgr.add(MenuFactory.newConnection(viewSite));
                    return;
                }

                NodeType type = currSelModel.getNodeType();
                if (type != NodeType.Column) {
                    menuMgr.add(MenuFactory.refreshTreeView(viewSite));
                    menuMgr.add(new Separator());
                }

                switch (type) {
                    case Database:
                        contextMenuFor(menuMgr, (DatabaseNode) currSelModel);
                        break;
                    case Table:
                        menuMgr.add(MenuFactory.query(viewSite));
                        menuMgr.add(MenuFactory.countQuery(viewSite));
                        menuMgr.add(new Separator());
                        menuMgr.add(MenuFactory.generateDomainSqlmap(viewSite));
                        menuMgr.add(MenuFactory.generateDomainEntity(viewSite));
                        break;
                    case Column:
                        menuMgr.add(MenuFactory.query(viewSite));
                        break;
                }
            }
        });

        Menu menu = menuMgr.createContextMenu(this.getControl());
        return menu;
    }

    /**
     * 展开树形节点事件处理
     * 
     * @param event
     */
    private void expandNode(BaseTreeNode node) {
        node.refresh();

        this.collapseToLevel(node, TreeViewer.ALL_LEVELS);
        this.refresh();
        this.expandToLevel(node, 1);
    }

    /**
     * 生成database节点的上下文菜单
     * 
     * @param menuMgr
     * @param model
     */
    private void contextMenuFor(final IMenuManager menuMgr, final DatabaseNode model) {
        if (model.isConnected() == false) {
            menuMgr.add(MenuFactory.doconnect(viewSite));
        } else {
            menuMgr.add(MenuFactory.changeConnction(viewSite));
            menuMgr.add(MenuFactory.disconnect(viewSite));
        }

        menuMgr.add(new Separator());
        menuMgr.add(MenuFactory.newConnection(viewSite));
        menuMgr.add(MenuFactory.editConnection(viewSite));
        menuMgr.add(MenuFactory.removeConnection(viewSite));
    }

    public void setTreeBusy(boolean busy) {
        this.setBusy(busy);
    }

    /**
     * 返回选中的节点model
     * 
     * @return
     */
    public BaseTreeNode getSelectedModel() {
        return (BaseTreeNode) ((StructuredSelection) this.getSelection()).getFirstElement();
    }

    /**
     * 根据选中的树节点构造select语句
     * 
     * @return
     */
    public String buildQueryStatment() {
        BaseTreeNode model = this.getSelectedModel();
        IStructuredSelection selection = (IStructuredSelection) getSelection();
        String query = model.buildQuery(selection);
        return query;
    }

    /**
     * 返回当前表名称
     * 
     * @return
     */
    public String getSelectTable() {
        BaseTreeNode model = this.getSelectedModel();
        if (model instanceof TableNode) {
            return model.getName();
        }
        if (model instanceof ColumnNode) {
            String text = model.getParent().getName();
            return text;
        }
        return null;
    }
}
