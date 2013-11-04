package org.jtester.wiki.ui.processor.rich;

import org.w3c.dom.Element;

public class DbFitProcessor extends ElementProcessor {

	@Override
	public String process(Element element, boolean unformatted) {
		return convertChildren(element, true, false, false);
	}

	public static class UnformattedProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = convertChildren(element, true, unformatted, true);
			return wiki.trim();
		}
	}

	public static class UlOlProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = convertChildren(element, false, unformatted, true);
			// remove leading new line if this is the first ul in this list set,
			// also add the final line break
			String parentTagName = element.getParentNode().getNodeName();
			if (parentTagName.equals("ul") || parentTagName.equals("ol") || parentTagName.equals("li")) {
				wiki = wiki.substring(1) + "\n";
			}
			return wiki;
		}
	}

	public static class LiProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "\n" + getListItemDepth(element);
			if (element.getParentNode().getNodeName().equals("ul")) {
				wiki += "* ";
			} else {
				wiki += "1 ";
			}
			wiki += convertChildren(element, true, unformatted, true);
			return wiki;
		}

		private static String getListItemDepth(Element element) {
			int depth = 0;
			while (true) {
				String parentName = element.getParentNode().getNodeName();
				if (parentName.equals("ul") || parentName.equals("ol")) {
					depth++;
				} else if (!parentName.equals("li")) {
					break;
				}
				element = (Element) element.getParentNode();
			}
			String spaces = "";
			for (int i = 0; i < depth; i++)
				spaces += " ";
			return spaces;
		}
	}

	public static class DefaultProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			return convertChildren(element, true, unformatted, true);
		}
	}
}
