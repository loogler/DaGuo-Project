package com.daguo.util.base;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.daguo.R;
import com.daguo.ui.commercial.Shop_GoodsDetailAty;
import com.daguo.ui.main.Main_2Aty;
import com.daguo.util.adapter.Mall_ItemAdapter;
import com.daguo.util.beans.Type;
import com.daguo.utils.AsyncImageLoader2;

/**
 * 具体销售商品 二级菜单栏目
 * 
 * @author Bugs_Rabbit 時間： 2015-8-13 上午12:46:01
 */
public class Fragment_Mall_Item extends Fragment {

	private ImageView hint_img;
	private GridView listView;
	private Mall_ItemAdapter adapter;
	private Type type;
	private ProgressBar progressBar;
	private String typename;
	private List<Type> infos = new ArrayList<Type>();

	private String price;
	public static String goodsPrice, goodsName, goodsId, goodsDesc, img_path,
			thumb_path, goodsscore,img_src;

	boolean istrue = true;
	Message msg;
	private Dialog dia;
	private AsyncImageLoader2 asyncImageLoader2 = new AsyncImageLoader2();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		hint_img = (ImageView) view.findViewById(R.id.hint_img);
		listView = (GridView) view.findViewById(R.id.listView);
		listView.setAdapter(adapter);
//		显示上面文字的 客户说不要
//		((TextView) view.findViewById(R.id.toptype)).setText(typename);
		progressBar.setVisibility(View.GONE);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long ids) {
				goodsId = infos.get(position).getId();
				goodsDesc = infos.get(position).getGoods_desc();
				goodsPrice = infos.get(position).getPrice();
				goodsscore = infos.get(position).getScore();
				img_path = infos.get(position).getImg_path();
				goodsName = infos.get(position).getName();
				thumb_path = infos.get(position).getThumb_path();
				img_src=infos.get(position).getImg_src();
				
				Intent intent = new Intent(getActivity(),
						Shop_GoodsDetailAty.class);
				startActivity(intent);

			}
		});
		//
		// GetTypeList();

		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		typename = getArguments().getString("typename");
		ArrayList<Type> tp = (ArrayList<Type>) Main_2Aty.lists;
		for (int i = 0; i < tp.size(); i++) {
			if (tp.get(i).getType_name().contains(typename)) {
				infos.add(tp.get(i));
			}
		}

		adapter = new Mall_ItemAdapter(getActivity(), (ArrayList<Type>) infos);

	}

	public void loadImage(final String url, final int id, final View view) {
		// 如果缓存过就会从缓存中取出图像，ImageCallback接口中方法也不会被执行
		Drawable cacheImage = asyncImageLoader2.loadDrawable(url,
				new AsyncImageLoader2.ImageCallback() {
					// 请参见实现：如果第一次加载url时下面方法会执行
					public void imageLoaded(Drawable imageDrawable) {
						((ImageView) view.findViewById(id))
								.setImageDrawable(imageDrawable);
					}
				});
		if (cacheImage != null) {
			((ImageView) view.findViewById(id)).setImageDrawable(cacheImage);
		}
	}

	// private void GetTypeList() {
	// list=new ArrayList<Type>();
	// for(int i=1;i<35;i++){
	// type=new Type(i, typename+i, "");
	// list.add(type);
	// }
	// progressBar.setVisibility(View.GONE);
	// }

	/*
	 * private class LoadTask extends AsyncTask<Void, Void, String>{
	 * 
	 * @Override protected String doInBackground(Void... params) { String
	 * name[]=new String[]{"shopid","type"}; String value[]=new
	 * String[]{"0","store"}; return
	 * NetworkHandle.requestBypost("app=u_favorite&act=index",name,value); }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * progressBar.setVisibility(View.GONE); list=new ArrayList<Shop>(); try {
	 * if(Constant.isDebug)System.out.println("result:"+result); JSONObject
	 * ob=new JSONObject(result); if(ob.getString("state").equals("1")){
	 * arrayToList(ob.getJSONArray("list")); adapter=new
	 * Love_shop_adapter(getActivity(), list,listView);
	 * listView.setAdapter(adapter); listView.onRefreshComplete();
	 * if(list.size()<20) listView.onPullUpRefreshFail();
	 * if(list.size()==0)hint_img.setVisibility(View.VISIBLE); else
	 * hint_img.setVisibility(View.GONE); }else{ //if(tradestate.equals("0"))
	 * //ResultUtils.handle((Activity_order)getActivity(), ob); } } catch
	 * (Exception e) { // if(tradestate.equals("0"))
	 * //ResultUtils.handle((Activity_order)getActivity(), "");
	 * e.printStackTrace(); } } }
	 * 
	 * private void arrayToList(JSONArray array) throws JSONException{
	 * JSONObject ob; for (int i = 0; i < array.length(); i++) {
	 * ob=array.getJSONObject(i); shop=new
	 * Shop(ob.getString("shopid"),ob.getString("shopname"),
	 * ob.getString("shoplogo"), ob.getString("weixin"),
	 * ob.getString("shopurl")); list.add(shop); } }
	 */

	/*
	 * private class LoadTaskMore extends AsyncTask<Void, Void, String>{
	 * 
	 * @Override protected String doInBackground(Void... params) { String
	 * name[]=new String[]{"shopid","type"}; String value[]=new
	 * String[]{list.get(list.size()-1).getShopid(),"store"}; return
	 * NetworkHandle.requestBypost("app=u_favorite&act=index",name,value); }
	 * 
	 * @Override protected void onPostExecute(String result) {
	 * if(Constant.isDebug)System.out.println("result:"+result); try {
	 * JSONObject ob=new JSONObject(result);
	 * if(ob.getString("state").equals("1")){ JSONArray
	 * array=ob.getJSONArray("list"); arrayToList(array); if(array.length()>0)
	 * adapter.notifyDataSetChanged(); if(array.length()<20)
	 * listView.onPullUpRefreshFail(); else listView.onPullUpRefreshComplete();
	 * } } catch (JSONException e) { e.printStackTrace(); } }
	 * 
	 * }
	 */

}
