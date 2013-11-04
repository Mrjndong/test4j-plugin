package org.jtester.wiki.ui.text.region;

import static org.jtester.plugin.JTesterPreferenceInitializer.URL;

import java.util.List;

import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.rules.IToken;
import org.jtester.plugin.helper.ColourManager;

public final class UrlTextRegion extends TextRegion {
	public UrlTextRegion(String text) {
		super(text);
	}

	public IToken getToken(ColourManager colourManager) {
		return super.getToken(URL, colourManager);
	}

	public List<ICompletionProposal> accept(TextRegionVisitor textRegionVisitor) {
		return textRegionVisitor.visit(this);
	}

	public boolean isLink() {
		return true;
	}
}