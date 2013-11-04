package org.jtester.savexp.xstream;

import java.io.File;
import java.io.IOException;
import java.io.NotActiveException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import org.jtester.savexp.xstream.converter.ExConverter;
import org.jtester.savexp.xstream.converter.ExNullConverter;
import org.jtester.savexp.xstream.converter.ExReflectionConverter;
import org.jtester.savexp.xstream.converter.ExSelfStreamingInstanceChecker;
import org.jtester.savexp.xstream.converter.base.ExBigDecimalConverter;
import org.jtester.savexp.xstream.converter.base.ExBigIntegerConverter;
import org.jtester.savexp.xstream.converter.base.ExBooleanConverter;
import org.jtester.savexp.xstream.converter.base.ExByteConverter;
import org.jtester.savexp.xstream.converter.base.ExCharConverter;
import org.jtester.savexp.xstream.converter.base.ExDateConverter;
import org.jtester.savexp.xstream.converter.base.ExDoubleConverter;
import org.jtester.savexp.xstream.converter.base.ExFloatConverter;
import org.jtester.savexp.xstream.converter.base.ExIntConverter;
import org.jtester.savexp.xstream.converter.base.ExLongConverter;
import org.jtester.savexp.xstream.converter.base.ExShortConverter;
import org.jtester.savexp.xstream.converter.base.ExSingleValueConverter;
import org.jtester.savexp.xstream.converter.base.ExSingleValueConverterWrapper;
import org.jtester.savexp.xstream.converter.base.ExStringBufferConverter;
import org.jtester.savexp.xstream.converter.base.ExStringBuilderConverter;
import org.jtester.savexp.xstream.converter.base.ExStringConverter;
import org.jtester.savexp.xstream.converter.base.ExURLConverter;
import org.jtester.savexp.xstream.converter.collections.ExArrayConverter;
import org.jtester.savexp.xstream.converter.collections.ExBitSetConverter;
import org.jtester.savexp.xstream.converter.collections.ExCharArrayConverter;
import org.jtester.savexp.xstream.converter.collections.ExCollectionConverter;
import org.jtester.savexp.xstream.converter.collections.ExMapConverter;
import org.jtester.savexp.xstream.converter.collections.ExPropertiesConverter;
import org.jtester.savexp.xstream.converter.collections.ExTreeMapConverter;
import org.jtester.savexp.xstream.converter.collections.ExTreeSetConverter;
import org.jtester.savexp.xstream.converter.enums.ExEnumConverter;
import org.jtester.savexp.xstream.converter.enums.ExEnumMapConverter;
import org.jtester.savexp.xstream.converter.enums.ExEnumSetConverter;
import org.jtester.savexp.xstream.converter.extended.ExCharsetConverter;
import org.jtester.savexp.xstream.converter.extended.ExCurrencyConverter;
import org.jtester.savexp.xstream.converter.extended.ExDurationConverter;
import org.jtester.savexp.xstream.converter.extended.ExDynamicProxyConverter;
import org.jtester.savexp.xstream.converter.extended.ExEncodedByteArrayConverter;
import org.jtester.savexp.xstream.converter.extended.ExFileConverter;
import org.jtester.savexp.xstream.converter.extended.ExGregorianCalendarConverter;
import org.jtester.savexp.xstream.converter.extended.ExJavaClassConverter;
import org.jtester.savexp.xstream.converter.extended.ExJavaMethodConverter;
import org.jtester.savexp.xstream.converter.extended.ExLocaleConverter;
import org.jtester.savexp.xstream.converter.extended.ExRegexPatternConverter;
import org.jtester.savexp.xstream.converter.extended.ExStackTraceElementConverter;
import org.jtester.savexp.xstream.converter.extended.ExSubjectConverter;
import org.jtester.savexp.xstream.converter.extended.ExThrowableConverter;
import org.jtester.savexp.xstream.converter.extended.ExUUIDConverter;
import org.jtester.savexp.xstream.converter.reflection.ExReflectionProvider;
import org.jtester.savexp.xstream.converter.reflection.ExSun14ReflectionProvider;
import org.jtester.savexp.xstream.converter.sql.ExSqlDateConverter;
import org.jtester.savexp.xstream.converter.sql.ExSqlTimeConverter;
import org.jtester.savexp.xstream.converter.sql.ExSqlTimestampConverter;
import org.jtester.savexp.xstream.mapper.ExAnnotationMapper;
import org.jtester.savexp.xstream.mapper.ExArrayMapper;
import org.jtester.savexp.xstream.mapper.ExAttributeAliasingMapper;
import org.jtester.savexp.xstream.mapper.ExAttributeMapper;
import org.jtester.savexp.xstream.mapper.ExCachingMapper;
import org.jtester.savexp.xstream.mapper.ExClassAliasingMapper;
import org.jtester.savexp.xstream.mapper.ExDefaultImplementationsMapper;
import org.jtester.savexp.xstream.mapper.ExDefaultMapper;
import org.jtester.savexp.xstream.mapper.ExDynamicProxyMapper;
import org.jtester.savexp.xstream.mapper.ExEnumMapper;
import org.jtester.savexp.xstream.mapper.ExFieldAliasingMapper;
import org.jtester.savexp.xstream.mapper.ExImmutableTypesMapper;
import org.jtester.savexp.xstream.mapper.ExImplicitCollectionMapper;
import org.jtester.savexp.xstream.mapper.ExLocalConversionMapper;
import org.jtester.savexp.xstream.mapper.ExMapper;
import org.jtester.savexp.xstream.mapper.ExOuterClassMapper;
import org.jtester.savexp.xstream.mapper.ExPackageAliasingMapper;
import org.jtester.savexp.xstream.mapper.ExSystemAttributeAliasingMapper;
import com.thoughtworks.xstream.MarshallingStrategy;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.converters.ConverterRegistry;
import com.thoughtworks.xstream.converters.DataHolder;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.core.JVM;
import com.thoughtworks.xstream.core.MapBackedDataHolder;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshallingStrategy;
import com.thoughtworks.xstream.core.util.ClassLoaderReference;
import com.thoughtworks.xstream.core.util.CompositeClassLoader;
import com.thoughtworks.xstream.core.util.CustomObjectOutputStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.StatefulWriter;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.AnnotationConfiguration;
import com.thoughtworks.xstream.mapper.AttributeAliasingMapper;
import com.thoughtworks.xstream.mapper.AttributeMapper;
import com.thoughtworks.xstream.mapper.ClassAliasingMapper;
import com.thoughtworks.xstream.mapper.DefaultImplementationsMapper;
import com.thoughtworks.xstream.mapper.FieldAliasingMapper;
import com.thoughtworks.xstream.mapper.ImmutableTypesMapper;
import com.thoughtworks.xstream.mapper.ImplicitCollectionMapper;
import com.thoughtworks.xstream.mapper.LocalConversionMapper;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import com.thoughtworks.xstream.mapper.PackageAliasingMapper;
import com.thoughtworks.xstream.mapper.SystemAttributeAliasingMapper;

