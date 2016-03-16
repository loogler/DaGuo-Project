package com.daguo.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
/**
 * 异步加载图片类
 * @author Bugs_Rabbit
 *  時間： 2015-8-13 上午2:36:18
 */
public class LoadImgUtil {
	Context context;
	static AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

	public LoadImgUtil(Context context) {
		this.context = context;
	}

	public static void loadImage(final String url, final int id, final View view) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
				new AsyncImageLoader2.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) view.findViewById(id))
								.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			((ImageView) view.findViewById(id)).setImageDrawable(cacheImage);
		}
	}
}
