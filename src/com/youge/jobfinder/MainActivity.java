package com.youge.jobfinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import model.VersionContent;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.LoginPopUpwindow;
import popup.PushPopUpWindow;
import popup.StartImgPopUpWindow;
import progressdialog.NewCustomProgressDialog;
import tools.Config;
import tools.DialogUtil;
import tools.Exit;
import tools.GPSUtil;
import tools.HttpClient;
import tools.MyDialog;
import tools.NetWorkUtil;
import tools.ServiceUtil;
import tools.Tools;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import baidu.GPSService;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.platform.comapi.map.A;
import com.baidu.platform.comapi.map.B;
import com.baidu.platform.comapi.map.C;
import com.baidu.platform.comapi.map.D;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.activity.GetOtherInfoActivity;
import com.youge.jobfinder.activity.LocationMainActivity;
import com.youge.jobfinder.activity.OrderListDetailActivity;

import fragment.DiscoverMainFragment;
import fragment.MainGrabFragment;
import fragment.MainPostOrderFragment;
import fragment.OrderListMainFragment;
import fragment.VipCenterMainFragment;

public class MainActivity extends BaseActivity implements OnClickListener, MainGrabCallBack{

	private TextView grabTV, postTV, location;
	public RelativeLayout parent;
	private LinearLayout addrLL;
	private FrameLayout mainBar;

	private LinearLayout main, order, discover, pcenter;
	private ImageView mainIv, orderIv, disIv, pcenterIv, new_news;
	private TextView mainTv, orderTv, disTv, pcenterTv;

	private MainGrabFragment mgFragment;
	private MainPostOrderFragment mpFragment;
	private LoginPopUpwindow popLogin;

	private OrderListMainFragment olFragment;
	private DiscoverMainFragment discoverFragment;
	private VipCenterMainFragment vipFragment;

	public static MainActivity instance;
	private boolean isExit = false;

	private LocationService locationService;
	private String lat = "0", lon = "0";
	private String city;

	private MyDialog updateDialog;

	private VersionContent versionContent;

	private ProgressDialog pds;
	private Boolean isFromTL = false;
	private Boolean isFromSplash;
	private String notificationID;
	private String notificationType, notificationoid, notificationcategory,
			notificationwhere, notificationorderState;
	private registerReceiver receiver;
	private FragmentManager fm;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		fm = getFragmentManager();
		if (savedInstanceState != null){
			mgFragment = (MainGrabFragment) fm.findFragmentByTag("mgFragment");
			mpFragment = (MainPostOrderFragment) fm
					.findFragmentByTag("mpFragment");
			olFragment = (OrderListMainFragment) fm
					.findFragmentByTag("olFragment");
			discoverFragment = (DiscoverMainFragment) fm
					.findFragmentByTag("discoverFragment");
			vipFragment = (VipCenterMainFragment) fm
					.findFragmentByTag("vipFragment");
		}
		setContentView(R.layout.activity_main);
		initView();
		initLoc();
		lat = new Tools().getCurrentLat(this);
		lon = new Tools().getCurrentLng(this);
		// String info = getDeviceInfo(this);
		// Log.e("info", info);

