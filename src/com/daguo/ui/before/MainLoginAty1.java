/**
 * 互相学习 共同进步
 */
package com.daguo.ui.before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.daguo.util.message.xioo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.TelNumberCheckUtil;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-26 上午10:44:34
 * @function ：登录页主界面
 */
public class MainLoginAty1 extends Activity {
    /**
     * initViews
     * 
     */
    private EditText telNumber_edt, verification_edt;
    private TextView timer_tv;
    private Button submit_btn;

    /**
     * data
     */
    private String telNumber;
    private int verificationCode;

    /**
     * tools
     */
    private Thread timerThread, infoCheckThread, submitThread;
    CustomProgressDialog dialog;

    /**
     * 倒计时
     */
    private int state = 75;
    /**
     * 倒计时线程
     */

    private boolean threadstop1 = true;

    Handler handler = new Handler() {
	public void handleMessage(Message msg) {

	    switch (msg.what) {
	    case -1:
		timer_tv.setText("点此重发");
		timer_tv.setClickable(true);
		break;
	    case 0:
		timer_tv.setBackgroundColor(getResources().getColor(
			R.color.orange));
		timer_tv.setText("点此重发");
		timer_tv.setTextColor(getResources().getColor(R.color.white));
		timer_tv.setClickable(true);
		state = 75;
		break;
	    default:

		timer_tv.setText("等待" + msg.what + "s");
		timer_tv.setClickable(false);
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
	setContentView(R.layout.aty_mainlogin1);
	dialog = CustomProgressDialog
		.createDialog(MainLoginAty1.this, "加载中。。。");
	initHeadView();
	initViews();
    }

    /**
     * 初始化界面，响应事件
     */
    private void initViews() {
	telNumber_edt = (EditText) findViewById(R.id.telNumber_edt);
	verification_edt = (EditText) findViewById(R.id.verification_edt);
	timer_tv = (TextView) findViewById(R.id.timer_tv);
	submit_btn = (Button) findViewById(R.id.submit_btn);

	timer_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		// 1处理数据 (判断号码，)2 判断是否已注册（判断，获取验证码，输入）
		dialog.show();
		if (judgeTel()) {
		    timerThread = new Thread(new TimerRunnable());
		    timerThread.start();
		    infoCheckThread = new Thread(new CheckInfoRunnable());
		    infoCheckThread.start();

		} else {
		    // 号码判断不通过
		}
	    }
	});

