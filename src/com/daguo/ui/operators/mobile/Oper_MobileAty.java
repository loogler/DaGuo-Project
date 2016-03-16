package com.daguo.ui.operators.mobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;

public class Oper_MobileAty extends Activity implements OnClickListener {

	/**
	 * initViews
	 */
	private ImageView mob_iv, tel_iv, uni_iv;
	private LinearLayout ll;

	private Bitmap bmll, bmm, bmt, bmu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_broadband);
		MyAppliation.getInstance().addActivity(this);
		initTitleView();
		initViews();

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

		title_tv.setText("大果校园选号");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/**
     * 
     */
	@SuppressWarnings("deprecation")
	private void initViews() {
		ll = (LinearLayout) findViewById(R.id.ll);
		mob_iv = (ImageView) findViewById(R.id.mob_iv);
		tel_iv = (ImageView) findViewById(R.id.tel_iv);
		uni_iv = (ImageView) findViewById(R.id.uni_iv);

		bmll = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_oper_brodband);
		bmm = BitmapFactory.decodeResource(getResources(),
				R.drawable.oper_brodband1);
		bmt = BitmapFactory.decodeResource(getResources(),
				R.drawable.oper_brodband3);
		bmu = BitmapFactory.decodeResource(getResources(),
				R.drawable.oper_brodband2);

		ll.setBackgroundDrawable(new BitmapDrawable(bmll));
		mob_iv.setBackgroundDrawable(new BitmapDrawable(bmm));
		tel_iv.setBackgroundDrawable(new BitmapDrawable(bmt));
		uni_iv.setBackgroundDrawable(new BitmapDrawable(bmu));

		mob_iv.setOnClickListener(this);
		tel_iv.setOnClickListener(this);
		uni_iv.setOnClickListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {

		case R.id.tel_iv:
			intent = new Intent(Oper_MobileAty.this, Oper_Mobile_FStepAty.class);
			intent.putExtra("busi_name", "5e5aa8f1-8c65-42bf-bebe-6fb6adbbee05");

			startActivity(intent);
			break;

		case R.id.uni_iv:

			intent = new Intent(Oper_MobileAty.this, Oper_Mobile_FStepAty.class);

			intent.putExtra("busi_name", "065017a4-1e05-41dd-88b5-aa02f99de3d6");
			startActivity(intent);

			break;

		case R.id.mob_iv:
			intent = new Intent(Oper_MobileAty.this, Oper_Mobile_FStepAty.class);
			intent.putExtra("busi_name", "b05bab2f-e7b3-41f7-b22f-acc01c0ef0f5");

			startActivity(intent);
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (null != bmll) {
			bmll.recycle();

		}
		if (null != bmu) {
			bmu.recycle();

		}
		if (null != bmt) {
			bmt.recycle();

		}
		if (null != bmm) {
			bmm.recycle();

		}
		System.gc();
		super.onDestroy();
	}

}
