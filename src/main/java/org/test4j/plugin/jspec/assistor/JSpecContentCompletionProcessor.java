package org.test4j.plugin.jspec.assistor;

import java.util.regex.Pattern;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

public class JSpecContentCompletionProcessor implements IContentAssistProcessor {
	protected static final ICompletionProposal[] NO_PROPOSALS = new ICompletionProposal[2];

	public static Pattern MODULE_PREFIX_PATTERN = Pattern.compile("([A-Za-z0-9_]+(::|->))+");

	public static Pattern VAR_PREFIX_PATTERN = Pattern.compile("\\$[A-Za-z0-9_]+(::|->)$");

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		return new ICompletionProposal[0];
	}
	/**
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		IDocument document = viewer.getDocument();
		ITextSelection selection = (ITextSelection) viewer.getSelectionProvider().getSelection();
		if (selection.getLength() > 0) {
			return NO_PROPOSALS;
		}
		String lastTextLine = null;

		int lineStartOffset = 0;
		try {
			int lastLine = document.getLineOfOffset(documentOffset);
			lineStartOffset = document.getLineOffset(lastLine);
			lastTextLine = document.get(lineStartOffset, documentOffset - lineStartOffset);
		} catch (BadLocationException localBadLocationException) {
		}
		ProjectAwareFastPartitioner partitioner = (ProjectAwareFastPartitioner) viewer.getDocument()
				.getDocumentPartitioner();
		IProject project = partitioner.getProject();

		if (BehaviorNature.getProjectCache(project) == null) {
			BehaviorNature.initiateProjectCache(project);
		}
		Set<String> projectCacheKeys = BehaviorNature.getProjectCache(project).keySet();
		List<BehaviorStepProposal> proposals = new ArrayList<BehaviorStepProposal>();
		Iterator<String> iterator = projectCacheKeys.iterator();
		LineParser lineParser = new LineParser();

		lastTextLine = lastTextLine.substring(0, lastTextLine.length() - 1);

		String parsedLine = lineParser.getLineWithoutKeyword(lastTextLine);

		String parsedKeyWork = lineParser.getKeyWord(lastTextLine);

		while (iterator.hasNext()) {
			String annotationStringRegExValue = (String) iterator.next();
			String auxParsedLine = parsedLine;
			int index = annotationStringRegExValue.indexOf(".*?");
			if ((index > 0) && (index < auxParsedLine.length())) {
				auxParsedLine = auxParsedLine.substring(0, index);
			}
			if ((auxParsedLine.length() != 0) && (!annotationStringRegExValue.startsWith(auxParsedLine)))
				continue;
			if ((parsedKeyWork != null) && (parsedKeyWork.trim().length() > 0)) {
				parsedKeyWork = parsedKeyWork.replaceFirst(new String(new char[] { parsedKeyWork.charAt(0) }),
						new String(new char[] { parsedKeyWork.charAt(0) }).toUpperCase());
				annotationStringRegExValue = parsedKeyWork + " " + annotationStringRegExValue;
			}
			proposals.add(new BehaviorStepProposal(annotationStringRegExValue, lineStartOffset, documentOffset));
		}

		Collections.sort(proposals);
		return proposals.toArray(new ICompletionProposal[proposals.size()]);
	}
	 **/

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '?' };
	}

	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	public String getErrorMessage() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}
}