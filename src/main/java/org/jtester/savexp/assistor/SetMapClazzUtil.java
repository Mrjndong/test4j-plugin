package org.jtester.savexp.assistor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaPrimitiveValue;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaValue;

public class SetMapClazzUtil {
	public static final String ITERATOR_SIGNATURE = "()Ljava/util/Iterator;";

	public static final String HASNEXT_SIGNATURE = "()Z";

	public static final String NEXT_SIGNATURE = "()Ljava/lang/Object;";

	public static final String ENTRYSET_SIGNATURE = "()Ljava/util/Set;";

	public static final String ENTRY_GETKEY_SIGNATRUE = "()Ljava/lang/Object;";

	public static final String ENTRY_GETVALUE_SIGNATRUE = "()Ljava/lang/Object;";

	public static final String COMPARATOR_SIGNATURE = "()Ljava/util/Comparator;";

	public static IJavaValue iterator(IJavaValue set) {
		if (JdtClazzUtil.isNullValue(set)) {
			return null;
		}
		IJavaObject value = (IJavaObject) set;

		try {
			IJavaThread thread = (IJavaThread) JdtClazzUtil.getStackFrame(value).getThread();
			IJavaValue result = value.sendMessage("iterator", ITERATOR_SIGNATURE, null, thread, false);
			return result;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean hasNext(IJavaValue iterator) {
		IJavaPrimitiveValue hasNext = (IJavaPrimitiveValue) JdtClazzUtil.invokeNoParaMethod(iterator, "hasNext",
				HASNEXT_SIGNATURE);
		return hasNext.getBooleanValue();
	}

	public static IJavaValue next(IJavaValue iterator) {
		IJavaValue next = JdtClazzUtil.invokeNoParaMethod(iterator, "next", NEXT_SIGNATURE);
		return next;
	}

	public static IJavaValue entrySet(IJavaValue map) {
		IJavaValue next = JdtClazzUtil.invokeNoParaMethod(map, "entrySet", ENTRYSET_SIGNATURE);
		return next;
	}

	public static IJavaValue getEntryKey(IJavaValue entry) {
		IJavaValue next = JdtClazzUtil.invokeNoParaMethod(entry, "getKey", ENTRY_GETKEY_SIGNATRUE);
		return next;
	}

	public static IJavaValue getEntryValue(IJavaValue entry) {
		IJavaValue next = JdtClazzUtil.invokeNoParaMethod(entry, "getValue", ENTRY_GETVALUE_SIGNATRUE);
		return next;
	}

	public static IJavaValue comparator(IJavaValue sortedMap) {
		IJavaValue comparator = JdtClazzUtil.invokeNoParaMethod(sortedMap, "comparator", COMPARATOR_SIGNATURE);
		return comparator;
	}
}
