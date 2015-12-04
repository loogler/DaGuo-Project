/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.shetuan;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.PullToRefreshLayout;
import com.daguo.libs.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.Eva_OrdinaryAdapter;
import com.daguo.util.adapter.SC_SheTuanDetailAdapter;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.SC_SheTuan;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-30 下午5:19:45
 * @function ：校园社团 详情页 主界面
 */
public class SC_SheTuanDetailAty extends Activity implements
	View.OnClickListener {

    private final int MSG_CONTENT_DATA = 100;
    private final int MSG_EVALUATE_DATA = 101;
    private final int MSG_APPLY_SUCCED = 102;
    private final int MSG_APPLY_FAILED = 103;
    private final int MSG_APPLYED = 104;
    private final int MSG_UNAPPLY = 105;

    /**
     * initViews
     */

    private TextView share_tv, apply_tv, evaluate_tv;

    //
    private PullToRefreshLayout refresh_view;
    private ListView content_view;

    /**
     * data
     */

    private String sheTuanId;
    private SC_SheTuan sheTuanList = new SC_SheTuan();

    private List<Evaluate_Ordinary> evaLists = new ArrayList<Evaluate_Ordinary>();
    private SC_SheTuanDetailAdapter adapter = null;

    private String p_id;
    private String feedbackContent;

    /**
     * tools
     */
    private Message msg;
    private int pageIndex = 1;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT_DATA:
		adapter.notifyDataSetChanged();
		break;
	    case MSG_EVALUATE_DATA:
		if (evaLists != null) {
		    evaLists.clear();
		}
		evaLists = (List<Evaluate_Ordinary>) msg.obj;
		adapter.notifyDataSetChanged();

		break;
	    case MSG_APPLY_FAILED:
		Toast.makeText(SC_SheTuanDetailAty.this, "报名失败，请重试",
			Toast.LENGTH_LONG).show();
		break;

	    case MSG_APPLY_SUCCED:

		Toast.makeText(SC_SheTuanDetailAty.this, "报名成功！恭喜哦",
			Toast.LENGTH_LONG).show();
		apply_tv.setBackgroundColor(getResources().getColor(
			R.color.gray));
		apply_tv.setTextColor(getResources().getColor(R.color.red_1));
		apply_tv.setText("已报名");
		apply_tv.setClickable(false);
		break;
	    case MSG_UNAPPLY:
		apply_tv.setBackgroundResource(R.drawable.shape_ellipse_orangewhite_cur);
		apply_tv.setTextColor(getResources().getColor(R.color.white));
		apply_tv.setText("报 名");
		apply_tv.setClickable(true);
		break;

	    case MSG_APPLYED:
		apply_tv.setBackgroundColor(getResources().getColor(
			R.color.gray));
		apply_tv.setTextColor(getResources().getColor(R.color.red_1));
		apply_tv.setText("已报名");
		apply_tv.setClickable(false);
		break;
	    default:
		break;
	    }
	};
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_sc_shetuandetial);
	Intent intent = getIntent();
	sheTuanId = intent.getStringExtra("id");
	if (sheTuanId == null) {
	    finish();// 禁止空参进入该界面
	}
	SharedPreferences sp = getSharedPreferences("userinfo",
		Activity.MODE_WORLD_READABLE);
	p_id = sp.getString("id", "");
	initHeadView();// 头部公共view
	initViews();// 初始化相关view
	loadContentData();// 处理该条社团信息数据
	loadFeedbackData();// 处理评论列表数据
	checkApplyInfo();// 查看是否已经报名

	adapter = new SC_SheTuanDetailAdapter(SC_SheTuanDetailAty.this,
		sheTuanList, evaLists);
	content_view.setAdapter(adapter);

    }

    /**
     * 通用的headview 不同位置会出现不同的页面要求，根据情况设置
     */
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
	title_tv.setText("社团详情");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 
     */
    private void initViews() {

	share_tv = (TextView) findViewById(R.id.share_tv);
	apply_tv = (TextView) findViewById(R.id.apply_tv);
	evaluate_tv = (TextView) findViewById(R.id.evaluate_tv);

	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (ListView) findViewById(R.id.content_view);

	share_tv.setOnClickListener(this);
	apply_tv.setOnClickListener(this);
	evaluate_tv.setOnClickListener(this);

	refresh_view.setOnRefreshListener(new MyRefreshListenner());

    }

    /**
     * 加载社团内容的数据
     */
    private void loadContentData() {
	new Thread(

	new Runnable() {
	    public void run() {
		String url = HttpUtil.QUERY_EVENT
			+ "&menu_id=d6c986c5-8e52-485e-9a6e-d5d98480564e&rows=1&page=1&id="
			+ sheTuanId;
		String res = null;
		try {
		    res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    int total = js.getInt("total");
		    if (total > 0) {
			JSONArray array = js.getJSONArray("rows");

			String content = array.optJSONObject(0).getString(
				"content");
			String create_time = array.optJSONObject(0).getString(
				"create_time");
			String feedback_count = array.optJSONObject(0)
				.getString("feedback_count");
			String good_count = array.optJSONObject(0).getString(
				"good_count");
			String id = array.optJSONObject(0).getString("id");
			String img_src = array.optJSONObject(0).getString(
				"img_src");
			String menu_id = array.optJSONObject(0).getString(
				"menu_id");
			String sort = array.optJSONObject(0).getString("sort");
			String title = array.optJSONObject(0)
				.getString("title");
			String title2 = array.optJSONObject(0).getString(
				"title2");

			sheTuanList.setContent(content);
			sheTuanList.setCreate_time(create_time);
			sheTuanList.setFeedback_count(feedback_count);
			sheTuanList.setGood_count(good_count);
			sheTuanList.setImg_src(img_src);
			sheTuanList.setTitle(title);
			sheTuanList.setTitle2(title2);

			msg = handler.obtainMessage(MSG_CONTENT_DATA);
			msg.sendToTarget();
		    } else {
			// 无数据或者出错了
		    }

		} catch (JSONException exception) {
		    Log.e("校园社团", "获取社团信息json异常");
		}

		catch (Exception e) {
		    Log.e("校园社团", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();
    }

    /**
     * 加载社团评论的数据
     */
    private void loadFeedbackData() {
	new Thread(

	new Runnable() {
	    public void run() {
		String url = HttpUtil.QUERY_EVENT_DETAIL
			+ "&menu_id=d6c986c5-8e52-485e-9a6e-d5d98480564e&page=1&a_id="
			+ sheTuanId + "&rows=" + pageIndex;
		String res = null;
		try {
		    res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    int total = js.getInt("total");
		    if (total > 0) {
			JSONArray array = js.getJSONArray("rows");
			List<Evaluate_Ordinary> abc = new ArrayList<Evaluate_Ordinary>();
			Evaluate_Ordinary bcd = null;
			for (int i = 0; i < array.length(); i++) {
			    bcd = new Evaluate_Ordinary();

			    String content = array.optJSONObject(i).getString(
				    "content");
			    String create_time = array.optJSONObject(i)
				    .getString("create_time");
			    String p_id = array.optJSONObject(i).getString(
				    "p_id");
			    String p_name = array.optJSONObject(i).getString(
				    "p_name");
			    String head_info = array.optJSONObject(i)
				    .getString("head_info");
			    String start_year = array.optJSONObject(i)
				    .getString("start_year");
			    String sex = array.optJSONObject(i)
				    .getString("sex");
			    String pro_name = array.optJSONObject(i).getString(
				    "pro_name");

			    bcd.setContent(content);
			    bcd.setCreate_time(create_time);
			    bcd.setHead_info(head_info);
			    bcd.setP_id(p_id);
			    bcd.setP_name(p_name);
			    bcd.setContent(start_year);
			    bcd.setSex(sex);
			    bcd.setPro_name(pro_name);

			    abc.add(bcd);
			}
			msg = handler.obtainMessage(MSG_EVALUATE_DATA);
			msg.obj = abc;
			msg.sendToTarget();
		    } else {
			// 无数据或者出错了
		    }

		} catch (JSONException exception) {
		    Log.e("校园社团", "获取社团信息json异常");
		}

		catch (Exception e) {
		    Log.e("校园社团", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();
    }

    @Override
    public void onPause() {// 继承自Activity
	super.onPause();
	// mWebView.onPause();
    }

    @Override
    public void onResume() {// 继承自Activity
	super.onResume();
	// mWebView.onResume();
    }

    /******************************** onclick 事件 ***************************************************************/
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.share_tv:
	    Intent sendIntent = new Intent();
	    sendIntent.setAction(Intent.ACTION_SEND);
	    sendIntent.putExtra(Intent.EXTRA_TEXT, "大果校园下载地址"
		    + HttpUtil.DOWNLOAD);// 分享内容
	    sendIntent.setType("text/plain");
	    startActivity(Intent.createChooser(sendIntent, "大果校园欢迎您"));// 分享标题
	    break;
	case R.id.apply_tv:
	    applyToSheTuan();
	    break;

	case R.id.evaluate_tv:

	    break;

	default:
	    break;
	}
    }

    /**
     * 检查该用户是否已经报名
     * 
     */
    private void checkApplyInfo() {
	new Thread(new Runnable() {
	    public void run() {

		String url = HttpUtil.QUERY_EVENT_APPLY + "&table_name=0&source_id="
			+ sheTuanId + "&p_id=" + p_id + "&page=1&rows=1";
		try {
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    if (js.getInt("total") > 0) {
			// 已经报名了
			msg = handler.obtainMessage(MSG_APPLYED);
			msg.sendToTarget();
		    } else {
			// 尚未报名
			msg = handler.obtainMessage(MSG_UNAPPLY);
			msg.sendToTarget();
		    }
		} catch (JSONException exception) {
		    Log.e("校园社团详情", "获取社团信息json异常");
		} catch (Exception e) {
		    Log.e("校园社团详情", "获取社团信息异常");
		    e.printStackTrace();
		}

	    }
	}).start();

    }

    /**
     * 报名社团线程
     */
    private void applyToSheTuan() {

	new Thread(new Runnable() {
	    public void run() {
		String url = HttpUtil.SUBMIT_EVENT_APPLY + "&table_name=0&type=0&p_id="
			+ p_id + "&source_id=" + sheTuanId;
		try {
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);

		    if ("1".equals(js.getString("result"))) {

			msg = handler.obtainMessage(MSG_APPLY_SUCCED);
			msg.sendToTarget();

		    } else {
			// 报名失败
			msg = handler.obtainMessage(MSG_APPLY_FAILED);
			msg.sendToTarget();
		    }

		} catch (JSONException exception) {
		    Log.e("校园社团详情", "获取社团信息json异常");
		} catch (Exception e) {
		    Log.e("校园社团详情", "获取社团信息异常");
		    e.printStackTrace();
		}
	    }
	}).start();

    }

    /**
     * 评论社团线程
     */
    private void evaluateSheTuan() {
	new Thread(

	new Runnable() {
	    public void run() {
		String url = HttpUtil.SUBMIT_EVENT_FEEDBACK + "&content="
			+ feedbackContent + "&a_id=" + p_id + "&p_id="
			+ sheTuanId;
		try {
		    String res=HttpUtil.getRequest(url);
		    JSONObject js =new JSONObject(res);
		    
		    if ("1".equals(js.getString("result"))) {
			//评论成功
		    }else {
			//评论失败
			//TODO  社团接口有待更改   
		    }
		} catch (JSONException exception) {
		    Log.e("校园社团评论", "获取社团信息json异常");
		} catch (Exception e) {
		    Log.e("校园社团评论", "获取社团信息异常");
		    e.printStackTrace();
		}
	    }
	}).start();
    }

    /**
     * 给出用户输入框，用于评论
     */
    private void showDialog() {
	
    }

    /************************ 刷新事件 ************************************************/
    /**
     * 
     * @author : BugsRabbit
     * @email 395360255@qq.com
     * @version 创建时间：2015-12-1 下午2:45:23
     * @function ：刷新监听事件
     */
    private class MyRefreshListenner implements OnRefreshListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onRefresh(com
	 * .daguo.libs.PullToRefreshLayout)
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	    pageIndex = 1;
	    loadFeedbackData();

	    // 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
	    new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    // 千万别忘了告诉控件刷新完毕了哦！

		    pullToRefreshLayout
			    .refreshFinish(PullToRefreshLayout.SUCCEED);
		}
	    }.sendEmptyMessageDelayed(0, 3000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.daguo.libs.PullToRefreshLayout.OnRefreshListener#onLoadMore(com
	 * .daguo.libs.PullToRefreshLayout)
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	    pageIndex++;
	    loadFeedbackData();
	    // 下拉刷新操作 仅仅为了实现好看，觉得努力加载中的样子
	    new Handler() {
		@Override
		public void handleMessage(Message msg) {
		    // 千万别忘了告诉控件刷新完毕了哦！

		    pullToRefreshLayout
			    .refreshFinish(PullToRefreshLayout.SUCCEED);
		}
	    }.sendEmptyMessageDelayed(0, 3000);
	}

    }

}
