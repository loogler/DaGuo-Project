/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.commercial.Shop_CartAdapter.CheckChange;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-28 下午9:42:14
 * @function ：购物车
 */
@SuppressLint("HandlerLeak")
public class Shop_CartAty extends Activity implements CheckChange,
	OnRefreshListener {

    private final int MSG_CARTDATA = 10001;
    private String p_id;
    /**
     * initViews
     * 
     */
    private CheckBox all_ckb;
    private TextView pay_tv, totalPrice_tv;

    private ListView contentView;
    private PullToRefreshLayout refresh_view;

    /**
     * Data
     */
    private List<Shop_GoodsItem> lists = new ArrayList<Shop_GoodsItem>();
    private Shop_GoodsItem list;
    private Shop_CartAdapter adapter;

    private int totalPrice = 0;

    private boolean[] is_choice;

    private int pageIndex = 1;

    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CARTDATA:
		if (null != msg.obj) {
		    List<Shop_GoodsItem> aaaGoodsItems = (List<Shop_GoodsItem>) msg.obj;
		    lists.addAll(aaaGoodsItems);
		    adapter.notifyDataSetChanged();

		}

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
	setContentView(R.layout.aty_shop_cart);
	is_choice = new boolean[lists.size()];
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");

	// List<Shop_GoodsItem> ls = new ArrayList<Shop_GoodsItem>();
	//
	// SharedPreferences sp = getSharedPreferences("order", 0);
	// String cart = sp.getString("cart", "");
	// if ("".equals(PublicTools.doWithNullData(cart))) {
	// Toast.makeText(Shop_CartAty.this, "购物车为空", Toast.LENGTH_LONG)
	// .show();
	// } else {
	// // 有商品
	// try {
	// JSONArray array = new JSONArray(cart);
	// for (int i = 0; i < array.length(); i++) {
	// Shop_GoodsItem sc = (Shop_GoodsItem) array.get(i);
	// ls.add(sc);
	//
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	//
	// }

	initViews();
	loadCartData();

	adapter = new Shop_CartAdapter(this, lists);
	contentView.setAdapter(adapter);

    }

    /**
     * 
     */
    private void initViews() {
	initHeadView();
	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	contentView = (ListView) findViewById(R.id.content_view);

	all_ckb = (CheckBox) findViewById(R.id.all_ckb);

	pay_tv = (TextView) findViewById(R.id.pay_tv);
	totalPrice_tv = (TextView) findViewById(R.id.totalPrice_tv);

	refresh_view.setOnRefreshListener(this);

	contentView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3) {
		Intent intent = new Intent(Shop_CartAty.this,
			Shop_OrderAty.class);
		intent.putExtra("id", lists.get(arg2).getId());
		intent.putExtra("name", lists.get(arg2).getName());
		intent.putExtra("price", lists.get(arg2).getPrice());
		intent.putExtra("pic", lists.get(arg2).getThumb_path());
		startActivity(intent);

	    }
	});

	all_ckb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    @Override
	    public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

		int isChoice_all = 0;
		if (arg1) {
		    for (int i = 0; i < lists.size(); i++) {
			((CheckBox) (contentView.getChildAt(i))
				.findViewById(R.id.cart_ckb)).setChecked(true);
		    }
		} else {
		    for (int i = 0; i < lists.size(); i++) {
			if (((CheckBox) (contentView.getChildAt(i))
				.findViewById(R.id.cart_ckb)).isChecked()) {
			    isChoice_all += 1;
			}
		    }
		    if (isChoice_all == lists.size()) {
			for (int i = 0; i < lists.size(); i++) {
			    ((CheckBox) (contentView.getChildAt(i))
				    .findViewById(R.id.cart_ckb))
				    .setChecked(false);
			}
		    }
		}
	    }
	});
    }

    /**
     * 通用的headview 不同位置会出现不同的页面要求，根据情况设置
     */
    private void initHeadView() {
	TextView back_tView = (TextView) findViewById(R.id.back_tv);
	TextView title_tv = (TextView) findViewById(R.id.title_tv);
	TextView function_tv = (TextView) findViewById(R.id.function_tv);
	ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

	back_tView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		finish();
	    }
	});
	title_tv.setText("购物车");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.ui.commercial.Shop_CartAdapter.CheckChange#getChoiceData(int,
     * boolean)
     */
    @Override
    public void getChoiceData(int position, boolean isChoice) {

	if (isChoice) {
	    if (lists.size() != 0) {

		totalPrice += Float.valueOf(lists.get(position).getPrice()
			.trim());
	    }
	} else {
	    if (lists.size() != 0) {
		totalPrice -= Float.valueOf(lists.get(position).getPrice()
			.trim());
	    }
	}

	int num_choice = 0;
	for (int i = 0; i < lists.size(); i++) {

	    if (null != contentView.getChildAt(i)
		    && ((CheckBox) (contentView.getChildAt(i))
			    .findViewById(R.id.cart_ckb)).isChecked()) {

		num_choice += 1;
		is_choice[i] = true;
	    }
	}

	if (num_choice == lists.size()) {
	    all_ckb.setChecked(true);
	} else {
	    all_ckb.setChecked(false);
	}

	totalPrice_tv.setText(totalPrice + "");

    }

    /******************** data thread ******************************/

    private void loadCartData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_CART + "&page=1&rows=10&p_id="
			    + p_id;// +
		    // pid

		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("totalPageNum") < pageIndex) {
			runOnUiThread(new Runnable() {
			    public void run() {
				Toast.makeText(Shop_CartAty.this, "加载完成，到底了。。",
					Toast.LENGTH_LONG).show();
			    }
			});
			return;
		    }
		    if (jsonObject.getInt("total") > 0) {

			List<Shop_GoodsItem> abc = new ArrayList<Shop_GoodsItem>();
			JSONArray array = jsonObject.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String id = array.optJSONObject(i).getString("id");
			    String name = array.optJSONObject(i).getString(
				    "name");
			    String price = array.optJSONObject(i).getString(
				    "price");
			    String thumb_path = array.optJSONObject(i)
				    .getString("thumb_path");

			    list = new Shop_GoodsItem();
			    list.setName(name);
			    list.setId(id);
			    list.setPrice(price);
			    list.setThumb_path(thumb_path);
			    abc.add(list);
			}

			msg = handler.obtainMessage(MSG_CARTDATA);
			msg.obj = abc;
			msg.sendToTarget();
		    } else {
			// 没有数据
		    }

		} catch (Exception e) {
		    Log.e("商品购物车列表数据", "加载异常");
		}
	    }
	}).start();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onRefresh
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	pageIndex = 1;
	loadCartData();

	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// 千万别忘了告诉控件刷新完毕了哦！

		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 3000);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onLoadMore
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	pageIndex++;
	loadCartData();
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// 千万别忘了告诉控件刷新完毕了哦！

		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 3000);

    }

}
