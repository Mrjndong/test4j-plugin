package org.test4j.plugin.savexp.json.converter;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.json.AbstractJsonConverter;

public class EnumConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        IJavaValue item = JdtClazzUtil.readField(input, "name");
        String value = JdtClazzUtil.getValueString(item);
        return value;
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(Enum.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
