package com.daguo.ui.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore.Audio;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.daguo.R;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.daguo.utils.Utils;
import com.daguo.view.dialog.CustomAlertDialog;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("HandlerLeak")
@SuppressWarnings("deprecation")
/**
 * 主页！
 * @author Bugs_Rabbit
 *  時間： 2015-7-28 下午2:09:30
 */
public class MainActivity extends Activity {

	private int index = 1;// 当前页卡编号
	private ViewPager viewPage;// 页卡内容
	private List<View> listViews;// Tab页面列表
	private RadioGroup radioGroup;// 底部栏
	private LocalActivityManager manager = null;
	private MyPagerAdapter mpAdapter = null;
	boolean isNew, isUp;
	private final int MSG_CENT_SUC = 10001;
	private final int MSG_CENT_FAIL = 10002;

	private String p_id;

	// 版本说明
	private String remark;

	// 安装下载 专用
	private int progress; // 定义进度值
	int handmsg = 1;//
	private NotificationManager nm = null;
	private Notification nn = null; // 引入通知
	private RemoteViews view = null; // 用来设置通知的View
	private String apkDownloadPath; // 应用下载的地址
	private String savePath; // APK下载之后保存的地址
	private String saveFileName; // APK的文件名
	private static final int DOWN_UPDATE = 0;// 下载中消息
	private static final int DOWN_OVER = 1;// 下载完成消息
	private TextView marquee;// 跑马灯文本框
	private Button start;// 开始按钮
	private Button install;// 安装；主要是用来当第一次下载完成提示安装时，用户选择错误，到时对话框消失，点击此按钮可以重新弹出对话框
	private AlertDialog dlg = null;
	private double version = 0.0;

