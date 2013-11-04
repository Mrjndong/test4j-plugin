package org.test4j.plugin.savexp.xstream.converter.collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.assistor.SetMapClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;
import org.test4j.plugin.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.collections.TreeSetConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExTreeSetConverter extends TreeSetConverter implements ExConverter {
	private ExMapper mapper;

	public ExTreeSetConverter(Mapper mapper) {
		super(mapper);
		this.mapper = (ExMapper) mapper;
	}

	public boolean canConvert(IJavaType type) {
		return false;
	}

	@Override
	public void marshal(Object original, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		if (JdtClazzUtil.isIJavaValue(original)) {
			try {
				this.marshal((IJavaValue) original, writer, context);
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			super.marshal(original, writer, context);
		}
	}

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		IJavaValue comparator = SetMapClazzUtil.comparator(source);
		CollectionConverterHelper.writeTreeComparator(comparator, mapper, writer, context);
		CollectionConverterHelper.marshalSet(mapper, source, writer, context);
	}
}
