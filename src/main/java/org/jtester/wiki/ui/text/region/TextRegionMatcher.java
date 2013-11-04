package org.jtester.wiki.ui.text.region;

import org.eclipse.jface.text.rules.IRule;
import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.ui.EditedWikiPage;

/**
 * 文本匹配器
 * 
 * @author darui.wudr
 * 
 */
public interface TextRegionMatcher extends IRule {
	/**
	 * 根据text文本和context创建系统定义好的TextRegion
	 * 
	 * @param text
	 * @param wikiFile
	 * @return
	 */
	public abstract TextRegion createTextRegion(String text, WikiFile wikiFile);

	public abstract void setEditor(EditedWikiPage paramWikiEditor);
}