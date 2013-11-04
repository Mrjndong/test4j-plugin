package org.test4j.plugin.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.DefaultMapper;

public class ExDefaultMapper extends DefaultMapper implements ExMapper {

	public ExDefaultMapper(ClassLoader classLoader) {
		super(classLoader);
	}

	// public SingleValueConverter getConverterFromItemType(String fieldName,
	// Class<?> fieldType, IJavaValue definedIn) {
	// return null;
	// }

	public ExMapper lookupMapperOfType(IJavaType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public SingleValueConverter getConverterFromItemType(String fieldName, IJavaType fieldType, IJavaType definedIn) {
		return null;
	}

	public String serializedClass(IJavaType type) throws DebugException {
		return type.getName();
	}
}
