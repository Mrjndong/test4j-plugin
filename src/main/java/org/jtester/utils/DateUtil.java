package org.jtester.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public final static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SIMPLE_DATE_FORMAT);

	public static String now() {
		Date date = new Date();
		return simpleDateFormat.format(date);
	}
}
