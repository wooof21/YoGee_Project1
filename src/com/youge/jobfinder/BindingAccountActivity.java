package com.youge.jobfinder;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.AlipayPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

public class BindingAccountActivity extends BaseActivity implements
		OnClickListener {

	private ImageView wx_imgs, alipay_imgs, back;
	private TextView wx_istrue, alipay_istrue, wx_binding, alipay_binding;
	private String wx_bindingString, alipay_bindingString;
	public static BindingAccountActivity instance;
	private View parent;
	private IWXAPI wxApi;
	private String openid;
	private Drawable binding_btn, unbinding_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_binding_account);
		findView();
		initView();
		bindingListHttpClient(new Tools().getUserId(getApplicationContext()));
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		wx_imgs = (ImageView) findViewById(R.id.wx_imgs);
		alipay_imgs = (ImageView) findViewById(R.id.alipay_imgs);
		back = (ImageView) findViewById(R.id.back);
		wx_istrue = (TextView) findViewById(R.id.wx_istrue);
		alipay_istrue = (TextView) findViewById(R.id.alipay_istrue);
		wx_binding = (TextView) findViewById(R.id.wx_binding);
		alipay_binding = (TextView) findViewById(R.id.alipay_binding);
		parent = findViewById(R.id.binding_parent);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		binding_btn = getResources().getDrawable(R.drawable.binding_green);
		unbinding_btn = getResources()
				.getDrawable(R.drawable.fill_order_orange);
		instance = this;
		wx_binding.setOnClickListener(this);
		alipay_binding.setOnClickListener(this);
		back.setOnClickListener(this);
		openid = getIntent().getStringExtra("openid");
		if (!TextUtils.isEmpty(openid)) {
			bindingHttpClient(new Tools().getUserId(this));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wx_binding:
			if ("绑定".equals(wx_binding.getText())) {
				SharedPreferences sharedPre = this.getSharedPreferences("user",
						Context.MODE_PRIVATE);
				Editor editor = sharedPre.edit();
				editor.putString("isFromBinding", "1");
				editor.commit();
				wxBinding();
			} else {
				removebindingHttpClient(new Tools().getUserId(this), "1");
			}

			break;
		case R.id.alipay_binding:
			if ("绑定".equals(alipay_binding.getText())) {
				new AlipayPopUpWindow(BindingAccountActivity.this, parent,
						new Tools().getUserId(BindingAccountActivity.this));
			} else {
				// 创建退出对话框
				AlertDialog isExit = new AlertDialog.Builder(this).create();
				// 设置对话框标题
				isExit.setTitle("温馨提示");
				// 设置对话框消息
				isExit.setMessage("您确定解绑此账户吗？");
				// 添加选择按钮并注册监听
				isExit.setButton("确定", listener);
				isExit.setButton2("取消", listener);
				// 显示对话框
				isExit.show();

			}

			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				removebindingHttpClient(
						new Tools().getUserId(BindingAccountActivity.this), "2");
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	private void wxBinding() {
		wxApi = WXAPIFactory.createWXAPI(this, "wx0a2552bac6e5416b", true);
		wxApi.registerApp("wx0a2552bac6e5416b");
		// send oauth request
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "WeChat_Login";
		wxApi.sendReq(req);
		MobclickAgent.onEvent(this, "3rd_party_auth_wx");
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 改变状态
				alipay_imgs.setImageDrawable(getResources().getDrawable(
						R.drawable.realname_success));
				alipay_istrue.setText("已绑定");
				alipay_istrue.setTextColor(getResources().getColor(
						R.color.sex_text_color));
				alipay_binding.setBackgroundDrawable(unbinding_btn);
				alipay_binding.setText("解绑");
				break;

			default:
				break;
			}
		}

	};

	private void bindingListHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.BINDING_LIST, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("绑定List接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONObject bindingList = data
												.getJSONObject("Binding");
										wx_bindingString = bindingList
												.getString("wx");
										alipay_bindingString = bindingList
												.getString("alipay");
										if ("0".equals(wx_bindingString)) {
											wx_imgs.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.realname_error));
											wx_istrue.setText("未绑定");
											wx_istrue
													.setTextColor(getResources()
															.getColor(
																	R.color.binding_text_color));
											wx_binding
													.setBackgroundDrawable(binding_btn);
											wx_binding.setText("绑定");
										} else if ("1".equals(wx_bindingString)) {
											wx_imgs.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.realname_success));
											wx_istrue.setText("已绑定");
											wx_istrue
													.setTextColor(getResources()
															.getColor(
																	R.color.sex_text_color));
											wx_binding
													.setBackgroundDrawable(unbinding_btn);
											wx_binding.setText("解绑");
										}
										if ("0".equals(alipay_bindingString)) {
											alipay_imgs
													.setImageDrawable(getResources()
															.getDrawable(
																	R.drawable.realname_error));
											alipay_istrue.setText("未绑定");
											alipay_istrue
													.setTextColor(getResources()
															.getColor(
																	R.color.binding_text_color));
											alipay_binding
													.setBackgroundDrawable(binding_btn);
											alipay_binding.setText("绑定");
										} else if ("1"
												.equals(alipay_bindingString)) {
											alipay_imgs
													.setImageDrawable(getResources()
															.getDrawable(
																	R.drawable.realname_success));
											alipay_istrue.setText("已绑定");
											alipay_istrue
													.setTextColor(getResources()
															.getColor(
																	R.color.sex_text_color));
											alipay_binding
													.setBackgroundDrawable(unbinding_btn);
											alipay_binding.setText("解绑");
										}
									} else {
										Toast.makeText(
												BindingAccountActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(BindingAccountActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}

	protected void onResume() {
		super.onResume();
	}

	private void bindingHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(BindingAccountActivity.this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("type", "1");
			job.put("openid", openid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(BindingAccountActivity.this, Config.BINDING, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String str = new String(arg2);
							System.out.println("绑定微信接口返回 ---> " + str);
							String message = "";
							try {
								JSONObject job = new JSONObject(str);
								String state = job.getString("state");
								message = job.getString("msg");
								if (state.equals("success")) {
									JSONObject data = job.getJSONObject("data");
									String result = data.getString("result");
									if (result.equals("1")) {
										Toast.makeText(
												BindingAccountActivity.this,
												"绑定成功", Toast.LENGTH_SHORT)
												.show();
										wx_imgs.setImageDrawable(getResources()
												.getDrawable(
														R.drawable.realname_success));
										wx_istrue.setText("已绑定");
										wx_istrue
												.setTextColor(getResources()
														.getColor(
																R.color.sex_text_color));
										wx_binding
												.setBackgroundDrawable(unbinding_btn);
										wx_binding.setText("解绑");
									} else {
										Toast.makeText(
												BindingAccountActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(BindingAccountActivity.this,
											message, Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(BindingAccountActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}

	private void removebindingHttpClient(String uid, final String type) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(BindingAccountActivity.this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("type", type);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(BindingAccountActivity.this, Config.REMOVE_BINDING,
					se, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String str = new String(arg2);
							System.out.println("解绑接口返回 ---> " + str);
							String message = "";
							try {
								JSONObject job = new JSONObject(str);
								String state = job.getString("state");
								message = job.getString("msg");
								if (state.equals("success")) {
									JSONObject data = job.getJSONObject("data");
									String result = data.getString("result");
									if (result.equals("1")) {
										Toast.makeText(
												BindingAccountActivity.this,
												"解绑成功", Toast.LENGTH_SHORT)
												.show();
										if ("1".equals(type)) {
											wx_imgs.setImageDrawable(getResources()
													.getDrawable(
															R.drawable.realname_error));
											wx_istrue.setText("未绑定");
											wx_istrue
													.setTextColor(getResources()
															.getColor(
																	R.color.binding_text_color));
											wx_binding
													.setBackgroundDrawable(binding_btn);
											wx_binding.setText("绑定");
										} else {
											alipay_imgs
													.setImageDrawable(getResources()
															.getDrawable(
																	R.drawable.realname_error));
											alipay_istrue.setText("未绑定");
											alipay_istrue
													.setTextColor(getResources()
															.getColor(
																	R.color.binding_text_color));
											alipay_binding
													.setBackgroundDrawable(binding_btn);
											alipay_binding.setText("绑定");
										}

									} else {
										Toast.makeText(
												BindingAccountActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(BindingAccountActivity.this,
											message, Toast.LENGTH_SHORT).show();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(BindingAccountActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}
}
