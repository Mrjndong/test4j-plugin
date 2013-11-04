package org.jtester.wiki.assistor;

import static org.jtester.plugin.helper.PluginSetting.WIKI_FILE_EXTENSION;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.jtester.plugin.helper.PluginHelper;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.helper.Resources;
import org.jtester.utils.JavaPackages;

public class WikiFile {
	private final IFile wikiFile;

	public WikiFile(IFile wikiFile) {
		this.wikiFile = wikiFile;
	}

	public IContainer getWorkingLocation() {
		return this.wikiFile.getParent();
	}

	/**
	 * 获取wiki文件所关联的project<br>
	 * 
	 * @return workingLocation.project
	 */
	public IProject getProject() {
		return getWorkingLocation().getProject();
	}

	/**
	 * 是否是java工程
	 * 
	 * @return
	 */
	public boolean isJavaProject() {
		IProject project = this.getProject();
		try {
			return PluginHelper.isJavaProject(project);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public IJavaProject getJavaProject() {
		boolean isJavaProject = this.isJavaProject();
		if (isJavaProject) {
			return JavaCore.create(getWorkingLocation().getProject());
		} else {
			throw new RuntimeException("this project isn't a java project.");
		}
	}

	/**
	 * 获得的wiki的文件名(去除后缀)
	 * 
	 * @return
	 */
	public String getWikiNameBeingEdited() {
		String fileName = this.wikiFile.getName();
		int last = fileName.lastIndexOf(WIKI_FILE_EXTENSION);
		return fileName.substring(0, last);
	}

	/**
	 * 读取wiki文件内容
	 * 
	 * @return
	 */
	public String[] getDocument() {
		try {
			List<String> lines = Resources.readLines(this.wikiFile);
			return lines.toArray(new String[0]);
		} catch (Exception e) {
			PluginLogger.log("read wiki context error", e);
			return new String[0];
		}
	}

	public IFile getWikiFile() {
		return wikiFile;
	}

	public synchronized boolean startsWithPackageName(String text) {
		if (!this.isJavaProject()) {
			return false;
		}
		List<String> packages = JavaPackages.getPackages(this.getJavaProject());
		if (packages.contains(text)) {
			return true;
		}

		for (String _package : packages) {
			if (text.startsWith(_package)) {
				return true;
			}
		}
		return false;
	}
}
