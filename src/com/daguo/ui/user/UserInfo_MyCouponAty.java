/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.MyCouponAdapter;
import com.daguo.util.beans.CouponEntity;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-29 下午5:20:55
 * @function ： 我的优惠卷主界面
 */
public class UserInfo_MyCouponAty extends Activity {

	private final int MSG_XINWENDATA = 101;
	private String p_id;

	/**
	 * initViews
	 */

	// ============== 新闻数据 ===============
	private PullToRefreshLayout refresh_view;
	private ListView content_view;

	private CouponEntity list;
	private List<CouponEntity> lists = new ArrayList<CouponEntity>();
	private MyCouponAdapter adapter;

	// ==========
	private int pageIndex = 1;

	/**
	 * tools
	 */
	Message msg;

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_XINWENDATA:
				if (null != msg.obj) {
					@SuppressWarnings("unchecked")
					List<CouponEntity> abc = (List<CouponEntity>) msg.obj;
					lists.addAll(abc);

				} else {
					//
				}

				adapter.notifyDataSetChanged();
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
		setContentView(R.layout.aty_userinfo_mycoupon);
		p_id = getSharedPreferences("userinfo", Activity.MODE_PRIVATE)
				.getString("id", "");

		setXinWenData();
	}

	/*------------ load information ----------------------*/
	/**
	 * 加载新闻信息方法 ，设置界面
	 */
	private void setXinWenData() {
		initViews();
		initTitleView();
		loadMyCouponData();
	}

	/**
	 * 初始化View
	 */
	private void initViews() {
		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_view = (ListView) findViewById(R.id.content_view);

		refresh_view.setOnRefreshListener(new MyRefreshListenner());
		adapter = new MyCouponAdapter(UserInfo_MyCouponAty.this, lists);
		content_view.setAdapter(adapter);

		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {

				Intent intent = new Intent(UserInfo_MyCouponAty.this,
						UserInfo_MyCouponDetailAty.class);
				intent.putExtra("id", lists.get(p).getId());
				startActivity(intent);
			}
		});
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

		title_tv.setText("我的优惠劵");
		back_fram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/* ================ data ====================== */
	/**
	 * 加载我的优惠卷数据 线程加载
	 */
	private void loadMyCouponData() {
		new Thread(new Runnable() {
			public void run() {
				String url = HttpUtil.QUERY_COUPON_MYLIST + "&rows=10&page="
						+ pageIndex + "&p_id=" + p_id;
				try {

					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int aaa = js.getInt("total");
					if (aaa != 0) {
						JSONArray array = js.getJSONArray("rows");
						List<CouponEntity> abc = new ArrayList<CouponEntity>();
						for (int i = 0; i < array.length(); i++) {

							list = new CouponEntity();
							String id = array.optJSONObject(i).getString("id");
							String content = array.optJSONObject(i).getString(
									"content");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String img_src = array.optJSONObject(i).getString(
									"img_src");
							String status = array.optJSONObject(i).getString(
									"status");
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
							list.setStatus(status);
							list.setId(id);
							list.setContent(content);
							list.setImg_path(img_path);
							list.setImg_src(img_src);
							list.setTitle(title);
							list.setTitle2(title2);
							abc.add(list);

						}
						msg = handler.obtainMessage(MSG_XINWENDATA);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 空的数据

					}

				} catch (JSONException exception) {
					Log.e("我的优惠卷", "获取优惠卷json异常");
				}

				catch (Exception e) {
					Log.e("我的优惠卷", "获取优惠卷异常");
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
	@SuppressLint("HandlerLeak")
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
			lists.clear();
			adapter.notifyDataSetChanged();
			loadMyCouponData();

			// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 1000);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onLoadMore(com
		 * .daguo.libs.PullToRefreshLayout)
		 */
		@SuppressLint("HandlerLeak")
		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
			pageIndex++;
			loadMyCouponData();
			// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 1000);
		}

	}

}
