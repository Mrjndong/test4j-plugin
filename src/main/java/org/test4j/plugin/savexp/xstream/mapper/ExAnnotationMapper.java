package org.test4j.plugin.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.test4j.plugin.savexp.xstream.converter.reflection.ExReflectionProvider;

import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.mapper.AnnotationMapper;

public class ExAnnotationMapper extends AnnotationMapper implements ExMapper {
	private final ExMapper wrapped;

	public ExAnnotationMapper(final ExMapper wrapped, final ConverterRegistry converterRegistry,
			final ClassLoader classLoader, final ExReflectionProvider reflectionProvider, final JVM jvm) {
		super(wrapped, converterRegistry, classLoader, reflectionProvider, jvm);
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
		return wrapped.serializedClass(type);// TODO
	}
}
