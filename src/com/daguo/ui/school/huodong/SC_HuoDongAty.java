package com.daguo.ui.school.huodong;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.adapter.SC_HuoDongAdapter;
import com.daguo.util.beans.SC_HuoDong;
import com.daguo.util.pulllistview.PullToRefreshView;
import com.daguo.util.pulllistview.PullToRefreshView.OnFooterRefreshListener;
import com.daguo.util.pulllistview.PullToRefreshView.OnHeaderRefreshListener;
import com.daguo.utils.HttpUtil;

/**
 * 活动界面--校园部分的
 * 
 * @author Bugs_Rabbit 時間： 2015-10-5 下午4:01:21
 */
public class SC_HuoDongAty extends Activity implements OnClickListener,
		OnHeaderRefreshListener, OnFooterRefreshListener {
	/**
	 * views
	 */
	private TextView shaixuanTextView, xiaoxiTextView, xuanzeTextView;
	private PullToRefreshView pullGrid;
	GridView mGridView;
	/**
	 * 顶部时间选择框 的部分
	 */
	Dialog dialog;
	Window window;
	private String searchString;// 传入搜索条件的参数

	/**
	 * 适配器 adapter部分
	 */
	private List<SC_HuoDong> lists = new ArrayList<SC_HuoDong>();
	private SC_HuoDong list;
	private SC_HuoDongAdapter adapter;

	/**
	 * 页码
	 */
	private int pageIndex = 1;
	/**
	 * thread
	 */
	Thread getDataThread;// 初始化界面数据的线程 可被控制
	Message msg;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:// 加载数据
				List<SC_HuoDong> bb = (List<SC_HuoDong>) msg.obj;
				if (bb != null && !bb.equals("")) {
					lists.addAll(bb);
					adapter.notifyDataSetChanged();
				}

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_huodong);
		initViews();
	}

	/**
	 * 初始化控件
	 * */
	private void initViews() {

		shaixuanTextView = (TextView) findViewById(R.id.shaixuan);
		xiaoxiTextView = (TextView) findViewById(R.id.xiaoxi);
		xuanzeTextView = (TextView) findViewById(R.id.xuanze);
		pullGrid = (PullToRefreshView) findViewById(R.id.pull_grid);
		mGridView = (GridView) findViewById(R.id.gridview);
		loadData();// 加载数据
		adapter = new SC_HuoDongAdapter(SC_HuoDongAty.this, lists);
		mGridView.setAdapter(adapter);
		pullGrid.setOnHeaderRefreshListener(this);
		pullGrid.setOnFooterRefreshListener(this);

		shaixuanTextView.setOnClickListener(this);
		xiaoxiTextView.setOnClickListener(this);
		xuanzeTextView.setOnClickListener(this);

	}

	/***************** 获取活动信息线程 **************************************************************************/
	/**
	 * 加载活动信息
	 */
	private void loadData() {

		getDataThread = new Thread(

		new Runnable() {
			public void run() {
				try {
					// 获取数据的线程
					String url = HttpUtil.QUERY_HUODONG + "&rows=10&page="
							+ pageIndex;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					JSONArray array = js.getJSONArray("rows");
					List<SC_HuoDong> aaa = new ArrayList<SC_HuoDong>();
					if (js.getInt("total") > 0) {
						for (int i = 0; i < array.length(); i++) {
							list = new SC_HuoDong();
							String id = array.optJSONObject(i).getString("id");
							String create_time = array.optJSONObject(i)
									.getString("create_time");
							String content = array.optJSONObject(i).getString(
									"content");
							String img_path = array.optJSONObject(i).getString(
									"img_path");// 这是列表显示的图片
							String title = array.optJSONObject(i).getString(
									"title");
							String sort = array.optJSONObject(i).getString(
									"sort");
							String title2 = array.optJSONObject(i).getString(
									"title2");
							String school_id = array.optJSONObject(i)
									.getString("school_id");
							String img_src = array.optJSONObject(i).getString(
									"img_src");// content 图片列表
							String s_date = array.optJSONObject(i).getString(
									"s_date");
							String e_date = array.optJSONObject(i).getString(
									"e_date");
							String s_time = array.optJSONObject(i).getString(
									"s_time");
							String e_time = array.optJSONObject(i).getString(
									"e_time");
							String tag = array.optJSONObject(i)
									.getString("tag");
							String good_count = array.optJSONObject(i)
									.getString("good_count");
							String feedback_count = array.optJSONObject(i)
									.getString("feedback_count");
							list.setId(id);
							list.setCreate_time(create_time);
							list.setContent(content);
							list.setImg_path(img_path);
							list.setTitle(title);
							list.setSort(sort);
							list.setTitle2(title2);
							list.setSchool_id(school_id);
							list.setImg_src(img_src);
							list.setS_date(s_date);
							list.setE_date(e_date);
							list.setS_time(s_time);
							list.setE_time(e_time);
							list.setTag(tag);
							list.setGood_count(good_count);
							list.setFeedback_count(feedback_count);
							aaa.add(list);

						}
						msg = handler.obtainMessage(0);
						msg.obj = aaa;
						msg.sendToTarget();

					} else {
						// 无数据
					}
				} catch (Exception e) {
				}

			}
		});
		getDataThread.start();
	}

	/***************** 顶部及点击事件 *****************************************************************************/
	/**
	 * 实现顶部点击事件，
	 * 
	 * @author bugs_rabbit
	 */
	private class Clicks implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.jintian:
				xuanzeTextView.setText("今天");
				break;
			case R.id.mingtian:
				xuanzeTextView.setText("明天");
				break;
			case R.id.zhoumo:
				xuanzeTextView.setText("周末");
				break;
			case R.id.quanbu:
				xuanzeTextView.setText("全部");
				break;
			case R.id.yijieshu:
				xuanzeTextView.setText("已结束");
				break;
			default:
				break;
			}
			dialog.dismiss();
		}

	}

	// *****************implement onclick pull
	// 事件****************************************/
	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.shaixuan:
			// TODO 定义此处的点击事件触发功能
			break;
		case R.id.xiaoxi:
			// TODO 定义此处的点击事件触发功能
			break;
		case R.id.xuanze:
			/*
			 * lp.x与lp.y表示相对于原始位置的偏移.
			 * 当参数值包含Gravity.LEFT时,对话框出现在左边,所以lp.x就表示相对左边的偏移,负值忽略.
			 * 当参数值包含Gravity.RIGHT时,对话框出现在右边,所以lp.x就表示相对右边的偏移,负值忽略.
			 * 当参数值包含Gravity.TOP时,对话框出现在上边,所以lp.y就表示相对上边的偏移,负值忽略.
			 * 当参数值包含Gravity.BOTTOM时,对话框出现在下边,所以lp.y就表示相对下边的偏移,负值忽略.
			 * 当参数值包含Gravity.CENTER_HORIZONTAL时
			 * ,对话框水平居中,所以lp.x就表示在水平居中的位置移动lp.x像素,正值向右移动,负值向左移动.
			 * 当参数值包含Gravity.CENTER_VERTICAL时
			 * ,对话框垂直居中,所以lp.y就表示在垂直居中的位置移动lp.y像素,正值向右移动,负值向左移动.
			 * gravity的默认值为Gravity.CENTER,即Gravity.CENTER_HORIZONTAL |
			 * Gravity.CENTER_VERTICAL.
			 */
			// 创建dialog 利用自定义的style以去除系统默认时会有的黑色背景
			dialog = new Dialog(SC_HuoDongAty.this, R.style.sc_huodong_dialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);// 设置为没有标题
			dialog.show();// 显示出来
			window = dialog.getWindow();
			window.setContentView(R.layout.item_sc_huodong_spinneradapter);
			TextView jin = (TextView) window.findViewById(R.id.jintian);
			TextView ming = (TextView) window.findViewById(R.id.mingtian);
			TextView zhou = (TextView) window.findViewById(R.id.zhoumo);
			TextView yij = (TextView) window.findViewById(R.id.yijieshu);
			TextView quanbu = (TextView) window.findViewById(R.id.quanbu);
			jin.setOnClickListener(new Clicks());
			ming.setOnClickListener(new Clicks());
			zhou.setOnClickListener(new Clicks());
			yij.setOnClickListener(new Clicks());
			quanbu.setOnClickListener(new Clicks());
			WindowManager.LayoutParams lp = window.getAttributes();
			window.clearFlags(lp.FLAG_BLUR_BEHIND | lp.FLAG_DIM_BEHIND);
			window.setGravity(Gravity.CENTER | Gravity.TOP);
			// lp.x = 100;
			lp.y = 50;
			// lp.width = 300; // 宽度
			// lp.height = 300; // 高度
			lp.alpha = 0.8f; // 透明度
			window.setAttributes(lp);

			break;

		default:
			break;
		}
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		lists.clear();
		pageIndex++;
		loadData();
		pullGrid.onFooterRefreshComplete();

	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		lists.clear();
		pageIndex = 1;
		loadData();
		pullGrid.onHeaderRefreshComplete();

	}
}
