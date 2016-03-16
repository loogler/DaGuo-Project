/**
 * 互相学习 共同进步
 */
package com.daguo.ui.message;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午7:07:15
 * @function ：
 */
public class MessageAty extends FragmentActivity implements OnClickListener {
    /**
     * initViews
     */
    private TextView back_tv, notifer_tv, chat_tv;
    private LinearLayout title_ll;
    private FrameLayout frame;

    private Message_Tab1Fragment tab1Fragment;
    private Message_Tab2Fragment tab2Fragment;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_message);
	initViews();
	replaceFragment1();

    }

    private void initViews() {
	back_tv = (TextView) findViewById(R.id.back_tv);
	notifer_tv = (TextView) findViewById(R.id.notifer_tv);
	chat_tv = (TextView) findViewById(R.id.chat_tv);

	title_ll = (LinearLayout) findViewById(R.id.title_ll);
	frame = (FrameLayout) findViewById(R.id.frame);

	back_tv.setOnClickListener(this);
	notifer_tv.setOnClickListener(this);
	chat_tv.setOnClickListener(this);

    }

    /*----------------------implements----------------------------*/

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back_tv:
	    finish();

	    break;
	case R.id.notifer_tv:

	    replaceFragment1();

	    break;
	case R.id.chat_tv:

	    replaceFragment2();

	    break;

	default:
	    break;
	}
    }

    /*-------------------tools---------------------------*/

    private void replaceFragment1() {
	title_ll.setBackgroundResource(R.drawable.button_whitegreen);
	notifer_tv.setTextColor(getResources().getColor(R.color.green_home));
	chat_tv.setTextColor(getResources().getColor(R.color.white));
	FragmentManager manager = getSupportFragmentManager();
	tab1Fragment = new Message_Tab1Fragment();
	FragmentTransaction transaction = manager.beginTransaction();
	transaction.replace(R.id.frame, tab1Fragment);
	transaction.commit();
    }

    private void replaceFragment2() {
	title_ll.setBackgroundResource(R.drawable.button_greenwhite);
	notifer_tv.setTextColor(getResources().getColor(R.color.white));
	chat_tv.setTextColor(getResources().getColor(R.color.green_home));
	FragmentManager manager = getSupportFragmentManager();
	tab2Fragment = new Message_Tab2Fragment();
	FragmentTransaction transaction = manager.beginTransaction();
	transaction.replace(R.id.frame, tab2Fragment);
	transaction.commit();
    }

}
