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
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.UserInfo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-18 下午2:13:00
 * @function ：新报道同学适配器
 */
public class SC_ShuoShuo_NewStudentListAdapter extends BaseAdapter {

    Activity activity;
    List<UserInfo> lists;
    FinalBitmap finalBitmap;
    LayoutInflater inflater;

    public SC_ShuoShuo_NewStudentListAdapter(Activity activity,
	    List<UserInfo> lists) {
	this.activity = activity;
	this.lists = lists;
	finalBitmap = FinalBitmap.create(activity);
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
    @SuppressLint("InflateParams")
    @Override
    public View getView(int p, View v, ViewGroup arg2) {
	VH vh = null;
	if (v == null) {
	    v = inflater.inflate(R.layout.adapter_sc_shuoshuo_newstudentlist,
		    null);
	    vh = getVH(vh, v);
	    v.setTag(vh);
	} else {
	    vh = (VH) v.getTag();
	}
	bindData(vh, p);
	return v;
    }

    /**
     * 绑定vh
     * 
     * @param vh
     * @param v
     * @return
     */
    private VH getVH(VH vh, View v) {
	vh = new VH();
	vh.name_tv = (TextView) v.findViewById(R.id.name_tv);
	vh.photo = (CircularImage) v.findViewById(R.id.photo);
	vh.schoolName_tv = (TextView) v.findViewById(R.id.schoolName_tv);
	vh.sex_iv = (ImageView) v.findViewById(R.id.sex_iv);

	return vh;

    }

    /**
     * 给view写值
     * 
     * @param vh
     * @param p
     */
    private void bindData(VH vh, int p) {
	vh.name_tv.setText(PublicTools.doWithNullData(lists.get(p).getName()));
	finalBitmap.display(
		vh.photo,
		HttpUtil.IMG_URL
			+ PublicTools.doWithNullData(lists.get(p)
				.getHead_info()));
	vh.schoolName_tv.setText(PublicTools.doWithNullData(lists.get(p)
		.getSchool_name()));
	String sex = PublicTools.doWithNullData(lists.get(p).getSex());
	if ("0".equals(sex)) {
	    // man
	    vh.sex_iv.setImageResource(R.drawable.icon_sex_man);
	} else if ("1".equals(sex)) {
	    // woman
	    vh.sex_iv.setImageResource(R.drawable.icon_sex_woman);

	} else {

	}

    }

    private class VH {
	CircularImage photo;
	TextView name_tv;
	ImageView sex_iv;
	TextView schoolName_tv;

    }

}
