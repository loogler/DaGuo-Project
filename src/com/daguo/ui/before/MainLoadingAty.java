package com.daguo.ui.before;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.service.PushService;
import com.daguo.ui.main.MainActivity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.umeng.analytics.MobclickAgent;

/**
 * 第一次加载时的判断页
 * 
 * @author Bugs_rabbit
 * @function 进入app时加载页。
 */
@SuppressLint("WorldReadableFiles")
public class MainLoadingAty extends Activity {
    private boolean isFirstLoading;

    private ImageView iv;
    private String img;

    
    @SuppressLint("HandlerLeak")
    Handler handler =new Handler() {
	public void handleMessage(android.os.Message msg) {
	    FinalBitmap.create(MainLoadingAty.this).display(iv, HttpUtil.IMG_URL+img);
	};
    };
    @SuppressLint("WorldReadableFiles")
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_mainloading);
	iv = (ImageView) findViewById(R.id.iv);
	loadIMG();
	MobclickAgent.setSessionContinueMillis(30000);

	this.startService(new Intent(this, PushService.class));

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
		    // Intent i = new Intent(MainLoadingAty.this,
		    // MainWelcomeAty.class);
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

//			 已登录，直接进主页
			 Intent intent = new Intent(MainLoadingAty.this,
			 MainActivity.class);

//			Intent intent = new Intent(MainLoadingAty.this,
//				LotteryAty.class);
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

    private void loadIMG() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_EVENT
			    + "&menu_id=74fd5ced-4ec3-4511-86bb-8091ed233895&page=1&rows=1";
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if (0 < jsonObject.getInt("total")) {
			 img = jsonObject.getJSONArray("rows")
				.optJSONObject(0).getString("img_path");
			if (!PublicTools.doWithNullData(img).isEmpty()) {
			    	handler.sendEmptyMessage(0);
			}else {
			    //图片加载异常
			}
		    } else {
			// /无结果
		    }
		} catch (Exception e) {
		}
	    }
	}).start();
    }

}
