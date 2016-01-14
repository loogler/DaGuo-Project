/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.AppDownload;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 下午1:46:44
 * @function ：
 */
public class APPDownloadAdapter extends BaseAdapter {

    Activity activity;
    List<AppDownload> lists;
    LayoutInflater inflater;
    FinalBitmap finalBitmap;

    public APPDownloadAdapter(Activity activity, List<AppDownload> lists) {
	this.activity = activity;
	this.lists = lists;
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
	    v = inflater.inflate(R.layout.adapter_appdownload, null);

	    vh = getVH(vh, v);
	    v.setTag(vh);
	} else {
	    vh = (VH) v.getTag();
	}
	bindData(vh, p);

	return v;
    }

    private VH getVH(VH vh, View view) {
	vh = new VH();
	vh.download_iv = (ImageView) view.findViewById(R.id.download_iv);
	vh.name_tv = (TextView) view.findViewById(R.id.name_tv);
	vh.pic_iv = (ImageView) view.findViewById(R.id.pic_iv);
	vh.size_tv = (TextView) view.findViewById(R.id.size_tv);

	return vh;
    }

    private void bindData(VH vh, final int p) {
	finalBitmap.display(vh.pic_iv, HttpUtil.IMG_URL
		+ lists.get(p).getImg_path());
	vh.name_tv.setText(PublicTools.doWithNullData(lists.get(p).getName()));
	vh.size_tv.setText(PublicTools.doWithNullData(lists.get(p).getSize())
		+ " MB");
	vh.download_iv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		String url = "http://115.29.224.248:8080/uploadFiles/"
			+ lists.get(p).getDownload_path();

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		activity.startActivity(intent);
	    }
	});

    }

    private class VH {

	ImageView pic_iv;
	ImageView download_iv;
	TextView size_tv;
	TextView name_tv;
    }

}
