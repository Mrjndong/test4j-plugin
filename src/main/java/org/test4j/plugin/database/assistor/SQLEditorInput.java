package org.test4j.plugin.database.assistor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

@SuppressWarnings("rawtypes")
public class SQLEditorInput implements IStorageEditorInput {
	String name = "SQL Editor";
	IStorage storage;

	public SQLEditorInput() {
		this.storage = new IStorage() {
			public InputStream getContents() throws CoreException {
				return new ByteArrayInputStream("".getBytes());
			}

			public IPath getFullPath() {
				return null;
			}

			public String getName() {
				return name;
			}

			public boolean isReadOnly() {
				return false;
			}

			public Object getAdapter(Class adapter) {
				return null;
			}
		};
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		return true;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/**
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return "Execute SQL";
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class adapter) {
		return null;
	}

	/**
	 * @see org.eclipse.ui.IStorageEditorInput#getStorage()
	 */
	public IStorage getStorage() throws CoreException {
		return this.storage;
	}
}
