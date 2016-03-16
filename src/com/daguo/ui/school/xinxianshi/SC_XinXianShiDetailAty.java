/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.xinxianshi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.SC_SheTuanDetailAdapter;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.News;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-27 下午4:04:18
 * @function ： 校园新鲜事主界面
 */
public class SC_XinXianShiDetailAty extends Activity {
	private final int MSG_NEWS_CONTENT = 100;
	private final int MSG_EVALUATE_DATA = 101;
	private final int MSG_EVALUATE_SCU = 102;
	private final int MSG_EVALUATE_FAIL = 103;

	Message msg;
	String id, p_id;
	private int pageIndex = 1;
	private String feedbackContent;

	// /////////////////////////////////
	/**
	 * initViews
	 */
	// 操作按钮
	private Button send_msg;
	private EditText reply;

	// 新闻

	private View headView;
	private TextView tv1, tv2, time_tv, feedbackCount_tv;
	private FrameLayout mFullscreenContainer;
	private FrameLayout mContentView;
	private View mCustomView = null;
	private WebView mWebView;

	private News content_news = new News();

	// 评论
	private ListView content_view;
	private List<Evaluate_Ordinary> evaLists = new ArrayList<Evaluate_Ordinary>();
	private SC_SheTuanDetailAdapter adapter;

	// ////////////////////////////////
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NEWS_CONTENT:
				initContentViews();

				break;
			case MSG_EVALUATE_DATA:
				if (evaLists.size() > 0) {
					evaLists.clear();
				}
				List<Evaluate_Ordinary> aaEvaluate_Ordinaries = (List<Evaluate_Ordinary>) msg.obj;
				evaLists.addAll(aaEvaluate_Ordinaries);
				adapter.notifyDataSetChanged();

				break;

			case MSG_EVALUATE_FAIL:
				Toast.makeText(SC_XinXianShiDetailAty.this, "提交失败，请重试",
						Toast.LENGTH_LONG).show();
				break;

