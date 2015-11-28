/**
 * 互相学习 共同进步
 */
package com.daguo.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebView;

import com.daguo.R;

/**
 * @author : BugsRabbit
 * @email 395360255@qq.com
 * @version 创建时间：2015-11-27 下午4:46:31
 * @function ：
 */
public class WebView_CommenAty extends Activity {
    
    WebView webView;
    /*
     * (non-Javadoc)
     * 
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏加载
	setContentView(R.layout.aty_webview_commen);
	
	String URL=null;
	Intent intent  =getIntent();
	URL=intent.getStringExtra("URL");
	
	 webView = (WebView) this.findViewById(R.id.webView); 

	        /**

	         * 调用loadUrl()方法进行加载内容

	         */ 

	 	Log.d("通用webView", URL);
	        webView.loadUrl(URL); 
	        

	        /**

	         * 设置WebView的属性，此时可以去执行JavaScript脚本

	         */ 

	        webView.getSettings().setJavaScriptEnabled(true); 
    }
}
