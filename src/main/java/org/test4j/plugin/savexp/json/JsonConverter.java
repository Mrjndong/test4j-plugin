package org.test4j.plugin.savexp.json;

import java.util.List;

import org.eclipse.jdt.debug.core.IJavaValue;

public interface JsonConverter {
    String convert(IJavaValue input) throws Exception;

    boolean accept(IJavaValue input, List<String> types);
}
