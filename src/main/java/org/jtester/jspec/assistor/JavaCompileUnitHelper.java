package org.jtester.jspec.assistor;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IParent;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ClassFile;
import org.eclipse.jdt.internal.core.ImportContainer;
import org.eclipse.jdt.internal.core.ImportDeclaration;
import org.eclipse.jdt.internal.core.PackageDeclaration;

/**
 * 工具类：根据编译文件查找指定方法和父类
 * 
 * @author darui.wudr
 */
@SuppressWarnings("restriction")
public class JavaCompileUnitHelper {
    IType              clazType        = null;

    ImportContainer    importContainer = null;

    PackageDeclaration packDeclaration = null;

    public JavaCompileUnitHelper(ICompilationUnit compileUnit) throws JavaModelException {
        init(compileUnit);
    }

    public JavaCompileUnitHelper(ClassFile clazFile) throws JavaModelException {
        init(clazFile);
    }

    public JavaCompileUnitHelper(IType clazType) throws JavaModelException {
        IJavaElement parent = clazType.getParent();
        if (parent instanceof ICompilationUnit) {
            init((ICompilationUnit) parent);
        } else if (parent instanceof ClassFile) {
            init((ClassFile) parent);
        }
    }

    private void init(IParent parentUnit) throws JavaModelException {
        IJavaElement[] elems = parentUnit.getChildren();
        for (IJavaElement je : elems) {
            if (je instanceof PackageDeclaration) {
                this.packDeclaration = (PackageDeclaration) je;
            }
            if (je instanceof ImportContainer) {
                this.importContainer = (ImportContainer) je;
            }
            if (je instanceof IType) {
                this.clazType = (IType) je;
            }
        }
    }

    public IMethod getIMethod(String methodname, int argv) throws JavaModelException {
        if (clazType == null) {
            return null;
        }
        IMethod[] methods = clazType.getMethods();
        if (methods == null || methods.length == 0) {
            return null;
        }
        for (IMethod method : methods) {
            if (method.getElementName().equals(methodname) == false) {
                continue;
            }
            if (method.getParameterNames().length == argv) {
                return method;
            }
        }
        return null;
    }

    public IType getSuperClass() throws JavaModelException {
        if (this.clazType == null) {
            return null;
        }
        String superClaz = this.clazType.getSuperclassName();
        if (superClaz == null || superClaz.equals("JSpec") || superClaz.endsWith(".JSpec")
                || superClaz.equals("Object") || superClaz.endsWith(".Object")) {
            return null;
        }
        IJavaElement[] imports = this.importContainer == null ? new IJavaElement[0] : this.importContainer
                .getChildren();
        for (IJavaElement item : imports) {
            if (!(item instanceof ImportDeclaration)) {
                continue;
            }
            ImportDeclaration declaration = (ImportDeclaration) item;
            String elementName = declaration.getElementName();
            if (elementName.endsWith("." + superClaz)) {
                return this.clazType.getJavaProject().findType(elementName);
            }
        }
        String packName = this.packDeclaration == null ? "" : this.packDeclaration.getElementName();
        String typeName = packName == null ? superClaz : packName + "." + superClaz;
        return this.packDeclaration.getJavaProject().findType(typeName);
    }

    public MixAnnotation getMixAnnotation() {
        if (this.clazType == null) {
            return null;
        }
        IAnnotation mix = this.clazType.getAnnotation("Mix");
        return mix == null || !mix.exists() ? null : new MixAnnotation(mix, this.importContainer, this.packDeclaration);
    }
}
