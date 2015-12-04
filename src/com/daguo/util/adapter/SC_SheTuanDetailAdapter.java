/**
 * 互相学习 共同进步
 */
package com.daguo.util.adapter;

import java.text.ParseException;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-1 下午4:41:48
 * @function ：
 */
public class SC_SheTuanDetailAdapter extends BaseAdapter {

    SC_SheTuan list;
    List<Evaluate_Ordinary> lists;
    Activity activity;

    View view = null;
    TextView title1_tv, title2_tv, feedbackCount_tv;
    FrameLayout mFullscreenContainer;
    FrameLayout mContentView;
    View mCustomView = null;
    WebView mWebView;

    public SC_SheTuanDetailAdapter(Activity activity, SC_SheTuan list,
	    List<Evaluate_Ordinary> lists) {
	this.activity = activity;
	this.list = list;
	this.lists = lists;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
	
	return lists.size() + 1;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
	
	return list ==null ?lists.get(position):lists.get(position+1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
	return position;
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

	if (position == 0) {
	    view = LayoutInflater.from(activity).inflate(
		    R.layout.item_sc_shetuan_eva_adapter, null);
	    title1_tv = (TextView) view.findViewById(R.id.title1_tv);
	    title2_tv = (TextView) view.findViewById(R.id.title2_tv);

	    feedbackCount_tv = (TextView) view
		    .findViewById(R.id.feedbackCount_tv);

	    mFullscreenContainer = (FrameLayout) view
		    .findViewById(R.id.fullscreen_custom_content);
	    mContentView = (FrameLayout) view.findViewById(R.id.main_content);
	    mWebView = (WebView) view.findViewById(R.id.webview_player);

	    title1_tv.setText(list.getTitle());
	    title2_tv.setText(list.getTitle2());
	    feedbackCount_tv.setText("全部评论 " + list.getFeedback_count());
	    initWebView();
	    mWebView.loadDataWithBaseURL("null", list.getContent(),
		    "text/html", "UTF-8", "");

	} else {
	    view = LayoutInflater.from(activity).inflate(
		    R.layout.adapter_eva_ordinary, null);

	    CircularImage head_circularimage = (CircularImage) view
		    .findViewById(R.id.head_circularimage);
	    TextView floor_tv = (TextView) view.findViewById(R.id.floor_tv);
	    TextView name_tv = (TextView) view.findViewById(R.id.name_tv);
	    TextView time_tv = (TextView) view.findViewById(R.id.time_tv);
	    ImageView sex_iv = (ImageView) view.findViewById(R.id.sex_iv);
	    TextView year_department_tv = (TextView) view
		    .findViewById(R.id.year_department_tv);
	    TextView content_tv = (TextView) view.findViewById(R.id.content_tv);

	    FinalBitmap.create(activity).display(head_circularimage,
		    HttpUtil.IMG_URL + lists.get(position).getHead_info());
	    floor_tv.setText(position + " 楼");
	    name_tv.setText(lists.get(position).getP_name());
	    try {
		time_tv.setText(PublicTools.DateFormat(lists.get(position)
			.getCreate_time()));
	    } catch (ParseException e) {
		Log.e("社交评论", "时间差计算的格式出现问题");
		e.printStackTrace();
	    }
	    if (lists.get(position).getSex().equals("0")) {
		// nan
		sex_iv.setImageResource(R.drawable.icon_sex_man);
	    } else if (lists.get(position).getSex().equals("1")) {
		// nv
		sex_iv.setImageResource(R.drawable.icon_sex_woman);
	    }

	    year_department_tv.setText(lists.get(position).getStart_year()
		    + "级 " + lists.get(position).getPro_name());
	    content_tv.setText(lists.get(position).getContent());

	}
	return view;
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
		mOriginalOrientation = activity.getRequestedOrientation();
		mContentView.setVisibility(View.INVISIBLE);
		mFullscreenContainer.setVisibility(View.VISIBLE);
		mFullscreenContainer.bringToFront();

		activity.setRequestedOrientation(mOriginalOrientation);
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

	    activity.setRequestedOrientation(mOriginalOrientation);
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

}
