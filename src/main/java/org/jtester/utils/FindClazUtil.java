package org.jtester.utils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * 
 * @author darui.wudr
 * 
 */
public class FindClazUtil {

	/**
	 * 在IDE容器环境下,获取classpath中指定package下所有clazz name
	 * 
	 * @param classpath
	 *            Java classpath列表
	 * @param packagename
	 *            package name
	 * @return
	 * @throws IOException
	 */
	protected static Set<String> findClazzInIdeApp(String classpath, String packagename) {
		Set<String> clazzes = new HashSet<String>();
		StringTokenizer tokenizer = new StringTokenizer(classpath, File.pathSeparator);
		while (tokenizer.hasMoreTokens()) {
			String entry = tokenizer.nextToken();

			if (entry.endsWith(".jar")) {
				try {
					clazzes.addAll(findClazzInJarFile(new JarFile(entry), packagename));
				} catch (IOException e) {
					String error = String.format("finding error,packagename:%s ; classpath entry:%s", packagename,
							entry);
					throw new RuntimeException(error, e);
				}
			} else {
				clazzes.addAll(findClazzInIdeTarget(entry, packagename));
			}
		}

		return clazzes;
	}

	/**
	 * 在jar app环境下,获得classpath中指定package下所有的class name<br>
	 * 包括jar包中manifest下指定的所有class path
	 * 
	 * @param clazPath
	 * @param packPath
	 * @return
	 * @throws IOException
	 */
	protected static Set<String> findClazzInJarApp(String jarName, String packPath) throws IOException {
		Set<String> clazzes = new HashSet<String>();

		JarFile jarFile = new JarFile(jarName);
		clazzes.addAll(findClazzInJarFile(jarFile, packPath));

		Manifest manifest = jarFile.getManifest();
		if (manifest == null) {
			return clazzes;
		}

		String jarClassPath = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH);
		StringTokenizer tokenizer = new StringTokenizer(jarClassPath, " ");

		while (tokenizer.hasMoreTokens()) {
			String jarFileName = (String) tokenizer.nextToken();
			clazzes.addAll(findClazzInJarFile(new JarFile(jarFileName), packPath));
		}

		return clazzes;
	}

	/**
	 * 在IDE的编译路径中获取packPath下所有的class
	 * 
	 * @param target
	 * @param pathPath
	 * @return
	 */
	protected static List<String> findClazzInIdeTarget(String target, String packPath) {
		List<String> clazzes = new LinkedList<String>();
		String dir = target + File.separator + packPath.replace('.', File.separatorChar);
		File directory = new File(dir);
		String[] classnames = directory.list();
		if (classnames == null) {
			return clazzes;
		}

		for (String clazname : classnames) {
			if (clazname.endsWith(".class") == false) {
				continue;
			}
			String claz = packPath + "." + clazname.substring(0, clazname.indexOf('.'));
			clazzes.add(claz);
		}
		return clazzes;
	}

	/**
	 * 获得jar文件中package下面所有的class
	 * 
	 * @param aJarFile
	 * @param aPackage
	 * @return
	 */
	private static List<String> findClazzInJarFile(JarFile aJarFile, String packPath) {
		List<String> clazzes = new LinkedList<String>();
		Enumeration<JarEntry> jarEntries = aJarFile.entries();

		String pack = FindClazUtil.pathReplace(packPath);
		while (jarEntries.hasMoreElements()) {
			String clazName = jarEntries.nextElement().getName();

			int index = clazName.lastIndexOf(".class");
			if (index == -1 || index != clazName.length() - 6) {
				continue;
			}
			clazName = FindClazUtil.pathReplace(clazName.substring(0, index));
			index = clazName.lastIndexOf(".");
			String packName = "";
			if (index != -1) {
				packName = clazName.substring(0, index);
			}
			if (packName.equals(pack)) {
				clazzes.add(clazName);
			}
		}
		return clazzes;
	}

	/**
	 * 替换路径中所有的/\为.
	 * 
	 * @param path
	 * @return
	 */
	protected static String pathReplace(String path) {
		return path.replace('/', '.').replace('\\', '.');
	}

	/**
	 * 获得packPath路径下所有的class
	 * 
	 * @param packPath
	 * @return
	 */
	public static final String SUN_BOOT_PATH = "sun.boot.class.path";

	public static final String JAVA_EXT_DIRS = "java.ext.dirs";

	public static final String JAVA_CLASS_PATH = "java.class.path";

	/**
	 * 获得java应用指定package下所有的class name
	 * 
	 * @param packagename
	 * @return
	 */
	public static Set<String> findClazz(String packagename) {
		String classPath = System.getProperty(JAVA_CLASS_PATH);
		boolean runInIDE = FindClazUtil.isAppRunningInIde(classPath);
		try {
			Set<String> clazzes = null;
			if (runInIDE) {
				clazzes = FindClazUtil.findClazzInIdeApp(classPath, packagename);
			} else {
				clazzes = FindClazUtil.findClazzInJarApp(classPath, packagename);
			}
			return clazzes;
		} catch (Exception e) {
			String error = String.format("RunInIDE : %s , classpath : %s", String.valueOf(runInIDE), classPath);
			throw new RuntimeException(error, e);
		}
	}

	/**
	 * 获得的与claz的package相同的所有class
	 * 
	 * @param claz
	 * @return
	 */
	public static Set<String> findClazz(Class<?> claz) {
		String pack = claz.getPackage().getName();
		return FindClazUtil.findClazz(pack);
	}

	/**
	 * 获得class的package路径
	 * 
	 * @param claz
	 * @return
	 */
	public static String finePackageDir(Class<?> claz) {
		String pack = claz.getPackage().getName();
		return pack.replaceAll("\\.", "/");
	}

	/**
	 * 判断应用程序是否运行在IDE容器里
	 * 
	 * @param clazPath
	 * @return
	 */
	protected static boolean isAppRunningInIde(String clazPath) {
		boolean in_ide = false;

		StringTokenizer tokenizer = new StringTokenizer(clazPath, File.pathSeparator);

		while (tokenizer.hasMoreTokens()) {
			String entry = (String) tokenizer.nextToken();

			if (entry.endsWith(".jar") == false) {
				in_ide = true;
				break;
			}
		}
		return in_ide;
	}

	/**
	 * 判断App是否是已jar方式运行的
	 * 
	 * @param clazPath
	 * @return
	 */
	protected static boolean isAppRunningInJar(String clazPath) {
		boolean result = false;

		StringTokenizer tokenizer = new StringTokenizer(clazPath, File.pathSeparator);

		if (tokenizer.countTokens() == 1 && clazPath.endsWith(".jar")) {
			result = true;
		}
		return result;
	}
}
