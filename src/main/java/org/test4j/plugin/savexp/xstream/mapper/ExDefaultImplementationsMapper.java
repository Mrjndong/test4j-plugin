package org.test4j.plugin.savexp.xstream.mapper;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.DefaultImplementationsMapper;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExDefaultImplementationsMapper extends DefaultImplementationsMapper implements ExMapper {
	private final ExMapper wrapped;

	public ExDefaultImplementationsMapper(ExMapper wrapped) {
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
		Class<?> clazz = defaultImplType.get(JdtClazzUtil.getClazz(type));
		if (clazz == null) {
			return this.wrapped.serializedClass(type);
		} else {
			return super.serializedClass(clazz);
		}
	}

	private Map<Class<?>, Class<?>> defaultImplType = new HashMap<Class<?>, Class<?>>() {
		private static final long serialVersionUID = -8289080699073030150L;

		{
			this.put(null, Mapper.Null.class);
			this.put(Boolean.class, boolean.class);
			this.put(Character.class, char.class);
			this.put(Integer.class, int.class);
			this.put(Float.class, float.class);
			this.put(Double.class, double.class);
			this.put(Short.class, short.class);
			this.put(Byte.class, byte.class);
			this.put(Long.class, long.class);
		}
	};
}
