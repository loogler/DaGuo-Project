package com.daguo.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.schedule.Main_Aty;
import com.daguo.ui.commercial.Shop_SearchAty;
import com.daguo.ui.commercial.school.SchoolShop_MainAty;
import com.daguo.ui.message.MessageAty;
import com.daguo.ui.operators.OperatorAty;
import com.daguo.ui.school.School_Main3Aty;
import com.daguo.ui.school.School_Main4Aty;
import com.daguo.ui.school.School_MainAty;
import com.daguo.ui.school.shetuan.SC_SheTuanAty;
import com.daguo.ui.school.xinwen.SC_XinWenAty;
import com.daguo.ui.school.xinwen.SC_XinWen_AwardsAty;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.adapter.Main1_FunctionIconAdapter;
import com.daguo.util.adapter.Main_BottomBannerAdapter;
import com.daguo.util.base.FrameLayout_3DBanner;
import com.daguo.util.base.MyGridView;
import com.daguo.util.base.ViewPager_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner.TransitionEffect;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-26 下午3:17:40
 * @function ：app 主页 主界面
 */
@SuppressLint("InlinedApi")
public class Main_1Aty extends Activity implements OnClickListener {
	// private static String tag = "Main_1Aty";
	// /////////////////////////////////////////////////////////////
	private String p_id;

	/*
	 * 头部广告位
	 */

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

	private List<AddBanner> topBannerLists = new ArrayList<AddBanner>();
	private MyAdapter adapter;
	private final int MSG_TOPBANNERLISTS = 111;
	private final int MSG_MIDBANNERLISTS = 112;
	private final int MSG_BOTTBANNERLISTS = 113;
	private final int MSG_INFORMATION = 114;
	private final int MSG_FUNCTION_SUC = 115;
	private final int MSG_NEW_MSG = 116;
	private final int MSG_NO_MSG = 117;
	// /////////////////////////////////////////////////////////////////////////
	/*
	 * 消息小红点
	 */
	private ImageView remind_iv;

	/*
	 * 中间通知
	 */
	private TextView tv_news;
	private String infomationText;

	/**
	 * 中间功能按钮
	 */
	private MyGridView myGridView;
	private List<SC_SheTuan> gridLists = new ArrayList<SC_SheTuan>();
	private Main1_FunctionIconAdapter gridAdapter;

	/**
	 * 三张图片
	 */
	private ImageView iv1, iv2, iv3;
	private List<AddBanner> midBnanerLists = new ArrayList<AddBanner>();

	/**
	 * 底部活动
	 */
	private ListView listView;
	private Main_BottomBannerAdapter bottomBannerAdapter;
	private List<AddBanner> bottBannerLists = new ArrayList<AddBanner>();

	/**
	 * dialog 提示用户登录/注册
	 */
	// private Dialog dia;

	/**
	 * tools
	 */

