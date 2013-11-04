package org.jtester.wiki.ui.processor.rich;

import static org.jtester.wiki.assistor.ProcessorHelper.ensureEndsWithLineBreak;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TableProcessor extends ElementProcessor {
	private String wiki = "";

	public String process(Element element, boolean unformatted) {
		boolean unformattedTable = hasUnformatedAttribute(element);
		wiki = unformattedTable ? "!" : "";
		wiki += convertChildren(element, false, unformattedTable, true);
		// wiki = ensureEndsWithLineBreak(wiki, element);
		/**
		 * 保证头尾的'|'是单独的一行,同时保证两个table中间有空行隔开
		 */
		wiki = "\n" + wiki.trim() + "\n";

		return wiki;
	}

	/**
	 * 只包括tr节点，其它类型的都过滤掉<br>
	 * 主要是fckeditor会加入&lt;br&gt;之类的符号影响解析<br>
	 * <br>{@inheritDoc}
	 */
	@Override
	protected List<Node> getChildren(Element element) {
		List<Node> nodes = new ArrayList<Node>();
		NodeList children = element.getChildNodes();
		for (int index = 0; index < children.getLength(); index++) {
			Node node = children.item(index);
			if (!(node instanceof Element)) {
				continue;
			}
			String tag = ((Element) node).getTagName();
			if (tag.equalsIgnoreCase("tr")) {
				nodes.add(node);
			}
		}
		return nodes;
	}

	public static class TrProcessor extends ElementProcessor {
		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "|";
			String child = convertChildren(element, false, unformatted, true);
			wiki += child;
			return ensureEndsWithLineBreak(wiki, element);
		}

		/**
		 * 只包括td节点，其它类型的都过滤掉<br>
		 * 主要是fckeditor会加入&lt;br&gt;之类的符号影响解析<br>
		 * <br>{@inheritDoc}
		 */
		@Override
		protected List<Node> getChildren(Element element) {
			List<Node> nodes = new ArrayList<Node>();
			NodeList children = element.getChildNodes();
			for (int index = 0; index < children.getLength(); index++) {
				Node node = children.item(index);
				if (!(node instanceof Element)) {
					continue;
				}
				String tag = ((Element) node).getTagName();
				if (tag.equalsIgnoreCase("td")) {
					nodes.add(node);
				}
			}
			return nodes;
		}
	}

	public static class TdProcessor extends ElementProcessor {

		@Override
		public String process(Element element, boolean unformatted) {
			String wiki = "";
			if (!unformatted) {
				wiki += checkForCentered(element);
			}
			wiki += convertChildren(element, true, unformatted, true);
			wiki += "|";
			return wiki;
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
}
