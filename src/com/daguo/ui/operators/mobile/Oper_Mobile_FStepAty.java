package com.daguo.ui.operators.mobile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-26 上午9:22:36
 * @function 功能:移动选号第一步 选择学校
 */
public class Oper_Mobile_FStepAty extends Activity {

	/**
	 * initViews
	 */

	private TextView submit_tv;
	private AutoCompleteTextView school_auto;

	/**
	 * data
	 */
	private String schoolName, schoolId, busi_name;

	/**
	 * 学校信息
	 */
	private String[] schoolNames;
	Map<String, String> maps = new HashMap<String, String>();
	private List<String> schooList = new ArrayList<String>();

	/**
	 * tools
	 */
	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				schoolNames = schooList.toArray(new String[schooList.size()]);
				Log.i("学校名称", schoolNames + "");

				school_auto.setAdapter(new ArrayAdapter<String>(
						Oper_Mobile_FStepAty.this,
						android.R.layout.simple_dropdown_item_1line,
						schoolNames));
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
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_oper_broadband_fstep);

		MyAppliation.getInstance().addActivity(this);
		busi_name = getIntent().getStringExtra("busi_name");

		initTitleView();
		initViews();

		loadSchoolData();

	}

	/**
     * 
     */
	private void initViews() {
		submit_tv = (TextView) findViewById(R.id.submit_tv);
		school_auto = (AutoCompleteTextView) findViewById(R.id.school_auto);

		submit_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				schoolName = PublicTools.doWithNullData(school_auto.getText()
						.toString().trim());
				if (!"".equals(schoolName)) {
					schoolId = maps.get(schoolName);
					Intent intent = new Intent(Oper_Mobile_FStepAty.this,
							Oper_Mobile_SStepAty.class);
					intent.putExtra("schoolName", schoolName);
					intent.putExtra("schoolId", schoolId);
					intent.putExtra("busi_name", busi_name);
					startActivity(intent);

				} else {

					// 学校没获得到
					new AlertDialog.Builder(Oper_Mobile_FStepAty.this)
							.setMessage("没有该学校的号码信息")
							.setPositiveButton("确定", null).create().show();

				}

			}
		});
	}

	/**
	 * 初始化通用标题栏
	 */
	private void initTitleView() {
		TextView title_tv = (TextView) findViewById(R.id.title_tv);
		FrameLayout back_fram = (FrameLayout) findViewById(R.id.back_fram);
		LinearLayout message_ll = (LinearLayout) findViewById(R.id.message_ll);
		// TextView function_tv = (TextView) findViewById(R.id.function_tv);
		// ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

		title_tv.setText("大果校园选号办理");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/*-------------------- data thread   -----------------------------------------/
	  /**
	 * 
	 */
	private void loadSchoolData() {
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
