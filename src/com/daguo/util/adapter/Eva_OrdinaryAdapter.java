package com.daguo.util.adapter;

import java.text.ParseException;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.SC_Event;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

public class Eva_OrdinaryAdapter extends BaseAdapter {
    private Context context;
    private List<Evaluate_Ordinary> lists;
   
    LayoutInflater inflater;

    public Eva_OrdinaryAdapter(Context context, List<Evaluate_Ordinary> lists)
	   {
	this.context = context;
	this.lists = lists;
	
	inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
	return lists.size() ;
    }

    @Override
    public Object getItem(int arg0) {
	return lists.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
	return arg0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {


	    view = inflater.inflate(R.layout.adapter_eva_ordinary, null);

	    CircularImage head_circularimage = (CircularImage) view
		    .findViewById(R.id.head_circularimage);
	    TextView floor_tv = (TextView) view.findViewById(R.id.floor_tv);
	    TextView name_tv = (TextView) view.findViewById(R.id.name_tv);
	    TextView time_tv = (TextView) view.findViewById(R.id.time_tv);
	    ImageView sex_iv = (ImageView) view.findViewById(R.id.sex_iv);
	    TextView year_department_tv = (TextView) view
		    .findViewById(R.id.year_department_tv);
	    TextView content_tv = (TextView) view.findViewById(R.id.content_tv);

	    FinalBitmap.create(context).display(head_circularimage,
		    HttpUtil.IMG_URL + lists.get(position).getHead_info());
	    floor_tv.setText(position + " 楼");
	    name_tv.setText(lists.get(position).getP_name());
	    try {
		time_tv.setText(PublicTools.DateFormat(lists.get(position)
			.getCreate_time()));
	    } catch (ParseException e) {
		Log.e("社交评论", "时间差计算的格式出现问题");
		e.printStackTrace();
	    }
	    if (lists.get(position).getSex().equals("0")) {
		// nan
		sex_iv.setImageResource(R.drawable.icon_sex_man);
	    } else if (lists.get(position).getSex().equals("1")) {
		// nv
		sex_iv.setImageResource(R.drawable.icon_sex_woman);
	    }

	    year_department_tv.setText(lists.get(position).getStart_year()
		    + "级 " + lists.get(position).getPro_name());
	    content_tv.setText(lists.get(position).getContent());

	    return view;
	

//	ImageView headView = (ImageView) view.findViewById(R.id.head);
//	TextView name = (TextView) view.findViewById(R.id.name);
//	TextView content = (TextView) view.findViewById(R.id.content);
//	FinalBitmap.create(context).display(headView,
//		HttpUtil.IMG_URL + lists.get(position).getHead_info());
//	name.setText(lists.get(position).getP_name());
//	content.setText(lists.get(position).getContent());
//
//	return view;

    }
}
