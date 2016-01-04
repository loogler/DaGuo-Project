package com.daguo.modem.choujiang;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-28 下午3:31:25
 * @function ：这是抽奖界面，主界面
 */
@SuppressLint("HandlerLeak")
public class ChouJiangConsoleAty extends Activity {
    int cent, newCent;
    private String orderId;
    private String p_id;

    Button ok;

    CustomProgressDialog customProgressDialog;
    Message msg;
    Handler handler = new Handler() {

	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case 10001:

		Toast.makeText(ChouJiangConsoleAty.this, "恭喜  购买成功！",
			Toast.LENGTH_LONG).show();
		customProgressDialog.dismiss();

		break;
	    case 10002:
		customProgressDialog.dismiss();
		new AlertDialog.Builder(ChouJiangConsoleAty.this)
			.setMessage("订单异常，请联系管理员！")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface arg0,
					    int arg1) {
					MyAppliation.getInstance().exit();
				    }
				}).create().show();

		break;

	    default:
		break;
	    }
	};
    };

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.item_choujiangconsole);
	MyAppliation.getInstance().addActivity(this);
	customProgressDialog = CustomProgressDialog.createDialog(this, "加载中");
	customProgressDialog.show();
	Intent i = getIntent();
	cent = i.getIntExtra("cent", 0);
	orderId = i.getStringExtra("orderId");
	p_id = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE)
		.getString("id", "");
	if ("".equals(p_id)) {
	    Toast.makeText(this, "用户信息异常！请重新登录", Toast.LENGTH_LONG).show();
	    return;
	}

	init();
	initData();
    }

    /**
     * 
     */
    private void initData() {
	new Thread(new Runnable() {
	    public void run() {
		try {

		    String urlString = HttpUtil.QUERY_USERINFO;
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("id", p_id);
		    // 积分 处理
		    String res = HttpUtil.postRequest(urlString, map);
		    if (res != null && !res.equals("")) {
			Log.i("获取个人积分", "=======成功");

			JSONObject jsonObject = new JSONObject(res);
			JSONArray array = jsonObject.getJSONArray("rows");

			String score = array.optJSONObject(0)
				.getString("score");
			int oldCent = 0;
			if (!"".equals(PublicTools.doWithNullData(score))) {

			    oldCent = Integer.parseInt(score);
			    newCent = oldCent + cent;
			} else {
			    oldCent = 0;
			    newCent = cent;
			}

			// 修改个人积分信息
			String url = HttpUtil.SUBMIT_USERINFO;
			Map<String, String> map1 = new HashMap<String, String>();

			map1.put("score", String.valueOf(newCent));
			map1.put("id", p_id);
			String res1 = HttpUtil.postRequest(url, map1);
			if (res1 == null) {
			    // 提交失败失败
			    Log.e("积分信息", "=======提交失败");
			}
			// 修改 订单状态信息
			String urlString2 = HttpUtil.SUBMIT_ORDER_PUB;
			Map<String, String> map2 = new HashMap<String, String>();
			map2.put("id", orderId);
			map2.put("win", "1");
			map2.put("score", String.valueOf(cent));

			String resString2 = HttpUtil.postRequest(urlString2,
				map2);
			if (resString2 != null && !resString2.equals("")) {
			    Log.i("修改订单状态", "======成功");
			    msg = handler.obtainMessage(10001);
			    msg.sendToTarget();
			} else {
			    Log.e("修改订单状态", "======失败");
			    msg = handler.obtainMessage(10002);
			    msg.sendToTarget();

			}
			// JSONObject jsonObject2= new JSONObject(resString2);
			// dia.dismiss();

		    } else {
			Log.i("获取个人积分", "=======失败");
		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }

    private void init() {
	ok = (Button) findViewById(R.id.ok);
	TextView queding = (TextView) findViewById(R.id.jieguo);
	Typeface tf = Typeface.createFromAsset(getAssets(),
		"Roboto-MediumItalic.ttf");
	queding.setTypeface(tf);
	queding.setText("恭喜您获得  " + cent + " 分");
	ok.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		// 计算 提交结果到服务器
		MyAppliation.getInstance().exit();

	    }
	});
    }
}
