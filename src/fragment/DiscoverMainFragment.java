package fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;

import tools.Config;
import tools.HttpClient;
import tools.Tools;
import view.ListViewForGrab;
import view.MGridView;
import view.MyImgScroll;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.WebViewActivity;

import discover.ConvenienceFacilitiyActivity;
import discover.CreditMallActivity;

import adapter.CreditMallGVAdapter;
import adapter.DiscoverMainNearbyItemAdapter;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

public class DiscoverMainFragment extends Fragment implements OnClickListener {

	private LinearLayout mall, bm, more;
	private MGridView gv;
	private ListViewForGrab lv;
	private ArrayList<HashMap<String, String>> list, lvList;
	private ArrayList<String> pics;
	private LinearLayout oval;//
	private MyImgScroll isv;
	private ArrayList<View> adList;
	private CreditMallGVAdapter adapter;
//	private DiscoverMainNearbyItemAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.discover_main, null);
		initView(view);
		// View view = inflater.inflate(R.layout.vip_update, null);
		// view.findViewById(R.id.back).setVisibility(View.GONE);
		// System.out.println("uid ---> " + new
		// Tools().getUserId(getActivity()));
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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("DiscoverMainFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("DiscoverMainFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		super.onPause();
	}

	private void initView(View view) {
		oval = (LinearLayout) view.findViewById(R.id.vb);
		isv = (MyImgScroll) view.findViewById(R.id.fragment_main_grab_isv);
		adList = new ArrayList<View>();
		// // 透明状态栏
		// getActivity().getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getActivity().getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		mall = (LinearLayout) view.findViewById(R.id.discover_mall);
		bm = (LinearLayout) view.findViewById(R.id.discover_bm);
		more = (LinearLayout) view.findViewById(R.id.discover_more);
		gv = (MGridView) view.findViewById(R.id.discover_gv);
		gv.setFocusable(false);
//		lv = (ListViewForGrab) view.findViewById(R.id.discover_lv);
//		lv.setFocusable(false);
//
		mall.setOnClickListener(this);
		bm.setOnClickListener(this);
		more.setOnClickListener(this);
//		
//		lvList = new ArrayList<HashMap<String,String>>();
//		adapter = new DiscoverMainNearbyItemAdapter(getActivity(), lvList);
//		lv.setAdapter(adapter);
		
		discoverHttp();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			gv.setColumnWidth(new Tools().getScreenWidth(getActivity()) / 2);
			gv.setNumColumns(2);
			adapter = new CreditMallGVAdapter(getActivity(), list, "2");
			gv.setAdapter(adapter);
		}

	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.discover_mall:
			startActivity(new Intent(getActivity(), CreditMallActivity.class));
			break;
		case R.id.discover_bm:
			Intent intent = new Intent(getActivity(),
					ConvenienceFacilitiyActivity.class);
			intent.putExtra("code", "0");
			intent.putExtra("name", "");
			intent.putExtra("img", "");
			startActivity(intent);
			break;
		case R.id.discover_more:
			startActivity(new Intent(getActivity(), CreditMallActivity.class));
			break;
		// case R.id.my_integral:
		// startActivity(new Intent(getActivity(),
		// MyIntegralActivity.class));
		// break;
		// case R.id.integral_store:
		// startActivity(new Intent(getActivity(),
		// CreditMallActivity.class));
		// break;
		default:
			break;
		}
	}

	private void discoverHttp() {
		list = new ArrayList<HashMap<String, String>>();
		pics = new ArrayList<String>();
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(getActivity());
		try {
			HttpClient.post(getActivity(), Config.DISCOVER_MAIN,
					new StringEntity(""), new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							String message = "";
							String str = new String(arg2);
							System.out.println("精选好礼接口返回 ---> " + str);
							try {
								JSONObject result = new JSONObject(str);
								String state = result.getString("state");
								message = result.getString("msg");
								if (state.equals("success")) {
									JSONObject data = result
											.getJSONObject("data");
									JSONArray jArray = data
											.getJSONArray("faxian");
									for (int i = 0, j = jArray.length(); i < j; i++) {
										JSONObject job = jArray
												.optJSONObject(i);
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("integralmallId",
												job.getString("integralmallId"));
										hashMap.put("integralmallName", job
												.getString("integralmallName"));
										hashMap.put(
												"integralmallPicture",
												job.getString("integralmallPicture"));
										hashMap.put("integralmallPoint", job
												.getString("integralmallPoint"));

										pics.add(job
												.getString("integralmallPicture"));
										list.add(hashMap);
									}

									JSONArray jArrayImg = data
											.getJSONArray("guanggao");
									DisplayImageOptions options = new DisplayImageOptions.Builder()
											.showImageForEmptyUri(
													R.drawable.default_ad)
											.showImageOnFail(
													R.drawable.default_ad)
											.showImageOnLoading(
													R.drawable.default_ad)
											.build();
									for (int i = 0, j = jArrayImg.length(); i < j; i++) {
										final JSONObject child = jArrayImg
												.optJSONObject(i);
										ImageView iv = new ImageView(
												getActivity());
										iv.setScaleType(ScaleType.FIT_XY);
										iv.setLayoutParams(new LinearLayout.LayoutParams(
												LayoutParams.FILL_PARENT,
												LayoutParams.FILL_PARENT));
										ImageLoader.getInstance().displayImage(
												child.getString("img"), iv,
												options);
										final String website = child
												.getString("url");
										iv.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated
												Intent intent = new Intent(
														getActivity(),
														WebViewActivity.class);
												intent.putExtra("url", website);
												startActivity(intent);
												System.out
														.println("广告点击  website ---> "
																+ website);
											}
										});
										adList.add(iv);
									}
									if (adList.size() != 0) {
										isv.start(getActivity(), adList, 3000,
												oval, R.layout.ad_bottom_item,
												R.id.ad_item_v,
												R.drawable.dot_focused,
												R.drawable.dot_normal);

									}
									Message msg = handler.obtainMessage();
									msg.sendToTarget();
								} else {
									Toast.makeText(getActivity(), message,
											Toast.LENGTH_SHORT).show();
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
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
