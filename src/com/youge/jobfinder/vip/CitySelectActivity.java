/**
 * 
 * @param
 */
package com.youge.jobfinder.vip;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.OccupationPopUpWindow;
import progressdialog.CustomProgressDialog;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;

import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import tools.XmlParserHandler;

import model.CityModel;
import model.DistrictModel;
import model.ProvinceModel;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class CitySelectActivity extends BaseActivity implements
		OnWheelChangedListener, OnClickListener {

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();

	/**
	 * key - 区 values - 邮编
	 */
	protected Map<String, String> mZipcodeDatasMap = new HashMap<String, String>();

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	/**
	 * 当前区的邮政编码
	 */
	protected String mCurrentZipCode = "";

	private ImageView back;
	private TextView city, commit;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private String[] cities;

	private String sSex, sName, sOccupation, sCity, sBirthday, cityDate,
			sPhone;

	private boolean isFromUserResume = false;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_select);
		initView();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
		commit = (TextView) findViewById(R.id.title_tv);
		city = (TextView) findViewById(R.id.city_select_city);
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);

		sSex = getIntent().getExtras().getString("sex");
		sName = getIntent().getExtras().getString("name");
		sOccupation = getIntent().getExtras().getString("occupation");
		sBirthday = getIntent().getExtras().getString("birthday");
		cityDate = getIntent().getExtras().getString("city");
		sPhone = getIntent().getExtras().getString("phone");
		isFromUserResume = getIntent().getBooleanExtra("isFromUserResume",
				false);
		if (!TextUtils.isEmpty(cityDate)) {
			if (!"请填写现住地址".equals(cityDate)) {
				city.setText(cityDate);
			}
		}

		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);

		back.setOnClickListener(this);
		commit.setOnClickListener(this);

		initProvinceDatas();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(
				CitySelectActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		updateCities();
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = getAssets();
		try {
			InputStream input = asset.open("province_data.xml");
			// 创建一个解析xml的工厂对象
			SAXParserFactory spf = SAXParserFactory.newInstance();
			// 解析xml
			SAXParser parser = spf.newSAXParser();
			XmlParserHandler handler = new XmlParserHandler();
			parser.parse(input, handler);
			input.close();
			// 获取解析出来的数据
			provinceList = handler.getDataList();
			// */ 初始化默认选中的省、市、区
			if (provinceList != null && !provinceList.isEmpty()) {
				mCurrentProviceName = provinceList.get(0).getName();
				List<CityModel> cityList = provinceList.get(0).getCityList();
				if (cityList != null && !cityList.isEmpty()) {
					mCurrentCityName = cityList.get(0).getName();
					List<DistrictModel> districtList = cityList.get(0)
							.getDistrictList();
					mCurrentDistrictName = districtList.get(0).getName();
					mCurrentZipCode = districtList.get(0).getZipcode();
				}
			}
			// */
			mProvinceDatas = new String[provinceList.size()];
			for (int i = 0; i < provinceList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = provinceList.get(i).getName();
				List<CityModel> cityList = provinceList.get(i).getCityList();
				String[] cityNames = new String[cityList.size()];
				for (int j = 0; j < cityList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = cityList.get(j).getName();
					List<DistrictModel> districtList = cityList.get(j)
							.getDistrictList();
					String[] distrinctNameArray = new String[districtList
							.size()];
					DistrictModel[] distrinctArray = new DistrictModel[districtList
							.size()];
					for (int k = 0; k < districtList.size(); k++) {
						// 遍历市下面所有区/县的数据
						DistrictModel districtModel = new DistrictModel(
								districtList.get(k).getName(), districtList
										.get(k).getZipcode());
						// 区/县对于的邮编，保存到mZipcodeDatasMap
						mZipcodeDatasMap.put(districtList.get(k).getName(),
								districtList.get(k).getZipcode());
						distrinctArray[k] = districtModel;
						distrinctNameArray[k] = districtModel.getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(provinceList.get(i).getName(), cityNames);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {

		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);

		// String = MD5 + "-" + title + "1"
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			getCurrentCity();
		}

		city.setText(mCurrentProviceName + " " + mCurrentCityName);
	}

	private void getCurrentCity() {
		int cCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = cities[cCurrent];
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
			if (!isFromUserResume) {
				updateHttpClient(
						new Tools().getUserId(CitySelectActivity.this), city
								.getText().toString());
			}
			Intent intent = getIntent();
			intent.putExtra("city", city.getText().toString());
			setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.back:
			setResult(RESULT_CANCELED);
			finish();
			break;
		default:
			break;
		}
	}

	private boolean bool = false;

	private boolean updateHttpClient(String uid, String city) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(CitySelectActivity.this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("name", sName);
			job.put("sex", sSex);
			job.put("occupation", sOccupation);
			job.put("birthday", sBirthday);
			job.put("city", city);
			job.put("phone", sPhone);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(CitySelectActivity.this,
					Config.UPDATE_USER_INFO_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String str = new String(arg2);
							System.out.println("修改个人信息接口返回 ---> " + str);
							String message = "";
							try {
								JSONObject job = new JSONObject(str);
								String state = job.getString("state");
								message = job.getString("msg");
								if (state.equals("success")) {
									JSONObject data = job.getJSONObject("data");
									String result = data.getString("result");
									if (result.equals("1")) {
										bool = true;
									} else {
										bool = false;
										Toast.makeText(CitySelectActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(CitySelectActivity.this,
											message, Toast.LENGTH_SHORT).show();
									bool = false;
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(CitySelectActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
							bool = false;
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

		return bool;
	}
}
