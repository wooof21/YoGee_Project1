/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import model.NowEvent;
import model.Quote;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindow;
import popup.ContentPopUpWindowSingleButton;
import popup.EventPopUpWindow;
import popup.HintPopUpWindow;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import u.aly.ca;
import u.aly.cg;
import view.MGridView;
import view.RoundImageView;
import view.UpMarqueeImageView;
import view.UpMarqueeTextView;

import baidu.BaiDuMapActivity;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.alipay.a.a.c;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.OrderDetailActivity.registerReceiver;

import adapter.ConvenienceFacilityGVAdapter;
import adapter.MainGrabGvInLvAdapter;
import android.R.integer;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class MainOrderDetailActivity extends BaseActivity implements
		OnClickListener{

	private RelativeLayout parent;
	private ImageView back;
	private ImageView share;

	/**
	 * bottom
	 */
	private FrameLayout bottom;
	private TextView grab, bl_total, bid, mb_bid, mb_status, mb_cancel;
	private LinearLayout bidList, myBid;
	private RoundImageView bl_iv1, bl_iv2, bl_iv3, mb_head;
	private RoundImageView[] bl_iv;

	/**
	 * top
	 */
	private RoundImageView head;
	private TextView name, phone, identify;
	private ImageView sex, identityPic;

	private View include;
	private TextView ago, title, price, content, address, date, total,
			timeHint, checkMap;
	private ImageView label1, label2, addrIv;
	private MGridView gv;

	private LinearLayout agreement;
	private CheckBox cb;
	private TextView cbtv;

	private LinearLayout menu_share, menu_report;

	private RelativeLayout eventRL;
	private UpMarqueeImageView eventIv;
	private UpMarqueeTextView eventTv;

	private String category, uid, rid, gid, oid, shareUrl, hint, orderState;
	private double orderLat, orderLng;

	private registerReceiver receiver;
	protected int sSize;
	public static MainOrderDetailActivity instance;
	private ArrayList<Quote> qList;

	private String orderStartTime, orderEndTime, currentTime;
	/** 菜单PopupWindow */
	private PopupWindow pw_menu;
	/** 屏幕宽 */
	int WIDTH;
	
	private ArrayList<NowEvent> nowEvents;
	private int count = 0;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_main_oder_detail);
		initView();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		registerReceiver();
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
	protected void onStop(){
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}

	private void initView(){
		Exit.getInstance().addActivity(this);
		// 举报 分享
		WIDTH = getWindowManager().getDefaultDisplay().getWidth();
		LayoutInflater inflater = LayoutInflater.from(this);
		View view_menu = inflater.inflate(R.layout.popup_menu, null);
		pw_menu = new PopupWindow(view_menu, WIDTH * 1 / 3,
				LayoutParams.WRAP_CONTENT, true);
		pw_menu.setBackgroundDrawable(new ColorDrawable());
		pw_menu.setOutsideTouchable(false);
		menu_share = (LinearLayout) view_menu.findViewById(R.id.menu_share);
		menu_report = (LinearLayout) view_menu.findViewById(R.id.menu_report);

		parent = (RelativeLayout) findViewById(R.id.order_detail_parent);
		back = (ImageView) findViewById(R.id.back);
		share = (ImageView) findViewById(R.id.title_tv);

		bottom = (FrameLayout) findViewById(R.id.new_main_order_detail_grab_framelayout);
		grab = (TextView) findViewById(R.id.new_main_order_detail_grab);
		bidList = (LinearLayout) findViewById(R.id.new_main_order_detail_bidlist);
		bl_iv1 = (RoundImageView) findViewById(R.id.new_main_order_detail_bidlist_iv1);
		bl_iv2 = (RoundImageView) findViewById(R.id.new_main_order_detail_bidlist_iv2);
		bl_iv3 = (RoundImageView) findViewById(R.id.new_main_order_detail_bidlist_iv3);
		bl_total = (TextView) findViewById(R.id.new_main_order_detail_bidlist_total);
		bid = (TextView) findViewById(R.id.new_main_order_detail_bid);
		myBid = (LinearLayout) findViewById(R.id.new_main_order_detail_mybid);
		mb_head = (RoundImageView) findViewById(R.id.new_main_order_detail_bid_frame_iv);
		mb_bid = (TextView) findViewById(R.id.new_main_order_detail_bid_frame_tv);
		mb_status = (TextView) findViewById(R.id.new_main_order_detail_bid_frame_status);
		mb_cancel = (TextView) findViewById(R.id.new_main_order_detail_bid_frame_cancel);
		bl_iv = new RoundImageView[] { bl_iv1, bl_iv2, bl_iv3 };

		head = (RoundImageView) findViewById(R.id.new_main_order_detail_grab_head);
		name = (TextView) findViewById(R.id.new_main_order_detail_grab_name);
		sex = (ImageView) findViewById(R.id.new_main_order_detail_grab_sex);
		phone = (TextView) findViewById(R.id.new_main_order_detail_grab_phone);
		identityPic = (ImageView) findViewById(R.id.new_main_order_detail_grab_identify_pic);
		identify = (TextView) findViewById(R.id.new_main_order_detail_grab_identify);

		include = findViewById(R.id.new_main_order_detail_include);
		ago = (TextView) include
				.findViewById(R.id.order_main_detail_include_ago);
		title = (TextView) include
				.findViewById(R.id.order_main_detail_include_title);
		price = (TextView) include
				.findViewById(R.id.order_main_detail_include_price);
		label1 = (ImageView) include
				.findViewById(R.id.order_main_detail_include_label1);
		label2 = (ImageView) include
				.findViewById(R.id.order_main_detail_include_label2);
		content = (TextView) include
				.findViewById(R.id.order_main_detail_include_content);
		gv = (MGridView) include
				.findViewById(R.id.order_main_detail_include_gv);
		address = (TextView) include
				.findViewById(R.id.order_main_detail_include_address);
		addrIv = (ImageView) include
				.findViewById(R.id.order_main_detail_include_address_iv);
		date = (TextView) include
				.findViewById(R.id.order_main_detail_include_finishtime);
		total = (TextView) include
				.findViewById(R.id.order_main_detail_include_total);
		timeHint = (TextView) include
				.findViewById(R.id.order_main_detail_include_hint);
		checkMap = (TextView) include
				.findViewById(R.id.order_main_detail_include_checkMap);

		agreement = (LinearLayout) findViewById(R.id.new_main_order_detail_agreement);
		cb = (CheckBox) findViewById(R.id.new_main_order_detail_cb);
		cbtv = (TextView) findViewById(R.id.new_main_order_detail_agreementtv);
		eventIv = (UpMarqueeImageView) findViewById(R.id.new_main_order_detail_headline_img);
		eventTv = (UpMarqueeTextView) findViewById(R.id.new_main_order_detail_headline_text);
		eventRL = (RelativeLayout) findViewById(R.id.new_main_order_detail_eventrl);

		back.setOnClickListener(this);
		share.setOnClickListener(this);
		phone.setOnClickListener(this);
		grab.setOnClickListener(this);
		bid.setOnClickListener(this);
		mb_cancel.setOnClickListener(this);
		head.setOnClickListener(this);
		agreement.setOnClickListener(this);
		bl_iv1.setOnClickListener(this);
		bl_iv2.setOnClickListener(this);
		bl_iv2.setOnClickListener(this);
		menu_share.setOnClickListener(this);
		menu_report.setOnClickListener(this);
		checkMap.setOnClickListener(this);
		eventRL.setOnClickListener(this);
		
		instance = this;
		category = getIntent().getExtras().getString("category");
		oid = getIntent().getExtras().getString("id");
		uid = new Tools().getUserId(this);
		System.out.println("category ---> " + category);
		System.out.println("id orderdetail---> " + oid);

		getImageFromAssetsFile(this, "logo.png");

		if (category.equals("0")){ // 抢单
			grabHttpClient(oid, uid);
			grab.setVisibility(View.VISIBLE);
		}else{
			// 报价
			bidHttpClient(oid, uid);
			total.setVisibility(View.VISIBLE);
		}
	}

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					System.out.println("startTime ---> " + orderStartTime);
					System.out.println("endTime ---> " + orderEndTime);
					System.out.println("currentTime ---> "
							+ new Tools().Today());
					long com1 = new Tools().compareDateWithTime(orderEndTime,
							orderStartTime);
					long com2 = new Tools().compareDateWithTime(
							new Tools().Today(), orderStartTime);
					System.out.println("com1 ---> " + com1);
					System.out.println("com2 ---> " + com2);
					float com = ((float) com2 / (float) com1) * 100;
					System.out.println("com ---> " + com);
					if (com >= 80){
						timeHint.setVisibility(View.VISIBLE);
					}
					final HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					ImageLoader.getInstance().displayImage(
							hashMap.get("releaseUserImg"), head);
					if (hashMap.get("releaseUserSex").equals("1")){ // 男
						sex.setImageResource(R.drawable.male);
					}
					if (hashMap.get("identity").equals("1")){ // 实名认证
						identify.setText("已实名认证");
					}else{
						identify.setText("未实名认证");
						identify.setTextColor(Color.rgb(154, 154, 154));
						identityPic.setImageResource(R.drawable.identity_grey);
					}
					if (hashMap.get("timeout").equals("1")){ // 超时赔付
						label2.setVisibility(View.VISIBLE);
						label2.setOnTouchListener(new OnTouchListener(){

							@Override
							public boolean onTouch(View v, MotionEvent event){
								// TODO Auto-generated method stub
								new HintPopUpWindow(
										MainOrderDetailActivity.this, v, "8",
										(int) event.getRawX(), (int) event
												.getRawY(), true);
								return true;
							}
						});
					}else{
						label2.setVisibility(View.GONE);
					}
					if (hashMap.get("label").equals("1")){ // song
						label1.setImageResource(R.drawable.zhu);
					}else if (hashMap.get("label").equals("2")){ // che
						label1.setImageResource(R.drawable.xiu);
					}else if (hashMap.get("label").equals("3")){ // xiu
						label1.setImageResource(R.drawable.zhi);
					}else if (hashMap.get("label").equals("4")){ // zhi
						label1.setImageResource(R.drawable.che);
					}else if (hashMap.get("label").equals("5")){ // zhu
						label1.setImageResource(R.drawable.song);
					}else if (hashMap.get("label").equals("6")){ // zhu
						label1.setImageResource(R.drawable.jia);
					}
					label1.setOnTouchListener(new OnTouchListener(){

						@Override
						public boolean onTouch(View v, MotionEvent event){
							// TODO Auto-generated method stub
							if (hashMap.get("label").equals("1")){
								new HintPopUpWindow(
										MainOrderDetailActivity.this, v, "1",
										(int) event.getRawX(), (int) event
												.getRawY(), true);
							}else if (hashMap.get("label").equals("2")){
								new HintPopUpWindow(
										MainOrderDetailActivity.this, v, "2",
										(int) event.getRawX(), (int) event
												.getRawY(), true);
							}else if (hashMap.get("label").equals("3")){
								new HintPopUpWindow(
										MainOrderDetailActivity.this, v, "3",
										(int) event.getRawX(), (int) event
												.getRawY(), true);
							}else if (hashMap.get("label").equals("4")){
								new HintPopUpWindow(
										MainOrderDetailActivity.this, v, "4",
										(int) event.getRawX(), (int) event
												.getRawY(), true);
							}else if (hashMap.get("label").equals("5")){
								new HintPopUpWindow(
										MainOrderDetailActivity.this, v, "5",
										(int) event.getRawX(), (int) event
												.getRawY(), true);
							}
							return true;
						}
					});
					name.setText(hashMap.get("releaseUserName"));
					title.setText(hashMap.get("title"));
					content.setText(hashMap.get("description"));
					if (hashMap.get("method").equals("1")){// 线下
						address.setText(hashMap.get("address"));
						checkMap.setVisibility(View.VISIBLE);
					}else{
						addrIv.setImageResource(R.drawable.xianshang);
						address.setText("线上完成");
					}
					date.setText(hashMap.get("endDate"));
					ago.setText(hashMap.get("createDate"));
					if (category.equals("1")){// 报价
						price.setText("竞价中");
					}else{
						price.setText(hashMap.get("price") + "元");
					}
					phone.setText(hashMap.get("releaseUserPhone").substring(0,
							3)
							+ "****"
							+ hashMap.get("releaseUserPhone").subSequence(7,
									hashMap.get("releaseUserPhone").length()));
					if (category.equals("1")){
						total.setVisibility(View.VISIBLE);
						total.setText("总需" + hashMap.get("nubmer") + "人");
					}
				break;
				case 2:
					ArrayList<String> list = (ArrayList<String>) msg.obj;
					if (list.size() == 3){
						gv.setColumnWidth(new Tools()
								.getScreenWidth(MainOrderDetailActivity.this) / 3);
						gv.setNumColumns(3);
					}else{
						gv.setColumnWidth(new Tools()
								.getScreenWidth(MainOrderDetailActivity.this) / 2);
						gv.setNumColumns(2);
					}
					MainGrabGvInLvAdapter adapter = new MainGrabGvInLvAdapter(
							MainOrderDetailActivity.this, list, parent);
					gv.setAdapter(adapter);
				break;
				case 3:// 报价人列表
					if (qList.size() != 0){
						for(int ii = 0, j = qList.size(); ii < j; ii++){
							bl_iv[ii].setVisibility(View.VISIBLE);
							ImageLoader.getInstance().displayImage(
									qList.get(ii).getImg(), bl_iv[ii]);
						}
					}
				break;
				case 4:// 我的报价
					final HashMap<String, String> map = (HashMap<String, String>) msg.obj;
					if (map.get("userid").equals("")){ // 我没有报价 显示报价人列表
						bidList.setVisibility(View.VISIBLE);
					}else{
						myBid.setVisibility(View.VISIBLE);
						ImageLoader.getInstance().displayImage(map.get("img"),
								mb_head);
						mb_bid.setText("我出价" + map.get("price") + "元");
						if (map.get("userState").equals("0")){
							mb_status.setVisibility(View.VISIBLE);
							mb_status.setText("<等待选择>");
						}else{
							mb_status.setVisibility(View.VISIBLE);
							mb_status.setText("<您已被选中>");
						}
					}
				break;
				case 5:
					refreshPage();
				break;
				case 55:// 当前活动
					if (nowEvents.size() == 0){
						eventRL.setVisibility(View.GONE);
					}else{
						eventRL.setVisibility(View.VISIBLE);
						handler.post(task);
					}
				break;
				case 100: // 取消订单确定
					cancelHttpClient(oid, uid);
				break;
				case 200: // 确定抢单
					grabCommitHttpClient(oid, uid);
				break;
				case 301:// 已选择列表
					ArrayList<HashMap<String, String>> sList = (ArrayList<HashMap<String, String>>) msg.obj;
					Log.e("sList ", "" + sList.size());
					bl_total.setText("已有" + (qList.size() + sList.size())
							+ "人报价");
				break;
				default:
				break;
			}
		}

	};

	private Runnable task = new Runnable() {
		public void run() {
			// TODO Auto-generated method stub
			handler.postDelayed(this, 5 * 1000);// 设置延迟时间，此处是5秒
			if (count == nowEvents.size()) {
				count = 0;
			}
			if(nowEvents.size() != 0){
				NowEvent nowEvent = nowEvents.get(count);
				// title_one.setTextColor(Integer.parseInt(headlines.getTitlecolor()));
				// title_one.setTextColor(0xff123456);
				ImageLoader.getInstance().displayImage(nowEvent.getActivityImg(),
						eventIv);
				// headline_img.setImageDrawable(getResources().getDrawable(
				// R.drawable.year));
				eventTv.setText(nowEvent.getActivityTile());
				// headline_text.setText(headlines.getText());

				count++;
			}
		}
	};
	
	
	private void refreshPage(){
		onCreate(null);
		if (category.equals("0")){ // 抢单
			grabHttpClient(oid, uid);
			grab.setVisibility(View.VISIBLE);
		}else{
			// 报价
			bidHttpClient(oid, uid);
		}
	}

	/**
	 * 取消报价 接口
	 * 
	 * @param
	 */
	private void cancelHttpClient(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("id", id);
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(MainOrderDetailActivity.this,
					Config.USER_TO_BID_CANCEL_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String message = "";
								String str = new String(arg2);
								System.out.println("取消报价返回 ---> " + str);
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										Toast.makeText(
												MainOrderDetailActivity.this,
												"报价已取消!", Toast.LENGTH_SHORT)
												.show();

										Message msg = handler.obtainMessage();
										msg.what = 5;
										msg.sendToTarget();
									}else{
										Toast.makeText(
												MainOrderDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(MainOrderDetailActivity.this,
										"取消报价失败,请重试!", Toast.LENGTH_SHORT)
										.show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(MainOrderDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}

	private void grabHttpClient(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("id", id);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.ORDER_DETAIL_GRAB_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){ // 成功
								String str = new String(arg2);
								System.out.println("抢单详情json结果 ---> " + str);
								String message = "";
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										JSONObject order = data
												.getJSONObject("GrobOrder");
										HashMap<String, String> hashMap = new HashMap<String, String>();
										shareUrl = order.getString("shareUrl");
										hashMap.put("address",
												order.getString("address"));
										hashMap.put("category",
												order.getString("category"));
										hashMap.put("countdown",
												order.getString("countdown"));
										hashMap.put("description",
												order.getString("description"));
										hashMap.put("endDate",
												order.getString("endDate"));
										orderEndTime = order
												.getString("endDate");
										hashMap.put("grabOrderState",
												order.getString("orderState"));
										orderState = order
												.getString("orderState");
										hashMap.put("grabOrderUserId", order
												.getString("grabOrderUserId"));
										gid = order
												.getString("grabOrderUserId");
										hashMap.put("grabOrderUserImg", order
												.getString("grabOrderUserImg"));
										hashMap.put("grabOrderUserName", order
												.getString("grabOrderUserName"));
										hashMap.put(
												"grabOrderUserPhone",
												order.getString("grabOrderUserPhone"));
										hashMap.put("id", order.getString("id"));
										oid = order.getString("id");
										hashMap.put("method",
												order.getString("method"));
										hashMap.put("nubmer",
												order.getString("nubmer"));
										hashMap.put("price",
												order.getString("price"));
										hashMap.put("releaseUserId", order
												.getString("releaseUserId"));
										rid = order.getString("releaseUserId");
										hashMap.put("releaseUserImg", order
												.getString("releaseUserImg"));
										hashMap.put("releaseUserName", order
												.getString("releaseUserName"));
										hashMap.put("releaseUserPhone", order
												.getString("releaseUserPhone"));
										hashMap.put("releaseUserSex", order
												.getString("releaseUserSex"));
										hashMap.put("startDate",
												order.getString("startDate"));
										orderStartTime = order
												.getString("startDate");
										hashMap.put("title",
												order.getString("title"));
										hashMap.put("label",
												order.getString("label"));
										hashMap.put("identity",
												order.getString("identity"));
										hashMap.put("timeout",
												order.getString("timeout"));
										hashMap.put("createDate",
												order.getString("createDate"));
										hashMap.put("lat",
												order.getString("lat"));
										orderLat = Double.valueOf(order
												.getString("lat"));
										hashMap.put("lng",
												order.getString("lng"));
										orderLng = Double.valueOf(order
												.getString("lng"));

										// 信息展示
										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();

										JSONArray activitys = data
												.getJSONArray("activity");
										nowEvents = new ArrayList<NowEvent>();
										for(int i = 0, j = activitys.length(); i < j; i++){
											JSONObject job1 = activitys
													.optJSONObject(i);
											NowEvent event = new NowEvent();
											event.setActivityId(job1
													.getString("activityId"));
											event.setActivityTile(job1
													.getString("activityTitle"));
											event.setActivityImg(job1
													.getString("activityImg"));
											nowEvents.add(event);
										}

										Message msg55 = handler.obtainMessage();
										msg55.what = 55;
										msg55.sendToTarget();

										JSONArray imgs = order
												.getJSONArray("imgList");
										if (imgs.length() != 0){
											ArrayList<String> list = new ArrayList<String>();
											for(int i = 0, j = imgs.length(); i < j; i++){
												String pic = imgs.optString(i);
												list.add(pic);
											}
											// 图片展示
											Message msg1 = handler
													.obtainMessage();
											msg1.what = 2;
											msg1.obj = list;
											msg1.sendToTarget();
										}

										pd.dismiss();
									}else{// 提交失败
										pd.dismiss();
										Toast.makeText(
												MainOrderDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}

								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							}else{
								pd.dismiss();
								Toast.makeText(MainOrderDetailActivity.this,
										"请求失败,请重试！", Toast.LENGTH_SHORT).show();
								MainOrderDetailActivity.this.finish();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(MainOrderDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}

	private void bidHttpClient(String id, String uid){

		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("id", id);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.ORDER_DETAIL_BID_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){ // 成功
								String str = new String(arg2);
								System.out.println("报价详情json结果 ---> " + str);
								String message = "";
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										JSONObject order = data
												.getJSONObject("order");
										HashMap<String, String> hashMap = new HashMap<String, String>();
										shareUrl = order.getString("shareUrl");
										hashMap.put("address",
												order.getString("address"));
										hashMap.put("category",
												order.getString("category"));
										hashMap.put("countdown",
												order.getString("countdown"));
										hashMap.put("description",
												order.getString("description"));
										hashMap.put("endDate",
												order.getString("endDate"));
										orderEndTime = order
												.getString("endDate");
										hashMap.put("id", order.getString("id"));
										oid = order.getString("id");
										hashMap.put("method",
												order.getString("method"));
										hashMap.put("nubmer",
												order.getString("nubmer"));
										hashMap.put("orderState",
												order.getString("orderState"));
										orderState = order
												.getString("orderState");
										hashMap.put("price",
												order.getString("price"));
										hashMap.put("releaseUserId", order
												.getString("releaseUserId"));
										rid = order.getString("releaseUserId");
										hashMap.put("releaseUserImg", order
												.getString("releaseUserImg"));
										hashMap.put("releaseUserName", order
												.getString("releaseUserName"));
										hashMap.put("releaseUserPhone", order
												.getString("releaseUserPhone"));
										hashMap.put("startDate",
												order.getString("startDate"));
										orderStartTime = order
												.getString("startDate");
										hashMap.put("title",
												order.getString("title"));
										hashMap.put("userState",
												order.getString("userState"));
										hashMap.put("label",
												order.getString("label"));
										hashMap.put("identity",
												order.getString("identity"));
										hashMap.put("timeout",
												order.getString("timeout"));
										hashMap.put("createDate",
												order.getString("createDate"));
										hashMap.put("releaseUserSex", order
												.getString("releaseUserSex"));
										hashMap.put("lat",
												order.getString("lat"));
										orderLat = Double.valueOf(order
												.getString("lat"));
										hashMap.put("lng",
												order.getString("lng"));
										orderLng = Double.valueOf(order
												.getString("lng"));

										// 信息展示
										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();

										JSONArray imgs = order
												.getJSONArray("imgList");
										if (imgs.length() != 0){
											ArrayList<String> list = new ArrayList<String>();
											for(int i = 0, j = imgs.length(); i < j; i++){
												String pic = imgs.optString(i);
												list.add(pic);
											}
											// 图片展示
											Message msg1 = handler
													.obtainMessage();
											msg1.what = 2;
											msg1.obj = list;
											msg1.sendToTarget();
										}

										// 我的报价
										JSONObject userQuote = order
												.getJSONObject("userQuote");
										hashMap = new HashMap<String, String>();
										hashMap.put("id",
												userQuote.getString("id"));
										hashMap.put("img",
												userQuote.getString("img"));
										hashMap.put("price",
												userQuote.getString("price"));
										hashMap.put("userid",
												userQuote.getString("userid"));
										hashMap.put("username",
												userQuote.getString("username"));
										hashMap.put("userState", userQuote
												.getString("userState"));

										Message msg4 = handler.obtainMessage();
										msg4.what = 4;
										msg4.obj = hashMap;
										msg4.sendToTarget();

										// 报价人列表
										JSONArray quoteList = order
												.getJSONArray("quoteList");
										qList = new ArrayList<Quote>();
										for(int i = 0, j = quoteList.length(); i < j; i++){
											JSONObject jObject = quoteList
													.optJSONObject(i);
											Quote quoteMap = new Quote();
											quoteMap.setId(jObject
													.getString("id"));
											quoteMap.setImg(jObject
													.getString("img"));
											quoteMap.setPrice(jObject
													.getString("price"));
											quoteMap.setUserid(jObject
													.getString("userid"));
											quoteMap.setUsername(jObject
													.getString("username"));

											qList.add(quoteMap);
										}
										// 显示报价fragment
										Message msg2 = handler.obtainMessage();
										msg2.what = 3;
										msg2.obj = qList;
										msg2.sendToTarget();

										JSONArray selected = order
												.getJSONArray("selected");
										ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
										for(int i = 0, j = selected.length(); i < j; i++){
											JSONObject sel = selected
													.optJSONObject(i);
											hashMap = new HashMap<String, String>();
											hashMap.put("id",
													sel.getString("id"));
											hashMap.put("userid",
													sel.getString("userid"));
											hashMap.put("username",
													sel.getString("username"));
											hashMap.put("img",
													sel.getString("img"));
											hashMap.put("price",
													sel.getString("price"));
											hashMap.put("userState",
													sel.getString("userState"));
											hashMap.put("isComment",
													sel.getString("isComment"));

											list.add(hashMap);
										}
										Message msg3 = handler.obtainMessage();
										msg3.what = 301;
										msg3.obj = list;
										msg3.sendToTarget();
										
										JSONArray activitys = data
												.getJSONArray("activity");
										nowEvents = new ArrayList<NowEvent>();
										for(int i = 0, j = activitys.length(); i < j; i++){
											JSONObject job1 = activitys
													.optJSONObject(i);
											NowEvent event = new NowEvent();
											event.setActivityId(job1
													.getString("activityId"));
											event.setActivityTile(job1
													.getString("activityTitle"));
											event.setActivityImg(job1
													.getString("activityImg"));
											nowEvents.add(event);
										}

										Message msg55 = handler.obtainMessage();
										msg55.what = 55;
										msg55.sendToTarget();

										pd.dismiss();
									}else{// 提交失败
										pd.dismiss();
										Toast.makeText(
												MainOrderDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}

								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							}else{
								pd.dismiss();
								Toast.makeText(MainOrderDetailActivity.this,
										"请求失败,请重试！", Toast.LENGTH_SHORT).show();
								MainOrderDetailActivity.this.finish();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(MainOrderDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}

	private void share(){
		statistics();
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("找事吧");
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(shareUrl);
		// text是分享文本，所有平台都需要这个字段
		oks.setText("有事就找它,没事找事吧");
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath(this.getFilesDir().getPath() + "/data/logo.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(shareUrl);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(shareUrl);

		// 启动分享GUI
		oks.show(this);
	}

	/**
	 * 统计点击分享总数
	 * 
	 * @param
	 */
	private void statistics(){
		MobclickAgent.onEvent(this, "share_total");
	}

	private Bitmap getImageFromAssetsFile(Context context, String fileName){
		// 获取应用的包名
		String packageName = context.getPackageName();
		// 定义存放这些图片的内存路径
		String path = this.getFilesDir().getPath() + "/data/";
		// 如果这个路径不存在则新建
		File file = new File(path);
		Bitmap image = null;
		boolean isExist = file.exists();
		if (!isExist){
			file.mkdirs();
		}
		// 获取assets下的资源
		AssetManager am = context.getAssets();
		try{
			// 图片放在img文件夹下
			InputStream is = am.open("img/" + fileName);
			image = BitmapFactory.decodeStream(is);
			FileOutputStream out = new FileOutputStream(path + fileName);
			// 这个方法非常赞
			image.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			is.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context){
		String CID = new Tools().getCID(this);
		String notificationText = new Tools().getNotificationText(this);
		Boolean isFirstStart = new Tools()
		.getisFirstStart(MainOrderDetailActivity.this);
		unBindAlias(new Tools().getUserId(this), new Tools().getCID(this), this);
		SharedPreferences sharedPre = this.getSharedPreferences("user",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(this, parent,
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
						public void onSuccess(int arg0, Header[]

						arg1, byte[] arg2){
							if (arg0 == 200){
								String str = new String

								(arg2);
								System.out.println("解除绑定账户接口返回 ---> " + str);
								String message = "";
								try{
									JSONObject result =

									new JSONObject(str);
									String state =

									result.getString("state");
									message =

									result.getString("msg");
									if (state.equals

									("success")){
										JSONObject

										data = result

										.getJSONObject("data");
										Log.v("别人资料", data.toString());
										String

										results = data

										.getString("result");
										if

										("0".equals(results)){

											Log.v("别人资料", "提交失败");
										}else{

											Log.v("别人资料", "提交成功");
										}
									}
								}catch(JSONException e){
									e.printStackTrace

									();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[]

						arg1, byte[] arg2, Throwable

						arg3){
						}
					});
		}catch(JSONException e){
			e.printStackTrace();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
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

			if ("1".equals(new Tools().getNotificationType(context))
					|| "2".equals(new Tools().getNotificationType

					(context))){
				if (pps[0] != null && pps[0].isShowing()){
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(MainOrderDetailActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"MainOrderDetailActivity");
				pps[0] = pp;
			}else if ("3".equals(new Tools().getNotificationType(context))){
				if (pps[0] != null && pps[0].isShowing()){
					pps[0].dismiss();
				}
				mandatoryExit(context);
			}else{
				NotificationManager nManager = (NotificationManager)

				context.getSystemService

				(context.NOTIFICATION_SERVICE);
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
				notification.setLatestEventInfo(context, "找事吧", new Tools

				().getNotificationText(context), pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, GetOtherInfoActivity.class);
		switch(v.getId()){
			case R.id.back:
				finish();
			break;
			case R.id.title_tv:
				if (!pw_menu.isShowing()){
					int location[] = new int[2];
					share.getLocationInWindow(location);
					pw_menu.showAsDropDown(share, -(WIDTH * 1 / 3
							- (WIDTH - location[0]) - 20), 0);
				}
			break;
			case R.id.new_main_order_detail_grab_phone:

			break;
			case R.id.new_main_order_detail_grab:
				if (new Tools().getUserId(this).equals(rid)){
					Toast.makeText(this, "不能抢自己发的单!", Toast.LENGTH_SHORT)
							.show();
				}else{
					new ContentPopUpWindow(this, parent, "确认要抢此订单么?~", "grab");
				}

			break;
			case R.id.new_main_order_detail_bid:
				if (new Tools().getUserId(this).equals(rid)){
					Toast.makeText(this, "不能对自己发的单报价!", Toast.LENGTH_SHORT)
							.show();
				}else{
					Intent intent1 = new Intent(MainOrderDetailActivity.this,
							ToBidActivity.class);
					intent1.putExtra("id", oid);
					intent1.putExtra("uid", uid);
					startActivityForResult(intent1, 100);
				}

			break;
			case R.id.new_main_order_detail_bid_frame_cancel:
				new ContentPopUpWindow(this, parent, "确认取消报价么?~", "bidcancel");
			break;
			case R.id.new_main_order_detail_grab_head:
				intent.putExtra("otherUserId", rid);
				startActivity(intent);
			break;
			case R.id.new_main_order_detail_agreement:
				startActivity(new Intent(MainOrderDetailActivity.this,
						AgreementActivity.class));
			// if(cb.isChecked()){
			// cb.setChecked(false);
			// cbtv.setTextColor(Color.rgb(108, 108, 108));
			// }else{
			// cb.setChecked(true);
			// cbtv.setTextColor(Color.rgb(34, 181, 112));
			// }
			break;
			case R.id.new_main_order_detail_bidlist_iv1:
				intent.putExtra("otherUserId", qList.get(0).getUserid());
				startActivity(intent);
			break;
			case R.id.new_main_order_detail_bidlist_iv2:
				intent.putExtra("otherUserId", qList.get(1).getUserid());
				startActivity(intent);
			break;
			case R.id.new_main_order_detail_bidlist_iv3:
				intent.putExtra("otherUserId", qList.get(2).getUserid());
				startActivity(intent);
			break;
			case R.id.menu_share:
				pw_menu.dismiss();
				share();
			break;
			case R.id.menu_report:
				pw_menu.dismiss();
				Intent intent2 = new Intent(this, ReportActivity.class);
				intent2.putExtra("oid", oid);
				startActivity(intent2);
			break;
			case R.id.order_main_detail_include_checkMap:// 查看地图
				Intent intent3 = new Intent(this, BaiDuMapActivity.class);
				intent3.putExtra("lat", orderLat);
				intent3.putExtra("lng", orderLng);
				startActivity(intent3);
			break;
			case R.id.new_main_order_detail_eventrl:
				new EventPopUpWindow(MainOrderDetailActivity.this, parent, nowEvents);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 100: // 我要报价
				if (resultCode == RESULT_OK){ // 报价成功
					System.out.println("on result");
					bidList.setVisibility(View.GONE);
					myBid.setVisibility(View.VISIBLE);
					String img = data.getExtras().getString("img");
					String price = data.getExtras().getString("price");
					ImageLoader.getInstance().displayImage(img, mb_head);
					mb_bid.setText("我出价" + price + "元");
					mb_status.setText("<等待选择>");
				}else{ // 取消报价
					Toast.makeText(MainOrderDetailActivity.this, "报价取消!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			default:
			break;
		}
	}

	private void grabCommitHttpClient(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(MainOrderDetailActivity.this);
		JSONObject job = new JSONObject();
		try{
			job.put("id", id);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString(), "utf-8");

			HttpClient.post(MainOrderDetailActivity.this, Config.USER_GRAB_URL,
					se, new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							String message = "";
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("抢单提交结果  ---> " + str);
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										String result = data
												.getString("result");
										if (result.equals("1")){
											Toast.makeText(
													MainOrderDetailActivity.this,
													"抢单成功!", Toast.LENGTH_SHORT)
													.show();
											MainOrderDetailActivity.this
													.finish();
										}else{
											Toast.makeText(
													MainOrderDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										//
										Toast.makeText(
												MainOrderDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(MainOrderDetailActivity.this,
										message, Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(MainOrderDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}
}
