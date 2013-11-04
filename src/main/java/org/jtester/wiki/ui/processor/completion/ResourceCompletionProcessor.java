package org.jtester.wiki.ui.processor.completion;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.jtester.wiki.ui.EditedWikiPage;
import org.jtester.wiki.ui.text.region.TextRegion;
import org.jtester.wiki.ui.text.region.TextRegionBuilder;
import org.jtester.wiki.ui.text.region.TextRegionVisitor;

public class ResourceCompletionProcessor {
	private final EditedWikiPage wikiEditor;
	private ITextViewer viewer;
	private int documentOffset;
	private ArrayList<ICompletionProposal> proposals;

	public ResourceCompletionProcessor(EditedWikiPage wikiEditor) {
		this.wikiEditor = wikiEditor;
	}

	public void addCompletions(ITextViewer viewer, int documentOffset, ArrayList<ICompletionProposal> proposals)
			throws BadLocationException {
		this.viewer = viewer;
		this.documentOffset = documentOffset;
		this.proposals = proposals;
		char ch = getCharacterAtDocumentOffset(this.viewer, this.documentOffset);
		if (this.viewer.getDocument().getLength() > 0 && ch == '.') {
			TextRegion textRegion = TextRegionBuilder.getTextRegionAtCursor(this.wikiEditor, viewer.getDocument(),
					documentOffset);
			List<ICompletionProposal> list = textRegion.accept(new TextRegionVisitor(this.wikiEditor, textRegion,
					this.documentOffset));
			this.proposals.addAll(list);
		}
	}

	/**
	 * viewer中偏移量为documentOffset的字符
	 * 
	 * @param viewer
	 * @param documentOffset
	 * @return
	 * @throws BadLocationException
	 */
	private char getCharacterAtDocumentOffset(ITextViewer viewer, int documentOffset) throws BadLocationException {
		int docLength = viewer.getDocument().getLength();
		if (documentOffset == docLength && docLength > 0) {
			return viewer.getDocument().getChar(documentOffset - 1);
		}
		return viewer.getDocument().getChar(documentOffset);
	}
}