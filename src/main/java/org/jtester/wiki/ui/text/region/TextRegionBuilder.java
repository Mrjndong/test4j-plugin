package org.jtester.wiki.ui.text.region;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.jtester.tools.commons.StringHelper;
import org.jtester.wiki.assistor.WikiFile;
import org.jtester.wiki.ui.EditedWikiPage;
import org.jtester.wiki.ui.text.matcher.DbFitRegionMatcher;
import org.jtester.wiki.ui.text.matcher.JavaTypeMatcher;
import org.jtester.wiki.ui.text.matcher.UrlMatcher;

public final class TextRegionBuilder {
	/**
	 * 用于代码高亮和补全操作
	 */
	private static TextRegionMatcher[] RENDERER_MATCHERS = { new UrlMatcher(), // <br>
			new DbFitRegionMatcher(), /** <br> */
			new JavaTypeMatcher(), /** <br> */
	};

	public static TextRegion getTextRegionAtCursor(EditedWikiPage editor, IDocument document, int initialPos) {
		try {
			int pos = initialPos;
			int line = document.getLineOfOffset(pos);
			int start = document.getLineOffset(line);
			int end = start + document.getLineInformation(line).getLength();

			if (pos > end) {
				pos = end;
			}

			String word = document.get(start, end - start);
			TextRegion textRegion = getFirstTextRegion(word, editor.getContext());
			textRegion.setCursorPosition(pos - start);
			textRegion.setLocationInDocument(start);

			while (start + textRegion.getLength() < pos) {
				start += textRegion.getLength();
				word = document.get(start, end - start);
				textRegion = getFirstTextRegion(word, editor.getContext());
				textRegion.setCursorPosition(pos - start);
				textRegion.setLocationInDocument(start);
			}
			return textRegion;
		} catch (BadLocationException e) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			MessageDialog.openError(shell, "TextRegionBuilder Error", e.getLocalizedMessage());
			return new BasicTextRegion("");
		}
	}

	public static TextRegion getFirstTextRegion(String text, WikiFile wikiFile) {
		if (StringHelper.isBlankOrNull(text)) {
			return new BasicTextRegion("");
		}
		TextRegion[] candidates = getCandidates(text, wikiFile);
		if (candidates == null || candidates.length == 0) {
			return new BasicTextRegion(text);
		}
		TextRegion chosen = candidates[0];
		for (TextRegion textRegion : candidates) {
			if (textRegion.getLength() > chosen.getLength()) {
				chosen = textRegion;
			}
		}
		return chosen;
	}

	private static TextRegion[] getCandidates(String text, WikiFile wikiFile) {
		ArrayList<TextRegion> candidates = new ArrayList<TextRegion>();

		for (TextRegionMatcher element : RENDERER_MATCHERS) {
			TextRegion textRegion = element.createTextRegion(text, wikiFile);
			if (textRegion != null) {
				candidates.add(textRegion);
			}
		}
		return ((TextRegion[]) candidates.toArray(new TextRegion[candidates.size()]));
	}
}