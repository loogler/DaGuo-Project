/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daguo.R;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.beans.AddBanner;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-30 上午10:53:18
 * @function ：
 */
public class Main_BottomBannerAdapter extends BaseAdapter {

    Activity activity;
    List<AddBanner> lists;

    public Main_BottomBannerAdapter(Activity activity, List<AddBanner> lists) {
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
    public Object getItem(int arg0) {
	return lists.get(arg0);
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
    @SuppressLint({ "ViewHolder", "InflateParams" })
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	View view = null;
	// if (v == null) {
	//
	// view.setTag(p);
	// } else {
	// v = (View) view.getTag();
	// }
	view = LayoutInflater.from(activity).inflate(
		R.layout.adapter_main_bottombanner, null);

	ImageView add_iv = (ImageView) view.findViewById(R.id.add_iv);
	ImageLoader.getInstance().displayImage(
		HttpUtil.IMG_URL + lists.get(p).getImg_path(), add_iv);
	int width = GetScreenRecUtil.getWindowWidth(activity);
	add_iv.setLayoutParams(new LinearLayout.LayoutParams(width,
		2 * width / 5));
	view.setOnClickListener(new AddBannerOnclickListener(activity, lists, p));
	return view;
    }

}
