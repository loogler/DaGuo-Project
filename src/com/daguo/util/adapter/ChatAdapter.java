/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.daguo.R;
import com.daguo.R.layout;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.ChatMsg;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-7 下午3:26:57
 * @function ：
 */
public class ChatAdapter extends BaseAdapter {

    Activity activity;
    List<ChatMsg> lists;
    LayoutInflater inflater;

    public ChatAdapter(Activity activity, List<ChatMsg> lists) {
	this.lists = lists;
	this.activity = activity;
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
	return arg0;
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
	VH vh = null;
	ChatMsg chatMsg = lists.get(p);
	boolean isSend = chatMsg.isSend();
	if (v == null) {
	    if (isSend) {
		v = inflater.inflate(R.layout.adapter_chat1, null);

	    } else {

		v = inflater.inflate(R.layout.adapter_chat2, null);
	    }
	    vh = bindViews(vh, v);

	    v.setTag(vh);

	} else {
	    vh = (VH) v.getTag();
	}
	setData(vh, p);

	return v;
    }

    private VH bindViews(VH vh, View v) {
	vh = new VH();
	vh.content_tv = (TextView) v.findViewById(R.id.content_tv);
	vh.photo = (CircularImage) v.findViewById(R.id.photo);
	vh.time_tv = (TextView) v.findViewById(R.id.time_tv);

	return vh;
    }

    private void setData(VH vh, int p) {
	vh.content_tv.setText(PublicTools.doWithNullData(lists.get(p)
		.getContent()));
	vh.time_tv.setText(PublicTools.doWithNullData(lists.get(p)
		.getCreate_time()));
	FinalBitmap.create(activity).display(
		vh.photo,
		HttpUtil.IMG_URL
			+ PublicTools.doWithNullData(lists.get(p).getPhoto()));

    }

    private class VH {
	TextView time_tv;
	TextView content_tv;
	CircularImage photo;
    }

}
