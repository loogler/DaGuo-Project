/**
 * 互相学习 共同进步
 */
package com.daguo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ta.utdid2.android.utils.StringUtils;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-30 上午10:47:55
 * @function ： 公用的工具方法
 */
public class PublicTools {

    // 如何写一个单利模式

    /**
     * 为嵌套的listView （当前为scrollView内部嵌套ListView） 提供高度不能显示完全的解决方案， 重新设置listview高度
     * ，将高度撑开 变成当前的View 如果存在底部不完全显示的问题，+xxx dp 意思是 缩短 -xxx dp 意思是底部增加xxx
     * dp高度来加长显示
     * 
     * @param listView
     *            控件为listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
	if (listView == null)
	    return;
	ListAdapter listAdapter = listView.getAdapter();
	if (listAdapter == null) {
	    // pre-condition
	    return;
	}
	int totalHeight = 0;
	for (int i = 0; i < listAdapter.getCount(); i++) {
	    View listItem = listAdapter.getView(i, null, listView);
	    listItem.measure(0, 0);
	    totalHeight += listItem.getMeasuredHeight();
	}
	ViewGroup.LayoutParams params = listView.getLayoutParams();
	params.height = totalHeight
		+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
	listView.setLayoutParams(params);
    }

    /**
     * 该工具用来 处理带html标签的字符串，获得纯净的字符串，去掉标签
     * 
     * @param content
     *            需要处理的html标签 字符串
     * @param length
     *            处理后取多长的文字内容 如果不足长度 取最大值
     * @return 正则之后的文字
     */
    public static String getContentSummary(String content, int length) {
	String regex = "<[^>]*>";

	Pattern pattern = Pattern.compile(regex);
	if (StringUtils.isEmpty(content)) {
	    return content;
	}
	Matcher matcher = pattern.matcher(content);

	content = matcher.replaceAll("");
	content = content.replace("\t", "");
	content = content.replace("\n", "");
	content = content.replace("&nbsp;", "");

	if (content.length() <= length) {
	    return content;
	}
	return content.substring(0, length) + "...";
    }

    /**
     * 用于计算时间差的工具类 xx秒前 xx分钟前 xx天前。。。。
     * 
     * @param time
     *            插入时间 ，时间格式必须为标准的 yyyy-MM-dd HH:m:s 格式 否则会出错
     * @return 时间差
     * @throws ParseException
     * 
     */
    public static String DateFormat(String time) throws ParseException {
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
	Date date = format.parse(time);
	long delta = new Date().getTime() - date.getTime();
	if (delta < 1L * 60000L) {
	    long seconds = toSeconds(delta);
	    return (seconds <= 0 ? 1 : seconds) + "秒前";
	}
	if (delta < 45L * 60000L) {
	    long minutes = toMinutes(delta);
	    return (minutes <= 0 ? 1 : minutes) + "分钟前";
	}
	if (delta < 24L * 3600000L) {
	    long hours = toHours(delta);
	    return (hours <= 0 ? 1 : hours) + "小时前";
	}
	if (delta < 48L * 3600000L) {
	    return "昨天";
	}
	if (delta < 30L * 86400000L) {
	    long days = toDays(delta);
	    return (days <= 0 ? 1 : days) + "天前";
	}
	if (delta < 12L * 4L * 604800000L) {
	    long months = toMonths(delta);
	    return (months <= 0 ? 1 : months) + "月前";
	} else {
	    long years = toYears(delta);
	    return (years <= 0 ? 1 : years) + "年前";
	}
    }

    private static long toSeconds(long date) {
	return date / 1000L;
    }

    private static long toMinutes(long date) {
	return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
	return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
	return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
	return toDays(date) / 30L;
    }

    private static long toYears(long date) {
	return toMonths(date) / 365L;
    }

    /**
     * @param res
     *            资源
     * @return 处理后的字符串
     * 
     *         处理数据 禁止加载空值为null 如果确实为null 则转化为“” 空字符串
     */
    public static String doWithNullData(String res) {
	if (res != null && !"".equals(res) && !"null".equals(res)
		&& !"[]".equals(res)) {
	    return res;
	} else {
	    return "";
	}
    }

    /**
     * 判断手机是否有SD卡。
     * 
     * @return 有SD卡返回true，没有返回false。
     */
    public static boolean hasSDCard() {
	return Environment.MEDIA_MOUNTED.equals(Environment
		.getExternalStorageState());
    }

    /**
     * 
     * 通过获取sdk号 来处理不同版本号之间的兼容问题
     * 
     * @return int类型的sdk版本号
     */
    public static int getPhoneAndroidSDK() {
	int version = 0;
	try {
	    version = Integer.valueOf(android.os.Build.VERSION.SDK);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	}
	return version;

    }

    /**
     * 将List<String> 数组转化为String
     * 
     * @param stringList
     *            List<String> 数组
     * @return 以逗号分隔开的String
     */
    public static String listToString(List<String> stringList) {
	if (stringList == null) {
	    return null;
	}
	StringBuilder result = new StringBuilder();
	boolean flag = false;
	for (String string : stringList) {
	    if (flag) {
		result.append(",");
	    } else {
		flag = true;
	    }
	    result.append(string);
	}
	return result.toString();
    }

}
