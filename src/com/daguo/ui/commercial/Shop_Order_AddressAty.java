/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.PublicTools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-25 上午11:50:32
 * @function ：
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

    }

    /**
     * 
     */
    private void initViews() {
	initHeadView();
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

    private void initHeadView() {
	TextView back_tv = (TextView) findViewById(R.id.back_tv);
	TextView title_tv = (TextView) findViewById(R.id.title_tv);
	TextView function_tv = (TextView) findViewById(R.id.function_tv);
	ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

	back_tv.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {

		finish();
	    }
	});
	title_tv.setText("收货地址");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

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
