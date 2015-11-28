package com.daguo.ui.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.daguo.R;
import com.daguo.util.pulllistview.XListView;
import com.daguo.util.pulllistview.XListView.IXListViewListener;

/**
 * 显示我的说说的界面
 * 
 * @author Bugs_Rabbit 時間： 2015-9-24 下午5:05:15
 */
public class UserInfo_MyShuoShuoAty extends Activity implements
		OnClickListener, OnItemClickListener, IXListViewListener {
	// views
	private ImageView backImageView;
	private XListView xListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_userinfo_myshuoshuo);
		init();
	}

	/**
	 * 初始化控件
	 */
	private void init() {
		backImageView = (ImageView) findViewById(R.id.back);
		xListView = (XListView) findViewById(R.id.xListView);
		backImageView.setOnClickListener(this);
		xListView.setOnItemClickListener(this);
	}
	

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			
			break;

		default:
			break;
		}
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {

	}
}
