package com.daguo.modem.photo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-11 上午11:58:37
 * @function ： 构造器 type ==1 小头像 适用于显示用户头像 type ==2 适用于说说图片
 */
public class MyGridAdapter extends BaseAdapter {
    private String[] files;
    Context context;
    int type;

    private LayoutInflater mLayoutInflater;

    /**
     * 
     * @param files
     *            String[] url路径
     * @param context
     *            context
     * @param type
     *            构造器 type ==1 小头像 适用于显示用户头像 type ==2 适用于说说图片
     */
    public MyGridAdapter(String[] files, Context context, int type) {
	this.files = files;
	this.context = context;
	this.type = type;
	mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
	return files == null ? 0 : files.length;
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

	if (type == 1) {
	    MyGridViewHolder viewHolder;
	    if (convertView == null) {
		viewHolder = new MyGridViewHolder();
		convertView = mLayoutInflater.inflate(R.layout.gridview_item,
			parent, false);
		viewHolder.imageView = (ImageView) convertView
			.findViewById(R.id.album_image);
		LayoutParams params = new LayoutParams(50, 50);
		viewHolder.imageView.setLayoutParams(params);
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

	} else if (type == 2) {
	    MyGridViewHolder viewHolder;
	    if (convertView == null) {
		viewHolder = new MyGridViewHolder();
		convertView = mLayoutInflater.inflate(R.layout.gridview_item,
			parent, false);
		viewHolder.imageView = (ImageView) convertView
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

	}
	return convertView;
    }

    private static class MyGridViewHolder {
	ImageView imageView;
    }
}
