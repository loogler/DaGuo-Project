package com.daguo.ui.operators.mobile;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.Oper_mobileAdapter;
import com.daguo.util.beans.TelNumber;
import com.daguo.utils.HttpUtil;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-26 上午9:28:44
 * @function 功能: 移动选号第二步 筛选结果界面
 */
public class Oper_Mobile_SStepAty extends Activity {

	private final int MSG_TEL_SUC = 10001;
	private int pageIndex = 1;
	private String school_id, busi_name;

	/**
	 * initViews
	 */
	private TextView refresh_tv;
	private GridView number_grid;

	/**
	 * data
	 */
	private TelNumber telNumber;
	private List<TelNumber> telNumbers = new ArrayList<TelNumber>();
	private Oper_mobileAdapter adapter;

	/**
	 * tools
	 */
	private Message msg;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TEL_SUC:
				if (msg.obj != null) {
					telNumbers.clear();
					@SuppressWarnings("unchecked")
					List<TelNumber> aList = (List<TelNumber>) msg.obj;
					telNumbers.addAll(aList);
					adapter.notifyDataSetChanged();

				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mobile);
		MyAppliation.getInstance().addActivity(this);
		Intent intent = getIntent();
		school_id = intent.getStringExtra("schoolId");
		busi_name = intent.getStringExtra("busi_name");

		initViews();
		loadtelNumber();

		adapter = new Oper_mobileAdapter(this, telNumbers);
		number_grid.setAdapter(adapter);
		number_grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int p,
					long arg3) {
				Intent intent = new Intent(Oper_Mobile_SStepAty.this,
						Oper_Mobile_OrderAty.class);

				intent.putExtra("tel", telNumbers.get(p).getTel());
				intent.putExtra("id", telNumbers.get(p).getId());
				intent.putExtra("cost_name", telNumbers.get(p).getCost_name());
				intent.putExtra("price", telNumbers.get(p).getPrice());

				startActivity(intent);

			}
		});

	}

	/**
     * 
     */
	private void initViews() {
		initTitleView();
		refresh_tv = (TextView) findViewById(R.id.refresh_tv);
		number_grid = (GridView) findViewById(R.id.number_grid);

		refresh_tv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				pageIndex++;

				if (pageIndex > 4) {
					pageIndex = 1;
				}
				loadtelNumber();
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

		title_tv.setText("大果校园选号");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	// -------------------------- data thread ---------------------/

	private void loadtelNumber() {
		new Thread(new Runnable() {
			public void run() {

				try {

					String url = HttpUtil.QUERY_NUMBER + "&rows=12&page="
							+ pageIndex + "&busi_name=" + busi_name
							+ "&school_id=" + school_id;
					String res = HttpUtil.getRequest(url);

					JSONObject jsonObject = new JSONObject(res);
					if (jsonObject.getInt("total") > 0) {
						List<TelNumber> abc = new ArrayList<TelNumber>();

						JSONArray array = jsonObject.getJSONArray("rows");
						for (int i = 0; i < array.length(); i++) {
							telNumber = new TelNumber();
							String tel = array.optJSONObject(i)
									.getString("tel");
							String id = array.optJSONObject(i).getString("id");
							String cost_name = array.optJSONObject(i)
									.getString("cost_name");
							String price = array.optJSONObject(i).getString(
									"price");

							telNumber.setPrice(price);
							telNumber.setCost_name(cost_name);
							telNumber.setTel(tel);
							telNumber.setId(id);

							abc.add(telNumber);
						}
						msg = handler.obtainMessage(MSG_TEL_SUC);
						msg.obj = abc;
						msg.sendToTarget();

					} else {
						// 并没有查到号码
						Log.e("选号号码列表", "查询为空");
					}

				} catch (Exception e) {
					Log.e("选号号码列表", "出现异常");
				}

			}
		}).start();
	}
}
