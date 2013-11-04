package org.test4j.plugin.database.assistor;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;
import org.test4j.plugin.helper.ColourManager;

/**
 * @author Ricardo Lecheta
 */
public class SQLKeywordScanner extends RuleBasedScanner {

	// "@" to execute files, ex: @c:\tmp\query.sql
	private static final String KEYWORDS[] = { "@", // <br>
			"WHEN", "PACKAGE", "CREATE", "FUNCTION", "IS", "RETURN", /** <br> */
			"RETURNING", "BEGIN", "END", "EXCEPTION", "WHEN", "THEN", /** <br> */
			"PROCEDURE", "EXIT", "BODY", "REPLACE", "ACCESS", "ACTIVATE", /** <br> */
			"ADD", "ADMIN", "AFTER", "ALL", "ALLOCATE", "ALL_ROWS", "ALTER", /** <br> */
			"ANALYZE", "AND", "ANY", "ARRAY", "AS", "ASC", "AT", "AUDIT", /** <br> */
			"AUTHENTICATED", "AUTHORIZATION", "AUTOEXTEND", "AUTOMATIC", /** <br> */
			"BACKUP", "BECOME", "BEFORE", "BETWEEN", "BITMAP", "BLOCK", "BY", /** <br> */
			"CACHE", "CANCEL", "CASCADE", "CAST", "CFILE", "CHAINED", "CHANGE", /** <br> */
			"CHARACTER", "CHAR_CS", "CHECK", "CHECKPOINT", "CHOOSE", "CHUNK", /** <br> */
			"CLEAR", "CLUSTER", "COALESCE", "COLUMN", "COLUMNS", "COMMENT", "COMMIT", /** <br> */
			"COMPATIBILITY", "COMPILE", "COMPLETE", "COMPRESS", "COMPUTE", "CONNECT", /** <br> */
			"CONSTRAINT", "CONSTRAINTS", "CONTENTS", "CONTINUE", "CONTROLFILE", "COST", /** <br> */
			"CURRENT", "CURSOR", "CYCLE", "DANGLING", "DATABASE", "DATAFILE", "DBA", /** <br> */
			"DEALLOCATE", "DEBUG", "DEFERRABLE", "DEFERRED", "DEGREE", "DELETE", /** <br> */
			"DESC", "DIRECTORY", "DISABLE", "DISCONNECT", "DISTINCT", "DISTRIBUTED", /** <br> */
			"DOUBLE", "DROP", "EACH", "ENABLE", "ENFORCE", "ENTRY", "ESCAPE", /** <br> */
			"ESTIMATE", "EVENTS", "EXCEPTIONS", "EXCHANGE", "EXCLUDING", "EXCLUSIVE", /** <br> */
			"EXECUTE", "EXISTS", "EXPIRE", "EXPLAIN", "EXTENT", "EXTENTS", "EXTERNALLY", /** <br> */
			"FAST", "FILE", "FIRST_ROWS", "FLUSH", "FORCE", "FOREIGN", "FOUND", /** <br> */
			"FREELIST", "FREELISTS", "FROM", "FULL", "GLOBAL", "GLOBAL_NAME", /** <br> */
			"GRANT", "GROUP", "GROUPS", "HASH", "HASHKEYS", "HAVING", "HEADER", /** <br> */
			"HEAP", "IDENTIFIED", "IDLE_TIME", "IMMEDIATE", "IN", "INCLUDING", /** <br> */
			"INCREMENT", "INDEX", "INDEXED", "INDEXES", "INDICATOR", "IND_PARTITION", /** <br> */
			"INITIAL", "INITIALLY", "INITRANS", "INSERT", "INSTANCE", "INSTANCES", /** <br> */
			"INSTEAD", "INTERSECT", "INTO", "IS NULL", "ISOLATION", "ISOLATION_LEVEL", /** <br> */
			"KEEP", "KEY", "KILL", "LAYER", "LESS", "LEVEL", "LIBRARY", "LIKE", "LIMIT", /** <br> */
			"LINK", "LIST", "LOB", "LOCAL", "LOCK", "LOGFILE", "LOGGING", "MASTER", /** <br> */
			"MAXEXTENTS", "MEMBER", "MINEXTENTS", "MINIMUM", "MINUS", "MINVALUE", /** <br> */
			"MODE", "MODIFY", "MOUNT", "MOVE", "MULTISET", "NATIONAL", "NCHAR_CS", /** <br> */
			"NEEDED", "NESTED", "NETWORK", "NEW", "NEXT", "NLS_CALENDAR", "NLS_CHARACTERSET", /** <br> */
			"NLS_ISO_CURRENCY", "NLS_LANGUAGE", "NLS_NUMERIC_", "NLS_SORT", "NLS_TERRITORY", /** <br> */
			"NOARCHIVELOG", "NOAUDIT", "NOCACHE", "NOCOMPRESS", "NOCYCLE", "NOFORCE", /** <br> */
			"NOLOGGING", "NOMAXVALUE", "NOMINVALUE", "NONE", "NOORDER", "NOOVERIDE", /** <br> */
			"NOPARALLEL", "NORESETLOGS", "NOREVERSE", "NORMAL", "NOSORT", "NOT", /** <br> */
			"NOTHING", "NOWAIT", "NULL", "NUMERIC", "OBJECT", "OF", "OFF", "OFFLINE", /** <br> */
			"OID", "OIDINDEX", "OLD", "ON", "ONLINE", "ONLY", "OPCODE", "OPEN", /** <br> */
			"OPTIMAL", "OPTIMIZER_GOAL", "OPTION", "OR", "ORDER", "OVERFLOW", "OWN", /** <br> */
			"PARALLEL", "PARTITION", "PASSWORD", "PCTFREE", "PCTINCREASE", "PCTUSED", /** <br> */
			"PERMANENT", "PLAN", "PLSQL_DEBUG", "PRECISION", "PRESERVE", "PRIMARY", /** <br> */
			"PRIOR", "PRIVATE", "PRIVILEGE", "PRIVILEGES", "PROFILE", "PUBLIC", /** <br> */
			"PURGE", "QUEUE", "QUOTA", "RANGE", "REBUILD", "RECOVER", "RECOVERABLE", /** <br> */
			"RECOVERY", "REF", "REFERENCES", "REFERENCING", "REFRESH", "RENAME", "RESET", /** <br> */
			"RESETLOGS", "RESIZE", "RESOURCE", "RESTRICTED", "REUSE", "REVERSE", /** <br> */
			"REVOKE", "ROLE", "ROLES", "ROLLBACK", "ROW", "ROWLABEL", "ROWNUM", /** <br> */
			"ROWS", "RULE", "SAMPLE", "SAVEPOINT", "SCHEMA", "SCOPE", "SELECT", "SEQUENCE", /** <br> */
			"SERIALIZABLE", "SESSION", "SET", "SHARE", "SHARED", "SHARED_POOL", /** <br> */
			"SHRINK", "SIZE", "SNAPSHOT", "SOME", "SORT", "SPECIFICATION", "SPLIT", /** <br> */
			"SQLERROR", "SQL_TRACE", "STANDBY", "START", "STATEMENT_ID", "STATISTICS", /** <br> */
			"STOP", "STORAGE", "STORE", "STRUCTURE", "SUCCESSFUL", "SWITCH", "SYNONYM", /** <br> */
			"SYSDBA", "SYSOPER", "SYSTEM", "TABLE", "TABLES", "TABLESPACE", "TEMPORARY", /** <br> */
			"THAN", "THE", "TIME", "TIMESTAMP", "TO", "TRACE", "TRACING", "TRANSACTION", /** <br> */
			"TRANSITIONAL", "TRIGGER", "TRIGGERS", "TRUNCATE", "TYPE", "UNDER", "UNDO", /** <br> */
			"UNION", "UNIQUE", "UNLIMITED", "UNLOCK", "UNRECOVERABLE", "UNTIL", /** <br> */
			"UNUSABLE", "UNUSED", "UPDATABLE", "UPDATE", "USAGE", "USE", "USING", /** <br> */
			"VALIDATE", "VALIDATION", "VALUE", "VALUES", "VARCHAR", "VARRAY", /** <br> */
			"VARYING", "VIEW", "WHENEVER", "WHERE", "WITH", "WITHOUT", "WORK", /** <br> */
			"ARRAYLEN", "CASE", "CLOSE", "CONSTANT", "CURRVAL", "DEBUGOFF", /** <br> */
			"DEBUGON", "DECLARE", "DEFAULT", "DEFINTION", "DELAY", "DIGITS", /** <br> */
			"DISPOSE", "DO", "ELSE", "ELSIF", "EXCEPTION_INIT", "FALSE", "FETCH", /** <br> */
			"FOR", "FORM", "GENERIC", "GOTO", "IF", "INTERFACE", "LIMITED", "LOOP", /** <br> */
			"NEXTVAL", "PRAGMA", "RAISE", "RECORD", "RELEASE", "ROWTYPE", "SIGNTYPE", /** <br> */
			"SPACE", "SQL", "STATEMENT", "SUBTYPE", "TASK", "TERMINATE", "TRUE", "VIEWS", "WHILE" };

