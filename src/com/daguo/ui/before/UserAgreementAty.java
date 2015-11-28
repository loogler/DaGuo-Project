package com.daguo.ui.before;

import java.io.InputStream;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.daguo.R;

public class UserAgreementAty extends Activity {
	Button buton_register_enter_2;
	TextView text_agreement;
	TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_agreemnet);
		buton_register_enter_2 = (Button) findViewById(R.id.button1);
		text_agreement = (TextView) findViewById(R.id.agreement);
		tv=(TextView) findViewById(R.id.nickname_tv);
		buton_register_enter_2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				UserAgreementAty.this.finish();
				// 返回填写信息界面
			}
		});
		try {
			InputStream is = getAssets().open("kuandaixieyi.txt");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			String text = new String(buffer, "GB2312");
			text_agreement.setText(text);

		} catch (Exception e) {
		}

	}

}
