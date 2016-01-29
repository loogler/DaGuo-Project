package com.daguo.ui.user;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuo_EvaluationAty1;
import com.daguo.util.adapter.SC_ShuoShuoAdapter;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-8 下午2:13:57
 * @function ：显示我的说说界面 接口未改 功能已废弃
 */
public class UserInfo_MyShuoShuoAty extends Activity implements
	OnItemClickListener, OnRefreshListener {

    private int pageIndex = 1;// 加载页码
    private final int MSG_CONTENT = 100;
    private String p_id;

    /**
     * initViews
     */
    private PullToRefreshLayout refresh_view;
    private ListView content_view;

    /**
     * 说说内容data
     */
    private List<ShuoShuoContent> contentLists = new ArrayList<ShuoShuoContent>();
    private ShuoShuoContent contentList = null;
    private List<HeadInfo> headInfos;
    // shuoshuo
    private SC_ShuoShuoAdapter adapter = null;

    /**
     * tools
     */
    private Message msg;
    private Handler handler = new Handler() {
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT:

		List<ShuoShuoContent> aaContents = (List<ShuoShuoContent>) msg.obj;
		contentLists.addAll(aaContents);
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
	setContentView(R.layout.aty_userinfo_myshuoshuo);
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");
	initViews();

	loadData();
	adapter = new SC_ShuoShuoAdapter(UserInfo_MyShuoShuoAty.this,
		contentLists);
	content_view.setAdapter(adapter);

    }

    /**
     * 初始化控件
     */
    private void initViews() {
	initHeadView();
	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (ListView) findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(this);
	content_view.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View v, int p,
		    long arg3) {
		Intent intent = new Intent(UserInfo_MyShuoShuoAty.this,
			SC_ShuoShuo_EvaluationAty1.class);
		intent.putExtra("id", contentLists.get(p).getId());
		UserInfo_MyShuoShuoAty.this.startActivity(intent);
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
	title_tv.setText("我的说说");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 加载说说列表
     * 
     * @param url
     *            传入加载url
     */
    private void loadData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_SHUOSHUO + "&rows=15&page="
			    + pageIndex + "&p_id=" + p_id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);

		    if (js.getInt("total") > 0) {
			List<ShuoShuoContent> ssss = new ArrayList<ShuoShuoContent>();
			JSONArray arr = js.getJSONArray("rows");
			for (int i = 0; i < arr.length(); i++) {
			    contentList = new ShuoShuoContent();
			    String id = arr.optJSONObject(i).getString("id");
			    String create_time = arr.optJSONObject(i)
				    .getString("create_time");
			    String img_path = arr.optJSONObject(i).getString(
				    "img_path");
			    String content = arr.optJSONObject(i).getString(
				    "content");
			    String good_count = arr.optJSONObject(i).getString(
				    "good_count");
			    String feedback_count = arr.optJSONObject(i)
				    .getString("feedback_count");
			    String type = arr.optJSONObject(i)
				    .getString("type");
			    String type_name = arr.optJSONObject(i).getString(
				    "type_name");
			    String school_id = arr.optJSONObject(i).getString(
				    "school_id");
			    String p_id = arr.optJSONObject(i)
				    .getString("p_id");
			    String p_name = arr.optJSONObject(i).getString(
				    "p_name");
			    String p_sex = arr.optJSONObject(i).getString(
				    "p_sex");
			    String school_name = arr.optJSONObject(i)
				    .getString("school_name");
			    String head_info = arr.optJSONObject(i).getString(
				    "head_info");

			    String tableName = arr.optJSONObject(i).getString(
				    "tableName");

			    JSONArray sign = arr.optJSONObject(i).getJSONArray(
				    "signs");
			    headInfos = new ArrayList<HeadInfo>();
			    if (sign.length() > 0) {
				for (int j = 0; j < sign.length(); j++) {
				    HeadInfo headInfo = new HeadInfo();
				    String idString = sign.optJSONObject(j)
					    .getString("id");
				    String p_head_info = sign.optJSONObject(j)
					    .getString("p_head_info");
				    headInfo.setId(idString);
				    headInfo.setP_head_info(p_head_info);

				    headInfos.add(headInfo);

				}
			    } else {
				// 无人点赞
			    }

			    contentList.setId(id);
			    contentList.setCreatTime(create_time);
			    contentList.setImg_path(img_path);
			    contentList.setContent(content);
			    contentList.setGood_count(good_count);
			    contentList.setFeedback_count(feedback_count);
			    contentList.setType(type);
			    contentList.setType_name(type_name);
			    contentList.setSchool_id(school_id);
			    contentList.setP_id(p_id);
			    contentList.setP_name(p_name);
			    contentList.setSchool_name(school_name);
			    contentList.setSigns(headInfos);
			    contentList.setP_photo(head_info);
			    contentList.setTableName(tableName);
			    contentList.setP_sex(p_sex);
			    ssss.add(contentList);

			}

			msg = handler.obtainMessage(MSG_CONTENT);
			msg.obj = ssss;
			msg.sendToTarget();
		    } else {
			// 数据为0
		    }
		} catch (Exception e) {
		    Log.e("最新说说", "loaddata获取信息失败");
		}
	    }
	}).start();

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    @SuppressLint("HandlerLeak")
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	// 下拉刷新操作
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// 千万别忘了告诉控件刷新完毕了哦！
		contentLists.clear();
		pageIndex = 1;
		loadData();
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 2000);

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener
     * #onLoadMore(com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		pageIndex++;
		loadData();
		// 千万别忘了告诉控件加载完毕了哦！
		pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 2000);

    }

}
