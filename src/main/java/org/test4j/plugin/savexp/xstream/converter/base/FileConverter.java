package org.test4j.plugin.savexp.xstream.converter.base;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.xstream.converter.AbstractJsonConverter;

public class FileConverter extends AbstractJsonConverter {

    public String convert(IJavaValue input) throws Exception {
        String path = JdtClazzUtil.callNoneParaReturnStringMethod(input, "getPath");
        return super.encode(path);
    }

    static Set<String> types = new HashSet<String>();
    static {
        types.add(File.class.getName());
    }

    @Override
    protected Set<String> getIdentifyType() {
        return types;
    }
}