public class ExXStream {
	private transient JVM jvm = new JVM();
	private ExMapper mapper;
	private ExReflectionProvider reflectionProvider;
	private HierarchicalStreamDriver hierarchicalStreamDriver;
	private ClassLoaderReference classLoaderReference;
	private MarshallingStrategy marshallingStrategy;
	private ConverterLookup converterLookup;
	private ConverterRegistry converterRegistry;

	private PackageAliasingMapper packageAliasingMapper;
	private ClassAliasingMapper classAliasingMapper;
	private FieldAliasingMapper fieldAliasingMapper;
	private AttributeAliasingMapper attributeAliasingMapper;
	private SystemAttributeAliasingMapper systemAttributeAliasingMapper;
	private AttributeMapper attributeMapper;
	private DefaultImplementationsMapper defaultImplementationsMapper;
	private ImmutableTypesMapper immutableTypesMapper;
	private ImplicitCollectionMapper implicitCollectionMapper;
	private LocalConversionMapper localConversionMapper;
	private AnnotationConfiguration annotationConfiguration;

	public static final int NO_REFERENCES = 1001;
	public static final int ID_REFERENCES = 1002;
	public static final int XPATH_RELATIVE_REFERENCES = 1003;
	public static final int XPATH_ABSOLUTE_REFERENCES = 1004;

	public static final int PRIORITY_VERY_HIGH = 10000;
	public static final int PRIORITY_NORMAL = 0;
	public static final int PRIORITY_LOW = -10;
	public static final int PRIORITY_VERY_LOW = -20;

	public ExXStream() {
		this(new DomDriver());
	}

	public ExXStream(HierarchicalStreamDriver hierarchicalStreamDriver) {
		this(new ExSun14ReflectionProvider(), hierarchicalStreamDriver, new ClassLoaderReference(
				new CompositeClassLoader()), (ExMapper) null, new ExDefaultConverterLookup(), null);
	}

	public ExXStream(ExReflectionProvider reflectionProvider, HierarchicalStreamDriver driver, ClassLoader classLoader,
			ExMapper mapper, ConverterLookup converterLookup, ConverterRegistry converterRegistry) {
		jvm = new JVM();
		if (reflectionProvider == null) {
			throw new RuntimeException("should provide a ExReflectionProvider instance");
		}
		this.reflectionProvider = reflectionProvider;
		this.hierarchicalStreamDriver = driver;
		this.classLoaderReference = classLoader instanceof ClassLoaderReference ? (ClassLoaderReference) classLoader
				: new ClassLoaderReference(classLoader);
		this.converterLookup = converterLookup;
		this.converterRegistry = converterRegistry != null ? converterRegistry
				: (converterLookup instanceof ConverterRegistry ? (ConverterRegistry) converterLookup : null);
		this.mapper = mapper == null ? buildMapper() : mapper;

		setupMappers();
		setupAliases();
		setupDefaultImplementations();
		setupConverters();
		setupImmutableTypes();
		setMode(XPATH_RELATIVE_REFERENCES);
	}

	private static final String ANNOTATION_MAPPER_TYPE = ExAnnotationMapper.class.getName();

	private ExMapper buildMapper() {
		ExMapper mapper = new ExDefaultMapper(classLoaderReference);

		mapper = new ExDynamicProxyMapper(mapper);
		mapper = new ExPackageAliasingMapper(mapper);
		mapper = new ExClassAliasingMapper(mapper);
		mapper = new ExFieldAliasingMapper(mapper);
		mapper = new ExAttributeAliasingMapper(mapper);
		mapper = new ExSystemAttributeAliasingMapper(mapper);
		mapper = new ExImplicitCollectionMapper(mapper);
		mapper = new ExOuterClassMapper(mapper);
		mapper = new ExArrayMapper(mapper);
		mapper = new ExDefaultImplementationsMapper(mapper);
		mapper = new ExAttributeMapper(mapper, converterLookup);
		if (JVM.is15()) {
			mapper = new ExEnumMapper(mapper);
		}
		mapper = new ExLocalConversionMapper(mapper);
		mapper = new ExImmutableTypesMapper(mapper);
		if (JVM.is15()) {
			mapper = buildMapperDynamically(ANNOTATION_MAPPER_TYPE, new Class[] { ExMapper.class,
					ConverterRegistry.class, ClassLoader.class, ExReflectionProvider.class, JVM.class }, new Object[] {
					mapper, converterLookup, classLoaderReference, reflectionProvider, jvm });
		}
		mapper = new ExCachingMapper(mapper);
		return mapper;
	}

	private ExMapper buildMapperDynamically(String className, Class<?>[] constructorParamTypes,
			Object[] constructorParamValues) {
		try {
			Class<?> type = Class.forName(className, false, classLoaderReference.getReference());
			Constructor<?> constructor = type.getConstructor(constructorParamTypes);
			return (ExMapper) constructor.newInstance(constructorParamValues);
		} catch (Exception e) {
			throw new RuntimeException("Could not instantiate mapper : " + className, e);
		}
	}

