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
		news = lists.get(position);
		convertView = inflater.inflate(R.layout.item_newsadapter, null);
		TextView tv1 = (TextView) convertView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) convertView.findViewById(R.id.tv2);
		ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
		tv1.setText(news.getTitle());
		tv2.setText(news.getTitle2());
		ImageLoader.getInstance().displayImage(
				HttpUtil.IMG_URL + news.getImg_path(), iv);
		return convertView;
	}

}
