/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.text.ParseException;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.Message_Inform;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-13 上午1:50:42
 * @function ：
 */
public class Message_Tab2Adapter extends BaseAdapter {

    Activity activity;
    List<Message_Inform> lists;
    LayoutInflater inflater;
    FinalBitmap finalBitmap;

    public Message_Tab2Adapter(Activity activity, List<Message_Inform> lists) {
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
    public View getView(int p, View v, ViewGroup arg2) {
	VH holder;

	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_message_tab1, null);
	    holder = getHolder(v);
	    v.setTag(holder);
	} else {
	    holder = (VH) v.getTag();
	}

	bindData(holder, p);

	return v;
    }

    private VH getHolder(View view) {
	VH holder = new VH();

	holder.name_tv = (TextView) view.findViewById(R.id.name_tv);
	holder.photo = (CircularImage) view.findViewById(R.id.photo);
	holder.schoolName_tv = (TextView) view.findViewById(R.id.schoolName_tv);
	holder.sex_iv = (ImageView) view.findViewById(R.id.sex_iv);
	holder.time_tv = (TextView) view.findViewById(R.id.time_tv);
	holder.type_tv = (TextView) view.findViewById(R.id.type_tv);

	return holder;

    }

    private void bindData(VH holder, int position) {
	holder.name_tv.setText(lists.get(position).getS_name());
	finalBitmap.display(holder.photo, HttpUtil.IMG_URL
		+ lists.get(position).getS_head_info());
	holder.schoolName_tv.setText(lists.get(position).getS_pro_name());
	if ("1".equals(lists.get(position).getS_sex())) {

	    holder.sex_iv.setImageResource(R.drawable.icon_sex_woman);
	} else if ("0".equals(lists.get(position).getS_sex())) {

	    holder.sex_iv.setImageResource(R.drawable.icon_sex_man);
	}
	try {
	    holder.time_tv.setText(PublicTools.DateFormat(lists.get(position)
		    .getCreate_time()));
	} catch (ParseException e) {
	    Log.e("时间有问题", "消息通信通知部分时间解析出错");
	    e.printStackTrace();
	}
	holder.type_tv.setText("发来了新消息");

    }

    private class VH {
	CircularImage photo;
	TextView name_tv;
	ImageView sex_iv;
	TextView schoolName_tv;
	TextView time_tv;
	TextView type_tv;
    }

}
