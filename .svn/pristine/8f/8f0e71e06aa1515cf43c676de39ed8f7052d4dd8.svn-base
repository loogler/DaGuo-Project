/**
 * 互相学习 共同进步
 */
package com.daguo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.main.WebView_CommenAty;
import com.daguo.ui.school.huodong.SC_HuoDong_DetailAty1;
import com.daguo.ui.school.shetuan.SC_SheTuanDetailAty;
import com.daguo.ui.school.xinwen.SC_XinWen_DetailAty;
import com.daguo.util.beans.PushMessage;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.NetCheckUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-17 下午4:59:34
 * @function ：service push
 * 
 * 
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("SimpleDateFormat")
public class PushService extends Service {
	PushMessage pushMessage;
	String idddd = "";
	String contentTitle;

	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:

				break;
			case 1:
				if (idddd.equals(pushMessage.getId())) {
					// 同一条push信息
				} else {
					String a = pushMessage.getSource_title();
					if (PublicTools.doWithNullData(a).isEmpty()) {
						contentTitle = "新的大果通知消息";
					} else {
						contentTitle = a;
					}
					setNotification();
					idddd = pushMessage.getId();
				}
				break;

			default:
				break;
			}

		};
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		if (NetCheckUtil.checkNetworkState(getApplicationContext())) {
			new Thread(new RB()).start();
		} else {
			Log.e("push信息", "断网  ，无法推送");
		}

	}

	private void setNotification() {
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

		long miaoshaTime_start = 0;
		long miaoshaTime_end = 0;
		long times = System.currentTimeMillis();

		try {
			if (pushMessage == null) {
				Toast.makeText(getApplicationContext(), "断网了",
						Toast.LENGTH_LONG).show();
				return;

			}
			Date dateStart = sdf.parse(pushMessage.getS_date().toString());
			Date dateEnd = sdf.parse(pushMessage.getE_date().toString());
			miaoshaTime_start = dateStart.getTime();
			miaoshaTime_end = dateEnd.getTime();

		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (miaoshaTime_start < times && times < miaoshaTime_end) {

			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
					this);
			mBuilder.setContentTitle("大果校园")
					// 设置通知栏标题
					.setContentText(contentTitle)
					// 设置通知栏显示内容</span>
					.setContentIntent(
							getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) // 设置通知栏点击意图
					// .setNumber(number) //设置通知集合的数量
					.setTicker("你有新的通知") // 通知首次出现在通知栏，带上升动画效果的
					.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
					.setPriority(Notification.PRIORITY_DEFAULT) // 设置该通知优先级
					.setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
					// .setOngoing(false)//
					// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
					.setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
					// Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音
					// // requires VIBRATE permission
					.setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON

			Notification notification = mBuilder.build();
			// notification.flags = Notification.FLAG_AUTO_CANCEL;
			mNotificationManager.notify(1, notification);
		} else {
			Log.d("推送", "不在推送时间范围内");
		}

	}

	public PendingIntent getDefalutIntent(int flags) {

		Intent intent = new Intent();

		String a = pushMessage.getView_type();
		// 0：介绍信息1：外链 2：下载包
		if ("0".equals(a)) {
			//

			onClicks(intent);

		} else if ("1".equals(a)) {

			intent.setClass(this, WebView_CommenAty.class);
			intent.putExtra("url", pushMessage.getUrl());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);

		} else if ("2".equals(a)) {
			String url = pushMessage.getApk_path();
			intent.setAction(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);

		} else {
			Log.e("推送信息", "推送内容类别出错");
		}

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, flags);

		return pendingIntent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// public class TimerTest extends TimerTask {
	// private int count = 1;
	//
	// public void main(String args[]) {
	// /*
	// * 抽象类不能实例化，由此可见一斑~！！~嘎嘎 TimerTask tt=new TimerTask();
	// */
	// TimerTest tt = new TimerTest();
	// Timer timer = new Timer();
	// Date date = new Date();
	// date.setMinutes(16);
	// // 线程在一个特定的Date启动：timer.schedule(tt, date);
	// // 线程延迟三秒后启动：timer.schedule(tt, 3000);
	// // 线程在date启动，每隔5秒启动一次：timer.schedule(tt, date, 5000);
	// // 线程延迟delay/1000秒以后启动，每隔period/1000秒运行一次：
	// // timer.schedule(task, delay, period);
	// }
	//
	// // 在类java.util.TimerTask中，并未实现run方法，所以，在该类中必须实现run方法
	// public void run() {
	// System.out.println("时间到！5秒后线程重新启动！");
	// System.out.println(count + "次定时运行" + System.currentTimeMillis()
	// / 1000);
	// count++;
	//
	// }
	// }

	class TK extends TimerTask {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {

			try {
				String url = HttpUtil.PUSH;
				String res = HttpUtil.getRequest(url);
				JSONObject jsonObject = new JSONObject(res);

				if (jsonObject.getInt("total") > 0) {

					JSONArray array = jsonObject.getJSONArray("rows");
					for (int i = 0; i < array.length(); i++) {
						String id = array.optJSONObject(i).getString("id");
						String menu_id = array.optJSONObject(i).getString(
								"menu_id");
						String network_type = array.optJSONObject(i).getString(
								"network_type");
						String province_id = array.optJSONObject(i).getString(
								"province_id");
						String user_type = array.optJSONObject(i).getString(
								"user_type");
						String source_id = array.optJSONObject(i).getString(
								"source_id");
						String version_type = array.optJSONObject(i).getString(
								"version_type");
						String apk_path = array.optJSONObject(i).getString(
								"apk_path");
						String urls = array.optJSONObject(i).getString("url");
						String school_id = array.optJSONObject(i).getString(
								"school_id");
						String e_date = array.optJSONObject(i).getString(
								"e_date");
						String level = array.optJSONObject(i)
								.getString("level");
						String view_type = array.optJSONObject(i).getString(
								"view_type");
						String busi_type = array.optJSONObject(i).getString(
								"busi_type");
						String s_date = array.optJSONObject(i).getString(
								"s_date");
						String region_type = array.optJSONObject(i).getString(
								"region_type");
						String source_title = array.optJSONObject(i).getString(
								"source_title");
						String school_name = array.optJSONObject(i).getString(
								"school_name");

						pushMessage = new PushMessage();
						pushMessage.setId(id);
						pushMessage.setMenu_id(menu_id);
						pushMessage.setNetwork_type(network_type);
						pushMessage.setProvince_id(province_id);
						pushMessage.setUser_type(user_type);
						pushMessage.setSource_id(source_id);
						pushMessage.setVersion_type(version_type);
						pushMessage.setApk_path(apk_path);
						pushMessage.setUrl(urls);
						pushMessage.setSchool_id(school_id);
						pushMessage.setE_date(e_date);
						pushMessage.setLevel(level);
						pushMessage.setView_type(view_type);
						pushMessage.setBusi_type(busi_type);
						pushMessage.setS_date(s_date);
						pushMessage.setRegion_type(region_type);
						pushMessage.setSource_title(source_title);
						pushMessage.setSchool_name(school_name);

					}

				} else {
					// 不存在push信息
				}
			} catch (Exception e) {
			}
			msg = handler.obtainMessage(1);
			msg.sendToTarget();
		}
	}

	class RB implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			Timer timer = new Timer();
			// Date date = new Date();
			// date.setMinutes(16);
			timer.schedule(new TK(), 10000, 10 * 1000);
			// TODO 时间修改 60*

		}

	}

	/******************************** tools ****************************/
	private void onClicks(Intent intent) {
		// TODO 调用一个统计方法接口

		if (pushMessage.getMenu_id().equals(
				"b3b7866c-3bf9-48a7-8caa-effa1fb86782")) {
			// 校园活动
			intent.setClass(this, SC_HuoDong_DetailAty1.class);
			intent.putExtra("id", pushMessage.getSource_id());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);

		} else if (pushMessage.getMenu_id().equals(
				"7176add9-6d46-4c97-8983-362848304388")) {
			// 校园新鲜事
			intent.setClass(this, SC_XinWen_DetailAty.class);
			intent.putExtra("id", pushMessage.getSource_id());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
		} else if (pushMessage.getMenu_id().equals(
				"db94a88d-5c78-448b-a3a7-4af1c3850571")) {
			// 校园新闻
			intent.setClass(this, SC_XinWen_DetailAty.class);
			intent.putExtra("id", pushMessage.getSource_id());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			// startActivity(intent);

		} else if (pushMessage.getMenu_id().equals(
				"d6c986c5-8e52-485e-9a6e-d5d98480564e")) {
			// 校园社团
			intent.setClass(this, SC_SheTuanDetailAty.class);
			intent.putExtra("id", pushMessage.getSource_id());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// startActivity(intent);
		}
	}

}
