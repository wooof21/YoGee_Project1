package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.ExamineUserResumeActivity;


public class GetOtherInfoActivity extends BaseActivity implements
		OnClickListener {

	private TextView name, sex, birth, occupation, address, sendOrder,
			getOrder, goodFeed, vip_main_credit, phoneOne, phoneTwo,
			phoneThree;

	private LinearLayout getResume, certification_renzhenone,
			certification_renzhentwo;

	private ImageView vip_main_head, back;

	public Handler mHandler;

	private String otherUserId, orderState;

	protected static final int FIND_RESUME_SUCCESS = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_other_info);
		findView();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView(){
		name = (TextView) findViewById(R.id.name);
		sex = (TextView) findViewById(R.id.sex);
		birth = (TextView) findViewById(R.id.birth);
		occupation = (TextView) findViewById(R.id.occupation);
		address = (TextView) findViewById(R.id.address);
		sendOrder = (TextView) findViewById(R.id.sendOrder);
		getOrder = (TextView) findViewById(R.id.getOrder);
		goodFeed = (TextView) findViewById(R.id.goodFeed);
		getResume = (LinearLayout) findViewById(R.id.getResume);
		vip_main_head = (ImageView) findViewById(R.id.vip_main_head);
		back = (ImageView) findViewById(R.id.back);
		vip_main_credit = (TextView) findViewById(R.id.vip_main_credit);
		phoneOne = (TextView) findViewById(R.id.phoneOne);
		phoneTwo = (TextView) findViewById(R.id.phoneTwo);
		phoneThree = (TextView) findViewById(R.id.phoneThree);
		certification_renzhenone = (LinearLayout) findViewById(R.id.certification_renzhenone);
		certification_renzhentwo = (LinearLayout) findViewById(R.id.certification_renzhentwo);
		getResume.setOnClickListener(this);
		back.setOnClickListener(this);
		
		System.out.println("need ---> " + getIntent().getExtras().getString("need", "0"));
		if(getIntent().getExtras().getString("need", "0").equals("1")){
			getResume.setVisibility(View.VISIBLE);
		}else{
			getResume.setVisibility(View.GONE);
		}

		otherUserId = getIntent().getStringExtra("otherUserId");
		othersUserInfo(otherUserId);
		
		orderState = getIntent().getExtras().getString("orderState", "0");
	}

	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.getResume:
				Intent intent = new Intent(GetOtherInfoActivity.this,
						ExamineUserResumeActivity.class);
				intent.putExtra("isFromVipCenter", false);
				intent.putExtra("isFromOtherInfo", true);
				intent.putExtra("otherUserID", otherUserId);
				intent.putExtra("orderState", orderState);
				startActivity(intent);
			break;
			case R.id.back:
				finish();
			break;
			default:
			break;
		}
	}

	/**
	 * 获取个人简历
	 */
	private void othersUserInfo(String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.OTHERS_USER_INFO, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("查看别人资料接口返回 ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										Log.v("别人资料", data.toString());
										JSONObject userInfo = data
												.getJSONObject("user");
										name.setText(userInfo
												.getString("username"));
										if ("1".equals(userInfo
												.getString("sex"))){
											sex.setText("男");
										}else{
											sex.setText("女");
										}

										birth.setText(userInfo
												.getString("birthday"));
										occupation.setText(userInfo
												.getString("occupation"));
										address.setText(userInfo
												.getString("city"));
										ImageLoader.getInstance().displayImage(
												userInfo.getString("img"),
												vip_main_head);
										sendOrder.setText(userInfo
												.getString("releaseCount")
												+ "次");
										getOrder.setText(userInfo
												.getString("grabCount") + "次");
										goodFeed.setText(userInfo
												.getString("praise") + "%");
										if ("0".equals(userInfo
												.getString("credit"))){
											vip_main_credit.setText("未绑定");
										}else{
											vip_main_credit.setText(userInfo
													.getString("credit") + "分");
										}
										if (userInfo.getString("phone")
												.length() != 0){
											phoneOne.setText(userInfo
													.getString("phone")
													.substring(0, 3));
											phoneTwo.setText(userInfo
													.getString("phone")
													.substring(3, 7));
											phoneThree.setText(userInfo
													.getString("phone")
													.substring(7, 11));
										}else{
											phoneOne.setText("");
											phoneTwo.setText("");
											phoneThree.setText("");
										}
										if ("0".equals(userInfo
												.getString("identity"))) {
											certification_renzhenone
													.setVisibility(View.GONE);
											certification_renzhentwo
													.setVisibility(View.VISIBLE);
										} else if ("1".equals(userInfo
												.getString("identity"))) {
											certification_renzhentwo
													.setVisibility(View.GONE);
											certification_renzhenone
													.setVisibility(View.VISIBLE);
										}

									}else{
										Toast.makeText(
												GetOtherInfoActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(GetOtherInfoActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			pd.dismiss();
		}
	}
}
