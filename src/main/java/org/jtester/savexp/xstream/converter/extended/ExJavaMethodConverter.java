package org.jtester.savexp.xstream.converter.extended;

import java.lang.reflect.Method;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.JavaMethodConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExJavaMethodConverter extends JavaMethodConverter implements ExConverter {
	private ExJavaClassConverter javaClassConverter;

	public ExJavaMethodConverter(ClassLoader classLoader) {
		super(classLoader);
		this.javaClassConverter = (ExJavaClassConverter) new ExJavaClassConverter(classLoader);
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

	public final static String GET_PARAMETERTYPE_SYGNATURE = "()[Ljava/lang/Class;";

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		IJavaValue clazzValue = JdtClazzUtil.readField(source, "clazz");
		String declaringClassName = this.javaClassConverter.toString(clazzValue);
		String methodname = null;
		if (source.getJavaType().getName().equals(Method.class.getName())) {
			methodname = JdtClazzUtil.readField(source, "name").getValueString();
		}

		IJavaValue paras = JdtClazzUtil.invokeNoParaMethod(source, "getParameterTypes", GET_PARAMETERTYPE_SYGNATURE);
		marshal(writer, declaringClassName, methodname, paras.getVariables());
	}

	private void marshal(HierarchicalStreamWriter writer, String declaringClassName, String methodName,
			IVariable[] parameterTypes) throws DebugException {

		writer.startNode("class");
		writer.setValue(declaringClassName);
		writer.endNode();

		if (methodName != null) { // it's a method and not a ctor
			writer.startNode("name");
			writer.setValue(methodName);
			writer.endNode();
		}

		writer.startNode("parameter-types");
		for (IVariable para : parameterTypes) {
			writer.startNode("class");
			IJavaValue type = (IJavaValue) para.getValue();
			writer.setValue(JdtClazzUtil.getClazzName(type));
			writer.endNode();
		}
		writer.endNode();
	}
}
