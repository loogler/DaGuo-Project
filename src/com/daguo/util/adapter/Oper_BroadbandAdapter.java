/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import com.daguo.R;
import com.daguo.util.beans.BroadBand;

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
 * @version 创建时间：2015-12-30 上午4:05:30
 * @function ：
 */
public class Oper_BroadbandAdapter extends BaseAdapter {

    Activity activity;
    List<BroadBand> lists;
    LayoutInflater inflater;

    public Oper_BroadbandAdapter(Activity activity, List<BroadBand> lists) {
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
	return null;
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
	ViewHolder holder = null;

	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_oper_broadband, null);
	    holder = getHolder(holder, v);
	    v.setTag(holder);

	} else {
	    holder = (ViewHolder) v.getTag();

	}

	if (lists != null) {

	    holder.name_tv.setText("宽带名称：" + lists.get(p).getDetailName());
	    holder.price_tv.setText("宽带价格： " + lists.get(p).getPrice()+" 元");
	    holder.school_tv.setText("所属学校： " + lists.get(p).getsName());
	    holder.time_tv.setText("服务时间： " + lists.get(p).getMonth()+" 月");

	}
	return v;
    }

    private ViewHolder getHolder(ViewHolder holder, View v) {
	holder = new ViewHolder();
	holder.name_tv = (TextView) v.findViewById(R.id.name_tv);
	holder.price_tv = (TextView) v.findViewById(R.id.price_tv);
	holder.school_tv = (TextView) v.findViewById(R.id.school_tv);
	holder.time_tv = (TextView) v.findViewById(R.id.time_tv);
	return holder;
    }

    private class ViewHolder {
	TextView name_tv;
	TextView price_tv;
	TextView school_tv;
	TextView time_tv;
    }

}
