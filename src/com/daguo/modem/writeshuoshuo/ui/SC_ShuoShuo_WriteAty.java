package com.daguo.modem.writeshuoshuo.ui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.writeshuoshuo.util.Bimp;
import com.daguo.modem.writeshuoshuo.util.FileUtils;
import com.daguo.modem.writeshuoshuo.util.ImageItem;
import com.daguo.modem.writeshuoshuo.util.PublicWay;
import com.daguo.modem.writeshuoshuo.util.Res;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.daguo.utils.UploadUtil;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-9 下午4:03:44
 * @function ：發說說界面
 */
public class SC_ShuoShuo_WriteAty extends Activity {
	private final int MSG_SEND_SUC = 100;
	private final int MSG_SEND_FAIL = 101;
	private final int MSG_UPLOAD_FINISH = 102;
	private final int TAKE_PICTURE = 0x000001;
	private final int CHOOSE_PHOTO = 0x000002;

	private TextView activity_selectimg_send;
	private GridView noScrollgridview;
	private GridAdapter adapter;
	private View parentView;
	private PopupWindow pop = null;
	private LinearLayout ll_popup;
	public static Bitmap bimap;

	private CustomProgressDialog dialog;

	// data
	String content;// 说说内容
	EditText content_edt; // 输入框

	String p_id, p_name, p_schoolId;

	// 添加的图片路径列表
	private List<String> img_pathListsList = new ArrayList<String>();

	Message msg;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SEND_SUC:
				dialog.dismiss();
				MyAppliation.getInstance().exit();

				break;

			case MSG_SEND_FAIL:
				dialog.dismiss();
				Toast.makeText(SC_ShuoShuo_WriteAty.this, "发表失败，请稍后重试",
						Toast.LENGTH_LONG).show();
				break;

			case MSG_UPLOAD_FINISH:
				submitContent(content, HttpUtil.SUBMIT_SHUOSHUO);
				break;

