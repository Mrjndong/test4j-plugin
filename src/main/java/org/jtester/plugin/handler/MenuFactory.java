package org.jtester.plugin.handler;

import org.eclipse.ui.IViewSite;
import org.jtester.database.handler.CopyAsCheckDataMenu;
import org.jtester.database.handler.CopyAsInsertDataMenu;
import org.jtester.database.handler.CopySpecFieldsAsInsertDataMenu;
import org.jtester.database.handler.CopySpecFieldsAsQueryDataMenu;
import org.jtester.database.handler.DBConnectionCloseMenu;
import org.jtester.database.handler.DBConnectionDoingMenu;
import org.jtester.database.handler.DBConnectionEditMenu;
import org.jtester.database.handler.DBConnectionNewMenu;
import org.jtester.database.handler.DBConnectionRemoveMenu;
import org.jtester.database.handler.DBConnectionSelectMenu;
import org.jtester.database.handler.DBTableCountQueryMenu;
import org.jtester.database.handler.DBTableQueryMenu;
import org.jtester.database.handler.DBTreeRefreshMenu;
import org.jtester.database.handler.GenerateDataMenu;
import org.jtester.database.handler.RefreshDataMenu;
import org.jtester.ibatis.handler.DomainEntityMenu;
import org.jtester.ibatis.handler.DomainSqlmapMenu;

/**
 * 一般上下文菜单构造工厂类
 * 
 * @author darui.wudr
 * 
 */
public class MenuFactory {
	/**
	 * 新建数据库连接菜单
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem newConnection(IViewSite viewSite) {
		return new DBConnectionNewMenu(viewSite);
	}

	/**
	 * 移除连接菜单
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem removeConnection(IViewSite viewSite) {
		return new DBConnectionRemoveMenu(viewSite);
	}

	/**
	 * 编辑数据库连接信息菜单
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem editConnection(IViewSite viewSite) {
		return new DBConnectionEditMenu(viewSite);
	}

	/**
	 * 刷新视图菜单
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem refreshTreeView(IViewSite viewSite) {
		return new DBTreeRefreshMenu(viewSite);
	}

	/**
	 * 连接数据库
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem doconnect(IViewSite viewSite) {
		return new DBConnectionDoingMenu(viewSite);
	}

	/**
	 * 切换选中的数据库
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem changeConnction(IViewSite viewSite) {
		return new DBConnectionSelectMenu(viewSite);
	}

	/**
	 * 中断的数据库连接
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem disconnect(IViewSite viewSite) {
		return new DBConnectionCloseMenu(viewSite);
	}

	/**
	 * 执行 select count(*) from table的操作
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem countQuery(IViewSite viewSite) {
		return new DBTableCountQueryMenu(viewSite);
	}

	/**
	 * 执行 select * from table的操作
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem query(IViewSite viewSite) {
		return new DBTableQueryMenu(viewSite);
	}

	/**
	 * 拷贝为插入数据的java代码
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem copyAsInsertData(IViewSite viewSite) {
		return new CopyAsInsertDataMenu(viewSite);
	}

	/**
	 * 拷贝为插入数据的java代码
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem copyAsQueryJava(IViewSite viewSite) {
		return new CopyAsCheckDataMenu(viewSite);
	}

	/**
	 * 拷贝为插入数据格式(java代码模式)
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem copySpecFieldsAsInsertJava(IViewSite viewSite) {
		return new CopySpecFieldsAsInsertDataMenu(viewSite);
	}

	/**
	 * 拷贝为验证数据格式(java代码模式)
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem copySpecFieldsAsQueryJava(IViewSite viewSite) {
		return new CopySpecFieldsAsQueryDataMenu(viewSite);
	}

	/**
	 * 刷新数据
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem refreshData(IViewSite viewSite) {
		return new RefreshDataMenu(viewSite);
	}

	/**
	 * 生成随机数据
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem generateData(IViewSite viewSite) {
		return new GenerateDataMenu(viewSite);
	}

	/**
	 * 执行 生成Domain Entity操作
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem generateDomainEntity(IViewSite viewSite) {
		return new DomainEntityMenu(viewSite);
	}

	/**
	 * 执行 生成Domain Sqlmap操作
	 * 
	 * @param viewSite
	 * @return
	 */
	public static BaseMenuItem generateDomainSqlmap(IViewSite viewSite) {
		return new DomainSqlmapMenu(viewSite);
	}
}
