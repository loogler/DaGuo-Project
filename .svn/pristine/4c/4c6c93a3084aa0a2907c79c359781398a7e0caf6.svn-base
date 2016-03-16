/**
 * 互相学习 共同进步
 */
package com.daguo.ui.commercial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;
import com.daguo.util.adapter.Main_2Adapter;
import com.daguo.util.beans.Shop_GoodsItem;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.PublicTools;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-17 下午2:46:14
 * @function ： 搜索的默认综合排序 （时间为默认）
 */
@SuppressLint("InflateParams")
public class Shop_Search_ComprehensiveFragment extends Fragment implements
	OnRefreshListener {

    private final int MSG_GOOD_DATA_SUC = 1001;
    private final int MSG_GOOD_DATA_FAIL = 1002;

    /**
     * initViews
     */
    // initViews
    private PullToRefreshLayout refresh_view;
    private GridView content_view;

    /**
     * data
     */
    private List<Shop_GoodsItem> lists = new ArrayList<Shop_GoodsItem>();
    private Shop_GoodsItem list;
    private Main_2Adapter adapter;

    /**
     * tools
     */
    private int pageIndex = 1;
    private String nameSearch; // TODO

    private Message msg;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
	public void handleMessage(Message msg) {
	    switch (msg.what) {
	    case MSG_GOOD_DATA_SUC:

		if (msg.obj != null) {

		    @SuppressWarnings("unchecked")
		    List<Shop_GoodsItem> abcGoodsItems = (List<Shop_GoodsItem>) msg.obj;
		    lists.addAll(abcGoodsItems);
		    adapter.notifyDataSetChanged();

		}

		break;
	    case MSG_GOOD_DATA_FAIL:
		Toast.makeText(getActivity(), "没有找到该商品", Toast.LENGTH_SHORT)
			.show();

		break;

	    default:
		break;
	    }

	};
    };

    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
//	nameSearch;
	if (!PublicTools.doWithNullData(nameSearch).isEmpty()) {
	    loadGoodsData();

	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
     * android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	
	return null;
    }

    /**************************************** get data thread *********************************************************/

    /**
     * 
     */
    private void loadGoodsData() {
	new Thread(new Runnable() {
	    public void run() {
		try {
		    String url = HttpUtil.QUERY_GOODSLIST + "&rows=20&page="
			    + pageIndex;
		    Map<String, String> map = new HashMap<String, String>();
		    map.put("name", nameSearch);
		    String res = HttpUtil.postRequest(url, map);
		    JSONObject jsonObject = new JSONObject(res);

		    if (jsonObject.getInt("total") > 0) {
			JSONArray array = jsonObject.getJSONArray("rows");
			List<Shop_GoodsItem> abcGoodsItems = new ArrayList<Shop_GoodsItem>();
			for (int i = 0; i < array.length(); i++) {
			    list = new Shop_GoodsItem();
			    String thumb_path = array.optJSONObject(i)
				    .getString("thumb_path");
			    String name = array.optJSONObject(i).getString(
				    "name");
			    String id = array.optJSONObject(i).getString("id");
			    String price = array.optJSONObject(i).getString(
				    "price");

			    list.setThumb_path(thumb_path);
			    list.setName(name);
			    list.setId(id);
			    list.setPrice(price);
			    abcGoodsItems.add(list);

			}
			msg = handler.obtainMessage(MSG_GOOD_DATA_SUC);
			msg.obj = abcGoodsItems;
			msg.sendToTarget();
		    } else {
			// 无此商品
			msg = handler.obtainMessage(MSG_GOOD_DATA_FAIL);
			msg.sendToTarget();
		    }
		} catch (JSONException e) {
		    Log.e("商品搜索", "json 异常");
		} catch (Exception e) {
		    Log.e("商品搜索", "获取数据异常");
		}
	    }
	}).start();

    }

    /*************************************** implement ******************************************************/
    /*
     * (non-Javadoc)
     * 
     * @see
     * com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener#onRefresh
     * (com.daguo.libs.pulltorefresh.PullToRefreshLayout)
     */
    @Override
    public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
	new Handler().postDelayed(new Runnable() {
	    public void run() {
		pageIndex = 1;
		loadGoodsData();
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}, 1500);

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
	new Handler().postDelayed(new Runnable() {
	    public void run() {
		pageIndex++;
		loadGoodsData();
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
	    }
	}, 1500);

    }
}
