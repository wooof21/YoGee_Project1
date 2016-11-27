package receiver;

import baidu.GPSService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

public class ShutdownBroadcastReceiver extends BroadcastReceiver{

	 private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN";

	@Override
	public void onReceive(Context context, Intent intent) {
        Log.e("Shut down this system, ShutdownBroadcastReceiver onReceive()", "");  
        
//        if (intent.getAction().equals(ACTION_SHUTDOWN)) {  
//            Log.e("ShutdownBroadcastReceiver onReceive(), MobileLocatorService Stop", "");  
//              
//            SharedPreferences sp = context.getSharedPreferences("MyMobileLocation", Context.MODE_PRIVATE);  
//            sp.edit().putString("instruct", "exit").commit();  
//              
//            context.stopService(new Intent(GPSService.ACTION_MY_LOCARION_SERVICE));  
//        }  
		
	}  
	 
	 
}
