package com.daguo.util.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.Type;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.LoadImgUtil;

/**
 * 具体商品 展示的 适配器
 * 
 * @author Bugs_Rabbit 時間： 2015-8-13 上午2:19:45
 */
public class Mall_ItemAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<Type> list;
	private Context context;
	private Type type;

	public Mall_ItemAdapter(Context context, ArrayList<Type> list) {
		mInflater = LayoutInflater.from(context);
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		if (list != null && list.size() > 0)
			return list.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MyView view;
		if (convertView == null) {
			view = new MyView();
			convertView = mInflater.inflate(R.layout.list_pro_type_item, null);
			view.icon = (ImageView) convertView.findViewById(R.id.typeicon);
			view.name = (TextView) convertView.findViewById(R.id.typename);
			view.price = (TextView) convertView.findViewById(R.id.typeprice);

			convertView.setTag(view);
		} else {
			view = (MyView) convertView.getTag();
		}
		if (list != null && list.size() > 0) {
			type = list.get(position);

			LoadImgUtil.loadImage(HttpUtil.IMG_URL + type.getThumb_path(),
					R.id.typeicon, convertView);

			view.name.setText( type.getName());
			view.price.setText("￥：  " + type.getPrice() );

			//
			// view.name.setText(type.getName());

			// view.name.setText(type.getType_name());
			// if (type.getTypename().contains("电脑")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_computer);
			// } else if (type.getTypename().contains("化妆")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_huazhuang);
			// }
			//
			// else if (type.getTypename().contains("女装")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_cloth);
			// }
			//
			// else if (type.getTypename().contains("玩具")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_toys);
			// }
			//
			// else if (type.getTypename().contains("男装")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_nanzhuang);
			// } else if (type.getTypename().contains("内衣")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_neiyi);
			// } else if (type.getTypename().contains("电器")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_dianqi);
			// } else if (type.getTypename().contains("手机")) {
			// view.icon.setBackgroundResource(R.drawable.shop_item_shouji);
			// } else {
			// view.icon.setBackgroundResource(R.drawable.shop_item_cloth);
			// }
		}

		return convertView;
	}

	private class MyView {
		private ImageView icon;
		private TextView name;
		private TextView price;
	}

}
