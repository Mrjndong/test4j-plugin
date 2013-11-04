package org.jtester.wiki.assistor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.jtester.utils.FolderUtils;


/**
 * 准备dbfit files
 * 
 * @author darui.wudr
 * 
 */
public class PrepareDbFitFiles {
	/**
	 * 将fitnesse的css，js文件拷贝到用户默认目录下<br>
	 * 将ckeditor文件copy到用户默认路径下解压
	 * 
	 * @throws Exception
	 */

	public static void copyDbFitFiles() {
		FolderUtils.copyFileToUserHome("files/ckeditor.zip", "ckeditor.zip");
		FolderUtils.copyFileToUserHome("files/fitnesse.css", "fitnesse.css");
		FolderUtils.copyFileToUserHome("files/EditDbFit.html", "EditDbFit.html");
		unzipFiles();

	}

	private static final int BUFFER = 2048;

	public static void unzipFiles() {
		try {
			String fileName = FolderUtils.USER_HOME + File.separatorChar + "ckeditor.zip";
			String filePath = FolderUtils.USER_HOME + File.separatorChar + "ckeditor" + File.separatorChar;
			ZipFile zipFile = new ZipFile(fileName);
			Enumeration<? extends ZipEntry> entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				// 建目录在建文件时会建，空目录忽略。
				if (entry.isDirectory()) {
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				File file = new File(filePath + entry.getName());

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
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
