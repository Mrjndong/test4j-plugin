package org.jtester.database.assistor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedPartitionScanner;
import org.eclipse.jface.text.rules.Token;

public class SQLPartitionScanner extends RuleBasedPartitionScanner {
	public final static String SQL_COMMENT = "__sql_comment";
	public final static String SQL_KEYWORD = "__sql_keyword";

	public SQLPartitionScanner() {

		List<IRule> rules = new ArrayList<IRule>();

		IToken sqlComment = new Token(SQL_COMMENT);

        /** @TODO: Change hardcoded sql comment keyword */
		rules.add(new EndOfLineRule("--", sqlComment));
		//rules.add(new EndOfLineRule("#", sqlComment));
//		rules.add(new SQLKeyword(tag));

		IPredicateRule[] result= new IPredicateRule[rules.size()];
		rules.toArray(result);
		setPredicateRules(result);
	}
}
