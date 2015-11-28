package com.daguo.ui.school;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuo_WriteAty;
import com.daguo.ui.user.UserInfo_ModifyAty1;

/**
 * 
 * @author Bugs_rabbit
 * @function 校园主页 模仿同学说软件
 */
@SuppressWarnings("deprecation")
public class School_MainAty extends TabActivity {
	private TabHost tabHost;
	String p_name, p_school, p_sex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_name = sp.getString("name", "");
		p_school = sp.getString("school_name", "");
		p_sex = sp.getString("sex", "");

		tabHost = getTabHost();
		setTabs();

	}

	void setTabs() {
		addTabs("首页", R.drawable.school_main_tabselector_drawable1,
				School_Main1Aty.class);
		addTabs("活动", R.drawable.school_main_tabselector_drawable2,
				School_Main2Aty.class);
		addTabs("", R.drawable.bg_no, School_Main2Aty.class);
		addTabs("课外", R.drawable.school_main_tabselector_drawable3,
				School_Main3Aty.class);
		addTabs("我", R.drawable.school_main_tabselector_drawable4,
				School_Main4Aty.class);
	}

	/**
	 * 
	 * @param labelId
	 *            标签
	 * @param drawableId
	 *            图片路径
	 * @param c
	 *            跳转的class 动态添加每个tab的方法。
	 */
	void addTabs(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.item_school_main_tabindicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		// getResources().getDrawable(drawableId).setBounds(0, 0, 40, 40);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);

	}

	/**
	 * 
	 * @param b
	 *            参数view 点击事件，此处是覆盖的方法使底部焦点被覆盖。
	 */
	public void openCameraActivity(View b) {

		if (check()) {

			Intent intent = new Intent(School_MainAty.this,
					SC_ShuoShuo_WriteAty.class);
			startActivity(intent);
		} else {
			new AlertDialog.Builder(School_MainAty.this)
					.setTitle("提示")
					.setMessage("您的信息尚未完善，请先完善！")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									Intent intent = new Intent(
											School_MainAty.this,
											UserInfo_ModifyAty1.class);
									startActivity(intent);
								}
							}).setNegativeButton("取消", null).create().show();
		}
	}
/**
 * 检查个人信息
 * @return
 */
	boolean check() {
		if (p_name != null && !p_name.equals("") && p_school != null
				&& !p_school.equals("") && p_sex != null && !p_sex.equals("")) {

			return true;
		} else {
			return false;

		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(getBaseContext(), MainActivity.class);
			startActivity(intent);
			finish();
		}
		return true;

	}
}
