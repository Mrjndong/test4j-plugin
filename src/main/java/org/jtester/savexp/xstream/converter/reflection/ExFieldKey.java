package org.jtester.savexp.xstream.converter.reflection;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaType;

public class ExFieldKey {
	final private String fieldName;
	final private IJavaType declaringClass;
	final private int depth;
	final private int order;

	public ExFieldKey(String fieldName, IJavaType declaringClass, int order) {
		if (fieldName == null || declaringClass == null) {
			throw new IllegalArgumentException("fieldName or declaringClass is null");
		}
		this.fieldName = fieldName;
		this.declaringClass = declaringClass;
		this.order = order;
		depth = this.getDepth(declaringClass);
	}

	private int getDepth(IJavaType type) {
		if (type instanceof IJavaClassType) {
			IJavaClassType cls = (IJavaClassType) type;
			int depth = 0;
			try {
				while (cls.getSuperclass() != null) {
					depth++;
					cls = cls.getSuperclass();
				}
				return depth;
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			return 0;
		}
	}

	public String getFieldName() {
		return this.fieldName;
	}

	public IJavaType getDeclaringClass() {
		return this.declaringClass;
	}

	public int getDepth() {
		return this.depth;
	}

	public int getOrder() {
		return this.order;
	}

	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof ExFieldKey))
			return false;

		final ExFieldKey fieldKey = (ExFieldKey) o;

		if (!declaringClass.equals(fieldKey.declaringClass))
			return false;
		if (!fieldName.equals(fieldKey.fieldName))
			return false;

		return true;
	}

	public int hashCode() {
		int result;
		result = fieldName.hashCode();
		result = 29 * result + declaringClass.hashCode();
		return result;
	}

	public String toString() {
		return "FieldKey{" + "order=" + order + ", writer=" + depth + ", declaringClass=" + declaringClass
				+ ", fieldName='" + fieldName + "'" + "}";
	}
}
