package org.test4j.plugin.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.BooleanConverter;

public class ExBooleanConverter extends BooleanConverter implements ExSingleValueConverter {
	private final String positive;
	private final String negative;

	public ExBooleanConverter(final String positive, final String negative, final boolean caseSensitive) {
		super(positive, negative, caseSensitive);
		this.positive = positive;
		this.negative = negative;
	}

	public ExBooleanConverter() {
		this("true", "false", false);
	}

	public boolean canConvert(IJavaType type) {
		Class<?> clazz = JdtClazzUtil.getClazz(type);
		return clazz.equals(boolean.class) || clazz.equals(Boolean.class);
	}

	@Override
	public String toString(Object obj) {
		if (JdtClazzUtil.isIJavaValue(obj)) {
			return this.toString((IJavaValue) obj);
		} else {
			return super.toString(obj);
		}
	}

	public String toString(final IJavaValue obj) {
		final Boolean value = Boolean.valueOf(JdtClazzUtil.getValueString(obj));
		return obj == null ? null : value.booleanValue() ? positive : negative;
	}
}
