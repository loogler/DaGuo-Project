/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.outlet;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.adapter.APPDownloadAdapter;
import com.daguo.util.base.FrameLayout_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner.TransitionEffect;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.AppDownload;
import com.daguo.util.beans.DLInfo;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 上午11:38:12
 * @function ： app下载软件
 */
public class AppDownLoadAty extends Activity {

	private final int MSG_ADD_SUC = 10001;
	private final int MSG_APP_SUC = 10002;
	private final int MSG_ADDS_SUC = 10003;
	private FinalBitmap finalBitmap;
	private ImageLoader imageLoader;

	/**
	 * initViews
	 */
	private ImageView add_iv;
	private ListView content_lv;

	private List<AddBanner> addBanners = new ArrayList<AddBanner>();
	private List<AddBanner> addBanners2 = new ArrayList<AddBanner>();

	private List<AppDownload> lists = new ArrayList<AppDownload>();
	private AppDownload list;
	private APPDownloadAdapter adapter;

	// ============== 广告切换 ===================
	private ViewPager_3DBanner mViewPager = null;
	private RelativeLayout topBanner_rl;

	/**
	 * 装指引的ImageView数组
	 */
	private ImageView[] mIndicators;

	/**
	 * 装ViewPager中ImageView的数组
	 */
	private ImageView[] mImageViews;
	// private List<String> mImageUrls = new ArrayList<String>();
	private LinearLayout mIndicator = null;
	private final int MSG_CHANGE_PHOTO = 1;
	/** 图片自动切换时间 */
	private final int PHOTO_CHANGE_TIME = 5000;
	private MyAdapter mAdapter;

	/**
	 * tools
	 */
	private Message msg;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_ADD_SUC:
				finalBitmap = FinalBitmap.create(AppDownLoadAty.this);
				finalBitmap.display(add_iv, HttpUtil.IMG_URL
						+ addBanners.get(0).getImg_path());
				add_iv.setOnClickListener(new AddBannerOnclickListener(
						AppDownLoadAty.this, addBanners, 0));
				break;

			case MSG_APP_SUC:

				if (null != msg.obj) {
					@SuppressWarnings("unchecked")
					List<AppDownload> abc = (List<AppDownload>) msg.obj;
					lists.addAll(abc);
					adapter.notifyDataSetChanged();

				} else {
					return;
				}

				break;

			case MSG_ADDS_SUC:
				setDot();

				setViewpager();
				break;