		if (Config.notificationNum != 0){
			for(int i = 1; i < Config.notificationNum + 1; i++){
				NotificationManager nManager = (NotificationManager) this
						.getSystemService(this.NOTIFICATION_SERVICE);
				nManager.cancel(i);
			}
			Config.notificationNum = 0;
		}
		if (isFromTL){
			setBg(4);
			setToVipFragment();
			isFromTL = false;
		}else{
			setDefaultFragment();
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run(){
					// TODO Auto-generated method stub
					if ("1".equals(notificationType)){//
						turnOrder();
					}else{
						setBg(1);
						if ("3".equals(notificationType)){
							mandatoryExit(MainActivity.this);
						}else if ("2".equals(notificationType)){
							Intent intent = new Intent(MainActivity.this,
									GetOtherInfoActivity.class);
							intent.putExtra("otherUserId", notificationID);
							startActivity(intent);
						}else{
							updateVersion();
						}

					}
				}
			}, 100);
		}
	}

	public static String getDeviceInfo(Context context){
		try{
			org.json.JSONObject json = new org.json.JSONObject();
			android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			String device_id = tm.getDeviceId();

			android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);

			String mac = wifi.getConnectionInfo().getMacAddress();
			json.put("mac", mac);

			if (TextUtils.isEmpty(device_id)){
				device_id = mac;
			}

			if (TextUtils.isEmpty(device_id)){
				device_id = android.provider.Settings.Secure.getString(
						context.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
			}

			json.put("device_id", device_id);

			return json.toString();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences sharedPre = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		if (sharedPre.getBoolean("isNewsPush", false)){
			new_news.setVisibility(View.VISIBLE);
		}else{
			new_news.setVisibility(View.GONE);
		}
		if ("3".equals(new Tools().getNotificationType(MainActivity.this))){
			mandatoryExit(MainActivity.this);
		}
		registerReceiver();
	}

	/**
	 * Stop location service
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
	}

	/***
	 * 
	 */
	@Override
	protected void onStop(){
		// TODO Auto-generated method stub
		super.onStop();
		try{
			unregisterReceiver(receiver);
			// locationService.unregisterListener(mListener); // 注销掉监听
			// locationService.stop(); // 停止定位服务
		}catch(IllegalArgumentException e){
			Log.e("IllegalArgumentException", e.toString());
		}
	}

	@Override
	protected void onDestroy(){
		// TODO Auto-generated method stub
		// IntentFilter filter = new IntentFilter();
		// this.registerReceiver(new PushReceiver(), filter);
		super.onDestroy();

	}

	// protected void onPause(){
	// super.onPause();
	// unregisterReceiver(receiver);
	// }

	private void initView(){
		isFromSplash = getIntent().getBooleanExtra("isFromSplash", false);
		SharedPreferences sharedPre = this.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		editor.putBoolean("isFromSplash", isFromSplash);

		if (isFromSplash){
			guidpageHttpClient();
		}

		// 下载进度条
		pds = new ProgressDialog(this);
		pds.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pds.setCancelable(false);// 设置是否可以通过点击Back键取消
		pds.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条

		parent = (RelativeLayout) findViewById(R.id.main_parent);
		grabTV = (TextView) findViewById(R.id.main_grab);
		postTV = (TextView) findViewById(R.id.main_post);
		addrLL = (LinearLayout) findViewById(R.id.main_addr);
		location = (TextView) findViewById(R.id.main_location);

		mainBar = (FrameLayout) findViewById(R.id.ffffs);
		mainBar.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener(){
					@Override
					public void onGlobalLayout(){
						SharedPreferences sharedPre = MainActivity.this
								.getSharedPreferences("user",
										Context.MODE_PRIVATE);
						Editor editor = sharedPre.edit();
						editor.putString("mainBarHeight", mainBar.getHeight()
								+ "");
						editor.commit();
						mainBar.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
					}
				});

		main = (LinearLayout) findViewById(R.id.bottom_frag_main_ll);
		order = (LinearLayout) findViewById(R.id.bottom_frag_order_ll);
		discover = (LinearLayout) findViewById(R.id.bottom_frag_discover_ll);
		pcenter = (LinearLayout) findViewById(R.id.bottom_frag_pcenter_ll);
		mainIv = (ImageView) findViewById(R.id.bottom_frag_main_iv);
		orderIv = (ImageView) findViewById(R.id.bottom_frag_order_iv);
		disIv = (ImageView) findViewById(R.id.bottom_frag_discover_iv);
		pcenterIv = (ImageView) findViewById(R.id.bottom_frag_pcenter_iv);
		mainTv = (TextView) findViewById(R.id.bottom_frag_main_tv);
		orderTv = (TextView) findViewById(R.id.bottom_frag_order_tv);
		disTv = (TextView) findViewById(R.id.bottom_frag_discover_tv);
		pcenterTv = (TextView) findViewById(R.id.bottom_frag_pcenter_tv);
		new_news = (ImageView) findViewById(R.id.new_news);
		if (sharedPre.getBoolean("isNewsPush", false)){
			new_news.setVisibility(View.VISIBLE);
		}
		editor.commit();
		isFromTL = getIntent().getBooleanExtra("isFromTL", false);
		notificationID = getIntent().getStringExtra("notificationID");
		notificationType = getIntent().getStringExtra("notificationType");
		notificationoid = getIntent().getStringExtra("notificationoid");
		notificationcategory = getIntent().getStringExtra(
				"notificationcategory");
		notificationwhere = getIntent().getStringExtra("notificationwhere");
		notificationorderState = getIntent().getStringExtra(
				"notificationorderState");

		instance = this;

		main.setOnClickListener(this);
		order.setOnClickListener(this);
		discover.setOnClickListener(this);
		pcenter.setOnClickListener(this);

		grabTV.setOnClickListener(this);
		postTV.setOnClickListener(this);
		addrLL.setOnClickListener(this);
		location.setOnClickListener(this);
	}

	private void initLoc(){
		// -----------location config ------------
		locationService = new LocationService(this);
		// locationService = ((MyApplication) getApplication()).locationService;
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		locationService.start();// 定位SDK
		// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request

	}

	public Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 0:
				break;

				default:
				break;
			}
		}
	};

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					location.setText((String) msg.obj);
				break;
				case 2:
					new StartImgPopUpWindow(MainActivity.this, parent,
							msg.obj.toString());
				break;

				default:
				break;
			}

		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 400:
				if (resultCode == RESULT_OK){
					String des = data.getExtras().getString("name");
					lat = data.getExtras().getString("lat");
					lon = data.getExtras().getString("lon");
					System.out.println("des --> " + des);
					System.out.println("lat   " + lat);
					System.out.println("lon    " + lon);
					setDefaultFragment();
					Message msg = handler.obtainMessage();
					msg.what = 1;
					msg.obj = des;
					msg.sendToTarget();
					SharedPreferences sp = MainActivity.this
							.getSharedPreferences("user", Context.MODE_PRIVATE);
					double d = new Tools().DistanceOfTwoPoints(
							Double.valueOf(lat), Double.valueOf(lon),
							Double.valueOf(sp.getString("lat_last", "")),
							Double.valueOf(sp.getString("lon_last", "")));
					Log.e("distace onActivityResult", d + "");
					if (d >= 1000){
						updateLatLon(new Tools().getUserId(MainActivity.this),
								lat, lon);
					}
					Editor editor = sp.edit();
					editor.putString("lat_last", lat);
					editor.putString("lon_last", lon);
					editor.commit();
				}
			break;
			case 999:// 登录
				if (resultCode == RESULT_OK){
					// isFromTL = data.getBooleanExtra("isFromTL", false);
					onCreate(null);
				}
			break;
			default:
			break;
		}
	}

	private BDLocationListener mListener = new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation arg0){
			// TODO Auto-generated method stub
			String describe = arg0.getLocationDescribe();//
			Message msg = handler.obtainMessage();
			msg.what = 1;
			msg.obj = describe + "(" + (arg0.getDistrict() + arg0.getStreet())
					+ "...)";
			msg.sendToTarget();
			// System.out.println("getAddrStr ---> " + arg0.getAddrStr());
			// System.out.println("getBuildingName ---> " +
			// arg0.getBuildingName());
			// System.out.println("getDerect ---> " + arg0.getDerect());
			// System.out.println("getDistrict ---> " + arg0.getDistrict());
			// System.out.println("getStreet ---> " + arg0.getStreet());
			// System.out.println("getStreetNumber ---> " +
			// arg0.getStreetNumber());
			if (Math.abs(arg0.getLatitude() - 0.0) > 0.000001
					&& Math.abs(arg0.getLongitude() - 0.0) > 0.000001){
				lat = "" + arg0.getLatitude();
				lon = "" + arg0.getLongitude();
				city = arg0.getCity();
				System.out.println("lat mListener---> " + lat);
				System.out.println("lon mListener---> " + lon);
				
				locationService.unregisterListener(mListener); // 注销掉监听
				locationService.stop(); // 停止定位服务

				SharedPreferences sp = MainActivity.this.getSharedPreferences(
						"user", Context.MODE_PRIVATE);
				if (!sp.getString("lat_last", "").equals("")
						&& !sp.getString("lon_last", "").equals("")){
					double d = new Tools().DistanceOfTwoPoints(
							Double.valueOf(lat), Double.valueOf(lon),
							Double.valueOf(sp.getString("lat_last", "")),
							Double.valueOf(sp.getString("lon_last", "")));
					Log.e("distace", d + "");
					if (d >= 1000){
						updateLatLon(new Tools().getUserId(MainActivity.this),
								lat, lon);
					}
				}

				Editor editor = sp.edit();
				editor.putString("lat_last", lat);
				editor.putString("lon_last", lon);
				editor.commit();
			}else{
				Log.e("mainactivity 定位", "Double.MIN_VALUE || 0.0");
			}
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

	/**
	 * 首页默认fragment
	 */
	private void setDefaultFragment(){
		mainBar.setVisibility(View.VISIBLE);
		grabTV.setBackgroundResource(R.drawable.corner_green_right);
		postTV.setBackgroundColor(Color.rgb(34, 181, 112));
		grabTV.setTextColor(Color.rgb(34, 181, 112));
		postTV.setTextColor(Color.rgb(255, 255, 255));
		setBg(1);
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.addToBackStack(null);
		hideAll(transaction);

		if (mgFragment != null){
			transaction.show(mgFragment);
		}else{
			mgFragment = new MainGrabFragment();
			Bundle data = new Bundle();
			data.putString("lat", lat);
			data.putString("lon", lon);
			mgFragment.setArguments(data);
			transaction.add(R.id.main_frame, mgFragment, "mgFragment");
		}
		System.out.println("lat before set---> " + lat);
		System.out.println("lon before set ---> " + lon);
		transaction.commit();
	}

	private void setPostFragment(){
		mainBar.setVisibility(View.VISIBLE);
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.addToBackStack(null);
		hideAll(transaction);
		if (mpFragment != null){
			transaction.show(mpFragment);
		}else{
			mpFragment = new MainPostOrderFragment();
			Bundle data = new Bundle();
			data.putString("lat", lat);
			data.putString("lon", lon);
			mpFragment.setArguments(data);
			transaction.add(R.id.main_frame, mpFragment, "mpFragment");
		}
		transaction.commit();
	}

	private void setToOrderListMainFragment(){
		mainBar.setVisibility(View.GONE);
		// mainBar.getBackground().setAlpha(255);
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.addToBackStack(null);
		hideAll(transaction);
		if (olFragment != null){
			transaction.show(olFragment);
		}else{
			olFragment = new OrderListMainFragment();
			transaction.add(R.id.main_frame, olFragment, "olFragment");
		}
		transaction.commit();
	}

	private void setToDiscoverFragment(){
		mainBar.setVisibility(View.GONE);
		// mainBar.getBackground().setAlpha(255);
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.addToBackStack(null);
		hideAll(transaction);
		if (discoverFragment != null){
			transaction.show(discoverFragment);
		}else{
			discoverFragment = new DiscoverMainFragment();
			transaction.add(R.id.main_frame, discoverFragment,
					"discoverFragment");
		}
		transaction.commit();
	}

	private void setToVipFragment(){
		mainBar.setVisibility(View.GONE);
		// mainBar.getBackground().setAlpha(255);
		// FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.addToBackStack(null);
		hideAll(transaction);
		if (vipFragment != null){
			transaction.show(vipFragment);
		}else{
			vipFragment = new VipCenterMainFragment();
			transaction.add(R.id.main_frame, vipFragment, "vipFragment");
		}
		transaction.commit();
	}

	private void hideAll(FragmentTransaction transaction){
		if (mgFragment != null){
			transaction.hide(mgFragment);
		}
		if (mpFragment != null){
			transaction.hide(mpFragment);
		}
		if (olFragment != null){
			transaction.hide(olFragment);
		}
		if (discoverFragment != null){
			transaction.hide(discoverFragment);
		}
		if (vipFragment != null){
			transaction.hide(vipFragment);
		}
	}

	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.main_grab:// 我要抢单
				grabTV.setBackgroundResource(R.drawable.corner_green_right);
				postTV.setBackgroundColor(Color.rgb(34, 181, 112));
				grabTV.setTextColor(Color.rgb(34, 181, 112));
				postTV.setTextColor(Color.rgb(255, 255, 255));
				// initLoc();
				setDefaultFragment();
			break;
			case R.id.main_post: // 我要发单
				if (new Tools().isUserLogin(this)){
					mainBar.setBackgroundColor(Color.rgb(34, 181, 112));
					postTV.setBackgroundResource(R.drawable.corner_green_left);
					grabTV.setBackgroundColor(Color.rgb(34, 181, 112));
					postTV.setTextColor(Color.rgb(34, 181, 112));
					grabTV.setTextColor(Color.rgb(255, 255, 255));
					setPostFragment();
				}else{
					popLogin = new LoginPopUpwindow(this, parent, "main");
					parent.setAlpha((float) 0.8);
					popLogin.setOnDismissListener(new OnDismissListener(){

						@Override
						public void onDismiss(){
							// TODO Auto-generated method stub
							parent.setAlpha((float) 1);
						}
					});

				}
			break;
			case R.id.main_location:
				Intent intent = new Intent(this, LocationMainActivity.class);
				intent.putExtra("city", city);
				startActivityForResult(intent, 400);
			break;

			case R.id.bottom_frag_main_ll:
				setDefaultFragment();
			// startActivity(new Intent(getActivity(), MainActivity.class));

			break;

			case R.id.bottom_frag_order_ll:
				setBg(2);
				SharedPreferences sharedPre = getSharedPreferences("user",
						Context.MODE_PRIVATE);
				Editor editor = sharedPre.edit();
				editor.putBoolean("isNewsPush", false);
				editor.commit();
				new_news.setVisibility(View.GONE);
				if (new Tools().isUserLogin(this)){
					setToOrderListMainFragment();
				}else{
					setBg(1);
					popLogin = new LoginPopUpwindow(this, parent, "main");
					parent.setAlpha((float) 0.8);
					popLogin.setOnDismissListener(new OnDismissListener(){

						@Override
						public void onDismiss(){
							// TODO Auto-generated method stub
							parent.setAlpha((float) 1);
						}
					});
				}
			// if (new Tools().isUserLogin(getActivity())){
			// setBg(2);
			// startActivity(new Intent(getActivity(), OrderListMain.class));
			// }else{
			// new LoginPopUpwindow(getActivity(),
			// MainActivity.instance.parent);
			// }

			break;
			case R.id.bottom_frag_discover_ll:
				setBg(3);
				// if (new Tools().isUserLogin(this)) {
				setToDiscoverFragment();
			// } else {
			// setBg(1);
			// popLogin = new LoginPopUpwindow(this, parent);
			// parent.setAlpha((float) 0.8);
			// popLogin.setOnDismissListener(new OnDismissListener() {
			//
			// @Override
			// public void onDismiss() {
			// // TODO Auto-generated method stub
			// parent.setAlpha((float) 1);
			// }
			// });
			// }
			break;

			case R.id.bottom_frag_pcenter_ll:
				setBg(4);
				if (new Tools().isUserLogin(this)){
					setToVipFragment();
				}else{
					setBg(1);
					popLogin = new LoginPopUpwindow(this, parent, "main");
					parent.setAlpha((float) 0.8);
					popLogin.setOnDismissListener(new OnDismissListener(){

						@Override
						public void onDismiss(){
							// TODO Auto-generated method stub
							parent.setAlpha((float) 1);
						}
					});
				}

			// if (new Tools().isUserLogin(getActivity())){
			// setBg(4);
			// startActivity(new Intent(getActivity(),
			// PersonalCenterMain.class));
			// }else{
			// new LoginPopUpwindow(getActivity(),
			// MainActivity.instance.parent);
			// }

			break;
			case R.id.download:// 点击下载
				if (updateDialog.isShowing()){
					updateDialog.dismiss();
				}
				if (!Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)){
					Toast.makeText(getApplicationContext(), "请检查SD卡是否安装正确",
							Toast.LENGTH_SHORT).show();
				}else{
					new DownloadApkTask().execute(versionContent.getUrl(),
							new File(Environment.getExternalStorageDirectory()
									+ "/JobFinder").getAbsolutePath());
				}
			case R.id.cancel:// 点击取消
				if (updateDialog.isShowing()){
					updateDialog.dismiss();
				}
			break;
			default:
			break;
		}
	}

	private void setBg(int id){
		setAllToGrey();
		switch(id){
			case 1: // 首页
				mainIv.setImageResource(R.drawable.main_green);
				mainTv.setTextColor(Color.rgb(34, 181, 112));
			break;
			case 2: // 订单
				orderIv.setImageResource(R.drawable.order_green);
				orderTv.setTextColor(Color.rgb(34, 181, 112));
			break;
			case 3: // 发现
				disIv.setImageResource(R.drawable.discover_green);
				disTv.setTextColor(Color.rgb(34, 181, 112));
			break;
			case 4: // 我的
				pcenterIv.setImageResource(R.drawable.pcenter_green);
				pcenterTv.setTextColor(Color.rgb(34, 181, 112));
			break;
			default:
			break;
		}
	}

	private void setAllToGrey(){
		mainIv.setImageResource(R.drawable.main_grey);
		mainTv.setTextColor(Color.rgb(51, 51, 51));
		orderIv.setImageResource(R.drawable.order_grey);
		orderTv.setTextColor(Color.rgb(51, 51, 51));
		disIv.setImageResource(R.drawable.discover_grey);
		disTv.setTextColor(Color.rgb(51, 51, 51));
		pcenterIv.setImageResource(R.drawable.pcenter_grey);
		pcenterTv.setTextColor(Color.rgb(51, 51, 51));
	}

	/*
	 * 按俩次back键退出程序
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK){
			if (!isExit){
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable(){
					public void run(){
						isExit = false;
					}
				}, 2000);

				return false;
			}
			stopService(new Intent(this, GPSService.class));
			Exit.getInstance().exit();
		}

		return super.onKeyDown(keyCode, event);
		// if(keyCode == KeyEvent.KEYCODE_MENU){
		// super.openOptionsMenu(); // 调用这个，就可以弹出菜单
		// }

	}

	/**
	 * 检查版本
	 * 
	 */
	private void updateVersion(){
		JSONObject job = new JSONObject();
		try{
			job.put("version", getNewVersion());
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.UPDATE_VERSION, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){

							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200){
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										String resumeResult = data
												.getString("result");
										if ("1".equals(resumeResult)){
											JSONObject userResume = data
													.getJSONObject("version");
											versionContent = new VersionContent();
											versionContent
													.setVersionNumber(userResume
															.getString("versionNumber"));
											versionContent.setContent(userResume
													.getString("content"));
											versionContent.setUrl(userResume
													.getString("url"));
											showUpdateDialog();
										}

									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(MainActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void updateLatLon(String uid, String lat, String lon){
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("lat", lat);
			job.put("lng", lon);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.UPDATE_LATLON_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("坐标提交接口返回 ---> " + str);
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 显示提醒更新的对话框
	 * 
	 */
	protected void showUpdateDialog(){
		updateDialog = new MyDialog(this, getWindowManager()
				.getDefaultDisplay().getWidth() * 3 / 4, 0,
				R.layout.update_dialog, R.style.my_dialog);
		updateDialog.setCanceledOnTouchOutside(false);
		TextView download = (TextView) updateDialog.findViewById(R.id.download);
		TextView cancel = (TextView) updateDialog.findViewById(R.id.cancel);
		TextView versionTextView = (TextView) updateDialog
				.findViewById(R.id.version);
		TextView contentTextView = (TextView) updateDialog
				.findViewById(R.id.content);
		versionTextView.setText(versionContent.getVersionNumber());
		contentTextView.setText(versionContent.getContent());
		download.setOnClickListener(this);
		cancel.setOnClickListener(this);

		updateDialog.show();

	}

	/**
	 * 获取当前版本号
	 * 
	 * @return 返回当前的版本号
	 */
	private String getNewVersion(){
		String currentVersion = "";
		try{
			currentVersion = getPackageManager().getPackageInfo(
					getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
		}catch(Exception e){
			e.printStackTrace();
		}
		return currentVersion;
	}

	/**
	 * 下载APK文件异步任务
	 * 
	 */
	protected class DownloadApkTask extends AsyncTask<String, Integer, Boolean>{
		File file;

		/**
		 * (non-JavaDoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 * @param result
		 *            标识是否下载成功
		 */
		@Override
		protected void onPostExecute(Boolean result){
			if (pds.isShowing()){
				pds.dismiss();
			}
			if (result){
				Toast.makeText(getApplicationContext(), "下载成功",
						Toast.LENGTH_SHORT).show();
				install(file);
			}else{
				Toast.makeText(getApplicationContext(), "下载失败",
						Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute(){
			pds.show();
			super.onPreExecute();
		}

		/**
		 * (non-JavaDoc)
		 * 
		 * @see android.os.AsyncTask#onProgressUpdate(java.lang.Object[])
		 * @param values
		 *            下载进度
		 */
		@Override
		protected void onProgressUpdate(Integer... values){
			pds.setProgress(values[0]);
			super.onProgressUpdate(values);
		}

		/**
		 * (non-JavaDoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(java.lang.Object[])
		 * @param params
		 *            下载地址,下载到本地路径
		 */
		@Override
		protected Boolean doInBackground(String... params){
			HttpGet httpGet = new HttpGet(params[0]);
			HttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000); // 设置连接时间
			HttpConnectionParams.setSoTimeout(httpParams, 10000); // 设置下载时间
			httpGet.setParams(httpParams);
			DefaultHttpClient httpClient = new DefaultHttpClient();
			InputStream is = null;
			try{
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity httpEntity = response.getEntity();
				if (response.getStatusLine().getStatusCode() == 200){
					pds.setMax((int) httpEntity.getContentLength());
					is = httpEntity.getContent();
					File path = new File(params[1]);
					if (!path.exists()){
						path.mkdirs();
					}
					file = new File(new File(params[1]), "JobFinder.apk");
					if (!file.exists()){
						file.createNewFile();
					}
					FileOutputStream os = new FileOutputStream(file);
					int len;
					int count = 0;
					byte[] buffer = new byte[1024];
					while((len = is.read(buffer)) != -1){
						os.write(buffer, 0, len);
						count += len;
						onProgressUpdate(count);
					}
					os.close();
					is.close();
					return true;

				}else{
					return false;
				}

			}catch(Exception e){
				e.printStackTrace();
				return false;
			}
		}

	}

	/**
	 * 激活安装组件 安装下载的APK文件
	 * 
	 */
	public void install(File file){
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 注册广播
	 * 
	 */
	private void registerReceiver(){
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

	protected class registerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent){

			if ("1".equals(new Tools().getNotificationType(context))){
				SharedPreferences sharedPre = getSharedPreferences("user",
						Context.MODE_PRIVATE);
				if (sharedPre.getBoolean("isNewsPush", false)){
					new_news.setVisibility(View.VISIBLE);
				}
			}

			if ("1".equals(new Tools().getNotificationType(context))
					|| "2".equals(new Tools().getNotificationType(context))){
				if (pps[0] != null && pps[0].isShowing()){
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(MainActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"MainActivity");
				pps[0] = pp;
			}else if ("3".equals(new Tools().getNotificationType(context))){
				if (pps[0] != null && pps[0].isShowing()){
					pps[0].dismiss();
				}
				mandatoryExit(context);
			}else{
				NotificationManager nManager = (NotificationManager) context
						.getSystemService(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.icon = R.drawable.logo;// 设置通知的图标
				// 添加声音提示
				notification.defaults = Notification.DEFAULT_SOUND;
				// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
				notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
				notification.defaults = Notification.DEFAULT_VIBRATE;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				long[] vibrate = { 0, 100, 200, 300 };
				notification.vibrate = vibrate;

				notification.tickerText = "找事吧"; // 显示在状态栏中的文字
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(), 0);
				// 点击状态栏的图标出现的提示信息设置
				notification
						.setLatestEventInfo(context, "找事吧",
								new Tools().getNotificationText(context),
								pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context){
		String CID = new Tools().getCID(MainActivity.this);
		String notificationText = new Tools()
				.getNotificationText(MainActivity.this);
		Boolean isFirstStart = new Tools().getisFirstStart(MainActivity.this);
		unBindAlias(new Tools().getUserId(MainActivity.this),
				new Tools().getCID(MainActivity.this), MainActivity.this);
		SharedPreferences sharedPre = MainActivity.this.getSharedPreferences(
				"user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(MainActivity.this, parent,
				new Tools().getNotificationText(context));
	}

	/**
	 * 绑定账户
	 */
	private void unBindAlias(String uid, String cid, Context context){
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", "1");// 强制退出是0

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.UN_BIND_ALIAS, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("解除绑定账户接口返回 ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										Log.v("别人资料", data.toString());
										String results = data
												.getString("result");
										if ("0".equals(results)){
											Log.v("别人资料", "提交失败");
										}else{
											Log.v("别人资料", "提交成功");
										}
									}
								}catch(JSONException e){
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
						}
					});
		}catch(JSONException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}

	private void turnOrder(){
		if (notificationwhere.equals("g")){
			if (notificationorderState.equals("1")
					|| notificationorderState.equals("2")){
				Intent intent = new Intent(this, OrderListDetailActivity.class);
				intent.putExtra("category", notificationcategory);
				intent.putExtra("oid", notificationoid);
				startActivity(intent);
			}else{
				Intent intent = new Intent(this, OrderListDetailActivity.class);
				intent.putExtra("where", new Tools().getWhere(this));
				intent.putExtra("category", notificationcategory);
				intent.putExtra("orderState", notificationorderState);
				intent.putExtra("oid", notificationoid);
				startActivity(intent);
			}
		}else{
			Intent intent = new Intent(this, OrderListDetailActivity.class);
			intent.putExtra("where", notificationwhere);
			intent.putExtra("category", notificationcategory);
			intent.putExtra("orderState", notificationorderState);
			intent.putExtra("oid", notificationoid);
			startActivity(intent);
		}
	}

	/**
	 * 
	 * 
	 */
	private void guidpageHttpClient(){
		JSONObject job = new JSONObject();
		try{
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.GUIDE_PAGE, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){

							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200){
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										Message msgs = handler.obtainMessage();
										msgs.what = 2;
										msgs.obj = data.getString("startImg");
										msgs.sendToTarget();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(MainActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Toast.makeText(MainActivity.this, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 
	 *@param
	 */
	@Override
	public String getLat(){
		// TODO Auto-generated method stub
		return lat;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public String getLng(){
		// TODO Auto-generated method stub
		return lon;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public void reFresh(){
		// TODO Auto-generated method stub
		initLoc();
	}
}
