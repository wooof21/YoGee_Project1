package fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.ActivityModel;
import model.GrabOrderModel;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.XsPopUpWindow;
import popup.ZnPopUpWindow;
import progressdialog.NewCustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.PullToRefreshLayout;
import tools.ScrollViewListener;
import tools.PullToRefreshLayout.OnRefreshListener;
import tools.Tools;
import view.ListViewForMainGrab;
import view.MyImgScroll;
import view.PullableScrollView;
import view.ScrollListView;
import adapter.MainGrabLvItemAdapter;
import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.MainActivity;
import com.youge.jobfinder.MainGrabCallBack;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.LableGradActivity;
import com.youge.jobfinder.activity.WebViewActivity;

public class MainGrabFragment extends Fragment implements OnClickListener,
		OnRefreshListener{


	private LinearLayout oval;//
	private MyImgScroll isv;

	private ArrayList<View> adList;
	private String adType = "1";// 广告type,初始默认=1(抢单)

	private FrameLayout shaixuan, zhineng, topShaixuan, topZhineng;
	private ImageView sxIv1, sxIv2, znIv1, znIv2, topSxIv1, topSxIv2, topZnIv1, topZnIv2;
	private TextView sxTv, znTv, topSxTv, topZnTv;
	private ListViewForMainGrab lv;
	private LinearLayout parent, mLayout, adLayout, noOrder, xs_zhu, xs_song,
			xs_jia, xs_zhi, xs_xiu, xs_che;
	private int width;

	private NewCustomProgressDialog pd;

	public static MainGrabFragment instance;

	private String type, method = "2", orderType = "2", timeout = "0",
			onlinePay = "0", sort = "0", lat = "0", lng = "0", label = "0",
			distance = "3", userid = "0"; // 筛选方式默认值
	private int total = 0; // 分页起始
	private int count = 10; // 分页结束//

	private ArrayList<GrabOrderModel> allData, signleData;
	private MainGrabLvItemAdapter mAdapter;
	private View view;
	// private View view;
	private PullToRefreshLayout refresh;
	private PullableScrollView sv;

	private LinearLayout sxHide;
	private int height1, height2;
	
	private MainGrabCallBack callBack;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){//
		// TODO Auto-generated method stub

		Log.e("main grab fragment ", "onCreateView");
		view = inflater.inflate(R.layout.fragment_main_grab, null);
		// view = inflater.inflate(R.layout.main_grab_order_list_head, null);
		SharedPreferences sharedPre = getActivity().getSharedPreferences(
				"user", Context.MODE_PRIVATE);
		dY = Integer.parseInt(sharedPre.getString("mainBarHeight", "100"));
		initView();
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		System.out.println("shaixuan ---> " + width);
		getAdHttpClientPost(adType);
		return view;
	}

	/**
	 * 包含Activity、Fragment或View的应用 1. MobclickAgent.onResume()
	 * 和MobclickAgent.onPause() 方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能) 2.
	 * MobclickAgent.onPageStart() 和 MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
	 * 
	 * 在仅有Activity的程序中，SDK 自动帮助开发者调用了 2. 中的方法，并把Activity
	 * 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
	 * 首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)
	 * 禁止默认的页面统计方式，这样将不会再自动统计Activity。
	 * 
	 * @param
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.e("main grab fragment ", "onCreate");
		MobclickAgent.openActivityDurationTrack(false);
	}

	private float lastX = 0;
	private float lastY = 0, dY, mY;

	private void initView(){//
		refresh = (PullToRefreshLayout) view.findViewById(R.id.refresh_view);
		refresh.setOnRefreshListener(MainGrabFragment.this);
		oval = (LinearLayout) view.findViewById(R.id.vb);
		sv = (PullableScrollView) view.findViewById(R.id.sv);
		sv.smoothScrollTo(0, 0);
		isv = (MyImgScroll) view.findViewById(R.id.fragment_main_grab_isv);
		isv.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener(){
					@Override
					public void onGlobalLayout(){
						lastY = isv.getHeight();
						isv.getViewTreeObserver().removeGlobalOnLayoutListener(
								this);
					}
				});
		mLayout = (LinearLayout) view
				.findViewById(R.id.fragment_main_grab_layout);
		adLayout = (LinearLayout) view
				.findViewById(R.id.fragment_main_grab_adlayout);
		shaixuan = (FrameLayout) view
				.findViewById(R.id.main_center_lv_fragment_fl1);
		topShaixuan = (FrameLayout) view
				.findViewById(R.id.main_center_lv_fragment_fl11);
		zhineng = (FrameLayout) view
				.findViewById(R.id.main_center_lv_fragment_fl2);
		topZhineng = (FrameLayout) view
				.findViewById(R.id.main_center_lv_fragment_fl22);
		lv = (ListViewForMainGrab) view
				.findViewById(R.id.main_center_lv_fragment_lv);
		lv.setFocusable(false);
		sxHide = (LinearLayout) view
				.findViewById(R.id.fragment_main_grab_parent1);

		xs_zhu = (LinearLayout) view.findViewById(R.id.xs_zhu);
		xs_song = (LinearLayout) view.findViewById(R.id.xs_song);
		xs_jia = (LinearLayout) view.findViewById(R.id.xs_jia);
		xs_zhi = (LinearLayout) view.findViewById(R.id.xs_zhi);
		xs_xiu = (LinearLayout) view.findViewById(R.id.xs_xiu);
		xs_che = (LinearLayout) view.findViewById(R.id.xs_che);

		sxIv1 = (ImageView) view.findViewById(R.id.shaixuanfangshi_iv1);
		topSxIv1 = (ImageView) view.findViewById(R.id.shaixuanfangshi_iv11);
		sxIv2 = (ImageView) view.findViewById(R.id.shaixuanfangshi_iv2);
		topSxIv2 = (ImageView) view.findViewById(R.id.shaixuanfangshi_iv22);
		znIv1 = (ImageView) view.findViewById(R.id.zhinengpaixu_iv1);
		topZnIv1 = (ImageView) view.findViewById(R.id.zhinengpaixu_iv12);
		znIv2 = (ImageView) view.findViewById(R.id.zhinengpaixu_iv2);//
		topZnIv2 = (ImageView) view.findViewById(R.id.zhinengpaixu_iv22);//
		sxTv = (TextView) view.findViewById(R.id.shaixuanfangshi_tv);
		topSxTv = (TextView) view.findViewById(R.id.shaixuanfangshi_tv1);
		znTv = (TextView) view.findViewById(R.id.zhinengpaixu_tv);
		topZnTv = (TextView) view.findViewById(R.id.zhinengpaixu_tv2);
		parent = (LinearLayout) view
				.findViewById(R.id.fragment_main_grab_parent);
		noOrder = (LinearLayout) view.findViewById(R.id.main_no_order);

		instance = this;

		type = "0";
		if (new Tools().isUserLogin(getActivity())){
			userid = new Tools().getUserId(getActivity());
		}else{
			userid = "0";
		}

		shaixuan.setOnClickListener(MainGrabFragment.this);
		zhineng.setOnClickListener(MainGrabFragment.this);
		topShaixuan.setOnClickListener(this);
		topZhineng.setOnClickListener(this);
		xs_zhu.setOnClickListener(MainGrabFragment.this);
		xs_song.setOnClickListener(MainGrabFragment.this);
		xs_jia.setOnClickListener(MainGrabFragment.this);
		xs_zhi.setOnClickListener(MainGrabFragment.this);
		xs_xiu.setOnClickListener(MainGrabFragment.this);
		xs_che.setOnClickListener(MainGrabFragment.this);

		// lv.setWaterDropListViewListener(MainGrabFragment.this);
		// lv.setPullLoadEnable(true);
		allData = new ArrayList<GrabOrderModel>();

		mAdapter = new MainGrabLvItemAdapter(getActivity(), allData, parent);
		lv.setAdapter(mAdapter);

		// 增加整体布局监听

		ViewTreeObserver vto = mLayout.getViewTreeObserver();

		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout(){
				// TODO Auto-generated method stub
				mLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				height1 = mLayout.getMeasuredHeight();
				Log.e("mLayout height ", height1+"");
			}

		});
		
		ViewTreeObserver vto1 = parent.getViewTreeObserver();

		vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
			@Override
			public void onGlobalLayout(){
				// TODO Auto-generated method stub
				parent.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				height2 = parent.getMeasuredHeight();
				Log.e("parent height ", height2+"");
			}

		});

		sv.setScrollViewListener(new ScrollViewListener(){

			@Override
			public void onScrollChanged(PullableScrollView scrollView, int x,
					int y, int oldx, int oldy){
				// TODO Auto-generated method stub
				if (y > (height1-height2)){
					sxHide.setVisibility(View.VISIBLE);
				}else{
					sxHide.setVisibility(View.GONE);
				}
			}
		});

	}

	// @Override
	// public void onScrollChanged(PullableScrollView scrollView, int x, int y,
	// int oldx, int oldy) {
	// // TODO Auto-generated method stub
	// final int headerHeight = (int) (lastY - dY);
	// int scrollY = (int) (getScrollY() - mY);
	// double newAlpha = ((double) scrollY / (double) headerHeight) * 255;
	// System.out.print("下拉距离---->" + newAlpha);
	// if (newAlpha <= 220 && newAlpha > 0) {
	// Message msg = MainActivity.instance.mHandler.obtainMessage();
	// msg.what = 0;
	// msg.obj = newAlpha;
	// MainActivity.instance.mHandler.sendMessage(msg);
	// } else if (newAlpha > 220) {
	// Message msg = MainActivity.instance.mHandler.obtainMessage();
	// msg.what = 0;
	// msg.obj = 220;
	// MainActivity.instance.mHandler.sendMessage(msg);
	// } else {
	// Message msg = MainActivity.instance.mHandler.obtainMessage();
	// msg.what = 0;
	// msg.obj = 0;
	// MainActivity.instance.mHandler.sendMessage(msg);
	// }
	// }

	public int getScrollY(){
		View c = lv.getChildAt(0);
		if (c == null){
			return 0;
		}
		int firstVisiblePosition = lv.getFirstVisiblePosition();
		int top = c.getTop();
		mY = c.getHeight();
		return -top + firstVisiblePosition * c.getHeight();
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public void onAttach(Activity activity){
		// TODO Auto-generated method stub
		super.onAttach(activity);
		callBack = (MainActivity) activity;
	}
	private void getData(){
		lat = callBack.getLat();
		lng = callBack.getLng();
//		lat = getArguments().getString("lat");
//		lng = getArguments().getString("lon");
//		lat = new Tools().getCurrentLat(getActivity());
//		lng = new Tools().getCurrentLng(getActivity());
		System.out.println("lat MainGrabFragment---> " + lat);
		System.out.println("lng MainGrabFragment---> " + lng);
		// 我要抢单开始默认type="0"
		getGrabListAsync(type, "f");
	}
	@Override
	public void onClick(View v){//
		// TODO Auto-generated method stub
		Intent intent = new Intent(getActivity(), LableGradActivity.class);
		intent.putExtra("lat", lat);
		intent.putExtra("lng", lng);
		switch(v.getId()){
			case R.id.main_center_lv_fragment_fl1:
				new XsPopUpWindow(getActivity(), parent, width, method,
						orderType, timeout, onlinePay, distance);
				setSxBg("grey");
			break;
			case R.id.main_center_lv_fragment_fl11://top shaixuan
				new XsPopUpWindow(getActivity(), sxHide, width, method,
						orderType, timeout, onlinePay, distance);
				setSxBg("grey");
			break;
			case R.id.main_center_lv_fragment_fl2:
				if ("智能排序".equals(znTv.getText())){
					sort = "0";
				}
				new ZnPopUpWindow(getActivity(), parent, width, sort);
				setZnBg("grey");
			break;
			case R.id.main_center_lv_fragment_fl22: //top zhineng
				if ("智能排序".equals(znTv.getText())){
					sort = "0";
				}
				new ZnPopUpWindow(getActivity(), sxHide, width, sort);
				setZnBg("grey");
			break;
			case R.id.xs_zhu:
				intent.putExtra("lable", "1");
				startActivity(intent);
			break;
			case R.id.xs_xiu:
				intent.putExtra("lable", "2");
				startActivity(intent);
			break;
			case R.id.xs_zhi:
				intent.putExtra("lable", "3");
				startActivity(intent);
			break;
			case R.id.xs_che:
				intent.putExtra("lable", "4");
				startActivity(intent);
			break;
			case R.id.xs_song:
				intent.putExtra("lable", "5");
				startActivity(intent);
			break;
			case R.id.xs_jia:
				intent.putExtra("lable", "6");
				startActivity(intent);
			break;
			default:
			break;
		}
	}

	/**
	 * 设置 筛选方式 背景颜色
	 * 
	 * @param color
	 */
	private void setSxBg(String color){
		if (color.equals("green")){
			sxIv1.setImageResource(R.drawable.shaixuanfangshi_green);
			sxIv2.setImageResource(R.drawable.tri_green_up);
			sxTv.setTextColor(Color.rgb(34, 181, 112));
			topSxIv1.setImageResource(R.drawable.shaixuanfangshi_green);
			topSxIv2.setImageResource(R.drawable.tri_green_up);
			topSxTv.setTextColor(Color.rgb(34, 181, 112));
		}else{
			sxIv1.setImageResource(R.drawable.shaixuanfangshi_grey);
			sxIv2.setImageResource(R.drawable.tri_grey_down);
			sxTv.setTextColor(Color.rgb(10, 11, 19));
			topSxIv1.setImageResource(R.drawable.shaixuanfangshi_grey);
			topSxIv2.setImageResource(R.drawable.tri_grey_down);
			topSxTv.setTextColor(Color.rgb(10, 11, 19));
		}
	}

	/**
	 * 设置 智能排序 背景颜色
	 * 
	 * @param color
	 */
	private void setZnBg(String color){
		if (color.equals("green")){
			znIv1.setImageResource(R.drawable.zhinengpaixu_green);
			znIv2.setImageResource(R.drawable.tri_green_up);
			znTv.setTextColor(Color.rgb(34, 181, 112));
			topZnIv1.setImageResource(R.drawable.zhinengpaixu_green);
			topZnIv2.setImageResource(R.drawable.tri_green_up);
			topZnTv.setTextColor(Color.rgb(34, 181, 112));
		}else{
			znIv1.setImageResource(R.drawable.zhinengpaixu_grey);
			znIv2.setImageResource(R.drawable.tri_grey_down);
			znTv.setTextColor(Color.rgb(10, 11, 19));
			topZnIv1.setImageResource(R.drawable.zhinengpaixu_grey);
			topZnIv2.setImageResource(R.drawable.tri_grey_down);
			topZnTv.setTextColor(Color.rgb(10, 11, 19));
		}
	}

	private void getGrabListAsync(final String type, final String refresh){
		SharedPreferences sharedPre = getActivity().getSharedPreferences(
				"user", Context.MODE_PRIVATE);
		if (!sharedPre.getBoolean("isFromSplash", false)){
			pd = NewCustomProgressDialog.createDialog(getActivity());
		}

		JSONObject job = new JSONObject();
		try{
			job.put("type", type);
			job.put("sort", sort);
			job.put("method", method);
			job.put("orderType", orderType);
			job.put("timeOut", timeout);
			job.put("onlinePay", onlinePay);
			// if (sort.equals("1")){
			job.put("lat", lat);
			job.put("lng", lng);
			// }else{
			// job.put("lat", "0");
			// job.put("lng", "0");
			// }
			job.put("distance", distance);
			job.put("userid", userid);
			job.put("total", "" + total);
			job.put("count", "" + count);
			job.put("label", "" + label);
			System.out.println("主页抢单提交数据Json " + "type=" + type + " ---> "
					+ job.toString());
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(getActivity(), Config.MAIN_GRAB_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							if (arg0 == 200){ // 成功
								String str = new String(arg2);
								System.out.println("主页抢单 " + "type=" + type
										+ " ---> " + str);
								try{
									JSONObject jObject = new JSONObject(str);
									String state = jObject.getString("state");
									if (state.equals("success")){
										signleData = new ArrayList<GrabOrderModel>();
										JSONObject data = jObject
												.getJSONObject("data");
										JSONArray list = data
												.getJSONArray("list");
										if (list.length() == 0 && total != 0){
											Toast.makeText(getActivity(),
													"只有这么多了~",
													Toast.LENGTH_SHORT).show();
										}
										for(int i = 0, j = list.length(); i < j; i++){
											JSONObject job = list
													.optJSONObject(i);
											GrabOrderModel gom = new GrabOrderModel();
											gom.setAddress(job
													.getString("address"));
											gom.setCategory(job
													.getString("category"));
											gom.setCreateDate(job
													.getString("createDate"));
											gom.setDistance(job
													.getString("distance"));
											gom.setId(job.getString("id"));
											gom.setPrice(job.getString("price"));
											gom.setReleaseUserId(job
													.getString("releaseUserId"));
											gom.setReleaseUserImg(job
													.getString("releaseUserImg"));
											gom.setReleaseUserName(job
													.getString("releaseUserName"));
											gom.setSynopsis(job
													.getString("synopsis"));
											gom.setTitle(job.getString("title"));
											gom.setStartDate(job
													.getString("endDate"));
											gom.setMethod(job
													.getString("method"));//
											gom.setLabel(job.getString("label"));//
											gom.setSex(job.getString("sex"));
											gom.setIdentity(job
													.getString("identity"));
											gom.setTimeout(job
													.getString("timeout"));
											gom.setPosition(i);
											gom.setShow(false);

											JSONArray imglist = job
													.getJSONArray("imgList");
											for(int m = 0, n = imglist.length(); m < n; m++){
												String img = imglist
														.optString(m);
												gom.getImgList().add(img);
											}
											JSONArray amArray = job
													.getJSONArray("activityList");
											ArrayList<ActivityModel> amList = new ArrayList<ActivityModel>();
											for(int x = 0, y = amArray.length(); x < y; x++){
												JSONObject object = amArray
														.optJSONObject(x);
												ActivityModel am = new ActivityModel();
												am.setActivityId(object
														.getString("activityId"));
												am.setActivityTitle(object
														.getString("activityTitle"));
												am.setActivityImg(object
														.getString("activityImg"));
												amList.add(am);
											}
											gom.setActivityList(amList);
											signleData.add(gom);
										}
										if (pd != null){
											pd.dismiss();
										}
										if (refresh.equals("f")){
											allData.clear();
											allData.addAll(signleData);
										}else{
											allData.addAll(signleData);
										}

										if (allData.size() == 0){
											Message msg = handler
													.obtainMessage();
											msg.what = 100;
											msg.sendToTarget();
										}else{
											noOrder.setVisibility(View.GONE);
											// lv.setPullLoadEnable(true);
										}
										mAdapter.notifyDataSetChanged();
										Message msg = handler.obtainMessage();
										msg.what = 101;
										msg.sendToTarget();
									}else{ // state = error
										if (pd != null){
											pd.dismiss();
										}
										Message msg = handler.obtainMessage();
										msg.what = 102;
										msg.sendToTarget();
										Toast.makeText(getActivity(),
												"暂无可抢单数据!", Toast.LENGTH_SHORT)
												.show();
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
									if (pd != null){
										pd.dismiss();
									}
									Message msg = handler.obtainMessage();
									msg.what = 102;
									msg.sendToTarget();
								}
							}else{// 提交失败
								if (pd != null){
									pd.dismiss();
								}
								Message msg = handler.obtainMessage();
								msg.what = 102;
								msg.sendToTarget();
								Toast.makeText(getActivity(), "暂无可抢单数据!",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							if (pd != null){
								pd.dismiss();
							}
							Message msg = handler.obtainMessage();
							msg.what = 102;
							msg.sendToTarget();
							// 网络断开时进行相关操作
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		}catch(JSONException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (pd != null){
				pd.dismiss();
			}
			Message msg = handler.obtainMessage();
			msg.what = 102;
			msg.sendToTarget();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (pd != null){
				pd.dismiss();
			}
			Message msg = handler.obtainMessage();
			msg.what = 102;
			msg.sendToTarget();
		}

	}

	/**
	 * 处理筛选,智能传值handler
	 */
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 0: // 点击取消,还原背景颜色
					setSxBg("grey");
				break;
				case 1: // 点击确定, method
					method = (String) msg.obj;
					System.out.println("确定 method ---> " + method);
					type = "1";
					znTv.setText("智能排序");
					topZnTv.setText("智能排序");
					setZnBg("grey");
				break;
				case 2: // 点击确定, orderType
					orderType = (String) msg.obj;
					System.out.println("确定 orderType ---> " + orderType);
					type = "1";
					znTv.setText("智能排序");
					topZnTv.setText("智能排序");
					setZnBg("grey");
				break;
				case 3: // 点击确定, timeout
					timeout = (String) msg.obj;
					System.out.println("确定 timeout ---> " + timeout);
					type = "1";
					znTv.setText("智能排序");
					topZnTv.setText("智能排序");
					setZnBg("grey");
				break;
				case 4: // 点击确定, onlinePay, 还原背景颜色
					onlinePay = (String) msg.obj;
					System.out.println("确定 onlinePay ---> " + onlinePay);
					type = "1";
					znTv.setText("智能排序");
					topZnTv.setText("智能排序");
					setZnBg("grey");
				break;
				case 7: // 点击确定, distance, 还原背景颜色
					distance = (String) msg.obj;
					System.out.println("确定 distance ---> " + distance);
					type = "1";
					znTv.setText("智能排序");
					topZnTv.setText("智能排序");
					setSxBg("grey");
				break;
				case 5: // 智能排序结果, sort
					total = 0;
					count = 10;
					sort = (String) msg.obj;
					method = "2";
					orderType = "2";
					timeout = "0";
					onlinePay = "0";
					distance = "3";
					if (sort.equals("1")){
						znTv.setText("距离最近");
						topZnTv.setText("距离最近");
					}else if (sort.equals("2")){
						znTv.setText("出价最高");
						topZnTv.setText("出价最高");
					}else if (sort.equals("3")){
						znTv.setText("最新发布");
						topZnTv.setText("最新发布");
					}else if (sort.equals("4")){
						znTv.setText("正在拼单");
						topZnTv.setText("正在拼单");
					}
					System.out.println("智能排序 sort ---> " + sort);
					setZnBg("grey");
					setSxBg("grey");
					type = "2";
					total = 0;
					getGrabListAsync(type, "f");//
				break;
				case 6: // 标签
					total = 0;
					count = 10;
					sort = "0";
					label = (String) msg.obj;
					setSxBg("grey");
					setZnBg("grey");
					type = "1";
					znTv.setText("智能排序");
					topZnTv.setText("智能排序");
					total = 0;
					getGrabListAsync(type, "f");
				break;
				case 100:
					// lv.setVisibility(View.GONE);
					noOrder.setVisibility(View.VISIBLE);
				// lv.disableLoadMore(true);
				break;
				case 101:
				if (total == 0) {
					refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else {
					refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				break;
			case 102:
				if (total == 0) {
					refresh.refreshFinish(PullToRefreshLayout.FAIL);
				} else {
					refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
				default:
				break;
			}
		}

	};
	/**
	 * 广告接口
	 * 
	 * @param type类型
	 */
	private void getAdHttpClientPost(final String type){
		adList = new ArrayList<View>();
		JSONObject job = new JSONObject();
		try{
			job.put("type", type);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(getActivity(), Config.MAIN_AD_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody){
							// TODO Auto-generated method stub
							if (statusCode == 200){ // 成功
								// 将byte 转换 String
								String str = new String(responseBody);
								System.out.println("广告  + " + "type=" + type
										+ " ---> " + str);
								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("guanggao");
										DisplayImageOptions options = new DisplayImageOptions.Builder()
												.showImageForEmptyUri(
														R.drawable.default_ad)
												.showImageOnFail(
														R.drawable.default_ad)
												.showImageOnLoading(
														R.drawable.default_ad)
												.build();
										for(int i = 0, j = jArray.length(); i < j; i++){
											final JSONObject child = jArray
													.optJSONObject(i);
											ImageView iv = new ImageView(
													getActivity());
											iv.setScaleType(ScaleType.FIT_XY);
											iv.setLayoutParams(new LinearLayout.LayoutParams(
													LayoutParams.FILL_PARENT,
													LayoutParams.FILL_PARENT));
											ImageLoader
													.getInstance()
													.displayImage(
															child.getString("img"),
															iv, options);
											final String website = child
													.getString("url");
											iv.setOnClickListener(new OnClickListener(){

												@Override
												public void onClick(View v){
													// TODO Auto-generated
													Intent intent = new Intent(
															getActivity(),
															WebViewActivity.class);
													intent.putExtra("url",
															website);
													startActivity(intent);
													// try {
													// Intent intent = new
													// Intent(
													// getActivity(),
													// AdvertisingDetailActivity.class);
													// intent.putExtra(
													// "detailsImg",
													// child.getString("detailsImg"));
													// intent.putExtra(
													// "title",
													// child.getString("title"));
													// startActivity(intent);
													// } catch (JSONException e)
													// {
													// // TODO Auto-generated
													// // catch block
													// e.printStackTrace();
													// }
													System.out
															.println("广告点击  website ---> "
																	+ website);
												}
											});
											adList.add(iv);
										}
										if (adList.size() != 0){
											isv.start(getActivity(), adList,
													3000, oval,
													R.layout.ad_bottom_item,
													R.id.ad_item_v,
													R.drawable.dot_focused,
													R.drawable.dot_normal);

										}

									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}else{ // 提交失败
								Toast.makeText(getActivity(), "请求广告轮播失败!",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error){
							// TODO Auto-generated method stub
							// 网络断开时进行相关操作
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
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
	public void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("main grab fragment ", "onResume");
		getData();
		MobclickAgent.onPageStart("MainGrabFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause(){
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("MainGrabFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
														// onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息
		super.onPause();
		isv.stopTimer();
	}

	@Override
	public void onRefresh(final PullToRefreshLayout pullToRefreshLayout){
		// TODO Auto-generated method stub
		callBack.reFresh();
		lat = new Tools().getCurrentLat(getActivity());
		lng = new Tools().getCurrentLng(getActivity());
		// 下拉刷新操作
		new Handler(){
			@Override
			public void handleMessage(Message msg){
				total = 0;
				getAdHttpClientPost(adType);
				type = "0";
				method = "2";
				orderType = "2";
				timeout = "0";
				onlinePay = "0";
				sort = "0";
				label = "0";
				distance = "3";
				getGrabListAsync(type, "f");
				// 千万别忘了告诉控件刷新完毕了哦！
			}
		}.sendEmptyMessageDelayed(0, 0);
	}

	@Override
	public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout){
		// TODO Auto-generated method stub
		// 加载操作
		new Handler(){
			@Override
			public void handleMessage(Message msg){
				total += count;
				getGrabListAsync(type, "n");
				// 千万别忘了告诉控件加载完毕了哦！
			}
		}.sendEmptyMessageDelayed(0, 0);
	}
}
