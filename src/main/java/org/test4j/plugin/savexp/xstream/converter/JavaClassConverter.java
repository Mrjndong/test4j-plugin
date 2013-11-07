package org.test4j.plugin.savexp.xstream.converter;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.AbstractJsonConverter;

public class JavaClassConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        String clazname = JdtClazzUtil.getClazzName(input);
        return super.encode(clazname);
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(Class.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
