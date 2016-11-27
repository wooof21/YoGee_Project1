/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import tools.Exit;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class WebViewActivity extends BaseActivity{

	private WebView wv;
	private String _url;
	private TextView title;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview);
		Exit.getInstance().addActivity(this);
		title = (TextView) findViewById(R.id.title_title);

		findViewById(R.id.back).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				finish();
			}
		});

		_url = getIntent().getExtras().getString("url");
		wv = (WebView) findViewById(R.id.webView1);
		// 打开页面时， 自适应屏幕：
		wv.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放
													// //将图片调整到适合webview的大小
		wv.getSettings().setLoadWithOverviewMode(true);
		// 便页面支持缩放：
		wv.getSettings().setJavaScriptEnabled(true);// 支持js
		// wv.getSettings().setBuiltInZoomControls(true);
		// wv.getSettings().setSupportZoom(true);// 支持缩放
		// 如果webView中需要用户手动输入用户名、密码或其他，则webview必须设置支持获取手势焦点。
		wv.requestFocusFromTouch();
		// 优先使用缓存
		wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		wv.setWebChromeClient(new wvChromeClient());
		wv.loadUrl(_url);
	}
	
	class wvChromeClient extends WebChromeClient{

		@Override
		public void onReceivedTitle(WebView view, String title){
			// TODO Auto-generated method stub
			System.out.println("title ---> " + title);
			WebViewActivity.this.title.setText(title);
			super.onReceivedTitle(view, title);
		}
		
	}
	
	class wv extends WebViewClient{
		
	}

	// 按返回键时， 不退出程序而是返回上一浏览页面：
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()){
			wv.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
	}
}
