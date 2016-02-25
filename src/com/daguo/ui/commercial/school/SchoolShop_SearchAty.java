package com.daguo.ui.commercial.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.Main_2Adapter;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-23 上午11:12:46
 * @function 功能:校园商城搜索界面 （原界面与需求不符 后增加）——
 */
public class SchoolShop_SearchAty extends FragmentActivity implements
		OnClickListener, OnRefreshListener {

	private final int MSG_GOOD_DATA_SUC = 1001;
	private final int MSG_GOOD_DATA_FAIL = 1002;
	private String loadType;// 是否是学校超市还是商城的区分

	/**
	 * initViews
	 */
	private EditText search_edt;
	private TextView submit_tv, back_tv;

	private PullToRefreshLayout refresh_view;
	private GridView content_view;

	/**
	 * data
	 */
	private String searchString;
	private int pageIndex = 1;

	private List<Shop_GoodsItem> lists = new ArrayList<Shop_GoodsItem>();
	private Shop_GoodsItem list;
	private Main_2Adapter adapter;

	/**
	 * tools
	 */

	private Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_GOOD_DATA_SUC:

				if (msg.obj != null) {

					@SuppressWarnings("unchecked")
					List<Shop_GoodsItem> abcGoodsItems = (List<Shop_GoodsItem>) msg.obj;
					lists.addAll(abcGoodsItems);
					adapter.notifyDataSetChanged();
					type_id = "";

				}

				break;
			case MSG_GOOD_DATA_FAIL:
				Toast.makeText(SchoolShop_SearchAty.this, "没有找到该商品",
						Toast.LENGTH_SHORT).show();

				break;

			default:
				break;
			}

		};
	};

	String type_id, id, school_id;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_schoolshop_search);

		initViews();
		Intent intent = getIntent();
		type_id = PublicTools.doWithNullData(intent.getStringExtra("type_id"));
		id = PublicTools.doWithNullData(intent.getStringExtra("id"));
		loadType = PublicTools.doWithNullData(intent.getStringExtra("type"));
		school_id = getSharedPreferences("userinfo", 0).getString("school_id",
				"");
		loadGoodsData();

	}

	/**
     * 
     */
	private void initViews() {
		search_edt = (EditText) findViewById(R.id.search_edt);
		submit_tv = (TextView) findViewById(R.id.submit_tv);
		back_tv = (TextView) findViewById(R.id.back_tv);

		submit_tv.setOnClickListener(this);
		back_tv.setOnClickListener(this);

		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_view = (GridView) findViewById(R.id.content_view);

		refresh_view.setOnRefreshListener(this);
		adapter = new Main_2Adapter(this, lists);
		content_view.setAdapter(adapter);

		// goods
		// comprehensive_iv = (ImageView) findViewById(R.id.comprehensive_iv);
		// number_iv = (ImageView) findViewById(R.id.number_iv);
		// price_iv = (ImageView) findViewById(R.id.price_iv);
		// comprehensive_tv = (TextView) findViewById(R.id.comprehensive_tv);
		// number_tv = (TextView) findViewById(R.id.number_tv);
		// price_tv = (TextView) findViewById(R.id.price_tv);

	}

	/***************** get data *******************************************************/
	private void loadGoodsData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = null;
					if (loadType.equals("school")) {
						url = HttpUtil.QUERY_GOODSLIST_SCHOOL
								+ "&rows=20&page=" + pageIndex;
					} else {
						url = HttpUtil.QUERY_GOODSLIST + "&rows=20&page="
								+ pageIndex;
					}
					Map<String, String> map = new HashMap<String, String>();
					if (null != searchString) {
						map.put("name", searchString);
					}
					if (loadType.equals("school")) {
						map.put("school_id", school_id);
					}
					map.put("type_id", type_id);
					String res = HttpUtil.postRequest(url, map);
					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("totalPageNum") < pageIndex) {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SchoolShop_SearchAty.this,
										"加载完成，到底了。。", Toast.LENGTH_LONG).show();
							}
						});
						return;
					}
					if (jsonObject.getInt("total") > 0) {
						JSONArray array = jsonObject.getJSONArray("rows");
						List<Shop_GoodsItem> abcGoodsItems = new ArrayList<Shop_GoodsItem>();
						for (int i = 0; i < array.length(); i++) {
							list = new Shop_GoodsItem();
							String thumb_path = array.optJSONObject(i)
									.getString("thumb_path");
							String name = array.optJSONObject(i).getString(
									"name");
							String id = array.optJSONObject(i).getString("id");
							String price = array.optJSONObject(i).getString(
									"price");

							list.setThumb_path(thumb_path);
							list.setName(name);
							list.setId(id);
							list.setPrice(price);
							abcGoodsItems.add(list);

						}
						msg = handler.obtainMessage(MSG_GOOD_DATA_SUC);
						msg.obj = abcGoodsItems;
						msg.sendToTarget();
					} else {
						// 无此商品
						msg = handler.obtainMessage(MSG_GOOD_DATA_FAIL);
						msg.sendToTarget();
					}
				} catch (JSONException e) {
					Log.e("商品搜索", "json 异常");
				} catch (Exception e) {
					Log.e("商品搜索", "获取数据异常");
				}
			}
		}).start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_tv:
			System.gc();
			finish();
			break;
		case R.id.submit_tv:
			lists.clear();
			searchString = search_edt.getText().toString().trim();
			PublicTools.doWithNullData(searchString);
			loadGoodsData();
			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onRefresh
	 * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				pageIndex = 1;
				loadGoodsData();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 1500);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onLoadMore
	 * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
			public void run() {
				pageIndex++;
				loadGoodsData();
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 1500);

	}

}
