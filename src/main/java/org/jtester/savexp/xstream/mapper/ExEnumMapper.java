package org.jtester.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.jtester.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.EnumMapper;

public class ExEnumMapper extends EnumMapper implements ExMapper {
	private final ExMapper wrapped;

	@SuppressWarnings("deprecation")
	public ExEnumMapper(ExMapper wrapped) {
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
		if (JdtClazzUtil.canFindClazz(type)) {
			Class<?> claz = JdtClazzUtil.getClazz(type);
			return super.serializedClass(claz);
		} else {
			return this.wrapped.serializedClass(type);
		}
	}
}
