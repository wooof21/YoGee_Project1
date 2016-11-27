package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;

import login.Login;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.smssdk.statistics.NewAppReceiver;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class FeedBackActivity extends BaseActivity implements OnClickListener{

	private EditText feed;
	private ImageView back;
	private TextView title_tv;
	private InputMethodManager inputManager;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		findView();
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

	/**
	 * 根据ID查找控件
	 */
	private void findView(){
		feed = (EditText) findViewById(R.id.feed);
		back = (ImageView) findViewById(R.id.back);
		title_tv = (TextView) findViewById(R.id.title_tv);
		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		title_tv.setOnClickListener(this);
		back.setOnClickListener(this);
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v){
		if (inputManager.isAcceptingText()){
			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		switch(v.getId()){
			case R.id.title_tv:
				if (TextUtils.isEmpty(feed.getText())){
					Toast.makeText(this, "请填写反馈意见", Toast.LENGTH_SHORT).show();
				}
				updateFeedBackHttpClient();
			break;
			case R.id.back:
				finish();
			break;
			default:
			break;
		}
	}

	/**
	 * 保存反馈意见
	 * 
	 */
	private void updateFeedBackHttpClient(){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("userid", new Tools().getUserId(this));
			job.put("content", feed.getText());
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.FEED_BACK, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){

							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200){
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										String resumeResult = data
												.getString("result");
										if ("1".equals(resumeResult)){
											pd.dismiss();
											Toast.makeText(
													FeedBackActivity.this,
													"提交成功", Toast.LENGTH_SHORT)
													.show();
											// startActivity(new Intent(
											// FeedBackActivity.this,
											// Login.class));
											finish();
										}else{
											pd.dismiss();
											Toast.makeText(
													FeedBackActivity.this, msg,
													Toast.LENGTH_SHORT).show();
										}

									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							pd.dismiss();
							// TODO Auto-generated method stub
							Toast.makeText(FeedBackActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}
}
