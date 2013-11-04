package org.jtester.wiki.ui.text.region;

import static org.jtester.plugin.JTesterPreferenceInitializer.JAVA_TYPE;

import java.util.List;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.rules.IToken;
import org.jtester.plugin.helper.ColourManager;

public final class JavaTypeTextRegion extends TextRegion {
	private IType javaType;

	public JavaTypeTextRegion(String text, IType javaType) {
		super(text);
		this.javaType = javaType;
	}

	public List<ICompletionProposal> accept(TextRegionVisitor textRegionVisitor) {
		return textRegionVisitor.visit(this);
	}

	public boolean isLink() {
		return true;
	}

	public IType getType() {
		return this.javaType;
	}

	public IToken getToken(ColourManager colourManager) {
		return getToken(JAVA_TYPE, colourManager);
	}
}