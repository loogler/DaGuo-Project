/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.school.huodong.SC_HuoDong_DetailAty1;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit 
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-8 下午3:14:59
 * @function ： 
 */
public class SC_HuoDongAdapter1 extends BaseAdapter {

    Activity activity;
    List<SC_SheTuan> lists;

    public SC_HuoDongAdapter1(Activity activity, List<SC_SheTuan> lists) {
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
	return lists.size();
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
    @Override
    public View getView(final int p, View v, ViewGroup arg2) {
	View view = LayoutInflater.from(activity).inflate(
		R.layout.adapter_sc_shetuan, null);
	bindViews(view, p);
	view.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		Intent intent = new Intent(activity, SC_HuoDong_DetailAty1.class);
		
		intent.putExtra("id", lists.get(p).getId());
		
		activity.startActivity(intent);

	    }
	});

	return view;
    }

    private void bindViews(View view, int position) {

	TextView typeName_tv = (TextView) view.findViewById(R.id.typeName_tv);
	TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
	TextView content_tv = (TextView) view.findViewById(R.id.content_tv);
	ImageView content_iv = (ImageView) view.findViewById(R.id.content_iv);
	TextView feedbackCount_tv = (TextView) view
		.findViewById(R.id.feedbackCount_tv);
	TextView goodCount_tv = (TextView) view.findViewById(R.id.goodCount_tv);

	typeName_tv.setText(lists.get(position).getTitle());
	title_tv.setText(lists.get(position).getTitle2());

	String content = PublicTools.getContentSummary(lists.get(position)
		.getContent(), 120);
	content_tv.setText(content);

	String img_path = lists.get(position).getImg_path();
	if (img_path != null && !img_path.equals("")
		&& !"null".equals(img_path) && !"[]".equals(img_path)) {

	    ImageLoader.getInstance().displayImage(HttpUtil.IMG_URL + img_path,
		    content_iv);
	}
	goodCount_tv
		.setText("感兴趣(" + lists.get(position).getGood_count() + ")");
	feedbackCount_tv.setText("评论("
		+ lists.get(position).getFeedback_count() + ")");

    }



}

