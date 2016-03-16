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
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.NewTask;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-22 下午5:04:34
 * @function ： 个人中心 --新手任务 适配器
 */
public class UserInfo_MyCent_NewTaskAdapter extends BaseAdapter {

	Activity activity;
	List<NewTask> newTasks;
	LayoutInflater inflater;
	FinalBitmap finalBitmap;

	public UserInfo_MyCent_NewTaskAdapter(Activity activity,
			List<NewTask> newTasks) {
		this.activity = activity;
		this.newTasks = newTasks;
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
		return newTasks.size();
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

		VH vh = null;
		if (v == null) {
			v = inflater
					.inflate(R.layout.adapter_userinfo_mycent_newtask, null);
			vh = getVH(vh, v);
			v.setTag(vh);

		} else {

			vh = (VH) v.getTag();
		}
		bindView(vh, p);

		return v;
	}

	private VH getVH(VH vh, View v) {
		vh = new VH();

		vh.cent = (TextView) v.findViewById(R.id.cent_tv);
		vh.name = (TextView) v.findViewById(R.id.name_tv);
		vh.state = (TextView) v.findViewById(R.id.state_tv);

		return vh;
	}

	private void bindView(final VH vh, int p) {
		vh.cent.setText("+ " + newTasks.get(p).getGrade());
		vh.name.setText(newTasks.get(p).getName());
		if ("0".equals(newTasks.get(p).getState())) {
			// 未完成
			vh.state.setText("现在就去");
			vh.state.setBackgroundColor(activity.getResources().getColor(
					R.color.orange));
		} else if ("1".equals(newTasks.get(p).getState())) {
			// 已完成
			vh.state.setText("已完成");
			vh.state.setBackgroundColor(activity.getResources().getColor(
					R.color.grey_1));
		} else {
			// 数据异常
		}
		vh.state.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vh.state.setText("已完成");
				vh.state.setBackgroundColor(activity.getResources().getColor(
						R.color.grey_1));

			}
		});
	}

	private class VH {
		TextView name;
		TextView cent;
		TextView state;
	}

}
