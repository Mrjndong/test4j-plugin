package org.test4j.plugin.savexp.xstream.converter;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.xstream.ExXStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExSelfStreamingInstanceChecker implements ExConverter {
	private final ExXStream self;
	private ExConverter defaultConverter;

	public ExSelfStreamingInstanceChecker(ExConverter defaultConverter, ExXStream xstream) {
		this.defaultConverter = defaultConverter;
		this.self = xstream;
	}

	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		this.marshal((IJavaValue) source, writer, context);
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context) {
		if (source == self) {
			throw new ConversionException("Cannot marshal the XStream instance in action");
		}
		try {
			defaultConverter.marshal(source, writer, context);
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("rawtypes")
	public boolean canConvert(Class type) {
		return type == self.getClass();
	}

	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		return null;
	}

	public boolean canConvert(IJavaType type) {
		try {
			String clazname = type.getName();
			return clazname.equals(ExXStream.class.getName()) || clazname.equals(XStream.class.getName());
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}
}
