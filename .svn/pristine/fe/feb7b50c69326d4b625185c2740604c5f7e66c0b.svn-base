/**
 * 互相学习 共同进步
 */
package com.daguo.modem.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-15 下午3:07:32
 * @function ： 专门用于显示用户圆形头像缩率图的适配 
 */
public class SC_ShuoShuo_HeadGridAdapter extends BaseAdapter {

    private String[] files;
    Context context;

    private LayoutInflater mLayoutInflater;

    /**
     * 
     * @param files
     *            String[] url路径
     * @param context
     *            context
     * @param type
     *            
     */
    public SC_ShuoShuo_HeadGridAdapter(String[] files, Context context) {
	this.files = files;
	this.context = context;

	mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	return files == null ? 0 : files.length > 5 ? 5 : files.length;
    }

    @Override
    public String getItem(int position) {
	return files[position];
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
	    convertView = mLayoutInflater.inflate(R.layout.adapter_sc_shuoshuo_headgrid,
		    parent, false);
	    viewHolder.imageView = (CircularImage) convertView
		    .findViewById(R.id.album_image);

	    convertView.setTag(viewHolder);
	} else {
	    viewHolder = (MyGridViewHolder) convertView.getTag();
	}
	String url = HttpUtil.IMG_URL + getItem(position);
	/*
	 * ImageLoader.getInstance().init(
	 * ImageLoaderConfiguration.createDefault(context));
	 */
	ImageLoader.getInstance().displayImage(url, viewHolder.imageView);

	return convertView;
    }

    private static class MyGridViewHolder {
	CircularImage imageView;
    }

}
