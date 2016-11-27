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
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class ResetPswActivity extends BaseActivity implements OnClickListener{

	private EditText password;
	private LinearLayout next;
	private ImageView back;
	private Drawable drawable_unclickable; // 按钮可点击时显示的图片
	private Drawable drawable_clickable;// 按钮不可点击时显示的图片
	private String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_psw);
		findView();
		initView();
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
	 * 查找控件ID
	 */
	private void findView(){
		password = (EditText) findViewById(R.id.password);
		next = (LinearLayout) findViewById(R.id.next);
		back = (ImageView) findViewById(R.id.back);
	}

	/**
	 * 初始化数据 设置监听事件
	 * 
	 */
	private void initView(){
		next.setOnClickListener(this);
		next.setClickable(false);
		back.setOnClickListener(this);
		phone = getIntent().getExtras().getString("phone");
		drawable_clickable = getResources().getDrawable(
				R.drawable.login_btn_click);
		drawable_unclickable = getResources().getDrawable(
				R.drawable.login_btn_unclick);
		password.addTextChangedListener(new TextWatcher(){

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count){
				if (s.length() > 0){
					next.setClickable(true);
					next.setBackgroundDrawable(drawable_clickable);
				}else{
					next.setBackgroundDrawable(drawable_unclickable);
					next.setClickable(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after){
			}

			@Override
			public void afterTextChanged(Editable s){
			}
		});
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.back:
				finish();
			break;
			case R.id.next:
				if (TextUtils.isEmpty(password.getText())){
					Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
				}else{
					updatePassword();
				}
			break;
			default:
			break;
		}

	}

	/**
	 * 确认密码
	 * 
	 */
	private void updatePassword(){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("phone", phone);
			job.put("newPassword", password.getText());
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.UPDATE_PASSWORD, se,
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
													ResetPswActivity.this,
													"密码修改成功",
													Toast.LENGTH_SHORT).show();
											startActivity(new Intent(
													ResetPswActivity.this,
													Login.class));
											finish();
										}else{
											pd.dismiss();
											Toast.makeText(
													ResetPswActivity.this, msg,
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
							Toast.makeText(ResetPswActivity.this,
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
