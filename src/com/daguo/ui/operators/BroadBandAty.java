package com.daguo.ui.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.BroadBandAdapter;
import com.daguo.util.beans.BroadBand;
import com.daguo.utils.HttpUtil;
import com.daguo.view.dialog.CustomProgressDialog;

@SuppressLint("HandlerLeak")
public class BroadBandAty extends Activity implements OnItemClickListener,
		OnClickListener {
	private static final String tag = "BroadBandAty";
	private Button startBtn;
	private AutoCompleteTextView autoText;
	private ListView listView;
	private TextView tv_show;
	private Button yidong, liantong, dianxin;
	private int num;
	public static String broadbandid, broadbandname, broadbandbusi_name,
			broadbandprice, broadbandcost_info, broadbandschoolid,
			broadbandmonth;

	private List<BroadBand> broadBands = new ArrayList<BroadBand>();
	private BroadBandAdapter adapter;

	private String searchText1, searchText2;

	Message message;
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_broadband);
		MyAppliation.getInstance().addActivity(this);
		startBtn = (Button) findViewById(R.id.startBtn);
		autoText = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		listView = (ListView) findViewById(R.id.detail_listview);
		listView.setOnItemClickListener(this);
		tv_show = (TextView) findViewById(R.id.tv_show);
		yidong = (Button) findViewById(R.id.yidongbtn);
		liantong = (Button) findViewById(R.id.liantongbtn);
		dianxin = (Button) findViewById(R.id.dianxinbtn);

		startBtn.setOnClickListener(this);
		yidong.setOnClickListener(this);
		dianxin.setOnClickListener(this);
		liantong.setOnClickListener(this);
		getText();

	}

	Handler handler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			startBtn.setClickable(true);
			runOnUiThread(new Runnable() {
				public void run() {
					dialog.dismiss();
				}
			});

			if (num == 0) {
				tv_show.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
			} else {
				tv_show.setVisibility(View.GONE);
				List<BroadBand> sssBands = (List<BroadBand>) message.obj;
				broadBands.addAll(sssBands);
				adapter = new BroadBandAdapter(getBaseContext(), broadBands);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
			}

		}
	};

	/**
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-6 下午4:28:33
	 */
	class GetInfoThread extends Thread {

		List<BroadBand> lists = new ArrayList<BroadBand>();
		BroadBand list = null;

		@Override
		public void run() {
			super.run();

			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("sname", searchText1);
				map.put("busi_name", searchText2);
				String url = HttpUtil.QUERY_BROADBAND;
				String res = HttpUtil.postRequest(url, map);
				if (res != null) {
					JSONObject jsonObject = new JSONObject(res);
					JSONArray array = jsonObject.getJSONArray("rows");

					for (int j = 0; j < array.length(); j++) {
						list = new BroadBand();
						String orderId = array.optJSONObject(j).getString("id");

						if (!orderId.equals("") && orderId != null) {
							list.setOrderId(orderId);
						} else {
							list.setOrderId("未知");
						}

						String busiName = array.optJSONObject(j).getString(
								"busi_name");
						if (!busiName.equals("") && busiName != null) {

							list.setBusiName(busiName);
						} else {
							list.setBusiName("");
						}
						Log.i(tag, busiName);
						String costInfo = array.optJSONObject(j).getString(
								"cost_info");
						if (!costInfo.equals("") && costInfo != null) {

							list.setCostInfo(costInfo + "");
						} else {
							list.setCostInfo("");
						}

						String month = array.optJSONObject(j)
								.getString("month");
						if (!month.equals("") && month != null) {

							list.setMonth(month + "  月");
						} else {
							list.setMonth("");
						}
						String name = array.optJSONObject(j).getString("name");
						if (!name.equals("") && name != null) {

							list.setDetailName(name);
						} else {
							list.setDetailName("");
						}
						String price = array.optJSONObject(j)
								.getString("price");
						if (!price.equals("") && price != null) {

							list.setPrice(price);
						} else {
							list.setPrice("");
						}
						String school_id = array.optJSONObject(j).getString(
								"school_id");
						if (!school_id.equals("") && school_id != null) {

							list.setSchool_id(school_id);
						} else {
							list.setSchool_id("");
						}
						Log.i(tag, "==============" + costInfo + name + month
								+ price);
						String sName = array.optJSONObject(j)
								.getString("sname");
						if (!sName.equals("") && sName != null
								&& !busiName.equals("") && busiName != null) {

							lists.add(list);
							num = lists.size();
							if (sName.equals(searchText1)
									|| sName.contains(searchText1)) {

								list.setsName(sName);

							}

						} else {
							sName = "无效校区";
						}
						Log.i(tag, "==========>sname==" + sName);

					}
					message = handler2.obtainMessage();
					message.obj = lists;
					message.sendToTarget();
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(BroadBandAty.this, "服务器异常，请稍后",
									Toast.LENGTH_SHORT).show();
							startBtn.setClickable(true);
							dialog.dismiss();
						}
					});

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View convertView,
			int position, long ids) {
		// Toast.makeText(getBaseContext(),
		// "tusi" + position + broadBands.get(position).getsName(),
		// Toast.LENGTH_SHORT).show();
		
		broadbandid = broadBands.get(position).getOrderId();
		broadbandbusi_name = broadBands.get(position).getBusiName();
		broadbandcost_info = broadBands.get(position).getCostInfo();
		broadbandmonth = broadBands.get(position).getMonth();
		broadbandname = broadBands.get(position).getDetailName();
		broadbandprice = broadBands.get(position).getPrice();
		broadbandschoolid = broadBands.get(position).getSchool_id();
		//跳转
		Intent intent = new Intent(BroadBandAty.this, BroadBandOrderAty.class);
		startActivity(intent);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.startBtn:

			runOnUiThread(new Runnable() {
				public void run() {
					dialog = CustomProgressDialog.createDialog(
							BroadBandAty.this, "请耐心等待加载。。。");
					dialog.show();

				}
			});
			startBtn.setClickable(false);
			broadBands.clear();
			searchText1 = autoText.getText().toString().trim();
			if (!searchText1.equals("") && searchText1 != null) {

				new GetInfoThread().start();

			} else {
				Toast.makeText(BroadBandAty.this, "搜索信息不完整", Toast.LENGTH_SHORT)
						.show();
				startBtn.setClickable(true);
				dialog.dismiss();

			}

			break;
		case R.id.yidongbtn:
			reSetBtn();
			yidong.setBackgroundResource(R.drawable.yuanjiao_choice);
			searchText2 = "b05bab2f-e7b3-41f7-b22f-acc01c0ef0f5";
			break;
		case R.id.liantongbtn:
			reSetBtn();
			liantong.setBackgroundResource(R.drawable.yuanjiao_choice);
			searchText2 = "065017a4-1e05-41dd-88b5-aa02f99de3d6";
			break;
		case R.id.dianxinbtn:
			reSetBtn();
			dianxin.setBackgroundResource(R.drawable.yuanjiao_choice);
			searchText2 = "5e5aa8f1-8c65-42bf-bebe-6fb6adbbee05";
			break;

		default:
			break;
		}

	}

	void reSetBtn() {
		yidong.setBackgroundResource(R.drawable.yuanjiao);
		dianxin.setBackgroundResource(R.drawable.yuanjiao);
		liantong.setBackgroundResource(R.drawable.yuanjiao);
	}

	private String[] schoolName;
	Map<String, String> maps = new HashMap<String, String>();
	private List<String> schooList = new ArrayList<String>();

	void getText() {

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
		// list转成数组

		schoolName = schooList.toArray(new String[schooList.size()]);
		Log.i("学校名称", schoolName + "");

		runOnUiThread(new Runnable() {
			public void run() {
				autoText.setAdapter(new ArrayAdapter<String>(BroadBandAty.this,
						android.R.layout.simple_dropdown_item_1line, schoolName));
			}
		});
	}

}
