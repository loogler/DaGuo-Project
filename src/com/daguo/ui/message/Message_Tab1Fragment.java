/**
 * 互相学习 共同进步
 */
package com.daguo.ui.message;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuo_EvaluationAty1;
import com.daguo.util.adapter.Message_Tab1Adapter;
import com.daguo.util.beans.Message_Inform;
import com.daguo.utils.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午8:56:47
 * @function ：消息中心通知
 */
public class Message_Tab1Fragment extends Fragment {

    private final int MSG_MESSAGE_SUC = 10001;

    private int pageIndex = 1;
    private String p_id;
    /**
     * @see initViews
     */
    private PullToRefreshLayout refresh_view;
    private ListView content_view;
    private List<Message_Inform> lists = new ArrayList<Message_Inform>();
    private Message_Inform list;
    private Message_Tab1Adapter adapter;

    /**
     * tools
     */

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case MSG_MESSAGE_SUC:
		if (null != msg.obj) {
		    @SuppressWarnings("unchecked")
		    List<Message_Inform> abc = (List<Message_Inform>) msg.obj;
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
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	p_id = getActivity().getSharedPreferences("userinfo",
		Activity.MODE_WORLD_READABLE).getString("id", "");
	loadMessageData();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_message_tab1, null);
	refresh_view = (PullToRefreshLayout) view
		.findViewById(R.id.refresh_view);
	content_view = (ListView) view.findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {

			pageIndex = 1;
			loadMessageData();
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
			loadMessageData();
			pullToRefreshLayout
				.refreshFinish(PullToRefreshLayout.SUCCEED);
		    }
		}, 1500);

	    }
	});
	adapter = new Message_Tab1Adapter(getActivity(), lists);

	content_view.setAdapter(adapter);
	content_view.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int p,
		    long arg3) {
		Intent intent = new Intent(getActivity(),
			SC_ShuoShuo_EvaluationAty1.class);
		intent.putExtra("id", lists.get(p).getSource_id());
		startActivity(intent);

	    }
	});

	return view;
    }

    /** -------------------------------------------------- */
    private void loadMessageData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_MESSAGE_INFORM + "&page="
			    + pageIndex + "&rows=15&r_id=" + p_id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");
			List<Message_Inform> abc = new ArrayList<Message_Inform>();

			for (int i = 0; i < array.length(); i++) {
			    String create_time = array.optJSONObject(i)
				    .getString("create_time");
			    String id = array.optJSONObject(i).getString("id");
			    String r_id = array.optJSONObject(i).getString(
				    "r_id");
			    String s_head_info = array.optJSONObject(i)
				    .getString("s_head_info");
			    String s_id = array.optJSONObject(i).getString(
				    "s_id");
			    String s_name = array.optJSONObject(i).getString(
				    "s_name");
			    String s_pro_name = array.optJSONObject(i)
				    .getString("s_pro_name");
			    String s_sex = array.optJSONObject(i).getString(
				    "s_sex");
			    String s_start_year = array.optJSONObject(i)
				    .getString("s_start_year");
			    String source_id = array.optJSONObject(i)
				    .getString("source_id");
			    String status = array.optJSONObject(i).getString(
				    "status");
			    String type = array.optJSONObject(i).getString(
				    "type");
			    list = new Message_Inform();

			    list.setCreate_time(create_time);
			    list.setId(id);
			    list.setR_id(r_id);
			    list.setS_head_info(s_head_info);
			    list.setS_id(s_id);
			    list.setS_name(s_name);
			    list.setS_pro_name(s_pro_name);
			    list.setS_sex(s_sex);
			    list.setS_start_year(s_start_year);
			    list.setSource_id(source_id);
			    list.setStatus(status);
			    list.setType(type);
			    if ("0".equals(status)) {
				// 0未读 1 已读
				abc.add(list);
			    } else {

			    }

			}
			msg = handler.obtainMessage(MSG_MESSAGE_SUC);
			msg.obj = abc;
			msg.sendToTarget();

		    } else {
			// 数量为0 或者不存在数据
		    }
		} catch (Exception e) {
		}
	    }
	}).start();

    }

}
