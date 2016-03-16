package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.adapter.SC_ShuoShuoAdapter;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;

/**
 * 同学说的 热门界面
 * 
 * @author Bugs_Rabbit 時間： 2015-9-24 下午2:06:31
 */
public class SC_ShuoShuo_TabRemenFragment extends Fragment {
    String tag = "SC_ShuoShuo_TabRemenFragment";
    private final int MSG_CONTENT = 100;
    private final int MSG_TOPBANNERLISTS = 101;

    // initViews
    private PullToRefreshLayout refresh_view;
    private ListView content_view;
    // 广告view
    private ImageView add1_iv, add2_iv;

    /**
     * 说说内容data
     */
    private List<ShuoShuoContent> contentLists = new ArrayList<ShuoShuoContent>();
    private ShuoShuoContent contentList = null;
    private List<HeadInfo> headInfos;
    // shuoshuo
    private SC_ShuoShuoAdapter adapter = null;

    // 广告位信息
    private List<AddBanner> addLists = new ArrayList<AddBanner>();

    /**
     * 通用data
     * 
     */

    private int pageIndex = 1;// 加载页码

    /**
     * tools
     */
    FinalBitmap finalBitmap;
    private Message msg;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT:

		List<ShuoShuoContent> aaContents = (List<ShuoShuoContent>) msg.obj;
		contentLists.addAll(aaContents);
		adapter.notifyDataSetChanged();

		break;
	    case MSG_TOPBANNERLISTS:
		finalBitmap.display(add1_iv, HttpUtil.IMG_URL
			+ addLists.get(0).getImg_path());
		finalBitmap.display(add2_iv, HttpUtil.IMG_URL
			+ addLists.get(1).getImg_path());
		add1_iv.setOnClickListener(new AddBannerOnclickListener(
			getActivity(), addLists, 0));
		add2_iv.setOnClickListener(new AddBannerOnclickListener(
			getActivity(), addLists, 1));
		break;

	    default:
		break;
	    }
	};
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	finalBitmap = FinalBitmap.create(getActivity());

	loadData();
	loadAddData();
	adapter = new SC_ShuoShuoAdapter(getActivity(), contentLists);
	content_view.setAdapter(adapter);

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {

	View view = inflater.inflate(R.layout.fragment_sc_shuoshuo_tabremen,
		null);
	refresh_view = (PullToRefreshLayout) view
		.findViewById(R.id.refresh_view);
	content_view = (ListView) view.findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(new MyRefreshListener());
	content_view.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View v, int p,
		    long arg3) {
		Intent intent = new Intent(getActivity(),
			SC_ShuoShuo_EvaluationAty1.class);
		intent.putExtra("id", contentLists.get(p).getId());
		getActivity().startActivity(intent);
	    }
	});
	// 广告位图
	add1_iv = (ImageView) view.findViewById(R.id.add1_iv);
	add2_iv = (ImageView) view.findViewById(R.id.add2_iv);

	return view;

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
		    String url = HttpUtil.QUERY_SHUOSHUO_REMEN
			    + "&rows=15&page=" + pageIndex;
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

    private void loadAddData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=10&page=1&rows=2";
		    String res = "";
		    JSONObject js = null;
		    int total = 0;
		    res = HttpUtil.getRequest(url);
		    js = new JSONObject(res);
		    total = js.getInt("total");
		    AddBanner list = null;
		    if (total == 0) {
			// 无广告 ，或者加载异常
			Log.e("同学说获取广告信息", "获取 广告信息异常");

		    } else {
			JSONArray array = js.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String id = array.optJSONObject(i).getString("id");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");
			    String menu_id = array.optJSONObject(i).getString(
				    "menu_id");
			    String source_id = array.optJSONObject(i)
				    .getString("source_id");
			    String type = array.optJSONObject(i).getString(
				    "type");
			    String urls = array.optJSONObject(i).getString(
				    "url");
			    list = new AddBanner();
			    list.setId(id);
			    list.setImg_path(img_path);
			    list.setMenu_id(menu_id);
			    list.setSource_id(source_id);
			    list.setType(type);
			    list.setUrl(urls);
			    addLists.add(list);
			}
			msg = handler.obtainMessage(MSG_TOPBANNERLISTS);
			msg.sendToTarget();
		    }

		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("同学说获取广告信息", "获取广告json异常");
		} catch (Exception e) {
		    Log.e("同学说获取广告信息", "获取广告异常");
		    e.printStackTrace();
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
	@SuppressLint("HandlerLeak")
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	    // 下拉刷新操作
	    new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    // 千万别忘了告诉控件刷新完毕了哦！
		    pageIndex = 1;
		    contentLists.clear();
		    loadData();
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
	@SuppressLint("HandlerLeak")
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	    new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    pageIndex++;
		    loadData();
		    // 千万别忘了告诉控件加载完毕了哦！
		    pullToRefreshLayout
			    .loadmoreFinish(PullToRefreshLayout.SUCCEED);
		}
	    }.sendEmptyMessageDelayed(0, 2000);

	}

    }

}
