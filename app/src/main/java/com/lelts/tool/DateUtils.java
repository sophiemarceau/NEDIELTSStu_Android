package com.lelts.tool;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;

public class DateUtils {
	private static SimpleDateFormat sf = null;

	/* 获取系统时间 格式为："yyyy/MM/dd " */
	public static String getCurrentDate() {
		Date d = new Date();
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}

	/* 时间戳转换成字符窜 */
	public static String getDateToString(long time) {
		Date d = new Date(time);
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		return sf.format(d);
	}

	/* 将字符串转为时间戳 */
	public static long getStringToDate(String time) {
		sf = new SimpleDateFormat("yyyy年MM月dd日");
		Date date = new Date();
		try {
			date = sf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return date.getTime();
	}

}
