package com.youge.jobfinder.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import model.NowEvent;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.DatePickerPopUpwindow;
import popup.EventPopUpWindow;
import popup.OrderPopUpWindow;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.JsonParser;
import tools.MD5Util;
import tools.PictureUtil;
import tools.Tools;
import view.RoundImageView;
import view.UpMarqueeImageView;
import view.UpMarqueeTextView;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.PubStandardActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.LabelSelectActivity;

import fragment.MainGrabFragment;

public class FillOrderMaintActivity extends BaseActivity implements
		OnClickListener {

	public static FillOrderMaintActivity instance;
	private ImageView add_user_photo, green_triangle_one, green_triangle_two,
			back, voice_post, voice_proceed, voice_succeed, voice_error,
			voice_no;
	private LinearLayout parent, container, label, fill_order_online,
			fill_order_offline, fill_order_finish_dates, fill_address_ss,
			fill_order_counts, fill_order_price_ll;
	private FrameLayout labelF;
	private UpMarqueeImageView headline_img;
	private int userImgListCount = 0;
	private static final int REQUEST_IMAGE = 300;
	public Handler mHandler;
	private InputMethodManager inputManager;
	private EditText fill_order_title, fill_order_request, fill_order_price,
			fill_order_phone, fill_order_count;
	private TextView fill_order_title_down, count_order_text,
			fill_order_request_count, fill_order_finish_date, fill_address,
			title_tv, publication_standard;
	private ArrayList<String> labelList;
	private String labelType, method, startDate, endDate, lat = "43.803211",
			lng = "125.271649", need, timeout, onlinePay, category, price,
			number, address, phone, userid;
	private Drawable fill_order_online_clickable,
			fill_order_online_unclickable, fill_order_offline_clickable,
			fill_order_offline_unclickable;
	private CheckBox cb1, cb2, cb3;
	/** 消息标识照片选择 */
	protected static final int PHOTO_OK = 6;
	private registerReceiver receiver;
	private RoundImageView vip_main_head;
	private PopupWindow pw_voice;
	// 语音听写对象
	private SpeechRecognizer mIat;
	private Toast mToast;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	int ret = 0; // 函数调用返回值
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	private String voice_result;

	private ArrayList<NowEvent> nowEvents;
	private int count = 0;
	private UpMarqueeTextView headline_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fill_order_main_t);
		findView();
		initView();
		handMessage();
		orderActivityHttpClient();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View view_voice = inflater.inflate(R.layout.popup_voice, null);
		voice_proceed = (ImageView) view_voice.findViewById(R.id.voice_proceed);
		voice_succeed = (ImageView) view_voice.findViewById(R.id.voice_succeed);
		voice_error = (ImageView) view_voice.findViewById(R.id.voice_error);
		voice_no = (ImageView) view_voice.findViewById(R.id.voice_no);
		pw_voice = new PopupWindow(view_voice, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		pw_voice.setBackgroundDrawable(new ColorDrawable(R.color.transparents)); // 设置背景
		pw_voice.setFocusable(true);
		pw_voice.setOutsideTouchable(true);
		vip_main_head = (RoundImageView) findViewById(R.id.vip_main_head);
		back = (ImageView) findViewById(R.id.back);
		parent = (LinearLayout) findViewById(R.id.fill_order_parent);
		add_user_photo = (ImageView) findViewById(R.id.add_user_photo);
		green_triangle_one = (ImageView) findViewById(R.id.green_triangle_one);
		green_triangle_two = (ImageView) findViewById(R.id.green_triangle_two);
		container = (LinearLayout) findViewById(R.id.container);
		voice_post = (ImageView) findViewById(R.id.voice_post);
		// 标题
		fill_order_title = (EditText) findViewById(R.id.fill_order_title);
		fill_order_title_down = (TextView) findViewById(R.id.fill_order_title_down);
		// 价格
		fill_order_price = (EditText) findViewById(R.id.fill_order_price);
		fill_order_price_ll = (LinearLayout) findViewById(R.id.fill_order_price_ll);
		// 电话
		fill_order_phone = (EditText) findViewById(R.id.fill_order_phone);
		if (!TextUtils.isEmpty(new Tools()
				.getPhone(FillOrderMaintActivity.this))) {
			fill_order_phone.setText(new Tools()
					.getPhone(FillOrderMaintActivity.this));
		}
		count_order_text = (TextView) findViewById(R.id.count_order_text);
		fill_order_request_count = (TextView) findViewById(R.id.fill_order_request_count);
		// 标签
		label = (LinearLayout) findViewById(R.id.fill_order_label);
		labelF = (FrameLayout) findViewById(R.id.fill_order_labelf);
		// 详细内容
		fill_order_request = (EditText) findViewById(R.id.fill_order_request);
		// 完成方式
		fill_order_online = (LinearLayout) findViewById(R.id.fill_order_online);
		fill_order_offline = (LinearLayout) findViewById(R.id.fill_order_offline);
		// 完成时间
		fill_order_finish_dates = (LinearLayout) findViewById(R.id.fill_order_finish_dates);
		fill_order_finish_date = (TextView) findViewById(R.id.fill_order_finish_date);
		// 地址
		fill_address_ss = (LinearLayout) findViewById(R.id.fill_address_ss);
		fill_address = (TextView) findViewById(R.id.fill_address);
		// 人数
		fill_order_counts = (LinearLayout) findViewById(R.id.fill_order_counts);
		fill_order_count = (EditText) findViewById(R.id.fill_order_count);
		// 完成
		title_tv = (TextView) findViewById(R.id.title_tv);
		publication_standard = (TextView) findViewById(R.id.publication_standard);
		headline_img = (UpMarqueeImageView) findViewById(R.id.headline_img);
		headline_text = (UpMarqueeTextView) findViewById(R.id.headline_text);
		cb1 = (CheckBox) findViewById(R.id.fill_order_cb1);
		cb2 = (CheckBox) findViewById(R.id.fill_order_cb2);
		cb3 = (CheckBox) findViewById(R.id.fill_order_cb3);
		cb3.setClickable(false);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		nowEvents = new ArrayList<NowEvent>();
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		// 语音监听
		mIat = SpeechRecognizer.createRecognizer(FillOrderMaintActivity.this,
				mInitListener);

		if (!TextUtils.isEmpty(new Tools().getUserimg(getApplicationContext()))) {
			ImageLoader.getInstance().displayImage(
					new Tools().getUserimg(getApplicationContext()),
					vip_main_head);
		}
		method = "";
		instance = this;
		inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		labelList = new ArrayList<String>();
		labelType = "";
		// 初始默认值, method='0'(线上), need=0(否需要简历), timeout=0(否超时赔付),
		// onlinePay="0"(否在线支付)
		need = "0";
		timeout = "0";
		category = "0";
		onlinePay = "1";
		price = "0";
		number = "1";
		startDate = "0";
		endDate = "0";
		address = "";
		phone = "";
		userid = new Tools().getUserId(this);

		category = getIntent().getExtras().getString("category");

		if ("1".equals(category)) {
			fill_order_price_ll.setVisibility(View.GONE);
		} else {
			fill_order_count.setFocusable(false);
		}
		lat = new Tools().getCurrentLat(this);
		lng = new Tools().getCurrentLng(this);
		fill_order_online_clickable = getResources().getDrawable(
				R.drawable.online_green);
		fill_order_online_unclickable = getResources().getDrawable(
				R.drawable.online_gray);
		fill_order_offline_clickable = getResources().getDrawable(
				R.drawable.offline_green);
		fill_order_offline_unclickable = getResources().getDrawable(
				R.drawable.offline_gray);
		fill_order_online.setBackgroundDrawable(fill_order_online_unclickable);
		fill_order_offline
				.setBackgroundDrawable(fill_order_offline_unclickable);

		add_user_photo.setOnClickListener(this);
		labelF.setOnClickListener(this);
		fill_order_online.setOnClickListener(this);
		fill_order_offline.setOnClickListener(this);
		fill_order_finish_dates.setOnClickListener(this);
		fill_address_ss.setOnClickListener(this);
		back.setOnClickListener(this);
		title_tv.setOnClickListener(this);
		publication_standard.setOnClickListener(this);
		voice_post.setOnClickListener(this);
		headline_text.setOnClickListener(this);
		fill_order_title.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (fill_order_title.getText().length() == 0) {
						fill_order_title.setText("我需要•");
						fill_order_title_down.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		fill_order_title.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fill_order_title_down.setVisibility(View.INVISIBLE);
				count_order_text.setText(s.length() + "");
				if (s.length() > 15) {
					count_order_text.setTextColor(android.graphics.Color
							.parseColor("#ff0000"));
				} else {
					count_order_text.setTextColor(android.graphics.Color
							.parseColor("#a1a1a1"));
				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		fill_order_request.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				fill_order_request_count.setText(s.length() + "");
				if (s.length() > 200) {
					fill_order_request_count
							.setTextColor(android.graphics.Color
									.parseColor("#ff0000"));
				} else {
					fill_order_request_count
							.setTextColor(android.graphics.Color
									.parseColor("#a1a1a1"));
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					timeout = "1";
				} else {
					timeout = "0";
				}
			}
		});
		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					need = "1";
				} else {
					need = "0";
				}
			}
		});
		cb3.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					onlinePay = "1";
				} else {
					onlinePay = "1";
				}
			}
		});
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
				case PHOTO_OK:
					ArrayList<String> rSelectPath = (ArrayList<String>) msg.obj;

					// container.removeAllViews();
					for (int i = 0, j = rSelectPath.size(); i < j; i++) {
						showPic(rSelectPath.get(i), true);
					}

					if (container.getChildCount() >= 3) {
						add_user_photo.setVisibility(View.GONE);
					} else if (container.getChildCount() == 1) {
						add_user_photo.setImageResource(R.drawable.add_two_img);
					} else if (container.getChildCount() == 2) {
						add_user_photo.setImageResource(R.drawable.add_one_img);
					}
					break;
				case 7:
					new Thread() {
						@Override
						public void run() {
							try {
								Thread.sleep(2000);
								Message msg = mHandler.obtainMessage();
								msg.what = 8;
								msg.sendToTarget();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							super.run();
						}
					}.start();

					break;
				case 8:
					if (pw_voice.isShowing()) {
						pw_voice.dismiss();
					}
					break;
				case 9:
					if (nowEvents != null) {
						mHandler.post(task);
					}
					break;
				case 10:
					finish();
					break;
				case 11:
					postOrderHttpClient();
					break;

				default:
					break;
				}
				return false;
			}

		});
	}

	private Runnable task = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			mHandler.postDelayed(this, 5 * 1000);// 设置延迟时间，此处是5秒
			if (count == nowEvents.size()) {
				count = 0;
			}
			if (nowEvents.size() != 0) {
				NowEvent nowEvent = nowEvents.get(count);
				// title_one.setTextColor(Integer.parseInt(headlines.getTitlecolor()));
				// title_one.setTextColor(0xff123456);
				ImageLoader.getInstance().displayImage(
						nowEvent.getActivityImg(), headline_img);
				// headline_img.setImageDrawable(getResources().getDrawable(
				// R.drawable.year));
				headline_text.setText(nowEvent.getActivityTile());
				// headline_text.setText(headlines.getText());

				count++;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// if (inputManager.isAcceptingText()){
		// inputManager.hideSoftInputFromWindow(getCurrentFocus()
		// .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		// }
		switch (v.getId()) {
		case R.id.add_user_photo:
			selectPic();
			break;
		case R.id.fill_order_labelf:
			Intent intent = new Intent(FillOrderMaintActivity.this,
					LabelSelectActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("labelData", labelList);
			intent.putExtras(bundle);
			intent.putExtra("isFromFillOrderBoolean", true);
			startActivityForResult(intent, 100);
			break;
		case R.id.fill_order_online:
			method = "0";
			fill_order_online
					.setBackgroundDrawable(fill_order_online_clickable);
			fill_order_offline
					.setBackgroundDrawable(fill_order_offline_unclickable);
			green_triangle_one.setVisibility(View.VISIBLE);
			green_triangle_two.setVisibility(View.INVISIBLE);
			fill_order_finish_dates.setVisibility(View.VISIBLE);
			fill_address_ss.setVisibility(View.GONE);
			fill_address.setText(" ");
			fill_order_counts.setVisibility(View.VISIBLE);
			break;
		case R.id.fill_order_offline:
			method = "1";
			setAddress();
			fill_order_online
					.setBackgroundDrawable(fill_order_online_unclickable);
			fill_order_offline
					.setBackgroundDrawable(fill_order_offline_clickable);
			green_triangle_one.setVisibility(View.INVISIBLE);
			green_triangle_two.setVisibility(View.VISIBLE);
			fill_order_finish_dates.setVisibility(View.VISIBLE);
			fill_address_ss.setVisibility(View.VISIBLE);
			fill_order_counts.setVisibility(View.VISIBLE);
			break;
		case R.id.fill_order_finish_dates:// 完成时间
			new DatePickerPopUpwindow(this, parent,
					new Tools().getTenMinLater(new Tools().Today()), "fs", true);
			break;
		case R.id.fill_address_ss:
			Intent intents = new Intent(FillOrderMaintActivity.this,
					AddressMainActivity.class);
			startActivityForResult(intents, 101);
			break;
		case R.id.back:

			new OrderPopUpWindow(FillOrderMaintActivity.this, parent,
					"您确定要放弃填写订单内容吗？", "1");
			// // 创建退出对话框
			// AlertDialog isExit = new AlertDialog.Builder(this).create();
			// // 设置对话框标题
			// isExit.setTitle("温馨提示");
			// // 设置对话框消息
			// isExit.setMessage("您确定要放弃填写订单内容吗？");
			// // 添加选择按钮并注册监听
			// isExit.setButton("确定", listener);
			// isExit.setButton2("取消", listener);
			// // 显示对话框
			// isExit.show();

			break;
		case R.id.title_tv:
			if (validate()) {
				new OrderPopUpWindow(FillOrderMaintActivity.this, parent,
						"确定要发布订单了吗？", "2");
			}
			break;
		case R.id.publication_standard:
			Intent intentss = new Intent(FillOrderMaintActivity.this,
					PubStandardActivity.class);
			startActivity(intentss);
			break;
		case R.id.voice_post:
			pw_voice.showAtLocation(parent, Gravity.CENTER_HORIZONTAL
					| Gravity.TOP, 0, 0);
			// 设置参数
			setParam();
			// 不显示听写对话框
			ret = mIat.startListening(mRecognizerListener);
			if (ret != ErrorCode.SUCCESS) {
				showTip("听写失败,错误码：" + ret);
			} else {
				showTip("请开始说话…");
			}
			break;
		case R.id.headline_text:
			new EventPopUpWindow(FillOrderMaintActivity.this, parent, nowEvents);
			break;
		default:
			break;
		}

	}

	private boolean validate() {
		if (fill_order_title.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写标题!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fill_order_title.getText().toString().length() < 3) {
			Toast.makeText(this, "标题请至少填写3个字!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fill_order_title.getText().toString().length() > 15) {
			Toast.makeText(this, "标题请少于15个字!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("我需要•".equals(fill_order_title.getText().toString())) {
			Toast.makeText(this, "标题请填写需要做的事", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (labelType.equals("")) {
			Toast.makeText(this, "请至少选择一个标签!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (fill_order_request.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写详细要求!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (fill_order_request.getText().toString().length() < 5) {
			Toast.makeText(this, "详细要求请至少填写5个字!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fill_order_request.getText().toString().length() > 200) {
			Toast.makeText(this, "详细要求请少于200个字!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if ("".equals(method)) {
			Toast.makeText(this, "请选择完成方式!", Toast.LENGTH_SHORT).show();
			return false;
		}

		if (method.equals("1") && address.equals(" ")) {
			Toast.makeText(this, "请选择地址!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (fill_order_phone.getText().toString().length() == 0
				|| fill_order_phone.getText().toString().length() != 11) {
			Toast.makeText(this, "请提供正确的联系电话!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			phone = fill_order_phone.getText().toString();
		}

		if (category.equals("0")) { // 出价状态, 价钱不可以为0, number为0
			if (fill_order_price.getText().toString().length() == 0
					|| Integer.valueOf(fill_order_price.getText().toString()) == 0) {
				Toast.makeText(this, "请填写价钱!", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				price = fill_order_price.getText().toString();
			}
		}
		if (category.equals("1")) { // 报价状态, 价钱可以为0, number不可为0
			if (fill_order_count.getText().toString().length() == 0
					|| Integer.valueOf(fill_order_count.getText().toString()) == 0) {
				Toast.makeText(this, "请填写人数!", Toast.LENGTH_SHORT).show();
				return false;
			} else {
				number = fill_order_count.getText().toString();
			}
		}
		if (startDate.equals("0")) {
			Toast.makeText(this, "请选择完成时间!", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void postOrderHttpClient() {
		RequestParams params = new RequestParams();
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(FillOrderMaintActivity.this);
		System.out.println("label tijiao ---> "
				+ labelType.substring(0, labelType.length() - 1));
		try {
			JSONObject job = new JSONObject();
			job.put("title", fill_order_title.getText().toString());
			job.put("details", fill_order_request.getText().toString());
			job.put("phone", phone);
			job.put("address", address);
			job.put("category", category);
			job.put("method", method);
			job.put("price", price);
			job.put("number", number);
			job.put("startDate", new Tools().Today());
			job.put("endDate", endDate);
			job.put("onlinePay", onlinePay);
			job.put("need", need);
			job.put("timeout", timeout);
			job.put("lat", lat);
			job.put("lng", lng);
			job.put("userid", userid);
			job.put("type", labelType.substring(0, labelType.length() - 1));

			params.put("data", job.toString());

			for (int i = 0; i < container.getChildCount(); i++) {
				params = getParamsOther(params, i);
			}

			HttpClient.post(Config.POST_ORDER_URL, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200) { // 成功
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									pd.dismiss();
									if (state.equals("success")) {
										JSONObject data = job
												.getJSONObject("data");
										String result = data
												.getString("result");
										if (result.equals("1")) {
											Toast.makeText(
													FillOrderMaintActivity.this,
													"发单成功", Toast.LENGTH_SHORT)
													.show();
											Intent intent = new Intent(
													FillOrderMaintActivity.this,
													OrderListDetailActivity.class);
											intent.putExtra("category",
													category);
											intent.putExtra("oid",
													data.getString("id"));
											startActivity(intent);
											FillOrderMaintActivity.this
													.finish();
											statistics(true, "成功");
										} else {
											Toast.makeText(
													FillOrderMaintActivity.this,
													msg, Toast.LENGTH_SHORT)
													.show();
											statistics(false, msg);
										}
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							} else {
								Toast.makeText(FillOrderMaintActivity.this,
										msg, Toast.LENGTH_SHORT).show();
								statistics(false, msg);
								pd.dismiss();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							statistics(false, "网络连接失败，请检测网络！");
							Toast.makeText(FillOrderMaintActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
							pd.dismiss();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}

	/**
	 * 统计发单点击次数, 线上线下单, 报价单抢单, 人数或价钱, 需要简历, 超时赔付
	 * 
	 * @param
	 */
	private void statistics(boolean success, String type) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("onoff", method.equals("0") ? "线上" : "线下");
		map.put("bidGrab", category.equals("0") ? "抢单" : "报价单");
		map.put("count", price.equals("0") ? number : price);
		map.put("resume", need.equals("0") ? "不要" : "需要");
		map.put("timeOut", timeout.equals("0") ? "不要" : "需要");
		map.put("success", success ? "成功" : "失败");
		map.put("success", type);
		MobclickAgent.onEvent(this, "post_order", map);
	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	private void selectPic() {
		int selectedMode = MultiImageSelectorActivity.MODE_MULTI; // 照片单选,多选
		boolean showCamera = true; // 开启照相机
		int maxNum = 3; // 最多可选几张照片
		maxNum = maxNum - userImgListCount;

		Intent intent = new Intent(FillOrderMaintActivity.this,
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

	/**
	 * 处理日期选择结果handler
	 */
	public Handler dHandler = new Handler() {//

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1: // 开始日期选择正确
				startDate = (String) msg.obj;
				endDate = startDate;
				fill_order_finish_date.setText(startDate);
				break;
			case 2: // 开始日期选择错误
				Toast.makeText(FillOrderMaintActivity.this,
						"不能选择之前的日期, 请重新选择!", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
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
				}
				Message msg = mHandler.obtainMessage();
				msg.what = PHOTO_OK;
				msg.obj = rSelectPath;
				msg.sendToTarget();
			}
			break;
		case 100:
			if (resultCode == 5) {
				labelType = "";
				labelList = (ArrayList<String>) data.getExtras()
						.getSerializable("labelData");
				label.removeAllViews();
				for (int i = 0, j = labelList.size(); i < j; i++) {
					ImageView iv = new ImageView(FillOrderMaintActivity.this);
					LayoutParams lp = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					iv.setLayoutParams(lp);
					if (labelList.get(i).equals("1")) {
						iv.setImageResource(R.drawable.zhu);
					} else if (labelList.get(i).equals("2")) {
						iv.setImageResource(R.drawable.xiu);
					} else if (labelList.get(i).equals("3")) {
						iv.setImageResource(R.drawable.zhi);
					} else if (labelList.get(i).equals("4")) {
						iv.setImageResource(R.drawable.che);
					} else if (labelList.get(i).equals("5")) {
						iv.setImageResource(R.drawable.song);
					} else if (labelList.get(i).equals("6")) {
						iv.setImageResource(R.drawable.jia);
					}

					label.addView(iv);

					labelType += labelList.get(i) + ",";
				}
				System.out.println("labelType ---> " + labelType);
			}
			break;
		case 101:
			if (resultCode == RESULT_OK) {
				setAddress();
			}
			break;
		default:
			break;
		}
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
				new AlertDialog.Builder(FillOrderMaintActivity.this)
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
											if (container.getChildCount() == 0) {
												add_user_photo
														.setImageResource(R.drawable.add_three_img);
											} else if (container
													.getChildCount() == 1) {
												add_user_photo
														.setImageResource(R.drawable.add_two_img);
											} else if (container
													.getChildCount() == 2) {
												add_user_photo
														.setImageResource(R.drawable.add_one_img);
											}
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

	private void setAddress() {
		SharedPreferences sp = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		String addressMainAddress = sp.getString("addressMainAddress", "");
		String addressMainName = sp.getString("addressMainName", "");
		String addressMainSex = sp.getString("addressMainSex", "");
		if ("1".equals(addressMainSex)) {
			addressMainSex = "先生";
		} else {
			addressMainSex = "女士";
		}
		if (!"".equals(addressMainAddress)) {
			fill_address.setText(addressMainAddress.split("\\ ")[3] + "("
					+ addressMainName.substring(0, 1) + addressMainSex + ")");
			address = addressMainAddress + "("
					+ addressMainName.substring(0, 1) + addressMainSex + ")";
		} else {
			fill_address.setText("");
			fill_address.setHint("选择地址");
			address = " ";
		}
	}

	/**
	 * 注册广播
	 * 
	 */
	private void registerReceiver() {
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

	protected class registerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			if ("1".equals(new Tools().getNotificationType(context))
					|| "2".equals(new Tools().getNotificationType

					(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(FillOrderMaintActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"FillOrderMainActivity");
				pps[0] = pp;
			} else if ("3".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				mandatoryExit(context);
			} else {
				NotificationManager nManager = (NotificationManager)

				context.getSystemService

				(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.icon = R.drawable.logo;// 设置通知的图标
				// 添加声音提示
				notification.defaults = Notification.DEFAULT_SOUND;
				// audioStreamType的值必须AudioManager中的值，代表着响铃的模式
				notification.audioStreamType =

				android.media.AudioManager.ADJUST_LOWER;
				notification.defaults = Notification.DEFAULT_VIBRATE;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				long[] vibrate = { 0, 100, 200, 300 };
				notification.vibrate = vibrate;

				notification.tickerText = "找事吧"; // 显示在状态栏中的文字
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(), 0);
				// 点击状态栏的图标出现的提示信息设置
				notification.setLatestEventInfo(context, "找事吧", new Tools

				().getNotificationText(context), pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context) {
		String CID = new Tools().getCID(FillOrderMaintActivity.this);
		String notificationText = new Tools()
				.getNotificationText(FillOrderMaintActivity.this);
		Boolean isFirstStart = new Tools()
				.getisFirstStart(FillOrderMaintActivity.this);
		unBindAlias(new Tools().getUserId(FillOrderMaintActivity.this),
				new Tools().getCID(FillOrderMaintActivity.this),
				FillOrderMaintActivity.this);
		SharedPreferences sharedPre = FillOrderMaintActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(FillOrderMaintActivity.this, parent,
				new Tools().getNotificationText(context));
	}

	/**
	 * 绑定账户
	 */
	private void unBindAlias(String uid, String cid, Context context) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("cid", cid);
			job.put("type", "1");// 强制退出是0

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(context, Config.UN_BIND_ALIAS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[]

						arg1, byte[] arg2) {
							if (arg0 == 200) {
								String str = new String

								(arg2);
								System.out.println("解除绑定账户接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result =

									new JSONObject(str);
									String state =

									result.getString("state");
									message =

									result.getString("msg");
									if (state.equals

									("success")) {
										JSONObject

										data = result

										.getJSONObject("data");
										Log.v("别人资料", data.toString());
										String

										results = data

										.getString("result");
										if

										("0".equals(results)) {

											Log.v("别人资料", "提交失败");
										} else {

											Log.v("别人资料", "提交成功");
										}
									}
								} catch (JSONException e) {
									e.printStackTrace

									();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[]

						arg1, byte[] arg2, Throwable

						arg3) {
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private RequestParams getParamsOther(RequestParams params, int count) {
		try {
			Bitmap image = null;
			ImageView imgImageView = (ImageView) container.getChildAt(count);
			image = ((BitmapDrawable) imgImageView.getDrawable()).getBitmap();

			File pic = new File(PictureUtil.getAlbumDir(),
					System.currentTimeMillis() + ".jpg");

			FileOutputStream fos = new FileOutputStream(pic);

			image.compress(Bitmap.CompressFormat.JPEG, 40, fos);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			MD5Util.getMD5String(b);
			params.put(MD5Util.getMD5String(b), pic);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		mIat.setParameter(SpeechConstant.DOMAIN, "iat");
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT, "1");

		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/msc/iat.wav");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA, "0");
	}

	/**
	 * 听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
			voice_no.setVisibility(View.GONE);
			voice_error.setVisibility(View.GONE);
			voice_proceed.setVisibility(View.VISIBLE);
			voice_succeed.setVisibility(View.GONE);
			showTip("开始说话");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
			voice_no.setVisibility(View.VISIBLE);
			voice_error.setVisibility(View.GONE);
			voice_proceed.setVisibility(View.GONE);
			voice_succeed.setVisibility(View.GONE);
			// showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			voice_no.setVisibility(View.GONE);
			voice_error.setVisibility(View.GONE);
			voice_proceed.setVisibility(View.GONE);
			voice_succeed.setVisibility(View.VISIBLE);
			Message msg = mHandler.obtainMessage();
			msg.what = 7;
			msg.sendToTarget();
			showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d("TAG", results.getResultString());
			voice_result = printResult(results);
			if (isLast) {
				// TODO 最后的结果
				fill_order_request.setText(voice_result);
				fill_order_request.setSelection(fill_order_request.length());
			}
		}

		@Override
		public void onVolumeChanged(int volume, byte[] data) {
			// showTip("当前正在说话，音量大小：" + volume);
			Log.d("TAG", "返回音频数据：" + data.length);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
			// 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
			// 若使用本地能力，会话id为null
			// if (SpeechEvent.EVENT_SESSION_ID == eventType) {
			// String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
			// Log.d(TAG, "session id =" + sid);
			// }
		}
	};

	private String printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}
		return fill_order_request.getText() + resultBuffer.toString();
	}

	/**
	 * 初始化监听器。
	 */
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d("TAG", "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				showTip("初始化失败，错误码：" + code);
			}
		}
	};

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 点击手机软键时调用
	 * 
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {

		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && pw_voice.isShowing()) {
			pw_voice.dismiss();
		}
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
				&& event.getAction() != KeyEvent.ACTION_UP) {
			new OrderPopUpWindow(FillOrderMaintActivity.this, parent,
					"您确定要放弃填写订单内容吗？", "1");
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * 广告接口
	 * 
	 * @param type类型
	 */
	private void orderActivityHttpClient() {
		JSONObject job = new JSONObject();
		try {
			job.put("type", "0");
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			// pd = CustomProgressDialog.createDialog(context);
			HttpClient.post(FillOrderMaintActivity.this,
					Config.ORDER_ACTIVITY_LIST, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							// TODO Auto-generated method stub
							if (statusCode == 200) { // 成功
								// 将byte 转换 String
								String str = new String(responseBody);
								Log.e("rr", "str--" + str);
								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									if (state.equals("success")) {
										JSONObject data = job
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("list");
										for (int i = 0, j = jArray.length(); i < j; i++) {
											JSONObject child = jArray
													.optJSONObject(i);
											NowEvent event = new NowEvent();
											event.setActivityId(child
													.getString("activityId"));
											event.setActivityTile(child
													.getString("activityTitle"));
											event.setActivityImg(child
													.getString("activityImg"));
											nowEvents.add(event);
										}
										Message msg = mHandler.obtainMessage();
										msg.what = 9;
										msg.sendToTarget();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									// pd.dismiss();
								}

							} else {// 提交失败
									// pd.dismiss();
								Toast.makeText(FillOrderMaintActivity.this,
										"请求广告轮播失败!", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// TODO Auto-generated method stub
							// pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(FillOrderMaintActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			// pd.dismiss();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
		super.onDestroy();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}
}
