package org.test4j.plugin.savexp.xstream.converter.extended;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.base.ExSingleValueConverter;

import com.thoughtworks.xstream.converters.extended.DurationConverter;

public class ExDurationConverter extends DurationConverter implements ExSingleValueConverter {

	public ExDurationConverter() throws DatatypeConfigurationException {
		super();
	}

	public ExDurationConverter(DatatypeFactory factory) {
		super(factory);
	}

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
