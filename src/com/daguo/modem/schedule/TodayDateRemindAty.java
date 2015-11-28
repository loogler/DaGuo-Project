package com.daguo.modem.schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.daguo.R;
/**
 * 
 * @author Bugs_rabbit
 * @function 备忘录的首界面，有添加记录的功能
 */
public class TodayDateRemindAty extends Activity implements OnClickListener {
	private static Util_ToDoDB toDoDB;
	public static Cursor cursor;
	public static ListView lv;
	private int _id;
	private Button bt1;

	// ArrayList<String> strlist=new ArrayList<String>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_remind);

		/*
		 * DateDay dd=new DateDay(this); TextView
		 * tv1=(TextView)this.findViewById(R.id.remind_tv1);
		 * tv1.setText("第"+dd.getWeedDay
		 * ()+"周 "+dd.getDays1()+" "+"   "+dd.getMonth
		 * ()+dd.getDate()+"日 "+dd.getYear());
		 */

		lv = (ListView) this.findViewById(R.id.myListView);
		bt1 = (Button) this.findViewById(R.id.app_remind_bt1);
		// bt1.setBackgroundDrawable(getResources().getDrawable(R.drawable.inserticon_3));

		bt1.setOnClickListener(this);

		toDoDB = new Util_ToDoDB(this);
		// toDoDB.intiTable();
		cursor = toDoDB.selectRemind();

		/* new SimpleCursorAdapter并将myCursor传入，显示数据的字段为todo_text */
		// BaseAdapter adapter =
		// new SimpleCursorAdapter(this, R.layout.list, cursor , new String[] {
		// ToDoDB.REMIND_TV }, new int[] { R.id.lv1 });

		Adapter_Folks adapter = new Adapter_Folks(TodayDateRemindAty.this, cursor);
		lv.setAdapter(adapter);

		/*
		 * Animation animation=AnimationUtils.loadAnimation(this,
		 * R.anim.translucent_enter); Animation animation_new=new
		 * TranslateAnimation(480/5, 0, 0, 0); //screenHeight*5/10
		 * animation_new.setDuration(1500); //lv.setAdapter(adapter);
		 * lv.startAnimation(animation);
		 */

		// 设置隐藏列表分割线
		lv.setDividerHeight(0);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView arg0, View arg1, int arg2,
					long arg3) {
				Context context = getApplicationContext();
				/* 将cursor移到所点选的值 */
				cursor.moveToPosition(arg2);
				/* 取得字段_id的值 */
				_id = cursor.getInt(0);
				/* 打开RemindEdit */
				// Log.i("", "已点击");
				Intent intent = new Intent();
				intent.putExtra("ARG2", arg2);
				// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setClass(context, RemindEditAty.class);
				TodayDateRemindAty.this.startActivity(intent);

			}
		});
		lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView arg0, View arg1, int arg2,
					long arg3) {
				/* getSelectedItem所取得的是SQLiteCursor */
				SQLiteCursor sc = (SQLiteCursor) arg0.getSelectedItem();
				_id = sc.getInt(0);
				// Log.i("", "已选择");
			}

			@Override
			public void onNothingSelected(AdapterView arg0) {
			}
		});
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, RemindInsertAty.class);
		TodayDateRemindAty.this.startActivity(intent);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		cursor.close();
		toDoDB.close();
	}
}
