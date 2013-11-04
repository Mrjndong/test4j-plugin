package org.jtester.savexp.xstream.converter.extended;

import java.nio.charset.Charset;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.base.ExSingleValueConverter;

import com.thoughtworks.xstream.converters.extended.CharsetConverter;

public class ExCharsetConverter extends CharsetConverter implements ExSingleValueConverter {

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isAssignableFrom(type, Charset.class);
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
		if (JdtClazzUtil.isNullValue(obj)) {
			return null;
		} else {
			String value = JdtClazzUtil.callNoneParaReturnStringMethod(obj, "name");
			return value;
		}
	}
}
