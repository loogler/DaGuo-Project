/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.message.Chat_Aty;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuo_EvaluationAty1;
import com.daguo.util.adapter.SC_ShuoShuoAdapter;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.util.beans.UserInfo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-5 上午11:56:17
 * @function ： 用户信息主界面，展示其他用户信息
 */
public class UserInfoAty extends Activity {
	private final int MSG_CONTENT = 10001;
	private final int MSG_USERINFO = 10002;

	private String p_id, a_id;// a_id 是对方id p_id是自己id

	// initViews
	private PullToRefreshLayout refresh_view;
	private ListView content_view;

	// 头部view

	private CircularImage photo;
	private TextView name_tv, schoolname_tv;
	private TextView attention_tv, chat_tv;
	private ImageView sex_iv;

	/**
	 * 说说内容data
	 */
	private List<ShuoShuoContent> contentLists = new ArrayList<ShuoShuoContent>();
	private ShuoShuoContent contentList = null;
	private List<HeadInfo> headInfos;
	// shuo shuo
	private SC_ShuoShuoAdapter adapter = null;

	/**
	 * 通用data
	 * 
	 */

	private int pageIndex = 1;// 加载页码

	/**
	 * tools
	 */
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
			case MSG_USERINFO:
				setHeadView();

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
		setContentView(R.layout.aty_userinfo);
		initView();
		addHeadView();
		p_id = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
				.getString("id", "");
		a_id = getIntent().getStringExtra("id");
		loadUserInfo();
		loadData();
		adapter = new SC_ShuoShuoAdapter(this, contentLists);
		content_view.setAdapter(adapter);

	}

	private void initView() {
		initHeadView();

		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_view = (ListView) findViewById(R.id.content_view);

		refresh_view.setOnRefreshListener(new MyRefreshListener());
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {
				Intent intent = new Intent(UserInfoAty.this,
						SC_ShuoShuo_EvaluationAty1.class);
				intent.putExtra("id", contentLists.get(p).getId());
				startActivity(intent);
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
		title_tv.setText("个人主页");
		function_tv.setVisibility(View.GONE);
		remind_iv.setVisibility(View.GONE);

	}

	@SuppressLint("InflateParams")
	private void addHeadView() {

		photo = (CircularImage) findViewById(R.id.photo);
		name_tv = (TextView) findViewById(R.id.name_tv);
		schoolname_tv = (TextView) findViewById(R.id.schoolname_tv);
		attention_tv = (TextView) findViewById(R.id.attention_tv);
		chat_tv = (TextView) findViewById(R.id.chat_tv);
		sex_iv = (ImageView) findViewById(R.id.sex_iv);

		attention_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				doAttention();
			}
		});
		chat_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(UserInfoAty.this, Chat_Aty.class);

				intent.putExtra("", "");
				startActivity(intent);

			}
		});
	}

	@SuppressLint("SimpleDateFormat")
	private void setHeadView() {

		FinalBitmap.create(UserInfoAty.this).display(photo,
				HttpUtil.IMG_URL + userInfo.getHead_info());
		name_tv.setText(userInfo.getName());
		if (!"".equals(PublicTools.doWithNullData(userInfo.getStart_year()))) {

			schoolname_tv.setText(userInfo.getStart_year().substring(0, 4)
					+ "级 " + userInfo.getSchool_name());

		} else {
			schoolname_tv.setText(userInfo.getSchool_name());
		}
		if ("0".equals(userInfo.getSex())) {
			sex_iv.setImageResource(R.drawable.icon_sex_man);
		} else if ("1".equals(userInfo.getSex())) {
			sex_iv.setImageResource(R.drawable.icon_sex_woman);

		}

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
							+ pageIndex + "&p_id=" + a_id;
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

	private UserInfo userInfo = new UserInfo();

	private void loadUserInfo() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_USERINFO + "&page=1&rows=1"
							+ "&id=" + a_id;

					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);

					if (null != jsonObject.getJSONArray("rows")) {
						JSONArray array = jsonObject.getJSONArray("rows");

						String head_info = array.optJSONObject(0).getString(
								"head_info");
						String name = array.optJSONObject(0).getString("name");
						String pro_name = array.optJSONObject(0).getString(
								"pro_name");
						String school_name = array.optJSONObject(0).getString(
								"school_name");
						String sex = array.optJSONObject(0).getString("sex");
						String start_year = array.optJSONObject(0).getString(
								"start_year");

						userInfo.setHead_info(head_info);
						userInfo.setName(name);
						userInfo.setPro_name(pro_name);
						userInfo.setSchool_name(school_name);
						userInfo.setSex(sex);
						userInfo.setStart_year(start_year);
						msg = handler.obtainMessage(MSG_USERINFO);
						msg.sendToTarget();

					} else {
						// 用户信息出错了 或者突然断网了
						Log.e("用户信息类", "查询用户信息失败  ");

					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/**
	 * 加关注
	 */
	private void doAttention() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.SUBMIT_ATTENTION + "&follow_id="
							+ p_id + "&followed_id=" + a_id;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);

					if (1 == jsonObject.getInt("result")) {
						// 提交成功
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(UserInfoAty.this, "已加入关注列表",
										Toast.LENGTH_LONG).show();

							}
						});

					} else if (2 == jsonObject.getInt("result")) {
						// 提交失败 已关注过
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(UserInfoAty.this, "关注失败，已在关注列表",
										Toast.LENGTH_LONG).show();

							}
						});
					} else if (3 == jsonObject.getInt("result")) {
						// 关注自己了
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(UserInfoAty.this, "关注人不能是自己",
										Toast.LENGTH_LONG).show();

							}
						});
					} else {
						// 提交失败
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(UserInfoAty.this, "关注失败，请重试",
										Toast.LENGTH_LONG).show();

							}
						});
					}

				} catch (Exception e) {
				}
			}
		}).start();
	}

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
					contentLists.clear();
					pageIndex = 1;
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
