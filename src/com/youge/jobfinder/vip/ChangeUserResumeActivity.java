package com.youge.jobfinder.vip;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import model.Certification;
import model.Experience;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.DatePickerPopUpwindow;
import popup.EducationPopUpWindow;
import popup.OccupationPopUpWindow;
import popup.OrderPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.MD5Util;
import tools.PictureUtil;
import tools.Tools;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import application.MyApplication;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.LocationService;
import com.youge.jobfinder.PrivacyPolicyActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.SchoolSecletActivity;
import com.youge.jobfinder.activity.ExperienceMainActivity;

public class ChangeUserResumeActivity extends BaseActivity implements
		OnClickListener {

	private TextView tv_resume_sexman, tv_resume_sexwoman, tv_resume_birth,
			tv_resume_occupation, tv_resume_address, tv_resume_education,
			tv_resume_skill, title_tv, skillTextOne, skillTextTwo,
			skillTextThree, skillTextFour, skillTextFive, tv_resume_school,
			tv_resume_experience, privacy_policy;
	private Drawable sex_man_clickable, sex_man_unclickable,
			sex_woman_clickable, sex_woman_unclickable;
	private EditText tv_resume_name, tv_resume_height, tv_resume_weight,
			tv_resume_speciality, tv_resume_phone, tv_resume_email;
	private String selDate = "";
	private String occupation = "";
	private String education = "";
	private String sCity;
	private String sex_choose = "1";
	private File pic;
	private String isFromExamine = "0";

	private ArrayList<String[]> skillImgList;
	/**
	 * 手机号码 移动：134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188
	 * 联通：130,131,132,152,155,156,185,186 电信：133,1349,153,180,189
	 */
	private String mobile = "^1(3[0-9]|5[0-35-9]|8[025-9])\\d{8}$";
	/**
	 * 10 * 中国移动：China Mobile 11 *
	 * 134[0-8],135,136,137,138,139,150,151,157,158,159,182,187,188 12
	 */
	private String CM = "^1(34[0-8]|(3[5-9]|5[017-9]|8[0-9]|7[0-9])\\d)\\d{7}$";
	/**
	 * 15 * 中国联通：China Unicom 16 * 130,131,132,152,155,156,185,186 17
	 */
	private String CU = "^1(3[0-2]|5[256]|8[56])\\d{8}$";
	/**
	 * 20 * 中国电信：China Telecom 21 * 133,1349,153,180,189 22
	 */
	private String CT = "^1((33|53|8[09])[0-9]|349)\\d{7}$";

	private String email_c = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}";

	// 出生年月
	private FrameLayout fv_resume_birth, fv_resume_occupation,
			fv_resume_address, fv_resume_education, fv_resume_skill,
			fv_resume_label, fl_resume_school, fl_resume_experience;

	private ImageView iv_resume_labelOne, iv_resume_labelTwo,
			iv_resume_labelThree, add_user_photo, back, skillImgOne,
			skillImgTwo, skillImgThree, skillImgFour, skillImgFive;

	private LinearLayout parent, lv_resume_label, container;

	private ArrayList<String> labelData;
	// private ArrayList<Certification> skillData;
	private int userImgListCount = 0;

	// private Map<String, String> mSelectPath;
	private Map<String, File> listFile;// 文件集
	private Map<String, String> listMD5;// MD5集
	public Handler mHandler;
	/** 消息标识 日期选择正确 */
	protected static final int RESUME_BIRTH_SUCCESS = 1;
	/** 消息标识 日期选择错误 */
	protected static final int RESUME_BIRTH_FAILED = 2;
	/** 消息标识 职业选择 */
	protected static final int RESUME_OCCUPATION = 3;
	/** 消息标识 学历选择 */
	protected static final int RESUME_EDUCATION = 4;
	/** 消息标识标签选择 */
	protected static final int LABEL_OK = 5;
	/** 消息标识照片选择 */
	protected static final int PHOTO_OK = 6;
	/** 消息标识技能证书选择 */
	protected static final int SKILL_OK = 7;

	private static final int REQUEST_IMAGE = 300;

	public static ChangeUserResumeActivity instance;

	// 简历数据
	private Object[] dataObj;

	private String resumeId;

	private InputMethodManager inputManager;

	private int skillImgListCount;

	private ArrayList<Certification> skillImgLists;

	private ArrayList<Experience> experienceList;

	private int first;
	private LocationService locationService;
	private String lat = "0", lon = "0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resume_edit);
		instance = this;
		findView();
		initView();
		if (dataObj == null) {
			System.out.println("1111111");
			first = 1;
		} else {
			System.out.println("2222222");
			initData();
			first = 2;
		}
		handMessage();
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

	private void initLoc() {
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

	private BDLocationListener mListener = new BDLocationListener() {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			lat = "" + arg0.getLatitude();
			lon = "" + arg0.getLongitude();
			System.out.println("lat ---> " + lat);
			System.out.println("lon ---> " + lon);
			locationService.unregisterListener(mListener); // 注销掉监听
			locationService.stop(); // 停止定位服务
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

	private void updateLatLon(String uid, String lat, String lon) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("lat", lat);
			job.put("lng", lon);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.UPDATE_LATLON_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("坐标提交接口返回 ---> " + str);
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(ChangeUserResumeActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		// 姓名
		tv_resume_name = (EditText) findViewById(R.id.tv_resume_name);
		// 身高
		tv_resume_height = (EditText) findViewById(R.id.tv_resume_height);
		// 体重
		tv_resume_weight = (EditText) findViewById(R.id.tv_resume_weight);
		// 特长
		tv_resume_speciality = (EditText) findViewById(R.id.tv_resume_speciality);
		// 学校
		fl_resume_school = (FrameLayout) findViewById(R.id.fl_resume_school);
		tv_resume_school = (TextView) findViewById(R.id.tv_resume_school);
		// 工作经验
		fl_resume_experience = (FrameLayout) findViewById(R.id.fl_resume_experience);
		tv_resume_experience = (TextView) findViewById(R.id.tv_resume_experience);
		// 电话
		tv_resume_phone = (EditText) findViewById(R.id.tv_resume_phone);
		// 邮件
		tv_resume_email = (EditText) findViewById(R.id.tv_resume_email);
		tv_resume_sexman = (TextView) findViewById(R.id.tv_resume_sexman);
		tv_resume_sexwoman = (TextView) findViewById(R.id.tv_resume_sexwoman);
		fv_resume_birth = (FrameLayout) findViewById(R.id.fv_resume_birth);
		// 出生年月
		tv_resume_birth = (TextView) findViewById(R.id.tv_resume_birth);
		parent = (LinearLayout) findViewById(R.id.resume_edit_parent);
		fv_resume_occupation = (FrameLayout) findViewById(R.id.fv_resume_occupation);
		// 职位
		tv_resume_occupation = (TextView) findViewById(R.id.tv_resume_occupation);
		fv_resume_address = (FrameLayout) findViewById(R.id.fv_resume_address);
		// 现住地
		tv_resume_address = (TextView) findViewById(R.id.tv_resume_address);
		fv_resume_education = (FrameLayout) findViewById(R.id.fv_resume_education);
		// 学历
		tv_resume_education = (TextView) findViewById(R.id.tv_resume_education);
		fv_resume_skill = (FrameLayout) findViewById(R.id.fv_resume_skill);
		tv_resume_skill = (TextView) findViewById(R.id.tv_resume_skill);
		fv_resume_label = (FrameLayout) findViewById(R.id.fv_resume_label);
		lv_resume_label = (LinearLayout) findViewById(R.id.lv_resume_label);
		iv_resume_labelOne = (ImageView) findViewById(R.id.iv_resume_labelOne);
		iv_resume_labelTwo = (ImageView) findViewById(R.id.iv_resume_labelTwo);
		iv_resume_labelThree = (ImageView) findViewById(R.id.iv_resume_labelThree);
		add_user_photo = (ImageView) findViewById(R.id.add_user_photo);
		container = (LinearLayout) findViewById(R.id.container);
		// 保存
		title_tv = (TextView) findViewById(R.id.title_tv);
		back = (ImageView) findViewById(R.id.back);
		skillTextOne = (TextView) findViewById(R.id.skillTextOne);
		skillTextTwo = (TextView) findViewById(R.id.skillTextTwo);
		skillTextThree = (TextView) findViewById(R.id.skillTextThree);
		skillTextFour = (TextView) findViewById(R.id.skillTextFour);
		skillTextFive = (TextView) findViewById(R.id.skillTextFive);
		skillImgOne = (ImageView) findViewById(R.id.skillImgOne);
		skillImgTwo = (ImageView) findViewById(R.id.skillImgTwo);
		skillImgThree = (ImageView) findViewById(R.id.skillImgThree);
		skillImgFour = (ImageView) findViewById(R.id.skillImgFour);
		skillImgFive = (ImageView) findViewById(R.id.skillImgFive);
		privacy_policy = (TextView) findViewById(R.id.privacy_policy);

	}

	/**
	 * 初始化控件 添加点击事件
	 */
	private void initView() {
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		labelData = new ArrayList<String>();
		skillImgList = new ArrayList<String[]>();
		skillImgLists = new ArrayList<Certification>();
		experienceList = new ArrayList<Experience>();
		listFile = new HashMap<String, File>();
		listMD5 = new HashMap<String, String>();//
		dataObj = (Object[]) getIntent().getSerializableExtra("userresume");
		isFromExamine = (String) getIntent().getStringExtra("isFromExamine");

		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		/*
		 * labelData = (ArrayList<String>) getIntent().getSerializableExtra(
		 * "labelData");
		 */
		tv_resume_sexman.setOnClickListener(this);
		tv_resume_sexwoman.setOnClickListener(this);
		fv_resume_birth.setOnClickListener(this);
		fv_resume_occupation.setOnClickListener(this);
		fv_resume_address.setOnClickListener(this);
		fv_resume_education.setOnClickListener(this);
		fv_resume_skill.setOnClickListener(this);
		fv_resume_label.setOnClickListener(this);
		fl_resume_school.setOnClickListener(this);
		add_user_photo.setOnClickListener(this);
		fl_resume_experience.setOnClickListener(this);
		title_tv.setOnClickListener(this);
		back.setOnClickListener(this);
		privacy_policy.setOnClickListener(this);
		sex_man_clickable = getResources().getDrawable(
				R.drawable.sex_man_choose1);
		sex_man_unclickable = getResources().getDrawable(
				R.drawable.sex_man_choose2);
		sex_woman_clickable = getResources().getDrawable(
				R.drawable.sex_woman_choose1);
		sex_woman_unclickable = getResources().getDrawable(
				R.drawable.sex_woman_choose2);
	}

	/**
	 * 初始化页面数据
	 */
	private void initData() {
		HashMap<String, String> hashMap = (HashMap<String, String>) dataObj[0];

		resumeId = hashMap.get("id");
		tv_resume_name.setText(hashMap.get("name"));
		String sexString = hashMap.get("sex");
		if ("1".equals(sexString)) {
			tv_resume_sexman.setBackgroundDrawable(sex_man_clickable);
			tv_resume_sexwoman.setBackgroundDrawable(sex_woman_unclickable);
			updateColor(tv_resume_sexman, tv_resume_sexwoman);
			sex_choose = "1";
		} else if ("2".equals(sexString)) {
			tv_resume_sexman.setBackgroundDrawable(sex_man_unclickable);
			tv_resume_sexwoman.setBackgroundDrawable(sex_woman_clickable);
			updateColor(tv_resume_sexwoman, tv_resume_sexman);
			sex_choose = "2";
		}
		tv_resume_birth.setText(hashMap.get("birthday"));
		tv_resume_height.setText(hashMap.get("height"));
		tv_resume_weight.setText(hashMap.get("weight"));
		tv_resume_speciality.setText(hashMap.get("speciality"));
		tv_resume_occupation.setText(hashMap.get("occupation"));
		tv_resume_address.setText(hashMap.get("address"));
		tv_resume_education.setText(hashMap.get("education"));
		tv_resume_school.setText(hashMap.get("school"));
		tv_resume_phone.setText(hashMap.get("phone"));
		tv_resume_email.setText(hashMap.get("email"));
		String lable = (String) hashMap.get("label");
		if (!TextUtils.isEmpty(lable)) {
			String[] str = lable.split("\\,");
			if (str.length == 1) {
				setVisible(iv_resume_labelOne);
				labelImg(iv_resume_labelOne, str[0]);
				labelData.add(str[0]);
			} else if (str.length == 2) {
				setVisible(iv_resume_labelOne);
				setVisible(iv_resume_labelTwo);
				labelImg(iv_resume_labelOne, str[0]);
				labelImg(iv_resume_labelTwo, str[1]);
				labelData.add(str[0]);
				labelData.add(str[1]);
			} else if (str.length == 3) {
				setVisible(iv_resume_labelOne);
				setVisible(iv_resume_labelTwo);
				setVisible(iv_resume_labelThree);
				labelImg(iv_resume_labelOne, str[0]);
				labelImg(iv_resume_labelTwo, str[1]);
				labelImg(iv_resume_labelThree, str[2]);
				labelData.add(str[0]);
				labelData.add(str[1]);
				labelData.add(str[2]);
			}
		}
		ArrayList<String> userImgList = (ArrayList<String>) dataObj[1];
		userImgListCount = userImgList.size();
		container.removeAllViews();
		for (int i = 0, j = userImgList.size(); i < j; i++) {
			showPic(userImgList.get(i), false);
		}
		if (container.getChildCount() >= 3) {
			add_user_photo.setVisibility(View.GONE);
		}
		skillImgList = (ArrayList<String[]>) dataObj[2];
		if (skillImgList != null) {
			int countSkill = skillImgList.size();
			skillImgListCount = countSkill;
			for (int i = 0; i < countSkill; i++) {
				setVisibleSkillImg(skillImgList.get(i)[0],
						skillImgList.get(i)[1], i + 1);
			}
			String skillName = "";
			for (int i = 0, j = skillImgList.size(); i < j; i++) {
				String name = skillImgList.get(i)[0];
				skillName = skillName + "(" + (i + 1 + "") + ")、" + name
						+ "    ";
			}
			tv_resume_skill.setText(skillName);
		}
		experienceList = (ArrayList<Experience>) dataObj[3];
		if (experienceList != null) {
			String text = "";
			for (int i = 0, j = experienceList.size(); i < j; i++) {
				if (i == 0) {
					text = experienceList.get(i).getStartDate() + "—"
							+ experienceList.get(i).getEndDate() + "\n"
							+ experienceList.get(i).getEmployer() + "\n"
							+ experienceList.get(i).getObligation();
				} else {
					text = text + "\n\n" + experienceList.get(i).getStartDate()
							+ "—" + experienceList.get(i).getEndDate() + "\n"
							+ experienceList.get(i).getEmployer() + "\n"
							+ experienceList.get(i).getObligation();
				}
			}

			tv_resume_experience.setText(text);
		}

	}

	/**
	 * 设置隐藏skill图片
	 */
	private void setVisibleSkillImg(String title, String path, int num) {
		if (num == 1) {
			skillTextOne.setText(title);
			ImageLoader.getInstance().displayImage(path, skillImgOne);
		} else if (num == 2) {
			skillTextTwo.setText(title);
			ImageLoader.getInstance().displayImage(path, skillImgTwo);
		} else if (num == 3) {
			skillTextThree.setText(title);
			ImageLoader.getInstance().displayImage(path, skillImgThree);
		} else if (num == 4) {
			skillTextFour.setText(title);
			ImageLoader.getInstance().displayImage(path, skillImgFour);
		} else if (num == 5) {
			skillTextFive.setText(title);
			ImageLoader.getInstance().displayImage(path, skillImgFive);
		}
	}

	/**
	 * 处理消息
	 * 
	 */
	private void handMessage() {
		mHandler = new Handler(new Handler.Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case RESUME_BIRTH_SUCCESS:
					selDate = (String) msg.obj;
					tv_resume_birth.setText(selDate);
					break;
				case RESUME_BIRTH_FAILED:
					Toast.makeText(ChangeUserResumeActivity.this,
							"不能选择今后的日期,请重新选择!", Toast.LENGTH_SHORT).show();
					break;
				case RESUME_OCCUPATION:
					occupation = (String) msg.obj;
					tv_resume_occupation.setText(occupation);
					break;
				case RESUME_EDUCATION:
					education = (String) msg.obj;
					tv_resume_education.setText(education);
					break;
				case PHOTO_OK:
					ArrayList<String> rSelectPath = (ArrayList<String>) msg.obj;

					// container.removeAllViews();
					for (int i = 0, j = rSelectPath.size(); i < j; i++) {
						showPic(rSelectPath.get(i), true);
					}

					if (container.getChildCount() >= 3) {
						add_user_photo.setVisibility(View.GONE);
					}
					break;
				case 12:
					if (!"0".equals(isFromExamine)) {
						Intent intent = new Intent(
								ChangeUserResumeActivity.this,
								ExamineUserResumeActivity.class);
						intent.putExtra("isFromVipCenter", true);
						startActivity(intent);
						finish();
					} else {
						finish();
					}
					break;
				default:
					break;
				}
				return false;
			}

		});
	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		if (inputManager.isAcceptingText()) {
			inputManager.hideSoftInputFromWindow(getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
		switch (v.getId()) {
		case R.id.tv_resume_sexman:
			tv_resume_sexman.setBackgroundDrawable(sex_man_clickable);
			tv_resume_sexwoman.setBackgroundDrawable(sex_woman_unclickable);
			updateColor(tv_resume_sexman, tv_resume_sexwoman);
			sex_choose = "1";
			break;

		case R.id.tv_resume_sexwoman:
			tv_resume_sexman.setBackgroundDrawable(sex_man_unclickable);
			tv_resume_sexwoman.setBackgroundDrawable(sex_woman_clickable);
			updateColor(tv_resume_sexwoman, tv_resume_sexman);
			sex_choose = "2";
			break;

		case R.id.fv_resume_birth:
			new DatePickerPopUpwindow(this, parent, new Tools().Today(),
					"resume", false);
			break;

		case R.id.fv_resume_occupation:
			new OccupationPopUpWindow(this, parent, tv_resume_occupation
					.getText().toString());
			break;

		case R.id.fv_resume_address:
			intent = new Intent(ChangeUserResumeActivity.this,
					CitySelectActivity.class);
			intent.putExtra("city", tv_resume_address.getText());
			intent.putExtra("isFromUserResume", true);
			startActivityForResult(intent, 1000);
			break;
		case R.id.fv_resume_education:
			new EducationPopUpWindow(this, parent, tv_resume_education
					.getText().toString());
			break;

		case R.id.fv_resume_skill:
			skillImgLists.clear();
			for (int i = 0; i < skillImgListCount; i++) {
				skillImgLists.add(getSkillImg(i + 1));
			}

			intent = new Intent(ChangeUserResumeActivity.this,
					SkillCertificationUploadActivity.class);

			Bundle bundle = new Bundle();
			bundle.putSerializable("skillImgList", skillImgLists);
			intent.putExtras(bundle);
			System.gc();
			startActivityForResult(intent, 1001);
			break;
		case R.id.fv_resume_label:
			intent = new Intent(ChangeUserResumeActivity.this,
					LabelSelectActivity.class);
			Bundle mBundle = new Bundle();
			mBundle.putSerializable("labelData", labelData);
			intent.putExtras(mBundle);
			startActivityForResult(intent, 1002);
			break;
		case R.id.add_user_photo:
			selectPic();
			break;
		case R.id.fl_resume_school:
			intent = new Intent(ChangeUserResumeActivity.this,
					SchoolSecletActivity.class);
			intent.putExtra("school", tv_resume_school.getText());
			startActivityForResult(intent, 1003);
			break;
		case R.id.title_tv:
			if (special()) {
				updateUserResumeHttpClient();
				if (first == 1) {
					updateLatLon(
							new Tools()
									.getUserId(ChangeUserResumeActivity.this),
							new Tools()
									.getLastLat(ChangeUserResumeActivity.this),
							new Tools()
									.getLastLon(ChangeUserResumeActivity.this));
				}
			}
			break;
		case R.id.back:
			new OrderPopUpWindow(ChangeUserResumeActivity.this, parent,
					"退出后您刚刚编辑过的信息将不会被保存 ，确认退出？", "3");
			// // 创建退出对话框
			// AlertDialog isExit = new AlertDialog.Builder(this).create();
			// // 设置对话框标题
			// isExit.setTitle("温馨提示");
			// // 设置对话框消息
			// isExit.setMessage("退出后您刚刚编辑过的信息将不会被保存 ，确认退出？");
			// // 添加选择按钮并注册监听
			// isExit.setButton("确定", listener);
			// isExit.setButton2("取消", listener);
			// // 显示对话框
			// isExit.show();
			break;
		case R.id.fl_resume_experience:
			intent = new Intent(ChangeUserResumeActivity.this,
					ExperienceMainActivity.class);
			Bundle mBundles = new Bundle();
			mBundles.putSerializable("experienceList", experienceList);
			intent.putExtras(mBundles);
			startActivityForResult(intent, 101);
			break;
		case R.id.privacy_policy:
			intent = new Intent(ChangeUserResumeActivity.this,
					PrivacyPolicyActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				if (!"0".equals(isFromExamine)) {
					Intent intent = new Intent(ChangeUserResumeActivity.this,
							ExamineUserResumeActivity.class);
					intent.putExtra("isFromVipCenter", true);
					startActivity(intent);
					finish();
				} else {
					finish();
				}
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	private Certification getSkillImg(int count) {
		Bitmap image = null;
		String title = "";
		Certification certification = new Certification();
		if (count == 1) {
			image = ((BitmapDrawable) skillImgOne.getDrawable()).getBitmap();
			title = (String) skillTextOne.getText();
		} else if (count == 2) {
			image = ((BitmapDrawable) skillImgTwo.getDrawable()).getBitmap();
			title = (String) skillTextTwo.getText();
		} else if (count == 3) {
			image = ((BitmapDrawable) skillImgThree.getDrawable()).getBitmap();
			title = (String) skillTextThree.getText();
		} else if (count == 4) {
			image = ((BitmapDrawable) skillImgFour.getDrawable()).getBitmap();
			title = (String) skillTextFour.getText();
		} else if (count == 5) {
			image = ((BitmapDrawable) skillImgFive.getDrawable()).getBitmap();
			title = (String) skillTextFive.getText();
		}
		certification.setPath(getPath(image));
		certification.setTitle(title);
		return certification;
	}

	public static String getPath(Bitmap bitmap) {
		try {
			File pic = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos = new FileOutputStream(pic);

			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			return pic.toString();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static Bitmap getBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 101:
			if (resultCode == RESULT_OK) {
				experienceList.clear();
				experienceList.addAll((ArrayList<Experience>) data
						.getSerializableExtra("experienceList"));
				if (experienceList != null) {
					String text = "";
					for (int i = 0, j = experienceList.size(); i < j; i++) {
						if (i == 0) {
							text = experienceList.get(i).getStartDate() + "—"
									+ experienceList.get(i).getEndDate() + "\n"
									+ experienceList.get(i).getEmployer()
									+ "\n"
									+ experienceList.get(i).getObligation();
						} else {
							text = text + "\n\n"
									+ experienceList.get(i).getStartDate()
									+ "—" + experienceList.get(i).getEndDate()
									+ "\n"
									+ experienceList.get(i).getEmployer()
									+ "\n"
									+ experienceList.get(i).getObligation();
						}
					}
					tv_resume_experience.setText(text);
				}
			}
			break;
		case 1000: // 选择地址
			if (resultCode == RESULT_OK) {
				sCity = data.getExtras().getString("city");
				tv_resume_address.setText(sCity);
			}
			break;
		case 1001: // 选择技能证书
			if (resultCode == SKILL_OK) {
				skillImgLists = (ArrayList<Certification>) data
						.getSerializableExtra("skillImgList");
				String skillName = "";
				skillImgListCount = skillImgLists.size();
				for (int i = 0, j = skillImgListCount; i < j; i++) {
					Certification certification = skillImgLists.get(i);
					skillName = skillName + "(" + (i + 1 + "") + ")、"
							+ certification.getTitle() + "   ";
					setVisibleSkillImgOne(certification.getTitle(),
							getBitmap(certification.getPath()), i + 1);
				}
				tv_resume_skill.setText(skillName);

			}
			break;
		case 1002: // 选择标签
			if (resultCode == LABEL_OK) {
				iv_resume_labelOne.setVisibility(View.INVISIBLE);
				iv_resume_labelTwo.setVisibility(View.INVISIBLE);
				iv_resume_labelThree.setVisibility(View.INVISIBLE);
				labelData = (ArrayList<String>) data
						.getSerializableExtra("labelData");
				int count = labelData.size();
				if (count == 1) {
					setVisible(iv_resume_labelOne);
					labelImg(iv_resume_labelOne, labelData.get(0));
				} else if (count == 2) {
					setVisible(iv_resume_labelOne);
					setVisible(iv_resume_labelTwo);
					labelImg(iv_resume_labelOne, labelData.get(0));
					labelImg(iv_resume_labelTwo, labelData.get(1));
				} else if (count == 3) {
					setVisible(iv_resume_labelOne);
					setVisible(iv_resume_labelTwo);
					setVisible(iv_resume_labelThree);
					labelImg(iv_resume_labelOne, labelData.get(0));
					labelImg(iv_resume_labelTwo, labelData.get(1));
					labelImg(iv_resume_labelThree, labelData.get(2));
				}
			}
			break;
		case 1003: // 选择学校
			if (resultCode == RESULT_OK) {
				String school = data.getStringExtra("school");
				tv_resume_school.setText(school);
			}
			break;
		case REQUEST_IMAGE: // 选择照片
			if (resultCode == RESULT_OK) {
				// mSelectPath = new HashMap<String, String>();
				ArrayList<String> rSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				for (String p : rSelectPath) {
					sb.append(p);
					sb.append("\n");
					System.out.println("pic path ---> " + sb.toString());
					userImgListCount++;
					// save(rSelectPath);
					Log.e("listFile2", listFile.toString());
					Log.e("listMD52", listMD5.toString());
				}
				Message msg = mHandler.obtainMessage();
				msg.what = PHOTO_OK;
				msg.obj = rSelectPath;
				msg.sendToTarget();
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 设置隐藏skill图片
	 */
	private void setVisibleSkillImgOne(String title, Bitmap btm, int num) {
		if (num == 1) {
			skillTextOne.setText(title);
			skillImgOne.setImageBitmap(btm);
		} else if (num == 2) {
			skillTextTwo.setText(title);
			skillImgTwo.setImageBitmap(btm);
		} else if (num == 3) {
			skillTextThree.setText(title);
			skillImgThree.setImageBitmap(btm);
		} else if (num == 4) {
			skillTextFour.setText(title);
			skillImgFour.setImageBitmap(btm);
		} else if (num == 5) {
			skillTextFive.setText(title);
			skillImgFive.setImageBitmap(btm);
		}
	}

	/**
	 * 设置标签图片
	 */
	private void labelImg(ImageView img, String num) {
		if ("1".equals(num)) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.zhu));
		} else if ("2".equals(num)) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.xiu));
		} else if ("3".equals(num)) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.zhi));
		} else if ("4".equals(num)) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.che));
		} else if ("5".equals(num)) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.song));
		} else if ("6".equals(num)) {
			img.setImageDrawable(getResources().getDrawable(R.drawable.jia));
		}
	}

	private void selectPic() {
		Log.e("listFile1", listFile.toString());
		Log.e("listMD51", listMD5.toString());
		int selectedMode = MultiImageSelectorActivity.MODE_MULTI; // 照片单选,多选
		boolean showCamera = true; // 开启照相机
		int maxNum = 3; // 最多可选几张照片
		maxNum = maxNum - userImgListCount;

		Intent intent = new Intent(ChangeUserResumeActivity.this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA,
				showCamera);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxNum);
		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				selectedMode);
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	private void showPic(String path, Boolean isFromPic) {
		ImageView iv = new ImageView(this);
		android.view.ViewGroup.LayoutParams cameraLp = add_user_photo
				.getLayoutParams();

		LayoutParams lp = new LayoutParams(cameraLp.width, cameraLp.height);
		lp.setMargins(10, 0, 0, 0);
		iv.setLayoutParams(lp);
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setPadding(5, 5, 5, 5);
		if (isFromPic) {
			// 1.获取原始图片的长和宽,
			// 以下代码是对图片进行解码，inJustDecodeBounds设置为true，可以不把图片读到内存中,但依然可以计算出图片的大小，这正好可以满足我们第一步的需要。
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(path, options);
			int height = options.outHeight;
			int width = options.outWidth;

			// 2.计算压缩比例, inSampleSize就是缩放值。
			// inSampleSize为1表示宽度和高度不缩放，为2表示压缩后的宽度与高度为原来的1/2
			int reqHeight = 100;
			int reqWidth = 100;
			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width
						/ (float) reqWidth);
				options.inSampleSize = heightRatio < widthRatio ? heightRatio
						: widthRatio;
			}

			// 3.缩放并压缩图片
			// 在内存中创建bitmap对象，这个对象按照缩放大小创建的
			options.inJustDecodeBounds = false;
			Bitmap bm = BitmapFactory.decodeFile(path, options);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			/**
			 * 
			 前3行的代码其实已经得到了一个缩放的bitmap对象，如果你在应用中显示图片，就可以使用这个bitmap对象了。
			 * 由于考虑到网络流量的问题。 我们好需要牺牲图片的质量来换取一部分空间
			 * ，这里调用bm.compress()方法进行压缩，这个方法的第二个参数，如果是100，表示不压缩，我这里设置的是60
			 * ，你也可以更加你的需要进行设置，在实验的过程中我设置为30，图片都不会失真。
			 */
			iv.setImageBitmap(bm);
		} else {
			ImageLoader.getInstance().displayImage(path, iv);
		}

		// if(container.getChildCount() < 3){
		container.addView(iv);
		// }else{
		// }
		iv.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(final View v) {
				new AlertDialog.Builder(ChangeUserResumeActivity.this)
						.setTitle("提示")
						.setMessage("是否删除此张照片")
						.setPositiveButton("确定",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										container.removeView(v);
										if (container.getChildCount() < 3) {
											add_user_photo
													.setVisibility(View.VISIBLE);
											userImgListCount--;
										}
									}

								})
						.setNegativeButton("取消",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
				return true;
			}
		});

	}

	/**
	 * 设置图片可见
	 */
	private void setVisible(ImageView img) {
		img.setVisibility(View.VISIBLE);
	}

	/**
	 * 性别改变时 颜色的改变
	 * 
	 */
	public void updateColor(TextView tv1, TextView tv2) {
		tv1.setTextColor(getResources().getColor(R.color.white));
		tv2.setTextColor(getResources().getColor(R.color.sex_text_color));
	}

	/**
	 * 判断是否为空
	 */
	private String isEmpty(String value) {
		String result = " ";
		if (!TextUtils.isEmpty(value)) {
			result = value;
		}
		return result;
	}

	/**
	 * 保存简历\\
	 * 
	 * @param List
	 *            <File> listMD5
	 * @param List
	 *            <File> listFile
	 */
	private void updateUserResumeHttpClient() {
		RequestParams params = new RequestParams();
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(ChangeUserResumeActivity.this);
		try {
			for (int i = 0; i < container.getChildCount(); i++) {
				params = getParamsOther(params, i);
			}

			for (int i = 0, j = skillImgListCount; i < j; i++) {
				params = getParams(params, i + 1);
			}

			// Certification certification = skillData.get(i);
			// params.put(setMD5(certification.getPath(),
			// certification.getFile())
			// + "-" + certification.getTitle() + "1",
			// certification.getFile());
			// }
			JSONObject job = new JSONObject();
			if ("0".equals(isFromExamine)) {
				job.put("id", isFromExamine);
			} else {
				job.put("id", resumeId);
			}
			job.put("userid", isEmpty(new Tools().getUserId(instance)
					.toString()));
			job.put("name", tv_resume_name.getText().toString());
			job.put("sex", sex_choose);
			job.put("birthday", tv_resume_birth.getText().toString());
			job.put("height", isEmpty(tv_resume_height.getText().toString()));
			job.put("weight", isEmpty(tv_resume_weight.getText().toString()));
			job.put("speciality", tv_resume_speciality.getText().toString());
			job.put("occupation", tv_resume_occupation.getText().toString());
			job.put("address", tv_resume_address.getText().toString());
			job.put("education", isEmpty(tv_resume_education.getText()
					.toString()));
			job.put("school", isEmpty(tv_resume_school.getText().toString()));
			job.put("phone", tv_resume_phone.getText().toString());
			job.put("email", tv_resume_email.getText().toString());
			JSONArray work = new JSONArray();
			for (int i = 0, j = experienceList.size(); i < j; i++) {
				JSONObject job11 = new JSONObject();
				// job11.put("wid", experienceList.get(i).getId());
				job11.put("wid", "0");
				job11.put("startDate", experienceList.get(i).getStartDate());
				job11.put("endDate", experienceList.get(i).getEndDate());
				job11.put("workUnit", experienceList.get(i).getEmployer());
				job11.put("position", experienceList.get(i).getObligation());
				work.put(job11);
			}
			job.put("work", work);
			String labelText = new String();
			for (int i = 0, j = labelData.size() - 1; i < j; i++) {
				labelText = labelText + labelData.get(i) + ",";
			}
			labelText = labelText + labelData.get(labelData.size() - 1);
			job.put("label", isEmpty(labelText));

			params.put("data", job.toString());

			HttpClient.post(Config.UPDATE_USER_RESUME_URL, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200) {
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									pd.dismiss();
									if (state.equals("success")) {
										Toast.makeText(
												ChangeUserResumeActivity.this,
												"保存成功!", Toast.LENGTH_SHORT)
												.show();
										Intent intent = new Intent(
												ChangeUserResumeActivity.this,
												ExamineUserResumeActivity.class);
										intent.putExtra("isFromVipCenter", true);
										startActivity(intent);
										finish();
									} else {
										Toast.makeText(
												ChangeUserResumeActivity.this,
												msg, Toast.LENGTH_SHORT).show();
										pd.dismiss();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							} else {
								Toast.makeText(ChangeUserResumeActivity.this,
										msg, Toast.LENGTH_SHORT).show();
								pd.dismiss();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							Toast.makeText(ChangeUserResumeActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * 特殊的ViewName处理
	 * 
	 */
	private boolean special() {
		if (container.getChildCount() == 0) {
			Toast.makeText(ChangeUserResumeActivity.this, "必须上传头像！",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (TextUtils.isEmpty(tv_resume_name.getText().toString())) {
			Toast.makeText(ChangeUserResumeActivity.this, "姓名不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(tv_resume_birth.getText().toString())) {
			Toast.makeText(ChangeUserResumeActivity.this, "出生年月不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(tv_resume_speciality.getText().toString())) {
			Toast.makeText(ChangeUserResumeActivity.this, "特长不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(tv_resume_occupation.getText().toString())) {
			Toast.makeText(ChangeUserResumeActivity.this, "职业不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(tv_resume_address.getText().toString())) {
			Toast.makeText(ChangeUserResumeActivity.this, "现住地不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (labelData.size() == 0) {
			Toast.makeText(ChangeUserResumeActivity.this, "标签不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		List<String> listNumList = new ArrayList<String>();
		listNumList.add(mobile);
		listNumList.add(CM);
		listNumList.add(CU);
		listNumList.add(CT);
		Boolean isNum = false;
		for (int i = 0, j = listNumList.size(); i < j; i++) {
			if (tv_resume_phone.getText().toString()
					.matches(listNumList.get(i))) {
				isNum = true;
				break;
			}
		}

		if (TextUtils.isEmpty(tv_resume_phone.getText().toString())) {
			Toast.makeText(ChangeUserResumeActivity.this, "电话不能为空！",
					Toast.LENGTH_SHORT).show();
			return false;
		} else if (!isNum) {
			Toast.makeText(ChangeUserResumeActivity.this, "电话格式不对！",
					Toast.LENGTH_SHORT).show();
			return false;
		}

		if (!TextUtils.isEmpty(tv_resume_email.getText().toString())
				&& !tv_resume_email.getText().toString().matches(email_c)) {
			Toast.makeText(ChangeUserResumeActivity.this, "邮箱格式不对！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private RequestParams getParams(RequestParams params, int count) {
		try {
			Bitmap image = null;
			String title = "";
			if (count == 1) {
				image = ((BitmapDrawable) skillImgOne.getDrawable())
						.getBitmap();
				title = (String) skillTextOne.getText();
			} else if (count == 2) {
				image = ((BitmapDrawable) skillImgTwo.getDrawable())
						.getBitmap();
				title = (String) skillTextTwo.getText();
			} else if (count == 3) {
				image = ((BitmapDrawable) skillImgThree.getDrawable())
						.getBitmap();
				title = (String) skillTextThree.getText();
			} else if (count == 4) {
				image = ((BitmapDrawable) skillImgFour.getDrawable())
						.getBitmap();
				title = (String) skillTextFour.getText();
			} else if (count == 5) {
				image = ((BitmapDrawable) skillImgFive.getDrawable())
						.getBitmap();
				title = (String) skillTextFive.getText();
			}
			pic = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos = new FileOutputStream(pic);

			image.compress(Bitmap.CompressFormat.JPEG, 40, fos);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			MD5Util.getMD5String(b);
			params.put(MD5Util.getMD5String(b) + "-" + title + "1", pic);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	private RequestParams getParamsOther(RequestParams params, int count) {
		try {
			Bitmap image = null;
			ImageView imgImageView = (ImageView) container.getChildAt(count);
			image = ((BitmapDrawable) imgImageView.getDrawable()).getBitmap();

			pic = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos = new FileOutputStream(pic);

			image.compress(Bitmap.CompressFormat.JPEG, 40, fos);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			MD5Util.getMD5String(b);
			params.put(MD5Util.getMD5String(b) + "0", pic);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && !"0".equals(isFromExamine)) {
			new OrderPopUpWindow(ChangeUserResumeActivity.this, parent,
					"退出后您刚刚编辑过的信息将不会被保存 ，确认退出？", "3");
		} else if (keyCode == KeyEvent.KEYCODE_BACK
				&& "0".equals(isFromExamine)) {
			new OrderPopUpWindow(ChangeUserResumeActivity.this, parent,
					"退出后您刚刚编辑过的信息将不会被保存 ，确认退出？", "3");
		}

		return false;

	}
}