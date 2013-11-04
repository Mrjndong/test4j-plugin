package org.test4j.plugin.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.ShortConverter;

public class ExShortConverter extends ShortConverter implements ExSingleValueConverter {

	public boolean canConvert(IJavaType type) {
		Class<?> clazz = JdtClazzUtil.getClazz(type);
		return clazz.equals(short.class) || clazz.equals(Short.class);
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
		return JdtClazzUtil.getValueString(obj);
	}
}
