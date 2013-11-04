package org.test4j.plugin.savexp.xstream.converter;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.reflection.ExReflectionProvider;
import org.test4j.plugin.savexp.xstream.converter.reflection.ReflectionConverterUtil;
import org.test4j.plugin.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ReflectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExReflectionConverter extends ReflectionConverter implements ExConverter {

	private ExReflectionProvider reflectionProvider;
	private ExMapper mapper;

	public ExReflectionConverter(ExMapper mapper, ExReflectionProvider reflectionProvider) {
		super(mapper, reflectionProvider);
		this.reflectionProvider = reflectionProvider;
		this.mapper = mapper;
	}

	public boolean canConvert(IJavaType type) {
		return false;
	}

	@Override
	public void marshal(Object original, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(original)) {
			this.marshal((IJavaValue) original, writer, context);
		} else {
			super.marshal(original, writer, context);
		}
	}

	public void marshal(final IJavaValue source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		ReflectionConverterUtil.marshal(reflectionProvider, mapper, source, writer, context);
	}
}
