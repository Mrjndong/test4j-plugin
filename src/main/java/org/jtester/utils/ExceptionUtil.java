package org.jtester.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtil {
	public final static String getThrowableTrace(Throwable throwable) {
		if (throwable == null) {
			return "not existing throwable";
		}
		StringWriter message = new StringWriter();
		PrintWriter log = new PrintWriter(message);
		throwable.printStackTrace(log);
		return message.toString();
	}
}
