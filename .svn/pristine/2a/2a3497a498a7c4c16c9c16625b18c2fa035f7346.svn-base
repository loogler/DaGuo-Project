package com.daguo.util.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.News;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewsAdapter extends BaseAdapter {
	private Context context;
	private List<News> lists = new ArrayList<News>();
	News news;
	private LayoutInflater inflater;

	public NewsAdapter(Context context, List<News> lists) {
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		news = lists.get(position);
		convertView = inflater.inflate(R.layout.item_newsadapter, null);
		TextView tv1 = (TextView) convertView.findViewById(R.id.title1_tv);
		TextView tv2 = (TextView) convertView.findViewById(R.id.title2_tv);
		ImageView iv = (ImageView) convertView.findViewById(R.id.img_iv);
		TextView feedback_tv=(TextView) convertView.findViewById(R.id.feedback_tv);
		tv1.setText(lists.get(position).getTitle());
		tv2.setText(lists.get(position).getTitle2());
		feedback_tv.setText(lists.get(position).getFeedbackCount()+"跟帖");
		ImageLoader.getInstance().displayImage(
				HttpUtil.IMG_URL + lists.get(position).getImg_path(), iv);
		return convertView;
	}

}
