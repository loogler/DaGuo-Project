package com.daguo.ui.settings;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daguo.R;

public class Setting_AboutAty extends Activity implements OnClickListener {
	private RelativeLayout gengxin, tuandui, jianjie;
	private TextView versionTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_setting_about);
		init();
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

			versionTextView.setText("Version: "+packageInfo.versionName);
		} catch (Exception e) {

		}

		gengxin.setOnClickListener(this);
		tuandui.setOnClickListener(this);
		jianjie.setOnClickListener(this);
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
