package com.daguo.ui.school.shuoshuo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.adapter.Eva_OrdinaryAdapter;
import com.daguo.util.base.CircularImage;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.pulllistview.XListView;
import com.daguo.util.pulllistview.XListView.IXListViewListener;
import com.daguo.utils.HttpUtil;
import com.daguo.view.dialog.CustomProgressDialog;

public class SC_ShuoShuo_EvaluationAty extends Activity implements
		OnClickListener, IXListViewListener, OnItemClickListener {
	String tag = "SC_ShuoShuo_EvaluationAty";
	final int  INITDATA=0 ,ZANADD=1,ZANMIN=2,PINGLUN=3;
	private ImageView headView, imgView, sex_iv;
	private TextView user_nick, content, date, schoolName, type;
	private RelativeLayout evaluat;
	CircularImage photo;
	private XListView listView;
	// 4个操作按钮 收藏点赞 分享评论
	private TextView shoucangTextView, fenxiangTextView, dianzanTextView,
			pinglunTextView;
/**
 * tool
 * 
 */
	CustomProgressDialog dialog;
	private FinalBitmap finalBitmap;
/**
 * 弹窗显示输入框
 */
	private PopupWindow editWindow;
	private EditText replyEdit;
	private Button sendBtn;
	private InputMethodManager manager;
	String id, good_count, feedback_count, content1, time, img_path, p_name,
			p_avator, p_sex, school_name, type_name;
	String feedback_content;
	String p_id;

	// 数据
	private List<Evaluate_Ordinary> lists = new ArrayList<Evaluate_Ordinary>();
	private Evaluate_Ordinary list;
	private Eva_OrdinaryAdapter adapter;

	Message msg;
	private int pageIndex = 1;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case INITDATA:
			
				if (msg.obj != null && !msg.obj.equals("")) {
					List<Evaluate_Ordinary> sss = (List<Evaluate_Ordinary>) msg.obj;
					lists.addAll(sss);
					adapter.notifyDataSetChanged();
				} else {
					Log.e(tag, "msg==null  位于msg=1");
				}
				break;
			case ZANADD:
				dialog.dismiss();
				break; 
			case ZANMIN:
				dialog.dismiss();
				break;
			case PINGLUN:
				dialog.dismiss();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_shuoshuo_evaluation);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		Intent i = getIntent();
		id = i.getStringExtra("id");
		good_count = i.getStringExtra("good_count");
		feedback_count = i.getStringExtra("feedback_count");
		content1 = i.getStringExtra("content");
		time = i.getStringExtra("time");
		img_path = i.getStringExtra("img_path");
		p_name = i.getStringExtra("p_name");
		p_avator = i.getStringExtra("p_avator");
		p_sex = i.getStringExtra("sex");
		school_name = i.getStringExtra("school_name");
		type_name = i.getStringExtra("type");

		init();//初始化控件
		adapter = new Eva_OrdinaryAdapter(SC_ShuoShuo_EvaluationAty.this,
				lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);
		listView.setPullRefreshEnable(true);
		listView.setPullLoadEnable(true);
		listView.setXListViewListener(this);
		initData();//初始化数据

	}
