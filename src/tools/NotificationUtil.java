package tools;

import com.youge.jobfinder.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class NotificationUtil{

	Context mContext;
	private Notification mNotification;

	private NotificationManager mNotificationManager;

	public NotificationUtil(Context context){
		mContext = context;
		mNotificationManager = (NotificationManager) mContext
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	/**
	 * 发送网络不可用的通知
	 * 
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public void sendNetworkNotification(){

		mNotification = new Notification(R.drawable.logo,
				"网络不可用，请确认WLAN、4G、3G、2G网络至少有一项可以使用。",
				System.currentTimeMillis());
		// 该标志表示当用户点击 Clear 之后，能够清除该通知。
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// 向QQ一样常驻通知栏
		// mNotification.flags = Notification.FLAG_ONGOING_EVENT;

		// 将使用默认的声音来提醒用户
		mNotification.defaults = Notification.DEFAULT_SOUND;

		Intent intent = new Intent();
		// 判断手机系统的版本 即API大于10 就是3.0或以上版本
		if (android.os.Build.VERSION.SDK_INT > 10){
			intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
		}else{
			ComponentName component = new ComponentName("com.android.settings",
					"com.android.settings.WirelessSettings");
			intent.setComponent(component);
			intent.setAction("android.intent.action.VIEW");
		}

		// 这里需要设置Intent.FLAG_ACTIVITY_NEW_TASK属性
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mContentIntent = PendingIntent.getActivity(mContext, 0,
				intent, 0);
		// 这里必需要用setLatestEventInfo(上下文,标题,内容,PendingIntent)不然会报错.
		mNotification.setLatestEventInfo(mContext,
				"网络提醒",
				"网络异常，请确认WLAN、4G、3G、2G网络至少有一项可以使用",
				mContentIntent);
		// 这里发送通知(消息ID,通知对象)
		mNotificationManager.notify(1104,
				mNotification);
	}

	/**
	 * 发送GPS未开启的通知
	 * 
	 * @param context
	 */
	@SuppressWarnings("deprecation")
	public void sendGPSNotification(){
		mNotification = new Notification(R.drawable.ic_launcher,
				"您的GPS没有开启，开启后可以为您提供更精确的定位服务。",
				System.currentTimeMillis());
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.defaults = Notification.DEFAULT_SOUND;

		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent mContentIntent = PendingIntent.getActivity(mContext, 0,
				intent, 0);
		mNotification.setLatestEventInfo(mContext,
				"网络提醒",
				"网络异常，请确认WLAN、4G、3G、2G网络至少有一项可以使用",
				mContentIntent);
		mNotificationManager.notify(1105,
				mNotification);
	}

	/**
	 * 取消通知
	 * 
	 * @param notificationId
	 */
	public void cancelNotification(int notificationId){
		mNotificationManager.cancel(notificationId);
	}
}
