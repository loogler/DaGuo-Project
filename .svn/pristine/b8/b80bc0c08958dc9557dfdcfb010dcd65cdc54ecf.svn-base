package com.daguo.util.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daguo.R;

public class NmAdapter extends BaseAdapter {
	private Context context;
	private List<String> data;
	LayoutInflater inflater;

	public NmAdapter(Context context, List<String> data) {
		this.context = context;
		this.data = data;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		view = inflater.inflate(R.layout.item_taocanxuanze, null);
		TextView tv = (TextView) view.findViewById(R.id.taocan);
		

		tv.setText(data.get(position));
		if (position == selectItem) {
			view.setBackgroundResource(R.drawable.yuanjiao);
//			tv.setBackgroundResource(R.drawable.yuanjiao);
		}else {
			view.setBackgroundResource(R.drawable.btn_yuanjiao);
//			tv.setBackgroundResource(R.drawable.btn_yuanjiao);
		}
		return view;
	}
	int selectItem;
	public void setSelectItem(int selectItem){
		this.selectItem=selectItem;
	}

}
