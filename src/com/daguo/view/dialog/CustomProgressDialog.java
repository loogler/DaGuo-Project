package com.daguo.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.widget.TextView;

import com.daguo.R;

public class CustomProgressDialog extends Dialog {
	private Context context = null;
	private static CustomProgressDialog customProgressDialog = null;
	

	public CustomProgressDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomProgressDialog(Context context, int theme) {
		super(context, theme);

	}
/**
 * 用于制作加载完成自动消除的dialog  而不是自动消失
 * @param context
 * @param message
 * @return
 */
	public CustomProgressDialog creatDia(Context context, String message) {
		customProgressDialog = new CustomProgressDialog(context, R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.dialog_customprogress);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setMessage("" + message);
		return customProgressDialog;

	}

	public static CustomProgressDialog createDialog(Context context,
			String message) {
		customProgressDialog = new CustomProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.dialog_customprogress);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setMessage("" + message);
		return customProgressDialog;
	}
/**
 * 定时消失的dialog
 * @param context
 * @param message
 * @param time
 * @return
 */
	public static CustomProgressDialog createDialog(Context context,
			String message, int time) {

		customProgressDialog = new CustomProgressDialog(context,
				R.style.CustomProgressDialog);
		customProgressDialog.setContentView(R.layout.dialog_customprogress);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setMessage("" + message);
		customProgressDialog.setTime(time);

		return customProgressDialog;

	}

	public CustomProgressDialog setTime(int time) {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				customProgressDialog.dismiss();
			}
		}, time);

		return null;

	}

	/** 标题哦 */
	public CustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/** 提示内容 */
	public CustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;

	}

	/** 用于开启这条线程的图片资源 if(onWindowFocusChanged ==true ) 启动.start */
	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		// ImageView imageView = (ImageView)
		// customProgressDialog.findViewById(R.id.loadingImageView);
		// AnimationDrawable animationDrawable = (AnimationDrawable)
		// imageView.getBackground();
		// animationDrawable.start();
	}

}
