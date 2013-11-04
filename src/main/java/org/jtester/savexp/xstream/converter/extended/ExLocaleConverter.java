package org.jtester.savexp.xstream.converter.extended;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.base.ExSingleValueConverter;

import com.thoughtworks.xstream.converters.extended.LocaleConverter;

public class ExLocaleConverter extends LocaleConverter implements ExSingleValueConverter {

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
		String toString = JdtClazzUtil.toString(obj);
		return toString;
	}
}
