/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import com.daguo.R;
import com.daguo.util.beans.TelNumber;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-29 下午8:46:41
 * @function ：
 */
public class Oper_mobileAdapter extends BaseAdapter {

    Activity activity;
    List<TelNumber> lists;
    LayoutInflater inflater;

    public Oper_mobileAdapter(Activity activity, List<TelNumber> lists) {
	this.activity = activity;
	this.lists = lists;
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
	return lists == null ? null : lists.get(arg0);
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
    @SuppressLint("InflateParams")
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	ViewHolder holder;
	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_oper_mobile, null);
	    holder = new ViewHolder();
	    holder.tel_tv = (TextView) v.findViewById(R.id.tel_tv);

	    v.setTag(holder);

	} else {
	    holder = (ViewHolder) v.getTag();

	}

	holder.tel_tv.setText(lists.get(p).getTel());

	return v;
    }

    private class ViewHolder {
	TextView tel_tv;
    }

}
