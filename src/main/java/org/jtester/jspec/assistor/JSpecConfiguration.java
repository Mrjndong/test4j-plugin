package org.jtester.jspec.assistor;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.quickassist.IQuickAssistAssistant;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.jtester.jspec.ui.JSpecEditor;
import org.jtester.plugin.helper.ColourManager;
import org.test4j.spec.scenario.step.txt.LineType;

public class JSpecConfiguration extends SourceViewerConfiguration {
	private ColourManager colourManager;
	private ContentAssistant contentAssist;

	private JSpecEditor editor;

	public JSpecConfiguration(ColourManager colourManager, JSpecEditor editor) {
		this.colourManager = colourManager;
		this.editor = editor;
	}

	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] { IDocument.DEFAULT_CONTENT_TYPE, JSpecDocumentProvider.GWZ_TAG };
	}

	public IQuickAssistAssistant getQuickAssistAssistant(ISourceViewer sourceViewer) {
		return null;
	}

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		if (this.contentAssist == null) {
			this.contentAssist = new ContentAssistant();
			this.contentAssist.setContentAssistProcessor(new JSpecContentCompletionProcessor(),
					JSpecDocumentProvider.GWZ_TAG);
			this.contentAssist.setContentAssistProcessor(new JSpecContentCompletionProcessor(),
					IDocument.DEFAULT_CONTENT_TYPE);

			this.contentAssist.setProposalPopupOrientation(20);
			this.contentAssist.setContextInformationPopupOrientation(10);
			this.contentAssist.setInformationControlCreator(getInformationControlCreator(sourceViewer));
			this.contentAssist.enableAutoActivation(true);
		}
		return this.contentAssist;
	}

	@Override
	public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer sourceViewer) {
		return new IHyperlinkDetector[] { new IHyperlinkDetector() {
			public IHyperlink[] detectHyperlinks(final ITextViewer viewer, IRegion region,
					boolean canShowMultipleHyperlinks) {
				IDocument doc = viewer.getDocument();
				try {
					int lineNo = doc.getLineOfOffset(region.getOffset());
					int lineOffset = doc.getLineOffset(lineNo);
					int lineLength = doc.getLineLength(lineNo);
					final String line = doc.get(lineOffset, lineLength).trim();
					LineType type = LineType.getLineType(line);
					switch (type) {
					case Given:
					case SkipGiven:
					case When:
					case SkipWhen:
					case Then:
					case SkipThen:
						IHyperlink stepLink = new StepMethodLink(doc, line, lineNo, lineOffset, lineLength);
						return new IHyperlink[] { stepLink };
					default:
						NoLink noLink = new NoLink();
						return new IHyperlink[] { noLink };
					}
				} catch (BadLocationException e) {
					throw new RuntimeException(e);
				}
			}
		} };
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner(IDocument.DEFAULT_CONTENT_TYPE));
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		String[] scanTags = JSpecDocumentProvider.getScanTags();
		for (String scanTag : scanTags) {
			dr = new DefaultDamagerRepairer(getScanner(scanTag));
			reconciler.setDamager(dr, scanTag);
			reconciler.setRepairer(dr, scanTag);
		}
		return reconciler;
	}

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		JSpecReconcilingStrategy strategy = new JSpecReconcilingStrategy();
		strategy.setEditor(editor);

		MonoReconciler reconciler = new MonoReconciler(strategy, false);

		return reconciler;
	}

	// ====
	IFile getCurrentFile() {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IFileEditorInput) {
			return ((IFileEditorInput) input).getFile();
		} else {
			return null;
		}
	}

	static boolean isGivenWhenZen(String line) {
		if (line == null) {
			return false;
		}
		if (line.startsWith("Given ") || line.startsWith("Given\t")) {
			return true;
		}
		if (line.startsWith("When ") || line.startsWith("When\t")) {
			return true;
		}
		if (line.startsWith("Then ") || line.startsWith("Then\t")) {
			return true;
		}
		return false;
	}

	private Map<String, ITokenScanner> TOKEN_SCANNER = new HashMap<String, ITokenScanner>();

	private TextAttribute getTextAttribute(String scanTag) {
		RGB rgb = ColourManager.KEY_COLOR.get(scanTag);
		Color color = this.colourManager.getColor(rgb == null ? ColourManager.DEFAULT : rgb);
		TextAttribute attr = new TextAttribute(color, null, SWT.BOLD, null);
		return attr;
	}

	private ITokenScanner getScanner(String scanTag) {
		if (TOKEN_SCANNER.containsKey(scanTag)) {
			return TOKEN_SCANNER.get(scanTag);
		} else {
			RuleBasedScanner scanner = new RuleBasedScanner();
			IToken token = new Token(this.getTextAttribute(scanTag));
			scanner.setDefaultReturnToken(token);
			TOKEN_SCANNER.put(scanTag, scanner);
			return scanner;
		}
	}
}