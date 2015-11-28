package com.daguo.ui.commercial;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.photo.MyGridAdapter;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.base.Fragment_Mall_Item;
import com.daguo.utils.AsyncImageLoader2;
import com.daguo.utils.HttpUtil;

public class Shop_GoodsDetailAty extends Activity {

	private ImageView goods_icon;
	private WebView wbView;
	private TextView goods_nameTextView, goods_priceTextView,
			goods_scoreTextView, goods_descTextView;
	private Button payButton;
	private NoScrollGridView grid;// 照片相册gridView=(NoScrollGridView)

	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();
	private String[] imageUrl;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_shop_goodsdetail);
		MyAppliation.getInstance().addActivity(this);
		goods_descTextView = (TextView) findViewById(R.id.godds_desc);
		goods_icon = (ImageView) findViewById(R.id.goods_icon);
		goods_nameTextView = (TextView) findViewById(R.id.goods_name);
		goods_priceTextView = (TextView) findViewById(R.id.goods_price);
		goods_scoreTextView = (TextView) findViewById(R.id.goods_score);
		grid = (NoScrollGridView) findViewById(R.id.grid);
		wbView = (WebView) findViewById(R.id.godds_des);

		goods_nameTextView.setText("商品名：  " + Fragment_Mall_Item.goodsName);
		goods_priceTextView.setText("价格：  " + Fragment_Mall_Item.goodsPrice
				+ " 元");
		goods_scoreTextView.setText("购买积分： " + Fragment_Mall_Item.goodsscore
				+ " 分");
		wbView.getSettings().setDefaultTextEncodingName("UTF-8");
		String content = Fragment_Mall_Item.goodsDesc;

		if (Fragment_Mall_Item.img_src.equals("")) {

		} else {

			String img_src[] = Fragment_Mall_Item.img_src.split(",");

			for (int j = 0; j < img_src.length; j++) {
				content = content.replaceAll(img_src[j],
						"http://115.29.224.248:8080" + img_src[j]);
			}
		}

		Log.i("商品介绍src", content);
		// wbView.getSettings().setUseWideViewPort(true);
		// wbView.getSettings().setLoadWithOverviewMode(true);
		wbView.getSettings().setTextSize(TextSize.NORMAL);

		// String str ="";
		//
		// URLImageParser p = new URLImageParser(goods_descTextView,
		// Shop_GoodsDetailAty.this);
		// CharSequence sequence = Html.fromHtml(str, p,
		// null);
		
		// goods_descTextView.setText(sequence);
		WebSettings settings = wbView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setPluginState(PluginState.ON);
		// settings.setPluginsEnabled(true);
		settings.setLoadWithOverviewMode(true);

		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setAllowFileAccess(true);
		settings.setDefaultTextEncodingName("UTF-8");

		wbView.setWebChromeClient(new MyWebChromeClient());
		wbView.setWebViewClient(new MyWebViewClient());

		wbView.setVisibility(View.VISIBLE);

		settings.setAppCacheEnabled(true);

		wbView.loadData(content, "text/html; charset=UTF-8", null);

		goods_icon.setVisibility(View.VISIBLE);
		loadImage(HttpUtil.IMG_URL + Fragment_Mall_Item.thumb_path,
				R.id.goods_icon);
		imageUrl = Fragment_Mall_Item.img_path.split(",");

		int size = imageUrl.length;

		int length = 100;

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
		grid.setLayoutParams(params);
		grid.setColumnWidth(itemWidth);

		grid.setHorizontalSpacing(5);
		grid.setStretchMode(GridView.NO_STRETCH);
		grid.setVerticalScrollBarEnabled(true);
		grid.setNumColumns(size);
		grid.setAdapter(new MyGridAdapter(imageUrl, Shop_GoodsDetailAty.this));
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				imageBrower(position, imageUrl);
			}
		});

		payButton = (Button) findViewById(R.id.pay_btn);
		payButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Shop_GoodsDetailAty.this,
						Shop_OrderAty.class);
				startActivity(intent);
			}
		});

	}

	private void loadImage(final String url, final int id) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
				new AsyncImageLoader2.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) findViewById(id))
								.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			((ImageView) findViewById(id)).setImageDrawable(cacheImage);
		}
	}

	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(Shop_GoodsDetailAty.this,
				com.daguo.modem.photo.ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(
				com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(
				com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
				position);
		startActivity(intent);
	}

	/**
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-18 下午9:42:21
	 */
	class URLImageParser implements ImageGetter {

		Context c;
		TextView tv_image;

		public URLImageParser(TextView t, Context c) {
			this.tv_image = t;
			this.c = c;
		}

		@Override
		public Drawable getDrawable(String source) {
			URLDrawable urlDrawable = new URLDrawable();
			ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(
					urlDrawable);
			asyncTask.execute(source);
			return urlDrawable;
		}

		public class ImageGetterAsyncTask extends
				AsyncTask<String, Void, Drawable> {
			URLDrawable urlDrawable;

			public ImageGetterAsyncTask(URLDrawable d) {
				this.urlDrawable = d;
			}

			@Override
			protected void onPostExecute(Drawable result) {
				if (result != null) {
					urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(),
							result.getIntrinsicHeight());
					urlDrawable.drawable = result;
					URLImageParser.this.tv_image.invalidate();
				}
			}

			@Override
			protected Drawable doInBackground(String... params) {
				String source = params[0];// URL
				return fetchDrawable(source);
			}

			//
			public Drawable fetchDrawable(String urlString) {
				try {
					InputStream is = fetch(urlString);
					Drawable drawable = Drawable.createFromResourceStream(
							getResources(), null, is, "src", null);
					drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
							drawable.getIntrinsicHeight());
					return drawable;
				} catch (Exception e) {
					return null;
				}
			}

			private InputStream fetch(String urlString)
					throws MalformedURLException, IOException {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet request = new HttpGet(urlString);
				HttpResponse response = httpClient.execute(request);
				return response.getEntity().getContent();
			}

		}

	}

	class URLDrawable extends BitmapDrawable {

		protected Drawable drawable;

		@Override
		public void draw(Canvas canvas) {

			if (drawable != null) {
				drawable.draw(canvas);
			}
		}
	}

	class MyWebChromeClient extends WebChromeClient {

		private CustomViewCallback mCustomViewCallback;
		private int mOriginalOrientation = 1;

		@Override
		public void onShowCustomView(View view, CustomViewCallback callback) {
			onShowCustomView(view, mOriginalOrientation, callback);
			super.onShowCustomView(view, callback);

		}

		public void onShowCustomView(View view, int requestedOrientation,
				WebChromeClient.CustomViewCallback callback) {

		}

		public void onHideCustomView() {

			// Show the content view.

			setRequestedOrientation(mOriginalOrientation);
		}

	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return super.shouldOverrideUrlLoading(view, url);
		}

	}

	public static int getPhoneAndroidSDK() {
		int version = 0;
		try {
			version = Integer.valueOf(android.os.Build.VERSION.SDK);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return version;

	}

	@Override
	public void onPause() {// 继承自Activity
		super.onPause();
		wbView.onPause();
	}

	@Override
	public void onResume() {// 继承自Activity
		super.onResume();
		wbView.onResume();
	}

}
