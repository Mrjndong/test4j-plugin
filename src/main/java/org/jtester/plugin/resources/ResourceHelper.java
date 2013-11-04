package org.jtester.plugin.resources;

import static org.jtester.plugin.helper.PluginSetting.PLUGIN_ID;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.plugin.helper.PluginSetting;
import org.jtester.tools.commons.StringHelper;

public class ResourceHelper {
	public static void setExcludedProperty(IProject project, String value) throws CoreException {
		project.setPersistentProperty(EXCLUDED_PROPERTY, value);
	}

	public static void setOverrideProperty(IProject project, boolean state) throws CoreException {
		project.setPersistentProperty(OVERRIDE_PROPERTY, Boolean.toString(state));
	}

	/**
	 * 设置是否运行全部测试
	 * 
	 * @param project
	 * @param state
	 * @throws CoreException
	 */
	public static void setRunAllTestProperty(IProject project, boolean state) throws CoreException {
		project.setPersistentProperty(RUN_PRJ_ALL_TEST, Boolean.toString(state));
	}

	public static void setSourcePathProperty(IProject project, String value) throws CoreException {
		project.setPersistentProperty(SOURCE_PATH, value);
	}

	public static void setResourcePathProperty(IProject project, String value) throws CoreException {
		project.setPersistentProperty(RESOURCE_PATH, value);
	}

	public static void setTestSourcePathProperty(IProject project, String value) throws CoreException {
		project.setPersistentProperty(TEST_SOURCE_PATH, value);
	}

	public static void setTestResourcePathProperty(IProject project, String value) throws CoreException {
		project.setPersistentProperty(TEST_RESOURCE_PATH, value);
	}

	/**
	 * 设置运行ct的vm参数
	 * 
	 * @param project
	 * @param value
	 * @throws CoreException
	 */
	public static void setCtVmParaProperty(IProject project, String value) throws CoreException {
		project.setPersistentProperty(CT_VM_PARA, value);
	}

	private static String getProperty(IProject project, QualifiedName property) {
		try {
			String raw = project.getPersistentProperty(property);
			return raw;
		} catch (CoreException e) {
			PluginLogger.log(e);
			return null;
		}
	}

	/**
	 * 是否运行所有的测试
	 * 
	 * @param project
	 * @return
	 */
	public static boolean getRunAllTestProperty(IProject project) {
		String value = getProperty(project, RUN_PRJ_ALL_TEST);
		return "true".equals(value);
	}

	public static boolean getOverrideProperty(IProject project) {
		String value = getProperty(project, OVERRIDE_PROPERTY);
		return "true".equals(value);
	}

	public static String getExcludedProperty(IProject project) {
		return getProperty(project, EXCLUDED_PROPERTY);
	}

	public static String getSourcePathProperty(IProject project) {
		return getProperty(project, SOURCE_PATH);
	}

	public static String getResourcePathProperty(IProject project) {
		return getProperty(project, RESOURCE_PATH);
	}

	public static String getTestSourcePathProperty(IProject project) {
		return getProperty(project, TEST_SOURCE_PATH);
	}

	public static String getTestResourcePathProperty(IProject project) {
		return getProperty(project, TEST_RESOURCE_PATH);
	}

//	/**
//	 * 返回运行ct的vm参数
//	 * 
//	 * @param project
//	 * @return
//	 */
//	public static String getCtVmParaProperty(IProject project) {
//		return getProperty(project, CT_VM_PARA);
//	}

	private static final QualifiedName CT_VM_PARA = new QualifiedName(PLUGIN_ID, "ct_vm_para");

	private static final QualifiedName RUN_PRJ_ALL_TEST = new QualifiedName(PLUGIN_ID, "run_prj_all_test");
	private static final QualifiedName OVERRIDE_PROPERTY = new QualifiedName(PLUGIN_ID, "override_global");
	private static final QualifiedName EXCLUDED_PROPERTY = new QualifiedName(PLUGIN_ID, "excluded_package");

	private static final QualifiedName SOURCE_PATH = new QualifiedName(PLUGIN_ID, "source_path");
	private static final QualifiedName RESOURCE_PATH = new QualifiedName(PLUGIN_ID, "resource_path");
	private static final QualifiedName TEST_SOURCE_PATH = new QualifiedName(PLUGIN_ID, "test_source_path");
	private static final QualifiedName TEST_RESOURCE_PATH = new QualifiedName(PLUGIN_ID, "test_resource_path");

	/**
	 * 获取项目的test resource路径
	 * 
	 * @param project
	 * @param _default
	 *            如果properites没有设定这个参数，返回默认值_default
	 * @return
	 */
	public static String getProjectTestResourcePath(IProject project) {
		if (project == null) {
			return "src";
		}
		String path = getTestResourcePathProperty(project);
		if (StringHelper.isEmpty(path) == false) {
			return path;
		}
		path = PluginSetting.getTestReSourcePath();
		if (StringHelper.isEmpty(path)) {
			return "src";
		} else {
			return path;
		}
	}
}
