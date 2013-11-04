package org.jtester.testsequence.assistor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.IJavaProject;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.helper.PluginSetting;
import org.jtester.tools.commons.ResourceHelper;

public class MethodSequence {
	private String testMethod;

	private String testClass;

	private IJavaProject testProject;

	public String getTestMethod() {
		return testMethod;
	}

	public String getTestClass() {
		return testClass;
	}

	public IJavaProject getTestProject() {
		return testProject;
	}

	public void setTestMethod(String testMethodClaz, String testMethodName) {
		this.testClass = testMethodClaz;
		this.testMethod = testMethodName;
	}

	public void setTestProject(IJavaProject testMethodProj) {
		this.testProject = testMethodProj;
	}

	/**
	 * 获取本地sequence列表
	 * 
	 * @return
	 */
	public String[] getLocalSequences() {
		String basedir = this.getBaseDir();
		File dirFile = new File(basedir);
		if (!dirFile.exists()) {
			return new String[0];
		}
		File[] files = dirFile.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				if (!name.startsWith(MethodSequence.this.testMethod + ".")) {
					return false;
				}
				if (!name.matches(".*\\.\\d{6}\\.\\d{6}\\.xml")) {
					return false;
				}
				return true;
			}

		});
		if (files == null || files.length == 0) {
			return new String[0];
		}
		List<String> names = new ArrayList<String>();
		for (File file : files) {
			names.add(file.getName());
		}

		return names.toArray(new String[0]);
	}

	private static String SEQUENCE_LIST = "select * from test_sequence where test_clazz=? and test_method=?";

	/**
	 * 获取远程sequence列表
	 * 
	 * @return
	 */
	public String[] getRemoteSequences() {
		try {
			Connection conn = MethodSequence.connection();
			PreparedStatement statement = conn.prepareStatement(SEQUENCE_LIST);
			statement.setString(1, this.testClass);
			statement.setString(2, this.testMethod);

			List<String> list = new ArrayList<String>();
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				String user = rs.getString("test_user");
				String time = rs.getString("test_time");
				list.add("db://" + time + "@" + user);
			}
			rs.close();
			statement.close();
			conn.close();
			return list.toArray(new String[0]);
		} catch (Exception e) {
			PluginLogger.log(e);
			return new String[0];
		}
	}

	private static String SPEC_SEQUENCE = "select test_log from test_sequence where test_user=? and test_clazz=? and test_method=? and test_time=?";

	/**
	 * 从远程数据库读取test sequence信息
	 * 
	 * @param sequenceID
	 *            构造如"db://yyyy-MM-dd HH:mm:ss@User"
	 * @return
	 */
	public String readSequenceFromRemote(String sequenceID) {
		int index = sequenceID.indexOf('@');
		String time = sequenceID.substring(5, index);
		String user = sequenceID.substring(index + 1);
		String testLog = "Nothing";
		try {
			Connection conn = MethodSequence.connection();
			PreparedStatement statement = conn.prepareStatement(SPEC_SEQUENCE);
			statement.setString(1, user);
			statement.setString(2, this.testClass);
			statement.setString(3, this.testMethod);
			statement.setString(4, time);

			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				testLog = rs.getString("test_log");
			}
			rs.close();
			statement.close();
			conn.close();
			return testLog;
		} catch (Exception e) {
			PluginLogger.log(e);
			return testLog;
		}
	}

	/**
	 * 从本地文件中读取test sequence信息
	 * 
	 * @param filename
	 * @return
	 */
	public String readSequenceFromLocal(String filename) {
		String file = this.getBaseDir() + filename;
		try {
			String context = ResourceHelper.readFromFile(new File(file));
			return context;
		} catch (FileNotFoundException e) {
			return "read context from file:" + file + " error, " + e.getMessage();
		}
	}

	/**
	 * 返回本地文件夹中最后一个test sequence文件
	 * 
	 * @return
	 */
	public File getLastSequenceFile() {
		String[] files = this.getLocalSequences();

		if (files == null || files.length == 0) {
			return null;
		}
		String last = null;
		for (String temp : files) {
			if (last == null) {
				last = temp;
			} else if (temp.compareTo(last) > 0) {
				last = temp;
			}
		}
		return new File(this.getBaseDir() + "/" + last);
	}

	public String getBaseDir() {
		// 读取配置 TODO
		File file = this.testProject.getProject().getLocation().makeAbsolute().toFile();
		String dir = file.getAbsolutePath() + "/target/tracer/" + this.testClass.replace('.', '/') + "/";
		return dir;
	}

	private static String dbUrl = "";

	private static String dbUsr = "";

	private static String dbPwd = "";

	/**
	 * 设置远程数据库连接信息
	 * 
	 * @param dbUrl
	 * @param dbUsr
	 * @param dbPwd
	 */
	public static void setDatabase(String dbUrl, String dbUsr, String dbPwd) {
		MethodSequence.dbUrl = dbUrl;
		MethodSequence.dbUsr = dbUsr;
		MethodSequence.dbPwd = dbPwd;
	}

	/**
	 * 设置远程数据库连接信息，设置值从plugin中读取
	 */
	public static void setDatabase() {
		MethodSequence.dbUrl = PluginSetting.getDbUrl();
		MethodSequence.dbUsr = PluginSetting.getDbUsr();
		MethodSequence.dbPwd = PluginSetting.getDbPwd();
	}

	public static Connection connection() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(dbUrl, dbUsr, dbPwd);

		if (conn.isClosed()) {
			throw new RuntimeException("Failed connecting to the Database, the connection has been closed!");
		} else {
			System.out.println("Succeeded connecting to the Database!");
			return conn;
		}
	}
}
