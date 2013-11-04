package org.jtester.savexp.xstream;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.utils.ReflectUtil;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.core.AbstractReferenceMarshaller;
import com.thoughtworks.xstream.core.ReferenceByXPathMarshaller;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.path.Path;
import com.thoughtworks.xstream.io.path.PathTracker;
import com.thoughtworks.xstream.mapper.Mapper;

public class ExReferenceByXPathMarshaller extends ReferenceByXPathMarshaller {
	private ExObjectIdDictionary references = new ExObjectIdDictionary();

	private ExObjectIdDictionary implicitElements = new ExObjectIdDictionary();

	private ExConverterLookup converterLookup;

	public ExReferenceByXPathMarshaller(HierarchicalStreamWriter writer, ExDefaultConverterLookup converterLookup,
			Mapper mapper, int mode) {
		super(writer, converterLookup, mapper, mode);
		this.converterLookup = converterLookup;
	}

	@Override
	public void start(Object item, DataHolder dataHolder) {
		if (JdtClazzUtil.isIJavaValue(item)) {
			this.startRef((IJavaValue) item, dataHolder);
		} else {
			super.start(item, dataHolder);
		}

	}

	private void startRef(IJavaValue item, DataHolder dataHolder) {
		ReflectUtil.setFieldValue(this, TreeMarshaller.class, "dataHolder", dataHolder);

		try {
			String itemClazzName = item.getJavaType().getName();
			ExtendedHierarchicalStreamWriterHelper.startNode(writer, itemClazzName, JdtClazzUtil.getClazz(item));
			convertAnother(item);

			writer.endNode();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void convertAnother(Object item, Converter converter) {
		if (JdtClazzUtil.isIJavaValue(item)) {
			this.convertAnother((IJavaValue) item, converter);
		} else {
			super.convert(item, converter);
		}
	}

	private void convertAnother(IJavaValue item, Converter converter) {
		if (converter == null) {
			try {
				converter = converterLookup.lookupConverterForType(item.getJavaType());
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			if (!converter.canConvert(JdtClazzUtil.getClazz(item))) {
				ConversionException e = new ConversionException("Explicit selected converter cannot handle item");
				e.add("item-type", item.getClass().getName());
				e.add("converter-type", converter.getClass().getName());
				throw e;
			}
		}
		convert(item, converter);
	}

	@Override
	public void convert(Object item, Converter converter) {
		if (JdtClazzUtil.isIJavaValue(item)) {
			try {
				this.convert((IJavaValue) item, (ExConverter) converter);
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		} else {
			super.convert(item, converter);
		}
	}

	private void convert(IJavaValue item, ExConverter converter) throws DebugException {
		if (getMapper().isImmutableValueType(JdtClazzUtil.getClazz(item))) {
			// strings, ints, dates, etc... don't bother using references.
			converter.marshal(item, writer, this);
		} else {
			Path currentPath = this.getPathTracker().getPath();
			Object existingReferenceKey = this.references.lookupId(item);
			if (existingReferenceKey != null) {
				String attributeName = getMapper().aliasForSystemAttribute("reference");
				if (attributeName != null) {
					writer.addAttribute(attributeName, createReference(currentPath, existingReferenceKey));
				}
			} else if (this.implicitElements.lookupId(item) != null) {
				throw new ReferencedImplicitElementException(item, currentPath);
			} else {
				if (this.getLastPath() == null || !currentPath.isAncestor(this.getLastPath())) {
					this.setLastPath(currentPath);
					this.references.associateId(item, currentPath);
				} else {
					this.implicitElements.associateId(item, currentPath);
				}
				converter.marshal(item, writer, this);
			}
		}
	}

	private PathTracker getPathTracker() {
		return (PathTracker) ReflectUtil.getFieldValue(AbstractReferenceMarshaller.class, this, "pathTracker");
	}

	private Path getLastPath() {
		return (Path) ReflectUtil.getFieldValue(AbstractReferenceMarshaller.class, this, "lastPath");
	}

	private void setLastPath(Path path) {
		ReflectUtil.setFieldValue(this, AbstractReferenceMarshaller.class, "lastPath", path);
	}
}
