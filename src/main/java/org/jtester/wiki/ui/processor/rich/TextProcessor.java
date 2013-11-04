package org.jtester.wiki.ui.processor.rich;

import org.w3c.dom.Element;

public class TextProcessor extends ElementProcessor {

	@Override
	public String process(Element element, boolean unformatted) {
		String wiki = "!-";
		String child = convertChildren(element, true, unformatted, true);
		wiki += child.trim();
		wiki += "-!";
		return wiki;
	}
}
