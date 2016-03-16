/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.commercial.Shop_GoodsDetailAty;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-14 下午1:52:29
 * @function ： 商品优惠推荐列表适配 位于主页第二页
 */
@SuppressLint("InflateParams")
public class Main_2Adapter extends BaseAdapter {

    Activity activity;
    List<Shop_GoodsItem> goodsItemLists;
    LayoutInflater inflater;
    FinalBitmap finalBitmap;

    public Main_2Adapter() {

    }

    public Main_2Adapter(Activity activity, List<Shop_GoodsItem> goodsItems) {
	this.activity = activity;
	this.goodsItemLists = goodsItems;
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
	return goodsItemLists == null ? 0 : goodsItemLists.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int arg0) {
	// return goodsItemLists == null ? null : goodsItemLists.get(arg0);
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
    public View getView(final int p, View v, ViewGroup arg2) {
	ViewHolder viewHolder = null;
	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_main_2, null);
	    viewHolder = getHolder(v);
	    v.setTag(viewHolder);

	} else {
	    viewHolder = (ViewHolder) v.getTag();
	}

	if (goodsItemLists != null) {
	    bindData(p, viewHolder);
	}

	v.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		Intent intent = new Intent(activity, Shop_GoodsDetailAty.class);
		intent.putExtra("id", goodsItemLists.get(p).getId());
		activity.startActivity(intent);

	    }
	});
	return v;
    }

    /**
     * 
     */
    private void bindData(int position, ViewHolder viewHolder) {
	if (goodsItemLists.get(position) == null) {
	    return;
	}

	if (!PublicTools.doWithNullData(
		goodsItemLists.get(position).getThumb_path()).isEmpty()) {
	    finalBitmap.display(
		    viewHolder.photo_iv,
		    HttpUtil.IMG_URL
			    + PublicTools.doWithNullData(goodsItemLists.get(
				    position).getThumb_path()));
	} else {
	    viewHolder.photo_iv
		    .setImageResource(R.drawable.plugin_camera_no_pictures);

	}

	if (!PublicTools.doWithNullData(goodsItemLists.get(position).getName())
		.isEmpty()) {
	    viewHolder.name_tv.setText(goodsItemLists.get(position).getName());

	} else {
	    viewHolder.name_tv.setText("商品");

	}

	if (!PublicTools
		.doWithNullData(goodsItemLists.get(position).getPrice())
		.isEmpty()) {
	    viewHolder.price_tv.setText("￥"
		    + goodsItemLists.get(position).getPrice());
	} else {
	    viewHolder.price_tv.setText("￥ --");
	}

    }

    private ViewHolder getHolder(View view) {
	ViewHolder viewHolder = new ViewHolder();

	viewHolder.photo_iv = (ImageView) view.findViewById(R.id.photo_iv);
	viewHolder.name_tv = (TextView) view.findViewById(R.id.name_tv);
	viewHolder.price_tv = (TextView) view.findViewById(R.id.price_tv);

	return viewHolder;

    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-12-14 下午2:28:03
     * @function ：
     */
    private class ViewHolder {
	ImageView photo_iv;
	TextView name_tv;
	TextView price_tv;

    }

}
