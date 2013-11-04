package org.jtester.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.jtester.JTesterActivator;
import org.jtester.plugin.helper.PluginLogger;

public class FolderUtils {
	private static DeferredFolderDeleteThread folderDeletionThread = new DeferredFolderDeleteThread();

	static {
		Runtime.getRuntime().addShutdownHook(folderDeletionThread);
	}

	/**
	 * 系统退出时,删除文件
	 * 
	 * @param path
	 */
	public static void deleteFileStructureOnExit(File path) {
		folderDeletionThread.add(path);
	}

	/**
	 * 创建临时文件夹
	 * 
	 * @param prefix
	 * @param baseFolder
	 * @return
	 * @throws IOException
	 */
	public static File createTempFolder(String prefix, File baseFolder) throws IOException {
		File tempFile = File.createTempFile(prefix, "", baseFolder);
		if (tempFile.delete() == false || tempFile.mkdir() == false) {
			throw new IOException("Temporary folder " + tempFile.getName() + " could not be created.  Already exists?");
		}
		return tempFile;
	}

	/**
	 * 删除目录/文件 (包括子目录下所有的文件)
	 * 
	 * @param path
	 * @return
	 */
	public static boolean deleteFileStructure(File path) {
		if (path == null || !path.exists()) {
			return false;
		}
		if (path.isDirectory()) {
			File[] subFiles = path.listFiles();
			if (subFiles != null) {
				for (File sub : subFiles) {
					if (deleteFileStructure(sub) == false) {
						return false;
					}
				}
			}
		}
		return path.delete();
	}

	/**
	 * 用于延时删除文件或目录<br>
	 * 有些文件无法立即删除
	 * 
	 * @author darui.wudr
	 * 
	 */
	protected static class DeferredFolderDeleteThread extends Thread {
		private ArrayList<File> folderList = new ArrayList<File>();

		public synchronized void add(File path) {
			this.folderList.add(path);
		}

		public void run() {
			synchronized (this) {
				Iterator<File> iterator = this.folderList.iterator();
				while (iterator.hasNext()) {
					FolderUtils.deleteFileStructure(iterator.next());
					iterator.remove();
				}
			}
		}
	}

	/**
	 * 读取文件中的文本内容
	 * 
	 * @param file
	 * @return
	 */
	public static String readFile(File file) {
		if (!file.exists()) {
			return file.toString() + " not exists";
		}
		try {
			InputStream stream = new FileInputStream(file);
			String text = convertStreamToString(stream);
			return text;
		} catch (Throwable e) {
			return "open file error," + e.getMessage();
		}
	}

	/*
	 * To convert the InputStream to String we use the BufferedReader.readLine()
	 * method. We iterate until the BufferedReader return null which means
	 * there's no more data to read. Each line will appended to a StringBuilder
	 * and returned as String.
	 */
	public static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder buffer = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return buffer.toString();
	}

	public static final String TEMP_PATH_PROP = "java.io.tmpdir";
	public static final String USER_DIR_PROP = "user.dir";
	public static final String USER_HOME_PROP = "user.home";

	public final static String USER_HOME = System.getProperty(FolderUtils.USER_HOME_PROP) + File.separatorChar
			+ "jtester";

	/**
	 * 将文件copy到user home对应的目录下
	 * 
	 * @param toPath
	 *            plugin的相对路径
	 * @param filename
	 *            userhome下的相对路径
	 */
	public static void copyFileToUserHome(String from_path, String to_path) {
		File to_file = new File(FolderUtils.USER_HOME + File.separatorChar + to_path);
		if (to_file.exists()) {
			return;
		}
		if (to_file.getParentFile().exists() == false) {
			to_file.getParentFile().mkdirs();
		}
		try {
			InputStream in = FileLocator.openStream(JTesterActivator.getDefault().getBundle(), new Path(from_path), false);

			writeStream(in, FolderUtils.USER_HOME + File.separatorChar + to_path);

		} catch (Exception e) {
			PluginLogger.log(e);
		}
	}

	private static final int BUFFER = 2048;

	/**
	 * 将inputstream流中的内容写到toFile文件中
	 * 
	 * @param in
	 * @param toFile
	 * @throws Exception
	 */
	public static void writeStream(InputStream in, String toFile) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(in);
		File file = new File(toFile);

		File parent = file.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		FileOutputStream fos = new FileOutputStream(file);
		BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

		byte data[] = new byte[BUFFER];
		int count = bis.read(data, 0, BUFFER);
		while (count != -1) {
			bos.write(data, 0, count);
			count = bis.read(data, 0, BUFFER);
		}
		bos.flush();
		bos.close();
		bis.close();
	}
}