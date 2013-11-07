package org.test4j.plugin.savexp.xstream.converter.collections;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.test4j.plugin.savexp.assistor.JdtClazzUtil;
import org.test4j.plugin.savexp.assistor.SetMapClazzUtil;
import org.test4j.plugin.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CollectionConverterHelper {

    public static void writeTreeComparator(IValue comparator, ExMapper mapper, HierarchicalStreamWriter writer,
                                           MarshallingContext context) {
        if (JdtClazzUtil.isNullValue(comparator)) {
            writer.startNode("no-comparator");
            writer.endNode();
        } else {
            writer.startNode("comparator");
            writer.addAttribute("class", mapper.serializedClass(comparator.getClass()));
            context.convertAnother(comparator);
            writer.endNode();
        }
    }

    public static void marshalSet(ExMapper mapper, IJavaValue set, HierarchicalStreamWriter writer,
                                  MarshallingContext context) throws DebugException {
        IJavaValue iterator = SetMapClazzUtil.iterator(set);

        while (SetMapClazzUtil.hasNext(iterator)) {
            IJavaValue item = SetMapClazzUtil.next(iterator);
            // CollectionConverterHelper.writeItem(mapper, item, context, writer);
        }
    }
}
