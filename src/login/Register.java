package login;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import android.R.integer;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.ChangeUserResumeActivity;

public class Register extends BaseActivity implements OnClickListener {

	private ImageView back, register_phone_delete;
	private EditText phones, vCode;
	private TextView getCode, commit, login;

	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private SmsObserver smsObserver;

	private int count = 0;
	private Timer timer;
	private TimerTask timerTask;
	/**
	 * 手机号码 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
	 * 联通：130,131,132,152,155,156,185,186 电信：133,1349,153,180,189
	 */
	private String mobile = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
	/**
	 * 10 * 中国移动：China Mobile 11 *
	 * 134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188 12
	 */
	private String CM = "^1(34[0-8]|(3[5-9]|5[017-9]|8[0-9]|7[0-9])\\d{7}$";
	/**
	 * 15 * 中国联通：China Unicom 16 * 130,131,132,152,155,156,185,186 17
	 */
	private String CU = "^1(3[0-2]|5[256]|8[56])\\d{8}$";
	/**
	 * 20 * 中国电信：China Telecom 21 * 133,1349,153,180,189 22
	 */
	private String CT = "^1((33|53|8[09])[0-9]|349)\\d{7}$";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		count = getIntent().getIntExtra("count", 0);
		if (count != 0) {
			startCount(count);
		}
		Log.e("count", count + "");
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
		System.out.println("sddsfsdafsadfdsafdsfsdafd     " + count);
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
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		register_phone_delete = (ImageView) findViewById(R.id.register_phone_delete);
		phones = (EditText) findViewById(R.id.register_phone);
		vCode = (EditText) findViewById(R.id.register_vcode);
		getCode = (TextView) findViewById(R.id.register_get_vcode);
		commit = (TextView) findViewById(R.id.register_commit);
		login = (TextView) findViewById(R.id.register_login);

