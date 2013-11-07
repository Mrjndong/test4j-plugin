package org.test4j.plugin.savexp.xstream.converter.base;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.xstream.converter.AbstractJsonConverter;

public class DateConverter extends AbstractJsonConverter {
    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public String convert(IJavaValue input) throws Exception {
        IVariable fastTime = input.getVariables()[7];
        IJavaPrimitiveValue value = (IJavaPrimitiveValue) fastTime.getValue();
        Date date = new Date();
        date.setTime(value.getLongValue());
        String dateStr = df.format(date);
        return "\"" + dateStr + "\"";
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(Date.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
