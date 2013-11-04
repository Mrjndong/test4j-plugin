package org.test4j.plugin.jspec.assistor;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.ImportContainer;
import org.eclipse.jdt.internal.core.ImportDeclaration;
import org.eclipse.jdt.internal.core.PackageDeclaration;

@SuppressWarnings("restriction")
public class MixAnnotation {
    private IAnnotation        annotation      = null;

    private ImportContainer    importContainer = null;

    private PackageDeclaration packDeclaration = null;

    public MixAnnotation(IAnnotation annotation, ImportContainer importContainer, PackageDeclaration packDeclaration) {
        this.annotation = annotation;
        this.importContainer = importContainer;
        this.packDeclaration = packDeclaration;
    }

    public IMethod findMethod(String methodName, int argv) throws JavaModelException {
        if (annotation == null) {
            return null;
        }
        Object[] steps = this.getStepsType();
        for (Object step : steps) {
            IMethod method = this.findMethodInSteps(String.valueOf(step), methodName, argv);
            if (method != null) {
                return method;
            }
        }
        return null;
    }

    private IMethod findMethodInSteps(String step, String methodName, int argv) throws JavaModelException {
        IType clazType = getStepClassType(step);
        IMethod[] methods = clazType.getMethods();
        if (methods == null || methods.length == 0) {
            return null;
        }
        for (IMethod method : methods) {
            if (method.getElementName().equals(methodName) == false) {
                continue;
            }
            if (method.getParameterNames().length == argv) {
                return method;
            }
        }
        return null;
    }

    private IType getStepClassType(String step) throws JavaModelException {
        IJavaElement[] imports = this.importContainer == null ? new IJavaElement[0] : this.importContainer
                .getChildren();
        for (IJavaElement item : imports) {
            if (!(item instanceof ImportDeclaration)) {
                continue;
            }
            ImportDeclaration declaration = (ImportDeclaration) item;
            String elementName = declaration.getElementName();
            if (elementName.endsWith("." + step)) {
                return this.packDeclaration.getJavaProject().findType(elementName);
            }
        }
        String packName = this.packDeclaration == null ? "" : this.packDeclaration.getElementName();
        String typeName = packName == null ? step : packName + "." + step;
        return this.packDeclaration.getJavaProject().findType(typeName);
    }

    private Object[] getStepsType() throws JavaModelException {
        IMemberValuePair[] pair = annotation.getMemberValuePairs();
        for (IMemberValuePair item : pair) {
            String name = item.getMemberName();
            if (!"value".equals(name)) {
                continue;
            }
            final Object value = item.getValue();
            if (value instanceof Object[]) {
                return (Object[]) value;
            } else {
                return new Object[] { value };
            }
        }
        return new String[0];
    }
}
