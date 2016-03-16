/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.GoodTypeItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-15 下午1:45:32
 * @function ： 商城分类栏目的适配 （中间的grid ）
 */
public class Main_2_GoodTypeAdapter extends BaseAdapter {
	Activity activity;
	List<GoodTypeItem> lists;
	LayoutInflater inflater;
	FinalBitmap finalBitmap;

	public Main_2_GoodTypeAdapter(Activity activity, List<GoodTypeItem> lists) {
		this.activity = activity;
		this.lists = lists;
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
		return lists == null ? 0 : lists.size();
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
	@SuppressLint({ "ViewHolder", "InflateParams" })
	@Override
	public View getView(final int position, View view, ViewGroup arg2) {

		view = inflater.inflate(R.layout.adapter_main_2_goodtype, null);
		TextView type_tv = (TextView) view.findViewById(R.id.type_tv);
		ImageView icon_iv = (ImageView) view.findViewById(R.id.icon_iv);

		if (PublicTools.doWithNullData(lists.get(position).getType_name())
				.isEmpty()) {

		} else {
			if (position != 9) {
				type_tv.setText(PublicTools.doWithNullData(lists.get(position)
						.getType_name()));
			} else {
				type_tv.setText("更多分类");
			}
		}

		if (PublicTools.doWithNullData(lists.get(position).getImg_path())
				.isEmpty()) {

		} else {
			if ("1".equals(PublicTools.doWithNullData(lists.get(position)
					.getImg_path()))) {
				icon_iv.setImageResource(R.drawable.icon_shop_more);

			} else {

				finalBitmap.display(
						icon_iv,
						HttpUtil.IMG_URL
								+ PublicTools.doWithNullData(lists
										.get(position).getImg_path()));
			}
		}

		return view;
	}

	// -------------------------------------------------

}
