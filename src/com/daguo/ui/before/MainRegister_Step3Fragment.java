/**
 * 互相学习 共同进步
 */
package com.daguo.ui.before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.daguo.util.message.xioo;
import com.daguo.utils.HttpUtil;
import com.daguo.view.dialog.CustomProgressDialog;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-25 上午9:04:38
 * @function ：
 */
public class MainRegister_Step3Fragment extends Fragment {

    /**
     * initViews
     */
    private TextView timer_tv, prompt_tv;
    private EditText verification_edt;
    private Button submit_btn;

    /**
     * data
     */
    private String telNumber, schoolName, schoolId, department;
    private int verificationCode;

    /**
     * 倒计时
     */
    private int state = 75;
    /**
     * 倒计时线程
     */
    private Thread tt;
    private boolean threadstop1 = true;
    private Thread timerThread;

    /**
     * tools
     */
    CustomProgressDialog dialog;

    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case -1:
		timer_tv.setText("点此重发");
		timer_tv.setClickable(true);
		break;
	    case 0:
		timer_tv.setBackgroundColor(getActivity().getResources()
			.getColor(R.color.orange));
		timer_tv.setText("点此重发");
		timer_tv.setTextColor(getActivity().getResources().getColor(
			R.color.white));
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
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	telNumber = MainRegister_Step2Fragment.telNumber;
	schoolId = MainRegister_Step1Fragment.schoolId;
	schoolName = MainRegister_Step1Fragment.schoolName;
	department = MainRegister_Step1Fragment.department;
	verificationCode = MainRegister_Step2Fragment.verificationCode;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	View view = LayoutInflater.from(getActivity()).inflate(
		R.layout.fragment_mainregister_step3, null);
	initHeadView(view);
	initViews(view);
	initData();

