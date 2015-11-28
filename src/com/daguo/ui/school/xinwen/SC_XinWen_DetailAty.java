package com.daguo.ui.school.xinwen;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.News;
import com.daguo.utils.HttpUtil;

public class SC_XinWen_DetailAty extends Activity {
	Message msg;
	String id, content, img_path, img_src, menu_id, title, title2;

	private TextView tv1, tv2;
	private FrameLayout mFullscreenContainer;
	private FrameLayout mContentView;
	private View mCustomView = null;
	private WebView mWebView;
	private ImageButton friend_back;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				tv1.setText(title);
				tv2.setText(title2);
				mWebView.loadDataWithBaseURL("null", content, "text/html",
						"UTF-8", "");

				break;
			case 1:

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
		if (getPhoneAndroidSDK() >= 14) {// 4.0 需打开硬件加速
			getWindow().setFlags(0x1000000, 0x1000000);
		}
		init();
		loadData();
		initWebView();

	}

	/**
	 * 初始化控件
	 */
	private void init() {
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		mWebView = (WebView) findViewById(R.id.webview_player);
		mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
		mContentView = (FrameLayout) findViewById(R.id.main_content);
		friend_back = (ImageButton) findViewById(R.id.friend_back);
		friend_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

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
			if (getPhoneAndroidSDK() >= 14) {
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

	public static int getPhoneAndroidSDK() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}

	@Override
	public void onPause() {// 继承自Activity
		super.onPause();
		mWebView.onPause();
	}

	@Override
	public void onResume() {// 继承自Activity
		super.onResume();
		mWebView.onResume();
	}

	/**
	 * 填充数据
	 */
	private void loadData() {
		new Thread(new Runnable() {
			public void run() {
				List<News> infos = new ArrayList<News>();

				try {
					String url = HttpUtil.QUERY_NEWS + "&rows=1&page=1&id="
							+ id;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int aaa = js.getInt("total");
					if (aaa != 0) {
						JSONArray array = js.getJSONArray("rows");

						content = array.optJSONObject(0).getString("content");
						img_path = array.optJSONObject(0).getString("img_path");
						img_src = array.optJSONObject(0).getString("img_src");
						menu_id = array.optJSONObject(0).getString("menu_id");
						title = array.optJSONObject(0).getString("title");
						title2 = array.optJSONObject(0).getString("title2");
						if (img_src != null && !img_src.equals("")
								&& !img_src.equals("null")) {

							String[] imgs = img_src.split(",");
							for (int j = 0; j < imgs.length; j++) {
								content = content.replaceAll(imgs[j],
										"http://115.29.224.248:8080" + imgs[j]);

							}

						}
						msg = handler.obtainMessage(0);
						msg.sendToTarget();

					} else {
						// 空的数据
						msg = handler.obtainMessage(1);
						msg.sendToTarget();
					}

				} catch (Exception e) {
				}

			}
		}).start();
	}
}
