package com.daguo.ui.school.shuoshuo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.radar.RadarNearbyInfo;
import com.baidu.mapapi.radar.RadarNearbyResult;
import com.baidu.mapapi.radar.RadarNearbySearchOption;
import com.baidu.mapapi.radar.RadarSearchError;
import com.baidu.mapapi.radar.RadarSearchListener;
import com.baidu.mapapi.radar.RadarSearchManager;
import com.baidu.mapapi.radar.RadarUploadInfo;
import com.baidu.mapapi.radar.RadarUploadInfoCallback;
import com.daguo.R;
import com.daguo.ui.user.UserInfoAty;
import com.daguo.util.adapter.OutLetAdapter;
import com.daguo.util.beans.OutLet;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

public class SC_ShuoShuo_TabShenbianFragment extends Fragment implements
		RadarUploadInfoCallback, RadarSearchListener, BDLocationListener {

	// 1 提交自己的经纬度坐标信息 然后获得周边用户的信息
	// 2 得到的信息通过服务器数据请求，得到用户信息详情
	// 3 点击进入个人信息界面选择聊天或者关注

	private final int MSG_USERINFO_DOWITH = 10001;
	private final int MSG_LOAD = 10002;
	private final int MSG_CHOICE = 10003;
	private final int MSG_FAIL = 10004;

	/**
	 * initViews
	 */
	private ListView listView;

	// 条件选择
	Dialog dialog;
	Window window;

	// 定位相关
	LocationClient mLocClient;
	private int pageIndex = 0;
	private LatLng pt = null;

	// 周边雷达相关
	RadarNearbyResult listResult = null;
	ListView mResultListView = null;
	private String userID = "";
	private String userComment = "";
	private boolean uploadAuto = false;
	private boolean isSearchFirst = true;

	// 界面数据相关
	private OutLetAdapter adapter;
	private List<OutLet> lists = new ArrayList<OutLet>();// 总的list
	private List<OutLet> choiceLists = new ArrayList<OutLet>();// 用于筛选的list
	private OutLet list;
	private String ids;// 搜索到的用户id

	private TextView back_tv, title_tv, function_tv;
	private ImageView remind_iv;
	private LinearLayout ll;

	/**
	 * tools
	 */
	// CustomProgressDialog progressDialog;

	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_USERINFO_DOWITH:
				listView.setVisibility(View.VISIBLE);
				ll.setVisibility(View.GONE);
				if (null != msg.obj) {
					@SuppressWarnings("unchecked")
					List<OutLet> outLets = (List<OutLet>) msg.obj;
					for (int i = 0; i < outLets.size(); i++) {
						lists.get(i).setName(outLets.get(i).getName());
						lists.get(i).setId(outLets.get(i).getId());
						lists.get(i)
								.setHead_info(outLets.get(i).getHead_info());
						lists.get(i).setSex(outLets.get(i).getSex());
						lists.get(i).setStart_year(
								outLets.get(i).getStart_year());
						lists.get(i).setPro_name(outLets.get(i).getPro_name());

					}

				}
				choiceLists.addAll(lists);
				adapter.notifyDataSetChanged();
				// progressDialog.dismiss();
				break;
			case MSG_LOAD:
				loadUserData();
				break;

			case MSG_CHOICE:
				lists.clear();
				if (null != msg.obj) {
					@SuppressWarnings("unchecked")
					List<OutLet> a = (List<OutLet>) msg.obj;
					lists.addAll(a);
					adapter.notifyDataSetChanged();

				}

				break;
			case MSG_FAIL:
				// progressDialog.dismiss();
				// 加载到的附近的人为空或者异常
				// TODO ！处理界面显示
				listView.setVisibility(View.GONE);
				ll.setVisibility(View.VISIBLE);

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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getShared();
		// progressDialog = CustomProgressDialog.createDialog(getActivity()
		// .getApplicationContext(), "加载中，请稍候");
		// progressDialog.show();
		// 初始化UI和地图

		SDKInitializer.initialize(getActivity().getApplicationContext());

		// 周边雷达设置监听
		RadarSearchManager.getInstance().addNearbyInfoListener(this);
		// 周边雷达设置用户，id为空默认是设备标识
		RadarSearchManager.getInstance().setUserID(userID);
		// 定位初始化
		mLocClient = new LocationClient(getActivity());
		mLocClient.registerLocationListener(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(false);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_sc_shuoshuo_tabshenbian,
				null);
		initUI(view);

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// // 退出时销毁定位 暂不销毁
		// mLocClient.stop();
		// // 释放周边雷达相关
		// RadarSearchManager.getInstance().removeNearbyInfoListener(this);
		// RadarSearchManager.getInstance().clearUserInfo();
		// RadarSearchManager.getInstance().destroy();
		System.gc();
		super.onDestroy();
	}

	/**
	 * 界面初始化
	 */
	private void initUI(View view) {
		listView = (ListView) view.findViewById(R.id.listView);
		adapter = new OutLetAdapter(getActivity(), lists);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int p,
					long arg3) {

				Intent intent = new Intent(getActivity(), UserInfoAty.class);
				intent.putExtra("id", lists.get(p).getId());
				startActivity(intent);
			}
		});

		back_tv = (TextView) view.findViewById(R.id.back_tv);
		function_tv = (TextView) view.findViewById(R.id.function_tv);
		title_tv = (TextView) view.findViewById(R.id.title_tv);
		remind_iv = (ImageView) view.findViewById(R.id.remind_iv);

		back_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				getActivity().finish();
			}
		});
		function_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showDialog();

			}
		});
		function_tv.setText("全部");
		title_tv.setText("身边的人");
		remind_iv.setVisibility(View.GONE);

		ll = (LinearLayout) view.findViewById(R.id.ll);
		ll.setVisibility(View.GONE);

	}

	private void getShared() {
		SharedPreferences sp = getActivity().getSharedPreferences("userinfo",
				Activity.MODE_PRIVATE);
		/**
		 * 用户信息绑定
		 */
		String p_id;
		p_id = sp.getString("id", "null");
		// p_sex = sp.getString("sex", "null");

		userID = p_id;

	}

	@SuppressLint("InlinedApi")
	@SuppressWarnings({ "static-access", "deprecation" })
	private void showDialog() {
		// 创建dialog 利用自定义的style以去除系统默认时会有的黑色背景
		dialog = new Dialog(getActivity(), R.style.sc_huodong_dialog);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置为没有标题
		dialog.show();// 显示出来
		window = dialog.getWindow();
		window.setContentView(R.layout.item_outlet_spinner);
		TextView all_tv = (TextView) window.findViewById(R.id.all_tv);
		TextView man_tv = (TextView) window.findViewById(R.id.man_tv);
		TextView woman_tv = (TextView) window.findViewById(R.id.woman_tv);

		all_tv.setOnClickListener(new Clicks());
		man_tv.setOnClickListener(new Clicks());
		woman_tv.setOnClickListener(new Clicks());

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

	/*-----------------------------------------------------------*/
	/**
	 * 上传一次位置
	 * 
	 * @param v
	 */
	private void uploadOnceClick() {
		if (pt == null) {
			Toast.makeText(getActivity(), "未获取到位置", Toast.LENGTH_LONG).show();
			return;
		}
		RadarUploadInfo info = new RadarUploadInfo();
		info.comments = userComment;
		info.pt = pt;
		RadarSearchManager.getInstance().uploadInfoRequest(info);

	}

	/**
	 * 查找周边的人
	 * 
	 * @param v
	 */
	private void searchNearby() {
		if (pt == null) {
			handler.sendEmptyMessage(MSG_FAIL);
			Toast.makeText(getActivity(), "未获取到位置", Toast.LENGTH_LONG).show();
			return;
		}
		pageIndex = 0;
		searchRequest(pageIndex);
	}

	/**
	 * 开始查找
	 * 
	 * @param index
	 */
	private void searchRequest(int index) {

		RadarNearbySearchOption option = new RadarNearbySearchOption()
				.centerPt(pt).pageNum(pageIndex).radius(10000);
		RadarSearchManager.getInstance().nearbyInfoRequest(option);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baidu.location.BDLocationListener#onReceiveLocation(com.baidu.location
	 * .BDLocation) 定位的sdk回调 ，多次回调使用该方法，确保精确度
	 */
	@Override
	public void onReceiveLocation(BDLocation arg0) {

		if (arg0 == null)
			return;
		pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
		if (pt == null) {
			handler.sendEmptyMessage(MSG_FAIL);
			Toast.makeText(getActivity(), "未获取到个人信息", Toast.LENGTH_LONG).show();
		} else {

			uploadOnceClick();

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baidu.mapapi.radar.RadarSearchListener#onGetClearInfoState(com.baidu
	 * .mapapi.radar.RadarSearchError) 清楚信息的回调，用于显示清除信息后返回结果。
	 */
	@Override
	public void onGetClearInfoState(RadarSearchError arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baidu.mapapi.radar.RadarSearchListener#onGetNearbyInfoList(com.baidu
	 * .mapapi.radar.RadarNearbyResult, com.baidu.mapapi.radar.RadarSearchError)
	 * 获取附近的人列表的回调，用于获取附近的人
	 */
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onGetNearbyInfoList(RadarNearbyResult result,
			RadarSearchError error) {
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// Toast.makeText(getActivity(), "查询周边成功",
			// Toast.LENGTH_LONG).show();
			// 获取成功
			listResult = result;
			List<RadarNearbyInfo> raList = result.infoList;
			List<String> idList = new ArrayList<String>();

			for (int i = 0; i < raList.size(); i++) {

				int distance = raList.get(i).distance;
				java.util.Date timeStamp = raList.get(i).timeStamp;
				String userId = raList.get(i).userID;

				list = new OutLet();
				list.setDistance(String.valueOf(distance) + "米");
				SimpleDateFormat sf = new SimpleDateFormat(
						"EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
				String time = timeStamp + "";
				try {
					Date date = sf.parse(time);
					String times = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(date);
					list.setTime(times);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				list.setId(userId);

				lists.add(list);
				idList.add(userId);
			}
			ids = PublicTools.listToString(idList);

			handler.sendEmptyMessage(MSG_LOAD);

		} else {
			// 获取失败
			// isSearchFirst = true;
			Log.e("加载附近的人功能", "加载无结果");
			handler.sendEmptyMessage(MSG_FAIL);
			// Toast.makeText(getActivity(), "查询周边失败" + error,
			// Toast.LENGTH_LONG)
			// .show();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baidu.mapapi.radar.RadarSearchListener#onGetUploadState(com.baidu
	 * .mapapi.radar.RadarSearchError) 上传结果回调，上传个人信息返回上传结果显示
	 */
	@Override
	public void onGetUploadState(RadarSearchError error) {
		if (error == RadarSearchError.RADAR_NO_ERROR) {
			// 上传成功
			if (!uploadAuto) {
				// Toast.makeText(OutLetAty.this, "单次上传位置成功", Toast.LENGTH_LONG)
				// .show();
				// 执行一次用户信息收集 。
				if (isSearchFirst) {
					isSearchFirst = false;
					searchNearby();

				} else {

				}

			}
		} else {
			// 上传失败
			if (!uploadAuto) {
				handler.sendEmptyMessage(MSG_FAIL);
				Toast.makeText(getActivity(), "单次上传位置失败", Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.baidu.mapapi.radar.RadarUploadInfoCallback#onUploadInfoCallback()
	 * 上传信息的回调，上传时调用此方法 上传个人信息
	 */
	@Override
	public RadarUploadInfo onUploadInfoCallback() {
		// TODO Auto-generated method stub
		return null;
	}

	/*---------------------------------------------*/

	/**
	 * 根据id查询用户
	 */
	private void loadUserData() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_USERINFO_NEARBY
							+ "&page=1&rows=150";
					Map<String, String> map = new HashMap<String, String>();
					map.put("ids", ids);

					String res = HttpUtil.postRequest(url, map);

					JSONObject jsonObject = new JSONObject(res);
					List<OutLet> abc = new ArrayList<OutLet>();
					if (!"0".equals(jsonObject.getString("total"))) {
						JSONArray array = jsonObject.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String id = array.optJSONObject(i).getString("id");
							String name = array.optJSONObject(i).getString(
									"name");
							String head_info = array.optJSONObject(i)
									.getString("head_info");
							String sex = array.optJSONObject(i)
									.getString("sex");
							String pro_name = array.optJSONObject(i).getString(
									"pro_name");
							String start_year = array.optJSONObject(i)
									.getString("start_year");

							list = new OutLet();
							list.setName(name);
							list.setId(id);
							list.setHead_info(head_info);
							list.setSex(sex);
							list.setStart_year(start_year);
							list.setPro_name(pro_name);
							abc.add(list);

						}
						msg = handler.obtainMessage(MSG_USERINFO_DOWITH);

						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 没有I用户

						handler.sendEmptyMessage(MSG_FAIL);

					}
				} catch (Exception e) {
				}
			}
		}).start();
	}

	/*---------------------------------------*/

	/**
	 * 实现顶部点击事件，
	 * 
	 * @author bugs_rabbit
	 */
	private class Clicks implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (null != choiceLists && choiceLists.size() > 0) {
				List<OutLet> l = new ArrayList<OutLet>();

				switch (v.getId()) {
				case R.id.all_tv:
					function_tv.setText("全部");

					msg = handler.obtainMessage(MSG_CHOICE);
					msg.obj = choiceLists;
					msg.sendToTarget();

					break;
				case R.id.man_tv:
					function_tv.setText("男");
					for (int i = 0; i < choiceLists.size(); i++) {

						if ("0".equals(choiceLists.get(i).getSex())) {
							l.add(choiceLists.get(i));
						}

					}
					msg = handler.obtainMessage(MSG_CHOICE);
					msg.obj = l;
					msg.sendToTarget();

					break;
				case R.id.woman_tv:
					function_tv.setText("女");
					for (int i = 0; i < choiceLists.size(); i++) {

						if ("1".equals(choiceLists.get(i).getSex())) {
							l.add(choiceLists.get(i));
						}

					}
					msg = handler.obtainMessage(MSG_CHOICE);
					msg.obj = l;
					msg.sendToTarget();

					break;

				default:
					break;
				}
				dialog.dismiss();
			} else {
				// 没有数据 不能选则
			}
		}

	}

}
