/**
 * 互相学习 共同进步
 */
package com.daguo.utils;

import com.daguo.ui.before.MyAppliation;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-11 上午11:02:56
 * @function ： 处理bitmap对象的工具类，优化内存处理。
 */
public class BitMapDecodedUtil {

    private static MyAppliation instance;

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
     * 
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
	    int reqWidth, int reqHeight) {
	int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
	Log.d("TAG", "Max memory is " + maxMemory + "KB");
	// 源图片的高度和宽度
	final int height = options.outHeight;
	final int width = options.outWidth;
	int inSampleSize = 1;
	if (height > reqHeight || width > reqWidth) {
	    // 计算出实际宽高和目标宽高的比率
	    final int heightRatio = Math.round((float) height
		    / (float) reqHeight);
	    final int widthRatio = Math.round((float) width / (float) reqWidth);
	    // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
	    // 一定都会大于等于目标的宽和高。
	    inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
	}
	return inSampleSize;
    }

    /**
     * 
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
	    int resId, int reqWidth, int reqHeight) {
	// 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
	final BitmapFactory.Options options = new BitmapFactory.Options();
	options.inJustDecodeBounds = true;
	BitmapFactory.decodeResource(res, resId, options);
	// 调用上面定义的方法计算inSampleSize值
	options.inSampleSize = calculateInSampleSize(options, reqWidth,
		reqHeight);
	// 使用获取到的inSampleSize值再次解析图片
	options.inJustDecodeBounds = false;
	return BitmapFactory.decodeResource(res, resId, options);
    }

}
