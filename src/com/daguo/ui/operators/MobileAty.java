package com.daguo.ui.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.NmAdapter;
import com.daguo.util.adapter.TelNumberAdapter;
import com.daguo.util.beans.TelNumber;
import com.daguo.utils.HttpUtil;
import com.daguo.view.dialog.CustomProgressDialog;

public class MobileAty extends Activity implements OnClickListener,
		OnItemClickListener {
	private Button startBtn;
	private Button yidong, liantong, dianxin;
	private TextView tv_show;
	private String searchText1, searchText2;
	private CustomProgressDialog dialog;
	Message message;
	public static String num_id, num_tel, num_price, num_bz, num_operator;
	Dialog dia;
	private List<TelNumber> lists = new ArrayList<TelNumber>();
	private ListView listView;
	TelNumberAdapter adapter;// 号码查询

	// data
	private Map<String, String> map = new HashMap<String, String>();
	private List<String> nameList = new ArrayList<String>();
	private ListView lv;
	private NmAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mobile);
		MyAppliation.getInstance().addActivity(this);
		lv = (ListView) findViewById(R.id.list);
		startBtn = (Button) findViewById(R.id.startBtn);
		listView = (ListView) findViewById(R.id.detail_listview);
		listView.setOnItemClickListener(this);
		adapter = new TelNumberAdapter(MobileAty.this, lists);
		listView.setAdapter(adapter);
		tv_show = (TextView) findViewById(R.id.tv_show);
		yidong = (Button) findViewById(R.id.yidongbtn);
		// liantong = (Button) findViewById(R.id.liantongbtn);
		// dianxin = (Button) findViewById(R.id.dianxinbtn);

		startBtn.setOnClickListener(this);
		yidong.setOnClickListener(this);
		// dianxin.setOnClickListener(this);
		// liantong.setOnClickListener(this);
		mAdapter = new NmAdapter(MobileAty.this, nameList);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				searchText1 = map.get(nameList.get(position));
				//设置选中不同背景色
