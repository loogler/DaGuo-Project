package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;

@SuppressLint("InflateParams")
public class SC_ShuoShuo_TabZuixinFragment extends Fragment {
    private final int MSG_CONTENT = 100;

    // initViews
    private PullToRefreshLayout refresh_view;
    private ListView content_view;

    /**
     * 说说内容data
     */
    List<ShuoShuoContent> contentLists = new ArrayList<ShuoShuoContent>();
    ShuoShuoContent contentList = null;

    /**
     * 通用data
     * 
     */
    private String p_id;
    private int pageIndex = 1;// 加载页码

    /**
     * tools
     */
    private Message msg;
    private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT:
		// TODO

		break;

	    default:
		break;
	    }
	};
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);

	String url = HttpUtil.QUERY_SHUOSHUO + "&rows=15&page=" + pageIndex;
	loadData(url);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_sc_shuoshuo_tabzuixin,
		null);
	refresh_view = (PullToRefreshLayout) view
		.findViewById(R.id.refresh_view);
	content_view = (ListView) view.findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(new MyRefreshListener());

	return view;
    }

    /**
     * 加载说说列表
     * 
     * @param url
     *            传入加载url
     */
    private void loadData(final String url) {
	new Thread(new Runnable() {
	    public void run() {
		try {
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
			    String signs = arr.optJSONObject(i).getString(
				    "signs");
			    String tableName = arr.optJSONObject(i).getString(
				    "tableName");
			    // * 对sign字段进行处理 由于其值为所有用户信息 这里只需头像信息，以及个人id
			    // JSONArray arrays
			    // =arr.optJSONObject(i).getJSONArray("signs");
			    // for (int j = 0; j < arrays.length(); j++) {
			    // String f_head
			    // =arrays.optJSONObject(j).getString("");
			    // }
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
			    contentList.setSigns(signs);
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

    /*******************************************************************************************/

    private class MyRefreshListener implements OnRefreshListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener
	 * #onRefresh(com.daguo.libs.pulltorefresh.PullToRefreshLayout)
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	    // 下拉刷新操作
	    new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    // 千万别忘了告诉控件刷新完毕了哦！
		    // TODO
		    pullToRefreshLayout
			    .refreshFinish(PullToRefreshLayout.SUCCEED);
		}
	    }.sendEmptyMessageDelayed(0, 2000);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener
	 * #onLoadMore(com.daguo.libs.pulltorefresh.PullToRefreshLayout)
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	    new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    // TODO
		    // 千万别忘了告诉控件加载完毕了哦！
		    pullToRefreshLayout
			    .loadmoreFinish(PullToRefreshLayout.SUCCEED);
		}
	    }.sendEmptyMessageDelayed(0, 2000);

	}

    }

}
