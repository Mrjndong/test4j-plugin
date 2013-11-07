package org.test4j.plugin.savexp.json.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.json.AbstractJsonConverter;

public class NumberConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        return JdtClazzUtil.getValueString(input);
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(boolean.class.getName());
        types.add(Boolean.class.getName());

        types.add(byte.class.getName());
        types.add(Byte.class.getName());

        types.add(int.class.getName());
        types.add(Integer.class.getName());

        types.add(long.class.getName());
        types.add(Long.class.getName());

        types.add(double.class.getName());
        types.add(Double.class.getName());

        types.add(float.class.getName());
        types.add(Float.class.getName());

        types.add(short.class.getName());
        types.add(Short.class.getName());

        types.add(BigDecimal.class.getName());
        types.add(BigInteger.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
