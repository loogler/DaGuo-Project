/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-24 下午3:34:58
 * @function ：商城图片详情适配
 */
public class Shop_GoodsDetail_BannerAdapter extends PagerAdapter {

    Activity activity;
    ImageView[] imageViews;

    /**
     * 商城图片详情适配
     * 
     * @param activity
     *            activity
     * @param lists
     *            图片View
     */
    public Shop_GoodsDetail_BannerAdapter(Activity activity,
	    ImageView[] imageViews) {
	this.activity = activity;
	this.imageViews = imageViews;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.view.PagerAdapter#getCount()
     */
    @Override
    public int getCount() {
	return imageViews == null ? 0 : imageViews.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View,
     * java.lang.Object)
     */
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
	return arg0 == arg1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.view.PagerAdapter#instantiateItem(android.view.ViewGroup
     * , int)
     */
    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
	View view = imageViews[position];

	container.addView(view);

	return view;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.view.PagerAdapter#destroyItem(android.view.ViewGroup,
     * int, java.lang.Object)
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
	container.removeView(imageViews[position]);

    }

}
