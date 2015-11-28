package com.daguo.modem.choujiang;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;

public class ChouJiangConsoleAty extends Activity {
	int cent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_choujiangconsole);
		MyAppliation.getInstance().addActivity(this);
		Intent i = getIntent();
		cent = i.getIntExtra("cnet", 0);
		init();
	}

	private void init() {
		Button ok = (Button) findViewById(R.id.ok);
		TextView queding = (TextView) findViewById(R.id.jieguo);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"Roboto-MediumItalic.ttf");
		queding.setTypeface(tf);
		queding.setText("恭喜您获得  " + cent + " 分");
		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 计算 提交结果到服务器
				MyAppliation.getInstance().exit();

			}
		});
	}
}
