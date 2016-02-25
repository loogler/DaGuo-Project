/**
 *2016-2-17下午5:16:43
 * * 
 * Copyright (c) 2015-2025 Founder Ltd. All Rights Reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Founder. You shall not disclose such Confidential Information 
 * and shall use it only in accordance with the terms of the agreements 
 * you entered into with Founder. 
 * 
 */

package com.daguo.view.dialog;

import com.daguo.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @E-mail 作者 E-mail:395360255@qq.com
 * @author BugsRabbit
 * @version 创建时间：2016-2-17 下午5:16:43
 * @function 功能:提示界面
 */

public class CustomAlertDialog extends Dialog {

	Context context;
	static CustomAlertDialog dialog = null;

	public CustomAlertDialog(Context context) {
		super(context);
		this.context = context;
	}

	public CustomAlertDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public static CustomAlertDialog createPositiveDialog(Context context,
			String msg) {
		dialog = new CustomAlertDialog(context, R.style.CustomProgressDialog);
		dialog.setContentView(R.layout.item_customalertdialog);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setMessage(msg);
		return dialog;

	}

	public static CustomAlertDialog createNegativeDialog(Context context,
			String msg) {

		dialog = new CustomAlertDialog(context, R.style.CustomProgressDialog);
		dialog.setContentView(R.layout.item_customalertdialog_neg);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		dialog.setMessageNeg(msg);
		return dialog;

	}

	/** 提示内容 */
	public CustomAlertDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) dialog.findViewById(R.id.msg_tv);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}
		Button reqBtn = (Button) dialog.findViewById(R.id.req_btn);
		// Button can_btn = (Button) dialog.findViewById(R.id.can_btn);

		reqBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();

			}
		});

		return dialog;

	}

	public CustomAlertDialog setMessageNeg(String msg) {
		TextView msg_tv = (TextView) dialog.findViewById(R.id.msg_tv);
		ImageView cancel_iv = (ImageView) dialog.findViewById(R.id.cancel_iv);
		if (msg_tv != null) {
			msg_tv.setText(msg);
		}

		cancel_iv.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		return dialog;
	}

}
