package org.jtester.wiki.ui.widget;

import org.jtester.JTesterActivator;
import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.ui.text.region.DbFitTextRegion;
import org.jtester.wiki.ui.text.region.JavaTypeTextRegion;
import org.jtester.wiki.ui.text.region.UrlTextRegion;

public final class IdeLinkWidget {// TODO
	private WikiFile wikiFile;

	public IdeLinkWidget(WikiFile wikiFile) {
		this.wikiFile = wikiFile;
	}

	public String make(DbFitTextRegion eclipseResourceTextRegion) {
		return getLink("http://--wiki/" + eclipseResourceTextRegion.getText(), eclipseResourceTextRegion.getText());
	}

	public String make(JavaTypeTextRegion region) {
		String url = "http://--wiki/Java:" + region.getType().getFullyQualifiedName();
		return getLink(url, getTextForJavaType(region));
	}

	public String make(UrlTextRegion urlTextRegion) {
		return getLink(urlTextRegion.getText(), urlTextRegion.getText());
	}

	public final void setContext(WikiFile wikiFile) {
		this.wikiFile = wikiFile;
	}

	public final WikiFile getContext() {
		return this.wikiFile;
	}

	private final String getLink(String url, String text) {
		return "<a href=\"" + url + "\">" + text + "</a>";
	}

	protected String getTextForJavaType(JavaTypeTextRegion region) {
		int lastSeparator = region.getText().lastIndexOf(46);
		if ((lastSeparator < 0)
				|| (JTesterActivator.getDefault().getPreferenceStore().getBoolean("renderFullyQualifiedTypeNames"))) {
			return region.getText();
		}
		return new String(region.getText().substring(lastSeparator + 1));
	}
}