	public ExReflectionProvider getReflectionProvider() {
		return this.reflectionProvider;
	}

	public void setMode(int mode) {
		switch (mode) {
		case NO_REFERENCES:
			setMarshallingStrategy(new TreeMarshallingStrategy());
			break;
		case ID_REFERENCES:
			setMarshallingStrategy(new ReferenceByIdMarshallingStrategy());
			break;
		case XPATH_RELATIVE_REFERENCES:
			setMarshallingStrategy(new ExReferenceByXPathMarshallingStrategy(
					ExReferenceByXPathMarshallingStrategy.RELATIVE));
			break;
		case XPATH_ABSOLUTE_REFERENCES:
			setMarshallingStrategy(new ExReferenceByXPathMarshallingStrategy(
					ExReferenceByXPathMarshallingStrategy.ABSOLUTE));
			break;
		default:
			throw new IllegalArgumentException("Unknown mode : " + mode);
		}
	}

	protected void setupConverters() {
		final ExReflectionConverter reflectionConverter = new ExReflectionConverter(this.mapper, this
				.getReflectionProvider());
		registerConverter(reflectionConverter, PRIORITY_VERY_LOW);

//		registerConverter(new ExSerializableConverter(this.mapper, this.getReflectionProvider()), PRIORITY_LOW);
//		registerConverter(new ExExternalizableConverter(this.mapper), PRIORITY_LOW);

		registerConverter(new ExNullConverter(), PRIORITY_VERY_HIGH);
		registerConverter(new ExIntConverter(), PRIORITY_NORMAL);
		registerConverter(new ExFloatConverter(), PRIORITY_NORMAL);
		registerConverter(new ExDoubleConverter(), PRIORITY_NORMAL);
		registerConverter(new ExLongConverter(), PRIORITY_NORMAL);
		registerConverter(new ExShortConverter(), PRIORITY_NORMAL);
		registerConverter((ExConverter) new ExCharConverter(), PRIORITY_NORMAL);
		registerConverter(new ExBooleanConverter(), PRIORITY_NORMAL);
		registerConverter(new ExByteConverter(), PRIORITY_NORMAL);

		registerConverter(new ExStringConverter(), PRIORITY_NORMAL);
		registerConverter(new ExStringBufferConverter(), PRIORITY_NORMAL);
		registerConverter(new ExDateConverter(), PRIORITY_NORMAL);
		registerConverter(new ExBitSetConverter(), PRIORITY_NORMAL);
		registerConverter(new ExURLConverter(), PRIORITY_NORMAL);
		registerConverter(new ExBigIntegerConverter(), PRIORITY_NORMAL);
		registerConverter(new ExBigDecimalConverter(), PRIORITY_NORMAL);

		registerConverter(new ExArrayConverter(this.mapper), PRIORITY_NORMAL);
		registerConverter(new ExCharArrayConverter(), PRIORITY_NORMAL);
		registerConverter(new ExCollectionConverter(this.mapper), PRIORITY_NORMAL);
		registerConverter(new ExMapConverter(this.mapper), PRIORITY_NORMAL);
		registerConverter(new ExTreeMapConverter(this.mapper), PRIORITY_NORMAL);
		registerConverter(new ExTreeSetConverter(this.mapper), PRIORITY_NORMAL);
		registerConverter(new ExPropertiesConverter(), PRIORITY_NORMAL);
		registerConverter(new ExEncodedByteArrayConverter(), PRIORITY_NORMAL);

		registerConverter(new ExFileConverter(), PRIORITY_NORMAL);
		if (this.jvm.supportsSQL()) {
			registerConverter(new ExSqlTimestampConverter(), PRIORITY_NORMAL);
			registerConverter(new ExSqlTimeConverter(), PRIORITY_NORMAL);
			registerConverter(new ExSqlDateConverter(), PRIORITY_NORMAL);
		}
		registerConverter(new ExDynamicProxyConverter(this.mapper, this.classLoaderReference), PRIORITY_NORMAL);
		registerConverter(new ExJavaClassConverter(this.classLoaderReference), PRIORITY_NORMAL);
		registerConverter(new ExJavaMethodConverter(this.classLoaderReference), PRIORITY_NORMAL);
		if (this.jvm.supportsAWT()) {
			// registerConverter(new FontConverter(), PRIORITY_NORMAL);
			// registerConverter(new ColorConverter(), PRIORITY_NORMAL);
			// registerConverter(new ExTextAttributeConverter(),
			// PRIORITY_NORMAL);
		}
		if (this.jvm.supportsSwing()) {
			// registerConverter(new LookAndFeelConverter(this.mapper,
			// this.reflectionProvider), PRIORITY_NORMAL);
		}
		registerConverter(new ExLocaleConverter(), PRIORITY_NORMAL);
		registerConverter(new ExGregorianCalendarConverter(), PRIORITY_NORMAL);

		if (JVM.is14()) {
			registerConverter(new ExSubjectConverter(this.mapper), PRIORITY_NORMAL);
			registerConverter(new ExThrowableConverter(reflectionConverter), PRIORITY_NORMAL);
			registerConverter(new ExStackTraceElementConverter(), PRIORITY_NORMAL);
			registerConverter(new ExCurrencyConverter(), PRIORITY_NORMAL);
			registerConverter(new ExRegexPatternConverter(reflectionConverter), PRIORITY_NORMAL);
			registerConverter(new ExCharsetConverter(), PRIORITY_NORMAL);
		}

		if (JVM.is15()) {
			dynamicallyRegisterConverter(ExDurationConverter.class, PRIORITY_NORMAL, null, null);
			registerConverter(new ExEnumConverter(), PRIORITY_NORMAL);
			registerConverter(new ExEnumSetConverter(this.mapper), PRIORITY_NORMAL);
			registerConverter(new ExEnumMapConverter(this.mapper), PRIORITY_NORMAL);
			registerConverter(new ExStringBuilderConverter(), PRIORITY_NORMAL);
			registerConverter(new ExUUIDConverter(), PRIORITY_NORMAL);
		}

		registerConverter(new ExSelfStreamingInstanceChecker(reflectionConverter, this), PRIORITY_NORMAL);
	}

