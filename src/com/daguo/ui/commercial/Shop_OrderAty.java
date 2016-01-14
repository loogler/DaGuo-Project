package com.daguo.ui.commercial;

import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.choujiang.ChouJiangAty;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.alipay.PayDemoActivity;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

public class Shop_OrderAty extends Activity implements OnClickListener {

    private final int REQ_ADDRESS = 10001;
    private final int MSG_SUBMIT_SUC = 10002;
    private final int MSG_SUBMIT_FAIL = 10003;
    private final int MSG_GET_ORDER_CHART_SUC = 10004;
    private final int MSG_GET_ORDER_CHART_FAIL = 10005;

    /**
     * initViews
     */
    private TextView consigneeName_tv, consigneeAddress_tv, consgineeChange_tv,
	    goodsName_tv, goodsPrice_tv, goodsCount2_tv, goodsPriceTotal_tv,
	    submit_tv;
    private TextView goodsCountAdd_tv, goodsCountMin_tv, goodsCount_tv;
    private EditText guidNo_edt;

    private ImageView goodsPic_iv;

    /**
     * 界面data
     */
    private String goodsId, goodsName, goodsPrice, guidNo, consigneeName,
	    consigneeAddress, consigneeTel, goodsPic, goodsPrice_total;
    private int goodsCount = 1;
    private String orderInfoId, p_id, orderId;

    /**
     * tools
     */

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_SUBMIT_SUC:
		Intent intent = new Intent(Shop_OrderAty.this,
			PayDemoActivity.class);
		intent.putExtra("orderId", orderId);
		intent.putExtra("name", goodsName);
		intent.putExtra("price_total", goodsPrice_total);
		intent.putExtra("price", goodsPrice);
		intent.putExtra("count", String.valueOf(goodsCount));

		startActivity(intent);

		break;
	    case MSG_SUBMIT_FAIL:

		Toast.makeText(Shop_OrderAty.this, "订单生成失败！请重试",
			Toast.LENGTH_LONG).show();
		break;