			default:
				break;
			}
		}
	};

	@SuppressLint("InflateParams")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Res.init(this);
		bimap = BitmapFactory.decodeResource(getResources(),
				R.drawable.icon_addpic_unfocused);
		PublicWay.activityList.add(this);
		MyAppliation.getInstance().addActivity(this);
		parentView = getLayoutInflater().inflate(R.layout.activity_selectimg,
				null);
		setContentView(parentView);
		dialog = CustomProgressDialog.createDialog(this, "加载中。。");
		getShared();
		Init();
	}

	/**
	 * 获得本地存储用户信息
	 */
	private void getShared() {
		SharedPreferences sp = getSharedPreferences("userinfo", 0);
		p_id = sp.getString("id", "");
		p_name = sp.getString("name", "");
		p_schoolId = sp.getString("school_id", "");
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void Init() {

		activity_selectimg_send = (TextView) findViewById(R.id.activity_selectimg_send);
		content_edt = (EditText) findViewById(R.id.content_edt);
		pop = new PopupWindow(SC_ShuoShuo_WriteAty.this);

		View view = getLayoutInflater().inflate(R.layout.item_popupwindows,
				null);

		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);

		pop.setWidth(LayoutParams.MATCH_PARENT);
		pop.setHeight(LayoutParams.WRAP_CONTENT);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setFocusable(true);
		pop.setOutsideTouchable(true);
		pop.setContentView(view);

		RelativeLayout parent = (RelativeLayout) view.findViewById(R.id.parent);
		Button bt1 = (Button) view.findViewById(R.id.item_popupwindows_camera);
		Button bt2 = (Button) view.findViewById(R.id.item_popupwindows_Photo);
		Button bt3 = (Button) view.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				photo();
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String con = content_edt.getText().toString().trim();
				content = PublicTools.doWithNullData(con);
				Intent intent = new Intent(SC_ShuoShuo_WriteAty.this,
						AlbumActivity.class);
				startActivityForResult(intent, CHOOSE_PHOTO);
				// startActivity(intent);
				overridePendingTransition(R.anim.activity_translate_in,
						R.anim.activity_translate_out);
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				pop.dismiss();
				ll_popup.clearAnimation();
			}
		});

		noScrollgridview = (GridView) findViewById(R.id.noScrollgridview);
		noScrollgridview.setSelector(new ColorDrawable(Color.TRANSPARENT));
		adapter = new GridAdapter(this);
		adapter.update();
		noScrollgridview.setAdapter(adapter);
		noScrollgridview.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == Bimp.tempSelectBitmap.size()) {
					Log.i("ddddddd", "----------");
					ll_popup.startAnimation(AnimationUtils.loadAnimation(
							SC_ShuoShuo_WriteAty.this,
							R.anim.activity_translate_in));
					pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
				} else {
					Intent intent = new Intent(SC_ShuoShuo_WriteAty.this,
							GalleryActivity.class);
					intent.putExtra("position", "1");
					intent.putExtra("ID", arg2);
					startActivity(intent);
				}
			}
		});

		activity_selectimg_send.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.show();

				String con = content_edt.getText().toString().trim();
				content = PublicTools.doWithNullData(con);
				upLoadPhoto(HttpUtil.IMG_URL_SUB);

			}
		});

		TextView back_tv = (TextView) findViewById(R.id.back_tv);
		back_tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	/**
	 * 将图片上传至服务器
	 * 
	 * @param url
	 *            服务器地址
	 */
	private void upLoadPhoto(final String url) {
		new Thread(new Runnable() {
			public void run() {

				for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {

					InputStream is = Bitmap2InputStream(Bimp.tempSelectBitmap
							.get(i).getBitmap(), 100);
					// String url =
					// "http://115.29.224.248:8080/XYYYT/service/fileUpload/upload?android=1";

					String request = UploadUtil.uploadFile(is, url);

					JSONObject js;
					try {
						js = new JSONObject(request);
						String img_path = js.getString("fileRelativePath");
						if (UploadUtil.rescode == 200) {
							// 成功
							img_pathListsList.add(img_path);

						} else {
							// 失败
						}
					} catch (JSONException e) {
						Log.e(TAG, "图片上传服务器json异常");
					} catch (Exception e) {
						Log.e(TAG, "图片上传服务器 异常");
						e.printStackTrace();
					}

				}
				msg = handler.obtainMessage(MSG_UPLOAD_FINISH);
				msg.sendToTarget();

			}
		}).start();
	}

	/**
	 * 发送说说
	 * 
	 * @param content
	 *            说说内容
	 * @param url
	 *            提交地址
	 */
	private void submitContent(final String content, final String url) {

		new Thread(new Runnable() {
			public void run() {
				String img_path = null;
				if (img_pathListsList.size() > 0) {
					img_path = PublicTools.listToString(img_pathListsList);
				} else {
					// 没有图片
				}
				try {

					Map<String, String> map = new HashMap<String, String>();
					map.put("p_id", p_id);
					map.put("p_name", p_name);
					map.put("content", content);
					map.put("img_path", img_path);
					map.put("good_count", "0");
					map.put("feedback_count", "0");
					map.put("school_id", p_schoolId);
					String res = HttpUtil.postRequest(url, map);

					JSONObject js = new JSONObject(res);
					if (js.getInt("result") == 1) {
						// 成功
						msg = handler.obtainMessage(MSG_SEND_SUC);
						msg.sendToTarget();
					} else {
						// 失败
						msg = handler.obtainMessage(MSG_SEND_FAIL);
						msg.sendToTarget();
					}
				} catch (JSONException exception) {
					Log.e(TAG, "说说上传服务器json异常");
				} catch (Exception e) {
					Log.e(TAG, "说说上传服务器json异常");

				}

			}
		}).start();
	}

	/******************************************************************/

	@SuppressLint("HandlerLeak")
	public class GridAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private int selectedPosition = -1;
		private boolean shape;

		public boolean isShape() {
			return shape;
		}

		public void setShape(boolean shape) {
			this.shape = shape;
		}

		public GridAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		public void update() {
			loading();
		}

		public int getCount() {
			if (Bimp.tempSelectBitmap.size() == 9) {
				return 9;
			}
			return (Bimp.tempSelectBitmap.size() + 1);
		}

		public Object getItem(int arg0) {
			return null;
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public void setSelectedPosition(int position) {
			selectedPosition = position;
		}

		public int getSelectedPosition() {
			return selectedPosition;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.item_published_grida,
						parent, false);
				holder = new ViewHolder();
				holder.image = (ImageView) convertView
						.findViewById(R.id.item_grida_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position == Bimp.tempSelectBitmap.size()) {
				holder.image.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), R.drawable.icon_addpic_unfocused));
				if (position == 9) {
					holder.image.setVisibility(View.GONE);
				}
			} else {
				holder.image.setImageBitmap(Bimp.tempSelectBitmap.get(position)
						.getBitmap());
			}

			return convertView;
		}

		private class ViewHolder {
			public ImageView image;
		}

		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					adapter.notifyDataSetChanged();
					break;
				}
				super.handleMessage(msg);
			}
		};

		public void loading() {
			new Thread(new Runnable() {
				public void run() {
					while (true) {
						if (Bimp.max == Bimp.tempSelectBitmap.size()) {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
							break;
						} else {
							Bimp.max += 1;
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						}
					}
				}
			}).start();
		}
	}

	public String getString(String s) {
		String path = null;
		if (s == null)
			return "";
		for (int i = s.length() - 1; i > 0; i++) {
			s.charAt(i);
		}
		return path;
	}

	protected void onRestart() {
		adapter.update();
		super.onRestart();
	}

	public void photo() {
		Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(openCameraIntent, TAKE_PICTURE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.tempSelectBitmap.size() < 9 && resultCode == RESULT_OK) {

				String fileName = String.valueOf(System.currentTimeMillis());
				Bitmap bm = (Bitmap) data.getExtras().get("data");
				FileUtils.saveBitmap(bm, fileName);

				ImageItem takePhoto = new ImageItem();
				takePhoto.setBitmap(bm);

				Bimp.tempSelectBitmap.add(takePhoto);
			}
			break;
		case CHOOSE_PHOTO:
			content_edt.setText(content);
			break;
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			for (int i = 0; i < PublicWay.activityList.size(); i++) {
				if (null != PublicWay.activityList.get(i)) {
					PublicWay.activityList.get(i).finish();
				}
			}
			System.exit(0);
		}
		return true;
	}

	// 将Bitmap转换成InputStream
	public InputStream Bitmap2InputStream(Bitmap bm, int quality) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, quality, baos);
		InputStream is = new ByteArrayInputStream(baos.toByteArray());
		return is;
	}

	private static final String TAG = "uploadFile";

	// private static final int TIME_OUT = 10 * 1000; // 超时时间

	// private static final String CHARSET = "utf-8"; // 设置编码

	public static int rescode = 0;

	@Override
	protected void onDestroy() {
		MyAppliation.getInstance().exit();
		super.onDestroy();
	}
}