	private void dynamicallyRegisterConverter(Class<?> clazz, int priority, Class<?>[] constructorParamTypes,
			Object[] constructorParamValues) {
		try {
			Constructor<?> constructor = clazz.getConstructor(constructorParamTypes);
			Object instance = constructor.newInstance(constructorParamValues);
			if (instance instanceof ExConverter) {
				registerConverter((ExConverter) instance, priority);
			} else if (instance instanceof ExSingleValueConverter) {
				registerConverter((ExSingleValueConverter) instance, priority);
			}
		} catch (Throwable e) {
			throw new RuntimeException("Could not instantiate converter : " + clazz.getName(), e);
		}
	}

	private void setupMappers() {
		packageAliasingMapper = (PackageAliasingMapper) this.mapper.lookupMapperOfType(PackageAliasingMapper.class);
		classAliasingMapper = (ClassAliasingMapper) this.mapper.lookupMapperOfType(ClassAliasingMapper.class);
		fieldAliasingMapper = (FieldAliasingMapper) this.mapper.lookupMapperOfType(FieldAliasingMapper.class);
		attributeMapper = (AttributeMapper) this.mapper.lookupMapperOfType(AttributeMapper.class);
		attributeAliasingMapper = (AttributeAliasingMapper) this.mapper
				.lookupMapperOfType(AttributeAliasingMapper.class);
		systemAttributeAliasingMapper = (SystemAttributeAliasingMapper) this.mapper
				.lookupMapperOfType(SystemAttributeAliasingMapper.class);
		implicitCollectionMapper = (ImplicitCollectionMapper) this.mapper
				.lookupMapperOfType(ImplicitCollectionMapper.class);
		defaultImplementationsMapper = (DefaultImplementationsMapper) this.mapper
				.lookupMapperOfType(DefaultImplementationsMapper.class);
		immutableTypesMapper = (ImmutableTypesMapper) this.mapper.lookupMapperOfType(ImmutableTypesMapper.class);
		localConversionMapper = (LocalConversionMapper) this.mapper.lookupMapperOfType(LocalConversionMapper.class);
		annotationConfiguration = (AnnotationConfiguration) this.mapper
				.lookupMapperOfType(AnnotationConfiguration.class);
	}

	protected void setupAliases() {
		if (classAliasingMapper == null) {
			return;
		}

		alias("null", Mapper.Null.class);
		alias("int", Integer.class);
		alias("float", Float.class);
		alias("double", Double.class);
		alias("long", Long.class);
		alias("short", Short.class);
		alias("char", Character.class);
		alias("byte", Byte.class);
		alias("boolean", Boolean.class);
		alias("number", Number.class);
		alias("object", Object.class);
		alias("big-int", BigInteger.class);
		alias("big-decimal", BigDecimal.class);

		alias("string-buffer", StringBuffer.class);
		alias("string", String.class);
		alias("java-class", Class.class);
		alias("method", Method.class);
		alias("constructor", Constructor.class);
		alias("date", Date.class);
		alias("url", URL.class);
		alias("bit-set", BitSet.class);

		alias("map", Map.class);
		alias("entry", Map.Entry.class);
		alias("properties", Properties.class);
		alias("list", List.class);
		alias("set", Set.class);

		alias("linked-list", LinkedList.class);
		alias("vector", Vector.class);
		alias("tree-map", TreeMap.class);
		alias("tree-set", TreeSet.class);
		alias("hashtable", Hashtable.class);

		if (jvm.supportsAWT()) {
			/**
			 * Instantiating these two classes starts the AWT system, which is
			 * undesirable. Calling loadClass ensures a reference to the class
			 * is found but they are not instantiated.
			 */
			alias("awt-color", jvm.loadClass("java.awt.Color"));
			alias("awt-font", jvm.loadClass("java.awt.Font"));
			alias("awt-text-attribute", jvm.loadClass("java.awt.font.TextAttribute"));
		}

		if (jvm.supportsSQL()) {
			alias("sql-timestamp", jvm.loadClass("java.sql.Timestamp"));
			alias("sql-time", jvm.loadClass("java.sql.Time"));
			alias("sql-date", jvm.loadClass("java.sql.Date"));
		}

		alias("file", File.class);
		alias("locale", Locale.class);
		alias("gregorian-calendar", Calendar.class);

		if (JVM.is14()) {
			alias("auth-subject", jvm.loadClass("javax.security.auth.Subject"));
			alias("linked-hash-map", jvm.loadClass("java.util.LinkedHashMap"));
			alias("linked-hash-set", jvm.loadClass("java.util.LinkedHashSet"));
			alias("trace", jvm.loadClass("java.lang.StackTraceElement"));
			alias("currency", jvm.loadClass("java.util.Currency"));
			aliasType("charset", jvm.loadClass("java.nio.charset.Charset"));
		}

		if (JVM.is15()) {
			alias("duration", jvm.loadClass("javax.xml.datatype.Duration"));
			alias("enum-set", jvm.loadClass("java.util.EnumSet"));
			alias("enum-map", jvm.loadClass("java.util.EnumMap"));
			alias("string-builder", jvm.loadClass("java.lang.StringBuilder"));
			alias("uuid", jvm.loadClass("java.util.UUID"));
		}
	}

	protected void setupDefaultImplementations() {
		if (defaultImplementationsMapper == null) {
			return;
		}
		addDefaultImplementation(HashMap.class, Map.class);
		addDefaultImplementation(ArrayList.class, List.class);
		addDefaultImplementation(HashSet.class, Set.class);
		addDefaultImplementation(GregorianCalendar.class, Calendar.class);
	}

