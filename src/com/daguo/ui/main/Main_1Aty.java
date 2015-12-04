package com.daguo.ui.main;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.schedule.Main_Aty;
import com.daguo.ui.commercial.Shop_GoodsDetailAty;
import com.daguo.ui.operators.OperatorAty;
import com.daguo.ui.school.School_MainAty;
import com.daguo.ui.school.huodong.SC_HuoDongAty;
import com.daguo.ui.school.huodong.SC_HuoDong_DetailAty;
import com.daguo.ui.school.shetuan.SC_SheTuanAty;
import com.daguo.ui.school.shetuan.SC_SheTuanDetailAty;
import com.daguo.ui.school.xinwen.SC_XinWenAty;
import com.daguo.ui.school.xinwen.SC_XinWen_AwardsAty;
import com.daguo.ui.school.xinwen.SC_XinWen_DetailAty;
import com.daguo.ui.school.zhuanti.SC_ZhuanTiDetailAty;
import com.daguo.ui.user.UserInfo_ModifyAty1;
import com.daguo.util.adapter.Main_BottomBannerAdapter;
import com.daguo.util.base.FrameLayout_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner.TransitionEffect;
import com.daguo.util.beans.AddBanner;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-26 下午3:17:40
 * @function ：app 主页 主界面
 */
public class Main_1Aty extends Activity implements OnClickListener {
    private static String tag = "Main_1Aty";
    // /////////////////////////////////////////////////////////////
    /*
     * 头部广告位
     */

    // ============== 广告切换 ===================
    private ViewPager_3DBanner mViewPager = null;
    private RelativeLayout topBanner_rl;

    /**
     * 装指引的ImageView数组
     */
    private ImageView[] mIndicators;

    /**
     * 装ViewPager中ImageView的数组
     */
    private ImageView[] mImageViews;
    // private List<String> mImageUrls = new ArrayList<String>();
    private LinearLayout mIndicator = null;
    private final int MSG_CHANGE_PHOTO = 1;
    /** 图片自动切换时间 */
    private final int PHOTO_CHANGE_TIME = 5000;

    private List<AddBanner> topBannerLists = new ArrayList<AddBanner>();
    private MyAdapter adapter;
    private final int MSG_TOPBANNERLISTS = 111;
    private final int MSG_MIDBANNERLISTS = 112;
    private final int MSG_BOTTBANNERLISTS = 113;
    // /////////////////////////////////////////////////////////////////////////
    /*
     * 中间通知
     */
    private TextView tv_news;

    /**
     * 中间功能按钮
     */
    private LinearLayout lll;
    private RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6, rl7, rl8;
    private ImageView iv_jiantou;

    /**
     * 三张图片
     */
    private ImageView iv1, iv2, iv3;
    private List<AddBanner> midBnanerLists = new ArrayList<AddBanner>();

    /**
     * 底部活动
     */
    private ListView listView;
    private Main_BottomBannerAdapter bottomBannerAdapter;
    private List<AddBanner> bottBannerLists = new ArrayList<AddBanner>();

    /**
     * dialog 提示用户登录/注册
     */
    private Dialog dia;

    /**
     * tools
     */

    ImageLoader imageLoader;
    Message msg;

    Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {

	    super.handleMessage(msg);
	    switch (msg.what) {
	    case MSG_CHANGE_PHOTO:
		int index = mViewPager.getCurrentItem();
		if (index == topBannerLists.size() - 1) {
		    index = -1;
		}
		mViewPager.setCurrentItem(index + 1);
		handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
			PHOTO_CHANGE_TIME);
		break;

	    case MSG_TOPBANNERLISTS:
		// 顶部广告栏
		setDot();

		setViewpager();

		break;
	    case MSG_MIDBANNERLISTS:
		// 中部广告栏
		imageLoader.displayImage(
			HttpUtil.IMG_URL + midBnanerLists.get(0).getImg_path(),
			iv1);
		imageLoader.displayImage(
			HttpUtil.IMG_URL + midBnanerLists.get(1).getImg_path(),
			iv2);
		imageLoader.displayImage(
			HttpUtil.IMG_URL + midBnanerLists.get(2).getImg_path(),
			iv3);

		iv1.setOnClickListener(Main_1Aty.this);
		iv2.setOnClickListener(Main_1Aty.this);
		iv3.setOnClickListener(Main_1Aty.this);

		break;

	    case MSG_BOTTBANNERLISTS:

		bottomBannerAdapter = new Main_BottomBannerAdapter(
			Main_1Aty.this, bottBannerLists);
		listView.setAdapter(bottomBannerAdapter);
		PublicTools.setListViewHeightBasedOnChildren(listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

		    @Override
		    public void onItemClick(AdapterView<?> arg0, View arg1,
			    int p, long arg3) {
			onclicks(p, bottBannerLists);

		    }

		});

