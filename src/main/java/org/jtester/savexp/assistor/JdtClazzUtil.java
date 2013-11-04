package org.jtester.savexp.assistor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.IStatusHandler;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.core.IJavaClassType;
import org.eclipse.jdt.debug.core.IJavaDebugTarget;
import org.eclipse.jdt.debug.core.IJavaFieldVariable;
import org.eclipse.jdt.debug.core.IJavaInterfaceType;
import org.eclipse.jdt.debug.core.IJavaObject;
import org.eclipse.jdt.debug.core.IJavaStackFrame;
import org.eclipse.jdt.debug.core.IJavaThread;
import org.eclipse.jdt.debug.core.IJavaType;
import org.eclipse.jdt.debug.core.IJavaValue;
import org.eclipse.jdt.debug.core.IJavaVariable;
import org.eclipse.jdt.internal.debug.core.JDIDebugPlugin;
import org.eclipse.jdt.internal.debug.core.model.JDIClassType;
import org.eclipse.jdt.internal.debug.core.model.JDIObjectValue;

import com.sun.jdi.Method;
import com.sun.jdi.ObjectReference;
import com.sun.jdi.ReferenceType;
import com.sun.jdi.Type;

@SuppressWarnings("restriction")
public class JdtClazzUtil {
	public static Class<?> getClazz(String clazzname, Class<?> refclazz) {
		if (clazzname == null) {
			return refclazz;
		}
		try {
			return Class.forName(clazzname);
		} catch (ClassNotFoundException e) {
			return refclazz;
		}
	}

	public static Map<String, Class<?>> maps = new HashMap<String, Class<?>>() {
		private static final long serialVersionUID = -1687941947929726195L;

		{
			this.put("int", int.class);
			this.put("long", long.class);
			this.put("float", float.class);
			this.put("double", double.class);
			this.put("short", short.class);
			this.put("char", char.class);
			this.put("byte", byte.class);
			this.put("boolean", boolean.class);
		}
	};

