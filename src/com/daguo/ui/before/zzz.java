//package com.daguo.ui.before;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.Log;
//import android.view.View;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.baidu.location.BDLocation;
//import com.baidu.location.BDLocationListener;
//import com.baidu.location.LocationClient;
//import com.baidu.location.LocationClientOption;
//import com.baidu.mapapi.SDKInitializer;
//import com.baidu.mapapi.model.LatLng;
//import com.baidu.mapapi.radar.RadarNearbyInfo;
//import com.baidu.mapapi.radar.RadarNearbyResult;
//import com.baidu.mapapi.radar.RadarNearbySearchOption;
//import com.baidu.mapapi.radar.RadarSearchError;
//import com.baidu.mapapi.radar.RadarSearchListener;
//import com.baidu.mapapi.radar.RadarSearchManager;
//import com.baidu.mapapi.radar.RadarUploadInfo;
//import com.baidu.mapapi.radar.RadarUploadInfoCallback;
//import com.daguo.R;
//import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
//import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
//import com.daguo.util.adapter.OutLetAdapter;
//import com.daguo.util.beans.OutLet;
//import com.daguo.utils.HttpUtil;
//import com.daguo.utils.PublicTools;
//import com.daguo.view.dialog.CustomProgressDialog;
//
///**
// * @author : BugsRabbit
// * @email 395360255@qq.com
// * @version 创建时间：2015-11-25 上午9:06:34
// * @function ：
// */
//
//public class zzz extends Activity implements BDLocationListener,
//	RadarSearchListener, RadarUploadInfoCallback {
//
//    private final int MSG_SEARCH = 10001;
//    private final int MSG_USERINFO = 10002;
//    private final int MSG_USERINFO_DOWITH = 10003;
//    private int radius = 2000;
//
//    private CustomProgressDialog dialog;
//
//    //
//    private ListView content_view;
//    private PullToRefreshLayout pullToRefreshLayout;
//
//    //
//
//    private OutLetAdapter adapter;
//    private List<OutLet> lists = new ArrayList<OutLet>();
//    private OutLet list;
//    private String ids;// 搜索到的用户id
//
//    Message msg;
//    @SuppressLint("HandlerLeak")
//    Handler handler = new Handler() {
//	public void handleMessage(android.os.Message msg) {
//	    switch (msg.what) {
//	    case MSG_SEARCH:
//
//		break;
//
//	    case MSG_USERINFO:
//
//
//		loadUserData();
//	    case MSG_USERINFO_DOWITH:
//		List<OutLet> l = (List<OutLet>) msg.obj;
//		for (int i = 0; i < lists.size(); i++) {
//		    lists.get(i).setName(l.get(i).getName());
//		    lists.get(i).setId(l.get(i).getId());
//		    lists.get(i).setHead_info(l.get(i).getHead_info());
//		    lists.get(i).setSex(l.get(i).getSex());
//		    lists.get(i).setStart_year(l.get(i).getStart_year());
//		    lists.get(i).setPro_name(l.get(i).getPro_name());
//
//		}
//		adapter.notifyDataSetChanged();
//
//		break;
//	    default:
//
//		break;
//	    }
//	};
//    };
//
//    /**
//     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
//     */
//    public class SDKReceiver extends BroadcastReceiver {
//	public void onReceive(Context context, Intent intent) {
//	    String s = intent.getAction();
//	    Log.d("zzz log", "action: " + s);
//	    TextView text = (TextView) findViewById(R.id.a);
//	    text.setTextColor(Color.RED);
//	    if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
//		text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
//	    } else if (s
//		    .equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
//		text.setText("key 验证成功! 功能可以正常使用");
//		text.setTextColor(Color.YELLOW);
//	    } else if (s
//		    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
//		text.setText("网络出错");
//		Toast.makeText(zzz.this, "网络出错！", Toast.LENGTH_LONG).show();
//	    }
//	}
//    }
//
//    private SDKReceiver mReceiver;
//
//    // 定位相关
//    LocationClient mLocClient;
//    private int pageIndex = 0;
//    private int curPage = 0;
//    private int totalPage = 0;
//    private LatLng pt = null;
//
//    // 周边雷达相关
//    RadarNearbyResult listResult = null;
//    ListView mResultListView = null;
//    // RadarResultListAdapter mResultListAdapter = null;
//    private String userID = "";
//    private String userComment = "";
//    private boolean uploadAuto = false;
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see android.app.Activity#onCreate(android.os.Bundle)
//     */
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//	super.onCreate(savedInstanceState);
//	setContentView(R.layout.aty_outlet);
//	regist();
//	getShared();
//	try {
//	    System.loadLibrary("locSDK_6.13");
//	} catch (UnsatisfiedLinkError e) {
//	    Log.e("baidu map ", "unsatisfy");
//	}
//
//	//
//	SDKInitializer.initialize(this.getApplicationContext());
//	// locationClient.start();
//	// 周边雷达设置监听
//	RadarSearchManager.getInstance().addNearbyInfoListener(this);
//	// 周边雷达设置用户，id为空默认是设备标识
//	RadarSearchManager.getInstance().setUserID(userID);
//	// 定位初始化
//	mLocClient = new LocationClient(this);
//	mLocClient.registerLocationListener(this);
//	LocationClientOption option = new LocationClientOption();
//	option.setOpenGps(false);// 打开gps
//	option.setCoorType("bd09ll"); // 设置坐标类型
//	option.setScanSpan(1000);
//	mLocClient.setLocOption(option);
//	mLocClient.start();
//
//	TextView tView = (TextView) findViewById(R.id.a);
//
//	// uploadOnceClick();
//	// searchNearby();
//
//
//	//
//	content_view = (ListView) findViewById(R.id.content_view);
//	pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
//	adapter = new OutLetAdapter(this, lists);
//	content_view.setAdapter(adapter);
//
//	pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//
//	    @Override
//	    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
//		new Handler().postDelayed(new Runnable() {
//		    public void run() {
//			// lists.clear();
//			// radius = 1000;
//			// uploadContinueClick();
//			pullToRefreshLayout
//				.refreshFinish(PullToRefreshLayout.SUCCEED);
//
//		    }
//		}, 2000);
//	    }
//
//	    @Override
//	    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
//		new Handler().postDelayed(new Runnable() {
//		    public void run() {
//			radius = radius * 2;
//			uploadOnceClick();
//			pullToRefreshLayout
//				.loadmoreFinish(PullToRefreshLayout.SUCCEED);
//		    }
//		}, 2000);
//	    }
//	});
//
//    }
//
//    private void getShared() {
//	SharedPreferences sp = getSharedPreferences("userinfo",
//		Activity.MODE_PRIVATE);
//	/**
//	 * 用户信息绑定
//	 */
//	String p_id, p_sex;
//	p_id = sp.getString("id", "null");
//
//	p_sex = sp.getString("sex", "null");
//
//	userComment = p_sex;
//	userID = p_id;
//
//    }
//
//    private void regist() {
//	// 注册 SDK 广播监听者
//	IntentFilter iFilter = new IntentFilter();
//	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
//	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
//	iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
//	mReceiver = new SDKReceiver();
//	registerReceiver(mReceiver, iFilter);
//    }
//
//    @Override
//    protected void onDestroy() {
//	super.onDestroy();
//	// 取消监听 SDK 广播
//	unregisterReceiver(mReceiver);
//    }
//
//    /**
//     * 上传一次位置
//     * 
//     * @param v
//     */
//    public void uploadOnceClick() {
//	if (pt == null) {
//	    Toast.makeText(zzz.this, "未获取到位置", Toast.LENGTH_LONG).show();
//	    return;
//	}
//	RadarUploadInfo info = new RadarUploadInfo();
//	info.comments = userComment;
//	info.pt = pt;
//	RadarSearchManager.getInstance().uploadInfoRequest(info);
//
//    }
//
////    /**
////     * 开始自动上传
////     * 
////     * @param v
////     */
////    private void uploadContinueClick() {
////	if (pt == null) {
////	    Toast.makeText(zzz.this, "未获取到位置", Toast.LENGTH_LONG).show();
////	    return;
////	}
////	uploadAuto = true;
////	RadarSearchManager.getInstance().startUploadAuto(this, 60000);
////	handler.sendEmptyMessage(MSG_SEARCH);
////    }
//
//    private void searchNearby() {
//	if (pt == null) {
//	    Toast.makeText(zzz.this, "未获取到位置", Toast.LENGTH_LONG).show();
//	    return;
//	}
//	pageIndex = 0;
//	searchRequest(pageIndex);
//    }
//
//    private void searchRequest(int index) {
//	curPage = 0;
//	totalPage = 0;
//
//	RadarNearbySearchOption option = new RadarNearbySearchOption()
//		.centerPt(pt).pageNum(pageIndex).radius(radius);
//	RadarSearchManager.getInstance().nearbyInfoRequest(option);
//
//	// listPreBtn.setVisibility(View.INVISIBLE);
//	// listNextBtn.setVisibility(View.INVISIBLE);
//	// mapPreBtn.setVisibility(View.INVISIBLE);
//	// mapNextBtn.setVisibility(View.INVISIBLE);
//	// listCurPage.setText("0/0");
//	// mapCurPage.setText("0/0");
//	// mBaiduMap.hideInfoWindow();
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * com.baidu.location.BDLocationListener#onReceiveLocation(com.baidu.location
//     * .BDLocation)
//     */
//    @Override
//    public void onReceiveLocation(BDLocation arg0) {
//	if (arg0 == null)
//	    return;
//	pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
//	// MyLocationData locData = new MyLocationData.Builder()
//	// .accuracy(arg0.getRadius())
//	// // 此处设置开发者获取到的方向信息，顺时针0-360
//	// .direction(100).latitude(arg0.getLatitude())
//	// .longitude(arg0.getLongitude()).build();
//	uploadOnceClick();
//
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * com.baidu.mapapi.radar.RadarSearchListener#onGetClearInfoState(com.baidu
//     * .mapapi.radar.RadarSearchError)
//     */
//    @Override
//    public void onGetClearInfoState(RadarSearchError error) {
//	if (error == RadarSearchError.RADAR_NO_ERROR) {
//	    // 清除成功
//	    Toast.makeText(zzz.this, "清除位置成功", Toast.LENGTH_LONG).show();
//	} else {
//	    // 清除失败
//	    Toast.makeText(zzz.this, "清除位置失败", Toast.LENGTH_LONG).show();
//	}
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * com.baidu.mapapi.radar.RadarSearchListener#onGetNearbyInfoList(com.baidu
//     * .mapapi.radar.RadarNearbyResult, com.baidu.mapapi.radar.RadarSearchError)
//     */
//    @Override
//    public void onGetNearbyInfoList(RadarNearbyResult result,
//	    RadarSearchError error) {
//	if (error == RadarSearchError.RADAR_NO_ERROR) {
//	    Toast.makeText(zzz.this, "查询周边成功", Toast.LENGTH_LONG).show();
//	    // 获取成功
//	    listResult = result;
//	    curPage = result.pageIndex;
//	    totalPage = result.pageNum;
//
//	    // 处理数据
//	    // parseResultToList(listResult);
//	    // parseResultToMap(listResult);
//	    // clearRstBtn.setEnabled(true);
//
//	    // ListView content_view = (ListView) zzz.this
//	    // .findViewById(R.id.content_view);
//	    // content_view.setAdapter(new
//	    // ArrayAdapter<RadarNearbyInfo>(zzz.this,
//	    // android.R.layout.simple_list_item_1, listResult.infoList));
//
//	    List<RadarNearbyInfo> aList = result.infoList;
//
//	    // List<OutLet> dList = new ArrayList<OutLet>();
//	    List<String> idList = new ArrayList<String>();
//	    for (int i = 0; i < aList.size(); i++) {
//		String b = aList.get(i).comments;
//		String c = aList.get(i).userID;
//
//		idList.add(c);
//		list = new OutLet();
//		try {
//
//		    // list.setName("张三");
//		    // list.setId("8ce10832-e592-4148-9e52-8ac3df4c421b");
//		    // list.setHead_info("image/20160120/1453287340010.JPEG");
//		    // list.setSex("0");
//		    // list.setStart_year("2016");
//		    // list.setPro_name("乐哈哈");
//
//		    list.setTime(new SimpleDateFormat("yyyy-mm-dd hh:MM:ss")
//			    .format(aList.get(i).timeStamp));
//		    list.setDistance(aList.get(i).distance + "米");
//		    lists.add(list);
//		} catch (Exception e) {
//
//		}
//
//	    }
//	    ids = PublicTools.listToString(idList);
//
//	    msg = handler.obtainMessage(MSG_USERINFO);
//
//	    msg.sendToTarget();
//	} else {
//	    // 获取失败
//	    curPage = 0;
//	    totalPage = 0;
//	    Toast.makeText(zzz.this, "查询周边失败" + error, Toast.LENGTH_LONG)
//		    .show();
//	}
//
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * com.baidu.mapapi.radar.RadarSearchListener#onGetUploadState(com.baidu
//     * .mapapi.radar.RadarSearchError)
//     */
//    @Override
//    public void onGetUploadState(RadarSearchError error) {
//	if (error == RadarSearchError.RADAR_NO_ERROR) {
//	    // 上传成功
//	    if (!uploadAuto) {
//		searchNearby();
//		Toast.makeText(zzz.this, "单次上传位置成功", Toast.LENGTH_LONG).show();
//	    }
//	} else {
//	    // 上传失败
//	    if (!uploadAuto) {
//		Toast.makeText(zzz.this, "单次上传位置失败", Toast.LENGTH_LONG).show();
//	    }
//	}
//    }
//
//    /*
//     * (non-Javadoc)
//     * 
//     * @see
//     * com.baidu.mapapi.radar.RadarUploadInfoCallback#onUploadInfoCallback()
//     */
//    @Override
//    public RadarUploadInfo onUploadInfoCallback() {
//	RadarUploadInfo info = new RadarUploadInfo();
//	info.comments = userComment;
//	info.pt = pt;
//	Log.e("hjtest", "OnUploadInfoCallback");
//	return info;
//    }
//
//    /* -------------------异步线程----------------------------------------------- */
//    /**
//     * 根据id查询用户
//     */
//    private void loadUserData() {
//	new Thread(new Runnable() {
//	    public void run() {
//		try {
//		    String url = HttpUtil.QUERY_USERINFO_NEARBY
//			    + "&page=1&rows=150&ids=" + ids;
//		    String res = HttpUtil.getRequest(url);
//
//		    JSONObject jsonObject = new JSONObject(res);
//		    List<OutLet> abc = new ArrayList<OutLet>();
//		    if (!"0".equals(jsonObject.getString("total"))) {
//			JSONArray array = jsonObject.getJSONArray("rows");
//			for (int i = 0; i < array.length(); i++) {
//			    String id = array.optJSONObject(i).getString("id");
//			    String name = array.optJSONObject(i).getString(
//				    "name");
//			    String head_info = array.optJSONObject(i)
//				    .getString("head_info");
//			    String sex = array.optJSONObject(i)
//				    .getString("sex");
//			    String pro_name = array.optJSONObject(i).getString(
//				    "pro_name");
//			    String start_year = array.optJSONObject(i)
//				    .getString("start_year");
//
//			    list = new OutLet();
//			    list.setName(name);
//			    list.setId(id);
//			    list.setHead_info(head_info);
//			    list.setSex(sex);
//			    list.setStart_year(start_year);
//			    list.setPro_name(pro_name);
//			    abc.add(list);
//
//			}
//			msg = handler.obtainMessage(MSG_USERINFO_DOWITH);
//
//			msg.obj = abc;
//			msg.sendToTarget();
//
//		    } else {
//			// 没有I用户
//			Toast.makeText(zzz.this, "没有用户", Toast.LENGTH_LONG)
//				.show();
//
//		    }
//		} catch (Exception e) {
//		}
//	    }
//	}).start();
//    }
//
//}
