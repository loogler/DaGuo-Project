/**
 *2016-2-14上午11:09:48
 * * 
 * Copyright (c) 2015-2025 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.util.beans.CouponEntity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.wifi.WifiConfiguration.Status;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-14 上午11:09:48
 * @function 功能:优惠卷列表（所有默认优惠卷）适配
 */

@SuppressLint("InflateParams")
public class CouponAdapter extends BaseAdapter {

	Activity activity;
	List<CouponEntity> lists;
	FinalBitmap finalBitmap;
	LayoutInflater inflater;

	public CouponAdapter(Activity activity, List<CouponEntity> lists) {

		this.activity = activity;
		this.lists = lists;
		finalBitmap = FinalBitmap.create(activity);
		inflater = LayoutInflater.from(activity);
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

	@Override
	public View getView(int p, View v, ViewGroup arg2) {
		VH vh = null;
		if (v == null) {
			v = inflater.inflate(R.layout.adapter_coupon, null);
			vh = getVH(vh, v);
			v.setTag(vh);
		} else {
			vh = (VH) v.getTag();
		}
		bindViews(vh, p);

		return v;
	}

	/*------------------------------*/

	private VH getVH(VH vh, View v) {
		vh = new VH();
		vh.img_iv = (ImageView) v.findViewById(R.id.img_iv);
		vh.name_tv = (TextView) v.findViewById(R.id.name_tv);
		vh.status_tv = (TextView) v.findViewById(R.id.status_tv);

		return vh;
	}

	private void bindViews(VH vh, int p) {
		vh.name_tv.setText(PublicTools.doWithNullData(lists.get(p).getTitle()));
		finalBitmap.display(
				vh.img_iv,
				HttpUtil.IMG_URL
						+ PublicTools
								.doWithNullData(lists.get(p).getImg_path()));
		String sta = PublicTools.doWithNullData(lists.get(p).getStatus());
		if (!sta.isEmpty()) {
			if ("0".equals(sta)) {
				// 状态为0 没有领取
				vh.status_tv.setText("去领取");
			} else if ("1".equals(sta)) {
				// 状态为1 已经领取
				vh.status_tv.setText("已领取");
			} else if ("2".equals(sta)) {
				// 状态为2 已经使用
				vh.status_tv.setText("已使用");
			}

		} else {
			// Status 异常
		}
	}

	private class VH {
		ImageView img_iv;
		TextView name_tv;
		TextView status_tv;

	}

}
