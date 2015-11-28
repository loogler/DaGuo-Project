package com.daguo.modem.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.daguo.R;
/**
 * 
 * @author Bugs_rabbit
 * @function 课程表主界面
 */
public class TodayDateScheduleAty extends Activity implements
		OnItemSelectedListener, OnItemClickListener,
		android.view.View.OnClickListener {

	private Integer[] array = { R.drawable.mon_icon, R.drawable.tue_icon,
			R.drawable.wed_icon, R.drawable.thu_icon, R.drawable.fri_icon };

	/*
	 * public TodayDateSchedule() { toDoDB_Schedule = new ToDoDB_Schedule(this);
	 * }
	 */

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// imageView.setImageResource(resIds[position]);
		Intent intent = new Intent();
		intent.setClass(this, ScheduleShowAty.class);
		intent.putExtra("POSITION", position + 1);
		TodayDateScheduleAty.this.startActivity(intent);
		overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// imageView.setImageResource(resIds[position]);
		Intent intent = new Intent();
		intent.setClass(this, ScheduleShowAty.class);
		intent.putExtra("POSITION", position + 1);
		// Log.i("intent.putExtra", position+"");
		TodayDateScheduleAty.this.startActivity(intent);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_schedule);
		inti();
	}

	private void inti() {
		Util_DateDay dd = new Util_DateDay(this);
		TextView tv1 = (TextView) findViewById(R.id.schedule_tv1);
		TextView tv2 = (TextView) findViewById(R.id.schedule_tv2);
		Typeface tf = Typeface.createFromAsset(getAssets(),
				"Roboto-MediumItalic.ttf");
		tv1.setTypeface(tf);
		tv1.setTextColor(getResources().getColor(R.color.purple));
		tv1.setText(dd.getWeedDay() + "周 ");
		tv2.setText(dd.getDays1());
		tv2.setTextColor(getResources().getColor(R.color.blue));

		Button bt1 = (Button) this.findViewById(R.id.app_schedule_bt1);
		bt1.setOnClickListener(this);

		GridView gridView = (GridView) findViewById(R.id.gridview);
		List<Map<String, Object>> cells = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < array.length; i++) {
			Map<String, Object> cell = new HashMap<String, Object>();
			cell.put("imageview", array[i]);
			cells.add(cell);
		}
		SimpleAdapter simpleAdapter = new SimpleAdapter(this, cells,
				R.layout.cell, new String[] { "imageview" },
				new int[] { R.id.cell_imageview });
		// Animation animation=(Animation)
		// getResources().getAnimation(R.anim.scale_rotate);
		// gridView.startAnimation(animation);

		gridView.setAdapter(simpleAdapter);

		gridView.setOnItemSelectedListener(this);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, ScheduleInsertAty.class);
		TodayDateScheduleAty.this.startActivity(intent);

	}

}
