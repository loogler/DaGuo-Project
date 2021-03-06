package com.daguo.util.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.daguo.R;
import com.daguo.modem.choujiang.ChouJiangAty;
import com.daguo.modem.choujiang.LotteryAty;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.beans.Awards;
import com.daguo.utils.HttpUtil;

@SuppressLint("WorldReadableFiles")
public class PayDemoActivity extends FragmentActivity {

    public static final String PARTNER = "2088021208285835";
    public static final String SELLER = "664059889@qq.com";
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALTJPjrNtrzOqSCrTvcF7CaLWAjKge4LYk9ZK+eXEvE/itiPNUYmI0WP+v14FloOBZlCtEAT8cHVTXwICCtlwdf3KXYcXP5S6VyXY4epPE7HS/v8Mv1tLZfgfr9j5fqJeR6XbHEKg5CoAY/LgMKXNZSFFOwU8HJ5GfYq1z9ramIPAgMBAAECgYEAq/3s6yq8mCGQO5letZn30a+toCdwtxQzgkPWhixvA/8sy9xqlYNA+TQCV9RAh4phfy47p96RmJOidMZ7ZzYW1BDL1/EcZlzs8kwz5gtTEuG7FrnDmv0QMzwGrpQFNF0QYaM2YG612Xsr37pl0b8FEmg2dsLxsC1ATkxuAs/CrSECQQDwxZN2QE66yJah7aJRji6xvJ/BLHEX2rjc0oVdsteJcp1/paDVMh3bN6+8AGR+wB4oZAnGvkxsKtrlnwPGXUkxAkEAwDhqw7CQEl9PQ6gvJT8mUVvWWoInX8sEcWdgSM6PhcbcELfuSbLjq+55z0llnCtWIBWA+AXqQZpPDiqNEwOPPwJAFaVtsH/vlBNcl4pJwZNK12fh5Pgd4ssjO5chrl7Zyd2oE0XMw94RmRfUkM5oOo0DKRZ1WozZShBARcyaBkd0wQJBAI7DmDA9m9O5bCKg8FsmmuwEXKEeV+Yi3+rmT7HjkN9YAix8n4OnVMdmuJvHNKuX2EC+F+qeYg1VcW/hFKRRmXsCQQC63LVVG3PfHjaYmY7BH4HbbD+7srUlOasZI9wwWYxf1cb8lrwp9+bao3Xyr2eeOkK8hGOSyEjuRRXjlMfnV131";
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC0yT46zba8zqkgq073Bewmi1gIyoHuC2JPWSvnlxLxP4rYjzVGJiNFj/r9eBZaDgWZQrRAE/HB1U18CAgrZcHX9yl2HFz+Uulcl2OHqTxOx0v7/DL9bS2X4H6/Y+X6iXkel2xxCoOQqAGPy4DClzWUhRTsFPByeRn2Ktc/a2piDwIDAQAB";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private final int MSG_UPDATE_SUC = 10001;
    private final int MSG_UPDATE_FAIL = 10002;
    private final int MSG_SUC = 10003;
    private String pay_num;
    private String orderId;
    private String p_id;

    private String price, name, count, priceTotal;
    private TextView priceButton, nameButton, numButton, priceTotalButton;

    /**
     * 此处初始化抽奖数据。
     */
    private List<Awards> lists = new ArrayList<Awards>();

    /**
     * tools
     */
    Message msg;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case SDK_PAY_FLAG: {
		Result resultObj = new Result((String) msg.obj);
		String resultStatus = resultObj.resultStatus;

		// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
		if (TextUtils.equals(resultStatus, "9000")) {
		    Toast.makeText(PayDemoActivity.this, "支付成功",
			    Toast.LENGTH_SHORT).show();

		    // 更新订单状态
		    update();

		} else {
		    // 判断resultStatus 为非“9000”则代表可能支付失败
		    // “8000”
		    // 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
		    if (TextUtils.equals(resultStatus, "8000")) {
			Toast.makeText(PayDemoActivity.this, "支付结果确认中",
				Toast.LENGTH_SHORT).show();

		    } else {
			Toast.makeText(PayDemoActivity.this, "支付失败",
				Toast.LENGTH_SHORT).show();

		    }
		}
		break;
	    }
	    case SDK_CHECK_FLAG: {
		Toast.makeText(PayDemoActivity.this, "检查结果为：" + msg.obj,
			Toast.LENGTH_SHORT).show();
		break;
	    }

