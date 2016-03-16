package com.daguo.modem.schedule;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
/**
 * 
 * @author Bugs_rabbit
 * @function  加载备忘录主界面listview的 适配器
 */
public class Adapter_Folks extends BaseAdapter {
	Context context;
	Cursor cursor;
	int screenWidth;
	int screenHeight;

	public Adapter_Folks(Context context, Cursor cursor) {
		this.context = context;
		this.cursor = cursor;
		screenWidth = Util_PhoneInfo.getScreenWidth((Activity) context);
		screenHeight = Util_PhoneInfo.getSreenHeight((Activity) context);
		// System.out.println("screenWidth=="+screenWidth);
		// System.out.println("screenHeight=="+screenHeight);
	}

	@Override
	public int getCount() {
		return cursor.getCount();
	}

	@Override
	public String getItem(int position) {
		// cursor.move(position);
		return cursor.getString(1);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView tv1;
		LinearLayout llLayout;
		convertView = LayoutInflater.from(context).inflate(R.layout.list_1,
				null);
		llLayout = (LinearLayout) convertView.findViewById(R.id.ll1);
		tv1 = (TextView) convertView.findViewById(R.id.lv1);

		switch (position % 6) {
		case 1:
			tv1.setBackgroundResource(R.drawable.remind_list_background_2);
			break;
		case 2:
			tv1.setBackgroundResource(R.drawable.remind_list_background_3);
			break;
		case 3:
			tv1.setBackgroundResource(R.drawable.remind_list_background_4);
			break;
		case 4:
			tv1.setBackgroundResource(R.drawable.remind_list_background_5);
			break;
		case 5:
			tv1.setBackgroundResource(R.drawable.remind_list_background_6);
			break;
		case 0:
			tv1.setBackgroundResource(R.drawable.remind_list_background_1);
			break;

		default:
			tv1.setBackgroundResource(R.drawable.remind_list_background_1);
			break;
		}

		// tv1.setBackgroundResource(R.drawable.remind_list_background_1);
		// Log.i("getView", position+"");

		// txtView.setText(pList.get(position)); //这里给tv赋上数据库里面的值
		cursor.moveToFirst();
		for (int i = 0; i <= position; i++) {

			tv1.setText(cursor.getString(1));
			cursor.moveToNext();
		}
		// Log.i("getView", position+"");

		Animation animation_new = new TranslateAnimation(position * 50
				+ screenWidth / 5, 0, 0, 0); // screenHeight*5/10
		animation_new.setDuration(400);
		llLayout.setAnimation(animation_new);
		return convertView;
	}

}
