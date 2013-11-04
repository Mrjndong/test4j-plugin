package org.jtester.savexp.xstream.converter.collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.ArrayConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExArrayConverter extends ArrayConverter implements ExConverter {

	public ExArrayConverter(Mapper mapper) {
		super(mapper);
	}

	@Override
	public void marshal(Object original, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(original)) {
			this.marshal((IJavaValue) original, writer, context);
		} else {
			super.marshal(original, writer, context);
		}
	}

	public boolean canConvert(IJavaType type) {
		if (type instanceof IJavaArrayType) {
			return true;
		} else {
			return false;
		}
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context) {
		IJavaArray value = (IJavaArray) source;
		try {
			for (IJavaValue var : value.getValues()) {
				writeItem(var, context, writer);
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	protected void writeItem(IJavaValue item, MarshallingContext context, HierarchicalStreamWriter writer)
			throws DebugException {
		CollectionConverterHelper.writeItem((ExMapper) mapper(), item, context, writer);
	}
}
