/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.text.ParseException;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-1 下午4:41:48
 * @function ：
 */
public class SC_SheTuanDetailAdapter extends BaseAdapter {

    List<Evaluate_Ordinary> lists;
    Activity activity;

    public SC_SheTuanDetailAdapter(Activity activity,
	    List<Evaluate_Ordinary> lists) {
	this.activity = activity;
	this.lists = lists;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {

	return lists.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {

	return lists.get(position);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
	return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	convertView = LayoutInflater.from(activity).inflate(
		R.layout.adapter_eva_ordinary, null);

	CircularImage head_circularimage = (CircularImage) convertView
		.findViewById(R.id.head_circularimage);
	TextView floor_tv = (TextView) convertView.findViewById(R.id.floor_tv);
	TextView name_tv = (TextView) convertView.findViewById(R.id.name_tv);
	TextView time_tv = (TextView) convertView.findViewById(R.id.time_tv);
	ImageView sex_iv = (ImageView) convertView.findViewById(R.id.sex_iv);
	TextView year_department_tv = (TextView) convertView
		.findViewById(R.id.year_department_tv);
	TextView content_tv = (TextView) convertView
		.findViewById(R.id.content_tv);

	String content = lists.get(position).getContent();
	String headInfo = lists.get(position).getHead_info();
	String p_name = lists.get(position).getP_name();
	String create_time = lists.get(position).getCreate_time();
	String sex = lists.get(position).getSex();
	String pro_name = lists.get(position).getPro_name();
	String start_year = lists.get(position).getStart_year();

	FinalBitmap.create(activity).display(head_circularimage,
		HttpUtil.IMG_URL + PublicTools.doWithNullData(headInfo));
	floor_tv.setText(position + " 楼");
	name_tv.setText(PublicTools.doWithNullData(p_name));
	try {
	    time_tv.setText(PublicTools.DateFormat(PublicTools
		    .doWithNullData(create_time)));
	} catch (ParseException e) {
	    Log.e("社交评论", "时间差计算的格式出现问题");
	    e.printStackTrace();
	}
	if (PublicTools.doWithNullData(sex).equals("0")) {
	    // nan
	    sex_iv.setImageResource(R.drawable.icon_sex_man);
	} else if (PublicTools.doWithNullData(sex).equals("1")) {
	    // nv
	    sex_iv.setImageResource(R.drawable.icon_sex_woman);
	}
	String ssString = "";
	if (PublicTools.doWithNullData(start_year).equals("")) {
	    ssString = "";
	} else {
	    ssString = PublicTools.doWithNullData(start_year) + "级";
	}

	year_department_tv.setText(ssString
		+ PublicTools.doWithNullData(pro_name));
	content_tv.setText(PublicTools.doWithNullData(content));

	return convertView;
    }

}
