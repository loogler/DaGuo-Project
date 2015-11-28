package com.daguo.ui.before;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * 第一次加载时的判断页
 * 
 * @author Bugs_rabbit
 * @function 进入app时加载页。
 */
@SuppressLint("WorldReadableFiles")
public class MainLoadingAty extends Activity {
	private boolean isFirstLoading;

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mainloading);
		MobclickAgent.setSessionContinueMillis(30000);
		UmengUpdateAgent.setDefault();

		// 获取本地数据
		SharedPreferences sPreferences = getSharedPreferences("setting",
				Context.MODE_WORLD_READABLE);
		isFirstLoading = sPreferences.getBoolean("isFirstLoading", false);
		Log.d("isfirstloading1", isFirstLoading + "");
		// isFirstLoading == false后期换这个判断首次加载
		if (isFirstLoading == false) {
			// 保存到本地
			SharedPreferences sp = getSharedPreferences("setting",
					Context.MODE_WORLD_READABLE);
			Editor editor = sp.edit();
			editor.putBoolean("isFirstLoading", true);
			editor.commit();
			Log.d("isfirstloading2", isFirstLoading + "");
			// 加载效果
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
//					Intent i = new Intent(MainLoadingAty.this,
//							MainWelcomeAty.class);
					Intent i = new Intent(MainLoadingAty.this,
							GuideViewDoor.class);
					startActivity(i);
					MainLoadingAty.this.finish();
				}
			}, 2000);

		} else {

			// 加载效果
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// Intent intent = new Intent(MainLoadingAty.this,
					// MainLoginAty.class);
					// startActivity(intent);
					// MainLoadingAty.this.finish();
					SharedPreferences sp = getSharedPreferences("userinfo",
							Context.MODE_WORLD_READABLE);
					String tel = sp.getString("tel", "");
					if (tel != null && !tel.equals("")) {

						// 已登录，直接进主页
						Intent intent = new Intent(MainLoadingAty.this,
								MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						// 未登录/注册
						Intent intent = new Intent(MainLoadingAty.this,
								MainBeforeLoginActivity.class);
						startActivity(intent);
						finish();
					}
				}
			}, 2000);

		}

	}

}
