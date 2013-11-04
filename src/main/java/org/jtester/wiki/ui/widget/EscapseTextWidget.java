package org.jtester.wiki.ui.widget;

import fitnesse.wikitext.widgets.LiteralWidget;
import fitnesse.wikitext.widgets.ParentWidget;

/**
 * 用EscapseTextWidget代替LiteralWidget主要是希望解析!-text-!为html时加上标识，好回转为wiki
 * 
 * @author darui.wudr
 * 
 */
public class EscapseTextWidget extends LiteralWidget {
	public EscapseTextWidget(ParentWidget parent, String text) {
		super(parent, text);
	}

	@Override
	public String render() throws Exception {
		StringBuffer html = new StringBuffer("<et>");
		html.append(super.render());
		html.append("</et>");
		return html.toString().replace("\n", "<br/>");
	}
}
