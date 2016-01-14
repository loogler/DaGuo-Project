/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial.cent;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.CentAdapter;
import com.daguo.util.beans.CentGoods;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午10:22:36
 * @function ：积分商城首页
 */
public class CentAty extends Activity {

    private final int MSG_GET_CENT_SUC = 10001;
    private final int MSG_GET_CENT_FAIL = 10002;
    private final int MSG_GOODS_ITEM = 10003;

    private String p_id;
    private String score;

    private int pageIndex = 1;

    /**
     * initViews
     */
    private TextView cent_tv;

    private PullToRefreshLayout refresh_view;
    private GridView content_view;

    /**
     * data
     * 
     */
    private List<CentGoods> lists = new ArrayList<CentGoods>();
    private CentGoods list;
    private CentAdapter adapter;

    // tools
    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_GET_CENT_SUC:
		cent_tv.setText(score);
		break;
	    case MSG_GET_CENT_FAIL:
		cent_tv.setText("积分信息异常");

		break;
	    case MSG_GOODS_ITEM:
		if (null != msg.obj) {
		    @SuppressWarnings("unchecked")
		    List<CentGoods> abc = (List<CentGoods>) msg.obj;
		    lists.addAll(abc);
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
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_cent);
	MyAppliation.getInstance().addActivity(this);
	p_id = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE)
		.getString("id", "");
	if ("".equals(PublicTools.doWithNullData(p_id))) {
	    Toast.makeText(this, "用户信息异常！请重新登录", Toast.LENGTH_LONG).show();
	    System.exit(0);
	}

	initViews();
	loadCentData();

	loadItemsData();
	adapter = new CentAdapter(CentAty.this, lists);
	content_view.setAdapter(adapter);
	content_view.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int p,
		    long arg3) {
		Intent intent = new Intent(CentAty.this,Cent_DetailAty.class);
		intent.putExtra("id", lists.get(p).getId());
		startActivity(intent);
	    }
	});

    }

    /**
     * 
     */
    private void initViews() {
	initHeadView();
	cent_tv = (TextView) findViewById(R.id.cent_tv);
	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (GridView) findViewById(R.id.content_view);
	refresh_view.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {
			pageIndex = 1;
			loadItemsData();
			pullToRefreshLayout
				.refreshFinish(PullToRefreshLayout.SUCCEED);
		    }
		}, 1500);
	    }

	    @Override
	    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {
			pageIndex++;
			loadItemsData();
			pullToRefreshLayout
				.refreshFinish(PullToRefreshLayout.SUCCEED);
		    }
		}, 1500);
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
	title_tv.setText("积分商城");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 积分信息获取
     */
    private void loadCentData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_USERINFO + "&id=" + p_id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") > 0) {

			JSONArray array = jsonObject.getJSONArray("rows");

			score = array.optJSONObject(0).getString("score");
			msg = handler.obtainMessage(MSG_GET_CENT_SUC);
			msg.sendToTarget();

		    } else {
			Log.e("获取个人积分出错", "个人信息异常");
			msg = handler.obtainMessage(MSG_GET_CENT_FAIL);
			msg.sendToTarget();
		    }

		} catch (Exception e) {
		}
	    }
	}).start();

    }

    /**
     * 
     */
    private void loadItemsData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_CENTGOODS + "&rows=12&page="
			    + pageIndex;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    List<CentGoods> abc = new ArrayList<CentGoods>();
		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String id = array.optJSONObject(i).getString("id");
			    String name = array.optJSONObject(i).getString(
				    "name");
			    String thumb_path = array.optJSONObject(i)
				    .getString("thumb_path");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");
			    String score = array.optJSONObject(i).getString(
				    "score");
			    String good_desc = array.optJSONObject(i)
				    .getString("good_desc");

			    list = new CentGoods();
			    list.setGood_desc(good_desc);
			    list.setId(id);
			    list.setImg_path(img_path);
			    list.setName(name);
			    list.setScore(score);
			    list.setThumb_path(thumb_path);
			    abc.add(list);

			}

			msg = handler.obtainMessage(MSG_GOODS_ITEM);
			msg.obj = abc;
			msg.sendToTarget();
		    } else {
			Log.e("积分商品列表为空", "查不到该类商品");
		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }
}
