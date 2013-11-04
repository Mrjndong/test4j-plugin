package org.test4j.plugin.savexp.xstream.converter.extended;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.RegexPatternConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExRegexPatternConverter extends RegexPatternConverter implements ExConverter {
	private ExConverter defaultConverter;

	public ExRegexPatternConverter(ExConverter defaultConverter) {
		super(defaultConverter);
		this.defaultConverter = defaultConverter;
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
		defaultConverter.marshal(source, writer, context);
	}
}
