package org.jtester.wiki.ui.text.region;

import java.util.List;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.jtester.plugin.helper.ColourManager;
import org.jtester.plugin.helper.PreferenceHelper;

public abstract class TextRegion {
	private String text;
	protected String displayText;
	private int cursorPosition;
	private boolean cursorPositionSet = false;
	private int locationInDocument;

	public TextRegion(String text) {
		this.text = text;
		this.displayText = text;
	}

	public TextRegion() {
		this.text = "";
	}

	public abstract List<ICompletionProposal> accept(TextRegionVisitor paramTextRegionVisitor);

	/**
	 * 获取文本区域的样式和前景色token
	 * 
	 * @param colorManager
	 * @return
	 */
	public abstract IToken getToken(ColourManager colorManager);

	/**
	 * 文本是否是链接
	 * 
	 * @return
	 */
	public abstract boolean isLink();

	public int getLength() {
		return this.text.length();
	}

	public String getText() {
		return this.text;
	}

	public String getDisplayText() {
		return this.displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public int getCursorPosition() {
		return this.cursorPosition;
	}

	public void setCursorPosition(int cursorPosition) {
		this.cursorPosition = cursorPosition;
		this.cursorPositionSet = true;
	}

	public String getTextToCursor() {
		if (this.cursorPosition <= 0) {
			return "";
		} else if (this.cursorPosition >= this.text.length()) {
			return this.text;
		} else {
			return this.text.substring(0, this.cursorPosition);
		}
	}

	/**
	 * 获取文本的样式和前景色token
	 * 
	 * @param preferenceKey
	 * @param colourManager
	 * @return
	 */
	protected IToken getToken(final String preferenceKey, final ColourManager colourManager) {
		RGB rgb = PreferenceHelper.textForeground(preferenceKey);
		Color foreground = colourManager.getColor(rgb);
		String style = PreferenceHelper.textStyle(preferenceKey);
		boolean bold = "bold".equals(style);
		return new Token(new TextAttribute(foreground, null, bold ? SWT.BOLD : SWT.NORMAL));
	}

	public void append(String newText) {
		this.text += newText;
	}

	public int getLocationInDocument() {
		return this.locationInDocument;
	}

	public void setLocationInDocument(int index) {
		this.locationInDocument = index;
	}

	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (object == null || object.getClass().equals(this.getClass()) == false) {
			return false;
		}
		TextRegion other = (TextRegion) object;
		return this.text.equals(other.text) && this.locationInDocument == other.locationInDocument
				&& this.cursorPosition == other.cursorPosition;
	}

	public int hashCode() {
		return this.text.hashCode() ^ this.locationInDocument ^ this.cursorPosition;
	}

	public String toString() {
		String cursor = cursorPositionSet ? ", cursor position = " + cursorPosition : ", cursor position not set.";
		return "Region text: " + this.text + ", length = " + this.text.length() + cursor;
	}

	public static final String DBFIT_LINK_PREFIX = "DbFit:";
	public static final String JAVA_LINK_PREFIX = "Java:";
}