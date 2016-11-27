package application;

import java.io.File;

import tools.Tools;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import baidu.BaiDuMapTraceActivity;
import baidu.GPSService;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.iflytek.cloud.SpeechUtility;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.youge.jobfinder.LocationService;

import exception.CrashHandler;

public class MyApplication extends Application{
	
	public static MyApplication application;
	public LocationService locationService;
	
	/**
	 * 轨迹服务
	 */
	public static Trace trace;

	/**
	 * entity标识
	 */
	public static String entityName;

	/**
	 * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
	 */
	public static long serviceId = 107704; // serviceId为开发者创建的鹰眼服务ID

	/**
	 * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
	 */
	public int traceType = 2;
	
	/**
	 * 开启轨迹服务监听器
	 */
	public static OnStartTraceListener startTraceListener;

	/**
	 * 停止轨迹服务监听器
	 */
	public static OnStopTraceListener stopTraceListener;

	/**
	 * 轨迹服务客户端
	 */
	public static LBSTraceClient client;

	public static MyApplication getApplication(){
		return application;
	}

	@Override
	public void onCreate(){
		// TODO Auto-generated method stub
		super.onCreate();
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
		initImageLoader(this);

		SMSSDK.initSDK(this, "d9ca66fec92c", "e449fa7ffd4bca1845630c663aaaecf3");

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());//

		/***
		 * 初始化定位sdk，建议在Application中创建
		 */
		locationService = new LocationService(getApplicationContext());

		ShareSDK.initSDK(getApplicationContext());
		SpeechUtility.createUtility(this, "appid=567a35bc");
		
		initTrace();
		
		startTrace();//
		
		initInfo();
		

		
		//initLoc();
	}
	
	private void initTrace(){

		entityName = new Tools().getEntityName(getApplicationContext());
		Log.e("entityName in trace", entityName);
		// 初始化轨迹服务客户端
		client = new LBSTraceClient(getApplicationContext());
		// 设置位置采集和打包周期
		client.setInterval(30, 30);
		client.setProtocolType(1);
		// 初始化轨迹服务
		trace = new Trace(getApplicationContext(), serviceId, entityName,
				traceType);
		Log.e("entityName after new", trace.getEntityName());
		// 初始化OnEntityListener
		initOnEntityListener();
	}
	
	public void startTrace(){
		client.startTrace(trace, startTraceListener);
	}
	
	public void stopTrace(){
		//停止轨迹服务
		client.stopTrace(trace,stopTraceListener);
	}
	
	/**
	 * 初始化OnEntityListener
	 */
	private void initOnEntityListener(){

		// 初始化startTraceListener
		startTraceListener = new OnStartTraceListener(){

			// 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
			public void onTraceCallback(int arg0, String arg1){
				// TODO Auto-generated method stub
				Log.e("startTraceListener", arg1);
			}

			// 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
			public void onTracePushCallback(byte arg0, String arg1){
				// TODO Auto-generated method stub
				// showMessage("轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1
				// + "]", null);
			}
		};

		// 初始化stopTraceListener
		stopTraceListener = new OnStopTraceListener(){

			// 轨迹服务停止成功
			public void onStopTraceSuccess(){
				// TODO Auto-generated method stub
				Log.e("stopTraceListener", "onStopTraceSuccess");
			}

			// 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
			public void onStopTraceFailed(int arg0, String arg1){
				// TODO Auto-generated method stub
				Log.e("stopTraceListener", "onStopTraceFailed");
			}
		};
	}


	private void initImageLoader(Context context){
		/******************* 配置ImageLoder ***********************************************/
//		DisplayImageOptions options = new DisplayImageOptions.Builder()
////				.showImageOnLoading(R.drawable.ic_stub) // 加载图片时的图片
////				.showImageForEmptyUri(R.drawable.ic_empty) // 没有图片资源时的默认图片
////				.showImageOnFail(R.drawable.ic_error) // 加载失败时的图片
////				.cacheInMemory(true) // 启用内存缓存
////				.cacheOnDisk(true) // 启用外存缓存
//				.considerExifParams(true) // 启用EXIF和JPEG图像格式
////				.displayer(new RoundedBitmapDisplayer(5)) // 设置显示风格这里是圆角矩形
//				.build();
//
//		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
//				"imageloader/Cache");
//
//		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(
//				context);
//		config.memoryCacheExtraOptions(480, 800);// max width, max height
//		config.threadPoolSize(3);// 线程池内加载的数量
//		config.threadPriority(Thread.NORM_PRIORITY - 2); //
//		// 降低线程的优先级保证主UI线程不受太大影响
//		config.denyCacheImageMultipleSizesInMemory();
//		config.memoryCache(new LruMemoryCache(5 * 1024 * 1024)); //
//		// 建议内存设在5-10M,可以有比较好的表现
//		config.memoryCacheSize(5 * 1024 * 1024);
//		config.diskCacheSize(50 * 1024 * 1024);
//		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//		config.tasksProcessingOrder(QueueProcessingType.LIFO);
//		config.diskCacheFileCount(100); // 缓存的文件数量
//		config.diskCache(new UnlimitedDiskCache(cacheDir));
//		config.defaultDisplayImageOptions(options);
//		// connectTimeout (5 s), readTimeout (30 s)
//		config.imageDownloader(new BaseImageDownloader(context, 5 * 1000,
//				30 * 1000));
//		config.writeDebugLogs(); // Remove for release app
//
//		ImageLoader.getInstance().init(config.build());// 全局初始化此配置

		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				"imageloader/Cache");

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiskCache(cacheDir))// 自定义缓存路径
				.build();// 开始构建
		ImageLoader.getInstance().init(config);// 全局初始化此配置
		/*********************************************************************************/
	}

	private void initInfo(){
		SharedPreferences sharedPre = this.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = sharedPre.edit();
		if (sharedPre.getString("hisAddr1", "").equals("")){
			editor.putString("hisAddr1", "");
		}
		if (sharedPre.getString("lat1", "").equals("")){
			editor.putString("lat1", "");
		}
		if (sharedPre.getString("lon1", "").equals("")){
			editor.putString("lon1", "");
		}
		if (sharedPre.getString("hisAddr2", "").equals("")){
			editor.putString("hisAddr2", "");
		}
		if (sharedPre.getString("lat2", "").equals("")){
			editor.putString("lat2", "");
		}
		if (sharedPre.getString("lon2", "").equals("")){
			editor.putString("lon2", "");
		}
		if (sharedPre.getString("autoLoc", "").equals("")){
			editor.putString("autoLoc", "auto");
		}
		editor.commit();
	}

}
