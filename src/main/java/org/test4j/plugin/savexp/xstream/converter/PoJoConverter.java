package org.test4j.plugin.savexp.xstream.converter;

import java.util.List;
import java.util.Set;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.xstream.AbstractJsonConverter;
import org.test4j.plugin.savexp.xstream.JSONHelper;

public class PoJoConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        StringBuilder buff = new StringBuilder("{");
        IVariable[] variables = input.getVariables();
        boolean isFirst = true;
        for (IVariable var : variables) {
            if (isFirst) {
                isFirst = false;
            } else {
                buff.append(",");
            }
            String name = var.getName();
            buff.append(super.encode(name));
            buff.append(":");
            IJavaValue item = (IJavaValue) var.getValue();
            String value = JSONHelper.toJSON(item);
            buff.append(value);
        }
        return buff.append("}").toString();
    }

    @Override
    public final boolean accept(IJavaValue input, List<String> types) {
        return true;
    }

    @Override
    protected Set<String> getIdentifyType() {
        return null;
    }
}
