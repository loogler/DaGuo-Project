/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.OutLet;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-7 上午10:53:13
 * @function ：
 */
public class OutLetAdapter extends BaseAdapter {

    Activity activity;
    List<OutLet> lists;
    LayoutInflater inflater;
    FinalBitmap finalBitmap;

    public OutLetAdapter(Activity activity, List<OutLet> lists) {
	this.lists = lists;
	this.activity = activity;
	inflater = LayoutInflater.from(activity);
	finalBitmap = FinalBitmap.create(activity);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
	return lists == null ? 0 : lists.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int arg0) {
	return arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int arg0) {
	return arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	VH vh = null;
	if (v == null) {
	    v = inflater.inflate(R.layout.outletadapter, null);
	    vh = bindView(vh, v);
	    v.setTag(vh);
	} else {
	    vh = (VH) v.getTag();
	}
	setData(vh, p);

	return v;
    }

    private VH bindView(VH vh, View v) {
	vh = new VH();
	vh.distance_tv = (TextView) v.findViewById(R.id.distance_tv);
	vh.name_tv = (TextView) v.findViewById(R.id.name_tv);
	vh.photo = (CircularImage) v.findViewById(R.id.photo);
	vh.schoolName_tv = (TextView) v.findViewById(R.id.schoolName_tv);
	vh.sex_iv = (ImageView) v.findViewById(R.id.sex_iv);
	vh.time_tv = (TextView) v.findViewById(R.id.time_tv);

	return vh;
    }

    /**
     * 
     */
    private void setData(VH vh, int p) {
	vh.distance_tv.setText(lists.get(p).getDistance());
	vh.name_tv.setText(lists.get(p).getName());
	finalBitmap.display(vh.photo, HttpUtil.IMG_URL
		+ lists.get(p).getHead_info());
	if ("0".equals(lists.get(p).getSex())) {
	    // 男
	    vh.sex_iv.setImageResource(R.drawable.icon_sex_man);
	} else if ("1".equals(lists.get(p).getSex())) {

	    vh.sex_iv.setImageResource(R.drawable.icon_sex_woman);
	}
	vh.schoolName_tv.setText(lists.get(p).getPro_name());
	try {
	    vh.time_tv.setText(PublicTools.DateFormat(lists.get(p).getTime()));
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    private class VH {
	CircularImage photo;
	TextView name_tv;
	ImageView sex_iv;
	TextView schoolName_tv;
	TextView distance_tv;
	TextView time_tv;
    }

}
