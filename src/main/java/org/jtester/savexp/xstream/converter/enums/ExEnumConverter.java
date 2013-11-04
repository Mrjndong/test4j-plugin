package org.jtester.savexp.xstream.converter.enums;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.enums.EnumConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExEnumConverter extends EnumConverter implements ExConverter {

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isEnum(type);
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
		IJavaValue value = JdtClazzUtil.readField(source, "name");
		writer.setValue(JdtClazzUtil.getValueString(value));
	}

}
