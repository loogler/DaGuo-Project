package com.daguo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 故先要整清楚现在已经开放了多少个号码段，国家号码段分配如下：
 * 
 * @author Bugs_Rabbit 時間： 2015-8-11 下午4:09:55
 */
public class TelNumberCheckUtil {
	public static boolean isMobileNO(String mobiles) {

		Pattern p = Pattern
				.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(19[0-9])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(mobiles);

		return m.matches();
	}
}
