package org.test4j.plugin.savexp.xstream.mapper;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaArrayType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.ArrayMapper;

public class ExArrayMapper extends ArrayMapper implements ExMapper {
	private final ExMapper wrapped;

	public ExArrayMapper(ExMapper wrapped) {
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
		StringBuffer arraySuffix = new StringBuffer();
		String name = null;
		while (type instanceof IJavaArrayType) {
			name = wrapped.serializedClass(type);
			if (type.getName().equals(name)) {
				type = ((IJavaArrayType) type).getComponentType();
				arraySuffix.append("-array");
				name = null;
			} else {
				break;
			}
		}
		if (name == null) {
			name = boxedTypeName(JdtClazzUtil.getClazz(type));
		}
		if (name == null) {
			name = wrapped.serializedClass(type);
		}
		if (arraySuffix.length() > 0) {
			return name + arraySuffix;
		} else {
			return name;
		}
	}

	@SuppressWarnings("rawtypes")
	private final static Collection BOXED_TYPES = Arrays.asList(new Class[] { Boolean.class, Byte.class,
			Character.class, Short.class, Integer.class, Long.class, Float.class, Double.class });

	private String boxedTypeName(Class<?> type) {
		return BOXED_TYPES.contains(type) ? type.getName() : null;
	}
}
