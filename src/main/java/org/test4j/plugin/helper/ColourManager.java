package org.test4j.plugin.helper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.test4j.plugin.jspec.assistor.JSpecDocumentProvider;

public final class ColourManager {
	private Map<RGB, Color> fColorTable = new HashMap<RGB, Color>();

	/**
	 * white
	 */
	public static final RGB BACKGROUND = new RGB(255, 255, 255);
	/**
	 * light green
	 */
	public static final RGB MULTI_LINE_COMMENT = new RGB(0, 128, 0);
	/**
	 * light green
	 */
	public static final RGB SINGLE_LINE_COMMENT = new RGB(0, 128, 0);
	/**
	 * blue
	 */
	public static final RGB KEYWORD = new RGB(0, 0, 128);
	/**
	 * red
	 */
	public static final RGB TYPE = new RGB(128, 0, 0);
	/**
	 * dark green
	 */
	public static final RGB STRING = new RGB(0, 96, 0);
	/**
	 * black
	 */
	public static final RGB DEFAULT = new RGB(0, 0, 0);

	@SuppressWarnings("serial")
	public static Map<String, RGB> KEY_COLOR = new HashMap<String, RGB>() {
		{
			/**
			 * Given When Then 蓝色
			 */
			put(JSpecDocumentProvider.GWZ_TAG, new RGB(0, 0, 255));
			/**
			 * Story 孔雀蓝
			 */
			put(JSpecDocumentProvider.STORY_TAG, new RGB(51, 161, 201));
			/**
			 * Scenario 深蓝色
			 */
			put(JSpecDocumentProvider.SCENARIO_TAG, new RGB(25, 25, 112));
			/**
			 * 森林绿
			 */
			put(JSpecDocumentProvider.PARAMETER_TAG, new RGB(34, 139, 34));
		}
	};

	public ColourManager() {

	}

	public void dispose() {
		for (Color color : this.fColorTable.values()) {
			color.dispose();
		}
	}

	public Color getColor(RGB rgb) {
		Color color = (Color) this.fColorTable.get(rgb);
		if (color == null && rgb != null) {
			color = new Color(Display.getCurrent(), rgb);
			this.fColorTable.put(rgb, color);
		}
		return color;
	}
}