	protected void setupImmutableTypes() {
		if (immutableTypesMapper == null) {
			return;
		}

		// primitives are always immutable
		addImmutableType(boolean.class);
		addImmutableType(Boolean.class);
		addImmutableType(byte.class);
		addImmutableType(Byte.class);
		addImmutableType(char.class);
		addImmutableType(Character.class);
		addImmutableType(double.class);
		addImmutableType(Double.class);
		addImmutableType(float.class);
		addImmutableType(Float.class);
		addImmutableType(int.class);
		addImmutableType(Integer.class);
		addImmutableType(long.class);
		addImmutableType(Long.class);
		addImmutableType(short.class);
		addImmutableType(Short.class);

		// additional types
		addImmutableType(Mapper.Null.class);
		addImmutableType(BigDecimal.class);
		addImmutableType(BigInteger.class);
		addImmutableType(String.class);
		addImmutableType(URL.class);
		addImmutableType(File.class);
		addImmutableType(Class.class);

		if (jvm.supportsAWT()) {
			addImmutableType(jvm.loadClass("java.awt.font.TextAttribute"));
		}

		if (JVM.is14()) {
			// late bound types - allows XStream to be compiled on earlier JDKs
			Class<?> type = jvm.loadClass("com.thoughtworks.xstream.converters.extended.CharsetConverter");
			addImmutableType(type);
		}
	}

	public void setMarshallingStrategy(MarshallingStrategy marshallingStrategy) {
		this.marshallingStrategy = marshallingStrategy;
	}

	/**
	 * Serialize an object to a pretty-printed XML String.
	 * 
	 * @throws XStreamException
	 *             if the object cannot be serialized
	 */
	public String toXML(Object obj) {
		Writer writer = new StringWriter();
		toXML(obj, writer);
		return writer.toString();
	}

	/**
	 * Serialize an object to the given Writer as pretty-printed XML. The Writer
	 * will be flushed afterwards and in case of an exception.
	 * 
	 * @throws XStreamException
	 *             if the object cannot be serialized
	 */
	public void toXML(Object obj, Writer out) {
		HierarchicalStreamWriter writer = hierarchicalStreamDriver.createWriter(out);
		try {
			marshal(obj, writer);
		} finally {
			writer.flush();
		}
	}

	/**
	 * Serialize an object to the given OutputStream as pretty-printed XML. The
	 * OutputStream will be flushed afterwards and in case of an exception.
	 * 
	 * @throws XStreamException
	 *             if the object cannot be serialized
	 */
	public void toXML(Object obj, OutputStream out) {
		HierarchicalStreamWriter writer = hierarchicalStreamDriver.createWriter(out);
		try {
			marshal(obj, writer);
		} finally {
			writer.flush();
		}
	}

	/**
	 * Serialize and object to a hierarchical data structure (such as XML).
	 * 
	 * @throws XStreamException
	 *             if the object cannot be serialized
	 */
	public void marshal(Object obj, HierarchicalStreamWriter writer) {
		marshal(obj, writer, null);
	}

	/**
	 * Serialize and object to a hierarchical data structure (such as XML).
	 * 
	 * @param dataHolder
	 *            Extra data you can use to pass to your converters. Use this as
	 *            you want. If not present, XStream shall create one lazily as
	 *            needed.
	 * @throws XStreamException
	 *             if the object cannot be serialized
	 */
	public void marshal(Object obj, HierarchicalStreamWriter writer, DataHolder dataHolder) {
		marshallingStrategy.marshal(writer, obj, converterLookup, mapper, dataHolder);
	}

	/**
	 * Alias a Class to a shorter name to be used in XML elements.
	 * 
	 * @param name
	 *            Short name
	 * @param type
	 *            Type to be aliased
	 * @throws RuntimeException
	 *             if no {@link ClassAliasingMapper} is available
	 */
	public void alias(String name, Class<?> type) {
		if (classAliasingMapper == null) {
			throw new RuntimeException("No " + ClassAliasingMapper.class.getName() + " available");
		}
		classAliasingMapper.addClassAlias(name, type);
	}

	/**
	 * Alias a type to a shorter name to be used in XML elements. Any class that
	 * is assignable to this type will be aliased to the same name.
	 * 
	 * @param name
	 *            Short name
	 * @param type
	 *            Type to be aliased
	 * @since 1.2
	 * @throws RuntimeException
	 *             if no {@link ClassAliasingMapper} is available
	 */
	public void aliasType(String name, Class<?> type) {
		if (classAliasingMapper == null) {
			throw new RuntimeException("No " + ClassAliasingMapper.class.getName() + " available");
		}
		classAliasingMapper.addTypeAlias(name, type);
	}

	/**
	 * Alias a Class to a shorter name to be used in XML elements.
	 * 
	 * @param name
	 *            Short name
	 * @param type
	 *            Type to be aliased
	 * @param defaultImplementation
	 *            Default implementation of type to use if no other specified.
	 * @throws RuntimeException
	 *             if no {@link DefaultImplementationsMapper} or no
	 *             {@link ClassAliasingMapper} is available
	 */
	public void alias(String name, Class<?> type, Class<?> defaultImplementation) {
		alias(name, type);
		addDefaultImplementation(defaultImplementation, type);
	}

	/**
	 * Alias a package to a shorter name to be used in XML elements.
	 * 
	 * @param name
	 *            Short name
	 * @param pkgName
	 *            package to be aliased
	 * @throws RuntimeException
	 *             if no {@link DefaultImplementationsMapper} or no
	 *             {@link PackageAliasingMapper} is available
	 * @since 1.3.1
	 */
	public void aliasPackage(String name, String pkgName) {
		if (packageAliasingMapper == null) {
			throw new RuntimeException("No " + PackageAliasingMapper.class.getName() + " available");
		}
		packageAliasingMapper.addPackageAlias(name, pkgName);
	}

