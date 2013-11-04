package org.test4j.plugin.savexp.xstream.converter.reflection;

import java.io.Externalizable;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;
import org.test4j.plugin.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.reflection.ExternalizableConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExExternalizableConverter extends ExternalizableConverter implements ExConverter {
	@SuppressWarnings("unused")
	private ExMapper mapper;

	public ExExternalizableConverter(ExMapper mapper) {
		super(mapper);
		this.mapper = mapper;
	}

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isAssignableFrom(type, Externalizable.class);
	}

	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(source)) {
			this.marshal((IJavaValue) source, writer, context);
		} else {
			super.marshal(source, writer, context);
		}
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context) {
		// TODO Auto-generated method stub
	}
}
