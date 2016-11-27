/**
 * 
 */
package tools;

import com.youge.jobfinder.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * @author Liming Chu
 * 
 * @param
 * @return
 */
public class ShowNotification {

	private Notification notification;
	private NotificationManager nManager;

	@SuppressWarnings("deprecation")
	public void show(Context context, String title, int id, String text,
			Intent intent, String notificationType, String notificationID,
			String oid, String category, String where, String orderState) {
		nManager = (NotificationManager) context
				.getSystemService(context.NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.drawable.logo;// 设置通知的图标
		// 添加声音提示
		notification.defaults = Notification.DEFAULT_SOUND;
		// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
		notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
		// 下边的两个方式可以添加音乐
		// notification.sound =
		// Uri.parse("file:///sdcard/notification/ringer.mp3");
		// notification.sound =
		// Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		notification.defaults = Notification.DEFAULT_VIBRATE;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		long[] vibrate = { 0, 100, 200, 300 };
		notification.vibrate = vibrate;
		intent.putExtra("notificationType", notificationType);
		intent.putExtra("notificationID", notificationID);
		intent.putExtra("notificationoid", oid);
		intent.putExtra("notificationcategory", category);
		intent.putExtra("notificationwhere", where);
		intent.putExtra("notificationorderState", orderState);
		System.out.println(notificationType + "dshfdskljfkldsjflkfdklf");
		// 创建一个PendingIntent，和Intent类似，不同的是由于不是马上调用，需要在下拉状态条出发的activity，所以采用的是PendingIntent,即点击Notification跳转启动到哪个Activity
		PendingIntent pendingIntent = PendingIntent.getActivity(context, id,
				intent, 0);

		notification.tickerText = title; // 显示在状态栏中的文字
		// 点击状态栏的图标出现的提示信息设置
		notification.setLatestEventInfo(context, title, text, pendingIntent);
		nManager.notify(id, notification);
	}
}
