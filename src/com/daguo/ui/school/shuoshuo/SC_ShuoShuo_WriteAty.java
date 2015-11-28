package com.daguo.ui.school.shuoshuo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.UploadPhotoUtil;
import com.daguo.utils.UploadUtil;
import com.daguo.view.dialog.MySelf_Mod_Photo;

public class SC_ShuoShuo_WriteAty extends Activity implements OnClickListener {
	String tag = "SC_ShuoShuo_WriteAty";
	private EditText content;
	private TextView send;
	private ImageView add_photo, show_pic;
	private LinearLayout llLayout;

	private String c;
	private String p_id, p_name, img_path;
	// 弹出窗
	MySelf_Mod_Photo menuWindow;
	// 拍照参数
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果
	// 创建一个以当前时间为名称的文件，获取相册资源
	private File tempFile;
	private File uploadFile;

	Message message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_shuoshuo_write);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		p_name = sp.getString("name", "");

		init();

	}

	/**
	 * 初始化
	 */
	private void init() {
		llLayout = (LinearLayout) findViewById(R.id.linearLayout_1);
		content = (EditText) findViewById(R.id.shuoshuo_content);
		send = (TextView) findViewById(R.id.sendtv);
		add_photo = (ImageView) findViewById(R.id.add_pic);
		show_pic = (ImageView) findViewById(R.id.show_pic);

		send.setOnClickListener(this);
		add_photo.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.sendtv:
			// 发送
			c = content.getText().toString().trim();
			if (c != null && !c.equals("")) {
				new Thread(upLoad).start();

			} else {
				Toast.makeText(SC_ShuoShuo_WriteAty.this, "还没输入文字呢",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.add_pic:
			// 添加图片
			menuWindow = new MySelf_Mod_Photo(SC_ShuoShuo_WriteAty.this, this);// shilihua
			menuWindow
					.showAtLocation(SC_ShuoShuo_WriteAty.this
							.findViewById(R.id.linearLayout_1), Gravity.BOTTOM
							| Gravity.CENTER_HORIZONTAL, 0, 0);
			Log.i(tag, "menuWindow执行完毕");
			break;
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
			//
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

	Runnable upLoad = new Runnable() {

		@Override
		public void run() {
			if (uploadFile != null) {

				String request = null;
				String url = HttpUtil.IMG_URL_SUB;
				System.out.println("uploadFile--->" + uploadFile.getName());
				UploadPhotoUtil up = new UploadPhotoUtil();

				request = UploadUtil.uploadFile(uploadFile, url);
				JSONObject js;
				try {
					js = new JSONObject(request);
					img_path = js.getString("fileRelativePath");
				} catch (Exception e) {
					e.printStackTrace();
				}

				// progressDialog.dismiss();
				if (UploadUtil.rescode == 200) {
					// 上传成功 提交至服务器产生新说说。
					String u1 = HttpUtil.SUBMIT_SHUOSHUO;
					Map<String, String> map = new HashMap<String, String>();
					map.put("p_id", p_id);
					map.put("p_name", p_name);
					map.put("content", c);
					map.put("img_path", img_path);
					map.put("good_count", "0");
					map.put("feedback_count", "0");
					try {
						String res = HttpUtil.postRequest(u1, map);
						if (res != null) {
							// 成功
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(SC_ShuoShuo_WriteAty.this,
											"发表成功！", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(
											SC_ShuoShuo_WriteAty.this,
											SC_ShuoShuoAty.class);
									startActivity(intent);
									finish();
								}
							});
						} else {
							// 失败
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(SC_ShuoShuo_WriteAty.this,
											"发表失败，请重试！", Toast.LENGTH_SHORT)
											.show();
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SC_ShuoShuo_WriteAty.this,
									"上传失败，请重新上传", Toast.LENGTH_SHORT).show();

						}
					});
				}
			} else {
				// 图片未上传 只上传文字说说
				try {

					String u1 = HttpUtil.SUBMIT_SHUOSHUO;
					Map<String, String> map = new HashMap<String, String>();
					map.put("p_id", p_id);
					map.put("p_name", p_name);
					map.put("content", c);
					map.put("img_path", img_path);
					map.put("good_count", "0");
					map.put("feedback_count", "0");
					try {
						String res = HttpUtil.postRequest(u1, map);
						if (res != null) {
							// 成功
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(SC_ShuoShuo_WriteAty.this,
											"发表成功！", Toast.LENGTH_SHORT).show();
									Intent intent = new Intent(
											SC_ShuoShuo_WriteAty.this,
											SC_ShuoShuoAty.class);
									startActivity(intent);
									finish();
								}
							});
						} else {
							// 失败
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(SC_ShuoShuo_WriteAty.this,
											"发表失败，请重试！", Toast.LENGTH_SHORT)
											.show();
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
				}

			}

		}
	};

	/********************************* split line ************************************************/
	/**
	 * 获取照片，名称 使用系统当前日期加以调整作为照片的名称
	 */
	private String getPhotoFileName(int i) {
		Log.i(tag, "getPhotoFileName获得照片名称");
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		if (i == 1) {
			return dateFormat.format(date) + "_crop.JPEG";
		} else {
			// Toast.makeText(SC_ShuoShuo_WriteAty.this, "照片已提交！",
			// Toast.LENGTH_SHORT).show();
			return dateFormat.format(date) + ".JPEG";
		}
	}

	/*************/
	/*** 拍照 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(tag, "void onActivityResult拍照/选照片");
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用

			// if (data != null){
			startPhotoZoom(Uri.fromFile(tempFile), 300);
			// }

			break;

		case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
			// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
			if (data != null)
				startPhotoZoom(data.getData(), 300);

			break;

		case PHOTO_REQUEST_CUT:// 返回的结果
			if (data != null) {
				// Intent intent =new Intent();
				// intent.setDataAndType(data.getData(), "image/*");

				Bitmap bitmap = data.getParcelableExtra("data");
				uploadFile = saveToLocal(bitmap);
				setPicToView(bitmap);
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
		show_pic.setImageDrawable(drawable);
		// picChange = 1;

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
