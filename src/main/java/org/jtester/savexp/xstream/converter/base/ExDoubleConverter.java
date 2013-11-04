package org.jtester.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.DoubleConverter;

public class ExDoubleConverter extends DoubleConverter implements ExSingleValueConverter {

	public boolean canConvert(IJavaType type) {
		Class<?> clazz = JdtClazzUtil.getClazz(type);
		return clazz.equals(double.class) || clazz.equals(Double.class);
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
