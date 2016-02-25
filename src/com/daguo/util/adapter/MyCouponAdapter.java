package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.util.beans.CouponEntity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-15 上午9:08:16
 * @function 功能: 我的优惠劵适配器
 */
public class MyCouponAdapter extends BaseAdapter {

	Activity activity;
	List<CouponEntity> lists;
	FinalBitmap finalBitmap;
	LayoutInflater inflater;

	public MyCouponAdapter(Activity activity, List<CouponEntity> lists) {
		this.activity = activity;
		this.lists = lists;
		inflater = LayoutInflater.from(activity);
		finalBitmap = FinalBitmap.create(activity);
	}

	@Override
	public int getCount() {
		return lists == null ? 0 : lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int p, View v, ViewGroup arg2) {
		VH vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.adapter_mycoupon, null);
			vh = getVH(vh, v);
			v.setTag(vh);

		} else {
			vh = (VH) v.getTag();
		}

		bindData(vh, p);
		return v;
	}

	/**
	 * 實例化控件
	 * 
	 * @param vh
	 * @param v
	 * @return
	 */
	private VH getVH(VH vh, View v) {
		vh = new VH();
		vh.img_iv = (ImageView) v.findViewById(R.id.img_iv);
		vh.title1_tv = (TextView) v.findViewById(R.id.title1_tv);
		vh.title2_tv = (TextView) v.findViewById(R.id.title2_tv);
		vh.status_btn = (Button) v.findViewById(R.id.status_btn);

		return vh;

	}

	private void bindData(VH vh, int p) {
		vh.title1_tv.setText(lists.get(p).getTitle());
		vh.title2_tv.setText(lists.get(p).getTitle2());
		finalBitmap.display(
				vh.img_iv,
				HttpUtil.IMG_URL
						+ PublicTools
								.doWithNullData(lists.get(p).getImg_path()));
		String sta = PublicTools.doWithNullData(lists.get(p).getStatus());
		if ("0".equals(sta)) {
			// 未使用
			vh.status_btn.setText("使用");
		} else if ("1".equals(sta)) {
			// 已使用
			vh.status_btn.setText("已使用");

		} else if ("2".equals(sta)) {

		}

	}

	private class VH {
		ImageView img_iv;
		TextView title1_tv;
		TextView title2_tv;
		Button status_btn;

	}

}
