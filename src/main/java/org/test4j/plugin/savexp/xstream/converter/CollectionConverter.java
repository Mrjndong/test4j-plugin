package org.test4j.plugin.savexp.xstream.converter;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.SetMapClazzUtil;
import org.test4j.plugin.savexp.xstream.AbstractJsonConverter;
import org.test4j.plugin.savexp.xstream.JSONHelper;

public class CollectionConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        StringBuffer buff = new StringBuffer("[");
        IJavaValue iterator = SetMapClazzUtil.iterator(input);
        boolean isFirst = true;
        while (SetMapClazzUtil.hasNext(iterator)) {
            if (isFirst) {
                isFirst = false;
            } else {
                buff.append(",");
            }
            IJavaValue item = SetMapClazzUtil.next(iterator);
            String value = JSONHelper.toJSON(item);
            buff.append(value);
        }
        return buff.append("]").toString();
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(Collection.class.getName());
        types.add(Set.class.getName());
        types.add(List.class.getName());
        types.add(ArrayList.class.getName());
        types.add(HashSet.class.getName());
        types.add(AbstractCollection.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
