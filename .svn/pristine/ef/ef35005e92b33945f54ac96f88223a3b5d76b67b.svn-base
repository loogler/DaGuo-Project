/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial.cent;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-4 上午11:25:36
 * @function ：积分兑换主界面
 */
public class Cent_CartAty extends Activity {

    private final int MSG_GET_CENT_SUC = 10001;
    private final int MSG_GET_CENT_FAIL = 10002;
    private final int MSG_CLEAR_SUC = 10003;

    //
    private String centName, centScore, centPic, centId;
    private String name, tel, logist;
    private String p_id;
    private String score;
    private int finalScore;

    /**
     * @see initViews
     */
    private TextView centName_tv, centScore_tv, submit_tv;
    private ImageView centPic_iv;
    private EditText name_edt, tel_edt, logist_edt;

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_GET_CENT_SUC:
		clear();
		break;
	    case MSG_GET_CENT_FAIL:
		Toast.makeText(Cent_CartAty.this, "无法获取个人积分！",
			Toast.LENGTH_LONG).show();
		break;

	    case MSG_CLEAR_SUC:
		Toast.makeText(Cent_CartAty.this, "恭喜！兑换成功！", Toast.LENGTH_LONG)
			.show();
		MyAppliation.getInstance().exit();
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
	setContentView(R.layout.aty_cent_cart);
	MyAppliation.getInstance().addActivity(this);
	p_id = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE)
		.getString("id", "");
	getIntents();
	initViews();

    }

    private void initViews() {
	initHeadView();
	centName_tv = (TextView) findViewById(R.id.centName_tv);
	centScore_tv = (TextView) findViewById(R.id.centScore_tv);
	submit_tv = (TextView) findViewById(R.id.submit_tv);

	centPic_iv = (ImageView) findViewById(R.id.centPic_iv);

	name_edt = (EditText) findViewById(R.id.name_edt);
	tel_edt = (EditText) findViewById(R.id.tel_edt);
	logist_edt = (EditText) findViewById(R.id.logist_edt);

	centName_tv.setText(centName);
	centScore_tv.setText(centScore);
	FinalBitmap.create(this)
		.display(centPic_iv, HttpUtil.IMG_URL + centPic);

	submit_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		new AlertDialog.Builder(Cent_CartAty.this)
			.setTitle("确定支付吗")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface arg0,
					    int arg1) {
					submit();
				    }
				}).setNegativeButton("我再考虑一下", null).create()
			.show();
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
	title_tv.setText("商品兑换");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 获得传递值
     */
    private void getIntents() {
	Intent intent = getIntent();

	centName = PublicTools.doWithNullData(intent.getStringExtra("name"));
	centId = PublicTools.doWithNullData(intent.getStringExtra("id"));
	centPic = PublicTools.doWithNullData(intent.getStringExtra("pic"));
	centScore = PublicTools.doWithNullData(intent.getStringExtra("score"));

    }

    /** -------------------------------------------------- */

    private void submit() {
	checkInfo();
	send();

    }

    /**
     * 
     */
    private void checkInfo() {
	name = name_edt.getText().toString().trim();
	tel = tel_edt.getText().toString().trim();
	logist = logist_edt.getText().toString().trim();
	if ("".equals(PublicTools.doWithNullData(name))) {
	    Toast.makeText(Cent_CartAty.this, "收货人没有填写", Toast.LENGTH_LONG)
		    .show();
	    return;

	}
	if ("".equals(PublicTools.doWithNullData(tel))) {
	    Toast.makeText(Cent_CartAty.this, "联系方式没有填写", Toast.LENGTH_LONG)
		    .show();
	    return;

	}
	if ("".equals(PublicTools.doWithNullData(logist))) {
	    Toast.makeText(Cent_CartAty.this, "收货地址没有填写", Toast.LENGTH_LONG)
		    .show();
	    return;

	}

    }

    private void send() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_USERINFO + "&page=1&rows=1&id=" + p_id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") > 0) {

			JSONArray array = jsonObject.getJSONArray("rows");

			score = array.optJSONObject(0).getString("score");
			try {
			    int c = Integer.parseInt(centScore);
			    int s = Integer.parseInt(score);
			    if (s >= c) {
				finalScore = s - c;
				msg = handler.obtainMessage(MSG_GET_CENT_SUC);
				msg.sendToTarget();

			    } else {
				runOnUiThread(new Runnable() {
				    public void run() {
					Toast.makeText(Cent_CartAty.this,
						"您的积分不足，无法兑换！当前积分 " + score,
						Toast.LENGTH_LONG).show();

				    }
				});
				return;
			    }

			} catch (Exception e) {
			    Log.e("积分信息获取异常，，商品积分兑换", "积分商品");
			    runOnUiThread(new Runnable() {
				public void run() {
				    Toast.makeText(Cent_CartAty.this,
					    "您的积分信息存在异常，请联系管理员",
					    Toast.LENGTH_LONG).show();
				}
			    });
			    return;
			}

		    } else {
			Log.e("获取个人积分出错", "个人信息异常");
			msg = handler.obtainMessage(MSG_GET_CENT_FAIL);
			msg.sendToTarget();
		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }

    private void clear() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.SUBMIT_CENTGOODS;
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("good_id", centId);
		    map.put("name", name);
		    map.put("tel", tel);
		    map.put("address", logist);
		    map.put("p_id", p_id);
		    String res = HttpUtil.postRequest(url, map);

		    JSONObject jsonObject = new JSONObject(res);
		    if ("1".equals(jsonObject.getString("result"))) {
			// 如果这里兑换信息生成了 ，将立即产生一个新的请求去减少积分，这是一个瞬间完成的操作，并不存在刷分

			String ur = HttpUtil.SUBMIT_USERINFO + "&id=" + p_id
				+ "&score=" + String.valueOf(finalScore);

			String re = HttpUtil.getRequest(ur);
			JSONObject js = new JSONObject(re);
			if (js.getInt("result") == 1) {
			    // 成功 整个流程就结束了
			    msg = handler.obtainMessage(MSG_CLEAR_SUC);
			    msg.sendToTarget();

			} else {
			    // 提交失败
			    Log.e("积分兑换商品出错", "兑换了商品，积分处理异常了 ！！！！！");
			}

		    } else {
			// 提交失败
		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }
}
