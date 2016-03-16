/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import com.daguo.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-22 下午5:52:46
 * @function ：
 */
public class UserInfo_MyCent_DailyTaskAdapter extends BaseAdapter {

    Activity activity;
    List<String> nameLists;
    List<String> centLists;
    LayoutInflater inflater;

    public UserInfo_MyCent_DailyTaskAdapter(Activity activity,
	    List<String> nameLists, List<String> centLists) {
	this.activity = activity;
	this.nameLists = nameLists;
	this.centLists = centLists;
	inflater = LayoutInflater.from(activity);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
	return nameLists.size();
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
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	v = inflater.inflate(R.layout.adapter_userinfo_mycent_dailytask, null);
	TextView name = (TextView) v.findViewById(R.id.name_tv);
	TextView cent = (TextView) v.findViewById(R.id.cent_tv);
	TextView cent2 = (TextView) v.findViewById(R.id.cent2_tv);

	name.setText(nameLists.get(p));
	cent.setText(centLists.get(p));
	cent2.setText(centLists.get(p));

	return v;
    }
}
