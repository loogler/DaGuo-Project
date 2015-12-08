/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.huodong;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.SC_SheTuanDetailAdapter;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-8 下午3:17:01
 * @function ：
 */
public class SC_HuoDong_DetailAty1 extends Activity implements
	View.OnClickListener {

    private final int MSG_CONTENT_DATA = 100;
    private final int MSG_EVALUATE_DATA = 101;
    private final int MSG_APPLY_SUCCED = 102;
    private final int MSG_APPLY_FAILED = 103;
    private final int MSG_APPLYED = 104;
    private final int MSG_UNAPPLY = 105;
    private final int MSG_EVALUATE_SCU = 106;
    private final int MSG_EVALUATE_FAIL = 107;

    /**
     * initViews
     */

    private TextView share_tv, apply_tv, evaluate_tv;

    //
    private PullToRefreshLayout refresh_view;
    private ListView content_view;

    /**
     * 评论弹出框
     */
    private PopupWindow editWindow;
    private EditText replyEdit;
    private Button sendBtn;
    private InputMethodManager manager;

    private String feedbackContent;

    /**
     * data
     */

    private String sheTuanId;
    private SC_SheTuan sheTuanList = new SC_SheTuan();
    // 头部显示该活动信息
    private View contentView;
    private TextView title1_tv, title2_tv, feedbackCount_tv;
    private FrameLayout mFullscreenContainer;
    private FrameLayout mContentView;
    private View mCustomView = null;
    private WebView mWebView;

    private List<Evaluate_Ordinary> evaLists = new ArrayList<Evaluate_Ordinary>();
    private SC_SheTuanDetailAdapter adapter = null;

    private String p_id;

    /**
     * tools
     */
    private Message msg;
    private int pageIndex = 1;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT_DATA:
		initContentView();
		break;
	    case MSG_EVALUATE_DATA:
		if (evaLists != null) {
		    evaLists.clear();
		}
		List<Evaluate_Ordinary> aaa = (List<Evaluate_Ordinary>) msg.obj;
		evaLists.addAll(aaa);

		adapter.notifyDataSetChanged();

		break;
	    case MSG_APPLY_FAILED:
		Toast.makeText(SC_HuoDong_DetailAty1.this, "报名失败，请重试",
			Toast.LENGTH_LONG).show();
		break;

	    case MSG_APPLY_SUCCED:

		Toast.makeText(SC_HuoDong_DetailAty1.this, "报名成功！恭喜哦",
			Toast.LENGTH_LONG).show();
		apply_tv.setBackgroundColor(getResources().getColor(
			R.color.gray));
		apply_tv.setTextColor(getResources().getColor(R.color.red_1));
		apply_tv.setText("已报名");
		apply_tv.setClickable(false);
		break;
	    case MSG_UNAPPLY:
		apply_tv.setBackgroundResource(R.drawable.shape_ellipse_orangewhite_cur);
		apply_tv.setTextColor(getResources().getColor(R.color.white));
		apply_tv.setText("报 名");
		apply_tv.setClickable(true);
		break;

	    case MSG_APPLYED:
		apply_tv.setBackgroundColor(getResources().getColor(
			R.color.gray));
		apply_tv.setTextColor(getResources().getColor(R.color.red_1));
		apply_tv.setText("已报名");
		apply_tv.setClickable(false);
		break;
	    case MSG_EVALUATE_SCU:
		loadFeedbackData();
		editWindow.dismiss();
		feedbackContent = "";

		break;

	    case MSG_EVALUATE_FAIL:
		Toast.makeText(SC_HuoDong_DetailAty1.this, "评论失败，请稍候再试",
			Toast.LENGTH_LONG).show();
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
	setContentView(R.layout.aty_sc_shetuandetial);
	Intent intent = getIntent();
	sheTuanId = intent.getStringExtra("id");
	if (sheTuanId == null) {
	    finish();// 禁止空参进入该界面
	}
	SharedPreferences sp = getSharedPreferences("userinfo",
		Activity.MODE_WORLD_READABLE);
	p_id = sp.getString("id", "");
	initHeadView();// 头部公共view
	initViews();// 初始化相关view
	loadContentData();// 处理该条社团信息数据
	loadFeedbackData();// 处理评论列表数据
	checkApplyInfo();// 查看是否已经报名

	adapter = new SC_SheTuanDetailAdapter(SC_HuoDong_DetailAty1.this,
		evaLists);
	content_view.setAdapter(adapter);

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
	title_tv.setText("社团详情");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 
     */
    private void initViews() {

	share_tv = (TextView) findViewById(R.id.share_tv);
	apply_tv = (TextView) findViewById(R.id.apply_tv);
	evaluate_tv = (TextView) findViewById(R.id.evaluate_tv);

	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (ListView) findViewById(R.id.content_view);

	share_tv.setOnClickListener(this);
	apply_tv.setOnClickListener(this);
	evaluate_tv.setOnClickListener(this);

	refresh_view.setOnRefreshListener(new MyRefreshListenner());

    }

    /**
     * 填充方式加载的 社团内容
     */
    private void initContentView() {
	contentView = LayoutInflater.from(SC_HuoDong_DetailAty1.this).inflate(
		R.layout.item_sc_shetuan_eva_adapter, null);
	title1_tv = (TextView) contentView.findViewById(R.id.title1_tv);
	title2_tv = (TextView) contentView.findViewById(R.id.title2_tv);

	feedbackCount_tv = (TextView) contentView
		.findViewById(R.id.feedbackCount_tv);

	mFullscreenContainer = (FrameLayout) contentView
		.findViewById(R.id.fullscreen_custom_content);
	mContentView = (FrameLayout) contentView
		.findViewById(R.id.main_content);
	mWebView = (WebView) contentView.findViewById(R.id.webview_player);

	title1_tv.setText(sheTuanList.getTitle());
	title2_tv.setText(sheTuanList.getTitle2());
	feedbackCount_tv.setText("全部评论 " + sheTuanList.getFeedback_count());
	initWebView();
	mWebView.loadDataWithBaseURL("null", sheTuanList.getContent(),
		"text/html", "UTF-8", "");

	content_view.addHeaderView(contentView);

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

    /*********************************************************************************************************/

    /**
     * 加载社团内容的数据
     */
    private void loadContentData() {
	new Thread(

	new Runnable() {
	    public void run() {
		String url = HttpUtil.QUERY_EVENT
			+ "&menu_id=b3b7866c-3bf9-48a7-8caa-effa1fb86782&rows=1&page=1&id="
			+ sheTuanId;
		String res = null;
		try {
		    res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    int total = js.getInt("total");
		    if (total > 0) {
			JSONArray array = js.getJSONArray("rows");

			String content = array.optJSONObject(0).getString(
				"content");
			String create_time = array.optJSONObject(0).getString(
				"create_time");
			String feedback_count = array.optJSONObject(0)
				.getString("feedback_count");
			String good_count = array.optJSONObject(0).getString(
				"good_count");
			String id = array.optJSONObject(0).getString("id");
			String img_src = array.optJSONObject(0).getString(
				"img_src");
			String menu_id = array.optJSONObject(0).getString(
				"menu_id");
			String sort = array.optJSONObject(0).getString("sort");
			String title = array.optJSONObject(0)
				.getString("title");
			String title2 = array.optJSONObject(0).getString(
				"title2");

			if (img_src != null && !img_src.equals("")
				&& !img_src.equals("null")) {

			    String[] imgs = img_src.split(",");
			    for (int j = 0; j < imgs.length; j++) {
				content = content.replaceAll(imgs[j],
					"http://115.29.224.248:8080" + imgs[j]);
			    }

			}
			sheTuanList.setContent(content);
			
			sheTuanList.setCreate_time(create_time);
			sheTuanList.setFeedback_count(feedback_count);
			sheTuanList.setGood_count(good_count);
			sheTuanList.setImg_src(img_src);
			sheTuanList.setTitle(title);
			sheTuanList.setTitle2(title2);

			msg = handler.obtainMessage(MSG_CONTENT_DATA);
			msg.sendToTarget();
		    } else {
			// 无数据或者出错了
		    }

		} catch (JSONException exception) {
		    Log.e("校园社团", "获取社团信息json异常");
		}

		catch (Exception e) {
		    Log.e("校园社团", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();
    }

    /**
     * 加载社团评论的数据
     */
    private void loadFeedbackData() {
	new Thread(

	new Runnable() {
	    public void run() {
		String url = HttpUtil.QUERY_EVENT_DETAIL
			+ "&menu_id=b3b7866c-3bf9-48a7-8caa-effa1fb86782&a_id="
			+ sheTuanId + "&rows=10&page=" + pageIndex;
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
		    Log.e("校园社团", "获取社团信息json异常");
		}

		catch (Exception e) {
		    Log.e("校园社团", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();
    }

    @Override
    public void onPause() {// 继承自Activity
	super.onPause();
	// mWebView.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
	super.onResume();
	// mWebView.onResume();
    }

    /******************************** onclick 事件 ***************************************************************/
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.share_tv:
	    Intent sendIntent = new Intent();
	    sendIntent.setAction(Intent.ACTION_SEND);
	    sendIntent.putExtra(Intent.EXTRA_TEXT, "大果校园下载地址"
		    + HttpUtil.DOWNLOAD);// 分享内容
	    sendIntent.setType("text/plain");
	    startActivity(Intent.createChooser(sendIntent, "大果校园欢迎您"));// 分享标题
	    break;
	case R.id.apply_tv:
	    applyToSheTuan();
	    break;

	case R.id.evaluate_tv:
	    showDialog();

	    break;
	case R.id.send_msg:
	    feedbackContent = replyEdit.getText().toString().trim();
	    if (feedbackContent != null && !"".equals(feedbackContent)
		    && !"null".equals(feedbackContent)) {
		evaluateSheTuan();
	    } else {
		Toast.makeText(SC_HuoDong_DetailAty1.this, "评价内容为空",
			Toast.LENGTH_LONG).show();

	    }

	    break;

	default:
	    break;
	}
    }

    /**
     * 检查该用户是否已经报名
     * 
     */
    private void checkApplyInfo() {
	new Thread(new Runnable() {
	    public void run() {

		String url = HttpUtil.QUERY_EVENT_APPLY
			+ "&table_name=0&source_id=" + sheTuanId + "&p_id="
			+ p_id + "&page=1&rows=1";
		try {
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    if (js.getInt("total") > 0) {
			// 已经报名了
			msg = handler.obtainMessage(MSG_APPLYED);
			msg.sendToTarget();
		    } else {
			// 尚未报名
			msg = handler.obtainMessage(MSG_UNAPPLY);
			msg.sendToTarget();
		    }
		} catch (JSONException exception) {
		    Log.e("校园社团详情", "获取社团信息json异常");
		} catch (Exception e) {
		    Log.e("校园社团详情", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();

    }

    /**
     * 报名社团线程
     */
    private void applyToSheTuan() {

	new Thread(new Runnable() {
	    public void run() {
		String url = HttpUtil.SUBMIT_EVENT_APPLY
			+ "&table_name=0&type=0&p_id=" + p_id + "&source_id="
			+ sheTuanId;
		try {
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);

		    if ("1".equals(js.getString("result"))) {

			msg = handler.obtainMessage(MSG_APPLY_SUCCED);
			msg.sendToTarget();

		    } else {
			// 报名失败
			msg = handler.obtainMessage(MSG_APPLY_FAILED);
			msg.sendToTarget();
		    }

		} catch (JSONException exception) {
		    Log.e("校园社团详情", "获取社团信息json异常");
		} catch (Exception e) {
		    Log.e("校园社团详情", "获取社团信息异常");
		    e.printStackTrace();
		}
	    }
	}).start();

    }

    /**
     * 评论社团线程
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
		    map.put("a_id", sheTuanId);
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

    /**
     * 给出用户输入框，用于评论
     */
    private void showDialog() {
	initPopupWindow();
	showDiscuss();
    }

    /**
     * 初始化评论弹出框
     * 
     */
    private void initPopupWindow() {
	View editView = getLayoutInflater().inflate(
		R.layout.item_popup_editinput, null);
	editWindow = new PopupWindow(editView, LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT, true);
	editWindow.setBackgroundDrawable(getResources().getDrawable(
		R.color.white));
	editWindow.setOutsideTouchable(true);
	replyEdit = (EditText) editView.findViewById(R.id.reply);
	sendBtn = (Button) editView.findViewById(R.id.send_msg);
	sendBtn.setOnClickListener(SC_HuoDong_DetailAty1.this);
    }

    /**
     * 显示回复评论框
     * 
     * @param reply
     */
    private void showDiscuss() {
	replyEdit.setFocusable(true);
	replyEdit.requestFocus();

	// 设置焦点，不然无法弹出输入法
	editWindow.setFocusable(true);

	// 以下两句不能颠倒
	editWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
	editWindow
		.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	editWindow.showAtLocation(refresh_view, Gravity.BOTTOM, 0, 0);

	// 显示键盘
	manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
	manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	editWindow.setOnDismissListener(new OnDismissListener() {
	    @Override
	    public void onDismiss() {
		manager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
	    }
	});

    }

    /************************ 刷新事件 ************************************************/
    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-12-1 下午2:45:23
     * @function ：刷新监听事件
     */
    private class MyRefreshListenner implements OnRefreshListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onRefresh(com
	 * .daguo.libs.PullToRefreshLayout)
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	    pageIndex = 1;
	    loadFeedbackData();

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onLoadMore(com
	 * .daguo.libs.PullToRefreshLayout)
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	    pageIndex++;
	    loadFeedbackData();
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
