package tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;

public class DialogUtil {

    /**
     * 弹出GPS未代打开提示对话框
     * @param activity Activity
     */
	public static void showGPSDialog(final Activity activity) {
		new AlertDialog.Builder(activity)
				.setTitle("GPS定位")
				.setMessage("您的GPS没有开启，是否去开启？（开启后可以为您提供更精确的定位服务。）")
				.setPositiveButton("去开启",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								Intent intent = new Intent(
										Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								activity.startActivity(intent);
								dialog.cancel();
							}
						})
				.setNeutralButton("否，谢谢",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								dialog.dismiss();
							}
						}).show();

	}

	/**
     * 弹出网络异常提示对话框
     * @param activity Activity
     */
	public static void showNetDialog(final Activity activity) {
		new AlertDialog.Builder(activity)
				.setTitle("网络异常")
				.setMessage("没有找到可用的网络，请确认WLAN、4G、3G、2G网络至少有一项可以使用。")
				.setPositiveButton("设置", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Intent intent = new Intent();
						// 判断手机系统的版本 即API大于10 就是3.0或以上版本
						if (android.os.Build.VERSION.SDK_INT > 10) {
							intent.setAction(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
						} else {
							ComponentName component = new ComponentName(
									"com.android.settings",
									"com.android.settings.WirelessSettings");
							intent.setComponent(component);
							intent.setAction("android.intent.action.VIEW");
						}
						activity.startActivity(intent);
						dialog.cancel();
					}
				})
				.setNeutralButton("退出", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();
						activity.finish();
						System.exit(0);
					}
				}).show();
	}
}