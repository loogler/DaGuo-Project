/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.outlet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MyLocationData;
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
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-5 下午3:56:27
 * @function ：身边的人定位
 */
public class OutLetAty extends Activity implements RadarUploadInfoCallback,
	RadarSearchListener, BDLocationListener {
    private final int MSG_USERINFO = 10001;

    //
    ListView content_view;
    PullToRefreshLayout pullToRefreshLayout;

    // 定位相关
    LocationClient mLocClient;
    private int pageIndex = 0;
    // private int curPage = 0;
    // private int totalPage = 0;
    private LatLng pt = null;
    boolean isUpload = false;

    // 周边雷达相关
    RadarNearbyResult listResult = null;
    ListView mResultListView = null;
    // RadarResultListAdapter mResultListAdapter = null;
    private String userID = "";
    private String userComment = "";
    private boolean uploadAuto = false;

    CustomProgressDialog dialog;

    private int radius = 1000;

    /**
     * shipei
     */
    OutLetAdapter adapter;
    List<OutLet> lists = new ArrayList<OutLet>();
    OutLet list;

    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_USERINFO:
		if (null == msg.obj) {
		    return;
		}
		List<OutLet> a = (List<OutLet>) msg.obj;
		lists.addAll(a);
		adapter.notifyDataSetChanged();

		break;

	    default:
		break;
	    }
	};
    };

    // 1 提交自己的经纬度坐标信息 然后获得周边用户的信息
    // 2 得到的信息通过服务器数据请求，得到用户信息详情
    // 3 点击进入个人信息界面选择聊天或者关注

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_outlet);
	getShared();

	content_view = (ListView) findViewById(R.id.content_view);
	pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.refresh_view);

	SDKInitializer.initialize(this.getApplicationContext());

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
	CustomProgressDialog.createDialog(OutLetAty.this, "加載中", 3000).show();

	adapter = new OutLetAdapter(this, lists);
	content_view.setAdapter(adapter);

	pullToRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {
			lists.clear();
			radius = 1000;
			uploadContinueClick();

		    }
		}, 2000);
	    }

	    @Override
	    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		new Handler().postDelayed(new Runnable() {
		    public void run() {
			radius = radius * 2;
			uploadContinueClick();
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

	userID = p_id + "," + p_img + "," + p_name + "," + p_pro + "," + p_sex
		+ "," + p_year;
    }

    /**
     * 开始自动上传
     * 
     * @param v
     */
    private void uploadContinueClick() {

	if (pt == null) {
	    Toast.makeText(OutLetAty.this, "未获取到位置", Toast.LENGTH_LONG).show();
	    return;
	}

	RadarSearchManager.getInstance().startUploadAuto(this, 10000);

	searchNearby();
    }

    private void searchNearby() {
	if (pt == null) {
	    Toast.makeText(OutLetAty.this, "未获取到位置", Toast.LENGTH_LONG).show();
	    return;
	}
	pageIndex = 0;
	searchRequest(pageIndex);
    }

    private void searchRequest(int index) {

	RadarNearbySearchOption option = new RadarNearbySearchOption()
		.centerPt(pt).pageNum(pageIndex).radius(radius);
	RadarSearchManager.getInstance().nearbyInfoRequest(option);

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onGetNearbyInfoList(RadarNearbyResult result,
	    RadarSearchError error) {
	if (error == RadarSearchError.RADAR_NO_ERROR) {
	    Toast.makeText(OutLetAty.this, "查询周边成功", Toast.LENGTH_LONG).show();
	    // 获取成功，处理数据

	    List<RadarNearbyInfo> aList = result.infoList;
	    List<OutLet> dList = new ArrayList<OutLet>();
	    for (int i = 0; i < aList.size(); i++) {
		String b = aList.get(i).userID;

		String[] user = b.split(",");

		list = new OutLet();
		try {

		    String p_id = PublicTools.doWithNullData(user[0]);
		    String p_img = PublicTools.doWithNullData(user[1]);
		    String p_name = PublicTools.doWithNullData(user[2]);
		    String p_pro = PublicTools.doWithNullData(user[3]);
		    String p_sex = PublicTools.doWithNullData(user[4]);
		    String p_year = PublicTools.doWithNullData(user[5]);
		    list.setName(p_name);
		    list.setId(p_id);
		    list.setHead_info(p_img);
		    list.setSex(p_sex);
		    list.setStart_year(p_year);
		    list.setPro_name(p_pro);
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
	    Toast.makeText(OutLetAty.this, "查询周边失败", Toast.LENGTH_LONG).show();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarSearchListener#onGetClearInfoState(com.baidu
     * .mapapi.radar.RadarSearchError)
     */
    @Override
    public void onGetClearInfoState(RadarSearchError arg0) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarSearchListener#onGetUploadState(com.baidu
     * .mapapi.radar.RadarSearchError)
     */
    @Override
    public void onGetUploadState(RadarSearchError arg0) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.baidu.mapapi.radar.RadarUploadInfoCallback#onUploadInfoCallback()
     * 实现上传callback，自动上传
     */
    @Override
    public RadarUploadInfo onUploadInfoCallback() {

	RadarUploadInfo info = new RadarUploadInfo();
	info.comments = "张三";
	info.pt = pt;

	return info;
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
	MyLocationData locData = new MyLocationData.Builder()
		.accuracy(arg0.getRadius())
		// 此处设置开发者获取到的方向信息，顺时针0-360
		.direction(100).latitude(arg0.getLatitude())
		.longitude(arg0.getLongitude()).build();
	if (!isUpload) {
	    uploadContinueClick();
	    isUpload = true;
	}
    }

}
