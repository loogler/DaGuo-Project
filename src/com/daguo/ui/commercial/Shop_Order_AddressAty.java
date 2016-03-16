/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-25 上午11:50:32
 * @function ：商品订单的物流地址界面
 */
public class Shop_Order_AddressAty extends Activity {
	/**
	 * initViews
	 */
	private EditText name_edt, tel_edt, address_edt, guid_edt;
	private Button submit_btn;

	String name;
	String tel;
	String address;
	String guid;

	Intent intent;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_shop_order_address);
		MyAppliation.getInstance().addActivity(this);

		initViews();
		getAddressShared();
		setText();

	}

	/**
	 * 获取保存的历史地址信息
	 */
	private void getAddressShared() {
		SharedPreferences sp = getSharedPreferences("orderadress",
				Activity.MODE_PRIVATE);
		guid = sp.getString("guidNo", "");
		address = sp.getString("consigneeAddress", "");
		name = sp.getString("consigneeName", "");
		tel = sp.getString("consigneeTel", "");

	}

	/**
	 * 设置文字内容
	 */
	private void setText() {
		guid_edt.setText(PublicTools.doWithNullData(guid));
		address_edt.setText(PublicTools.doWithNullData(address));
		tel_edt.setText(PublicTools.doWithNullData(tel));
		name_edt.setText(PublicTools.doWithNullData(name));
	}

	/**
     * 
     */
	private void initViews() {
		initTitleView();
		name_edt = (EditText) findViewById(R.id.name_edt);
		tel_edt = (EditText) findViewById(R.id.tel_edt);
		address_edt = (EditText) findViewById(R.id.address_edt);
		guid_edt = (EditText) findViewById(R.id.guid_edt);

		submit_btn = (Button) findViewById(R.id.submit_btn);
		submit_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				checkInfos();
				intent = new Intent(Shop_Order_AddressAty.this,
						Shop_OrderAty.class);
				intent.putExtra("name", PublicTools.doWithNullData(name));
				intent.putExtra("tel", PublicTools.doWithNullData(tel));
				intent.putExtra("guid", PublicTools.doWithNullData(guid));
				intent.putExtra("address", PublicTools.doWithNullData(address));

				setResult(10001, intent);
				finish();
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

		title_tv.setText("收货地址");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

	private void checkInfos() {
		name = name_edt.getText().toString().trim();
		tel = tel_edt.getText().toString().trim();
		address = address_edt.getText().toString().trim();
		guid = guid_edt.getText().toString().trim();

		if ("".equals(PublicTools.doWithNullData(name))) {
			Toast.makeText(Shop_Order_AddressAty.this, "名称不能为空",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ("".equals(PublicTools.doWithNullData(tel))) {
			Toast.makeText(Shop_Order_AddressAty.this, "联系号码不能为空",
					Toast.LENGTH_LONG).show();
			return;
		}
		if ("".equals(PublicTools.doWithNullData(address))) {
			Toast.makeText(Shop_Order_AddressAty.this, "地址不能为空",
					Toast.LENGTH_LONG).show();
			return;
		}

	}

}
