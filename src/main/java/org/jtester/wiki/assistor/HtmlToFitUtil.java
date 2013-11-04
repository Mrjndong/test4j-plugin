package org.jtester.wiki.assistor;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jtester.wiki.ui.processor.rich.ElementProcessor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class HtmlToFitUtil {
	public static final String AMPERSAND_SUBSTITUTE = "AmPeRsAnD";
	public static final String LEFT_ESCAPE = "&lt;";
	public static final String RIGHT_ESCAPE = "&gt;";
	public static final String DOC_ELEMENT = "RichNesse";

	public static final String SINGLE_QUOT = "&#39;";

	// protected static final String HTML_ESCAPED_SPACE = "%20";

	/**
	 * 将html转换为wiki文本
	 * 
	 * @param html
	 * @return
	 * @throws Exception
	 */
	public static String toFitWiki(String html, String encodingCharacter) throws Exception {
		String richXml = cleanupRichTextBeforeXmlLoad(html);

		Element root = createRichTextElement(richXml, encodingCharacter);
		ElementProcessor processor = ElementProcessor.findProcessor(root.getTagName(), false);
		String fitText = processor.process(root, false);

		fitText = fitText.replace("\r", "").replaceAll("\n{4,}", "\n\n\n").replaceAll(AMPERSAND_SUBSTITUTE, "&")
				.replaceAll(LEFT_ESCAPE, "<").replaceAll("&quot;", "\"").replaceAll(RIGHT_ESCAPE, ">");

		fitText = fitText.replace(SINGLE_QUOT, "'");
		return fitText;
	}

	private static String cleanupRichTextBeforeXmlLoad(String rich) {
		// this is an unusual situation where FCKeditor posts non XML compliant
		// rich text
		if (rich.length() >= 3 && rich.substring(rich.length() - 3).equals("<p>")) {
			rich = rich.substring(0, rich.length() - 3);
		}
		rich = rich.replaceAll("<tbody>", "").replaceAll("</tbody>", "").replaceAll("&nbsp;", " ").replaceAll("&amp;",
				"&").replaceAll("&", AMPERSAND_SUBSTITUTE);
		return rich;
	}

	/**
	 * 根据html内容构建xml的dom对象
	 * 
	 * @param html
	 * @return
	 * @throws Exception
	 */
	private static Element createRichTextElement(String html, String encodingCharacter) throws Exception {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		String xml = String.format("<?xml version='1.0' encoding='%s' standalone='no'?>", encodingCharacter);
		xml += "<" + DOC_ELEMENT + ">" + html + "</" + DOC_ELEMENT + ">";
		// ByteArrayInputStream input = new
		// ByteArrayInputStream(xml.getBytes());
		// Document doc = builder.parse(input);
		Document doc = builder.parse(new InputSource(new StringReader(xml)));
		return doc.getDocumentElement();

	}
}
