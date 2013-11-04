package org.jtester.wiki.ui.text.matcher;

import org.apache.commons.lang.StringUtils;
import org.jtester.plugin.helper.PluginSetting;
import org.jtester.wiki.ui.text.region.TextRegion;
import org.jtester.wiki.ui.text.region.UrlTextRegion;

public final class UrlMatcher extends PatternMatcher {
	private static final String URL_REGEX = "(" + StringUtils.join(PluginSetting.URL_PREFIXES, '|')
			+ "):(//)?([-_\\.!~*';/?:@#&=+$%,\\p{Alnum}])+";

	public UrlMatcher() {
		super(URL_REGEX);
	}

	protected boolean accepts(char c, boolean firstCharacter) {
		if (firstCharacter) {
			for (int i = 0; i < PluginSetting.URL_PREFIXES.length; ++i) {
				if (c == PluginSetting.URL_PREFIXES[i].charAt(0)) {
					return true;
				}
			}
		}
		return (c == ' ');
	}

	protected TextRegion createTextRegion(String text) {
		return new UrlTextRegion(text);
	}
}