	/**
	 * Create an alias for a field name.
	 * 
	 * @param alias
	 *            the alias itself
	 * @param definedIn
	 *            the type that declares the field
	 * @param fieldName
	 *            the name of the field
	 * @throws RuntimeException
	 *             if no {@link FieldAliasingMapper} is available
	 */
	public void aliasField(String alias, Class<?> definedIn, String fieldName) {
		if (fieldAliasingMapper == null) {
			throw new RuntimeException("No " + FieldAliasingMapper.class.getName() + " available");
		}
		fieldAliasingMapper.addFieldAlias(alias, definedIn, fieldName);
	}

	/**
	 * Create an alias for an attribute
	 * 
	 * @param alias
	 *            the alias itself
	 * @param attributeName
	 *            the name of the attribute
	 * @throws RuntimeException
	 *             if no {@link AttributeAliasingMapper} is available
	 */
	public void aliasAttribute(String alias, String attributeName) {
		if (attributeAliasingMapper == null) {
			throw new RuntimeException("No " + AttributeAliasingMapper.class.getName() + " available");
		}
		attributeAliasingMapper.addAliasFor(attributeName, alias);
	}

	/**
	 * Create an alias for a system attribute.
	 * 
	 * XStream will not write a system attribute if its alias is set to
	 * <code>null</code>. However, this is not reversible, i.e. deserialization
	 * of the result is likely to fail afterwards and will not produce an object
	 * equal to the originally written one.
	 * 
	 * @param alias
	 *            the alias itself (may be <code>null</code>)
	 * @param systemAttributeName
	 *            the name of the system attribute
	 * @throws RuntimeException
	 *             if no {@link SystemAttributeAliasingMapper} is available
	 * @since 1.3.1
	 */
	public void aliasSystemAttribute(String alias, String systemAttributeName) {
		if (systemAttributeAliasingMapper == null) {
			throw new RuntimeException("No " + SystemAttributeAliasingMapper.class.getName() + " available");
		}
		systemAttributeAliasingMapper.addAliasFor(systemAttributeName, alias);
	}

	/**
	 * Create an alias for an attribute.
	 * 
	 * @param definedIn
	 *            the type where the attribute is defined
	 * @param attributeName
	 *            the name of the attribute
	 * @param alias
	 *            the alias itself
	 * @throws RuntimeException
	 *             if no {@link AttributeAliasingMapper} is available
	 * @since 1.2.2
	 */
	public void aliasAttribute(Class<?> definedIn, String attributeName, String alias) {
		aliasField(alias, definedIn, attributeName);
		useAttributeFor(definedIn, attributeName);
	}

	/**
	 * Use an attribute for a field or a specific type.
	 * 
	 * @param fieldName
	 *            the name of the field
	 * @param type
	 *            the Class of the type to be rendered as XML attribute
	 * @throws RuntimeException
	 *             if no {@link AttributeMapper} is available
	 * @since 1.2
	 */
	public void useAttributeFor(String fieldName, Class<?> type) {
		if (attributeMapper == null) {
			throw new RuntimeException("No " + AttributeMapper.class.getName() + " available");
		}
		attributeMapper.addAttributeFor(fieldName, type);
	}

	/**
	 * Use an attribute for a field declared in a specific type.
	 * 
	 * @param fieldName
	 *            the name of the field
	 * @param definedIn
	 *            the Class containing such field
	 * @throws RuntimeException
	 *             if no {@link AttributeMapper} is available
	 * @since 1.2.2
	 */
	public void useAttributeFor(Class<?> definedIn, String fieldName) {
		if (attributeMapper == null) {
			throw new RuntimeException("No " + AttributeMapper.class.getName() + " available");
		}
		attributeMapper.addAttributeFor(definedIn, fieldName);
	}

	/**
	 * Use an attribute for an arbitrary type.
	 * 
	 * @param type
	 *            the Class of the type to be rendered as XML attribute
	 * @throws RuntimeException
	 *             if no {@link AttributeMapper} is available
	 * @since 1.2
	 */
	public void useAttributeFor(Class<?> type) {
		if (attributeMapper == null) {
			throw new RuntimeException("No " + AttributeMapper.class.getName() + " available");
		}
		attributeMapper.addAttributeFor(type);
	}

	/**
	 * Associate a default implementation of a class with an object. Whenever
	 * XStream encounters an instance of this type, it will use the default
	 * implementation instead. For example, java.util.ArrayList is the default
	 * implementation of java.util.List.
	 * 
	 * @param defaultImplementation
	 * @param ofType
	 * @throws RuntimeException
	 *             if no {@link DefaultImplementationsMapper} is available
	 */
	public void addDefaultImplementation(Class<?> defaultImplementation, Class<?> ofType) {
		if (defaultImplementationsMapper == null) {
			throw new RuntimeException("No " + DefaultImplementationsMapper.class.getName() + " available");
		}
		defaultImplementationsMapper.addDefaultImplementation(defaultImplementation, ofType);
	}

	/**
	 * Add immutable types. The value of the instances of these types will
	 * always be written into the stream even if they appear multiple times.
	 * 
	 * @throws RuntimeException
	 *             if no {@link ImmutableTypesMapper} is available
	 */
	public void addImmutableType(Class<?> type) {
		if (immutableTypesMapper == null) {
			throw new RuntimeException("No " + ImmutableTypesMapper.class.getName() + " available");
		}
		immutableTypesMapper.addImmutableType(type);
	}

	public void registerConverter(ExConverter converter) {
		registerConverter(converter, PRIORITY_NORMAL);
	}

	public void registerConverter(ExConverter converter, int priority) {
		if (converterRegistry != null) {
			converterRegistry.registerConverter(converter, priority);
		}
	}

	public void registerConverter(ExSingleValueConverter converter) {
		registerConverter(converter, PRIORITY_NORMAL);
	}

	public void registerConverter(ExSingleValueConverter converter, int priority) {
		if (converterRegistry != null) {
			converterRegistry.registerConverter(new ExSingleValueConverterWrapper(converter), priority);
		}
	}

