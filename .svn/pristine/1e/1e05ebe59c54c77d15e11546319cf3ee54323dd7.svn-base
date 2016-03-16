package com.daguo.ui.school.xinwen;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.utils.HttpUtil;
import com.daguo.view.dialog.CustomProgressDialog;

public class SC_XinWen_AwardsAty extends Activity {
	private Button getiv;
	private ImageButton back;
	private String p_id;
	private boolean isGet;
	CustomProgressDialog dialog ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_awards);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		dialog=CustomProgressDialog.createDialog(SC_XinWen_AwardsAty.this, "正在查询领奖记录");
		dialog.show();
		new Thread(new Runnable() {
			public void run() {
				String url = HttpUtil.QUERY_ACCEPTPRIZE;
				Map<String, String> map = new HashMap<String, String>();
				map.put("p_id", p_id);
				try {
					String res = HttpUtil.postRequest(url, map);
					JSONObject js = new JSONObject(res);
					if (js.getInt("total") == 0) {
						// 未领取
						isGet = false;
						getiv.setText("点击领取");
						getiv.setClickable(true);

					} else {
						// 领取过
						isGet = true;
						runOnUiThread(new Runnable() {
							public void run() {
								getiv.setBackgroundResource(R.drawable.yuanjiao_choice);
								getiv.setText("已领取");
								getiv.setClickable(false);
								Toast.makeText(SC_XinWen_AwardsAty.this,
										"您已参与活动，快去推荐其他小伙伴加入吧",
										Toast.LENGTH_SHORT).show();
							}
						});
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				dialog.dismiss();
				
			}
		}).start();
		getiv = (Button) findViewById(R.id.getiv);

		getiv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// 点击领奖
				if (!isGet) {
					getiv.setClickable(true);
					new AlertDialog.Builder(SC_XinWen_AwardsAty.this)
							.setMessage("请速去校园活动处领取吧！")
							.setPositiveButton("确定", null).create().show();
					getiv.setText("已领取");
					getiv.setClickable(false);
					new Thread(new Runnable() {
						public void run() {
							String url = HttpUtil.SUBMIT_AWARDSTATUS;
							Map<String, String> map = new HashMap<String, String>();
							map.put("p_id", p_id);
							map.put("status", "0");
							try {
								String res = HttpUtil.postRequest(url, map);
								JSONObject js = new JSONObject(res);
								if (js.getInt("result") == 1) {
									Log.i("领取礼物状态提交", "成功");

								} else {
									Log.e("领取礼物状态提交", "失败");
								}
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}).start();

				} else {
					getiv.setText("已领取");
					getiv.setClickable(false);
					Toast.makeText(SC_XinWen_AwardsAty.this, "您已参与活动，快去推荐其他小伙伴加入吧",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		back = (ImageButton) findViewById(R.id.friend_back);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();

			}
		});
	}
}
