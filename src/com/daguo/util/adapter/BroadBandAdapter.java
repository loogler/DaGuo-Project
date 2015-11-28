package com.daguo.util.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.BroadBand;

public class BroadBandAdapter extends BaseAdapter {

	private List<BroadBand> infos;
	private Context context;
	private LayoutInflater inflater;

	public BroadBandAdapter(Context context, List<BroadBand> infos) {
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

		view = inflater.inflate(R.layout.item_broadband_list, null);
		TextView busi_name = (TextView) view.findViewById(R.id.busi_name);
		TextView month = (TextView) view.findViewById(R.id.month);
		TextView price = (TextView) view.findViewById(R.id.price);
		TextView costinfo = (TextView) view.findViewById(R.id.costinfo);
		TextView detailname = (TextView) view.findViewById(R.id.detailname);
		TextView sName = (TextView) view.findViewById(R.id.sname);
		busi_name.setText(infos.get(position).getBusiName());
		month.setText(infos.get(position).getMonth());
		price.setText(infos.get(position).getPrice());
		costinfo.setText(infos.get(position).getCostInfo());
		detailname.setText(infos.get(position).getDetailName());
		sName.setText(infos.get(position).getsName());
		return view;
	}

}