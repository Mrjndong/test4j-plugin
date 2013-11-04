package org.test4j.plugin.savexp.xstream.converter.base;

import java.util.Date;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.basic.DateConverter;

public class ExDateConverter extends DateConverter implements ExSingleValueConverter {
	public boolean canConvert(IJavaType type) {
		Class<?> clazz = JdtClazzUtil.getClazz(type);
		return clazz.equals(Date.class);
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
		try {
			IVariable fastTime = obj.getVariables()[7];
			IJavaPrimitiveValue value = (IJavaPrimitiveValue) fastTime.getValue();
			Date date = new Date();
			date.setTime(value.getLongValue());
			return super.toString(date);
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}
}
