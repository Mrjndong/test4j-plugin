package org.jtester.savexp.xstream.converter.extended;

import java.lang.reflect.Proxy;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaInterfaceType;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.DynamicProxyConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExDynamicProxyConverter extends DynamicProxyConverter implements ExConverter {
	private ExMapper mapper;

	public ExDynamicProxyConverter(Mapper mapper, ClassLoader classLoader) {
		super(mapper, classLoader);
		this.mapper = (ExMapper) mapper;
	}

	public boolean canConvert(IJavaType type) {
		return JdtClazzUtil.isAssignableFrom(type, Proxy.class);
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
		addInterfacesToXml(source, writer);
		IJavaValue invocationHandler = (IJavaValue) source.getVariables()[12].getValue();
		writer.startNode("handler");
		String attributeName = mapper.aliasForSystemAttribute("class");
		if (attributeName != null) {
			writer.addAttribute(attributeName, mapper.serializedClass(invocationHandler.getJavaType()));
		}
		context.convertAnother(invocationHandler);
		writer.endNode();
	}

	private void addInterfacesToXml(IJavaValue source, HierarchicalStreamWriter writer) throws DebugException {
		IJavaInterfaceType[] interfaces = ((IJavaClassType) source.getJavaType()).getInterfaces();
		for (IJavaInterfaceType it : interfaces) {
			writer.startNode("interface");
			writer.setValue(mapper.serializedClass(it));
			writer.endNode();
		}
	}
}
