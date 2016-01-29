package com.daguo.ui.before;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
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
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.OutLetAdapter;
import com.daguo.util.beans.OutLet;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-25 上午9:06:34
 * @function ：
 */

public class zzz extends Activity implements BDLocationListener,
	RadarSearchListener, RadarUploadInfoCallback {

    private final int MSG_SEARCH = 10001;
    private final int MSG_USERINFO = 10002;
    private int radius = 2000;

    //
    ListView content_view;
    PullToRefreshLayout pullToRefreshLayout;

    //

    OutLetAdapter adapter;
    List<OutLet> lists = new ArrayList<OutLet>();
    OutLet list;

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {
	    switch (msg.what) {
	    case MSG_SEARCH:

		break;

	    case MSG_USERINFO:
		if (null == msg.obj) {
		    return;
		}
		List<OutLet> a = (List<OutLet>) msg.obj;
		lists.addAll(a);
		adapter.notifyDataSetChanged();

	    default:
		break;
	    }
	};
    };

    /**
     * 构造广播监听类，监听 SDK key 验证以及网络异常广播
     */
    public class SDKReceiver extends BroadcastReceiver {
	public void onReceive(Context context, Intent intent) {
	    String s = intent.getAction();
	    Log.d("zzz log", "action: " + s);
	    TextView text = (TextView) findViewById(R.id.text_Info);
	    text.setTextColor(Color.RED);
	    if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
		text.setText("key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置");
	    } else if (s
		    .equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
		text.setText("key 验证成功! 功能可以正常使用");
		text.setTextColor(Color.YELLOW);
	    } else if (s
		    .equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
		text.setText("网络出错");
	    }
	}
    }

    private SDKReceiver mReceiver;

    // 定位相关
    LocationClient mLocClient;
    private int pageIndex = 0;
    private int curPage = 0;
    private int totalPage = 0;
    private LatLng pt = null;

    // 周边雷达相关
    RadarNearbyResult listResult = null;
    ListView mResultListView = null;
    // RadarResultListAdapter mResultListAdapter = null;
    private String userID = "";
    private String userComment = "";
    private boolean uploadAuto = false;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_outlet);
	regist();
	getShared();
	try {
	    System.loadLibrary("locSDK_6.13");
	} catch (UnsatisfiedLinkError e) {
	    Log.e("baidu map ", "unsatisfy");
	}

	//
	SDKInitializer.initialize(this.getApplicationContext());
	// locationClient.start();
	// 周边雷达设置监听
	RadarSearchManager.getInstance().addNearbyInfoListener(this);
	// 周边雷达设置用户，id为空默认是设备标识
	RadarSearchManager.getInstance().setUserID(userID);
	// 定位初始化
	mLocClient = new LocationClient(this);
	mLocClient.registerLocationListener(this);
	LocationClientOption option = new LocationClientOption();
	option.setOpenGps(false);// 打开gps
	option.setCoorType("bd09ll"); // 设置坐标类型
	option.setScanSpan(1000);
	mLocClient.setLocOption(option);
	mLocClient.start();

	TextView tView = (TextView) findViewById(R.id.text_Info);
	tView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		uploadContinueClick();
	    }
	});
	TextView tv = (TextView) findViewById(R.id.text_show);
	tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		searchNearby();
	    }
	});

	//
	content_view = (ListView) findViewById(R.id.content_view);
	pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	adapter = new OutLetAdapter(this, lists);
	content_view.setAdapter(adapter);

	pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {
			// lists.clear();
			// radius = 1000;
			// uploadContinueClick();
			pullToRefreshLayout
				.refreshFinish(PullToRefreshLayout.SUCCEED);

		    }
		}, 2000);
	    }

	    @Override
	    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {
			radius = radius * 2;
			uploadContinueClick();
			pullToRefreshLayout
				.loadmoreFinish(PullToRefreshLayout.SUCCEED);
		    }
		}, 2000);
	    }
	});

    }

    private void getShared() {
	SharedPreferences sp = getSharedPreferences("userinfo",
		Activity.MODE_PRIVATE);
	/**
	 * 用户信息绑定
	 */
	String p_id, p_name, p_img, p_sex, p_pro, p_year;
	p_id = sp.getString("id", "null");
	p_img = sp.getString("head_info", "null");
	p_name = sp.getString("name", "null");
	p_pro = sp.getString("pro_name", "null");
	p_sex = sp.getString("sex", "null");
	p_year = sp.getString("start_year", "null");

	userComment = "A";
	userID = p_id;
//	 + "," + p_img + "," + p_name + "," + p_pro + "," + p_sex
//		+ "," + p_year;
//	 userID = "zhang san ";
    }

    private void regist() {
	// 注册 SDK 广播监听者
	IntentFilter iFilter = new IntentFilter();
	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
	iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
	iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
	mReceiver = new SDKReceiver();
	registerReceiver(mReceiver, iFilter);
    }

    @Override
    protected void onDestroy() {
	super.onDestroy();
	// 取消监听 SDK 广播
	unregisterReceiver(mReceiver);
    }

    /**
     * 开始自动上传
     * 
     * @param v
     */
    private void uploadContinueClick() {
	if (pt == null) {
	    Toast.makeText(zzz.this, "未获取到位置", Toast.LENGTH_LONG).show();
	    return;
	}
	uploadAuto = true;
	RadarSearchManager.getInstance().startUploadAuto(this, 50000);
	handler.sendEmptyMessage(MSG_SEARCH);
    }

    private void searchNearby() {
	if (pt == null) {
	    Toast.makeText(zzz.this, "未获取到位置", Toast.LENGTH_LONG).show();
	    return;
	}
	pageIndex = 0;
	searchRequest(pageIndex);
    }

    private void searchRequest(int index) {
	curPage = 0;
	totalPage = 0;

	RadarNearbySearchOption option = new RadarNearbySearchOption()
		.centerPt(pt).pageNum(pageIndex).radius(radius);
	RadarSearchManager.getInstance().nearbyInfoRequest(option);

	// listPreBtn.setVisibility(View.INVISIBLE);
	// listNextBtn.setVisibility(View.INVISIBLE);
	// mapPreBtn.setVisibility(View.INVISIBLE);
	// mapNextBtn.setVisibility(View.INVISIBLE);
	// listCurPage.setText("0/0");
	// mapCurPage.setText("0/0");
	// mBaiduMap.hideInfoWindow();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.location.BDLocationListener#onReceiveLocation(com.baidu.location
     * .BDLocation)
     */
    @Override
    public void onReceiveLocation(BDLocation arg0) {
	if (arg0 == null)
	    return;
	pt = new LatLng(arg0.getLatitude(), arg0.getLongitude());
	// MyLocationData locData = new MyLocationData.Builder()
	// .accuracy(arg0.getRadius())
	// // 此处设置开发者获取到的方向信息，顺时针0-360
	// .direction(100).latitude(arg0.getLatitude())
	// .longitude(arg0.getLongitude()).build();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarSearchListener#onGetClearInfoState(com.baidu
     * .mapapi.radar.RadarSearchError)
     */
    @Override
    public void onGetClearInfoState(RadarSearchError error) {
	if (error == RadarSearchError.RADAR_NO_ERROR) {
	    // 清除成功
	    Toast.makeText(zzz.this, "清除位置成功", Toast.LENGTH_LONG).show();
	} else {
	    // 清除失败
	    Toast.makeText(zzz.this, "清除位置失败", Toast.LENGTH_LONG).show();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarSearchListener#onGetNearbyInfoList(com.baidu
     * .mapapi.radar.RadarNearbyResult, com.baidu.mapapi.radar.RadarSearchError)
     */
    @Override
    public void onGetNearbyInfoList(RadarNearbyResult result,
	    RadarSearchError error) {
	if (error == RadarSearchError.RADAR_NO_ERROR) {
	    Toast.makeText(zzz.this, "查询周边成功", Toast.LENGTH_LONG).show();
	    // 获取成功
	    listResult = result;
	    curPage = result.pageIndex;
	    totalPage = result.pageNum;

	    // 处理数据
	    // parseResultToList(listResult);
	    // parseResultToMap(listResult);
	    // clearRstBtn.setEnabled(true);

	    // ListView content_view = (ListView) zzz.this
	    // .findViewById(R.id.content_view);
	    // content_view.setAdapter(new
	    // ArrayAdapter<RadarNearbyInfo>(zzz.this,
	    // android.R.layout.simple_list_item_1, listResult.infoList));

	    List<RadarNearbyInfo> aList = result.infoList;

	    List<OutLet> dList = new ArrayList<OutLet>();
	    for (int i = 0; i < aList.size(); i++) {
		String b = aList.get(i).comments;
		String c = aList.get(i).userID;

		String[] user = b.split(",");

		list = new OutLet();
		try {

		    String p_id = PublicTools.doWithNullData(c);
		    String p_img = PublicTools.doWithNullData(user[0]);
		    String p_name = PublicTools.doWithNullData(user[1]);
		    String p_pro = PublicTools.doWithNullData(user[2]);
		    String p_sex = PublicTools.doWithNullData(user[3]);
		    String p_year = PublicTools.doWithNullData(user[4]);
		    list.setName(p_name);
		    list.setId(p_id);
		    list.setHead_info(p_img);
		    list.setSex(p_sex);
		    list.setStart_year(p_year);
		    list.setPro_name(p_pro);
		    // list.setName("张三");
		    // list.setId("8ce10832-e592-4148-9e52-8ac3df4c421b");
		    // list.setHead_info("image/20160120/1453287340010.JPEG");
		    // list.setSex("0");
		    // list.setStart_year("2016");
		    // list.setPro_name("乐哈哈");

		    list.setTime(new SimpleDateFormat("yyyy-mm-dd hh:MM:ss")
			    .format(aList.get(i).timeStamp));
		    list.setDistance(aList.get(i).distance + "米");
		    dList.add(list);
		} catch (Exception e) {

		}

	    }
	    msg = handler.obtainMessage(MSG_USERINFO);
	    msg.obj = dList;
	    msg.sendToTarget();
	} else {
	    // 获取失败
	    curPage = 0;
	    totalPage = 0;
	    Toast.makeText(zzz.this, "查询周边失败" + error, Toast.LENGTH_LONG)
		    .show();
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarSearchListener#onGetUploadState(com.baidu
     * .mapapi.radar.RadarSearchError)
     */
    @Override
    public void onGetUploadState(RadarSearchError error) {
	if (error == RadarSearchError.RADAR_NO_ERROR) {
	    // 上传成功
	    if (!uploadAuto) {
		Toast.makeText(zzz.this, "单次上传位置成功", Toast.LENGTH_LONG).show();
	    }
	} else {
	    // 上传失败
	    if (!uploadAuto) {
		Toast.makeText(zzz.this, "单次上传位置失败", Toast.LENGTH_LONG).show();
	    }
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarUploadInfoCallback#onUploadInfoCallback()
     */
    @Override
    public RadarUploadInfo onUploadInfoCallback() {
	RadarUploadInfo info = new RadarUploadInfo();
	info.comments = userComment;
	info.pt = pt;
	Log.e("hjtest", "OnUploadInfoCallback");
	return info;
    }

    /** ------------------------------------------------------------------ */

}
