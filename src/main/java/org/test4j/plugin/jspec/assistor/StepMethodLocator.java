package org.test4j.plugin.jspec.assistor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.test4j.spec.annotations.Named;
import org.test4j.spec.inner.StepType;
import org.test4j.tools.commons.StringHelper;

@SuppressWarnings("restriction")
public class StepMethodLocator {
    public static IJavaElement findMethod(StepType gwzType, String step, String[] args, IFile file) throws Exception {
        IPath javapath = new Path(file.getProjectRelativePath().toOSString().replace(".story", ".java"));
        IFile javafile = file.getProject().getFile(javapath);
        if (javafile.exists() == false) {
            createJavaFile(javafile);
        }
        String methodName = StringHelper.camel(step);
        IMethod method = getMethod(javafile, methodName, args.length);
        if (method == null) {
            createJSpecStepMethod(javafile, gwzType, step, args);
            return getMethod(javafile, methodName, args.length);
        } else {
            return method;
        }
    }

    private static IMethod getMethod(IFile java, String methodName, int argv) throws JavaModelException {
        ICompilationUnit unit = JavaCore.createCompilationUnitFrom(java);
        JavaCompileUnitHelper jcu = new JavaCompileUnitHelper(unit);
        MixAnnotation mix = null;
        while (jcu != null) {
            IMethod method = jcu.getIMethod(methodName, argv);
            if (method != null) {
                return method;
            } else if (mix == null) {
                mix = jcu.getMixAnnotation();
            }
            IType superType = jcu.getSuperClass();
            if (superType == null) {
                break;
            }
            jcu = new JavaCompileUnitHelper(superType);
        }
        return mix == null ? null : mix.findMethod(methodName, argv);
    }

    static void createJSpecStepMethod(IFile ifile, StepType gwzType, String step, String[] argNames) throws Exception {
        ICompilationUnit unit = JavaCore.createCompilationUnitFrom(ifile);
        String methodName = StringHelper.camel(step);
        String context = buildMethodContext(gwzType, methodName, argNames);

        IType type = unit.findPrimaryType();
        type.createMethod(context, null, true, null);
        unit.createImport(gwzType.getAnnotatonClaz().getName(), null, null);
        if (argNames != null && argNames.length != 0) {
            unit.createImport(Named.class.getName(), null, null);
        }
    }

    private static void createJavaFile(IFile javaFile) throws CoreException {
        String context = buildJavaContext(getPackName(javaFile), getClazName(javaFile));
        InputStream is = new ByteArrayInputStream(context.getBytes());
        javaFile.create(is, IResource.NONE, null);
    }

    static String getPackName(IFile file) {
        IFolder folder = (IFolder) file.getParent();
        IJavaElement packElement = JavaCore.create(folder);
        if (packElement instanceof PackageFragmentRoot) {
            return "";
        } else {
            return packElement.getElementName();
        }
    }

    static String getClazName(IFile file) {
        String name = file.getName();
        return name.replace(".java", "");
    }

    /**
     * 生成新的JSpec java文件
     * 
     * @param packName story文件所在的package路径
     * @param clazName story文件名称
     * @return
     */
    static String buildJavaContext(String packName, String clazName) {
        StringBuilder buff = new StringBuilder();

        if (packName != null && "".equals(packName) == false) {
            buff.append("package ").append(packName).append(";\n");
            buff.append("\n");
        }
        buff.append("import org.test4j.spec.JSpec;\n");
        buff.append("import org.testng.annotations.Test;\n");
        buff.append("import org.test4j.spec.step.JSpecScenario;\n");
        buff.append("\n");

        buff.append("public class ").append(clazName).append(" extends JSpec {\n");
        // buff.append("\t@Override\n");
        buff.append("\t@Test(dataProvider = \"story\", groups = \"jspec\")\n");
        buff.append("\tpublic void runStory(JSpecScenario scenario) throws Exception {\n");
        buff.append("\t\tthis.run(scenario);\n");
        buff.append("\t}\n");
        buff.append("}");

        return buff.toString();
    }

    /**
     * 构建jspec step方法
     * 
     * @param steptype step类型@Given @When @Then
     * @param methodname step方法名称
     * @param argNames 参数名称和个数
     * @return
     */
    static String buildMethodContext(StepType steptype, String methodname, String[] argNames) {
        StringBuilder buff = new StringBuilder();

        buff.append("\t@").append(steptype).append("\n");
        buff.append("\tpublic void ").append(methodname).append("(");
        if (argNames != null) {
            boolean isFirst = true;
            for (String arg : argNames) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    buff.append(", // <br>\n\t\t");
                }
                buff.append("final @Named(\"").append(arg).append("\") ");
                buff.append("String ").append(arg);
            }
        }
        if (argNames != null && argNames.length != 0) {
            buff.append("//<br>\n\t");
        }
        buff.append(")throws Exception {\n");
        buff.append("\t\t// TODO\n");
        buff.append("\t}\n");
        return buff.toString();
    }
}
