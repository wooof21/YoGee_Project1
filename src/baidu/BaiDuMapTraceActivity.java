/**
 * 
 * @param
 */
package baidu;

import java.util.ArrayList;
import java.util.List;

import progressdialog.CustomProgressDialog;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class BaiDuMapTraceActivity extends BaseActivity{

	private static MapView mapView;
	private static BaiduMap mBaiduMap;

	private double lat, lng, orderLat, orderLng;
	private String name;

	/**
	 * 轨迹服务
	 */
	protected static Trace trace = null;

	/**
	 * entity标识
	 */
	protected static String entityName = null;

	/**
	 * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
	 */
	protected static long serviceId = 107704; // serviceId为开发者创建的鹰眼服务ID

	/**
	 * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
	 */
	private int traceType = 2;

	/**
	 * 轨迹服务客户端
	 */
	protected static LBSTraceClient client = null;
	/**
	 * Entity监听器
	 */
	protected static OnEntityListener entityListener = null;
	protected static boolean isInUploadFragment = true;
	protected boolean isTraceStart = false;
	/**
	 * 刷新地图线程(获取实时点)
	 */
	protected RefreshThread refreshThread = null;

	/**
	 * 开启轨迹服务监听器
	 */
	protected static OnStartTraceListener startTraceListener = null;

	/**
	 * 停止轨迹服务监听器
	 */
	protected static OnStopTraceListener stopTraceListener = null;

	/**
	 * 采集周期（单位 : 秒）
	 */
	private int gatherInterval = 30;

	private static List<LatLng> pointList = new ArrayList<LatLng>();
	protected static MapStatusUpdate msUpdate = null;
	/**
	 * 图标
	 */
	private BitmapDescriptor realtimeBitmap;

	// 覆盖物
	protected static OverlayOptions overlay, myTOption, myMOption;
	// 路线覆盖物
	private static PolylineOptions polyline = null;

	private CustomProgressDialog pd;
	private LatLng orderPoint;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_trace_view);
		initView();
	}

	private void initView(){
		mapView = (MapView) findViewById(R.id.baidu_trace_mapview);
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		// 卫星地图
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(false);

		lat = getIntent().getDoubleExtra("lat", 0.0);
		lng = getIntent().getDoubleExtra("lng", 0.0);
		orderLat = getIntent().getDoubleExtra("myLat", 0.0);
		orderLng = getIntent().getDoubleExtra("myLng", 0.0);
		String userState = getIntent().getExtras().getString("userState");
		System.out.println("orderLat ---> " + orderLat);
		System.out.println("orderLng ---> " + orderLng);

		name = getIntent().getStringExtra("name");

		LatLng point = new LatLng(lat, lng);

		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19)
				.build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// 构建文字Option对象，用于在地图上添加文字
		OverlayOptions tOption = new TextOptions().bgColor(0xCC22b570)
				.fontSize(18).fontColor(0xFFFFFFFF).text(name + "的位置")
				.rotate(0).position(point);
		// 在地图上添加该文字对象并显示
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		// 构建MarkerOption，用于在地图上添加Marker
		OverlayOptions mOption = new MarkerOptions().position(point).icon(
				bitmap);

		// 构建Marker图标
		BitmapDescriptor bitmap1 = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding_own_loc);
		orderPoint = new LatLng(orderLat, orderLng);
		myTOption = new TextOptions().bgColor(0xCC22b570).fontSize(18)
				.fontColor(0xFFFFFFFF).text("订单位置").rotate(0)
				.position(orderPoint);
		myMOption = new MarkerOptions().position(orderPoint).icon(bitmap1);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(tOption);
		mBaiduMap.addOverlay(mOption);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(myTOption);
		mBaiduMap.addOverlay(myMOption);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0){
				// TODO Auto-generated method stub
				Log.e("marker", "clicked");

				return false;
			}
		});

		if (!userState.equals("3")){
			startTrack();
		}
	}

	private void startTrack(){
		entityName = getIntent().getExtras().getString("entityName", "");
		Log.e("entityName in trace", entityName);
		// 初始化轨迹服务客户端
		client = new LBSTraceClient(this);
		// 设置位置采集和打包周期
		client.setInterval(gatherInterval, 30);
		client.setProtocolType(1);
		// 初始化轨迹服务
		trace = new Trace(getApplicationContext(), serviceId, entityName,
				traceType);
		Log.e("entityName after new", trace.getEntityName());
		// 初始化OnEntityListener
		initOnEntityListener();
		// 添加entity
		addEntity();

		//client.startTrace(trace, startTraceListener);

		showMessage("准备实时追踪中...", null);
		isTraceStart = true;
		startRefreshThread(true);

	}

	private void realTimeTrace(){
		// entity标识列表（多个entityName，以英文逗号"," 分割）
		String entityNames = entityName;
		// 检索条件（格式为 : "key1=value1,key2=value2,....."）
		String columnKey = "";
		// 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
		int returnType = 0;
		// 活跃时间，UNIX时间戳（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
		int activeTime = 0;  
//		int activeTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
		// 分页大小
		int pageSize = 100;
		// 分页索引
		int pageIndex = 1;

		// 查询实时轨迹
		client.queryEntityList(serviceId, entityName, columnKey, returnType,
				activeTime, pageSize, pageIndex, entityListener);
	}

	/**
	 * 添加entity
	 * 
	 */
	protected void addEntity(){//
		// 属性名称（格式 : "key1=value1,columnKey2=columnValue2......."）
		String columnKey = "";
		client.addEntity(serviceId, entityName, columnKey, entityListener);
	}

	/**
	 * 初始化OnEntityListener
	 */
	private void initOnEntityListener(){
		entityListener = new OnEntityListener(){

			// 请求失败回调接口
			@Override
			public void onRequestFailedCallback(String arg0){
				// TODO Auto-generated method stub
				// Looper.prepare();
				// Toast.makeText(getApplicationContext(),
				// "entity请求失败回调接口消息 : " + arg0, Toast.LENGTH_SHORT)
				// .show();
				// Looper.loop();
			}

			// 添加entity回调接口
			@Override
			public void onAddEntityCallback(String arg0){
				// TODO Auto-generated method stub
				// Looper.prepare();
				// Toast.makeText(getApplicationContext(),
				// "添加entity回调接口消息 : " + arg0, Toast.LENGTH_SHORT).show();
				// Looper.loop();
			}

			// 查询entity列表回调接口
			@Override
			public void onQueryEntityListCallback(String message){
				// TODO Auto-generated method stub
				System.out.println("onQueryEntityListCallback ---> " + message);
				/**
				 * 查询实体集合回调函数，此时调用实时轨迹方法
				 */
				showRealtimeTrack(message);
			}

			@Override
			public void onReceiveLocation(TraceLocation location){
				// TODO Auto-generated method stub
				// showRealtimeTrack(location);
				Log.e("onReceiveLocation", "" + location.getLatitude());
				Log.e("onReceiveLocation", "" + location.getLongitude());
			}

		};

		// 初始化startTraceListener
		startTraceListener = new OnStartTraceListener(){

			// 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
			public void onTraceCallback(int arg0, String arg1){
				// TODO Auto-generated method stub
				if (0 == arg0 || 10006 == arg0){
				}
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
				// showMessage("停止轨迹服务成功", Integer.valueOf(1));
				isTraceStart = false;
				startRefreshThread(false);
				try{
					pd.dismiss();
					System.out.println("1111111112333333331222222222222");
					new Handler().postDelayed(new Runnable(){

						@Override
						public void run(){
							// TODO Auto-generated method stub
							BaiDuMapTraceActivity.this.finish();
						}
					}, 100);
				}catch(Exception e){
					// TODO: handle exception
				}
			}

			// 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
			public void onStopTraceFailed(int arg0, String arg1){
				// TODO Auto-generated method stub
				try{
					pd.dismiss();
					showMessage("停止实时定位出错, 请重试!", null);
				}catch(Exception e){
					// TODO: handle exception
				}
			}
		};
	}

	/**
	 * 展示实时线路图 for onQueryEntityListCallback
	 * 
	 * @param realtimeTrack
	 */
	protected void showRealtimeTrack(String realtimeTrack){

		if (refreshThread == null || !refreshThread.refresh){
			return;
		}

		// 数据以JSON形式存取
		RealtimeTrackData realtimeTrackData = GsonService.parseJson(
				realtimeTrack, RealtimeTrackData.class);

		if (realtimeTrackData != null && realtimeTrackData.getStatus() == 0){

			LatLng latLng = realtimeTrackData.getRealtimePoint();

			if(latLng != null){  
	            pointList.add(latLng);  
	            drawRealtimePoint(latLng);  
	        }  
	        else{  
	            Toast.makeText(getApplicationContext(), "当前无轨迹点", Toast.LENGTH_LONG).show();  
	        }  
//			if (latLng != null){
//				pointList.add(latLng);
//				if (isInUploadFragment){
//					// 绘制实时点
//					drawRealtimePoint(latLng);
//				}
//			}else{
//				showMessage("当前查询无轨迹点", null);
//			}

		}

	}

	/**
	 * 显示实时轨迹 for onReceiveLocation
	 * 
	 * @param realtimeTrack
	 */
	protected void showRealtimeTrack(TraceLocation location){

		if (null == refreshThread || !refreshThread.refresh){
			return;
		}

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		if (Math.abs(latitude - 0.0) < 0.000001
				&& Math.abs(longitude - 0.0) < 0.000001){
			showMessage("当前查询无轨迹点", null);

		}else{

			LatLng latLng = new LatLng(latitude, longitude);

			pointList.add(latLng);

			if (isInUploadFragment){
				// 绘制实时点
				drawRealtimePoint(latLng);
			}

		}

	}

	/**
	 * 绘制实时点
	 * 
	 * @param points
	 */
	private void drawRealtimePoint(LatLng point){

		mBaiduMap.clear();

		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(18)
				.build();

		msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

		realtimeBitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);

		overlay = new MarkerOptions().position(point).icon(realtimeBitmap)
				.zIndex(9).draggable(true);
		// 构建文字Option对象，用于在地图上添加文字
		OverlayOptions tOption = new TextOptions().bgColor(0xCC22b570)
				.fontSize(18).fontColor(0xFFFFFFFF).text(name + "的位置")
				.rotate(0).position(point);

		if (pointList.size() >= 2 && pointList.size() <= 1000){
			// 添加路线（轨迹）
			polyline = new PolylineOptions().width(5).dottedLine(true)
					.color(Color.GREEN).points(pointList);
		}

		addMarker(tOption);

	}

	/**
	 * 添加地图覆盖物
	 */
	protected static void addMarker(OverlayOptions tOption){

		if (null != msUpdate){
			mBaiduMap.setMapStatus(msUpdate);
		}

		// 路线覆盖物
		if (null != polyline){
			mBaiduMap.addOverlay(polyline);
		}

		// // 围栏覆盖物
		// if (null != Geofence.fenceOverlay) {
		// mBaiduMap.addOverlay(Geofence.fenceOverlay);
		// }

		// 实时点覆盖物
		if (null != overlay){
			mBaiduMap.addOverlay(overlay);
		}

		mBaiduMap.addOverlay(tOption);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(myTOption);
		mBaiduMap.addOverlay(myMOption);
	}

	protected void startRefreshThread(boolean isStart){
		if (null == refreshThread){
			refreshThread = new RefreshThread();
		}
		refreshThread.refresh = isStart;
		if (isStart){
			if (!refreshThread.isAlive()){
				refreshThread.start();
			}
		}else{
			refreshThread = null;
		}
	}

	protected class RefreshThread extends Thread{

		protected boolean refresh = true;

		@Override
		public void run(){
			// TODO Auto-generated method stub

			while(refresh){
				// 查询实时轨迹
				queryRealtimeTrack();
				Log.e("查询实时轨迹", trace.getEntityName());
				try{
					Thread.sleep(gatherInterval * 1000);
				}catch(InterruptedException e){
					// TODO Auto-generated catch block
					System.out.println("线程休眠失败");
				}
			}

		}
	}

	/**
	 * 查询实时轨迹
	 * 
	 * @param v
	 */
	private void queryRealtimeTrack(){
//		client.queryRealtimeLoc(serviceId, entityListener);
		realTimeTrace();
	}

	private void showMessage(final String message, final Integer errorNo){
		new Handler(this.getMainLooper()).post(new Runnable(){
			public void run(){
				Toast.makeText(BaiDuMapTraceActivity.this, message,
						Toast.LENGTH_LONG).show();

				if (null != errorNo){
					if (0 == errorNo.intValue() || 10006 == errorNo.intValue()
							|| 10008 == errorNo.intValue()
							|| 10009 == errorNo.intValue()){
					}else if (1 == errorNo.intValue()
							|| 10004 == errorNo.intValue()){
					}
				}
			}
		});

	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
		isInUploadFragment = false;
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		client.onDestroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
//		if (pd == null){
//			pd = CustomProgressDialog.createDialog(this);
//			pd.setCancelable(false);
//			if (client != null && trace != null){
//				stopTrace();
//			}else{
//				pd.dismiss();
//				finish();
//			}
//
//		}
		isTraceStart = false;
		startRefreshThread(false);
		finish();
	}

	/**
	 * 停止轨迹服务
	 * 
	 */
	private void stopTrace(){
		// 通过轨迹服务客户端client停止轨迹服务
		client.stopTrace(trace, stopTraceListener);
	}
}
