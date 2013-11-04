package org.jtester.wiki.ui.text.matcher;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.utils.ClazzUtil;
import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.ui.text.region.JavaTypeTextRegion;
import org.jtester.wiki.ui.text.region.TextRegion;

public final class JavaTypeMatcher extends AbstractTextRegionMatcher {
	public TextRegion createTextRegion(String text, WikiFile wikiFile) {
		IJavaProject javaProject = wikiFile.getJavaProject();
		try {
			if (!(isCandidateForTextRegion(text, wikiFile))) {
				return null;
			}
			text = stripBadChars(text);

			IType type = javaProject.findType(text);
			if (type != null) {
				return new JavaTypeTextRegion(text, type);
			}

			int packageNameLength = findPackageNameLength(javaProject, text);
			type = findType(javaProject, text, packageNameLength);
			if (type != null) {
				return new JavaTypeTextRegion(type.getFullyQualifiedName().replaceAll("\\$", "."), type);
			}
			return null;
		} catch (JavaModelException e) {
			PluginLogger.log("Could not create TextRegion", e);
		}
		return null;
	}

	private boolean isCandidateForTextRegion(String text,  WikiFile wikiFile) {
		if ((!(wikiFile.isJavaProject())) || (text.length() == 0) || (!(accepts(text.charAt(0), true)))) {
			return false;
		}
		return wikiFile.startsWithPackageName(text);
	}

	private IType findType(IJavaProject javaProject, String text, int packageEnd) throws JavaModelException {
		int index = text.indexOf(46, packageEnd + 1);
		if (index < 0) {
			index = packageEnd + 1;
		}

		IType match = null;
		while ((index > 0) && (index <= text.length())) {
			String candidate = new String(text.substring(0, index));
			IType javaType = javaProject.findType(candidate);
			if (javaType == null) {
				return match;
			}
			match = javaType;
			index = text.indexOf(46, index + 1);
		}
		return match;
	}

	private int findPackageNameLength(IJavaProject javaProject, String text) throws JavaModelException {
		int index = text.indexOf(46);
		if (index < 0) {
			index = text.length();
		}

		int match = 0;
		while ((index > 0) && (index <= text.length())) {
			String candidate = new String(text.substring(0, index));
			IPath path = path(candidate);
			IJavaElement javaElement = javaProject.findElement(path);
			if (javaElement == null) {
				return match;
			}
			match = index;
			index = text.indexOf(46, index + 1);
		}
		return match;
	}

	private String stripBadChars(String text) {
		if (text.length() == 0) {
			return text;
		}
		int maxIndex = maxLengthOfValidCharacters(text);
		maxIndex = lengthWithDotsStripped(text, maxIndex);
		return new String(text.substring(0, maxIndex + 1));
	}

	private int lengthWithDotsStripped(String text, int maxIndex) {
		while ((maxIndex > 0) && (text.charAt(maxIndex) == '.')) {
			--maxIndex;
		}
		return maxIndex;
	}

	private int maxLengthOfValidCharacters(String text) {
		int maxIndex = 0;
		do {
			++maxIndex;

			if (maxIndex >= text.length())
				break;
		} while (accepts(text.charAt(maxIndex), maxIndex == 0));

		--maxIndex;
		return maxIndex;
	}

	private IPath path(String fullyQualifiedType) {
		return new Path(fullyQualifiedType.replaceAll("\\.", "/"));
	}

	protected boolean accepts(char c, boolean firstCharacter) {
		if (firstCharacter) {
			return Character.isJavaIdentifierPart(c);
		}
		return ClazzUtil.isJavaClassNamePart(c);
	}
}