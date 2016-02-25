package com.daguo.ui.commercial.coupon;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.beans.CouponEntity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-14 下午1:52:08
 * @function 功能:优惠劵详细信息界面 不包含 （我的优惠卷界面）
 */
@SuppressLint({ "HandlerLeak", "SetJavaScriptEnabled" })
public class CouponDetailAty extends Activity {

	private String p_id;// 个人id
	private String a_id;// 该优惠劵的id
	private final int MSG_COUPONDETAIL = 10001;
	private final int MSG_COUPON_APPLY = 10002;
	private final int MSG_COUPON_APPLY_F = 10003;

	/**
	 * @see initViews ..
	 */
	private ImageView img_iv;
	private TextView name_tv;
	private Button status_btn;
	private FrameLayout mFullscreenContainer, mContentView;
	private WebView mWebView;
	private View mCustomView = null;// 用于转换不同sdk版本的界面，作为中间参数

	/**
	 * data
	 */
	private CouponEntity list = new CouponEntity();

	/**
	 * tools
	 */
	private CustomProgressDialog dialog;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_COUPONDETAIL:
				initContentViews();

				break;
			case MSG_COUPON_APPLY:
				// 提交成功
				dialog.dismiss();
				Toast.makeText(CouponDetailAty.this, "领取成功！", Toast.LENGTH_LONG)
						.show();
				status_btn.setEnabled(false);
				status_btn.setText("已领取");
				break;
			case MSG_COUPON_APPLY_F:
				// 提交失败
				dialog.dismiss();
				Toast.makeText(CouponDetailAty.this, "领取失败，请重试！",
						Toast.LENGTH_LONG).show();

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_coupondetail);
		p_id = getSharedPreferences("userinfo", Activity.MODE_PRIVATE)
				.getString("id", "");
		a_id = getIntent().getStringExtra("id");
		initTitleView();
		initViews();
		loadCouponDetailInfo();

	}

	/**
	 * 
	 */
	private void initViews() {
		img_iv = (ImageView) findViewById(R.id.img_iv);
		name_tv = (TextView) findViewById(R.id.name_tv);
		status_btn = (Button) findViewById(R.id.status_btn);
		mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
		mContentView = (FrameLayout) findViewById(R.id.main_content);
		mWebView = (WebView) findViewById(R.id.webview_player);

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

		title_tv.setText("优惠劵");
		back_fram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/**
	 * 在获得数据后，填充界面详情
	 */
	private void initContentViews() {
		FinalBitmap.create(this).display(
				img_iv,
				HttpUtil.IMG_URL
						+ PublicTools.doWithNullData(list.getImg_path()));
		img_iv.setLayoutParams(new LinearLayout.LayoutParams(PublicTools
				.getWindowWidth(CouponDetailAty.this), 3 * PublicTools
				.getWindowWidth(CouponDetailAty.this) / 5));
		name_tv.setText(PublicTools.doWithNullData(list.getTitle()));
		String sta = PublicTools.doWithNullData(list.getStatus());
		if ("0".equals(sta)) {
			status_btn.setText("领取");
			status_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					statusClick(0);
				}
			});
		} else if ("1".equals(sta)) {
			status_btn.setText("已领取");
			status_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					statusClick(1);
				}
			});
		} else if ("2".equals(sta)) {
			status_btn.setText("已使用");
			status_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					statusClick(2);
				}
			});
		}

		initWebView();
		mWebView.loadDataWithBaseURL("null", list.getContent(), "text/html",
				"UTF-8", "");

	}

	/*------------  *获取数据线程*  ---------------------*/

	/**
	 * 获取详细的优惠卷信息
	 */
	private void loadCouponDetailInfo() {

		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_COUPON_LIST
							+ "&page=1&rows=12&p_id=" + p_id + "&a_id=" + a_id;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);

					int total = jsonObject.getInt("total");

					if (total > 0) {
						JSONArray array = jsonObject.getJSONArray("rows");

						for (int i = 0; i < array.length(); i++) {
							String id = array.optJSONObject(i).getString("id");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String title = array.optJSONObject(i).getString(
									"title");
							String status = array.optJSONObject(i).getString(
									"status");
							String content = array.optJSONObject(i).getString(
									"content");
							String img_src = array.optJSONObject(i).getString(
									"img_src");
							if (img_src != null && !img_src.equals("")
									&& !img_src.equals("null")) {

								String[] imgs = img_src.split(",");
								for (int j = 0; j < imgs.length; j++) {
									content = content.replaceAll(imgs[j],
											"http://115.29.224.248:8080"
													+ imgs[j]);
								}

							}

							list.setId(id);
							list.setImg_path(img_path);
							list.setTitle(title);
							list.setStatus(status);
							list.setContent(content);
							list.setImg_src(img_src);

						}

						handler.sendEmptyMessage(MSG_COUPONDETAIL);

					} else {
						// 没有优惠卷
					}

				} catch (Exception e) {
					Log.e("获取详细的优惠卷信息",
							"CouponDetailAty  =====> loadCouponDetailInfo");
				}
			}
		}).start();

	}

	/**
	 * 提交一个领取优惠卷的请求
	 */
	private void SubmitConpondInfo() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.SUBMIT_COUPON_APPLY + "&p_id=" + p_id
							+ "&a_id=" + a_id;
					String res = HttpUtil.getRequest(url);

					JSONObject jsonObject = new JSONObject(res);
					String result = jsonObject.getString("result");
					if ("1".equals(result)) {
						// suc
						handler.sendEmptyMessage(MSG_COUPON_APPLY);
					} else {
						// 提交失败
						handler.sendEmptyMessage(MSG_COUPON_APPLY_F);
					}

				} catch (Exception e) {
					Log.e("优惠卷详情界面", "CouponDetailAty =====》 SubmitConpondInfo");
				}
			}
		}).start();

	}

	/*--------------- * 点击状态按钮触发事件 * -------------------------*/
	private void statusClick(int type) {
		switch (type) {
		case 0:
			// 领取该优惠劵
			dialog = CustomProgressDialog.createDialog(CouponDetailAty.this,
					"领取中");
			dialog.show();
			SubmitConpondInfo();
			break;
		case 1:
			// 已领取的 在我的优惠卷里使用
			Toast.makeText(CouponDetailAty.this, "您已经领取过啦！", Toast.LENGTH_LONG)
					.show();

			break;

		case 2:
			// 使用完的 就没有了
			Toast.makeText(CouponDetailAty.this, "您的优惠卷已被使用！",
					Toast.LENGTH_LONG).show();

			break;
		default:
			break;
		}
	}

	/*--------------- * 一系列方法来支持手机端 展示webview的方法集合 * -----------------------*/

	/**
	 * 初始化webview
	 */
	@SuppressWarnings("deprecation")
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

	@SuppressWarnings("deprecation")
	public static int getPhoneAndroidSDK() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}
}
