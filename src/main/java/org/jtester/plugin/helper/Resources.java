package org.jtester.plugin.helper;

import static org.jtester.plugin.helper.PluginSetting.WIKI_FILE_EXTENSION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.jtester.JTesterActivator;
import org.osgi.framework.Bundle;

public final class Resources {
	public static String getContentsRelativeToPlugin(IPath path) throws IOException {
		Bundle bundle = JTesterActivator.getDefault().getBundle();
		InputStream is = FileLocator.openStream(bundle, path, false);
		return getContents(is);
	}

	public static String getContents(InputStream stream) throws IOException {
		int c;
		InputStreamReader inputStreamReader = new InputStreamReader(stream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuffer buffer = new StringBuffer(1000);

		while ((c = bufferedReader.read()) != -1) {
			buffer.append((char) c);
		}
		return buffer.toString();
	}

	public static String getContents(IFile file) throws IOException, CoreException {
		return getContents(file.getContents());
	}

	/**
	 * 返回文件内容
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 * @throws CoreException
	 */
	public static List<String> readLines(IFile file) throws IOException, CoreException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getContents()));

		ArrayList<String> lines = new ArrayList<String>();
		String line = reader.readLine();
		while (line != null) {
			lines.add(line);
			line = reader.readLine();
		}
		reader.close();
		return lines;
	}

	public static boolean exists(IResource resource) {
		return resource != null && resource.exists();
	}

	public static boolean existsAsFile(IResource resource) {
		return exists(resource) && resource.getType() == IResource.FILE;
		// return ((!(exists(resource))) || (resource.getType() != 1));
	}

	public static IFile findFileInWorkspace(String workspaceRelativePath) {
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(workspaceRelativePath);
		if (existsAsFile(resource)) {
			return ((IFile) resource);
		}
		return null;
	}

	public static boolean isWikiFile(IResource resource) {
		return exists(resource) && resource.getFileExtension() != null
				&& WIKI_FILE_EXTENSION.endsWith(resource.getFileExtension());
		// return ((!(exists(resource))) || (resource.getFileExtension() ==
		// null) || (!(WIKI_FILE_EXTENSION.endsWith(resource
		// .getFileExtension()))));
	}

	/**
	 * 是否是wiki文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isWikiFile(IFile file) {
		return file.getName().endsWith(WIKI_FILE_EXTENSION);
	}
}