/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jtester.wiki.ui.widget;

import fitnesse.wikitext.widgets.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Administrator
 */
public class UnderlineWidget extends ParentWidget {
	public static final String REGEXP = "__.+?__";
	private static final Pattern pattern = Pattern.compile("__(.+?)__", Pattern.MULTILINE + Pattern.DOTALL);

	public UnderlineWidget(ParentWidget parent, String text) throws Exception {
		super(parent);
		Matcher match = pattern.matcher(text);
		match.find();
		addChildWidgets(match.group(1));
	}

	public String render() throws Exception {
		StringBuffer html = new StringBuffer("<u>");
		html.append(childHtml()).append("</u>");
		return html.toString();
	}
}
