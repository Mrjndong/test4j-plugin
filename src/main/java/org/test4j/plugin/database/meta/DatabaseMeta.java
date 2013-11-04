package org.test4j.plugin.database.meta;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.test4j.plugin.utils.PropertiesUtil;

/**
 * 数据库连接元信息
 * 
 * @author darui.wudr
 * 
 */
public class DatabaseMeta {
	/**
	 * eclipse 中M2_REPO的值
	 */
	private static String M2_REPO = null;
	static {
		try {
			IPath path = (IPath) JavaCore.getClasspathVariable("M2_REPO");
			M2_REPO = path.toString();
		} catch (Throwable e) {
			e.printStackTrace();
			M2_REPO = "${M2_REPO}";
		}
	}

	private String manulName = "";
	/**
	 * 要展现的shema视图
	 */
	private String schema = "";
	/**
	 * 数据库类型
	 */
	private DatabaseType databaseType;

	/**
	 * catalog名称
	 */
	private String catalog = "";
	/**
	 * 连接用户
	 */
	private String username = "";
	/**
	 * 连接密码
	 */
	private String password = "";
	/**
	 * 连接url
	 */
	private String url = "";
	/**
	 * driver class
	 */
	private String driverClazz = "";

	/**
	 * driver jar路径名称，以;分割
	 */
	private String driverJars = "";
	/**
	 * driver jar的路径
	 */
	private URL[] driverURLs = null;

	private boolean showTables = true;

	private boolean showViews = false;

	public DatabaseMeta() {

	}

	/**
	 * 从properties文件中解析
	 * 
	 * @param propFile
	 */
	public DatabaseMeta(String propFile) {
		Properties p = PropertiesUtil.loadProperties(propFile);

		this.setUsername(p.getProperty("database.type", ""));
		this.setDatabaseType(p.getProperty("database.type", ""));
		this.setSchema(p.getProperty("database.schemaNames", ""));
		this.setUsername(p.getProperty("database.userName", ""));
		this.setPassword(p.getProperty("database.password", ""));
		this.setUrl(p.getProperty("database.url", ""));

		this.setDriverClazz(p.getProperty("database.driverClassName", ""));
		this.setDriverJars(p.getProperty("database.driverJar", ""));
	}

	/**
	 * 验证参数的有效性
	 * 
	 * @return
	 */
	public final boolean validate() {
		if (databaseType == null) {
			return false;
		}
		if (schema.length() == 0 || username.length() == 0 || url.length() == 0 || driverClazz.length() == 0
				|| driverURLs.length == 0) {
			return false;
		} else {
			return true;
		}
	}

	private Connection connection = null;

	/**
	 * 数据库处于连接状态
	 * 
	 * @return
	 */
	public final boolean isConnected() {
		try {
			return connection != null && connection.isClosed() == false;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public final Connection getConnection() {
		try {
			if (connection == null || connection.isClosed()) {
				this.connection = this.connect();
			}
			return connection;
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 根据参数连接数据库
	 * 
	 * @throws Exception
	 */
	private final Connection connect() throws Exception {
		URLClassLoader loader = new URLClassLoader(this.driverURLs);
		Class<?> driverClass = loader.loadClass(driverClazz);
		Driver driver = (Driver) driverClass.newInstance();

		Properties props = new Properties();
		props.put("user", username);
		props.put("password", password);

		Connection connection = driver.connect(url, props);
		if (connection == null) {
			throw new RuntimeException("Connection Invalid, please verify URL.");
		} else {
			return connection;
		}
	}

	/**
	 * 返回schema下所有的数据表名称
	 * 
	 * @param types
	 *            {"TABLE","VIEW"}
	 * @return
	 */
	public List<String> getTableNames(String types[]) {
		try {
			if (connection == null || connection.isClosed()) {
				this.connection = this.connect();
			}
			DatabaseMetaData dbmd = connection.getMetaData();

			ResultSet rsTables = dbmd.getTables(this.getCatalog(), this.getSchema(), null, types);
			List<String> tables = new ArrayList<String>();
			while (rsTables.next()) {
				String table = rsTables.getString("TABLE_NAME");
				tables.add(table);
			}

			rsTables.close();
			return tables;
		} catch (Exception e) {
			if (e instanceof RuntimeException) {
				throw (RuntimeException) e;
			} else {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 分离jar文件路径
	 * 
	 * @param jars
	 * @return
	 */
	private static URL[] getStringList(String driverjar) {
		List<URL> list = new ArrayList<URL>();
		if (driverjar == null) {
			return new URL[0];
		}
		String[] jars = driverjar.split("[;\n\r]");
		if (jars == null || jars.length == 0) {
			return new URL[0];
		}
		try {
			for (String jar : jars) {
				if (jar == null) {
					continue;
				}
				String str = jar.trim();
				if ("".equals(str)) {
					continue;
				}
				str = str.replace("${M2_REPO}", M2_REPO);
				URL url = new File(str).toURI().toURL();
				list.add(url);
			}
			return list.toArray(new URL[0]);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 没有实质意义，用于给连接一个人可识别的名称
	 */
	public String getManulName() {
		if (this.manulName != null && "".equals(this.manulName.trim()) == false) {
			return this.manulName;
		} else {
			return this.schema;
		}
	}

	public void setManulName(String manulName) {
		this.manulName = manulName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema.toUpperCase();
		this.catalog = this.schema;
	}

	public String getCatalog() {
		return catalog;
	}

	public DatabaseType getDatabaseType() {
		return databaseType;
	}

	public String getDatabaseTypeStr() {
		return databaseType == null ? "" : databaseType.name();
	}

	public void setDatabaseType(DatabaseType databaseType) {
		this.databaseType = databaseType;
	}

	public void setDatabaseType(String databaseType) {
		this.databaseType = DatabaseType.dbType(databaseType);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDriverClazz() {
		return driverClazz;
	}

	public void setDriverClazz(String driverClazz) {
		this.driverClazz = driverClazz;
	}

	public String getDriverJars() {
		return driverJars;
	}

	public void setDriverJars(String driverJars) {
		this.driverJars = driverJars.replace(';', '\n');
		this.driverURLs = getStringList(driverJars);
	}

	public boolean isShowTables() {
		return showTables;
	}

	public void setShowTables(boolean showTables) {
		this.showTables = showTables;
	}

	public boolean isShowViews() {
		return showViews;
	}

	public void setShowViews(boolean showViews) {
		this.showViews = showViews;
	}
}