	/**
	 * Register a local {@link Converter} for a field.
	 * 
	 * @param definedIn
	 *            the class type the field is defined in
	 * @param fieldName
	 *            the field name
	 * @param converter
	 *            the converter to use
	 * @since 1.3
	 */
	public void registerLocalConverter(Class<?> definedIn, String fieldName, Converter converter) {
		if (localConversionMapper == null) {
			throw new RuntimeException("No " + LocalConversionMapper.class.getName() + " available");
		}
		localConversionMapper.registerLocalConverter(definedIn, fieldName, converter);
	}

	/**
	 * Register a local {@link SingleValueConverter} for a field.
	 * 
	 * @param definedIn
	 *            the class type the field is defined in
	 * @param fieldName
	 *            the field name
	 * @param converter
	 *            the converter to use
	 * @since 1.3
	 */
	public void registerLocalConverter(Class<?> definedIn, String fieldName, SingleValueConverter converter) {
		registerLocalConverter(definedIn, fieldName, (Converter) new ExSingleValueConverterWrapper(converter));
	}

	/**
	 * Retrieve the {@link Mapper}. This is by default a chain of
	 * {@link MapperWrapper MapperWrappers}.
	 * 
	 * @return the mapper
	 * @since 1.2
	 */
	public Mapper getMapper() {
		return mapper;
	}

	public ConverterLookup getConverterLookup() {
		return converterLookup;
	}

	/**
	 * Adds a default implicit collection which is used for any unmapped XML
	 * tag.
	 * 
	 * @param ownerType
	 *            class owning the implicit collection
	 * @param fieldName
	 *            name of the field in the ownerType. This field must be a
	 *            concrete collection type or matching the default
	 *            implementation type of the collection type.
	 */
	public void addImplicitCollection(Class<?> ownerType, String fieldName) {
		if (implicitCollectionMapper == null) {
			throw new RuntimeException("No " + ImplicitCollectionMapper.class.getName() + " available");
		}
		implicitCollectionMapper.add(ownerType, fieldName, null, null);
	}

	/**
	 * Adds implicit collection which is used for all items of the given
	 * itemType.
	 * 
	 * @param ownerType
	 *            class owning the implicit collection
	 * @param fieldName
	 *            name of the field in the ownerType. This field must be a
	 *            concrete collection type or matching the default
	 *            implementation type of the collection type.
	 * @param itemType
	 *            type of the items to be part of this collection.
	 * @throws RuntimeException
	 *             if no {@link ImplicitCollectionMapper} is available
	 */
	public void addImplicitCollection(Class<?> ownerType, String fieldName, Class<?> itemType) {
		if (implicitCollectionMapper == null) {
			throw new RuntimeException("No " + ImplicitCollectionMapper.class.getName() + " available");
		}
		implicitCollectionMapper.add(ownerType, fieldName, null, itemType);
	}

	/**
	 * Adds implicit collection which is used for all items of the given element
	 * name defined by itemFieldName.
	 * 
	 * @param ownerType
	 *            class owning the implicit collection
	 * @param fieldName
	 *            name of the field in the ownerType. This field must be a
	 *            concrete collection type or matching the default
	 *            implementation type of the collection type.
	 * @param itemFieldName
	 *            element name of the implicit collection
	 * @param itemType
	 *            item type to be aliases be the itemFieldName
	 * @throws RuntimeException
	 *             if no {@link ImplicitCollectionMapper} is available
	 */
	public void addImplicitCollection(Class<?> ownerType, String fieldName, String itemFieldName, Class<?> itemType) {
		if (implicitCollectionMapper == null) {
			throw new RuntimeException("No " + ImplicitCollectionMapper.class.getName() + " available");
		}
		implicitCollectionMapper.add(ownerType, fieldName, itemFieldName, itemType);
	}

	/**
	 * Create a DataHolder that can be used to pass data to the converters. The
	 * DataHolder is provided with a call to
	 * {@link #marshal(Object, HierarchicalStreamWriter, DataHolder)} or
	 * {@link #unmarshal(HierarchicalStreamReader, Object, DataHolder)}.
	 * 
	 * @return a new {@link DataHolder}
	 */
	public DataHolder newDataHolder() {
		return new MapBackedDataHolder();
	}

	/**
	 * Creates an ObjectOutputStream that serializes a stream of objects to the
	 * writer using XStream.
	 * <p>
	 * To change the name of the root element (from &lt;object-stream&gt;), use
	 * {@link #createObjectOutputStream(java.io.Writer, String)}.
	 * </p>
	 * 
	 * @see #createObjectOutputStream(com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      String)
	 * @see #createObjectInputStream(com.thoughtworks.xstream.io.HierarchicalStreamReader)
	 * @since 1.0.3
	 */
	public ObjectOutputStream createObjectOutputStream(Writer writer) throws IOException {
		return createObjectOutputStream(hierarchicalStreamDriver.createWriter(writer), "object-stream");
	}

	/**
	 * Creates an ObjectOutputStream that serializes a stream of objects to the
	 * writer using XStream.
	 * <p>
	 * To change the name of the root element (from &lt;object-stream&gt;), use
	 * {@link #createObjectOutputStream(java.io.Writer, String)}.
	 * </p>
	 * 
	 * @see #createObjectOutputStream(com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      String)
	 * @see #createObjectInputStream(com.thoughtworks.xstream.io.HierarchicalStreamReader)
	 * @since 1.0.3
	 */
	public ObjectOutputStream createObjectOutputStream(HierarchicalStreamWriter writer) throws IOException {
		return createObjectOutputStream(writer, "object-stream");
	}

	/**
	 * Creates an ObjectOutputStream that serializes a stream of objects to the
	 * writer using XStream.
	 * 
	 * @see #createObjectOutputStream(com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      String)
	 * @see #createObjectInputStream(com.thoughtworks.xstream.io.HierarchicalStreamReader)
	 * @since 1.0.3
	 */
	public ObjectOutputStream createObjectOutputStream(Writer writer, String rootNodeName) throws IOException {
		return createObjectOutputStream(hierarchicalStreamDriver.createWriter(writer), rootNodeName);
	}

