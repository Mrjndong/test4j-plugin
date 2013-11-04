package org.jtester.wiki.ui.text.matcher;

import static org.jtester.wiki.ui.text.region.TextRegion.DBFIT_LINK_PREFIX;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.ui.text.region.DbFitTextRegion;
import org.jtester.wiki.ui.text.region.TextRegion;

public final class DbFitRegionMatcher extends AbstractTextRegionMatcher {
	public DbFitRegionMatcher() {
	}

	public TextRegion createTextRegion(String text, WikiFile wikiFile) {
		if (text.startsWith(DBFIT_LINK_PREFIX)) {
			return new DbFitTextRegion(text.substring(0, matchLength(text, wikiFile)));
		}
		return null;
	}

	protected File findResourceFromPath(String section, WikiFile wikiFile) {
		try {
			// IJavaProject project = context.getJavaProject();
			// project.

			return ResourcesPlugin.getWorkspace().getRoot().findMember(section).getLocation().toFile();
		} catch (Exception localException) {
			return null;
		}
	}

	protected int matchLength(String candidate, WikiFile wikiFile) {
		String text = candidate;

		if (text.indexOf(DBFIT_LINK_PREFIX, 1) > 0) {
			text = text.substring(0, text.indexOf(DBFIT_LINK_PREFIX, 1));
		}

		for (int index = text.length(); index >= DBFIT_LINK_PREFIX.length(); --index) {
			String section = text.substring(DBFIT_LINK_PREFIX.length(), index);
			if (section.length() > 0 && section.charAt(section.length() - 1) == ':') {
				continue;
			}
			File resource = findResourceFromPath(section, wikiFile);
			if ((resource == null) || (!(resource.exists())))
				continue;
			if ((index < text.length()) && (':' == text.charAt(index))) {
				return (DBFIT_LINK_PREFIX.length() + section.length() + getLineNumberLength(text, index) + 1);
			}
			return index;
		}

		return DBFIT_LINK_PREFIX.length();
	}

	private int getLineNumberLength(String text, int colon) {
		String number = new String(text.substring(colon + 1));
		if (number.trim().length() == 0) {
			return 0;
		}
		int index = 0;
		while ((index < number.length()) && (Character.isDigit(number.charAt(index)))) {
			++index;
		}
		return index;
	}

	protected boolean accepts(char c, boolean firstCharacter) {
		if (firstCharacter) {
			return c == DBFIT_LINK_PREFIX.charAt(0);
		} else {
			return true;
		}
	}
}