package org.jtester.utils;

public class MethodID {
	private final String className;
	private final String methodName;

	public MethodID(String className, String methodName) {
		this.className = className;
		this.methodName = methodName;
	}

	public int hashCode() {
		return this.className.hashCode() + this.methodName.hashCode();
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof MethodID)) {
			return false;
		}
		MethodID id = (MethodID) obj;
		return this.className.equals(id.className) && this.methodName.equals(id.methodName);
	}

	/**
	 * 返回testcase的class name
	 * 
	 * @return
	 */
	public String clazz() {
		return className;
	}

	/**
	 * 返回testcase的method name
	 * 
	 * @return
	 */
	public String method() {
		return methodName;
	}
}
