package org.jtester.savexp.xstream.converter.reflection;

import java.util.Map;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaType;

public interface ExFieldKeySorter {
	Map<ExFieldKey, IVariable> sort(IJavaType type, Map<ExFieldKey, IVariable> keyedByFieldKey);
}
