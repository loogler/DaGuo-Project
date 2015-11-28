package com.daguo.ui.school;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.schedule.Main_Aty;
import com.daguo.ui.school.huodong.SC_HuoDongAty;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuoAty1;
import com.daguo.ui.school.xinwen.SC_XinWenAty;

public class School_Main1Aty extends Activity implements OnClickListener {
	private ImageView  iv_mail, iv_point,iv_back ;
	private TextView schoolname;
	private ImageView tv_shuoshuo,tv_map, tv_news, tv_group, tv_class, tv_activity;

	private String p_id, p_school, p_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main1);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_school = sp.getString("school_name", "");
		p_name = sp.getString("name", "");
		init();

	}

	// ------------------------------------------------------------------
	private void init() {
		iv_back=(ImageView) findViewById(R.id.iv_back);
		schoolname = (TextView) findViewById(R.id.schoolname);
		iv_mail = (ImageView) findViewById(R.id.iv_mail);
		iv_point = (ImageView) findViewById(R.id.iv_point);
		tv_map = (ImageView) findViewById(R.id.iv_ditu);
		tv_news = (ImageView) findViewById(R.id.iv_xinwen);
		tv_group = (ImageView) findViewById(R.id.iv_shetuan);
		tv_class = (ImageView) findViewById(R.id.iv_kebiao);
		tv_activity = (ImageView) findViewById(R.id.iv_huodong);
		tv_shuoshuo =(ImageView) findViewById(R.id.iv_shuoshuo);

		iv_back.setOnClickListener(this);
		iv_mail.setOnClickListener(this);
		tv_shuoshuo.setOnClickListener(this);
		tv_map.setOnClickListener(this);
		tv_news.setOnClickListener(this);
		tv_group.setOnClickListener(this);
		tv_class.setOnClickListener(this);
		tv_activity.setOnClickListener(this);
		schoolname.setText(p_school);

	}

	// -----------------------------------------------------------------------

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
//		case R.id.iv_user:
//			// 验证学生证
//			if (p_school != null && !p_school.equals("")) {
//				// 已经验证的学生证信息
//				Toast.makeText(getBaseContext(), p_name + " 你好！",
//						Toast.LENGTH_SHORT).show();
//
//			} else {
//				// 尚未验证的学生信息
//				new AlertDialog.Builder(getBaseContext())
//						.setMessage("您还未验证学生证，请先完善资料")
//						.setPositiveButton("确定",
//								new DialogInterface.OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										Intent intent = new Intent(
//												School_Main1Aty.this,
//												UserInfo_ModifyAty.class);
//										startActivity(intent);
//									}
//								}).setNegativeButton("取消", null).create()
//						.show();
//			}
//
//			break;
		case R.id.iv_shuoshuo:
			Intent intent =new  Intent(School_Main1Aty.this,SC_ShuoShuoAty1.class);
			startActivity(intent);
			break;
		case R.id.iv_mail:
			// 消息中心（仅限于学生交流 不包括商城）
			Toast.makeText(getBaseContext(), "消息中心组建中！", Toast.LENGTH_SHORT)
					.show();

			break;
		case R.id.iv_ditu:
			// 校园地图
			break;
		case R.id.iv_xinwen:
			// 校园新闻
			Intent intent1 = new Intent(getBaseContext(), SC_XinWenAty.class);
			startActivity(intent1);
			break;
		case R.id.iv_shetuan:
			// 校园社团

			break;
		case R.id.iv_kebiao:
			// 校园课程
			Intent intent2 = new Intent(getBaseContext(), Main_Aty.class);
			startActivity(intent2);
			break;
		case R.id.iv_huodong:
			// 校园活动
			Intent intent3 = new Intent(getBaseContext(), SC_HuoDongAty.class);
			startActivity(intent3);
			break;
		case R.id.iv_back:
			finish();
			break;
			

		default:
			break;
		}
	}

}
