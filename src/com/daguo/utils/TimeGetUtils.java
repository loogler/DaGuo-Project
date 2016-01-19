package com.daguo.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-19 上午10:37:23
 * @function ：获取时间处理信息的工具
 */
public class TimeGetUtils {

    /**
     * 
     * @param format
     * @return 当前时间
     */
    public static String getCurrentTime(String format) {
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
	String currentTime = sdf.format(date);
	return currentTime;
    }

    /**
     * 
     * @return
     */
    public static String getCurrentTime() {
	return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }

    /**
     * 获取当前的calendar 转化为date
     * 
     * @return
     */
    public static Date CalendarToDate(Calendar cal) {
	// cal = Calendar.getInstance();
	Date date = cal.getTime();
	return date;

    }

    /**
     * 获取date转化为calendar 可以通过calend.year calendar.moth 获取当前的年月日。
     * 
     * @param date
     * @return
     */
    public static Calendar dateToCalendar(Date date) {
	// Date date=new Date();
	Calendar cal = Calendar.getInstance();
	cal.setTime(date);
	return cal;
    }
}
