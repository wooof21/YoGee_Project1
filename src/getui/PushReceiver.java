/**
 * 
 * @param
 */
package getui;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.HttpClient;
import tools.ShowNotification;
import tools.Tools;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.SplashActivity;
import com.youge.jobfinder.activity.FillOrderMainActivity;

/**
 * 
 * @param
 */
public class PushReceiver extends BroadcastReceiver {

	/**
	 * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView ==
	 * null)
	 */
	public static StringBuilder payloadData = new StringBuilder();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d("GetuiSdkDemo", "onReceive() action=" + bundle.getInt("action"));

		// if(intent.getAction().equals("com.youge.jobfinder.close")){
		// Toast.makeText(context, "1111", Toast.LENGTH_SHORT).show();
		// PushReceiver.this.goAsync();
		// }
		switch (bundle.getInt(PushConsts.CMD_ACTION)) {
		case PushConsts.GET_MSG_DATA:
			// 获取透传数据
			// String appid = bundle.getString("appid");
			byte[] payload = bundle.getByteArray("payload");

			String taskid = bundle.getString("taskid");
			String messageid = bundle.getString("messageid");

			// smartPush第三方回执调用接口，actionid范围为90000-90999，可根据业务场景执行
			boolean result = PushManager.getInstance().sendFeedbackMessage(
					context, taskid, messageid, 90001);
			System.out.println("第三方回执接口调用" + (result ? "成功" : "失败"));

			if (payload != null) {
				String data = new String(payload);

				Log.d("GetuiSdkDemo", "receiver payload : " + data);

				isRunningForeground(context, data);

				payloadData.append(data);
				payloadData.append("\n");
				//
			}
			break;

		case PushConsts.GET_CLIENTID:
			// 获取ClientID(CID)
			// 第三方应用需要将CID上传到第三方服务器，并且将当前用户帐号和CID进行关联，以便日后通过用户帐号查找CID进行消息推送
			String cid = bundle.getString("clientid");//
			System.out.println("cid --> " + cid);
			SharedPreferences sharedPre = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			Editor editor = sharedPre.edit();
			editor.putString("cid", cid);
			editor.commit();
			if (!"".equals(new Tools().getUserId(context))) {
				bindAlias(new Tools().getUserId(context), cid, new Tools().getEntityName(context), context);
			}
			break;

		case PushConsts.THIRDPART_FEEDBACK:
			/*
			 * String appid = bundle.getString("appid"); String taskid =
			 * bundle.getString("taskid"); String actionid =
			 * bundle.getString("actionid"); String result =
			 * bundle.getString("result"); long timestamp =
			 * bundle.getLong("timestamp"); Log.d("GetuiSdkDemo", "appid = " +
			 * appid); Log.d("GetuiSdkDemo", "taskid = " + taskid);
			 * Log.d("GetuiSdkDemo", "actionid = " + actionid);
			 * Log.d("GetuiSdkDemo", "result = " + result);
			 * Log.d("GetuiSdkDemo", "timestamp = " + timestamp);
			 */
			break;

		default:
			break;
		}
	}

	public static boolean isRunningForeground(Context context, String data) {

		String[] resultInfo = data.split("\\&");
		String status = resultInfo[1];
		String oid = "", category = "", where = "", orderState = "";
		SharedPreferences sharedPres = context.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		boolean setting_order = sharedPres.getBoolean("setting_order", true);
		boolean setting_all = sharedPres.getBoolean("setting_all", true);
		boolean setting_disturbing = sharedPres.getBoolean(
				"setting_disturbing", false);
		if (setting_disturbing) {
			if (new Tools().getNowTime()) {
				return false;
			}
		}
		if (setting_all) {
			if (!setting_order) {
				if ("4".equals(status)) {
					return false;
				}
			}
		} else {
			return false;
		}
		String text = resultInfo[0];
		String id = "";
		if ("2".equals(status)) {
			id = resultInfo[2];
		} else if (status.equals("1")) {
			SharedPreferences sharedPre = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			Editor editor = sharedPre.edit();
			editor.putBoolean("isNewsPush", true);
			editor.commit();
			oid = resultInfo[2];
			category = resultInfo[3];
			String type = resultInfo[4];
			orderState = resultInfo[5];
			if (type.equals("1")) {
				where = "g";
			} else if (type.equals("2")) {
				where = "p";
			} else if (type.equals("3")) {
				where = "inprocess_grab";
			} else if (type.equals("4")) {
				where = "inprocess_post";
			}
		}
		// Intent startIntent = new Intent(context, SplashActivity.class);
		// startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_NEW_TASK);
		// new ShowNotification().show(context, "找事吧", Integer.valueOf(status),
		// text, startIntent);
		System.out.println("status ---> " + status);
		System.out.println("text ---> " + text);
		String packageName = getPackageName(context);
		String topActivityClassName = getTopActivityName(context);
		System.out.println("packageName=" + packageName
				+ ",topActivityClassName=" + topActivityClassName);
		if (packageName != null && topActivityClassName != null
				&& topActivityClassName.startsWith(packageName)) {
			SharedPreferences sharedPre = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			Editor editor = sharedPre.edit();
			editor.putString("notificationType", status);
			editor.putString("notificationText", text);
			editor.putString("notificationId", id);
			editor.putString("oid", oid);
			editor.putString("category", category);
			editor.putString("where", where);
			editor.putString("orderState", orderState);
			editor.commit();
			Intent intent = new Intent();
			intent.setAction("com.service.fresh");
			context.sendBroadcast(intent);
			System.out.println("--->前台运行 isRunningForeGround");
			return true;
		} else {
			Intent startIntent = new Intent(context, SplashActivity.class);
			startIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_NEW_TASK);

			if (status.equals("1")) {
				Config.notificationNum = Config.notificationNum + 1;
				// 订单
				System.out.println("oid ---> " + oid);
				System.out.println("category ---> " + category);
				System.out.println("where ---> " + where);
				System.out.println("orderState ---> " + orderState);
				new ShowNotification().show(context, "找事吧",
						Config.notificationNum, text, startIntent, status, "",
						oid, category, where, orderState);
			} else if (status.equals("2")) {
				Config.notificationNum = Config.notificationNum + 1;
				// 别人资料
				new ShowNotification().show(context, "找事吧",
						Config.notificationNum, text, startIntent, status, id,
						"", "", "", "");
			} else if (status.equals("3")) {
				Config.notificationNum = Config.notificationNum + 1;
				String CID = new Tools().getCID(context);
				String notificationText = new Tools()
						.getNotificationText(context);
				Boolean isFirstStart = new Tools().getisFirstStart(context);
				unBindAlias(new Tools().getUserId(context),
						new Tools().getCID(context), context);
				SharedPreferences sharedPre = context.getSharedPreferences(
						"user", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPre.edit();

				editor.clear();
				editor.putString("cid", CID);
				editor.putBoolean("isFirstStart", isFirstStart);
				editor.putString("notificationText", notificationText);
				editor.commit();
				// 正常
				new ShowNotification().show(context, "找事吧",
						Config.notificationNum, text, startIntent, status, "",
						"", "", "", "");
			} else {
				Config.notificationNum = Config.notificationNum + 1;
				// 正常
				new ShowNotification().show(context, "找事吧",
						Config.notificationNum, text, startIntent, status, "",
						"", "", "", "");
			}
			System.out.println("--->后台运行 isRunningBackGround");
			return false;
		}
		// return false;
	}

	public static String getTopActivityName(Context context) {
		String topActivityClassName = null;
		ActivityManager activityManager = (ActivityManager) (context
				.getSystemService(android.content.Context.ACTIVITY_SERVICE));
		List<RunningTaskInfo> runningTaskInfos = activityManager
				.getRunningTasks(1);
		if (runningTaskInfos != null) {
			ComponentName f = runningTaskInfos.get(0).topActivity;
			topActivityClassName = f.getClassName();
		}
		return topActivityClassName;
	}

	public static String getPackageName(Context context) {
		String packageName = context.getPackageName();
		return packageName;
	}

	/**
	 * 
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
	 * 绑定账户
	 */
	private static void unBindAlias(String uid, String cid, Context context) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", "1");// 强制退出是0

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

}
