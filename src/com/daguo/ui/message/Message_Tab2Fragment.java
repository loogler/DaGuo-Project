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
import com.daguo.util.adapter.Message_Tab2Adapter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午9:04:56
 * @function ：
 */
public class Message_Tab2Fragment extends Fragment {
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
    private Message_Tab2Adapter adapter;

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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_message_tab2, null);
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
	
	view.findViewById(R.id.tv).setOnClickListener(new OnClickListener() {
	    
	    @Override
	    public void onClick(View arg0) {
		Intent intent =new Intent(getActivity() ,Chat_Aty.class);
		intent.putExtra("id", "5c38c078-ee1f-4591-a5ed-47c0831bd883");
		intent.putExtra("photo", "image/20151230/1451409212521.JPEG");
		startActivity(intent);
	    }
	});
	
	adapter = new Message_Tab2Adapter(getActivity(), lists);

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

    private void loadMessageData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_MESSAGE_CHAT + "&page="
			    + pageIndex + "&rows=20&r_p_id=" + p_id;
		    String res =HttpUtil.getRequest(url);
		    
		    JSONObject jsonObject =new JSONObject(res);
		    if ( null != jsonObject) {
			
			if (jsonObject.getInt("total")>0) {
			    JSONArray array =jsonObject.getJSONArray("rows");
			    
			    for (int i = 0; i < array.length(); i++) {
				list=new Message_Inform();
				
				String create_time =array.optJSONObject(i).getString("create_time");
				list.setCreate_time(create_time);
				
				String s_head_info =array.optJSONObject(i).getString("s_head");
				list.setS_head_info(s_head_info);
				
String s_name=array.optJSONObject(i).getString("s_name");
				list.setS_name(s_name);
				String s_pro_name =array.optJSONObject(i).getString("s_pro_name");
				list.setS_pro_name(s_pro_name);
				
				
				
			    }
			}else {
			    //无数据
			}
			
		    } 
		    
		} catch (Exception e) {
		}
	    }
	}).start();

    }

}
