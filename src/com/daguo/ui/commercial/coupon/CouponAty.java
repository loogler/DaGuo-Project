package com.daguo.ui.commercial.coupon;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.libs.staggeredgridview.StaggeredGridView;
import com.daguo.util.adapter.CouponAdapter;
import com.daguo.util.beans.CouponEntity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-14 上午10:00:04
 * @function 功能:优惠卷主界面，此处领取优惠卷
 */
@SuppressLint("HandlerLeak")
public class CouponAty extends Activity implements OnItemClickListener {

	private final int MSG_COUPONLIST = 10001;

	private String p_id;
	private int pageIndex = 1;

	/**
	 * @see initViews
	 */
	private com.daguo.libs.pulltorefresh.PullToRefreshLayout pullToRefreshLayout;
	private GridView gridView;

	private List<CouponEntity> lists = new ArrayList<CouponEntity>();
	private CouponEntity list;
	private CouponAdapter adapter;

	private Message msg;
	private Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_COUPONLIST:
				if (null != msg.obj) {
					List<CouponEntity> abc = (List<CouponEntity>) msg.obj;
					lists.addAll(abc);
				} else {
					// null...
				}
				adapter.notifyDataSetChanged();

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_coupon);
		p_id = getSharedPreferences("userinfo", Activity.MODE_PRIVATE)
				.getString("id", "");
		initViews();
		initTitleView();
		loadCouponListInfo();
	}

	private void initViews() {
		pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		gridView = (GridView) findViewById(R.id.content_view);

		adapter = new CouponAdapter(this, lists);
		gridView.setAdapter(adapter);

		pullToRefreshLayout.setOnRefreshListener(new OnrefreshListener());

		gridView.setOnItemClickListener(this);

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

		title_tv.setText("优惠大放送");
		back_fram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/*-------------获取数据线程--------------------------*/
	/**
	 * 获取优惠卷列表
	 */
	private void loadCouponListInfo() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_COUPON_LIST + "&rows=12&p_id="
							+ p_id + "&page=" + pageIndex;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);

					int total = jsonObject.getInt("total");
					int totalPageNum = jsonObject.getInt("totalPageNum");
					if (totalPageNum < pageIndex) {
						// 超过最大页数,不继续加载数据
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(CouponAty.this, "加载完成，没有更多数据了",
										Toast.LENGTH_LONG).show();
							}
						});
						return;

					}
					if (total > 0) {
						JSONArray array = jsonObject.getJSONArray("rows");
						List<CouponEntity> abc = new ArrayList<CouponEntity>();
						for (int i = 0; i < array.length(); i++) {
							String id = array.optJSONObject(i).getString("id");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String title = array.optJSONObject(i).getString(
									"title");
							String status = array.optJSONObject(i).getString(
									"status");

							list = new CouponEntity();
							list.setId(id);
							list.setImg_path(img_path);
							list.setTitle(title);
							list.setStatus(status);
							abc.add(list);

						}
						msg = handler.obtainMessage(MSG_COUPONLIST);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 没有优惠卷
					}

				} catch (Exception e) {
					Log.e("获取优惠卷信息", "loadCouponListInfo  ====》error");
				}

			}
		}).start();
	}

	/**
	 * 
	 * @E-mail 作者 E-mail:395360255@qq.com
	 * @author BugsRabbit
	 * @version 创建时间：2016-2-14 上午11:42:00
	 * @function 功能:实现下拉刷新 上拉加载功能
	 */
	private class OnrefreshListener implements OnRefreshListener {

		@SuppressLint("HandlerLeak")
		@Override
		public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
			pageIndex = 1;
			lists.clear();
			adapter.notifyDataSetChanged();
			loadCouponListInfo();
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 1500);

		}

		@SuppressLint("HandlerLeak")
		@Override
		public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {

			pageIndex++;
			loadCouponListInfo();
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 1500);

		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int p, long arg3) {
		Intent intent = new Intent(CouponAty.this, CouponDetailAty.class);
		intent.putExtra("id", PublicTools.doWithNullData(lists.get(p).getId()));
		startActivity(intent);

	}

}
