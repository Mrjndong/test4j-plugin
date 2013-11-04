package org.jtester.savexp.xstream.converter.base;

import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;

import com.thoughtworks.xstream.converters.SingleValueConverter;

public interface ExSingleValueConverter extends SingleValueConverter {
	public String toString(IJavaValue obj);

	public boolean canConvert(IJavaType type);
}