			default:
				break;
			}
		};
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_appdownload);
		// TODO 该类需要处理问题
		imageLoader = ImageLoader.getInstance();
		setTopBanner();
		initView();
		loadAddData();

		loadAPPData();

	}

	private void initView() {
		initTitleView();

		add_iv = (ImageView) findViewById(R.id.add_iv);
		content_lv = (ListView) findViewById(R.id.content_lv);

		adapter = new APPDownloadAdapter(this, lists);
		content_lv.setAdapter(adapter);

	}

	/**
	 * 初始化通用标题栏
	 */
	private void initTitleView() {
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		FrameLayout back_fram = (FrameLayout) findViewById(R.id.back_fram);
		LinearLayout message_ll = (LinearLayout) findViewById(R.id.message_ll);
		// TextView function_tv = (TextView) findViewById(R.id.function_tv);
		// ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

		title_tv.setText("软件超市");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/**
	 * 专为顶部轮播banner设定的模块
	 */
	private void setTopBanner() {
		// 1 小红点 2 图片 两部分
		mViewPager = (ViewPager_3DBanner) findViewById(R.id.index_product_images_container);
		mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
		topBanner_rl = (RelativeLayout) findViewById(R.id.topBanner_rl);

		// 设置高度 长宽比 1:3
		LayoutParams params = new LayoutParams(
				GetScreenRecUtil.getWindowWidth(AppDownLoadAty.this),
				2 * GetScreenRecUtil.getWindowWidth(AppDownLoadAty.this) / 5);
		topBanner_rl.setLayoutParams(params);

		loadAddDatas();

	}

	/**
	 * 设置Viewpager界面
	 */
	private void setViewpager() {
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setCurrentItem(0);
		handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
		mAdapter = new MyAdapter();
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (addBanners2.size() == 0 || addBanners2.size() == 1)
					return true;
				else
					return false;
			}
		});
	}

	/**
	 * 设置圆点
	 */
	@SuppressLint("InlinedApi")
	private void setDot() {
		mIndicators = new ImageView[addBanners2.size()];
		if (addBanners2.size() <= 1) {
			mIndicator.setVisibility(View.GONE);
		}

		for (int i = 0; i < mIndicators.length; i++) {
			ImageView imageView = new ImageView(this);
			// LayoutParams params = new LayoutParams(0,
			// LayoutParams.WRAP_CONTENT, Gravity.CENTER);
			LayoutParams params2 = new LayoutParams(20, 20);
			if (i != 0) {
				params2.leftMargin = 5;
				params2.gravity = Gravity.END;
			}
			imageView.setLayoutParams(params2);
			mIndicators[i] = imageView;
			if (i == 0) {
				mIndicators[i]
						.setBackgroundResource(R.drawable.shape_dot_green);
			} else {
				mIndicators[i]
						.setBackgroundResource(R.drawable.shape_dot_white);
			}

			mIndicator.addView(imageView);
		}

		mImageViews = new ImageView[addBanners2.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
	}

	/** ----------------------data thread -------------------------------------- */
	/**
	 * 加载广告栏信息
	 */
	private void loadAddData() {
		new Thread(

		new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=9&page=1&rows=1";
					String res = "";
					JSONObject js = null;
					int total = 0;
					res = HttpUtil.getRequest(url);
					js = new JSONObject(res);
					total = js.getInt("total");
					AddBanner list = null;
					if (0 == total) {
						// 无广告 ，或者加载异常
						Log.e("获取广告信息", "获取app下载 顶部广告信息异常");

					} else {
						JSONArray array = js.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String id = array.optJSONObject(i).getString("id");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String menu_id = array.optJSONObject(i).getString(
									"menu_id");
							String source_id = array.optJSONObject(i)
									.getString("source_id");
							String type = array.optJSONObject(i).getString(
									"type");
							String urls = array.optJSONObject(i).getString(
									"url");
							list = new AddBanner();
							list.setId(id);
							list.setImg_path(img_path);
							list.setMenu_id(menu_id);
							list.setSource_id(source_id);
							list.setType(type);
							list.setUrl(urls);
							addBanners.add(list);
						}
						handler.sendEmptyMessage(MSG_ADD_SUC);
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("APP下载获取广告信息", "获取广告json异常");
				} catch (Exception e) {
					Log.e("APP下载获取广告信息", "获取广告异常");
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 加载轮播广告
	 */
	private void loadAddDatas() {
		new Thread(

		new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=8&page=1&rows=1";
					String res = "";
					JSONObject js = null;
					int total = 0;
					res = HttpUtil.getRequest(url);
					js = new JSONObject(res);
					total = js.getInt("total");
					AddBanner list = null;
					if (0 == total) {
						// 无广告 ，或者加载异常
						Log.e("获取广告信息", "获取app下载 顶部广告信息异常");

					} else {
						JSONArray array = js.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String id = array.optJSONObject(i).getString("id");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String menu_id = array.optJSONObject(i).getString(
									"menu_id");
							String source_id = array.optJSONObject(i)
									.getString("source_id");
							String type = array.optJSONObject(i).getString(
									"type");
							String urls = array.optJSONObject(i).getString(
									"url");
							list = new AddBanner();
							list.setId(id);
							list.setImg_path(img_path);
							list.setMenu_id(menu_id);
							list.setSource_id(source_id);
							list.setType(type);
							list.setUrl(urls);
							addBanners2.add(list);
						}
						handler.sendEmptyMessage(MSG_ADDS_SUC);

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("APP下载获取广告信息", "获取广告json异常");
				} catch (Exception e) {
					Log.e("APP下载获取广告信息", "获取广告异常");
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 加载app下载列表
	 */
	private void loadAPPData() {
		new Thread(new Runnable() {
			public void run() {
				try {

					String url = HttpUtil.QUERY_APP_DOWNLOAD + "&rows=6&page=1";
					// String url =
					// "http://192.168.0.56:8080/XYYYT/service/softInfo/querySoftInfoListByMobile?android=1&rows=6&page=1";
					String res = HttpUtil.getRequest(url);

					JSONArray jsonArray = new JSONArray(res);
					List<AppDownload> abcd = new ArrayList<AppDownload>();
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONArray array = jsonArray.getJSONArray(i);
						// JSONArray arr1=array.optJSONArray(i);
						list = new AppDownload();
						List<DLInfo> infos = new ArrayList<DLInfo>();

						String name = array.optJSONObject(0).getString("name");
						list.setType(name);
						JSONArray arr2 = array.getJSONArray(1);
						for (int j = 0; j < arr2.length(); j++) {
							DLInfo info = new DLInfo();
							String cut_path = arr2.optJSONObject(j).getString(
									"cut_path");
							String dev_company = arr2.optJSONObject(j)
									.getString("dev_company");
							String download_path = arr2.optJSONObject(j)
									.getString("download_path");
							String img_path = arr2.optJSONObject(j).getString(
									"img_path");
							String remark = arr2.optJSONObject(j).getString(
									"remark");
							String size = arr2.optJSONObject(j).getString(
									"size");
							String id = arr2.optJSONObject(j).getString("id");
							String download_num = arr2.optJSONObject(j)
									.getString("download_num");
							String names = arr2.optJSONObject(j).getString(
									"name");

							info.setCut_path(cut_path);
							info.setDev_company(dev_company);
							info.setDownload_path(download_path);
							info.setId(id);
							info.setImg_path(img_path);
							info.setName(names);
							info.setRemark(remark);
							info.setSize(size);

							infos.add(info);

						}
						list.setLists(infos);

						abcd.add(list);
					}
					msg = handler.obtainMessage(MSG_APP_SUC);
					msg.obj = abcd;
					msg.sendToTarget();

					// for (int i = 0; i < array.length(); i++) {
					// JSONArray arr = array.getJSONArray(i);
					// for (int j = 0; j < arr.length(); j++) {
					// String type = arr.optJSONObject(0)
					// .getString("name");
					//
					// }
					//
					// }

				} catch (Exception e) {
					Log.e("app下载", "获取数据异常");
				}
			}
		}).start();

	}

	/* ================================================ */

	/**
	 * 
	 * @author : BugsRabbit
	 * @email 395360255@qq.com
	 * @version 创建时间：2015-11-27 下午4:33:26
	 * @function ：跳转适配器
	 */
	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			if (view instanceof FrameLayout_3DBanner) {
				return ((FrameLayout_3DBanner) view).getChildAt(0) == obj;
			} else {
				return view == obj;
			}
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViewPager
					.findViewFromObject(position));
		}

		@Override
		public Object instantiateItem(View container, final int position) {

			imageLoader.displayImage(
					HttpUtil.IMG_URL + addBanners2.get(position).getImg_path(),
					mImageViews[position]);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			mImageViews[position]
					.setOnClickListener(new AddBannerOnclickListener(
							AppDownLoadAty.this, addBanners2, position));

			return mImageViews[position];
		}
	}

	/**
	 * 
	 * @author : BugsRabbit
	 * @email 395360255@qq.com
	 * @version 创建时间：2015-11-27 下午4:32:54
	 * @function ： This method will be invoked when a new page becomes selected.
	 *           position: Position index of the new selected page.
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int position) {
			setImageBackground(position);
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItemsIndex
	 */
	private void setImageBackground(int selectItemsIndex) {
		for (int i = 0; i < mIndicators.length; i++) {
			if (i == selectItemsIndex) {
				mIndicators[i]
						.setBackgroundResource(R.drawable.shape_dot_green);
			} else {
				mIndicators[i]
						.setBackgroundResource(R.drawable.shape_dot_white);
			}
		}
	}

}
