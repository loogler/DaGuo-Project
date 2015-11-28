package com.daguo.ui.before;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.utils.HttpUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    /**
     * 学校信息
     */
    private String[] schoolNames;
    Map<String, String> maps = new HashMap<String, String>();
    private List<String> schooList = new ArrayList<String>();
    /**
     * 填写信息
     */
    public static  String schoolName, department,schoolId;

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
	initHeadView(view);
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
		infoCheck();
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
		schoolId=maps.get(schoolName);
		activity = (MainRegisterAty1) getActivity();
		FragmentTransaction ft = activity.getSupportFragmentManager()
			.beginTransaction();
		ft.replace(R.id.framelayout, activity.step2Fragment);
		ft.commit();

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
