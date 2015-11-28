package com.daguo.ui.before;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.util.adapter.ViewPagerAdapter;
/**
 * 
 * @author Bugs_rabbit
 * @function 
 */
public class MainWelcomeAty extends Activity {
	private ViewPager viewPager;
	private ViewPagerAdapter vpAdapter;
	private ArrayList<View> views;
	private View view1, view2, view3, view4, view6;
	private ImageView pointImage0, pointImage1, pointImage2, pointImage3,
			pointImage5;
	private Button startBt;

	// private int currIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mainwelcome);
		initView();

		initData();
	}

	/**
		 */
	private void initView() {
		LayoutInflater mLi = LayoutInflater.from(this);
		view1 = mLi.inflate(R.layout.guide_view01, null);
		view2 = mLi.inflate(R.layout.guide_view02, null);
		view3 = mLi.inflate(R.layout.guide_view03, null);
		view4 = mLi.inflate(R.layout.guide_view04, null);
		view6 = mLi.inflate(R.layout.guide_view06, null);
		viewPager = (ViewPager) findViewById(R.id.viewpager);

		views = new ArrayList<View>();

		vpAdapter = new ViewPagerAdapter(views);

		pointImage0 = (ImageView) findViewById(R.id.page0);
		pointImage1 = (ImageView) findViewById(R.id.page1);
		pointImage2 = (ImageView) findViewById(R.id.page2);
		pointImage3 = (ImageView) findViewById(R.id.page3);
		pointImage5 = (ImageView) findViewById(R.id.page5);
		startBt = (Button) view6.findViewById(R.id.startBtn);
	}

	/**
		 * 
		 */
	private void initData() {
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		views.add(view6);
		viewPager.setAdapter(vpAdapter);

		startBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startbutton();
			}
		});
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			switch (position) {
			case 0:
				pointImage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
				pointImage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				break;
			case 1:
				pointImage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
				pointImage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				pointImage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				break;
			case 2:
				pointImage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
				pointImage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				pointImage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				break;
			case 3:
				pointImage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
				pointImage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				break;
			case 4:
				pointImage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				pointImage5.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_unfocused));
				break;
			case 5:
				pointImage5.setImageDrawable(getResources().getDrawable(
						R.drawable.page_indicator_focused));
				break;
			}
			// currIndex = position;
			// animation.setFillAfter(true);// True:....
			// animation.setDuration(300);
			// mPageImg.startAnimation(animation);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

	}

	/**
		 */
	private void startbutton() {
		Intent intent = new Intent();
		intent.setClass(MainWelcomeAty.this, GuideViewDoor.class);
		startActivity(intent);
		this.finish();
	}

}
