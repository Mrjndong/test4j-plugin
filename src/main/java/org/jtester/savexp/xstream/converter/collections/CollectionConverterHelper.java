package org.jtester.savexp.xstream.converter.collections;

import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.assistor.SetMapClazzUtil;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class CollectionConverterHelper {
	public static void writeItem(ExMapper mapper, IJavaValue item, MarshallingContext context,
			HierarchicalStreamWriter writer) throws DebugException {
		if (item.getValueString().equals("null")) {
			String name = mapper.serializedClass((Class<?>) null);
			writer.startNode(name);
			writer.endNode();
		} else {
			Class<?> clazz = JdtClazzUtil.getClazz(item);
			String name = mapper.serializedClass(item.getJavaType());
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, name, clazz);
			context.convertAnother(item);
			writer.endNode();
		}
	}

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
			CollectionConverterHelper.writeItem(mapper, item, context, writer);
		}
	}

	public static void marshalMap(ExMapper mapper, IJavaValue map, HierarchicalStreamWriter writer,
			MarshallingContext context) throws DebugException {
		IJavaValue entries = SetMapClazzUtil.entrySet(map);
		IJavaValue iterator = SetMapClazzUtil.iterator(entries);
		while (SetMapClazzUtil.hasNext(iterator)) {
			IJavaValue entry = SetMapClazzUtil.next(iterator);
			IJavaValue key = SetMapClazzUtil.getEntryKey(entry);
			IJavaValue value = SetMapClazzUtil.getEntryValue(entry);
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, mapper.serializedClass(Map.Entry.class),
					Map.Entry.class);
			CollectionConverterHelper.writeItem(mapper, key, context, writer);
			CollectionConverterHelper.writeItem(mapper, value, context, writer);
			writer.endNode();
		}
	}

	public static void marshalProperty(IJavaValue property, HierarchicalStreamWriter writer, MarshallingContext context)
			throws DebugException {
		IJavaValue entries = SetMapClazzUtil.entrySet(property);
		IJavaValue iterator = SetMapClazzUtil.iterator(entries);
		while (SetMapClazzUtil.hasNext(iterator)) {
			IJavaValue entry = SetMapClazzUtil.next(iterator);
			IJavaValue key = SetMapClazzUtil.getEntryKey(entry);
			IJavaValue value = SetMapClazzUtil.getEntryValue(entry);
			writer.startNode("property");
			writer.addAttribute("name", JdtClazzUtil.getValueString(key));
			writer.addAttribute("value", JdtClazzUtil.getValueString(value));
			writer.endNode();
		}
	}
}
