package com.daguo.ui.operators;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daguo.R;

public class OperatorAty extends Activity {
	private Button kuandaiBtn, liuliangBtn, xuanhaoBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_operator);
		kuandaiBtn = (Button) findViewById(R.id.button2);
		kuandaiBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OperatorAty.this, BroadBandAty.class);
				startActivity(intent);
			}
		});
		liuliangBtn = (Button) this.findViewById(R.id.button1);
		liuliangBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Intent intent = new Intent(OperatorAty.this,
//						com.mopote.sdk.surface.FlowShopActivity.class);
//				startActivity(intent);
			}
		});

		xuanhaoBtn = (Button) findViewById(R.id.button3);
		xuanhaoBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(OperatorAty.this, MobileAty.class);
				startActivity(intent);

			}
		});

	}

}
