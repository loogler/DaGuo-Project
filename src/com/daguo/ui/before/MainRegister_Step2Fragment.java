package com.daguo.ui.before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.message.xioo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.TelNumberCheckUtil;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-24 下午4:50:13
 * @function ： 注册的第二个界面
 */
public class MainRegister_Step2Fragment extends Fragment {

	/**
	 * initView
	 */
	private EditText tel_edt, guid_edt;
	private Button submit_btn;

	/**
	 * data
	 */
	public static String telNumber, guidNumber;
	public static int verificationCode;

	/**
	 * tools
	 */
	private CustomProgressDialog dialog;
	MainRegisterAty1 activity;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				dialog.dismiss();
				dialog = null;
				activity = (MainRegisterAty1) getActivity();
				FragmentTransaction ft = activity.getSupportFragmentManager()
						.beginTransaction();
				ft.replace(R.id.framelayout, activity.step3Fragment);
				ft.commit();

				break;
			case 1:
				new AlertDialog.Builder(getActivity())
						.setMessage("号码已被注册，请直接登录！")
						.setPositiveButton("确定", new OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Intent intent = new Intent(getActivity(),
										MainLoginAty1.class);
								startActivity(intent);
								getActivity().finish();
							}
						}).setNegativeButton("取消", null).create().show();
				break;

			default:
				break;
			}
		};
	};

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
				R.layout.fragment_mainregister_step2, null);
		initTitleView(view);
		initViews(view);

		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化通用标题栏
	 */
	private void initTitleView(View view) {
		TextView title_tv = (TextView) view.findViewById(R.id.title_tv);
		FrameLayout back_fram = (FrameLayout) view.findViewById(R.id.back_fram);
		LinearLayout message_ll = (LinearLayout) view
				.findViewById(R.id.message_ll);
		// TextView function_tv = (TextView) findViewById(R.id.function_tv);
		// ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

		title_tv.setText("注册");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				getActivity().finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/**
     * 
     */
	private void initViews(View view) {
		submit_btn = (Button) view.findViewById(R.id.submit_btn);
		tel_edt = (EditText) view.findViewById(R.id.tel_edt);
		guid_edt = (EditText) view.findViewById(R.id.guid_edt);

		submit_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog = CustomProgressDialog.createDialog(getActivity(),
						"加载中，请稍等。。。");
				dialog.show();
				telNumber = tel_edt.getText().toString().trim();
				if (infoCheck(telNumber)) {
					getVerificationCode(telNumber);
					guidNumber = guid_edt.getText().toString().trim();
				}

			}
		});

	}

	/**
	 * 检查信息完整性
	 * 
	 * @param res
	 * @return
	 */
	private boolean infoCheck(String res) {
		if (telNumber != null && !telNumber.equals("")) {
			if (TelNumberCheckUtil.isMobileNO(telNumber)) {
				// 执行注册准备事件，1检查是否被注册，2执行注册
				return true;

			} else {
				Toast.makeText(getActivity(), "号码格式错误", Toast.LENGTH_LONG)
						.show();
				return false;
			}

		} else {
			Toast.makeText(getActivity(), "号码不能为空", Toast.LENGTH_LONG).show();
			return false;

		}

	}

	/**
	 * 获得验证码的线程。
	 */
	private void getVerificationCode(final String tel) {
		new Thread(new Runnable() {
			public void run() {
				String url = HttpUtil.QUERY_USERINFO + "&page=1&rows=1";
				Map<String, String> map = new HashMap<String, String>();
				map.put("tel", tel);
				String res = "";
				int total = 100;

				try {
					res = HttpUtil.postRequest(url, map);
					total = new JSONObject(res).getInt("total");

					if (total == 0) {
						// 未被注册
						int haoma = (int) ((Math.random() * 9 + 1) * 100000);
						verificationCode = haoma;
						String duanxin = "短信验证码为： " + haoma + " ，请勿将验证码提供给他人!";
						getActivity().runOnUiThread(new Runnable() {
							public void run() {

								Toast.makeText(getActivity(),
										"短信已发送，请勿将验证码提供给他人！", Toast.LENGTH_LONG)
										.show();
							}
						});
						try {
							xioo.main(duanxin, telNumber);

							Message msg = handler.obtainMessage(0);

							msg.obj = total;
							msg.sendToTarget();

						} catch (IOException e1) {
							e1.printStackTrace();
							Log.e("短信服务器异常", "服务器异常");
							getActivity().runOnUiThread(new Runnable() {
								public void run() {

									Toast.makeText(getActivity(),
											"短信服务器异常，请重试！", Toast.LENGTH_LONG)
											.show();
								}
							});

						}

					} else {
						// 已注册
						Message msg = handler.obtainMessage(1);
						msg.sendToTarget();

					}

				} catch (JSONException e) {
					e.printStackTrace();
					Log.e("注册-获取号码是否注册", "json异常");
				} catch (Exception e) {
					e.printStackTrace();
					Log.e("注册-获取号码是否注册", "异常");
				}
			}
		}).start();
	}

}
