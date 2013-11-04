package org.jtester.wiki.ui.text.region;

import static org.jtester.plugin.JTesterPreferenceInitializer.DBFIT_RESOURCE;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.rules.IToken;
import org.jtester.plugin.helper.ColourManager;

public final class DbFitTextRegion extends TextRegion {
	public DbFitTextRegion(String text) {
		super(text);
	}

	public IToken getToken(ColourManager colourManager) {
		return getToken(DBFIT_RESOURCE, colourManager);
	}

	public List<ICompletionProposal> accept(TextRegionVisitor textRegionVisitor) {
		return textRegionVisitor.visit(this);
	}

	public boolean isLink() {
		return true;
	}

	public IResource getResource() {
		String file = getText().substring(DBFIT_LINK_PREFIX.length());
		return ResourcesPlugin.getWorkspace().getRoot().findMember(file);
	}
}