/**
 * 互相学习 共同进步
 */
package com.daguo.ui.operators.broadband;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.ui.before.UserAgreementAty;
import com.daguo.util.alipay.PayDemoActivity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.daguo.utils.UploadUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午5:15:52
 * @function ：
 */
public class Oper_BroadBand_OrderAty extends Activity implements
		OnClickListener {
	private final int INTENT_CAMERAL1 = 10001;
	private final int MSG_UPLOAD_SUC = 10002;
	private final int INTENT_CAMERAL2 = 10003;
	private final int MSG_GET_ORDER_CHART_SUC = 10004;
	private final int MSG_GET_ORDER_CHART_FAIL = 10005;
	private final int MSG_GET_ORDER_DETAIL_CHART_FAIL = 10006;
	private final int MSG_GET_ORDER_DETAIL_CHART_SUC = 10007;

	/**
	 * initViews
	 */

	private View parentView;
	private TextView tel_tv, repick_tv, agreement_tv, submit_tv;
	private CheckBox agreement_ckb;
	private EditText name_edt, cardId_edt, contact_edt, logist_edt, guid_edt;
	private ImageView pic1_iv, pic2_iv;

	/**
	 * data
	 */
	private String p_id;
	private String id, cost_name, price;
	private String orderId;

	private String name, cardId, contact, logist, guid;

	private boolean isAgree = true;

	private File file;

	private List<String> imgLists = new ArrayList<String>();

	/**
	 * tools
	 */

	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_UPLOAD_SUC:
				JSONObject jsonObject1;
				try {
					jsonObject1 = new JSONObject(msg.obj.toString());
					String imgSRC = jsonObject1.getString("fileRelativePath");
					imgLists.add(imgSRC);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case MSG_GET_ORDER_CHART_SUC:
				submit();

				break;
			case MSG_GET_ORDER_CHART_FAIL:
				Toast.makeText(Oper_BroadBand_OrderAty.this, "订单生成失败！请重试",
						Toast.LENGTH_LONG).show();

				break;

			case MSG_GET_ORDER_DETAIL_CHART_FAIL:

				Toast.makeText(Oper_BroadBand_OrderAty.this, "订单生成失败！请重试",
						Toast.LENGTH_LONG).show();
				break;

			case MSG_GET_ORDER_DETAIL_CHART_SUC:
				Intent intent = new Intent(Oper_BroadBand_OrderAty.this,
						PayDemoActivity.class);
				intent.putExtra("orderId", orderId);
				intent.putExtra("name", cost_name);
				intent.putExtra("price_total", price);
				intent.putExtra("price", price);
				intent.putExtra("count", "1");
				startActivity(intent);

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
	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		parentView = LayoutInflater.from(this).inflate(
				R.layout.aty_oper_mobile_order, null);
		setContentView(parentView);
		MyAppliation.getInstance().addActivity(this);
		getDataFrom();
		initViews();

		tel_tv.setText(cost_name);

	}

	/**
     * 
     */
	private void initViews() {
		initHeadView();
		tel_tv = (TextView) findViewById(R.id.tel_tv);
		repick_tv = (TextView) findViewById(R.id.repick_tv);
		agreement_tv = (TextView) findViewById(R.id.agreement_tv);
		submit_tv = (TextView) findViewById(R.id.submit_tv);

		agreement_ckb = (CheckBox) findViewById(R.id.agreement_ckb);

		name_edt = (EditText) findViewById(R.id.name_edt);
		cardId_edt = (EditText) findViewById(R.id.cardId_edt);
		contact_edt = (EditText) findViewById(R.id.contact_edt);
		logist_edt = (EditText) findViewById(R.id.logist_edt);
		guid_edt = (EditText) findViewById(R.id.guid_edt);

		pic1_iv = (ImageView) findViewById(R.id.pic1_iv);
		pic2_iv = (ImageView) findViewById(R.id.pic2_iv);

		repick_tv.setOnClickListener(this);
		agreement_tv.setOnClickListener(this);
		submit_tv.setOnClickListener(this);

		pic1_iv.setOnClickListener(this);
		pic2_iv.setOnClickListener(this);

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
		title_tv.setText("办理信息");
		function_tv.setVisibility(View.GONE);
		remind_iv.setVisibility(View.GONE);

	}

	/**
	 * 从其他位置获取变量
	 */
	private void getDataFrom() {
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		if ("".equals(p_id)) {
			Toast.makeText(Oper_BroadBand_OrderAty.this, "用户信息异常，请重新登陆",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = getIntent();
		id = intent.getStringExtra("id");

		price = intent.getStringExtra("price");
		cost_name = intent.getStringExtra("cost_name");

	}

	/*-----------------  implements -----------------------*/
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.repick_tv:

			finish();

			break;

		case R.id.agreement_tv:
			Intent intent = new Intent(Oper_BroadBand_OrderAty.this,

			UserAgreementAty.class);
			intent.putExtra("type", "broadband");
			startActivity(intent);

			break;

		case R.id.pic1_iv:
			Intent openCameral = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			file = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName());
			startActivityForResult(openCameral, INTENT_CAMERAL1);

			break;

		case R.id.pic2_iv:
			Intent openCameral2 = new Intent(
					"android.media.action.IMAGE_CAPTURE");
			file = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName());
			startActivityForResult(openCameral2, INTENT_CAMERAL2);
			break;

		case R.id.submit_tv:
			checkInfo();

			break;

		default:
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onActivityResult(int, int,
	 * android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == INTENT_CAMERAL1 && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			pic1_iv.setImageBitmap(imageBitmap);
			upLoadPhotos(imageBitmap);
		} else if (requestCode == INTENT_CAMERAL2 && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			pic2_iv.setImageBitmap(imageBitmap);
			upLoadPhotos(imageBitmap);

		}
	}

	/*--------------- data thread --------------------*/

	private void upLoadPhotos(final Bitmap bm) {

		new Thread(new Runnable() {
			public void run() {

				Log.i("", "UploadPhoto上传至网站");

				String request = null;
				String url = HttpUtil.IMG_URL_SUB;
				System.out.println("uploadFile--->" + file.getName());
				// UploadPhotoUtil up = new UploadPhotoUtil();
				InputStream isInputStream = PublicTools.Bitmap2InputStream(bm,
						100);
				request = UploadUtil.uploadFile(isInputStream, url);
				// progressDialog.dismiss();
				msg = handler.obtainMessage(MSG_UPLOAD_SUC);
				msg.obj = request;
				msg.sendToTarget();

			}
		}).start();

	}

	/**
	 * 先获得订单表 在与详情表关联
	 */
	private void getOrder() {

		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.SUBMIT_ORDER_PUB;
					Map<String, String> map = new HashMap<String, String>();

					map.put("p_id", p_id);
					map.put("status", "0");
					map.put("pay_type", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");

					map.put("order_type", "1");
					map.put("pay_status", "0");
					// map.put("pay_num", pay_num);
					map.put("person_num", guid);

					String res = HttpUtil.postRequest(url, map);
					JSONObject jsonObject = new JSONObject(res);
					if ("1".equals(jsonObject.getString("result"))) {
						// 成功
						orderId = jsonObject.getJSONObject("obj").getString(
								"id");

						msg = handler.obtainMessage(MSG_GET_ORDER_CHART_SUC);
						msg.sendToTarget();
					} else {
						// 失败
						msg = handler.obtainMessage(MSG_GET_ORDER_CHART_FAIL);
						msg.sendToTarget();

					}

				} catch (JSONException ex) {
					Log.e("支付跳转", "Json异常崩溃");

				} catch (Exception e) {
					Log.e("支付跳转", "异常崩溃");
				}
			}
		}).start();

	}

	/**
	 * 生成详情表
	 */
	private void submit() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.SUBMIT_BROADBAND_DETAIL_UNPAID;
					Map<String, String> map = new HashMap<String, String>();
					map.put("order_id", orderId);
					map.put("name", name);
					map.put("order_tel", contact);
					map.put("order_id_card", cardId);
					map.put("order_id_card_copy",
							PublicTools.listToString(imgLists));
					map.put("address", logist);
					map.put("broadband_id", id);
					String res = HttpUtil.postRequest(url, map);

					JSONObject jsonObject = new JSONObject(res);

					if (jsonObject.getInt("result") == 1) {
						// 成功
						msg = handler
								.obtainMessage(MSG_GET_ORDER_DETAIL_CHART_SUC);
						msg.sendToTarget();

					} else {
						// 失败
						msg = handler
								.obtainMessage(MSG_GET_ORDER_DETAIL_CHART_FAIL);
						msg.sendToTarget();
					}

				} catch (JSONException ex) {
					Log.e("支付跳转", "Json异常崩溃");

				} catch (Exception e) {
					Log.e("支付跳转", " 异常崩溃");
				}
			}
		}).start();

	}

	/*------------------  tools   -------------------------*/
	/**
	 * 文件命名
	 * 
	 * @return 文件名
	 */
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");

		return (dateFormat.format(date) + ".JPEG");

	}

	private void checkInfo() {

		isAgree = agreement_ckb.isChecked();
		name = PublicTools.doWithNullData(name_edt.getText().toString().trim());
		cardId = PublicTools.doWithNullData(cardId_edt.getText().toString()
				.trim());
		contact = PublicTools.doWithNullData(contact_edt.getText().toString()
				.trim());
		logist = PublicTools.doWithNullData(logist_edt.getText().toString()
				.trim());
		guid = PublicTools.doWithNullData(guid_edt.getText().toString().trim());

		if (!isAgree) {
			return;
		}
		if ("".equals(name)) {
			Toast.makeText(Oper_BroadBand_OrderAty.this, "名字不可为空",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ("".equals(cardId)) {
			Toast.makeText(Oper_BroadBand_OrderAty.this, "身份证不可为空",
					Toast.LENGTH_LONG).show();
			return;

		}
		if ("".equals(contact)) {
			Toast.makeText(Oper_BroadBand_OrderAty.this, "联系号码不可为空",
					Toast.LENGTH_LONG).show();
			return;

		}
		if ("".equals(logist)) {
			Toast.makeText(Oper_BroadBand_OrderAty.this, "地址不可为空",
					Toast.LENGTH_LONG).show();
			return;

		}
		if (imgLists.size() < 2) {
			Toast.makeText(Oper_BroadBand_OrderAty.this, "必须先上传照片",
					Toast.LENGTH_LONG).show();
			return;
		}

		getOrder();

	}

}
