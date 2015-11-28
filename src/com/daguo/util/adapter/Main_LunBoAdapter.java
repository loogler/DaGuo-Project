package com.daguo.util.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.util.beans.Main_1Lunbo;

public class Main_LunBoAdapter extends BaseAdapter {
	private Context context;
	private List<Main_1Lunbo> list;
	LayoutInflater inflater;

	public Main_LunBoAdapter(Context context, List<Main_1Lunbo> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup group) {
		view = inflater.inflate(R.layout.item_main1adapter_lunbo, null);
		ImageView ivImageView = (ImageView) view.findViewById(R.id.iv_img);
		// TODO 添加方法显示图片
		return view;
	}

}
