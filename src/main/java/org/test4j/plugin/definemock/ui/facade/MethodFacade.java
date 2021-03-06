package org.test4j.plugin.definemock.ui.facade;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.internal.compiler.classfmt.ClassFileConstants;

@SuppressWarnings("restriction")
public class MethodFacade {
	private final IMethod method;

	public MethodFacade(IMethod method) {
		this.method = method;
	}

	public boolean isTestMethod() {

		try {
			return Signature.SIG_VOID.equals(method.getReturnType())
					&& (method.getFlags() & ClassFileConstants.AccPublic) != 0;
		} catch (JavaModelException e) {
			return false;
		}
	}

	public boolean isAnonymous() {
		return isAnonymous(this.method);
	}

	private boolean isAnonymous(IMethod method) {
		try {
			return method.getParent() instanceof IType && ((IType) method.getParent()).isAnonymous();
		} catch (JavaModelException e) {
			return false;
		}
	}

	public IMethod getFirstNonAnonymousMethodCallingThisMethod() {
		IMethod result = this.method;
		while (isAnonymous(result) && result.getParent().getParent() instanceof IMethod) {
			result = (IMethod) result.getParent().getParent();
		}
		return result;
	}
}
