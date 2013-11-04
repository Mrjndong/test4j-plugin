package org.jtester.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.jtester.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;

public class ExClassAliasingMapper extends ClassAliasingMapper implements ExMapper {
	private final ExMapper wrapped;

	public ExClassAliasingMapper(ExMapper wrapped) {
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
		Class<?> claz = JdtClazzUtil.getClazz(type);
		if (claz.getName().startsWith("org.eclipse.jdt")) {
			return this.wrapped.serializedClass(type);
		} else {
			return super.serializedClass(claz);
		}
	}
}