		break;

	    default:
		break;
	    }

	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_main1);
	imageLoader = ImageLoader.getInstance();
	initViews();
	setTopBanner();

    }

    /**
     * 实例化界面
     */
    void initViews() {

	rl1 = (RelativeLayout) findViewById(R.id.rl1);
	rl1.setOnClickListener(this);

	tv_news = (TextView) this.findViewById(R.id.tv_news);
	tv_news.setOnClickListener(this);

	iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
	lll = (LinearLayout) findViewById(R.id.lll);
	rl1 = (RelativeLayout) this.findViewById(R.id.rl1);
	rl2 = (RelativeLayout) this.findViewById(R.id.rl2);
	rl3 = (RelativeLayout) this.findViewById(R.id.rl3);
	rl4 = (RelativeLayout) this.findViewById(R.id.rl4);
	rl5 = (RelativeLayout) this.findViewById(R.id.rl5);
	rl6 = (RelativeLayout) this.findViewById(R.id.rl6);
	rl7 = (RelativeLayout) this.findViewById(R.id.rl7);
	rl8 = (RelativeLayout) this.findViewById(R.id.rl8);
	iv_jiantou.setOnClickListener(this);
	rl1.setOnClickListener(this);
	rl2.setOnClickListener(this);
	rl3.setOnClickListener(this);
	rl4.setOnClickListener(this);
	rl5.setOnClickListener(this);
	rl6.setOnClickListener(this);
	rl7.setOnClickListener(this);
	rl8.setOnClickListener(this);

	iv1 = (ImageView) this.findViewById(R.id.iv_pic1);
	iv2 = (ImageView) this.findViewById(R.id.iv_pic2);
	iv3 = (ImageView) this.findViewById(R.id.iv_pic3);
	setMidBannerDimension();

	listView = (ListView) findViewById(R.id.listView);

    }

    /*********************** 主页广告栏模块 ******************************************************/
    /**
     * 设置中部广告栏位的长宽尺寸
     */
    private void setMidBannerDimension() {
	int width = GetScreenRecUtil.getWindowWidth(Main_1Aty.this) / 3;
	iv3.setLayoutParams(new LinearLayout.LayoutParams(width, 5 * width / 3));
	iv2.setLayoutParams(new LinearLayout.LayoutParams(width, 5 * width / 3));
	iv1.setLayoutParams(new LinearLayout.LayoutParams(width, 5 * width / 3));
    }

    /**
     * 专为顶部轮播banner设定的模块
     */
    private void setTopBanner() {
	// 1 小红点 2 图片 两部分
	mViewPager = (ViewPager_3DBanner) findViewById(R.id.index_product_images_container);
	mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
	topBanner_rl = (RelativeLayout) findViewById(R.id.topBanner_rl);

	// 设置高度 长宽比 1:2
	LayoutParams params = new LayoutParams(
		GetScreenRecUtil.getWindowWidth(Main_1Aty.this),
		GetScreenRecUtil.getWindowWidth(Main_1Aty.this) / 2);
	topBanner_rl.setLayoutParams(params);

	initData();

    }

    /**
     * 设置Viewpager界面
     */
    private void setViewpager() {
	mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
	mViewPager.setCurrentItem(0);
	handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
	adapter = new MyAdapter();
	mViewPager.setAdapter(adapter);
	mViewPager.setOnPageChangeListener(new MyPageChangeListener());
	mViewPager.setOnTouchListener(new OnTouchListener() {

	    @Override
	    public boolean onTouch(View v, MotionEvent event) {
		if (topBannerLists.size() == 0 || topBannerLists.size() == 1)
		    return true;
		else
		    return false;
	    }
	});
    }

    /**
     * 设置圆点
     */
    private void setDot() {
	mIndicators = new ImageView[topBannerLists.size()];
	if (topBannerLists.size() <= 1) {
	    mIndicator.setVisibility(View.GONE);
	}

	for (int i = 0; i < mIndicators.length; i++) {
	    ImageView imageView = new ImageView(this);
	    // LayoutParams params = new LayoutParams(0,
	    // LayoutParams.WRAP_CONTENT, Gravity.CENTER);
	    LayoutParams params2 = new LayoutParams(20, 20);
	    if (i != 0) {
		params2.leftMargin = 5;
		params2.gravity = Gravity.END;
	    }
	    imageView.setLayoutParams(params2);
	    mIndicators[i] = imageView;
	    if (i == 0) {
		mIndicators[i]
			.setBackgroundResource(R.drawable.shape_dot_green);
	    } else {
		mIndicators[i]
			.setBackgroundResource(R.drawable.shape_dot_white);
	    }

	    mIndicator.addView(imageView);
	}

	mImageViews = new ImageView[topBannerLists.size()];

	for (int i = 0; i < mImageViews.length; i++) {
	    ImageView imageView = new ImageView(this);
	    imageView.setScaleType(ScaleType.CENTER_CROP);
	    mImageViews[i] = imageView;
	}
    }

    /**
     * 初始化广告栏的数据 包含三部分 主页轮播广告栏 ，主页中部横排三个广告 底部若干个广告竖排 tips ： 这里还缺少一个最顶部的小广告 通知栏
     */
    private void initData() {
	new Thread(new Runnable() {
	    public void run() {
		// 最多不要超过15张广告 顶部广告栏

		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=1&page=1&rows=15";
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
			    topBannerLists.add(list);
			}
			msg = handler.obtainMessage(MSG_TOPBANNERLISTS);
			msg.sendToTarget();
		    }

		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("主页获取广告信息", "获取广告json异常");
		} catch (Exception e) {
		    Log.e("主页获取广告信息", "获取广告异常");
		    e.printStackTrace();
		}
		// 中部广告栏
		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=2&page=1&rows=3";
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
			    midBnanerLists.add(list);
			}
			msg = handler.obtainMessage(MSG_MIDBANNERLISTS);
			msg.sendToTarget();
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("主页获取广告信息", "获取中部广告json异常");
		} catch (Exception e) {
		    Log.e("主页获取广告信息", "获取中部广告异常");
		    e.printStackTrace();
		}

		// 底部广告栏 最多不要超过15张
		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=3&page=1&rows=15";
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
			    bottBannerLists.add(list);
			}
			msg = handler.obtainMessage(MSG_BOTTBANNERLISTS);
			msg.sendToTarget();
		    }
		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("主页获取广告信息", "获取中部广告json异常");
		} catch (Exception e) {
		    Log.e("主页获取广告信息", "获取中部广告异常");
		    e.printStackTrace();
		}

	    }
	}).start();

    }

    /**
     * 
     */
    private void onclicks(int position, List<AddBanner> lists) {
	String sourceId = null;
	String menuId = null;
	String type = null;
	String url = null;
	sourceId = lists.get(position).getSource_id();
	type = lists.get(position).getType();

	if (type.equals("1")) {// 外连接
	    url = lists.get(position).getUrl();
	    Intent intent = new Intent(Main_1Aty.this, WebView_CommenAty.class);
	    intent.putExtra("URL", url);
	    startActivity(intent);
	} else if (type.equals("2")) {// 跳转栏目
	    menuId = lists.get(position).getMenu_id();
	    if (menuId.equals("b3b7866c-3bf9-48a7-8caa-effa1fb86782")) {
		// 校园活动
		Intent intent = new Intent(Main_1Aty.this, SC_HuoDongAty.class);
		startActivity(intent);

	    } else if (menuId.equals("7176add9-6d46-4c97-8983-362848304388")) {
		// 校园新鲜事
		Intent intent = new Intent(Main_1Aty.this, SC_XinWenAty.class);
		startActivity(intent);
	    } else if (menuId.equals("db94a88d-5c78-448b-a3a7-4af1c3850571")) {
		// 校园新闻
		Intent intent = new Intent(Main_1Aty.this, SC_XinWenAty.class);
		startActivity(intent);

	    } else if (menuId.equals("d6c986c5-8e52-485e-9a6e-d5d98480564e")) {
		// 校园社团
		Intent intent = new Intent(Main_1Aty.this, SC_SheTuanAty.class);
		startActivity(intent);

	    }

	} else if (type.equals("3")) {// 跳转数据
	    menuId = lists.get(position).getMenu_id();
	    sourceId = lists.get(position).getSource_id();
	    if (menuId.equals("b3b7866c-3bf9-48a7-8caa-effa1fb86782")) {
		// 校园活动
		Intent intent = new Intent(Main_1Aty.this,
			SC_HuoDong_DetailAty.class);
		intent.putExtra("id", sourceId);
		startActivity(intent);

	    } else if (menuId.equals("7176add9-6d46-4c97-8983-362848304388")) {
		// 校园新鲜事
		Intent intent = new Intent(Main_1Aty.this,
			SC_XinWen_DetailAty.class);
		intent.putExtra("id", sourceId);
		startActivity(intent);
	    } else if (menuId.equals("db94a88d-5c78-448b-a3a7-4af1c3850571")) {
		// 校园新闻
		Intent intent = new Intent(Main_1Aty.this,
			SC_XinWen_DetailAty.class);
		intent.putExtra("id", sourceId);
		startActivity(intent);

	    } else if (menuId.equals("d6c986c5-8e52-485e-9a6e-d5d98480564e")) {
		// 校园社团
		Intent intent = new Intent(Main_1Aty.this,
			SC_SheTuanDetailAty.class);
		intent.putExtra("id", sourceId);
		startActivity(intent);
	    }
	} else if (type.equals("4")) {// 跳转专题
	    Intent intent = new Intent(Main_1Aty.this,
		    SC_ZhuanTiDetailAty.class);
	    intent.putExtra("id", sourceId);
	    startActivity(intent);
	} else if (type.equals("5")) {// 跳转商品
	    // TODO 跳转商品详情 需要具体商品详情页   通知商城详情页接收商品信息
	    Intent intent = new Intent(Main_1Aty.this,
		    Shop_GoodsDetailAty.class);
	    intent.putExtra("id", sourceId);
	    startActivity(intent);

	}

    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-27 下午4:33:26
     * @function ：跳转适配器
     */
    private class MyAdapter extends PagerAdapter {

	@Override
	public int getCount() {
	    return mImageViews.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
	    if (view instanceof FrameLayout_3DBanner) {
		return ((FrameLayout_3DBanner) view).getChildAt(0) == obj;
	    } else {
		return view == obj;
	    }
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
	    ((ViewPager) container).removeView(mViewPager
		    .findViewFromObject(position));
	}

	@Override
	public Object instantiateItem(View container, final int position) {

	    imageLoader.displayImage(
		    HttpUtil.IMG_URL
			    + topBannerLists.get(position).getImg_path(),
		    mImageViews[position]);
	    ((ViewPager) container).addView(mImageViews[position], 0);
	    mViewPager.setObjectForPosition(mImageViews[position], position);
	    mImageViews[position]
		    .setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
			    Toast.makeText(Main_1Aty.this,
				    "dianji +" + position, Toast.LENGTH_SHORT)
				    .show();
			    onclicks(position, topBannerLists);
			}
		    });

	    return mImageViews[position];
	}

    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-27 下午4:32:54
     * @function ： This method will be invoked when a new page becomes selected.
     *           position: Position index of the new selected page.
     */
    private class MyPageChangeListener implements OnPageChangeListener {

	public void onPageSelected(int position) {
	    setImageBackground(position);
	}

	public void onPageScrollStateChanged(int arg0) {

	}

	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}
    }

    /**
     * 设置选中的tip的背景
     * 
     * @param selectItemsIndex
     */
    private void setImageBackground(int selectItemsIndex) {
	for (int i = 0; i < mIndicators.length; i++) {
	    if (i == selectItemsIndex) {
		mIndicators[i]
			.setBackgroundResource(R.drawable.shape_dot_green);
	    } else {
		mIndicators[i]
			.setBackgroundResource(R.drawable.shape_dot_white);
	    }
	}
    }

    /********************************* split line onclick事件 ****************************************/

    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.lin1:
	    Intent intents = new Intent(Main_1Aty.this,
		    UserInfo_ModifyAty1.class);
	    startActivity(intents);

	    break;
	case R.id.iv_mail:

	    break;
	case R.id.rl11:
	    Toast.makeText(Main_1Aty.this, "功能咱未开放 ", Toast.LENGTH_SHORT)
		    .show();

	    break;
	case R.id.tv_news:
	    Toast.makeText(getBaseContext(), "功能咱未开放 ", Toast.LENGTH_SHORT)
		    .show();
	    break;
	case R.id.rl1:
	    Intent intent2 = new Intent(Main_1Aty.this, School_MainAty.class);
	    startActivity(intent2);
	    // finish(); 不能在这里结束activity，保持让所有进程被结束时 还能回到这里。

	    break;
	case R.id.rl2:

	    Intent intent8 = new Intent(Main_1Aty.this, SC_XinWenAty.class);
	    startActivity(intent8);
	    break;
	case R.id.rl3:

	    // Toast.makeText(getBaseContext(), "跳转至移动生活", Toast.LENGTH_SHORT)
	    // .show();
	    Intent intent3 = new Intent(Main_1Aty.this, OperatorAty.class);
	    startActivity(intent3);
	    break;

	case R.id.rl4:
	    Intent intent = new Intent(Main_1Aty.this, Main_Aty.class);
	    startActivity(intent);
	    break;
	case R.id.rl5:
	    Toast.makeText(getBaseContext(), "往右翻页就可以啦", Toast.LENGTH_SHORT)
		    .show();
	    break;
	case R.id.rl6:
	    Toast.makeText(getBaseContext(), "功能咱未开放 ", Toast.LENGTH_SHORT)
		    .show();
	    break;
	case R.id.rl7:
	    Intent intent7 = new Intent(Main_1Aty.this, School_MainAty.class);
	    startActivity(intent7);
	    break;

	case R.id.rl8:
	    // Toast.makeText(getBaseContext(), "功能咱未开放 ", Toast.LENGTH_SHORT)
	    // .show();
	    Intent intent10 = new Intent(Main_1Aty.this, SC_SheTuanAty.class);
	    startActivity(intent10);

	    break;
	case R.id.iv_pic1:

	    onclicks(0, midBnanerLists);

	    // Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能1",
	    // Toast.LENGTH_SHORT).show();
	    break;
	case R.id.iv_pic2:
	    onclicks(1, midBnanerLists);
	    // Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能2",
	    // Toast.LENGTH_SHORT).show();
	    break;
	case R.id.iv_pic3:
	    onclicks(2, midBnanerLists);
	    // Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能3",
	    // Toast.LENGTH_SHORT).show();
	    // Intent intent4 = new Intent(Main_1Aty.this, BroadBandAty.class);
	    // startActivity(intent4);
	    break;
	case R.id.iv_buttom2:
	    // Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能1++",
	    // Toast.LENGTH_SHORT).show();

	    break;
	case R.id.iv_buttom1:
	    // Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能2++",
	    // Toast.LENGTH_SHORT).show();
	    Intent intent5 = new Intent(Main_1Aty.this,
		    SC_XinWen_AwardsAty.class);
	    startActivity(intent5);
	    break;
	case R.id.iv_jiantou:
	    if (lll.getVisibility() == View.GONE) {
		lll.setVisibility(View.VISIBLE);
		iv_jiantou
			.setImageResource(R.drawable.tabhome1_function_shouqi);
	    } else if (lll.getVisibility() == View.VISIBLE) {
		lll.setVisibility(View.GONE);
		iv_jiantou
			.setImageResource(R.drawable.tabhome1_function_zhankai);
	    }

	    break;
	default:
	    break;
	}
    }

}
