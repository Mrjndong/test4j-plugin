package org.test4j.plugin.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.StringBufferConverter;

public class ExStringBufferConverter extends StringBufferConverter implements ExSingleValueConverter {

	public boolean canConvert(IJavaType type) {
		return StringBuffer.class.equals(JdtClazzUtil.getClazz(type));
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
		String toString = JdtClazzUtil.toString(obj);
		return toString;
	}
}
