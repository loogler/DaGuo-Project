package com.daguo.ui.before;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.utils.PublicTools;

public class UserAgreementAty extends Activity {
	Button buton_register_enter_2;
	TextView text_agreement;

	private String type;// 协议分类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_agreemnet);

		PublicTools.doWithNullData(type = getIntent().getStringExtra("type"));

		buton_register_enter_2 = (Button) findViewById(R.id.button1);
		text_agreement = (TextView) findViewById(R.id.agreement);

		buton_register_enter_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserAgreementAty.this.finish();
				// 返回填写信息界面
			}
		});
		InputStream is = null;
		if ("tel".equals(type)) {

			try {
				is = getAssets().open("tel_agreement.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("broadband".equals(type)) {

			try {
				is = getAssets().open("broadband_agreement.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ("user".equals(type)) {
			try {
				is = getAssets().open("user_agreement.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer, "GB2312");
			text_agreement.setText(text);

		} catch (Exception e) {
		}

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

		title_tv.setText("用户协议");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

}
