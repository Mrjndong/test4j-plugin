package org.jtester.wiki.ui.processor.completion;

import static org.eclipse.jdt.core.CompletionProposal.PACKAGE_REF;
import static org.eclipse.jdt.core.CompletionProposal.TYPE_REF;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jdt.core.CompletionProposal;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.eval.IEvaluationContext;
import org.eclipse.jdt.ui.text.java.CompletionProposalComparator;
import org.eclipse.jdt.ui.text.java.CompletionProposalLabelProvider;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;
import org.jtester.wiki.assistor.WikiHelper;

/**
 * java 补全处理器
 * 
 * @author darui.wudr
 * 
 */
public class JavaCompletionProcessor extends CompletionRequestor {
	private String textToComplete;

	private Set<ICompletionProposal> proposals = new TreeSet<ICompletionProposal>(new CompletionProposalComparator());
	private int documentOffset;

	public ArrayList<ICompletionProposal> getProposals(IJavaProject project, ITextViewer viewer, int documentOffset)
			throws BadLocationException, JavaModelException {
		this.proposals.clear();
		this.documentOffset = documentOffset;
		this.textToComplete = WikiHelper.getTextToCompleteJava(viewer, documentOffset);

		if (this.textToComplete != null) {
			performCodeComplete(project, this.textToComplete);
			ArrayList<ICompletionProposal> results = new ArrayList<ICompletionProposal>(this.proposals);
			this.proposals.clear();
			return results;
		}
		return new ArrayList<ICompletionProposal>();
	}

	public void accept(CompletionProposal proposal) {
		if (!(proposal.getKind() == TYPE_REF || proposal.getKind() == PACKAGE_REF)) {
			return;
		}
		CompletionProposalLabelProvider labelProvider = new CompletionProposalLabelProvider();
		String matchName = getReplacementText(proposal);
		String displayText = labelProvider.createLabel(proposal);
		Image image = labelProvider.createImageDescriptor(proposal).createImage();
		ICompletionProposal myProposal = new org.eclipse.jface.text.contentassist.CompletionProposal(matchName,
				this.documentOffset - this.textToComplete.length(), this.textToComplete.length(), matchName.length(),
				image, displayText, null, null);
		this.proposals.add(myProposal);
	}

	private void performCodeComplete(IJavaProject project, String text) throws JavaModelException {
		IEvaluationContext context = project.newEvaluationContext();
		context.codeComplete(text, text.length(), this);
	}

	private String getReplacementText(CompletionProposal proposal) {
		String matchName = new String(proposal.getCompletion());
		String pack = new String(proposal.getDeclarationSignature());
		if (!(matchName.startsWith(pack))) {
			matchName = pack + "." + matchName;
		}
		return matchName;
	}
}