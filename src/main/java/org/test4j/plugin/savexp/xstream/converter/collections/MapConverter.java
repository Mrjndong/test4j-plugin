package org.test4j.plugin.savexp.xstream.converter.collections;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.SetMapClazzUtil;
import org.test4j.plugin.savexp.xstream.JSONHelper;
import org.test4j.plugin.savexp.xstream.converter.AbstractJsonConverter;

public class MapConverter extends AbstractJsonConverter {

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
            buff.append(keyStr).append(":").append(valueStr);
        }
        return buff.append("}").toString();
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(Map.class.getName());
        types.add(AbstractMap.class.getName());
        types.add(HashMap.class.getName());
        types.add(Hashtable.class.getName());
        types.add(Properties.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
