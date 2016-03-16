/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.annotation.SuppressLint;
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
 * @version 创建时间：2016-1-19 下午3:19:11
 * @function ：主页中间功能按钮适配
 */
public class Main1_FunctionIconAdapter extends BaseAdapter {
    Activity activity;
    List<SC_SheTuan> lists;
    FinalBitmap finalBitmap;
    LayoutInflater inflater;

    public Main1_FunctionIconAdapter(Activity activity, List<SC_SheTuan> lists) {
	this.activity = activity;
	this.lists = lists;
	finalBitmap = FinalBitmap.create(activity);
	inflater = LayoutInflater.from(activity);

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
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int arg0) {
	return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	VH vh = null;

	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_main1_functionicon, null);
	    vh = getVH(vh, v);
	    v.setTag(vh);

	} else {
	    vh = (VH) v.getTag();
	}
	bindData(vh, p);

	return v;
    }

    private VH getVH(VH vh, View v) {

	vh = new VH();
	vh.icon_iv = (ImageView) v.findViewById(R.id.icon_iv);
	vh.kinds_tv = (TextView) v.findViewById(R.id.kinds_tv);

	return vh;
    }

    private void bindData(VH vh, int p) {

	vh.kinds_tv
		.setText(PublicTools.doWithNullData(lists.get(p).getTitle()));
	finalBitmap.display(vh.icon_iv, HttpUtil.IMG_URL
		+ lists.get(p).getImg_path());

    }

    private class VH {
	ImageView icon_iv;
	TextView kinds_tv;
    }

}
