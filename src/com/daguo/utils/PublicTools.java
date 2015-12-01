/**
 * 互相学习 共同进步
 */
package com.daguo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

}
