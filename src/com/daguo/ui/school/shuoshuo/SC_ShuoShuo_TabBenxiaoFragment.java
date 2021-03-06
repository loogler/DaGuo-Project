package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.util.adapter.SC_ShuoShuoAdapter;
import com.daguo.util.adapter.SC_ShuoShuo_NewStudentAdapter;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.util.beans.UserInfo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

@SuppressLint({ "InflateParams", "HandlerLeak" })
public class SC_ShuoShuo_TabBenxiaoFragment extends Fragment {

	private final int MSG_CONTENT = 100;
	private final int MSG_USERHEAD_SUC = 101;

	// initViews
	private PullToRefreshLayout refresh_view;
	private ListView content_view;
	// 本校说说头部的新报道同学 。
	private TextView more_tv;
	private NoScrollGridView grid;
	private SC_ShuoShuo_NewStudentAdapter gridApter;

	private List<UserInfo> userLists = new ArrayList<UserInfo>();

	/**
	 * 说说内容data
	 */ 
	private List<ShuoShuoContent> contentLists = new ArrayList<ShuoShuoContent>();
	private ShuoShuoContent contentList = null;
	private List<HeadInfo> headInfos;
	// shuoshuo
	private SC_ShuoShuoAdapter adapter = null;

	/**
	 * 通用data
	 * 
	 */
	private String school_id;// 學校id
	private String school_name;// 學校名
	private int pageIndex = 1;// 加载页码

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
			case MSG_USERHEAD_SUC:

				gridApter.notifyDataSetChanged();

				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadData();
		loadGridData();
		school_id = getActivity().getSharedPreferences("userinfo",
				Activity.MODE_PRIVATE).getString("school_id", "");
		if (school_id.isEmpty()) {
			// getActivity().finish();
			Toast.makeText(getActivity(), "学校信息异常", Toast.LENGTH_LONG).show();
			// 禁止沒有學校信息的用戶進入本界面
		}
		adapter = new SC_ShuoShuoAdapter(getActivity(), contentLists);
		content_view.setAdapter(adapter);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sc_shuoshuo_tabbenxiao,
				null);

		more_tv = (TextView) view.findViewById(R.id.more_tv);

		more_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(),
						SC_ShuoShuo_NewStudentAty.class);
				startActivity(intent);
			}
		});
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

		gridApter = new SC_ShuoShuo_NewStudentAdapter(userLists, getActivity());
		grid = (NoScrollGridView) view.findViewById(R.id.grid);
		grid.setAdapter(gridApter);

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
					JSONObject js = null;
					if (!school_id.isEmpty() && !school_id.equals("null")
							&& !school_id.equals("[]")) {
						String url = HttpUtil.QUERY_SHUOSHUO + "&rows=10&page="
								+ pageIndex + "&school_id=" + school_id;
						String res = HttpUtil.getRequest(url);
						js = new JSONObject(res);
						// TODO 返回数据为空 。。
					} else {
						// school_id 存在問題，改用schoolName
						String url = HttpUtil.QUERY_SHUOSHUO + "&rows=15&page="
								+ pageIndex;
						Map<String, String> map = new HashMap<String, String>();
						map.put("school_name", school_name);
						String res = HttpUtil.postRequest(url, map);
						js = new JSONObject(res);

					}

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

	/**
	 * 加载新报道的同学
	 */
	private void loadGridData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					if (!"".equals(PublicTools.doWithNullData(school_id))) {
						String url = HttpUtil.QUERY_USERINFO
								+ "&page=1&rows=40&school_id=" + school_id;
						String res = HttpUtil.getRequest(url);

						JSONObject jsonObject = new JSONObject(res);

						if (jsonObject.getInt("total") > 0) {
							// 查询到数条记录
							JSONArray array = jsonObject.getJSONArray("rows");
							for (int i = 0; i < array.length(); i++) {
								String head_info = array.optJSONObject(i)
										.getString("head_info");
								String name = array.optJSONObject(i).getString(
										"name");
								String id = array.optJSONObject(i).getString(
										"id");

								if (!"".equals(PublicTools
										.doWithNullData(head_info))) {
									// 没有头像的 不显示，避免新注册用户过多导致大部分人不显示头像
									UserInfo info = new UserInfo();
									info.setHead_info(head_info);
									info.setName(name);
									info.setId(id);

									userLists.add(info);
								}
							}
							msg = handler.obtainMessage(MSG_USERHEAD_SUC);
							msg.sendToTarget();

						} else {
							// 查询本校信息失败 ，或者该校就一个人注册
						}

					} else {
						// 学校信息出错
						Log.e("同学说-本校说说", "查询本校信息  学校id为空");

					}

				} catch (Exception e) {
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