	/**
	 * Creates an ObjectOutputStream that serializes a stream of objects to the
	 * OutputStream using XStream.
	 * <p>
	 * To change the name of the root element (from &lt;object-stream&gt;), use
	 * {@link #createObjectOutputStream(java.io.Writer, String)}.
	 * </p>
	 * 
	 * @see #createObjectOutputStream(com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      String)
	 * @see #createObjectInputStream(com.thoughtworks.xstream.io.HierarchicalStreamReader)
	 * @since 1.3
	 */
	public ObjectOutputStream createObjectOutputStream(OutputStream out) throws IOException {
		return createObjectOutputStream(hierarchicalStreamDriver.createWriter(out), "object-stream");
	}

	/**
	 * Creates an ObjectOutputStream that serializes a stream of objects to the
	 * OutputStream using XStream.
	 * 
	 * @see #createObjectOutputStream(com.thoughtworks.xstream.io.HierarchicalStreamWriter,
	 *      String)
	 * @see #createObjectInputStream(com.thoughtworks.xstream.io.HierarchicalStreamReader)
	 * @since 1.3
	 */
	public ObjectOutputStream createObjectOutputStream(OutputStream out, String rootNodeName) throws IOException {
		return createObjectOutputStream(hierarchicalStreamDriver.createWriter(out), rootNodeName);
	}

	/**
	 * Creates an ObjectOutputStream that serializes a stream of objects to the
	 * writer using XStream.
	 * <p>
	 * Because an ObjectOutputStream can contain multiple items and XML only
	 * allows a single root node, the stream must be written inside an enclosing
	 * node.
	 * </p>
	 * <p>
	 * It is necessary to call ObjectOutputStream.close() when done, otherwise
	 * the stream will be incomplete.
	 * </p>
	 * <h3>Example</h3>
	 * 
	 * <pre>
	 *  ObjectOutputStream out = xstream.createObjectOutputStream(aWriter, &quot;things&quot;);
	 *   out.writeInt(123);
	 *   out.writeObject(&quot;Hello&quot;);
	 *   out.writeObject(someObject)
	 *   out.close();
	 * </pre>
	 * 
	 * @param writer
	 *            The writer to serialize the objects to.
	 * @param rootNodeName
	 *            The name of the root node enclosing the stream of objects.
	 * @see #createObjectInputStream(com.thoughtworks.xstream.io.HierarchicalStreamReader)
	 * @since 1.0.3
	 */
	public ObjectOutputStream createObjectOutputStream(final HierarchicalStreamWriter writer, String rootNodeName)
			throws IOException {
		final StatefulWriter statefulWriter = new StatefulWriter(writer);
		statefulWriter.startNode(rootNodeName, null);
		return new CustomObjectOutputStream(new CustomObjectOutputStream.StreamCallback() {
			public void writeToStream(Object object) {
				marshal(object, statefulWriter);
			}

			@SuppressWarnings("rawtypes")
			public void writeFieldsToStream(Map fields) throws NotActiveException {
				throw new NotActiveException("not in call to writeObject");
			}

			public void defaultWriteObject() throws NotActiveException {
				throw new NotActiveException("not in call to writeObject");
			}

			public void flush() {
				statefulWriter.flush();
			}

			public void close() {
				if (statefulWriter.state() != StatefulWriter.STATE_CLOSED) {
					statefulWriter.endNode();
					statefulWriter.close();
				}
			}
		});
	}

	/**
	 * Change the ClassLoader XStream uses to load classes.
	 * 
	 * Creating an XStream instance it will register for all kind of classes and
	 * types of the current JDK, but not for any 3rd party type. To ensure that
	 * all other types are loaded with your classloader, you should call this
	 * method as early as possible - or consider to provide the classloader
	 * directly in the constructor.
	 * 
	 * @since 1.1.1
	 */
	public void setClassLoader(ClassLoader classLoader) {
		classLoaderReference.setReference(classLoader);
	}

	/**
	 * Retrieve the ClassLoader XStream uses to load classes.
	 * 
	 * @since 1.1.1
	 */
	public ClassLoader getClassLoader() {
		return classLoaderReference.getReference();
	}

	/**
	 * Prevents a field from being serialized. To omit a field you must always
	 * provide the declaring type and not necessarily the type that is
	 * converted.
	 * 
	 * @since 1.1.3
	 * @throws RuntimeException
	 *             if no {@link FieldAliasingMapper} is available
	 */
	public void omitField(Class<?> definedIn, String fieldName) {
		if (fieldAliasingMapper == null) {
			throw new RuntimeException("No " + FieldAliasingMapper.class.getName() + " available");
		}
		fieldAliasingMapper.omitField(definedIn, fieldName);
	}

	/**
	 * Process the annotations of the given types and configure the XStream.
	 * 
	 * @param types
	 *            the types with XStream annotations
	 * @since 1.3
	 */
	public void processAnnotations(final Class<?>[] types) {
		if (annotationConfiguration == null) {
			throw new RuntimeException("No " + ANNOTATION_MAPPER_TYPE + " available");
		}
		annotationConfiguration.processAnnotations(types);
	}

	/**
	 * Process the annotations of the given type and configure the XStream. A
	 * call of this method will automatically turn the auto-detection mode for
	 * annotations off.
	 * 
	 * @param type
	 *            the type with XStream annotations
	 * @since 1.3
	 */
	public void processAnnotations(final Class<?> type) {
		processAnnotations(new Class[] { type });
	}

	/**
	 * Set the auto-detection mode of the AnnotationMapper. Note that
	 * auto-detection implies that the XStream is configured while it is
	 * processing the XML steams. This is a potential concurrency problem. Also
	 * is it technically not possible to detect all class aliases at
	 * deserialization. You have been warned!
	 * 
	 * @param mode
	 *            <code>true</code> if annotations are auto-detected
	 * @since 1.3
	 */
	public void autodetectAnnotations(boolean mode) {
		if (annotationConfiguration != null) {
			annotationConfiguration.autodetectAnnotations(mode);
		}
	}

	private Object readResolve() {
		jvm = new JVM();
		return this;
	}

}
