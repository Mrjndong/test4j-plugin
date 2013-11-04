package org.jtester.savexp.xstream.converter.extended;

import java.awt.font.TextAttribute;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.base.ExSingleValueConverter;

import com.thoughtworks.xstream.converters.extended.TextAttributeConverter;

public class ExTextAttributeConverter extends TextAttributeConverter implements ExSingleValueConverter {

	public boolean canConvert(IJavaType type) {
		try {
			if (type.getName().equals(TextAttribute.class.getName())) {
				return true;
			} else {
				return false;
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString(Object item) {
		if (JdtClazzUtil.isIJavaValue(item)) {
			return this.toString((IJavaValue) item);
		} else {
			return super.toString(item);
		}
	}

	public String toString(IJavaValue obj) {
		// if
		// (obj.getJavaType().getName().equals(AttributedCharacterIterator.Attribute.class.getName()))
		// {
		// ObjectReference ref = ((JDIObjectValue) obj).getUnderlyingObject();
		// // TODO
		//
		// }
		return "";
	}

	// private static final Method getName;
	// static {
	// try {
	// getName =
	// AttributedCharacterIterator.Attribute.class.getDeclaredMethod("getName",
	// (Class[]) null);
	// } catch (NoSuchMethodException e) {
	// throw new
	// ExceptionInInitializerError("Missing AttributedCharacterIterator.Attribute.getName()");
	// }
	// }
}