			case MSG_EVALUATE_SCU:
				reply.setText("");
				feedbackContent = "";
				pageIndex = 1;
				initFeedbackData();
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_news_detail);
		Intent intent = getIntent();
		id = intent.getStringExtra("id");
		p_id = getSharedPreferences("userinfo", Activity.MODE_WORLD_READABLE)
				.getString("id", "");
		if (id == null || p_id.equals("")) {
			finish();// 禁止空参进入该界面
		}
		if (PublicTools.getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
			getWindow().setFlags(0x1000000, 0x1000000);
		}

		initTitleView();// 标题栏
		initViews();// view组件
		initContentData();
		initFeedbackData();
		adapter = new SC_SheTuanDetailAdapter(SC_XinXianShiDetailAty.this,
				evaLists);
		content_view.setAdapter(adapter);

	}

	private void initViews() {
		content_view = (ListView) findViewById(R.id.content_view);
		PullToRefreshLayout fresh = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		fresh.setOnRefreshListener(new MyRefreshListenner());
		reply = (EditText) findViewById(R.id.reply);
		send_msg = (Button) findViewById(R.id.send_msg);

		send_msg.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				sendFeedback();

			}
		});

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

		title_tv.setText("校园新鲜事");
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
	 * 这是在获得数据以后进行填充的，后面数据不会变动 所以写一起了
	 */
	private void initContentViews() {
		headView = LayoutInflater.from(SC_XinXianShiDetailAty.this).inflate(
				R.layout.item_sc_xinwen_eva_adapter, null);

		tv1 = (TextView) headView.findViewById(R.id.tv1);
		tv2 = (TextView) headView.findViewById(R.id.tv2);
		feedbackCount_tv = (TextView) headView
				.findViewById(R.id.feedbackCount_tv);
		time_tv = (TextView) headView.findViewById(R.id.time_tv);
		mWebView = (WebView) headView.findViewById(R.id.webview_player);
		mFullscreenContainer = (FrameLayout) headView
				.findViewById(R.id.fullscreen_custom_content);
		mContentView = (FrameLayout) headView.findViewById(R.id.main_content);

		tv1.setText(PublicTools.doWithNullData(content_news.getTitle()));
		tv2.setText(PublicTools.doWithNullData(content_news.getTitle2()));
		time_tv.setText(PublicTools.doWithNullData(content_news
				.getCreate_time()));
		feedbackCount_tv.setText("全部评论 ("
				+ PublicTools.doWithNullData(content_news.getFeedbackCount())
				+ ")");
		initWebView();
		mWebView.loadDataWithBaseURL("null",
				PublicTools.doWithNullData(content_news.getContent()),
				"text/html", "UTF-8", "");

		content_view.addHeaderView(headView);

	}

	/**
	 * 获取新闻内容
	 */
	private void initContentData() {

		new Thread(new Runnable() {
			public void run() {

				try {
					String url = HttpUtil.QUERY_EVENT
							+ "&menu_id=7176add9-6d46-4c97-8983-362848304388&rows=1&page=1&id="
							+ id;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int aaa = js.getInt("total");
					if (aaa != 0) {
						JSONArray array = js.getJSONArray("rows");

						String content = array.optJSONObject(0).getString(
								"content");
						String create_time = array.optJSONObject(0).getString(
								"create_time");
						String img_path = array.optJSONObject(0).getString(
								"img_path");
						String img_src = array.optJSONObject(0).getString(
								"img_src");
						String menu_id = array.optJSONObject(0).getString(
								"menu_id");
						String title = array.optJSONObject(0)
								.getString("title");
						String title2 = array.optJSONObject(0).getString(
								"title2");
						String feedback_count = array.optJSONObject(0)
								.getString("feedback_count");
						String good_count = array.optJSONObject(0).getString(
								"good_count");
						if (img_src != null && !img_src.equals("")
								&& !img_src.equals("null")) {

							String[] imgs = img_src.split(",");
							for (int j = 0; j < imgs.length; j++) {
								content = content.replaceAll(imgs[j],
										"http://115.29.224.248:8080" + imgs[j]);
							}

						}

						content_news.setCreate_time(create_time);
						content_news.setContent(content);
						content_news.setFeedbackCount(feedback_count);
						content_news.setGoodCount(good_count);
						content_news.setImg_path(img_path);
						content_news.setTitle(title);
						content_news.setTitle2(title2);

						msg = handler.obtainMessage(MSG_NEWS_CONTENT);
						msg.sendToTarget();

					} else {
						// 空的数据
						Log.e("新闻内容为空", "新闻详情内容为空");

					}

				} catch (Exception e) {
				}

			}
		}).start();

	}

	/**
	 * 初始化评论数据
	 */
	private void initFeedbackData() {

		new Thread(

		new Runnable() {
			public void run() {
				String url = HttpUtil.QUERY_EVENT_DETAIL
						+ "&menu_id=7176add9-6d46-4c97-8983-362848304388&a_id="
						+ id + "&rows=10&page=" + pageIndex;
				String res = null;
				try {
					res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int total = js.getInt("total");
					if (total > 0) {
						JSONArray array = js.getJSONArray("rows");
						List<Evaluate_Ordinary> abc = new ArrayList<Evaluate_Ordinary>();
						Evaluate_Ordinary bcd = null;
						for (int i = 0; i < array.length(); i++) {
							bcd = new Evaluate_Ordinary();

							String content = array.optJSONObject(i).getString(
									"content");
							String create_time = array.optJSONObject(i)
									.getString("create_time");
							String p_id = array.optJSONObject(i).getString(
									"p_id");
							String p_name = array.optJSONObject(i).getString(
									"p_name");
							String head_info = array.optJSONObject(i)
									.getString("head_info");
							String start_year = array.optJSONObject(i)
									.getString("start_year");
							String sex = array.optJSONObject(i)
									.getString("sex");
							String pro_name = array.optJSONObject(i).getString(
									"pro_name");

							bcd.setContent(content);
							bcd.setCreate_time(create_time);
							bcd.setHead_info(head_info);
							bcd.setP_id(p_id);
							bcd.setP_name(p_name);
							bcd.setStart_year(start_year);
							bcd.setSex(sex);
							bcd.setPro_name(pro_name);

							abc.add(bcd);
						}
						msg = handler.obtainMessage(MSG_EVALUATE_DATA);
						msg.obj = abc;
						msg.sendToTarget();
					} else {
						// 无数据或者出错了
					}

				} catch (JSONException exception) {
					Log.e("校园新闻", "获取新闻信息json异常");
				}

				catch (Exception e) {
					Log.e("校园新闻", "获取新闻信息异常");
					e.printStackTrace();
				}

			}
		}).start();

	}

	/**
	 * 发送评论操作
	 */
	private void sendFeedback() {
		feedbackContent = reply.getText().toString().trim();
		if (feedbackContent != null && !"".equals(feedbackContent)
				&& !"null".equals(feedbackContent)) {
			evaluateSheTuan();
			InputMethodManager manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
			manager.toggleSoftInput(0, InputMethodManager.RESULT_HIDDEN);
		} else {
			Toast.makeText(SC_XinXianShiDetailAty.this, "评价内容为空",
					Toast.LENGTH_LONG).show();

		}
	}

	/**
	 * 评论线程
	 */
	private void evaluateSheTuan() {

		new Thread(

		new Runnable() {
			public void run() {
				String url = HttpUtil.SUBMIT_EVENT_FEEDBACK + "&table_name=0";
				// 此处接口必须写成post请求模式 否则提交时会出现乱码
				try {
					Map<String, String> map = new HashMap<String, String>();

					map.put("content", feedbackContent);
					map.put("a_id", id);
					map.put("p_id", p_id);
					String res = HttpUtil.postRequest(url, map);

					JSONObject js = new JSONObject(res);

					if ("1".equals(js.getString("result"))) {
						// 评论成功

						msg = handler.obtainMessage(MSG_EVALUATE_SCU);
						msg.sendToTarget();
					} else {
						// 评论失败
						msg = handler.obtainMessage(MSG_EVALUATE_FAIL);
						msg.sendToTarget();
					}
				} catch (JSONException exception) {
					Log.e("校园社团评论", "获取社团信息json异常");
				} catch (Exception e) {
					Log.e("校园社团评论", "获取社团信息异常");
					e.printStackTrace();
				}
			}
		}).start();

	}

	/*************************************************************************************/

	/**
	 * 初始化控件
	 */

	private void initWebView() {
		WebSettings settings = mWebView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(PluginState.ON);
		// settings.setPluginsEnabled(true);
		settings.setLoadWithOverviewMode(true);

		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("UTF-8");
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 适应屏幕
		settings.setLoadWithOverviewMode(true);

		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new MyWebViewClient());

		mWebView.setVisibility(View.VISIBLE);

		settings.setAppCacheEnabled(true);

	}

	class MyWebChromeClient extends WebChromeClient {

		private CustomViewCallback mCustomViewCallback;
		private int mOriginalOrientation = 1;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			onShowCustomView(view, mOriginalOrientation, callback);
			super.onShowCustomView(view, callback);

		}

		public void onShowCustomView(View view, int requestedOrientation,
				WebChromeClient.CustomViewCallback callback) {
			if (mCustomView != null) {
				callback.onCustomViewHidden();
				return;
			}
			if (PublicTools.getPhoneAndroidSDK() >= 14) {
				mFullscreenContainer.addView(view);
				mCustomView = view;
				mCustomViewCallback = callback;
				mOriginalOrientation = getRequestedOrientation();
				mContentView.setVisibility(View.INVISIBLE);
				mFullscreenContainer.setVisibility(View.VISIBLE);
				mFullscreenContainer.bringToFront();

				setRequestedOrientation(mOriginalOrientation);
			}

		}

		public void onHideCustomView() {
			mContentView.setVisibility(View.VISIBLE);
			if (mCustomView == null) {
				return;
			}
			mCustomView.setVisibility(View.GONE);
			mFullscreenContainer.removeView(mCustomView);
			mCustomView = null;
			mFullscreenContainer.setVisibility(View.GONE);
			try {
				mCustomViewCallback.onCustomViewHidden();
			} catch (Exception e) {
			}
			// Show the content view.

			setRequestedOrientation(mOriginalOrientation);
		}

	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	@Override
	public void onPause() {// 继承自Activity
		super.onPause();
		if (mWebView != null) {
			mWebView.onPause();

		}
	}

	@Override
	public void onResume() {// 继承自Activity
		super.onResume();
		if (mWebView != null) {

			mWebView.onResume();
		}
	}

	/**
	 * 
	 * @author : BugsRabbit
	 * @email 395360255@qq.com
	 * @version 创建时间：2015-12-8 下午2:43:27
	 * @function ：刷新控件的刷新事件
	 */
	class MyRefreshListenner implements OnRefreshListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener
		 * #onRefresh(com.daguo.libs.pulltorefresh.PullToRefreshLayout)
		 */
		@Override
		public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
			pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
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
			pageIndex++;
			initFeedbackData();
			// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
			new Handler() {
				@Override
				public void handleMessage(Message msg) {
					// 千万别忘了告诉控件刷新完毕了哦！

					pullToRefreshLayout
							.refreshFinish(PullToRefreshLayout.SUCCEED);
				}
			}.sendEmptyMessageDelayed(0, 3000);
		}

	}

}
