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

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindow;
import popup.ContentPopUpWindowSingleButton;
import popup.HintPopUpWindow;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import view.RoundImageView;
import view.ScrollListView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pingplusplus.android.PaymentActivity;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.PayActivity;
import com.youge.jobfinder.R;

import adapter.ConvenienceFacilityGVAdapter;
import adapter.MainGrabGvInLvAdapter;
import adapter.MyEmployeeSelectedAdapter;
import adapter.OrderStatusDetailLVAdapter;
import adapter.PaySuccessEmployeeSelectGVAdapter;
import android.R.integer;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @param
 */
public class OrderStatusDetailActivity extends BaseActivity implements
		OnClickListener{

	private ImageView back, includeDial, whoDial;
	private View include;
	private TextView title, price, content, startDt, endDt, timeCount, address,
			fix, cancelTop, cancelBottom, refund, refundTop;
	private GridView gv;
	private LinearLayout includeHide, label, selToPay, parent, twoButton;
	private RoundImageView includeHead, whoHead;
	private TextView includeName, includePhone, enough, selToPayPrice, btn1,
			btn2;

	private LinearLayout grabTop, postTop, commentLL;
	private RatingBar rb1, rb2;
	private ImageView commentGrade;
	private TextView commentText, commentContent, commentTitle;
	private ImageView leaving, onroad, finish, submit, pay, inprocess, done,
			whoPic;
	private TextView grabTopLine1, grabTopLine2, postTopLine1, postTopLine2,
			postTopLine3, whoText;
	private FrameLayout totalCount, who, finishBottom;
	private TextView total, count, whoContent, whoPhone, commit;
	private ListView selLv, toChooseLv;
	private GridView selToPayGv;

	private String from, orderStartTime;
	private String category;
	private String orderState;

	private String orderId, commitStatus, oid, pid, bPhone, hint, eid;
	private int totalNeed, updateState;

	public static OrderStatusDetailActivity instance;

	private ArrayList<HashMap<String, String>> qList, sList, cList;
	private OrderStatusDetailLVAdapter osdAdapter;
	private MyEmployeeSelectedAdapter mesAdapter;
	private PaySuccessEmployeeSelectGVAdapter gvAdapter;
	private double totalPrice;

	private registerReceiver receiver;

	private PopupWindow popup;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.order_status_detail);
		initView();
	}

	private void initView(){
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		refundTop = (TextView) findViewById(R.id.title_tv);
		back.setOnClickListener(this);
		refundTop.setOnClickListener(this);
		instance = this;
		qList = new ArrayList<HashMap<String, String>>();
		sList = new ArrayList<HashMap<String, String>>();
		cList = new ArrayList<HashMap<String, String>>();

		parent = (LinearLayout) findViewById(R.id.order_status_parent);
		/******************* include ---> 订单详情 *********************************/
		include = (View) findViewById(R.id.order_status_detail_include);
		title = (TextView) include.findViewById(R.id.order_include_title);
		label = (LinearLayout) include.findViewById(R.id.order_include_label);
		price = (TextView) include.findViewById(R.id.order_include_price);
		content = (TextView) include.findViewById(R.id.order_include_content);
		startDt = (TextView) include.findViewById(R.id.order_include_starttime);
		endDt = (TextView) include.findViewById(R.id.order_include_finishtime);
		timeCount = (TextView) include
				.findViewById(R.id.order_include_timecount);
		address = (TextView) include.findViewById(R.id.order_include_address);
		gv = (GridView) include.findViewById(R.id.order_include_gv);//
		includeHide = (LinearLayout) include
				.findViewById(R.id.order_include_hidell);
		includeHead = (RoundImageView) include
				.findViewById(R.id.order_include_head);
		includeName = (TextView) include.findViewById(R.id.order_include_name);
		includePhone = (TextView) include
				.findViewById(R.id.order_include_phone);
		includeDial = (ImageView) include.findViewById(R.id.order_include_dial);
		includeHead.setOnClickListener(this);
		includeDial.setOnClickListener(this);
		/***********************************************************************/

		/********************* 评价 ****************************/
		commentLL = (LinearLayout) findViewById(R.id.order_status_detail_grab_comment);
		commentGrade = (ImageView) findViewById(R.id.order_status_detail_grab_comment_grade);
		commentText = (TextView) findViewById(R.id.order_status_detail_grab_comment_text);
		rb1 = (RatingBar) findViewById(R.id.order_status_detail_grab_comment_rb1);
		rb2 = (RatingBar) findViewById(R.id.order_status_detail_grab_comment_rb2);
		commentContent = (TextView) findViewById(R.id.order_status_detail_grab_comment_content);
		commentTitle = (TextView) findViewById(R.id.order_status_detail_grab_comment_title);
		/***********************************************************************/

		/********************* 我的抢单---订单已取消 ****************************/
		cancelTop = (TextView) findViewById(R.id.order_status_detail_cancel_top);
		cancelBottom = (TextView) findViewById(R.id.order_status_detail_cancelbottom);
		/*****************************************************************/

		/********************* 我的抢单---订单已取消 ****************************/
		// also have cancelTop, & totalCount(unclickable), & hide count
		/*****************************************************************/

		/********************* 我的抢单---已完成 ******************************/
		grabTop = (LinearLayout) findViewById(R.id.order_status_detail_grabtop); // 顶部状态栏
		leaving = (ImageView) findViewById(R.id.order_status_detail_grabtop_leaving);
		grabTopLine1 = (TextView) findViewById(R.id.order_status_detail_grabtop_line1);
		grabTopLine2 = (TextView) findViewById(R.id.order_status_detail_grabtop_line2);
		onroad = (ImageView) findViewById(R.id.order_status_detail_grabtop_onroad);
		finish = (ImageView) findViewById(R.id.order_status_detail_grabtop_finish);
		finishBottom = (FrameLayout) findViewById(R.id.order_status_detail_finishbottom);
		refund = (TextView) findViewById(R.id.order_status_detail_refund);
		refund.setOnClickListener(this);
		/*****************************************************************/

		/********************* 我的发单(固定价格)---待抢单, 状态=订单已提交(未支付) ******************************/
		postTop = (LinearLayout) findViewById(R.id.order_status_detail_posttop);
		submit = (ImageView) findViewById(R.id.order_status_detail_posttop_submit);
		postTopLine1 = (TextView) findViewById(R.id.order_status_detail_posttop_line1);
		postTopLine2 = (TextView) findViewById(R.id.order_status_detail_posttop_line2);
		postTopLine3 = (TextView) findViewById(R.id.order_status_detail_posttop_line3);
		pay = (ImageView) findViewById(R.id.order_status_detail_posttop_pay);
		inprocess = (ImageView) findViewById(R.id.order_status_detail_posttop_inprocess);
		done = (ImageView) findViewById(R.id.order_status_detail_posttop_done);
		commit = (TextView) findViewById(R.id.order_status_detail_commit);
		// commit.setText("去支付");
		// commitStatus = "pay";
		commit.setOnClickListener(this);
		/********************************************************************************************/

		/********************* 进行中订单 ************************************************************/
		twoButton = (LinearLayout) findViewById(R.id.order_status_detail_two_button);
		btn1 = (TextView) findViewById(R.id.order_status_detail_button1);
		btn2 = (TextView) findViewById(R.id.order_status_detail_button2);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		/********************************************************************************************/
		/********************* 我的发单(固定价格)---被抢单, 状态=订单已支付(可催单) ******************************/
		who = (FrameLayout) findViewById(R.id.order_status_detail_who);
		whoHead = (RoundImageView) findViewById(R.id.order_status_detail_who_head);
		whoContent = (TextView) findViewById(R.id.order_status_detail_who_content);
		whoPhone = (TextView) findViewById(R.id.order_status_detail_who_phone);
		whoDial = (ImageView) findViewById(R.id.order_status_detail_who_dial);
		whoPic = (ImageView) findViewById(R.id.order_status_detail_who_pic);
		whoText = (TextView) findViewById(R.id.order_status_detail_who_text);
		whoHead.setOnClickListener(this);
		// commit.setText("催单");
		// commitStatus = "urge";
		/********************************************************************************************/

		/********************* 我的发单(固定价格)---无人抢单, 状态=订单已支付 ******************************/
		// who--->gone
		/********************************************************************************************/

		/********************* 我的发单(固定价格)---催单后, 状态=正在进行 ******************************/
		// same as 我的发单(固定价格)---被抢单, 状态=订单已支付(可催单), except commit--->gone
		/********************************************************************************************/

		/********************* 我的发单(固定价格)---状态=已完成, 已评价 ******************************/
		// same as 我的发单(固定价格)---被抢单, 状态=订单已支付(可催单), except commit--->gone
		/********************************************************************************************/

		/********************* 我的发单(固定价格)---状态=已完成, 未评价 ******************************/
		// same as 我的发单(固定价格)---被抢单, 状态=订单已支付(可催单)
		// commit.setText("去评价");
		// commitStatus = "comment";
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已提交(无人报价) ******************************/
		// postTop--->visible(订单已提交), count--->0人报价
		totalCount = (FrameLayout) findViewById(R.id.order_status_detail_total_hide);
		total = (TextView) findViewById(R.id.order_status_detail_total);
		count = (TextView) findViewById(R.id.order_status_detail_count);
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已提交(有人报价, 显示报价人列表) ******************************/
		// same as 我的发单(报价订单)---状态=已提交(无人报价)
		toChooseLv = (ListView) findViewById(R.id.order_status_detail_total_tochoose_lv);
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已提交(有人报价也选择了谁的报价, 显示报价人列表, 已选择列表) ******/
		// same as 我的发单(报价订单)---状态=已提交(有人报价, 显示报价人列表)
		selLv = (ListView) findViewById(R.id.order_status_detail_total_sel_lv);
		// selLv.setVisibility(View.VISIBLE);
		// ArrayList<HashMap<String, String>> list = new
		// ArrayList<HashMap<String,String>>();
		// for(int i=0,j=5;i<j;i++){
		// HashMap<String, String> hashMap = new HashMap<String, String>();
		// list.add(hashMap);
		// }
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// MyEmployeeSelectedAdapter adapter = new
		// MyEmployeeSelectedAdapter(this, list, dm.widthPixels);
		// selLv.setAdapter(adapter);
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已提交(已选择足够报价人) *******************************/
		// same as 我的发单(报价订单)---状态=已提交(有人报价也选择了谁的报价, 显示报价人列表, 已选择列表)
		enough = (TextView) findViewById(R.id.order_status_detail_total_enough); // 可见
		// commit.setText("确认");
		// commitStatus = "commit";
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已提交(已选择足够报价人, 确定之后去支付) ******/
		// totalCount--->visible
		selToPay = (LinearLayout) findViewById(R.id.order_status_detail_total_seltopay); // 可见
		selToPayGv = (GridView) findViewById(R.id.order_status_detail_total_gv);
		selToPayPrice = (TextView) findViewById(R.id.order_status_detail_total_seltopay_price);
		// commit.setText("去支付");
		// commitStatus = "pay";
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已支付(支付完成之后,可催单) ***************************/
		// same as 我的发单(报价订单)---状态=已提交(已选择足够报价人, 确定之后去支付)
		// commit.setText("催单");
		// commitStatus = "urge";
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=正在进行(催单之后) **********************************/
		// same as 我的发单(报价订单)---状态=已支付(支付完成之后,可催单)
		// commit--->gone
		/********************************************************************************************/

		/********************* 我的发单(报价订单)---状态=已完成(未评价) **********************************/
		// same as 我的发单(报价订单)---状态=已支付(支付完成之后,可催单)
		// commit---> 去评价
		/********************************************************************************************/

		from = getIntent().getExtras().getString("where");
		System.out.println("where ---> " + from);
		System.out.println("category ---> " + getIntent().getExtras().getString("category"));
		System.out.println("oid ---> " + getIntent().getExtras().getString("oid"));
		if (from.equals("FillOrder")){ // 填写订单,提交之后
			category = getIntent().getExtras().getString("category"); // 0-出价(抢单详情),
																		// 1-报价(报价详情)
			oid = getIntent().getExtras().getString("oid");
			if (category.equals("0")){
				grabDetailHttpClient(oid, new Tools().getUserId(this));
			}else{
				bidDetailHttpClient(oid, new Tools().getUserId(this));
			}
			refundTop.setVisibility(View.VISIBLE);

		}else if (from.equals("inprocess_post")){ // 订单进行中-我的发单
			category = getIntent().getExtras().getString("category"); // 0-出价(抢单详情),
																		// 1-报价(报价详情)
			oid = getIntent().getExtras().getString("oid");
			// orderState = "3"; // orderState只能是3, 代表进行中
			if (category.equals("0")){
				grabDetailHttpClient(oid, new Tools().getUserId(this));
			}else{
				bidDetailHttpClient(oid, new Tools().getUserId(this));
			}
			refundTop.setVisibility(View.VISIBLE);

		}else if (from.equals("inprocess_grab")){ // 订单进行中-我的抢单 相同页面 不需要判断报价单或抢单
			category = getIntent().getExtras().getString("category"); // 0-出价(抢单详情),
			// 1-报价(报价详情)
			oid = getIntent().getExtras().getString("oid");
			orderState = "3"; // orderState只能是3, 代表进行中
			grabDetailHttpClient(oid, new Tools().getUserId(this));

		}else if (from.equals("g")){ // 订单列表, 我的抢单(已完成或已取消)
			category = getIntent().getExtras().getString("category"); // 0-出价(抢单详情),
																		// 1-报价(报价详情)
			oid = getIntent().getExtras().getString("oid");
			orderState = getIntent().getExtras().getString("orderState"); // 1没抢（没报价）2报价没选中，4完成，5订单取消
			if (category.equals("0")){
				grabDetailHttpClient(oid, new Tools().getUserId(this));
			}else{
				bidDetailHttpClient(oid, new Tools().getUserId(this));
			}
		}else{ // 订单列表, 我的发单
			category = getIntent().getExtras().getString("category"); // 0-出价(抢单详情),
																		// 1-报价(报价详情)
			oid = getIntent().getExtras().getString("oid");
			// orderState = getIntent().getExtras().getString("orderState"); //
			// 1没抢（没报价(带报价
			// 带抢单)）2报价没选中，4完成，5订单取消，6评价
			if (category.equals("0")){
				grabDetailHttpClient(oid, new Tools().getUserId(this));
			}else{
				bidDetailHttpClient(oid, new Tools().getUserId(this));
			}
		}

	}

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1: // 抢单详情 共有信息填写
					HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					title.setText(hashMap.get("title"));

					if (hashMap.get("price").equals("0")){
						price.setText("?");
					}else{
						price.setText(hashMap.get("price"));
					}
					content.setText(hashMap.get("description"));
					ImageLoader.getInstance().displayImage(
							hashMap.get("releaseUserImg"), includeHead);
					includeName.setText(hashMap.get("releaseUserName"));
					includePhone.setText(hashMap.get("releaseUserPhone"));
					startDt.setText(hashMap.get("startDate"));
					endDt.setText(hashMap.get("endDate"));
					timeCount.setText(hashMap.get("countdown"));
					address.setText(hashMap.get("address"));
					ImageLoader.getInstance().displayImage(
							hashMap.get("grabOrderUserImg"), whoHead);
					whoContent.setText(hashMap.get("grabOrderUserName")
							+ "已经抢了您的订单");
					whoPhone.setText(hashMap.get("grabOrderUserPhone"));

					/*********************** label *****************************/
					String[] labels = hashMap.get("label").split(",");

					ImageView onoff = new ImageView(
							OrderStatusDetailActivity.this);
					LayoutParams lp = new LayoutParams(new Tools().dip2px(
							OrderStatusDetailActivity.this, 20),
							new Tools().dip2px(OrderStatusDetailActivity.this,
									20));
					lp.gravity = Gravity.CENTER_VERTICAL;
					onoff.setLayoutParams(lp);

					if (hashMap.get("method").equals("1")){
						onoff.setImageResource(R.drawable.xianshang);
						hint = "6";
					}else{
						onoff.setImageResource(R.drawable.xianxia);
						hint = "0";
					}
					onoff.setOnTouchListener(new OnTouchListener(){

						@Override
						public boolean onTouch(View v, MotionEvent event){
							// TODO Auto-generated method stub
							popup = new HintPopUpWindow(
									OrderStatusDetailActivity.this, v, hint,
									(int) event.getRawX(), (int) event
											.getRawY(), false);
							return true;
						}
					});
					label.removeAllViews();
					label.addView(onoff);

					for(int i = 0, j = labels.length; i < j; i++){
						if (labels[i].equals("1")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.zhu);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"1", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (labels[i].equals("2")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.xiu);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"2", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (labels[i].equals("3")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.zhi);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"3", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (labels[i].equals("4")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.che);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"4", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (labels[i].equals("5")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.song);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"5", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}
					}
					/*********************************************************************/

					if (from.equals("FillOrder")){ // 顶部4个状态显示(订单已提交),
													// 底部判断是否为报价单(显示总需几人)
						postTop.setVisibility(View.VISIBLE);
						commit.setVisibility(View.VISIBLE);
						commit.setText("去支付");
						commitStatus = "pay";
					}else if (from.equals("inprocess_post")){ // 进行中, 我的发单,
																// 顶部4个状态显示(根据orderState判断状态)
						postTop.setVisibility(View.VISIBLE);
						pay.setImageResource(R.drawable.order_status_pay);
						postTopLine2.setTextColor(Color.rgb(204, 255, 255));
						inprocess
								.setImageResource(R.drawable.order_status_inprocess);
						done.setImageResource(R.drawable.order_status_done_grey);
						// 催单和确定
						twoButton.setVisibility(View.VISIBLE);
						who.setVisibility(View.VISIBLE);
					}else if (from.equals("inprocess_grab")){
						grabTop.setVisibility(View.VISIBLE);
						includeHide.setVisibility(View.VISIBLE);
						if (hashMap.get("userState").equals("0")){
							updateState = 0;
							leaving.setImageResource(R.drawable.order_status_leaving_grey);
							leaving.setOnClickListener(OrderStatusDetailActivity.this);
							onroad.setOnClickListener(OrderStatusDetailActivity.this);
							finish.setOnClickListener(OrderStatusDetailActivity.this);
							finishBottom.setVisibility(View.VISIBLE);
						}else if (hashMap.get("userState").equals("1")){
							updateState = 1;
							leaving.setImageResource(R.drawable.order_status_leaving);
							leaving.setClickable(false);
							onroad.setOnClickListener(OrderStatusDetailActivity.this);
							finish.setOnClickListener(OrderStatusDetailActivity.this);
							finishBottom.setVisibility(View.VISIBLE);
						}else if (hashMap.get("userState").equals("2")){
							updateState = 2;
							leaving.setImageResource(R.drawable.order_status_leaving);
							leaving.setClickable(false);
							onroad.setImageResource(R.drawable.order_status_onroad);
							onroad.setClickable(false);
							finish.setOnClickListener(OrderStatusDetailActivity.this);
							finishBottom.setVisibility(View.VISIBLE);
						}else{
							updateState = 3;
							leaving.setImageResource(R.drawable.order_status_leaving);
							onroad.setImageResource(R.drawable.order_status_onroad);
							finish.setImageResource(R.drawable.order_status_finish);
							leaving.setClickable(false);
							onroad.setClickable(false);
							finish.setClickable(false);
						}
						System.out.println("updateState ---> " + updateState);

					}else if (from.equals("g")){ // 订单列表,
													// 我的抢单(已完成或已取消)
						if (orderState.equals("5")){ // 订单已取消
							cancelTop.setVisibility(View.VISIBLE);
						}else if (orderState.equals("7")){
							cancelTop.setVisibility(View.VISIBLE);
							cancelTop.setText("此订单已过有效时间");
						}else{ // 4 已完成(需判断抢单或报价单) ----> 抢单详情接口只有可能为4, 5
							grabTop.setVisibility(View.VISIBLE);
							leaving.setImageResource(R.drawable.order_status_leaving);
							grabTopLine1.setTextColor(Color.rgb(204, 255, 255));
							grabTopLine2.setTextColor(Color.rgb(204, 255, 255));
							onroad.setImageResource(R.drawable.order_status_onroad);
							finish.setImageResource(R.drawable.order_status_finish);
							includeHide.setVisibility(View.VISIBLE);
							// who.setVisibility(View.VISIBLE);
							// whoPic.setImageResource(R.drawable.order_status_finish);
							// whoText.setText("已完成");
						}

					}else if (from.equals("p")){ // 我的发单
						if (orderState.equals("0")){ // 未支付
							postTop.setVisibility(View.VISIBLE);
							commit.setVisibility(View.VISIBLE);
							commit.setText("去支付");
							commitStatus = "pay";
							refundTop.setVisibility(View.VISIBLE);
						}else if (orderState.equals("1")){ // 待报价 待抢单
							postTop.setVisibility(View.VISIBLE);
							pay.setImageResource(R.drawable.order_status_pay);
							postTopLine2.setTextColor(Color.rgb(204, 255, 255));
							commit.setVisibility(View.GONE);
							refundTop.setVisibility(View.VISIBLE);
						}else if (orderState.equals("2")){
							refundTop.setVisibility(View.VISIBLE);
						}else if (orderState.equals("4")){ // 已完成
							postTop.setVisibility(View.VISIBLE);
							pay.setImageResource(R.drawable.order_status_pay);
							postTopLine2.setTextColor(Color.rgb(204, 255, 255));
							postTopLine3.setTextColor(Color.rgb(204, 255, 255));
							inprocess
									.setImageResource(R.drawable.order_status_inprocess);
							done.setImageResource(R.drawable.order_status_done);
							who.setVisibility(View.VISIBLE);
							whoPic.setImageResource(R.drawable.order_status_finish);
							whoText.setText("已完成");
						}else if (orderState.equals("6")){ // 待评价
							postTop.setVisibility(View.VISIBLE);
							pay.setImageResource(R.drawable.order_status_pay);
							postTopLine2.setTextColor(Color.rgb(204, 255, 255));
							postTopLine3.setTextColor(Color.rgb(204, 255, 255));
							inprocess
									.setImageResource(R.drawable.order_status_inprocess);
							done.setImageResource(R.drawable.order_status_done);
							commit.setVisibility(View.VISIBLE);
							commit.setText("去评价");
							commitStatus = "comment";
							who.setVisibility(View.VISIBLE);
							whoPic.setImageResource(R.drawable.order_status_finish);
							whoText.setText("已完成");
						}else if (orderState.equals("5")){// 订单已取消
							cancelTop.setVisibility(View.VISIBLE);
						}else if (orderState.equals("7")){// 订单已取消
							cancelTop.setVisibility(View.VISIBLE);
							cancelTop.setText("此订单已过有效时间");
						}
					}
				break;
				case 2:
					ArrayList<String> list = (ArrayList<String>) msg.obj;
					
					if (list.size() == 0){
						gv.setVisibility(View.GONE);
					}else{
						MainGrabGvInLvAdapter adapter = new MainGrabGvInLvAdapter(
								OrderStatusDetailActivity.this, list, parent);
						gv.setAdapter(adapter);
					}
				break;
				case 3: // 报价信息显示
					HashMap<String, String> hashMap1 = (HashMap<String, String>) msg.obj;
					title.setText(hashMap1.get("title"));
					
					if (hashMap1.get("price").equals("0")){
						price.setText("?");
					}else{
						price.setText(hashMap1.get("price"));
					}
					content.setText(hashMap1.get("description"));
					ImageLoader.getInstance().displayImage(
							hashMap1.get("releaseUserImg"), includeHead);
					includeName.setText(hashMap1.get("releaseUserName"));
					includePhone.setText(hashMap1.get("releaseUserPhone"));
					startDt.setText(hashMap1.get("startDate"));
					endDt.setText(hashMap1.get("endDate"));
					timeCount.setText(hashMap1.get("countdown"));
					address.setText(hashMap1.get("address"));
					total.setText("总需" + totalNeed + "人");

					/*********************** label *****************************/
					String[] _labels = hashMap1.get("label").split(",");

					ImageView _onoff = new ImageView(
							OrderStatusDetailActivity.this);
					LayoutParams _lp = new LayoutParams(new Tools().dip2px(
							OrderStatusDetailActivity.this, 20),
							new Tools().dip2px(OrderStatusDetailActivity.this,
									20));
					_lp.gravity = Gravity.CENTER_VERTICAL;
					_onoff.setLayoutParams(_lp);

					if (hashMap1.get("method").equals("1")){
						_onoff.setImageResource(R.drawable.xianshang);
						hint = "6";
					}else{
						_onoff.setImageResource(R.drawable.xianxia);
						hint = "0";
					}
					_onoff.setOnTouchListener(new OnTouchListener(){

						@Override
						public boolean onTouch(View v, MotionEvent event){
							// TODO Auto-generated method stub
							popup = new HintPopUpWindow(
									OrderStatusDetailActivity.this, v, hint,
									(int) event.getRawX(), (int) event
											.getRawY(), false);
							return true;
						}
					});
					label.removeAllViews();
					label.addView(_onoff);

					for(int i = 0, j = _labels.length; i < j; i++){
						if (_labels[i].equals("1")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(_lp);
							iv.setImageResource(R.drawable.zhu);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"1", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (_labels[i].equals("2")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(_lp);
							iv.setImageResource(R.drawable.xiu);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"2", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (_labels[i].equals("3")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(_lp);
							iv.setImageResource(R.drawable.zhi);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"3", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (_labels[i].equals("4")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(_lp);
							iv.setImageResource(R.drawable.che);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"4", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}else if (_labels[i].equals("5")){
							final ImageView iv = new ImageView(
									OrderStatusDetailActivity.this);
							iv.setLayoutParams(_lp);
							iv.setImageResource(R.drawable.song);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderStatusDetailActivity.this, iv,
											"5", (int) event.getRawX(),
											(int) event.getRawY(), false);
									return true;
								}
							});
						}
					}
					/*********************************************************************/

					if (from.equals("g")){ // 订单列表, 只有4和5
											// 我的抢单(已完成或已取消)
						if (orderState.equals("5")){ // 订单已取消cancelTop.setText("此订单已过有效时间");
							cancelTop.setVisibility(View.VISIBLE);
							cancelBottom.setVisibility(View.VISIBLE);
							cancelBottom.setText("您已报价" + hashMap1.get("price")
									+ "元");
						}else if (orderState.equals("7")){// 超时
							cancelTop.setVisibility(View.VISIBLE);
							cancelTop.setText("此订单已过有效时间");
							cancelBottom.setVisibility(View.VISIBLE);
							cancelBottom.setText("您已报价" + hashMap1.get("price")
									+ "元");
						}else if (orderState.equals("4")){// 4 已完成
							grabTop.setVisibility(View.VISIBLE);
							includeHide.setVisibility(View.VISIBLE);
							onroad.setImageResource(R.drawable.order_status_onroad);
							finish.setImageResource(R.drawable.order_status_finish);
							commentLL.setVisibility(View.VISIBLE);
							for(int i = 0, j = cList.size(); i < j; i++){
								if (new Tools().getUserId(	
										OrderStatusDetailActivity.this).equals(
										cList.get(i).get("userid"))){
									HashMap<String, String> cMap = cList.get(i);
									if (cMap.get("grade").equals("1")){
										commentGrade
												.setImageResource(R.drawable.comment_good);
										commentText.setText("好评");
									}else if (cMap.get("grade").equals("2")){
										commentGrade
												.setImageResource(R.drawable.comment_medium);
										commentText.setText("中评");
									}else{
										commentGrade
												.setImageResource(R.drawable.comment_bad);
										commentText.setText("差评");
									}
									float rate1 = Float.valueOf(cMap
											.get("speed"));
									float rate2 = Float.valueOf(cMap
											.get("quality"));
									rb1.setRating(rate1);
									rb2.setRating(rate2);
									commentContent.setText(cMap.get("content"));
								}
							}
						}else if (orderState.equals("6")){
							grabTop.setVisibility(View.VISIBLE);
							includeHide.setVisibility(View.VISIBLE);
							onroad.setImageResource(R.drawable.order_status_onroad);
							finish.setImageResource(R.drawable.order_status_finish);
						}
					}else if (from.equals("p")){
						if (orderState.equals("1") || orderState.equals("2")){ // 1无人报价
																				// 2未选择
							postTop.setVisibility(View.VISIBLE);
							totalCount.setVisibility(View.VISIBLE);
							toChooseLv.setVisibility(View.VISIBLE);
							selLv.setVisibility(View.VISIBLE);
							check();
							refundTop.setVisibility(View.VISIBLE);
						}else if (orderState.equals("4")){ // 完成
							postTop.setVisibility(View.VISIBLE);
							pay.setImageResource(R.drawable.order_status_pay);
							postTopLine2.setTextColor(Color.rgb(204, 255, 255));
							postTopLine3.setTextColor(Color.rgb(204, 255, 255));
							inprocess
									.setImageResource(R.drawable.order_status_inprocess);
							done.setImageResource(R.drawable.order_status_done);
							selToPay.setVisibility(View.VISIBLE);
						}else if (orderState.equals("5")){ // 取消
							cancelTop.setVisibility(View.VISIBLE);
							totalCount.setVisibility(View.VISIBLE);
						}else if (orderState.equals("7")){ // 超时
							cancelTop.setVisibility(View.VISIBLE);
							cancelTop.setText("此订单已过有效时间");
							totalCount.setVisibility(View.VISIBLE);
						}else if (orderState.equals("6")){ // 评价
							postTop.setVisibility(View.VISIBLE);
							pay.setImageResource(R.drawable.order_status_pay);
							postTopLine2.setTextColor(Color.rgb(204, 255, 255));
							postTopLine3.setTextColor(Color.rgb(204, 255, 255));
							inprocess
									.setImageResource(R.drawable.order_status_inprocess);
							done.setImageResource(R.drawable.order_status_done);
							commit.setVisibility(View.VISIBLE);
							commit.setText("去评价");
							commitStatus = "comment";
						}
					}else if (from.equals("inprocess_post")){ // 进行中, 我的发单,
						// 顶部4个状态显示(根据orderState判断状态)
						postTop.setVisibility(View.VISIBLE);
						pay.setImageResource(R.drawable.order_status_pay);
						postTopLine2.setTextColor(Color.rgb(204, 255, 255));
						inprocess
								.setImageResource(R.drawable.order_status_inprocess);
						done.setImageResource(R.drawable.order_status_done_grey);
						selToPay.setVisibility(View.VISIBLE);
						refundTop.setVisibility(View.VISIBLE);
					}else if (from.equals("FillOrder")){
						totalCount.setVisibility(View.VISIBLE);
						// total.setText("总需" + hashMap1.get("number") + "人");
					}

				break;
				case 4: // quoteList

					count.setText((qList.size() + sList.size()) + "人报价");
					if ((qList.size() + sList.size()) == 0){
						totalCount.setClickable(false);
						selLv.setVisibility(View.GONE);
						toChooseLv.setVisibility(View.GONE);
					}else{
						totalCount
								.setOnClickListener(OrderStatusDetailActivity.this);
					}
					// else if(qList.size() == totalNeed){
					// selLv.setVisibility(View.VISIBLE);
					// toChooseLv.setVisibility(View.GONE);
					// enough.setVisibility(View.VISIBLE);
					// }else{
					// selLv.setVisibility(View.VISIBLE);
					// toChooseLv.setVisibility(View.VISIBLE);
					// }
					osdAdapter = new OrderStatusDetailLVAdapter(
							OrderStatusDetailActivity.this, qList, oid,
							chooseHandler, "1", 0, "1");
					toChooseLv.setAdapter(osdAdapter);
					ScrollListView.setListViewHeightBasedOnChildren(toChooseLv);
				// lv.setAdapter(adapter);
				break;
				case 5: // 已选择列表
					DisplayMetrics metric = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(metric);
					int width = metric.widthPixels; // 屏幕宽度（像素）
					mesAdapter = new MyEmployeeSelectedAdapter(
							OrderStatusDetailActivity.this, sList, width, oid,
							chooseHandler, "1");
					selLv.setAdapter(mesAdapter);
					ScrollListView.setListViewHeightBasedOnChildren(selLv);
					gvAdapter = new PaySuccessEmployeeSelectGVAdapter(
							OrderStatusDetailActivity.this, sList, orderState,
							oid, category, cList, handler, "0");
					selToPayGv.setAdapter(gvAdapter);
					ScrollListView.setGridViewHeightBasedOnChildren(selToPayGv);
				break;
				case 6: // 抢单评价
					if (cList.size() == 0){
						commentLL.setVisibility(View.GONE);
					}else{
						commentLL.setVisibility(View.VISIBLE);
						HashMap<String, String> cMap = cList.get(0);
						if (new Tools().getUserId(
								OrderStatusDetailActivity.this).equals(
								cMap.get("userid"))){
							commentTitle.setText("我的评价");
						}
						if (cMap.get("grade").equals("1")){
							commentGrade
									.setImageResource(R.drawable.comment_good);
							commentText.setText("好评");
						}else if (cMap.get("grade").equals("2")){
							commentGrade
									.setImageResource(R.drawable.comment_medium);
							commentText.setText("中评");
						}else{
							commentGrade
									.setImageResource(R.drawable.comment_bad);
							commentText.setText("差评");
						}
						float rate1 = Float.valueOf(cMap.get("speed"));
						float rate2 = Float.valueOf(cMap.get("quality"));
						rb1.setRating(rate1);
						rb2.setRating(rate2);
						commentContent.setText(cMap.get("content"));
					}
				break;
				case 7: // 报价单评价
					if (cList.size() == 0){
						commentLL.setVisibility(View.GONE);
					}else{
						commentLL.setVisibility(View.VISIBLE);
						HashMap<String, String> cMap = cList.get(0);
						if (cMap.get("grade").equals("1")){
							commentGrade
									.setImageResource(R.drawable.comment_good);
							commentText.setText("好评");
						}else if (cMap.get("grade").equals("2")){
							commentGrade
									.setImageResource(R.drawable.comment_medium);
							commentText.setText("中评");
						}else{
							commentGrade
									.setImageResource(R.drawable.comment_bad);
							commentText.setText("差评");
						}
						float rate1 = Float.valueOf(cMap.get("speed"));
						float rate2 = Float.valueOf(cMap.get("quality"));
						rb1.setRating(rate1);
						rb2.setRating(rate2);
						commentContent.setText(cMap.get("content"));
					}
				break;
				case 100: // 申请退单点击确定

				break;
				case 101:// 催单成功 隐藏
					btn1.setClickable(false);
				break;
				case 200:// 刷新
					onCreate(null);
					from = "p";
					if (category.equals("0")){
						grabDetailHttpClient(
								oid,
								new Tools()
										.getUserId(OrderStatusDetailActivity.this));
					}else{
						bidDetailHttpClient(
								oid,
								new Tools()
										.getUserId(OrderStatusDetailActivity.this));
					}

				break;
				case 300:
					finish();
					break;
				default:
				break;
			}
		}

	};

	private Handler chooseHandler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1: // 选择报价人成功 //选择报价人之后, 添加到已选择list(sList)
					int position = msg.arg1;
					System.out.println("position ---> " + position);
					HashMap<String, String> map = qList.get(position);
					totalPrice += Double.valueOf(qList.get(position)
							.get("price").toString());
					System.out.println("totalPrice ---> " + totalPrice);
					sList.add(map);
					qList.remove(position);
					osdAdapter.notifyDataSetChanged();
					mesAdapter.notifyDataSetChanged();
					check();
					ScrollListView.setListViewHeightBasedOnChildren(selLv);
					ScrollListView.setListViewHeightBasedOnChildren(toChooseLv);//
				break;
				case 2: // 删除报价人成功
					int which = msg.arg1;
					System.out.println("which ---> " + which);
					HashMap<String, String> hashMap = sList.get(which);
					totalPrice -= Double.valueOf(sList.get(which).get("price")
							.toString());
					System.out.println("totalPrice ---> " + totalPrice);
					qList.add(hashMap);
					sList.remove(which);
					osdAdapter.notifyDataSetChanged();
					mesAdapter.notifyDataSetChanged();
					check();
					ScrollListView.setListViewHeightBasedOnChildren(selLv);
					ScrollListView.setListViewHeightBasedOnChildren(toChooseLv);
					count.setText("已选择" + sList.size() + "人");
				break;
				default:
				break;
			}
		}

	};

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
											Message msg200 = handler
													.obtainMessage();
											msg200.what = 200;
											msg200.sendToTarget();
											Toast.makeText(
													OrderStatusDetailActivity.this,
													"订单取消成功!",
													Toast.LENGTH_SHORT).show();
										}else{
											Toast.makeText(
													OrderStatusDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderStatusDetailActivity.this,
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
							Toast.makeText(OrderStatusDetailActivity.this,
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

	private void check(){
		System.out.println("sList size ---> " + sList.size());
		System.out.println("qList size ---> " + qList.size());
		if (qList.size() == 0){
			toChooseLv.setVisibility(View.GONE);
		}else{
			toChooseLv.setVisibility(View.VISIBLE);
		}
		if (sList.size() == 0){
			selLv.setVisibility(View.GONE);
		}else{
			selLv.setVisibility(View.VISIBLE);
		}
		if (totalNeed == sList.size()){
			enough.setVisibility(View.VISIBLE);
			commit.setVisibility(View.VISIBLE);
			commit.setText("确认");
			commitStatus = "commit_employee";
			// onCreate(null);//
			// from = "p";
			// bidDetailHttpClient(
			// oid,
			// new Tools()
			// .getUserId(OrderStatusDetailActivity.this));
			// if (category.equals("0")){
			// grabDetailHttpClient(
			// oid,
			// new Tools()
			// .getUserId(OrderStatusDetailActivity.this));
			// }else{
			//
			// }
		}else{
			enough.setVisibility(View.GONE);
			commit.setVisibility(View.GONE);
		}
	}

	private void bidDetailHttpClient(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("id", id);
			job.put("userid", uid);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(OrderStatusDetailActivity.this,
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
										hashMap.put("id", order.getString("id"));
										hashMap.put("method",
												order.getString("method"));
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
										eid = order.getString("releaseUserId");
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
										hashMap.put("label",
												order.getString("label"));

										Message msg1 = handler.obtainMessage();
										msg1.what = 3;
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

										// JSONObject userQuote =
										// order.getJSONObject("userQuote");
										JSONArray quoteList = order
												.getJSONArray("quoteList");
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
											// hashMap.put("userState",
											// quote.getString("userState"));

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

											totalPrice += Double.valueOf(sel
													.getString("price")
													.toString());

											sList.add(hashMap);
										}

										Message msg4 = handler.obtainMessage();
										msg4.what = 5;
										msg4.obj = sList;
										msg4.sendToTarget();

										JSONArray comment = data
												.getJSONArray("comment");

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

											cList.add(hashMap);
										}
										Message msg5 = handler.obtainMessage();
										msg5.what = 7;
										msg5.obj = cList;
										msg5.sendToTarget();

									}else{
										Toast.makeText(
												OrderStatusDetailActivity.this,
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
							Toast.makeText(OrderStatusDetailActivity.this,
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
								System.out.println("抢单详情接口返回  " + from
										+ " ---> " + str);
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
										hashMap.put("orderState",
												order.getString("orderState"));
										orderState = order
												.getString("orderState");
										hashMap.put("grabOrderUserId", order
												.getString("grabOrderUserId"));
										pid = order
												.getString("grabOrderUserId");
										hashMap.put("grabOrderUserName", order
												.getString("grabOrderUserName"));
										hashMap.put(
												"grabOrderUserPhone",
												order.getString("grabOrderUserPhone"));
										hashMap.put("grabOrderUserImg", order
												.getString("grabOrderUserImg"));
										hashMap.put("id", order.getString("id"));
										hashMap.put("method",
												order.getString("method"));
										hashMap.put("nubmer",
												order.getString("nubmer"));
										hashMap.put("price",
												order.getString("price"));
										totalPrice = Double.valueOf(order
												.getString("price"));
										hashMap.put("releaseUserId", order
												.getString("releaseUserId"));
										eid = order.getString("releaseUserId");
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
										hashMap.put("label",
												order.getString("label"));

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
												OrderStatusDetailActivity.this,
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
							Toast.makeText(OrderStatusDetailActivity.this,
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
			case R.id.back: // 返回
				finish();
			break;
			case R.id.title_tv: // 顶部退单
				System.out.println("commitStatus ---> " + commitStatus);
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String now = sdf.format(curDate);
				System.out.println("now ---> " + now);
				System.out.println("orderStartTime ---> " + orderStartTime);
				System.out.println("compare ---> "
						+ new Tools().compareDateWithTime(now, orderStartTime));
				if (from.equals("p") || from.equals("FillOrder")){
					if (orderState.equals("1") || orderState.equals("2")){
						refundHttpClient(
								new Tools()
										.getUserId(OrderStatusDetailActivity.this),
								oid, category);
					}else if(orderState.equals("3")){
						if (new Tools().compareDateWithTime(now, orderStartTime) <= 1000
								&& new Tools().compareDateWithTime(now,
										orderStartTime) >= 0){
							refundHttpClient(
									new Tools()
											.getUserId(OrderStatusDetailActivity.this),
									oid, category);
						}else{
							new ContentPopUpWindow(OrderStatusDetailActivity.this,
									parent, "您的订单已进行超过10分钟,请联系客服退单!", "",
									"0431-81854515", "refund");
						}
					}
				}else{ //进行中订单
					if (new Tools().compareDateWithTime(now, orderStartTime) <= 1000
							&& new Tools().compareDateWithTime(now,
									orderStartTime) >= 0){
						refundHttpClient(
								new Tools()
										.getUserId(OrderStatusDetailActivity.this),
								oid, category);
					}else{
						new ContentPopUpWindow(OrderStatusDetailActivity.this,
								parent, "您的订单已进行超过10分钟,请联系客服退单!", "",
								"0431-81854515", "refund");
					}
				}
			break;
			case R.id.order_status_detail_commit: // 评价或确认
				if (commitStatus.equals("commit_employee")){
					gvAdapter = new PaySuccessEmployeeSelectGVAdapter(
							OrderStatusDetailActivity.this, sList, orderState,
							oid, category, cList, handler, "0");
					selToPayGv.setAdapter(gvAdapter);
					ScrollListView.setGridViewHeightBasedOnChildren(selToPayGv);
					selToPay.setVisibility(View.VISIBLE);
					selLv.setVisibility(View.GONE);
					toChooseLv.setVisibility(View.GONE);
					selToPayPrice.setText(totalPrice + "元");
					commit.setText("去支付");
					commitStatus = "pay";
				}else if (commitStatus.equals("comment")){
					Intent intent = new Intent(OrderStatusDetailActivity.this,
							CommentActivity.class);
					intent.putExtra("oid", oid);
					intent.putExtra("type", category);
					startActivityForResult(intent, 1098);
				}else if (commitStatus.equals("pay")){
					// pay(oid,
					// new Tools()
					// .getUserId(OrderStatusDetailActivity.this),
					// totalPrice + "");
					pay();
				}
			break;
			case R.id.order_include_dial: // include 雇主头像栏拨打电话
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ includePhone.getText().toString())));
			break;
			case R.id.order_status_detail_who_dial: // 被谁抢单栏 拨打电话
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ whoPhone.getText().toString())));
			break;
			case R.id.order_status_detail_total_hide: // 几人报价 点击显示lv
			break;
			case R.id.order_status_detail_grabtop_leaving: // 我的抢单 顶部状态 点击 我出发啦
															// 改变背景颜色
				stateUpdate(
						new Tools().getUserId(OrderStatusDetailActivity.this),
						oid, 1, category);
			// if(state < 1){
			//
			// }else{
			// Toast.makeText(OrderStatusDetailActivity.this, "",
			// Toast.LENGTH_SHORT).show();
			// }
			break;
			case R.id.order_status_detail_grabtop_onroad: // 我的抢单 顶部状态 点击 正在途中
															// 改变背景颜色
				if (updateState == 1){
					stateUpdate(
							new Tools()
									.getUserId(OrderStatusDetailActivity.this),
							oid, 2, category);
				}else{
					Toast.makeText(OrderStatusDetailActivity.this,
							"请先更改状态出发啦!", Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.order_status_detail_grabtop_finish: // 我的抢单 顶部状态 点击 已完成
															// 改变背景颜色 完成订单
				if (updateState == 2){
					stateUpdate(
							new Tools()
									.getUserId(OrderStatusDetailActivity.this),
							oid, 3, category);
				}else{
					Toast.makeText(OrderStatusDetailActivity.this,
							"请先更改状态在途中!", Toast.LENGTH_SHORT).show();
				}
			break;
			case R.id.order_status_detail_refund: // 申请退单
				new ContentPopUpWindow(OrderStatusDetailActivity.this, parent,
						"申请退单?", bPhone, "0431-81854515", "null");
			break;
			case R.id.order_status_detail_button1: // 催单
				urgeHttp(pid, oid, category);
			break;
			case R.id.order_status_detail_button2: // 完成
				stateUpdate(
						new Tools().getUserId(OrderStatusDetailActivity.this),
						oid, 4, category);
				refundTop.setVisibility(View.GONE);
			break;
			case R.id.order_include_head: // 雇主头像
				Intent intent = new Intent(this, GetOtherInfoActivity.class);
				intent.putExtra("otherUserId", eid);
				startActivity(intent);
			break;
			case R.id.order_status_detail_who_head: //抢单人头像
				Intent intent1 = new Intent(this, GetOtherInfoActivity.class);
				intent1.putExtra("otherUserId", pid);
				startActivity(intent1);
				break;
			default:
			break;
		}
	}
	
	/**
	 * 
	 *@param
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
			case 1098:
			if(resultCode == RESULT_OK){
				commit.setVisibility(View.GONE);
			}
			break;
			case 1020:
				if(resultCode == RESULT_OK){
					commit.setVisibility(View.GONE);
					pay.setImageResource(R.drawable.order_status_pay);
				}
				break;
			default:
			break;
		}
	}

	private void pay(String oid, String uid, String total){
		JSONObject job = new JSONObject();

		try{
			job.put("id", oid);
			job.put("userid", uid);
			job.put("amount", total);

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.PAY_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							System.out.println("1111");
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
													OrderStatusDetailActivity.this,
													"您已经催单，请耐心等候哦！", Toast.LENGTH_SHORT)
													.show();
											Message msg = handler
													.obtainMessage();
											msg.what = 101;
											msg.sendToTarget();
										}else{
											Toast.makeText(
													OrderStatusDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderStatusDetailActivity.this,
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
							Toast.makeText(OrderStatusDetailActivity.this,
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
											Message msg = stateHandler
													.obtainMessage();
											msg.what = _state;
											msg.sendToTarget();
										}else{
											Toast.makeText(
													OrderStatusDetailActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									}else{
										Toast.makeText(
												OrderStatusDetailActivity.this,
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
							Toast.makeText(OrderStatusDetailActivity.this,
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
					leaving.setImageResource(R.drawable.order_status_leaving);
					updateState = 1;
				break;
				case 2:
					onroad.setImageResource(R.drawable.order_status_onroad);
					updateState = 2;
				break;
				case 3:
					updateState = 3;
					finish.setImageResource(R.drawable.order_status_finish);
				break;
				case 4:
					done.setImageResource(R.drawable.order_status_done);
					twoButton.setVisibility(View.GONE);
					commit.setVisibility(View.VISIBLE);
					commit.setText("去评价");
					commitStatus = "comment";
					Toast.makeText(OrderStatusDetailActivity.this,
							"订单完成, 请评价!", Toast.LENGTH_SHORT).show();
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
					|| "2".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				pp = new PushPopUpWindow(OrderStatusDetailActivity.this,
						parent, new Tools().getNotificationText(context),
						new Tools().getNotificationType(context), "OrderStatusDetailActivity");
				pps[0] = pp;
			} else if ("3".equals(new Tools().getNotificationType(context))) {
				if (pps[0] != null && pps[0].isShowing()) {
					pps[0].dismiss();
				}
				mandatoryExit(context);
			} else {
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
	private void mandatoryExit(Context context) {
		String CID = new Tools().getCID(OrderStatusDetailActivity.this);
		String notificationText = new Tools()
				.getNotificationText(OrderStatusDetailActivity.this);
		Boolean isFirstStart = new Tools()
		.getisFirstStart(OrderStatusDetailActivity.this);
		unBindAlias(new Tools().getUserId(OrderStatusDetailActivity.this),
				new Tools().getCID(OrderStatusDetailActivity.this),
				OrderStatusDetailActivity.this);
		SharedPreferences sharedPre = OrderStatusDetailActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(OrderStatusDetailActivity.this,
				parent, new Tools().getNotificationText(context));
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

	private final int REQUEST_CODE_PAYMENT = 555;

	private void pay(){
		Intent intent = new Intent(this, PayActivity.class);
		intent.putExtra("id", oid); // 订单id
		intent.putExtra("amount", totalPrice + "");// 金额
		startActivityForResult(intent, 1020);
	}

	// protected void onPause(){
	// super.onPause();
	// unregisterReceiver(receiver);
	// }

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
	 *@param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop(){
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
	}

}