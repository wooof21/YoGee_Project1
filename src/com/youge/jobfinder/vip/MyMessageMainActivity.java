/**
 * 
 * @param
 */
package com.youge.jobfinder.vip;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import popup.PushPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.PullToRefreshLayout;
import tools.PullToRefreshLayout.OnRefreshListener;
import tools.Tools;
import view.ListViewForGrab;
import view.PullableScrollView;
import adapter.MyMessageLvItemAdapter;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class MyMessageMainActivity extends BaseActivity implements
		OnRefreshListener{

	private PullToRefreshLayout refresh;
	private PullableScrollView psv;
	private ListViewForGrab lv;
	private LinearLayout noMsg;
	private int total = 0; // 分页起始
	private int count = 10; // 分页结束

	private ArrayList<HashMap<String, String>> list;
	private MyMessageLvItemAdapter adapter;
	private registerReceiver receiver;
	private LinearLayout parent;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_message_main);//
		initView();
	}

	private void initView(){
		findViewById(R.id.back).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				finish();
			}
		});
		refresh = (PullToRefreshLayout) findViewById(R.id.my_msg_main_refresh_view);
		psv = (PullableScrollView) findViewById(R.id.my_msg_main_psv);
		lv = (ListViewForGrab) findViewById(R.id.my_msg_main_lv);
		psv.smoothScrollTo(0, 0);
		refresh.setOnRefreshListener(this);
		noMsg = (LinearLayout) findViewById(R.id.my_msg_nomsg);

		list = new ArrayList<HashMap<String, String>>();
		parent = (LinearLayout) findViewById(R.id.parent);
		adapter = new MyMessageLvItemAdapter(this, list,
				new Tools().getScreenWidth(this), handler);
		lv.setAdapter(adapter);

		msgListHttp(new Tools().getUserId(this), "r");
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			list.clear();
			msgListHttp(new Tools().getUserId(MyMessageMainActivity.this), "r");
		}

	};
	private Handler hideHandler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					lv.setVisibility(View.GONE);
					noMsg.setVisibility(View.VISIBLE);
				break;
				case 2:
					lv.setVisibility(View.VISIBLE);
					noMsg.setVisibility(View.GONE);
				break;
				default:
				break;
			}
		}

	};

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

	private void msgListHttp(String uid, final String refresh){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("total", total + "");
			job.put("count", count + "");

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.MESSAGE_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("获取消息接口返回  ---> " + str);
								String message = "";
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										JSONArray news = data
												.getJSONArray("news");
										ArrayList<HashMap<String, String>> single = new ArrayList<HashMap<String, String>>();
										for(int i = 0, j = news.length(); i < j; i++){
											JSONObject _news = news
													.optJSONObject(i);
											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put("content",
													_news.getString("content"));
											hashMap.put("id",
													_news.getString("id"));
											hashMap.put("orderTime", _news
													.getString("orderTime"));
											hashMap.put("title",
													_news.getString("title"));
											hashMap.put("type",
													_news.getString("type"));
											hashMap.put("goodOrder", _news
													.getString("goodOrder"));
											hashMap.put("orderState", _news
													.getString("orderState"));
											hashMap.put("style",
													_news.getString("style"));

											single.add(hashMap);
										}
										if (refresh.equals("r")){
											list.clear();
											list.addAll(single);
										}else{
											list.addAll(single);
										}

										Message msg = hideHandler
												.obtainMessage();
										if (list.size() == 0){
											msg.what = 1;
										}else{
											msg.what = 2;
										}
										msg.sendToTarget();
										adapter.notifyDataSetChanged();
									}else{
										Toast.makeText(
												MyMessageMainActivity.this,
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
							Toast.makeText(MyMessageMainActivity.this,
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
				pp = new PushPopUpWindow(MyMessageMainActivity.this, parent,
						new Tools().getNotificationText(context),
						new Tools().getNotificationType(context),
						"MyMessageMainActivity");
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
	private void mandatoryExit(Context context){
		String CID = new Tools().getCID(MyMessageMainActivity.this);
		String notificationText = new Tools()
				.getNotificationText(MyMessageMainActivity.this);
		Boolean isFirstStart = new Tools()
				.getisFirstStart(MyMessageMainActivity.this);
		unBindAlias(new Tools().getUserId(MyMessageMainActivity.this),
				new Tools().getCID(MyMessageMainActivity.this),
				MyMessageMainActivity.this);
		SharedPreferences sharedPre = MyMessageMainActivity.this
				.getSharedPreferences("user", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPre.edit();

		editor.clear();
		editor.putString("cid", CID);
		editor.putBoolean("isFirstStart", isFirstStart);
		editor.putString("notificationText", notificationText);
		editor.commit();
		new ContentPopUpWindowSingleButton(MyMessageMainActivity.this, parent,
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

	@Override
	protected void onStop(){
		// TODO Auto-generated method stub
		unregisterReceiver(receiver);
		super.onStop();
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
				msgListHttp(new Tools().getUserId(MyMessageMainActivity.this),
						"r");
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
				msgListHttp(new Tools().getUserId(MyMessageMainActivity.this),
						"m");
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}

}
