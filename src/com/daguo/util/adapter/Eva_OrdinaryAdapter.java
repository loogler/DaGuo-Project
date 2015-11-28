package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.utils.HttpUtil;

public class Eva_OrdinaryAdapter extends BaseAdapter {
	private Context context;
	private List<Evaluate_Ordinary> lists;
	LayoutInflater inflater;
	

	public Eva_OrdinaryAdapter(Context context, List<Evaluate_Ordinary> lists) {
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {

		view = inflater.inflate(R.layout.item_sc_shuoshuo_evaadapter, null);
		ImageView headView = (ImageView) view.findViewById(R.id.head);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView content = (TextView) view.findViewById(R.id.content);
		FinalBitmap.create(context)
				.display(headView, HttpUtil.IMG_URL + lists.get(position).getHead_info());
		name.setText(lists.get(position).getP_name());
		content.setText(lists.get(position).getContent());

		return view;
	}

}
