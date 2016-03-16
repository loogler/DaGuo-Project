/**
 * 互相学习 共同进步
 */
package com.daguo.util.base;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-24 上午11:26:51
 * @function ：普通的商品展示区域图片显示情况  实体类
 */
public class ViewPager_Hacky extends ViewPager {

    public ViewPager_Hacky(Context context) {
	super(context);
    }

    public ViewPager_Hacky(Context context, AttributeSet attrs) {
	super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
	try {
	    return super.onInterceptTouchEvent(ev);
	} catch (IllegalArgumentException e) {
	    // �����
	    return false;
	} catch (ArrayIndexOutOfBoundsException e) {
	    // �����
	    return false;
	}
    }

}
