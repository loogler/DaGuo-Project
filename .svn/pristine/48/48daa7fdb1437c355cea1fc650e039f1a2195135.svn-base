package com.daguo.modem.writeshuoshuo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.daguo.modem.writeshuoshuo.adapter.FolderAdapter;
import com.daguo.modem.writeshuoshuo.util.Bimp;
import com.daguo.modem.writeshuoshuo.util.PublicWay;
import com.daguo.modem.writeshuoshuo.util.Res;
import com.daguo.ui.before.MyAppliation;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-9 下午4:02:37
 * @function ：这个类主要是用来进行显示包含图片的文件夹
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(Res.getLayoutID("plugin_camera_image_file"));
		PublicWay.activityList.add(this);
		MyAppliation.getInstance().addActivity(this);
		mContext = this;
		bt_cancel = (Button) findViewById(Res.getWidgetID("cancel"));
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(Res
				.getWidgetID("fileGridView"));
		TextView textView = (TextView) findViewById(Res
				.getWidgetID("headerTitle"));
		textView.setText(Res.getString("photo"));
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			// 清空选择的图片
			Bimp.tempSelectBitmap.clear();
			finish();
			Intent intent = new Intent();
			intent.setClass(mContext, SC_ShuoShuo_WriteAty.class);
			startActivity(intent);
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			Intent intent = new Intent();
			intent.setClass(mContext, SC_ShuoShuo_WriteAty.class);
			startActivity(intent);
		}

		return true;
	}

}
