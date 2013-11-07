package org.test4j.plugin.savexp.json.converter;

import java.util.List;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.json.AbstractJsonConverter;

public class JavaPrimitiveConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        return JdtClazzUtil.getValueString(input);
    }

    @Override
    public boolean accept(IJavaValue input, List<String> types) {
        return input instanceof IJavaPrimitiveValue;
    }

    @Override
    protected Set<String> getIdentifyType() {
        return null;
    }
}
