/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.outlet;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.adapter.APPDownloadAdapter;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.AppDownload;
import com.daguo.utils.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 上午11:38:12
 * @function ： app下载软件
 */
public class AppDownLoadAty extends Activity implements OnRefreshListener {

    private final int MSG_ADD_SUC = 10001;
    private final int MSG_APP_SUC = 10002;
    private FinalBitmap finalBitmap;
    private int pageIndex = 1;

    /**
     * initViews
     */
    private ImageView add_iv;
    private PullToRefreshLayout refresh_view;
    private GridView content_view;

    private List<AddBanner> addBanners = new ArrayList<AddBanner>();

    private List<AppDownload> lists = new ArrayList<AppDownload>();
    private AppDownload list;
    private APPDownloadAdapter adapter;

    /**
     * tools
     */
    private Message msg;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_ADD_SUC:
		finalBitmap = FinalBitmap.create(AppDownLoadAty.this);
		finalBitmap.display(add_iv, HttpUtil.IMG_URL
			+ addBanners.get(0).getImg_path());
		add_iv.setOnClickListener(new AddBannerOnclickListener(
			AppDownLoadAty.this, addBanners, 0));
		break;

	    case MSG_APP_SUC:

		if (null != msg.obj) {
		    @SuppressWarnings("unchecked")
		    List<AppDownload> abc = (List<AppDownload>) msg.obj;
		    lists.addAll(abc);
		    adapter.notifyDataSetChanged();

		} else {
		    return;
		}

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
	setContentView(R.layout.aty_appdownload);

	initView();
	loadAddData();
	loadAPPData();

    }

    private void initView() {
	initHeadView();
	add_iv = (ImageView) findViewById(R.id.add_iv);
	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (GridView) findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(this);

	adapter = new APPDownloadAdapter(this, lists);
	content_view.setAdapter(adapter);
	content_view.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int p,
		    long arg3) {
		Intent intent = new Intent(AppDownLoadAty.this,
			AppDownLoadDetailAty.class);
		intent.putExtra("id", lists.get(p).getId());
		startActivity(intent);
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
	title_tv.setText("软件下载");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onRefresh
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	pageIndex = 1;
	lists.clear();
	loadAPPData();

	// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// 千万别忘了告诉控件刷新完毕了哦！

		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 3000);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onLoadMore
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @SuppressLint("HandlerLeak")
    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	pageIndex++;
	loadAPPData();
	// 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// 千万别忘了告诉控件刷新完毕了哦！

		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 3000);

    }

    /** ----------------------data thread -------------------------------------- */
    /**
     * 加载广告栏信息
     */
    private void loadAddData() {
	new Thread(

	new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=8&page=1&rows=1";
		    String res = "";
		    JSONObject js = null;
		    int total = 0;
		    res = HttpUtil.getRequest(url);
		    js = new JSONObject(res);
		    total = js.getInt("total");
		    AddBanner list = null;
		    if (0 == total) {
			// 无广告 ，或者加载异常
			Log.e("主页获取广告信息", "获取首页顶部广告信息异常");

		    } else {
			JSONArray array = js.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String id = array.optJSONObject(i).getString("id");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");
			    String menu_id = array.optJSONObject(i).getString(
				    "menu_id");
			    String source_id = array.optJSONObject(i)
				    .getString("source_id");
			    String type = array.optJSONObject(i).getString(
				    "type");
			    String urls = array.optJSONObject(i).getString(
				    "url");
			    list = new AddBanner();
			    list.setId(id);
			    list.setImg_path(img_path);
			    list.setMenu_id(menu_id);
			    list.setSource_id(source_id);
			    list.setType(type);
			    list.setUrl(urls);
			    addBanners.add(list);
			}
			msg = handler.obtainMessage(MSG_ADD_SUC);
			msg.sendToTarget();
		    }

		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("APP下载获取广告信息", "获取广告json异常");
		} catch (Exception e) {
		    Log.e("APP下载获取广告信息", "获取广告异常");
		    e.printStackTrace();
		}
	    }
	}).start();

    }

    /**
     * 加载app下载列表
     */
    private void loadAPPData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    List<AppDownload> abc = new ArrayList<AppDownload>();
		    String url = HttpUtil.QUERY_APP_DOWNLOAD + "&rows=6&page="
			    + pageIndex;
		    String res = HttpUtil.getRequest(url);

		    JSONObject jsonObject = new JSONObject(res);
		    int totalPageNum = jsonObject.getInt("totalPageNum");
		    if (pageIndex > totalPageNum) {
			runOnUiThread(new Runnable() {
			    public void run() {
				Toast.makeText(AppDownLoadAty.this, "已全部加载完了",
					Toast.LENGTH_LONG).show();
			    }
			});
			return;
		    }
		    if (jsonObject.getInt("total") > 0) {
			// data
			JSONArray array = jsonObject.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    list = new AppDownload();

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

			    list.setCut_path(cut_path);
			    list.setDev_company(dev_company);
			    list.setDownload_path(download_path);
			    list.setId(id);
			    list.setImg_path(img_path);
			    list.setName(name);
			    list.setRemark(remark);
			    list.setSize(size);
			    list.setType(type);
			    abc.add(list);

			}
			msg = handler.obtainMessage(MSG_APP_SUC);
			msg.obj = abc;
			msg.sendToTarget();

		    } else {

		    }

		} catch (Exception e) {
		}
	    }
	}).start();

    }
}
