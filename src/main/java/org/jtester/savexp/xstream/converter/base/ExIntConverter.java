package org.jtester.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.IntConverter;

public class ExIntConverter extends IntConverter implements ExSingleValueConverter {
	public boolean canConvert(IJavaType type) {
		Class<?> clazz = JdtClazzUtil.getClazz(type);
		return clazz.equals(int.class) || clazz.equals(Integer.class);
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
