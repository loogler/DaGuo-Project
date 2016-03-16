/**
 * 互相学习 共同进步
 */
package com.daguo.util.Imp;

import com.daguo.ui.commercial.Shop_GoodsDetailAty;
import com.daguo.ui.main.Main_1Aty;
import com.daguo.ui.main.WebView_CommenAty;
import com.daguo.ui.school.huodong.SC_HuoDongAty1;
import com.daguo.ui.school.huodong.SC_HuoDong_DetailAty1;
import com.daguo.ui.school.shetuan.SC_SheTuanAty;
import com.daguo.ui.school.shetuan.SC_SheTuanDetailAty;
import com.daguo.ui.school.xinwen.SC_XinWenAty;
import com.daguo.ui.school.xinwen.SC_XinWen_DetailAty;
import com.daguo.ui.school.zhuanti.SC_ZhuanTiDetailAty;
import com.daguo.util.beans.AddBanner;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2016-1-11 上午11:13:16
 * @function ：
 */
public class AddBannerOnclickListener implements OnClickListener {

	Activity activity;
	List<AddBanner> lists;
	int position;

	public AddBannerOnclickListener(Activity activity,
			List<AddBanner> addBanners, int position) {
		this.position = position;
		this.lists = addBanners;
		this.activity = activity;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {

		String sourceId = null;
		String menuId = null;
		String type = null;
		String url = null;
		sourceId = lists.get(position).getSource_id();
		type = lists.get(position).getType();

		if (type.equals("1")) {// 外连接
			url = lists.get(position).getUrl();
			Intent intent = new Intent(activity, WebView_CommenAty.class);
			intent.putExtra("URL", url);
			activity.startActivity(intent);
		} else if ("2".equals(type)) {// 跳转栏目
			menuId = lists.get(position).getMenu_id();
			if ("b3b7866c-3bf9-48a7-8caa-effa1fb86782".equals(menuId)) {
				// 校园活动
				Intent intent = new Intent(activity, SC_HuoDongAty1.class);
				activity.startActivity(intent);

			} else if ("7176add9-6d46-4c97-8983-362848304388".equals(menuId)) {
				// 校园新鲜事
				Intent intent = new Intent(activity, SC_XinWenAty.class);
				activity.startActivity(intent);
			} else if ("db94a88d-5c78-448b-a3a7-4af1c3850571".equals(menuId)) {
				// 校园新闻
				Intent intent = new Intent(activity, SC_XinWenAty.class);
				activity.startActivity(intent);

			} else if ("d6c986c5-8e52-485e-9a6e-d5d98480564e".equals(menuId)) {
				// 校园社团
				Intent intent = new Intent(activity, SC_SheTuanAty.class);
				activity.startActivity(intent);
			}
		} else if (type.equals("3")) {// 跳转数据
			menuId = lists.get(position).getMenu_id();
			sourceId = lists.get(position).getSource_id();
			if ("b3b7866c-3bf9-48a7-8caa-effa1fb86782".equals(menuId)) {
				// 校园活动
				Intent intent = new Intent(activity,
						SC_HuoDong_DetailAty1.class);
				intent.putExtra("id", sourceId);
				activity.startActivity(intent);

			} else if ("7176add9-6d46-4c97-8983-362848304388".equals(menuId)) {
				// 校园新鲜事
				Intent intent = new Intent(activity, SC_XinWen_DetailAty.class);
				intent.putExtra("id", sourceId);
				activity.startActivity(intent);
			} else if (menuId.equals("db94a88d-5c78-448b-a3a7-4af1c3850571")) {
				// 校园新闻
				Intent intent = new Intent(activity, SC_XinWen_DetailAty.class);
				intent.putExtra("id", sourceId);
				activity.startActivity(intent);

			} else if ("d6c986c5-8e52-485e-9a6e-d5d98480564e".equals(menuId)) {
				// 校园社团
				Intent intent = new Intent(activity, SC_SheTuanDetailAty.class);
				intent.putExtra("id", sourceId);
				activity.startActivity(intent);
			}
		} else if (type.equals("4")) {// 跳转专题
			// TODO 跳转专题需要处理数据
			Intent intent = new Intent(activity, SC_ZhuanTiDetailAty.class);
			intent.putExtra("id", sourceId);
			activity.startActivity(intent);
		} else if (type.equals("5")) {// 跳转商品

			Intent intent = new Intent(activity, Shop_GoodsDetailAty.class);
			intent.putExtra("id", sourceId);
			activity.startActivity(intent);

		}

	}

}
