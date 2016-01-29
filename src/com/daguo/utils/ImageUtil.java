/**
 * 互相学习 共同进步
 */
package com.daguo.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-28 下午2:06:07
 * @function ： 处理图片的工具类
 */
public class ImageUtil {
    /**
     * 通过路径获取输入流
     * 
     * @param path
     *            路径
     * @return 输入流
     * @throws Exception
     *             异常
     */
    public static InputStream getRequest(String path) throws Exception {
	URL url = new URL(path);
	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	conn.setRequestMethod("GET");
	conn.setConnectTimeout(5000);
	if (conn.getResponseCode() == 200) {
	    return conn.getInputStream();
	}
	return null;
    }

    /**
     * 从流中读取二进制数据
     * 
     * @param inStream
     *            输入流
     * @return 二进制数据
     * @throws Exception
     *             异常
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception {
	ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
	byte[] buffer = new byte[4096];
	int len = 0;
	while ((len = inStream.read(buffer)) != -1) {
	    outSteam.write(buffer, 0, len);
	}
	outSteam.close();
	inStream.close();
	return outSteam.toByteArray();
    }

    /**
     * 把一个路径转换成Drawable对象
     * 
     * @param url
     *            路径
     * @return Drawable对象
     */
    public static Drawable loadImageFromUrl(String url) {
	URL m;
	InputStream i = null;
	try {
	    m = new URL(url);
	    i = (InputStream) m.getContent();
	} catch (MalformedURLException e1) {
	    e1.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
	Drawable d = Drawable.createFromStream(i, "src");
	return d;
    }

    /**
     * 把一个路径转换成Drawable对象
     * 
     * @param url
     *            字符串路径
     * @return Drawable对象
     * @throws Exception
     *             异常
     */
    public static Drawable getDrawableFromUrl(String url) throws Exception {
	return Drawable.createFromStream(getRequest(url), null);
    }

    /**
     * 从路径中得到位图
     * 
     * @param url
     *            字符串路径
     * @return 位图
     * @throws Exception
     *             异常
     */
    public static Bitmap getBitmapFromUrl(String url) throws Exception {
	byte[] bytes = getBytesFromUrl(url);
	return byteToBitmap(bytes);
    }

    /**
     * 从路径中得到圆角位图
     * 
     * @param url
     *            字符串路径
     * @param pixels
     *            圆角弧度
     * @return 圆角位图
     * @throws Exception
     *             异常
     */
    public static Bitmap getRoundBitmapFromUrl(String url, int pixels)
	    throws Exception {
	byte[] bytes = getBytesFromUrl(url);
	Bitmap bitmap = byteToBitmap(bytes);
	return toRoundCorner(bitmap, pixels);
    }

    /**
     * 从路径中得到圆角Drawable对象
     * 
     * @param url
     *            字符串路径
     * @param pixels
     *            圆角弧度
     * @return 圆角Drawable对象
     * @throws Exception
     *             异常
     */
    public static Drawable geRoundDrawableFromUrl(String url, int pixels)
	    throws Exception {
	byte[] bytes = getBytesFromUrl(url);
	BitmapDrawable bitmapDrawable = (BitmapDrawable) byteToDrawable(bytes);
	return toRoundCorner(bitmapDrawable, pixels);
    }

    /**
     * 从路径中得到二进制数据
     * 
     * @param url
     *            字符串路径
     * @return 二进制数据
     * @throws Exception
     *             异常
     */
    public static byte[] getBytesFromUrl(String url) throws Exception {
	return readInputStream(getRequest(url));
    }

    /**
     * 从二进制数据中得到位图
     * 
     * @param byteArray
     *            二进制数据
     * @return 位图
     */
    public static Bitmap byteToBitmap(byte[] byteArray) {
	if (byteArray.length != 0) {
	    return BitmapFactory
		    .decodeByteArray(byteArray, 0, byteArray.length);
	} else {
	    return null;
	}
    }

    /**
     * 从二进制数据中得到Drawable对象
     * 
     * @param byteArray
     *            二进制数据
     * @return Drawable对象
     */
    public static Drawable byteToDrawable(byte[] byteArray) {
	ByteArrayInputStream ins = new ByteArrayInputStream(byteArray);
	return Drawable.createFromStream(ins, null);
    }

    /**
     * 把位图转换称二进制数据
     * 
     * @param bm
     *            位图
     * @return 二进制数据
     */
    public static byte[] Bitmap2Bytes(Bitmap bm) {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
	return baos.toByteArray();
    }

    /**
     * 把Drawable对象转换称位图
     * 
     * @param drawable
     *            Drawable对象
     * @return 位图
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {

	Bitmap bitmap = Bitmap
		.createBitmap(
			drawable.getIntrinsicWidth(),
			drawable.getIntrinsicHeight(),
			drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
	Canvas canvas = new Canvas(bitmap);
	drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
		drawable.getIntrinsicHeight());
	drawable.draw(canvas);
	return bitmap;
    }

    /**
     * 图片去色,返回灰度图片
     * 
     * @param bmpOriginal
     *            传入的图片
     * @return 去色后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal) {
	int width, height;
	height = bmpOriginal.getHeight();
	width = bmpOriginal.getWidth();

	Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
		Bitmap.Config.RGB_565);
	Canvas c = new Canvas(bmpGrayscale);
	Paint paint = new Paint();
	ColorMatrix cm = new ColorMatrix();
	cm.setSaturation(0);
	ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	paint.setColorFilter(f);
	c.drawBitmap(bmpOriginal, 0, 0, paint);
	return bmpGrayscale;
    }

    /**
     * 去色同时加圆角
     * 
     * @param bmpOriginal
     *            原图
     * @param pixels
     *            圆角弧度
     * @return 修改后的图片
     */
    public static Bitmap toGrayscale(Bitmap bmpOriginal, int pixels) {
	return toRoundCorner(toGrayscale(bmpOriginal), pixels);
    }

    /**
     * 把位图变成圆角位图
     * 
     * @param bitmap
     *            需要修改的位图
     * @param pixels
     *            圆角的弧度
     * @return 圆角位图
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

	Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		bitmap.getHeight(), Config.ARGB_8888);
	Canvas canvas = new Canvas(output);

	final int color = 0xff424242;
	final Paint paint = new Paint();
	final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	final RectF rectF = new RectF(rect);
	final float roundPx = pixels;

	paint.setAntiAlias(true);
	canvas.drawARGB(0, 0, 0, 0);
	paint.setColor(color);
	canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	canvas.drawBitmap(bitmap, rect, rect, paint);

	return output;
    }

    /**
     * 将BitampDrawable转换成圆角的BitampDrawable
     * 
     * @param bitmapDrawable
     *            原生BitampDrawable对象
     * @param pixels
     *            圆角弧度
     * @return 圆角BitampDrawable对象
     */
    public static BitmapDrawable toRoundCorner(BitmapDrawable bitmapDrawable,
	    int pixels) {
	Bitmap bitmap = bitmapDrawable.getBitmap();
	bitmapDrawable = new BitmapDrawable(toRoundCorner(bitmap, pixels));
	return bitmapDrawable;
    }

    /**
     * 图片水印生成的方法
     * 
     * @param src
     *            源图片位图
     * @param watermark
     *            水印图片位图
     * @return 返回一个加了水印的图片
     */
    public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
	if (src == null)
	    return null;
	int w = src.getWidth();
	int h = src.getHeight();
	int ww = watermark.getWidth();
	int wh = watermark.getHeight();
	Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
	Canvas cv = new Canvas(newb);// 初始化画布
	cv.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
	cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);// 在src的右下角画入水印
	cv.save(Canvas.ALL_SAVE_FLAG);// 保存，用来保存Canvas的状态。save之后，可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。
	cv.restore();// 存储，用来恢复Canvas之前保存的状态。防止save后对Canvas执行的操作对后续的绘制有影响。
	return newb;
    }
}
