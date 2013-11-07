package org.test4j.plugin.savexp.xstream.converter;

import java.util.List;
import java.util.Set;

public abstract class AbstractJsonConverter implements JsonConverter {

    public boolean accept(List<String> types) {
        Set<String> _types = this.getIdentifyType();
        for (String type : types) {
            if (type != null && _types.contains(type)) {
                return true;
            }
        }
        return false;
    }

    protected abstract Set<String> getIdentifyType();
}
