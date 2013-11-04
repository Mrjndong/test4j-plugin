package org.jtester.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.Mapper;

public interface ExMapper extends Mapper {
	// SingleValueConverter getConverterFromItemType(String fieldName, Class<?>
	// fieldType, IJavaValue definedIn);

	ExMapper lookupMapperOfType(IJavaType type);

	SingleValueConverter getConverterFromItemType(String fieldName, IJavaType type, IJavaType definedIn);

	public String serializedClass(IJavaType type) throws DebugException;
}
