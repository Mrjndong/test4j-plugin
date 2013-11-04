package org.jtester.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.BigDecimalConverter;

public class ExBigDecimalConverter extends BigDecimalConverter implements ExSingleValueConverter {

	public boolean canConvert(IJavaType type) {
		return false;
	}

	@Override
	public String toString(Object obj) {
		if (JdtClazzUtil.isIJavaValue(obj)) {
			return this.toString((IJavaValue) obj);
		} else {
			return super.toString(obj);
		}
	}

	public String toString(IJavaValue obj) {
		String toString = JdtClazzUtil.toString((IJavaObject) obj);
		return toString;
	}
}
