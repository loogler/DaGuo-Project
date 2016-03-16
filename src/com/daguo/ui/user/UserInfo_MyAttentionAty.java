/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import android.app.LocalActivityManager;
import android.app.TabActivity;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.schedule.TodayDateRemindAty;
import com.daguo.modem.schedule.TodayDateScheduleAty;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-25 下午3:03:08
 * @function ：我的关注的人 == > 详情代码见课表Main_Aty
 */
@SuppressWarnings("deprecation")
public class UserInfo_MyAttentionAty extends TabActivity {
    // ViewPager是google SDk中自带的一个附加包的一个类，可以用来实现屏幕间的切换。
    // 包名为 android-support-v4.jar
    private ViewPager mPager;// 页卡内容,即主要显示内容的画面
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片
    // private Button bt1, bt2, bt3;// 页卡头标,即页卡1,页卡2...
    TextView mine_tv, yours_tv;

    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    private LayoutInflater mLayoutInflater;
    private TabHost tabHost;
    private LocalActivityManager manager = null;
//    public static String mTextviewArray[] = { "备忘录", "课程表" };
//    @SuppressWarnings("rawtypes")
//    public static Class mTabClassArray[] = { TodayDateRemindAty.class,
//	    TodayDateScheduleAty.class };

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_userinfo_myattention);
	init(savedInstanceState);
	initTitleView();
	InitImageView();
	InitTextView();
	InitViewPager();
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {
	// bt1 = (Button) findViewById(R.id.bt1);
	// bt2 = (Button) findViewById(R.id.bt2);
	// t3 = (TextView) findViewById(R.id.text3);
	mine_tv = (TextView) findViewById(R.id.mine_tv);
	yours_tv = (TextView) findViewById(R.id.yours_tv);

	mine_tv.setOnClickListener(new MyOnClickListener(0));
	yours_tv.setOnClickListener(new MyOnClickListener(1));
	// t3.setOnClickListener(new MyOnClickListener(2));

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

		title_tv.setText("我的关注");
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
     * 初始化ViewPager
     */
    private void InitViewPager() {
	mPager = (ViewPager) findViewById(R.id.vPager);
	listViews = new ArrayList<View>();
	MyPagerAdapter mpAdapter = new MyPagerAdapter(listViews);
	Intent intent = new Intent(this, UserInfo_MyAttention_MyAty.class);
	listViews.add(getView("Remind", intent));
	Intent intent2 = new Intent(this, UserInfo_MyAttention_YourAty.class);
	listViews.add(getView("Schedule", intent2));

	mPager.setAdapter(mpAdapter);
	mPager.setCurrentItem(0);
	mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 为mPage设置了另一个监听

    }

    /**
     * 初始化动画
     */
    private void InitImageView() {
	cursor = (ImageView) findViewById(R.id.cursor);
	bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
		.getWidth();// 获取图片宽度
	DisplayMetrics dm = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(dm);
	int screenW = dm.widthPixels;// 获取分辨率宽度
	offset = (screenW / 2 - bmpW) / 2;// 计算偏移量
	Matrix matrix = new Matrix();
	matrix.postTranslate(offset, 0);
	cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * ViewPager适配器
     */
    public class MyPagerAdapter extends PagerAdapter {
	public List<View> mListViews;

	public MyPagerAdapter(List<View> listViews) {
	    this.mListViews = listViews;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
	    Log.i("destroyItem", "destroyItem");
	    ((ViewPager) arg0).removeView(mListViews.get(arg1));

	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
	    Log.i("getCount", "getCount");
	    return mListViews.size();
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {
	    Log.i("instantiateItem", "instantiateItem");
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

	@Override
	public void notifyDataSetChanged() {
	    super.notifyDataSetChanged();
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
	    switch (index) {
	    case 0:
		// mine_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s1_p));
		// yours_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s2_p));
		mine_tv.setTextColor(getResources()
			.getColor(R.color.green_home));
		yours_tv.setTextColor(getResources().getColor(R.color.grey_2));
		break;

	    case 1:
		mine_tv.setTextColor(getResources().getColor(R.color.grey_2));
		yours_tv.setTextColor(getResources().getColor(
			R.color.green_home));
		// mine_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s1_p));
		// yours_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s2_p));
		break;

	    default:
		break;
	    }

	}
    }

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {

	int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
	int two = one * 2;// 页卡1 -> 页卡3 偏移量

	@Override
	public void onPageSelected(int arg0) {
	    Animation animation = null;

	    switch (arg0) {
	    case 0:
		// mine_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s1_p));
		// yours_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s2_p));
		mine_tv.setTextColor(getResources()
			.getColor(R.color.green_home));
		yours_tv.setTextColor(getResources().getColor(R.color.grey_2));
		tabHost.setCurrentTab(0);
		if (currIndex == 1) {
		    animation = new TranslateAnimation(one, 0, 0, 0);
		} else if (currIndex == 2) {
		    animation = new TranslateAnimation(two, 0, 0, 0);
		}
		break;
	    case 1:
		mine_tv.setTextColor(getResources().getColor(R.color.grey_2));
		yours_tv.setTextColor(getResources().getColor(
			R.color.green_home));
		// mine_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s1_p));
		// yours_tv.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.remind_title_f_s2_p));
		tabHost.setCurrentTab(1);
		if (currIndex == 0) {
		    animation = new TranslateAnimation(offset, one, 0, 0);
		} else if (currIndex == 2) {
		    animation = new TranslateAnimation(two, one, 0, 0);
		}
		break;
	    case 2:
		if (currIndex == 0) {
		    animation = new TranslateAnimation(offset, two, 0, 0);
		} else if (currIndex == 1) {
		    animation = new TranslateAnimation(one, two, 0, 0);
		}
		break;
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

    private void init(Bundle savedInstanceState) {
	tabHost = getTabHost();
	tabHost.addTab(tabHost.newTabSpec("备忘录").setIndicator("")
		.setContent(new Intent(this, TodayDateRemindAty.class)));
	tabHost.addTab(tabHost.newTabSpec("课程表").setIndicator("")
		.setContent(new Intent(this, TodayDateScheduleAty.class)));

	// mLayoutInflater = LayoutInflater.from(this);
	// int count =mTabClassArray.length;
	// for(int i = 0; i < count; i++){
	// TabSpec tabSpec = tabHost.newTabSpec(mTextviewArray[i]).
	// setIndicator(getTabItemView(i)).
	// setContent(getTabItemIntent(i));
	// tabHost.addTab(tabSpec);
	//
	// }
	tabHost.setCurrentTab(0);

	manager = new LocalActivityManager(this, true);
	manager.dispatchCreate(savedInstanceState);

	// 设置点击动画
	// tabHost.getTabWidget().getChildAt(0).setBackgroundResource(R.drawable.selector_tab_background_0);
	// tabHost.getTabWidget().getChildAt(1).setBackgroundResource(R.drawable.selector_tab_background_1);
    }

    // private View getTabItemView(int index) {
    //
    // // 定义了item那一栏的view分布,不采用原始tab的视图
    // View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
    //
    // // ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    //
    // //
    // imageView.setImageDrawable(getResources().getDrawable(mImageViewArray[index]));
    //
    // TextView textView = (TextView) view.findViewById(R.id.textview);
    //
    // textView.setText(mTextviewArray[index]);
    //
    // return view;
    // }

    // private Intent getTabItemIntent(int index) {
    // Intent intent = new Intent(this, mTabClassArray[index]);
    //
    // return intent;
    // }

    public boolean onPrepareOptionsMenu(Menu menu) {
	menu.clear();
	// MenuInflater inflater = getMenuInflater();
	// menu.add(组序号，item.getid（），显示顺序 也可以从0开始，中文显示提示);
	// menu.add(1, 1, 3, "退出");
	// menu.add(1, 3, 1, "关于");
	menu.add(1, 2, 2, "设置");

	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	// case 1:
	// finish();
	// break;

	// case 2:
	// Intent intent = new Intent(this, TodayDateSettingAty.class);
	// this.startActivity(intent);
	// overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
	// break;

	// case 3:
	// Intent intent2 = new Intent(this, TodayDateAbout.class);
	// this.startActivity(intent2);
	// overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
	// break;
	// default:
	// break;
	}

	return super.onOptionsItemSelected(item);
    }

    private View getView(String id, Intent intent) {
	return manager.startActivity(id, intent).getDecorView();
    }

    // boolean isExit = false;
    // Handler mHandler = new Handler() {
    // @Override
    // public void handleMessage(Message msg) {
    // super.handleMessage(msg);
    // isExit = false;
    // }
    //
    // };
    //
    // @Override
    // public boolean onKeyDown(int keyCode, KeyEvent event) {
    // if (keyCode == KeyEvent.KEYCODE_BACK) {
    // if (!isExit) {
    // isExit = true;
    // Toast.makeText(getApplicationContext(), "再按一次退出程序",
    // Toast.LENGTH_SHORT).show();
    // // 利用handler延迟发送更改状态信息
    // mHandler.sendEmptyMessageDelayed(0, 2000);
    // } else {
    // finish();
    // System.exit(0);
    // }
    // }
    // return false;
    // }

}
