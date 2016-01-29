package com.daguo.ui.before;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daguo.R;
import com.daguo.utils.GetScreenRecUtil;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-23 下午5:19:51
 * @function ：
 */
public class MainBeforeLoginActivity extends Activity implements
	OnClickListener {

    /**
     * initViews
     */
    private Button register_btn, login_btn;
    private View view;
    private LinearLayout ll;// 跟布局

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_mainbeforelogin);
	MyAppliation.getInstance().addActivity(this);//
	initViews();
    }

    /**
     * 
     */
    private void initViews() {
	ll = (LinearLayout) findViewById(R.id.ll);
	register_btn = (Button) findViewById(R.id.register_btn);
	login_btn = (Button) findViewById(R.id.login_btn);
	view = findViewById(R.id.view);
	setViewHeight(view);
	setBack();

	register_btn.setOnClickListener(this);
	login_btn.setOnClickListener(this);
    }

    /**
     * 获取view高度， view== View
     */
    private void setViewHeight(View view) {
	int height = GetScreenRecUtil
		.getWindowHeigh(MainBeforeLoginActivity.this);
	int width = GetScreenRecUtil.getWindowWidth(this);
	view.setLayoutParams(new LinearLayout.LayoutParams(width, height / 2));
    }

    /**
     * 设置背景图，减少内存开支
     */
    private void setBack() {

	// BitmapFactory.Options opt = new BitmapFactory.Options();
	//
	// opt.inPreferredConfig = Bitmap.Config.RGB_565;
	//
	// opt.inPurgeable = true;
	//
	// opt.inInputShareable = true;
	//
	// //获取资源图片
	//
	// InputStream is = getResources().;
	//
	// BitmapFactory.decodeStream(is,null,opt);

	Bitmap bm = BitmapFactory.decodeResource(getResources(),
		R.drawable.bg_beforelogin);

	BitmapDrawable bd = new BitmapDrawable(bm);

	ll.setBackground(bd);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.register_btn:
	    setRegister_btn(register_btn);
	    break;
	case R.id.login_btn:
	    setLogin_btn(login_btn);
	    break;

	default:
	    break;
	}
    }

    /*********************** onclick 事件 ********************************/
    /**
     * 注册事件
     * 
     * @param register_btn
     *            the register_btn to set
     */
    public void setRegister_btn(Button register_btn) {
	this.register_btn = register_btn;
	Intent intent = new Intent(MainBeforeLoginActivity.this,
		MainRegisterAty1.class);
	startActivity(intent);

    }

    /**
     * 登录事件
     * 
     * @param login_btn
     *            the login_btn to set
     */
    public void setLogin_btn(Button login_btn) {
	this.login_btn = login_btn;
	Intent intent = new Intent(MainBeforeLoginActivity.this,
		MainLoginAty1.class);
	startActivity(intent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onDestroy()
     */
    @Override
    protected void onDestroy() {
	System.gc();
	super.onDestroy();
    }

}
