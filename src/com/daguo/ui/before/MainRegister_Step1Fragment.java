package com.daguo.ui.before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-24 上午11:51:19
 * @function ：注册第一步，输入学校。
 */
@SuppressLint("InflateParams")
public class MainRegister_Step1Fragment extends Fragment {
	/*
	 * initViews
	 */
	private Button submit_btn;
	private EditText department_edt;
	private AutoCompleteTextView schoolName_auto;

	private CheckBox agreement_ckb;
	private TextView agreement_tv;

	/**
	 * 学校信息
	 */
	private String[] schoolNames;
	Map<String, String> maps = new HashMap<String, String>();
	private List<String> schooList = new ArrayList<String>();
	/**
	 * 填写信息
	 */
	public static String schoolName, department, schoolId;

	/**
	 * tools
	 */
	private MainRegisterAty1 activity;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initData();
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
				R.layout.fragment_mainregister_step1, null);
		initTitleView(view);
		initViews(view);
		return view;

	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// list转成数组

				schoolNames = schooList.toArray(new String[schooList.size()]);
				Log.i("学校名称", schoolNames + "");

				schoolName_auto.setAdapter(new ArrayAdapter<String>(
						getActivity(),
						android.R.layout.simple_dropdown_item_1line,
						schoolNames));

				break;

			default:
				break;
			}
		};
	};

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
	 * 初始化控件
	 */
	private void initViews(View view) {
		schoolName_auto = (AutoCompleteTextView) view
				.findViewById(R.id.schoolName_auto);
		submit_btn = (Button) view.findViewById(R.id.submit_btn);
		department_edt = (EditText) view.findViewById(R.id.department_edt);

		submit_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (agreement_ckb.isChecked()) {
					infoCheck();

				} else {
					Toast.makeText(getActivity(), "请先阅读并同意用户协议",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		agreement_ckb = (CheckBox) view.findViewById(R.id.agreement_ckb);
		agreement_tv = (TextView) view.findViewById(R.id.agreement_tv);
		agreement_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(),
						UserAgreementAty.class);
				intent.putExtra("type", "user");
				startActivity(intent);
			}
		});

	}

	/**
	 * 检查输入信息
	 */
	private void infoCheck() {
		schoolName = schoolName_auto.getText().toString().trim();
		department = department_edt.getText().toString().trim();
		if (schoolName != null && !schoolName.equals("")
				&& !schoolName.equals("null")) {

			if (department != null && !department.equals("")
					&& !department.equals("null")) {

				// infoCheck success
				//

				schoolId = maps.get(schoolName);
				activity = (MainRegisterAty1) getActivity();
				if (schoolId != null && !schoolId.isEmpty()) {

					FragmentTransaction ft = activity
							.getSupportFragmentManager().beginTransaction();
					ft.replace(R.id.framelayout, activity.step2Fragment);
					ft.commit();
				} else {
					Toast.makeText(activity, "学校信息未找到，请确认学校！",
							Toast.LENGTH_LONG).show();
				}

			} else {

				Toast.makeText(getActivity(), "专业名称不符", Toast.LENGTH_LONG)
						.show();
			}

		} else {
			Toast.makeText(getActivity(), "学校名称不符", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 初始化学校信息
	 */
	private void initData() {
		new Thread(new Runnable() {
			public void run() {

				try {
					String urlString = HttpUtil.DICT_SCHOOLNAME;
					String reString = HttpUtil.getRequest(urlString);
					JSONObject jsObject = new JSONObject(reString);
					JSONArray array = jsObject.getJSONArray("rows");
					for (int i = 0; i < array.length(); i++) {
						String name = array.optJSONObject(i).getString("name");
						String id = array.optJSONObject(i).getString("id");

						schooList.add(name);
						maps.put(name, id);

					}

				} catch (Exception e) {
				}
				Message msg = handler.obtainMessage(0);
				msg.sendToTarget();
			}
		}).start();
	}
}
