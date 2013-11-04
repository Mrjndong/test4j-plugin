package org.test4j.plugin.savexp.xstream.converter.reflection;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaFieldVariable;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

public class ExSun14ReflectionProvider extends Sun14ReflectionProvider implements ExReflectionProvider {
	public ExSun14ReflectionProvider() {
		super();
	}

	@Override
	public void visitSerializableFields(Object object, ReflectionProvider.Visitor visitor) {
		if (JdtClazzUtil.isIJavaValue(object)) {
			try {
				this.visitSerializableFields((IJavaValue) object, (ExVisitor) visitor);
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			super.visitSerializableFields(object, visitor);
		}
	}

	private void visitSerializableFields(IJavaValue object, ExSun14ReflectionProvider.ExVisitor visitor)
			throws DebugException {
		for (IVariable var : object.getVariables()) {
			if (JdtClazzUtil.isNullValue(var.getValue())) {
				continue;
			}
			if (JdtClazzUtil.isStaticOrTransient(var)) {
				continue;
			}
			IJavaFieldVariable field = (IJavaFieldVariable) var;
			if (isSkipField(object.getJavaType(), field)) {
				continue;
			}
			IJavaType definedIn = field.getDeclaringType();
			visitor.visit(field.getName(), field.getJavaType(), definedIn, field);
		}
	}

	private static boolean isSkipField(IJavaType type, IJavaFieldVariable field) throws DebugException {
		if (field.getName().equals("cause") && JdtClazzUtil.isAssignableFrom(type, Throwable.class)) {
			return true;
		}
		// if (RefClazzUtil.isSerializable(field.getJavaType(), true)) {
		// return true;
		// }
		return false;
	}
}
