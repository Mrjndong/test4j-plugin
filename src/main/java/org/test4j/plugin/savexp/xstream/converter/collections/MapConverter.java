package org.test4j.plugin.savexp.xstream.converter.collections;

import java.util.List;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.SetMapClazzUtil;
import org.test4j.plugin.savexp.xstream.JSONHelper;
import org.test4j.plugin.savexp.xstream.converter.JsonConverter;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MapConverter implements JsonConverter {

    public void marshal(IJavaValue source, HierarchicalStreamWriter writer, MarshallingContext context)
            throws DebugException {
        CollectionConverterHelper.marshalMap(null, source, writer, context);
    }

    public String convert(IJavaValue input) throws Exception {
        StringBuilder buff = new StringBuilder("{");
        IJavaValue entries = SetMapClazzUtil.entrySet(input);
        IJavaValue iterator = SetMapClazzUtil.iterator(entries);
        boolean isFirst = true;
        while (SetMapClazzUtil.hasNext(iterator)) {
            IJavaValue entry = SetMapClazzUtil.next(iterator);
            IJavaValue key = SetMapClazzUtil.getEntryKey(entry);
            IJavaValue value = SetMapClazzUtil.getEntryValue(entry);
            String keyStr = JSONHelper.toJSON(key);
            String valueStr = JSONHelper.toJSON(value);
            if (isFirst) {
                isFirst = false;
            } else {
                buff.append(",");
            }
            buff.append("\"").append(keyStr).append("\"").append(":").append("\"").append(valueStr).append("\"");
        }
        return buff.append("}").toString();
    }

    public boolean accept(List<String> types) {
        for (String type : types) {
            if (Map.class.getName().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
