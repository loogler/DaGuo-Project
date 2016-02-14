package com.daguo.ui.school;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.daguo.ui.commercial.Shop_CartAty;
import com.daguo.ui.commercial.cent.CentAty;
import com.daguo.ui.main.Main_4Aty;
import com.daguo.ui.user.UserInfo_MyAttentionAty;
import com.daguo.ui.user.UserInfo_MyBaoMingAty;
import com.daguo.ui.user.UserInfo_MyCentAty;
import com.daguo.ui.user.UserInfo_MyCouponAty;
import com.daguo.ui.user.UserInfo_MyDianZan;
import com.daguo.ui.user.UserInfo_MyHuoDongAty;
import com.daguo.ui.user.UserInfo_MyHuoDong_EvaAty;
import com.daguo.ui.user.UserInfo_MyOrderAty;
import com.daguo.ui.user.UserInfo_MyShuoShuoAty;
import com.daguo.util.beans.UserCenter;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * 校园第四页
 * 
 * @author Bugs_Rabbit 時間： 2015-9-20 下午8:12:32
 */
public class School_Main4Aty extends Activity implements OnClickListener {
	private final int MSG_USERDATA = 10001;

	private String p_id;
	/**
	 * initViews
	 */
	private TextView nicknameTextView, infoTextView, feedbackTextView,
			shuoshuoTextView, huodong_0TextView, guanzhuTextView,
			jifenTextView, huodongTextView, dianzanTextView;
	private ImageView photosView, registView, sexView, backview;

	private LinearLayout shuoshuoLayout, huodong_0Layout, guanzhuLayout,
			jifenLayout, duihuanLayout, huodongLayout, dianzanLayout,
			youhuijuanLayout, dingdanLayout, baomingLayout, shezhiLayout,
			cartLayout;

	/**
	 * userData
	 */
	private String id, headImg, nickName, sex, schoolYear, school, department,
			schoolCard_copy, shuoshuo_count, huodong0_count, guanzhu_count,
			cent, huodong_count, dianzan_count;
	private UserCenter userCenter = new UserCenter();

	/**
	 * ******
	 */
	Intent intent;
	Message message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main4);
		p_id = getSharedPreferences("userinfo", Activity.MODE_PRIVATE)
				.getString("id", "");
		getSharedP();
		initViews();
		new Thread(new GetUserInfo()).start();

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_USERDATA:
				setUserData();

				break;

			default:
				break;
			}
		};
	};

	private void getSharedP() {
		@SuppressWarnings("deprecation")
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
		cartLayout = (LinearLayout) findViewById(R.id.cart_ll);

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
		cartLayout.setOnClickListener(this);

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
	 * 根据获得的数据 给view、 赋值
	 */
	private void setUserData() {
		shuoshuoTextView.setText(PublicTools.doWithNullData(userCenter
				.getTopic_num()));// 我发表的说说
		huodong_0TextView.setText(PublicTools.doWithNullData(userCenter
				.getAct_num()));// 我参加的活动
		guanzhuTextView.setText(PublicTools.doWithNullData(userCenter
				.getFollow_num()));// 我关注的人

	}

	/**
	 * 异步获取用户动态信息 显示到界面
	 * 
	 * @author Bugs_Rabbit 時間： 2015-9-20 下午8:16:17
	 */
	private class GetUserInfo implements Runnable {

		@Override
		public void run() {
			try {
				String url = HttpUtil.QUERY_MYINFO + "&p_id=" + p_id;

				String res = HttpUtil.getRequest(url);
				JSONObject js = new JSONObject(res);

				String follow_num = js.getString("follow_num");
				String act_num = js.getString("act_num");
				String topic_num = js.getString("topic_num");

				userCenter.setAct_num(act_num);
				userCenter.setFollow_num(follow_num);
				userCenter.setTopic_num(topic_num);

				message = handler.obtainMessage(MSG_USERDATA);
				message.sendToTarget();

			} catch (Exception e) {

			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.shezhi_ll:
			intent = new Intent(School_Main4Aty.this, Main_4Aty.class);
			startActivity(intent);
			break;
		case R.id.shuoshuo_ll:
			intent = new Intent(School_Main4Aty.this,
					UserInfo_MyShuoShuoAty.class);
			startActivity(intent);

			break;

		case R.id.huodong0_ll:
			intent = new Intent(School_Main4Aty.this,
					UserInfo_MyHuoDongAty.class);
			startActivity(intent);
			break;

		case R.id.guanzhu_ll:
			intent = new Intent(School_Main4Aty.this,
					UserInfo_MyAttentionAty.class);
			startActivity(intent);

			break;
		case R.id.jifen_ll:
			intent = new Intent(School_Main4Aty.this, UserInfo_MyCentAty.class);
			startActivity(intent);

			break;
		case R.id.duihuan_ll:
			// 积分兑换商城
			intent = new Intent(School_Main4Aty.this, CentAty.class);
			startActivity(intent);

			break;

		case R.id.huodong_ll:
			// 我的活动跳转
			intent = new Intent(School_Main4Aty.this,
					UserInfo_MyHuoDong_EvaAty.class);
			startActivity(intent);
			break;
		case R.id.dianzan_ll:
			// 我的點讚跳转
			intent = new Intent(School_Main4Aty.this, UserInfo_MyDianZan.class);
			startActivity(intent);

			break;
		case R.id.youhuijuan_ll:
			intent = new Intent(School_Main4Aty.this,
					UserInfo_MyCouponAty.class);
			startActivity(intent);

			break;

		case R.id.baoming_ll:
			// 我的报名

			intent = new Intent(School_Main4Aty.this,
					UserInfo_MyBaoMingAty.class);
			startActivity(intent);
			break;
		case R.id.dingdan_ll:
			intent = new Intent(School_Main4Aty.this, UserInfo_MyOrderAty.class);
			startActivity(intent);
			break;
		case R.id.cart_ll:
			intent = new Intent(School_Main4Aty.this, Shop_CartAty.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
