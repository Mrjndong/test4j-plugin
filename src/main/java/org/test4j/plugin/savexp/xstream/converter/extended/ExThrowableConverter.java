package org.test4j.plugin.savexp.xstream.converter.extended;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.ThrowableConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExThrowableConverter extends ThrowableConverter implements ExConverter {
	private ExConverter defaultConverter;

	public ExThrowableConverter(ExConverter defaultConverter) {
		super(defaultConverter);
		this.defaultConverter = defaultConverter;
	}

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isAssignableFrom(type, Throwable.class);
	}

	private final static String GET_STACKTRACE_SIGNATURE = "()[Ljava/lang/StackTraceElement;";

	public void marshal(IJavaValue throwable, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		JdtClazzUtil.invokeNoParaMethod(throwable, "getStackTrace", GET_STACKTRACE_SIGNATURE);
		defaultConverter.marshal(throwable, writer, context);
	}
}
