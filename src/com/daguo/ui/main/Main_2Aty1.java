/**
 * 互相学习 共同进步
 */
package com.daguo.ui.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import android.provider.ContactsContract.Contacts.Data;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.staggeredgridview.StaggeredGridView;
import com.daguo.libs.staggeredgridview.StaggeredGridView.OnLoadmoreListener;
import com.daguo.ui.commercial.Shop_SearchAty;
import com.daguo.util.adapter.Main_2Adapter;
import com.daguo.util.adapter.Main_2_GoodTypeAdapter;
import com.daguo.util.base.CustomDigitalClock;
import com.daguo.util.base.CustomDigitalClock.ClockListener;
import com.daguo.util.base.FrameLayout_3DBanner;
import com.daguo.util.base.GoodTypeItem;
import com.daguo.util.base.ViewPager_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner.TransitionEffect;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-8 下午4:08:37
 * @function ：大果主页-2 商城页
 */
@SuppressLint({ "ClickableViewAccessibility", "SimpleDateFormat" })
public class Main_2Aty1 extends Activity implements OnFocusChangeListener,
	com.daguo.libs.staggeredgridview.StaggeredGridView.OnItemClickListener,
	OnLoadmoreListener, android.widget.AdapterView.OnItemClickListener {
    private final int MSG_GOOD_ITEM = 100;
    private final int MSG_TOPBANNERLISTS = 101;
    private final int MSG_GOOD_TYPE = 109;
    private final int MSG_MIAOSHA_DATA_SUC = 199;
    private final int MSG_MIAOSHA_DATA_FAIL = 299;

    /**
     * @see initViews
     */
    private EditText search_edt;
    private StaggeredGridView staggeredGridView;

    // top Views
    View topView = null;

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
    // 图片自动切换时间
    private final int PHOTO_CHANGE_TIME = 5000;

    // 轮播广告
    private List<AddBanner> topBannerLists = new ArrayList<AddBanner>();
    private MyAdapter BannerAdapter;

    // 商品分类
    private GridView goodstype_grid;
    private List<GoodTypeItem> goodTypeLists = new ArrayList<GoodTypeItem>();
    private Main_2_GoodTypeAdapter typeAdapter;

    // 秒杀 商品
    private Shop_GoodsItem miaoshaGoodsItem = new Shop_GoodsItem();
    private TextView miaoshaName_tv, miaoshaPrice_tv, miaoshaInfo_tv;
    private ImageView miaosha_iv;
    private LinearLayout miaosha_ll;
    private CustomDigitalClock digitalClock;
    //
    private boolean isMiaoSHaTime = false;

    /**
     * @see Data
     * 
     */

    // 优惠商品
    private List<Shop_GoodsItem> goodsItemLists = new ArrayList<Shop_GoodsItem>();
    private Shop_GoodsItem goodsItemList = null;
    Main_2Adapter adapter = null;

    // tools
    ImageLoader imageLoader;
    private int pageIndex = 1;
    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg) {
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
	    case MSG_GOOD_ITEM:

		if (msg.obj == null) {
		    return;
		}
		List<Shop_GoodsItem> aaa = (List<Shop_GoodsItem>) msg.obj;
		if (aaa.size() <= 1) {
		    Toast.makeText(Main_2Aty1.this, "没有更多数据了",
			    Toast.LENGTH_SHORT).show();
		}
		goodsItemLists.addAll(aaa);
		adapter.notifyDataSetChanged();

		break;
	    case MSG_TOPBANNERLISTS:
		// 顶部广告栏
		setDot();

		setViewpager();
		break;

	    case MSG_GOOD_TYPE:
		typeAdapter.notifyDataSetChanged();

		break;
	    case MSG_MIAOSHA_DATA_SUC:
		miaosha_ll.setVisibility(View.VISIBLE);
		FinalBitmap.create(Main_2Aty1.this).display(miaosha_iv,
			HttpUtil.IMG_URL + miaoshaGoodsItem.getThumb_path());
		miaoshaName_tv.setText(miaoshaGoodsItem.getName());
		miaoshaPrice_tv.setText("￥" + miaoshaGoodsItem.getPrice());
		setRemainTime();

		break;

	    case MSG_MIAOSHA_DATA_FAIL:
		miaosha_ll.setVisibility(View.GONE);

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
	setContentView(R.layout.aty_main2_1);
	imageLoader = ImageLoader.getInstance();
	initViews();
	loadAddData();
	loadGoodItems();
	loadTypeData();
	loadMiaoShaData();

	adapter = new Main_2Adapter(this, goodsItemLists);
	staggeredGridView.setAdapter(adapter);

	typeAdapter = new Main_2_GoodTypeAdapter(this, goodTypeLists);
	goodstype_grid.setAdapter(typeAdapter);

    }

    /**
     * 
     */
    private void initViews() {
	search_edt = (EditText) findViewById(R.id.search_edt);
	staggeredGridView = (StaggeredGridView) findViewById(R.id.staggeredGridView);

	search_edt.setOnFocusChangeListener(this);
	staggeredGridView.setOnItemClickListener(this);
	staggeredGridView.setOnLoadmoreListener(this);

	initTopViews();

    }

    /**
     * 商城顶部增加的模块
     */
    @SuppressLint("InflateParams")
    private void initTopViews() {
	topView = LayoutInflater.from(this).inflate(
		R.layout.item_main2_1_headview, null);

	// 3d 广告
	mViewPager = (ViewPager_3DBanner) topView
		.findViewById(R.id.index_product_images_container);
	mIndicator = (LinearLayout) topView
		.findViewById(R.id.index_product_images_indicator);
	topBanner_rl = (RelativeLayout) topView.findViewById(R.id.topBanner_rl);

	// 设置高度 长宽比 1:2
	LayoutParams params = new LayoutParams(
		GetScreenRecUtil.getWindowWidth(Main_2Aty1.this),
		GetScreenRecUtil.getWindowWidth(Main_2Aty1.this) / 2);
	topBanner_rl.setLayoutParams(params);

	// /////
	goodstype_grid = (GridView) topView.findViewById(R.id.goodstype_grid);

	goodstype_grid.setOnItemClickListener(this);
	// //

	miaosha_iv = (ImageView) topView.findViewById(R.id.miaosha_iv);
	miaoshaName_tv = (TextView) topView.findViewById(R.id.miaoshaName_tv);
	miaoshaPrice_tv = (TextView) topView.findViewById(R.id.miaoshaPrice_tv);
	miaoshaInfo_tv = (TextView) topView.findViewById(R.id.miaoshaInfo_tv);
	digitalClock=(CustomDigitalClock) topView.findViewById(R.id.remainTime);

	miaosha_ll = (LinearLayout) topView.findViewById(R.id.miaosha_ll);
	miaosha_ll.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		if (isMiaoSHaTime) {

		    Intent intent = new Intent();
		    // TODO 跳转商品详情
		} else {
		    Toast.makeText(Main_2Aty1.this, "不在秒杀时间内",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	//

	staggeredGridView.setHeaderView(topView);

    }

    /************************ set Data *****************************************************************/

    /**
     * 设置圆点
     */
    @SuppressLint("InlinedApi")
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
     * 设置Viewpager界面
     */
    @SuppressLint("ClickableViewAccessibility")
    private void setViewpager() {
	mViewPager.setTransitionEffect(TransitionEffect.CubeOut);
	mViewPager.setCurrentItem(0);
	handler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, PHOTO_CHANGE_TIME);
	BannerAdapter = new MyAdapter();
	mViewPager.setAdapter(BannerAdapter);
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

    /************************ get Data *****************************************************************/
    /**
     * 加载推荐商品列表
     */
    private void loadGoodItems() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    // &type_id=a6ad60a7-a587-4216-b83d-54094b05af5b
		    String url = HttpUtil.QUERY_GOODSLIST + "&type_id=a6ad60a7-a587-4216-b83d-54094b05af5b&rows=20&page="
			    + pageIndex;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");
			List<Shop_GoodsItem> abc = new ArrayList<Shop_GoodsItem>();
			for (int i = 0; i < array.length(); i++) {
			    goodsItemList = new Shop_GoodsItem();

			    String thumb_path = array.optJSONObject(i)
				    .getString("thumb_path");
			    String name = array.optJSONObject(i).getString(
				    "name");
			    String id = array.optJSONObject(i).getString("id");
			    String price = array.optJSONObject(i).getString(
				    "price");

			    goodsItemList.setId(id);
			    goodsItemList.setThumb_path(thumb_path);
			    goodsItemList.setName(name);
			    goodsItemList.setPrice(price);

			    abc.add(goodsItemList);

			}
			msg = handler.obtainMessage(MSG_GOOD_ITEM);
			msg.obj = abc;
			msg.sendToTarget();

		    } else {
			// 不存在的商品类别

		    }
		} catch (Exception e) {
		}
	    }
	}).start();

    }

    /**
     * 加载广告栏
     */
    private void loadAddData() {
	new Thread(new Runnable() {
	    public void run() {

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
	    }
	}).start();

    }

    /**
     * 加载商品分类
     */
    private void loadTypeData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_GOOD_TYPE + "&page=1&rows=9";
		    ;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") != 9) {
			// 栏目不等于9个 会出现异常布局
			Log.e("商品类型", "商品分类查询数量不为9个，导致布局异常");
		    } else if (jsonObject.getInt("total") > 0) {
			// 栏目有数据
			GoodTypeItem goodTypeItem;
			JSONArray array = jsonObject.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String type_name = array.optJSONObject(i)
				    .getString("type_name");
			    String type_id = array.optJSONObject(i).getString(
				    "type_id");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");

			    goodTypeItem = new GoodTypeItem();
			    goodTypeItem.setImg_path(img_path);
			    goodTypeItem.setType_id(type_id);
			    goodTypeItem.setType_name(type_name);

			    goodTypeLists.add(goodTypeItem);

			}
			goodTypeItem = new GoodTypeItem();
			goodTypeItem.setImg_path("1");
			goodTypeItem.setType_id("");
			goodTypeItem.setType_name("更多分类");

			goodTypeLists.add(goodTypeItem);

			msg = handler.obtainMessage(MSG_GOOD_TYPE);
			msg.sendToTarget();

		    } else {
			Log.e("商品类型", "商品分类查询时返回数据为0或者其他，请检查上传分类表");

		    }

		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("主页获取分类信息", "获取分类json异常");
		} catch (Exception e) {
		    Log.e("主页获取分类信息", "获取分类异常");
		    e.printStackTrace();
		}
	    }
	}).start();
    }

    private void loadMiaoShaData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_GOODSLIST
			    + "&page=1&rows=1&type_id=567603cf-a5dd-4cc8-afd3-4f7c961fbf9c";
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");
			String end_time = array.optJSONObject(0).getString(
				"end_time");
			String id = array.optJSONObject(0).getString("id");
			String name = array.optJSONObject(0).getString("name");
			String price = array.optJSONObject(0)
				.getString("price");
			String start_time = array.optJSONObject(0).getString(
				"start_time");
			String thumb_path = array.optJSONObject(0).getString(
				"thumb_path");

			miaoshaGoodsItem.setEnd_time(end_time);
			miaoshaGoodsItem.setId(id);
			miaoshaGoodsItem.setName(name);
			miaoshaGoodsItem.setPrice(price);
			miaoshaGoodsItem.setStart_time(start_time);
			miaoshaGoodsItem.setThumb_path(thumb_path);

			msg = handler.obtainMessage(MSG_MIAOSHA_DATA_SUC);
			msg.sendToTarget();

		    } else {
			// 没有秒杀商品
			msg = handler.obtainMessage(MSG_MIAOSHA_DATA_FAIL);
		    }

		} catch (JSONException e) {

		} catch (Exception e) {
		}
	    }
	}).start();
    }

    /********************** implement ****************************************************************************/

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.view.View.OnFocusChangeListener#onFocusChange(android.view.View,
     * boolean)
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
	if (hasFocus) {
	    Intent intent = new Intent(Main_2Aty1.this, Shop_SearchAty.class);
	    startActivity(intent);

	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.staggeredgridview.StaggeredGridView.OnItemClickListener
     * #onItemClick(com.daguo.libs.staggeredgridview.StaggeredGridView,
     * android.view.View, int, long)
     */
    @Override
    public void onItemClick(StaggeredGridView parent, View view, int position,
	    long id) {

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.staggeredgridview.StaggeredGridView.OnLoadmoreListener
     * #onLoadmore()
     */
    @Override
    public void onLoadmore() {

	new Handler().postDelayed(new Runnable() {
	    public void run() {
		pageIndex++;
		loadGoodItems();
	    }
	}, 2000);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int p, long arg3) {
	Intent intent = new Intent(Main_2Aty1.this, Shop_SearchAty.class);
	intent.putExtra("type_id", goodTypeLists.get(p).getType_id());
	startActivity(intent);

    }

    /**************************** tools ************************************/

    // public class DateFormate {
    // public long formatDate(String dateStr){
    // java.sql.Date d=null;
    // try{
    // SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // d=(java.sql.Date) Date(sf.parse(dateStr));
    // }
    // catch(Exception e){
    //
    // }
    // return d.getTime();
    // }
    //
    // private Date Date(java.util.Date date) {
    // Date dateTime;
    // dateTime =new java.sql.Date(date.getTime());
    // return dateTime;
    // }

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private void setRemainTime() {
	long currentTime = System.currentTimeMillis();
	long miaoshaTime_start = 0;
	long miaoshaTime_end = 0;

	try {
	    Date dateStart = sdf.parse(miaoshaGoodsItem.getStart_time()
		    .toString());
	    Date dateEnd = sdf.parse(miaoshaGoodsItem.getEnd_time().toString());
	    miaoshaTime_start = dateStart.getTime();
	    miaoshaTime_end = dateEnd.getTime();

	} catch (ParseException e) {
	    e.printStackTrace();
	}

	if (currentTime > miaoshaTime_end) {
	    // 当前时间比结束时间大 说明已结束
	    isMiaoSHaTime = false;
	    digitalClock.setEndTime(0);
	    miaoshaInfo_tv.setText("已结束");

	} else if (currentTime < miaoshaTime_start) {
	    // 当前时间比开始时间小 说明未开始
	    isMiaoSHaTime = false;
	    digitalClock.setEndTime(miaoshaTime_start);
	    miaoshaInfo_tv.setText("秒杀即将开始");
	    digitalClock.setClockListener(new ClockListener() {

		@Override
		public void timeEnd() {
		    isMiaoSHaTime = true;
		    Toast.makeText(Main_2Aty1.this, "秒杀尚未开始",
			    Toast.LENGTH_SHORT).show();
		    miaoshaInfo_tv.setText("限时秒杀");

		}

		@Override
		public void remainFiveMinutes() {

		}
	    });

	} else if (miaoshaTime_end > currentTime
		&& currentTime > miaoshaTime_start) {
	    // 刚好在秒杀范围时间内
	    isMiaoSHaTime = true;

	    // String t = sdf.format(new Date(miaoshaTime_end - currentTime));
	    //
	    // digitalClock.setText(t);
	    digitalClock.setEndTime(miaoshaTime_end);
	    digitalClock.setClockListener(new ClockListener() {

		@Override
		public void timeEnd() {
		    isMiaoSHaTime = false;
		    Toast.makeText(Main_2Aty1.this, "本次秒杀结束",
			    Toast.LENGTH_SHORT).show();
		    miaoshaInfo_tv.setText("已结束");
		    digitalClock.setEndTime(0);
		}

		@Override
		public void remainFiveMinutes() {

		}
	    });
	} else {
	    // 时间算错了 或是异常
	    isMiaoSHaTime = false;
	    miaosha_ll.setVisibility(View.GONE);
	    Log.e("商品秒杀", "秒杀时间计算出错啦");
	}

    }

    // ////////////////////////////
    private void onclicks() {

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
			    // Toast.makeText(Main_1Aty.this,
			    // "dianji +" + position, Toast.LENGTH_SHORT)
			    // .show();
			    // onclicks(position, topBannerLists);
			    // TODO
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

}
