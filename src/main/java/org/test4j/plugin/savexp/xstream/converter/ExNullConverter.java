package org.test4j.plugin.savexp.xstream.converter;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.basic.NullConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExNullConverter extends NullConverter implements ExConverter {
	@Override
	public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(source)) {
			this.marshal((IJavaValue) source, writer, context);
		} else {
			super.marshal(source, writer, context);
		}
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context) {
		ExtendedHierarchicalStreamWriterHelper.startNode(writer, "null", null);
		writer.endNode();
	}

	public boolean canConvert(IJavaType type) {
		return type == null || JdtClazzUtil.isAssignableFrom(type, Mapper.Null.class);
	}

}
