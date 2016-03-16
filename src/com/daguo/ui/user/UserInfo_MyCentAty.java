/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.writeshuoshuo.ui.SC_ShuoShuo_WriteAty;
import com.daguo.ui.commercial.cent.CentAty;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuoAty1;
import com.daguo.ui.school.xinwen.SC_XinWenAty;
import com.daguo.util.adapter.UserInfo_MyCent_DailyTaskAdapter;
import com.daguo.util.adapter.UserInfo_MyCent_NewTaskAdapter;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.NewTask;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-22 下午2:58:31
 * @function ：个人积分界面
 */
public class UserInfo_MyCentAty extends Activity {

	private String p_id;
	private final int MSG_NEWTASK = 10001;
	private final int MSG_CENT = 10002;
	private final int MSG_BANNERLISTSCENT = 10003;
	private final int REQ_SHARE = 10004;
	private final int MSG_SUB = 10005;

	/**
	 * initViews
	 */
	private TextView cent_tv, exchange_tv;
	private ImageView add_iv;

	private ListView newTask_list, dailyTask_list;
	// 新手任务
	private NewTask newTask;
	private List<NewTask> newTasks = new ArrayList<NewTask>();
	private UserInfo_MyCent_NewTaskAdapter newTaskAdapte;
	// 每日任务
	private List<String> nameLists = new ArrayList<String>();
	private List<String> centLists = new ArrayList<String>();
	private UserInfo_MyCent_DailyTaskAdapter dailyTaskAdapter;
	// 广告栏
	private List<AddBanner> addLists = new ArrayList<AddBanner>();
	/**
	 * data
	 */
	private String cent;

	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NEWTASK:
				newTaskAdapte.notifyDataSetChanged();
				PublicTools.setListViewHeightBasedOnChildren(newTask_list);

				break;
			case MSG_CENT:
				cent_tv.setText(PublicTools.doWithNullData(cent));

				break;
			case MSG_BANNERLISTSCENT:
				FinalBitmap.create(UserInfo_MyCentAty.this).display(add_iv,
						HttpUtil.IMG_URL + addLists.get(0).getImg_path());
				break;

			case MSG_SUB:

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
		setContentView(R.layout.aty_userinfo_mycent);
		p_id = getSharedPreferences("userinfo", 0).getString("id", "");
		if ("".equals(PublicTools.doWithNullData(p_id))) {
			Toast.makeText(this, "用户登录信息异常", Toast.LENGTH_LONG).show();
			return;
		}