	public SQLKeywordScanner(ColourManager provider) {
		IToken def = new Token(new TextAttribute(provider.getColor(ColourManager.DEFAULT)));

		IToken string = new Token(new TextAttribute(provider.getColor(ColourManager.STRING)));

		IToken keywordToken = new Token(new TextAttribute(provider.getColor(ColourManager.KEYWORD), provider
				.getColor(ColourManager.BACKGROUND), SWT.BOLD));

		Vector<IRule> rules = new Vector<IRule>();

		// Add rule for single and double quotes
		rules.add(new SingleLineRule("\"", "\"", string, '\\'));
		rules.add(new SingleLineRule("'", "'", string, '\\'));

		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new IWhitespaceDetector() {
			public boolean isWhitespace(char c) {
				return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
			}
		}));

		// Add word rule for keywords, types, and constants.
		// WordRule wordRule = new WordRule(new SQLKeywordDetector());
		SQLWordRule wordRule = new SQLWordRule(def);

		for (String keyword : KEYWORDS) {
			wordRule.addKeyword(keyword, keywordToken);
		}

		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.copyInto(result);
		setRules(result);
	}

	public IToken nextToken() {
		return super.nextToken();
	}
}

class SQLWordRule implements IRule {
	private IToken token;
	private HashMap<String, IToken> keywords = new HashMap<String, IToken>();

	public SQLWordRule(IToken token) {
		this.token = token;
	}

	public void addKeyword(String word, IToken token) {
		keywords.put(word.toUpperCase(), token);
	}

	public IToken evaluate(ICharacterScanner scanner) {
		char c = (char) scanner.read();

		if (Character.isLetter(c)) {
			// System.out.println("char: " + c);
			StringBuffer value = new StringBuffer();
			do {
				value.append(c);
				c = (char) scanner.read();
			} while (Character.isLetterOrDigit(c) || c == '_');
			scanner.unread();
			IToken retVal = (IToken) keywords.get(value.toString().toUpperCase());
			if (retVal != null) {
				return retVal;
			} else {
				return token;
			}
		} else {
			scanner.unread();
			return Token.UNDEFINED;
		}
	}
}