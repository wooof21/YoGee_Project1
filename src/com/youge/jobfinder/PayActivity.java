package com.youge.jobfinder;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.PushPopUpWindow;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.pingplusplus.android.PaymentActivity;
import com.youge.jobfinder.activity.PayAgreementActivity;

public class PayActivity extends BaseActivity implements OnClickListener {

	private ImageView yinlianzhifu_cb1, zhifubaozhifu_cb, weixinzhifu_cb,
			payment_agreement;
	private TextView cancel_pay, affirm_pay, order_pay_protocol, amount;
	private Drawable payment_sel_mode;
	private Drawable payment_unsel_mode;
	private Drawable payment_agree;
	private Drawable payment_unagree;
	private Boolean payment_agreement_agree = true;
	private RelativeLayout parent;
	private static final int REQUEST_CODE_PAYMENT = 1;
	/**
	 * 银联支付渠道
	 */
	private static final String CHANNEL_UPACP = "upacp";
	/**
	 * 微信支付渠道
	 */
	private static final String CHANNEL_WECHAT = "wx";
	/**
	 * 支付支付渠道
	 */
	private static final String CHANNEL_ALIPAY = "alipay";

	private String channel;

	private String oid;
	private double totalPrice;

	private registerReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_pay);
		findView();
		initView();
		registerReceiver();
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
	 * 
	 * @param
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		unregisterReceiver(receiver);
	}

	/**
	 * 根据ID查找空间
	 */
	private void findView() {
		yinlianzhifu_cb1 = (ImageView) findViewById(R.id.yinlianzhifu_cb1);
		zhifubaozhifu_cb = (ImageView) findViewById(R.id.zhifubaozhifu_cb);
		weixinzhifu_cb = (ImageView) findViewById(R.id.weixinzhifu_cb);
		payment_agreement = (ImageView) findViewById(R.id.payment_agreement);
		cancel_pay = (TextView) findViewById(R.id.cancel_pay);
		affirm_pay = (TextView) findViewById(R.id.affirm_pay);
		order_pay_protocol = (TextView) findViewById(R.id.order_pay_protocol);
		parent = (RelativeLayout) findViewById(R.id.parent);
		amount = (TextView) findViewById(R.id.amount);
	}

	/**
	 * 初始化空间 设置事件
	 */
	private void initView() {
		totalPrice = Double.valueOf(getIntent().getStringExtra("amount"));
		System.out.println("totalPrice ---> " + (int) totalPrice);
		amount.setText(totalPrice + "");
		oid = getIntent().getStringExtra("id");
		payment_sel_mode = getResources().getDrawable(R.drawable.circle_sel);
		payment_unsel_mode = getResources().getDrawable(R.drawable.circle_uns);
		payment_agree = getResources().getDrawable(R.drawable.checkbox_pressed);
		payment_unagree = getResources().getDrawable(
				R.drawable.checkbox_normal_one);
		yinlianzhifu_cb1.setOnClickListener(this);
		zhifubaozhifu_cb.setOnClickListener(this);
		weixinzhifu_cb.setOnClickListener(this);
		payment_agreement.setOnClickListener(this);
		cancel_pay.setOnClickListener(this);
		affirm_pay.setOnClickListener(this);
		order_pay_protocol.setOnClickListener(this);
		payment_agreement.setBackgroundDrawable(payment_agree);
		changeColor(zhifubaozhifu_cb, yinlianzhifu_cb1, weixinzhifu_cb);
		channel = CHANNEL_ALIPAY;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.yinlianzhifu_cb1:
			changeColor(yinlianzhifu_cb1, zhifubaozhifu_cb, weixinzhifu_cb);
			channel = CHANNEL_UPACP;
			break;
		case R.id.zhifubaozhifu_cb:
			changeColor(zhifubaozhifu_cb, yinlianzhifu_cb1, weixinzhifu_cb);
			channel = CHANNEL_ALIPAY;
			break;
		case R.id.weixinzhifu_cb:
			changeColor(weixinzhifu_cb, zhifubaozhifu_cb, yinlianzhifu_cb1);
			channel = CHANNEL_WECHAT;
			break;
		case R.id.payment_agreement:
			if (payment_agreement_agree) {
				payment_agreement_agree = false;
				payment_agreement.setBackgroundDrawable(payment_unagree);
				order_pay_protocol.setTextColor(Color.rgb(145, 145, 145));
			} else {
				payment_agreement_agree = true;
				payment_agreement.setBackgroundDrawable(payment_agree);
				order_pay_protocol.setTextColor(Color.rgb(34, 181, 112));
			}

			break;
		case R.id.cancel_pay:
			setResult(RESULT_CANCELED);
			finish();
			break;
		case R.id.affirm_pay:
			if (payment_agreement_agree) {
				getCharge(channel, (int) totalPrice + "");
			} else {
				Toast.makeText(getApplicationContext(), "请同意支付协议",
						Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.order_pay_protocol:
			startActivity(new Intent(this, PayAgreementActivity.class));
			break;

		default:
			break;
		}

	}

	private void changeColor(ImageView imgOne, ImageView imgOTwo,
			ImageView imgThree) {
		imgOne.setBackgroundDrawable(payment_sel_mode);
		imgOTwo.setBackgroundDrawable(payment_unsel_mode);
		imgThree.setBackgroundDrawable(payment_unsel_mode);
	}

	/**
	 * 获取支付的charge
	 */
	private void getCharge(String channel, String amount) {
		// final CustomProgressDialog pd = CustomProgressDialog
		// .createDialog(WXEntryActivity.this);
		JSONObject job = new JSONObject();
		System.out.println("支付amount ---> " + amount);
		try {
			job.put("channel", channel);
			job.put("amount", amount);
			job.put("id", oid);
			job.put("userid", new Tools().getUserId(PayActivity.this));
			job.put("type", "1");

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.postOne(PayActivity.this, Config.BASE_URL + "pay", se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// pd.dismiss();
							if (arg0 == 200) {
								String charge = new String(arg2);
								Intent intent = new Intent();
								String packageName = getPackageName();
								ComponentName componentName = new ComponentName(
										packageName, packageName
												+ ".wxapi.WXPayEntryActivity");
								intent.setComponent(componentName);
								intent.putExtra(PaymentActivity.EXTRA_CHARGE,
										charge);
								startActivityForResult(intent,
										REQUEST_CODE_PAYMENT);
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(PayActivity.this, "网络连接失败，请检测网络！",
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

	/**
	 * onActivityResult 获得支付结果，如果支付成功，服务器会收到ping++ 服务器发送的异步通知。 最终支付成功根据异步通知为准
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 支付页面返回处理
		if (requestCode == REQUEST_CODE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				String result = data.getExtras().getString("pay_result");
				if (result.equals("success")) {
					Toast.makeText(PayActivity.this, "交易成功", Toast.LENGTH_SHORT)
							.show();
					setResult(RESULT_OK);
					PayActivity.this.finish();
				} else if (result.equals("fail")) {
					Toast.makeText(PayActivity.this, "交易失败", Toast.LENGTH_SHORT)
							.show();
				} else if (result.equals("cancel")) {
					Toast.makeText(PayActivity.this, "用户取消交易",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(PayActivity.this, "支付插件没有安装",
							Toast.LENGTH_SHORT).show();
				}
				// /*
				// * 处理返回值 "success" - payment succeed "fail" - payment failed
				// * "cancel" - user canceld "invalid" - payment plugin not
				// * installed
				// */
				// String errorMsg = data.getExtras().getString("error_msg"); //
				// 错误信息
				// String extraMsg = data.getExtras().getString("extra_msg"); //
				// 错误信息
				// showMsg(result, errorMsg, extraMsg);
			}
		}
	}

	public void showMsg(String title, String msg1, String msg2) {
		String str = title;
		if (null != msg1 && msg1.length() != 0) {
			str += "\n" + msg1;
		}
		if (null != msg2 && msg2.length() != 0) {
			str += "\n" + msg2;
		}
		AlertDialog.Builder builder = new Builder(PayActivity.this);
		builder.setMessage(str);
		builder.setTitle("提示");
		builder.setPositiveButton("OK", null);
		builder.create().show();
	}

	/**
	 * 注册广播
	 * 
	 */
	private void registerReceiver() {
		receiver = new registerReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.service.fresh");
		registerReceiver(receiver, filter);
	}

	/**
	 * 接收广播，设置下拉刷新是否显示
	 * 
	 */
	private PushPopUpWindow pp;
	private PushPopUpWindow[] pps = new PushPopUpWindow[] { null, null };

	protected class registerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if ("1".equals(new Tools().getNotificationType(context))
					|| "2".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(PayActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context), "PayActivity");
				pps[0] = pp;
			} else if ("3".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				mandatoryExit(context);
			} else {
				NotificationManager nManager = (NotificationManager) context
						.getSystemService(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.icon = R.drawable.logo;// 设置通知的图标
				// 添加声音提示
				notification.defaults = Notification.DEFAULT_SOUND;
				// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
				notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
				notification.defaults = Notification.DEFAULT_VIBRATE;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				long[] vibrate = { 0, 100, 200, 300 };
				notification.vibrate = vibrate;

				notification.tickerText = "找事吧"; // 显示在状态栏中的文字
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(), 0);
				// 点击状态栏的图标出现的提示信息设置
				notification
						.setLatestEventInfo(context, "找事吧",
								new Tools().getNotificationText(context),
								pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context) {
		String CID = new Tools().getCID(PayActivity.this);
		String notificationText = new Tools()
				.getNotificationText(PayActivity.this);
		Boolean isFirstStart = new Tools()
		.getisFirstStart(PayActivity.this);
		unBindAlias(new Tools().getUserId(PayActivity.this),
				new Tools().getCID(PayActivity.this), PayActivity.this);
		SharedPreferences sharedPre = PayActivity.this.getSharedPreferences(
				"user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(PayActivity.this, parent,
				new Tools().getNotificationText(context));
	}

	/**
	 * 绑定账户
	 */
	private void unBindAlias(String uid, String cid, Context context) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", "1");// 强制退出是0

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.UN_BIND_ALIAS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("解除绑定账户接口返回 ---> " + str);
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
}
