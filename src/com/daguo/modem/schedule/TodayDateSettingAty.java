package com.daguo.modem.schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.DatePicker;

import com.daguo.R;
/**
 * 
 * @author Bugs_rabbit
 * @function  菜单栏界面
 */
public class TodayDateSettingAty extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Resources res = getResources(); Drawable drawable =
		 * res.getDrawable(R.color.light);
		 * this.getWindow().setBackgroundDrawable(drawable);
		 * //this.getWindow().setTitleColor
		 * (getResources().getColor(R.color.title));
		 */

		SharedPreferences share = getSharedPreferences("INIT",
				Context.MODE_WORLD_READABLE);
		String str = share.getString("SET", "0");

		if (str.equals("0")) {
			showDialog(2);
		}

		addPreferencesFromResource(R.xml.preference);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 在欢迎界面设置BACK键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
		}
		return false;
	}

	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
			Preference preference) {

		if (preference.getKey().equals("SET")) {
			// Toast.makeText(this, preference.getKey(),
			// Toast.LENGTH_SHORT).show();

			showDialog(1);

			/*
			 * 该处通过intent调用FirstDaySet.java来转到时间调整页面 Intent intent=new Intent();
			 * intent.setClass(TodayDateEdit.this, FirstDaySet.class);
			 * //intent.putExtra("psKey", preference.getKey());
			 * TodayDateEdit.this.startActivity(intent);
			 * overridePendingTransition
			 * (android.R.anim.fade_in,android.R.anim.fade_out)
			 */;
		} 
//		else if (preference.getKey().equals("ABOUT")) {
//			Intent intent = new Intent();
//			intent.setClass(TodayDateSetting.this, TodayDateAbout.class);
//			TodayDateSetting.this.startActivity(intent);
//			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
//		} else if (preference.getKey().equals("EXIT")) {
//			finish();
//			overridePendingTransition(R.anim.slide_up_in, R.anim.slide_down_out);
//		}

		return false;
	}

	protected Dialog onCreateDialog(int id, Bundle args) {
		switch (id) {
		case 1:
			Log.i("已进入onCreateDialog", "已进入onCreateDialog");
			SharedPreferences share = getSharedPreferences("INIT",
					MODE_WORLD_WRITEABLE);
			DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
			try {
				Date date = df.parse(share.getString("SET_DATE", "2012-0-1"));
				// Log.i("已进入try", df.format(date));
				DatePickerDialog dpd = new DatePickerDialog(this,
						onDateSetListener, date.getYear() + 1900,
						date.getMonth() + 1, date.getDate());
				// Log.i("已进入try",
				// Integer.toString(date.getYear())+"年"+Integer.toString(date.getMonth())+"月"+Integer.toString(date.getDate())+"日");
				// return new DatePickerDialog(this, onDateSetListener,
				// date.getYear(), date.getMonth(), date.getDate());

				return dpd;
			} catch (ParseException e) {
				e.printStackTrace();
			}

		case 2:
			return new AlertDialog.Builder(this).setTitle("您还没有定义第一周").create();
		default:
			break;
		}
		return super.onCreateDialog(id);
	}

	DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String dateString = Integer.toString(year) + "-"
					+ Integer.toString(monthOfYear) + "-"
					+ Integer.toString(dayOfMonth);
			int setInt;
			Util_FindDayOfYear fdDayOfYear = new Util_FindDayOfYear();
			setInt = fdDayOfYear.getDayOfYear(year, monthOfYear, dayOfMonth);

			SharedPreferences share = getSharedPreferences("INIT",
					Context.MODE_WORLD_WRITEABLE);
			Editor editor = share.edit();
			editor.putString("SET", Integer.toString(setInt)).commit();
			editor.putString("SET_DATE", dateString).commit();
			Log.d("TodayDateEdit", "相差天数为:" + Integer.toString(setInt));
			Log.d("TodayDateEdit", "当前已选择" + dateString);
			Intent intent = new Intent(
					"android.appwidget.action.APPWIDGET_UPDATE");
			TodayDateSettingAty.this.sendBroadcast(intent);

		}
	};

}
