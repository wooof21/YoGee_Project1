/**
 * 
 * @param
 */
package baidu;

import java.util.Timer;
import java.util.TimerTask;

import tools.GPSUtil;
import tools.NetWorkUtil;
import tools.NotificationUtil;
import tools.ServiceUtil;
import tools.Tools;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.youge.jobfinder.LocationService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import application.MyApplication;

/**
 * 
 * @param
 */
public class GPSService extends Service{

	/**
	 * Service action.
	 */
	public static final String ACTION_MY_LOCARION_SERVICE = "com.jobfinder.gpsService";

	private LocationService locationService;
	

	/**
	 * 
	 * @param
	 */
	@Override
	public IBinder onBind(Intent intent){
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate(){
		Log.e("--------------GPSService onCreate()----------------", "");
		// 设置为前台进程，尽量避免被系统干掉。
		// MobileLocatorService.this.setForeground(true);
	}
	

	/**
	 * 
	 * @param
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		// TODO Auto-generated method stub
		Log.e("--------------GPSService onStartCommand()----------------", "");

		
		// 初始化定位服务，配置相应参数
		initLoc();
		return Service.START_REDELIVER_INTENT;
	}
	/**
	 * 初始化定位服务，配置相应参数
	 */
	private void initLoc(){
		// -----------location config ------------
		locationService = new LocationService(this);
//		locationService = ((MyApplication) getApplication()).locationService;
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		locationService.start();// 定位SDK
		// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request

	}

	private BDLocationListener mListener = new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation arg0){
			// TODO Auto-generated method stub
			System.out.println("lat in service ---> " + arg0.getLatitude());
			System.out.println("lng in service ---> " + arg0.getLongitude());
			if(Math.abs(arg0.getLatitude() - 0.0) > 0.000001
					&& Math.abs(arg0.getLongitude() - 0.0) > 0.000001){
				save(arg0.getLatitude(), arg0.getLongitude());
			}else{
				Log.e("currentLatLng in service ", "4.9E-324 || 0.0");
			}
			// System.out.println("getAddrStr ---> " + arg0.getAddrStr());
			// System.out.println("getBuildingName ---> " +
			// arg0.getBuildingName());
			// System.out.println("getDerect ---> " + arg0.getDerect());
			// System.out.println("getDistrict ---> " + arg0.getDistrict());
			// System.out.println("getStreet ---> " + arg0.getStreet());
			// System.out.println("getStreetNumber ---> " +
			// arg0.getStreetNumber());

			// locationService.unregisterListener(mListener); // 注销掉监听
			// locationService.stop(); // 停止定位服务

			// System.out.println("des ---> " + describe);
			// System.out.println("error ---> " + arg0.getLocType());
			// System.out.println("District ---> " + arg0.getDistrict());
			// System.out.println("Street ---> " + arg0.getStreet());
			// System.out.println("getStreetNumber ---> " +
			// arg0.getStreetNumber());
			// System.out.println("address ---> " +
			// arg0.getAddress().streetNumber);
			// System.out.println("addressStr ---> " + arg0.getAddrStr());
			// System.out.println("getBuildingID ---> " + arg0.getBuildingID());
			// System.out.println("getBuildingName ---> " +
			// arg0.getBuildingName());
			// System.out.println("getFloor ---> " + arg0.getFloor());
		}

	};
	
	private void save(double lat, double lng){
		SharedPreferences sharedPre = this.getSharedPreferences("jobfinder",
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putString("currentLat", ""+lat);
		editor.putString("currentLng", ""+lng);
		editor.commit();
		System.out.println("after save lat ---> " + new Tools().getCurrentLat(this));
		System.out.println("after save lng ---> " + new Tools().getCurrentLng(this));
	}

	@Override
	public void onDestroy() {
		Log.e("---------------MyLocatorService onDestroy()----------------", "");
		locationService.unregisterListener(mListener); // 注销掉监听
		locationService.stop(); // 停止定位服务
		// 销毁时重新启动Service
		// Intent intent = new Intent(ACTION_MY_LOCATION_SERVICE);
		// intent.putExtra("startingMode", startingMode);
		// this.startService(intent);
	}
}
