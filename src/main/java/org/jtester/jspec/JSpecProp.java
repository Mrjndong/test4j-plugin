package org.jtester.jspec;

import static org.test4j.tools.commons.StringHelper.removeTheWhiteSpaces;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jtester.plugin.resources.prop.ClassPathPropertiesFile;

public class JSpecProp {
	private static final String PLUGIN_PROPERTIES = "org/jtester/jspec/jspec.properties";

	private static final ClassPathPropertiesFile classPathPropertiesFile = new ClassPathPropertiesFile(
			PLUGIN_PROPERTIES);

	/**
	 * SCENARIO_KEY_WORD = "Scenario"
	 */
	public static String SCENARIO_KEY_WORD = "Scenario";
	/**
	 * STORY_KEY_WORD = "Story"
	 */
	public static String STORY_KEY_WORD = "Story";

	public static Set<String> KEY_WORDS = new HashSet<String>();

	public static String CANNOT_LOCATE_STEP_TITLE = "";
	public static String CANNOT_LOCATE_STEP_MESSAGE = "";
	public static String STEP_NOT_FOUND_TITLE = "";
	public static String STEP_NOT_FOUND_MESSAGE = "";

	/**
	 * 变量开始符，实际值是资源文件中读取的'【'
	 */
	public static char STEP_PARA_START_CHAR = '[';
	/**
	 * 变量结束符，实际值是资源文件中读取的'】'
	 */
	public static char STEP_PARA_END_CHAR = ']';

	public static char JSPEC_ESCAPE_CHAR = '\\';

	public static Set<Character> ILLEGAL_PARA_CHARS = new HashSet<Character>();

	public static String JSPEC_EDITOR_NAME = "Story Editor";

	public static String JSPEC_COMPARE_VIEWER = "Parameter Compare";

	public static String MARK_FOLDER_COLLAPSED = "collapse folder";

	public static String MARK_FOLDER_EXPANDED = "expaned folder";

	static {

		String propertyValue = classPathPropertiesFile.getProperty("org.jtester.spec.key_words");
		if (propertyValue != null) {
			KEY_WORDS.addAll(removeTheWhiteSpaces(Arrays.asList(propertyValue.trim().split(","))));
		}

		CANNOT_LOCATE_STEP_TITLE = getProp("cannot_locate_step_title", "");

		CANNOT_LOCATE_STEP_MESSAGE = getProp("cannot_locate_step_message", "");

		STEP_NOT_FOUND_TITLE = getProp("step_not_found_title", "");

		STEP_NOT_FOUND_MESSAGE = getProp("step_not_found_message", "");

		STEP_PARA_START_CHAR = getProp("step_para_start_char", '[');

		STEP_PARA_END_CHAR = getProp("step_para_end_char", ']');

		propertyValue = getProp("illegal_para_char", "");
		for (char ch : propertyValue.toCharArray()) {
			ILLEGAL_PARA_CHARS.add(ch);
		}

		JSPEC_EDITOR_NAME = getProp("story_editor_name", JSPEC_EDITOR_NAME);
		JSPEC_COMPARE_VIEWER = getProp("story_compare_viewer", JSPEC_COMPARE_VIEWER);

		MARK_FOLDER_COLLAPSED = getProp("mark.folder.collapsed", MARK_FOLDER_COLLAPSED);

		MARK_FOLDER_EXPANDED = getProp("mark.folder.expanded", MARK_FOLDER_EXPANDED);
	}

	private static String getProp(String key, String _default) {
		String propertyValue = classPathPropertiesFile.getProperty(key);
		if (propertyValue == null) {
			return _default;
		} else {
			return propertyValue.trim();
		}
	}

	private static char getProp(String key, char _default) {
		String value = getProp(key, "");
		if (value == null || value.length() == 0) {
			return _default;
		} else {
			return value.charAt(0);
		}
	}
}
