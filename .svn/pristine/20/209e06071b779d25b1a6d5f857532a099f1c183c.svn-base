package com.daguo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

/**
 * 
 * @author Bugs_rabbit
 * @function 检查是否有网络，判断网络状态。
 */
public class NetCheckUtil {
	
	public static ConnectivityManager manager;

	public static boolean checkNetworkState(Context context) {

		boolean flag = false;
		// 得到网络连接信息
		manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// 去进行判断网络是否连接
		if (manager.getActiveNetworkInfo() != null) {
			flag = manager.getActiveNetworkInfo().isAvailable();
		}
		if (!flag) {
			//断网了
			Toast.makeText(context, "断网了，亲", Toast.LENGTH_LONG).show();
		} else {
			// 网络良好 这里可以判断是不是wifi 来区别当前操作。
		}

		return flag;

	}
}
