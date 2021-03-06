package login;

import java.io.UnsupportedEncodingException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.MainActivity;
import com.youge.jobfinder.R;

public class Login extends BaseActivity implements OnClickListener {

	private ImageView back, login_phone_delete, login_psw_delete;
	private EditText phone, psw;
	private TextView login, register, forget;
	private LinearLayout wx, qq;
	private Tencent mTencent;
	private IWXAPI wxApi;
	private IUiListener loginListener; // 授权登录监听器
	private IUiListener userInfoListener; // 获取用户信息监听器
	private UserInfo userInfo; // qq用户信息
	private String openid;
	private String accessToken;
	private String expires;
	private String nickname;
	private String sex;
	private String headimgurl;
	private final int LOGIN_SUCCESS = 1;
	private int count;
	private Timer timer;
	private TimerTask timerTask;
	private String from;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		initView();
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

	private void initView() {
		mTencent = Tencent.createInstance("1104943672", this);
		from = getIntent().getStringExtra("from");
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		phone = (EditText) findViewById(R.id.login_phone);
		psw = (EditText) findViewById(R.id.login_psw);
		login = (TextView) findViewById(R.id.login_login);
		register = (TextView) findViewById(R.id.login_register);
		forget = (TextView) findViewById(R.id.login_forget);
		wx = (LinearLayout) findViewById(R.id.login_wx);
		qq = (LinearLayout) findViewById(R.id.login_qq);
		login_phone_delete = (ImageView) findViewById(R.id.login_phone_delete);
		login_psw_delete = (ImageView) findViewById(R.id.login_psw_delete);

		back.setOnClickListener(this);
		login.setOnClickListener(this);
		register.setOnClickListener(this);
		forget.setOnClickListener(this);
		wx.setOnClickListener(this);
		qq.setOnClickListener(this);
		login_phone_delete.setOnClickListener(this);
		login_psw_delete.setOnClickListener(this);
		phone.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (phone.getText().length() == 0) {
					login_phone_delete.setVisibility(View.INVISIBLE);
				} else {
					login_phone_delete.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		psw.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (psw.getText().length() == 0) {
					login_psw_delete.setVisibility(View.INVISIBLE);
				} else {
					login_psw_delete.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		loginListener();
		userInfoListener();

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			if ("".equals(from) || from == null) {
				startActivity(new Intent(this, MainActivity.class));
			}
			finish();

			break;
		case R.id.login_login:
			if (validate()) {
				MobclickAgent.onEvent(this, "login_phone");
				loginHttpClient(phone.getText().toString(), psw.getText()
						.toString());
			}
			break;
		case R.id.login_register:
			if (timer != null) {
				timer.cancel();
			}
			Intent intent = new Intent(this, Register.class);
			intent.putExtra("count", count);
			startActivityForResult(intent, 10000);
			break;
		case R.id.login_forget:
			startActivity(new Intent(this, ForgetPsw.class));
			break;
		case R.id.login_wx:
			wxLogin();
			break;
		case R.id.login_qq:
			txLogin();
			break;
		case R.id.login_phone_delete:
			phone.setText("");
			break;
		case R.id.login_psw_delete:
			psw.setText("");
			break;
		default:
			break;
		}
	}

	private void txLogin() {
		// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
		// 其中APP_ID是分配给第三方应用的appid，类型为String。

		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", loginListener);
		}
		MobclickAgent.onEvent(this, "3rd_party_auth_qq");

	}

