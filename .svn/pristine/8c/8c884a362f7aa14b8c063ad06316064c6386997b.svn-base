/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-27 下午10:43:11
 * @function ：
 */
public class UserInfo_MyOrderAdapter extends BaseAdapter {

    Activity activity;
    List<Shop_GoodsItem> lists;
    LayoutInflater inflater;
    FinalBitmap finalBitmap;

    public UserInfo_MyOrderAdapter(Activity activity, List<Shop_GoodsItem> lists) {
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
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	VH vh = null;
	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_userinfo_myorder, null);
	    vh = getVh(vh, v);
	    v.setTag(vh);
	} else {
	    vh = (VH) v.getTag();
	}
	bindData(vh, p);
	return v;
    }

    /**
     * @return
     */
    private VH getVh(VH vh, View v) {
	vh = new VH();
	vh.goodsCount_tv = (TextView) v.findViewById(R.id.goodsCount_tv);
	vh.goodsName_tv = (TextView) v.findViewById(R.id.goodsName_tv);
	vh.goodsPic_iv = (ImageView) v.findViewById(R.id.goodsPic_iv);
	vh.goodsPrice_tv = (TextView) v.findViewById(R.id.goodsPrice_tv);
	vh.goodsState_tv = (TextView) v.findViewById(R.id.goodsState_tv);
	vh.goosdsDesc_tv = (TextView) v.findViewById(R.id.goosdsDesc_tv);
	return vh;
    }

    private void bindData(VH vh, int p) {
	vh.goodsCount_tv.setText(PublicTools.doWithNullData("* "
		+ lists.get(p).getNumber()));
	vh.goodsName_tv.setText(PublicTools.doWithNullData(lists.get(p)
		.getName()));
	vh.goodsPrice_tv.setText(PublicTools.doWithNullData("￥"+lists.get(p)
		.getPrice()));
	// vh.goodsState_tv.setText(PublicTools.doWithNullData("* "
	// + lists.get(p).));
	if ("0".equals(PublicTools.doWithNullData(lists.get(p).getPay_status()))) {
	    // 未付款
	    vh.goodsState_tv.setText("去付款");
	} else if ("1".equals(PublicTools.doWithNullData(lists.get(p)
		.getPay_status()))) {
	    // 已付款
	    vh.goodsState_tv.setText("已付款");
	}

	vh.goosdsDesc_tv.setText(PublicTools.doWithNullData("共"
		+ lists.get(p).getNumber())
		+ "件商品， 共计 "
		+ (Double.parseDouble(lists.get(p).getPrice()) * Integer
			.parseInt(lists.get(p).getNumber())) + "元");

	if (!"".equals(PublicTools.doWithNullData(lists.get(p).getThumb_path()))) {
	    finalBitmap.display(vh.goodsPic_iv, HttpUtil.IMG_URL
		    + lists.get(p).getThumb_path());
	}

    }

    private class VH {
	ImageView goodsPic_iv;
	TextView goodsName_tv;
	TextView goodsPrice_tv;
	TextView goodsCount_tv;
	TextView goosdsDesc_tv;
	TextView goodsState_tv;
    }
}
