package org.jtester.wiki.ui.processor.rich;

import org.w3c.dom.Element;

public class StyleProcessor {
	public static class BoldFontProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "'''";
			wiki += convertChildren(element, true, unformatted, true);
			wiki += "'''";
			return wiki;
		}
	}

	public static class UnderlineProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "__";
			wiki += convertChildren(element, true, unformatted, true);
			wiki += "__";
			return wiki;
		}
	}

	public static class ItalicProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "''";
			wiki += convertChildren(element, true, unformatted, true);
			wiki += "''";
			return wiki;
		}
	}

	public static class StrikeProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "--";
			wiki += convertChildren(element, true, unformatted, true);
			wiki += "--";
			return wiki;
		}

	}
}
