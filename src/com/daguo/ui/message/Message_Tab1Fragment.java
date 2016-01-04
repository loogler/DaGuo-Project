/**
 * 互相学习 共同进步
 */
package com.daguo.ui.message;

import com.daguo.R;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout;
import com.daguo.libs.pulltorefresh.PullToRefreshLayout.OnRefreshListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-12-30 上午8:56:47
 * @function ：
 */
public class Message_Tab1Fragment extends Fragment {
    /*
     * (non-Javadoc)
     * 
     * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
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
	View view = inflater.inflate(R.layout.fragment_message_tab1, null);
	PullToRefreshLayout refresh_view = (PullToRefreshLayout) view
		.findViewById(R.id.refresh_view);
	ListView content_view = (ListView) view.findViewById(R.id.content_view);

	refresh_view.setOnRefreshListener(new OnRefreshListener() {

	    @Override
	    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

	    }

	    @Override
	    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

	    }
	});
	// content_view.setAdapter(adapter);
	// TODO 待完善方法
	return view;
    }

}
