package org.test4j.plugin.database.assistor;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved.
 */

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.test4j.plugin.database.ui.DatabaseView;
import org.test4j.plugin.database.ui.tree.BaseTreeNode;
import org.test4j.plugin.database.ui.tree.DatabaseNode;

/**
 * Example Java doc completion processor.
 */
public class SQLCompletionProcessor implements IContentAssistProcessor {

	private DatabaseNode connectionModelCache;
	// private DatabaseModel databaseModelCache;
	private String[] databaseNamesCache;
	private String[] tableNamesCache;

	/*
	 * (non-Javadoc) Method declared on IContentAssistProcessor
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int documentOffset) {
		DatabaseNode activeDatabaseNode = DatabaseView.getActiveDatabase();
		if (activeDatabaseNode == null) {
			return null;
		}

		String[] proposals = null;
		if (activeDatabaseNode != connectionModelCache) {
			tableNamesCache = getModelNames(activeDatabaseNode.getChildren());
			proposals = tableNamesCache;
		} else {
			databaseNamesCache = getModelNames(activeDatabaseNode.getChildren());
			proposals = databaseNamesCache;
		}

		return getProposals(proposals, documentOffset);
	}

	private String[] getModelNames(BaseTreeNode model[]) {
		String[] proposals = new String[model.length];
		for (int i = 0; i < proposals.length; i++) {
			proposals[i] = model[i].getName();
		}
		return proposals;
	}

	private ICompletionProposal[] getProposals(String[] proposals, int documentOffset) {
		ICompletionProposal[] result = new ICompletionProposal[proposals.length];
		for (int i = 0; i < proposals.length; i++) {
			result[i] = new CompletionProposal(proposals[i], documentOffset, 0, proposals[i].length());
		}

		return result;
	}

	/*
	 * (non-Javadoc) Method declared on IContentAssistProcessor
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int documentOffset) {
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on IContentAssistProcessor
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return new char[] { '#', '$' };
	}

	/*
	 * (non-Javadoc) Method declared on IContentAssistProcessor
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on IContentAssistProcessor
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	/*
	 * (non-Javadoc) Method declared on IContentAssistProcessor
	 */
	public String getErrorMessage() {
		return null;
	}
}
