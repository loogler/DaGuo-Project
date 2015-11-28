package com.daguo.modem.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * 
 * @author Bugs_rabbit
 * @function 存储课表的数据库
 */
public class Util_ToDoDB extends SQLiteOpenHelper {
	private final static String DATABASE_NAME = "todo_db";
	private final static int DATABASE_VERSION = 3;

	private final String REMIND_TABLE = "todo_table";
	private final String SCHEDULE_TABLE = "todo_schedule";

	public final String FIELD_id = "_id";

	public final String REMIND_TV = "todo_remind";
	public final String REMIND_TIME = "todo_remind_time";
	public final String REMIND_TIME_ID = "todo_remind_timeId";

	public final String SCHEDULE_WEEK = "todo_week";
	public final String SCHEDULE_TV1 = "todo_section";
	public final String SCHEDULE_TV2 = "todo_course";
	public final String SCHEDULE_TV3 = "todo_add";

	// public SQLiteDatabase db;

	public Util_ToDoDB(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// this.db=db;
		//
		// intiTable();
		// }
		//
		// public void intiTable() {

		/* 建立table */
		String sql = "CREATE TABLE " + REMIND_TABLE + " (" + FIELD_id
				+ " INTEGER primary key autoincrement, " + REMIND_TV
				+ " text, " + REMIND_TIME + " text, " + REMIND_TIME_ID
				+ " text " + " )";
		Log.i("ToDoDB", "sql1代码如下:" + sql);
		db.execSQL(sql);

		sql = "CREATE TABLE " + SCHEDULE_TABLE + " (" + FIELD_id
				+ " INTEGER primary key autoincrement, " + " " + SCHEDULE_WEEK
				+ " text, " + SCHEDULE_TV1 + " text, " + SCHEDULE_TV2
				+ " text, " + SCHEDULE_TV3 + " text )";
		Log.i("ToDoDB", "sql2代码如下:" + sql);
		db.execSQL(sql);
		try {
			// ToDoDB_Schedule toDoDB_Schedule=new
			// ToDoDB_Schedule(ScheduleInsert.this, "Schedule");
			// SQLiteDatabase db=toDoDB_Schedule.getWritableDatabase();
			db.execSQL("drop table todo_schedule");
			db.execSQL("create table if not exists todo_schedule(_id int primary key,todo_week int,todo_section int,todo_course varchar,todo_add varchar)");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(1,1,1,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(2,1,2,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(3,1,3,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(4,1,4,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(5,1,5,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(6,2,1,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(7,2,2,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(8,2,3,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(9,2,4,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(10,2,5,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(11,3,1,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(12,3,2,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(13,3,3,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(14,3,4,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(15,3,5,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(16,4,1,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(17,4,2,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(18,4,3,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(19,4,4,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(20,4,5,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(21,5,1,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(22,5,2,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(23,5,3,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(24,5,4,'','')");
			db.execSQL("insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(25,5,5,'','')");
			/*
			 * db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(26,6,1,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(27,6,2,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(28,6,3,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(29,6,4,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(30,6,5,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(31,7,1,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(32,7,2,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(33,7,3,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(34,7,4,'空','空','空')"
			 * ); db.execSQL(
			 * "insert into todo_schedule(_id,todo_week,todo_section,todo_course,todo_add) values(35,7,5,'空','空','空')"
			 * );
			 */

			Log.i("", "已初始化数据库");

		} catch (Exception e) {
		}
	}

	// @Override
	// public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	// {
	// String sql = "DROP TABLE IF EXISTS "
	// + REMIND_TABLE;
	// db.execSQL(sql);
	// onCreate(db);
	// }
	public Cursor selectRemind() {
		SQLiteDatabase db = this.getReadableDatabase();
		String orderBy = REMIND_TIME_ID + " desc";
		Cursor cursor = db.query(REMIND_TABLE, null, null, null, null, null,
				orderBy);
		return cursor;
	}

	public long insertRemind(String text, String time, String timeStr) {
		SQLiteDatabase db = this.getWritableDatabase();
		/* 将新增的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(REMIND_TV, text);
		cv.put(REMIND_TIME, time);
		cv.put(REMIND_TIME_ID, timeStr);
		long row = db.insert(REMIND_TABLE, null, cv);

		return row;
	}

	public void delete(int id, String table) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		db.delete(table, where, whereValue);
	}

	public void updateRemind(int id, String text, String time, String timeStr) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		/* 将修改的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(REMIND_TV, text);
		cv.put(REMIND_TIME, time);
		cv.put(REMIND_TIME_ID, timeStr);
		db.update(REMIND_TABLE, cv, where, whereValue);
	}

	public void updateCourse(int id, String text) {

		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		/* 将修改的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(SCHEDULE_TV2, text);
		db.update(SCHEDULE_TABLE, cv, where, whereValue);
	}

	public void updateAdd(int id, String text) {
		SQLiteDatabase db = this.getWritableDatabase();
		String where = FIELD_id + " = ?";
		String[] whereValue = { Integer.toString(id) };
		/* 将修改的值放入ContentValues */
		ContentValues cv = new ContentValues();
		cv.put(SCHEDULE_TV3, text);
		db.update(SCHEDULE_TABLE, cv, where, whereValue);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
