package org.jtester.wiki.ui.text.matcher;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.jtester.wiki.ui.EditedWikiPage;
import org.jtester.wiki.ui.text.region.TextRegion;
import org.jtester.wiki.ui.text.region.TextRegionMatcher;

abstract class AbstractTextRegionMatcher implements TextRegionMatcher {
	private EditedWikiPage editor;

	public void setEditor(EditedWikiPage editor) {
		this.editor = editor;
	}

	public final IToken evaluate(ICharacterScanner scanner) {
		String text = getText(scanner);
		TextRegion region = createTextRegion(text, this.editor.getContext());
		unwind(scanner, text, region);
		if (region == null) {
			return Token.UNDEFINED;
		}
		return region.getToken(this.editor.getColourManager());
	}

	/**
	 * 返回符合条件的字符串
	 * 
	 * @param scanner
	 * @return
	 */
	protected final String getText(ICharacterScanner scanner) {
		StringBuffer buffer = new StringBuffer(100);
		boolean firstCharacter = true;
		char c = (char) scanner.read();
		while (accepts(c, firstCharacter) && c != 0xFFFF && c != '\n' && c != '\r') {
			buffer.append(c);
			firstCharacter = false;
			c = (char) scanner.read();
		}

		scanner.unread();
		return buffer.toString();
	}

	/**
	 * 扫描字符是否符合region规则
	 * 
	 * @param ch
	 * @param paramBoolean
	 * @return
	 */
	protected abstract boolean accepts(char ch, boolean paramBoolean);

	/**
	 * 回退字符扫描的指针
	 * 
	 * @param scanner
	 * @param text
	 * @param textRegion
	 */
	protected final void unwind(ICharacterScanner scanner, String text, TextRegion textRegion) {
		int unwindLength = text.length();
		if (textRegion != null) {
			unwindLength -= textRegion.getLength();
		}
		for (int i = 0; i < unwindLength; ++i) {
			scanner.unread();
		}
	}
}