	public static boolean canFindClazz(IJavaType type) {
		if (type == null) {
			return false;
		}
		try {
			String clazz = type.getName();
			getClazz(clazz);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean canFindClazz(IJavaValue item) {
		if (item == null) {
			return false;
		}
		try {
			String clazz = item.getJavaType().getName();
			getClazz(clazz);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static Class<?> getClazz(IJavaValue item) {
		if (item == null) {
			return null;
		}
		try {
			String clazz = item.getJavaType().getName();
			return getClazz(clazz);
		} catch (ClassNotFoundException e) {
			return item.getClass();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static Class<?> getClazz(String clazz) throws ClassNotFoundException {
		if (maps.containsKey(clazz)) {
			return maps.get(clazz);
		} else {
			return Class.forName(clazz);
		}
	}

	public static Class<?> getClazz(IVariable item) {
		if (item == null) {
			return null;
		}
		try {
			IJavaValue value = (IJavaValue) item.getValue();
			String clazzname = value.getJavaType().getName();
			return getClazz(clazzname);
		} catch (ClassNotFoundException e) {
			return item.getClass();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static Class<?> getClazz(IJavaType type) {
		if (type == null) {
			return null;
		}
		try {
			String clazzname = type.getName();
			return getClazz(clazzname);
		} catch (ClassNotFoundException e) {
			return type.getClass();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isIJavaValue(Object item) {
		if (item == null) {
			return false;
		} else if (item instanceof IJavaValue) {
			return true;
		} else {
			return false;
		}
	}

	public static String getValueString(IValue value) {
		try {
			return value == null ? null : value.getValueString();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean typeEquals(IJavaType type1, IJavaType type2) {
		try {
			String claz1 = type1.getName();
			String claz2 = type2.getName();
			return claz1.equals(claz2);
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isStaticOrTransient(IVariable var) {
		try {
			if (var instanceof IJavaFieldVariable) {
				IJavaFieldVariable value = (IJavaFieldVariable) var;
				return value.isStatic() || value.isTransient();
			} else if (var instanceof IJavaVariable) {
				IJavaVariable value = (IJavaVariable) var;
				return value.isStatic();
			} else {
				return false;
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isNullValue(IValue value) {
		if (value == null) {
			return true;
		}
		try {
			if (value.getReferenceTypeName().equals("null")) {
				return true;
			} else {
				return false;
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static final String TOSTRING_SELECTOR = "toString"; //$NON-NLS-1$

	public static String toString(IJavaValue item) {
		return callNoneParaReturnStringMethod(item, TOSTRING_SELECTOR);
	}

	public static final String CLAZZ_GETNAME = "getName";

	public static String getClazzName(IJavaValue clazz) throws DebugException {
		Class<?> claz = JdtClazzUtil.getClazz(clazz.getJavaType());
		if (!claz.equals(Class.class)) {
			return clazz.getJavaType().getName();
		}
		return callNoneParaReturnStringMethod(clazz, CLAZZ_GETNAME);
	}

	public static final String NONE_PARA_RETURN_STR = "()Ljava/lang/String;"; //$NON-NLS-1$

	public static String callNoneParaReturnStringMethod(IJavaValue item, String methodname) {
		if (isNullValue(item)) {
			return null;
		}
		try {
			IJavaValue result = invokeNoParaMethod(item, methodname, NONE_PARA_RETURN_STR);
			return result.getValueString();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static IJavaValue invokeNoParaMethod(IJavaValue item, String methodname, String signature) {
		if (isNullValue(item)) {
			return null;
		}
		IJavaObject value = (IJavaObject) item;
		try {
			IJavaThread thread = (IJavaThread) getStackFrame(value).getThread();
			IJavaValue result = value.sendMessage(methodname, signature, null, thread, false);
			return result;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	public static IJavaValue invokeParaMethod(IJavaValue item, String methodname, String signature, IJavaValue[] args) {
		if (isNullValue(item)) {
			return null;
		}
		IJavaObject value = (IJavaObject) item;
		try {
			IJavaThread thread = (IJavaThread) getStackFrame(value).getThread();
			IJavaValue result = value.sendMessage(methodname, signature, args, thread, false);
			return result;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static void printMethods(IJavaValue value) {
		ObjectReference ref = ((JDIObjectValue) value).getUnderlyingObject();
		List<Method> methods = ((ReferenceType) ref.type()).allMethods();
		for (Method method : methods) {
			System.out.println(method.name() + ":" + method.signature());
		}
	}

	/**
	 * 获得运行时的StackFrame信息
	 * 
	 * @param value
	 * @return
	 * @throws CoreException
	 */
	public static IJavaStackFrame getStackFrame(IValue value) throws CoreException {
		IStatusHandler handler = DebugPlugin.getDefault().getStatusHandler(JDIDebugPlugin.STATUS_GET_EVALUATION_FRAME);
		if (handler != null) {
			IJavaStackFrame stackFrame = (IJavaStackFrame) handler.handleStatus(
					JDIDebugPlugin.STATUS_GET_EVALUATION_FRAME, value);
			if (stackFrame != null) {
				return stackFrame;
			}
		}
		IDebugTarget target = value.getDebugTarget();
		IJavaDebugTarget javaTarget = (IJavaDebugTarget) target.getAdapter(IJavaDebugTarget.class);
		if (javaTarget != null) {
			IThread[] threads = javaTarget.getThreads();
			for (int i = 0; i < threads.length; i++) {
				IThread thread = threads[i];
				if (thread.isSuspended()) {
					return (IJavaStackFrame) thread.getTopStackFrame();
				}
			}
		}
		return null;
	}

	public static boolean isEnum(IJavaType type) {
		if (!(type instanceof IJavaClassType)) {
			return false;
		}

		try {
			return ((IJavaClassType) type).isEnum();
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isAssignableFrom(IJavaType type, Class<?> clazz) {
		if (!(type instanceof IJavaClassType)) {
			return false;
		}
		try {
			Class<?> cls = JdtClazzUtil.getClazz(type);
			if (cls.getName().startsWith("org.eclipse.jdt")) {
				IJavaClassType superType = ((IJavaClassType) type).getSuperclass();
				return hasInterface(type, clazz) || isAssignableFrom(superType, clazz);
			} else {
				return clazz.isAssignableFrom(cls);
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean hasInterface(IJavaType type, Class<?> interfaceType) {
		if (!(type instanceof JDIClassType)) {
			return false;
		}
		return hasInterface(type, interfaceType.getName());
	}

	public static boolean hasInterface(IJavaType type, String interfaceType) {
		if (!(type instanceof JDIClassType)) {
			return false;
		}
		try {
			IJavaInterfaceType[] interfaces = ((IJavaClassType) type).getInterfaces();
			for (IJavaInterfaceType it : interfaces) {
				if (it.getName().equalsIgnoreCase(interfaceType)) {
					return true;
				}
			}
			return false;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isAssignableFrom(IJavaType type, String clazz) {
		if (!(type instanceof IJavaClassType)) {
			return false;
		}
		try {
			if (type.getName().equalsIgnoreCase(clazz)) {
				return true;
			} else if (type.getName().equals("java.lang.Object")) {
				return false;
			} else {
				IJavaClassType superType = ((IJavaClassType) type).getSuperclass();
				return hasInterface(type, clazz) || isAssignableFrom(superType, clazz);
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isSerializable(IJavaType type, boolean includeBaseClasses) {
		if (!(type instanceof JDIClassType)) {
			return false;
		}
		try {
			IJavaInterfaceType[] interfaces = ((IJavaClassType) type).getAllInterfaces();
			boolean hasSerializableInterface = false;
			for (IJavaInterfaceType it : interfaces) {
				if (it.getName().equalsIgnoreCase(Serializable.class.getName())) {
					hasSerializableInterface = true;
					break;
				}
			}
			if (hasSerializableInterface == false) {
				return false;
			}
			return supportsWriteObject(type, includeBaseClasses) || supportsWriteObject(type, includeBaseClasses);
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public final static String READ_OBJECT_SIGNATURE = "(Ljava/io/ObjectInputStream;)V";

	public final static String WRITE_OBJECT_SIGNATURE = "(Ljava/io/ObjectOutputStream;)V";

	@SuppressWarnings("unchecked")
	public static boolean supportsWriteObject(IJavaType type, boolean includeBaseClasses) {
		if (!(type instanceof JDIClassType)) {
			return false;
		}
		try {
			Type reftype = ((JDIClassType) type).getUnderlyingType();
			List<Method> writes = ((ReferenceType) reftype).methodsByName("writeObject", WRITE_OBJECT_SIGNATURE);
			if (writes == null || writes.size() == 0) {
				return false;
			} else if (includeBaseClasses == true) {
				return true;
			}
			for (Method method : writes) {
				ReferenceType declareingType = method.declaringType();
				if (declareingType.name().equalsIgnoreCase(type.getName())) {
					return true;
				}
			}
			return false;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	public static boolean supportsReadObject(IJavaType type, boolean includeBaseClasses) {
		if (!(type instanceof JDIClassType)) {
			return false;
		}
		try {
			Type reftype = ((JDIClassType) type).getUnderlyingType();
			List<Method> writes = ((ReferenceType) reftype).methodsByName("readObject", READ_OBJECT_SIGNATURE);
			if (writes == null || writes.size() == 0) {
				return false;
			} else if (includeBaseClasses == true) {
				return true;
			}
			for (Method method : writes) {
				ReferenceType declareingType = method.declaringType();
				if (declareingType.name().equalsIgnoreCase(type.getName())) {
					return true;
				}
			}
			return false;
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<IJavaType> hierarchyFor(IJavaType type) {
		List<IJavaType> result = new ArrayList<IJavaType>();
		if (!(type instanceof IJavaClassType)) {
			return result;
		}
		IJavaClassType clazz = (IJavaClassType) type;
		try {
			while (!clazz.getName().equalsIgnoreCase(Object.class.getName())) {
				result.add(clazz);
				clazz = clazz.getSuperclass();
			}
		} catch (DebugException e) {
			throw new RuntimeException(e);
		}
		Collections.reverse(result);
		return result;
	}

	public static IJavaValue readField(IJavaValue source, String field) throws DebugException {
		IVariable[] vars = source.getVariables();
		for (IVariable var : vars) {
			if (field.equals(var.getName())) {
				return (IJavaValue) var.getValue();
			}
		}
		throw new RuntimeException("no such field named " + field + " in " + source.getReferenceTypeName());
	}

	public static IJavaProject getProject(IValue value) throws CoreException {
		IJavaStackFrame javaStackFrame = JdtClazzUtil.getStackFrame(value);
		ILaunch launch = value.getLaunch();
		if (launch == null) {
			return null;
		}
		ISourceLocator locator = launch.getSourceLocator();
		if (locator == null) {
			return null;
		}

		Object sourceElement = locator.getSourceElement(javaStackFrame);
		if (!(sourceElement instanceof IJavaElement) && sourceElement instanceof IAdaptable) {
			sourceElement = ((IAdaptable) sourceElement).getAdapter(IJavaElement.class);
		}
		if (sourceElement instanceof IJavaElement) {
			return ((IJavaElement) sourceElement).getJavaProject();
		}
		return null;
	}

	public static IJavaProject getProject(IJavaStackFrame javaStackFrame) {
		ILaunch launch = javaStackFrame.getLaunch();
		if (launch == null) {
			return null;
		}
		ISourceLocator locator = launch.getSourceLocator();
		if (locator == null) {
			return null;
		}

		Object sourceElement = locator.getSourceElement(javaStackFrame);
		if (!(sourceElement instanceof IJavaElement) && sourceElement instanceof IAdaptable) {
			sourceElement = ((IAdaptable) sourceElement).getAdapter(IJavaElement.class);
		}
		if (sourceElement instanceof IJavaElement) {
			return ((IJavaElement) sourceElement).getJavaProject();
		}
		return null;
	}
}
