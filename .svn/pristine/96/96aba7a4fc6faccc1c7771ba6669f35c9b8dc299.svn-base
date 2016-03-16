/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.ui.commercial.cent.CentAty;
import com.daguo.ui.commercial.cent.Cent_DetailAty;
import com.daguo.util.beans.CentGoods;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-3 下午12:17:32
 * @function ： 积分商城的商品列表适配
 */
public class CentAdapter extends BaseAdapter {

    Activity activity;
    List<CentGoods> lists;
    LayoutInflater inflater;
    FinalBitmap bmp = FinalBitmap.create(activity);

    public CentAdapter(Activity activity, List<CentGoods> lists) {
	this.activity = activity;
	this.lists = lists;
	inflater = LayoutInflater.from(activity);

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
	return lists.get(arg0);
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
    @SuppressLint("InflateParams")
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	ViewHolder holder;

	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_cent, null);
	    holder = getHolder(v);
	    v.setTag(holder);

	} else {
	    holder = (ViewHolder) v.getTag();
	}
	bindData(holder, p);

	return v;
    }

    private void bindData(ViewHolder holder, final int position) {
	holder.exchange_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		Intent intent = new Intent(activity, Cent_DetailAty.class);
		intent.putExtra("id", lists.get(position).getId());
		activity.startActivity(intent);
	    }
	});
	holder.goodsCent_tv.setText(PublicTools.doWithNullData(lists.get(
		position).getScore())
		+ " 积分");
	holder.goodsName_tv.setText(PublicTools.doWithNullData(lists.get(
		position).getName()));

	bmp.display(
		holder.goodsPic_iv,
		HttpUtil.IMG_URL
			+ PublicTools.doWithNullData(lists.get(position)
				.getThumb_path()));

    }

    private ViewHolder getHolder(View view) {
	ViewHolder holder = new ViewHolder();
	holder.exchange_tv = (TextView) view.findViewById(R.id.exchange_tv);
	holder.goodsCent_tv = (TextView) view.findViewById(R.id.goodsCent_tv);
	holder.goodsName_tv = (TextView) view.findViewById(R.id.goodsName_tv);
	holder.goodsPic_iv = (ImageView) view.findViewById(R.id.goodsPic_iv);

	return holder;
    }

    private class ViewHolder {
	ImageView goodsPic_iv;
	TextView goodsName_tv;
	TextView goodsCent_tv;
	TextView exchange_tv;

    }
}
