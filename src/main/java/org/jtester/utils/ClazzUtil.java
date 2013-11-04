package org.jtester.utils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ClazzUtil {

	/**
	 * 根据class names查找对应的class
	 * 
	 * @param clazzNames
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> findClazzes(Set<String> clazzNames) throws ClassNotFoundException {
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		for (String clazzName : clazzNames) {
			clazzes.add(Class.forName(clazzName));
		}
		return clazzes;
	}

	/**
	 * 根据class names查找对应的class
	 * 
	 * @param clazzNames
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Set<Class<?>> findClazzes(String[] clazzNames) throws ClassNotFoundException {
		Set<Class<?>> clazzes = new HashSet<Class<?>>();
		for (String clazzName : clazzNames) {
			clazzes.add(Class.forName(clazzName));
		}
		return clazzes;
	}

	/**
	 * 判断clazz是否是tested class的测试类
	 * 
	 * @param clazz
	 * @param tested
	 *            被测试的java类
	 * @return
	 */
	public static final boolean isTestClazzFor(String clazz, Class<?> tested) {
		if (!(clazz.endsWith("Test") || clazz.contains("Test_"))) {
			return false;
		}
		return clazz.contains(tested.getSimpleName());
	}

	public final static String getPackFromClassName(String clazzName) {
		int index = clazzName.lastIndexOf(".");
		String pack = "";
		if (index > 0) {
			pack = clazzName.substring(0, index);
		}
		return pack;
	}

	public final static String getPathFromPath(String clazzName) {
		String pack = getPackFromClassName(clazzName);
		return pack.replace(".", String.valueOf(File.separatorChar));
	}

	/**
	 * 是否是合法的java clazz name字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isJavaClassNamePart(char c) {
		return Character.isJavaIdentifierPart(c) || c == '.';
	}
}