		initViews();
		loadCentInfo();
		loadAddBanner();
		loadNewTask();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (resultCode) {
		case 0:

			break;

		default:
			break;
		}
	}

	/**
	 * 初始化界面
	 */
	private void initViews() {
		initTitleView();
		cent_tv = (TextView) findViewById(R.id.cent_tv);
		exchange_tv = (TextView) findViewById(R.id.exchange_tv);
		add_iv = (ImageView) findViewById(R.id.add_iv);
		newTask_list = (ListView) findViewById(R.id.newTask_list);
		dailyTask_list = (ListView) findViewById(R.id.dailyTask_list);

		exchange_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(UserInfo_MyCentAty.this,
						CentAty.class);
				startActivity(intent);
			}
		});

		newTaskAdapte = new UserInfo_MyCent_NewTaskAdapter(this, newTasks);
		newTask_list.setAdapter(newTaskAdapte);

		newTask_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {
				if ("0".equals(newTasks.get(p).getState())) {
					Intent intent = new Intent();
					setIntent(intent, p);
				} else if ("1".equals(newTasks.get(p).getState())) {
					Toast.makeText(UserInfo_MyCentAty.this, "已完成",
							Toast.LENGTH_SHORT).show();

				} else {
					Toast.makeText(UserInfo_MyCentAty.this, "信息错误",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

		nameLists.add("打开客户端");
		nameLists.add("发大果帖");
		nameLists.add("帖子被评论");
		nameLists.add("点赞/评论");
		nameLists.add("被赞次数>3");
		centLists.add("2");
		centLists.add("5");
		centLists.add("2");
		centLists.add("1");
		centLists.add("10");
		dailyTaskAdapter = new UserInfo_MyCent_DailyTaskAdapter(this,
				nameLists, centLists);
		dailyTask_list.setAdapter(dailyTaskAdapter);
		PublicTools.setListViewHeightBasedOnChildren(dailyTask_list);

	}

	private void setIntent(Intent intent, int p) {
		String idString = newTasks.get(p).getId();
		if ("071f777d-85cb-43e7-a276-da136057d12f".equals(idString)) {
			// 关注某同学===》跳转同学说列表
			submitMyCent(idString);
			intent.setClass(UserInfo_MyCentAty.this, SC_ShuoShuoAty1.class);
			startActivity(intent);

		} else if ("0906df1c-613e-4cae-8b3a-136f8e86fce5".equals(idString)) {
			// 推荐app给好友===》分享链接
			submitMyCent(idString);

			intent.setAction(Intent.ACTION_SEND);
			intent.putExtra(Intent.EXTRA_TEXT,
					"我在大果校园免费兑换了礼物哎，免费的哟，还能兑换话费流量，不要太赞，你快来看看吧，顺手就能兑换一个礼物呢！传送门"
							+ HttpUtil.DOWNLOAD);// 分享内容
			intent.setType("text/plain");
			startActivity(Intent.createChooser(intent, "大果校园"));// 分享标题
			// startActivityForResult(Intent.createChooser(sendIntent, "大果校园"),
			// REQ_SHARE);
		} else if ("19416592-1497-4211-a20b-a4e518cb51e6".equals(idString)) {
			// 分享告示
			// TODO 分享告示 ===》跳转新闻列表，分享一个信息
			submitMyCent(idString);
			intent.setClass(UserInfo_MyCentAty.this, SC_XinWenAty.class);
			startActivity(intent);
		} else if ("2a35d32a-e3f7-4427-8ac1-2e6907d79128".equals(idString)) {
			// 第一次发表评论
			submitMyCent(idString);
			intent.setClass(UserInfo_MyCentAty.this, SC_ShuoShuoAty1.class);
			startActivity(intent);

		} else if ("a3306d73-6087-4c64-acdd-2977f33f3b7d".equals(idString)) {
			// 分身份认证
			submitMyCent(idString);
			intent.setClass(UserInfo_MyCentAty.this, UserInfo_ModifyAty1.class);
			startActivity(intent);
		} else if ("a38e0962-7382-45c8-b113-e0dce7d24035".equals(idString)) {
			// 第一次发帖
			submitMyCent(idString);
			intent.setClass(UserInfo_MyCentAty.this, SC_ShuoShuo_WriteAty.class);
			startActivity(intent);
		}

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

		title_tv.setText("我的积分");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/*-------------- 异步获取数据------- ----------------------------*/

	/**
	 * 获取新手任务详情
	 */
	private void loadNewTask() {

		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_MYCENT + "&page=1&rows=1&p_id="
							+ p_id;
					String res = HttpUtil.getRequest(url);

					JSONArray array = new JSONArray(res);
					if (array.length() > 0) {
						for (int i = 0; i < array.length(); i++) {
							String name = array.optJSONObject(i).getString(
									"name");
							String grade = array.optJSONObject(i).getString(
									"grade");
							String id = array.optJSONObject(i).getString("id");
							String state = array.optJSONObject(i).getString(
									"status");

							newTask = new NewTask();
							newTask.setGrade(grade);
							newTask.setName(name);
							newTask.setId(id);
							newTask.setState(state);
							newTasks.add(newTask);

						}
						handler.sendEmptyMessage(MSG_NEWTASK);

					} else {
						// 没有数据
					}

				} catch (Exception e) {
				}
			}
		}).start();

	}

	/**
	 * 修改新手任务积分 参数为选定的具体新手任务
	 */
	private void submitMyCent(final String TaskId) {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.SUBMIT_MYCENT + "&p_id=" + p_id
							+ "&type_id=" + TaskId;
					String res = HttpUtil.getRequest(url);
					JSONObject jsonObject = new JSONObject(res);
					if ("1".equals(jsonObject.getString("result"))) {
						// suc
						handler.sendEmptyMessage(MSG_SUB);
					} else {
						// fail
					}

				} catch (Exception e) {
				}
			}
		}).start();
	}

	private void loadAddBanner() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_ADD_BANNER
							+ "&position=13&page=1&rows=16";
					String res = "";
					JSONObject js = null;
					int total = 0;
					res = HttpUtil.getRequest(url);
					js = new JSONObject(res);
					total = js.getInt("total");
					AddBanner list = null;
					if (total == 0) {
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
							addLists.add(list);

						}
						msg = handler.obtainMessage(MSG_BANNERLISTSCENT);
						msg.sendToTarget();
					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("新闻获取广告信息", "获取广告json异常");
				} catch (Exception e) {
					Log.e("新闻获取广告信息", "获取广告异常");
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * 获取个人积分
	 */
	private void loadCentInfo() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_USERINFO
							+ "&page=1&rows=1&p_id=" + p_id;

					String res = HttpUtil.getRequest(url);

					JSONObject jsonObject = new JSONObject(res);

					JSONArray array = jsonObject.getJSONArray("rows");
					cent = array.optJSONObject(0).getString("score");

				} catch (Exception e) {
				}
				handler.sendEmptyMessage(MSG_CENT);
			}
		}

		).start();
	}

	/*----------- 实体类 -----------------*/

}
