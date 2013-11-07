package org.test4j.plugin.savexp.xstream.converter;

import java.util.List;

import org.eclipse.jdt.debug.core.IJavaValue;

public interface JsonConverter {
    String convert(IJavaValue input) throws Exception;

    boolean accept(List<String> types);
}
