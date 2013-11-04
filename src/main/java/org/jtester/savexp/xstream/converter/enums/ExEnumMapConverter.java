package org.jtester.savexp.xstream.converter.enums;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.converter.collections.CollectionConverterHelper;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.enums.EnumMapConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExEnumMapConverter extends EnumMapConverter implements ExConverter {
	protected ExMapper mapper;

	public ExEnumMapConverter(Mapper mapper) {
		super(mapper);
		this.mapper = (ExMapper) mapper;
	}

	public boolean canConvert(IJavaType type) {
		return false;
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {

		IJavaValue keyType = JdtClazzUtil.readField(source, "keyType");
		String attributeName = mapper().aliasForSystemAttribute("enum-type");
		if (attributeName != null) {
			writer.addAttribute(attributeName, JdtClazzUtil.getClazzName(keyType));
		}
		CollectionConverterHelper.marshalMap(mapper, source, writer, context);
	}
}
