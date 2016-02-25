package com.daguo.ui.before;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

import com.daguo.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 
 * 然后在每个Activity的onCreate()方法中添加下面代码：
 * MyAppliation.getInstance().addActivity(this);
 * 
 * 在需要结束所有Activity的时候调用exit方法： MyAppliation.getInstance().exit();
 * 
 * @author Bugs_Rabbit 時間： 2015-8-9 下午10:59:49
 */
public class MyAppliation extends Application {

	@SuppressWarnings("unchecked")
	private List<Activity> activityList = new LinkedList();
	private static MyAppliation instance;

	public MyAppliation() {
	}

	/**
	 * 单例模式中获取唯一的MyApplication实例
	 * 
	 * @return instance
	 */
	public static MyAppliation getInstance() {
		if (null == instance) {
			instance = new MyAppliation();
		}
		return instance;
	}

	/**
	 * 添加Activity到容器中
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish
	public void exit() {
		for (Activity activity : activityList) {
			activity.finish();
		}
		// System.exit(0);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
				.cacheOnDisc(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(20)// 缓存二十张图片
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);

	}

}
