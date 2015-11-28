package com.daguo.ui.main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.daguo.R;
import com.daguo.utils.HttpUtil;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

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
	
	//版本说明
	private String remark ;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MobclickAgent.setSessionContinueMillis(30000);
		UmengUpdateAgent.setDefault();
		// PushAgent mPushAgent = PushAgent.getInstance(MainActivity.this);
		// mPushAgent.enable();
		// String device_token =
		// UmengRegistrar.getRegistrationId(MainActivity.this);
		// Log.i("device_token", device_token);

		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY, "AdIscyAlmlAHQxB83IfNoFIl");
		radioGroup = (RadioGroup) findViewById(R.id.rdg);
		viewPage = (ViewPager) findViewById(R.id.vPager);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
//		 init();
		initViewPager();
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
											Main_2Aty.class)));
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void initViewPager() {

		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);
		intent = new Intent(MainActivity.this, Main_1Aty.class);
		listViews.add(getView("A", intent));
		intent = new Intent(MainActivity.this, Main_2Aty.class);
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
		RadioButton home, interaction, shop, cent;
		home = (RadioButton) findViewById(R.id.home);
		interaction = (RadioButton) findViewById(R.id.interaction);
		shop = (RadioButton) findViewById(R.id.shop);
		cent = (RadioButton) findViewById(R.id.cent);
		setDrawableTop(home);
		setDrawableTop(interaction);
		setDrawableTop(shop);
		setDrawableTop(cent);
	}

	/**
	 * 设置drawabletop大小
	 * 
	 * @param tv
	 */
	public void setDrawableTop(RadioButton tv) {
		Drawable drawable = tv.getCompoundDrawables()[1];
		drawable.setBounds(0, 0, 40, 40);
//		getMyBitmap(drawable ,40,40);

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
								Main_2Aty.class)));
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
				Double va = Double.parseDouble(packageInfo.versionName);
				remark=js.getString("remark");
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
										String url = HttpUtil.DOWNLOAD;
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										intent.setData(Uri.parse(url));
										startActivity(intent);
									}
								})
						.setOnCancelListener(
								new DialogInterface.OnCancelListener() {

									@Override
									public void onCancel(DialogInterface arg0) {
										isUp = false;
										String url = HttpUtil.DOWNLOAD;
										Intent intent = new Intent(
												Intent.ACTION_VIEW);
										intent.setData(Uri.parse(url));
										startActivity(intent);
									}
								})
						// .setNegativeButton("取消", null)
						.create().show();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}