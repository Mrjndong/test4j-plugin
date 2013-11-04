package org.jtester.savexp.xstream.converter;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;

import org.jtester.savexp.xstream.converter.reflection.ExReflectionProvider;
import org.jtester.savexp.xstream.mapper.ExMapper;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.reflection.SerializableConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExSerializableConverter extends SerializableConverter implements ExConverter {
	protected ExReflectionProvider reflectionProvider;
	protected ExMapper mapper;

	public ExSerializableConverter(Mapper mapper, ExReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
		this.mapper = (ExMapper) mapper;
		this.reflectionProvider = reflectionProvider;
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(source)) {
			this.marshal((IJavaValue) source, writer, context);
		} else {
			super.marshal(source, writer, context);
		}
	}

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isSerializable(type, true);
	}

	public void marshal(IJavaValue original, HierarchicalStreamWriter writer, MarshallingContext context) {
		// TODO
	}
}
