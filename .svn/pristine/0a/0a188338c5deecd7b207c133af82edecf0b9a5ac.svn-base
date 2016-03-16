package com.daguo.ui.settings;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.utils.HttpUtil;

/**
 * 用户反馈信息 界面
 * 
 * @author Bugs_Rabbit 時間： 2015-9-28 下午10:02:12
 */
public class Setting_App_UserOpinion extends Activity {

	private EditText content;
	private Button send;
	private ImageButton back;

	private String contents;
	private String p_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		setContentView(R.layout.aty_setting_app_useropinion);
		content = (EditText) findViewById(R.id.user_opinion_content_edt);
		send = (Button) findViewById(R.id.friend_more);
		back = (ImageButton) findViewById(R.id.friend_back);

		send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				send();
			}
		});
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

	/**
	 * 发送反馈信息至服务器的线程
	 * 
	 * @param content
	 */

	private void send() {
		contents = content.getText().toString();
		if (infoCheck(contents)) {
			new Thread(new Runnable() {
				public void run() {
					String url = HttpUtil.USEROPINION;
					Map<String, String> map = new HashMap<String, String>();
					map.put("p_id", p_id);
					map.put("content", contents);
					try {
						String res = HttpUtil.postRequest(url, map);
						JSONObject js = new JSONObject(res);
						int resultCode = js.getInt("result");
						if (resultCode == 1) {
							// 成功
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											Setting_App_UserOpinion.this,
											"提交成功，感谢您的宝贵意见", Toast.LENGTH_SHORT)
											.show();
								}
							});

							finish();

						} else {
							// 失败
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(
											Setting_App_UserOpinion.this,
											"提交失败，请稍后重试", Toast.LENGTH_SHORT)
											.show();
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(Setting_App_UserOpinion.this,
										"提交失败，服务器异常", Toast.LENGTH_SHORT)
										.show();
								finish();
							}
						});

					}

				}
			}).start();
		} else {
			Toast.makeText(Setting_App_UserOpinion.this, "请输入合适的文字",
					Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 检查输入文字合法性
	 * 
	 * @param str
	 * @return
	 */
	boolean infoCheck(String str) {
		if (str != null && !str.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}
}
