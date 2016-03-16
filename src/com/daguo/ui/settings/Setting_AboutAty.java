package com.daguo.ui.settings;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daguo.R;

/**
 * 
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-3-10 下午4:35:59
 * @function 功能:设置
 */
public class Setting_AboutAty extends Activity implements OnClickListener {
	private RelativeLayout gengxin, tuandui, jianjie;
	private TextView versionTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_setting_about);
		init();
		initTitleView();
	}

	private void init() {
		gengxin = (RelativeLayout) this.findViewById(R.id.rl_gengxin);
		tuandui = (RelativeLayout) findViewById(R.id.rl_kaifa);
		jianjie = (RelativeLayout) findViewById(R.id.rl_jianjie);
		versionTextView = (TextView) findViewById(R.id.version);
		PackageManager packageManager = this.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);

			versionTextView.setText("Version: " + packageInfo.versionName);
		} catch (Exception e) {

		}

		gengxin.setOnClickListener(this);
		tuandui.setOnClickListener(this);
		jianjie.setOnClickListener(this);
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

		title_tv.setText("关于大果校园");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rl_gengxin:

			break;
		case R.id.rl_kaifa:
			break;
		case R.id.rl_jianjie:
			break;

		default:
			break;
		}
	}

}
