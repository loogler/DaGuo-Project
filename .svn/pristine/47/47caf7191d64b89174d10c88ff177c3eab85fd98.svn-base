package com.daguo.util.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 调节是否可左右滑动的  viewpager 基类
 * @author Bugs_Rabbit
 *  時間： 2015-9-22 下午3:12:10
 */


public class ViewPagerSlipper extends ViewPager {
	private boolean isSlipping = true;//可滑动标志位

	public ViewPagerSlipper(Context context) {
		super(context);
	}

	public ViewPagerSlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if (!isSlipping) {
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if (!isSlipping) {
			return false;
		}
		return super.onTouchEvent(arg0);
	}

	/**
	 *@Title: setSlipping
	 *@Description: TODO设置ViewPager是否可滑动
	 *@param isSlipping
	 */
	public void setSlipping(boolean isSlipping) {
		this.isSlipping = isSlipping;
	}
}