	@Override
	protected void onStart() {
		Log.i("", "onStart()");
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("", "onNewIntent()");
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	@Override
	public void onBackPressed() {
		Log.i("", "onBackPressed()");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		Log.i("", "onPause()");
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		Log.i("", "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("", "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);

		if (getIntent() != null) {
			// 这个才是默认页面的决定因素
			index = getIntent().getIntExtra("index", 0);
			viewPage.setCurrentItem(index);
			setIntent(null);
		} else {
			if (index < 3) {
				index = index + 1;
				viewPage.setCurrentItem(index);
				index = index - 1;
				viewPage.setCurrentItem(index);

			} else if (index == 3) {
				index = index - 1;
				viewPage.setCurrentItem(index);
				index = index + 1;
				viewPage.setCurrentItem(index);
			}
		}
	}

	// 处理下载进度的Handler Start
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				// ShowToast(progress, MainActivity.this);
				view.setProgressBar(R.id.download_progressbar, 100, handmsg,
						false);
				view.setTextViewText(R.id.download_progress_text, handmsg + "%");
				nn.contentView = view;
				nm.notify(0, nn);
				super.handleMessage(msg);
				break;
			case DOWN_OVER:
				// TODO 这里可以做一个点击以后进入下载内容详情的列表
				// view.setOnClickPendingIntent(R.id.download_progressbar, new
				// PendingIntent(MainActivity.this,));
				// install.setVisibility(View.VISIBLE);
				Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT)
						.show();
				dlg = new AlertDialog.Builder(MainActivity.this)
						.setTitle("安装")
						.setMessage("下载完成是否安装")
						.setPositiveButton("安装",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										installApk();
										// install.setVisibility(View.GONE);
									}
								})
						.setNegativeButton("删除",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (deleteAllFilesOfDir(new File(
												savePath))) {

											Toast.makeText(MainActivity.this,
													"存放目录和APK已删除",
													Toast.LENGTH_SHORT).show();
										} else {
											Toast.makeText(MainActivity.this,
													"删除失败，请检查路径，并手动删除",
													Toast.LENGTH_SHORT).show();

										}
										// install.setVisibility(View.GONE);
									}
								})
						.setNeutralButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dlg.dismiss();
									}
								}).show();

				break;

			case MSG_CENT_SUC:

				CustomAlertDialog.createPositiveDialog(MainActivity.this,
						"每日登录 积分 +2").show();
				Time t = new Time();
				t.setToNow();
				int lastmonth = t.month + 1;
				final String str = t.year + "年" + lastmonth + "月" + t.monthDay
						+ "日";
				Editor edt = getSharedPreferences("dailytask", MODE_PRIVATE)
						.edit();
				edt.putString("qiandao", str);
				edt.commit();

				break;

			case MSG_CENT_FAIL:

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MobclickAgent.setSessionContinueMillis(30000);
		
		
		// 开启logcat输出，方便debug，发布时请关闭
		// XGPushConfig.enableDebug(this, true);
		// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
		// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
		// 具体可参考详细的开发指南
		// 传递的参数为ApplicationContext
		Context context = getApplicationContext();
		XGPushManager.registerPush(context);    
		 
		// 2.36（不包括）之前的版本需要调用以下2行代码
		Intent service = new Intent(context, XGPushService.class);
		context.startService(service);
		 
		 
		// 其它常用的API：
		// 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
		// 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
		// 反注册（不再接收消息）：unregisterPush(context)
		// 设置标签：setTag(context, tagName)
		// 删除标签：deleteTag(context, tagName)
		

		p_id = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
				.getString("id", "");
		radioGroup = (RadioGroup) findViewById(R.id.rdg);
		viewPage = (ViewPager) findViewById(R.id.vPager);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		init();
		initViewPager();
		dailyTaskSgin();
		viewPage.setCurrentItem(0);
		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int id) {
						switch (id) {
						case R.id.home:
							index = 0;
							listViews.set(
									0,
									getView("A", new Intent(MainActivity.this,
											Main_1Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(0);

							break;
						case R.id.interaction:
							index = 1;
							listViews.set(
									1,
									getView("B", new Intent(MainActivity.this,
											Main_2Aty1.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(1);

							break;
						case R.id.shop:
							index = 2;
							listViews.set(
									2,
									getView("C", new Intent(MainActivity.this,
											Main_3Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(2);

							break;
						case R.id.cent:
							index = 3;
							listViews.set(
									3,
									getView("D", new Intent(MainActivity.this,
											Main_4Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(3);

							break;

						default:
							break;
						}
					}
				});

		try {
			isUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void initViewPager() {

		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);
		intent = new Intent(MainActivity.this, Main_1Aty.class);
		listViews.add(getView("A", intent));
		intent = new Intent(MainActivity.this, Main_2Aty1.class);
		listViews.add(getView("B", intent));
		intent = new Intent(MainActivity.this, Main_3Aty.class);
		listViews.add(getView("C", intent));
		intent = new Intent(MainActivity.this, Main_4Aty.class);
		listViews.add(getView("D", intent));
		viewPage.setOffscreenPageLimit(0);
		viewPage.setAdapter(mpAdapter);

		viewPage.setCurrentItem(0);
		// viewPage.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	/**
	 * 设置bounds 更改drawatop大小 结果呢 不同手机上显示还不一样
	 * 
	 */
	private void init() {

		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);// 获取系统通知的服务
		nn = new Notification();// 创建一个通知对象

		// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		apkDownloadPath = HttpUtil.DOWNLOAD;
		// 存放位置为手机默认目录下的NotificationDemo文件夹（如果没有会默认生成一个这样的文件夹，详见下载块）
		savePath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/DaGuo"
				+ PublicTools.doWithNullData(String.valueOf(version));
		// 为了测试我们把下载的apk的文件名也明明为NotificationDemo
		saveFileName = savePath + "/DaGuo"
				+ PublicTools.doWithNullData(String.valueOf(version)) + ".apk";
	}

	/**
	 * 设置drawabletop大小
	 * 
	 * @param tv
	 */
	public void setDrawableTop(RadioButton tv) {
		Drawable drawable = tv.getCompoundDrawables()[1];
		drawable.setBounds(0, 0, 40, 40);
		// getMyBitmap(drawable ,40,40);

		tv.setCompoundDrawables(null, drawable, null, null);
	}

	/**
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */

	public View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/***************************** split line **********************************/
	/**
	 * 
	 * @author Bugs_rabbit
	 * @function 页卡适配
	 */
	class MyPagerAdapter extends PagerAdapter {

		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/**
	 * 
	 * @author Bugs_rabbit
	 * @function 翻页监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int arg0) {
			manager.dispatchResume();
			switch (arg0) {
			case 0:
				index = 0;
				radioGroup.check(R.id.home);
				listViews.set(
						0,
						getView("A", new Intent(MainActivity.this,
								Main_1Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 1:
				index = 1;
				radioGroup.check(R.id.interaction);
				listViews.set(
						1,
						getView("B", new Intent(MainActivity.this,
								Main_2Aty1.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 2:
				index = 2;
				radioGroup.check(R.id.shop);
				listViews.set(
						2,
						getView("C", new Intent(MainActivity.this,
								Main_3Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 3:
				index = 3;
				radioGroup.check(R.id.cent);
				listViews.set(
						3,
						getView("D", new Intent(MainActivity.this,
								Main_4Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;

			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			// 如果此前没有按back键，不退出
			if (System.currentTimeMillis() - exitTime > 2000) {
				Toast.makeText(MainActivity.this, "再按一次返回桌面", Toast.LENGTH_LONG)
						.show();
				exitTime = System.currentTimeMillis();
			} else {
				/*
				 * //退出程序的方法 finish(); System.exit(0);
				 */

				// 返回桌面的方法，相当于按一次HOME
				Intent home = new Intent(Intent.ACTION_MAIN);
				home.addCategory(Intent.CATEGORY_HOME);
				startActivity(home);

			}
			return true;
		}

		return super.onKeyDown(keyCode, event);

	}

	// boolean isUpdate;

	/**
	 * 菜单按钮中用到的isUpdate方法
	 * */

	@SuppressLint("SimpleDateFormat")
	public void isUpdate() throws NameNotFoundException {

		String res = null;
		PackageManager packageManager = this.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);
			try {
				String url = HttpUtil.UPDATE;
				// +"&"+ packageInfo.versionName;
				res = HttpUtil.getRequest(url);
				JSONObject js = new JSONObject(res);
				Double vc = js.getDouble("version");
				version = vc;
				Double va = Double.parseDouble(packageInfo.versionName);
				remark = js.getString("remark");
				if (vc > va) {
					isNew = true;
				} else if (vc <= va) {
					isNew = false;
				} else {
					Log.e("更新信息获取失败", "版本获取异常");
				}

				Log.d("url", url);
				Log.d("res", res);
			} catch (Exception e) {
				runOnUiThread(new Runnable() {
					public void run() {

						Toast.makeText(MainActivity.this, "服务器 异常，请稍后再试！",
								Toast.LENGTH_LONG).show();
					}
				});
				e.printStackTrace();
			}

			SharedPreferences sharedPreferences = getSharedPreferences(
					"userInfo", Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("ischeck", true);
			editor.putString("checkdate", new SimpleDateFormat("yyyy-MM-dd")
					.format(new Date(System.currentTimeMillis())));
			editor.commit();
			// return isNew;

		} catch (Exception e) {

		}
		isUp = isNew;
		upDate();
	}

	void upDate() {
		try {
			if (isNew) {

				new AlertDialog.Builder(MainActivity.this)
						.setTitle("升级提示")
						.setMessage(remark)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface arg0,
											int arg1) {
										isUp = false;
										try {
											if ((savePath + "/DaGuo" + String.valueOf(Double
													.parseDouble(MainActivity.this
															.getPackageManager()
															.getPackageInfo(
																	MainActivity.this
																			.getPackageName(),
																	0).versionName)))
													.equals(saveFileName)) {
											} else {
												Toast.makeText(
														MainActivity.this,
														"开始在后台下载新版本",
														Toast.LENGTH_LONG)
														.show();

												view = new RemoteViews(
														getPackageName(),
														R.layout.download_progress_state_view);
												nn.icon = R.drawable.ic_launcher;
												view.setImageViewResource(
														R.id.download_progress_img,
														R.drawable.ic_launcher);
												new Thread(mdownApkRunnable)
														.start();

											}
										} catch (NumberFormatException e) {
											e.printStackTrace();
										} catch (NameNotFoundException e) {
											e.printStackTrace();
										}

										// String url = HttpUtil.DOWNLOAD;
										// Intent intent = new Intent(
										// Intent.ACTION_VIEW);
										// intent.setData(Uri.parse(url));
										// startActivity(intent);
									}
								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {

									@Override
									public void onCancel(DialogInterface arg0) {
										isUp = false;
										// String url = HttpUtil.DOWNLOAD;
										Toast.makeText(MainActivity.this,
												"开始在后台下载新版本", Toast.LENGTH_LONG)
												.show();

										view = new RemoteViews(
												getPackageName(),
												R.layout.download_progress_state_view);
										nn.icon = R.drawable.ic_launcher;
										view.setImageViewResource(
												R.id.download_progress_img,
												R.drawable.ic_launcher);
										new Thread(mdownApkRunnable).start();
										// Intent intent = new Intent(
										// Intent.ACTION_VIEW);
										// intent.setData(Uri.parse(url));
										// startActivity(intent);
									}
								})
						// .setNegativeButton("取消", null)
						.create().show();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// 下载安装包

	// 下载APK的线程匿名类START
	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkDownloadPath);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(savePath);
				Log.e("test", file.exists() + "");
				if (!file.exists()) {
					Log.e("test1", file.exists() + "");
					file.mkdir();
					Log.e("test2", file.exists() + "");
				}
				String apkFile = saveFileName;
				Log.e("test3", apkFile);
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					if (handmsg < progress) {
						handmsg++;
						mHandler.sendEmptyMessage(DOWN_UPDATE);
					}
					// 更新进度
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (true);// 点击取消就停止下载.
				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				Log.e("test", e.getMessage());
			}
		}
	};

	// 安装apk
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {

			Toast.makeText(MainActivity.this, "要安装的文件不存在，请检查路径",
					Toast.LENGTH_SHORT).show();
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		startActivity(i);
	}

	// 删除APK
	/**
	 * @param path
	 *            Apk存放的目录，是目录，不是APK文件的路径！否则只会删除APK 不会删除存放的目录
	 * @return
	 */
	public static boolean deleteAllFilesOfDir(File path) {
		if (!path.exists())
			return false;
		if (path.isFile()) {
			path.delete();
			return true;
		}
		File[] files = path.listFiles();
		for (int i = 0; i < files.length; i++) {
			deleteAllFilesOfDir(files[i]);
		}
		path.delete();
		return true;
	}

	public PendingIntent getDefalutIntent(int flags) {

		Intent intent = new Intent();

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, flags);

		return pendingIntent;
	}

	/**
	 * 每日签到的方法
	 */
	private void dailyTaskSgin() {
		Time t = new Time();
		t.setToNow();
		int lastmonth = t.month + 1;
		final String str = t.year + "年" + lastmonth + "月" + t.monthDay + "日";
		SharedPreferences sp = getSharedPreferences("dailytask", MODE_PRIVATE);
		String qiandao = sp.getString("qiandao", "");

		if (!str.equals(qiandao)) {
			// 未签到 防止多次签到
			// 执行签到 每日签到积分为 2 分

			new Thread(new Runnable() {
				public void run() {
					try {
						String url = HttpUtil.SUBMIT_USERINFO_CENT + "&p_id="
								+ p_id + "&score=2";
						String res = HttpUtil.getRequest(url);
						JSONObject js = new JSONObject(res);

						if ("1".equals(js.getString("result"))) {
							// 成功
							mHandler.sendEmptyMessage(MSG_CENT_SUC);
						} else if ("0".equals(js.getString("result"))) {
							// 积分处理异常
							mHandler.sendEmptyMessage(MSG_CENT_FAIL);

						} else {
							// 其他
							mHandler.sendEmptyMessage(MSG_CENT_FAIL);
						}
					} catch (Exception e) {
					}
				}
			}).start();

		} else {
			// 已签到 不用提示

		}

	}
}