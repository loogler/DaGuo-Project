package com.daguo.modem.schedule;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;

/**
 * 
 * @author Bugs_rabbit
 * @function 课程表预览界面，
 */
public class ScheduleShowAty extends Activity {

	// ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。
	// 包名为 android-support-v4.jar
	private ViewPager mPager;// 页卡内容,即主要显示内容的画面
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度

	private static String[] days1 = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private TextView tv1, tv2, tv3, tv4, tv5;
	private String WEEK = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_schedule_show);

		InitTextView();

		InitImageView();
		InitViewPager();
	}

	// //public static View inflate (Context context, int resource, ViewGroup
	// root);
	//
	// View view=View.inflate(this, R.layout.app_schedule_show_page, null);
	//
	//
	// tv0=(TextView) view.findViewById(R.id.show_tv0);
	//
	// tv1_2=(TextView) view.findViewById(R.id.show_tv1_2);
	// tv1_3=(TextView) view.findViewById(R.id.show_tv1_3);
	//
	// tv2_2=(TextView) view.findViewById(R.id.show_tv2_2);
	// tv2_3=(TextView) view.findViewById(R.id.show_tv2_3);
	//
	// tv3_2=(TextView) view.findViewById(R.id.show_tv3_2);
	// tv3_3=(TextView) view.findViewById(R.id.show_tv3_3);
	//
	// tv4_2=(TextView) view.findViewById(R.id.show_tv4_2);
	// tv4_3=(TextView) view.findViewById(R.id.show_tv4_3);
	//
	// tv5_2=(TextView) view.findViewById(R.id.show_tv5_2);
	// tv5_3=(TextView) view.findViewById(R.id.show_tv5_3);
	//
	// Intent intent=getIntent();
	// WEEK=intent.getIntExtra("POSITION", 0)+"";//0为星期一
	// Log.i("WEEK", WEEK);
	//
	// SQLiteDatabase db;
	// mToDoDB_Schedule=new ToDoDB_Schedule(this, "Schedule");
	// db=mToDoDB_Schedule.getReadableDatabase();
	// String sql="select * from todo_schedule where todo_week="+WEEK;
	// mCursor =db.rawQuery(sql, null);
	// Log.i("", sql);
	// //判断游标是否为空
	// if(mCursor !=null ){
	// int i=0,n=mCursor .getCount();
	// mCursor .moveToFirst();
	// Log.i("", "mCursor !=null");
	// Log.i("n=?", n+"");
	//
	// //遍历游标 11.
	// while (!mCursor .isAfterLast()) {
	//
	// //获得ID
	// //int id = mCursor .getInt(0);
	// //获得用户名
	// course[i]=mCursor .getString(3);
	// Log.i("", mCursor .getString(3));
	// //获得密码
	// add[i]=mCursor .getString(4);
	// i++;
	// mCursor .moveToNext();
	// }
	// }
	//
	//
	// tv0.setText(days1[Integer.parseInt(WEEK)-1]);
	//
	// tv1_2.setText(course[0]);
	// tv1_3.setText(add[0]);
	//
	// tv2_2.setText(course[1]);
	// tv2_3.setText(add[1]);
	//
	// tv3_2.setText(course[2]);
	// tv3_3.setText(add[2]);
	//
	// tv4_2.setText(course[3]);
	// tv4_3.setText(add[3]);
	//
	// tv5_2.setText(course[4]);
	// tv5_3.setText(add[4]);
	//
	//
	// Log.i("tv", "已设置tv");
	// mCursor .close();
	// mToDoDB_Schedule.close();
	// }
	//
	// @Override
	// protected void onDestroy() {
	// super.onDestroy();
	// mCursor.close();
	// mToDoDB_Schedule.close();
	// }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			return false;
		}
		return false;
	}

	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		tv1 = (TextView) findViewById(R.id.text1);
		tv2 = (TextView) findViewById(R.id.text2);
		tv3 = (TextView) findViewById(R.id.text3);
		tv4 = (TextView) findViewById(R.id.text4);
		tv5 = (TextView) findViewById(R.id.text5);

		tv1.setOnClickListener(new MyOnClickListener(0));
		tv2.setOnClickListener(new MyOnClickListener(1));
		tv3.setOnClickListener(new MyOnClickListener(2));
		tv4.setOnClickListener(new MyOnClickListener(3));
		tv5.setOnClickListener(new MyOnClickListener(4));
	}

	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();

		GetSchedule getSchedule = new GetSchedule(this);

		View monView = getSchedule.getScheduleView(1);// 1为星期一
		View tueView = getSchedule.getScheduleView(2);
		View wedView = getSchedule.getScheduleView(3);
		View thuView = getSchedule.getScheduleView(4);
		View friView = getSchedule.getScheduleView(5);

		listViews.add(monView);
		listViews.add(tueView);
		listViews.add(wedView);
		listViews.add(thuView);
		listViews.add(friView);

		Intent intent = getIntent();
		WEEK = intent.getIntExtra("POSITION", 1) + "";// 1为星期一
		// Log.i("intent.getIntExtra", WEEK);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		// currIndex=Integer.parseInt(WEEK)-1;
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 为mPage设置了另一个监听
		mPager.setCurrentItem(Integer.parseInt(WEEK) - 1);

		// Log.i("WEEK_int", Integer.parseInt(WEEK)-1+"");

	}

	/**
	 * 初始化动画
	 */
	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a_small)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 5 - bmpW) / 2;// 计算偏移量
		// int startoffset=offset*2*currIndex;
		Matrix matrix = new Matrix();
		// int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		// int two = one * 2;// 页卡1 -> 页卡3 偏移量
		// int three = one * 3;
		// int four = one * 4;
		// switch (currIndex) {
		// case 0:
		// matrix.postTranslate(offset, 0);
		// break;
		// case 1:
		// matrix.postTranslate(one, 0);
		// break;
		// case 2:
		// matrix.postTranslate(two, 0);
		// break;
		// case 3:
		// matrix.postTranslate(three, 0);
		// break;
		// case 4:
		// matrix.postTranslate(four, 0);
		// break;
		//
		// default:
		// break;
		// }
		//
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}

	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量
		int three = one * 3;
		int four = one * 4;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			Log.i("arg0", arg0 + "");
			switch (arg0) {// arg0为目的选项卡

			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, two, 0, 0);
				}
				break;
			case 3:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, three, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
				} else if (currIndex == 4) {
					animation = new TranslateAnimation(four, three, 0, 0);
				}
				break;
			case 4:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, four, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, four, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, four, 0, 0);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, four, 0, 0);
				}
				break;
			// default:
			// arg0=currIndex;
			// animation = new TranslateAnimation(offset, one*currIndex , 0, 0);
			// break;
			}

			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

}
