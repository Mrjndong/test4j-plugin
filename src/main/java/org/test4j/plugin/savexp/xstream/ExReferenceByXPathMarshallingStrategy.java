package org.test4j.plugin.savexp.xstream;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExReferenceByXPathMarshallingStrategy extends ReferenceByXPathMarshallingStrategy {
	private int mode;

	public ExReferenceByXPathMarshallingStrategy(int mode) {
		super(mode);
		this.mode = mode;
	}

	@Override
	protected TreeMarshaller createMarshallingContext(HierarchicalStreamWriter writer, ConverterLookup converterLookup,
			Mapper mapper) {
		return new ExReferenceByXPathMarshaller(writer, (ExDefaultConverterLookup) converterLookup, mapper, mode);
	}

}