	submit_btn.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		//
		// 1 判断是否正确输入 2执行登录 3 保存返回数据到本地
		dialog.show();
		if (verificationCheck()) {

		    submitThread = new Thread(new SubmitRunnable());
		    submitThread.start();
		} else {
		    // 验证码错误
		}

	    }
	});
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
	title_tv.setText("登录");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /********************************* 处理登录事件 **************************************/
    /**
     * 
     * @param telNumbeer
     *            号码 号码判断
     */
    private boolean judgeTel() {
	telNumber = telNumber_edt.getText().toString().trim();
	if (telNumber != null && !telNumber.equals("")) {
	    if (TelNumberCheckUtil.isMobileNO(telNumber)) {// 检查号码是不是正确
		return true;
	    } else {

		Toast.makeText(MainLoginAty1.this, "手机号码有误！",
			Toast.LENGTH_SHORT).show();
		return false;
	    }

	} else {
	    Toast.makeText(MainLoginAty1.this, "手机号码未输入", Toast.LENGTH_LONG)
		    .show();
	    return false;
	}
    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-26 下午2:03:10
     * @function ：线程类 检查该号码是否已注册，如果尚未注册 先注册
     */
    class CheckInfoRunnable implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	    // 判断号码输入没有

	    String url = HttpUtil.QUERY_USERINFO;
	    Map<String, String> map = new HashMap<String, String>();
	    map.put("tel", telNumber);
	    String res;
	    try {
		res = HttpUtil.postRequest(url, map);
		int total = new JSONObject(res).getInt("total");
		if (total != 0) {

		    // 取到值 说明存在账号 获取验证码
		    verificationCode = (int) ((Math.random() * 9 + 1) * 100000);
		    String duanxin = "短信验证码为： " + verificationCode
			    + " ，请勿将验证码提供给他人";
		    try {
			xioo.main(duanxin, telNumber);
			runOnUiThread(new Runnable() {
			    public void run() {
				Toast.makeText(MainLoginAty1.this,
					"短信已发送，请勿将验证码提供给他人", Toast.LENGTH_LONG)
					.show();
			    }
			});
		    } catch (IOException e1) {
			e1.printStackTrace();
			Log.e("登录-获取验证码", "服务器异常或者联网失败");
		    }

		} else {
		    // 账号不存在
		    runOnUiThread(new Runnable() {
			public void run() {
			    Toast.makeText(MainLoginAty1.this, "该账号还未注册，请先注册！",
				    Toast.LENGTH_LONG).show();
			}
		    });
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	    dialog.dismiss();
	}

    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-26 下午2:04:14
     * @function ：倒计时线程类，用于倒计时
     */
    class TimerRunnable implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

	    while (threadstop1 && state > 0) {
		Message msg = new Message();
		msg.what = state;
		handler.sendMessage(msg);
		try {
		    Thread.sleep(1000);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		state = state - 1;
	    }
	    if (state == 0) {
		Message msg = new Message();
		msg.what = 0;
		handler.sendMessage(msg);
	    }
	    threadstop1 = false;

	}

    }

    private boolean verificationCheck() {
	String ver = verification_edt.getText().toString().trim();
	int v = Integer.parseInt(ver);
	if (verificationCode == v) {
	    return true;
	} else {
	    // 验证码不符
	    Toast.makeText(MainLoginAty1.this, "验证码错误", Toast.LENGTH_SHORT)
		    .show();
	    return false;
	}
    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-26 下午2:04:44
     * @function ：提交线程类， 用于用户登录线程
     */
    class SubmitRunnable implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
	    try {

		dialog.show();
		String url = HttpUtil.LOGIN;
		Map<String, String> map = new HashMap<String, String>();
		map.put("tel", telNumber);
		String reString = HttpUtil.postRequest(url, map);
		JSONObject jsonObject = new JSONObject(reString);
		JSONArray array = jsonObject.getJSONArray("rows");
		int l = array.length();
		if (l != 0) {
		    // 成功

		    String tel = array.optJSONObject(0).getString("tel");
		    String school_idString = array.optJSONObject(0).getString(
			    "school_id");
		    String id = array.optJSONObject(0).getString("id");
		    String pro_name = array.optJSONObject(0).getString(
			    "pro_name");
		    String birthday = array.optJSONObject(0).getString(
			    "birthday");
		    String sex = array.optJSONObject(0).getString("sex");
		    String head_info = array.optJSONObject(0).getString(
			    "head_info");
		    String school_name = array.optJSONObject(0).getString(
			    "school_name");
		    String start_year = array.optJSONObject(0).getString(
			    "start_year");
		    String score = array.optJSONObject(0).getString("score");
		    String id_card = array.optJSONObject(0)
			    .getString("id_card");
		    String id_card_copy = array.optJSONObject(0).getString(
			    "id_card_copy");
		    String address = array.optJSONObject(0)
			    .getString("address");
		    String name = array.optJSONObject(0).getString("name");
		    String stu_card_copy = array.optJSONObject(0).getString(
			    "stu_card_copy");

		    SharedPreferences sPreferences = getSharedPreferences(
			    "userinfo", Context.MODE_WORLD_READABLE);
		    Editor editor = sPreferences.edit();
		    editor.putString("tel", tel);// 电话号码
		    editor.putString("school_id", school_idString);// 学校id
		    editor.putString("pro_name", pro_name);// 专业名臣
		    editor.putString("birthday", birthday);// 生日
		    editor.putString("sex", sex);// 性别
		    editor.putString("head_info", head_info);// 头像路径
		    editor.putString("school_name", school_name);// 学校名称
		    editor.putString("start_year", start_year);// 学年
		    editor.putString("score", score);// 积分
		    editor.putString("id_card", id_card);// 学生证号 这里的身份证废弃现在成为学号
		    editor.putString("id_card_copy", id_card_copy);// 身份证路径
		    editor.putString("address", address);// 地址
		    editor.putString("name", name);// 名字
		    editor.putString("stu_card_copy", stu_card_copy);// 学生证路径
		    editor.putString("id", id);// 个人唯一id

		    editor.commit();

		    dialog.dismiss();
		    Intent intent = new Intent(MainLoginAty1.this,
			    MainActivity.class);
		    startActivity(intent);
		    MyAppliation.getInstance().exit();
		    MainLoginAty1.this.finish();

		} else {
		    // 失败
		    dialog.dismiss();
		    Toast.makeText(MainLoginAty1.this, "账号有误，请检查",
			    Toast.LENGTH_LONG).show();
		}

	    } catch (JSONException e) {
		Log.e("登录-登录事件", "json异常");

	    } catch (Exception e) {
		Log.e("登录-登录事件", " 登录的其他异常");
	    }

	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
	if (timerThread != null && infoCheckThread != null
		&& submitThread != null) {

	    timerThread.interrupt();
	    infoCheckThread.interrupt();
	    submitThread.interrupt();
	}
	super.onDestroy();
    }

}
