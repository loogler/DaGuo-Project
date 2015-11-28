package com.daguo.util.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.TelNumber;

public class TelNumberAdapter extends BaseAdapter {
	private List<TelNumber> infos;
	private Context context;
	private LayoutInflater inflater;

	public TelNumberAdapter(Context context, List<TelNumber> infos) {
		this.context = context;
		this.infos = infos;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup group) {

		view = inflater.inflate(R.layout.item_number_list, null);
		TextView tv_taocan = (TextView) view.findViewById(R.id.tv_taocan);
		TextView tv_number = (TextView) view.findViewById(R.id.tv_number);
		TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
		if (infos!=null) {
			
			tv_taocan.setText(infos.get(position).getCost_name());
			tv_detail.setText(infos.get(position).getGz());
			tv_number.setText(infos.get(position).getTel());
		}

		return view;
	}

}