	return view;
    }

    /**
     * 通用的headview 不同位置会出现不同的页面要求，根据情况设置
     */
    private void initHeadView(View view) {
	TextView back_tView = (TextView) view.findViewById(R.id.back_tv);
	TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
	TextView function_tv = (TextView) view.findViewById(R.id.function_tv);
	ImageView remind_iv = (ImageView) view.findViewById(R.id.remind_iv);

	back_tView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		getActivity().finish();
	    }
	});
	title_tv.setText("注册");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 
     * @param view
     */
    private void initViews(View view) {
	prompt_tv = (TextView) view.findViewById(R.id.prompt_tv);
	timer_tv = (TextView) view.findViewById(R.id.timer_tv);
	submit_btn = (Button) view.findViewById(R.id.submit_btn);
	verification_edt = (EditText) view.findViewById(R.id.verification_edt);

	prompt_tv.setText("验证码已发送至您的手机： " + telNumber);
	timer_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		timer_tv.setBackgroundColor(getActivity().getResources()
			.getColor(R.color.grey_1));
		timer_tv.setTextColor(getActivity().getResources().getColor(
			R.color.black));
		timer_tv.setClickable(false);
		threadstop1 = true;
		initData();
		sendSMS();
	    }
	});
	submit_btn.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View v) {
		register();
	    }
	});

    }

    /**
     * 发送短消息验证码
     */
    private void sendSMS() {

	new Thread(new Runnable() {
	    public void run() {

		// 未被注册
		verificationCode = (int) ((Math.random() * 9 + 1) * 100000);
		String duanxin = "短信验证码为： " + verificationCode
			+ " ，请勿将验证码提供给他人!";
		getActivity().runOnUiThread(new Runnable() {
		    public void run() {

			Toast.makeText(getActivity(), "短信已发送，请勿将验证码提供给他人！",
				Toast.LENGTH_LONG).show();
		    }
		});
		try {
		    xioo.main(duanxin, telNumber);

		} catch (IOException e1) {
		    e1.printStackTrace();
		    Log.e("短信服务器异常", "服务器异常");
		    getActivity().runOnUiThread(new Runnable() {
			public void run() {

			    Toast.makeText(getActivity(), "短信服务器异常，请重试！",
				    Toast.LENGTH_LONG).show();
			}
		    });

		}

	    }
	}).start();
    }

    /****************************** 注册事件 *****************************************************/
    /**
     * 注册事件主方法
     */
    private void register() {
	// 1 获取用户信息 2 提交至服务器 3本地数据更新、
	getUserInfo();
    }

    /**
     * 获取用户信息
     */
    private void getUserInfo() {
	String ve = verification_edt.getText().toString().trim();
	if (ve != null && !ve.equals("")) {
	    int abc = 0;
	    try {
		abc = Integer.parseInt(ve);
	    } catch (Exception e) {
		Log.e("注册", "验证码转换错误");
		Toast.makeText(getActivity(), "验证码有误", Toast.LENGTH_LONG)
			.show();
	    }
	    if (verificationCode == abc) {
		// 注册条件符合 开始注册

		dialog = CustomProgressDialog.createDialog(getActivity(),
			"正在提交个人资料");
		dialog.show();
		submit();
	    } else {
		Toast.makeText(getActivity(), "验证码错误", Toast.LENGTH_LONG)
			.show();

	    }

	} else {

	    Toast.makeText(getActivity(), "请输入验证码", Toast.LENGTH_LONG).show();
	}

    }

    /**
     * 提交信息至服务器
     */
    private void submit() {
	new Thread(new Runnable() {
	    public void run() {

		String url = HttpUtil.SUBMIT_USERINFO;
		Map<String, String> map = new HashMap<String, String>();
		map.put("tel", telNumber);
		map.put("school_id", schoolId);
		map.put("pro_name", department);
		map.put("school_name", schoolName);

		try {
		    String res = HttpUtil.postRequest(url, map);
		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("result") == 1) {

			// 提交成功
			JSONObject js = jsonObject.getJSONObject("obj");
			String id = js.getString("id");

			SharedPreferences sharedPreferences = getActivity()
				.getSharedPreferences("userinfo",
					Context.MODE_WORLD_READABLE);
			Editor editor = sharedPreferences.edit();
			editor.putString("tel", telNumber);
			editor.putString("school_id", schoolId);
			editor.putString("pro_name", department);
			editor.putString("school_name", schoolName);
			editor.putString("id", id);
			editor.commit();

			Intent intent = new Intent(getActivity(),
				MainActivity.class);
			startActivity(intent);
			dialog.dismiss();
			MyAppliation.getInstance().exit();
			getActivity().finish();

		    } else {
			// 注册失败
			getActivity().runOnUiThread(new Runnable() {
			    public void run() {
				dialog.dismiss();
				Toast.makeText(getActivity(),
					"注册失败，请重试或者联系管理员", Toast.LENGTH_LONG)
					.show();
			    }
			});
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    Log.e("注册执行失败", "");
		}
		// 如果注册成功，则执行如下代码，不成继续注册。

	    }
	}).start();
    }

    /**
     * 保存信息到本地 shared
     */
    private void saveToLocal() {

    }

    /*************************************************************************************/

    /**
     * 初始化界面倒计时事件
     */
    private void initData() {
	timerThread = new Thread(new TimerRunnable());
	timerThread.start();

    }

    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-11-25 下午3:03:17
     * @function ：倒计时线程runnable
     */
    class TimerRunnable implements Runnable {
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

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onDestroy()
     */
    @Override
    public void onDestroy() {
	while (!timerThread.isInterrupted()) {
	    timerThread.interrupt();
	}
	// 静态变量置空
	MainRegister_Step1Fragment.department = null;
	MainRegister_Step1Fragment.schoolId = null;
	MainRegister_Step1Fragment.schoolName = null;
	MainRegister_Step2Fragment.telNumber = null;
	MainRegister_Step2Fragment.verificationCode = 0;
	super.onDestroy();

    }

}
