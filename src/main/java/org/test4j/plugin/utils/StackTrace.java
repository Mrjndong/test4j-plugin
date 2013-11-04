package org.test4j.plugin.utils;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用于保存testcase失败的堆栈信息
 * 
 * @author darui.wudr
 * 
 */
public class StackTrace {
	static boolean beenLogged = false;
	private final String error;
	private final List<StackTraceElement> frames;
	private final List<StackTraceElement> beforeTestTraceFrames;
	private final MethodID methodID;

	public StackTrace(MethodID methodID, String error, List<StackTraceElement> frames) {
		this.methodID = methodID;
		this.error = error;
		this.frames = frames;
		this.beforeTestTraceFrames = getBeforeTestCaseTraceFrames();
	}

	public static StackTrace create(Throwable exception) {
		List<StackTraceElement> frameList = new ArrayList<StackTraceElement>();
		for (StackTraceElement each : exception.getStackTrace()) {
			frameList.add(each);
		}
		return new StackTrace(null, exception.toString(), frameList);
	}

	public String getError() {
		Pattern comparison = Pattern.compile("org\\.junit\\.ComparisonFailure\\: (.*)", Pattern.UNICODE_CASE);
		Matcher matcher = comparison.matcher(this.error);
		if (matcher.find()) {
			return matcher.group(1);
		}
		Pattern objectComparison = Pattern.compile("java\\.lang\\.AssertionError\\: (.*)", Pattern.UNICODE_CASE);
		matcher.usePattern(objectComparison);
		if (matcher.find()) {
			String message = matcher.group(1).trim();
			if (message.length() > 0) {
				return message;
			}
		}
		return this.error;
	}

	/**
	 * 截取testcase错误信息跟踪列表
	 * 
	 * @return
	 */
	private List<StackTraceElement> getBeforeTestCaseTraceFrames() {
		List<StackTraceElement> traces = new ArrayList<StackTraceElement>();
		StackTraceElement lastTrace = null;
		for (StackTraceElement trace : this.frames) {
			if (trace.getClassName().equals("sun.reflect.NativeMethodAccessorImpl")
					&& trace.getMethodName().equals("invoke0")) {
				if (lastInvokedIsTestCase(lastTrace)) {
					break;
				}
			}
			traces.add(trace);
			lastTrace = trace;
		}
		return traces;
	}

	private boolean lastInvokedIsTestCase(StackTraceElement trace) {
		if (trace == null) {
			return false;
		}
		if (trace.getMethodName().equals("invokeExplosively")) {
			return false;
		}
		if (trace.getMethodName().equals("runTest")) {
			return false;
		}
		return true;
	}

	public int getTestLineNumber() {
		for (StackTraceElement frame : this.beforeTestTraceFrames) {
			if (frame.getClassName().equals(methodID.clazz())) {
				return frame.getLineNumber();
			}
		}
		return -1;
	}

	/**
	 * 获得错误消息
	 * 
	 * @param trace
	 * @return
	 */
	public String failureMessage() {
		StringWriter writer = new StringWriter();
		writer.write(this.getError());
		
//		for (StackTraceElement frame : this.beforeTestTraceFrames) {
//			writer.write("\t");
//			writer.write(frame.getClassName());
//			writer.write(".");
//			writer.write(frame.getMethodName());
//			writer.write("(...)");
//		}
		return writer.toString();
	}

	public List<StackTraceElement> getBeforeTestTraceFrames() {
		return beforeTestTraceFrames;
	}
}