		back.setOnClickListener(this);
		getCode.setOnClickListener(this);
		commit.setOnClickListener(this);
		login.setOnClickListener(this);
		register_phone_delete.setOnClickListener(this);
		phones.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					register_phone_delete.setVisibility(View.VISIBLE);
				} else {
					register_phone_delete.setVisibility(View.INVISIBLE);
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

		EventHandler eh = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {

				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}

		};
		SMSSDK.registerEventHandler(eh);
	}

	/**
	 * 获取验证码,验证验证码回调handler
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			int event = msg.arg1;
			int result = msg.arg2;
			Object data = msg.obj;
			Log.e("短信验证 event ", " event=" + event);
			if (result == SMSSDK.RESULT_COMPLETE) {
				// 短信注册成功后，返回MainActivity,然后提示新好友
				if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
					Toast.makeText(getApplicationContext(), "提交验证码成功",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(Register.this,
							RegisterEditInfo.class);
					intent.putExtra("phone", phones.getText().toString());
					System.out.println("phone send ---> "
							+ phones.getText().toString());
					startActivity(intent);
					finish();

				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();
					Message message = msgHandler.obtainMessage();
					message.what = 1;
					message.sendToTarget();

					smsObserver = new SmsObserver(Register.this, msgHandler);
					getContentResolver().registerContentObserver(SMS_INBOX,
							true, smsObserver);
				}
			} else {
				((Throwable) data).printStackTrace();
				// int resId = getStringRes(Register.this,
				// "smssdk_network_error");
				Toast.makeText(Register.this, "验证码错误", Toast.LENGTH_SHORT)
						.show();

			}
		}

	};

	/**
	 * 获取短信, 倒计时handler
	 */
	private Handler msgHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 开始倒计时
				startCount(60);
				break;
			case 2: // 计时中
				getCode.setText("重新发送" + "(" + count + "s)");
				getCode.invalidate();
				System.out.println("时间线程" + "测试文档:。。" + getCode.getText());
				getCode.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.corner_grey));
				getCode.setClickable(false);
				break;
			case 3: // 计时结束
				getCode.setText("重新发送");
				timer.cancel();
				getCode.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.corner_green));
				getCode.setClickable(true);
				getCode.setOnClickListener(Register.this);
				break;
			case 5: // 自动填写验证码
				String code = (String) msg.obj;
				vCode.setText(code);
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 倒计时
	 * 
	 * @param
	 */
	private void startCount(int c) {

		count = c;
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (count > 0) {
					System.out.println("时间线程" + ":  正在 执行。。" + count);
					Message msg = msgHandler.obtainMessage();
					msg.what = 2;
					msgHandler.sendMessage(msg);
				} else {

					Message msg = msgHandler.obtainMessage();
					msg.what = 3;
					msgHandler.sendMessage(msg);
				}
				count--;

			}
		};
		timer.schedule(timerTask, 0, 1000);

	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			if (timer != null) {
				timer.cancel();
			}
			Intent intent = getIntent();
			intent.putExtra("count", count + "");
			setResult(10000, intent);
			finish();
			break;
		case R.id.register_get_vcode: // 获取验证码
			if (validate()) {
				userCheckHttpClient(phones.getText().toString());
			}
			break;
		case R.id.register_commit: // 确认注册, 先验证验证码是否有效,成功跳到下一页
			if (validate()) {
				if (vCode.getText().toString().length() == 0) {
					Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
				} else {
					SMSSDK.submitVerificationCode("86", phones.getText()
							.toString(), vCode.getText().toString());
				}
			}
			break;
		case R.id.register_login:
			startActivity(new Intent(this, Login.class));
			finish();
			break;
		case R.id.register_phone_delete:
			phones.setText("");
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		List<String> listNumList = new ArrayList<String>();
		listNumList.add(mobile);
		listNumList.add(CM);
		listNumList.add(CU);
		listNumList.add(CT);
		Boolean isNum = true;
		if (phones.getText().toString().length() != 0) {
			for (int i = 0, j = listNumList.size(); i < j; i++) {
				if (phones.getText().toString().matches(listNumList.get(i))) {
					isNum = false;
					break;
				}
			}
		}

		if (phones.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写手机号!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (phones.getText().toString().length() != 11) {
			Toast.makeText(this, "请填写11位手机号!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (isNum) {
			Toast.makeText(this, "电话格式不对！", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent intent = getIntent();
		Bundle b = new Bundle();
		b.putInt("count", count);
		intent.putExtra("b", b);
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		SMSSDK.unregisterAllEventHandler();
	}

	/**
	 * 监听短信
	 */
	class SmsObserver extends ContentObserver {

		public SmsObserver(Context context, Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			// 每当有新短信到来时，使用我们获取短消息的方法
			readSMS();
		}
	}

	/**
	 * 读取短信
	 * 
	 * @param
	 */
	private void readSMS() {
		ContentResolver cr = getContentResolver();
		String[] projection = new String[] { "body" };// "_id", "address",
		// "person",, "date",
		// "type
		String where = "address = '10690032980066' AND date >  "
				+ (System.currentTimeMillis() - 10 * 60 * 1000);
		Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
		if (null == cur)
			return;
		if (cur.moveToNext()) {
			// String number =
			// cur.getString(cur.getColumnIndex("address"));//手机号
			// String name =
			// cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
			String body = cur.getString(cur.getColumnIndex("body"));
			System.out.println(body);
			// 这里我是要获取自己短信服务号码中的验证码~~
			Pattern pattern = Pattern.compile("[a-zA-Z0-9]{4}");
			Matcher matcher = pattern.matcher(body);
			if (matcher.find()) { // 发送验证码
				String res = matcher.group().substring(0, 4);
				Message msg = msgHandler.obtainMessage();
				msg.what = 5;
				msg.obj = res;
				msg.sendToTarget();
			}
		}
	}

	/**
	 * 6.1. 登陆注册认证码接口
	 * 
	 * @param List
	 *            <File> listMD5
	 * @param List
	 *            <File> listFile
	 */
	private void userCheckHttpClient(final String phone) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try {
			job.put("phone", phone);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.USER_CHECK, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) {
								String str = new String(arg2);//
								System.out.println("注册返回 ---> " + str);

								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									String msg = result.getString("msg");
									if (state.equals("success")) { // state =
																	// success
										JSONObject data = result
												.getJSONObject("data");
										String results = data
												.getString("result");
										if ("0".equals(results)) {
											pd.dismiss();
											SMSSDK.getVerificationCode("86",
													phones.getText().toString());
										} else {
											pd.dismiss();
											//
											Toast.makeText(Register.this,
													"手机已经注册过",
													Toast.LENGTH_SHORT).show();
										}
									} else { // state = error
										pd.dismiss();
										//
										Toast.makeText(Register.this, msg,
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
							Toast.makeText(Register.this, "网络连接失败，请检测网络！",
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
}
