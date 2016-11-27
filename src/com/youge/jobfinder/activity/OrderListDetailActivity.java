/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import model.NowEvent;

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
import view.MGridView;
import view.MarqueeTV;
import view.RoundImageView;
import view.ScrollListView;
import view.UpMarqueeImageView;
import view.UpMarqueeTextView;
import adapter.ConvenienceFacilityGVAdapter;
import adapter.MainGrabGvInLvAdapter;
import adapter.MyEmployeeSelectedAdapter;
import adapter.OrderStatusDetailLVAdapter;
import adapter.PaySuccessEmployeeSelectGVAdapter;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.NoCopySpan.Concrete;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import baidu.BaiDuMapActivity;
import baidu.BaiDuMapTraceActivity;

import com.alipay.android.phone.mrpc.core.s;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.LocationService;
import com.youge.jobfinder.PayActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class OrderListDetailActivity extends BaseActivity implements
		OnClickListener{

	private RelativeLayout parent;
	private ImageView back;

	/**
	 * 底部按钮
	 */
	private FrameLayout bottomBtn; // 底部framelayout
	private LinearLayout bottomTwoBtn; // 催单, 完成
	private TextView twoBtn1, twoBtn2;
	private TextView bottomCommit; // 底部确认
	private MarqueeTV adtv; // 底部滚动
	private TextView bottomGrab; // 底部抢单
	private LinearLayout bidList; // 报价人列表
	private RoundImageView bid_iv1, bid_iv2, bid_iv3; // 3个报价人头像
	private TextView bidTotal; // 多少人报价
	private TextView bottomBid; // 底部报价
	private RoundImageView[] bid_iv;

	/**
	 * 我的报价
	 */
	private LinearLayout myBid; // 我的报价
	private RoundImageView myBidHead; // 我的报价 头像
	private TextView myBidPrice; // 我的出价
	private TextView myBidStatus; // 我的选择状态
	private TextView myBidCancel; // 我的报价 取消报价

	/**
	 * 顶部状态栏 我的抢单(3个状态)
	 */
	private LinearLayout myGrabTop;
	private ImageView gTopLeaving; // 我出发啦
	private TextView gTopLine1, gTopLine2;
	private ImageView gTopOnRoad; // 在途中
	private ImageView gTopDone; // 已完成
	private TextView gTopClickable;

	/**
	 * 顶部状态栏 我的发单(4个状态)
	 */
	private LinearLayout myPostTop;
	private ImageView pTopSubmit; // 订单已提交
	private TextView pTopLine1, pTopLine2, pTopLine3;
	private ImageView pTopPay; // 订单已支付
	private ImageView pTopInprocess; // 正在进行
	private ImageView pTopDone; // 订单已完成

	private TextView cancelTop;
	private View include;
	/**
	 * include
	 */
	private TextView includeTitle;
	private ImageView label1, label2;
	private TextView includePrice;
	private TextView includeContent;
	private MGridView includeGv;
	private ImageView includeAddrPic;
	private TextView includeAddr;
	private TextView includeDate;
	private TextView checkMap;

	private FrameLayout toConfirm;
	private RoundImageView toConfirmHead;
	private TextView toConfirmName;
	private TextView toConfirmCheckResume;
	private TextView toConfirmConfirm, toConfirmCancel;

	/**
	 * 几人报价, 总需几人
	 */
	private FrameLayout countTotal;
	private TextView count; // 几人报价
	private TextView totalNeedTv; // 总需几人

	private ListView selectedLv; // 报价人, 已选择列表
	private ListView chooseLv; // 报价人, 待选择列表
	private ListView commitLv; // 报价人选择确认列表

	private TextView totalPriceTv; // 总价
	private TextView cancelOrder; // 取消订单, 申请退单

	/**
	 * 雇主, 抢单人
	 */
	private FrameLayout who;
	private RoundImageView whoHead;
	private TextView whoName;
	private ImageView whoSex;
	private ImageView whoPhonePic;
	private TextView whoPhone;
	private ImageView whoIdentityPic;
	private TextView whoIdentity;
	private LinearLayout whoStatus; // 抢单人时显示
	private ImageView whoStatusPic;
	private TextView whoStatusTv;
	private LinearLayout grabTrace;

	/**
	 * 评价
	 */
	private LinearLayout comment;
	private TextView commentTitle; // 雇主看时-->我的评价 , 抢单人看时-->雇主对我的评价
	private TextView commentDate;
	private TextView uncomment; // 没有评价时显示
	private LinearLayout commentDetail; // 有评价时显示
	private ImageView commentGrade;
	private TextView commentGradeTv;
	private RatingBar speedRb; // 完成速度
	private RatingBar qualityRb; // 质量
	private TextView commentContent; // 评价内容

	private RelativeLayout eventRL;
	private UpMarqueeImageView eventIv;
	private UpMarqueeTextView eventTv;

	private String oid, category, rid, gid, orderState, commitState, orderType,
			cancelType, bPhone, need, userState, online;
	public static OrderListDetailActivity instance;
	private ArrayList<HashMap<String, String>> qList, sList, cList;
	private double totalPrice;
	private double orderLat, orderLng, userTraceLat,
			userTraceLng;
	private String userLat, userLng;
	private String userTraceName, entityName;
	private LocationService locationService;
	private int totalNeed, updateState;
	private boolean ownPost;

	private registerReceiver receiver;
	private OrderStatusDetailLVAdapter osdAdapter1, osdAdapter2;
	private MyEmployeeSelectedAdapter mesAdapter;
	private PaySuccessEmployeeSelectGVAdapter pseAdapter;

	private ArrayList<NowEvent> nowEvents;
	private int eventsCount = 0;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.new_order_list_detail);
		initView();
		//initLoc();
	}

	private void initView(){
		Exit.getInstance().addActivity(this);
		parent = (RelativeLayout) findViewById(R.id.new_order_list_detail_parent);
		back = (ImageView) findViewById(R.id.back);

		bottomBtn = (FrameLayout) findViewById(R.id.new_order_list_detail_bottom_btn);
		bottomTwoBtn = (LinearLayout) findViewById(R.id.new_order_list_detail_bottom_two_button);
		twoBtn1 = (TextView) findViewById(R.id.new_order_list_detail_bottom_button1);
		twoBtn2 = (TextView) findViewById(R.id.new_order_list_detail_bottom_button2);
		bottomCommit = (TextView) findViewById(R.id.new_order_list_detail_bottom_commit);
		adtv = (MarqueeTV) findViewById(R.id.new_order_list_detail_bottom_adtv);
		bottomGrab = (TextView) findViewById(R.id.new_order_list_detail_bottom_grab);
		bidList = (LinearLayout) findViewById(R.id.new_order_list_detail_bottom_bidlist);
		bid_iv1 = (RoundImageView) findViewById(R.id.new_order_list_detail_bottom_bidlist_iv1);
		bid_iv2 = (RoundImageView) findViewById(R.id.new_order_list_detail_bottom_bidlist_iv2);
		bid_iv3 = (RoundImageView) findViewById(R.id.new_order_list_detail_bottom_bidlist_iv3);
		bidTotal = (TextView) findViewById(R.id.new_order_list_detail_bottom_bidlist_total);
		bottomBid = (TextView) findViewById(R.id.new_order_list_detail_bottom_bid);
		bid_iv = new RoundImageView[] { bid_iv1, bid_iv2, bid_iv3 };

		myBid = (LinearLayout) findViewById(R.id.new_order_list_detail_bottom_mybid);
		myBidHead = (RoundImageView) findViewById(R.id.new_order_list_detail_bottom_mybid_head);
		myBidPrice = (TextView) findViewById(R.id.new_order_list_detail_bottom_mybid_price);
		myBidStatus = (TextView) findViewById(R.id.new_order_list_detail_bottom_mybid_status);
		myBidCancel = (TextView) findViewById(R.id.new_order_list_detail_bottom_mybid_cancel);

		myGrabTop = (LinearLayout) findViewById(R.id.new_order_list_detail_grabtop);
		gTopLeaving = (ImageView) findViewById(R.id.new_order_list_detail_leaving);
		gTopLine1 = (TextView) findViewById(R.id.new_order_list_detail_line1);
		gTopLine2 = (TextView) findViewById(R.id.new_order_list_detail_line2);
		gTopOnRoad = (ImageView) findViewById(R.id.new_order_list_detail_onroad);
		gTopDone = (ImageView) findViewById(R.id.new_order_list_detail_finish);
		gTopClickable = (TextView) findViewById(R.id.new_order_list_detail_grabtop_clickable);

		myPostTop = (LinearLayout) findViewById(R.id.new_order_list_detail_posttop);
		pTopSubmit = (ImageView) findViewById(R.id.new_order_list_detail_posttop_submit);
		pTopLine1 = (TextView) findViewById(R.id.new_order_list_detail_posttop_line1);
		pTopLine2 = (TextView) findViewById(R.id.new_order_list_detail_posttop_line2);
		pTopLine3 = (TextView) findViewById(R.id.new_order_list_detail_posttop_line3);
		pTopPay = (ImageView) findViewById(R.id.new_order_list_detail_posttop_pay);
		pTopInprocess = (ImageView) findViewById(R.id.new_order_list_detail_posttop_inprocess);
		pTopDone = (ImageView) findViewById(R.id.new_order_list_detail_posttop_done);

		cancelTop = (TextView) findViewById(R.id.new_order_list_detail_cancel_top);
		include = findViewById(R.id.new_order_list_detail_include);

		countTotal = (FrameLayout) findViewById(R.id.new_order_list_detail_counttotal);
		count = (TextView) findViewById(R.id.new_order_list_detail_count);
		totalNeedTv = (TextView) findViewById(R.id.new_order_list_detail_totalneed);

		selectedLv = (ListView) findViewById(R.id.new_order_list_detail_total_sel_lv);
		chooseLv = (ListView) findViewById(R.id.new_order_list_detail_total_tochoose_lv);
		commitLv = (ListView) findViewById(R.id.new_order_list_detail_total_commit_lv);

		totalPriceTv = (TextView) findViewById(R.id.new_order_list_detail_total_price);
		cancelOrder = (TextView) findViewById(R.id.new_order_list_detail_cancelorder);

		who = (FrameLayout) findViewById(R.id.order_list_detail_who);
		whoHead = (RoundImageView) findViewById(R.id.order_list_detail_who_head);
		whoName = (TextView) findViewById(R.id.order_list_detail_who_name);
		whoSex = (ImageView) findViewById(R.id.order_list_detail_who_sex);
		whoPhonePic = (ImageView) findViewById(R.id.order_list_detail_who_phonepic);
		whoPhone = (TextView) findViewById(R.id.order_list_detail_who_phone);
		whoIdentityPic = (ImageView) findViewById(R.id.new_order_list_detail_employer_identify_pic);
		whoIdentity = (TextView) findViewById(R.id.new_order_list_detail_employer_identify);
		whoStatus = (LinearLayout) findViewById(R.id.new_order_list_detail_who_status);
		whoStatusPic = (ImageView) findViewById(R.id.new_order_list_detail_who_status_pic);
		whoStatusTv = (TextView) findViewById(R.id.new_order_list_detail_who_status_text);
		grabTrace = (LinearLayout) findViewById(R.id.new_order_list_detail_trace);

		toConfirm = (FrameLayout) findViewById(R.id.order_list_detail_who_tocomfirm_frame);
		toConfirmHead = (RoundImageView) findViewById(R.id.order_list_detail_who_tocomfirm_head);
		toConfirmName = (TextView) findViewById(R.id.order_list_detail_who_tocomfirm_name);
		toConfirmCheckResume = (TextView) findViewById(R.id.order_list_detail_who_tocomfirm_checkresume);
		toConfirmConfirm = (TextView) findViewById(R.id.order_list_detail_who_tocomfirm_comfirm);
		toConfirmCancel = (TextView) findViewById(R.id.order_list_detail_who_tocomfirm_cancel);

		comment = (LinearLayout) findViewById(R.id.new_order_list_detail_comment);
		commentTitle = (TextView) findViewById(R.id.new_order_list_detail_comment_title);
		commentDate = (TextView) findViewById(R.id.new_order_list_detail_comment_date);
		uncomment = (TextView) findViewById(R.id.new_order_list_detail_uncomment);
		commentDetail = (LinearLayout) findViewById(R.id.new_order_list_detail_commentdetail);
		commentGrade = (ImageView) findViewById(R.id.new_order_list_detail_commentdetail_grade);
		commentGradeTv = (TextView) findViewById(R.id.new_order_list_detail_commentdetail_text);
		speedRb = (RatingBar) findViewById(R.id.new_order_list_detail_commentdetail_rb1);
		qualityRb = (RatingBar) findViewById(R.id.new_order_list_detail_commentdetail_rb2);
		commentContent = (TextView) findViewById(R.id.new_order_list_detail_commentdetail_content);

		includeTitle = (TextView) include
				.findViewById(R.id.order_list_include_title);
		label1 = (ImageView) include
				.findViewById(R.id.order_list_include_label1);
		label2 = (ImageView) include
				.findViewById(R.id.order_list_include_label2);
		includePrice = (TextView) include
				.findViewById(R.id.order_list_include_price);
		includeContent = (TextView) include
				.findViewById(R.id.order_list_include_content);
		includeGv = (MGridView) include
				.findViewById(R.id.order_list_include_gv);
		includeAddrPic = (ImageView) include
				.findViewById(R.id.order_list_include_addr_pic);
		includeAddr = (TextView) include
				.findViewById(R.id.order_list_include_address);
		includeDate = (TextView) include
				.findViewById(R.id.order_list_include_finishtime);
		checkMap = (TextView) include
				.findViewById(R.id.order_list_include_checkMap);
		eventRL = (RelativeLayout) findViewById(R.id.new_order_list_detail_eventrl);
		eventIv = (UpMarqueeImageView) findViewById(R.id.new_order_list_detail_headline_img);
		eventTv = (UpMarqueeTextView) findViewById(R.id.new_order_list_detail_headline_text);

		back.setOnClickListener(this);
		twoBtn2.setOnClickListener(this);
		twoBtn1.setOnClickListener(this);
		bottomCommit.setOnClickListener(this);
		bottomGrab.setOnClickListener(this);
		bid_iv1.setOnClickListener(this);
		bid_iv2.setOnClickListener(this);
		bid_iv3.setOnClickListener(this);
		bottomBid.setOnClickListener(this);
		myBidCancel.setOnClickListener(this);
		cancelOrder.setOnClickListener(this);
		whoHead.setOnClickListener(this);
		whoPhone.setOnClickListener(this);
		checkMap.setOnClickListener(this);
		toConfirmCheckResume.setOnClickListener(this);
		toConfirmConfirm.setOnClickListener(this);
		toConfirmCancel.setOnClickListener(this);
		eventRL.setOnClickListener(this);
		grabTrace.setOnClickListener(this);

		userLat = new Tools().getCurrentLat(this);
		userLng = new Tools().getCurrentLng(this);
		getData();
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
//			userLat = arg0.getLatitude();
//			userLng = arg0.getLongitude();
//			System.out.println("userLat ---> " + userLat);
//			System.out.println("userLng ---> " + userLng);
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

	private void getData(){
		instance = this;
		qList = new ArrayList<HashMap<String, String>>();
		sList = new ArrayList<HashMap<String, String>>();
		cList = new ArrayList<HashMap<String, String>>();
		category = getIntent().getExtras().getString("category"); // 0-出价(抢单详情),
		// 1-报价(报价详情)
		oid = getIntent().getExtras().getString("oid");
		if (category.equals("0")){
			grabDetailHttpClient(oid, new Tools().getUserId(this));
		}else{
			bidDetailHttpClient(oid, new Tools().getUserId(this));
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
		try{
			unregisterReceiver(receiver);
		}catch(Exception e){
			// TODO: handle exception
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onStop(){
		// TODO Auto-generated method stub
		super.onStop();
//		try{
//			locationService.unregisterListener(mListener); // 注销掉监听
//			locationService.stop(); // 停止定位服务
//		}catch(Exception e){
//			// TODO: handle exception
//		}
	}

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1: // 抢单详情
					final HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					includeTitle.setText(hashMap.get("title"));
					if (hashMap.get("price").equals("0")){// 报价
						includePrice.setText("竞价中");
					}else{
						includePrice.setText(hashMap.get("price") + "元");
					}
					includeContent.setText(hashMap.get("description"));
					includeDate.setText(hashMap.get("endDate"));
					if (hashMap.get("timeout").equals("1")){ // 超时赔付
						label2.setVisibility(View.VISIBLE);
						label2.setOnTouchListener(new OnTouchListener(){

							@Override
							public boolean onTouch(View v, MotionEvent event){
								// TODO Auto-generated method stub
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "8",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
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
										OrderListDetailActivity.this, v, "1",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap.get("label").equals("2")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "2",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap.get("label").equals("3")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "3",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap.get("label").equals("4")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "4",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap.get("label").equals("5")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "5",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}
							return true;
						}
					});
					if (hashMap.get("method").equals("1")){// 线下
						includeAddr.setText(hashMap.get("address"));
						checkMap.setVisibility(View.VISIBLE);
					}else{
						includeAddrPic.setImageResource(R.drawable.xianshang);
						includeAddr.setText("线上完成");
					}
					if (rid.equals(new Tools()
							.getUserId(OrderListDetailActivity.this))){ // 自己发的单
						ImageLoader.getInstance().displayImage(
								hashMap.get("grabOrderUserImg"), whoHead);
						ImageLoader.getInstance().displayImage(
								hashMap.get("grabOrderUserImg"), toConfirmHead);
						whoName.setText(hashMap.get("grabOrderUserName"));
						toConfirmName.setText(hashMap.get("grabOrderUserName"));
						whoPhone.setText(hashMap.get("grabOrderUserPhone"));
						if (hashMap.get("userlat").equals("")
								|| hashMap.get("userlng").equals("") || hashMap.get("method").equals("0")){
							grabTrace.setVisibility(View.GONE);
						}else{
							if(gid.length() != 0 && orderState.equals("3")){
								grabTrace.setVisibility(View.VISIBLE);
								userTraceLat = Double.valueOf(hashMap
										.get("userlat"));
								userTraceLng = Double.valueOf(hashMap
										.get("userlng"));
							}else{
								grabTrace.setVisibility(View.GONE);
							}
						}
						if (hashMap.get("userState").equals("1")){// 1出发，2正
																	// 在途中，3完成
							whoStatus.setVisibility(View.VISIBLE);
							whoStatusPic
									.setImageResource(R.drawable.order_status_leaving);
							whoStatusTv.setText("已出发");
						}else if (hashMap.get("userState").equals("2")){
							whoStatus.setVisibility(View.VISIBLE);
							whoStatusPic
									.setImageResource(R.drawable.order_status_onroad);
							whoStatusTv.setText("在途中");
						}else if (hashMap.get("userState").equals("3")){
							whoStatus.setVisibility(View.VISIBLE);
							whoStatusPic
									.setImageResource(R.drawable.order_status_done);
							whoStatusTv.setText("已完成");
						}
						ownPost = true;
					}else{
						ownPost = false;
						ImageLoader.getInstance().displayImage(
								hashMap.get("releaseUserImg"), whoHead);
						whoName.setText(hashMap.get("releaseUserName"));
						whoPhone.setText(hashMap.get("releaseUserPhone"));
						if (hashMap.get("releaseUserSex").equals("1")){
							whoSex.setImageResource(R.drawable.male);
						}else{
							whoSex.setImageResource(R.drawable.famale);
						}
						if (hashMap.get("identity").equals("1")){ // 实名认证
							whoIdentity.setText("已实名认证");
						}else{
							whoIdentity.setText("未实名认证");
							whoIdentityPic
									.setImageResource(R.drawable.identity_grey);
							whoIdentity.setTextColor(Color.rgb(154, 154, 154));
						}
					}

					// if (where.equals("FillOrder")){
					// myPostTop.setVisibility(View.VISIBLE);
					// bottomBtn.setVisibility(View.VISIBLE);
					// bottomCommit.setVisibility(View.VISIBLE);
					// bottomCommit.setText("去支付");
					// commitState = "pay";
					// }else{
					if (orderState.equals("5")){
						cancelTop.setVisibility(View.VISIBLE);
						cancelOrder.setVisibility(View.GONE);
					}else if (orderState.equals("7")){
						cancelTop.setVisibility(View.VISIBLE);
						cancelTop.setText("此订单已过有效时间");
						cancelOrder.setVisibility(View.GONE);
					}
					System.out.println("orderType ---> " + orderType);
					if (orderType.equals("p")){// 我的发单
						if (orderState.equals("6")){ // 待评价
							who.setVisibility(View.VISIBLE);
							bottomBtn.setVisibility(View.VISIBLE);
							bottomCommit.setVisibility(View.VISIBLE);
							bottomCommit.setText("去评价");
							commitState = "comment";
							myPostTop.setVisibility(View.VISIBLE);
							pTopSubmit
									.setImageResource(R.drawable.order_status_submit);
							pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine3
									.setBackgroundResource(R.drawable.dash_green);
							pTopInprocess
									.setImageResource(R.drawable.order_status_inprocess);
							pTopDone.setImageResource(R.drawable.order_status_done);
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("4")){ // 已完成
							who.setVisibility(View.VISIBLE);
							myPostTop.setVisibility(View.VISIBLE);
							pTopSubmit
									.setImageResource(R.drawable.order_status_submit);
							pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine3
									.setBackgroundResource(R.drawable.dash_green);
							pTopInprocess
									.setImageResource(R.drawable.order_status_inprocess);
							pTopDone.setImageResource(R.drawable.order_status_done);
							comment.setVisibility(View.VISIBLE);
							commentGrade.setVisibility(View.VISIBLE);
							commentTitle.setText("我的评价");
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("0")){ // 订单已提交, 未支付
							myPostTop.setVisibility(View.VISIBLE);
							cancelOrder.setVisibility(View.VISIBLE);
							cancelType = "employer_cancel";
							bottomBtn.setVisibility(View.VISIBLE);
							bottomCommit.setVisibility(View.VISIBLE);
							bottomCommit.setText("去支付");
							commitState = "pay";
						}else if (orderState.equals("1")){ // 待报价 待抢单
							myPostTop.setVisibility(View.VISIBLE);
							pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1.setTextColor(Color.rgb(204, 255, 255));
							cancelOrder.setVisibility(View.VISIBLE);
							cancelType = "employer_cancel";
						}else if (orderState.equals("2")){
							myPostTop.setVisibility(View.VISIBLE);
							pTopSubmit
									.setImageResource(R.drawable.order_status_submit);
							pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							cancelTop.setVisibility(View.GONE);
							cancelOrder.setVisibility(View.VISIBLE);
							cancelType = "employer_cancel";
							who.setVisibility(View.GONE);
							toConfirm.setVisibility(View.VISIBLE);
							if (need.equals("0")){
								toConfirmCheckResume.setVisibility(View.GONE);
							}else{
								toConfirmCheckResume
										.setVisibility(View.VISIBLE);
							}
						}
					}else if (orderType.equals("inprocess_post")){ // 进行中
																	// 我的发单
						myPostTop.setVisibility(View.VISIBLE);
						pTopSubmit
								.setImageResource(R.drawable.order_status_submit);
						pTopPay.setImageResource(R.drawable.order_status_pay);
						pTopLine1.setBackgroundResource(R.drawable.dash_green);
						pTopLine2.setBackgroundResource(R.drawable.dash_green);
						pTopInprocess
								.setImageResource(R.drawable.order_status_inprocess);
						who.setVisibility(View.VISIBLE);
						bottomBtn.setVisibility(View.VISIBLE);
						bottomTwoBtn.setVisibility(View.VISIBLE);
						if (userState.equals("3")){
							twoBtn1.setClickable(false);
							twoBtn1.setBackgroundResource(R.drawable.corner_grey);
						}else{
							twoBtn1.setClickable(true);
							twoBtn1.setBackgroundResource(R.drawable.corner_green);
							cancelOrder.setVisibility(View.VISIBLE);
							cancelType = "employer_cancel";
						}
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
						String now = sdf.format(curDate);
						System.out.println("now ---> " + now);
						System.out.println("orderStartTime ---> "
								+ orderStartTime);
						System.out.println("compare ---> "
								+ new Tools().compareDateWithTime(now,
										orderStartTime));
						if (new Tools()
								.compareDateWithTime(now, orderStartTime) <= 1000
								&& new Tools().compareDateWithTime(now,
										orderStartTime) >= 0){
							twoBtn1.setClickable(false);
							twoBtn1.setBackgroundResource(R.drawable.corner_grey);
						}
					}else if (orderType.equals("inprocess_grab")){ // 进行中
																	// 我的抢单
																	// 抢单
						myGrabTop.setVisibility(View.VISIBLE);
						gTopClickable.setVisibility(View.VISIBLE);

						if (hashMap.get("userState").equals("0")){
							updateState = 0;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving_grey);
						}else if (hashMap.get("userState").equals("1")){//
							updateState = 1;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
						}else if (hashMap.get("userState").equals("2")){
							updateState = 2;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
						}else if (hashMap.get("userState").equals("3")){
							updateState = 3;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_done);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);
						}
						gTopLeaving
								.setOnClickListener(OrderListDetailActivity.this);
						gTopOnRoad
								.setOnClickListener(OrderListDetailActivity.this);
						gTopDone.setOnClickListener(OrderListDetailActivity.this);
						who.setVisibility(View.VISIBLE);
						if (!hashMap.get("userState").equals("3")){
							cancelOrder.setVisibility(View.VISIBLE);
							cancelOrder.setText("申请退单");
							cancelType = "employee_cancel";
						}
					}else if (orderType.equals("g")){
						who.setVisibility(View.VISIBLE);
						if (orderState.equals("4")){
							myGrabTop.setVisibility(View.VISIBLE);
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_finish);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							comment.setVisibility(View.VISIBLE);
							commentDetail.setVisibility(View.VISIBLE);
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("6")){
							myGrabTop.setVisibility(View.VISIBLE);
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_finish);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							comment.setVisibility(View.VISIBLE);
							uncomment.setVisibility(View.VISIBLE);
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("2")){
							cancelTop.setVisibility(View.VISIBLE);
							cancelTop.setText("如果5分钟内发单人没有确认订单,系统将自动开始");
							cancelOrder.setVisibility(View.GONE);
							if (hashMap.get("grabOrderUserId").equals("")){
								who.setVisibility(View.GONE);
							}else{
								who.setVisibility(View.VISIBLE);
							}
						}
					}
				// }
				break;
				case 6:// 我的评价
					if (cList.size() != 0){
						HashMap<String, String> cMap = cList.get(0);
						commentDetail.setVisibility(View.VISIBLE);
						if (cMap.get("grade").equals("1")){
							commentGrade
									.setImageResource(R.drawable.comment_good);
							commentGradeTv.setText("好评");
						}else if (cMap.get("grade").equals("2")){
							commentGrade
									.setImageResource(R.drawable.comment_medium);
							commentGradeTv.setText("中评");
						}else{
							commentGrade
									.setImageResource(R.drawable.comment_bad);
							commentGradeTv.setText("差评");
						}
						float rate1 = Float.valueOf(cMap.get("speed"));
						float rate2 = Float.valueOf(cMap.get("quality"));
						speedRb.setRating(rate1);
						qualityRb.setRating(rate2);
						commentContent.setText(cMap.get("content"));
						commentDate.setText(cMap.get("createDate"));
					}
				break;
				case 2:// 图片
					ArrayList<String> list = (ArrayList<String>) msg.obj;
					if (list.size() == 3){
						includeGv
								.setColumnWidth(new Tools()
										.getScreenWidth(OrderListDetailActivity.this) / 3);
						includeGv.setNumColumns(3);
					}else{
						includeGv
								.setColumnWidth(new Tools()
										.getScreenWidth(OrderListDetailActivity.this) / 2);
						includeGv.setNumColumns(2);
					}
					MainGrabGvInLvAdapter adapter = new MainGrabGvInLvAdapter(
							OrderListDetailActivity.this, list, parent);
					includeGv.setAdapter(adapter);
				break;
				case 3: // 报价详情, 基本信息
					final HashMap<String, String> hashMap1 = (HashMap<String, String>) msg.obj;
					includeTitle.setText(hashMap1.get("title"));
					if (hashMap1.get("price").equals("0")){// 报价
						includePrice.setText("竞价中");
					}else{
						includePrice.setText(hashMap1.get("price") + "元");
					}
					includeContent.setText(hashMap1.get("description"));
					ImageLoader.getInstance().displayImage(
							hashMap1.get("releaseUserImg"), whoHead);
					whoName.setText(hashMap1.get("releaseUserName"));
					whoPhone.setText(hashMap1.get("releaseUserPhone"));
					if (hashMap1.get("releaseUserSex").equals("1")){
						whoSex.setImageResource(R.drawable.male);
					}else{
						whoSex.setImageResource(R.drawable.famale);
					}
					includeDate.setText(hashMap1.get("endDate"));
					if (hashMap1.get("identity").equals("1")){ // 实名认证
						whoIdentity.setText("已实名认证");
					}else{
						whoIdentity.setText("未实名认证");
						whoIdentityPic
								.setImageResource(R.drawable.identity_grey);
						whoIdentity.setTextColor(Color.rgb(154, 154, 154));
					}
					if (hashMap1.get("timeout").equals("1")){ // 超时赔付
						label2.setVisibility(View.VISIBLE);
						label2.setOnTouchListener(new OnTouchListener(){

							@Override
							public boolean onTouch(View v, MotionEvent event){
								// TODO Auto-generated method stub
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "8",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
								return true;
							}
						});
					}else{
						label2.setVisibility(View.GONE);
					}
					if (hashMap1.get("label").equals("1")){ // song
						label1.setImageResource(R.drawable.zhu);
					}else if (hashMap1.get("label").equals("2")){ // che
						label1.setImageResource(R.drawable.xiu);
					}else if (hashMap1.get("label").equals("3")){ // xiu
						label1.setImageResource(R.drawable.zhi);
					}else if (hashMap1.get("label").equals("4")){ // zhi
						label1.setImageResource(R.drawable.che);
					}else if (hashMap1.get("label").equals("5")){ // zhu
						label1.setImageResource(R.drawable.song);
					}else if (hashMap1.get("label").equals("6")){ // zhu
						label1.setImageResource(R.drawable.jia);
					}
					label1.setOnTouchListener(new OnTouchListener(){

						@Override
						public boolean onTouch(View v, MotionEvent event){
							// TODO Auto-generated method stub
							if (hashMap1.get("label").equals("1")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "1",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap1.get("label").equals("2")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "2",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap1.get("label").equals("3")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "3",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap1.get("label").equals("4")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "4",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}else if (hashMap1.get("label").equals("5")){
								new HintPopUpWindow(
										OrderListDetailActivity.this, v, "5",
										(int) event.getRawX(), (int) event
												.getRawY(), false);
							}
							return true;
						}
					});
					if (hashMap1.get("method").equals("1")){// 线下
						includeAddr.setText(hashMap1.get("address"));
						checkMap.setVisibility(View.VISIBLE);
					}else{
						includeAddrPic.setImageResource(R.drawable.xianshang);
						includeAddr.setText("线上完成");
					}
					countTotal.setVisibility(View.VISIBLE);
					totalNeedTv.setText("总需" + hashMap1.get("nubmer") + "人");

					// if (where.equals("FillOrder")){
					// myPostTop.setVisibility(View.VISIBLE);
					// countTotal.setVisibility(View.VISIBLE);
					// }else{
					// 订单取消或超时
					if (orderState.equals("5")){
						cancelTop.setVisibility(View.VISIBLE);
						cancelOrder.setVisibility(View.GONE);
						count.setVisibility(View.GONE);
						cancelOrder.setVisibility(View.GONE);
					}else if (orderState.equals("7")){
						cancelTop.setVisibility(View.VISIBLE);
						cancelTop.setText("此订单已过有效时间");
						cancelOrder.setVisibility(View.GONE);
						count.setVisibility(View.GONE);
						cancelOrder.setVisibility(View.GONE);
					}
					System.out.println("orderType ---> " + orderType);
					if (orderType.equals("g")){ // 我的抢单
						who.setVisibility(View.VISIBLE);
						if (orderState.equals("4")){
							myGrabTop.setVisibility(View.VISIBLE);
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_finish);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							gTopLeaving.setClickable(false);
							gTopOnRoad.setClickable(false);
							gTopDone.setClickable(false);
							comment.setVisibility(View.VISIBLE);
							uncomment.setVisibility(View.GONE);
							commentDetail.setVisibility(View.VISIBLE);
							count.setVisibility(View.GONE);
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("6")){
							myGrabTop.setVisibility(View.VISIBLE);
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_finish);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							gTopLeaving.setClickable(false);
							gTopOnRoad.setClickable(false);
							gTopDone.setClickable(false);
							comment.setVisibility(View.VISIBLE);
							uncomment.setVisibility(View.VISIBLE);
							count.setVisibility(View.GONE);
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("1")
								|| orderState.equals("2")){
							count.setVisibility(View.GONE);
							cancelOrder.setVisibility(View.GONE);
							myGrabTop.setVisibility(View.GONE);
						}
					}else if (orderType.equals("p")){ // 我的发单//报价单//
						if (orderState.equals("4")){
							myPostTop.setVisibility(View.VISIBLE);
							pTopSubmit
									.setImageResource(R.drawable.order_status_submit);
							pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine3
									.setBackgroundResource(R.drawable.dash_green);
							pTopInprocess
									.setImageResource(R.drawable.order_status_inprocess);
							pTopDone.setImageResource(R.drawable.order_status_done);
							countTotal.setVisibility(View.GONE);
							pseAdapter = new PaySuccessEmployeeSelectGVAdapter(
									OrderListDetailActivity.this, sList,
									orderState, oid, category, cList,
									stateHandler, online);
							commitLv.setVisibility(View.VISIBLE);
							commitLv.setAdapter(pseAdapter);
							ScrollListView
									.setListViewHeightBasedOnChildren(commitLv);
							cancelOrder.setVisibility(View.GONE);
						}else if (orderState.equals("1")
								|| orderState.equals("2")){
							myPostTop.setVisibility(View.VISIBLE);
							pTopSubmit
									.setImageResource(R.drawable.order_status_submit);
							// pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							// pTopLine2
							// .setBackgroundResource(R.drawable.dash_green);
							chooseLv.setVisibility(View.VISIBLE);
							selectedLv.setVisibility(View.VISIBLE);
							count.setText("已选" + sList.size() + "人");
							totalPriceTv.setVisibility(View.VISIBLE);
							totalPriceTv.setText(Html
									.fromHtml("共计:<font color='red'>"
											+ (int) totalPrice + "</font>元"));
							cancelType = "employer_cancel";
						}else if (orderState.equals("6")){
							count.setText("已选" + sList.size() + "人");
							myPostTop.setVisibility(View.VISIBLE);
							pTopSubmit
									.setImageResource(R.drawable.order_status_submit);
							pTopPay.setImageResource(R.drawable.order_status_pay);
							pTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine2
									.setBackgroundResource(R.drawable.dash_green);
							pTopLine3
									.setBackgroundResource(R.drawable.dash_green);
							pTopInprocess
									.setImageResource(R.drawable.order_status_inprocess);
							pTopDone.setImageResource(R.drawable.order_status_done);
							totalPriceTv.setVisibility(View.VISIBLE);
							totalPriceTv.setText(Html
									.fromHtml("共计:<font color='red'>"
											+ (int) totalPrice + "</font>元"));
							pseAdapter = new PaySuccessEmployeeSelectGVAdapter(
									OrderListDetailActivity.this, sList,
									orderState, oid, category, cList,
									stateHandler, online);
							commitLv.setVisibility(View.VISIBLE);
							commitLv.setAdapter(pseAdapter);
							ScrollListView
									.setListViewHeightBasedOnChildren(commitLv);
							cancelOrder.setVisibility(View.GONE);
						}

					}else if (orderType.equals("inprocess_post")){
						myPostTop.setVisibility(View.VISIBLE);
						pTopSubmit
								.setImageResource(R.drawable.order_status_submit);
						pTopPay.setImageResource(R.drawable.order_status_pay);
						pTopLine1.setBackgroundResource(R.drawable.dash_green);
						pTopLine2.setBackgroundResource(R.drawable.dash_green);
						pTopInprocess
								.setImageResource(R.drawable.order_status_inprocess);
						count.setText("已选" + sList.size() + "人");
						totalPriceTv.setVisibility(View.VISIBLE);
						totalPriceTv.setText(Html
								.fromHtml("共计:<font color='red'>"
										+ (int) totalPrice + "</font>元"));
					}else if (orderType.equals("inprocess_grab")){
						myGrabTop.setVisibility(View.VISIBLE);
						gTopClickable.setVisibility(View.VISIBLE);
						count.setVisibility(View.GONE);
						if (hashMap1.get("userState").equals("0")){
							updateState = 0;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving_grey);
						}else if (hashMap1.get("userState").equals("1")){//
							updateState = 1;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
						}else if (hashMap1.get("userState").equals("2")){
							updateState = 2;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
						}else if (hashMap1.get("userState").equals("3")){
							updateState = 3;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_done);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);
						}else if (hashMap1.get("userState").equals("4")){
							updateState = 3;
							gTopLeaving
									.setImageResource(R.drawable.order_status_leaving);
							gTopOnRoad
									.setImageResource(R.drawable.order_status_onroad);
							gTopDone.setImageResource(R.drawable.order_status_done);
							gTopLine1
									.setBackgroundResource(R.drawable.dash_green);
							gTopLine2
									.setBackgroundResource(R.drawable.dash_green);

						}
						gTopLeaving
								.setOnClickListener(OrderListDetailActivity.this);
						gTopOnRoad
								.setOnClickListener(OrderListDetailActivity.this);
						gTopDone.setOnClickListener(OrderListDetailActivity.this);
						who.setVisibility(View.VISIBLE);
						if (!hashMap1.get("userState").equals("3")
								&& !hashMap1.get("userState").equals("4")){
							cancelOrder.setVisibility(View.VISIBLE);
							cancelOrder.setText("申请退单");
							cancelType = "employee_cancel";
						}
					}
				// }
				break;
				case 301:// 用户报价
					HashMap<String, String> qMap = (HashMap<String, String>) msg.obj;
					if (orderType.equals("g")){
						if (!qMap.get("userid").equals("")){
							if (orderState.equals("4")
									|| orderState.equals("6")){
								bottomBtn.setVisibility(View.GONE);
								bidList.setVisibility(View.GONE);
							}else{
								bottomBtn.setVisibility(View.VISIBLE);
								myBid.setVisibility(View.VISIBLE);
								ImageLoader.getInstance().displayImage(
										qMap.get("img"), myBidHead);
								myBidPrice.setText("我出价" + qMap.get("price")
										+ "元");
								if (qMap.get("userState").equals("0")){
									myBidStatus.setText("<等待选择>");
								}else{
									myBidStatus.setText("<您已被选中>");
								}
								if (orderState.equals("5")
										|| orderState.equals("7")){
									myBidCancel.setVisibility(View.GONE);
								}
							}
						}else{
							bottomBtn.setVisibility(View.GONE);
							bidList.setVisibility(View.GONE);
						}
					}

				break;
				case 4: // quoteList
					if (orderType.equals("g")){
						bidTotal.setText("已有" + qList.size() + "人报价");
						if (qList.size() != 0){
							for(int i = 0, j = qList.size(); i < j; i++){
								bid_iv[i].setVisibility(View.VISIBLE);
								ImageLoader.getInstance().displayImage(
										qList.get(i).get("img"), bid_iv[i]);
							}
						}
					}else if (orderType.equals("p")){
						osdAdapter1 = new OrderStatusDetailLVAdapter(
								OrderListDetailActivity.this, qList, oid,
								chooseHandler, "1", totalNeed, need);
						chooseLv.setAdapter(osdAdapter1);
						ScrollListView
								.setListViewHeightBasedOnChildren(chooseLv);
					}

				break;
				case 5:// 已选择列表
					DisplayMetrics metric = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metric);
					int width = metric.widthPixels; // 屏幕宽度（像素）
					if (orderType.equals("inprocess_post")){
						pseAdapter = new PaySuccessEmployeeSelectGVAdapter(
								OrderListDetailActivity.this, sList,
								orderState, oid, category, cList, stateHandler, online);
						commitLv.setVisibility(View.VISIBLE);
						commitLv.setAdapter(pseAdapter);
						ScrollListView
								.setListViewHeightBasedOnChildren(commitLv);
					}else{
						mesAdapter = new MyEmployeeSelectedAdapter(
								OrderListDetailActivity.this, sList, width,
								oid, chooseHandler, need);
						selectedLv.setAdapter(mesAdapter);
						ScrollListView
								.setListViewHeightBasedOnChildren(selectedLv);
						check();
					}
				break;
				case 7:// 评价 cList

				break;
				case 1001:// 取消报价
					cancelHttpClient(oid,
							new Tools().getUserId(OrderListDetailActivity.this));
				break;
				case 101: // 催单成功
					twoBtn1.setClickable(false);
					twoBtn1.setBackgroundResource(R.drawable.corner_grey);
				break;
				case 300:
					finish();
				break;
				case 100:// 取消订单
					refundHttpClient(
							new Tools().getUserId(OrderListDetailActivity.this),
							oid, category);
					cancelOrder.setVisibility(View.GONE);
				break;
				case 500:// 确认完成订单
					stateUpdate(
							new Tools().getUserId(OrderListDetailActivity.this),
							oid, 4, category);
				break;
				case 5000: // 抢单人状态 已出发 提交
					stateUpdate(
							new Tools().getUserId(OrderListDetailActivity.this),
							oid, 1, category);
				break;
				case 5001: // 抢单人状态 在路上 提交
					stateUpdate(
							new Tools().getUserId(OrderListDetailActivity.this),
							oid, 2, category);
				break;
				case 5002:// 抢单人状态 已完成 提交
					stateUpdate(
							new Tools().getUserId(OrderListDetailActivity.this),
							oid, 3, category);
				break;
				case 4999:// 确定选择抢单人
					stateUpdate(
							new Tools().getUserId(OrderListDetailActivity.this),
							oid, 0, category);
				break;
				case 4998:// 取消选择抢单人
					cancelGrabHttp();
				break;
				case 55:
					if (nowEvents.size() == 0){
						eventRL.setVisibility(View.GONE);
					}else{
						eventRL.setVisibility(View.VISIBLE);
						handler.post(task);
					}
				break;
				default:
				break;
			}
		}

	};

	private Runnable task = new Runnable(){
		public void run(){
			// TODO Auto-generated method stub
			handler.postDelayed(this, 5 * 1000);// 设置延迟时间，此处是5秒
			if (eventsCount == nowEvents.size()){
				eventsCount = 0;
			}
			if(nowEvents.size() != 0){
				NowEvent nowEvent = nowEvents.get(eventsCount);
				// title_one.setTextColor(Integer.parseInt(headlines.getTitlecolor()));
				// title_one.setTextColor(0xff123456);
				ImageLoader.getInstance().displayImage(nowEvent.getActivityImg(),
						eventIv);
				// headline_img.setImageDrawable(getResources().getDrawable(
				// R.drawable.year));
				eventTv.setText(nowEvent.getActivityTile());
				// headline_text.setText(headlines.getText());

				eventsCount++;
			}
		}
	};

	private Handler chooseHandler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:// 选择报价人成功 //选择报价人之后, 添加到已选择list(sList)
					String uid = (String) msg.obj;
					System.out.println("uid ---> " + uid);
					System.out.println("qlist size 1 ---> " + qList.size());
					for(int i = 0, j = qList.size(); i < j; i++){
						if (qList.get(i).get("userid").equals(uid)){
							totalPrice += Double.valueOf(qList.get(i)
									.get("price").toString());
							System.out.println("totalPrice ---> " + totalPrice);
							sList.add(qList.get(i));
							qList.remove(i);
							break;
						}
					}
					osdAdapter1.notifyDataSetChanged();
					mesAdapter.notifyDataSetChanged();
					ScrollListView.setListViewHeightBasedOnChildren(selectedLv);
					ScrollListView.setListViewHeightBasedOnChildren(chooseLv);//
					count.setText("已选择" + sList.size() + "人");
					check();
					System.out.println("slist size 1 ---> " + sList.size());
				break;
				case 2: // 删除报价人成功
					System.out.println("slist size 2 ---> " + sList.size());
					String uid1 = (String) msg.obj;
					System.out.println("uid ---> " + uid1);
					for(int m = 0, n = sList.size(); m < n; m++){
						if (sList.get(m).get("userid").equals(uid1)){
							totalPrice -= Double.valueOf(sList.get(m)
									.get("price").toString());
							System.out.println("totalPrice ---> " + totalPrice);
							qList.add(sList.get(m));
							sList.remove(m);
							break;
						}
					}
					osdAdapter1.notifyDataSetChanged();
					mesAdapter.notifyDataSetChanged();
					ScrollListView.setListViewHeightBasedOnChildren(selectedLv);
					ScrollListView.setListViewHeightBasedOnChildren(chooseLv);
					count.setText("已选择" + sList.size() + "人");
					check();
					System.out.println("qlist size 2 ---> " + qList.size());
				break;
				default:
				break;
			}
		}

	};

	public int getS(){
		return sList.size();
	}

	private void check(){
		if (orderType.equals("p")){
			if (orderState.equals("1") || orderState.equals("2")){
				if (sList.size() == totalNeed){
					chooseLv.clearFocus();
					bottomBtn.setVisibility(View.VISIBLE);
					bottomCommit.setVisibility(View.VISIBLE);
					bottomCommit.setText("确认");
					commitState = "commit_employee";
					Toast.makeText(this, "您已选择足够人数, 请点击确认之后支付订单!",
							Toast.LENGTH_SHORT).show();
					totalPriceTv.setText(Html.fromHtml("共计:<font color='red'>"
							+ (int) totalPrice + "</font>元"));
				}else{
					chooseLv.requestFocus();
					bottomBtn.setVisibility(View.GONE);
					bottomCommit.setVisibility(View.GONE);
					totalPriceTv.setText(Html.fromHtml("共计:<font color='red'>"
							+ (int) totalPrice + "</font>元"));
				}
			}
		}
	}

	private void cancelGrabHttp(){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("id", oid);
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.CANCEL_ORDER_GRABER_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("取消抢单人接口返回 ---> " + str);
								String message = "";
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
											refreshPage();
										}else{
											Toast.makeText(
													OrderListDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderListDetailActivity.this,
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

	protected String orderStartTime, orderEndTime, currentTime;

	/**
	 * 报价详情
	 * 
	 * @param
	 */
	private void bidDetailHttpClient(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("id", id);
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(OrderListDetailActivity.this,
					Config.ORDER_DETAIL_BID_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("报价详情接口返回  ---> " + str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONObject order = data
												.getJSONObject("order");
										HashMap<String, String> hashMap = new HashMap<String, String>();
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
										hashMap.put("need",
												order.getString("need"));
										need = order.getString("need");
										orderEndTime = order
												.getString("endDate");
										hashMap.put("id", order.getString("id"));
										hashMap.put("method",
												order.getString("method"));
										online = order.getString("method");
										hashMap.put("nubmer",
												order.getString("nubmer"));
										totalNeed = Integer.valueOf(order
												.getString("nubmer"));
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
										bPhone = order
												.getString("releaseUserPhone");
										hashMap.put("startDate",
												order.getString("startDate"));
										orderStartTime = order
												.getString("startDate");
										hashMap.put("title",
												order.getString("title"));
										hashMap.put("userState",
												order.getString("userState"));
										userState = order
												.getString("userState");
										hashMap.put("label",
												order.getString("label"));
										hashMap.put("identity",
												order.getString("identity"));
										hashMap.put("createDate",
												order.getString("createDate"));
										hashMap.put("releaseUserSex", order
												.getString("releaseUserSex"));
										hashMap.put("timeout",
												order.getString("timeout"));
										hashMap.put("lat",
												order.getString("lat"));
										orderLat = Double.valueOf(order
												.getString("lat"));
										hashMap.put("lng",
												order.getString("lng"));
										orderLng = Double.valueOf(order
												.getString("lng"));

										if (orderState.equals("3")){ // 进行中
											if (rid.equals(new Tools()
													.getUserId(OrderListDetailActivity.this))){ // 我的发单
												orderType = "inprocess_post";
											}else{
												orderType = "inprocess_grab";
											}
										}else{
											if (rid.equals(new Tools()
													.getUserId(OrderListDetailActivity.this))){ // 我的发单
												orderType = "p";
											}else{
												orderType = "g";
											}
										}

										Message msg1 = handler.obtainMessage();
										msg1.what = 3;
										msg1.obj = hashMap;
										msg1.sendToTarget();

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

										Message msg301 = handler
												.obtainMessage();
										msg301.what = 301;
										msg301.obj = hashMap;
										msg301.sendToTarget();

										JSONArray jArray = order
												.getJSONArray("imgList");
										ArrayList<String> imgList = new ArrayList<String>();
										for(int i = 0, j = jArray.length(); i < j; i++){
											String imgAddr = jArray
													.optString(i);
											imgList.add(imgAddr);
										}
										Message msg2 = handler.obtainMessage();
										msg2.what = 2;
										msg2.obj = imgList;
										msg2.sendToTarget();

										// JSONObject userQuote =
										// order.getJSONObject("userQuote");
										JSONArray quoteList = order
												.getJSONArray("quoteList");
										qList.clear();
										for(int i = 0, j = quoteList.length(); i < j; i++){
											JSONObject quote = quoteList
													.optJSONObject(i);
											hashMap = new HashMap<String, String>();
											hashMap.put("id",
													quote.getString("id"));
											hashMap.put("userid",
													quote.getString("userid"));
											hashMap.put("username",
													quote.getString("username"));
											hashMap.put("img",
													quote.getString("img"));
											hashMap.put("price",
													quote.getString("price"));
											hashMap.put("userState", quote
													.getString("userState"));
											hashMap.put("isComment", quote
													.getString("isComment"));
											hashMap.put("userlat",
													quote.getString("userlat"));
											hashMap.put("userlng",
													quote.getString("userlng"));
											hashMap.put("entityName",
													quote.getString("entityName"));

											qList.add(hashMap);
										}
										Message msg3 = handler.obtainMessage();
										msg3.what = 4;
										msg3.obj = qList;
										msg3.sendToTarget();

										JSONArray selected = order
												.getJSONArray("selected");
										sList.clear();
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
											hashMap.put("userlat",
													sel.getString("userlat"));
											hashMap.put("userlng",
													sel.getString("userlng"));
											hashMap.put("entityName",
													sel.getString("entityName"));

											totalPrice += Double.valueOf(sel
													.getString("price")
													.toString());

											sList.add(hashMap);
										}

										Message msg4 = handler.obtainMessage();
										msg4.what = 5;
										msg4.obj = sList;
										msg4.sendToTarget();

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

										JSONArray comment = data
												.getJSONArray("comment");
										cList.clear();
										for(int i = 0, j = comment.length(); i < j; i++){
											JSONObject job = comment
													.optJSONObject(i);
											hashMap = new HashMap<String, String>();
											hashMap.put("content",
													job.getString("content"));
											hashMap.put("grade",
													job.getString("grade"));
											hashMap.put("quality",
													job.getString("quality"));
											hashMap.put("speed",
													job.getString("speed"));
											hashMap.put("userid",
													job.getString("userid"));
											hashMap.put("username",
													job.getString("username"));
											hashMap.put("img",
													job.getString("img"));
											hashMap.put("createDate",
													job.getString("createDate"));

											cList.add(hashMap);
										}
										Message msg5 = handler.obtainMessage();
										msg5.what = 6;
										msg5.obj = cList;
										msg5.sendToTarget();

									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderListDetailActivity.this,
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

	/**
	 * 抢单详情接口
	 * 
	 * @param
	 */
	private void grabDetailHttpClient(String id, String uid){
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
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("抢单详情接口返回  " + " ---> "
										+ str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONObject order = data
												.getJSONObject("GrobOrder");
										HashMap<String, String> hashMap = new HashMap<String, String>();
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
										hashMap.put("orderState",
												order.getString("orderState"));
										orderState = order
												.getString("orderState");
										hashMap.put("grabOrderUserId", order
												.getString("grabOrderUserId"));
										gid = order
												.getString("grabOrderUserId");
										hashMap.put("grabOrderUserName", order
												.getString("grabOrderUserName"));
										userTraceName = order
												.getString("grabOrderUserName");
										entityName = order
												.getString("entityName");
										hashMap.put(
												"grabOrderUserPhone",
												order.getString("grabOrderUserPhone"));
										hashMap.put("grabOrderUserImg", order
												.getString("grabOrderUserImg"));
										hashMap.put("id", order.getString("id"));
										hashMap.put("method",
												order.getString("method"));
										online = order.getString("method");
										hashMap.put("nubmer",
												order.getString("nubmer"));
										hashMap.put("price",
												order.getString("price"));
										totalPrice = Double.valueOf(order
												.getString("price"));
										hashMap.put("releaseUserId", order
												.getString("releaseUserId"));
										rid = order.getString("releaseUserId");
										hashMap.put("releaseUserImg", order
												.getString("releaseUserImg"));
										hashMap.put("releaseUserName", order
												.getString("releaseUserName"));
										hashMap.put("releaseUserPhone", order
												.getString("releaseUserPhone"));
										bPhone = order
												.getString("releaseUserPhone");
										hashMap.put("startDate",
												order.getString("startDate"));
										orderStartTime = order
												.getString("startDate");
										hashMap.put("title",
												order.getString("title"));
										hashMap.put("userState",
												order.getString("userState"));
										userState = order
												.getString("userState");
										hashMap.put("label",
												order.getString("label"));
										hashMap.put("identity",
												order.getString("identity"));
										hashMap.put("createDate",
												order.getString("createDate"));
										hashMap.put("releaseUserSex", order
												.getString("releaseUserSex"));
										hashMap.put("timeout",
												order.getString("timeout"));
										hashMap.put("lat",
												order.getString("lat"));
										orderLat = Double.valueOf(order
												.getString("lat"));
										hashMap.put("userlat",
												order.getString("userlat"));
										hashMap.put("lng",
												order.getString("lng"));
										orderLng = Double.valueOf(order
												.getString("lng"));
										hashMap.put("userlng",
												order.getString("userlng"));
										need = order.getString("need");

										if (orderState.equals("3")){ // 进行中
											if (rid.equals(new Tools()
													.getUserId(OrderListDetailActivity.this))){ // 我的发单
												orderType = "inprocess_post";
											}else{
												orderType = "inprocess_grab";
											}
										}else{
											if (rid.equals(new Tools()
													.getUserId(OrderListDetailActivity.this))){ // 我的发单
												orderType = "p";
											}else{
												orderType = "g";
											}
										}

										Message msg1 = handler.obtainMessage();
										msg1.what = 1;
										msg1.obj = hashMap;
										msg1.sendToTarget();

										JSONArray jArray = order
												.getJSONArray("imgList");
										ArrayList<String> imgList = new ArrayList<String>();
										for(int i = 0, j = jArray.length(); i < j; i++){
											String imgAddr = jArray
													.optString(i);
											imgList.add(imgAddr);
										}
										Message msg2 = handler.obtainMessage();
										msg2.what = 2;
										msg2.obj = imgList;
										msg2.sendToTarget();

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

										JSONArray comment = data
												.getJSONArray("comment");
										if (comment.length() != 0){
											for(int i = 0, j = comment.length(); i < j; i++){
												JSONObject job = comment
														.optJSONObject(i);
												hashMap = new HashMap<String, String>();
												hashMap.put("content", job
														.getString("content"));
												hashMap.put("grade",
														job.getString("grade"));
												hashMap.put("quality", job
														.getString("quality"));
												hashMap.put("speed",
														job.getString("speed"));
												hashMap.put("userid",
														job.getString("userid"));
												hashMap.put("username", job
														.getString("username"));
												hashMap.put("img",
														job.getString("img"));
												hashMap.put(
														"createDate",
														job.getString("createDate"));

												cList.add(hashMap);
											}
											Message msg5 = handler
													.obtainMessage();
											msg5.what = 6;
											msg5.obj = cList;
											msg5.sendToTarget();
										}
									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderListDetailActivity.this,
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
			case R.id.new_order_list_detail_bottom_mybid_cancel: // 取消报价
				new ContentPopUpWindow(this, parent, "确认取消报价么?~",
						"bidcancellist");
			break;
			case R.id.new_order_list_detail_bottom_bid: // 报价
				// if(cb.isChecked()){
				if (new Tools().getUserId(this).equals(rid)){
					Toast.makeText(this, "不能对自己发的单报价!", Toast.LENGTH_SHORT)
							.show();
				}else{
					Intent intent = new Intent(OrderListDetailActivity.this,
							ToBidActivity.class);
					intent.putExtra("id", oid);
					intent.putExtra("uid",
							new Tools().getUserId(OrderListDetailActivity.this));
					startActivityForResult(intent, 100);
				}
			// }else{
			// Toast.makeText(this, "请同意用户协议!", Toast.LENGTH_SHORT).show();
			// }
			break;
			case R.id.new_order_list_detail_bottom_commit: // 底部大按钮, 去评价, 确定
				if (commitState.equals("comment")){ // 去评价
					Intent intent = new Intent(OrderListDetailActivity.this,
							CommentActivity.class);
					intent.putExtra("oid", oid);
					intent.putExtra("type", category);
					intent.putExtra("userid", gid);
					startActivityForResult(intent, 1098);
				}else if (commitState.equals("pay")){ // 去支付
					pay();
				}else if (commitState.equals("commit_employee")){ // 选择足够报价人,
																	// 点击确认
					osdAdapter2 = new OrderStatusDetailLVAdapter(
							OrderListDetailActivity.this, sList, oid,
							chooseHandler, "2", totalNeed, need);
					selectedLv.setVisibility(View.GONE);
					chooseLv.setVisibility(View.GONE);
					commitLv.setVisibility(View.VISIBLE);
					commitLv.setAdapter(osdAdapter2);
					ScrollListView.setListViewHeightBasedOnChildren(commitLv);
					totalPriceTv.setVisibility(View.VISIBLE);
					totalPriceTv.setText(Html.fromHtml("共计:<font color='red'>"
							+ (int) totalPrice + "</font>元"));
					bottomCommit.setText("去支付");
					commitState = "pay";
				}
			break;
			case R.id.new_order_list_detail_cancelorder: // 取消订单
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String now = sdf.format(curDate);
				System.out.println("now ---> " + now);
				System.out.println("orderStartTime ---> " + orderStartTime);
				System.out.println("compare ---> "
						+ new Tools().compareDateWithTime(now, orderStartTime));
				if (cancelType.equals("employer_cancel")){ // 发单人取消订单
					if (orderState.equals("0") || orderState.equals("1")
							|| orderState.equals("2")){ // 开始之前的订单可以直接取消
						new ContentPopUpWindow(this, parent, "确认取消此订单么?~",
								"refund");
					}else if (orderState.equals("3")){
						if (new Tools()
								.compareDateWithTime(now, orderStartTime) <= 1000
								&& new Tools().compareDateWithTime(now,
										orderStartTime) >= 0){
							new ContentPopUpWindow(this, parent, "确认取消此订单么?~",
									"refund");
						}else{
							new ContentPopUpWindow(
									OrderListDetailActivity.this, parent,
									"您的订单已进行超过10分钟,请联系客服退单!", "",
									"0431-81854515", "refund");
						}
					}
				}else if (cancelType.equals("employee_cancel")){
					new ContentPopUpWindow(OrderListDetailActivity.this,
							parent, "申请退单?", bPhone, "0431-81854515", "null");
				}
			break;
			case R.id.new_order_list_detail_bottom_button1: // 底部俩个按钮1 催单
				urgeHttp(gid, oid, category);
			break;
			case R.id.new_order_list_detail_bottom_button2: // 底部俩个按钮2 完成
				new ContentPopUpWindow(OrderListDetailActivity.this, v,
						"我们将把报酬转账给抢单人\n请确认该订单已经完成?", "finishOrder");
			break;
			case R.id.new_order_list_detail_leaving:
				if (updateState == 0){
					submitGpsHttp(new Tools().getUserId(this));
					new ContentPopUpWindow(OrderListDetailActivity.this, v,
							"您确定通知雇主您已经出发了?", "notify_leaving");
				}else{
					Toast.makeText(OrderListDetailActivity.this,
							"您已出发, 不需重复提交状态!", Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.new_order_list_detail_onroad:
				if (updateState == 1){
					submitGpsHttp(new Tools().getUserId(this));
					new ContentPopUpWindow(OrderListDetailActivity.this, v,
							"您确定通知雇主您已在途中么?", "notify_onroad");
				}else{
					Toast.makeText(OrderListDetailActivity.this, "请先更改状态出发啦!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.new_order_list_detail_finish:
				if (updateState == 2){
					submitGpsHttp(new Tools().getUserId(this));
					new ContentPopUpWindow(OrderListDetailActivity.this, v,
							"您确定通知雇主您已完成订单么?", "notify_finish");
				}else if (updateState == 3 || updateState == 4){
					Toast.makeText(OrderListDetailActivity.this,
							"您已完成订单, 无需重复提交状态!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(OrderListDetailActivity.this, "请先更改状态在途中!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.order_list_detail_who_phone:
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ whoPhone.getText().toString())));
			break;
			case R.id.order_list_detail_who_head:
				Intent intent = new Intent(this, GetOtherInfoActivity.class);
				if (rid.equals(new Tools()
						.getUserId(OrderListDetailActivity.this))){ // 自己发的单
					intent.putExtra("otherUserId", gid);
				}else{
					intent.putExtra("otherUserId", rid);
				}
				startActivity(intent);
			break;
			case R.id.order_list_include_checkMap:// 查看地图
				Intent intent3 = new Intent(this, BaiDuMapActivity.class);
				intent3.putExtra("lat", orderLat);
				intent3.putExtra("lng", orderLng);
				startActivity(intent3);
			break;
			case R.id.order_list_detail_who_tocomfirm_comfirm: // 确认抢单人(5分钟)
				new ContentPopUpWindow(OrderListDetailActivity.this, v,
						"确定选择此人抢单么?", "notify_confirm");
			break;
			case R.id.order_list_detail_who_tocomfirm_checkresume:// 查看抢单人简历(5分钟)
				Intent intent4 = new Intent(this, GetOtherInfoActivity.class);
				intent4.putExtra("otherUserId", gid);
				startActivity(intent4);
			break;
			case R.id.order_list_detail_who_tocomfirm_cancel: // 取消抢单人抢单
				new ContentPopUpWindow(OrderListDetailActivity.this, v,
						"确定取消此人抢单么?", "notify_cancel");//
			break;
			case R.id.new_order_list_detail_trace:// 抢单详情, 查看抢单人实时位置
				Intent intent5 = new Intent(OrderListDetailActivity.this,
						BaiDuMapTraceActivity.class);
				intent5.putExtra("lat", userTraceLat);
				intent5.putExtra("lng", userTraceLng);
				intent5.putExtra("name", userTraceName);
				intent5.putExtra("entityName", entityName);
				intent5.putExtra("myLat", orderLat);
				intent5.putExtra("myLng", orderLng);
				intent5.putExtra("userState", userState);
				startActivity(intent5);
			break;
			case R.id.new_order_list_detail_eventrl:
				new EventPopUpWindow(OrderListDetailActivity.this, parent, nowEvents);
				break;
			default:
			break;
		}
	}

	private void submitGpsHttp(String uid){
		JSONObject job = new JSONObject();

		try{
			job.put("id", oid);
			job.put("userid", uid);
			job.put("category", category);
			job.put("userlat", userLat);
			job.put("userlng", userLng);
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.SUBMIT_GPS_COORDINATE_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("坐标接口返回  ---> " + str);
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 100: // 我要报价
				if (resultCode == RESULT_OK){ // 报价成功
					refreshPage();
				}else{ // 取消报价
					Toast.makeText(OrderListDetailActivity.this, "报价取消!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			case 1098:
				if (resultCode == RESULT_OK){
					refreshPage();
				}else{
					Toast.makeText(OrderListDetailActivity.this, "评价取消!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			case 1020:// 支付回调
				if (resultCode == RESULT_OK){
					System.out.println("刷新    " + "category ---> " + category
							+ "     oid ---> " + oid);
					bottomBtn.setVisibility(View.GONE);
					// onCreate(null);
					new Handler().postDelayed(new Runnable(){

						@Override
						public void run(){
							// TODO Auto-generated method stub
							refreshPage();
						}
					}, 300);
				}else{
					bottomBtn.setVisibility(View.VISIBLE);
					Toast.makeText(OrderListDetailActivity.this, "支付取消!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			default:
			break;
		}
	}

	/**
	 * 底部俩个按钮 催单
	 * 
	 * @param
	 */
	private void urgeHttp(String uid, String oid, String type){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("id", oid);
			job.put("type", type);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.REMINDER_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("催单接口返回  ---> " + str);
								String message = "";
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
													OrderListDetailActivity.this,
													"您已经催单，请耐心等候哦！", Toast.LENGTH_SHORT)
													.show();
											Message msg = handler
													.obtainMessage();
											msg.what = 101;
											msg.sendToTarget();
										}else{
											Toast.makeText(
													OrderListDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderListDetailActivity.this,
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

	/**
	 * 取消订单
	 * 
	 * @param
	 */
	private void refundHttpClient(String uid, String oid, String type){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("id", oid);
			job.put("type", type);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.REFUND_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("退单接口返回  ---> " + str);
								String message = "";
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
													OrderListDetailActivity.this,
													"订单取消成功!",
													Toast.LENGTH_SHORT).show();
											refreshPage();
										}else{
											Toast.makeText(
													OrderListDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderListDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}catch(UnsupportedEncodingException e1){
			// TODO Auto-generated catch block
			e1.printStackTrace();
			pd.dismiss();
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
			HttpClient.post(OrderListDetailActivity.this,
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
												OrderListDetailActivity.this,
												"报价已取消!", Toast.LENGTH_SHORT)
												.show();

										refreshPage();
									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(OrderListDetailActivity.this,
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
							Toast.makeText(OrderListDetailActivity.this,
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

	/**
	 * 状态提交结果
	 * 
	 * @param
	 */
	private void stateUpdate(String uid, String oid, final int _state,
			String type){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("id", oid);
			job.put("state", _state + "");
			job.put("type", type);

			StringEntity se = new StringEntity(job.toString());

			HttpClient.post(this, Config.STATE_UPDATE_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							String message = "";
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("状态提交接口返回  ---> " + str);

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
											if (_state == 4 || _state == 0){
												refreshPage();
											}else{
												Message msg = stateHandler
														.obtainMessage();
												msg.what = _state;
												msg.sendToTarget();
											}
										}else{
											Toast.makeText(
													OrderListDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderListDetailActivity.this,
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
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderListDetailActivity.this,
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

	private Handler stateHandler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					gTopLeaving
							.setImageResource(R.drawable.order_status_leaving);
					updateState = 1;
				break;
				case 2:
					gTopOnRoad.setImageResource(R.drawable.order_status_onroad);
					gTopLine1.setBackgroundResource(R.drawable.dash_green);
					updateState = 2;
				break;
				case 3:
					updateState = 3;
					gTopDone.setImageResource(R.drawable.order_status_finish);
					gTopLine2.setBackgroundResource(R.drawable.dash_green);
					cancelOrder.setVisibility(View.GONE);
				break;
				case 4:
				// done.setImageResource(R.drawable.order_status_done);
				// twoButton.setVisibility(View.GONE);
				// commit.setVisibility(View.VISIBLE);
				// commit.setText("去评价");
				// commitStatus = "comment";
				// Toast.makeText(OrderStatusDetailActivity.this,
				// "订单完成, 请评价!", Toast.LENGTH_SHORT).show();
				break;
				case 100: // 单个人确认
					String userid = (String) msg.obj;
					stateUpdate(userid, oid, 4, category);
				break;
				default:
				break;
			}
		}

	};

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
					|| "2".equals(new Tools().getNotificationType(context))){
				if (pps[0] != null && pps[0].isShowing()){
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(OrderListDetailActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"OrderListDetailActivity");
				pps[0] = pp;
			}else if ("3".equals(new Tools().getNotificationType(context))){
				if (pps[0] != null && pps[0].isShowing()){
					pps[0].dismiss();
				}
				mandatoryExit(context);
			}else{
				NotificationManager nManager = (NotificationManager) context
						.getSystemService(context.NOTIFICATION_SERVICE);
				Notification notification = new Notification();
				notification.icon = R.drawable.logo;// 设置通知的图标
				// 添加声音提示
				notification.defaults = Notification.DEFAULT_SOUND;
				// audioStreamType的值必须AudioManager中的值，代表着响铃的 模式
				notification.audioStreamType = android.media.AudioManager.ADJUST_LOWER;
				notification.defaults = Notification.DEFAULT_VIBRATE;
				notification.flags = Notification.FLAG_AUTO_CANCEL;
				long[] vibrate = { 0, 100, 200, 300 };
				notification.vibrate = vibrate;

				notification.tickerText = "找事吧"; // 显示在状态栏中的文字
				PendingIntent pendingIntent = PendingIntent.getActivity(
						context, 0, new Intent(), 0);
				// 点击状态栏的图标出现的提示信息设置
				notification
						.setLatestEventInfo(context, "找事吧",
								new Tools().getNotificationText(context),
								pendingIntent);
				nManager.notify(1000, notification);
			}

		}
	}

	/**
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context){
		String CID = new Tools().getCID(OrderListDetailActivity.this);
		String notificationText = new Tools()
				.getNotificationText(OrderListDetailActivity.this);
		Boolean isFirstStart = new Tools()
		.getisFirstStart(OrderListDetailActivity.this);
		unBindAlias(new Tools().getUserId(OrderListDetailActivity.this),
				new Tools().getCID(OrderListDetailActivity.this),
				OrderListDetailActivity.this);
		SharedPreferences sharedPre = OrderListDetailActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(OrderListDetailActivity.this,
				parent, new Tools().getNotificationText(context));
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

	private void pay(){
		Log.e("totalPrice", totalPrice + "");
		Intent intent = new Intent(this, PayActivity.class);
		intent.putExtra("id", oid); // 订单id
		intent.putExtra("amount", totalPrice + "");// 金额
		startActivityForResult(intent, 1020);
		bottomBtn.setVisibility(View.GONE);
	}
	
	/**
	 * 
	 *@param
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState){
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	private void refreshPage(){
		onCreate(null);
		parent.setPadding(0, new Tools().dip2px(this, 20), 0, 0);
//		parent.setFitsSystemWindows(true);
		// new Handler().postDelayed(new Runnable(){
		//
		// @Override
		// public void run(){
		// // TODO Auto-generated method stub
		// totalPrice = 0;
		// if (category.equals("0")){
		// grabDetailHttpClient(oid,
		// new Tools().getUserId(OrderListDetailActivity.this));
		// }else{
		// bidDetailHttpClient(oid,
		// new Tools().getUserId(OrderListDetailActivity.this));
		// }
		// }
		// }, 500);

	}
}
