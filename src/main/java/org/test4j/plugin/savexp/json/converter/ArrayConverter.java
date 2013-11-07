package org.test4j.plugin.savexp.json.converter;

import java.util.List;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaArray;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.json.AbstractJsonConverter;
import org.test4j.plugin.savexp.json.JSONHelper;

public class ArrayConverter extends AbstractJsonConverter {
    public String convert(IJavaValue input) throws Exception {
        StringBuilder buff = new StringBuilder("[");
        IJavaArray value = (IJavaArray) input;
        boolean isFirst = true;
        for (IJavaValue var : value.getValues()) {
            if (isFirst) {
                isFirst = false;
            } else {
                buff.append(",");
            }
            String item = JSONHelper.toJSON(var);
            buff.append(item);
        }
        return buff.append("]").toString();
    }

    @Override
    public final boolean accept(IJavaValue input, List<String> types) {
        return input instanceof IJavaArray;
    }

    @Override
    protected Set<String> getIdentifyType() {
        return null;
    }
}
