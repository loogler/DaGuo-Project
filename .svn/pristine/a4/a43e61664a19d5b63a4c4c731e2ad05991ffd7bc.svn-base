/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.outlet;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.util.beans.AppDownload;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 下午2:46:10
 * @function ：软件下载详情主界面
 */
@SuppressLint("HandlerLeak")
public class AppDownLoadDetailAty extends Activity {

    private final int MSG_APP_DATA_SUC = 10001;
    private String id;

    /**
     * initViews
     */
    private ImageView pic_iv;
    private TextView comp_tv, name_tv;
    private NoScrollGridView grid;

    // 内容
    private FrameLayout mFullscreenContainer;
    private FrameLayout mContentView;
    private View mCustomView = null;
    private WebView mWebView;

    // xiazai
    private TextView download_tv;

    /**
     * data
     */
    private AppDownload list = new AppDownload();

    // 软件截图
    private String[] files;

    /**
     * tools
     */

    private Message msg;
    private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_APP_DATA_SUC:

		setData();
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
	setContentView(R.layout.aty_appdownloaddetail);
	id = getIntent().getStringExtra("id");
	initViews();
	loadAppData();
    }

    private void initViews() {
	initHeadView();
	grid = (NoScrollGridView) findViewById(R.id.grid);
	pic_iv = (ImageView) findViewById(R.id.pic_iv);
	comp_tv = (TextView) findViewById(R.id.comp_tv);
	name_tv = (TextView) findViewById(R.id.name_tv);

	mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
	mContentView = (FrameLayout) findViewById(R.id.main_content);
	mWebView = (WebView) findViewById(R.id.webview_player);

	download_tv = (TextView) findViewById(R.id.download_tv);
	download_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
//		String url = "http://115.29.224.248:8080/uploadFiles/"
//			+ list.getDownload_path();

//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setData(Uri.parse(url));
//		startActivity(intent);
	    }
	});
    }

    /**
     * 通用的 headview 不同位置会出现不同的页面要求，根据情况设置
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
	title_tv.setText("软件详情");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 根据获取的值 给视图赋值
     */
    private void setData() {
//	FinalBitmap.create(AppDownLoadDetailAty.this).display(pic_iv,
//		HttpUtil.IMG_URL + list.getImg_path());
//	comp_tv.setText(PublicTools.doWithNullData(list.getDev_company()));
//	name_tv.setText(PublicTools.doWithNullData(list.getName()));

	int size = files.length;
	int length = PublicTools.getWindowWidth(AppDownLoadDetailAty.this) / 4;

	DisplayMetrics dm = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(dm);
	float density = dm.density;
	int gridviewWidth = (int) (size * (length + 4) * density);
	int itemWidth = (int) (length * density);

	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
	grid.setLayoutParams(params);
	grid.setColumnWidth(itemWidth);

	grid.setHorizontalSpacing(10);
	grid.setStretchMode(GridView.NO_STRETCH);
	grid.setVerticalScrollBarEnabled(true);
	grid.setNumColumns(size);
	grid.setAdapter(new GridAdapter());
	grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		imageBrower(position, files);
	    }
	});

	initWebView();
//	mWebView.loadDataWithBaseURL("null", list.getRemark(), "text/html",
//		"UTF-8", "");
    }

    /**
     * 加载app详细情况
     */
    private void loadAppData() {
	new Thread(new Runnable() {
	    public void run() {

		try {

		    String url = HttpUtil.QUERY_APP_DOWNLOAD
			    + "&rows=6&page=1&id=" + id;
		    String res = HttpUtil.getRequest(url);

		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("total") > 0) {
			// data
			JSONArray array = jsonObject.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {

			    String cut_path = array.optJSONObject(i).getString(
				    "cut_path");// 截图
			    String dev_company = array.optJSONObject(i)
				    .getString("dev_company");// 开发公司
			    String download_path = array.optJSONObject(i)
				    .getString("download_path");// 下载地址
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");// 图片介绍
			    String name = array.optJSONObject(i).getString(
				    "name");// 软件名
			    String id = array.optJSONObject(i).getString("id");// id
			    String remark = array.optJSONObject(i).getString(
				    "remark");// 软件介绍
			    String size = array.optJSONObject(i).getString(
				    "size");// 软件大小
			    String type = array.optJSONObject(i).getString(
				    "type");// 分类

//			    list.setCut_path(cut_path);
//			    list.setDev_company(dev_company);
//			    list.setDownload_path(download_path);
//			    list.setId(id);
//			    list.setImg_path(img_path);
//			    list.setName(name);
//			    list.setRemark(remark);
//			    list.setSize(size);
//			    list.setType(type);

			    files = cut_path.split(",");

			}
			msg = handler.obtainMessage(MSG_APP_DATA_SUC);

			msg.sendToTarget();

		    } else {

		    }

		} catch (Exception e) {
		}

	    }
	}).start();

    }

    @SuppressLint("SetJavaScriptEnabled")
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

    //
    private void imageBrower(int position, String[] urls) {
	Intent intent = new Intent(AppDownLoadDetailAty.this,
		com.daguo.modem.photo.ImagePagerActivity.class);
	// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
		position);
	startActivity(intent);
    }

    class GridAdapter extends BaseAdapter {

	@Override
	public int getCount() {
	    return files == null ? 0 : files.length > 5 ? 5 : files.length;
	}

	@Override
	public String getItem(int position) {
	    return files[position];
	}

	@Override
	public long getItemId(int position) {
	    return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    MyGridViewHolder viewHolder;
	    if (convertView == null) {
		viewHolder = new MyGridViewHolder();
		convertView = LayoutInflater.from(AppDownLoadDetailAty.this)
			.inflate(R.layout.gridview_item_app, parent, false);
		viewHolder.imageView = (ImageView) convertView
			.findViewById(R.id.album_image);
		LayoutParams params = new LayoutParams(
			PublicTools.getWindowWidth(AppDownLoadDetailAty.this) / 2,
			PublicTools.getWindowWidth(AppDownLoadDetailAty.this) * 5 / 6);
		viewHolder.imageView.setLayoutParams(params);
		convertView.setTag(viewHolder);
	    } else {
		viewHolder = (MyGridViewHolder) convertView.getTag();
	    }
	    String url = HttpUtil.IMG_URL + getItem(position);
	    /*
	     * ImageLoader.getInstance().init(
	     * ImageLoaderConfiguration.createDefault(context));
	     */
	    ImageLoader.getInstance().displayImage(url, viewHolder.imageView);

	    return convertView;
	}

    }

    private static class MyGridViewHolder {
	ImageView imageView;
    }
}
