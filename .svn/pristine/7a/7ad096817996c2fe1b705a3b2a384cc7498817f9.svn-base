package login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import login.Register.SmsObserver;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.ResetPswActivity;

public class ForgetPsw extends BaseActivity implements OnClickListener {

	private EditText telnum, checknum;
	private ImageView back, telnum_delete;
	private TextView checknumtext;
	private LinearLayout next, getchecknum;
	private Drawable drawable_unclickable; // 按钮可点击时显示的图片
	private Drawable drawable_clickable;// 按钮不可点击时显示的图片
	private Drawable drawable_checkunclickable; // 注册按钮可点击时显示的图片
	private Drawable drawable_checkclickable;// 注册按钮不可点击时显示的图片
	private Drawable drawable_checkunclick;// 注册按钮不可点击时显示的图片
	private TimeCount time;
	private Uri SMS_INBOX = Uri.parse("content://sms/");
	private SmsObserver smsObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_password);
		findView();
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

	/**
	 * 查找控件ID
	 */
	private void findView() {
		back = (ImageView) findViewById(R.id.back);
		checknum = (EditText) findViewById(R.id.checknum);
		telnum = (EditText) findViewById(R.id.telnum);
		next = (LinearLayout) findViewById(R.id.next);
		getchecknum = (LinearLayout) findViewById(R.id.getchecknum);
		checknumtext = (TextView) findViewById(R.id.checknumtext);
		telnum_delete = (ImageView) findViewById(R.id.telnum_delete);
	}

	/**
	 * 初始化数据 设置监听事件
	 * 
	 */
	private void initView() {
		back.setOnClickListener(this);
		next.setOnClickListener(this);
		getchecknum.setOnClickListener(this);
		next.setClickable(false);
		getchecknum.setClickable(false);
		telnum_delete.setOnClickListener(this);
		time = new TimeCount(60000, 1000);// 构造CountDownTimer对象
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
		drawable_clickable = getResources().getDrawable(
				R.drawable.login_btn_click);
		drawable_unclickable = getResources().getDrawable(
				R.drawable.login_btn_unclick);
		drawable_checkclickable = getResources().getDrawable(
				R.drawable.login_btn_click);
		drawable_checkunclickable = getResources().getDrawable(
				R.drawable.login_btn_unclick);

		drawable_checkunclick = getResources().getDrawable(
				R.drawable.checknum_btn_unclick);

		telnum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					telnum_delete.setVisibility(View.VISIBLE);
					if (checknum.getText().length() != 0) {
						next.setClickable(true);
						next.setBackgroundDrawable(drawable_clickable);
					}
					getchecknum.setClickable(true);
					getchecknum.setBackgroundDrawable(drawable_checkclickable);
				} else {
					telnum_delete.setVisibility(View.INVISIBLE);
					next.setBackgroundDrawable(drawable_unclickable);
					next.setClickable(false);
					getchecknum
							.setBackgroundDrawable(drawable_checkunclickable);
					getchecknum.setClickable(false);
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
		// 验证码EditText添加文本改变监听事件
		checknum.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.length() > 0) {
					if (telnum.getText().length() != 0) {
						next.setClickable(true);
						next.setBackgroundDrawable(drawable_clickable);
					}
				} else {
					next.setBackgroundDrawable(drawable_unclickable);
					next.setClickable(false);
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
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.getchecknum:
			time.start();// 开始计时
			if (validate()) {
				SMSSDK.getVerificationCode("86", telnum.getText().toString());
			}
			break;
		case R.id.next:
			if (validate()) {

				SMSSDK.submitVerificationCode("86",
						telnum.getText().toString(), checknum.getText()
								.toString());
			}
			break;
		case R.id.back:
			finish();
			break;
		case R.id.telnum_delete:
			telnum.setText("");
			break;
		default:

		}

	}

	private boolean validate() {
		if (telnum.getText().toString().length() != 11) {
			Toast.makeText(this, "请填写11位手机号!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			checknumtext.setText("获取验证码");
			telnum.setEnabled(true);
			getchecknum.setBackgroundDrawable(drawable_checkclickable);
			getchecknum.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			getchecknum.setClickable(false);
			telnum.setEnabled(false);
			getchecknum.setBackgroundDrawable(drawable_checkunclick);
			checknumtext.setText("重新发送(" + millisUntilFinished / 1000 + "s)");
		}
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
					Intent intent = new Intent(ForgetPsw.this,
							ResetPswActivity.class);
					intent.putExtra("phone", telnum.getText().toString());
					System.out.println("phone send ---> "
							+ telnum.getText().toString());
					startActivity(intent);
				} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
					Toast.makeText(getApplicationContext(), "验证码已经发送",
							Toast.LENGTH_SHORT).show();

					smsObserver = new SmsObserver(ForgetPsw.this, msgHandler);
					getContentResolver().registerContentObserver(SMS_INBOX,
							true, smsObserver);
				}
			} else {
				((Throwable) data).printStackTrace();
				// int resId = getStringRes(Register.this,
				// "smssdk_network_error");
				Toast.makeText(ForgetPsw.this, "验证码错误", Toast.LENGTH_SHORT)
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
			case 1: // 自动填写验证码
				String code = (String) msg.obj;
				checknum.setText(code);
				break;
			default:
				break;
			}
		}

	};

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
				msg.what = 1;
				msg.obj = res;
				msg.sendToTarget();
			}
		}
	}
}