//				mAdapter.setSelectItem(position);
//				mAdapter.notifyDataSetInvalidated();
				for (int i = 0; i < parent.getCount(); i++) {
					View v = parent.getChildAt(parent.getCount() - 1 - i);
					if (position == i) {
						v.setBackgroundColor(Color.RED);
					} else {
						v.setBackgroundColor(Color.TRANSPARENT);
					}
				}

			}
		});

		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.DICT_NUMBER;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					if (js != null && !js.equals("")) {// get success
						JSONArray array = js.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							String id = array.optJSONObject(i).getString("id");
							String name = array.optJSONObject(i).getString(
									"name");
							nameList.add(name);
							map.put(name, id);// get data
						}

					} else {
						// get failed
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				message = handler2.obtainMessage(2);
				message.obj = nameList;
				message.sendToTarget();

			}
		}).start();

	}

	Runnable queryNum = new Runnable() {

		@Override
		public void run() {
			List<TelNumber> infos = new ArrayList<TelNumber>();
			TelNumber info = null;
			try {
				searchText2 = "b05bab2f-e7b3-41f7-b22f-acc01c0ef0f5";
				String url = HttpUtil.QUERY_NUMBER;
				Map<String, String> map = new HashMap<String, String>();
				map.put("cost_name", searchText1);
				map.put("busi_name", searchText2);
				String res = HttpUtil.postRequest(url, map);
				JSONObject js = new JSONObject(res);
				JSONArray arr = js.getJSONArray("rows");
				if (arr.length() == 0) {
					info = new TelNumber();
					infos.add(info);
				} else {

					for (int i = 0; i < arr.length(); i++) {
						info = new TelNumber();

						String id = arr.optJSONObject(i).getString("id");
						String cost_name = arr.optJSONObject(i).getString(
								"cost_name");
						String busi_name = arr.optJSONObject(i).getString(
								"busi_name");
						String tel = arr.optJSONObject(i).getString("tel");
						String price = arr.optJSONObject(i).getString("price");
						String bz = arr.optJSONObject(i).getString("bz");

						info.setBusi_name(busi_name);
						info.setCost_name(cost_name);
						info.setGz(bz);
						info.setId(id);
						info.setPrice(price);
						info.setTel(tel);
						infos.add(info);
					}
				}

			} catch (Exception e) {
			}
			message = handler2.obtainMessage(1);
			message.obj = infos;
			message.sendToTarget();

			// Intent intent = new Intent(MobileAty.this, MoblieOrderAty.class);
			// startActivity(intent);

		}
	};
	Handler handler2 = new Handler() {
		public void handleMessage(Message msg) {
			switch (message.what) {
			case 1:
				startBtn.setClickable(true);
				runOnUiThread(new Runnable() {
					public void run() {
						dialog.dismiss();

					}
				});

				tv_show.setVisibility(View.GONE);
				if (msg.obj != null && !msg.obj.equals("")) {
					List<TelNumber> sssBands = (List<TelNumber>) message.obj;
					lists.addAll(sssBands);
					if (sssBands != null) {

						adapter.notifyDataSetChanged();

					}
				}
				break;
			case 2:
				if (msg.obj != null && !msg.equals("")) {
					
					mAdapter.notifyDataSetChanged();

				}

				break;

			default:
				break;
			}

		};
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		num_bz = lists.get(position).getGz();
		num_id = lists.get(position).getId();
		num_price = lists.get(position).getPrice();
		num_tel = lists.get(position).getTel();
		num_operator = lists.get(position).getBusi_name();

		dia = new Dialog(MobileAty.this);
		dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dia.show();

		final Window wd = dia.getWindow();
		wd.setContentView(R.layout.dialog_mobile_desc);
		TextView tel = (TextView) wd.findViewById(R.id.tel);
		TextView price = (TextView) wd.findViewById(R.id.price);
		TextView operator = (TextView) wd.findViewById(R.id.operator);
		TextView desc = (TextView) wd.findViewById(R.id.desc);
		TextView pay_btn = (TextView) wd.findViewById(R.id.pay_btn);

		tel.setText(num_tel);
		price.setText(num_price);
		operator.setText(num_operator);
		desc.setText(num_bz);
		pay_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (num_tel != null && !num_tel.equals("")) {

					Intent intent = new Intent(MobileAty.this,
							MoblieOrderAty.class);
					startActivity(intent);
					dia.dismiss();

				} else {
					dia.dismiss();
				}
			}
		});

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		// case R.id.yd20a:
		// resetBTN();
		//
		// break;
		// case R.id.yd40a:
		// resetBTN();
		// break;

		case R.id.yidongbtn:
			// reSetBtn();
			yidong.setBackgroundResource(R.drawable.yuanjiao_choice);

			break;
		// case R.id.liantongbtn:
		// reSetBtn();
		// liantong.setBackgroundResource(R.drawable.yuanjiao_choice);
		// searchText2 = "55151c7a-1202-4618-9eb7-9a763135453a";
		// searchText1 = "联通";
		// // Toast.makeText(MobileAty.this, "运营商正在筹备中", Toast.LENGTH_SHORT)
		// // .show();
		// break;
		// case R.id.dianxinbtn:
		// // Toast.makeText(MobileAty.this, "运营商正在筹备中", Toast.LENGTH_SHORT)
		// // .show();
		//
		// reSetBtn();
		// dianxin.setBackgroundResource(R.drawable.yuanjiao_choice);
		// searchText2 = "a5492a4b-42da-46dc-a906-c93ccebb3be8";
		// searchText1 = "电信";
		// break;
		case R.id.startBtn:
			if (searchText1 == null || searchText1.equals("")) {
				Toast.makeText(MobileAty.this, "尚未选择套餐", Toast.LENGTH_SHORT)
						.show();
			} else {

				dialog = CustomProgressDialog.createDialog(MobileAty.this,
						"加载中。。。");
				dialog.show();
				startBtn.setClickable(false);
				// broadBands.clear();
				lists.clear();
				new Thread(queryNum).start();
			}

			break;

		default:
			break;
		}

	}

	// void resetBTN() {
	// yd20a.setBackgroundResource(R.drawable.yuanjiao);
	// yd40a.setBackgroundResource(R.drawable.yuanjiao);
	// }

	void reSetBtn() {
		yidong.setBackgroundResource(R.drawable.yuanjiao);
		dianxin.setBackgroundResource(R.drawable.yuanjiao);
		liantong.setBackgroundResource(R.drawable.yuanjiao);
	}

}
