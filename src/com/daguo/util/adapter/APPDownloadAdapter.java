/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.AppDownload;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 下午1:46:44
 * @function ：app下载列表适配
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

	@Override
	public int getCount() {
		return lists == null ? 0 : lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

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

	/**
	 * 
	 * @param vh
	 * @param v
	 * @return
	 */
	private VH getVH(VH vh, View v) {
		vh = new VH();
		vh.content_grid = (GridView) v.findViewById(R.id.content_grid);
		vh.type_tv = (TextView) v.findViewById(R.id.type_tv);

		return vh;
	}

	/**
	 * 
	 * @param vh
	 * @param p
	 */
	private void bindData(VH vh, int p) {
		vh.content_grid.setAdapter(new APPDownload_GridAdapter(activity, lists
				.get(p).getLists()));
		vh.type_tv.setText(lists.get(p).getType());
	}

	private class VH {
		TextView type_tv;
		GridView content_grid;
	}
}
