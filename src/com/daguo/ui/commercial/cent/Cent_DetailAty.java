/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial.cent;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.Shop_GoodsDetail_BannerAdapter;
import com.daguo.util.base.ViewPager_Hacky;
import com.daguo.util.beans.CentGoods;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-3 下午4:16:27
 * @function ：积分商品详情。
 */
public class Cent_DetailAty extends Activity {

    private final int MSG_CENT_SUC = 10001;
    private final int MSG_CENT_FAIL = 10002;

    /**
     * 
     */
    private String centId;
    private int k;

    /**
     * initViews
     */
    private ViewPager_Hacky photo_vp;
    private TextView name_tv, cent_tv, submit_tv, detail_tv;

    /**
     * data
     */
    private CentGoods centGoods = new CentGoods();
    private Shop_GoodsDetail_BannerAdapter adapter;
    // 图片
    private String[] picLists;
    ImageView[] picViews;

    FinalBitmap finalBitmap;
    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CENT_SUC:
		setContentData();
		break;
	    case MSG_CENT_FAIL:
		Toast.makeText(Cent_DetailAty.this, "积分获取异常，请重试",
			Toast.LENGTH_SHORT).show();
		finish();
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
	setContentView(R.layout.aty_cent_detail);
	MyAppliation.getInstance().addActivity(this);
	Intent intent = getIntent();
	centId = intent.getStringExtra("id");
	finalBitmap = FinalBitmap.create(getApplicationContext());

	initViewws();
	loadCentData();

    }

    /*-------------------   ----------------------*/

    /**
     * 
     */
    private void initViewws() {
	initHeadView();
	photo_vp = (ViewPager_Hacky) findViewById(R.id.photo_vp);
	name_tv = (TextView) findViewById(R.id.name_tv);
	cent_tv = (TextView) findViewById(R.id.cent_tv);
	submit_tv = (TextView) findViewById(R.id.submit_tv);
	detail_tv = (TextView) findViewById(R.id.detail_tv);

	submit_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		Intent intent = new Intent(Cent_DetailAty.this,
			Cent_CartAty.class);
		intent.putExtra("id", centId);
		intent.putExtra("pic", centGoods.getThumb_path());
		intent.putExtra("name", centGoods.getName());
		intent.putExtra("score", centGoods.getScore());

		startActivity(intent);
	    }
	});
    }

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
	title_tv.setText("宝贝详情");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    private void setContentData() {
	picViews = new ImageView[picLists.length];

	for (k = 0; k < picViews.length; k++) {
	    ImageView imageView = new ImageView(Cent_DetailAty.this);
	    imageView.setScaleType(ScaleType.FIT_CENTER);

	    finalBitmap.display(imageView, HttpUtil.IMG_URL + picLists[k]);

	    imageView.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View arg0) {
		    imageBrower(k, picLists);
		}
	    });

	    picViews[k] = imageView;

	}
	name_tv.setText(PublicTools.doWithNullData(centGoods.getName()));
	cent_tv.setText(PublicTools.doWithNullData(centGoods.getScore()));
	detail_tv.setText(PublicTools.doWithNullData(centGoods.getGood_desc()));
	adapter = new Shop_GoodsDetail_BannerAdapter(Cent_DetailAty.this,
		picViews);

	photo_vp.setOnPageChangeListener(new OnPageChangeListener() {
	    @Override
	    public void onPageSelected(int arg0) {

	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }
	});
	photo_vp.setAdapter(adapter);
	adapter.notifyDataSetChanged();
    }

    /*-----------    ---------------------------------------*/

    private void loadCentData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_CENTGOODS
			    + "&page=1&rows=1&id=" + centId;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");

			String name = array.optJSONObject(0).getString("name");
			String thumb_path = array.optJSONObject(0).getString(
				"thumb_path");
			String img_path = array.optJSONObject(0).getString(
				"img_path");
			String score = array.optJSONObject(0)
				.getString("score");
			String good_desc = array.optJSONObject(0).getString(
				"good_desc");

			picLists = PublicTools.doWithNullData(img_path).split(
				",");

			centGoods.setGood_desc(good_desc);
			centGoods.setImg_path(img_path);
			centGoods.setName(name);
			centGoods.setScore(score);
			centGoods.setThumb_path(thumb_path);

			msg = handler.obtainMessage(MSG_CENT_SUC);
			msg.sendToTarget();
		    } else {
			// 并没有这样的积分商品
			Log.e("积分商品信息", "积分详情查询异常");
			msg = handler.obtainMessage(MSG_CENT_FAIL);
			msg.sendToTarget();

		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }

    /*---------------------------------------*/
    private void imageBrower(int position, String[] urls) {
	Intent intent = new Intent(Cent_DetailAty.this,
		com.daguo.modem.photo.ImagePagerActivity.class);
	// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
		position);
	startActivity(intent);
    }

}
