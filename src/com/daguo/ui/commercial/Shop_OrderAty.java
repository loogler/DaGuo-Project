package com.daguo.ui.commercial;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.ui.user.UserInfo_ModifyAty1;
import com.daguo.util.alipay.PayDemoActivity;
import com.daguo.util.base.Fragment_Mall_Item;
import com.daguo.util.base.LineEditText;
import com.daguo.utils.HttpUtil;

public class Shop_OrderAty extends Activity {
	private LineEditText nameEditText, telEditText, logisticsEditText;
	private Button submitBtn;
	public static String name, tel, logistics;
	private String p_id, p_name;
	public static String orderId;
	String orderInfoId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_shop_order);
		MyAppliation.getInstance().addActivity(this);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		p_name = sp.getString("name", "");

		nameEditText = (LineEditText) findViewById(R.id.nameEdit);
		telEditText = (LineEditText) findViewById(R.id.telEdit);
		logisticsEditText = (LineEditText) findViewById(R.id.logisticsEdit);
		submitBtn = (Button) findViewById(R.id.submitbtn);
		submitBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				name = nameEditText.getText().toString().trim();
				tel = telEditText.getText().toString().trim();
				logistics = logisticsEditText.getText().toString().trim();
				if (name != null && !name.equals("") && tel != null
						&& !tel.equals("") && logistics != null
						&& !logistics.equals("")) {
					if (p_name != null && !p_name.equals("")) {

						new Thread(new UploadThread()).start();
					} else {
						new AlertDialog.Builder(Shop_OrderAty.this)
								.setMessage("请先完善个人资料")
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface arg0,
													int arg1) {
												Intent intent = new Intent(
														Shop_OrderAty.this,
														UserInfo_ModifyAty1.class);
												startActivity(intent);
											}
										}).create().show();
					}

				} else {
					Toast.makeText(Shop_OrderAty.this, "资料尚未完善",
							Toast.LENGTH_SHORT).show();
				}

			}
		});

	}

	class UploadThread implements Runnable {
		@Override
		public void run() {

			try {
				// 生成订单主表
				getOrder(p_id, "3", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
				// 生成订单详情
				String url = HttpUtil.SUBMIT_GOODS_DETAIL;
				Map<String, String> map = new HashMap<String, String>();

				map.put("address", logistics);
				map.put("phone", tel);
				map.put("count", "1");
				map.put("price", Fragment_Mall_Item.goodsPrice);
				map.put("total_price", Fragment_Mall_Item.goodsPrice);
				map.put("goods_id", Fragment_Mall_Item.goodsId);
				map.put("order_id", orderInfoId);
				map.put("order_name", name);
				try {
					String res = HttpUtil.postRequest(url, map);
					if (res != null && !res.equals("")) {
						// 详细表提交成功
						Intent intent = new Intent(Shop_OrderAty.this,
								PayDemoActivity.class);
						intent.putExtra("price", Fragment_Mall_Item.goodsPrice);
						intent.putExtra("name", Fragment_Mall_Item.goodsName);
						intent.putExtra("detail", "商城商品");
						intent.putExtra("orderInfoId", orderInfoId);
						startActivity(intent);

					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								new AlertDialog.Builder(Shop_OrderAty.this)
										.setMessage("订单生成失败，请重新提交！")
										.setPositiveButton("确定", null).create()
										.show();
							}
						});
						Log.e("商品详细订单", "=======生成失败");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			// try {
			// String url = HttpUtil.SUBMIT_ORDER_PUB;
			// Map<String, String> map = new HashMap<String, String>();
			// map.put("pay_type", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
			// map.put("pay_status", "0");
			// map.put("p_id", p_id);
			// map.put("order_type", "3");
			// map.put("status", "0");
			// String res = HttpUtil.postRequest(url, map);
			// JSONObject js = new JSONObject(res);
			// if (js.getInt("result") == 1) {
			// // 订单生成成功
			// orderId = js.getJSONObject("obj").getString("id");
			// Intent intent = new Intent(Shop_OrderAty.this,
			// PayDemoActivity.class);
			//
			// startActivity(intent);
			// } else {
			// // 订单失败
			//
			// }
			//
			// } catch (Exception e) {
			// }
		}
	}

	/**
	 * // 生成一个订单，（公用订单），主表
	 * 
	 * @param pid
	 *            ===== 个人id
	 * @param order_type
	 *            ==== 商品类型 1 :宽带入网 2：入网选号 3：商品购物'
	 * @param pay_type
	 *            === 支付宝： ba6cb325-65c1-4f53-87a7-bfe67b37adfe
	 * @throws JSONException
	 */
	void getOrder(String pid, String order_type, String pay_type)
			throws JSONException {
		try {
			String url = HttpUtil.SUBMIT_ORDER_PUB;
			Map<String, String> sss = new HashMap<String, String>();
			sss.put("p_id", pid);// 个人账号id
			sss.put("status", "0");// 0 未处理 --1已处理 无需处理，只要我提交为0
			sss.put("order_type", order_type);// 商品类型 1 :宽带入网 2：入网选号
			// 3：商品购物'
			sss.put("pay_status", "0");// 支付状态
			sss.put("pay_type", pay_type);// 支付类型

			String resString = HttpUtil.postRequest(url, sss);
			JSONObject jsonObject = new JSONObject(resString);
			if (jsonObject != null && !jsonObject.equals("")) {

				orderInfoId = jsonObject.getJSONObject("obj").getString("id");
			} else {
				Log.e("生成主表订单", "=====失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d("订单id", "orderInfoId*****" + orderInfoId);
	}

}
