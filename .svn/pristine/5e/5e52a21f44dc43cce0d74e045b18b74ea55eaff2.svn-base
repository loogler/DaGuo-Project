package com.daguo.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
/**
 * 获取屏幕长宽信息
 * @author Bugs_Rabbit
 *  時間： 2015-8-4 上午9:16:02
 */
public class GetScreenRecUtil {
/**
 * 获取屏幕宽度
 * @param context 上下文
 * @return 宽度
 */
	public static int getWindowWidth(Context context) {
		// 获取屏幕宽度
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;
		Log.i("屏幕宽度", mScreenWidth+"");
		return mScreenWidth;
	}
/**
 * 获取屏幕高度
 * @param context 上下文参数
 * @return 高度
 */
	public static int getWindowHeigh(Context context) {
		// 获取屏幕高度
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		Log.i("屏幕高度", mScreenHeigh+"");
		return mScreenHeigh;
	}
}
