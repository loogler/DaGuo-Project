/**
 * 互相学习 共同进步
 */
package com.daguo.ui.message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.ChatAdapter;
import com.daguo.util.beans.ChatMsg;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-7 下午2:38:34
 * @function ：聊天 主界面
 */
public class Chat_Aty extends Activity {
	private final int MSG_SEND_SUC = 10001;
	private final int MSG_SEND_FAIL = 10002;
	private final int MSG_CHATDATA_SUC = 10003;
	private final int MSG_LOAD = 10004;

	// 是否继续加载聊天信息线程 （退出时需要关闭消息轮播线程 ）
	private boolean isLoadChatMsg = true;

	private int pageIndex = 1;

	private String p_id, id;// p_id= my id , id= chat with id
	private String p_photo;//

	private String content;// 聊天内容

	/**
	 * @see initViews
	 */
	private PullToRefreshLayout refresh_view;
	private ListView content_view;
	private EditText content_edt;
	private TextView send_tv;

	/**
	 * 聊天记录
	 */
	List<ChatMsg> lists = new ArrayList<ChatMsg>();
	ChatMsg list;
	ChatAdapter adapter;

	/**
	 * tools
	 */

	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SEND_SUC:
				ChatMsg chatMsg = new ChatMsg();
				chatMsg.setContent(content);
				chatMsg.setCreate_time(new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss").format(System
						.currentTimeMillis()));
				chatMsg.setPhoto(p_photo);
				chatMsg.setR_p_id(id);
				chatMsg.setS_p_id(p_id);
				chatMsg.setSend(false);
				chatMsg.setStatus("0");
				lists.add(chatMsg);
				adapter.notifyDataSetChanged();
				content_view
						.setSelection(content_view.getAdapter().getCount() - 1);

				content_edt.setText("");
				content = "";

				break;
			case MSG_SEND_FAIL:
				Toast.makeText(Chat_Aty.this, "发送失败，", Toast.LENGTH_LONG)
						.show();

				break;
			case MSG_CHATDATA_SUC:
				if (null != msg.obj) {
					@SuppressWarnings("unchecked")
					List<ChatMsg> abc = (List<ChatMsg>) msg.obj;
					lists.addAll(abc);
				}
				adapter.notifyDataSetChanged();
				content_view
						.setSelection(content_view.getAdapter().getCount() - 1);

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
		setContentView(R.layout.aty_chat);
		p_id = getSharedPreferences("userinfo", 0).getString("id", "");
		p_photo = getSharedPreferences("userinfo", 0)
				.getString("head_info", "");
		id = getIntent().getStringExtra("id");

		initViews();
		loadChatMsg();

		new Thread(new Runnable() {
			public void run() {
				while (isLoadChatMsg) {
					loadMessage();
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		isLoadChatMsg = false;
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	protected void onPause() {

		super.onPause();
	}

	private void initViews() {
		initHeadViews();
		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_edt = (EditText) findViewById(R.id.content_edt);
		content_view = (ListView) findViewById(R.id.content_view);
		send_tv = (TextView) findViewById(R.id.send_tv);

		refresh_view.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

				pageIndex++;
				loadChatMsg();
				new Handler().postDelayed(new Runnable() {
					public void run() {
						pullToRefreshLayout
								.refreshFinish(PullToRefreshLayout.SUCCEED);
					}
				}, 1500);

			}

			@Override
			public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		});

		send_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendMsg();
			}
		});

		adapter = new ChatAdapter(Chat_Aty.this, lists);
		content_view.setAdapter(adapter);
	}

	private void initHeadViews() {

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
		title_tv.setText("聊天");
		function_tv.setVisibility(View.GONE);
		remind_iv.setVisibility(View.GONE);

	}

	/**
	 * 加载历史聊天记录
	 */
	private void loadChatMsg() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_CHAT_DETAIL + "&page="
							+ pageIndex + "&rows=20&r_p_id=" + p_id
							+ "&s_p_id=" + id;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);
					if (pageIndex > jsonObject.getInt("totalPageNum")) {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(Chat_Aty.this, "已加载全部数据",
										Toast.LENGTH_LONG).show();
							}
						});
					}
					if (jsonObject.getInt("total") > 0) {
						// 有了历史聊天记录
						JSONArray array = jsonObject.getJSONArray("rows");
						List<ChatMsg> abc = new ArrayList<ChatMsg>();
						for (int i = 0; i < array.length(); i++) {
							String s_p_id = array.optJSONObject(i).getString(
									"s_p_id");// 发送者

							String r_p_id = array.optJSONObject(i).getString(
									"r_p_id");// 接收者
							String content = array.optJSONObject(i).getString(
									"content");
							String create_time = array.optJSONObject(i)
									.getString("create_time");
							String status = array.optJSONObject(i).getString(
									"status");// 状态(0:未读 1：已读)
							String r_head = array.optJSONObject(i).getString(
									"r_head");
							String s_head = array.optJSONObject(i).getString(
									"s_head");
							boolean isSend;

							list = new ChatMsg();
							if (p_id.equals(r_p_id)) {
								// 别人发出去的 显示别人头像，
								isSend = true;
								list.setPhoto(s_head);
							} else {
								// 自己发过来的
								isSend = false;
								list.setPhoto(s_head);
							}
							list.setContent(content);
							list.setCreate_time(create_time);
							list.setSend(isSend);
							list.setStatus(status);

							abc.add(list);

						}
						Collections.reverse(abc);// 倒序聊天记录

						msg = handler.obtainMessage(MSG_CHATDATA_SUC);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 并没有聊天记录
					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/**
	 * 发送一条聊天信息
	 */
	private void sendMsg() {

		content = PublicTools.doWithNullData(content_edt.getText().toString()
				.trim());
		if ("".equals(content))
			return;

		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.SUBMIT_CHAT_SEND;
					Map<String, String> map = new HashMap<String, String>();
					map.put("s_p_id", p_id);
					map.put("r_p_id", id);
					map.put("content", content);
					map.put("status", "0");
					String res = HttpUtil.postRequest(url, map);

					JSONObject jsonObject = new JSONObject(res);
					if ("1".equals(jsonObject.getString("result"))) {

						// 成功发送
						msg = handler.obtainMessage(MSG_SEND_SUC);
						msg.sendToTarget();

					} else {
						// 发送失败
						msg = handler.obtainMessage(MSG_SEND_FAIL);
						msg.sendToTarget();
					}

				} catch (Exception e) {
				}
			}
		}

		).start();
	}

	/**
	 * 加载聊天时的消息新收到的消息
	 */
	private void loadMessage() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_CHATNOW
							+ "&page=1&rows=100&r_p_id=" + p_id + "&s_p_id="
							+ id;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("total") > 0) {
						// 有了历史聊天记录
						JSONArray array = jsonObject.getJSONArray("rows");
						List<ChatMsg> abc = new ArrayList<ChatMsg>();
						for (int i = 0; i < array.length(); i++) {
							String s_p_id = array.optJSONObject(i).getString(
									"s_p_id");// 发送者
							@SuppressWarnings("unused")
							String r_p_id = array.optJSONObject(i).getString(
									"r_p_id");// 接收者
							String content = array.optJSONObject(i).getString(
									"content");
							String create_time = array.optJSONObject(i)
									.getString("create_time");
							String status = array.optJSONObject(i).getString(
									"status");// 状态(0:未读 1：已读)
							String r_head = array.optJSONObject(i).getString(
									"r_head");
							String s_head = array.optJSONObject(i).getString(
									"s_head");
							boolean isSend;

							list = new ChatMsg();

							isSend = true;

							list.setPhoto(s_head);
							list.setContent(content);
							list.setCreate_time(create_time);
							list.setSend(isSend);
							list.setStatus(status);

							abc.add(list);

						}
						Collections.reverse(abc);// 倒序聊天记录

						msg = handler.obtainMessage(MSG_CHATDATA_SUC);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 对方尚未发送消息
					}

				} catch (Exception e) {
				}
			}
		}).start();

	}
}
