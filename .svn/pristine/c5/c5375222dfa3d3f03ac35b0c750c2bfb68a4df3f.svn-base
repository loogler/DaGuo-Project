/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.UserInfo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-15 下午4:45:44
 * @function ： 用于显示同学说中 新报道同学的一排列表 适配器
 */
public class SC_ShuoShuo_NewStudentAdapter extends BaseAdapter {

    private List<UserInfo> lists;

    Context context;

    private LayoutInflater mLayoutInflater;

    /**
     * 
     * @param files
     *            String[] url路径
     * @param context
     *            context
     * @param type
     */
    public SC_ShuoShuo_NewStudentAdapter(List<UserInfo> lists, Context context) {

	this.context = context;
	this.lists = lists;

	mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	return lists == null ? 0 : lists.size() > 6 ? 6 : lists.size();
    }

    @Override
    public UserInfo getItem(int position) {
	return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
	return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	MyGridViewHolder viewHolder;
	if (convertView == null) {
	    viewHolder = new MyGridViewHolder();
	    convertView = mLayoutInflater.inflate(
		    R.layout.adapter_sc_shuoshuo_headgrid, parent, false);
	    viewHolder.imageView = (CircularImage) convertView
		    .findViewById(R.id.album_image);
	    viewHolder.textView = (TextView) convertView.findViewById(R.id.tv);

	    convertView.setTag(viewHolder);
	} else {
	    viewHolder = (MyGridViewHolder) convertView.getTag();
	}
	String url = HttpUtil.IMG_URL + lists.get(position).getHead_info();
	/*
	 * ImageLoader.getInstance().init(
	 * ImageLoaderConfiguration.createDefault(context));
	 */
	ImageLoader.getInstance().displayImage(url, viewHolder.imageView);
	viewHolder.textView.setText(PublicTools.doWithNullData(lists.get(
		position).getName()));

	return convertView;
    }

    private static class MyGridViewHolder {
	CircularImage imageView;
	TextView textView;

    }

}
