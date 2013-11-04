package org.test4j.plugin.savexp.xstream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaValue;

public class ExObjectIdDictionary {
	private final Map<Wrapper, Object> map = new HashMap<Wrapper, Object>();
	private volatile int counter;

	private static interface Wrapper {
		int hashCode();

		boolean equals(Object obj);

		String toString();

		Object get();

		long getObjId();

		String getObjClassName();
	}

	private static class IdWrapper implements Wrapper {

		protected final String clazzname;
		protected final long objId;
		private final int hashCode;
		private IJavaValue value;

		public IdWrapper(IJavaValue value) {
			try {
				String toString = value.toString();
				hashCode = toString.hashCode();
				this.clazzname = value.getJavaType().getName();
				this.objId = value instanceof IJavaObject ? ((IJavaObject) value).getUniqueId() : -1;
				this.value = value;
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		}

		public int hashCode() {
			return hashCode;
		}

		public boolean equals(Object obj) {
			Wrapper other = (Wrapper) obj;
			return this.objId == other.getObjId() && clazzname.equals(other.getObjClassName());
		}

		public String toString() {
			try {
				return value.getValueString();
			} catch (DebugException e) {
				throw new RuntimeException(e);
			}
		}

		public IJavaValue get() {
			return value;
		}

		public String getObjClassName() {
			return this.clazzname;
		}

		public long getObjId() {
			return this.objId;
		}
	}

	public void associateId(IJavaValue obj, Object id) {
		map.put(new IdWrapper(obj), id);
		++counter;
		cleanup();
	}

	public Object lookupId(IJavaValue obj) {
		Object id = map.get(new IdWrapper(obj));
		++counter;
		return id;
	}

	public boolean containsId(IJavaValue item) {
		boolean b = map.containsKey(new IdWrapper(item));
		++counter;
		return b;
	}

	public void removeId(IJavaValue item) {
		map.remove(new IdWrapper(item));
		++counter;
		cleanup();
	}

	public int size() {
		return map.size();
	}

	private void cleanup() {
		if (counter > 10000) {
			counter = 0;
			// much more efficient to remove any orphaned wrappers at once
			for (final Iterator<Wrapper> iterator = map.keySet().iterator(); iterator.hasNext();) {
				final Wrapper key = iterator.next();
				if (key.get() == null) {
					iterator.remove();
				}
			}
		}
	}
}
