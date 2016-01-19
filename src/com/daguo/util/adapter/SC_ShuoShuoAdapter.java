package com.daguo.util.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.photo.MyGridAdapter;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.modem.photo.SC_ShuoShuo_HeadGridAdapter;
import com.daguo.util.base.CircularImage;
import com.daguo.util.base.MyGridView;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

public class SC_ShuoShuoAdapter extends BaseAdapter {
    @SuppressLint("SimpleDateFormat")
    private Context context;
    private List<ShuoShuoContent> infos;
    private ShuoShuoContent info;

    LayoutInflater inflater;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public SC_ShuoShuoAdapter(Context context, List<ShuoShuoContent> infos) {
	this.context = context;
	this.infos = infos;
	inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
	return infos == null ? 0 : infos.size();
    }

    @Override
    public Object getItem(int position) {
	return infos == null ? null : infos.get(position);
    }

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
    public View getView(int position, View view, ViewGroup arg2) {
	ViewHoldler viewHoldler = null;
	if (view == null) {
	    view = inflater.inflate(R.layout.item_sc_shuoshuoadapter, null);
	    viewHoldler = getHolder(view);
	    view.setTag(viewHoldler);
	} else {
	    viewHoldler = (ViewHoldler) view.getTag();
	}

	if (infos != null) {
	    bindData(viewHoldler, position);
	}
	return view;
    }

    // ////************************************************//////

