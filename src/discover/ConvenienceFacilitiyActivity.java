/**
 * 
 * @param
 */
package discover;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.HttpClient;
import tools.PullToRefreshLayout;
import tools.ScrollViewListener;
import tools.PullToRefreshLayout.OnRefreshListener;
import view.ListViewForGrab;
import view.MGridView;
import view.PullableScrollView;
import adapter.ConvenienceFacilityGVAdapter;
import adapter.ConvenienceFacilityLvAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.LocationService;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class ConvenienceFacilitiyActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener{

	private ImageView back, toTop;
	private PullToRefreshLayout refresh;
	private PullableScrollView psv;
	private MGridView gv;
	private ImageView iv;
	private TextView tv;
	private ListViewForGrab lv;

	private ConvenienceFacilityGVAdapter gvAdapter;
	private ConvenienceFacilityLvAdapter lvAdapter;
	private ArrayList<HashMap<String, String>> dataList;

	private LocationService locationService;
	private String lat = "0", lng = "0", city = "", code, img, name;
	private int total = 0, count = 20;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.convenience_facility_main);
		new Handler().postDelayed(new Runnable(){

			@Override
			public void run(){
				// TODO Auto-generated method stub
				initLoc();
			}
		}, 300);
		initView();

	}

	private void initView(){
		back = (ImageView) findViewById(R.id.back);
		toTop = (ImageView) findViewById(R.id.convenience_backtotop);
		refresh = (PullToRefreshLayout) findViewById(R.id.convenience_refresh_view);
		psv = (PullableScrollView) findViewById(R.id.convenience_psv);
		psv.smoothScrollTo(0, 0);
		refresh.setOnRefreshListener(this);
		gv = (MGridView) findViewById(R.id.convenience_facility_item1_gv);
		iv = (ImageView) findViewById(R.id.convenience_facility_item2_pic);
		tv = (TextView) findViewById(R.id.convenience_facility_item2_tv);
		lv = (ListViewForGrab) findViewById(R.id.convenience_facility_item3_lv);

		name = getIntent().getExtras().getString("name");
		img = getIntent().getExtras().getString("img");
		if (name.equals("") && img.equals("")){
			iv.setImageResource(R.drawable.post_location);
			tv.setText("附近三公里");
		}else{
			ImageLoader.getInstance().displayImage(img, iv);
			tv.setText(name);
		}
		code = getIntent().getExtras().getString("code");
		dataList = new ArrayList<HashMap<String, String>>();
		lvAdapter = new ConvenienceFacilityLvAdapter(this, dataList);
		lv.setAdapter(lvAdapter);

		filterHttp("2");
		toTop.setOnClickListener(this);
		back.setOnClickListener(this);
		
		psv.setScrollViewListener(new ScrollViewListener(){
			
			@Override
			public void onScrollChanged(PullableScrollView scrollView, int x, int y,
					int oldx, int oldy){
				// TODO Auto-generated method stub
				if(y > 500){
					toTop.setVisibility(View.VISIBLE);
				}else{
					toTop.setVisibility(View.GONE);
				}
			}
		});
		
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
			city = arg0.getCity();
			lat = arg0.getLatitude() + "";
			lng = arg0.getLongitude() + "";
			System.out.println("lat ---> " + lat);
			System.out.println("lng ---> " + lng);
			locationService.unregisterListener(mListener);
			locationService.stop();
			listHttp(code, "f");
			// Message msg = handler.obtainMessage();
			// msg.what = 1;
			// msg.sendToTarget();
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

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					locationService.unregisterListener(mListener);
					locationService.stop();
					filterHttp("2");
					listHttp(code, "f");
				break;
				case 2:// 附近3公里筛选回调
					final ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) msg.obj;
					gvAdapter = new ConvenienceFacilityGVAdapter(
							ConvenienceFacilitiyActivity.this, list);
					gv.setAdapter(gvAdapter);
					gv.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id){
							// TODO Auto-generated method stub
							img = list.get(position).get("img");
							name = list.get(position).get("name");
							code = list.get(position).get("code");
							ImageLoader.getInstance().displayImage(img, iv);
							tv.setText(name);
							listHttp(code, "f");
						}
					});
				break;
				case 100:
					toTop.setVisibility(View.GONE);
				break;
				case 200:
					toTop.setVisibility(View.VISIBLE);
				break;
				default:
				break;
			}
		}

	};

	private void filterHttp(String type){
		JSONObject job = new JSONObject();

		try{
			job.put("type", type);
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.CONVENIENCE_FACILITY_FILTER_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("便民设施三公里筛选返回 ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("list");
										ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
										for(int i = 0, j = jArray.length(); i < j; i++){
											JSONObject job = jArray
													.optJSONObject(i);
											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put("code",
													job.getString("code"));
											hashMap.put("id",
													job.getString("id"));
											hashMap.put("img",
													job.getString("img"));
											hashMap.put("name",
													job.getString("name"));

											list.add(hashMap);
										}
										Message msg = handler.obtainMessage();
										msg.what = 2;
										msg.obj = list;
										msg.sendToTarget();

									}else{
										Toast.makeText(
												ConvenienceFacilitiyActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Toast.makeText(ConvenienceFacilitiyActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
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

	private void listHttp(String code, final String type){
		JSONObject job = new JSONObject();
		System.out.println("with lat ---> " + lat);
		System.out.println("with lng ---> " + lng);
		try{
			job.put("city", city);
			job.put("code", code);
			job.put("count", "" + count);
			job.put("total", "" + total);
			job.put("lat", lat);
			job.put("lng", lng);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.CONVENIENCE_FACILITY_FILTER_LIST_URL,
					se, new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("便民设施列表接口返回 ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("list");
										ArrayList<HashMap<String, String>> singleList = new ArrayList<HashMap<String, String>>();
										for(int i = 0, j = jArray.length(); i < j; i++){
											JSONObject job = jArray
													.optJSONObject(i);
											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put("address",
													job.getString("address"));
											hashMap.put("distance",
													job.getString("distance"));
											hashMap.put("grade",
													job.getString("grade"));
											hashMap.put("id",
													job.getString("id"));
											hashMap.put("img",
													job.getString("img"));
											hashMap.put("lat",
													job.getString("lat"));
											hashMap.put("lng",
													job.getString("lng"));
											hashMap.put("name",
													job.getString("name"));
											hashMap.put("phone",
													job.getString("phone"));
											singleList.add(hashMap);
										}
										if (type.equals("f")){
											dataList.clear();
											dataList.addAll(singleList);
										}else{
											dataList.addAll(singleList);
										}
										lvAdapter.notifyDataSetChanged();
									}else{
										Toast.makeText(
												ConvenienceFacilitiyActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Toast.makeText(ConvenienceFacilitiyActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
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
	 * 
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		// new Handler().postDelayed(new Runnable(){
		//
		// @Override
		// public void run(){
		// // TODO Auto-generated method stub
		// initLoc();
		// }
		// }, 100);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back:
				finish();
			break;
			case R.id.convenience_backtotop:
				psv.smoothScrollTo(0, 0);
			break;
			default:
			break;
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout){
		// TODO Auto-generated method stub
		// 下拉刷新操作
		new Handler(){
			@Override
			public void handleMessage(Message msg){
				total = 0;
				listHttp(code, "f");
				// 千万别忘了告诉控件刷新完毕了哦！
				pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout){
		// TODO Auto-generated method stub
		// 加载操作
		new Handler(){
			@Override
			public void handleMessage(Message msg){
				total += count;
				listHttp(code, "n");
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}
}
