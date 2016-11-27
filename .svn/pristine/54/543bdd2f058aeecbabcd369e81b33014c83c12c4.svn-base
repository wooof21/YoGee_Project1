package getui;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;

public class PushSleepService extends Service {

	private Boolean isFirstDengBoolean = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					if (isFirstDengBoolean) {
						Thread.sleep(5000);
					}
					SharedPreferences sharedPre = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putBoolean("isFirstDengBoolean", false);
					editor.commit();
					Intent intents = new Intent();
					intents.setAction("com.service.fresh");
					sendBroadcast(intents);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		return super.onStartCommand(intent, flags, startId);
	}
}
