package org.jtester.wiki.ui.processor.rich;

import static org.jtester.wiki.assistor.ProcessorHelper.ensureEndsWithLineBreak;

import org.jtester.tools.commons.StringHelper;
import org.w3c.dom.Element;

public class BlockProcessor extends ElementProcessor {

	@Override
	public String process(Element element, boolean unformatted) {
		String wiki = convertChildren(element, true, unformatted, true);
		// wiki = ensureEndsWithLineBreak(wiki, element);
		wiki = StringHelper.isBlankOrNull(wiki) ? "\n" : wiki.trim();
		return wiki;
		// return wiki;
	}

	public static class BrProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			return "\n";
		}
	}

	public static class HrefProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			if (element.hasAttribute("href")) {
				String href = element.getAttribute("href");
				href = href.replaceAll(" ", "%20");
				return href;
			} else {
				return convertChildren(element, true, unformatted, true);
			}
		}
	}

	public static class DivProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "!c ";
			wiki += convertChildren(element, true, unformatted, true);
			return ensureEndsWithLineBreak(wiki, element);
		}
	}

	public static class H1Processor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "!1 " + checkForCentered(element);
			wiki += convertChildren(element, true, unformatted, true);
			return ensureEndsWithLineBreak(wiki, element);
		}

	}

	public static class H2Processor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "!2 " + checkForCentered(element);
			wiki += convertChildren(element, true, unformatted, true);
			return ensureEndsWithLineBreak(wiki, element);
		}
	}

	public static class H3Processor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "!3 " + checkForCentered(element);
			wiki += convertChildren(element, true, unformatted, true);
			return ensureEndsWithLineBreak(wiki, element);
		}
	}

	public static class HrProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			return "----";
		}
	}

	public static class PreProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = convertChildren(element, true, false, true);
			return String.format("{{{%s}}}", wiki.replaceAll(RICH_TAB_SUBSTITUTE, "\t"));
		}
	}

	private static String checkForCentered(Element element) {
		if (element.hasAttribute("style")) {
			if (element.getAttribute("style").indexOf("text-align: center") > -1
					|| element.getAttribute("style").indexOf("text-align:center") > -1) {
				return "!c ";
			}
		}
		return "";
	}
}
