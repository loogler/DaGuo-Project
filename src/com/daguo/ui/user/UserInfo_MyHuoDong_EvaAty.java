/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.libs.staggeredgridview.StaggeredGridView;
import com.daguo.libs.staggeredgridview.StaggeredGridView.OnLoadmoreListener;
import com.daguo.util.adapter.SC_HuoDongAdapter1;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit 
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-25 下午2:25:38
 * @function ：  我的活动---我点赞的活动。
 */
public class UserInfo_MyHuoDong_EvaAty  extends Activity{
    


    private final int MSG_INITDATA = 100;
    private String p_id;

    /**
     * initViews
     */
    // private GridView gridView;
    private ImageView add_iv;
    private StaggeredGridView gridView;

    /**
     * data
     */
    private List<SC_SheTuan> lists = new ArrayList<SC_SheTuan>();
    private SC_SheTuan list = null;
    private SC_HuoDongAdapter1 adapter = null;

    /**
     * tools
     */
    private int pageIndex = 1;
    Message msg;

    Handler handler = new Handler() {
	public void handleMessage(Message msg) {

	    switch (msg.what) {
	    case MSG_INITDATA:

		if (lists.size() > 0) {
		    lists.clear();
		}
		List<SC_SheTuan> abc = (List<SC_SheTuan>) msg.obj;
		lists.addAll(abc);

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
	setContentView(R.layout.aty_myhuodong);
	p_id = getSharedPreferences("userinfo", Activity.MODE_PRIVATE)
		.getString("id", "");
	initTitleView();
	initViews();
	loadData();

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

		title_tv.setText("我的活动");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}
    /**
     * 
     */
    private void initViews() {
	gridView = (StaggeredGridView) findViewById(R.id.staggeredGridView);
	add_iv = (ImageView) findViewById(R.id.add_iv);
	// ((PullToRefreshLayout) findViewById(R.id.refresh_view))
	// .setOnRefreshListener(new MyListener());

	int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
	gridView.setItemMargin(margin);
	gridView.setPadding(margin, 0, margin, 0);

	adapter = new SC_HuoDongAdapter1(UserInfo_MyHuoDong_EvaAty.this, lists);
	gridView.setAdapter(adapter);
	// gridView.setOnItemClickListener(new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> arg0, View v, int p,
	// long arg3) {
	// // Toast.makeText(SC_SheTuanAty.this, "dianji " + p,
	// // Toast.LENGTH_SHORT).show();
	//
	// Intent intent = new Intent(SC_SheTuanAty.this,
	// SC_SheTuanDetailAty.class);
	// intent.putExtra("id", lists.get(p).getId());
	// startActivity(intent);
	// }
	// });
	gridView.setOnLoadmoreListener(new OnLoadmoreListener() {

	    @Override
	    public void onLoadmore() {
		pageIndex++;
		loadData();
	    }
	});

	// gridView.setOnItemClickListener(new
	// StaggeredGridView.OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(StaggeredGridView parent, View view,
	// int position, long id) {
	//
	// Intent intent = new Intent(SC_SheTuanAty.this,
	// SC_SheTuanDetailAty.class);
	// intent.putExtra("id", lists.get(position).getId());
	// startActivity(intent);
	// }
	// });

    }

    /**
     * 加载本页社团信息
     */
    private void loadData() {
	new Thread(new Runnable() {
	    public void run() {
		String url = HttpUtil.QUERY_MYHUODONG_EVA + "&rows=10&page="
			+ pageIndex + "&p_id=" + p_id;
		String res = null;
		try {
		    res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    int total = js.getInt("total");
		    if (total > 0) {
			JSONArray array = js.getJSONArray("rows");
			List<SC_SheTuan> abc = new ArrayList<SC_SheTuan>();
			for (int i = 0; i < array.length(); i++) {
			    String content = array.optJSONObject(i).getString(
				    "content");
			    String create_time = array.optJSONObject(i)
				    .getString("create_time");
			    String feedback_count = array.optJSONObject(i)
				    .getString("feedback_count");
			    String good_count = array.optJSONObject(i)
				    .getString("good_count");
			    String id = array.optJSONObject(i).getString("id");
			    String img_src = array.optJSONObject(i).getString(
				    "img_src");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");
			    String menu_id = array.optJSONObject(i).getString(
				    "menu_id");
			    String sort = array.optJSONObject(i).getString(
				    "sort");
			    String title = array.optJSONObject(i).getString(
				    "title");
			    String title2 = array.optJSONObject(i).getString(
				    "title2");

			    list = new SC_SheTuan();
			    list.setContent(content);
			    list.setCreate_time(create_time);
			    list.setFeedback_count(feedback_count);
			    list.setGood_count(good_count);
			    list.setId(id);
			    list.setImg_src(img_src);
			    list.setImg_path(img_path);
			    list.setMenu_id(menu_id);
			    list.setSort(sort);
			    list.setTitle(title);
			    list.setTitle2(title2);

			    abc.add(list);
			}
			msg = handler.obtainMessage(MSG_INITDATA);
			msg.obj = abc;
			msg.sendToTarget();
		    } else {
			// 无数据或者出错了
		    }

		} catch (JSONException exception) {
		    Log.e("校园社团", "获取社团信息json异常");
		}

		catch (Exception e) {
		    Log.e("校园社团", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();
    }

    /**
     * 点击广告栏事件
     */
    private void onClicks() {
	// TODO 点击广告栏事件

    }

    /**
     * 通用框架 pulltorefresh 需要实现的监听方法 用来监听 上下啦的
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-30 下午4:53:33
     * @function ：
     */
    class MyListener implements OnRefreshListener {

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

	    loadData();
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
	    loadData();

	    // 上拉加载操作 仅仅为了实现好看，觉得努力加载中的样子
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

