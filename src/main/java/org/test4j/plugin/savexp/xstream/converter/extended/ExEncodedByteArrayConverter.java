package org.test4j.plugin.savexp.xstream.converter.extended;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.EncodedByteArrayConverter;
import com.thoughtworks.xstream.core.util.Base64Encoder;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExEncodedByteArrayConverter extends EncodedByteArrayConverter implements ExConverter {
	private static final Base64Encoder base64 = new Base64Encoder();

	public boolean canConvert(IJavaType type) {
		if (type instanceof IJavaArrayType) {
			try {
				IJavaType componentType = ((IJavaArrayType) type).getComponentType();
				Class<?> claz = JdtClazzUtil.getClazz(componentType);
				return claz.equals(byte.class);
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
		IVariable[] vars = source.getVariables();
		byte[] bytes = new byte[vars.length];
		int index = 0;
		for (IVariable var : vars) {
			String value = JdtClazzUtil.getValueString(var.getValue());
			byte b = Byte.valueOf(value);
			bytes[index++] = b;
		}
		writer.setValue(base64.encode(bytes));
	}
}
