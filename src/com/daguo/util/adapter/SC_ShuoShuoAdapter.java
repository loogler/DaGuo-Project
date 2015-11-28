package com.daguo.util.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.photo.MyGridAdapter;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuo_EvaluationAty;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;

public class SC_ShuoShuoAdapter extends BaseAdapter {
	@SuppressLint("SimpleDateFormat")
	private Context context;
	private List<ShuoShuoContent> infos;
	private ShuoShuoContent info;
	private NoScrollGridView grid;// 照片相册gridView=(NoScrollGridView)
	LayoutInflater inflater;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private FinalBitmap finalBit;
	String[] urls;
	String[] imgUrl;

	public SC_ShuoShuoAdapter(Context context, List<ShuoShuoContent> infos) {
		this.context = context;
		this.infos = infos;
		inflater = LayoutInflater.from(context);
		finalBit = FinalBitmap.create(context);
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

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View view, ViewGroup parent) {
		ViewHoldler holdler = null;
		// if (view == null) {
		view = inflater.inflate(R.layout.item_sc_shuoshuoadapter, null);
		holdler = getHolder(view);
		view.setTag(holdler);

		// } else {
		// holdler = (ViewHoldler) view.getTag();
		// 视图复用实在不知道大牛们怎么写的 我一写就异常 为什么

		// }

		if (infos != null) {

			info = infos.get(position);
			holdler.content.setText(info.getContent());
			holdler.name.setText(info.getP_name());
			holdler.pinglun.setText("评论(" + info.getFeedback_count() + ")");
			holdler.time.setText(handTime(info.getCreatTime()));
			holdler.type.setText(info.getType_name());
			holdler.zan.setText("赞(" + info.getGood_count() + ")");
			holdler.schoolName.setText(info.getSchool_name());
			if (info.getP_sex().equals("0")) {
				holdler.sex_iv.setVisibility(view.VISIBLE);
				holdler.sex_iv.setImageResource(R.drawable.icon_sex_man);
			} else if (info.getP_sex().equals("1")) {
				holdler.sex_iv.setVisibility(view.VISIBLE);
				holdler.sex_iv.setImageResource(R.drawable.icon_sex_woman);
			} else {
				// 性别不明
				holdler.sex_iv.setVisibility(view.GONE);
			}
			
			if (!isEmpty(info.getSigns())) {
				imgUrl = info.getSigns().split(",");
				int size = imgUrl.length;

				int length = 100;

				DisplayMetrics dm = new DisplayMetrics();
				((Activity) context).getWindowManager().getDefaultDisplay()
						.getMetrics(dm);
				float density = dm.density;
				int gridviewWidth = (int) (size * (length + 4) * density);
				int itemWidth = (int) (length * density);

				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
				grid.setLayoutParams(params);
				grid.setColumnWidth(itemWidth);

				grid.setHorizontalSpacing(5);
				grid.setStretchMode(GridView.NO_STRETCH);
				grid.setVerticalScrollBarEnabled(true);
				grid.setNumColumns(size);
				grid.setAdapter(new MyGridAdapter(imgUrl, context));
			}
			if (!isEmpty(info.getP_photo())) {

				finalBit.display(holdler.photo,
						HttpUtil.IMG_URL + info.getP_photo());
			} else {
				holdler.photo.setImageResource(R.drawable.user_logo);
			}
			if (!isEmpty(info.getImg_path())) {

				// ImageLoader.getInstance().displayImage(
				// HttpUtil.IMG_URL + info.getImg_path(),
				// holdler.img_content);
				finalBit.display(holdler.img_content,
						HttpUtil.IMG_URL + info.getImg_path());

			} else {
				holdler.img_content.setVisibility(View.GONE);
			}

		} else {
			// no !
		}

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//
				Intent intent = new Intent(context,
						SC_ShuoShuo_EvaluationAty.class);
				String id = infos.get(position).getId();
				intent.putExtra("id", id);
				intent.putExtra("good_count", infos.get(position)
						.getGood_count());
				intent.putExtra("feedback_count", infos.get(position)
						.getFeedback_count());
				intent.putExtra("content", infos.get(position).getContent());
				intent.putExtra("time", infos.get(position).getCreatTime());
				intent.putExtra("img_path", infos.get(position).getImg_path());
				intent.putExtra("p_name", infos.get(position).getP_name());
				intent.putExtra("p_avator", infos.get(position).getP_photo());
				intent.putExtra("sex", infos.get(position).getP_sex());
				intent.putExtra("school_name", infos.get(position).getSchool_name());
				intent.putExtra("type", infos.get(position).getType_name());
				context.startActivity(intent);
			}
		});
		holdler.img_content.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String[] urls = new String[] { infos.get(position)
						.getImg_path() };

				imageBrower(0, urls);
			}
		});
		return view;
	}

	class ViewHoldler {
		CircularImage photo;
		TextView name;
		ImageView sex_iv;
		TextView schoolName;
		TextView content;
		TextView time;
		TextView type ;
		TextView zan;
		TextView pinglun;
		// ImageButton caozuo;
		ImageView img_content;
		NoScrollGridView grid;

	}

	ViewHoldler getHolder(View view) {
		ViewHoldler holdler = new ViewHoldler();
		holdler.content = (TextView) view.findViewById(R.id.content_text);
		holdler.name = (TextView) view.findViewById(R.id.name);
		holdler.sex_iv = (ImageView) view.findViewById(R.id.sex_iv);
		holdler.photo = (CircularImage) view.findViewById(R.id.photo);
		holdler.pinglun = (TextView) view.findViewById(R.id.fenxiang);
		holdler.time = (TextView) view.findViewById(R.id.date);
		holdler.type=(TextView) view.findViewById(R.id.type);
		holdler.zan = (TextView) view.findViewById(R.id.shoucang);
		holdler.schoolName = (TextView) view.findViewById(R.id.schoolname);
		// holdler.caozuo = (ImageButton) view.findViewById(R.id.reply_content);
		holdler.img_content = (ImageView) view.findViewById(R.id.image_content);
		holdler.grid = (NoScrollGridView) view.findViewById(R.id.grid);
		return holdler;
	}

	/**
	 * 处理时间
	 * 
	 * @param string
	 * @return
	 */
	private String handTime(String time) {
		if (time == null || "".equals(time.trim())) {
			return "";
		}
		try {
			Date date = format.parse(time);
			long tm = System.currentTimeMillis();// 当前时间戳
			long tm2 = date.getTime();// 发表动态的时间戳
			long d = (tm - tm2) / 1000;// 时间差距 单位秒
			if ((d / (60 * 60 * 24)) > 0) {
				return d / (60 * 60 * 24) + "天前";
			} else if ((d / (60 * 60)) > 0) {
				return d / (60 * 60) + "小时前";
			} else if ((d / 60) > 0) {
				return d / 60 + "分钟前";
			} else {
				// return d + "秒前";
				return "刚刚";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 判断指定的字符串是否是 正确的（不为“”、null 、“null”）
	 * 
	 * @param str
	 * @return
	 */
	private boolean isEmpty(String str) {
		if (str != null && !str.equals("") && !str.equals("null")
				&& !str.equals("[]")) {
			return false;
		} else {
			return true;
		}

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
