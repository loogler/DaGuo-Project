/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.map;

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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.NewsAdapter;
import com.daguo.util.beans.News;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-25 下午1:36:23
 * @function ：校园地图 主界面
 */
@SuppressLint("HandlerLeak")
public class SchoolMapAty extends Activity {
	final int MSG_BANNERLISTS = 100;
	private final int MSG_XINWENDATA = 101;
	private String p_school_id;
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

	/**
	 * tools
	 */
	Message msg;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg) {
			switch (msg.what) {

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
		setContentView(R.layout.aty_schoolmap);
		p_school_id = getSharedPreferences("userinfo", 0).getString(
				"school_id", "");
		initTitleView();

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

		title_tv.setText("校园地图");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
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
		xinWenAdapter = new NewsAdapter(SchoolMapAty.this, xinWenLists);
		content_view.setAdapter(xinWenAdapter);

		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {

				Intent intent = new Intent(SchoolMapAty.this,
						SchoolMapDetailAty.class);
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
						+ "&school_id="
						+ p_school_id
						+ "&menu_id=026c8c60-a590-4e2f-9e23-176a06d9c78c&rows=10&page="
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
				@SuppressLint("HandlerLeak")
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
				@SuppressLint("HandlerLeak")
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
