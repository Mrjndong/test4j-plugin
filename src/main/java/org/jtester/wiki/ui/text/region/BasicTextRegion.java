package org.jtester.wiki.ui.text.region;

import static org.jtester.plugin.JTesterPreferenceInitializer.OTHER;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.rules.IToken;
import org.jtester.plugin.helper.ColourManager;

public final class BasicTextRegion extends TextRegion {
	public BasicTextRegion(String word) {
		super(word);
	}

	public List<ICompletionProposal> accept(TextRegionVisitor textRegionVisitor) {
		return textRegionVisitor.visit(this);
	}

	public IToken getToken(ColourManager colourManager) {
		return getToken(OTHER, colourManager);
	}

	public boolean isLink() {
		return false;
	}
}