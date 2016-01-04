package com.daguo.ui.operators;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.daguo.R;
import com.daguo.util.base.FrameLayout_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner;
import com.daguo.util.base.ViewPager_3DBanner.TransitionEffect;
import com.daguo.util.beans.AddBanner;
import com.daguo.utils.GetScreenRecUtil;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint({ "ClickableViewAccessibility", "InlinedApi" })
public class OperatorAty extends Activity implements OnClickListener {

    private final int MSG_ADD_SUC = 10001;

    /**
     * initViews
     */

    private ImageView tel_iv, net_iv, gprs_iv;

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

    private MyAdapter adapter;

    /**
     * data
     */
    private List<AddBanner> topBannerLists = new ArrayList<AddBanner>();

    /**
     * tools
     */
    ImageLoader imageLoader = ImageLoader.getInstance();
    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {

	    switch (msg.what) {
	    case MSG_ADD_SUC:
		setDot();

		setViewpager();
		break;

	    default:
		break;
	    }

	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_operator);
	initHeadView();
	initViews();
	setTopBanner();
    }

    /**
     * 通用的headview 不同位置会出现不同的页面要求，根据情况设置
     */
    private void initHeadView() {
	TextView back_tView = (TextView) findViewById(R.id.back_tv);
	TextView title_tv = (TextView) findViewById(R.id.title_tv);
	TextView function_tv = (TextView) findViewById(R.id.function_tv);
	ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

	back_tView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		finish();
	    }
	});
	title_tv.setText("移动业务办理");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 
     */
    private void initViews() {
	tel_iv = (ImageView) findViewById(R.id.tel_iv);
	net_iv = (ImageView) findViewById(R.id.net_iv);
	gprs_iv = (ImageView) findViewById(R.id.gprs_iv);

	tel_iv.setOnClickListener(this);
	net_iv.setOnClickListener(this);
	gprs_iv.setOnClickListener(this);
    }

    private void setTopBanner() {
	// 1 小红点 2 图片 两部分
	mViewPager = (ViewPager_3DBanner) findViewById(R.id.index_product_images_container);
	mIndicator = (LinearLayout) findViewById(R.id.index_product_images_indicator);
	topBanner_rl = (RelativeLayout) findViewById(R.id.topBanner_rl);

	// 设置高度 长宽比 1:2
	LayoutParams params = new LayoutParams(
		GetScreenRecUtil.getWindowWidth(OperatorAty.this),
		GetScreenRecUtil.getWindowWidth(OperatorAty.this) / 2);
	topBanner_rl.setLayoutParams(params);

	loadAddData();

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

    /************************ implement ******************************************/
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	Intent intent;
	switch (v.getId()) {
	case R.id.tel_iv:
	    intent = new Intent(OperatorAty.this, Oper_MobileAty.class);
	    startActivity(intent);
	    break;
	case R.id.net_iv:
	    intent = new Intent(OperatorAty.this, Oper_BroadBandAty.class);
	    startActivity(intent);

	    break;
	case R.id.gprs_iv:
	    // TODO 功能缺失

	    break;

	default:
	    break;
	}
    }

    /*********************** data -thread **************************************/
    private void onclicks(int position, List<AddBanner> listsAddBanners) {
	// TODO

    }

    private void loadAddData() {
	new Thread(new Runnable() {
	    public void run() {

		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=1&page=1&rows=15";
		    // TODO position= 14

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
			msg = handler.obtainMessage(MSG_ADD_SUC);
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
			    onclicks(position, topBannerLists);
			}
		    });

	    return mImageViews[position];
	}

    }

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
