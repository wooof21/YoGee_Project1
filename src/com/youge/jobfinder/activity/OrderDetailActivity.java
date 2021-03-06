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

import model.Quote;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.HintPopUpWindow;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;
import view.RoundImageView;
import adapter.ConvenienceFacilityGVAdapter;
import adapter.MainGrabGvInLvAdapter;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import fragment.OrderDetailToBidFragment;
import fragment.OrderDetailToGrabFragment;

/**
 * 
 * @param
 */
public class OrderDetailActivity extends BaseActivity implements
		OnClickListener{

	private ImageView back, dial;
	private TextView share, name, phone;
	private RoundImageView head, myBidHead;
	private View include;
	private RelativeLayout hide;
	private TextView title, type, price, content, startDt, endDt, timeCount,
			address, myBidPrice, myBidCancel, selStatus;
	private GridView gv;

	private OrderDetailToBidFragment bidFragment;
	private OrderDetailToGrabFragment grabFragment;

	private boolean isBid = false;
	private String category, id, uid, rid, oid, shareUrl, hint, eid,
			orderState;

	public LinearLayout parent, label;
	public static OrderDetailActivity instance;
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
		setContentView(R.layout.order_detail_toshare);
		initView();

		setBottomFragment(category);
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
		dial = (ImageView) findViewById(R.id.order_detail_dial);
		share = (TextView) findViewById(R.id.title_tv);
		name = (TextView) findViewById(R.id.order_detail_name);
		phone = (TextView) findViewById(R.id.order_detail_phone);
		head = (RoundImageView) findViewById(R.id.order_detail_head);
		include = (View) findViewById(R.id.order_detail_include);
		parent = (LinearLayout) findViewById(R.id.order_detail_parent);

		title = (TextView) include.findViewById(R.id.order_include_title);
		price = (TextView) include.findViewById(R.id.order_include_price);
		content = (TextView) include.findViewById(R.id.order_include_content);
		startDt = (TextView) include.findViewById(R.id.order_include_starttime);
		endDt = (TextView) include.findViewById(R.id.order_include_finishtime);
		timeCount = (TextView) include
				.findViewById(R.id.order_include_timecount);
		address = (TextView) include.findViewById(R.id.order_include_address);
		gv = (GridView) include.findViewById(R.id.order_include_gv);//
		label = (LinearLayout) include.findViewById(R.id.order_include_label);

		hide = (RelativeLayout) findViewById(R.id.order_detail_to_bid_frame);
		myBidHead = (RoundImageView) findViewById(R.id.order_detail_to_bid_frame_iv);
		myBidPrice = (TextView) findViewById(R.id.order_detail_to_bid_frame_tv);
		myBidCancel = (TextView) findViewById(R.id.order_detail_to_bid_frame_cancel);
		selStatus = (TextView) findViewById(R.id.order_detail_to_bid_frame_status);

		back.setOnClickListener(this);
		dial.setOnClickListener(this);
		share.setOnClickListener(this);
		head.setOnClickListener(this);

		instance = this;

		category = getIntent().getExtras().getString("category");
		id = getIntent().getExtras().getString("id");
		rid = getIntent().getExtras().getString("rid");
		uid = new Tools().getUserId(this);
		System.out.println("category ---> " + category);
		System.out.println("id orderdetail---> " + id);

		getImageFromAssetsFile(this, "logo.png");
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
			case R.id.title_tv:
				share();
			break;
			case R.id.order_detail_dial:
				startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
						+ phone.getText().toString())));
			break;
			case R.id.order_detail_head:
				Intent intent = new Intent(this, GetOtherInfoActivity.class);
				intent.putExtra("otherUserId", eid);
				startActivity(intent);
			break;
			default:
			break;
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

	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					ImageLoader.getInstance().displayImage(
							hashMap.get("releaseUserImg"), head);
					name.setText(hashMap.get("releaseUserName"));
					if (orderState.equals("1")){
						dial.setImageResource(R.drawable.order_detail_phone);
						dial.setClickable(false);
						phone.setText(hashMap.get("releaseUserPhone")
								.substring(0, 3)
								+ "****"
								+ hashMap.get("releaseUserPhone").subSequence(
										6,
										hashMap.get("releaseUserPhone")
												.length() - 1));
					}else{
						dial.setImageResource(R.drawable.phone_color);
						dial.setClickable(true);
						phone.setText(hashMap.get("releaseUserPhone"));
					}
					title.setText(hashMap.get("title"));

					/*********************** label *****************************/
					String[] labels = hashMap.get("label").split(",");

					final ImageView onoff = new ImageView(
							OrderDetailActivity.this);
					LayoutParams lp = new LayoutParams(new Tools().dip2px(
							OrderDetailActivity.this, 20), new Tools().dip2px(
							OrderDetailActivity.this, 20));
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
									OrderDetailActivity.this, onoff, hint,
									(int) event.getRawX(), (int) event
											.getRawY(), false);
							System.out.println("x raw ---> " + event.getRawX());
							System.out.println("y raw ---> " + event.getRawY());
							return true;
						}
					});
					label.removeAllViews();
					label.addView(onoff);

					for(int i = 0, j = labels.length; i < j; i++){
						if (labels[i].equals("1")){
							final ImageView iv = new ImageView(
									OrderDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.zhu);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderDetailActivity.this, iv, "1",
											(int) event.getRawX(), (int) event
													.getRawY(), false);
									System.out.println("x raw ---> "
											+ event.getRawX());
									System.out.println("y raw ---> "
											+ event.getRawY());
									return true;
								}
							});
						}else if (labels[i].equals("2")){
							final ImageView iv = new ImageView(
									OrderDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.xiu);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderDetailActivity.this, iv, "2",
											(int) event.getRawX(), (int) event
													.getRawY(), false);
									System.out.println("x raw ---> "
											+ event.getRawX());
									System.out.println("y raw ---> "
											+ event.getRawY());
									return true;
								}
							});
						}else if (labels[i].equals("3")){
							final ImageView iv = new ImageView(
									OrderDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.zhi);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderDetailActivity.this, iv, "3",
											(int) event.getRawX(), (int) event
													.getRawY(), false);
									System.out.println("x raw ---> "
											+ event.getRawX());
									System.out.println("y raw ---> "
											+ event.getRawY());
									return true;
								}
							});
						}else if (labels[i].equals("4")){
							final ImageView iv = new ImageView(
									OrderDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.che);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderDetailActivity.this, iv, "4",
											(int) event.getRawX(), (int) event
													.getRawY(), false);
									System.out.println("x raw ---> "
											+ event.getRawX());
									System.out.println("y raw ---> "
											+ event.getRawY());
									return true;
								}
							});
						}else if (labels[i].equals("5")){
							final ImageView iv = new ImageView(
									OrderDetailActivity.this);
							iv.setLayoutParams(lp);
							iv.setImageResource(R.drawable.song);
							label.addView(iv);
							iv.setOnTouchListener(new OnTouchListener(){

								@Override
								public boolean onTouch(View v, MotionEvent event){
									// TODO Auto-generated method stub
									popup = new HintPopUpWindow(
											OrderDetailActivity.this, iv, "5",
											(int) event.getRawX(), (int) event
													.getRawY(), false);
									System.out.println("x raw ---> "
											+ event.getRawX());
									System.out.println("y raw ---> "
											+ event.getRawY());
									return true;
								}
							});
						}
					}
					/*********************************************************************/

					if (hashMap.get("price").equals("0")){
						price.setText("?");
					}else{
						price.setText(hashMap.get("price"));
					}
					content.setText(hashMap.get("description"));
					startDt.setText(hashMap.get("startDate"));
					endDt.setText(hashMap.get("endDate"));
					timeCount.setText(hashMap.get("countdown"));
					address.setText(hashMap.get("address"));
				break;
				case 2:
					ArrayList<String> list = (ArrayList<String>) msg.obj;
					MainGrabGvInLvAdapter adapter = new MainGrabGvInLvAdapter(
							OrderDetailActivity.this, list, parent);
					gv.setAdapter(adapter);
				break;
				case 3:
					FragmentManager fm = getFragmentManager();
					FragmentTransaction transaction = fm.beginTransaction();
					bidFragment = new OrderDetailToBidFragment();
					Bundle b = new Bundle();
					b.putString("id", id);
					b.putString("uid", uid);
					b.putString("rid", rid);
					b.putBoolean("isBid", isBid);
					b.putSerializable("quote", (ArrayList<Quote>) msg.obj);
					bidFragment.setArguments(b);
					transaction.replace(R.id.order_detail_replace, bidFragment);
					transaction.commit();
				break;
				case 4: // 我是否有报价
					final HashMap<String, String> map = (HashMap<String, String>) msg.obj;
					if (map.get("userid").equals("")){ // 我没有报价 隐藏取消报价
						hide.setVisibility(View.GONE);
						isBid = false;
					}else{
						hide.setVisibility(View.VISIBLE);
						isBid = true;
						ImageLoader.getInstance().displayImage(map.get("img"),
								myBidHead);
						myBidPrice.setText("我出价" + map.get("price") + "元");
						if (map.get("userState").equals("0")){
							selStatus.setVisibility(View.VISIBLE);
							selStatus.setText("您未被选中");
						}else{
							selStatus.setVisibility(View.VISIBLE);
							selStatus.setText("您已被选中");
						}
						myBidCancel.setOnClickListener(new OnClickListener(){

							@Override
							public void onClick(View v){
								// TODO Auto-generated method stub
								cancelHttpClient(oid, map.get("userid"));//
							}
						});
					}
				break;
				case 5:
					setBottomFragment(category);
				break;
				case 300:
					finish();
				break;
				default:
				break;
			}
		}

	};

	/**
	 * 我要报价 回调
	 * 
	 * @param
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		// TODO Auto-generated method stub
		switch(requestCode){
			case 100: // 我要报价
				if (resultCode == RESULT_OK){ // 报价成功
					hide.setVisibility(View.VISIBLE);
					final String id = data.getExtras().getString("id");
					final String uid = data.getExtras().getString("uid");
					String img = data.getExtras().getString("img");
					String price = data.getExtras().getString("price");
					ImageLoader.getInstance().displayImage(img, myBidHead);
					myBidPrice.setText("我出价" + price + "元");
					myBidCancel.setOnClickListener(new OnClickListener(){//

								@Override
								public void onClick(View v){
									// TODO Auto-generated method stub
									cancelHttpClient(oid, uid);
								}
							});
				}else{ // 取消报价
					Toast.makeText(OrderDetailActivity.this, "报价取消!",
							Toast.LENGTH_SHORT).show();
				}
			break;

			default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
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
			HttpClient.post(OrderDetailActivity.this,
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
												OrderDetailActivity.this,
												"报价已取消!", Toast.LENGTH_SHORT)
												.show();
										hide.setVisibility(View.GONE);
										Message msg = handler.obtainMessage();
										msg.what = 5;
										msg.sendToTarget();
									}else{
										Toast.makeText(
												OrderDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Toast.makeText(OrderDetailActivity.this,
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
							Toast.makeText(OrderDetailActivity.this,
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
										hashMap.put("grabOrderState",
												order.getString("orderState"));
										orderState = order
												.getString("orderState");
										hashMap.put("grabOrderUserId", order
												.getString("grabOrderUserId"));
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
										eid = order.getString("releaseUserId");
										hashMap.put("releaseUserImg", order
												.getString("releaseUserImg"));
										hashMap.put("releaseUserName", order
												.getString("releaseUserName"));
										hashMap.put("releaseUserPhone", order
												.getString("releaseUserPhone"));
										hashMap.put("startDate",
												order.getString("startDate"));
										hashMap.put("title",
												order.getString("title"));
										hashMap.put("label",
												order.getString("label"));
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

										pd.dismiss();
									}else{// 提交失败
										pd.dismiss();
										Toast.makeText(
												OrderDetailActivity.this,
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
								Toast.makeText(OrderDetailActivity.this,
										"请求失败,请重试！", Toast.LENGTH_SHORT).show();
								OrderDetailActivity.this.finish();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderDetailActivity.this,
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
										eid = order.getString("releaseUserId");
										hashMap.put("releaseUserImg", order
												.getString("releaseUserImg"));
										hashMap.put("releaseUserName", order
												.getString("releaseUserName"));
										hashMap.put("releaseUserPhone", order
												.getString("releaseUserPhone"));
										hashMap.put("startDate",
												order.getString("startDate"));
										hashMap.put("title",
												order.getString("title"));
										hashMap.put("userState",
												order.getString("userState"));
										hashMap.put("label",
												order.getString("label"));

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
										ArrayList<Quote> quote = new ArrayList<Quote>();
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

											quote.add(quoteMap);
										}
										// 显示报价fragment
										Message msg2 = handler.obtainMessage();
										msg2.what = 3;
										msg2.obj = quote;
										msg2.sendToTarget();

										pd.dismiss();
									}else{// 提交失败
										pd.dismiss();
										Toast.makeText(
												OrderDetailActivity.this,
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
								Toast.makeText(OrderDetailActivity.this,
										"请求失败,请重试！", Toast.LENGTH_SHORT).show();
								OrderDetailActivity.this.finish();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(OrderDetailActivity.this,
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

	private void setBottomFragment(String cat){
		FragmentManager fm = getFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();

		if (cat.equals("0")){ // 抢单
			grabFragment = new OrderDetailToGrabFragment();
			Bundle b = new Bundle();
			b.putString("id", id);
			b.putString("uid", uid);
			b.putString("rid", rid);
			grabFragment.setArguments(b);
			transaction.replace(R.id.order_detail_replace, grabFragment);
			grabHttpClient(id, uid);
		}else{
			// 报价
			bidHttpClient(id, uid);
		}
		transaction.commit();
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
				pp = new PushPopUpWindow(OrderDetailActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"OrderDetailActivity");
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
	 * 用户在其他地方登陆
	 */
	private void mandatoryExit(Context context){
		String CID = new Tools().getCID(OrderDetailActivity.this);
		String notificationText = new Tools()
				.getNotificationText(OrderDetailActivity.this);
		Boolean isFirstStart = new Tools()
		.getisFirstStart(OrderDetailActivity.this);
		unBindAlias(new Tools().getUserId(OrderDetailActivity.this),
				new Tools().getCID(OrderDetailActivity.this),
				OrderDetailActivity.this);
		SharedPreferences sharedPre = OrderDetailActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(OrderDetailActivity.this, parent,
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

	// protected void onPause() {
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
	 * @param
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
