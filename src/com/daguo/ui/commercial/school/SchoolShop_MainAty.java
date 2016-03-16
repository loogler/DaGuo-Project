/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial.school;

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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.commercial.Shop_GoodsDetailAty;
import com.daguo.util.adapter.Main_2Adapter;
import com.daguo.util.adapter.Main_2_GoodTypeAdapter;
import com.daguo.util.base.GoodTypeItem;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-30 下午2:57:12
 * @function ： 校园超市主界面
 */
@SuppressLint("HandlerLeak")
public class SchoolShop_MainAty extends Activity {
	private final int MSG_GOOD_TYPE = 10001;
	private final int MSG_GOOD_ITEM = 10002;
	private String p_school_id;

	private int pageIndex = 1;

	/**
	 * initViews
	 */
	private GridView goodstype_grid;
	private PullToRefreshLayout refresh_view;
	private GridView content_view;

	//

	// 商品信息
	private Shop_GoodsItem goodsItemList;
	private List<Shop_GoodsItem> goodsItemLists = new ArrayList<Shop_GoodsItem>();
	private Main_2Adapter adapter;

	/**
	 * data
	 */
	private Main_2_GoodTypeAdapter typeAdapter;
	private List<GoodTypeItem> goodTypeLists = new ArrayList<GoodTypeItem>();

	/**
	 * tools
	 */
	private Message msg;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_GOOD_TYPE:
				typeAdapter.notifyDataSetChanged();

				break;
			case MSG_GOOD_ITEM:
				if (msg.obj == null) {
					return;
				}
				@SuppressWarnings("unchecked")
				List<Shop_GoodsItem> aaa = (List<Shop_GoodsItem>) msg.obj;
				if (aaa.size() <= 1) {
					Toast.makeText(SchoolShop_MainAty.this, "没有更多数据了",
							Toast.LENGTH_SHORT).show();
				}
				goodsItemLists.addAll(aaa);
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
		setContentView(R.layout.aty_schoolshop_main);
		p_school_id = getSharedPreferences("userinfo", Activity.MODE_PRIVATE)
				.getString("school_id", "");
		if ("".equals(PublicTools.doWithNullData(p_school_id))) {
			Toast.makeText(this, "学校信息异常，请及时联系管员", Toast.LENGTH_LONG).show();
		}
		initTitleView();
		initViews();

		loadTypeInfo();
		loadGoodsItemsData();

		typeAdapter = new Main_2_GoodTypeAdapter(this, goodTypeLists);
		goodstype_grid.setAdapter(typeAdapter);

		adapter = new Main_2Adapter(this, goodsItemLists);
		content_view.setAdapter(adapter);

	}

	/**
	 * 实例化
	 */
	@SuppressLint("HandlerLeak")
	private void initViews() {
		goodstype_grid = (GridView) findViewById(R.id.goodstype_grid);
		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_view = (GridView) findViewById(R.id.content_view);

		goodstype_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SchoolShop_MainAty.this,
						SchoolShop_SearchAty.class);
				intent.putExtra("type", "school");
				intent.putExtra("type_id", goodTypeLists.get(arg2).getType_id());
				startActivity(intent);
			}
		});
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int p,
					long arg3) {
				Intent intent = new Intent(SchoolShop_MainAty.this,
						Shop_GoodsDetailAty.class);

				intent.putExtra("id", goodsItemLists.get(p).getId());
				startActivity(intent);
			}
		});

		refresh_view.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
				pageIndex = 1;
				goodsItemLists.clear();
				adapter.notifyDataSetChanged();
				loadGoodsItemsData();

				// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// 千万别忘了告诉控件刷新完毕了哦！

						pullToRefreshLayout
								.refreshFinish(PullToRefreshLayout.SUCCEED);
					}
				}.sendEmptyMessageDelayed(0, 1500);

			}

			@Override
			public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
				pageIndex++;
				loadGoodsItemsData();
				// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
				new Handler() {
					@Override
					public void handleMessage(Message msg) {
						// 千万别忘了告诉控件刷新完毕了哦！

						pullToRefreshLayout
								.refreshFinish(PullToRefreshLayout.SUCCEED);
					}
				}.sendEmptyMessageDelayed(0, 1500);
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

		title_tv.setText("校园超市");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}
	/*-*---------加载数据-------------*/

	/**
	 * 加载分类信息
	 */
	private void loadTypeInfo() {

		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_SCHOOL_GOODSTYPE
							+ "&page=1&rows=10";
					;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("total") != 10) {
						// 栏目不等于10个 会出现异常布局
						Log.e("商品类型", "商品分类查询数量不为10个，导致布局异常");
					} else if (jsonObject.getInt("total") > 0) {
						// 栏目有数据
						GoodTypeItem goodTypeItem = null;
						JSONArray array = jsonObject.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String type_name = array.optJSONObject(i)
									.getString("type_name");
							String type_id = array.optJSONObject(i).getString(
									"type_id");
							String img_path = array.optJSONObject(i).getString(
									"img_path");

							goodTypeItem = new GoodTypeItem();
							goodTypeItem.setImg_path(img_path);
							goodTypeItem.setType_id(type_id);
							goodTypeItem.setType_name(type_name);

							goodTypeLists.add(goodTypeItem);

						}
						// goodTypeItem = new GoodTypeItem();
						// goodTypeItem.setImg_path("1");
						// goodTypeItem.setType_id("");
						// goodTypeItem.setType_name("更多分类");

						msg = handler.obtainMessage(MSG_GOOD_TYPE);
						msg.sendToTarget();

					} else {
						Log.e("商品类型", "商品分类查询时返回数据为0或者其他，请检查上传分类表");

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("主页获取分类信息", "获取分类json异常");
				} catch (Exception e) {
					Log.e("主页获取分类信息", "获取分类异常");
					e.printStackTrace();
				}
			}
		}).start();

	}

	/**
	 * 加载商品列表
	 */
	private void loadGoodsItemsData() {

		new Thread(new Runnable() {
			public void run() {
				try {
					// &type_id=a6ad60a7-a587-4216-b83d-54094b05af5b
					String url = HttpUtil.QUERY_SCHOOL_GOODSITEM
							+ "&school_id=" + p_school_id + "&rows=20&page="
							+ pageIndex;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("totalPageNum") < pageIndex) {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SchoolShop_MainAty.this,
										"加载完成，到底了。。", Toast.LENGTH_LONG).show();
							}
						});
						return;
					}
					if (jsonObject.getInt("total") > 0) {
						JSONArray array = jsonObject.getJSONArray("rows");
						List<Shop_GoodsItem> abc = new ArrayList<Shop_GoodsItem>();
						for (int i = 0; i < array.length(); i++) {
							goodsItemList = new Shop_GoodsItem();

							String thumb_path = array.optJSONObject(i)
									.getString("thumb_path");
							String name = array.optJSONObject(i).getString(
									"name");
							String id = array.optJSONObject(i).getString("id");
							String price = array.optJSONObject(i).getString(
									"price");

							goodsItemList.setId(id);
							goodsItemList.setThumb_path(thumb_path);
							goodsItemList.setName(name);
							goodsItemList.setPrice(price);

							abc.add(goodsItemList);

						}
						msg = handler.obtainMessage(MSG_GOOD_ITEM);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 不存在的商品类别

					}
				} catch (Exception e) {
				}
			}
		}).start();

	}

}
