/**
 * 
 * @param
 */
package baidu;

import popup.ContentPopUpWindow;
import tools.Tools;
import android.R.integer;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;
import baidu.mapapi.overlayutil.DrivingRouteOverlay;
import baidu.mapapi.overlayutil.OverlayManager;
import baidu.mapapi.overlayutil.TransitRouteOverlay;
import baidu.mapapi.overlayutil.WalkingRouteOverlay;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
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
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.baidu.mapapi.utils.route.BaiduMapRoutePlan;
import com.baidu.mapapi.utils.route.RouteParaOption;
import com.baidu.mapapi.utils.route.RouteParaOption.EBusStrategyType;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager.RoutePlanListener;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.LocationService;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class BaiDuMapActivity extends BaseActivity implements
		OnGetRoutePlanResultListener, OnClickListener{

	private MapView mapView;
	private BaiduMap mBaiduMap;
	private double lat, myLat;
	private double lng, myLng;
	private String title;
	private LatLng endPoint, startPoint;

	private LocationService locationService;
	// 搜索相关
	private RoutePlanSearch mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private int nodeIndex = -1;// 节点索引,供浏览节点时使用
	private RouteLine route = null;
	private OverlayManager routeOverlay = null;
	private OverlayOptions tOption, mOption, myTOption, myMOption;

	private TextView drive, walk, bus, content;
	private String city, routeState;

	private LinearLayout navi;
	private RelativeLayout parent;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_map_view);
		initView();
		initLoc();
	}

	private void initView(){
		mapView = (MapView) findViewById(R.id.bmapView);
		drive = (TextView) findViewById(R.id.baidu_map_drive);
		walk = (TextView) findViewById(R.id.baidu_map_walk);
		bus = (TextView) findViewById(R.id.baidu_map_bus);
		content = (TextView) findViewById(R.id.baidu_map_content);
		navi = (LinearLayout) findViewById(R.id.baidu_map_navi);
		parent = (RelativeLayout) findViewById(R.id.baidu_map_parent);
		
		navi.setOnClickListener(this);
		drive.setOnClickListener(this);
		walk.setOnClickListener(this);
		bus.setOnClickListener(this);
		mBaiduMap = mapView.getMap();
		// 普通地图
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		// 初始化搜索模块，注册事件监听
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(this);

		// 卫星地图
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(false);

		title = getIntent().getExtras().getString("title", "");
		if (title.equals("")){
			title = "订单发布位置";
		}
		lat = getIntent().getDoubleExtra("lat", 0.0);
		lng = getIntent().getDoubleExtra("lng", 0.0);
		Log.e("lat", "" + lat);
		Log.e("lng", "" + lng);
		endPoint = new LatLng(lat, lng);

		MapStatus mMapStatus = new MapStatus.Builder().target(endPoint)
				.zoom(19).build();
		MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
				.newMapStatus(mMapStatus);
		mBaiduMap.setMapStatus(mMapStatusUpdate);

		// 构建文字Option对象，用于在地图上添加文字
		tOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(18)
				.fontColor(0xFFFF00FF).text(title).rotate(10)
				.position(endPoint);
		// 在地图上添加该文字对象并显示
		// 构建Marker图标
		BitmapDescriptor bitmap = BitmapDescriptorFactory
				.fromResource(R.drawable.icon_gcoding);
		// 构建MarkerOption，用于在地图上添加Marker
		mOption = new MarkerOptions().position(endPoint).icon(bitmap);
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(tOption);
		mBaiduMap.addOverlay(mOption);
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener(){

			@Override
			public boolean onMarkerClick(Marker arg0){
				// TODO Auto-generated method stub
				Log.e("marker", "clicked");
				return false;
			}
		});
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			startPoint = new LatLng(myLat, myLng);

			// 构建文字Option对象，用于在地图上添加文字
			myTOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(18)
					.fontColor(0xFFFF00FF).text("我的位置").rotate(10)
					.position(startPoint);
			// 在地图上添加该文字对象并显示
			// 构建Marker图标
			BitmapDescriptor bitmap = BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding_own_loc);
			// 构建MarkerOption，用于在地图上添加Marker
			myMOption = new MarkerOptions().position(startPoint).icon(bitmap);
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
			locationService.unregisterListener(mListener); // 注销掉监听
			locationService.stop(); // 停止定位服务

			drawDriveRoute(startPoint, endPoint);
		}

	};

	private void drawDriveRoute(LatLng sp, LatLng ep){
		mBaiduMap.clear();
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(tOption);
		mBaiduMap.addOverlay(mOption);
		mBaiduMap.addOverlay(myTOption);
		mBaiduMap.addOverlay(myMOption);
		routeState = "drive";
		PlanNode sNode = PlanNode.withLocation(sp);
		PlanNode eNode = PlanNode.withLocation(ep);
		mSearch.drivingSearch((new DrivingRoutePlanOption()).from(sNode).to(
				eNode));
	}

	private void drawWalkRoute(LatLng sp, LatLng ep){
		mBaiduMap.clear();
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(tOption);
		mBaiduMap.addOverlay(mOption);
		mBaiduMap.addOverlay(myTOption);
		mBaiduMap.addOverlay(myMOption);
		routeState = "walk";
		PlanNode sNode = PlanNode.withLocation(sp);
		PlanNode eNode = PlanNode.withLocation(ep);
		mSearch.walkingSearch((new WalkingRoutePlanOption()).from(sNode).to(
				eNode));
	}

	private void drawBusRoute(LatLng sp, LatLng ep){
		mBaiduMap.clear();
		// 在地图上添加Marker，并显示
		mBaiduMap.addOverlay(tOption);
		mBaiduMap.addOverlay(mOption);
		mBaiduMap.addOverlay(myTOption);
		mBaiduMap.addOverlay(myMOption);
		routeState = "bus";
		PlanNode sNode = PlanNode.withLocation(sp);
		PlanNode eNode = PlanNode.withLocation(ep);
		mSearch.transitSearch((new TransitRoutePlanOption()).from(sNode)
				.city(city).to(eNode));
	}

	class routePlanListener implements RoutePlanListener{

		private BNRoutePlanNode mBNRoutePlanNode = null;

		public routePlanListener(BNRoutePlanNode node){
			mBNRoutePlanNode = node;
		}

		/**
		 * 
		 * @param
		 */
		@Override
		public void onJumpToNavigator(){
			// TODO Auto-generated method stub

		}

		/**
		 * 
		 * @param
		 */
		@Override
		public void onRoutePlanFailed(){
			// TODO Auto-generated method stub

		}

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

	private BDLocationListener mListener = new BDLocationListener(){

		@Override
		public void onReceiveLocation(BDLocation arg0){
			// TODO Auto-generated method stub
			myLat = arg0.getLatitude();
			myLng = arg0.getLongitude();
			city = arg0.getCity();
			System.out.println("myLat ---> " + myLat);
			System.out.println("myLng ---> " + myLng);

			Message msg = handler.obtainMessage();
			msg.sendToTarget();
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
	private int distance;
	private int duration;

	@Override
	protected void onDestroy(){
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onStop(){
		// TODO Auto-generated method stub
		super.onStop();
		try{
			locationService.unregisterListener(mListener); // 注销掉监听
			locationService.stop(); // 停止定位服务
		}catch(IllegalArgumentException e){
			Log.e("IllegalArgumentException", e.toString());
		}
	}

	@Override
	protected void onResume(){
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapView.onResume();
	}

	@Override
	protected void onPause(){
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result){
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
			Toast.makeText(BaiDuMapActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR){
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR){
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
			routeOverlay = overlay;
			mBaiduMap.setOnMarkerClickListener(overlay);
			overlay.setData(result.getRouteLines().get(0));
			distance = result.getRouteLines().get(0).getDistance();
			duration = result.getRouteLines().get(0).getDuration();
			overlay.addToMap();
			overlay.zoomToSpan();
			content.setText("距离约 " + distance + "米" + "\n" + "驾车约 "
					+ new Tools().convertSec(duration) + "秒");
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result){
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
			Toast.makeText(BaiDuMapActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR){
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR){
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			TransitRouteOverlay overlay = new MyTransitRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			distance = result.getRouteLines().get(0).getDistance();
			duration = result.getRouteLines().get(0).getDuration();
			System.out.println("bus route getDistance "
					+ result.getRouteLines().get(0).getDistance());
			System.out.println("bus route getDuration "
					+ result.getRouteLines().get(0).getDuration());
			System.out.println("bus route getTitle "
					+ result.getRouteLines().get(0).getTitle());
			System.out.println("bus route describeContents "
					+ result.getRouteLines().get(0).describeContents());
			System.out.println("bus route getTaxitInfo "
					+ result.getRouteLines().get(0).getTaxitInfo());
			System.out.println(" getStarting describeContents ---> "
					+ route.getStarting().describeContents());
			System.out.println(" getTerminal describeContents ---> "
					+ route.getTerminal().describeContents());
			System.out.println(" getStarting getTitle ---> "
					+ route.getStarting().getTitle());
			System.out.println(" getTerminal getTitle ---> "
					+ route.getTerminal().getTitle());
			overlay.addToMap();
			overlay.zoomToSpan();//
			content.setText("距离约 " + distance + "米" + "\n" + "乘车约 "
					+ new Tools().convertSec(duration) + "秒");
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result){
		// TODO Auto-generated method stub
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR){
			Toast.makeText(BaiDuMapActivity.this, "抱歉，未找到结果",
					Toast.LENGTH_SHORT).show();
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR){
			// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
			// result.getSuggestAddrInfo()
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR){
			nodeIndex = -1;
			route = result.getRouteLines().get(0);
			WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(mBaiduMap);
			mBaiduMap.setOnMarkerClickListener(overlay);
			routeOverlay = overlay;
			overlay.setData(result.getRouteLines().get(0));
			distance = result.getRouteLines().get(0).getDistance();
			duration = result.getRouteLines().get(0).getDuration();
			overlay.addToMap();
			overlay.zoomToSpan();
			content.setText("距离约 " + distance + "米" + "\n" + "步行约 "
					+ new Tools().convertSec(duration) + "秒");
		}
	}

	// 定制RouteOverly
	private class MyDrivingRouteOverlay extends DrivingRouteOverlay{

		public MyDrivingRouteOverlay(BaiduMap baiduMap){
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding_own_loc);
		}

		@Override
		public BitmapDescriptor getTerminalMarker(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding);
		}
	}

	private class MyWalkingRouteOverlay extends WalkingRouteOverlay{

		public MyWalkingRouteOverlay(BaiduMap baiduMap){
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding_own_loc);
		}

		@Override
		public BitmapDescriptor getTerminalMarker(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding);
		}
	}

	private class MyTransitRouteOverlay extends TransitRouteOverlay{

		public MyTransitRouteOverlay(BaiduMap baiduMap){
			super(baiduMap);
		}

		@Override
		public BitmapDescriptor getStartMarker(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding_own_loc);
		}

		@Override
		public BitmapDescriptor getTerminalMarker(){
			return BitmapDescriptorFactory
					.fromResource(R.drawable.icon_gcoding);
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.baidu_map_drive:
				if (!routeState.equals("drive")){
					resetBg();
					drawDriveRoute(startPoint, endPoint);
					drive.setBackgroundResource(R.drawable.corner_green);
					drive.setTextColor(Color.rgb(255, 255, 255));
				}
			break;
			case R.id.baidu_map_walk:
				if (!routeState.equals("walk")){
					resetBg();
					drawWalkRoute(startPoint, endPoint);
					walk.setBackgroundResource(R.drawable.corner_green);
					walk.setTextColor(Color.rgb(255, 255, 255));
				}
			break;
			case R.id.baidu_map_bus:
				if (!routeState.equals("bus")){
					resetBg();
					drawBusRoute(startPoint, endPoint);
					bus.setBackgroundResource(R.drawable.corner_green);
					bus.setTextColor(Color.rgb(255, 255, 255));
				}
			break;
			case R.id.baidu_map_navi:
				new ContentPopUpWindow(BaiDuMapActivity.this, parent, routeState, handler2);
			break;
			default:
			break;
		}
	}
	
	private Handler handler2 = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					startRoutePlanDriving();
				break;
				case 2:
					startRoutePlanWalking();
					break;
				case 3:
					startRoutePlanTransit();
					break;
				default:
				break;
			}
		}
		
	};

	/**
	 * 启动百度地图驾车路线规划
	 */
	private void startRoutePlanDriving(){

		// 构建 route搜索参数
		RouteParaOption para = new RouteParaOption().startPoint(startPoint)
				.startName("我的位置").endPoint(endPoint).endName(title)
				.cityName(city);
		try{
			BaiduMapRoutePlan.openBaiduMapDrivingRoute(para, this);
		}catch(Exception e){
			e.printStackTrace();
			showInstallBaiduMapDialog();
		}

	}

	/**
	 * 启动百度地图公交路线规划
	 */
	private void startRoutePlanTransit(){

		// 构建 route搜索参数
		RouteParaOption para = new RouteParaOption().startPoint(startPoint)
				.startName("我的位置").endPoint(endPoint).endName(title)
				.busStrategyType(EBusStrategyType.bus_recommend_way);

		try{
			BaiduMapRoutePlan.openBaiduMapTransitRoute(para, this);
		}catch(Exception e){
			e.printStackTrace();
			showInstallBaiduMapDialog();
		}

	}

	/**
	 * 启动百度地图步行路线规划
	 */
	private void startRoutePlanWalking(){

		// 构建 route搜索参数
		RouteParaOption para = new RouteParaOption().startPoint(startPoint)
				.startName("我的位置").endPoint(endPoint).endName(title)
				.cityName(city);

		try{
			BaiduMapRoutePlan.openBaiduMapWalkingRoute(para, this);
		}catch(Exception e){
			e.printStackTrace();
			showInstallBaiduMapDialog();
		}

	}

	/**
	 * 提示未安装百度地图app或app版本过低
	 */
	private void showInstallBaiduMapDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("您尚未安装百度地图app或app版本过低，点击确认安装？");
		builder.setTitle("提示");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
				OpenClientUtil.getLatestBaiduMapApp(BaiDuMapActivity.this);
			}
		});

		builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which){
				dialog.dismiss();
			}
		});

		builder.create().show();

	}

	private void resetBg(){
		drive.setBackgroundResource(R.drawable.corner_green_stroke);
		drive.setTextColor(Color.rgb(16, 17, 25));
		walk.setBackgroundResource(R.drawable.corner_green_stroke);
		walk.setTextColor(Color.rgb(16, 17, 25));
		bus.setBackgroundResource(R.drawable.corner_green_stroke);
		bus.setTextColor(Color.rgb(16, 17, 25));
	}
}