	private void wxLogin() {
		wxApi = WXAPIFactory.createWXAPI(this, "wx0a2552bac6e5416b", true);
		wxApi.registerApp("wx0a2552bac6e5416b");
		// send oauth request
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "WeChat_Login";
		wxApi.sendReq(req);
		MobclickAgent.onEvent(this, "3rd_party_auth_wx");
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOGIN_SUCCESS:
				String cid = new Tools().getCID(Login.this);
				String uid = (String) msg.obj;
				MobclickAgent.onProfileSignIn(uid);
				if (!"".equals(cid)) {
					bindAlias(new Tools().getUserId(Login.this), cid, new Tools().getEntityName(Login.this),
							Login.this);
				}
				if ("".equals(from) || from == null) {
					Intent intent = new Intent(Login.this, MainActivity.class);
					startActivity(intent);
				} else {
					Intent intent = getIntent();
					intent.putExtra("isFromTL", false);
					setResult(RESULT_OK, intent);
				}
				Login.this.finish();
				break;
			}
			super.handleMessage(msg);
		}
	};

	/**
	 * 绑定账户
	 */
	private void bindAlias(String uid, String cid, String entityName, Context context) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("entityName", entityName);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.BIND_ALIAS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("绑定账户接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										Log.v("别人资料", data.toString());
										String results = data
												.getString("result");
										if ("0".equals(results)) {
											Log.v("别人资料", "提交失败");
										} else {
											Log.v("别人资料", "提交成功");
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 登录
	 * 
	 * @param
	 */
	private void loginHttpClient(String phone, String psw) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try {
			job.put("phone", phone);
			job.put("password", psw);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.LOGIN_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) {
								String str = new String(arg2);//
								System.out.println("登录返回 ---> " + str);

								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									String msg = result.getString("msg");
									if (state.equals("success")) { // state =
																	// success
										JSONObject data = result
												.getJSONObject("data");
										JSONObject user = data
												.getJSONObject("user");
										String credit = user.getString("point");
										if (credit == null) {
											credit = "";
										}
										saveInfo(user.getString("userid"), user
												.getString("username"),
												Login.this.phone.getText()
														.toString(), user
														.getString("passwd"),
												user.getString("img"));
										pd.dismiss();
										Toast.makeText(Login.this, "登录成功!",
												Toast.LENGTH_SHORT).show();
										Message msgs = mHandler.obtainMessage();
										msgs.what = LOGIN_SUCCESS;
										msgs.obj = user.getString("userid");
										msgs.sendToTarget();
									} else { // state = error
										pd.dismiss();
										//
										Toast.makeText(Login.this, msg,
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(Login.this, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
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

	private void saveInfo(String uid, String uname, String phone, String psw,
			String img) {
		SharedPreferences sharedPre = this.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString("phone", phone);
		editor.putString("password", psw);
		editor.putString("id", uid);
		editor.putString("username", uname);
		editor.putString("userimg", img);
		editor.commit();
	}

	private boolean validate() {
		if (phone.getText().length() == 0) {
			Toast.makeText(this, "请填写电话!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (phone.getText().length() != 11) {
			Toast.makeText(this, "请填写11位手机号!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (psw.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写密码!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	private void loginListener() {
		loginListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub
			}

			/**
			 * 返回json数据样例
			 * 
			 * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
			 * "pf":"desktop_m_qq-10000144-android-2002-",
			 * "query_authority_cost":448, "authority_cost":-136792089,
			 * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
			 * "expires_in":7776000, "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
			 * "msg":"", "access_token":"A2455F491478233529D0106D2CE6EB45",
			 * "login_cost":499}
			 */
			@Override
			public void onComplete(Object value) {
				// TODO Auto-generated method stub

				System.out.println("有数据返回..");
				if (value == null) {
					return;
				}
				try {
					JSONObject jo = (JSONObject) value;

					int ret = jo.getInt("ret");

					System.out.println("json=" + String.valueOf(jo));

					if (ret == 0) {
						Toast.makeText(Login.this, "登录成功", Toast.LENGTH_LONG)
								.show();

						openid = jo.getString("openid");
						accessToken = jo.getString("access_token");
						expires = jo.getString("expires_in");
						mTencent.setOpenId(openid);
						mTencent.setAccessToken(accessToken, expires);

						userInfo = new UserInfo(Login.this,
								mTencent.getQQToken());
						userInfo.getUserInfo(userInfoListener);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		};

	}

	private void userInfoListener() {
		userInfoListener = new IUiListener() {

			@Override
			public void onError(UiError arg0) {
				// TODO Auto-generated method stub

			}

			/**
			 * 返回用户信息样例
			 * 
			 * {"is_yellow_year_vip":"0","ret":0, "figureurl_qq_1":
			 * "http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB
			 * 3 E 5 9 D E 2 D \ / 4 0 " , "figureurl_qq_2":
			 * "http:\/\/q.qlogo.cn\/qqapp\/1104732758\/015A22DED93BD15E0E6B0DDB
			 * 3 E 5 9 D E 2 D \ / 1 0 0 " ,
			 * "nickname":"攀爬←蜗牛","yellow_vip_level":"0","is_lost":0,"msg":"",
			 * "city":"黄冈","
			 * figureurl_1":"http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758
			 * \/015A22DED93BD15E0E6B0DDB3E59DE2D\/50", "vip":"0","level":"0",
			 * "figureurl_2":
			 * "http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B
			 * 0 D D B 3 E 5 9 D E 2 D \ / 1 0 0
			 * " , "province":"湖北", "is_yellow_vip":"0","gender":"男",
			 * "figureurl":
			 * "http:\/\/qzapp.qlogo.cn\/qzapp\/1104732758\/015A22DED93BD15E0E6B
			 * 0 D D B 3 E 5 9 D E 2 D \ / 3 0 " }
			 */
			@Override
			public void onComplete(Object arg0) {
				// TODO Auto-generated method stub
				if (arg0 == null) {
					return;
				}
				try {
					JSONObject jo = (JSONObject) arg0;
					int ret = jo.getInt("ret");
					System.out.println("json=" + String.valueOf(jo));
					nickname = jo.getString("nickname");
					sex = jo.getString("gender");
					headimgurl = jo.getString("figureurl_qq_1");
					TXUserInfo();
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void onCancel() {
				// TODO Auto-generated method stub

			}
		};
	}

	/**
	 * 微信登录成功 获取个人信息
	 */
	private void TXUserInfo() {
		// final CustomProgressDialog pd = CustomProgressDialog
		// .createDialog(WXEntryActivity.this);
		JSONObject job = new JSONObject();

		try {
			job.put("openid", openid);
			job.put("name", nickname);
			job.put("img", headimgurl);
			job.put("type", "2");
			if ("女".equals(sex)) {
				job.put("sex", "2");
			} else {
				job.put("sex", "1");
			}

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(Login.this, Config.OTHER_LOGIN, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("腾讯登录接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONObject userResult = data
												.getJSONObject("user");
										MobclickAgent.onProfileSignIn("QQ",
												userResult.getString("userid"));
										saveInfo(
												userResult.getString("userid"),
												userResult
														.getString("username"),
												openid, userResult
														.getString("img"));
										Toast.makeText(Login.this, "登录成功!",
												Toast.LENGTH_SHORT).show();
										String cid = new Tools()
												.getCID(Login.this);
										if (!"".equals(cid)) {
											bindAlias(new Tools()
													.getUserId(Login.this),
													cid, new Tools().getEntityName(Login.this), Login.this);
										}
										Intent intent = new Intent(Login.this,
												MainActivity.class);
										intent.putExtra("isFromTL", true);
										startActivity(intent);
										Login.this.finish();
									} else {
										Toast.makeText(Login.this, message,
												Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(Login.this, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
			// pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			// pd.dismiss();
		}
	}

	private void saveInfo(String uid, String uname, String openid, String img) {
		SharedPreferences sharedPre = this.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString("id", uid);
		editor.putString("username", uname);
		editor.putString("openid", openid);
		editor.putString("loginType", "otherRegister");
		editor.putString("userimg", img);
		editor.commit();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.e("requestCode+resultCode", requestCode + "---" + resultCode + "");
		mTencent.onActivityResultData(requestCode, resultCode, data,
				loginListener);
		if (requestCode == Constants.REQUEST_LOGIN) {
			if (resultCode == Constants.ACTIVITY_OK) {
				mTencent.handleResultData(data, loginListener);
			}
		} else if (requestCode == 10000) {
			try{
				count = Integer.parseInt(data.getExtras().getString("count", "0"));
				if (count != 0) {
					startCount();
				}	
			}catch(NullPointerException e){
				// TODO: handle exception
			}
		}
	}

	/**
	 * 倒计时
	 * 
	 * @param
	 */
	private void startCount() {

		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (count < 0) {
					timer.cancel();
				} else {
					count--;
				}
			}
		};
		timer.schedule(timerTask, 0, 1000);

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ("".equals(from) || from == null) {
				startActivity(new Intent(this, MainActivity.class));
			}
			finish();
		}
		return false;

	}

}
