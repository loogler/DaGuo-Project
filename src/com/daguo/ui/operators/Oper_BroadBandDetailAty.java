package com.daguo.ui.operators;

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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.ui.operators.broadband.Oper_BroadBand_OrderAty;
import com.daguo.util.beans.BroadBand;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-19 上午9:29:07
 * @function 功能:宽带详情
 */
public class Oper_BroadBandDetailAty extends Activity {

	private String TAG = "OperatorDetailAty";
	private final int MSG_DETAIL = 10001;
	private String id;// 该套餐的id

	/**
	 * @see initViews
	 */
	private TextView name_tv, school_tv, price_tv, time_tv, content_tv;
	private Button submit_btn;

	/**
	 * data 数据
	 */
	private BroadBand broadBand = new BroadBand();

	/**
	 * tools
	 */
//	private Message msgMessage;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_DETAIL:
				setViews();

				break;

			default:
				break;
			}
		};
	};

	/* ===================== void ========================== */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyAppliation.getInstance().addActivity(this);
		setContentView(R.layout.aty_operatordetail);
		id = getIntent().getStringExtra("id");
		initViews();
		initTitleView();
		loadOperatorInfo();

	}

	/**
	 * init View
	 */
	private void initViews() {
		name_tv = (TextView) findViewById(R.id.name_tv);
		school_tv = (TextView) findViewById(R.id.school_tv);
		price_tv = (TextView) findViewById(R.id.price_tv);
		time_tv = (TextView) findViewById(R.id.time_tv);
		content_tv = (TextView) findViewById(R.id.content_tv);
		submit_btn = (Button) findViewById(R.id.submit_btn);
		submit_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Oper_BroadBandDetailAty.this,
						Oper_BroadBand_OrderAty.class);
				intent.putExtra("cost_name", broadBand.getDetailName());
				intent.putExtra("id", broadBand.getOrderId());
				intent.putExtra("price", broadBand.getPrice());

				startActivity(intent);
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

		title_tv.setText("我的优惠劵");
		back_fram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	/**
	 * 设置界面数据填充信息
	 */
	private void setViews() {
		if (broadBand != null) {

			// holder.name_tv.setText("宽带名称：" + lists.get(p).getDetailName());
			// holder.price_tv.setText("宽带价格： " + lists.get(p).getPrice()+" 元");
			// holder.school_tv.setText("所属学校： " + lists.get(p).getsName());
			// holder.time_tv.setText("服务时间： " + lists.get(p).getMonth()+" 月");

			name_tv.setText("宽带名称："
					+ PublicTools.doWithNullData(broadBand.getDetailName()));
			school_tv.setText("所属学校： "
					+ PublicTools.doWithNullData(broadBand.getsName()));
			price_tv.setText("宽带价格： "
					+ PublicTools.doWithNullData(broadBand.getPrice()) + " 元");
			content_tv.setText(PublicTools.doWithNullData(broadBand
					.getCostInfo()));
			time_tv.setText("服务时间： "
					+ PublicTools.doWithNullData(broadBand.getMonth()) + " 月");

		} else {
			// 初始化异常
		}
	}

	/* -================ 获取数据 ============================ */

	/**
	 * 获取 宽带/套餐 详情线程
	 */
	private void loadOperatorInfo() {
		new Thread(new Runnable() {
			public void run() {
				try {
					String url = HttpUtil.QUERY_BROADBAND_DETAIL
							+ "&page=1&rows=1&id=" + id;

					String res = HttpUtil.getRequest(url);

					JSONObject jsonObject = new JSONObject(res);

					if (jsonObject.getInt("total") > 0) {

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
							String content = array.optJSONObject(i).getString(
									"cost_info");
							String busiName = array.optJSONObject(i).getString(
									"busi_name");

							broadBand.setOrderId(id);
							broadBand.setDetailName(bro_name);
							broadBand.setPrice(bro_price);
							broadBand.setMonth(bro_time);
							broadBand.setsName(bro_school);
							broadBand.setCostInfo(content);
							broadBand.setBusiName(busiName);

						}
						handler.sendEmptyMessage(MSG_DETAIL);

					} else {

						// 暂无数据
					}

				} catch (Exception e) {

					Log.e(TAG, "");
				}
			}
		}).start();

	}
}
