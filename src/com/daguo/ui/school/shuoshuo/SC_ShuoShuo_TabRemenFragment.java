package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.daguo.R;
import com.daguo.util.adapter.SC_ShuoShuoAdapter;
import com.daguo.util.base.AutoListView;
import com.daguo.util.base.AutoListView.OnLoadListener;
import com.daguo.util.base.AutoListView.OnRefreshListener;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;

/**
 * 同学说的 热门界面
 * 
 * @author Bugs_Rabbit 時間： 2015-9-24 下午2:06:31
 */
public class SC_ShuoShuo_TabRemenFragment extends Fragment implements
		OnLoadListener,OnRefreshListener, OnClickListener, OnItemClickListener {
	String tag = "SC_ShuoShuo_TabRemenFragment";
	/**
	 * init Views
	 */
	private AutoListView autoListView;
	/**
	 * user data
	 */
	private String p_photo, p_name, p_school, p_sex;
	/**
	 * adapter
	 */
	private SC_ShuoShuoAdapter adapter;
	private List<ShuoShuoContent> lists = new ArrayList<ShuoShuoContent>();
	private ShuoShuoContent list;
	// 页码
	private int pageIndex = 1;

	/**
	 * 
	 */
	Message msg;
	
	@Override
	    public void setUserVisibleHint(boolean isVisibleToUser) {
                    //判断Fragment中的ListView时候存在，判断该Fragment时候已经正在前台显示  通过这两个判断，就可以知道什么时候去加载数据了
			if (isVisibleToUser && isVisible() && autoListView.getVisibility() != View.VISIBLE) {
//	            initData(); //加载数据的方法
	        }
	        super.setUserVisibleHint(isVisibleToUser);
	    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_name = sp.getString("name", "");
		p_school = sp.getString("school_name", "");
		p_sex = sp.getString("sex", "");
		View view = inflater.inflate(R.layout.fragment_sc_shuoshuo_tabremen,
				null);
		autoListView = (AutoListView) view
				.findViewById(R.id.autolistview);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initViews();
		loadData();

	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 加载

				if (msg.obj != null) {
					List<ShuoShuoContent> aaa = (List<ShuoShuoContent>) msg.obj;
					lists.addAll(aaa);
					// autoListView.setResultSize(lists.size());
					adapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 初始化控件
	 */
	private void initViews() {

		adapter = new SC_ShuoShuoAdapter(getActivity(), lists);
		autoListView.setAdapter(adapter);
		// XListView.setResultSize(lists.size());
		// XListView.setOnRefreshListener(this);
		// XListView.setOnLoadListener(this);
		autoListView.setOnItemClickListener(this);
		autoListView.setOnRefreshListener(this);
		autoListView.setOnLoadListener(this);
	}

	/**
	 * 加载数据，获得说说数据
	 */
	private void loadData() {

		new Thread(new Runnable() {
			public void run() {
				try {
					List<ShuoShuoContent> ssss = new ArrayList<ShuoShuoContent>();
					String url = HttpUtil.QUERY_SHUOSHUO + "&rows=10&page="
							+ pageIndex;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					if (js.getInt("total") > 0) {
						JSONArray arr = js.getJSONArray("rows");
						for (int i = 0; i < arr.length(); i++) {
							list = new ShuoShuoContent();
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
							//* 对sign字段进行处理  由于其值为所有用户信息  这里只需头像信息，以及个人id
//							JSONArray arrays =arr.optJSONObject(i).getJSONArray("signs");
//							for (int j = 0; j < arrays.length(); j++) {
//								String f_head =arrays.optJSONObject(j).getString("");
//							}
							list.setId(id);
							list.setCreatTime(create_time);
							list.setImg_path(img_path);
							list.setContent(content);
							list.setGood_count(good_count);
							list.setFeedback_count(feedback_count);
							list.setType(type);
							list.setType_name(type_name);
							list.setSchool_id(school_id);
							list.setP_id(p_id);
							list.setP_name(p_name);
							list.setSchool_name(school_name);
							list.setSigns(signs);
							list.setP_photo(head_info);
							list.setTableName(tableName);
							list.setP_sex(p_sex);
							ssss.add(list);

						}

						msg = handler.obtainMessage(0);
						msg.obj = ssss;
						msg.sendToTarget();
					} else {
						// 数据为0
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();

	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onClick(View arg0) {

	}

	@Override
	public void onLoad() {
		
		pageIndex++;
		loadData();
		onLoad();
		autoListView.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		lists.clear();
		loadData();
		
		autoListView.onRefreshComplete();


	}

}
