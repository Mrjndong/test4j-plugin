package org.test4j.plugin.savexp.xstream.converter.base;

import java.util.Collections;
import java.util.WeakHashMap;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.StringConverter;

public class ExStringConverter extends StringConverter implements ExSingleValueConverter {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ExStringConverter() {
		super(Collections.synchronizedMap(new WeakHashMap()));
	}

	public boolean canConvert(IJavaType type) {
		Class<?> clazz = JdtClazzUtil.getClazz(type);
		return clazz.equals(String.class) || clazz.equals(StringBuffer.class);
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
