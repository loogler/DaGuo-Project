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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.daguo.R;
import com.daguo.util.Imp.AddBannerOnclickListener;
import com.daguo.util.adapter.APPDownloadAdapter;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.AppDownload;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-12 上午11:38:12
 * @function ： app下载软件
 */
public class AppDownLoadAty extends Activity  {

	private final int MSG_ADD_SUC = 10001;
	private final int MSG_APP_SUC = 10002;
	private FinalBitmap finalBitmap;

	/**
	 * initViews
	 */
	private ImageView add_iv;
	private ListView content_lv;

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
		//TODO 该类需要处理问题

		initView();
		loadAddData();
		loadAPPData();

	}

	private void initView() {

		add_iv = (ImageView) findViewById(R.id.add_iv);
		content_lv = (ListView) findViewById(R.id.content_lv);

		adapter = new APPDownloadAdapter(this, lists);
		// content_view.setAdapter(adapter);
		// content_view.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int p,
		// long arg3) {
		// Intent intent = new Intent(AppDownLoadAty.this,
		// AppDownLoadDetailAty.class);
		// intent.putExtra("id", lists.get(p).getId());
		// startActivity(intent);
		// }
		// });
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
					String url = HttpUtil.QUERY_APP_DOWNLOAD + "&rows=6&page=1";
					// String url =
					// "http://192.168.0.56:8080/XYYYT/service/softInfo/querySoftInfoListByMobile?android=1&rows=6&page=1";
					String res = HttpUtil.getRequest(url);
					JSONArray array = new JSONArray(res);
					for (int i = 0; i < array.length(); i++) {
						JSONArray arr = array.getJSONArray(i);
						for (int j = 0; j < arr.length(); j++) {
							String type =arr.optJSONObject(0).getString("name");
							
						}
						
					}

					// JSONObject jsonObject = new JSONObject(res);
					//
					// int totalPageNum = jsonObject.getInt("totalPageNum");
					//
					// if (jsonObject.getInt("total") > 0) {
					// // data
					// JSONArray array = jsonObject.getJSONArray("rows");
					// for (int i = 0; i < array.length(); i++) {
					// list = new AppDownload();
					//
					// String cut_path = array.optJSONObject(i).getString(
					// "cut_path");// 截图
					// String dev_company = array.optJSONObject(i)
					// .getString("dev_company");// 开发公司
					// String download_path = array.optJSONObject(i)
					// .getString("download_path");// 下载地址
					// String img_path = array.optJSONObject(i).getString(
					// "img_path");// 图片介绍
					// String name = array.optJSONObject(i).getString(
					// "name");// 软件名
					// String id = array.optJSONObject(i).getString("id");// id
					// String remark = array.optJSONObject(i).getString(
					// "remark");// 软件介绍
					// String size = array.optJSONObject(i).getString(
					// "size");// 软件大小
					// String type = array.optJSONObject(i).getString(
					// "type");// 分类
					//
					// list.setCut_path(cut_path);
					// list.setDev_company(dev_company);
					// list.setDownload_path(download_path);
					// list.setId(id);
					// list.setImg_path(img_path);
					// list.setName(name);
					// list.setRemark(remark);
					// list.setSize(size);
					// list.setType(type);
					// abc.add(list);
					//
					// }
					// msg = handler.obtainMessage(MSG_APP_SUC);
					// msg.obj = abc;
					// msg.sendToTarget();
					//
					// } else {
					//
					// }

				} catch (Exception e) {
				}
			}
		}).start();

	}
}
