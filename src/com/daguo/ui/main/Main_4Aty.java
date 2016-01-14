package com.daguo.ui.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MainLoginAty1;
import com.daguo.ui.message.MessageAty;
import com.daguo.ui.settings.Setting_AboutAty;
import com.daguo.ui.settings.Setting_App_IntroduceAty;
import com.daguo.ui.settings.Setting_App_UserAgreementAty;
import com.daguo.ui.settings.Setting_App_UserOpinion;
import com.daguo.ui.user.UserInfo_ModifyAty1;
import com.daguo.util.test.Test_Bitmap;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * 主页第四页 设置页
 * 
 * @author Bugs_Rabbit 時間： 2015-9-28 下午10:34:28
 */
@SuppressLint("WorldReadableFiles")
public class Main_4Aty extends Activity implements OnClickListener {
    Intent intent;

    /**
     * initViews
     */
    private TextView banbenTextView, jianjieTextView, xieyiTextView,
	    xiugaiTextView, tixingTextView, huancunTextView, fankuiTextView,
	    tuijianTextView, guanyuTextView, zhuxiaoTextView;
    private Button tuichuButton;
    private ImageView imageView1;

    /******************************/

    private String packageName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_main4);
	init();

    }

    /**
     * initViews
     * 
     */
    private void init() {
	banbenTextView = (TextView) findViewById(R.id.banben);
	jianjieTextView = (TextView) findViewById(R.id.jianjie);
	xieyiTextView = (TextView) findViewById(R.id.xieyi);
	xiugaiTextView = (TextView) findViewById(R.id.xiugai);
	tixingTextView = (TextView) findViewById(R.id.tixing);
	huancunTextView = (TextView) findViewById(R.id.huancun);
	fankuiTextView = (TextView) findViewById(R.id.fankui);
	tuijianTextView = (TextView) findViewById(R.id.tuijian);
	guanyuTextView = (TextView) findViewById(R.id.guanyu);
	zhuxiaoTextView = (TextView) findViewById(R.id.zhuxiao);
	tuichuButton = (Button) findViewById(R.id.tuichu);
	imageView1 = (ImageView) findViewById(R.id.back);

	jianjieTextView.setOnClickListener(this);
	xieyiTextView.setOnClickListener(this);
	xiugaiTextView.setOnClickListener(this);
	tixingTextView.setOnClickListener(this);
	huancunTextView.setOnClickListener(this);
	fankuiTextView.setOnClickListener(this);
	tuijianTextView.setOnClickListener(this);
	guanyuTextView.setOnClickListener(this);
	zhuxiaoTextView.setOnClickListener(this);
	tuichuButton.setOnClickListener(this);
	imageView1.setOnClickListener(this);
	
	banbenTextView.setOnClickListener(this);
	setPackageInfo();

    }

    /**
     * 获得版本号
     */
    void setPackageInfo() {
	try {
	    packageName = "V "
		    + Main_4Aty.this.getPackageManager().getPackageInfo(
			    this.getPackageName(), 0).versionName;
	} catch (NameNotFoundException e) {
	    e.printStackTrace();
	    packageName = "";
	}
	banbenTextView.setText(packageName);

    }

    @Override
    public void onClick(View arg0) {
	switch (arg0.getId()) {

	case R.id.jianjie:// 大果简介
	    // Toast.makeText(Main_4Aty.this, "这是简介",
	    // Toast.LENGTH_SHORT).show();
	    intent = new Intent(Main_4Aty.this, Setting_App_IntroduceAty.class);
	    startActivity(intent);
	    break;
	case R.id.xieyi:// 用户协议
	    // Toast.makeText(Main_4Aty.this, "这是协议",
	    // Toast.LENGTH_SHORT).show();
	    intent = new Intent(Main_4Aty.this,
		    Setting_App_UserAgreementAty.class);
	    startActivity(intent);
	    break;
	case R.id.xiugai:// 修改资料
	    intent = new Intent(Main_4Aty.this, UserInfo_ModifyAty1.class);
	    startActivity(intent);
	    break;
	case R.id.tixing:// 消息中心
	    // Toast.makeText(Main_4Aty.this, "这是消息中心",
	    // Toast.LENGTH_SHORT).show();
	    intent = new Intent(Main_4Aty.this, MessageAty.class);
	    startActivity(intent);
	    break;
	case R.id.huancun:// 清除缓存
	    CustomProgressDialog.createDialog(Main_4Aty.this, "清除中。。。", 2000)
		    .show();

	    break;
	case R.id.fankui:// 反馈

	    intent = new Intent(Main_4Aty.this, Setting_App_UserOpinion.class);
	    startActivity(intent);
	    break;
	case R.id.tuijian:// 推荐给好友
	    Toast.makeText(Main_4Aty.this, "推荐给好友", Toast.LENGTH_SHORT).show();
	    break;

	case R.id.back:

	    break;

	case R.id.tuichu:// 退出
	    new AlertDialog.Builder(Main_4Aty.this)
		    .setMessage("确定要退出吗？")
		    .setPositiveButton("确定",
			    new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0,
					int arg1) {
				    android.os.Process
					    .killProcess(android.os.Process
						    .myPid());
				    System.exit(0);
				}
			    }).setNegativeButton("取消", null).create().show();

	    break;
	case R.id.zhuxiao:// 注销 切换账号
	    new AlertDialog.Builder(Main_4Aty.this)
		    .setMessage("确定要注销吗？")
		    .setPositiveButton("确定",
			    new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface arg0,
					int arg1) {
				    @SuppressWarnings("deprecation")
				    SharedPreferences sp = getSharedPreferences(
					    "userinfo",
					    Context.MODE_WORLD_READABLE);
				    Editor editor = sp.edit();
				    editor.putString("tel", "");
				    editor.putString("school_id", "");
				    editor.putString("pro_name", "");
				    editor.putString("birthday", "");
				    editor.putString("sex", "");
				    editor.putString("head_info", "");
				    editor.putString("school_name", "");
				    editor.putString("start_year", "");
				    editor.putString("score", "");
				    editor.putString("id_card", "");
				    editor.putString("id_card_copy", "");
				    editor.putString("address", "");
				    editor.putString("name", "");
				    editor.putString("stu_card_copy", "");
				    editor.putString("id", "");
				    editor.commit();
				    // System.exit(0);
				    Intent intent = new Intent(Main_4Aty.this,
					    MainLoginAty1.class);
				    startActivity(intent);
				    finish();
				}
			    }).setNegativeButton("取消", null).create().show();

	    break;

	case R.id.guanyu:// 关于大果校园
	    Intent intent1 = new Intent(Main_4Aty.this, Setting_AboutAty.class);
	    startActivity(intent1);
	    break;
	case R.id.banben:
//	    Intent i=new Intent(Main_4Aty.this ,Test_Bitmap.class);
//	    startActivity(i);
	    break;

	default:
	    break;
	}

    }
}
