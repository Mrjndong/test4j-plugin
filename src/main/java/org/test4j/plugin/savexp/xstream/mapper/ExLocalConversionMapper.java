package org.test4j.plugin.savexp.xstream.mapper;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.LocalConversionMapper;

public class ExLocalConversionMapper extends LocalConversionMapper implements ExMapper {
	private final ExMapper wrapped;

	public ExLocalConversionMapper(ExMapper wrapped) {
		super(wrapped);
		this.wrapped = wrapped;
	}

	// PUBLIC SINGLEVALUECONVERTER GETCONVERTERFROMITEMTYPE(STRING FIELDNAME,
	// CLASS<?> FIELDTYPE, IJAVAVALUE DEFINEDIN) {
	// RETURN WRAPPED.GETCONVERTERFROMITEMTYPE(FIELDNAME, FIELDTYPE, DEFINEDIN);
	// }

	public ExMapper lookupMapperOfType(IJavaType type) {
		// TODO Auto-generated method stub
		return null;
	}

	public SingleValueConverter getConverterFromItemType(String fieldName, IJavaType fieldType, IJavaType definedIn) {
		return wrapped.getConverterFromItemType(fieldName, fieldType, definedIn);
	}

	public String serializedClass(IJavaType type) throws DebugException {
		return wrapped.serializedClass(type);
	}
}
