/**
 * 互相学习 共同进步
 */
package com.daguo.ui.operators.broadband;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-29 下午2:51:59
 * @function ： 宽带选择的界面第一页
 */
public class Oper_BroadBand_FStepAty extends Activity {

    /**
     * initViews
     */

    private TextView submit_tv;
    private AutoCompleteTextView school_auto;

    /**
     * data
     */
    private String schoolName, schoolId,busi_name;

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
			Oper_BroadBand_FStepAty.this,
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
	busi_name=getIntent().getStringExtra("busi_name");
	
	initHeadView();
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
		    Intent intent = new Intent(Oper_BroadBand_FStepAty.this,
			    Oper_BroadBand_SStepAty.class);
		    intent.putExtra("schoolName", schoolName);
		    intent.putExtra("schoolId", schoolId);
		    intent.putExtra("busi_name", busi_name);
		    startActivity(intent);

		} else {

		    // 学校没获得到
		    new AlertDialog.Builder(Oper_BroadBand_FStepAty.this)
			    .setMessage("没有该学校的宽带信息")
			    .setPositiveButton("确定", null).create().show();

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
	title_tv.setText("大果校园宽带办理");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

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
