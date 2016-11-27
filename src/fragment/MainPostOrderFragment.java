/**
 * 
 * @param
 */
package fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.Headline;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.PostOrderPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.SceneAnimation;
import tools.Tools;
import view.MGridView;
import view.MyImgScroll;
import view.PostView;
import view.PostView1;
import view.UpMarqueeTextView;
import adapter.ConvenienceFacilityGVAdapter;
import android.R.array;
import android.R.integer;
import android.app.Fragment;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.android.phone.mrpc.core.r;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.R;
import com.youge.jobfinder.R.id;
import com.youge.jobfinder.activity.MainOrderDetailActivity;
import com.youge.jobfinder.activity.WebViewActivity;

import discover.ConvenienceFacilitiyActivity;

/**
 * 
 * @param
 */
public class MainPostOrderFragment extends Fragment implements OnClickListener {

	private LinearLayout oval, parent, title_lv;
	private MyImgScroll isv;
	private MGridView gv;
	private PostView1 pv;

	private ArrayList<View> adList;
	private String adType = "2";// 广告type,2 发单

	private UpMarqueeTextView title_one, headline_text;

	private String lat, lon;

	private List<Headline> headline;
	private int count = 0;
	private TextView title_id;
	private HashMap<String, String> headlineMap;

	/**
	 * 
	 * @param
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_order_post, null);
		initView(view);
		topLineHttp();
		return view;
	}

	private void filterHttp(String type) {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(getActivity());
		JSONObject job = new JSONObject();

		try {
			job.put("type", type);
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(getActivity(),
					Config.CONVENIENCE_FACILITY_FILTER_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("便民设施三公里筛选返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("list");
										ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
										for (int i = 0, j = jArray.length(); i < j; i++) {
											JSONObject job = jArray
													.optJSONObject(i);
											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put("code",
													job.getString("code"));
											hashMap.put("id",
													job.getString("id"));
											hashMap.put("img",
													job.getString("img"));
											hashMap.put("name",
													job.getString("name"));

											list.add(hashMap);
										}
										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = list;
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
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
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

	private void topLineHttp() {
		final CustomProgressDialog pd = CustomProgressDialog
				.createDialog(getActivity());
		JSONObject job = new JSONObject();

		try {
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(getActivity(), Config.TOP_LINE, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("找事头条返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("topLine");
										for (int i = 0, j = jArray.length(); i < j; i++) {
											JSONObject job = jArray
													.optJSONObject(i);
											Headline headlines = new Headline();
											headlines.setTitle(job
													.getString("name"));
											headlines.setText(job
													.getString("title"));
											headlines.setId(job.getString("id"));
											headlines.setUrl(job
													.getString("url"));
											headline.add(headlines);
											headlineMap.put(
													job.getString("id"),
													job.getString("url"));
										}
										Message msg = handler.obtainMessage();
										msg.what = 2;
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
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
						}
					});
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		MobclickAgent.onPageStart("MainPostOrderFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("MainPostOrderFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
		// onPageEnd 在onPause
		// 之前调用,因为 onPause 中会保存信息
		super.onPause();
		isv.stopTimer();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				final ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) msg.obj;
				ConvenienceFacilityGVAdapter adapter = new ConvenienceFacilityGVAdapter(
						getActivity(), list);
				gv.setAdapter(adapter);
				gv.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						System.out.println("position ---> " + position);
						Intent intent = new Intent(getActivity(),
								ConvenienceFacilitiyActivity.class);
						intent.putExtra("code", list.get(position).get("code"));
						intent.putExtra("name", list.get(position).get("name"));
						intent.putExtra("img", list.get(position).get("img"));
						startActivity(intent);
					}
				});
				break;
			case 2:
				if (headline != null) {
					handler.post(task);// 立即调用
				}
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
			if (count == headline.size()) {
				count = 0;
			}
			if(headline.size() != 0){
				Headline headlines = headline.get(count);
				// title_one.setTextColor(Integer.parseInt(headlines.getTitlecolor()));
				// title_one.setTextColor(0xff123456);
				title_one.setText("【" + headlines.getTitle() + "】");
				headline_text.setText(headlines.getText());
				title_id.setText(headlines.getId());
				count++;
			}
		}
	};

	private void initView(View root) {
		headline = new ArrayList<Headline>();
		headlineMap = new HashMap<String, String>();
		oval = (LinearLayout) root.findViewById(R.id.fragment_main_post_vb);
		title_lv = (LinearLayout) root.findViewById(R.id.title_lv);
		isv = (MyImgScroll) root.findViewById(R.id.fragment_main_post_isv);
		title_one = (UpMarqueeTextView) root.findViewById(R.id.title_one);
		gv = (MGridView) root.findViewById(R.id.main_post_mgv);
		headline_text = (UpMarqueeTextView) root
				.findViewById(R.id.headline_text);
		title_id = (TextView) root.findViewById(R.id.title_id);
		pv = (PostView1) root.findViewById(R.id.main_post_postview);
		pv.setOnClickListener(this);
		title_lv.setOnClickListener(this);
		// post = (ImageView) root.findViewById(R.id.main_post_anim);
		// post.setOnClickListener(this);
		// getIv();
		// ViewTreeObserver vto = frame.getViewTreeObserver();
		// vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener(){
		// @Override
		// public void onGlobalLayout(){
		// frame.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		// height = frame.getHeight();
		// frame.getWidth();
		// System.out.println("height ---> " + height);
		// right = new Tools().getScreenWidth(getActivity());
		// bottom = new Tools().getScreenHeight(getActivity());
		// top = bottom - height;
		// }
		// });

		parent = (LinearLayout) root.findViewById(R.id.parent);

		// try{
		// Thread.sleep(1000);
		// System.out.println("right ---> " + right);
		// System.out.println("bottom ---> " + bottom);
		// System.out.println("top ---> " + top);
		// View view = new PostView(getActivity(), 0, top, right, bottom);
		// LayoutParams lp = new LayoutParams(
		// android.widget.LinearLayout.LayoutParams.MATCH_PARENT,
		// android.widget.LinearLayout.LayoutParams.MATCH_PARENT);
		// lp.gravity = Gravity.CENTER;
		// view.setLayoutParams(lp);
		// frame.addView(view);
		// }catch(InterruptedException e){
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// vw.setEnabled(false);
		// vw.getSettings().setUseWideViewPort(true);
		// vw.getSettings().setLoadWithOverviewMode(true);
		// vw.setInitialScale(0);
		// vw.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// vw.loadDataWithBaseURL(
		// null,
		// "<HTML><body bgcolor='#ffffff'><div align=center><IMG src='file:///android_asset/order_post_pic.gif'/></div></body></html>",
		// "text/html", "UTF-8", null);
		// fragment_main_grab_order = (LinearLayout) root
		// .findViewById(R.id.fragment_main_grab_order);
		// fragment_main_post_order = (LinearLayout) root
		// .findViewById(R.id.fragment_main_post_order);

		// fragment_main_grab_order.setOnClickListener(this);
		// fragment_main_post_order.setOnClickListener(this);

		// SharedPreferences sp = getActivity().getSharedPreferences("user",
		// Context.MODE_PRIVATE);
		// String credit = sp.getString("credit", "");
		// if(credit == null){
		// creditTv.setText("0");
		// }else{
		// creditTv.setText(credit);
		// }
		lat = getArguments().getString("lat");
		lon = getArguments().getString("lon");

		getAdHttpClientPost(adType);
		filterHttp("1");
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		Intent intent = null;
		// TODO Auto-generated method stub
		switch (v.getId()) {
		// case R.id.fragment_main_grab_order:
		// intent = new Intent(getActivity(), FillOrderMaintActivity.class);
		// intent.putExtra("category", "0");
		// intent.putExtra("lat", lat);
		// intent.putExtra("lon", lon);
		// startActivity(intent);
		// break;
		// case R.id.fragment_main_post_order:
		// intent = new Intent(getActivity(), FillOrderMaintActivity.class);
		// intent.putExtra("category", "1");
		// intent.putExtra("lat", lat);
		// intent.putExtra("lon", lon);
		// startActivity(intent);
		// break;
		case R.id.main_post_postview:
			new PostOrderPopUpWindow(getActivity(), parent,
					new Tools().getUserId(getActivity()), lat, lon);
			break;
		case R.id.title_lv:
			intent = new Intent(getActivity(), WebViewActivity.class);
			intent.putExtra("url", headlineMap.get(title_id.getText()));
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 广告接口
	 * 
	 * @param type类型
	 */
	private void getAdHttpClientPost(final String type) {
		adList = new ArrayList<View>();
		JSONObject job = new JSONObject();
		try {
			job.put("type", type);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			// pd = CustomProgressDialog.createDialog(context);
			HttpClient.post(getActivity(), Config.MAIN_AD_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int statusCode, Header[] headers,
								byte[] responseBody) {
							// TODO Auto-generated method stub
							if (statusCode == 200) { // 成功
								// 将byte 转换 String
								String str = new String(responseBody);
								System.out.println("广告  + " + "type=" + type
										+ " ---> " + str);
								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									if (state.equals("success")) {
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
										for (int i = 0, j = jArray.length(); i < j; i++) {
											JSONObject child = jArray
													.optJSONObject(i);
											ImageView iv = new ImageView(
													getActivity());
											iv.setScaleType(ScaleType.CENTER_CROP);
											iv.setLayoutParams(new LinearLayout.LayoutParams(
													LayoutParams.FILL_PARENT,
													LayoutParams.FILL_PARENT));
											ImageLoader
													.getInstance()
													.displayImage(
															child.getString("img"),
															iv, options);
											final String website = child
													.getString("id");
											iv.setOnClickListener(new OnClickListener() {

												@Override
												public void onClick(View v) {
													// TODO Auto-generated
													// method stub
													Intent intent = new Intent(
															getActivity(),
															WebViewActivity.class);
													intent.putExtra("url",
															website);
													startActivity(intent);
													System.out
															.println("广告点击  website ---> "
																	+ website);
												}
											});
											adList.add(iv);
										}
										if (adList.size() != 0) {
											isv.start(getActivity(), adList,
													3000, oval,
													R.layout.ad_bottom_item,
													R.id.ad_item_v,
													R.drawable.dot_focused,
													R.drawable.dot_normal);
											// pd.dismiss();
										}

									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									// pd.dismiss();
								}

							} else {// 提交失败
									// pd.dismiss();
								Toast.makeText(getActivity(), "请求广告轮播失败!",
										Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error) {
							// TODO Auto-generated method stub
							// pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(getActivity(), "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
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

}
