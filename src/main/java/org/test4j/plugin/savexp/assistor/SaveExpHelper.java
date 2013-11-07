package org.test4j.plugin.savexp.assistor;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.test4j.plugin.helper.PluginLogger;
import org.test4j.plugin.resources.ResourceHelper;
import org.test4j.plugin.savexp.xstream.JSONHelper;

public class SaveExpHelper {
    private final static SimpleDateFormat dateFormat                 = new SimpleDateFormat("yyMMdd HHmmss.SSS");

    private final static String           SAVE_EXPRESSION_FILE_SUFIX = ".json";

    /**
     * 获得保存变量json文件的全路径名称
     * 
     * @param basePath 保存json文件的目录
     * @param variableName 变量名称
     * @return
     */
    public static String getJsonFile(final String basePath, final String variableName) {
        String filename = variableName.replaceAll("[^a-zA-Z_0-9\\.]", "_");
        filename = filename.replaceAll("_{2,}", "_");
        if (filename.length() > 20) {
            filename = filename.substring(0, 20);
        }
        File file = new File(basePath + filename + SAVE_EXPRESSION_FILE_SUFIX);
        if (file.exists()) {
            filename += "-" + dateFormat.format(new Date());
        }
        File dir = new File(basePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return basePath + filename + SAVE_EXPRESSION_FILE_SUFIX;
    }

    /**
     * 将pojo保存为json格式的文件
     * 
     * @param <T>
     * @param o 需要序列化的对象
     * @param filename 存储文件的路径名称
     */
    public static void toJSON(IJavaObject o, String filename) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            String json = JSONHelper.toJSON(o);
            fos.write(json.getBytes());
            fos.close();
        } catch (Throwable e) {
            e.printStackTrace();
            PluginLogger.log("save expression to json excpetion", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据watch expression的jdt value + 项目预设定的test resource路径获取保存变量的目录
     * 
     * @param value
     * @return
     */
    public static String getSavePath(IValue value) {

        try {
            IJavaStackFrame javaStackFrame = JdtClazzUtil.getStackFrame(value);

            IProject project = getProject(value.getLaunch(), javaStackFrame);
            String projectPath = project.getLocation().toString();
            String testResourcePath = ResourceHelper.getProjectTestResourcePath(project);

            String sourceClazzPath = javaStackFrame.getSourcePath();
            sourceClazzPath = sourceClazzPath.replaceFirst("[^\\/\\\\]+\\.java", "");
            return projectPath + File.separatorChar + testResourcePath + File.separatorChar + sourceClazzPath;
        } catch (Throwable e) {
            return Platform.getLocation().toFile().getAbsolutePath() + File.separatorChar + "SaveXML"
                    + File.separatorChar;
        }
    }

    /**
     * 根据launch和JavaStackFrame判断当前活动的Project
     * 
     * @param launch
     * @param javaStackFrame
     * @return
     */
    public static IProject getProject(final ILaunch launch, final IJavaStackFrame javaStackFrame) {
        if (launch == null) {
            return null;
        }
        ISourceLocator locator = launch.getSourceLocator();
        if (locator == null) {
            return null;
        }

        Object sourceElement = locator.getSourceElement(javaStackFrame);
        if (!(sourceElement instanceof IJavaElement) && sourceElement instanceof IAdaptable) {
            sourceElement = ((IAdaptable) sourceElement).getAdapter(IJavaElement.class);
        }

        if (!(sourceElement instanceof IJavaElement)) {
            return null;
        }

        IProject project = ((IJavaElement) sourceElement).getResource().getProject();
        return project;
    }
}
