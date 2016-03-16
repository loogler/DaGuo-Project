package com.daguo.ui.operators.broadband;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午3:41:09
 * @function ：宽带引导页 主界面
 */
@SuppressLint("HandlerLeak")
public class Oper_BroadBandAty extends Activity implements OnClickListener {

    /**
     * initViews
     */
    private ImageView mob_iv, tel_iv, uni_iv;
    private LinearLayout ll;

    private Bitmap bmll, bmm, bmt, bmu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_broadband);
	MyAppliation.getInstance().addActivity(this);
	initTitleView();
	initViews();

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

		title_tv.setText("大果校园宽带");
		back_fram.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				System.gc();
				finish();
			}
		});
		message_ll.setVisibility(View.INVISIBLE);
	}

    /**
     * 
     */
    @SuppressWarnings("deprecation")
    private void initViews() {
	ll = (LinearLayout) findViewById(R.id.ll);
	mob_iv = (ImageView) findViewById(R.id.mob_iv);
	tel_iv = (ImageView) findViewById(R.id.tel_iv);
	uni_iv = (ImageView) findViewById(R.id.uni_iv);

	bmll = BitmapFactory.decodeResource(getResources(),
		R.drawable.bg_oper_brodband);
	bmm = BitmapFactory.decodeResource(getResources(),
		R.drawable.oper_brodband1);
	bmt = BitmapFactory.decodeResource(getResources(),
		R.drawable.oper_brodband3);
	bmu = BitmapFactory.decodeResource(getResources(),
		R.drawable.oper_brodband2);

	ll.setBackgroundDrawable(new BitmapDrawable(bmll));
	mob_iv.setBackgroundDrawable(new BitmapDrawable(bmm));
	tel_iv.setBackgroundDrawable(new BitmapDrawable(bmt));
	uni_iv.setBackgroundDrawable(new BitmapDrawable(bmu));

	mob_iv.setOnClickListener(this);
	tel_iv.setOnClickListener(this);
	uni_iv.setOnClickListener(this);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	Intent intent;
	switch (v.getId()) {

	case R.id.tel_iv:
	    intent = new Intent(Oper_BroadBandAty.this,
		    Oper_BroadBand_FStepAty.class);
	    intent.putExtra("busi_name", "5e5aa8f1-8c65-42bf-bebe-6fb6adbbee05");

	    startActivity(intent);
	    break;

	case R.id.uni_iv:

	    intent = new Intent(Oper_BroadBandAty.this,
		    Oper_BroadBand_FStepAty.class);

	    intent.putExtra("busi_name", "065017a4-1e05-41dd-88b5-aa02f99de3d6");
	    startActivity(intent);

	    break;

	case R.id.mob_iv:
	    intent = new Intent(Oper_BroadBandAty.this,
		    Oper_BroadBand_FStepAty.class);
	    intent.putExtra("busi_name", "b05bab2f-e7b3-41f7-b22f-acc01c0ef0f5");

	    startActivity(intent);
	    break;

	default:
	    break;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
	if (null != bmll) {
	    bmll.recycle();

	}
	if (null != bmu) {
	    bmu.recycle();

	}
	if (null != bmt) {
	    bmt.recycle();

	}
	if (null != bmm) {
	    bmm.recycle();

	}
	System.gc();
	super.onDestroy();
    }

    // ------------------------------------------------------------------------------------/

    // /**
    // *
    // * @author Bugs_Rabbit 時間： 2015-8-6 下午4:28:33
    // */
    // class GetInfoThread extends Thread {
    //
    // List<BroadBand> lists = new ArrayList<BroadBand>();
    // BroadBand list = null;
    //
    // @Override
    // public void run() {
    // super.run();
    //
    // try {
    // Map<String, String> map = new HashMap<String, String>();
    // map.put("sname", searchText1);
    // map.put("busi_name", searchText2);
    // String url = HttpUtil.QUERY_BROADBAND;
    // String res = HttpUtil.postRequest(url, map);
    // if (res != null) {
    // JSONObject jsonObject = new JSONObject(res);
    // JSONArray array = jsonObject.getJSONArray("rows");
    //
    // for (int j = 0; j < array.length(); j++) {
    // list = new BroadBand();
    // String orderId = array.optJSONObject(j).getString("id");
    //
    // if (!orderId.equals("") && orderId != null) {
    // list.setOrderId(orderId);
    // } else {
    // list.setOrderId("未知");
    // }
    //
    // String busiName = array.optJSONObject(j).getString(
    // "busi_name");
    // if (!busiName.equals("") && busiName != null) {
    //
    // list.setBusiName(busiName);
    // } else {
    // list.setBusiName("");
    // }
    // Log.i(tag, busiName);
    // String costInfo = array.optJSONObject(j).getString(
    // "cost_info");
    // if (!costInfo.equals("") && costInfo != null) {
    //
    // list.setCostInfo(costInfo + "");
    // } else {
    // list.setCostInfo("");
    // }
    //
    // String month = array.optJSONObject(j)
    // .getString("month");
    // if (!month.equals("") && month != null) {
    //
    // list.setMonth(month + "  月");
    // } else {
    // list.setMonth("");
    // }
    // String name = array.optJSONObject(j).getString("name");
    // if (!name.equals("") && name != null) {
    //
    // list.setDetailName(name);
    // } else {
    // list.setDetailName("");
    // }
    // String price = array.optJSONObject(j)
    // .getString("price");
    // if (!price.equals("") && price != null) {
    //
    // list.setPrice(price);
    // } else {
    // list.setPrice("");
    // }
    // String school_id = array.optJSONObject(j).getString(
    // "school_id");
    // if (!school_id.equals("") && school_id != null) {
    //
    // list.setSchool_id(school_id);
    // } else {
    // list.setSchool_id("");
    // }
    // Log.i(tag, "==============" + costInfo + name + month
    // + price);
    // String sName = array.optJSONObject(j)
    // .getString("sname");
    // if (!sName.equals("") && sName != null
    // && !busiName.equals("") && busiName != null) {
    //
    // lists.add(list);
    // num = lists.size();
    // if (sName.equals(searchText1)
    // || sName.contains(searchText1)) {
    //
    // list.setsName(sName);
    //
    // }
    //
    // } else {
    // sName = "无效校区";
    // }
    // Log.i(tag, "==========>sname==" + sName);
    //
    // }
    // message = handler2.obtainMessage();
    // message.obj = lists;
    // message.sendToTarget();
    // } else {
    // runOnUiThread(new Runnable() {
    // public void run() {
    // Toast.makeText(Oper_BroadBandAty.this, "服务器异常，请稍后",
    // Toast.LENGTH_SHORT).show();
    // startBtn.setClickable(true);
    // dialog.dismiss();
    // }
    // });
    //
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    //
    // }
    // }
    //
    // @Override
    // public void onItemClick(AdapterView<?> arg0, View convertView,
    // int position, long ids) {
    // // Toast.makeText(getBaseContext(),
    // // "tusi" + position + broadBands.get(position).getsName(),
    // // Toast.LENGTH_SHORT).show();
    //
    // broadbandid = broadBands.get(position).getOrderId();
    // broadbandbusi_name = broadBands.get(position).getBusiName();
    // broadbandcost_info = broadBands.get(position).getCostInfo();
    // broadbandmonth = broadBands.get(position).getMonth();
    // broadbandname = broadBands.get(position).getDetailName();
    // broadbandprice = broadBands.get(position).getPrice();
    // broadbandschoolid = broadBands.get(position).getSchool_id();
    // // 跳转
    // Intent intent = new Intent(Oper_BroadBandAty.this,
    // BroadBandOrderAty.class);
    // startActivity(intent);
    //
    // }
    //
    // public void onClicksss(View v) {
    // switch (v.getId()) {
    //
    // case R.id.startBtn:
    //
    // runOnUiThread(new Runnable() {
    // public void run() {
    // dialog = CustomProgressDialog.createDialog(
    // Oper_BroadBandAty.this, "请耐心等待加载。。。");
    // dialog.show();
    //
    // }
    // });
    // startBtn.setClickable(false);
    // broadBands.clear();
    // searchText1 = autoText.getText().toString().trim();
    // if (!searchText1.equals("") && searchText1 != null) {
    //
    // new GetInfoThread().start();
    //
    // } else {
    // Toast.makeText(Oper_BroadBandAty.this, "搜索信息不完整",
    // Toast.LENGTH_SHORT).show();
    // startBtn.setClickable(true);
    // dialog.dismiss();
    //
    // }
    //
    // break;
    // case R.id.yidongbtn:
    // reSetBtn();
    // yidong.setBackgroundResource(R.drawable.yuanjiao_choice);
    // searchText2 = "b05bab2f-e7b3-41f7-b22f-acc01c0ef0f5";
    // break;
    // case R.id.liantongbtn:
    // reSetBtn();
    // liantong.setBackgroundResource(R.drawable.yuanjiao_choice);
    // searchText2 = "065017a4-1e05-41dd-88b5-aa02f99de3d6";
    // break;
    // case R.id.dianxinbtn:
    // reSetBtn();
    // dianxin.setBackgroundResource(R.drawable.yuanjiao_choice);
    // searchText2 = "5e5aa8f1-8c65-42bf-bebe-6fb6adbbee05";
    // break;
    //
    // default:
    // break;
    // }
    //
    // }
    //
    // void reSetBtn() {
    // yidong.setBackgroundResource(R.drawable.yuanjiao);
    // dianxin.setBackgroundResource(R.drawable.yuanjiao);
    // liantong.setBackgroundResource(R.drawable.yuanjiao);
    // }
    //
    // private String[] schoolName;
    // Map<String, String> maps = new HashMap<String, String>();
    // private List<String> schooList = new ArrayList<String>();
    //
    // void getText() {
    //
    // try {
    // String urlString = HttpUtil.DICT_SCHOOLNAME;
    // String reString = HttpUtil.getRequest(urlString);
    // JSONObject jsObject = new JSONObject(reString);
    // JSONArray array = jsObject.getJSONArray("rows");
    // for (int i = 0; i < array.length(); i++) {
    // String name = array.optJSONObject(i).getString("name");
    // String id = array.optJSONObject(i).getString("id");
    //
    // schooList.add(name);
    // maps.put(name, id);
    //
    // }
    //
    // } catch (Exception e) {
    // }
    // // list转成数组
    //
    // schoolName = schooList.toArray(new String[schooList.size()]);
    // Log.i("学校名称", schoolName + "");
    //
    // runOnUiThread(new Runnable() {
    // public void run() {
    // autoText.setAdapter(new ArrayAdapter<String>(
    // Oper_BroadBandAty.this,
    // android.R.layout.simple_dropdown_item_1line, schoolName));
    // }
    // });
    // }

}
