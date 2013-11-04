package org.jtester.savexp.xstream.converter.enums;

import java.util.EnumSet;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.assistor.SetMapClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.enums.EnumSetConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExEnumSetConverter extends EnumSetConverter implements ExConverter {
	private ExMapper mapper;

	public ExEnumSetConverter(Mapper mapper) {
		super(mapper);
		this.mapper = (ExMapper) mapper;
	}

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isAssignableFrom(type, EnumSet.class);
	}

	@Override
	public void marshal(Object original, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(original)) {
			try {
				this.marshal((IJavaValue) original, writer, context);
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			super.marshal(original, writer, context);
		}
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		IJavaValue typeValue = JdtClazzUtil.readField(source, "elementType");
		String type = JdtClazzUtil.getClazzName(typeValue);
		String attributeName = mapper.aliasForSystemAttribute("enum-type");
		if (attributeName != null && type != null) {
			writer.addAttribute(attributeName, type);
		}
		String enums = joinEnumValues(source);
		writer.setValue(enums);
	}

	private String joinEnumValues(IJavaValue source) throws DebugException {
		boolean seenFirst = false;
		StringBuffer result = new StringBuffer();
		IJavaValue iterator = SetMapClazzUtil.iterator(source);
		while (SetMapClazzUtil.hasNext(iterator)) {
			if (seenFirst) {
				result.append(',');
			} else {
				seenFirst = true;
			}
			IJavaValue enumValue = SetMapClazzUtil.next(iterator);
			String value = JdtClazzUtil.callNoneParaReturnStringMethod(enumValue, "name");
			result.append(value);
		}
		return result.toString();
	}
}
