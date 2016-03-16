/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.xinxianshi;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.adapter.NewsAdapter;
import com.daguo.util.base.FrameLayout_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner.TransitionEffect;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.News;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-27 上午11:45:37
 * @function ： 校园新鲜事主界面
 */
public class SC_XinXianShiAty extends Activity {
	private final int MSG_BANNERLISTS = 100;
	private final int MSG_XINWENDATA = 101;

	/**
	 * initViews
	 */

	// ============== 新闻数据 ===============
	private PullToRefreshLayout refresh_view;
	private ListView content_view;

	private News xinWenList;
	private List<News> xinWenLists = new ArrayList<News>();
	private NewsAdapter xinWenAdapter;

	// ==========
	private int pageIndex = 1;

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
	private List<AddBanner> addLists = new ArrayList<AddBanner>();
	private MyAdapter adapter;

	/**
	 * data
	 */

	/**
	 * tools
	 */
	Message msg;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = mViewPager.getCurrentItem();
				if (index == addLists.size() - 1) {
					index = -1;
				}
				mViewPager.setCurrentItem(index + 1);
				handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
						PHOTO_CHANGE_TIME);
				break;

			case MSG_BANNERLISTS:
				setDot();
				setViewpager();
				break;

			case MSG_XINWENDATA:
				xinWenAdapter.notifyDataSetChanged();
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
		setContentView(R.layout.aty_event_news);
		initTitleView();
		setTopBanner();

		setXinWenData();
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

		title_tv.setText("校园新鲜事");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/********************* 3d 广告栏 *****************************************/
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
				GetScreenRecUtil.getWindowWidth(SC_XinXianShiAty.this),
				GetScreenRecUtil.getWindowWidth(SC_XinXianShiAty.this) / 2);
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
				if (addLists.size() == 0 || addLists.size() == 1)
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
		mIndicators = new ImageView[addLists.size()];
		if (addLists.size() <= 1) {
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

		mImageViews = new ImageView[addLists.size()];

		for (int i = 0; i < mImageViews.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			mImageViews[i] = imageView;
		}
	}

	/**
	 * 
	 * @author : BugsRabbit
	 * @email 395360255@qq.com
	 * @version 创建时间：2015-11-27 下午4:33:26
	 * @function ：跳转适配器
	 */
	public class MyAdapter extends PagerAdapter {

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

			FinalBitmap.create(SC_XinXianShiAty.this).display(
					mImageViews[position],
					HttpUtil.IMG_URL + addLists.get(position).getImg_path());
			((ViewPager) container).addView(mImageViews[position], 0);
			mViewPager.setObjectForPosition(mImageViews[position], position);
			mImageViews[position]
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {
							new AddBannerOnclickListener(SC_XinXianShiAty.this,
									addLists, position);
						}
					});

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

	private void initData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=13&page=1&rows=15";
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
							addLists.add(list);
						}
						msg = handler.obtainMessage(MSG_BANNERLISTS);
						msg.sendToTarget();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("新闻获取广告信息", "获取广告json异常");
				} catch (Exception e) {
					Log.e("新闻获取广告信息", "获取广告异常");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/************************* 加载新闻信息 **************************************/
	/**
	 * 加载新闻信息方法 ，设置界面
	 */
	private void setXinWenData() {
		initXinWenViews();
		loadXinWenData();
	}

	/**
	 * 初始化新闻的View
	 */
	private void initXinWenViews() {
		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_view = (ListView) findViewById(R.id.content_view);

		refresh_view.setOnRefreshListener(new MyRefreshListenner());
		xinWenAdapter = new NewsAdapter(SC_XinXianShiAty.this, xinWenLists);
		content_view.setAdapter(xinWenAdapter);

		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {

				Intent intent = new Intent(SC_XinXianShiAty.this,
						SC_XinXianShiDetailAty.class);
				intent.putExtra("id", xinWenLists.get(p).getId());
				startActivity(intent);
			}
		});
	}

	/**
	 * 加载新闻数据 线程加载
	 */
	private void loadXinWenData() {
		new Thread(new Runnable() {
			public void run() {
				String url = HttpUtil.QUERY_EVENT
						+ "&menu_id=7176add9-6d46-4c97-8983-362848304388&rows=10&page="
						+ pageIndex;
				try {

					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int aaa = js.getInt("total");
					if (aaa != 0) {
						JSONArray array = js.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {

							xinWenList = new News();
							String id = array.optJSONObject(i).getString("id");
							String content = array.optJSONObject(i).getString(
									"content");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String img_src = array.optJSONObject(i).getString(
									"img_src");
							String menu_id = array.optJSONObject(i).getString(
									"menu_id");
							String good_count = array.optJSONObject(i)
									.getString("good_count");
							String feedback_count = array.optJSONObject(i)
									.getString("feedback_count");
							String title = array.optJSONObject(i).getString(
									"title");
							String title2 = array.optJSONObject(i).getString(
									"title2");
							if (img_src != null && !img_src.equals("")
									&& !img_src.equals("null")) {

								String[] imgs = img_src.split(",");
								for (int j = 0; j < imgs.length; j++) {
									content = content.replaceAll(imgs[j],
											"http://115.29.224.248:8080"
													+ imgs[j]);

								}
							}
							xinWenList.setId(id);
							xinWenList.setContent(content);
							xinWenList.setImg_path(img_path);
							xinWenList.setImg_src(img_src);
							xinWenList.setMenu_id(menu_id);
							xinWenList.setGoodCount(good_count);
							xinWenList.setFeedbackCount(feedback_count);
							xinWenList.setTitle(title);
							xinWenList.setTitle2(title2);
							xinWenLists.add(xinWenList);

						}
						msg = handler.obtainMessage(MSG_XINWENDATA);
						msg.sendToTarget();

					} else {
						// 空的数据

					}

				} catch (JSONException exception) {
					Log.e("校园新闻", "获取新闻信息json异常");
				}

				catch (Exception e) {
					Log.e("校园新闻", "获取新闻信息异常");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 
	 * @author : BugsRabbit
	 * @email 395360255@qq.com
	 * @version 创建时间：2015-12-1 下午2:45:23
	 * @function ：刷新监听事件
	 */
	private class MyRefreshListenner implements OnRefreshListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onRefresh(com
		 * .daguo.libs.PullToRefreshLayout)
		 */
		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			pageIndex = 1;
			loadXinWenData();

			// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 3000);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onLoadMore(com
		 * .daguo.libs.PullToRefreshLayout)
		 */
		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			pageIndex++;
			loadXinWenData();
			// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 3000);
		}

	}

}
