package org.jtester.savexp.xstream.converter.reflection;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaVariable;

import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;

public interface ExReflectionProvider extends ReflectionProvider {
	public interface ExVisitor extends Visitor {
		void visit(String name, IJavaType type, IJavaType definedIn, IJavaVariable value) throws DebugException;
	}
}