	ImageLoader imageLoader;
	Message msg;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = mViewPager.getCurrentItem();
				if (index == topBannerLists.size() - 1) {
					index = -1;
				}
				mViewPager.setCurrentItem(index + 1);
				handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
						PHOTO_CHANGE_TIME);
				break;

			case MSG_TOPBANNERLISTS:
				// 顶部广告栏
				setDot();

				setViewpager();

				break;
			case MSG_MIDBANNERLISTS:
				// 中部广告栏
				imageLoader.displayImage(
						HttpUtil.IMG_URL + midBnanerLists.get(0).getImg_path(),
						iv1);
				imageLoader.displayImage(
						HttpUtil.IMG_URL + midBnanerLists.get(1).getImg_path(),
						iv2);
				imageLoader.displayImage(
						HttpUtil.IMG_URL + midBnanerLists.get(2).getImg_path(),
						iv3);

				iv1.setOnClickListener(new AddBannerOnclickListener(
						Main_1Aty.this, midBnanerLists, 0));
				iv2.setOnClickListener(new AddBannerOnclickListener(
						Main_1Aty.this, midBnanerLists, 1));
				iv3.setOnClickListener(new AddBannerOnclickListener(
						Main_1Aty.this, midBnanerLists, 2));

				break;

			case MSG_BOTTBANNERLISTS:

				bottomBannerAdapter = new Main_BottomBannerAdapter(
						Main_1Aty.this, bottBannerLists);
				listView.setAdapter(bottomBannerAdapter);
				PublicTools.setListViewHeightBasedOnChildren(listView);

				break;

			case MSG_INFORMATION:
				tv_news.setText(PublicTools.doWithNullData(infomationText));

				break;

			case MSG_FUNCTION_SUC:
				gridAdapter.notifyDataSetChanged();
				loadMessageInfo();

				break;
			case MSG_NEW_MSG:
				remind_iv.setVisibility(View.VISIBLE);
				// 检测到新消息

				break;
			case MSG_NO_MSG:
				// 未检测到新消息
				remind_iv.setVisibility(View.INVISIBLE);

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main1);
		imageLoader = ImageLoader.getInstance();
		p_id = getSharedPreferences("userinfo", 0).getString("id", "");
		initViews();
		setTopBanner();
		loadFunctionIconData();
		loadInfomation();

	}

	/**
	 * 实例化界面
	 */
	void initViews() {
		initHeadInfo();

		tv_news = (TextView) this.findViewById(R.id.tv_news);
		tv_news.setOnClickListener(this);

		myGridView = (MyGridView) findViewById(R.id.function_grid);
		gridAdapter = new Main1_FunctionIconAdapter(Main_1Aty.this, gridLists);
		myGridView.setAdapter(gridAdapter);
		myGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {
				Intent intent = null;
				if ("cbd55d85-b515-4958-946f-e514e23559f4".equals(gridLists
						.get(p).getMenu_id())) {// 新鲜事
					intent = new Intent(Main_1Aty.this, SC_XinWenAty.class);
					startActivity(intent);

				} else if ("e012e43b-8628-467f-9b88-99810be1ffc4"
						.equals(gridLists.get(p).getMenu_id())) {// 校园广场
					intent = new Intent(Main_1Aty.this, School_MainAty.class);
					startActivity(intent);
				} else if ("e1a2452e-8279-4869-90bf-e8299de58a80"
						.equals(gridLists.get(p).getMenu_id())) {// 社团
					intent = new Intent(Main_1Aty.this, SC_SheTuanAty.class);
					startActivity(intent);
				} else if ("34c074bc-943c-4bfb-80f9-3666a74d115d"
						.equals(gridLists.get(p).getMenu_id())) {// 移动生活
					intent = new Intent(Main_1Aty.this, OperatorAty.class);
					startActivity(intent);
				} else if ("d5f503a1-ddea-47ab-822b-6471ff25f1b9"
						.equals(gridLists.get(p).getMenu_id())) {// 课表服务
					intent = new Intent(Main_1Aty.this, Main_Aty.class);
					startActivity(intent);
				} else if ("c88e7c70-a9ef-4524-9fcd-92580480c6e9"
						.equals(gridLists.get(p).getMenu_id())) {// 校园超市
					intent = new Intent(Main_1Aty.this,
							SchoolShop_MainAty.class);
					startActivity(intent);

				} else if ("d3490024-4e1c-4b99-8eb0-ac643f09eae8"
						.equals(gridLists.get(p).getMenu_id())) {// 聚会达人
					intent = new Intent(Main_1Aty.this, Shop_SearchAty.class);
					startActivity(intent);

				} else if ("dc0d893b-69a1-4d0f-9179-6f374757773c"
						.equals(gridLists.get(p).getMenu_id())) {// 校外世界
					intent = new Intent(Main_1Aty.this, School_Main3Aty.class);
					startActivity(intent);
				}
			}
		});

		iv1 = (ImageView) this.findViewById(R.id.iv_pic1);
		iv2 = (ImageView) this.findViewById(R.id.iv_pic2);
		iv3 = (ImageView) this.findViewById(R.id.iv_pic3);
		setMidBannerDimension();

		listView = (ListView) findViewById(R.id.listView);

	}

	/**
	 * 头部按钮
	 */
	private void initHeadInfo() {
		TextView personInfo_tv = (TextView) findViewById(R.id.personInfo_tv);
		TextView function_tv = (TextView) findViewById(R.id.function_tv);
		remind_iv = (ImageView) findViewById(R.id.remind_iv);

		personInfo_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Main_1Aty.this,
						School_Main4Aty.class);
				startActivity(intent);
			}
		});
		function_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Main_1Aty.this, MessageAty.class);
				startActivity(intent);
			}
		});
		remind_iv.setVisibility(View.INVISIBLE);

	}

	/*********************** 主页广告栏模块 ******************************************************/
	/**
	 * 设置中部广告栏位的长宽尺寸
	 */
	private void setMidBannerDimension() {
		int width = GetScreenRecUtil.getWindowWidth(Main_1Aty.this) / 3;
		iv3.setLayoutParams(new LinearLayout.LayoutParams(width,
				245 * width / 230));
		iv2.setLayoutParams(new LinearLayout.LayoutParams(width,
				245 * width / 230));
		iv1.setLayoutParams(new LinearLayout.LayoutParams(width,
				245 * width / 230));
	}

	/**
	 * 专为顶部轮播banner设定的模块
	 */
	private void setTopBanner() {
		// 1 小红点 2 图片 两部分
		mViewPager = (ViewPager_3DBanner) findViewById(R.id.index_product_images_container);
		mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
		topBanner_rl = (RelativeLayout) findViewById(R.id.topBanner_rl);

		// 设置高度 长宽比 1:2
		LayoutParams params = new LayoutParams(
				GetScreenRecUtil.getWindowWidth(Main_1Aty.this),
				GetScreenRecUtil.getWindowWidth(Main_1Aty.this) / 2);
		topBanner_rl.setLayoutParams(params);

		initData();

	}

	/**
	 * 设置Viewpager界面
	 */
	private void setViewpager() {
		mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
		mViewPager.setCurrentItem(0);
		handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
		adapter = new MyAdapter();
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new MyPageChangeListener());
		mViewPager.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (topBannerLists.size() == 0 || topBannerLists.size() == 1)
					return true;
				else
					return false;
			}
		});
	}

	/**
	 * 设置圆点
	 */
	private void setDot() {
		mIndicators = new ImageView[topBannerLists.size()];
		if (topBannerLists.size() <= 1) {
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

		mImageViews = new ImageView[topBannerLists.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
	}

	/**
	 * 加载首页通知小文子
	 */
	private void loadInfomation() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_EVENT
							+ "&menu_id=f2681529-4307-44da-b267-5512f935009b&page=1&rows=1";
					String res = HttpUtil.getRequest(url);
					JSONObject object = new JSONObject(res);
					if (object.getInt("total") > 0) {
						infomationText = object.getJSONArray("rows")
								.optJSONObject(0).getString("title");
						msg = handler.obtainMessage(MSG_INFORMATION);
						msg.sendToTarget();

					} else {
						// 无显示文字
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/**
	 * 加载小圈圈按钮的数据
	 */
	private void loadFunctionIconData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_EVENT
							+ "&page=1&rows=8&menu_id=71ff981e-55b4-4a35-9ca6-5b0a15d7b6c8";
					String res = HttpUtil.getRequest(url);

					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("total") > 0) {
						//
						JSONArray array = jsonObject.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String menu_id = array.optJSONObject(i).getString(
									"id");
							String title = array.optJSONObject(i).getString(
									"title");
							String sort = array.optJSONObject(i).getString(
									"sort");
							SC_SheTuan abc = new SC_SheTuan();
							abc.setImg_path(img_path);
							abc.setMenu_id(menu_id);
							abc.setTitle(title);
							abc.setSort(sort);

							gridLists.add(abc);

						}
						msg = handler.obtainMessage(MSG_FUNCTION_SUC);
						msg.sendToTarget();

					} else {
						// 没有查询到该类信息。。
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/**
	 * 初始化广告栏的数据 包含三部分 主页轮播广告栏 ，主页中部横排三个广告 底部若干个广告竖排 tips ： 这里还缺少一个最顶部的小广告 通知栏
	 */
	private void initData() {
		new Thread(new Runnable() {
			public void run() {
				// 最多不要超过15张广告 顶部广告栏

				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=1&page=1&rows=15";
					String res = "";
					JSONObject js = null;
					int total = 0;
					res = HttpUtil.getRequest(url);
					js = new JSONObject(res);
					total = js.getInt("total");
					AddBanner list = null;
					if (total == 0) {
						// 无广告 ，或者加载异常
						Log.e("主页获取广告信息", "获取首页顶部广告信息异常");

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
							topBannerLists.add(list);
						}
						msg = handler.obtainMessage(MSG_TOPBANNERLISTS);
						msg.sendToTarget();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("主页获取广告信息", "获取广告json异常");
				} catch (Exception e) {
					Log.e("主页获取广告信息", "获取广告异常");
					e.printStackTrace();
				}
				// 中部广告栏
				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=2&page=1&rows=3";
					String res = "";
					JSONObject js = null;
					int total = 0;
					res = HttpUtil.getRequest(url);
					js = new JSONObject(res);
					total = js.getInt("total");
					AddBanner list = null;
					if (total == 0) {
						// 无广告 ，或者加载异常
						Log.e("主页获取广告信息", "获取首页顶部广告信息异常");

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
							midBnanerLists.add(list);
						}
						msg = handler.obtainMessage(MSG_MIDBANNERLISTS);
						msg.sendToTarget();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("主页获取广告信息", "获取中部广告json异常");
				} catch (Exception e) {
					Log.e("主页获取广告信息", "获取中部广告异常");
					e.printStackTrace();
				}

				// 底部广告栏 最多不要超过15张
				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=3&page=1&rows=15";
					String res = "";
					JSONObject js = null;
					int total = 0;
					res = HttpUtil.getRequest(url);
					js = new JSONObject(res);
					total = js.getInt("total");
					AddBanner list = null;
					if (total == 0) {
						// 无广告 ，或者加载异常
						Log.e("主页获取广告信息", "获取首页顶部广告信息异常");

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
							bottBannerLists.add(list);
						}
						msg = handler.obtainMessage(MSG_BOTTBANNERLISTS);
						msg.sendToTarget();
					}
				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("主页获取广告信息", "获取中部广告json异常");
				} catch (Exception e) {
					Log.e("主页获取广告信息", "获取中部广告异常");
					e.printStackTrace();
				}

			}
		}).start();

	}

	/**
	 * 加载消息通知小圆点
	 */
	private void loadMessageInfo() {
		new Thread(

		new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_MESSAGE_INFORM
							+ "&page=1&rows=15&r_id=" + p_id;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);

					if (0 < jsonObject.getInt("total")) {
						JSONArray array = jsonObject.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String status = array.optJSONObject(i).getString(
									"status");
							if ("0".equals(PublicTools.doWithNullData(status))) {
								handler.sendEmptyMessage(MSG_NEW_MSG);
								return;
							}
						}
						// 消息都阅读过
						handler.sendEmptyMessage(MSG_NO_MSG);
					} else {
						// 并没有通知
						handler.sendEmptyMessage(MSG_NO_MSG);

					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

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
					HttpUtil.IMG_URL
							+ topBannerLists.get(position).getImg_path(),
					mImageViews[position]);
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			mImageViews[position]
					.setOnClickListener(new AddBannerOnclickListener(
							Main_1Aty.this, topBannerLists, position));

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

	/********************************* split line onclick事件 ****************************************/

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.lin1:
		// Intent intents = new Intent(Main_1Aty.this,
		// UserInfo_ModifyAty1.class);
		// startActivity(intents);

		// break;
		case R.id.iv_mail:

			break;

		case R.id.tv_news:

			break;

		case R.id.iv_buttom2:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能1++",
			// Toast.LENGTH_SHORT).show();

			break;
		case R.id.iv_buttom1:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能2++",
			// Toast.LENGTH_SHORT).show();
			Intent intent5 = new Intent(Main_1Aty.this,
					SC_XinWen_AwardsAty.class);
			startActivity(intent5);
			break;

		default:
			break;
		}
	}

}
