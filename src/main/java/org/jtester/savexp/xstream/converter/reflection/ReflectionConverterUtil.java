package org.jtester.savexp.xstream.converter.reflection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.debug.core.IJavaFieldVariable;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.jtester.savexp.assistor.JdtClazzUtil;
import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.mapper.ExMapper;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.ExtendedHierarchicalStreamWriterHelper;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
@SuppressWarnings("rawtypes")
public class ReflectionConverterUtil {
	public static void marshal(final ExReflectionProvider reflectionProvider, final ExMapper mapper,
			final IJavaValue source, final HierarchicalStreamWriter writer, final MarshallingContext context) {
		final Set<String> seenFields = new HashSet<String>();
		final Map<String, IJavaVariable> defaultFieldDefinition = new HashMap<String, IJavaVariable>();

		// Attributes might be preferred to child elements ...
		reflectionProvider.visitSerializableFields(source, new ExSun14ReflectionProvider.ExVisitor() {
			
			public void visit(String fieldName, Class fieldType, Class definedIn, Object value) {
			}

			public void visit(String fieldName, IJavaType fieldType, IJavaType definedIn, IJavaVariable value)
					throws DebugException {
				if (!defaultFieldDefinition.containsKey(fieldName)) {
					defaultFieldDefinition.put(fieldName, value);
				}

				SingleValueConverter converter = mapper.getConverterFromItemType(fieldName, fieldType, definedIn);
				if (converter != null) {
					if (value != null) {
						if (seenFields.contains(fieldName)) {
							throw new ConversionException("Cannot write field with name '" + fieldName
									+ "' twice as attribute for object of type " + source.getClass().getName());
						}
						final String str = converter.toString(value);
						if (str != null) {
							writer.addAttribute(mapper.aliasForAttribute(mapper.serializedMember(JdtClazzUtil
									.getClazz(definedIn), fieldName)), str);
						}
					}
					seenFields.add(fieldName);
				}
			}
		});

		// Child elements not covered already processed as attributes ...
		reflectionProvider.visitSerializableFields(source, new ExSun14ReflectionProvider.ExVisitor() {
			public void visit(String fieldName, Class fieldType, Class definedIn, Object newObj) {

			}

			public void visit(String fieldName, IJavaType fieldType, IJavaType definedIn, IJavaVariable newObj)
					throws DebugException {
				if (!seenFields.contains(fieldName) && !(newObj.getValue().getReferenceTypeName().equals("null"))) {
					Mapper.ImplicitCollectionMapping mapping = mapper.getImplicitCollectionDefForFieldName(JdtClazzUtil
							.getClazz(source), fieldName);
					if (mapping != null) {
						if (mapping.getItemFieldName() != null) {
							for (IVariable var : newObj.getValue().getVariables()) {
								if (JdtClazzUtil.isStaticOrTransient(var)) {
									continue;
								}
								String aliasname = var == null ? mapper.serializedClass((IJavaType) null) : mapping
										.getItemFieldName();
								writeField(fieldName, aliasname, source.getJavaType(), definedIn, (IJavaValue) newObj
										.getValue());
							}
						} else {
							context.convertAnother(newObj);
						}
					} else {
						writeField(fieldName, null, fieldType, definedIn, (IJavaValue) newObj.getValue());
					}
				}
			}

			private void writeField(String fieldName, String aliasName, IJavaType fieldType, IJavaType definedIn,
					IJavaValue newObj) throws DebugException {
				String serializeName = mapper.serializedMember(source.getClass(), fieldName);
				ExtendedHierarchicalStreamWriterHelper.startNode(writer, aliasName != null ? aliasName : serializeName,
						JdtClazzUtil.getClazz(fieldType));
				if (newObj == null) {
					writer.endNode();
					return;
				}

				if (!JdtClazzUtil.typeEquals(newObj.getJavaType(), fieldType)) {
					String serializedClassName = mapper.serializedClass(newObj.getJavaType());
					Class<?> defaultType = mapper.defaultImplementationOf(JdtClazzUtil.getClazz(fieldType));
					if (!serializedClassName.equals(mapper.serializedClass(defaultType))) {
						String attributeName = mapper.aliasForSystemAttribute("class");
						if (attributeName != null) {
							writer.addAttribute(attributeName, serializedClassName);
						}
					}
				}

				final IJavaFieldVariable defaultField = (IJavaFieldVariable) defaultFieldDefinition.get(fieldName);
				if (defaultField.getDeclaringType().getName() != definedIn.getName()) {
					String attributeName = mapper.aliasForSystemAttribute("defined-in");
					if (attributeName != null) {
						writer.addAttribute(attributeName, mapper.serializedClass(JdtClazzUtil.getClazz(definedIn)));
					}
				}
				marshallField(mapper, context, fieldName, (IJavaValue) newObj);
				writer.endNode();
			}
		});
	}

	protected static void marshallField(final ExMapper mapper, final MarshallingContext context, String fieldname,
			IJavaValue value) {
		try {
			Class<?> clazz = JdtClazzUtil.getClazz(value.getJavaType());
			ExConverter converter = (ExConverter) mapper.getLocalConverter(clazz, fieldname);
			context.convertAnother(value, converter);
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}
}
