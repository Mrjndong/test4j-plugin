package org.test4j.plugin.savexp.xstream.converter.reflection;

import java.util.Map;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaType;

public class ExImmutableFieldKeySorter implements ExFieldKeySorter {

	public Map<ExFieldKey, IVariable> sort(IJavaType type, Map<ExFieldKey, IVariable> keyedByFieldKey) {
		return keyedByFieldKey;
	}

}