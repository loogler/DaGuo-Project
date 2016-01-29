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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.daguo.R;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-8 下午2:46:59
 * @function ：我的订单界面 ，用于查看已经下单购买或准备购买的商品
 */
@SuppressWarnings("deprecation")
public class UserInfo_MyOrderAty extends TabActivity {

    /**
     * @see initViews
     */
    private ViewPager mPager;// 页卡内容,即主要显示内容的画面
    private List<View> listViews; // Tab页面列表
    private ImageView cursor;// 动画图片

    private TextView all_tv, unpay_tv, pay_tv, done_tv;

    /**
     * tools
     */
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度

    // private LayoutInflater mLayoutInflater;
    private TabHost tabHost;

    private LocalActivityManager manager = null;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_myorder);
	init(savedInstanceState);
	initHeadView();
	InitImageView();
	InitTextView();
	InitViewPager();
    }

    private void init(Bundle savedInstanceState) {
	tabHost = getTabHost();

	manager = new LocalActivityManager(this, true);
	manager.dispatchCreate(savedInstanceState);

    }

    /**
     * 通用的headview 不同位置会出现不同的页面要求，根据情况设置
     */
    private void initHeadView() {
	TextView back_tView = (TextView) findViewById(R.id.back_tv);
	TextView title_tv = (TextView) findViewById(R.id.title_tv);
	TextView function_tv = (TextView) findViewById(R.id.function_tv);
	ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

	back_tView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		finish();
	    }
	});
	title_tv.setText("我的订单");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

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
	offset = (screenW / 4 - bmpW) / 4;// 计算偏移量
	Matrix matrix = new Matrix();
	matrix.postTranslate(offset, 0);
	cursor.setImageMatrix(matrix);// 设置动画初始位置
    }

    /**
     * 初始化头标
     */
    private void InitTextView() {

	all_tv = (TextView) findViewById(R.id.all_tv);
	unpay_tv = (TextView) findViewById(R.id.unpay_tv);
	pay_tv = (TextView) findViewById(R.id.pay_tv);
	done_tv = (TextView) findViewById(R.id.done_tv);

	all_tv.setOnClickListener(new MyOnClickListener(0));
	unpay_tv.setOnClickListener(new MyOnClickListener(1));
	pay_tv.setOnClickListener(new MyOnClickListener(2));
	done_tv.setOnClickListener(new MyOnClickListener(3));

    }

    /**
     * 初始化ViewPager
     */
    private void InitViewPager() {
	mPager = (ViewPager) findViewById(R.id.vPager);
	listViews = new ArrayList<View>();
	MyPagerAdapter mpAdapter = new MyPagerAdapter(listViews);
	Intent intent = new Intent(this, UserInfo_MyOrder_AllAty.class);
	listViews.add(getView("Remind", intent));
	Intent intent2 = new Intent(this, UserInfo_MyOrder_UnPayAty.class);
	listViews.add(getView("Schedule", intent2));
	Intent intent3 = new Intent(this, UserInfo_MyOrder_PayAty.class);
	listViews.add(getView("Reminds", intent3));
	Intent intent4 = new Intent(this, UserInfo_MyOrder_FinishAty.class);
	listViews.add(getView("Schedules", intent4));

	mPager.setAdapter(mpAdapter);
	mPager.setCurrentItem(0);
	mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 为mPage设置了另一个监听

    }

    private View getView(String id, Intent intent) {
	return manager.startActivity(id, intent).getDecorView();
    }

    /*------- implements-----------------------------*/

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
		reSetColor();
		all_tv.setTextColor(getResources().getColor(R.color.green_home));
		break;

	    case 1:
		reSetColor();
		unpay_tv.setTextColor(getResources().getColor(
			R.color.green_home));

		break;

	    case 2:
		reSetColor();
		pay_tv.setTextColor(getResources().getColor(R.color.green_home));
		break;

	    case 3:
		reSetColor();
		done_tv.setTextColor(getResources()
			.getColor(R.color.green_home));
		break;

	    default:
		break;
	    }

	}
    }

    /**
     * 初始化字体颜色 ，出现选中颜色不一样的效果
     */
    private void reSetColor() {
	all_tv.setTextColor(getResources().getColor(R.color.grey_2));
	unpay_tv.setTextColor(getResources().getColor(R.color.grey_2));
	pay_tv.setTextColor(getResources().getColor(R.color.grey_2));
	done_tv.setTextColor(getResources().getColor(R.color.grey_2));
    }

    /*-/--/*/

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

    /*--/**-/-*/

    /**
     * 页卡切换监听
     */
    public class MyOnPageChangeListener implements OnPageChangeListener {
	// TODO 便宜量计算麻烦，先忽略处理
	int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
	int two = one * 2;// 页卡1 -> 页卡3 偏移量

	@Override
	public void onPageSelected(int arg0) {
	    Animation animation = null;

	    switch (arg0) {
	    case 0:
		reSetColor();
		all_tv.setTextColor(getResources().getColor(R.color.green_home));

		tabHost.setCurrentTab(0);
		if (currIndex == 1) {
		    animation = new TranslateAnimation(one, 0, 0, 0);
		} else if (currIndex == 2) {
		    animation = new TranslateAnimation(one * 2, 0, 0, 0);
		} else if (currIndex == 3) {
		    animation = new TranslateAnimation(one * 3, 0, 0, 0);

		}
		break;
	    case 1:
		reSetColor();
		unpay_tv.setTextColor(getResources().getColor(
			R.color.green_home));

		tabHost.setCurrentTab(1);
		if (currIndex == 0) {
		    animation = new TranslateAnimation(offset, one, 0, 0);
		} else if (currIndex == 2) {
		    animation = new TranslateAnimation(two, one * 3, 0, 0);
		} else if (currIndex == 3) {
		    animation = new TranslateAnimation(two, one * 4, 0, 0);

		}
		break;
	    case 2:
		reSetColor();
		pay_tv.setTextColor(getResources().getColor(R.color.green_home));

		if (currIndex == 0) {
		    animation = new TranslateAnimation(offset, one, 0, 0);
		} else if (currIndex == 1) {
		    animation = new TranslateAnimation(one, two, 0, 0);
		} else if (currIndex == 3) {
		    animation = new TranslateAnimation(one, one * 4, 0, 0);

		}
		break;

	    case 3:
		reSetColor();
		done_tv.setTextColor(getResources()
			.getColor(R.color.green_home));

		if (currIndex == 0) {
		    animation = new TranslateAnimation(offset, one, 0, 0);
		} else if (currIndex == 1) {
		    animation = new TranslateAnimation(one, two, 0, 0);
		} else if (currIndex == 2) {
		    animation = new TranslateAnimation(one, one * 3, 0, 0);
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

}
