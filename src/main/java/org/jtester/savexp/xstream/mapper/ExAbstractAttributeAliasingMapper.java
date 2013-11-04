package org.jtester.savexp.xstream.mapper;

import org.eclipse.jdt.debug.core.IJavaType;

import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.AbstractAttributeAliasingMapper;

public abstract class ExAbstractAttributeAliasingMapper extends AbstractAttributeAliasingMapper implements ExMapper {
	private AbstractAttributeAliasingMapper abstractAttributeAliasingMapper;
	private final ExMapper wrapped;

	public ExAbstractAttributeAliasingMapper(ExMapper wrapped) {
		super(wrapped);
		this.abstractAttributeAliasingMapper = this.getAbstractAttributeAliasingMapper();
		this.wrapped = wrapped;
	}

	protected abstract AbstractAttributeAliasingMapper getAbstractAttributeAliasingMapper();

	public void addAliasFor(final String attributeName, final String alias) {
		this.abstractAttributeAliasingMapper.addAliasFor(attributeName, alias);
	}

	public SingleValueConverter getConverterFromItemType(String fieldName, IJavaType fieldType, IJavaType definedIn) {
		return wrapped.getConverterFromItemType(fieldName, fieldType, definedIn);
	}
}
