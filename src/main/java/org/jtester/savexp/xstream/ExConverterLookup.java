package org.jtester.savexp.xstream;

import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.Converter;

public interface ExConverterLookup {
	Converter lookupConverterForType(IJavaType type);
}
