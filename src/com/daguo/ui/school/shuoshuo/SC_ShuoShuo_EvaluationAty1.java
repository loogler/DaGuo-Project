/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.shuoshuo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.modem.photo.MyGridAdapter;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.ui.message.Chat_Aty;
import com.daguo.util.adapter.Eva_OrdinaryAdapter;
import com.daguo.util.base.CircularImage;
import com.daguo.util.base.MyGridView;
import com.daguo.util.beans.Evaluate_Ordinary;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-11 下午4:36:06
 * @function ：说说评论主界面
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class SC_ShuoShuo_EvaluationAty1 extends Activity implements
	OnClickListener, OnRefreshListener {
    //
    private final int MSG_CONTENT = 100;
    private final int MSG_GOOD_DATA_Y = 101;
    private final int MSG_GOOD_DATA_N = 102;
    private final int MSG_FEEDBACK_DATA = 103;
    private final int MSG_GOODADD_SUC = 104;
    private final int MSG_GOODADD_FAIL = 105;
    private final int MSG_GOODMIN_SUC = 106;
    private final int MSG_GOODMIN_FAIL = 107;
    private final int MSG_FEEDBACK_SUC = 108;
    private final int MSG_FEEDBACK_FAIL = 109;

    /**
     * initViews
     */
    private PullToRefreshLayout refresh_view;
    private ListView content_view;

    // initTopViews
    private View topView;
    private TextView name_tv, schoolname_tv, content_tv, date_tv, type_tv,
	    share_tv, good_tv, feedback_tv, chat_tv, goodCount_tv;
    private ImageView sex_iv, good_iv;
    private CircularImage photo;
    private MyGridView content_grid;
    private NoScrollGridView photo_grid;

    // popupwindow
    private PopupWindow editWindow;
    private EditText replyEdit;
    private Button sendBtn;

    private InputMethodManager manager;

    /**
     * data
     */

    private String id = null;// 说说id
    private String p_id = null;// 用户id
    private String p_photo = null;// 用户头像
    private int pageIndex = 1;// 页码数
    private boolean isGoodAdd = false;// 是否点赞

    //
    private String feedbackContent;

    // topView Data
    private ShuoShuoContent contentList = new ShuoShuoContent();
    private List<HeadInfo> headInfos;

    // feedback Data
    private List<Evaluate_Ordinary> evaLists = new ArrayList<Evaluate_Ordinary>();
    private Evaluate_Ordinary evaList = null;
    private Eva_OrdinaryAdapter adapter;

    /**
     * tools
     */
    Message msg;
    Handler handler = new Handler() {
	@SuppressWarnings("unchecked")
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT:
		setContentData();
		content_view.addHeaderView(topView);
		loadFeedbackData();
		adapter = new Eva_OrdinaryAdapter(
			SC_ShuoShuo_EvaluationAty1.this, evaLists);
		content_view.setAdapter(adapter);
		break;

	    case MSG_GOOD_DATA_Y:
		// 有点赞记录

		good_iv.setImageResource(R.drawable.schoolmain4_icon_dianzan);
		break;
	    case MSG_GOOD_DATA_N:

		good_iv.setImageResource(R.drawable.sc_shuoshuo_zan);
		break;

	    case MSG_FEEDBACK_DATA:

		List<Evaluate_Ordinary> aaa = (List<Evaluate_Ordinary>) msg.obj;
		evaLists.addAll(aaa);
		adapter.notifyDataSetChanged();

		break;

	    case MSG_GOODADD_SUC:

		good_iv.setImageResource(R.drawable.schoolmain4_icon_dianzan);
		break;
	    case MSG_GOODADD_FAIL:

		Toast.makeText(SC_ShuoShuo_EvaluationAty1.this, "操作失败，请稍后重试",
			Toast.LENGTH_LONG).show();
		break;

	    case MSG_GOODMIN_SUC:

		good_iv.setImageResource(R.drawable.sc_shuoshuo_zan);
		break;

	    case MSG_GOODMIN_FAIL:

		Toast.makeText(SC_ShuoShuo_EvaluationAty1.this, "操作失败，请稍后重试",
			Toast.LENGTH_LONG).show();

		break;
	    case MSG_FEEDBACK_SUC:
		replyEdit.setText("");
		feedbackContent = null;
		editWindow.dismiss();
		loadFeedbackData();

		break;

	    case MSG_FEEDBACK_FAIL:
		Toast.makeText(SC_ShuoShuo_EvaluationAty1.this, "提交评论失败，请稍后重试",
			Toast.LENGTH_SHORT).show();

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
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_sc_shuoshuo_evaluation);
	Intent intent = getIntent();
	id = intent.getStringExtra("id");
	@SuppressWarnings("deprecation")
	SharedPreferences sp = getSharedPreferences("userinfo",
		Activity.MODE_WORLD_READABLE);
	p_id = sp.getString("id", "");
	p_photo = sp.getString("head_info", "");

	initHeadView();
	initViews();
	loadContentData();
	loadGoodData();

    }

    /*************************************** init view *******************************************************/
    /**
     * 
     */
    private void initViews() {

	refresh_view = (PullToRefreshLayout) findViewById(R.id.refresh_view);
	content_view = (ListView) findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(this);

	initTopViews();

    }

    /**
     * 
     */
    @SuppressLint("InflateParams")
    private void initTopViews() {
	topView = LayoutInflater.from(SC_ShuoShuo_EvaluationAty1.this).inflate(
		R.layout.item_sc_shuoshuo_evaluation, null);
	name_tv = (TextView) topView.findViewById(R.id.name_tv);
	schoolname_tv = (TextView) topView.findViewById(R.id.schoolname_tv);
	content_tv = (TextView) topView.findViewById(R.id.content_tv);
	date_tv = (TextView) topView.findViewById(R.id.date_tv);
	type_tv = (TextView) topView.findViewById(R.id.type_tv);
	share_tv = (TextView) topView.findViewById(R.id.share_tv);
	good_tv = (TextView) topView.findViewById(R.id.good_tv);
	feedback_tv = (TextView) topView.findViewById(R.id.feedback_tv);
	chat_tv = (TextView) topView.findViewById(R.id.chat_tv);
	goodCount_tv = (TextView) topView.findViewById(R.id.goodCount_tv);

	sex_iv = (ImageView) topView.findViewById(R.id.sex_iv);
	good_iv = (ImageView) topView.findViewById(R.id.good_iv);

	photo = (CircularImage) topView.findViewById(R.id.photo);
	content_grid = (MyGridView) topView.findViewById(R.id.content_grid);
	photo_grid = (NoScrollGridView) topView.findViewById(R.id.photo_grid);

	chat_tv.setOnClickListener(this);
	share_tv.setOnClickListener(this);
	good_tv.setOnClickListener(this);
	feedback_tv.setOnClickListener(this);

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
	title_tv.setText("说说详情");
	function_tv.setVisibility(View.GONE);
	remind_iv.setVisibility(View.GONE);

    }

    /**
     * 初始化评论弹出框
     * 
     */
    private void initPopupWindow() {
	View editView = getLayoutInflater().inflate(
		R.layout.item_popup_editinput, null);
	editWindow = new PopupWindow(editView, LayoutParams.MATCH_PARENT,
		LayoutParams.WRAP_CONTENT, true);
	editWindow.setBackgroundDrawable(getResources().getDrawable(
		R.color.white));
	editWindow.setOutsideTouchable(true);
	replyEdit = (EditText) editView.findViewById(R.id.reply);
	sendBtn = (Button) editView.findViewById(R.id.send_msg);
	sendBtn.setOnClickListener(SC_ShuoShuo_EvaluationAty1.this);
    }

    /**
     * 显示回复评论框
     * 
     * @param reply
     */
    private void showDiscuss() {
	replyEdit.setFocusable(true);
	replyEdit.requestFocus();

	// 设置焦点，不然无法弹出输入法
	editWindow.setFocusable(true);

	// 以下两句不能颠倒
	editWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
	editWindow
		.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	editWindow.showAtLocation(refresh_view, Gravity.BOTTOM, 0, 0);

	// 显示键盘
	manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	editWindow.setOnDismissListener(new OnDismissListener() {
	    @Override
	    public void onDismiss() {
		manager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
	    }
	});

    }

    /***************************************** set data *****************************************************************/
    /**
     * 說說內容
     */
    private void setContentData() {

	if (contentList == null) {
	    return;
	}
	if (contentList.getContent().isEmpty()) {
	    content_tv.setVisibility(View.GONE);
	} else {
	    content_tv.setVisibility(View.VISIBLE);
	    content_tv.setText(contentList.getContent());
	}

	if (contentList.getCreatTime().isEmpty()) {
	    date_tv.setVisibility(View.GONE);
	} else {
	    date_tv.setVisibility(View.VISIBLE);
	    try {
		date_tv.setText(PublicTools.DateFormat(contentList
			.getCreatTime()));
	    } catch (ParseException e) {
		Log.e("说说列表适配", "dateformate出错");
		e.printStackTrace();
	    }
	}

	if (contentList.getImg_path().isEmpty()) {
	    content_grid.setVisibility(View.GONE);
	} else {
	    content_grid.setVisibility(View.VISIBLE);
	    // data 处理
	    String urls = contentList.getImg_path();
	    // ArrayList urlLists = new ArrayList(urls.split(","));

	    final String[] urlStrings = urls.split(",");
	    content_grid.setAdapter(new MyGridAdapter(urlStrings,
		    SC_ShuoShuo_EvaluationAty1.this, 2));
	    content_grid.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int p,
			long arg3) {
		    imageBrower(p, urlStrings);
		}
	    });
	}

	if (contentList.getP_name().isEmpty()) {
	    name_tv.setVisibility(View.GONE);
	} else {
	    name_tv.setVisibility(View.VISIBLE);
	    name_tv.setText(contentList.getP_name());
	}
	if (contentList.getP_photo().isEmpty()) {
	    photo.setImageResource(R.drawable.user_logo);
	} else {
	    photo.setVisibility(View.VISIBLE);
	    // viewHoldler.photo.setText(infos.get(position).getP_photo());
	    FinalBitmap.create(SC_ShuoShuo_EvaluationAty1.this).display(photo,
		    HttpUtil.IMG_URL + contentList.getP_photo());

	}

	if (contentList.getP_sex().isEmpty()) {
	    sex_iv.setVisibility(View.GONE);
	} else {
	    sex_iv.setVisibility(View.VISIBLE);
	    // viewHoldler.content.setText(infos.get(position).getContent());
	    if ("1".equals(contentList.getP_sex())) {
		// 女
		sex_iv.setImageResource(R.drawable.icon_sex_woman);
	    } else if ("0".equals(contentList.getP_sex())) {
		// 男
		sex_iv.setImageResource(R.drawable.icon_sex_man);

	    } else {
		// 性别不明
		sex_iv.setVisibility(View.GONE);
	    }
	}

	if (contentList.getSchool_name().isEmpty()) {
	    schoolname_tv.setVisibility(View.GONE);
	} else {
	    schoolname_tv.setVisibility(View.VISIBLE);
	    schoolname_tv.setText(contentList.getSchool_name());
	}

	if (contentList.getSigns() == null || contentList.getSigns().isEmpty()) {
	    photo_grid.setVisibility(View.GONE);
	} else {
	    photo_grid.setVisibility(View.VISIBLE);
	    List<HeadInfo> headInfos = contentList.getSigns();
	    List<String> headInfoLists = new ArrayList<String>();
	    for (int i = 0; i < headInfos.size(); i++) {
		String p_head_info = headInfos.get(i).getP_head_info();
		headInfoLists.add(p_head_info);
	    }
	    String[] str = (String[]) headInfoLists
		    .toArray(new String[headInfoLists.size()]);
	    photo_grid.setAdapter(new MyGridAdapter(str,
		    SC_ShuoShuo_EvaluationAty1.this, 1));
	    photo_grid.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int p,
			long arg3) {
		    // Toast.makeText(context, "点击头像" + p + "当前说说" + position,
		    // Toast.LENGTH_SHORT).show();
		    // Intent intent = new Intent();
		    // TODO 说说列表 跳转个人信息
		}
	    });

	}

	if (contentList.getType().isEmpty()) {
	    type_tv.setVisibility(View.GONE);
	} else {
	    type_tv.setVisibility(View.VISIBLE);
	    type_tv.setText(contentList.getType());
	}
	if (contentList.getGood_count().isEmpty()) {
	    goodCount_tv.setText("全部评论(" + "0)");
	} else {
	    goodCount_tv.setText("全部评论(" + contentList.getFeedback_count()
		    + ")");
	}

    }

    /*************************************** get data **************************************************************/

    /**
     * 加载评论列表
     */
    private void loadFeedbackData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_SHUOSHUO_EVA + "&t_id=" + id
			    + "&rows=15&page=" + pageIndex;
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);
		    if (js.getInt("total") > 0) {
			List<Evaluate_Ordinary> abc = new ArrayList<Evaluate_Ordinary>();
			JSONArray array = js.getJSONArray("rows");
			for (int i = 0; i < array.length(); i++) {
			    String content = array.optJSONObject(i).getString(
				    "content");
			    String create_time = array.optJSONObject(i)
				    .getString("create_time");
			    String head_info = array.optJSONObject(i)
				    .getString("head_info");
			    String p_id = array.optJSONObject(i).getString(
				    "p_id");
			    String p_name = array.optJSONObject(i).getString(
				    "p_name");
			    String pro_name = array.optJSONObject(i).getString(
				    "pro_name");
			    String sex = array.optJSONObject(i)
				    .getString("sex");
			    String start_year = array.optJSONObject(i)
				    .getString("start_year");

			    evaList = new Evaluate_Ordinary();
			    evaList.setContent(content);
			    evaList.setCreate_time(create_time);
			    evaList.setHead_info(head_info);
			    evaList.setP_id(p_id);
			    evaList.setP_name(p_name);
			    evaList.setPro_name(pro_name);
			    evaList.setSex(sex);
			    evaList.setStart_year(start_year);

			    abc.add(evaList);

			}
			msg = handler.obtainMessage(MSG_FEEDBACK_DATA);
			msg.obj = abc;
			msg.sendToTarget();
		    } else {
			// 无评论
		    }
		} catch (JSONException e) {

		} catch (Exception e) {

		}
	    }
	}).start();
    }

    /**
     * 加载内容
     */
    private void loadContentData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_SHUOSHUO + "&rows=1&page=1&id="
			    + id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject js = new JSONObject(res);

		    if (js.getInt("total") > 0) {

			JSONArray arr = js.getJSONArray("rows");
			for (int i = 0; i < arr.length(); i++) {

			    String id = arr.optJSONObject(i).getString("id");
			    String create_time = arr.optJSONObject(i)
				    .getString("create_time");
			    String img_path = arr.optJSONObject(i).getString(
				    "img_path");
			    String content = arr.optJSONObject(i).getString(
				    "content");
			    String good_count = arr.optJSONObject(i).getString(
				    "good_count");
			    String feedback_count = arr.optJSONObject(i)
				    .getString("feedback_count");
			    String type = arr.optJSONObject(i)
				    .getString("type");
			    String type_name = arr.optJSONObject(i).getString(
				    "type_name");
			    String school_id = arr.optJSONObject(i).getString(
				    "school_id");
			    String p_id = arr.optJSONObject(i)
				    .getString("p_id");
			    String p_name = arr.optJSONObject(i).getString(
				    "p_name");
			    String p_sex = arr.optJSONObject(i).getString(
				    "p_sex");
			    String school_name = arr.optJSONObject(i)
				    .getString("school_name");
			    String head_info = arr.optJSONObject(i).getString(
				    "head_info");

			    String tableName = arr.optJSONObject(i).getString(
				    "tableName");

			    JSONArray sign = arr.optJSONObject(i).getJSONArray(
				    "signs");
			    headInfos = new ArrayList<HeadInfo>();
			    if (sign.length() > 0) {
				for (int j = 0; j < sign.length(); j++) {
				    HeadInfo headInfo = new HeadInfo();
				    String idString = sign.optJSONObject(j)
					    .getString("id");
				    String p_head_info = sign.optJSONObject(j)
					    .getString("p_head_info");
				    headInfo.setId(idString);
				    headInfo.setP_head_info(p_head_info);

				    headInfos.add(headInfo);

				}
			    } else {
				// 无人点赞
			    }

			    contentList.setId(id);
			    contentList.setCreatTime(create_time);
			    contentList.setImg_path(img_path);
			    contentList.setContent(content);
			    contentList.setGood_count(good_count);
			    contentList.setFeedback_count(feedback_count);
			    contentList.setType(type);
			    contentList.setType_name(type_name);
			    contentList.setSchool_id(school_id);
			    contentList.setP_id(p_id);
			    contentList.setP_name(p_name);
			    contentList.setSchool_name(school_name);
			    contentList.setSigns(headInfos);
			    contentList.setP_photo(head_info);
			    contentList.setTableName(tableName);
			    contentList.setP_sex(p_sex);

			}

			msg = handler.obtainMessage(MSG_CONTENT);
			msg.sendToTarget();
		    } else {
			// 数据为0
		    }
		} catch (Exception e) {
		    Log.e("最新说说", "loaddata获取信息失败");
		}

	    }
	}).start();
    }

    /**
     * 获得是否已经点咱
     */

    private void loadGoodData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_EVENT_APPLY
			    + "&table_name=tbl_topic&source_id=" + id
			    + "&type=1&p_id=" + p_id + "&page=1&rows=1";
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if (jsonObject.getInt("total") > 0) {
			isGoodAdd = true;
			// 有点赞记录
			msg = handler.obtainMessage(MSG_GOOD_DATA_Y);
			msg.sendToTarget();
		    } else {
			// 无点赞记录
			isGoodAdd = false;
			msg = handler.obtainMessage(MSG_GOOD_DATA_N);
			msg.sendToTarget();

		    }
		} catch (JSONException e) {
		    Log.e("说说评论", "获取点赞信息json异常");

		} catch (Exception e) {
		    Log.e("说说评论", "获取点赞信息异常");
		}
	    }
	}).start();

    }

    /**
     * 提交评论
     */
    private void submitFeedback() {
	new Thread(new Runnable() {
	    public void run() {
		try {

		    String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
		    Map<String, String> rawMaps = new HashMap<String, String>();
		    rawMaps.put("t_id", id);
		    rawMaps.put("p_id", p_id);
		    rawMaps.put("content", feedbackContent);

		    String res = HttpUtil.postRequest(url, rawMaps);
		    JSONObject js = new JSONObject(res);
		    if ("1".equals(js.getString("result"))) {
			// 成功
			msg = handler.obtainMessage(MSG_FEEDBACK_SUC);
			msg.sendToTarget();
		    } else {
			// 失败
			msg = handler.obtainMessage(MSG_FEEDBACK_FAIL);
			msg.sendToTarget();
		    }

		} catch (JSONException e) {
		    Log.e("说说评论", " 评论操作json异常");

		} catch (Exception e) {
		    Log.e("说说评论", " 评论操作异常");
		}
	    }
	}).start();
    }

    /**
     * 点赞 +1
     */
    private void goodAdd() {
	new Thread(new Runnable() {
	    public void run() {

		try {
		    String url = HttpUtil.SUBMIT_EVENT_APPLY
			    + "&table_name=tbl_topic&source_id=" + id
			    + "&type=1&p_id=" + p_id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if ("1".equals(jsonObject.getString("result"))) {
			// 成功
			isGoodAdd = true;
			msg = handler.obtainMessage(MSG_GOODADD_SUC);
			msg.sendToTarget();
		    } else {
			// 点赞失败
			isGoodAdd = false;
			msg = handler.obtainMessage(MSG_GOODADD_FAIL);
			msg.sendToTarget();

		    }
		} catch (JSONException e) {
		    Log.e("说说评论", " 点赞操作json异常");

		} catch (Exception e) {
		    Log.e("说说评论", "点赞操作 异常");
		}
	    }
	}).start();
    }

    /**
     * 点赞 -1
     */
    private void goodMin() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.SUBMIT_EVENT_CANCEL
			    + "&table_name=tbl_topic&source_id=" + id
			    + "&type=1&p_id=" + p_id;
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);
		    if ("1".equals(jsonObject.getString("result"))) {
			// 成功
			isGoodAdd = false;
			msg = handler.obtainMessage(MSG_GOODMIN_SUC);
			msg.sendToTarget();
		    } else {
			// 点赞失败
			isGoodAdd = true;
			msg = handler.obtainMessage(MSG_GOODMIN_FAIL);
			msg.sendToTarget();
		    }
		} catch (JSONException e) {
		    Log.e("说说评论", " 点赞取消操作json异常");

		} catch (Exception e) {
		    Log.e("说说评论", "点赞取消操作 异常");
		}
	    }
	}).start();
    }

    /*********************************** implement *********************************************/
    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {

	case R.id.send_msg:
	    feedbackContent = replyEdit.getText().toString().trim();
	    if (feedbackContent != null && !feedbackContent.isEmpty()) {
		submitFeedback();
	    } else {
		Toast.makeText(SC_ShuoShuo_EvaluationAty1.this, "输入内容不能为空",
			Toast.LENGTH_LONG).show();
	    }

	    break;

	case R.id.share_tv:

	    Intent sendIntent = new Intent();
	    sendIntent.setAction(Intent.ACTION_SEND);
	    sendIntent.putExtra(Intent.EXTRA_TEXT, "大果校园下载地址"
		    + HttpUtil.DOWNLOAD);// 分享内容
	    sendIntent.setType("text/plain");
	    startActivity(Intent.createChooser(sendIntent, "大果校园欢迎您"));// 分享标题

	    break;

	case R.id.feedback_tv:

	    initPopupWindow();
	    showDiscuss();

	    break;
	case R.id.good_tv:
	    if (isGoodAdd) {
		goodMin();
	    } else {
		goodAdd();
	    }

	    break;
	case R.id.chat_tv:
	    Intent intent = new Intent(SC_ShuoShuo_EvaluationAty1.this,
		    Chat_Aty.class);

	    intent.putExtra("id", p_id);
	    intent.putExtra("photo", p_photo);
	    startActivity(intent);

	    break;

	default:
	    break;
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onRefresh
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	// 下拉刷新操作
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		// 千万别忘了告诉控件刷新完毕了哦！
		pageIndex = 1;
		loadFeedbackData();
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 2000);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onLoadMore
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @Override
    public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
	new Handler() {
	    @Override
	    public void handleMessage(Message msg) {
		pageIndex++;
		loadFeedbackData();
		// 千万别忘了告诉控件加载完毕了哦！
		pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
	    }
	}.sendEmptyMessageDelayed(0, 2000);
    }

    /***************************** private tools **************************************************/

    /**
     * 
     * @param position
     * @param urls
     */
    private void imageBrower(int position, String[] urls) {
	Intent intent = new Intent(SC_ShuoShuo_EvaluationAty1.this,
		com.daguo.modem.photo.ImagePagerActivity.class);
	// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
	intent.putExtra(
		com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
		position);
	startActivity(intent);
    }

}
