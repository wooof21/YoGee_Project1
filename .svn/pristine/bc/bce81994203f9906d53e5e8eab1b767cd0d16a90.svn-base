/**
 * 
 * @param
 */
package discover;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import login.Login;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;

import tools.Config;
import tools.HttpClient;
import tools.PullToRefreshLayout;
import tools.PullToRefreshLayout.OnRefreshListener;
import tools.Tools;
import view.MGridView;
import view.MyImgScroll;
import view.PullableScrollView;
import view.RoundImageView;
import adapter.CreditMallGVAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;


import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.WebViewActivity;

import find.IntegralExplainActivity;

/**
 * 
 * @param
 */
public class CreditMallActivity extends BaseActivity implements
		OnRefreshListener, OnClickListener{

	private LinearLayout parent;
	private ImageView back;
	private PullToRefreshLayout refresh;
	private PullableScrollView psv;
	private LinearLayout oval;//
	private MyImgScroll isv;

	private RoundImageView head;
	private TextView credit;
	private TextView how;
	private TextView record;
	private MGridView mgv1, mgv2;

	private ArrayList<View> adList;

	private CreditMallGVAdapter cmAdapter1, cmAdapter2;

	private ArrayList<HashMap<String, String>> goods;

	private int count, total;
	private String uid, recordStatus;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_mall_main);
		initView();
	}

	private void initView(){
		parent = (LinearLayout) findViewById(R.id.credit_mall_main_parent);
		back = (ImageView) findViewById(R.id.back);
		refresh = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		psv = (PullableScrollView) findViewById(R.id.credit_mall_psv);
		psv.smoothScrollTo(0, 0);
		refresh.setOnRefreshListener(this);
		isv = (MyImgScroll) findViewById(R.id.credit_mall_main_isv);
		oval = (LinearLayout) findViewById(R.id.credit_mall_main_vb);

		head = (RoundImageView) findViewById(R.id.credit_mall_head);
		credit = (TextView) findViewById(R.id.credit_mall_credit);
		how = (TextView) findViewById(R.id.credit_mall_how);
		record = (TextView) findViewById(R.id.credit_mall_record);
		mgv1 = (MGridView) findViewById(R.id.credit_mall_gv11);
		mgv2 = (MGridView) findViewById(R.id.credit_mall_gv22);

		back.setOnClickListener(this);
		how.setOnClickListener(this);
		record.setOnClickListener(this);

		goods = new ArrayList<HashMap<String, String>>();
		cmAdapter2 = new CreditMallGVAdapter(this, goods, "2");
		mgv2.setAdapter(cmAdapter2);
		getAd();
	}

	private void getAd(){
		total = 0;
		count = 6;
		getAdHttpClientPost("3");
		if (new Tools().isUserLogin(this)){
			uid = new Tools().getUserId(this);
		}else{
			uid = " ";
		}
		mallHttp(uid, "f");
		// list1 = new ArrayList<HashMap<String, String>>();
		// list2 = new ArrayList<HashMap<String, String>>();
		// for(int i = 0; i < 4; i++){
		// HashMap<String, String> hashMap = new HashMap<String, String>();
		// list1.add(hashMap);
		// list2.add(hashMap);
		// }
		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put("pic", "pic");
		// map.put("credit", "140");
		//
		// adapter = new CreditMallMainAdapter(this, view, map, list1, list2);
		// wdlv.setAdapter(adapter);
		// mallHttp(new Tools().getUserId(this));
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					HashMap<String, String> user = (HashMap<String, String>) msg.obj;
					if (new Tools().isUserLogin(CreditMallActivity.this)){
						credit.setText(user.get("point"));
						ImageLoader.getInstance().displayImage(user.get("img"),
								head);
						recordStatus = "logged";
					}else{
						head.setVisibility(View.GONE);
						credit.setText("");
						how.setText("登录后查看我的积分");
						record.setText("立即登录");
						recordStatus = "login";
					}
				break;
				case 2:
					final ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) msg.obj;
					cmAdapter1 = new CreditMallGVAdapter(
							CreditMallActivity.this, list, "1");
					mgv1.setAdapter(cmAdapter1);
					mgv1.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id){
							// TODO Auto-generated method stub
							Intent intent = new Intent(CreditMallActivity.this, CreditMallCategoryMainActivity.class);
							intent.putExtra("id", list.get(position).get("id"));
							intent.putExtra("name", list.get(position).get("name"));
							startActivity(intent);
						}});
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
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
		isv.stopTimer();
	}

	private void mallHttp(String uid, final String type){
		//final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("userid", uid);
			job.put("count", count + "");
			job.put("total", total + "");
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.CREDIT_MALL_MAIN, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							//pd.dismiss();
							String str = new String(arg2);
							String message = "";
							System.out.println("积分商城主页返回 ---> " + str);
							try{
								JSONObject result = new JSONObject(str);
								String state = result.getString("state");
								message = result.getString("msg");
								if (state.equals("success")){
									JSONObject data = result
											.getJSONObject("data");
									HashMap<String, String> userMap = new HashMap<String, String>();
									userMap.put("img",
											data.getString("userimg"));
									userMap.put("point",
											data.getString("userpoint"));
									Message msg = handler.obtainMessage();
									msg.what = 1;
									msg.obj = userMap;
									msg.sendToTarget();

									JSONArray mallType = data
											.getJSONArray("malltype");
									ArrayList<HashMap<String, String>> filterSingle = new ArrayList<HashMap<String, String>>();
									for(int i = 0, j = mallType.length(); i < j; i++){
										JSONObject type = mallType
												.optJSONObject(i);
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("id", type.getString("id"));
										hashMap.put("img",
												type.getString("img"));
										hashMap.put("name",
												type.getString("name"));

										filterSingle.add(hashMap);
									}

									JSONArray mall = data.getJSONArray("mall");
									ArrayList<HashMap<String, String>> goodsSingle = new ArrayList<HashMap<String, String>>();
									for(int i = 0, j = mall.length(); i < j; i++){
										JSONObject job = mall.optJSONObject(i);
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

										goodsSingle.add(hashMap);
									}
									if (type.equals("f")){
										goods.clear();
										goods.addAll(goodsSingle);

										Message msg2 = handler.obtainMessage();
										msg2.what = 2;
										msg2.obj = filterSingle;
										msg2.sendToTarget();
									}else{
										goods.addAll(goodsSingle);
									}
									cmAdapter2.notifyDataSetChanged();
								}else{
									Toast.makeText(CreditMallActivity.this,
											message, Toast.LENGTH_SHORT).show();
								}
							}catch(JSONException e){
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							//pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(CreditMallActivity.this,
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
			HttpClient.post(this, Config.MAIN_AD_URL, se,
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
													CreditMallActivity.this);
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
															CreditMallActivity.this,
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
											isv.start(CreditMallActivity.this,
													adList, 3000, oval,
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
								Toast.makeText(CreditMallActivity.this,
										"请求广告轮播失败!", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int statusCode, Header[] headers,
								byte[] responseBody, Throwable error){
							// TODO Auto-generated method stub
							// 网络断开时进行相关操作
							Toast.makeText(CreditMallActivity.this,
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
				mallHttp(uid, "f");
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
				mallHttp(uid, "n");
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
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
			case R.id.credit_mall_how:
				startActivity(new Intent(CreditMallActivity.this,
						IntegralExplainActivity.class));
			break;
			case R.id.credit_mall_record:
				if (recordStatus.equals("logged")){
					startActivity(new Intent(CreditMallActivity.this,
							CreditMallExchangeListMain.class));
				}else{
					Intent intent = new Intent(CreditMallActivity.this,
							Login.class);
					intent.putExtra("from", "creditMall");
					startActivityForResult(intent, 1001);
				}
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
		onCreate(null);
		parent.setPadding(0, new Tools().dip2px(CreditMallActivity.this, 20), 0, 0);
	}

}
