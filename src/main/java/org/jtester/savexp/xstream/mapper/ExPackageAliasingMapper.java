package org.jtester.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.PackageAliasingMapper;

public class ExPackageAliasingMapper extends PackageAliasingMapper implements ExMapper {
	private static final long serialVersionUID = 3143708598442768974L;
	private final ExMapper wrapped;

	public ExPackageAliasingMapper(ExMapper wrapped) {
		super(wrapped);
		this.wrapped = wrapped;
	}

	// public SingleValueConverter getConverterFromItemType(String fieldName,
	// Class<?> fieldType, IJavaValue definedIn) {
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
