package org.jtester.wiki.assistor;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.jtester.JTesterActivator;
import org.jtester.tools.commons.StringHelper;
import org.jtester.wiki.ui.EditedWikiPage;
import org.jtester.wiki.ui.processor.completion.WikiCompletionProcessor;
import org.jtester.wiki.ui.text.matcher.DbFitRegionMatcher;
import org.jtester.wiki.ui.text.matcher.JavaTypeMatcher;
import org.jtester.wiki.ui.text.matcher.UrlMatcher;
import org.jtester.wiki.ui.text.region.TextRegionMatcher;

/**
 * Source viewer configuration for the wiki text editor.
 * 
 * @author darui.wudr
 * 
 */
public final class WikiConfiguration extends TextSourceViewerConfiguration {
	private static TextRegionMatcher[] DEFAULT_SCANNER_MATCHERS = { new UrlMatcher(),// <br>
			new DbFitRegionMatcher(), /** <br> */
			new JavaTypeMatcher(), /** <br> */
	};

	private EditedWikiPage wikiEditor;

	public WikiConfiguration(EditedWikiPage wikiEditor) {
		super(JTesterActivator.getDefault().getPreferenceStore());
		this.wikiEditor = wikiEditor;
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer viewer) {
		PresentationReconciler reconciler = (PresentationReconciler) super.getPresentationReconciler(viewer);
		DefaultDamagerRepairer dr = getDamagerRepairer(wikiEditor);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	/**
	 * 获得缺省的damage/repaire处理器
	 * 
	 * @return
	 */
	public DefaultDamagerRepairer getDamagerRepairer(final EditedWikiPage editor) {
		ITokenScanner scanner = new RuleBasedScanner() {
			{
				ArrayList<IRule> rules = new ArrayList<IRule>();
				rules.add(new WhitespaceRule(new IWhitespaceDetector() {
					public boolean isWhitespace(char c) {
						return StringHelper.isWhiteSpaceCharacter(c);
					}
				}));
				for (TextRegionMatcher matcher : DEFAULT_SCANNER_MATCHERS) {
					matcher.setEditor(editor);
				}
				rules.addAll(Arrays.asList(DEFAULT_SCANNER_MATCHERS));
				this.setRules(rules.toArray(new IRule[0]));
			}
		};
		return new DefaultDamagerRepairer(scanner);
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new WikiCompletionProcessor(this.wikiEditor),
				IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoInsert(true);
		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(12);
		assistant.setContextInformationPopupOrientation(20);
		assistant.setProposalSelectorBackground(Display.getCurrent().getSystemColor(1));

		return assistant;
	}

	@Override
	public IQuickAssistAssistant getQuickAssistAssistant(ISourceViewer sourceViewer) {
		JTesterActivator.getDefault().getPreferenceStore().setValue("spellingEnabled", true);
		return super.getQuickAssistAssistant(sourceViewer);
	}
}