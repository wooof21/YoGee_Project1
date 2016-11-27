/**
 * 
 * @param
 */
package popup;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ArrayWheelAdapter;
import model.CityModel;
import model.DistrictModel;
import model.ProvinceModel;
import tools.XmlParserHandler;
import android.R.integer;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.AddAddressActivity;

/**
 * 
 * @param
 */
public class AddAddressPopUpWindow extends PopupWindow implements
		OnWheelChangedListener, OnClickListener {

	private Context context;
	private String province, city, district;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;
	private LinearLayout address_ll;
	private TextView cancle, complete;
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

	public AddAddressPopUpWindow(Context context, View parent, String province,
			String city, String district) {
		View view = View.inflate(context, R.layout.add_address_popup, null);

		this.context = context;
		this.province = province;
		this.city = city;
		this.district = district;

		mViewProvince = (WheelView) view.findViewById(R.id.id_province);
		mViewCity = (WheelView) view.findViewById(R.id.id_city);
		mViewDistrict = (WheelView) view.findViewById(R.id.id_district);
		complete = (TextView) view.findViewById(R.id.complete);
		cancle = (TextView) view.findViewById(R.id.cancle);
		address_ll = (LinearLayout) view.findViewById(R.id.address_ll);
		address_ll.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.address_bottom_in));
		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);

		complete.setOnClickListener(this);
		cancle.setOnClickListener(this);
		initProvinceDatas();

		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context,
				mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();

		if (!TextUtils.isEmpty(province) && !TextUtils.isEmpty(city)
				&& !TextUtils.isEmpty(district)) {
			setAddressView();
		}

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(R.color.transparents));
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.TOP, 0, 0);
		update();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			updateAreas();
		} else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
			// mCurrentZipCode = mZipcodeDatasMap.get(mCurrentDistrictName);
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity
				.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context,
				areas));
		mViewDistrict.setCurrentItem(0);
		int nCurrent = mViewDistrict.getCurrentItem();
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[nCurrent];
	}

	/**
	 * 解析省市区的XML数据
	 */

	protected void initProvinceDatas() {
		List<ProvinceModel> provinceList = null;
		AssetManager asset = context.getAssets();
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.complete:
			Message msg = AddAddressActivity.instance.handler.obtainMessage();
			msg.what = 1;
			msg.obj = mCurrentProviceName + " " + mCurrentCityName + " "
					+ mCurrentDistrictName;
			msg.sendToTarget();
			dismiss();
			break;
		case R.id.cancle:
			dismiss();
			break;

		default:
			break;
		}
	}

	/**
	 * 回显
	 */
	private void setAddressView() {
		int prpvinceCount = 0;
		int citieCount = 0;
		int districtCount = 0;
		for (int i = 0, j = mProvinceDatas.length; i < j; i++) {
			prpvinceCount++;
			if (province.equals(mProvinceDatas[i])) {
				break;
			}
		}
		mViewProvince.setCurrentItem(prpvinceCount - 1);
		mCurrentProviceName = mProvinceDatas[prpvinceCount - 1];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity
				.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));

		for (int i = 0, j = cities.length; i < j; i++) {
			citieCount++;
			if (city.equals(cities[i])) {
				break;
			}
		}
		mViewCity.setCurrentItem(citieCount - 1);

		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[citieCount - 1];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		if (areas == null) {
			areas = new String[] { "" };
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context,
				areas));
		for (int i = 0, j = areas.length; i < j; i++) {
			districtCount++;
			if (district.equals(areas[i])) {
				break;
			}
		}
		mViewDistrict.setCurrentItem(districtCount - 1);
		int nCurrent = mViewDistrict.getCurrentItem();
		mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[nCurrent];
	}
}
