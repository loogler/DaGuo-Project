/**
 * 互相学习 共同进步
 */
package com.daguo.modem.choujiang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.utils.HttpUtil;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-29 上午11:01:26
 * @function ： 抽奖主界面
 */
public class LotteryAty extends Activity implements RotateListener,
	OnClickListener {

    private final int MSG_SHOW = 11112;
    private String p_id;

    private boolean isStart = true;

    // 文字提示
    private TextView title;
    // 中奖显示
    private TextView info;
    // 抽奖转盘
    private LotteryView lotteryView;
    // 指针按钮
    private DynamicImage arrowBtn;

    private int[] itemColor;// 选项颜色
    private String[] itemText;// 选项文字
    private String[] itemIds;// 文字对应的奖品id
    private String orderId;// 订单id
    private String awardId;// 抽中的奖品id
    public ArrayList<String> arrayList;
    private float surfacViewWidth = 0;
    private float surfacViewHeight = 0;

    // private SoundPool soundPool = null;
    // private int explosionId = 0; //内存加载ID
    // private int playSourceId = 0; //播放ID

    Message msg;

    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.aty_lottery);
	this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
	p_id = getSharedPreferences("userinfo", 0).getString("id", "");
	initView();

    }

    protected void onResume() {
	super.onResume();
	// if (soundPool == null) {
	// //指定声音池的最大音频流数目为10，声音品质为5
	// soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
	// //载入音频流，返回在池中的id
	// explosionId = soundPool.load(this, R.raw.music, 1);
	// }
    }

    protected void onPause() {
	super.onPause();
	// if (soundPool != null) {
	// soundPool.stop(explosionId);
	// soundPool.release();
	// soundPool = null;
	// }
	if (lotteryView != null) {
	    lotteryView.rotateDisable();
	}
    }

    /**
     * 
     * Description:初始化界面元素
     * 
     */
    public void initView() {
	initItem();
	title = (TextView) this.findViewById(R.id.title);
	info = (TextView) this.findViewById(R.id.info);
	arrowBtn = (DynamicImage) this.findViewById(R.id.arrowBtn);
	lotteryView = (LotteryView) this.findViewById(R.id.lotteryView);

	arrowBtn.setOnClickListener(this);
	lotteryView.initAll(itemColor, itemText);
	lotteryView.setRotateListener(this);
	lotteryView.start();

	surfacViewHeight = lotteryView.getHeight();
	surfacViewWidth = lotteryView.getWidth();

	Log.d("Log", "width = " + surfacViewWidth + ":height = "
		+ surfacViewHeight);
    }

    /**
     * 
     * Description:初始化转盘的颜色，文字
     * 
     */
    public void initItem() {

//	Intent intent = getIntent();
//	itemText = intent.getStringArrayExtra("names");
//	itemIds = intent.getStringArrayExtra("ids");
//	orderId = intent.getStringExtra("orderId");

	// 转盘选项的颜色
	itemColor = new int[] { 0xFFB0E0E6,// 粉蓝色　
		0xFF444444,// 深灰色
		0xFF008B8B,// 暗青色
		0xFFFFA500,// 橙色
		0xFF7FFF00,// 黄绿色
		0xFFF08080,// 亮珊瑚色
		0xFFB0C4DE,// 亮钢兰色
		0xFFFFFFFF // 白色
	};

//	 转盘选项的名称
	 itemText = new String[] { "恭喜发财", "智能手机", "5元话费", "2元话费", "1元话费",
	 "恭喜发财" };
    }

    /**
     * 
     * Description:转盘开始旋转
     * 
     * @param sp
     * @param isRoating
     */
    public void begin(float speed, int group, boolean isRoating) {
	lotteryView.setDirection(speed, group, isRoating);
	lotteryView.rotateEnable();
	/*
	 * 播放音频，第二个参数为左声道音量;第三个参数为右声道音量; 第四个参数为优先级；第五个参数为循环次数，0不循环，-1循环;
	 * 第六个参数为速率，速率 最低0.5最高为2，1代表正常速度
	 */
	// playSourceId = soundPool.play(explosionId,1, 1, 0, -1, 1);
    }

    String ms;
    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler() {
	public void handleMessage(android.os.Message msg) {

	    switch (msg.what) {

	    case MSG_SHOW:
		ms = (String) msg.obj;
		info.setText(ms);
		title.setText("恭喜您获得");
		if (!lotteryView.isRotateEnabled()) {
		    // soundPool.stop(playSourceId);
		    arrowBtn.stopRotation();
		}
		break;

	    case 10001:
		// 订单更新成功
		// Toast.makeText(LotteryAty.this, "订单更新成共", Toast.LENGTH_SHORT)
		// .show();
		new AlertDialog.Builder(LotteryAty.this)
			.setMessage("购买成功！")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {

					MyAppliation.getInstance().exit();
				    }
				}).create().show();

		break;

	    case 10002:
		// /订单更新失败
		// Toast.makeText(LotteryAty.this, "订单更新失败", Toast.LENGTH_SHORT)
		// .show();
		new AlertDialog.Builder(LotteryAty.this)
			.setMessage("订单生成失败，请联系管理员处理订单")
			.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {

				    @Override
				    public void onClick(DialogInterface dialog,
					    int which) {

					MyAppliation.getInstance().exit();
				    }
				}).create().show();
		break;

	    default:
		break;
	    }

	};
    };

    @Override
    public void showEndRotate(String str) {
	Message msg = handler.obtainMessage(MSG_SHOW);
	msg.obj = str;
	handler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {

	if (isStart) {

	    // 没有旋转状态
	    if (!lotteryView.isRotateEnabled()) {
		// title.setText("抽奖按钮变红时按下更容易中奖哦");
		begin(Math.abs(50), 8, false);
		arrowBtn.startRoation(new int[] { R.drawable.arrow_green,
			R.drawable.arrow_red }, 500);
	    }
	    // 旋转状态
	    else {
		// 一直旋转状态
		if (!lotteryView.isRoating()) {
		    // 设置中奖项(随机的话请注释)
		    lotteryView.setAwards(((int) Math.random() * 6) + 1);
		    // 设置为缓慢停止
		    lotteryView.setRoating(true);
		    title.setText("");
		    isStart = false;

		}
	    }
	} else {
	    arrowBtn.setClickable(false);
	    Toast.makeText(LotteryAty.this, "生成订单中。。", Toast.LENGTH_LONG)
		    .show();

	    new Handler().postDelayed(new Runnable() {
		public void run() {
		    loadSubmitInfo();
		}
	    }, 2500);

	}
    }

    /*----------数据线程------------------------------------------*/

    private void loadSubmitInfo() {

	new Thread(new Runnable() {
	    public void run() {
		try {

		    String url = HttpUtil.SUBMIT_LOTTERY + "&p_id=" + p_id
			    + "&gift_id=" + awardId;

		    // 积分 处理
		    String res = HttpUtil.getRequest(url);
		    JSONObject jsonObject = new JSONObject(res);

		    if ("1".equals(jsonObject.getString("result"))) {
			Log.i("修改中奖情况", "=======成功");

		    } else {
			// 修改错误
			Log.e("抽奖", "抽奖修改个人获奖信息错误");
		    }

		    // 修改 订单状态信息
		    String urlString2 = HttpUtil.SUBMIT_ORDER_PUB;
		    Map<String, String> map2 = new HashMap<String, String>();
		    map2.put("id", orderId);
		    map2.put("win", "1");
		    map2.put("gift_id", awardId);

		    String resString2 = HttpUtil.postRequest(urlString2, map2);
		    JSONObject jsonObject2 = new JSONObject(resString2);

		    if ("1".equals(jsonObject2.getString("result"))) {
			Log.i("修改订单状态", "======成功");
			handler.sendEmptyMessage(10001);
		    } else {
			Log.e("修改订单状态", "======失败");
			handler.sendEmptyMessage(10002);

		    }
		    // JSONObject jsonObject2= new JSONObject(resString2);
		    // dia.dismiss();

		} catch (Exception e) {
		}
	    }
	}).start();

    }
}
