package org.jtester.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;

public class ExSystemAttributeAliasingMapper extends SystemAttributeAliasingMapper implements ExMapper {
	private final ExMapper wrapped;

	public ExSystemAttributeAliasingMapper(ExMapper wrapped) {
		super(wrapped);
		this.wrapped = wrapped;
	}

	// public SingleValueConverter getConverterFromItemType(String fieldName,
	// Class<?> fieldType, IJavaType definedIn) {
	// return wrapped.getConverterFromItemType(fieldName, fieldType, definedIn);
	// }

	public ExMapper lookupMapperOfType(IJavaType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public SingleValueConverter getConverterFromItemType(String fieldName, IJavaType fieldType, IJavaType definedIn) {
		return wrapped.getConverterFromItemType(fieldName, fieldType, definedIn);
	}

	public String serializedClass(IJavaType type) throws DebugException {
		return this.wrapped.serializedClass(type);
	}
}
