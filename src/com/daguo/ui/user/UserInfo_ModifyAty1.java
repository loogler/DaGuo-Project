package com.daguo.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.base.CircularImage;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.UploadUtil;
import com.daguo.view.dialog.CustomProgressDialog;
import com.daguo.view.dialog.MySelf_Mod_Photo;

public class UserInfo_ModifyAty1 extends Activity implements OnClickListener {
	/**
	 * init views
	 */
	private LinearLayout ll1, ll2, ll3, ll4, ll5, ll6, ll7;
	private TextView nickTextView, genderTextView, schoolTextView,
			schoolyearTextView, departmentTextView, stunumberTextView;
	private TextView backTextView;
	private ImageView iv1, iv2;
	private Button sendButton;
	private CircularImage photo;

	/**
	 * user info
	 */

	private String p_nick, p_id, p_photo, p_gender, p_schoolName, p_schoolYear,
			p_department, p_stuNumber, p_schoolCardCopy;

	/**
	 * tools
	 */
	private String tag = "UserInfo_ModifyAty1";
	Message mMessage;// 消息
	boolean isInfoChange;// 判断资料是否修改
	private File tempFile;
	private File uploadFile;
	private boolean isHeadCopy = false, isStuCopy = false;

	// 拍照参数
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	// 弹出窗
	MySelf_Mod_Photo menuWindow;
	// dialog
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_userinfo_modify1);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_nick = sp.getString("name", "");
		p_id = sp.getString("id", "");
		p_photo = sp.getString("head_info", "");
		p_gender = sp.getString("sex", "");
		p_schoolName = sp.getString("school_name", "");
		p_schoolYear = sp.getString("start_year", "");
		p_department = sp.getString("pro_name", "");
		p_stuNumber = sp.getString("id_card", "");
		p_schoolCardCopy = sp.getString("stu_card_copy", "");
		initViews();
	}

	/**
	 * init views
	 */
	private void initViews() {
		backTextView = (TextView) findViewById(R.id.back);

		ll1 = (LinearLayout) findViewById(R.id.ll1);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		ll3 = (LinearLayout) findViewById(R.id.ll3);
		ll4 = (LinearLayout) findViewById(R.id.ll4);
		ll5 = (LinearLayout) findViewById(R.id.ll5);
		ll6 = (LinearLayout) findViewById(R.id.ll6);
		ll7 = (LinearLayout) findViewById(R.id.ll7);

		photo = (CircularImage) findViewById(R.id.photo);
		nickTextView = (TextView) findViewById(R.id.nick);
		genderTextView = (TextView) findViewById(R.id.gender);
		schoolTextView = (TextView) findViewById(R.id.school);
		schoolyearTextView = (TextView) findViewById(R.id.schoolyear);
		departmentTextView = (TextView) findViewById(R.id.department);
		stunumberTextView = (TextView) findViewById(R.id.stunumber);

		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);

		sendButton = (Button) findViewById(R.id.tijiao);

		backTextView.setOnClickListener(this);
		photo.setOnClickListener(this);
		ll1.setOnClickListener(this);
		ll2.setOnClickListener(this);
		ll3.setOnClickListener(this);
		ll4.setOnClickListener(this);
		ll5.setOnClickListener(this);
		ll6.setOnClickListener(this);
		ll7.setOnClickListener(this);

		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);

		sendButton.setOnClickListener(this);
		// //////////////////////////
		FinalBitmap.create(UserInfo_ModifyAty1.this).display(photo,
				HttpUtil.IMG_URL + p_photo);
		FinalBitmap.create(UserInfo_ModifyAty1.this).display(iv1,
				HttpUtil.IMG_URL + p_schoolCardCopy);
		setData(p_nick, nickTextView);
		setData(p_schoolName, schoolTextView);
		setData(p_schoolYear, schoolyearTextView);
		setData(p_department, departmentTextView);
		setData(p_stuNumber, stunumberTextView);

		if (infoCheck(p_gender)) {
			if (p_gender.equals("0")) {
				genderTextView.setText("男");
			} else if (p_gender.equals("1")) {
				genderTextView.setText("女");
			}
		} else {
			genderTextView.setText("");
		}

	}

	/************************* 线程 *************************************/
	/**
	 * 初始化的线程 从网络获取个人资料
	 */

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 10:

				break;
			case 13:// 处理相片上传服务器以后返回的消息
				if (UploadUtil.rescode == 200) {
					try {
						JSONObject jsonObject1 = new JSONObject(
								msg.obj.toString());
						String imgSRC = jsonObject1
								.getString("fileRelativePath");
						if (isHeadCopy && infoCheck(imgSRC)) {
							p_photo = imgSRC;
						} else if (isStuCopy && infoCheck(imgSRC)) {
							p_schoolCardCopy = imgSRC;
						} else {
							// 都没动
						}

					} catch (Exception e) {
					}

					Toast.makeText(UserInfo_ModifyAty1.this, "上传成功，请提交资料",
							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(UserInfo_ModifyAty1.this, "上传失败，请重新上传",
							Toast.LENGTH_SHORT).show();

				}
				break;
			default:
				break;
			}

		};
	};
	Thread modifyDataThread = new Thread(new Runnable() {
		public void run() {

			try {

				String url = HttpUtil.SUBMIT_USERINFO;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", p_id);
				map.put("name", p_nick);
				map.put("head_info", p_photo);
				map.put("sex", p_gender);
				map.put("school", p_schoolName);
				map.put("pro_name", p_department);
				map.put("id_card", p_stuNumber);

				map.put("start_year", p_schoolYear);
				map.put("birthday", "");
				map.put("stu_card_copy", "");
				map.put("id_card_copy", p_stuNumber);
				String res = HttpUtil.postRequest(url, map);
				if (res != null) {
					// success

					SharedPreferences sp = getSharedPreferences("userinfo",
							Context.MODE_WORLD_READABLE);
					Editor ed = sp.edit();

					ed.putString("name", p_nick);
					ed.putString("head_info", p_photo);
					ed.putString("sex", p_gender);
					ed.putString("school_name", p_schoolName);
					ed.putString("pro_name", p_department);
					ed.putString("id_card", "");
					ed.putString("address", "");
					ed.putString("start_year", p_schoolYear);
					ed.putString("birthday", "");
					ed.putString("stu_card_copy", p_schoolCardCopy);
					ed.putString("id_card_copy", p_stuNumber);

					ed.commit();
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							Toast.makeText(UserInfo_ModifyAty1.this, "修改成功",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});
				} else {
					// fail
					dialog.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(UserInfo_ModifyAty1.this, "修改失败了！",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});

				}

			} catch (Exception e) {
			}

		}
	});

	Thread initDataThread = new Thread(new Runnable() {
		public void run() {

			String url = HttpUtil.QUERY_USERINFO;
			Map<String, String> map = new HashMap<String, String>();
			map.put("id", p_id);
			String res;
			try {
				res = HttpUtil.postRequest(url, map);

				JSONObject js = new JSONObject(res);
				JSONArray array = js.getJSONArray("rows");
				if (array.length() > 0) {
					// 有值
					String namesss = array.optJSONObject(0).getString("name");
					String sexsss = array.optJSONObject(0).getString("sex");
					String schoolsss = array.optJSONObject(0).getString(
							"school_name");
					String departmentsss = array.optJSONObject(0).getString(
							"pro_name");
					String idcardsss = array.optJSONObject(0).getString(
							"id_card");

					String schoolyearsss = array.optJSONObject(0).getString(
							"start_year");
					String idcardCopysss = array.optJSONObject(0).getString(
							"id_card_copy");
					String schoolcardCopysss = array.optJSONObject(0)
							.getString("stu_card_copy");
					String birthdaysss = array.optJSONObject(0).getString(
							"birthday");
					String img = array.optJSONObject(0).getString("head_info");

					if (infoCheck(namesss)) {
						p_nick = namesss;
					} else {
						p_nick = "";
					}
					if (infoCheck(sexsss)) {
						p_gender = sexsss;
					} else {
						p_gender = "";
					}
					if (infoCheck(schoolsss)) {
						p_schoolName = schoolsss;
					} else {
						p_schoolName = "";
					}
					if (infoCheck(departmentsss)) {
						p_department = departmentsss;
					} else {
						p_department = "";
					}
					if (infoCheck(idcardsss)) {
						p_stuNumber = idcardsss;
					} else {
						p_stuNumber = "";
					}

					if (infoCheck(schoolyearsss)) {
						p_schoolYear = schoolyearsss;

					} else {
						p_schoolYear = "";
					}

					// if (infoCheck(schoolcardCopysss)) {
					// schoolcardCopy = schoolcardCopysss;
					//
					// } else {
					// schoolcardCopy = "";
					// }
					// if (infoCheck(birthdaysss)) {
					// birthday = birthdaysss;
					// } else {
					// birthday = "";
					// }
					if (infoCheck(img)) {
						p_photo = img;
					} else {
						p_photo = "";
					}

					mMessage = handler.obtainMessage(0);
					mMessage.sendToTarget();

				} else {
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(UserInfo_ModifyAty1.this, "服务器异常！",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	});

	/****************** implement onclick ***********************/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();

			break;
		case R.id.ll1:
			isHeadCopy=true;
			isStuCopy=false;
			setBack(ll1);
			modifyPhoto();
			reSetBack();
			break;
		case R.id.ll2:
			setBack(ll2);
			modifyName();
			reSetBack();
			break;
		case R.id.ll3:
			setBack(ll3);
			modifySex();
			reSetBack();
			break;
		case R.id.ll4:
			setBack(ll4);
			modifySchool();
			reSetBack();
			break;
		case R.id.ll5:
			setBack(ll5);
			modifySchoolYear();
			reSetBack();
			break;
		case R.id.ll6:
			setBack(ll6);
			modifyDepartment();
			reSetBack();
			break;
		case R.id.ll7:
			setBack(ll7);
			modifyStuNumber();
			reSetBack();
			break;

		case R.id.iv1:
			modifyPhoto();
			isHeadCopy=false;
			isStuCopy=true;
			break;

		case R.id.tijiao:
			dialog=CustomProgressDialog.createDialog(UserInfo_ModifyAty1.this, "加载中。。。。");
			dialog.show();
			modifyDataThread.start();
			break;

		// //////////调用相机////////////////////////////
		case R.id.btn_take_photo:
			menuWindow.dismiss();
			// 调用系统的拍照功能
			Log.i(tag, "拍照========");
			Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			tempFile = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName(2));
			takeintent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(takeintent, PHOTO_REQUEST_TAKEPHOTO);
			break;
		case R.id.btn_pick_photo:
			menuWindow.dismiss();
			Log.i(tag, "选择照片========");
			Intent pickintent = new Intent(Intent.ACTION_PICK, null);
			pickintent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(pickintent, PHOTO_REQUEST_GALLERY);
			break;

		default:
			break;
		}

	}

	/**
	 * 照片上传
	 */
	private void modifyPhoto() {
		menuWindow = new MySelf_Mod_Photo(UserInfo_ModifyAty1.this, this);// shilihua
		menuWindow.showAtLocation(
				UserInfo_ModifyAty1.this.findViewById(R.id.rl), Gravity.BOTTOM
						| Gravity.CENTER_HORIZONTAL, 0, 0);
		Log.i(tag, "menuWindow执行完毕");
	}

	/**
	 * 修改名字
	 */
	private void modifyName() {

		LayoutInflater inflater = LayoutInflater.from(UserInfo_ModifyAty1.this);
		final View textEntryView = inflater.inflate(R.layout.dialog_infochange,
				null);
		final EditText edit = (EditText) textEntryView.findViewById(R.id.dia2);

		new AlertDialog.Builder(UserInfo_ModifyAty1.this).setTitle("姓名")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(textEntryView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String str = edit.getText().toString();
						p_nick = str;
						nickTextView.setText("" + str);
						isInfoChange = true;
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}

	/**
	 * 修改性别
	 */
	private void modifySex() {
		final String[] asd = new String[] { "男", "女" };
		{
			new AlertDialog.Builder(UserInfo_ModifyAty1.this)
					.setItems(asd, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// ssss = asd[arg1];

							genderTextView.setText(asd[arg1]);
							if (asd[arg1].equals("男")) {
								p_gender = "0";
							} else if (asd[arg1].equals("女")) {
								p_gender = "1";
							}
							isInfoChange = true;

						}
					}).create().show();
		}

	}

	/**
	 * 修改学校信息 不可修改
	 */
	private void modifySchool() {
		new AlertDialog.Builder(UserInfo_ModifyAty1.this)
				.setMessage("修改学校需联系管理员").setPositiveButton("确定", null)
				.create().show();
	}

	/**
	 * 修改入学时间
	 */
	private void modifySchoolYear() {
		new DatePickerDialog(UserInfo_ModifyAty1.this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker arg0, int year, int mon,
							int day) {
						schoolyearTextView.setText(year + "-" + (mon + 1) + "-"
								+ day);

						p_schoolYear = year + "-" + (mon + 1) + "-" + day;
						isInfoChange = true;

					}
				}, 2010, 01, 01).show();
	}

	/**
	 * 修改专业 不可修改
	 */
	private void modifyDepartment() {
		new AlertDialog.Builder(UserInfo_ModifyAty1.this)
				.setMessage("修改专业需联系管理员").setPositiveButton("确定", null)
				.create().show();
	}

	/**
	 * 修改学号
	 */
	private void modifyStuNumber() {
		LayoutInflater inflater = LayoutInflater.from(UserInfo_ModifyAty1.this);
		final View textEntryView = inflater.inflate(R.layout.dialog_infochange,
				null);
		final EditText edit = (EditText) textEntryView.findViewById(R.id.dia2);

		new AlertDialog.Builder(UserInfo_ModifyAty1.this).setTitle("学号")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setView(textEntryView)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String str = edit.getText().toString();
						p_stuNumber = str;
						stunumberTextView.setText("" + str);
						isInfoChange = true;
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}

	/**************************** tools ***************************/
	/**
	 * 
	 * @param a
	 *            data
	 * @param b
	 *            views
	 */
	private void setData(String a, TextView b) {
		if (infoCheck(a)) {
			b.setText(a);
		} else {
			b.setText("未填写");
		}
	}

	/**
	 * 
	 * @param res
	 *            param
	 * @return 是否通过检测
	 */
	private boolean infoCheck(String res) {
		if (res != null && !res.equals("") && !res.equals("null")) {
			return true;
		} else {
			return false;
		}
	}

	private void setBack(LinearLayout ll) {
		ll.setBackgroundColor(getResources().getColor(R.color.gray));
		

	}

	private void reSetBack() {
		ll1.setBackgroundColor(getResources().getColor(R.color.white));
		ll2.setBackgroundColor(getResources().getColor(R.color.white));
		ll3.setBackgroundColor(getResources().getColor(R.color.white));
		ll4.setBackgroundColor(getResources().getColor(R.color.white));
		ll5.setBackgroundColor(getResources().getColor(R.color.white));
		ll6.setBackgroundColor(getResources().getColor(R.color.white));
		ll7.setBackgroundColor(getResources().getColor(R.color.white));
	}

	/*************** 照片处理部分 **********************************************/
	/**
	 * 获取照片，名称 使用系统当前日期加以调整作为照片的名称
	 */
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName(int i) {
		Log.i(tag, "getPhotoFileName获得照片名称");
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		if (i == 1) {
			return dateFormat.format(date) + "_crop.JPEG";
		} else {

			return dateFormat.format(date) + ".JPEG";
		}
	}

	/*** 拍照 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(tag, "void onActivityResult拍照/选照片");
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
			// if (data != null)
			startPhotoZoom(Uri.fromFile(tempFile), 300);
			break;

		case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
			// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
			if (data != null)
				startPhotoZoom(data.getData(), 300);
			break;

		case PHOTO_REQUEST_CUT:// 返回的结果
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				uploadFile = saveToLocal(bitmap);
				setPicToView(bitmap);

				new Thread(new Runnable() {
					public void run() {

						Log.i(tag, "UploadPhoto上传至网站");

						String request = null;
						String url = HttpUtil.IMG_URL_SUB;
						System.out.println("uploadFile--->"
								+ uploadFile.getName());
						// UploadPhotoUtil up = new UploadPhotoUtil();

						request = UploadUtil.uploadFile(uploadFile, url);
						// progressDialog.dismiss();
						mMessage = handler.obtainMessage(13);

						mMessage.obj = request;
						mMessage.sendToTarget();

					}
				}).start();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/***
	 * void startActivityForResult
	 * */
	private void startPhotoZoom(Uri uri, int size) {
		Log.i(tag, "void startPhotoZoom还是拍照/选照片，裁剪");

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/**** 将进行剪裁后的图片显示到UI界面上 */
	private void setPicToView(Bitmap bitmap) {

		Drawable drawable = new BitmapDrawable(bitmap);
		if (isHeadCopy) {
			photo.setImageDrawable(drawable);

		} else if (isStuCopy) {
			iv1.setImageDrawable(drawable);
		}

		// picChange = 1;
		isInfoChange = true;
		Log.i(tag, "setPicToView显示至ui");
		Toast.makeText(getApplicationContext(), "照片已提交！", Toast.LENGTH_LONG)
				.show();
		;
	}

	/** 保存至本地 */
	public File saveToLocal(Bitmap bitmap) {
		Log.i(tag, "saveToLocal保存至本地");
		// 需要裁剪后保存为新图片

		File f = new File(Environment.getExternalStorageDirectory(),
				getPhotoFileName(1));
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * fileName = f.getName(); uploadFilePath = f.getPath();
		 */
		return f;

	}

}