	    case MSG_UPDATE_SUC:

		break;
	    case MSG_UPDATE_FAIL:
		Toast.makeText(PayDemoActivity.this, "订单更新失败！请联系管理员处理订单！",
			Toast.LENGTH_LONG).show();

		break;

	    default:
		break;
	    }
	};
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.pay_main);
	MyAppliation.getInstance().addActivity(this);
	p_id = getSharedPreferences("userinfo", Activity.MODE_WORLD_READABLE)
		.getString("id", "");
	if ("".equals(p_id)) {
	    return;
	}

	getDataFromActivity();

	priceButton = (TextView) findViewById(R.id.product_price);
	nameButton = (TextView) findViewById(R.id.product_subject);
	numButton = (TextView) findViewById(R.id.product_info);
	priceTotalButton = (TextView) findViewById(R.id.product_priceTotal);

	priceButton.setText(price);
	nameButton.setText(name);
	numButton.setText(count);
	priceTotalButton.setText(priceTotal);

	if (price == null || price.equals("")) {
	    new AlertDialog.Builder(PayDemoActivity.this).setMessage(
		    "支付价格为空，订单异常").setPositiveButton("确定",
		    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			    MyAppliation.getInstance().exit();
			}
		    });
	} else if (name == null || name.equals("")) {
	    new AlertDialog.Builder(PayDemoActivity.this).setMessage(
		    "商品名称不存在，订单异常").setPositiveButton("确定",
		    new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
			    MyAppliation.getInstance().exit();
			}
		    });
	}

	initData();

    }

    private void getDataFromActivity() {
	Intent intent = getIntent();
	orderId = intent.getStringExtra("orderId");
	price = intent.getStringExtra("price");
	priceTotal = intent.getStringExtra("price_total");
	name = intent.getStringExtra("name");
	count = intent.getStringExtra("count");

    }

    private void update() {
	if (lists.size() > 0) {

	    new Thread(new Runnable() {
		public void run() {
		    try {
			String url = HttpUtil.SUBMIT_ORDER_PUB;
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", orderId);
			map.put("pay_num", pay_num);
			map.put("pay_status", "1");

			String res = HttpUtil.postRequest(url, map);
			JSONObject jsonObject = new JSONObject(res);
			if ("1".equals(jsonObject.getString("result"))) {
			    // 成功

			    Intent intent = new Intent(PayDemoActivity.this,
				    LotteryAty.class);

			    List<String> aList = new ArrayList<String>();
			    for (int i = 0; i < lists.size(); i++) {
				String aString = lists.get(i).getName();
				aList.add(aString);
			    }
			    String[] names = aList.toArray(new String[aList
				    .size()]);
			    List<String> bList = new ArrayList<String>();
			    for (int j = 0; j < lists.size(); j++) {
				String bString = lists.get(j).getId();
				bList.add(bString);
			    }

			    String[] ids = bList.toArray(new String[bList
				    .size()]);
			    intent.putExtra("names", names);
			    intent.putExtra("ids", ids);
			    intent.putExtra("orderId", orderId);
			    startActivity(intent);
			} else {
			    // 失败
			    msg = mHandler.obtainMessage(MSG_UPDATE_FAIL);
			    msg.sendToTarget();

			}

		    } catch (JSONException ex) {
			Log.e("支付跳转", "Json异常崩溃");

		    } catch (Exception e) {
			Log.e("支付跳转", "异常崩溃");
		    }
		}
	    }).start();

	} else {
	    Toast.makeText(PayDemoActivity.this, "信息初始化异常，请检查网络",
		    Toast.LENGTH_SHORT).show();
	    initData();

	}
    }

    private void initData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = "http://115.29.224.248:8080/XYYYT/service/gift/queryGiftList?android=1&page=1&rows=6";
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    JSONArray array = jsonObject.getJSONArray("rows");

		    for (int i = 0; i < 6; i++) {
			String name = array.optJSONObject(i).getString("name");
			String id = array.optJSONObject(i).getString("id");
			String type = array.optJSONObject(i).getString("type");
			Awards list = new Awards();
			list.setId(id);
			list.setName(name);
			list.setType(type);
			lists.add(list);

		    }

		    mHandler.sendEmptyMessage(MSG_SUC);
		} catch (Exception e) {
		}
	    }
	}).start();
    }

    /**
     * 更新支付状态
     * 
     * @author Bugs_Rabbit 時間： 2015-8-9 下午2:25:27
     */
    // class Submit_Pay_statusThread extends Thread {
    // @Override
    // public void run() {
    // super.run();
    // try {
    // String url = HttpUtil.SUBMIT_ORDER_PUB;
    // Map<String, String> map = new HashMap<String, String>();
    // map.put("pay_status", "1");
    // map.put("pay_num", pay_num);
    // map.put("id", BroadBandOrderAty.orderInfoId_unPay);
    // String reString = HttpUtil.postRequest(url, map);
    // if (reString != null && !reString.equals("")) {
    // // 更改成功
    // Log.d("支付状态更改 ", "=========成功！");
    // }
    // } catch (Exception e) {
    // }
    // }
    // }

    /**
     * call alipay sdk pay. 调用SDK支付
     * 
     */
    public void pay(View v) {
	// String name = BroadBandAty.orderinfos;
	// String price = BroadBandAty.orderprice;
	// String detail = BroadBandAty.ordername + " 月";

	String orderInfo = getOrderInfo(name, count, priceTotal);
	String sign = sign(orderInfo);
	try {
	    // 仅需对sign 做URL编码
	    sign = URLEncoder.encode(sign, "UTF-8");
	} catch (UnsupportedEncodingException e) {
	    e.printStackTrace();
	}
	final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
		+ getSignType();

	Runnable payRunnable = new Runnable() {

	    @Override
	    public void run() {
		// 构造PayTask 对象
		PayTask alipay = new PayTask(PayDemoActivity.this);
		// 调用支付接口
		String result = alipay.pay(payInfo);

		Message msg = new Message();
		msg.what = SDK_PAY_FLAG;
		msg.obj = result;
		mHandler.sendMessage(msg);
	    }
	};

	Thread payThread = new Thread(payRunnable);
	payThread.start();
    }

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     * 
     */
    public void check(View v) {
	Runnable checkRunnable = new Runnable() {

	    @Override
	    public void run() {
		PayTask payTask = new PayTask(PayDemoActivity.this);
		boolean isExist = payTask.checkAccountIfExist();

		Message msg = new Message();
		msg.what = SDK_CHECK_FLAG;
		msg.obj = isExist;
		mHandler.sendMessage(msg);
	    }
	};

	Thread checkThread = new Thread(checkRunnable);
	checkThread.start();

    }

    /**
     * get the sdk version. 获取SDK版本号
     * 
     */
    public void getSDKVersion() {
	PayTask payTask = new PayTask(this);
	String version = payTask.getVersion();
	Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }

    /**
     * create the order info. 创建订单信息
     * 
     */

    public String getOrderInfo(String subject, String body, String price) {

	// 合作者身份ID
	String orderInfo = "partner=" + "\"" + PARTNER + "\"";

	// 卖家支付宝账号
	orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

	// 商户网站唯一订单号
	orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

	// 商品名称
	orderInfo += "&subject=" + "\"" + subject + "\"";

	// 商品详情
	orderInfo += "&body=" + "\"" + body + "\"";

	// 商品金额
	orderInfo += "&total_fee=" + "\"" + price + "\"";

	// 服务器异步通知页面路径
	orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm"
		+ "\"";

	// 接口名称， 固定值
	orderInfo += "&service=\"mobile.securitypay.pay\"";

	// 支付类型， 固定值
	orderInfo += "&payment_type=\"1\"";

	// 参数编码， 固定值
	orderInfo += "&_input_charset=\"utf-8\"";

	// 设置未付款交易的超时时间
	// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
	// 取值范围：1m～15d。
	// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
	// 该参数数值不接受小数点，如1.5h，可转换为90m。
	orderInfo += "&it_b_pay=\"30m\"";

	// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
	orderInfo += "&return_url=\"m.alipay.com\"";

	// 调用银行卡支付，需配置此参数，参与签名， 固定值
	// orderInfo += "&paymethod=\"expressGateway\"";

	return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 获取外部订单号
     * 
     */
    public String getOutTradeNo() {
	SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
		Locale.getDefault());
	Date date = new Date();
	String key = format.format(date);

	Random r = new Random();
	key = key + r.nextInt();
	key = key.substring(0, 15);
	pay_num = key;
	return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     * 
     * @param content
     *            待签名订单信息
     */
    public String sign(String content) {
	return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     * 
     */
    public String getSignType() {
	return "sign_type=\"RSA\"";
    }

}
