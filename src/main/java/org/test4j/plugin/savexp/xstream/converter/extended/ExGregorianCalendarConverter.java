package org.test4j.plugin.savexp.xstream.converter.extended;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.ExConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.extended.GregorianCalendarConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExGregorianCalendarConverter extends GregorianCalendarConverter implements ExConverter {

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

	public final static String GET_TIMEINMILLIS_SYGNATURE = "()J";

	public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		IJavaPrimitiveValue time = (IJavaPrimitiveValue) JdtClazzUtil.invokeNoParaMethod(source, "getTimeInMillis",
				GET_TIMEINMILLIS_SYGNATURE);

		ExtendedHierarchicalStreamWriterHelper.startNode(writer, "time", long.class);
		writer.setValue(String.valueOf(time.getLongValue()));
		writer.endNode();

		IJavaValue zone = JdtClazzUtil.invokeNoParaMethod(source, "getZone", "()Ljava/util/TimeZone;");
		IJavaValue zoneID = JdtClazzUtil.invokeNoParaMethod(zone, "getID", "()Ljava/lang/String;");
		ExtendedHierarchicalStreamWriterHelper.startNode(writer, "timezone", String.class);
		writer.setValue(JdtClazzUtil.getValueString(zoneID));
		writer.endNode();
	}

}
