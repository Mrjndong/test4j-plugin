package org.jtester.wiki.assistor;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class ProcessorHelper {
	/**
	 * 转换text node的文本为wiki文本
	 * 
	 * @param node
	 * @param keepLineBreaks
	 * @return
	 */
	public final static String convertTextNode(Text node, boolean keepLineBreaks) {
		String text = node.getNodeValue();
		text = text.trim();
		if (node.getNodeName().equals(HtmlToFitUtil.DOC_ELEMENT)) {
			if (text.startsWith("?") && text.length() > 1) {
				text = text.substring(1);
			} else {
				text = "";
			}
		}
		if (keepLineBreaks == false) {
			text = text.replaceAll("\n", "").replaceAll("\r", "");
		}
		return text;
	}

	public static String ensureEndsWithLineBreak(String fitText, Element element) {
		if (isInTableCell(element) == false) {
			if (fitText.endsWith("\n") == false) {
				fitText += "\n";
			}
		}
		return fitText;
	}

	/**
	 * 标签是否是在td中
	 * 
	 * @param element
	 * @return
	 */
	public static boolean isInTableCell(Element element) {
		while (element != null) {
			String tag = element.getNodeName();
			if (tag.equalsIgnoreCase("td")) {
				return true;
			}
			if (element.getParentNode() != null && element.getParentNode() instanceof Element) {
				element = (Element) element.getParentNode();
			} else {
				element = null;
			}
		}
		return false;
	}
}