/**
 * 初始化控件
 */
	private void init() {
		evaluat = (RelativeLayout) findViewById(R.id.evaluat);// 根布局
		// 实例化的数据
		headView = (ImageView) findViewById(R.id.photo);
		imgView = (ImageView) findViewById(R.id.image_content);
		user_nick = (TextView) findViewById(R.id.name);
		content = (TextView) findViewById(R.id.content_text);
		date = (TextView) findViewById(R.id.date);
		listView = (XListView) findViewById(R.id.xlistview);
		schoolName = (TextView) findViewById(R.id.schoolname);
		sex_iv = (ImageView) findViewById(R.id.sex_iv);
		type = (TextView) findViewById(R.id.type);
		// 操作按钮
		shoucangTextView = (TextView) findViewById(R.id.shoucang);
		fenxiangTextView = (TextView) findViewById(R.id.fenxiang);
		dianzanTextView = (TextView) findViewById(R.id.dianzan);
		pinglunTextView = (TextView) findViewById(R.id.pinglun);
		shoucangTextView.setOnClickListener(this);
		fenxiangTextView.setOnClickListener(this);
		dianzanTextView.setOnClickListener(this);
		pinglunTextView.setOnClickListener(this);

		imgView.setOnClickListener(this);
		content.setText(content1);

		schoolName.setText(school_name);
		if (p_sex.equals("0")) {
			sex_iv.setVisibility(View.VISIBLE);
			sex_iv.setImageResource(R.drawable.icon_sex_man);
		} else if (p_sex.equals("1")) {
			sex_iv.setVisibility(View.VISIBLE);
			sex_iv.setImageResource(R.drawable.icon_sex_woman);
		} else {
			// 性别不明
			sex_iv.setVisibility(View.GONE);
		}
		type.setText(type_name);
		date.setText(handTime(time));
		FinalBitmap.create(SC_ShuoShuo_EvaluationAty.this).display(headView,
				HttpUtil.IMG_URL + p_avator);
		if (img_path != null && !img_path.equals("")
				&& !img_path.equals("null") && !img_path.equals("[]")) {
			FinalBitmap.create(SC_ShuoShuo_EvaluationAty.this).display(imgView,
					HttpUtil.IMG_URL + img_path);

		} else {
			imgView.setVisibility(View.GONE);
		}

	}

	/**
	 * 初始化的界面数据 也可用于操作后界面刷新
	 */
	private void initData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					List<Evaluate_Ordinary> ls = new ArrayList<Evaluate_Ordinary>();
					String url = HttpUtil.QUERY_SHUOSHUO_EVA + "&rows=20&page="
							+ pageIndex;
					Map<String, String> map = new HashMap<String, String>();
					map.put("t_id", id);
					String res = HttpUtil.postRequest(url, map);
					JSONObject js = new JSONObject(res);
					int total = js.getInt("total");
					if (total != 0) {
						// 有评论
						JSONArray array = js.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							list = new Evaluate_Ordinary();
							String parent_id = array.optJSONObject(i)
									.getString("parent_id");
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
							list.setContent(content);
							list.setCreate_time(create_time);
							list.setHead_info(head_info);
							list.setP_id(p_id);
							list.setP_name(p_name);
							list.setParent_id(parent_id);
							ls.add(list);
						}

						msg = handler.obtainMessage(INITDATA);
						msg.obj = ls;
						msg.sendToTarget();

					} else {
						// 无评论
//						msg = handler.obtainMessage(1);
//						msg.sendToTarget();

					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	/**
	 * 点赞数+1
	 */
	private void zanAdd() {
		new Thread(new Runnable() {
			public void run() {

				// 修改点赞数

				try {
					String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", id);// id
					String res = HttpUtil.postRequest(url, map);
					JSONObject js = new JSONObject(res);
					String aaa = js.getString("msg");// 返回字段
					if (aaa.contains("操作失败")) {
						// 失败
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SC_ShuoShuo_EvaluationAty.this,
										"提交失败，请重试", Toast.LENGTH_SHORT).show();
							}
						});
					} else {
						// 界面赞数增加+1
						msg = handler.obtainMessage(ZANADD);
						msg.sendToTarget();
					}

				} catch (Exception e) {
				}

			}
		}).start();
	}

	/**
	 * 点赞数-1
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-31 上午9:01:33
	 */
	private void zanMin() {
		new Thread(new Runnable() {
			public void run() {

				try {
					int cc = Integer.parseInt(good_count) - 2;
					String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
					Map<String, String> map = new HashMap<String, String>();
					map.put("id", id);// id
					map.put("godd_count", String.valueOf(cc));
					String res = HttpUtil.postRequest(url, map);
					JSONObject js = new JSONObject(res);
					String aaa = js.getString("msg");// 返回字段
					if (aaa.contains("操作失败")) {
						// 失败
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(SC_ShuoShuo_EvaluationAty.this,
										"提交失败，请重试", Toast.LENGTH_SHORT).show();
							}
						});

					} else {
						// 界面赞数增加-1
						msg = handler.obtainMessage(ZANMIN);
						msg.sendToTarget();
					}

				} catch (Exception e) {
				}

			}
		}).start();
	}

	/**
	 * 评论说说
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-28 上午10:31:24
	 */
	private void sendShuoShuo() {
		new Thread(new Runnable() {
			public void run() {

				// /先调出输入框 获取文字 提交 显示在界面
				feedback_content = replyEdit.getText().toString();
				if (feedback_content != null && !feedback_content.equals("")) {
					String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
					// String
					// url="http://192.168.1.103:8080/XYYYT/service/topicFeedback/saveOrUpdate?android=1";
					Map<String, String> map = new HashMap<String, String>();
					map.put("t_id", id);
					map.put("content", feedback_content);
					map.put("p_id", p_id);

					try {
						String res = HttpUtil.postRequest(url, map);

						msg = handler.obtainMessage(PINGLUN);
						msg.sendToTarget();
						initData();

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					// 空内容
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SC_ShuoShuo_EvaluationAty.this,
									"文字不能为空", Toast.LENGTH_SHORT).show();
						}
					});
				}
				// dialog.dismiss();

			}
		}).start();
	}

	/**
	 * 这是一个现实edittext 这个隐性视图的方法 显示然后可以输入
	 */
	private void showDiscuss() {

		View editView = getLayoutInflater().inflate(
				R.layout.item_shuoshuo_reply, null);
		editWindow = new PopupWindow(editView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		editWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.white));
		editWindow.setOutsideTouchable(true);
		replyEdit = (EditText) editView.findViewById(R.id.reply);
		sendBtn = (Button) editView.findViewById(R.id.send_msg);

		replyEdit.setFocusable(true);
		replyEdit.requestFocus();

		// 设置焦点，不然无法弹出输入法
		editWindow.setFocusable(true);

		// 以下两句不能颠倒
		editWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		editWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		editWindow.showAtLocation(evaluat, Gravity.BOTTOM, 0, 0);

		// 显示键盘
		manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		editWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				manager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
			}
		});
		// 发表说说的评论
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog = CustomProgressDialog.createDialog(
						SC_ShuoShuo_EvaluationAty.this, "加载中。。。");
				editWindow.dismiss();
				dialog.show();
				sendShuoShuo();
			}
		});

	}

	/**
	 * 处理时间
	 * 
	 * @param string
	 * @return
	 */
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String handTime(String time) {
		if (time == null || "".equals(time.trim())) {
			return "";
		}
		try {
			Date date = format.parse(time);
			long tm = System.currentTimeMillis();// 当前时间戳
			long tm2 = date.getTime();// 发表动态的时间戳
			long d = (tm - tm2) / 1000;// 时间差距 单位秒
			if ((d / (60 * 60 * 24)) > 0) {
				return d / (60 * 60 * 24) + "天前";
			} else if ((d / (60 * 60)) > 0) {
				return d / (60 * 60) + "小时前";
			} else if ((d / 60) > 0) {
				return d / 60 + "分钟前";
			} else {
				// return d + "秒前";
				return "刚刚";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param position
	 * @param urls
	 */
	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(SC_ShuoShuo_EvaluationAty.this,
				com.daguo.modem.photo.ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(
				com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(
				com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
				position);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.shoucang:
			
			break;
		case R.id.fenxiang:

			break;
		case R.id.pinglun:
			
			showDiscuss();

			break;
		case R.id.dianzan:
			dialog = CustomProgressDialog.createDialog(
					SC_ShuoShuo_EvaluationAty.this, "加载中。。。");
			dialog.show();
			zanAdd();
			
			
			break;
		case R.id.image_content:
			String[] urls = new String[] { img_path };

			imageBrower(0, urls);

			break;

		default:
			break;
		}
	}

	private void onLoad() {
		listView.stopRefresh();
		listView.stopLoadMore();
		listView.setRefreshTime("0");
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		lists.clear();
		initData();

		onLoad();

	}

	@Override
	public void onLoadMore() {
		pageIndex++;
		lists.clear();
		initData();
		onLoad();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
}
