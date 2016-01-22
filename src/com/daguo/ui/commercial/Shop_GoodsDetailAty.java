package com.daguo.ui.commercial;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.util.adapter.Shop_GoodsDetail_BannerAdapter;
import com.daguo.util.base.ViewPager_Hacky;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * 
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-24 上午10:58:11
 * @function ：商品详情主界面
 */
@SuppressLint("HandlerLeak")
public class Shop_GoodsDetailAty extends Activity implements OnClickListener,
	OnItemClickListener {

    private String id;
    private String p_id;
    private final int MSG_GOODS_DATA = 1001;
    private final int MSG_JOIN_SUC = 1002;
    private final int MSG_JOIN_FAIL = 1003;

    private int k;

    /**
     * initViews
     */
    private ViewPager_Hacky photo_vp;
    private TextView name_tv, price_tv, jion_tv, buy_tv;
    private TextView stockNum_tv, number_tv;
    // 内容
    private FrameLayout mFullscreenContainer;
    private FrameLayout mContentView;
    private View mCustomView = null;
    private WebView mWebView;

    /**
     * data
     */
    private Shop_GoodsItem shop_GoodsItem = new Shop_GoodsItem();

    // 图片
    private String[] picLists;
    ImageView[] picViews;

    private Shop_GoodsDetail_BannerAdapter adapter;

    /**
     * tools
     */

    CustomProgressDialog customProgressDialog;
    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_GOODS_DATA:
		setContentData();

		break;
	    case MSG_JOIN_FAIL:
		Toast.makeText(Shop_GoodsDetailAty.this, "加入失败哦，请重试",
			Toast.LENGTH_LONG).show();
		customProgressDialog.dismiss();

		break;
	    case MSG_JOIN_SUC:
		Toast.makeText(Shop_GoodsDetailAty.this, "加入购物车成功",
			Toast.LENGTH_LONG).show();
		customProgressDialog.dismiss();
		break;

	    default:
		break;
	    }
	}

    };

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_shop_goodsdetail);
	MyAppliation.getInstance().addActivity(this);
	Intent intent = getIntent();
	id = PublicTools.doWithNullData(intent.getStringExtra("id"));
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");

	initViews();
	loadGoodsData();

    }

    /**
     * 
     */
    private void initViews() {
	initHeadView();
	photo_vp = (ViewPager_Hacky) findViewById(R.id.photo_vp);

	name_tv = (TextView) findViewById(R.id.name_tv);
	price_tv = (TextView) findViewById(R.id.price_tv);
	jion_tv = (TextView) findViewById(R.id.jion_tv);
	buy_tv = (TextView) findViewById(R.id.buy_tv);

	number_tv = (TextView) findViewById(R.id.number_tv);
	stockNum_tv = (TextView) findViewById(R.id.stockNum_tv);

    }

    private void initHeadView() {
	TextView back_tView = (TextView) findViewById(R.id.back_tv);
	TextView title_tv = (TextView) findViewById(R.id.title_tv);
	TextView function_tv = (TextView) findViewById(R.id.function_tv);
	ImageView remind_iv = (ImageView) findViewById(R.id.remind_iv);

	back_tView.setOnClickListener(new View.OnClickListener() {

	    @Override
	    public void onClick(View arg0) {
		finish();
	    }
	});
	title_tv.setText("宝贝详情");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    private void setContentData() {
	jion_tv.setOnClickListener(this);
	buy_tv.setOnClickListener(this);
	name_tv.setOnClickListener(this);

	name_tv.setText(PublicTools.doWithNullData(shop_GoodsItem.getName()));
	price_tv.setText("￥ "
		+ PublicTools.doWithNullData(shop_GoodsItem.getPrice()));
	stockNum_tv.setText("库存："
		+ PublicTools.doWithNullData(shop_GoodsItem.getStock_num())
		+ " 件");
	number_tv
		.setText("已售："
			+ PublicTools.doWithNullData(shop_GoodsItem.getNumber()
				+ " 件"));

	mFullscreenContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);
	mContentView = (FrameLayout) findViewById(R.id.main_content);
	mWebView = (WebView) findViewById(R.id.webview_player);

	initWebView();
	mWebView.loadDataWithBaseURL("null", shop_GoodsItem.getGoods_desc(),
		"text/html", "UTF-8", "");

	adapter = new Shop_GoodsDetail_BannerAdapter(this, picViews);
	photo_vp.setOnPageChangeListener(new OnPageChangeListener() {
	    @Override
	    public void onPageSelected(int arg0) {

	    }

	    @Override
	    public void onPageScrolled(int arg0, float arg1, int arg2) {
	    }

	    @Override
	    public void onPageScrollStateChanged(int arg0) {
	    }
	});
	photo_vp.setAdapter(adapter);
	adapter.notifyDataSetChanged();

    };

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.jion_tv:

	    customProgressDialog = CustomProgressDialog.createDialog(this,
		    "添加中，请稍等。。");
	    customProgressDialog.show();
	    joinTo();

	    break;

	case R.id.buy_tv:
	    if (Integer.parseInt(shop_GoodsItem.getStock_num()) > 0) {

		Intent intent = new Intent(Shop_GoodsDetailAty.this,
			Shop_OrderAty.class);
		intent.putExtra("id", id);
		intent.putExtra("price", shop_GoodsItem.getPrice());
		intent.putExtra("name", shop_GoodsItem.getName());
		intent.putExtra("pic", shop_GoodsItem.getThumb_path());

		startActivity(intent);
	    } else {
		Toast.makeText(Shop_GoodsDetailAty.this, "库存不足啦！",
			Toast.LENGTH_LONG).show();
	    }

	    break;

	case R.id.name_tv:
	    // TODO ce shi
	    Intent intent2 = new Intent(Shop_GoodsDetailAty.this,
		    Shop_CartAty.class);
	    startActivity(intent2);
	    break;

	default:
	    break;
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
     * .AdapterView, android.view.View, int, long)
     */
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

    }

    /************** thread ********************************************/

    private void loadGoodsData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_GOODSLIST
			    + "&rows=1&page=1&id=" + id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {

			    String goods_desc = array.optJSONObject(i)
				    .getString("goods_desc");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");
			    String img_src = array.optJSONObject(i).getString(
				    "img_src");
			    String name = array.optJSONObject(i).getString(
				    "name");
			    String price = array.optJSONObject(i).getString(
				    "price");
			    String thumb_path = array.optJSONObject(i)
				    .getString("thumb_path");
			    String type_name = array.optJSONObject(i)
				    .getString("type_name");
			    String number = array.optJSONObject(i).getString(
				    "number");
			    String stock_num = array.optJSONObject(i)
				    .getString("stock_num");

			    if (img_src != null && !img_src.equals("")
				    && !img_src.equals("null")) {

				String[] imgs = img_src.split(",");
				for (int j = 0; j < imgs.length; j++) {
				    goods_desc = goods_desc.replaceAll(imgs[j],
					    "http://115.29.224.248:8080"
						    + imgs[j]);
				}
			    }

			    picLists = PublicTools.doWithNullData(img_path)
				    .split(",");

			    picViews = new ImageView[picLists.length];

			    for (k = 0; k < picViews.length; k++) {
				ImageView imageView = new ImageView(
					Shop_GoodsDetailAty.this);
				imageView.setScaleType(ScaleType.FIT_CENTER);
				FinalBitmap.create(Shop_GoodsDetailAty.this)
					.display(imageView,
						HttpUtil.IMG_URL + picLists[k]);
				imageView
					.setOnClickListener(new View.OnClickListener() {

					    @Override
					    public void onClick(View arg0) {
						imageBrower(k, picLists);
					    }
					});

				picViews[k] = imageView;

			    }

			    shop_GoodsItem.setGoods_desc(goods_desc);
			    shop_GoodsItem.setImg_path(img_path);
			    shop_GoodsItem.setImg_src(img_src);
			    shop_GoodsItem.setName(name);
			    shop_GoodsItem.setPrice(price);
			    shop_GoodsItem.setThumb_path(thumb_path);
			    shop_GoodsItem.setType_name(type_name);
			    shop_GoodsItem.setNumber(number);
			    shop_GoodsItem.setStock_num(stock_num);

			    msg = handler.obtainMessage(MSG_GOODS_DATA);
			    msg.sendToTarget();

			}
		    } else {
			// 没取道 数据
		    }

		} catch (JSONException e) {
		    Log.e("商品详情", "商品详情解析json异常");

		} catch (Exception e) {
		    Log.e("商品详情", "商品详情解析 异常");
		}
	    }
	}).start();
    }

    /**
     * 加入购物车操作
     */
    @SuppressLint("CommitPrefEdits")
    private void joinTo() {
	// 提交到购物车
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.SUBMIT_CART_JION + "&p_id=" + p_id
			    + "&good_id=" + id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if (1 == jsonObject.getInt("result")) {
			// 加入成共
			handler.sendEmptyMessage(MSG_JOIN_SUC);
		    } else {
			// 加入失败
			handler.sendEmptyMessage(MSG_JOIN_FAIL);
		    }
		} catch (Exception e) {
		}
	    }
	}).start();

    }

    /*************************************************************************/

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void initWebView() {
	WebSettings settings = mWebView.getSettings();
	settings.setJavaScriptEnabled(true);
	settings.setJavaScriptCanOpenWindowsAutomatically(true);
	settings.setPluginState(PluginState.ON);
	// settings.setPluginsEnabled(true);
	settings.setLoadWithOverviewMode(true);

	settings.setJavaScriptCanOpenWindowsAutomatically(true);
	settings.setAllowFileAccess(true);
	settings.setDefaultTextEncodingName("UTF-8");
	settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 适应屏幕
	settings.setLoadWithOverviewMode(true);

	mWebView.setWebChromeClient(new MyWebChromeClient());
	mWebView.setWebViewClient(new MyWebViewClient());

	mWebView.setVisibility(View.VISIBLE);

	settings.setAppCacheEnabled(true);

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
	    if (mCustomView != null) {
		callback.onCustomViewHidden();
		return;
	    }
	    if (getPhoneAndroidSDK() >= 14) {
		mFullscreenContainer.addView(view);
		mCustomView = view;
		mCustomViewCallback = callback;
		mOriginalOrientation = getRequestedOrientation();
		mContentView.setVisibility(View.INVISIBLE);
		mFullscreenContainer.setVisibility(View.VISIBLE);
		mFullscreenContainer.bringToFront();

		setRequestedOrientation(mOriginalOrientation);
	    }

	}

	public void onHideCustomView() {
	    mContentView.setVisibility(View.VISIBLE);
	    if (mCustomView == null) {
		return;
	    }
	    mCustomView.setVisibility(View.GONE);
	    mFullscreenContainer.removeView(mCustomView);
	    mCustomView = null;
	    mFullscreenContainer.setVisibility(View.GONE);
	    try {
		mCustomViewCallback.onCustomViewHidden();
	    } catch (Exception e) {
	    }
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

    @SuppressWarnings("deprecation")
    public static int getPhoneAndroidSDK() {
	int version = 0;
	try {
	    version = Integer.valueOf(android.os.Build.VERSION.SDK);
	} catch (NumberFormatException e) {
	    e.printStackTrace();
	}
	return version;

    }

    //
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

    //
    // private ImageView goods_icon;
    // private WebView wbView;
    // private TextView goods_nameTextView, goods_priceTextView,
    // goods_scoreTextView, goods_descTextView;
    // private Button payButton;
    // private NoScrollGridView grid;// 照片相册gridView=(NoScrollGridView)
    //
    // private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();
    // private String[] imageUrl;
    //
    // @SuppressWarnings("deprecation")
    // @Override
    // protected void onCreate(Bundle savedInstanceState) {
    // super.onCreate(savedInstanceState);
    // setContentView(R.layout.aty_shop_goodsdetail);
    // MyAppliation.getInstance().addActivity(this);
    // goods_descTextView = (TextView) findViewById(R.id.godds_desc);
    // goods_icon = (ImageView) findViewById(R.id.goods_icon);
    // goods_nameTextView = (TextView) findViewById(R.id.goods_name);
    // goods_priceTextView = (TextView) findViewById(R.id.goods_price);
    // goods_scoreTextView = (TextView) findViewById(R.id.goods_score);
    // grid = (NoScrollGridView) findViewById(R.id.grid);
    // wbView = (WebView) findViewById(R.id.godds_des);
    //
    // goods_nameTextView.setText("商品名：  " + Fragment_Mall_Item.goodsName);
    // goods_priceTextView.setText("价格：  " + Fragment_Mall_Item.goodsPrice
    // + " 元");
    // goods_scoreTextView.setText("购买积分： " + Fragment_Mall_Item.goodsscore
    // + " 分");
    // wbView.getSettings().setDefaultTextEncodingName("UTF-8");
    // String content = Fragment_Mall_Item.goodsDesc;
    //
    // if (Fragment_Mall_Item.img_src.equals("")) {
    //
    // } else {
    //
    // String img_src[] = Fragment_Mall_Item.img_src.split(",");
    //
    // for (int j = 0; j < img_src.length; j++) {
    // content = content.replaceAll(img_src[j],
    // "http://115.29.224.248:8080" + img_src[j]);
    // }
    // }
    //
    // Log.i("商品介绍src", content);
    // // wbView.getSettings().setUseWideViewPort(true);
    // // wbView.getSettings().setLoadWithOverviewMode(true);
    // wbView.getSettings().setTextSize(TextSize.NORMAL);
    //
    // // String str ="";
    // //
    // // URLImageParser p = new URLImageParser(goods_descTextView,
    // // Shop_GoodsDetailAty.this);
    // // CharSequence sequence = Html.fromHtml(str, p,
    // // null);
    //
    // // goods_descTextView.setText(sequence);
    // WebSettings settings = wbView.getSettings();
    // settings.setJavaScriptEnabled(true);
    // settings.setJavaScriptCanOpenWindowsAutomatically(true);
    // settings.setPluginState(PluginState.ON);
    // // settings.setPluginsEnabled(true);
    // settings.setLoadWithOverviewMode(true);
    //
    // settings.setJavaScriptCanOpenWindowsAutomatically(true);
    // settings.setAllowFileAccess(true);
    // settings.setDefaultTextEncodingName("UTF-8");
    //
    // wbView.setWebChromeClient(new MyWebChromeClient());
    // wbView.setWebViewClient(new MyWebViewClient());
    //
    // wbView.setVisibility(View.VISIBLE);
    //
    // settings.setAppCacheEnabled(true);
    //
    // wbView.loadData(content, "text/html; charset=UTF-8", null);
    //
    // goods_icon.setVisibility(View.VISIBLE);
    // loadImage(HttpUtil.IMG_URL + Fragment_Mall_Item.thumb_path,
    // R.id.goods_icon);
    // imageUrl = Fragment_Mall_Item.img_path.split(",");
    //
    // int size = imageUrl.length;
    //
    // int length = 100;
    //
    // DisplayMetrics dm = new DisplayMetrics();
    // getWindowManager().getDefaultDisplay().getMetrics(dm);
    // float density = dm.density;
    // int gridviewWidth = (int) (size * (length + 4) * density);
    // int itemWidth = (int) (length * density);
    //
    // LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    // gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
    // grid.setLayoutParams(params);
    // grid.setColumnWidth(itemWidth);
    //
    // grid.setHorizontalSpacing(5);
    // grid.setStretchMode(GridView.NO_STRETCH);
    // grid.setVerticalScrollBarEnabled(true);
    // grid.setNumColumns(size);
    // grid.setAdapter(new MyGridAdapter(imageUrl, Shop_GoodsDetailAty.this,2));
    // grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //
    // @Override
    // public void onItemClick(AdapterView<?> parent, View view,
    // int position, long id) {
    // imageBrower(position, imageUrl);
    // }
    // });
    //
    // payButton = (Button) findViewById(R.id.pay_btn);
    // payButton.setOnClickListener(new View.OnClickListener() {
    //
    // @Override
    // public void onClick(View arg0) {
    // Intent intent = new Intent(Shop_GoodsDetailAty.this,
    // Shop_OrderAty.class);
    // startActivity(intent);
    // }
    // });
    //
    // }
    //
    // private void loadImage(final String url, final int id) {
    // // 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
    // Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
    // new AsyncImageLoader2.ImageCallback() {
    // // 请参见实现：如果第一次加载url时下面方法会执行
    // public void imageLoaded(Drawable imageDrawable) {
    // ((ImageView) findViewById(id))
    // .setImageDrawable(imageDrawable);
    // }
    // });
    // if (cacheImage != null) {
    // ((ImageView) findViewById(id)).setImageDrawable(cacheImage);
    // }
    // }
    //
    // private void imageBrower(int position, String[] urls) {
    // Intent intent = new Intent(Shop_GoodsDetailAty.this,
    // com.daguo.modem.photo.ImagePagerActivity.class);
    // // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
    // intent.putExtra(
    // com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
    // intent.putExtra(
    // com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
    // position);
    // startActivity(intent);
    // }
    //
    // /**
    // *
    // * @author Bugs_Rabbit 時間： 2015-8-18 下午9:42:21
    // */
    // class URLImageParser implements ImageGetter {
    //
    // Context c;
    // TextView tv_image;
    //
    // public URLImageParser(TextView t, Context c) {
    // this.tv_image = t;
    // this.c = c;
    // }
    //
    // @Override
    // public Drawable getDrawable(String source) {
    // URLDrawable urlDrawable = new URLDrawable();
    // ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(
    // urlDrawable);
    // asyncTask.execute(source);
    // return urlDrawable;
    // }
    //
    // public class ImageGetterAsyncTask extends
    // AsyncTask<String, Void, Drawable> {
    // URLDrawable urlDrawable;
    //
    // public ImageGetterAsyncTask(URLDrawable d) {
    // this.urlDrawable = d;
    // }
    //
    // @Override
    // protected void onPostExecute(Drawable result) {
    // if (result != null) {
    // urlDrawable.setBounds(0, 0, result.getIntrinsicWidth(),
    // result.getIntrinsicHeight());
    // urlDrawable.drawable = result;
    // URLImageParser.this.tv_image.invalidate();
    // }
    // }
    //
    // @Override
    // protected Drawable doInBackground(String... params) {
    // String source = params[0];// URL
    // return fetchDrawable(source);
    // }
    //
    // //
    // public Drawable fetchDrawable(String urlString) {
    // try {
    // InputStream is = fetch(urlString);
    // Drawable drawable = Drawable.createFromResourceStream(
    // getResources(), null, is, "src", null);
    // drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
    // drawable.getIntrinsicHeight());
    // return drawable;
    // } catch (Exception e) {
    // return null;
    // }
    // }
    //
    // private InputStream fetch(String urlString)
    // throws MalformedURLException, IOException {
    // DefaultHttpClient httpClient = new DefaultHttpClient();
    // HttpGet request = new HttpGet(urlString);
    // HttpResponse response = httpClient.execute(request);
    // return response.getEntity().getContent();
    // }
    //
    // }
    //
    // }
    //
    // class URLDrawable extends BitmapDrawable {
    //
    // protected Drawable drawable;
    //
    // @Override
    // public void draw(Canvas canvas) {
    //
    // if (drawable != null) {
    // drawable.draw(canvas);
    // }
    // }
    // }
    //
    // class MyWebChromeClient extends WebChromeClient {
    //
    // private CustomViewCallback mCustomViewCallback;
    // private int mOriginalOrientation = 1;
    //
    // @Override
    // public void onShowCustomView(View view, CustomViewCallback callback) {
    // onShowCustomView(view, mOriginalOrientation, callback);
    // super.onShowCustomView(view, callback);
    //
    // }
    //
    // public void onShowCustomView(View view, int requestedOrientation,
    // WebChromeClient.CustomViewCallback callback) {
    //
    // }
    //
    // public void onHideCustomView() {
    //
    // // Show the content view.
    //
    // setRequestedOrientation(mOriginalOrientation);
    // }
    //
    // }
    //
    // class MyWebViewClient extends WebViewClient {
    //
    // @Override
    // public boolean shouldOverrideUrlLoading(WebView view, String url) {
    // view.loadUrl(url);
    // return super.shouldOverrideUrlLoading(view, url);
    // }
    //
    // }
    //
    // public static int getPhoneAndroidSDK() {
    // int version = 0;
    // try {
    // version = Integer.valueOf(android.os.Build.VERSION.SDK);
    // } catch (NumberFormatException e) {
    // e.printStackTrace();
    // }
    // return version;
    //
    // }
    //
    // @Override
    // public void onPause() {// 继承自Activity
    // super.onPause();
    // wbView.onPause();
    // }
    //
    // @Override
    // public void onResume() {// 继承自Activity
    // super.onResume();
    // wbView.onResume();
    // }

}
