package org.jtester.wiki.assistor;

import static org.jtester.plugin.helper.PluginSetting.DEFAULT_CHARSET;
import static org.jtester.plugin.helper.PluginSetting.WIKI_FILE_EXTENSION;

import java.io.File;
import java.nio.charset.Charset;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.helper.Resources;
import org.jtester.utils.ClazzUtil;
import org.jtester.utils.FolderUtils;

public class WikiHelper {
	/**
	 * 获得viewer中需要补全java代码的prefix text
	 * 
	 * @param viewer
	 * @param documentOffset
	 * @return
	 * @throws BadLocationException
	 */
	public static String getTextToCompleteJava(ITextViewer viewer, int documentOffset) throws BadLocationException {
		IDocument document = viewer.getDocument();
		int characterIndex = documentOffset - 1;
		if (characterIndex < 0 || !ClazzUtil.isJavaClassNamePart(document.getChar(characterIndex))) {
			return null;
		}
		int start = characterIndex;
		do {
			start--;
		} while (start > 0 && ClazzUtil.isJavaClassNamePart(document.getChar(start)));

		while (start < characterIndex && !Character.isJavaIdentifierPart(document.getChar(start))) {
			++start;
		}
		String prefix = document.get(start, characterIndex - start + 1);
		if (".".equals(prefix)) {
			return null;
		}
		return prefix;
	}

	/**
	 * 获取文件编码<br>
	 * 默认值是utf-8
	 * 
	 * @param document
	 * @return
	 */
	public static Charset charset(final IFile document) {
		try {
			return Charset.forName(document.getCharset());
		} catch (CoreException e) {
			PluginLogger.log("Unable to get charset", e);
		}
		return Charset.forName(DEFAULT_CHARSET);
	}

	/**
	 * 返回container中指定的文件
	 * 
	 * @param container
	 * @param localFileName
	 * @return
	 */
	public static IFile getLocalFile(final IContainer container, final String localFileName) {
		Path path = new Path(localFileName);
		return container.getFile(path);
	}

	/**
	 * 根据wiki名称返回container中的wiki文件
	 * 
	 * @param container
	 * @param wikiName
	 * @return
	 */
	public static IFile getWikiFileByName(final IContainer container, final String wikiName) {
		if (!Resources.exists(container)) {
			return null;
		}
		IResource resource = container.findMember(wikiName + WIKI_FILE_EXTENSION);
		if (!Resources.existsAsFile(resource)) {
			return null;
		}
		return (IFile) resource;
	}

	/**
	 * 是否是临时的wiki文件
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isTempWiki(IFile file) {
		if (file.getProject() == null || !file.getProject().isOpen()) {
			return true;
		}
		return "wiki_temp".equals(file.getProject().getName());
	}

	final static String DBFIT_OUTPUT = "/target/dbfit";// TODO

	/**
	 * 返回wiki文件的测试结果html文件
	 * 
	 * @param project
	 * @param wiki
	 * @return
	 * @throws Exception
	 */
	public static File getDBFitTestHtml(IProject project, IFile wiki) throws Exception {
		IPath wikiPath = wiki.getFullPath();
		IJavaProject javaprj = JavaCore.create(project);

		IPackageFragmentRoot[] roots = javaprj.getAllPackageFragmentRoots();
		for (IPackageFragmentRoot root : roots) {

			if (root.getPath().isPrefixOf(wikiPath)) {
				String clazpath = wikiPath.toOSString().substring(root.getPath().toOSString().length());
				int last = clazpath.lastIndexOf(WIKI_FILE_EXTENSION);
				String temp = DBFIT_OUTPUT + File.separatorChar + clazpath.substring(0, last) + ".html";
				String path = project.getFile(temp).getLocation().toString();
				return new File(path);
			}
		}
		return null;
	}

	/**
	 * 返回fitnesse默认的的css url<br>
	 * o fitnesse.css url <br>
	 * o other todo
	 * 
	 * @return
	 */
	public static String getFitNesseStyleUrl() {

		StringBuffer url = new StringBuffer();
		url.append(String.format("<link rel='stylesheet' media='all' type='text/css' href='file:///%s/%s' />",
				FolderUtils.USER_HOME, "fitnesse.css"));
		url.append("\n");

		return url.toString();
	}
}
