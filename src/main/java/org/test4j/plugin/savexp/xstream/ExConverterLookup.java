package org.test4j.plugin.savexp.xstream;

import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.Converter;

public interface ExConverterLookup {
	Converter lookupConverterForType(IJavaType type);
}
