/**
 * 互相学习 共同进步
 */
package com.daguo.ui.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.Oper_BroadbandAdapter;
import com.daguo.util.beans.BroadBand;
import com.daguo.utils.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-29 下午3:44:13
 * @function ：宽带选择页 第二页
 */
public class Oper_BroadBand_SStepAty extends Activity {
    private final int MSG_BRO_SUC = 10001;

    /**
     * initViews
     */

    private ListView listView;
    private Oper_BroadbandAdapter adapter;
    private List<BroadBand> lists = new ArrayList<BroadBand>();
    private BroadBand list;

    private String schoolId, schoolName, busi_name;

    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_BRO_SUC:
		if (null != msg.obj) {
		    List<BroadBand> aaBands = (List<BroadBand>) msg.obj;
		    lists.addAll(aaBands);
		    adapter.notifyDataSetChanged();
		} else {

		}

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

	setContentView(R.layout.aty_oper_broadband_sstep);
	MyAppliation.getInstance().addActivity(this);

	schoolId = getIntent().getStringExtra("schoolId");
	schoolName = getIntent().getStringExtra("schoolName");
	busi_name = getIntent().getStringExtra("busi_name");

	initViews();
	loadData();
	adapter = new Oper_BroadbandAdapter(this, lists);
	listView.setAdapter(adapter);
	listView.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View v, int p,
		    long arg3) {
		Intent intent = new Intent(Oper_BroadBand_SStepAty.this,
			Oper_BroadBand_OrderAty.class);
		intent.putExtra("cost_name", lists.get(p).getDetailName());
		intent.putExtra("id", lists.get(p).getOrderId());
		intent.putExtra("price", lists.get(p).getPrice());

		startActivity(intent);
	    }
	});

    }

    /**
     * 
     */
    private void initViews() {
	initHeadView();
	listView = (ListView) findViewById(R.id.listView);

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
	title_tv.setText("套餐选择");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /** -------------- data ---------------------- */

    private void loadData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_BROADBAND + "&busi_name="
			    + busi_name;

		    String res = HttpUtil.getRequest(url);

		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("total") > 0) {
			List<BroadBand> abc = new ArrayList<BroadBand>();
			JSONArray array = jsonObject.getJSONArray("rows");

			for (int i = 0; i < array.length(); i++) {

			    String bro_name = array.optJSONObject(i).getString(
				    "name");
			    String bro_price = array.optJSONObject(i)
				    .getString("price");
			    String bro_school = array.optJSONObject(i)
				    .getString("school_name");
			    String bro_time = array.optJSONObject(i).getString(
				    "month");
			    String id = array.optJSONObject(i).getString("id");
			    String school_name = array.optJSONObject(i)
				    .getString("school_name");
			    if (schoolName.contains(school_name)) {
				list = new BroadBand();

				list.setOrderId(id);
				list.setDetailName(bro_name);
				list.setPrice(bro_price);
				list.setMonth(bro_time);
				list.setsName(bro_school);

				abc.add(list);
			    }
			}
			msg = handler.obtainMessage(MSG_BRO_SUC);
			msg.obj = abc;
			msg.sendToTarget();

		    } else {

			// 暂无数据
		    }

		} catch (Exception e) {
		}
	    }
	}).start();
    }
}