    private void bindData(ViewHoldler viewHoldler, final int position) {
	if (infos.get(position) == null) {
	    return;
	}
	if (infos.get(position).getContent().isEmpty()) {
	    viewHoldler.content.setVisibility(View.GONE);
	} else {
	    viewHoldler.content.setVisibility(View.VISIBLE);
	    viewHoldler.content.setText(infos.get(position).getContent());
	}

	if (infos.get(position).getCreatTime().isEmpty()) {
	    viewHoldler.time.setVisibility(View.GONE);
	} else {
	    viewHoldler.time.setVisibility(View.VISIBLE);
	    try {
		viewHoldler.time.setText(PublicTools.DateFormat(infos.get(
			position).getCreatTime()));
	    } catch (ParseException e) {
		Log.e("说说列表适配", "dateformate出错");
		e.printStackTrace();
	    }
	}
	if (infos.get(position).getFeedback_count().isEmpty()) {
	    viewHoldler.pinglun.setVisibility(View.GONE);
	} else {
	    viewHoldler.pinglun.setVisibility(View.VISIBLE);
	    viewHoldler.pinglun
		    .setText(infos.get(position).getFeedback_count());
	}

	if (infos.get(position).getGood_count().isEmpty()) {
	    viewHoldler.zan.setVisibility(View.GONE);
	} else {
	    viewHoldler.zan.setVisibility(View.VISIBLE);
	    viewHoldler.zan.setText(infos.get(position).getGood_count());
	}

	if (infos.get(position).getImg_path().isEmpty()) {
	    viewHoldler.img_content.setVisibility(View.GONE);
	} else {
	    viewHoldler.img_content.setVisibility(View.VISIBLE);
	    // data 处理
	    String urls = infos.get(position).getImg_path();
	    // ArrayList urlLists = new ArrayList(urls.split(","));

	    final String[] urlStrings = urls.split(",");
	    viewHoldler.img_content.setAdapter(new MyGridAdapter(urlStrings,
		    context, 2));
	    viewHoldler.img_content
		    .setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
				int p, long arg3) {
			    imageBrower(p, urlStrings);
			}
		    });
	}

	if (infos.get(position).getP_name().isEmpty()) {
	    viewHoldler.name_tv.setVisibility(View.GONE);
	} else {
	    viewHoldler.name_tv.setVisibility(View.VISIBLE);
	    viewHoldler.name_tv.setText(infos.get(position).getP_name());
	}
	if (infos.get(position).getP_photo().isEmpty()) {
	    viewHoldler.photo.setImageResource(R.drawable.user_logo);
	} else {
	    viewHoldler.photo.setVisibility(View.VISIBLE);
	    // viewHoldler.photo.setText(infos.get(position).getP_photo());
	    FinalBitmap.create(context).display(viewHoldler.photo,
		    HttpUtil.IMG_URL + infos.get(position).getP_photo());

	}

	if (infos.get(position).getP_sex().isEmpty()) {
	    viewHoldler.sex_iv.setVisibility(View.GONE);
	} else {
	    viewHoldler.sex_iv.setVisibility(View.VISIBLE);
	    // viewHoldler.content.setText(infos.get(position).getContent());
	    if ("1".equals(infos.get(position).getP_sex())) {
		// 女
		viewHoldler.sex_iv.setImageResource(R.drawable.icon_sex_woman);
	    } else if ("0".equals(infos.get(position).getP_sex())) {
		// 男
		viewHoldler.sex_iv.setImageResource(R.drawable.icon_sex_man);

	    } else {
		// 性别不明
		viewHoldler.sex_iv.setVisibility(View.GONE);
	    }
	}

	if (infos.get(position).getSchool_name().isEmpty()) {
	    viewHoldler.schoolName.setVisibility(View.GONE);
	} else {
	    viewHoldler.schoolName.setVisibility(View.VISIBLE);
	    viewHoldler.schoolName
		    .setText(infos.get(position).getSchool_name());
	}

	if (infos.get(position).getSigns() == null
		|| infos.get(position).getSigns().isEmpty()) {
	    viewHoldler.grid.setVisibility(View.GONE);
	} else {
	    viewHoldler.grid.setVisibility(View.VISIBLE);
	    List<HeadInfo> headInfos = infos.get(position).getSigns();
	    List<String> headInfoLists = new ArrayList<String>();
	    for (int i = 0; i < headInfos.size(); i++) {
		String p_head_info = headInfos.get(i).getP_head_info();
		headInfoLists.add(p_head_info);
	    }
	    String[] str = (String[]) headInfoLists
		    .toArray(new String[headInfoLists.size()]);
	    viewHoldler.grid.setAdapter(new SC_Thumb_HeadInfo_Small_Adapter(str,
		    context));
	    viewHoldler.grid.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int p,
			long arg3) {
		    // Toast.makeText(context, "点击头像" + p + "当前说说" + position,
		    // Toast.LENGTH_SHORT).show();
		    // Intent intent = new Intent();
		    // TODO 说说列表 跳转个人信息
		}
	    });

	}

	if (infos.get(position).getType().isEmpty()) {
	    viewHoldler.type.setVisibility(View.GONE);
	} else {
	    viewHoldler.type.setVisibility(View.GONE);
	    viewHoldler.type.setText(infos.get(position).getType());
	}

    }

    class ViewHoldler {
	CircularImage photo;
	TextView name_tv;
	ImageView sex_iv;
	TextView schoolName;
	TextView content;
	TextView time;
	TextView type;
	TextView zan;
	TextView pinglun;
	// ImageButton caozuo;
	MyGridView img_content;
	NoScrollGridView grid;

    }

    private ViewHoldler getHolder(View view) {
	ViewHoldler holdler = new ViewHoldler();
	holdler.content = (TextView) view.findViewById(R.id.content_text);
	holdler.name_tv = (TextView) view.findViewById(R.id.name_tv);
	holdler.sex_iv = (ImageView) view.findViewById(R.id.sex_iv);
	holdler.photo = (CircularImage) view.findViewById(R.id.photo);
	holdler.pinglun = (TextView) view.findViewById(R.id.fenxiang);
	holdler.time = (TextView) view.findViewById(R.id.date);
	holdler.type = (TextView) view.findViewById(R.id.type);
	holdler.zan = (TextView) view.findViewById(R.id.shoucang);
	holdler.schoolName = (TextView) view.findViewById(R.id.schoolname);
	// holdler.caozuo = (ImageButton) view.findViewById(R.id.reply_content);
	holdler.img_content = (MyGridView) view
		.findViewById(R.id.image_content);
	holdler.grid = (NoScrollGridView) view.findViewById(R.id.grid);
	return holdler;
    }

    /**
     * 
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
	Intent intent = new Intent(context,
		com.daguo.modem.photo.ImagePagerActivity.class);
	// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
		position);
	context.startActivity(intent);
    }

}