	    case MSG_GET_ORDER_CHART_SUC:
		submit();
		break;
	    case MSG_GET_ORDER_CHART_FAIL:
		Toast.makeText(Shop_OrderAty.this, "订单生成失败！请重试",
			Toast.LENGTH_LONG).show();
		return;

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
	setContentView(R.layout.aty_shop_order);
	MyAppliation.getInstance().addActivity(this);
	p_id = getSharedPreferences("userinfo", Context.MODE_WORLD_READABLE)
		.getString("id", "");
	if ("".equals(p_id)) {
	    return;
	}
	getDataFromActivity();
	initViews();

    }

    /**
     * 通过上个界面传递的商品信息 intent
     */
    private void getDataFromActivity() {
	Intent intent = getIntent();
	goodsId = intent.getStringExtra("id");
	goodsName = intent.getStringExtra("name");
	goodsPrice = intent.getStringExtra("price");
	goodsPic = intent.getStringExtra("pic");
	goodsPrice_total = goodsPrice;// 这步为了防止editext没有变动，出现总价为0的尴尬现象。

    }

    /**
     * 
     */
    private void initViews() {
	consigneeName_tv = (TextView) findViewById(R.id.consigneeName_tv);
	consigneeAddress_tv = (TextView) findViewById(R.id.consigneeAddress_tv);
	consgineeChange_tv = (TextView) findViewById(R.id.consgineeChange_tv);

	goodsName_tv = (TextView) findViewById(R.id.goodsName_tv);
	goodsPrice_tv = (TextView) findViewById(R.id.goodsPrice_tv);
	goodsCount_tv = (TextView) findViewById(R.id.goodsCount_tv);
	goodsCountMin_tv = (TextView) findViewById(R.id.goodsCountMin_tv);
	goodsCountAdd_tv = (TextView) findViewById(R.id.goodsCountAdd_tv);

	guidNo_edt = (EditText) findViewById(R.id.guidNo_edt);
	goodsCount2_tv = (TextView) findViewById(R.id.goodsCount2_tv);
	goodsPriceTotal_tv = (TextView) findViewById(R.id.goodsPriceTotal_tv);
	submit_tv = (TextView) findViewById(R.id.submit_tv);

	goodsPic_iv = (ImageView) findViewById(R.id.goodsPic_iv);

	goodsCountAdd_tv.setOnClickListener(this);
	goodsCountMin_tv.setOnClickListener(this);
	consgineeChange_tv.setOnClickListener(this);
	submit_tv.setOnClickListener(this);

	initHeadView();

	setData();

	// 去掉edit监听 换成text监听
	// goodsCount_edt.addTextChangedListener(new TextWatcher() {
	// int l = 0;// //////记录字符串被删除字符之前，字符串的长度
	// int location = 0;// 记录光标的位置
	//
	// @Override
	// public void onTextChanged(CharSequence s, int start, int before,
	// int count) {
	//
	// }
	//
	// @Override
	// public void beforeTextChanged(CharSequence s, int start, int count,
	// int after) {
	// l = s.length();
	// location = goodsCount_edt.getSelectionStart();
	// }
	//
	// @Override
	// public void afterTextChanged(Editable s) {
	// if (l != s.toString().length()) {
	// //
	// // gyf.function.face_analysis faceAnalysis=new
	// // gyf.function.face_analysis(Shop_OrderAty.this);
	// // SpannableStringBuilder
	// // sBuilder=faceAnalysis.getSpannableStringBuilder(s.toString());
	// // eText.setText(sBuilder);
	// // eText.setText("");
	// String abc = goodsCount_edt.getText().toString().trim();
	// if ("".equals(abc)) {
	// abc = "1";
	// Toast.makeText(Shop_OrderAty.this, "订单数不能少于1",
	// Toast.LENGTH_SHORT).show();
	// }
	// goodsCount_edt.setText(abc);
	// try {
	// goodsCount = Integer.parseInt(abc);
	// } catch (NumberFormatException e) {
	// Toast.makeText(Shop_OrderAty.this, "输入的数量异常",
	// Toast.LENGTH_LONG).show();
	// return;
	// }
	// goodsCount2_tv.setText(abc);
	// goodsPrice_total = String.valueOf(goodsCount
	// * Double.parseDouble(goodsPrice));
	// goodsPriceTotal_tv.setText(goodsPrice_total);
	// Editable etable = goodsCount_edt.getText();
	// Selection.setSelection(etable, location);
	// // Toast.makeText(releaseComment.this, "11111",
	// // Toast.LENGTH_SHORT).show();
	//
	// }
	//
	// // Toast.makeText(releaseComment.this, "0000",
	// // Toast.LENGTH_SHORT).show();
	// }
	// });

    }

    /**
     * 
     */
    private void setData() {
	goodsName_tv.setText(PublicTools.doWithNullData(goodsName));
	goodsPrice_tv.setText(PublicTools.doWithNullData("￥"+goodsPrice));
	FinalBitmap.create(this).display(goodsPic_iv,
		HttpUtil.IMG_URL + PublicTools.doWithNullData(goodsPic));
	goodsCount_tv.setText("1");
	goodsCount2_tv.setText("1");
	goodsPriceTotal_tv.setText(PublicTools.doWithNullData(goodsPrice));

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
	title_tv.setText("确定订单");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	switch (requestCode) {
	case REQ_ADDRESS:
	    if (data != null) {

		guidNo = PublicTools
			.doWithNullData(data.getStringExtra("guid"));
		consigneeAddress = PublicTools.doWithNullData(data
			.getStringExtra("address"));
		consigneeName = PublicTools.doWithNullData(data
			.getStringExtra("name"));
		consigneeTel = PublicTools.doWithNullData(data
			.getStringExtra("tel"));

		guidNo_edt.setText(PublicTools.doWithNullData(guidNo));
		consigneeAddress_tv.setText(" "+PublicTools
			.doWithNullData(consigneeAddress));
		consigneeName_tv.setText(PublicTools
			.doWithNullData(consigneeName)
			+ "  "
			+ PublicTools.doWithNullData(consigneeTel));
	    }
	    break;

	default:
	    break;
	}
    }

    /******************* data thread *************************************************/

    /**
     * 先获取订单表 ，然后网订单详情表关联订单表，
     */
    private void getOrderCHart() {

	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.SUBMIT_ORDER_PUB;
		    Map<String, String> map = new HashMap<String, String>();

		    map.put("p_id", p_id);
		    map.put("status", "0");
		    map.put("pay_type", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");

		    map.put("order_type", "3");
		    map.put("pay_status", "0");
		    // map.put("pay_num", pay_num);
		    map.put("person_num", guidNo);

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

    private void submit() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.SUBMIT_GOODS_DETAIL;
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("address",
			    PublicTools.doWithNullData(consigneeAddress));
		    map.put("phone", PublicTools.doWithNullData(consigneeTel));
		    map.put("count", String.valueOf(goodsCount));
		    map.put("price", PublicTools.doWithNullData(goodsPrice));
		    map.put("total_price", goodsPrice_total);
		    map.put("goods_id", goodsId);
		    map.put("order_name", consigneeName);
		    map.put("order_id", orderId);
		    String resString = HttpUtil.postRequest(url, map);
		    JSONObject jsJsonObject = new JSONObject(resString);
		    if ("1".equals(jsJsonObject.getString("result"))) {
			// 提交成功 --- 支付宝开始支付
			orderInfoId = jsJsonObject.getJSONObject("obj")
				.getString("order_id");
			msg = handler.obtainMessage(MSG_SUBMIT_SUC);
			msg.sendToTarget();

		    } else {
			// 订单提交失败 重新下单
			msg = handler.obtainMessage(MSG_SUBMIT_FAIL);
			msg.sendToTarget();

		    }

		} catch (JSONException e) {
		    Log.e("提交商品订单", "json出现异常解析");
		} catch (Exception ex) {
		    Log.e("提交商品订单", "出现异常");
		}
	    }
	}).start();
    }

    /***************************************************************************/

    /*
     * implement Onclick事件 (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.submit_tv:
	    guidNo = PublicTools.doWithNullData(guidNo_edt.getText().toString()
		    .trim());
	    getOrderCHart();

	    break;
	case R.id.consgineeChange_tv:
	    Intent intent = new Intent(Shop_OrderAty.this,
		    Shop_Order_AddressAty.class);
	    startActivityForResult(intent, REQ_ADDRESS);

	    break;
	case R.id.goodsCountAdd_tv:
	    goodsCount--;
	    goodsCount_tv.setText(String.valueOf(goodsCount));
	    goodsPrice_total = String.valueOf(goodsCount
		    * Double.parseDouble(goodsPrice));
	    goodsCount2_tv.setText(String.valueOf(goodsCount));
	    goodsPriceTotal_tv.setText(goodsPrice_total);
	    break;
	case R.id.goodsCountMin_tv:
	    goodsCount++;
	    goodsCount_tv.setText(String.valueOf(goodsCount));
	    goodsPrice_total = String.valueOf(goodsCount
		    * Double.parseDouble(goodsPrice));
	    goodsCount2_tv.setText(String.valueOf(goodsCount));
	    goodsPriceTotal_tv.setText(goodsPrice_total);
	    break;

	default:
	    break;
	}

    }
    // private LineEditText nameEditText, telEditText, logisticsEditText;
    // private Button submitBtn;
    // public static String name, tel, logistics;
    // private String p_id, p_name;
    // public static String orderId;
    // String orderInfoId;
    //
    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.aty_shop_order);
    // MyAppliation.getInstance().addActivity(this);
    // SharedPreferences sp = getSharedPreferences("userinfo",
    // Context.MODE_WORLD_READABLE);
    // p_id = sp.getString("id", "");
    // p_name = sp.getString("name", "");
    //
    // nameEditText = (LineEditText) findViewById(R.id.nameEdit);
    // telEditText = (LineEditText) findViewById(R.id.telEdit);
    // logisticsEditText = (LineEditText) findViewById(R.id.logisticsEdit);
    // submitBtn = (Button) findViewById(R.id.submitbtn);
    // submitBtn.setOnClickListener(new View.OnClickListener() {
    //
    // @Override
    // public void onClick(View arg0) {
    //
    // name = nameEditText.getText().toString().trim();
    // tel = telEditText.getText().toString().trim();
    // logistics = logisticsEditText.getText().toString().trim();
    // if (name != null && !name.equals("") && tel != null
    // && !tel.equals("") && logistics != null
    // && !logistics.equals("")) {
    // if (p_name != null && !p_name.equals("")) {
    //
    // new Thread(new UploadThread()).start();
    // } else {
    // new AlertDialog.Builder(Shop_OrderAty.this)
    // .setMessage("请先完善个人资料")
    // .setPositiveButton("确定",
    // new DialogInterface.OnClickListener() {
    //
    // @Override
    // public void onClick(
    // DialogInterface arg0,
    // int arg1) {
    // Intent intent = new Intent(
    // Shop_OrderAty.this,
    // UserInfo_ModifyAty1.class);
    // startActivity(intent);
    // }
    // }).create().show();
    // }
    //
    // } else {
    // Toast.makeText(Shop_OrderAty.this, "资料尚未完善",
    // Toast.LENGTH_SHORT).show();
    // }
    //
    // }
    // });
    //
    // }
    //
    // class UploadThread implements Runnable {
    // @Override
    // public void run() {
    //
    // try {
    // // 生成订单主表
    // getOrder(p_id, "3", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
    // // 生成订单详情
    // String url = HttpUtil.SUBMIT_GOODS_DETAIL;
    // Map<String, String> map = new HashMap<String, String>();
    //
    // map.put("address", logistics);
    // map.put("phone", tel);
    // map.put("count", "1");
    // map.put("price", Fragment_Mall_Item.goodsPrice);
    // map.put("total_price", Fragment_Mall_Item.goodsPrice);
    // map.put("goods_id", Fragment_Mall_Item.goodsId);
    // map.put("order_id", orderInfoId);
    // map.put("order_name", name);
    // try {
    // String res = HttpUtil.postRequest(url, map);
    // if (res != null && !res.equals("")) {
    // // 详细表提交成功
    // Intent intent = new Intent(Shop_OrderAty.this,
    // PayDemoActivity.class);
    // intent.putExtra("price", Fragment_Mall_Item.goodsPrice);
    // intent.putExtra("name", Fragment_Mall_Item.goodsName);
    // intent.putExtra("detail", "商城商品");
    // intent.putExtra("orderInfoId", orderInfoId);
    // startActivity(intent);
    //
    // } else {
    // runOnUiThread(new Runnable() {
    // public void run() {
    // new AlertDialog.Builder(Shop_OrderAty.this)
    // .setMessage("订单生成失败，请重新提交！")
    // .setPositiveButton("确定", null).create()
    // .show();
    // }
    // });
    // Log.e("商品详细订单", "=======生成失败");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
    //
    // // try {
    // // String url = HttpUtil.SUBMIT_ORDER_PUB;
    // // Map<String, String> map = new HashMap<String, String>();
    // // map.put("pay_type", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
    // // map.put("pay_status", "0");
    // // map.put("p_id", p_id);
    // // map.put("order_type", "3");
    // // map.put("status", "0");
    // // String res = HttpUtil.postRequest(url, map);
    // // JSONObject js = new JSONObject(res);
    // // if (js.getInt("result") == 1) {
    // // // 订单生成成功
    // // orderId = js.getJSONObject("obj").getString("id");
    // // Intent intent = new Intent(Shop_OrderAty.this,
    // // PayDemoActivity.class);
    // //
    // // startActivity(intent);
    // // } else {
    // // // 订单失败
    // //
    // // }
    // //
    // // } catch (Exception e) {
    // // }
    // }
    // }
    //
    // /**
    // * // 生成一个订单，（公用订单），主表
    // *
    // * @param pid
    // * ===== 个人id
    // * @param order_type
    // * ==== 商品类型 1 :宽带入网 2：入网选号 3：商品购物'
    // * @param pay_type
    // * === 支付宝： ba6cb325-65c1-4f53-87a7-bfe67b37adfe
    // * @throws JSONException
    // */
    // void getOrder(String pid, String order_type, String pay_type)
    // throws JSONException {
    // try {
    // String url = HttpUtil.SUBMIT_ORDER_PUB;
    // Map<String, String> sss = new HashMap<String, String>();
    // sss.put("p_id", pid);// 个人账号id
    // sss.put("status", "0");// 0 未处理 --1已处理 无需处理，只要我提交为0
    // sss.put("order_type", order_type);// 商品类型 1 :宽带入网 2：入网选号
    // // 3：商品购物'
    // sss.put("pay_status", "0");// 支付状态
    // sss.put("pay_type", pay_type);// 支付类型
    //
    // String resString = HttpUtil.postRequest(url, sss);
    // JSONObject jsonObject = new JSONObject(resString);
    // if (jsonObject != null && !jsonObject.equals("")) {
    //
    // orderInfoId = jsonObject.getJSONObject("obj").getString("id");
    // } else {
    // Log.e("生成主表订单", "=====失败");
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // Log.d("订单id", "orderInfoId*****" + orderInfoId);
    // }
    //

    /**
     * 
     */

}
