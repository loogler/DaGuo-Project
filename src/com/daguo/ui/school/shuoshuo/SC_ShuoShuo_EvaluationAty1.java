/**
 * 互相学习 共同进步
 */
package com.daguo.ui.school.shuoshuo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.modem.photo.MyGridAdapter;
import com.daguo.modem.photo.NoScrollGridView;
import com.daguo.util.base.CircularImage;
import com.daguo.util.base.MyGridView;
import com.daguo.util.beans.HeadInfo;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-11 下午4:36:06
 * @function ：
 */
public class SC_ShuoShuo_EvaluationAty1 extends Activity implements
	OnClickListener {
    //
    private final int MSG_CONTENT = 100;

    /**
     * initViews
     */
    private EditText feedbackContent_edt;
    private Button submit_btn;

    // initTopViews
    private View topView;
    private TextView name_tv, schoolname_tv, content_tv, date_tv, type_tv,
	    share_tv, good_tv, feedback_tv;
    private ImageView sex_iv;
    private CircularImage photo;
    private MyGridView content_grid;
    private NoScrollGridView photo_grid;

    /**
     * data
     */

    private String id = null;// 说说id

    // topView Data
    private ShuoShuoContent contentList = new ShuoShuoContent();
    private List<HeadInfo> headInfos;

    /**
     * tools
     */
    Message msg;
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_CONTENT:
		setContentData();
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
	setContentView(R.layout.aty_sc_shuoshuo_evaluation);
	Intent intent = getIntent();
	id = intent.getStringExtra("id");

	initHeadView();
	initViews();
	loadContentData();
	loadFeedbackData();

    }

    /*************************************** init view *******************************************************/
    /**
     * 
     */
    private void initViews() {
	feedbackContent_edt = (EditText) findViewById(R.id.feedbackContent_edt);
	submit_btn = (Button) findViewById(R.id.submit_btn);

	submit_btn.setOnClickListener(this);
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

	sex_iv = (ImageView) topView.findViewById(R.id.sex_iv);

	photo = (CircularImage) topView.findViewById(R.id.photo);
	content_grid = (MyGridView) topView.findViewById(R.id.content_grid);
	photo_grid = (NoScrollGridView) topView.findViewById(R.id.photo_grid);

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

    }

    /*************************************** get data **************************************************************/

    /**
     * 加载评论列表
     */
    private void loadFeedbackData() {
	new Thread(new Runnable() {
	    public void run() {
		// TODO
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
     * 提交评论
     */
    private void submitFeedback() {
	new Thread(new Runnable() {
	    public void run() {
		// TODO
	    }
	}).start();
    }

    /**
     * 点赞 +1
     */
    private void goodAdd() {
	new Thread(new Runnable() {
	    public void run() {
		// TODO
	    }
	}).start();
    }

    /**
     * 点赞 -1
     */
    private void goodMin() {
	new Thread(new Runnable() {
	    public void run() {
		// TODO
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
	case R.id.submit_btn:

	    break;

	default:
	    break;
	}

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
