package org.jtester.wiki.ui.processor.completion;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.jtester.plugin.helper.PluginHelper;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.wiki.ui.EditedWikiPage;

/**
 * wiki语法补全处理器
 * 
 * @author darui.wudr
 * 
 */
public final class WikiCompletionProcessor implements IContentAssistProcessor {
	private static final char[] AUTO_ACTIVATION_CHARACTERS = { '.', '/' };
	private static final ICompletionProposal[] EMPTY_COMPLETIONS = new ICompletionProposal[0];
	private EditedWikiPage wikiEditor;
	private JavaCompletionProcessor javaCompletionProcessor;
	private ResourceCompletionProcessor resourceCompletions;

	public WikiCompletionProcessor(EditedWikiPage wikiEditor) {
		this.wikiEditor = wikiEditor;
		this.javaCompletionProcessor = new JavaCompletionProcessor();
		this.resourceCompletions = new ResourceCompletionProcessor(wikiEditor);
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		try {
			ArrayList<ICompletionProposal> proposals = new ArrayList<ICompletionProposal>();
			IProject project = wikiEditor.getProject();
			this.resourceCompletions.addCompletions(viewer, documentOffset, proposals);

			if (PluginHelper.isJavaProject(project)) {
				IJavaProject javaProject = JavaCore.create(project);
				proposals.addAll(this.javaCompletionProcessor.getProposals(javaProject, viewer, documentOffset));
			}
			return ((ICompletionProposal[]) proposals.toArray(new ICompletionProposal[proposals.size()]));
		} catch (Exception e) {
			PluginLogger.log("Completion Processor", e.getLocalizedMessage(), e);
		}
		return EMPTY_COMPLETIONS;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		return new IContextInformation[0];
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return AUTO_ACTIVATION_CHARACTERS;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return new char[0];
	}

	public String getErrorMessage() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return new IContextInformationValidator() {
			public boolean isContextInformationValid(int offset) {
				return false;
			}

			public void install(IContextInformation info, ITextViewer viewer, int offset) {
			}
		};
	}
}