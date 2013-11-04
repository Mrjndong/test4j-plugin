package org.jtester.wiki.ui.processor.rich;

import static org.jtester.wiki.assistor.HtmlToFitUtil.DOC_ELEMENT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jtester.wiki.assistor.ProcessorHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public abstract class ElementProcessor {
	public static final String RICH_TAB_SUBSTITUTE = "    ";

	/**
	 * 将xml dom对象反序列化为wiki文本
	 * 
	 * @param element
	 * @return
	 */
	public abstract String process(Element element, boolean unformatted);

	private final static Map<String, ElementProcessor> processors = new HashMap<String, ElementProcessor>();
	static {
		processors.put(DOC_ELEMENT.toUpperCase(), new DbFitProcessor());
		processors.put("table".toUpperCase(), new TableProcessor());
		processors.put("tr".toUpperCase(), new TableProcessor.TrProcessor());
		processors.put("td".toUpperCase(), new TableProcessor.TdProcessor());
		processors.put("ul".toUpperCase(), new DbFitProcessor.UlOlProcessor());
		processors.put("ol".toUpperCase(), new DbFitProcessor.UlOlProcessor());
		processors.put("li".toUpperCase(), new DbFitProcessor.LiProcessor());
		processors.put("b".toUpperCase(), new StyleProcessor.BoldFontProcessor());
		processors.put("u".toUpperCase(), new StyleProcessor.UnderlineProcessor());
		processors.put("i".toUpperCase(), new StyleProcessor.ItalicProcessor());
		processors.put("p".toUpperCase(), new BlockProcessor());
		processors.put("br".toUpperCase(), new BlockProcessor.BrProcessor());
		processors.put("strike".toUpperCase(), new StyleProcessor.StrikeProcessor());
		processors.put("a".toUpperCase(), new BlockProcessor.HrefProcessor());
		processors.put("div".toUpperCase(), new BlockProcessor.DivProcessor());
		processors.put("h1".toUpperCase(), new BlockProcessor.H1Processor());
		processors.put("h2".toUpperCase(), new BlockProcessor.H2Processor());
		processors.put("h3".toUpperCase(), new BlockProcessor.H3Processor());
		processors.put("hr".toUpperCase(), new BlockProcessor.HrProcessor());
		processors.put("pre".toUpperCase(), new BlockProcessor.PreProcessor());

		processors.put("et".toUpperCase(), new TextProcessor());
	}

	public static ElementProcessor findProcessor(String tag, boolean unformatted) {
		ElementProcessor processor = processors.get(tag.toUpperCase());
		if (processor == null) {
			processor = new DbFitProcessor.DefaultProcessor();
		}
		return processor;
	}

	public static final String UNFORMATTED = "unformatted";

	/**
	 * dom节点有属性unformatted
	 * 
	 * @param element
	 * @return
	 */
	protected boolean hasUnformatedAttribute(Element element) {
		return element.hasAttribute(UNFORMATTED) && !element.getAttribute(UNFORMATTED).equals("true");
	}

	/**
	 * 获得直接子节点列表
	 * 
	 * @param element
	 * @return
	 */
	protected List<Node> getChildren(Element element) {
		List<Node> nodes = new ArrayList<Node>();
		NodeList children = element.getChildNodes();
		for (int index = 0; index < children.getLength(); index++) {
			Node node = children.item(index);

			nodes.add(node);
		}
		return nodes;
	}

	protected String convertChildren(Element element, boolean getText, boolean unformatted, boolean keepLineBreaks) {
		String fitText = "";
		List<Node> children = getChildren(element);
		for (Node node : children) {
			if (node instanceof Element) {
				ElementProcessor processor = findProcessor(((Element) node).getTagName(), unformatted);
				String wiki = processor.process((Element) node, unformatted);
				fitText += wiki;
			} else if (getText && node instanceof Text) {
				String text = ProcessorHelper.convertTextNode((Text) node, keepLineBreaks);
				fitText += text;
			}
		}
		return fitText;
	}
}
