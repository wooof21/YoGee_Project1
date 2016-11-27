/**
 * 
 * @param
 */
package com.youge.jobfinder.vip;

import java.io.UnsupportedEncodingException;

import login.Login;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.DataCleanManager;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.AboutUsActivity;
import com.youge.jobfinder.activity.AgreementActivity;
import com.youge.jobfinder.activity.FeedBackActivity;
import com.youge.jobfinder.activity.SettingFingerPostActivity;

/**
 * 
 * @param
 */
public class MoreSettingMainActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back;
	private ToggleButton loc, setting_order, setting_all, setting_disturbing;
	private FrameLayout clear;
	private TextView cache, logout;
	private RelativeLayout agreement, about, feedback, fingerpost;
	private registerReceiver receiver;
	private LinearLayout parent;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_main);
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
		registerReceiver();
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
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		SharedPreferences sharedPre = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		loc = (ToggleButton) findViewById(R.id.setting_overtime);
		setting_order = (ToggleButton) findViewById(R.id.setting_order);
		setting_all = (ToggleButton) findViewById(R.id.setting_all);
		setting_disturbing = (ToggleButton) findViewById(R.id.setting_disturbing);
		setting_order.setChecked(sharedPre.getBoolean("setting_order", true));
		setting_all.setChecked(sharedPre.getBoolean("setting_all", true));
		setting_disturbing.setChecked(sharedPre.getBoolean(
				"setting_disturbing", false));
		clear = (FrameLayout) findViewById(R.id.setting_main_clear_cache);
		feedback = (RelativeLayout) findViewById(R.id.setting_main_feedback);
		about = (RelativeLayout) findViewById(R.id.setting_main_aboutus);
		agreement = (RelativeLayout) findViewById(R.id.setting_main_agreement);
		logout = (TextView) findViewById(R.id.setting_main_logout);
		cache = (TextView) findViewById(R.id.setting_main_cache);
		fingerpost = (RelativeLayout) findViewById(R.id.setting_main_fingerpost);
		parent = (LinearLayout) findViewById(R.id.main_parent);

		back.setOnClickListener(this);
		clear.setOnClickListener(this);
		feedback.setOnClickListener(this);
		about.setOnClickListener(this);
		agreement.setOnClickListener(this);
		logout.setOnClickListener(this);
		fingerpost.setOnClickListener(this);

		loc.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {

				}
			}
		});
		setting_order.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (setting_order.isChecked()) {
					SharedPreferences sharedPre = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putBoolean("setting_order", true);
					editor.commit();
				} else {
					SharedPreferences sharedPre = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putBoolean("setting_order", false);
					editor.commit();
				}
			}
		});
		setting_all.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (setting_all.isChecked()) {
					setting_order.setClickable(true);
					SharedPreferences sharedPre = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putBoolean("setting_all", true);
					editor.commit();
				} else {
					setting_order.setChecked(false);
					setting_order.setClickable(false);
					SharedPreferences sharedPre = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putBoolean("setting_all", false);
					editor.putBoolean("setting_order", false);
					editor.commit();
				}
			}
		});

		setting_disturbing
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (setting_disturbing.isChecked()) {
							SharedPreferences sharedPre = getSharedPreferences(
									"user", Context.MODE_PRIVATE);
							Editor editor = sharedPre.edit();
							editor.putBoolean("setting_disturbing", true);
							editor.commit();
						} else {
							SharedPreferences sharedPre = getSharedPreferences(
									"user", Context.MODE_PRIVATE);
							Editor editor = sharedPre.edit();
							editor.putBoolean("setting_disturbing", false);
							editor.commit();
						}
					}
				});
		try {
			String cacheSize = DataCleanManager.getTotalCacheSize(this);
			cache.setText(cacheSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
			finish();
			break;
		case R.id.setting_main_clear_cache:
			final CustomProgressDialog pd = CustomProgressDialog
					.createDialog(this);
			DataCleanManager.clearAllCache(this);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pd.dismiss();
					try {
						cache.setText(DataCleanManager
								.getTotalCacheSize(MoreSettingMainActivity.this));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, 1000);
			break;

		case R.id.setting_main_feedback:
			startActivity(new Intent(this, FeedBackActivity.class));
			break;
		case R.id.setting_main_aboutus:
			startActivity(new Intent(this, AboutUsActivity.class));
			break;
		case R.id.setting_main_agreement:
			startActivity(new Intent(this, AgreementActivity.class));
			break;
		case R.id.setting_main_fingerpost:
			startActivity(new Intent(this, SettingFingerPostActivity.class));
			break;
		case R.id.setting_main_logout:
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MoreSettingMainActivity.this);

			builder.setMessage("是否确定注销账号");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							String CID = new Tools()
									.getCID(MoreSettingMainActivity.this);
							String notificationText = new Tools()
									.getNotificationText(MoreSettingMainActivity.this);
							Boolean isFirstStart = new Tools()
									.getisFirstStart(MoreSettingMainActivity.this);
							unBindAlias(
									new Tools()
											.getUserId(MoreSettingMainActivity.this),
									new Tools()
											.getCID(MoreSettingMainActivity.this),
									MoreSettingMainActivity.this);
							SharedPreferences sharedPre = MoreSettingMainActivity.this
									.getSharedPreferences("user",
											Context.MODE_PRIVATE);
							SharedPreferences.Editor editor = sharedPre.edit();

							editor.clear();
							editor.putString("cid", CID);
							editor.putBoolean("isFirstStart", isFirstStart);
							editor.putString("notificationText",
									notificationText);
							editor.commit();

							dialog.dismiss();

							Intent intent = new Intent(
									MoreSettingMainActivity.this, Login.class);
							startActivity(intent);
							finish();
						}
					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							// finish();
							dialog.dismiss();
						}
					});
			builder.create();
			builder.show();
			break;
		default:
			break;
		}
	}

	/**
	 * 绑定账户
	 */
	private void unBindAlias(String uid, String cid, Context context) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", "0");// 正常退出是0

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
					|| "2".equals(new Tools().getNotificationType

					(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(MoreSettingMainActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"MoreSettingMainActivity");
				pps[0] = pp;
			} else if ("3".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				mandatoryExit(context);
			} else {
				NotificationManager nManager = (NotificationManager)

				context.getSystemService

				(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.icon = R.drawable.logo;// 设置通知的图标
				// 添加声音提示
				notification.defaults = Notification.DEFAULT_SOUND;
				// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
				notification.audioStreamType =

				android.media.AudioManager.ADJUST_LOWER;
				notification.defaults = Notification.DEFAULT_VIBRATE;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				long[] vibrate = { 0, 100, 200, 300 };
				notification.vibrate = vibrate;

				notification.tickerText = "找事吧"; // 显示在状态栏中的文字
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(), 0);
				// 点击状态栏的图标出现的提示信息设置
				notification.setLatestEventInfo(context, "找事吧", new Tools

				().getNotificationText(context), pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context) {
		String CID = new Tools().getCID(MoreSettingMainActivity.this);
		String notificationText = new Tools()
				.getNotificationText(MoreSettingMainActivity.this);
		Boolean isFirstStart = new Tools()
				.getisFirstStart(MoreSettingMainActivity.this);
		unBindAlias(new Tools().getUserId(MoreSettingMainActivity.this),
				new Tools().getCID(MoreSettingMainActivity.this),
				MoreSettingMainActivity.this, "1");
		SharedPreferences sharedPre = MoreSettingMainActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(MoreSettingMainActivity.this,
				parent, new Tools().getNotificationText(context));
	}

	/**
	 * 绑定账户
	 */
	private void unBindAlias(String uid, String cid, Context context,
			String type) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", type);// 强制退出是1

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.UN_BIND_ALIAS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[]

						arg1, byte[] arg2) {
							if (arg0 == 200) {
								String str = new String

								(arg2);
								System.out.println("解除绑定账户接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result =

									new JSONObject(str);
									String state =

									result.getString("state");
									message =

									result.getString("msg");
									if (state.equals

									("success")) {
										JSONObject

										data = result

										.getJSONObject("data");
										Log.v("别人资料", data.toString());
										String

										results = data

										.getString("result");
										if

										("0".equals(results)) {

											Log.v("别人资料", "提交失败");
										} else {

											Log.v("别人资料", "提交成功");
										}
									}
								} catch (JSONException e) {
									e.printStackTrace

									();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[]

						arg1, byte[] arg2, Throwable

						arg3) {
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	// protected void onPause() {
	// super.onPause();
	// unregisterReceiver(receiver);
	// }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}

}
