package com.daguo.ui.school;

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
import com.daguo.ui.school.outlet.AppDownLoadAty;
import com.daguo.ui.school.outlet.OutLetAty;
import com.daguo.ui.school.outlet.WorldViewAty;
import com.daguo.ui.school.xinxianshi.SC_XinXianShiAty;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-8 上午12:31:31
 * @function ： 校外世界主要界面 是校园第三个翻页的界面
 */

public class School_Main3Aty extends Activity implements OnClickListener {
	/**
	 * initViews
	 */
	private LinearLayout ll;
	private ImageView look_iv, news_iv, apps_iv, nearby_iv;

	Bitmap bm;

	Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main3);
		initViews();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		bm.recycle();
		System.gc();
		super.onDestroy();
	}

	@SuppressWarnings("deprecation")
	private void initViews() {
		initTitleView();
		ll = (LinearLayout) findViewById(R.id.ll);
		nearby_iv = (ImageView) findViewById(R.id.nearby_iv);
		look_iv = (ImageView) findViewById(R.id.look_iv);
		news_iv = (ImageView) findViewById(R.id.news_iv);
		apps_iv = (ImageView) findViewById(R.id.apps_iv);
		bm = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_outschool);
		ll.setBackgroundDrawable(new BitmapDrawable(bm));

		apps_iv.setOnClickListener(this);
		news_iv.setOnClickListener(this);
		look_iv.setOnClickListener(this);
		nearby_iv.setOnClickListener(this);
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

		title_tv.setText("校外世界");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.apps_iv:

			intent = new Intent(School_Main3Aty.this, AppDownLoadAty.class);
			startActivity(intent);

			break;

		case R.id.news_iv:
			intent = new Intent(School_Main3Aty.this, SC_XinXianShiAty.class);
			startActivity(intent);

			break;
		case R.id.look_iv:
			intent = new Intent(School_Main3Aty.this, WorldViewAty.class);
			startActivity(intent);
			break;
		case R.id.nearby_iv:
			// 附近的人
			intent = new Intent(School_Main3Aty.this, OutLetAty.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
