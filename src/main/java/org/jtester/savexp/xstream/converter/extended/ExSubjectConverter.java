package org.jtester.savexp.xstream.converter.extended;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.assistor.SetMapClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.converter.collections.CollectionConverterHelper;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.SubjectConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExSubjectConverter extends SubjectConverter implements ExConverter {
	private ExMapper mapper;

	public ExSubjectConverter(Mapper mapper) {
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
		IJavaValue set = JdtClazzUtil.readField(source, "pubCredentials");
		IJavaValue iterator = SetMapClazzUtil.iterator(set);
		marshalPrincipals(iterator, writer, context);

		IJavaPrimitiveValue readOnly = (IJavaPrimitiveValue) JdtClazzUtil.readField(source, "readOnly");
		marshalReadOnly(readOnly.getBooleanValue(), writer);
	}

	protected void marshalPrincipals(IJavaValue iterator, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		writer.startNode("principals");
		while (SetMapClazzUtil.hasNext(iterator)) {
			IJavaValue item = SetMapClazzUtil.next(iterator);
			CollectionConverterHelper.writeItem(mapper, item, context, writer);
		}
		writer.endNode();
	}

	protected void marshalReadOnly(boolean readOnly, HierarchicalStreamWriter writer) {
		writer.startNode("readOnly");
		writer.setValue(String.valueOf(readOnly));
		writer.endNode();
	}
}
