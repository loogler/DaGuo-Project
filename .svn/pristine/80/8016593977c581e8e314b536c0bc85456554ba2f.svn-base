package com.daguo.modem.choujiang;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.view.dialog.CustomProgressDialog;

public class ChouJiangAty extends Activity {
	private LuckyPanView mLuckyPanView;
	private ImageView mStartBtn;
	private Dialog dialog;
	private int cent;
	private int newCent;
	private CustomProgressDialog dia;
	private String pid, orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_choujiang);
		MyAppliation.getInstance().addActivity(this);
		Intent intent = getIntent();
		orderId = intent.getStringExtra("orderId");

		MyAppliation.getInstance().addActivity(this);
		SharedPreferences sp = getSharedPreferences("userinfo", 0);
		pid = sp.getString("id", "");

		mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
		mStartBtn = (ImageView) findViewById(R.id.id_start_btn);

		mStartBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!mLuckyPanView.isStart()) {
					mStartBtn.setImageResource(R.drawable.stop);
					mLuckyPanView.luckyStart(1);

				} else {
					if (!mLuckyPanView.isShouldEnd())

					{
						mStartBtn.setImageResource(R.drawable.start);
						mLuckyPanView.luckyEnd();
						mStartBtn.setClickable(false);// 终止本次抽奖

					}
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// new AlertDialog.Builder(ChouJiangAty.this)
							// .setMessage("您抽到了" + LuckyPanView.console)
							// .setPositiveButton("确定", null).show();
							// dialog = new Dialog(ChouJiangAty.this);
							// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							// dialog.show();
							//
							// final Window window = dialog.getWindow();
							// window.setContentView(R.layout.item_choujiangconsole);
							// Button ok = (Button)
							// window.findViewById(R.id.ok);
							// TextView queding = (TextView) window
							// .findViewById(R.id.jieguo);
							// Typeface tf =
							// Typeface.createFromAsset(getAssets(),
							// "Roboto-MediumItalic.ttf");
							// queding.setTypeface(tf);
							// cent = Integer
							// .parseInt(LuckyPanView.console);
							// queding.setText("恭喜您获得  " + LuckyPanView.console
							// + " 分");
							// ok.setOnClickListener(new View.OnClickListener()
							// {
							//
							// @Override
							// public void onClick(View arg0) {
							// // 计算 提交结果到服务器
							//
							// Log.i("抽中多少积分", "" + cent);
							//
							// dialog.dismiss();
							// // 结束当前activity
							// TODO cent =
							// Integer.parseInt(LuckyPanView.console);
							// new Get_Cent().start();
							Intent intent = new Intent(ChouJiangAty.this,
									ChouJiangConsoleAty.class);
							intent.putExtra("cent", cent);
							intent.putExtra("orderId", orderId);
							//
							// }
							// });
						}
					}, 3000);
				}

			}
		});
	}

	/**
	 * 更新积分
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-9 下午2:44:01
	 */
	// class Get_Cent extends Thread {
	// @Override
	// public void run() {
	// super.run();
	// runOnUiThread(new Runnable() {
	// public void run() {
	// dia = CustomProgressDialog.createDialog(ChouJiangAty.this,
	// "正在生成订单,请稍等");
	// dia.show();
	// }
	// });
	//
	// try {
	// String urlString = HttpUtil.QUERY_USERINFO;
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("id", pid);
	// // 积分 处理
	// String res = HttpUtil.postRequest(urlString, map);
	// if (res != null && !res.equals("")) {
	// Log.i("获取个人积分", "=======成功");
	//
	// JSONObject jsonObject = new JSONObject(res);
	// JSONArray array = jsonObject.getJSONArray("rows");
	//
	// String score = array.optJSONObject(0).getString("score");
	// int oldCent = 0;
	// if (!score.equals("") && score != null && score != "null") {
	//
	// oldCent = Integer.parseInt(score);
	// newCent = oldCent + cent;
	// } else {
	// oldCent = 0;
	// newCent = cent;
	// }
	//
	// // 修改个人积分信息
	// String url = HttpUtil.SUBMIT_USERINFO;
	// Map<String, String> map1 = new HashMap<String, String>();
	//
	// map1.put("score", "" + newCent);
	// map1.put("id", pid);
	// String res1 = HttpUtil.postRequest(url, map1);
	// if (res1 == null) {
	// // 提交失败失败
	// Log.e("积分信息", "=======提交失败");
	// }
	// // 修改 订单状态信息
	// String urlString2 = HttpUtil.SUBMIT_ORDER_PUB;
	// Map<String, String> map2 = new HashMap<String, String>();
	// map2.put("id", orderId);
	// map2.put("win", "1");
	// map2.put("score", "" + cent);
	// String resString2 = HttpUtil.postRequest(urlString2, map2);
	// if (resString2 != null && !resString2.equals("")) {
	// Log.i("修改订单状态", "======成功");
	// runOnUiThread(new Runnable() {
	// public void run() {
	// Toast.makeText(ChouJiangAty.this, "恭喜  购买成功！",
	// Toast.LENGTH_LONG).show();
	// }
	// });
	// } else {
	// Log.e("修改订单状态", "======失败");
	//
	// }
	// // JSONObject jsonObject2= new JSONObject(resString2);
	// // dia.dismiss();
	//
	// } else {
	// Log.i("获取个人积分", "=======失败");
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// }

	//
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// new AlertDialog.Builder(ChouJiangAty.this).setMessage("抽奖中，请等待")
	// .setPositiveButton("确定", null).create().show();
	// }
	//
	// return true;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.gc();
	}

}
