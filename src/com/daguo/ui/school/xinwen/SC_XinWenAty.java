package com.daguo.ui.school.xinwen;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.school.huodong.SC_HuoDongAty;
import com.daguo.util.adapter.BannerAdapter;
import com.daguo.util.adapter.NewsAdapter;
import com.daguo.util.base.AutoListView;
import com.daguo.util.base.AutoListView.OnLoadListener;
import com.daguo.util.base.AutoListView.OnRefreshListener;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.News;
import com.daguo.util.pulllistview.XListView;
import com.daguo.util.pulllistview.XListView.IXListViewListener;
import com.daguo.utils.HttpUtil;

public class SC_XinWenAty extends Activity implements IXListViewListener,
		OnItemClickListener {
	private ImageView backBtn;
	private XListView xListView;
	private int pageIndex = 1;
	Message msg;

	List<News> lists = new ArrayList<News>();
	NewsAdapter adapter;

	/*********************** 轮播广告 ********************/
	// 声明控件
	private ViewPager mViewPager;
	private List<ImageView> mlist;
	private TextView mTextView;
	private LinearLayout mLinearLayout;
	// 广告图素材

	private List<String> bannerImages = new ArrayList<String>();
	// 广告语
	private List<String> bannerTexts = new ArrayList<String>();
	// ViewPager适配器与监听器
	private BannerAdapter mAdapter;
	private BannerListener bannerListener;
	// 圆圈标志位
	private int pointIndex = 0;
	// 线程标志
	private boolean isStop = false;
	// banner数据
	private List<AddBanner> aaa = new ArrayList<AddBanner>();
	private AddBanner bbb;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 有新闻

				List<News> aaa = (List<News>) msg.obj;
				lists.addAll(aaa);
				adapter.notifyDataSetChanged();

				break;
			case 1:
				// 无任何新闻

				break;
			case 2:

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_news);
		init();
		initData();
		initAction();
		loadData();
		adapter = new NewsAdapter(SC_XinWenAty.this, lists);
		xListView.setAdapter(adapter);
		xListView.setOnItemClickListener(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);

		// 轮播广告的定时任务线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!isStop) {
					SystemClock.sleep(4500);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mViewPager.setCurrentItem(mViewPager
									.getCurrentItem() + 1);
						}
					});
				}
			}
		}).start();

	}

	/**
	 * 初始化 点击监听
	 */
	private void init() {
		// /listview
		backBtn = (ImageView) findViewById(R.id.friend_back);// 后退按钮
		xListView = (XListView) findViewById(R.id.xListView);// listview

		// //轮播广告
		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		mTextView = (TextView) findViewById(R.id.tv_bannertext);
		mLinearLayout = (LinearLayout) findViewById(R.id.points);

		// ///
		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 返回
				finish();
			}
		});
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long ids) {
		// // 点击进入详情
		//
		//
		// }
		// });

	}

	/**
	 * 初始化广告栏事件
	 */
	private void initAction() {
		bannerListener = new BannerListener();
		mViewPager.setOnPageChangeListener(bannerListener);
		// 取中间数来作为起始位置
		int index = (Integer.MAX_VALUE / 2)
				- (Integer.MAX_VALUE / 2 % mlist.size());
		// 用来出发监听器
		mViewPager.setCurrentItem(index);
		mLinearLayout.getChildAt(pointIndex).setEnabled(true);
	}

	/**
	 * 初始化 广告栏数据
	 */
	private void initData() {
		mlist = new ArrayList<ImageView>();
		View view;
		LayoutParams params;

		try {
			String url = HttpUtil.QUERY_ADD_BANNER
					+ "&position=13&page=1&rows=20";
			String res = HttpUtil.getRequest(url);
			JSONObject js = new JSONObject(res);
			JSONArray array = js.getJSONArray("rows");

			for (int i = 0; i < array.length(); i++) {
				bbb = new AddBanner();
				String img_path = array.optJSONObject(i).getString("img_path");
				String source_title = array.optJSONObject(i).getString(
						"source_title");
				String menu_id = array.optJSONObject(i).getString("menu_id");
				String source_id = array.optJSONObject(i)
						.getString("source_id");
				String type = array.optJSONObject(i).getString("type");
				String url_detail = array.optJSONObject(i).getString("url");
				bannerImages.add(img_path);
				bannerTexts.add("");
				bbb.setImg_path(img_path);
				bbb.setMenu_id(menu_id);
				bbb.setSource_id(source_id);
				bbb.setSource_title(source_title);
				bbb.setType(type);
				bbb.setUrl(url_detail);
				aaa.add(bbb);
			}
		} catch (Exception e) {
			Toast.makeText(SC_XinWenAty.this, "网络异常", Toast.LENGTH_SHORT)
					.show();
			finish();

		}

		for (int i = 0; i < bannerImages.size(); i++) {
			// 设置广告图
			ImageView imageView = new ImageView(SC_XinWenAty.this);
			imageView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			FinalBitmap.create(SC_XinWenAty.this).display(imageView,
					HttpUtil.IMG_URL + bannerImages.get(i));
			mlist.add(imageView);
			// 设置圆圈点
			view = new View(SC_XinWenAty.this);
			params = new LayoutParams(5, 5);
			params.leftMargin = 10;
			view.setBackgroundResource(R.drawable.point_background);
			view.setLayoutParams(params);
			view.setEnabled(false);
			mLinearLayout.addView(view);
		}
		mAdapter = new BannerAdapter(mlist, SC_XinWenAty.this);
		mViewPager.setAdapter(mAdapter);

	}

	// 实现VierPager监听器接口
	class BannerListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			final int newPosition = position % bannerImages.size();
			mTextView.setText(bannerTexts.get(newPosition));
			mLinearLayout.getChildAt(newPosition).setEnabled(true);
			mLinearLayout.getChildAt(pointIndex).setEnabled(false);
			// 更新标志位
			pointIndex = newPosition;
			mlist.get(newPosition).setOnClickListener(
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							onClicks(newPosition);
						}
					});
		}
	}

	/**
	 * 广告栏位的点击事件
	 * 
	 * @param newPosition
	 *            点击的位置
	 */
	private void onClicks(int newPosition) {
		String type = aaa.get(newPosition).getType();
		String url = aaa.get(newPosition).getUrl();
		String menu_id = aaa.get(newPosition).getMenu_id();
		String source_id = aaa.get(newPosition).getSource_id();
		if (type.equals("1")) {// 外链网址
			Toast.makeText(SC_XinWenAty.this, "这是一个连接: " + url,
					Toast.LENGTH_SHORT).show();
		} else if (type.equals("2")) {// 跳转栏目
			if (menu_id.equals("db94a88d-5c78-448b-a3a7-4af1c3850571")) {
				// 跳转新闻

			} else if (menu_id.equals("d6c986c5-8e52-485e-9a6e-d5d98480564e")) {
				// 跳转社团
				// Intent intent1 =new Intent(SC_XinWenAty.this,);
			} else if (menu_id.equals("b3b7866c-3bf9-48a7-8caa-effa1fb86782")) {
				// 校园活动
				Intent intent2 = new Intent(SC_XinWenAty.this,
						SC_HuoDongAty.class);
				startActivity(intent2);
			} else if (menu_id.equals("7176add9-6d46-4c97-8983-362848304388")) {
				// 校园新鲜事
				// Intent intent3 =new Intent(SC_XinWenAty.this,);
			}

		} else if (type.equals("3")) {
			if (menu_id.equals("db94a88d-5c78-448b-a3a7-4af1c3850571")) {
				// 跳转新闻

			} else if (menu_id.equals("d6c986c5-8e52-485e-9a6e-d5d98480564e")) {
				// 跳转社团
				// Intent intent1 =new Intent(SC_XinWenAty.this,);
			} else if (menu_id.equals("b3b7866c-3bf9-48a7-8caa-effa1fb86782")) {
				// 校园活动
				Intent intent2 = new Intent(SC_XinWenAty.this,
						SC_HuoDongAty.class);
				startActivity(intent2);
			} else if (menu_id.equals("7176add9-6d46-4c97-8983-362848304388")) {
				// 校园新鲜事
				// Intent intent3 =new Intent(SC_XinWenAty.this,);
			}
		} else if (type.equals("4")) {

		} else if (type.equals("5")) {

		}

	}

	void loadData() {
		new Thread(new Runnable() {
			public void run() {

				List<News> infos = new ArrayList<News>();
				News info = null;
				try {

					String url = HttpUtil.QUERY_NEWS + "&rows=12&page="
							+ pageIndex;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int aaa = js.getInt("total");
					if (aaa != 0) {
						JSONArray array = js.getJSONArray("rows");

						for (int i = 0; i < array.length(); i++) {
							info = new News();
							String id = array.optJSONObject(i).getString("id");
							String content = array.optJSONObject(i).getString(
									"content");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String img_src = array.optJSONObject(i).getString(
									"img_src");
							String menu_id = array.optJSONObject(i).getString(
									"menu_id");
							String school_id = array.optJSONObject(i)
									.getString("school_id");
							String status = array.optJSONObject(i).getString(
									"status");
							String title = array.optJSONObject(i).getString(
									"title");
							String title2 = array.optJSONObject(i).getString(
									"title2");

							info.setId(id);
							info.setContent(content);
							info.setImg_path(img_path);
							info.setImg_src(img_src);
							info.setMenu_id(menu_id);
							info.setSchool_id(school_id);
							info.setStatus(status);
							info.setTitle(title);
							info.setTitle2(title2);
							infos.add(info);
						}

						msg = handler.obtainMessage(0);
						msg.obj = infos;
						msg.sendToTarget();

					} else {
						// 空的数据
						msg = handler.obtainMessage(1);
						msg.sendToTarget();
					}

				} catch (Exception e) {
				}

			}
		}).start();
	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("0");
	}

	// /// 这是关闭定时任务的线程 方法
	@Override
	protected void onDestroy() {
		// 关闭定时器
		isStop = true;

		super.onDestroy();
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		lists.clear();
		loadData();

		onLoad();
	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		lists.clear();
		loadData();
		onLoad();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int p, long arg3) {
		Intent intent = new Intent(SC_XinWenAty.this, SC_XinWen_DetailAty.class);
		intent.putExtra("id", lists.get(p-1).getId());
		startActivity(intent);

	}

	/**
	 * 用于初始化界面
	 * ******************************************************************
	 * ************
	 */

}
