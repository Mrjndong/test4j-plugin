package org.jtester.database.assistor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.jtester.plugin.helper.ColourManager;

public class SQLSourceViewerConfiguration extends SourceViewerConfiguration {
	private SQLDoubleClickStrategy doubleClickStrategy;
	private SQLKeywordScanner tagScanner;
	private ColourManager colorManager;

	public SQLSourceViewerConfiguration(ColourManager colorManager) {
		this.colorManager = colorManager;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, SQLPartitionScanner.SQL_COMMENT,
				SQLPartitionScanner.SQL_KEYWORD };
	}

	public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new SQLDoubleClickStrategy();
		return doubleClickStrategy;
	}

	/**
	 * (non-Javadoc) Method declared on SourceViewerConfiguration
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {

		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new SQLCompletionProcessor(), IDocument.DEFAULT_CONTENT_TYPE);

		assistant.enableAutoActivation(true);
		assistant.setAutoActivationDelay(500);
		assistant.setProposalPopupOrientation(IContentAssistant.PROPOSAL_OVERLAY);
		assistant.setContextInformationPopupOrientation(IContentAssistant.CONTEXT_INFO_ABOVE);
		// assistant.setContextInformationPopupBackground(JavaEditorEnvironment.getJavaColorProvider().getColor(new
		// RGB(150, 150, 0)));

		return assistant;
	}

	protected SQLKeywordScanner getSQLKeywordScanner() {
		if (tagScanner == null) {
			tagScanner = new SQLKeywordScanner(colorManager);
		}
		return tagScanner;
	}

	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getSQLKeywordScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager
				.getColor(ColourManager.SINGLE_LINE_COMMENT)));
		reconciler.setDamager(ndr, SQLPartitionScanner.SQL_COMMENT);
		reconciler.setRepairer(ndr, SQLPartitionScanner.SQL_COMMENT);

		return reconciler;
	}
}