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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.SC_SheTuanAdapter;
import com.daguo.libs.staggeredgridview.StaggeredGridView;
import com.daguo.libs.staggeredgridview.StaggeredGridView.OnLoadmoreListener;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-8 下午3:03:30
 * @function ：我的报名主界面，显示我已经报名参加的
 */
public class UserInfo_MyBaoMingAty extends Activity {
    private final int MSG_INITDATA = 100;
    private String p_id;

    /**
     * initViews
     */
    private StaggeredGridView gridView;

    /**
     * data
     */
    /**
     * data
     */
    private List<SC_SheTuan> lists = new ArrayList<SC_SheTuan>();
    private SC_SheTuan list = null;
    private SC_SheTuanAdapter adapter = null;

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
	setContentView(R.layout.aty_mybaoming);
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

		title_tv.setText("我的报名");
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

	// ((PullToRefreshLayout) findViewById(R.id.refresh_view))
	// .setOnRefreshListener(new MyListener());

	int margin = getResources().getDimensionPixelSize(R.dimen.stgv_margin);
	gridView.setItemMargin(margin);
	gridView.setPadding(margin, 0, margin, 0);

	adapter = new SC_SheTuanAdapter(UserInfo_MyBaoMingAty.this, lists);
	gridView.setAdapter(adapter);

	gridView.setOnLoadmoreListener(new OnLoadmoreListener() {

	    @Override
	    public void onLoadmore() {
		pageIndex++;
		loadData();
	    }
	});

    }

    /**
     * 加载本页社团信息
     */
    private void loadData() {
	new Thread(new Runnable() {
	    public void run() {
		String url = HttpUtil.QUERY_MYBAOMING + "&rows=10&page="
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

}
