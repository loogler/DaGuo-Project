package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.school.huodong.SC_HuoDong_DetailAty;
import com.daguo.util.beans.SC_HuoDong;
import com.daguo.utils.HttpUtil;

/**
 * 活动适配器
 * 
 * @author Bugs_Rabbit 時間： 2015-10-10 下午3:40:07
 */
public class SC_HuoDongAdapter extends BaseAdapter {
	Context context;
	List<SC_HuoDong> lists;
	SC_HuoDong list;
	LayoutInflater inflater;

	public SC_HuoDongAdapter(Context context, List<SC_HuoDong> lists) {
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
	public View getView(final int p, View v, ViewGroup v2) {
		v = inflater.inflate(R.layout.adapter_sc_huodong, null);
		TextView type_name = (TextView) v.findViewById(R.id.type_name);
		TextView title1 = (TextView) v.findViewById(R.id.title1);
		TextView start_date = (TextView) v.findViewById(R.id.start_date);
		TextView end_date = (TextView) v.findViewById(R.id.end_date);
		TextView type_send = (TextView) v.findViewById(R.id.type_send);
		TextView content = (TextView) v.findViewById(R.id.content);
		ImageView img_content = (ImageView) v.findViewById(R.id.img_content);

		list = lists.get(p);
		type_name.setText(list.getTag());
		title1.setText(list.getTitle());
		start_date.setText(list.getS_date() + " " + list.getS_time());
		end_date.setText(list.getE_date() + " " + list.getE_time());
		type_send.setText("在线提交作品");
		content.setText(list.getContent());
		FinalBitmap.create(context).display(img_content,
				HttpUtil.IMG_URL + list.getImg_path());
		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// Toast.makeText(context, "position" + p, 2000).show();
				Intent intent = new Intent(context, SC_HuoDong_DetailAty.class);
				intent.putExtra("type_name", list.getTag());
				intent.putExtra("title1", list.getTitle());
				intent.putExtra("start_date",
						list.getS_date() + " " + list.getS_time());
				intent.putExtra("end_date",
						list.getE_date() + " " + list.getE_time());
				intent.putExtra("type_send", "在线提交作品");
				intent.putExtra("content", list.getContent());
				intent.putExtra("img_content",
						HttpUtil.IMG_URL + list.getImg_path());
				context.startActivity(intent);

			}
		});
		return v;
	}
}
