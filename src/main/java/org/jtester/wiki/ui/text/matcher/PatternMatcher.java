package org.jtester.wiki.ui.text.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.ui.text.region.TextRegion;

public abstract class PatternMatcher extends AbstractTextRegionMatcher {
	private final Pattern pattern;
	private Matcher matcher;

	public PatternMatcher(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}

	public final TextRegion createTextRegion(String text, WikiFile wikiFile) {
		int matchLength = matchLength(text);
		if (matchLength > 0) {
			return createTextRegion(new String(text.substring(0, matchLength)));
		}
		return null;
	}

	protected abstract TextRegion createTextRegion(String paramString);

	private int matchLength(String text) {
		this.matcher = this.pattern.matcher(text);
		if ((this.matcher.find()) && (this.matcher.start() == 0)) {
			return this.matcher.end();
		}
		return -1;
	}

	public Matcher getMatcher() {
		return this.matcher;
	}
}