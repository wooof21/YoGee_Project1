package tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.widget.Toast;

public class NetWorkUtil {
	/**
	 * 网络连接状态:未连接，GPRS状态，WIFI状态
	 */
	public static final int NET_STATE_NULL=-1;
	public static final int NET_STATE_GPRS=0;
	public static final int NET_STATE_WIFI=1;
	/**
	 * 检测当的网络（WLAN、3G/2G）状态
	 * @param context Context
	 * @return true 表示网络可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mConnecManager 
		                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnecManager != null) {
			NetworkInfo info = mConnecManager.getActiveNetworkInfo();
			if (info != null && info.isConnected()) 
			{
				// 当前网络是连接的
				if (info.getState() == NetworkInfo.State.CONNECTED) 
				{
					// 当前所连接的网络可用
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 网络已经连接,判断是wifi还是GPRS
	 * 设置一些自己的逻辑调用
	 * */
	public static int judgeNetworkState(Context context)
	{
	ConnectivityManager mConnecManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	State gprs = mConnecManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();  
	State wifi = mConnecManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();  
	//如果是gprs连接
	if(gprs==State.CONNECTED||gprs==State.CONNECTING)
	{
		Toast.makeText(context, "网络连接可用! GPRS模式!", Toast.LENGTH_SHORT)
		.show();
		return NET_STATE_GPRS;
	}
	//如果是wifi状态，一般wifi状态可以做一些其它的事情(如加载广告)
	if(wifi==State.CONNECTED|| wifi == State.CONNECTING)
	{
		Toast.makeText(context, "网络连接可用! WIFI模式!", Toast.LENGTH_SHORT)
		.show();
		return NET_STATE_WIFI;
	}else {
		return NET_STATE_NULL;
	}
	}
}
