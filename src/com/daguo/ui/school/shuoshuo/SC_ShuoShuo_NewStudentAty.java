/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.ui.user.UserInfoAty;
import com.daguo.util.adapter.SC_ShuoShuo_NewStudentListAdapter;
import com.daguo.util.beans.UserInfo;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-18 上午11:49:07
 * @function ：新报道的同学界面
 */
public class SC_ShuoShuo_NewStudentAty extends Activity implements
		OnRefreshListener {

	private final int MSG_STUDENTDATA_SUC = 10001;

	//
	private String school_id;
	private int pageIndex = 1;
	private String sex ="";

	/**
	 * initViews
	 */
	// 标题
	private TextView back_tv, function_tv;

	private Dialog dialog;
	private Window window;
	private String searchString;// 传入搜索条件的参数

	// 同学内容

	private PullToRefreshLayout refresh_view;
	private ListView content_view;

	/**
	 * data
	 * 
	 */
	// 学生信息data
	private List<UserInfo> lists = new ArrayList<UserInfo>();
	private UserInfo list;
	private SC_ShuoShuo_NewStudentListAdapter adapter;

	/**
	 * tools
	 */
	// msg
	private Message msg;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_STUDENTDATA_SUC:
				if (msg.obj != null) {

					@SuppressWarnings("unchecked")
					List<UserInfo> abc = (List<UserInfo>) msg.obj;
					lists.addAll(abc);
					adapter.notifyDataSetChanged();

				} else {
					// msg==null
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
		setContentView(R.layout.aty_sc_shuoshuo_newstudent);

		getDataFromShared();
		initViews();
		loadStudentData(sex);
		// TODO 没有处理性别分类的接口。

	}

	/**
	 * 从本地获取数据
	 */
	private void getDataFromShared() {
		SharedPreferences sp = getSharedPreferences("userinfo",
				Activity.MODE_PRIVATE);

		school_id = sp.getString("school_id", "");
	}

	/**
	 * init
	 */
	private void initViews() {

		// 标题
		back_tv = (TextView) findViewById(R.id.back_tv);
		function_tv = (TextView) findViewById(R.id.function_tv);
		back_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

		function_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setFunctionClicks();
			}
		});

		// 新同学列表
		refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		content_view = (ListView) findViewById(R.id.content_view);

		refresh_view.setOnRefreshListener(this);
		adapter = new SC_ShuoShuo_NewStudentListAdapter(this, lists);
		content_view.setAdapter(adapter);
		content_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(SC_ShuoShuo_NewStudentAty.this,
						UserInfoAty.class);
				intent.putExtra("id", lists.get(arg2).getId());
				startActivity(intent);
			}
		});

	}

	/**
	 * 功能按钮点击
	 */
	@SuppressLint("InlinedApi")
	@SuppressWarnings({ "static-access", "deprecation" })
	private void setFunctionClicks() {

		// 创建dialog 利用自定义的style以去除系统默认时会有的黑色背景
		dialog = new Dialog(SC_ShuoShuo_NewStudentAty.this,
				R.style.sc_huodong_dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置为没有标题
		dialog.show();// 显示出来
		window = dialog.getWindow();
		window.setContentView(R.layout.item_spinner_sexchoice);
		TextView all_tv = (TextView) window.findViewById(R.id.all_tv);
		TextView man_tv = (TextView) window.findViewById(R.id.man_tv);
		TextView woman_tv = (TextView) window.findViewById(R.id.woman_tv);

		all_tv.setOnClickListener(new Clicks());
		woman_tv.setOnClickListener(new Clicks());
		man_tv.setOnClickListener(new Clicks());

		WindowManager.LayoutParams lp = window.getAttributes();
		window.clearFlags(lp.FLAG_BLUR_BEHIND | lp.FLAG_DIM_BEHIND);
		window.setGravity(Gravity.END | Gravity.TOP);
		// lp.x = 100;
		lp.y = 50;
		// lp.width = 300; // 宽度
		// lp.height = 300; // 高度
		lp.alpha = 0.8f; // 透明度
		window.setAttributes(lp);

	}

	/**
	 * 
	 * @author : BugsRabbit
	 * @email 395360255@qq.com
	 * @version 创建时间：2016-1-18 下午4:22:08
	 * @function ：选择的点击响应
	 */
	private class Clicks implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.all_tv:
				function_tv.setText("全部");
				sex="";
				lists.clear();
				adapter.notifyDataSetChanged();
				loadStudentData(sex);
				break;
			case R.id.man_tv:
				function_tv.setText("男生");
				sex="0";
				lists.clear();
				adapter.notifyDataSetChanged();
				loadStudentData(sex);
				break;
			case R.id.woman_tv:
				function_tv.setText("女生");
				sex="1";
				lists.clear();
				adapter.notifyDataSetChanged();
				loadStudentData(sex);
				break;

			default:
				break;
			}
			dialog.dismiss();
		}

	}

	/**
	 * ---------- load data from
	 * thread--------------------------------------------
	 */
	/**
	 * 加载新报道的同学
	 */
	private void loadStudentData(final String sex) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_USERINFO + "&rows=15&page="
							+ pageIndex + "&school_id=" + school_id+"&sex="+sex;
					String res = HttpUtil.getRequest(url);

					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("totalPageNum") < pageIndex) {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SC_ShuoShuo_NewStudentAty.this,
										"已加载全部了！", Toast.LENGTH_SHORT).show();
							}
						});
						return;
					}
					if (jsonObject.getInt("total") > 0) {
						JSONArray array = jsonObject.getJSONArray("rows");
						List<UserInfo> abc = new ArrayList<UserInfo>();
						for (int i = 0; i < array.length(); i++) {
							String head_info = array.optJSONObject(i)
									.getString("head_info");
							String id = array.optJSONObject(i).getString("id");
							String name = array.optJSONObject(i).getString(
									"name");
							String pro_name = array.optJSONObject(i).getString(
									"pro_name");
							String school_id = array.optJSONObject(i)
									.getString("school_id");
							String school_name = array.optJSONObject(i)
									.getString("school_name");
							String sex = array.optJSONObject(i)
									.getString("sex");
							String start_year = array.optJSONObject(i)
									.getString("start_year");

							list = new UserInfo();
							list.setHead_info(head_info);
							list.setId(id);
							list.setName(name);
							list.setPro_name(pro_name);
							list.setSchool_id(school_id);
							list.setSchool_name(school_name);
							list.setSex(sex);
							list.setStart_year(start_year);

							abc.add(list);

						}
						msg = handler.obtainMessage(MSG_STUDENTDATA_SUC);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 获取信息异常，此时应该有自己的帐号。
					}

				} catch (Exception e) {
					Log.e("新报道的同学", "解析异常");
				}
			}
		}).start();
	}

	/*------------------ implement method---------------------------------------*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onRefresh
	 * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		pageIndex = 1;
		lists.clear();
		adapter.notifyDataSetChanged();
		loadStudentData(sex);
		new Handler().postDelayed(new Runnable() {
			public void run() {

				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 1000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onLoadMore
	 * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		pageIndex++;
		loadStudentData(sex);
		new Handler().postDelayed(new Runnable() {
			public void run() {

				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}, 1000);
	}

}
