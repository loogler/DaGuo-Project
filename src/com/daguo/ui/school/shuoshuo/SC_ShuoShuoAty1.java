package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.user.UserInfo_ModifyAty1;
import com.daguo.util.base.ViewPagerSlipper;

/**
 * 说说框架页面 包含了热门 附近 等等内容
 * 
 * @author Bugs_Rabbit 時間： 2015-9-22 下午3:26:02
 */
public class SC_ShuoShuoAty1 extends FragmentActivity {
	/**
	 * init Views
	 * 
	 */
	public ViewPagerSlipper viewPager;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	private ImageView mTab1, mTab2, mTab3, mTab4;
	private TextView remenTextView,zuixinTextView,benxiaoTextView,fujinTextView;
	
	private ImageButton addButton, backButton;
	
	private String p_photo, p_name, p_school, p_sex;

	private int currIndex = 0;// 当前页卡编号
	private int zero = 0;// 动画图片偏移量
	private int one;// 单个水平动画位移
	private int two;
	private int three;
	private FragmentManager fragmentManager;
	private int displayWidth, displayHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_shuoshuo1);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_name = sp.getString("name", "");
		p_school = sp.getString("school_name", "");
		p_sex = sp.getString("sex", "");
		
		init();
		fragments.add(new SC_ShuoShuo_TabRemenFragment());
		fragments.add(new SC_ShuoShuo_TabZuixinFragment());
		fragments.add(new SC_ShuoShuo_TabBenxiaoFragment());
		fragments.add(new SC_ShuoShuo_TabShenbianFragment());

		fragmentManager = this.getSupportFragmentManager();

		viewPager = (ViewPagerSlipper) findViewById(R.id.viewPager);
		// viewPager.setSlipping(false);//设置ViewPager是否可以滑动
		viewPager.setOffscreenPageLimit(4);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(new MyPagerAdapter());
		
		addButton = (ImageButton) findViewById(R.id.friend_more);
		backButton = (ImageButton) findViewById(R.id.friend_back);
		addButton.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (check()) {

					Intent intent = new Intent(SC_ShuoShuoAty1.this,
							SC_ShuoShuo_WriteAty.class);
					startActivity(intent);
				} else {
					new AlertDialog.Builder(SC_ShuoShuoAty1.this)
							.setTitle("提示")
							.setMessage("您的信息尚未完善，请先完善！")
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(DialogInterface arg0,
												int arg1) {
											Intent intent = new Intent(
													SC_ShuoShuoAty1.this,
													UserInfo_ModifyAty1.class);
											startActivity(intent);
										}
									}).setNegativeButton("取消", null).create()
							.show();
				}
				
			}
		});
		backButton.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}
	/**
	 * 设置发说说关卡 检查个人信息
	 * @return
	 */
	boolean check() {
		if (p_name != null && !p_name.equals("") && p_school != null
				&& !p_school.equals("") && p_sex != null && !p_sex.equals("")) {

			return true;
		} else {
			return false;

		}

	}

	/**
	 * init views
	 */
	private void init() {

		final LinearLayout tab1Layout = (LinearLayout) findViewById(R.id.tab1Layout);
		mTab1 = (ImageView) findViewById(R.id.a);
		mTab2 = (ImageView) findViewById(R.id.b);
		mTab3 = (ImageView) findViewById(R.id.c);
		mTab4 = (ImageView) findViewById(R.id.d);
		remenTextView = (TextView) findViewById(R.id.remen_tv);
		remenTextView.setTextColor(getResources().getColor(R.color.green_home));
		zuixinTextView=(TextView) findViewById(R.id.zuixin_tv);
		benxiaoTextView=(TextView) findViewById(R.id.benxiao_tv);
		fujinTextView=(TextView) findViewById(R.id.fujin_tv);

		// mTabImg = (ImageView) findViewById(R.id.img_tab_now);
		mTab1.setOnClickListener(new MyOnClickListener(0));
		mTab2.setOnClickListener(new MyOnClickListener(1));
		mTab3.setOnClickListener(new MyOnClickListener(2));
		mTab4.setOnClickListener(new MyOnClickListener(3));

		// 以下是设置移动条的初始位置

		int[] location = new int[2];
		mTab1.getLocationInWindow(location);
		int marginLeft = (displayWidth / 4 - 122) / 2;// 122是tab下面移动条的宽度
	}

	/**
	 * 点击适配
	 * 
	 * @author Bugs_Rabbit 時間： 2015-9-22 下午3:38:24
	 */
	private class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}
/**
 * 翻页适配器
 * @author Bugs_Rabbit
 *  時間： 2015-9-22 下午3:54:51
 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				mTab1.setImageDrawable(getResources().getDrawable(
						R.drawable.sc_shuoshuo_tab_back));
				reSetBackGround();
				remenTextView.setTextColor(getResources().getColor(R.color.green_home));
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
					mTab2.setImageDrawable(null);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
					mTab3.setImageDrawable(null);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, 0, 0, 0);
					mTab4.setImageDrawable(null);
				}
				break;
			case 1:
				mTab2.setImageDrawable(getResources().getDrawable(
						R.drawable.sc_shuoshuo_tab_back));
				reSetBackGround();
				zuixinTextView.setTextColor(getResources().getColor(R.color.green_home));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, one, 0, 0);
					mTab1.setImageDrawable(null);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
					mTab3.setImageDrawable(null);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, one, 0, 0);
					mTab4.setImageDrawable(null);
				}
				break;
			case 2:
				mTab3.setImageDrawable(getResources().getDrawable(
						R.drawable.sc_shuoshuo_tab_back));
				reSetBackGround();
				benxiaoTextView.setTextColor(getResources().getColor(R.color.green_home));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, two, 0, 0);
					mTab1.setImageDrawable(null);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
					mTab2.setImageDrawable(null);
				} else if (currIndex == 3) {
					animation = new TranslateAnimation(three, two, 0, 0);
					mTab4.setImageDrawable(null);
				}
				break;
			case 3:
				mTab4.setImageDrawable(getResources().getDrawable(
						R.drawable.sc_shuoshuo_tab_back));
				reSetBackGround();
				fujinTextView.setTextColor(getResources().getColor(R.color.green_home));
				if (currIndex == 0) {
					animation = new TranslateAnimation(zero, three, 0, 0);
					mTab1.setImageDrawable(null);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, three, 0, 0);
					mTab2.setImageDrawable(null);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, three, 0, 0);
					mTab3.setImageDrawable(null);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(150);
			// mTabImg.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	/**
	 * 重置字体颜色，然后重新设置选中颜色
	 * 
	 */
	void reSetBackGround(){
		remenTextView.setTextColor(getResources().getColor(R.color.text));
		zuixinTextView.setTextColor(getResources().getColor(R.color.text));
		benxiaoTextView.setTextColor(getResources().getColor(R.color.text));
		fujinTextView.setTextColor(getResources().getColor(R.color.text));
	}
	/**
	 * 页码适配
	 * @author Bugs_Rabbit
	 *  時間： 2015-9-22 下午3:55:15
	 */
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(fragments.get(position)
					.getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragments.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				FragmentTransaction ft = fragmentManager.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中,用异步的方式来执行。 如果想要立即执行这个等待中的操作,就要调用这个方法(只能在主线程中调用)。
				 * 要注意的是,所有的回调和相关的行为都会在这个调用中被执行完成,因此要仔细确认这个方法的调用位置。
				 */
				fragmentManager.executePendingTransactions();
			}

			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}
			return fragment.getView();
		}
	}

}
