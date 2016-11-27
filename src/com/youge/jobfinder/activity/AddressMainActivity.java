/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;

import model.Address;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.LocationService;
import com.youge.jobfinder.R;

import adapter.AddressLVItemAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;

/**
 * 
 * @param
 */
public class AddressMainActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back, refresh;
	private ListView lv;
	private FrameLayout add, frame;
	private TextView title_tv, loc;

	private ArrayList<Address> list;
	private AddressLVItemAdapter adapter;

	private LocationService locationService;
	private String from;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.address_main_lv);
		initView();
		AddressHttpClient(new Tools().getUserId(this));
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initLoc();
			}
		}, 500);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void initView() {
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		lv = (ListView) findViewById(R.id.address_main_lv_lv);
		add = (FrameLayout) findViewById(R.id.address_main_lv_add);
		title_tv = (TextView) findViewById(R.id.title_tv);
		refresh = (ImageView) findViewById(R.id.fill_order_refresh);
		loc = (TextView) findViewById(R.id.fill_order_current_loc);
		frame = (FrameLayout) findViewById(R.id.fill_order_frame);

		back.setOnClickListener(this);
		add.setOnClickListener(this);
		title_tv.setOnClickListener(this);
		refresh.setOnClickListener(this);

		from = getIntent().getStringExtra("from");
		if (TextUtils.isEmpty(from)) {
			from = "";
		}
		// from = getIntent().getExtras().getString("from", "0");

		list = new ArrayList<Address>();
		adapter = new AddressLVItemAdapter(this, list);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				System.out.println("position ---> " + position);
				if (from.equals("creditmall")) {
					Intent intent = getIntent();
					intent.putExtra("addrId", list.get(position).getId());
					setResult(RESULT_OK, intent);
					AddressMainActivity.this.finish();
				} else {
					for (int i = 0, j = list.size(); i < j; i++) {
						list.get(i).setIsSelected("0");
					}
					list.get(position).setIsSelected("1");
					adapter.notifyDataSetChanged();
					SharedPreferences sharedPre = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sharedPre.edit();
					editor.putString("addressMainAddress", list.get(position)
							.getAddress());
					editor.putString("addressMainPhone", list.get(position)
							.getPhone());
					editor.putString("addressMainName", list.get(position)
							.getName());
					editor.putString("addressMainSex", list.get(position)
							.getSex());
					editor.commit();
					selectAddrHttpClient(list.get(position).getId(),
							new Tools().getUserId(AddressMainActivity.this));
				}
			}
		});
	}

	private void selectAddrHttpClient(String id, String uid) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try {
			job.put("id", id);
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.SELECT_USER_ADDRESS_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("更改默认地址接口返回 ---> " + str);
								setResult(RESULT_OK);
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(AddressMainActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_tv:
			Intent intentss = getIntent();
			setResult(RESULT_OK, intentss);
			finish();
			break;
		case R.id.back:
			if (from.equals("creditmall")) {
				setResult(RESULT_CANCELED);
				finish();
			} else {
				Intent intents = getIntent();
				setResult(RESULT_OK, intents);
				finish();
			}
			break;
		case R.id.address_main_lv_add:
			Intent intent = new Intent(AddressMainActivity.this,
					AddAddressActivity.class);
			intent.putExtra("id", "0");
			intent.putExtra("name", "");
			intent.putExtra("phone", "");
			intent.putExtra("address", "");
			intent.putExtra("sex", "1");
			startActivityForResult(intent, 100);
			break;
		case R.id.fill_order_refresh:
			roate(refresh);
			frame.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					initLoc();
					frame.setVisibility(View.GONE);
					refresh.clearAnimation();
				}
			}, 2900);
			break;
		default:
			break;
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			loc.setText((String) msg.obj);
			locationService.unregisterListener(mListener);
			locationService.stop();
		}

	};

	private void initLoc() {
		// -----------location config ------------
//		locationService = new LocationService(this);
		locationService = ((MyApplication) getApplication()).locationService;
		locationService.setLocationOption(locationService
				.getDefaultLocationClientOption());
		// 获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
		locationService.registerListener(mListener);
		locationService.start();// 定位SDK
		// start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request

	}

	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			Message msg = handler.obtainMessage();
			msg.obj = arg0.getLocationDescribe();
			msg.sendToTarget();
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

	private void roate(View view) {
		RotateAnimation animation = new RotateAnimation(0f, -360f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(3000);// 设置动画持续时间
		animation.setRepeatCount(6);// 设置重复次数
		animation.setRepeatMode(Animation.REVERSE);
		animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
		LinearInterpolator lin = new LinearInterpolator();
		animation.setInterpolator(lin);
		/** 开始动画 */
		view.startAnimation(animation);
	}

	/***
	 * Stop location service
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		// unregisterReceiver(receiver);
		try {
			locationService.unregisterListener(mListener); // 注销掉监听
			locationService.stop(); // 停止定位服务
		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onStop();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				list.clear();
				list.addAll((ArrayList<Address>) data.getExtras()
						.getSerializable("list"));
				adapter.notifyDataSetChanged();
			}
			break;
		case 200: // 地址修改回调
			if (resultCode == RESULT_OK) {
				list.clear();
				list.addAll((ArrayList<Address>) data.getExtras()
						.getSerializable("list"));
				adapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	private void AddressHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.FIND_USER_ADDRESS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("查看用户地址信息 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONArray aList = data
												.getJSONArray("list");

										for (int i = 0, j = aList.length(); i < j; i++) {
											JSONObject addr = aList
													.optJSONObject(i);
											Address addre = new Address();
											addre.setAddress(addr
													.getString("address"));
											addre.setId(addr.getString("id"));
											addre.setName(addr
													.getString("name"));
											addre.setPhone(addr
													.getString("phone"));
											addre.setSex(addr.getString("sex"));
											addre.setIsSelected(addr
													.getString("isSelected"));
											list.add(addre);
										}
										adapter.notifyDataSetChanged();

									} else {
										Toast.makeText(
												AddressMainActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(AddressMainActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}
	}
}
