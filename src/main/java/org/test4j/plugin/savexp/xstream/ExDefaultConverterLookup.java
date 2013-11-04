package org.test4j.plugin.savexp.xstream;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jdt.debug.core.IJavaType;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;
import org.test4j.plugin.savexp.xstream.converter.ExReflectionConverter;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.core.util.PrioritizedList;

public class ExDefaultConverterLookup implements ConverterLookup, ConverterRegistry, ExConverterLookup {
	private transient Map<Class<?>, Converter> typeToConverterMap = Collections
			.synchronizedMap(new HashMap<Class<?>, Converter>());
	private final PrioritizedList converters = new PrioritizedList();

	public ExDefaultConverterLookup() {
	}

	public Converter lookupConverterForType(IJavaType type) {
		Converter converter = (Converter) lookupConverterForType(JdtClazzUtil.getClazz(type));
		if (converter != null && !(converter instanceof ExReflectionConverter)) {
			return converter;
		}
		Iterator<?> iterator = converters.iterator();
		while (iterator.hasNext()) {
			ExConverter exConverter = (ExConverter) iterator.next();
			if (exConverter.canConvert(type)) {
				return exConverter;
			}
		}
		if (converter == null) {
			throw new ConversionException("No converter specified for " + type);
		} else {
			return converter;
		}
	}

	@SuppressWarnings("rawtypes")
	public Converter lookupConverterForType(Class type) {
		Converter cachedConverter = (Converter) typeToConverterMap.get(type);
		if (cachedConverter != null) {
			return cachedConverter;
		}
		Iterator<?> iterator = converters.iterator();
		while (iterator.hasNext()) {
			Converter converter = (Converter) iterator.next();
			if (converter.canConvert(type)) {
				typeToConverterMap.put(type, converter);
				return converter;
			}
		}
		throw new ConversionException("No converter specified for " + type);
	}

	public void registerConverter(Converter converter, int priority) {
		converters.add(converter, priority);
		for (Iterator<?> iter = this.typeToConverterMap.keySet().iterator(); iter.hasNext();) {
			Class<?> type = (Class<?>) iter.next();
			if (converter.canConvert(type)) {
				iter.remove();
			}
		}
	}
}
