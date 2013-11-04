package org.test4j.plugin.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.SingleValueConverterWrapper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExSingleValueConverterWrapper extends SingleValueConverterWrapper implements ExConverter {

	public ExSingleValueConverterWrapper(SingleValueConverter wrapped) {
		super(wrapped);
	}

	public boolean canConvert(IJavaType type) {
		return false;
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context) {
		writer.setValue(toString(source));
	}

}
