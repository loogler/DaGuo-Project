/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.ViewPagerSlipper;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-14 上午11:45:22
 * @function ：
 */
public class Shop_SearchAty extends FragmentActivity implements OnClickListener {

    /**
     * initViews
     */
    private EditText search_edt;
    private TextView submit_tv, back_tv;
    // 搜索结果部分
    public ViewPagerSlipper viewPager;
    public List<Fragment> fragments = new ArrayList<Fragment>();
    private ImageView comprehensive_iv, number_iv, price_iv;
    private TextView comprehensive_tv, number_tv, price_tv;

    /**
     * data
     */
    private String searchString;

    // 切换
    private int currIndex = 0;// 当前页卡编号
    private int zero = 0;// 动画图片偏移量
    private int one;// 单个水平动画位移
    private int two;
    private int three;
    private FragmentManager fragmentManager;
    private int displayWidth, displayHeight;

    /**
     * tools
     */

    String type_id, id;

    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.aty_shop_search);

	initViews();
	Intent intent = getIntent();
	type_id = PublicTools.doWithNullData(intent.getStringExtra("type_id"));
	id = PublicTools.doWithNullData(intent.getStringExtra("id"));

	// fragments.add();
    }

    /**
     * 
     */
    private void initViews() {
	search_edt = (EditText) findViewById(R.id.search_edt);
	submit_tv = (TextView) findViewById(R.id.submit_tv);
	back_tv = (TextView) findViewById(R.id.back_tv);

	submit_tv.setOnClickListener(this);
	back_tv.setOnClickListener(this);

	// goods
	comprehensive_iv = (ImageView) findViewById(R.id.comprehensive_iv);
	number_iv = (ImageView) findViewById(R.id.number_iv);
	price_iv = (ImageView) findViewById(R.id.price_iv);
	comprehensive_tv = (TextView) findViewById(R.id.comprehensive_tv);
	number_tv = (TextView) findViewById(R.id.number_tv);
	price_tv = (TextView) findViewById(R.id.price_tv);

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
	switch (v.getId()) {
	case R.id.back_tv:
	    System.gc();
	    finish();
	    break;
	case R.id.submit_tv:
	    searchString = search_edt.getText().toString().trim();
	    PublicTools.doWithNullData(searchString);
	    // TODO searchForGoods();
	    break;

	default:
	    break;
	}
    }

}
