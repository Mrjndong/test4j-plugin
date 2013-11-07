package org.test4j.plugin.savexp.xstream.converter;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.AbstractJsonConverter;

public class CharsetConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        String value = JdtClazzUtil.callNoneParaReturnStringMethod(input, "name");
        return super.encode(value);
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(Charset.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
