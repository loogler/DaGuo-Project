package com.daguo.ui.school.huodong;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.w3c.dom.Text;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.adapter.Eva_OrdinaryAdapter;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.pulllistview.XListView;
import com.daguo.util.pulllistview.XListView_Scroll;
import com.daguo.utils.HttpUtil;

/**
 * 活动详细内容
 * 
 * @author Bugs_Rabbit 時間： 2015-10-19 上午12:14:32
 */
public class SC_HuoDong_DetailAty extends Activity {

	private String type_name;
	private String title1;
	private String start_date;
	private String end_date;
	private String type_send;
	private String content;
	private String img_content;

	private TextView type_nameTextView;
	private TextView title1TextView;
	private TextView start_dateTextView;
	private TextView end_dateTextView;
	private TextView type_sendTextView;
	private TextView contentTextView;
	private ImageView img_contentTextView;

	private XListView_Scroll xListView_Scroll;
	private Eva_OrdinaryAdapter adapter;
	private List<Evaluate_Ordinary> lists = new ArrayList<Evaluate_Ordinary>();

	Message msg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_huodong_detail);
		Intent intent = getIntent();
		type_name = intent.getStringExtra("type_name");
		title1 = intent.getStringExtra("title1");
		start_date = intent.getStringExtra("start_date");
		end_date = intent.getStringExtra("end_date");
		type_send = intent.getStringExtra("type_send");
		content = intent.getStringExtra("content");
		img_content = intent.getStringExtra("img_content");

		initView();

	}

	private void initView() {
		type_nameTextView = (TextView) findViewById(R.id.type_name);
		title1TextView = (TextView) findViewById(R.id.title1);
		start_dateTextView = (TextView) findViewById(R.id.start_date);
		end_dateTextView = (TextView) findViewById(R.id.end_date);
		type_sendTextView = (TextView) findViewById(R.id.type_send);
		contentTextView = (TextView) findViewById(R.id.content);
		img_contentTextView = (ImageView) findViewById(R.id.img_content);

		type_nameTextView.setText(type_name);
		title1TextView.setText(title1);
		start_dateTextView.setText(start_date);
		end_dateTextView.setText(end_date);
		type_sendTextView.setText(type_send);
		contentTextView.setText(content);
		FinalBitmap.create(SC_HuoDong_DetailAty.this).display(
				img_contentTextView, img_content);

		xListView_Scroll = (XListView_Scroll) findViewById(R.id.xlistview);
		adapter = new Eva_OrdinaryAdapter(SC_HuoDong_DetailAty.this, lists);
		xListView_Scroll.setAdapter(adapter);
		initThread.start();

	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			adapter.notifyDataSetChanged();
		}
	};

	Thread initThread = new Thread(new Runnable() {
		public void run() {
			
			try {
			
			} catch (Exception e) {
			}
			for (int i = 0; i < 30; i++) {

				Evaluate_Ordinary list = new Evaluate_Ordinary();
				list.setContent("评论内容" + i);
				list.setCreate_time("2015-10-15");
				list.setHead_info("http://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E7%AE%80%E7%AC%94%E7%94%BB&step_word=&pn=42&spn=0&di=106293922130&pi=&rn=1&tn=baiduimagedetail&is=&istype=0&ie=utf-8&oe=utf-8&in=&cl=2&lm=-1&st=-1&cs=640630213%2C1392512045&os=2041330386%2C1591008734&adpicid=0&ln=1000&fr=&fmq=1444822267819_R&ic=0&s=undefined&se=&sme=&tab=0&width=&height=&face=undefined&ist=&jit=&cg=&bdtype=0&objurl=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201401%2F25%2F20140125072445_mEQJr.thumb.700_0.jpeg&fromurl=ippr_z2C%24qAzdH3FAzdH3Fooo_z%26e3B17tpwg2_z%26e3Bv54AzdH3Frj5rsjAzdH3F4ks52AzdH3F8b9nc090lAzdH3F1jpwtsAzdH3F&gsm=0");
				list.setP_name("哈哈哈哈" + i);
				lists.add(list);
			}
			msg = handler.obtainMessage();

			msg.sendToTarget();
		}
	});

}
