package org.test4j.plugin.savexp.xstream;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaInterfaceType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.xstream.converter.JsonConverter;
import org.test4j.plugin.savexp.xstream.converter.base.CharsetConverter;
import org.test4j.plugin.savexp.xstream.converter.base.DateConverter;
import org.test4j.plugin.savexp.xstream.converter.base.NumberConverter;
import org.test4j.plugin.savexp.xstream.converter.base.StringConverter;
import org.test4j.plugin.savexp.xstream.converter.collections.MapConverter;

public class JSONHelper {
    final static List<JsonConverter> converts = new ArrayList<JsonConverter>();

    static {
        converts.add(new StringConverter());
        converts.add(new CharsetConverter());
        converts.add(new NumberConverter());
        converts.add(new DateConverter());
        converts.add(new MapConverter());
    }

    public static JsonConverter getJsonConverter(IJavaValue obj) throws Exception {
        List<String> types = getJavaTypes(obj);
        for (JsonConverter converter : converts) {
            if (converter.accept(types)) {
                return converter;
            }
        }
        return null;
    }

    private JSONHelper() {
    }

    public static String toJSON(IJavaValue obj) throws Exception {
        JsonConverter converter = getJsonConverter(obj);
        if (converter == null) {
            return "unkown converter for type:" + obj.getJavaType().getName();
        } else {
            String json = converter.convert(obj);
            return json;
        }
    }

    private static List<String> getJavaTypes(IJavaValue obj) throws DebugException, Exception {
        List<String> typeNames = new ArrayList<String>();
        IJavaClassType type = (IJavaClassType) obj.getJavaType();
        while (type != null) {
            String name = type.getName();
            typeNames.add(name);
            IJavaInterfaceType[] interfaces = type.getInterfaces();
            for (IJavaInterfaceType _interface : interfaces) {
                addSuperInterfaces(_interface, typeNames);
            }
            type = type.getSuperclass();
        }
        return typeNames;
    }

    private static void addSuperInterfaces(IJavaInterfaceType interfaceType, List<String> typeNames) throws Exception {
        typeNames.add(interfaceType.getName());
        IJavaInterfaceType[] interfaces = interfaceType.getSuperInterfaces();
        for (IJavaInterfaceType _interface : interfaces) {
            addSuperInterfaces(_interface, typeNames);
        }
    }

    public static void registerConverter(JsonConverter converter) {
        converts.add(converter);
    }
}
