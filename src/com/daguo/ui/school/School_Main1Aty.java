package com.daguo.ui.school;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.schedule.Main_Aty;
import com.daguo.ui.message.MessageAty;
import com.daguo.ui.school.huodong.SC_HuoDongAty1;
import com.daguo.ui.school.map.SchoolMapAty;
import com.daguo.ui.school.shetuan.SC_SheTuanAty;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuoAty1;
import com.daguo.ui.school.xinwen.SC_XinWenAty;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

public class School_Main1Aty extends Activity implements OnClickListener {
	// private ImageView iv_mail, iv_point, iv_back;
	// private TextView schoolname;
	private final int MSG_NEW_MSG = 10001;
	private final int MSG_NO_MSG = 10002;

	private ImageView tv_shuoshuo, tv_map, tv_news, tv_group, tv_class,
			tv_activity;
	private ImageView remind_iv;

	private String p_id, p_school, p_name;

	private Intent intents = null;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_NEW_MSG:

				break;

			case MSG_NO_MSG:

				break;

			default:
				break;
			}
		}

		;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main1);
		SharedPreferences sp = getSharedPreferences("userinfo", 0);
		p_school = sp.getString("school_name", "");
		p_name = sp.getString("name", "");
		p_id = sp.getString("id", "");
		init();
		loadMessageInfo();

	}

	// ------------------------------------------------------------------
	private void init() {
		initTitleView();
		// iv_back = (ImageView) findViewById(R.id.iv_back);
		// schoolname = (TextView) findViewById(R.id.schoolname);
		// iv_mail = (ImageView) findViewById(R.id.iv_mail);
		// iv_point = (ImageView) findViewById(R.id.iv_point);
		tv_map = (ImageView) findViewById(R.id.iv_ditu);
		tv_news = (ImageView) findViewById(R.id.iv_xinwen);
		tv_group = (ImageView) findViewById(R.id.iv_shetuan);
		tv_class = (ImageView) findViewById(R.id.iv_kebiao);
		tv_activity = (ImageView) findViewById(R.id.iv_huodong);
		tv_shuoshuo = (ImageView) findViewById(R.id.iv_shuoshuo);

		// iv_back.setOnClickListener(this);
		// iv_mail.setOnClickListener(this);
		tv_shuoshuo.setOnClickListener(this);
		tv_map.setOnClickListener(this);
		tv_news.setOnClickListener(this);
		tv_group.setOnClickListener(this);
		tv_class.setOnClickListener(this);
		tv_activity.setOnClickListener(this);
		// schoolname.setText(p_school);

	}

	/**
	 * 初始化通用标题栏
	 */
	private void initTitleView() {
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		FrameLayout back_fram = (FrameLayout) findViewById(R.id.back_fram);
		LinearLayout message_ll = (LinearLayout) findViewById(R.id.message_ll);
		// TextView function_tv = (TextView) findViewById(R.id.function_tv);
		// ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

		title_tv.setText(p_school);
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(School_Main1Aty.this,
						MessageAty.class);
				startActivity(intent);
			}
		});
	}
//	/**
//	 * 通用的headview 不同位置会出现不同的页面要求，根据情况设置
//	 */
//	private void initHeadView() {
//		TextView back_tView = (TextView) findViewById(R.id.back_tv);
//		TextView title_tv = (TextView) findViewById(R.id.title_tv);
//		TextView function_tv = (TextView) findViewById(R.id.function_tv);
//		remind_iv = (ImageView) findViewById(R.id.remind_iv);
//
//		back_tView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
//		title_tv.setText(p_school);
//		function_tv.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				Intent intent = new Intent(School_Main1Aty.this,
//						MessageAty.class);
//				startActivity(intent);
//			}
//		});
//
//		function_tv.setVisibility(View.VISIBLE);
//		remind_iv.setVisibility(View.INVISIBLE);
//
//	}

	// -----------------------------------------------------------------------

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// case R.id.iv_user:
		// // 验证学生证
		// if (p_school != null && !p_school.equals("")) {
		// // 已经验证的学生证信息
		// Toast.makeText(getBaseContext(), p_name + " 你好！",
		// Toast.LENGTH_SHORT).show();
		//
		// } else {
		// // 尚未验证的学生信息
		// new AlertDialog.Builder(getBaseContext())
		// .setMessage("您还未验证学生证，请先完善资料")
		// .setPositiveButton("确定",
		// new DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog,
		// int which) {
		// Intent intent = new Intent(
		// School_Main1Aty.this,
		// UserInfo_ModifyAty.class);
		// startActivity(intent);
		// }
		// }).setNegativeButton("取消", null).create()
		// .show();
		// }
		//
		// break;
		case R.id.iv_shuoshuo:
			Intent intent = new Intent(School_Main1Aty.this,
					SC_ShuoShuoAty1.class);
			startActivity(intent);
			break;

		case R.id.iv_ditu:
			// 校园地图
			intents = new Intent(School_Main1Aty.this, SchoolMapAty.class);
			startActivity(intents);
			break;
		case R.id.iv_xinwen:
			// 校园新闻
			Intent intent1 = new Intent(getBaseContext(), SC_XinWenAty.class);
			startActivity(intent1);
			break;
		case R.id.iv_shetuan:
			// 校园社团
			intents = new Intent(School_Main1Aty.this, SC_SheTuanAty.class);
			startActivity(intents);

			break;
		case R.id.iv_kebiao:
			// 校园课程
			Intent intent2 = new Intent(getBaseContext(), Main_Aty.class);
			startActivity(intent2);
			break;
		case R.id.iv_huodong:
			// 校园活动
			Intent intent3 = new Intent(getBaseContext(), SC_HuoDongAty1.class);
			startActivity(intent3);
			break;

		default:
			break;
		}
	}

	/**
	 * 加载消息通知小圆点
	 */
	private void loadMessageInfo() {
		new Thread(

		new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_MESSAGE_INFORM
							+ "&page=1&rows=15&r_id=" + p_id;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);

					if (0 < jsonObject.getInt("total")) {
						JSONArray array = jsonObject.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String status = array.optJSONObject(i).getString(
									"status");
							if ("0".equals(PublicTools.doWithNullData(status))) {
								handler.sendEmptyMessage(MSG_NEW_MSG);
								return;
							}
						}
						// 消息都阅读过
						handler.sendEmptyMessage(MSG_NO_MSG);
					} else {
						// 并没有通知
						handler.sendEmptyMessage(MSG_NO_MSG);

					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

}
