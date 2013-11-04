package org.test4j.plugin.savexp.xstream.converter;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public interface ExConverter extends Converter {
	void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context) throws DebugException;

	public boolean canConvert(IJavaType type);
}
