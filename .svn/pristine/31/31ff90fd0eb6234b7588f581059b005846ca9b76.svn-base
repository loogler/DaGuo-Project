/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;

import android.R.integer;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-28 下午10:06:59
 * @function ：
 */
public class Shop_CartAdapter extends BaseAdapter {

    Activity activity;
    List<Shop_GoodsItem> lists;
    FinalBitmap fbm = FinalBitmap.create(activity);
    CheckChange checkChange;

    public Shop_CartAdapter(Activity activity, List<Shop_GoodsItem> lists) {
	this.activity = activity;
	this.lists = lists;

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
	return lists == null ? arg0 : lists.get(arg0);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int arg0) {
	return arg0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	ViewHolder holder = null;

	if (v == null) {
	    v = LayoutInflater.from(activity).inflate(
		    R.layout.adapter_shop_cart, null);
	    holder = getHolder(v);
	    v.setTag(holder);

	} else {
	    holder = (ViewHolder) v.getTag();

	}

	if (lists != null) {
	    bindData(holder, p);
	} else {
	    // no data
	}

	return v;
    }

    /**
     * 
     */
    private void bindData(ViewHolder holder, final int p) {
	holder.goodsName_tv.setText(lists.get(p).getName());
	fbm.display(holder.goodsPic_iv, HttpUtil.IMG_URL
		+ lists.get(p).getThumb_path());
	holder.goodsPrice_tv.setText(lists.get(p).getPrice());
	holder.cart_ckb
		.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

		    @Override
		    public void onCheckedChanged(CompoundButton arg0,
			    boolean arg1) {
			checkChange.getChoiceData(p, arg1);
		    }
		});
    }

    private ViewHolder getHolder(View view) {
	ViewHolder holder = new ViewHolder();
	holder.cart_ckb = (CheckBox) view.findViewById(R.id.cart_ckb);
	holder.goodsPic_iv = (ImageView) view.findViewById(R.id.goodsPic_iv);
	holder.goodsName_tv = (TextView) view.findViewById(R.id.goodsName_tv);
	holder.goodsPrice_tv = (TextView) view.findViewById(R.id.goodsPrice_tv);

	return holder;

    }

    private class ViewHolder {
	CheckBox cart_ckb;
	ImageView goodsPic_iv;
	TextView goodsName_tv;
	TextView goodsPrice_tv;

    }

    public interface CheckChange {
	public void getChoiceData(int position, boolean isChoice);
    }

    public void setCheckChangeListener(CheckChange checkChange) {
	this.checkChange = checkChange;
    }

}
