package org.test4j.plugin.savexp.xstream.converter.collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.PropertiesConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExPropertiesConverter extends PropertiesConverter implements ExConverter {

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
		CollectionConverterHelper.marshalProperty(source, writer, context);

		IJavaValue defaults = JdtClazzUtil.readField(source, "defaults");
		if (!JdtClazzUtil.isNullValue(defaults)) {
			writer.startNode("defaults");
			marshal(defaults, writer, context);
			writer.endNode();
		}
	}
}
