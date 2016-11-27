package receiver;

import baidu.GPSService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootBroadcastReceiver extends BroadcastReceiver{

	private static final String ACTION_BOOT="android.intent.action.BOOT_COMPLETED";
	@Override
	public void onReceive(Context context, Intent intent) {
//		 Log.e("Boot this system , BootBroadcastReceiver onReceive()", "");  
//		 
//		 if (intent.getAction().equals(ACTION_BOOT)) {  
//	            Log.e("BootBroadcastReceiver onReceive(), MobileLocatorService Start", "");  
//	            //启动后台服务
//	            Intent mIntent = new Intent(GPSService.ACTION_MY_LOCARION_SERVICE);  
//	            mIntent.putExtra("startingMode",1);  
//	            context.startService(mIntent);  
//	        }  
	}

}
