package com.daguo.modem.schedule;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.daguo.R;

/**
 * 
 * @author Bugs_rabbit
 * @function 给viewPager上不同数据,周一到周五，自定义的一个视图窗口
 */
public class GetSchedule {
	private static String[] days1 = { "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
	private TextView tv0, tv1_2, tv1_3, tv2_2, tv2_3, tv3_2, tv3_3, tv4_2,
			tv4_3, tv5_2, tv5_3;
	private String[] course = new String[5];
	private String[] add = new String[5];
	private Util_ToDoDB toDoDB;
	private Cursor mCursor;
	private Context context;

	public GetSchedule(Context context) {
		this.context = context;
	}

	public View getScheduleView(int week) {// 1为星期一

		// View view=View.inflate(this, R.layout.app_schedule_show_page, null);
		LayoutInflater mInflater = LayoutInflater.from(context);
		View myView = mInflater.inflate(R.layout.app_schedule_show_page, null);

		tv0 = (TextView) myView.findViewById(R.id.show_tv0);

		tv1_2 = (TextView) myView.findViewById(R.id.show_tv1_2);
		tv1_3 = (TextView) myView.findViewById(R.id.show_tv1_3);

		tv2_2 = (TextView) myView.findViewById(R.id.show_tv2_2);
		tv2_3 = (TextView) myView.findViewById(R.id.show_tv2_3);

		tv3_2 = (TextView) myView.findViewById(R.id.show_tv3_2);
		tv3_3 = (TextView) myView.findViewById(R.id.show_tv3_3);

		tv4_2 = (TextView) myView.findViewById(R.id.show_tv4_2);
		tv4_3 = (TextView) myView.findViewById(R.id.show_tv4_3);

		tv5_2 = (TextView) myView.findViewById(R.id.show_tv5_2);
		tv5_3 = (TextView) myView.findViewById(R.id.show_tv5_3);

		// Log.i("GetSchedule", week+"");

		SQLiteDatabase db;
		toDoDB = new Util_ToDoDB(context);
		db = toDoDB.getReadableDatabase();
		String sql = "select * from todo_schedule where todo_week=" + week;// 1为星期一
		mCursor = db.rawQuery(sql, null);
		Log.i("", sql);
		// 判断游标是否为空
		if (mCursor != null) {
			int i = 0, n = mCursor.getCount();
			mCursor.moveToFirst();
			Log.i("", "mCursor !=null");
			Log.i("n=?", n + "");

			// 遍历游标 11.
			while (!mCursor.isAfterLast()) {

				// 获得ID
				// int id = mCursor .getInt(0);
				// 获得用户名
				course[i] = mCursor.getString(3);
				Log.i("", mCursor.getString(3));
				// 获得密码
				add[i] = mCursor.getString(4);
				i++;
				mCursor.moveToNext();
			}
		}

		tv0.setText(days1[week - 1]);

		tv1_2.setText(course[0]);
		tv1_3.setText(add[0]);

		tv2_2.setText(course[1]);
		tv2_3.setText(add[1]);

		tv3_2.setText(course[2]);
		tv3_3.setText(add[2]);

		tv4_2.setText(course[3]);
		tv4_3.setText(add[3]);

		tv5_2.setText(course[4]);
		tv5_3.setText(add[4]);

		Log.i("tv", "已设置tv");
		mCursor.close();
		toDoDB.close();
		return myView;
	}

}
