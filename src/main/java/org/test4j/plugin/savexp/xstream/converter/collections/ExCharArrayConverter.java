package org.test4j.plugin.savexp.xstream.converter.collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.CharArrayConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExCharArrayConverter extends CharArrayConverter implements ExConverter {

	public boolean canConvert(IJavaType type) {
		if (type instanceof IJavaArrayType) {
			try {
				IJavaType componentType = ((IJavaArrayType) type).getComponentType();
				Class<?> claz = JdtClazzUtil.getClazz(componentType);
				return claz.equals(char.class);
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			return false;
		}
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
		StringBuffer buffer = new StringBuffer();
		for (IVariable var : source.getVariables()) {
			String value = var.getValue().getValueString();
			buffer.append(value);
		}
		writer.setValue(buffer.toString());
	}
}
