/**
 * 
 */
package org.jtester.definemock.ui.facade;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceMethod;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.jtester.plugin.helper.PluginLogger;
import org.jtester.testsequence.assistor.MethodSequence;
import org.test4j.tools.commons.StringHelper;

/**
 * @author vera 25.01.2006 21:50:37 EditorPartFacade offers easy access to
 *         {@link IEditorPart}
 */
@SuppressWarnings("restriction")
public class EditorPartFacade {

	private final IEditorPart editorPart;
	private final IFile file;

	public EditorPartFacade(IEditorPart editorPart) {
		if (editorPart == null) {
			throw new RuntimeException("Can not wrap a null editor part");
		}
		this.editorPart = editorPart;
		this.file = (IFile) editorPart.getEditorInput().getAdapter(IFile.class);
	}

	public IFile getFile() {
		return file;
	}

	public boolean isJavaFile() {
		return file != null && file.getName().endsWith(".java");
	}

	public ICompilationUnit getCompilationUnit() {
		return file == null ? null : JavaCore.createCompilationUnitFrom(file);
	}

	public ITextSelection getTextSelection() {
		IWorkbenchPartSite site = editorPart.getSite();
		ISelectionProvider selectionProvider = site.getSelectionProvider();
		return (ITextSelection) selectionProvider.getSelection();
	}

	/**
	 * Returns the method that directly surrounds the cursor position, even if
	 * it is part of an anonymous type (you may want to use
	 * {@link #getFirstNonAnonymousMethodSurroundingCursorPosition()} instead).
	 */
	public IMethod getMethodUnderCursorPosition() {
		IMethod method = null;
		try {
			ICompilationUnit compilationUnit = getCompilationUnit();
			if (compilationUnit == null) {
				return null;
			}
			ITextSelection textSelection = getTextSelection();

			IJavaElement javaElement = compilationUnit.getElementAt(textSelection.getOffset());
			if (javaElement instanceof IMethod) {
				method = (IMethod) javaElement;
			} else {
				PluginLogger.log("No method found under cursor position.");
			}
		} catch (JavaModelException e) {
			PluginLogger.log(e);
		}

		return method;
	}

	public IJavaProject getJavaProject() {
		ICompilationUnit compilationUnit = getCompilationUnit();
		return compilationUnit == null ? null : compilationUnit.getJavaProject();
	}

	public IEditorPart getEditorPart() {
		return editorPart;
	}

	/**
	 * Returns the first method that surrounds the cursor position and that is
	 * not part of an anonymous type.
	 */
	public IType getFirstNonAnonymousMethodSurroundingCursorPosition() {
		IType type = getMockUpType();
		IProgressMonitor progressMonitor = this.editorPart.getEditorSite().getActionBars().getStatusLineManager()
				.getProgressMonitor();

		try {
			String superClassName = type.getSuperclassName();
			String parameterTypeName = this.getMockUpClassName(superClassName);
			if (parameterTypeName == null || "".equals(parameterTypeName.trim())) {
				return null;
			}
			List<String> mockTypes = this.getClazzType(type, parameterTypeName);

			System.out.println();

			ITypeHierarchy typeHierarchy = type.newTypeHierarchy(progressMonitor);

			IType superType = typeHierarchy.getType();

			ITypeParameter[] params = superType.getTypeParameters();
			ITypeParameter param = params[0];
			String name = param.getElementName();

			System.out.println();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// return type == null ? null : new
		// MethodFacade(method).getFirstNonAnonymousMethodCallingThisMethod();
		return null;
	}

	private List<String> getClazzType(IType type, String simpleName) throws JavaModelException {
		String[][] clazzes = type.resolveType(simpleName);
		List<String> list = new ArrayList<String>();
		for (String[] clazz : clazzes) {
			String fullname = clazz[0] + "." + clazz[1];
			list.add(fullname);
		}
		return list;
	}

	private String getMockUpClassName(String superClassName) {
		String[] splits = superClassName.split("[<>]");
		if (splits == null || splits.length < 2) {
			return null;
		}
		String mockup = splits[0];
		String clazz = splits[1];
		boolean isMockUp = mockup.endsWith("MockUp");
		if (isMockUp == false) {
			return null;
		} else {
			return clazz;
		}
	}

	private IType getMockUpType() {
		IJavaElement javaElement = getSelectedJavaElement();
		while (javaElement != null) {
			if (javaElement instanceof IType) {
				return (IType) javaElement;
			} else {
				javaElement = javaElement.getParent();
			}
		}
		return null;
	}

	/**
	 * 返回选中文本所对应的JavaElement对象
	 * 
	 * @return
	 */
	private IJavaElement getSelectedJavaElement() {
		if (file == null) {
			return null;
		}
		try {
			ICompilationUnit cunit = JavaCore.createCompilationUnitFrom(file);
			ITextSelection textSelection = getTextSelection();
			IJavaElement javaElement = cunit.getElementAt(textSelection.getOffset());
			return javaElement;
		} catch (JavaModelException e) {
			PluginLogger.log(e);
			return null;
		}
	}

	/**
	 * 返回选中的测试方法所对应的序列记录文件
	 * 
	 * @return
	 */
	public MethodSequence getTestMethodSequence() {
		IJavaElement javaElement = getSelectedJavaElement();
		while (javaElement != null) {
			IJavaElement parent = javaElement.getParent();
			if (javaElement instanceof SourceMethod && parent != null && parent.getParent() instanceof ICompilationUnit) {
				SourceMethod method = (SourceMethod) javaElement;
				ICompilationUnit cunit = (ICompilationUnit) parent.getParent();

				MethodSequence sequence = new MethodSequence();
				MethodSequence.setDatabase();
				String pack = cunit.getParent().getElementName();
				String claz = parent.getElementName();
				claz = StringHelper.isBlankOrNull(pack) ? claz : pack + "." + claz;
				sequence.setTestProject(cunit.getJavaProject());
				sequence.setTestMethod(claz, method.getElementName());
				return sequence;
			} else {
				javaElement = javaElement.getParent();
			}
		}
		return null;
	}
}
