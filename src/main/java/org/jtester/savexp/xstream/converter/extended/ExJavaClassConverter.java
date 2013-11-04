package org.jtester.savexp.xstream.converter.extended;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.base.ExSingleValueConverter;

import com.thoughtworks.xstream.converters.extended.JavaClassConverter;

public class ExJavaClassConverter extends JavaClassConverter implements ExSingleValueConverter {
	public ExJavaClassConverter(ClassLoader classLoader) {
		super(classLoader);
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
		try {
			String clazname = JdtClazzUtil.getClazzName(obj);
			return clazname;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}
}
