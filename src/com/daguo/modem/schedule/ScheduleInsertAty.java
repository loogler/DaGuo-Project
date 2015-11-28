package com.daguo.modem.schedule;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.daguo.R;

/**
 * 
 * @author Bugs_rabbit
 * @function 课表编辑界面
 */
public class ScheduleInsertAty extends Activity implements
		android.view.View.OnClickListener {

	private String WEEK[] = { "星期一", "星期二", "星期三", "星期四", "星期五" };
	private String[] course = new String[5];
	private String[] add = new String[5];
	// private static EditText
	// et1_2,et1_3,et2_2,et2_3,et3_2,et3_3,et4_2,et4_3,et5_2,et5_3;
	private ArrayList<EditText> et_2EditTexts = new ArrayList<EditText>();
	private ArrayList<EditText> et_3EditTexts = new ArrayList<EditText>();

	private String week = "1";

	private Util_ToDoDB toDoDB;
	private Cursor mCursor;
	private SQLiteDatabase db;
	private int _id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_schedule_insert);
		// 设置spinner的显示
		Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
		// Spinner spinner2=(Spinner)findViewById(R.id.spinner2);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, WEEK);
		// ArrayAdapter<String>adapter2=new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, NO);
		spinner1.setAdapter(adapter1);
		// spinner2.setAdapter(adapter2);
		// 设置按钮
		ImageButton bt1 = (ImageButton) findViewById(R.id.schedule_insert_bt1);
		ImageButton bt2 = (ImageButton) findViewById(R.id.schedule_insert_bt2);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);

		// 设置edittext
		et_2EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et1_2));
		et_3EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et1_3));
		et_2EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et2_2));
		et_3EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et2_3));
		et_2EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et3_2));
		et_3EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et3_3));
		et_2EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et4_2));
		et_3EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et4_3));
		et_2EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et5_2));
		et_3EditTexts.add((EditText) findViewById(R.id.scheduleInsert_et5_3));

		toDoDB = new Util_ToDoDB(this);
		db = toDoDB.getReadableDatabase();

		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				week = Integer.toString(position + 1);

				String sql = "select * from todo_schedule where todo_week="
						+ week;
				mCursor = db.rawQuery(sql, null);
				if (mCursor != null) {
					int i = 0, n = mCursor.getCount();
					mCursor.moveToFirst();
					Log.i("", "mCursor!=null");
					Log.i("n=?", n + "");

					// 遍历游标 11.
					while (!mCursor.isAfterLast()) {

						// 获得ID
						// int id = mCursor.getInt(0);
						// 获得用户名
						course[i] = mCursor.getString(3);
						// Log.i("", mCursor.getString(3));
						// 获得密码
						add[i] = mCursor.getString(4);
						i++;
						mCursor.moveToNext();
					}
				}
				// 给edittext赋初值
				for (int i = 0; i < 5; i++) {
					et_3EditTexts.get(i).setHint("上课地点");
					et_2EditTexts.get(i).setHint("课程名称");

					et_2EditTexts.get(i).setText(course[i]);
					et_3EditTexts.get(i).setText(add[i]);
				}

			}

			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.schedule_insert_bt1) {

			// summit
			// 插入测试课程

			// et=et1_2.getText().toString();
			editTodo();
			Toast.makeText(ScheduleInsertAty.this, "更新课程成功！", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(
					"android.appwidget.action.APPWIDGET_UPDATE");
			ScheduleInsertAty.this.sendBroadcast(intent);
			/*
			 * try{ String cname=et1.getText().toString(); String
			 * address=et2.getText().toString(); ToDoDB_Schedule
			 * toDoDB_Schedule=new ToDoDB_Schedule(ScheduleInsert.this,
			 * "Schedule"); SQLiteDatabase
			 * db=toDoDB_Schedule.getWritableDatabase(); String
			 * sql="Update todo_schedule set todo_course = '"
			 * +cname+"', todo_add='"+address+"'" +
			 * "where todo_week='"+week+"' and todo_section='"+no+"'";
			 * db.execSQL(sql); Toast.makeText(ScheduleInsert.this, "更新数据成功！",
			 * Toast.LENGTH_SHORT).show(); }catch (Exception e) { //
			 * handle exception }
			 */

		} else if (v.getId() == R.id.schedule_insert_bt2) {
			// back
			mCursor.close();
			toDoDB.close();
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);

		}

	}

	private void editTodo() {

		mCursor.moveToFirst();
		_id = mCursor.getInt(0);
		/* 修改数据 */
		for (int i = 0; i < 5; i++) {
			// Log.i("et_2EditTexts[i]",i+"");//正常
			toDoDB.updateCourse(_id, getEditText_2(i));
			toDoDB.updateAdd(_id, getEditText_3(i));
			// Log.i("et_2EditTexts[i]",et1_2.getText().toString());//正常
			// Log.i("et_2EditTexts[i]", _id+"");1~5
			_id++;
		}
		_id = 0;
	}

	private String getEditText_2(int index) {
		EditText etEditText = et_2EditTexts.get(index);
		String string = etEditText.getText().toString();
		return string;
	}

	private String getEditText_3(int index) {
		EditText etEditText = et_3EditTexts.get(index);
		String string = etEditText.getText().toString();
		return string;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mCursor.close();
		toDoDB.close();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果按下的是返回键，并且没有重复
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
			return false;
		}
		return false;
	}

}
