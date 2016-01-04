package com.daguo.ui.operators;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.Oper_mobileAdapter;
import com.daguo.util.beans.TelNumber;
import com.daguo.utils.HttpUtil;

public class Oper_MobileAty extends Activity {

    private final int MSG_TEL_SUC = 10001;
    private int pageIndex = 1;

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
	initViews();
	loadtelNumber();

	adapter = new Oper_mobileAdapter(this, telNumbers);
	number_grid.setAdapter(adapter);
	number_grid.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View v, int p,
		    long arg3) {
		Intent intent = new Intent(Oper_MobileAty.this,
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
	initHeadView();
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
	title_tv.setText("大果校园选号");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    // -------------------------- data thread ---------------------/

    private void loadtelNumber() {
	new Thread(new Runnable() {
	    public void run() {

		try {

		    String url = HttpUtil.QUERY_NUMBER + "&rows=12&page="
			    + pageIndex;
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
			    String cost_name =array.optJSONObject(i).getString("cost_name");
			    String price =array.optJSONObject(i).getString("price");
			    
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
    //
    // /*------------------------------
    // ----------------------------------------*/
    //
    // private void aaa() {
    // new Thread(new Runnable() {
    // public void run() {
    // try {
    // String url = HttpUtil.DICT_NUMBER;
    // String res = HttpUtil.getRequest(url);
    // JSONObject js = new JSONObject(res);
    // if (js != null && !js.equals("")) {// get success
    // JSONArray array = js.getJSONArray("rows");
    // for (int i = 0; i < array.length(); i++) {
    // String id = array.optJSONObject(i).getString("id");
    // String name = array.optJSONObject(i).getString(
    // "name");
    // nameList.add(name);
    // map.put(name, id);// get data
    // }
    //
    // } else {
    // // get failed
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // message = handler2.obtainMessage(2);
    // message.obj = nameList;
    // message.sendToTarget();
    //
    // }
    // }).start();
    //
    // }
    //
    // Runnable queryNum = new Runnable() {
    //
    // @Override
    // public void run() {
    // List<TelNumber> infos = new ArrayList<TelNumber>();
    // TelNumber info = null;
    // try {
    // searchText2 = "b05bab2f-e7b3-41f7-b22f-acc01c0ef0f5";
    // String url = HttpUtil.QUERY_NUMBER;
    // Map<String, String> map = new HashMap<String, String>();
    // map.put("cost_name", searchText1);
    // map.put("busi_name", searchText2);
    // String res = HttpUtil.postRequest(url, map);
    // JSONObject js = new JSONObject(res);
    // JSONArray arr = js.getJSONArray("rows");
    // if (arr.length() == 0) {
    // info = new TelNumber();
    // infos.add(info);
    // } else {
    //
    // for (int i = 0; i < arr.length(); i++) {
    // info = new TelNumber();
    //
    // String id = arr.optJSONObject(i).getString("id");
    // String cost_name = arr.optJSONObject(i).getString(
    // "cost_name");
    // String busi_name = arr.optJSONObject(i).getString(
    // "busi_name");
    // String tel = arr.optJSONObject(i).getString("tel");
    // String price = arr.optJSONObject(i).getString("price");
    // String bz = arr.optJSONObject(i).getString("bz");
    //
    // info.setBusi_name(busi_name);
    // info.setCost_name(cost_name);
    // info.setGz(bz);
    // info.setId(id);
    // info.setPrice(price);
    // info.setTel(tel);
    // infos.add(info);
    // }
    // }
    //
    // } catch (Exception e) {
    // }
    // message = handler2.obtainMessage(1);
    // message.obj = infos;
    // message.sendToTarget();
    //
    // // Intent intent = new Intent(MobileAty.this, MoblieOrderAty.class);
    // // startActivity(intent);
    //
    // }
    // };
    // Handler handler2 = new Handler() {
    // public void handleMessage(Message msg) {
    // switch (message.what) {
    // case 1:
    // startBtn.setClickable(true);
    // runOnUiThread(new Runnable() {
    // public void run() {
    // dialog.dismiss();
    //
    // }
    // });
    //
    // tv_show.setVisibility(View.GONE);
    // if (msg.obj != null && !msg.obj.equals("")) {
    // List<TelNumber> sssBands = (List<TelNumber>) message.obj;
    // lists.addAll(sssBands);
    // if (sssBands != null) {
    //
    // adapter.notifyDataSetChanged();
    //
    // }
    // }
    // break;
    // case 2:
    // if (msg.obj != null && !msg.equals("")) {
    //
    // mAdapter.notifyDataSetChanged();
    //
    // }
    //
    // break;
    //
    // default:
    // break;
    // }
    //
    // };
    // };
    //
    // @Override
    // public void onItemClick(AdapterView<?> arg0, View view, int position,
    // long arg3) {
    // num_bz = lists.get(position).getGz();
    // num_id = lists.get(position).getId();
    // num_price = lists.get(position).getPrice();
    // num_tel = lists.get(position).getTel();
    // num_operator = lists.get(position).getBusi_name();
    //
    // dia = new Dialog(Oper_MobileAty.this);
    // dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
    // dia.show();
    //
    // final Window wd = dia.getWindow();
    // wd.setContentView(R.layout.dialog_mobile_desc);
    // TextView tel = (TextView) wd.findViewById(R.id.tel);
    // TextView price = (TextView) wd.findViewById(R.id.price);
    // TextView operator = (TextView) wd.findViewById(R.id.operator);
    // TextView desc = (TextView) wd.findViewById(R.id.desc);
    // TextView pay_btn = (TextView) wd.findViewById(R.id.pay_btn);
    //
    // tel.setText(num_tel);
    // price.setText(num_price);
    // operator.setText(num_operator);
    // desc.setText(num_bz);
    // pay_btn.setOnClickListener(new View.OnClickListener() {
    //
    // @Override
    // public void onClick(View arg0) {
    // if (num_tel != null && !num_tel.equals("")) {
    //
    // Intent intent = new Intent(Oper_MobileAty.this,
    // MoblieOrderAty.class);
    // startActivity(intent);
    // dia.dismiss();
    //
    // } else {
    // dia.dismiss();
    // }
    // }
    // });
    //
    // }
    //
    // @Override
    // public void onClick(View arg0) {
    // switch (arg0.getId()) {
    // // case R.id.yd20a:
    // // resetBTN();
    // //
    // // break;
    // // case R.id.yd40a:
    // // resetBTN();
    // // break;
    //
    // case R.id.yidongbtn:
    // // reSetBtn();
    // yidong.setBackgroundResource(R.drawable.yuanjiao_choice);
    //
    // break;
    // // case R.id.liantongbtn:
    // // reSetBtn();
    // // liantong.setBackgroundResource(R.drawable.yuanjiao_choice);
    // // searchText2 = "55151c7a-1202-4618-9eb7-9a763135453a";
    // // searchText1 = "联通";
    // // // Toast.makeText(MobileAty.this, "运营商正在筹备中", Toast.LENGTH_SHORT)
    // // // .show();
    // // break;
    // // case R.id.dianxinbtn:
    // // // Toast.makeText(MobileAty.this, "运营商正在筹备中", Toast.LENGTH_SHORT)
    // // // .show();
    // //
    // // reSetBtn();
    // // dianxin.setBackgroundResource(R.drawable.yuanjiao_choice);
    // // searchText2 = "a5492a4b-42da-46dc-a906-c93ccebb3be8";
    // // searchText1 = "电信";
    // // break;
    // case R.id.startBtn:
    // if (searchText1 == null || searchText1.equals("")) {
    // Toast.makeText(Oper_MobileAty.this, "尚未选择套餐",
    // Toast.LENGTH_SHORT).show();
    // } else {
    //
    // dialog = CustomProgressDialog.createDialog(Oper_MobileAty.this,
    // "加载中。。。");
    // dialog.show();
    // startBtn.setClickable(false);
    // // broadBands.clear();
    // lists.clear();
    // new Thread(queryNum).start();
    // }
    //
    // break;
    //
    // default:
    // break;
    // }
    //
    // }
    //
    // // void resetBTN() {
    // // yd20a.setBackgroundResource(R.drawable.yuanjiao);
    // // yd40a.setBackgroundResource(R.drawable.yuanjiao);
    // // }
    //
    // void reSetBtn() {
    // yidong.setBackgroundResource(R.drawable.yuanjiao);
    // dianxin.setBackgroundResource(R.drawable.yuanjiao);
    // liantong.setBackgroundResource(R.drawable.yuanjiao);
    // }

}
