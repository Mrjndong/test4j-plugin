package org.jtester.jspec.assistor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.ui.editors.text.FileDocumentProvider;
import org.eclipse.ui.part.FileEditorInput;
import org.jtester.jspec.JSpecProp;

public class JSpecDocumentProvider extends FileDocumentProvider {
	public static final String GWZ_TAG = "__gwz__tag";

	public static final String SCENARIO_TAG = "__scenario__tag";

	public static final String STORY_TAG = "__story__tag";

	public static final String PARAMETER_TAG = "__parameter__tag";

	protected IDocument createDocument(Object element) throws CoreException {
		if (!(element instanceof FileEditorInput)) {
			return null;
		}
		IProject project = ((FileEditorInput) element).getFile().getProject();
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner = new JSpecFastPartitioner(project);
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}

	public static String[] getScanTags() {
		return new String[] { GWZ_TAG, SCENARIO_TAG, STORY_TAG, PARAMETER_TAG };
	}
}

class JSpecFastPartitioner extends FastPartitioner {
	IProject project;

	JSpecFastPartitioner(IProject project) {
		super(new JSpecPartitionScanner(), JSpecDocumentProvider.getScanTags());
		this.project = project;
	}

	public IProject getProject() {
		return this.project;
	}
}

class JSpecPartitionScanner extends RuleBasedPartitionScanner {

	public JSpecPartitionScanner() {
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		rules.addAll(getGwzRules());
		rules.add(new SingleLineRule(JSpecProp.SCENARIO_KEY_WORD, " ", new Token(JSpecDocumentProvider.SCENARIO_TAG)));
		rules.add(new SingleLineRule(JSpecProp.STORY_KEY_WORD, " ", new Token(JSpecDocumentProvider.STORY_TAG)));
		// rules.add(new
		// SingleLineRule(String.valueOf(JSpecProp.STEP_PARA_START_CHAR), "=",
		// new Token(
		// JSpecDocumentProvider.PARAMETER_TAG)));
		// rules.add(new
		// SingleLineRule(String.valueOf(JSpecProp.STEP_PARA_END_CHAR), "", new
		// Token(
		// JSpecDocumentProvider.PARAMETER_TAG)));
		rules.add(new MultiLineRule(String.valueOf(JSpecProp.STEP_PARA_START_CHAR), String
				.valueOf(JSpecProp.STEP_PARA_END_CHAR), new Token(JSpecDocumentProvider.PARAMETER_TAG),
				JSpecProp.JSPEC_ESCAPE_CHAR));

		super.setPredicateRules(rules.toArray(new IPredicateRule[0]));
	}

	private List<IPredicateRule> getGwzRules() {
		IToken gwz = new Token(JSpecDocumentProvider.GWZ_TAG);
		List<IPredicateRule> rules = new ArrayList<IPredicateRule>();
		for (String key : JSpecProp.KEY_WORDS) {
			rules.add(new SingleLineRule(key, " ", gwz));
		}
		return rules;
	}
}