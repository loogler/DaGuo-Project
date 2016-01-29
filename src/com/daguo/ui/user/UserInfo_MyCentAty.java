/**
 * 互相学习 共同进步
 */
package com.daguo.ui.user;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.util.adapter.UserInfo_MyCent_DailyTaskAdapter;
import com.daguo.util.adapter.UserInfo_MyCent_NewTaskAdapter;
import com.daguo.util.beans.AddBanner;
import com.daguo.util.beans.NewTask;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-22 下午2:58:31
 * @function ：
 */
public class UserInfo_MyCentAty extends Activity {

    private String p_id;
    private final int MSG_NEWTASK = 10001;
    private final int MSG_CENT = 10002;
    private final int MSG_BANNERLISTSCENT = 10003;
    private final int REQ_SHARE = 10004;

    /**
     * initViews
     */
    private TextView cent_tv, exchange_tv;
    private ImageView add_iv;

    private ListView newTask_list, dailyTask_list;
    // 新手任务
    private NewTask newTask;
    private List<NewTask> newTasks = new ArrayList<NewTask>();
    private UserInfo_MyCent_NewTaskAdapter newTaskAdapte;
    // 每日任务
    private List<String> nameLists = new ArrayList<String>();
    private List<String> centLists = new ArrayList<String>();
    private UserInfo_MyCent_DailyTaskAdapter dailyTaskAdapter;
    // 广告栏
    private List<AddBanner> addLists = new ArrayList<AddBanner>();
    /**
     * data
     */
    private String cent;

    Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_NEWTASK:
		newTaskAdapte.notifyDataSetChanged();

		break;
	    case MSG_CENT:
		cent_tv.setText(PublicTools.doWithNullData(cent));

		break;
	    case MSG_BANNERLISTSCENT:
		FinalBitmap.create(UserInfo_MyCentAty.this).display(add_iv,
			HttpUtil.IMG_URL + addLists.get(0).getImg_path());
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
	setContentView(R.layout.aty_userinfo_mycent);
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");
	if ("".equals(PublicTools.doWithNullData(p_id))) {
	    Toast.makeText(this, "用户登录信息异常", Toast.LENGTH_LONG).show();
	    return;
	}

