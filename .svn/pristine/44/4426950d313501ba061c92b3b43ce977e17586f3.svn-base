/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.AttentionUserAdapter;
import com.daguo.util.beans.UserInfo;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-27 下午5:30:27
 * @function ： 我关注的人 主界面
 */
public class UserInfo_MyAttention_MyAty extends Activity {
    private final int MSG_USERINFO = 10001;

    private int pageIndex = 1;
    private String p_id;

    /**
     * @see initViews
     */
    private PullToRefreshLayout refresh_view;
    private ListView content_view;
    private UserInfo list;
    private List<UserInfo> lists = new ArrayList<UserInfo>();
    private AttentionUserAdapter adapter;

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_USERINFO:
		if (null != msg.obj) {
		    @SuppressWarnings("unchecked")
		    List<UserInfo> abc = (List<UserInfo>) msg.obj;
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
	setContentView(R.layout.aty_userinfo_myattention_my);
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");

	initViews();

	loadUserList();

	adapter = new AttentionUserAdapter(this, lists);
	content_view.setAdapter(adapter);
    }

    /**
     * 
     */
    @SuppressLint("HandlerLeak")
    private void initViews() {
	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (ListView) findViewById(R.id.content_view);
	refresh_view.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		lists.clear();
		adapter.notifyDataSetChanged();
		pageIndex = 1;
		loadUserList();
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
		loadUserList();
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

    /*----------数据加载-------------------------------------*/
    /**
     * 加载我关注人的列表
     */
    private void loadUserList() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_ATTENTION + "&rows=15&page="
			    + pageIndex + "&follow_id=" + p_id;
		    String res = HttpUtil.getRequest(url);

		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") > 0) {
			// 返回数据处理
			if (pageIndex > jsonObject.getInt("totalPageNum")) {
			    runOnUiThread(new Runnable() {
				public void run() {
				    Toast.makeText(
					    UserInfo_MyAttention_MyAty.this,
					    "加载完了", Toast.LENGTH_SHORT).show();

				}
			    });
			} else {
			    // 开始处理
			    JSONArray array = jsonObject.getJSONArray("rows");
			    List<UserInfo> abc = new ArrayList<UserInfo>();
			    for (int i = 0; i < array.length(); i++) {
				String head_info = array.optJSONObject(i)
					.getString("followed_head_info");
				String id = array.optJSONObject(i).getString(
					"followed_id");
				String name = array.optJSONObject(i).getString(
					"followed_name");
				String school_name = array.optJSONObject(i)
					.getString("followed_school_name");
				String sex = array.optJSONObject(i).getString(
					"followed_sex");

				list = new UserInfo();
				list.setHead_info(head_info);
				list.setName(name);
				list.setId(id);
				list.setSchool_name(school_name);
				list.setSex(sex);
				abc.add(list);

			    }
			    msg = handler.obtainMessage(MSG_USERINFO);
			    msg.obj = abc;
			    msg.sendToTarget();

			}

		    } else {
			// 没有数据

		    }

		} catch (Exception e) {
		    Log.e("查询我关注的人", "暑假加载异常");
		    return;
		}
	    }
	}).start();
    }
}
