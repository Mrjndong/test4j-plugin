package org.jtester.savexp.xstream.converter.collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.BitSetConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExBitSetConverter extends BitSetConverter implements ExConverter {

	public boolean canConvert(IJavaType type) {
		return false;
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
		IVariable[] bitUnits = source.getVariables()[6].getValue().getVariables();

		IJavaPrimitiveValue bits = (IJavaPrimitiveValue) bitUnits[0].getValue();
		StringBuffer buffer = new StringBuffer();
		long value = bits.getLongValue();
		boolean seenFirst = false;
		int index = 0;
		while (value != 0) {
			if (value % 2 == 1) {
				if (seenFirst) {
					buffer.append(",");
				} else {
					seenFirst = true;
				}
				buffer.append(index);
			}
			index++;
			value = value / 2;
		}
		writer.setValue(buffer.toString());
	}
}
