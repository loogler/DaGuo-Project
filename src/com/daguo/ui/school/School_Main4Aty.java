package com.daguo.ui.school;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.utils.HttpUtil;

/**
 * 校园第四页
 * 
 * @author Bugs_Rabbit 時間： 2015-9-20 下午8:12:32
 */
public class School_Main4Aty extends Activity implements OnClickListener {
	/**
	 * initViews
	 */
	private TextView nicknameTextView, infoTextView, feedbackTextView,
			shuoshuoTextView, huodong_0TextView, guanzhuTextView,
			jifenTextView, huodongTextView, dianzanTextView;
	private ImageView photosView, registView, sexView, backview;

	private LinearLayout shuoshuoLayout, huodong_0Layout, guanzhuLayout,
			jifenLayout, duihuanLayout, huodongLayout, dianzanLayout,
			youhuijuanLayout, dingdanLayout, baomingLayout, shezhiLayout;

	/**
	 * userData
	 */
	private String id, headImg, nickName, sex, schoolYear, school, department,
			schoolCard_copy, shuoshuo_count, huodong0_count, guanzhu_count,
			cent, huodong_count, dianzan_count;

	/**
	 * ******
	 */
	Message message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main4);
		getSharedP();
		initViews();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				break;

			default:
				break;
			}
		};
	};

	private void getSharedP() {
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		id = sp.getString("id", "");
		headImg = sp.getString("head_info", "");
		nickName = sp.getString("name", "");
		sex = sp.getString("sex", "");
		schoolYear = sp.getString("start_year", "");
		school = sp.getString("school_name", "");
		department = sp.getString("pro_name", "");
		schoolCard_copy = sp.getString("stu_card_copy", "");

	}

	/**
	 * 
	 * @param res
	 * @return if(true){ 检查通过}else 不通过
	 */
	private boolean infoCheck(String res) {
		if (res != null && !res.equals("") && !res.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	private void initViews() {
		nicknameTextView = (TextView) findViewById(R.id.nickname_tv);
		infoTextView = (TextView) findViewById(R.id.year_dep_sch_tv);
		feedbackTextView = (TextView) findViewById(R.id.feedback_tv);
		shuoshuoTextView = (TextView) findViewById(R.id.shuoshuo_tv);
		huodong_0TextView = (TextView) findViewById(R.id.huodong0_tv);
		guanzhuTextView = (TextView) findViewById(R.id.guanzhu_tv);
		jifenTextView = (TextView) findViewById(R.id.jifen_tv);
		huodongTextView = (TextView) findViewById(R.id.huodong_tv);
		dianzanTextView = (TextView) findViewById(R.id.dianzan_tv);

		photosView = (ImageView) findViewById(R.id.photos);
		registView = (ImageView) findViewById(R.id.regist_iv);
		sexView = (ImageView) findViewById(R.id.sex_iv);
		backview = (ImageView) findViewById(R.id.back);
		photosView.setOnClickListener(this);
		registView.setOnClickListener(this);

		shuoshuoLayout = (LinearLayout) findViewById(R.id.shuoshuo_ll);
		huodong_0Layout = (LinearLayout) findViewById(R.id.huodong0_ll);
		guanzhuLayout = (LinearLayout) findViewById(R.id.guanzhu_ll);
		jifenLayout = (LinearLayout) findViewById(R.id.jifen_ll);
		duihuanLayout = (LinearLayout) findViewById(R.id.duihuan_ll);
		huodongLayout = (LinearLayout) findViewById(R.id.huodong_ll);
		dianzanLayout = (LinearLayout) findViewById(R.id.dianzan_ll);
		youhuijuanLayout = (LinearLayout) findViewById(R.id.youhuijuan_ll);
		dingdanLayout = (LinearLayout) findViewById(R.id.dingdan_ll);
		baomingLayout = (LinearLayout) findViewById(R.id.baoming_ll);
		shezhiLayout = (LinearLayout) findViewById(R.id.shezhi_ll);
		shuoshuoLayout.setOnClickListener(this);
		huodong_0Layout.setOnClickListener(this);
		guanzhuLayout.setOnClickListener(this);
		jifenLayout.setOnClickListener(this);
		duihuanLayout.setOnClickListener(this);
		huodongLayout.setOnClickListener(this);
		dianzanLayout.setOnClickListener(this);
		youhuijuanLayout.setOnClickListener(this);
		dingdanLayout.setOnClickListener(this);
		baomingLayout.setOnClickListener(this);
		shezhiLayout.setOnClickListener(this);

		if (infoCheck(nickName)) {
			nicknameTextView.setText(nickName);
		} else {
			nicknameTextView.setText("");
		}
		if (infoCheck(headImg)) {
			FinalBitmap.create(School_Main4Aty.this).display(photosView,
					HttpUtil.IMG_URL + headImg);
		}
		infoTextView.setText(schoolYear + " 级  " + department + " " + school);
		feedbackTextView.setText("这家伙很懒，什么都没留下");
		if (infoCheck(schoolCard_copy)) {
			registView.setImageResource(R.drawable.schoolmain4_icon_regist);
		} else {
			registView.setImageResource(R.drawable.schoolmain4_icon_unregist);
		}
		if (infoCheck(sex)) {
			if (sex.equals("0")) {
				sexView.setImageResource(R.drawable.icon_sex_man);
			} else if (sex.equals("1")) {
				sexView.setImageResource(R.drawable.icon_sex_woman);
			} else {
				sexView.setVisibility(View.GONE);
			}
		} else {
			sexView.setVisibility(View.GONE);
		}

	}

	/**
	 * 异步获取用户动态信息 显示到界面
	 * 
	 * @author Bugs_Rabbit 時間： 2015-9-20 下午8:16:17
	 */
	class GetUserInfo implements Runnable {

		@Override
		public void run() {
			try {
				String url = HttpUtil.QUERY_USERINFO;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);
				String res = HttpUtil.postRequest(url, map);
				JSONObject js = new JSONObject(res);
				JSONArray array = js.getJSONArray("rows");
				if (array.length() > 0) {// 用户信息可查询
					// 有值
					String score = array.optJSONObject(0).getString("score");

					if (infoCheck(score)) {
						cent = score;
					}
					message = handler.obtainMessage(0);
					message.sendToTarget();
				}

			} catch (Exception e) {

			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shezhi_ll:

			break;
		case R.id.shuoshuo_ll:
			
			break;

		default:
			break;
		}
	}

}
