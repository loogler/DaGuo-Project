/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial.cent;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.util.base.ViewPager_Hacky;
import com.daguo.utils.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    /**
     * initViews
     */
    private ViewPager_Hacky photo_vp;
    private TextView name_tv, cent_tv, submit_tv, detail_tv;

    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CENT_SUC:

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
	Intent intent = getIntent();
	centId = intent.getStringExtra("id");

	initViewws();

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

    /*-----------    ---------------------------------------*/

    private void loadCentData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = "&id=" + centId;
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
			
			
			msg=handler.obtainMessage(MSG_CENT_SUC) ;
			msg.sendToTarget();
		    } else {
			// 并没有这样的积分商品
			Log.e("积分商品信息", "积分详情查询异常");
			msg=handler.obtainMessage(MSG_CENT_FAIL);
			
		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }

}
