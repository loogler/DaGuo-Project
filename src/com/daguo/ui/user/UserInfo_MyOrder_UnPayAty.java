/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.UserInfo_MyOrderAdapter;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-28 上午10:53:51
 * @function ：未付款的订单 主界面
 */
public class UserInfo_MyOrder_UnPayAty extends Activity {
    private final int MSG_ORDERINFO = 10001;

    private int pageIndex = 1;
    private String p_id;

    /**
     * @see initViews
     */
    private PullToRefreshLayout refresh_view;
    private ListView content_view;

    private List<Shop_GoodsItem> lists = new ArrayList<Shop_GoodsItem>();
    private Shop_GoodsItem list;
    private UserInfo_MyOrderAdapter adapter;

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_ORDERINFO:
		if (null != msg.obj) {
		    @SuppressWarnings("unchecked")
		    List<Shop_GoodsItem> abc = (List<Shop_GoodsItem>) msg.obj;
		    lists.addAll(abc);
		    adapter.notifyDataSetChanged();
		} else {
		    // ...
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
	setContentView(R.layout.aty_userinfo_myorder_unpay);
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");
	initViews();
	loadOrderInfo();

	adapter = new UserInfo_MyOrderAdapter(this, lists);
	content_view.setAdapter(adapter);

    }

    /**
     * initViews
     */
    @SuppressLint("HandlerLeak")
    private void initViews() {
	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (ListView) findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pageIndex=1;
		lists.clear();
		adapter.notifyDataSetChanged();
		loadOrderInfo();
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
		loadOrderInfo();
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

    /*------- 数据线程--------------------------------------------*/
    private void loadOrderInfo() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_MYORDER + "&rows=15&page="
			    + pageIndex + "&p_id=" + p_id+"&pay_status=0";
		    String res = HttpUtil.getRequest(url);

		    JSONObject jsonObject = new JSONObject(res);

		    if (!"0".equals(jsonObject.getString("rows"))) {
			// 有效数据
			if (pageIndex > jsonObject.getInt("totalPageNum")) {
			    runOnUiThread(new Runnable() {
				public void run() {
				    Toast.makeText(
					    UserInfo_MyOrder_UnPayAty.this,
					    "加载完成，没有更多数据了", Toast.LENGTH_SHORT)
					    .show();
				}
			    });
			} else {
			    JSONArray array = jsonObject.getJSONArray("rows");
			    List<Shop_GoodsItem> abc = new ArrayList<Shop_GoodsItem>();
			    for (int i = 0; i < array.length(); i++) {

				String id = array.optJSONObject(i).getString(
					"goods_id");
				String pay_status = array.optJSONObject(i)
					.getString("pay_status");
				String number = array.optJSONObject(i)
					.getString("tgdcount");
				String orderId = array.optJSONObject(i)
					.getString("id");
				String price = array.optJSONObject(i)
					.getString("tgdprice");
				String name = array.optJSONObject(i).getString(
					"tgsname");
				String thumb_path = array.optJSONObject(i)
					.getString("thumb_path");
				// String orderId
				// =array.optJSONObject(i).getString("id");

				list = new Shop_GoodsItem();
				list.setThumb_path(thumb_path);
				list.setName(name);
				list.setPrice(price);
				list.setNumber(number);
				list.setPay_status(pay_status);
				list.setId(id);
				list.setOrderId(orderId);
				abc.add(list);

			    }
			    msg = handler.obtainMessage(MSG_ORDERINFO);
			    msg.obj = abc;
			    msg.sendToTarget();

			}

		    } else {
			// 数据不存在或者为0
		    }

		} catch (Exception e) {
		    Log.e("用户订单信息 ", "订单信息获取异常");
		    return;
		}
	    }
	}).start();
    }
}
