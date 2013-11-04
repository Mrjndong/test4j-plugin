package org.jtester.wiki.assistor;

public class HtmlHelper {
	public static String encapsulateTextArea(final String html) {
		String _html = html.replaceAll("\"", "'");
		_html = _html.replaceAll("[\n\r]+", "");
		_html = filterTagSpace(_html);
		_html = filterSpacePhrase(_html);
		return _html;
	}

	/**
	 * 将tag中的空格全部过滤
	 * 
	 * @param html
	 * @return
	 */
	public static String filterTagSpace(String html) {
		String _html = html.replaceAll("\\s+>", ">");
		_html = _html.replaceAll("<\\s+", "<");
		_html = _html.replaceAll("<\\s+/", "</");
		_html = _html.replaceAll("</\\s+", "</");

		return _html;
	}

	/**
	 * 过滤空行
	 * 
	 * @param html
	 * @return
	 */
	public static String filterSpacePhrase(String html) {
		String _html = html.replaceAll("</p><p>\\s*</p>", "</p>");
		_html = _html.replaceAll("<p>\\s*</p><p>", "<p>");
		return _html;
	}
}