	initViews();
	loadCentInfo();
	loadAddBanner();
	loadNewTask();

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onActivityResult(int, int,
     * android.content.Intent)
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);
	switch (resultCode) {
	case 0:
	    
	    break;

	default:
	    break;
	}
    }

    /**
     * 初始化界面
     */
    private void initViews() {
	initHeadView();
	cent_tv = (TextView) findViewById(R.id.cent_tv);
	exchange_tv = (TextView) findViewById(R.id.exchange_tv);
	add_iv = (ImageView) findViewById(R.id.add_iv);
	newTask_list = (ListView) findViewById(R.id.newTask_list);
	dailyTask_list = (ListView) findViewById(R.id.dailyTask_list);

	newTaskAdapte = new UserInfo_MyCent_NewTaskAdapter(this, newTasks);
	newTask_list.setAdapter(newTaskAdapte);
	newTask_list.setOnItemClickListener(new OnItemClickListener() {

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int p,
		    long arg3) {
		if ("0".equals(newTasks.get(p).getState())) {

		} else if ("1".equals(newTasks.get(p).getState())) {
		    Toast.makeText(UserInfo_MyCentAty.this, "已完成",
			    Toast.LENGTH_SHORT).show();

		} else {
		    Toast.makeText(UserInfo_MyCentAty.this, "信息错误",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	nameLists.add("打开客户端");
	nameLists.add("发大果帖");
	nameLists.add("帖子被评论");
	nameLists.add("点赞/评论");
	nameLists.add("被赞次数>3");
	centLists.add("2");
	centLists.add("5");
	centLists.add("2");
	centLists.add("1");
	centLists.add("10");
	dailyTaskAdapter = new UserInfo_MyCent_DailyTaskAdapter(this,
		nameLists, centLists);
	dailyTask_list.setAdapter(dailyTaskAdapter);

    }

    private void setIntent(Intent intent, int p) {
	String idString = newTasks.get(p).getId();
	if ("071f777d-85cb-43e7-a276-da136057d12f".equals(idString)) {
	    // 关注某同学
	} else if ("0906df1c-613e-4cae-8b3a-136f8e86fce5".equals(idString)) {
	    // 推荐app给好友
	    Intent sendIntent = new Intent();
	    sendIntent.setAction(Intent.ACTION_SEND);
	    sendIntent.putExtra(Intent.EXTRA_TEXT,
		    "我在大果校园免费兑换了礼物哎，免费的哟，还能兑换话费流量，不要太赞，你快来看看吧，顺手就能兑换一个礼物呢！传送门"
			    + HttpUtil.DOWNLOAD);// 分享内容
	    sendIntent.setType("text/plain");
	     startActivity(Intent.createChooser(sendIntent, "大果校园"));// 分享标题
//	    startActivityForResult(Intent.createChooser(sendIntent, "大果校园"),
//		    REQ_SHARE);
	} else if ("19416592-1497-4211-a20b-a4e518cb51e6".equals(idString)) {
	    // 分享告示
	} else if ("2a35d32a-e3f7-4427-8ac1-2e6907d79128".equals(idString)) {
	    // 第一次发表评论
	} else if ("a3306d73-6087-4c64-acdd-2977f33f3b7d".equals(idString)) {
	    // 分身份认证
	} else if ("a38e0962-7382-45c8-b113-e0dce7d24035".equals(idString)) {
	    // 第一次发帖
	}

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
	title_tv.setText("我的积分");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /*-------------- 异步获取数据------- ----------------------------*/

    /**
     * 获取新手任务详情
     */
    private void loadNewTask() {

	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_MYCENT + "&page=1&rows=1&p_id="
			    + p_id;
		    String res = HttpUtil.getRequest(url);

		    JSONArray array = new JSONArray(res);
		    if (array.length() > 0) {
			for (int i = 0; i < array.length(); i++) {
			    String name = array.optJSONObject(i).getString(
				    "name");
			    String grade = array.optJSONObject(i).getString(
				    "grade");
			    String id = array.optJSONObject(i).getString("id");
			    String state = array.optJSONObject(i).getString(
				    "state");

			    newTask = new NewTask();
			    newTask.setGrade(grade);
			    newTask.setName(name);
			    newTask.setId(id);
			    newTask.setState(state);
			    newTasks.add(newTask);

			}
			handler.sendEmptyMessage(MSG_NEWTASK);

		    } else {
			// 没有数据
		    }

		} catch (Exception e) {
		}
	    }
	}).start();

    }

    private void loadAddBanner() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_ADD_BANNER
			    + "&position=13&page=1&rows=16";
		    String res = "";
		    JSONObject js = null;
		    int total = 0;
		    res = HttpUtil.getRequest(url);
		    js = new JSONObject(res);
		    total = js.getInt("total");
		    AddBanner list = null;
		    if (total == 0) {
			// 无广告 ，或者加载异常
			Log.e("主页获取广告信息", "获取首页顶部广告信息异常");

		    } else {
			JSONArray array = js.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String id = array.optJSONObject(i).getString("id");
			    String img_path = array.optJSONObject(i).getString(
				    "img_path");
			    String menu_id = array.optJSONObject(i).getString(
				    "menu_id");
			    String source_id = array.optJSONObject(i)
				    .getString("source_id");
			    String type = array.optJSONObject(i).getString(
				    "type");
			    String urls = array.optJSONObject(i).getString(
				    "url");
			    list = new AddBanner();
			    list.setId(id);
			    list.setImg_path(img_path);
			    list.setMenu_id(menu_id);
			    list.setSource_id(source_id);
			    list.setType(type);
			    list.setUrl(urls);
			    addLists.add(list);

			}
			msg = handler.obtainMessage(MSG_BANNERLISTSCENT);
			msg.sendToTarget();
		    }

		} catch (JSONException e) {
		    e.printStackTrace();
		    Log.e("新闻获取广告信息", "获取广告json异常");
		} catch (Exception e) {
		    Log.e("新闻获取广告信息", "获取广告异常");
		    e.printStackTrace();
		}
	    }
	}).start();
    }

    /**
     * 获取个人积分
     */
    private void loadCentInfo() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_USERINFO
			    + "&page=1&rows=1&p_id=" + p_id;

		    String res = HttpUtil.getRequest(url);

		    JSONObject jsonObject = new JSONObject(res);

		    JSONArray array = jsonObject.getJSONArray("rows");
		    cent = array.optJSONObject(0).getString("score");

		} catch (Exception e) {
		}
		handler.sendEmptyMessage(MSG_CENT);
	    }
	}

	).start();
    }

    /*----------- 实体类 -----------------*/

}
