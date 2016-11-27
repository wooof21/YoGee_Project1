/**
 * 
 * @param
 */
package discover;

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
import tools.PullToRefreshLayout;
import tools.PullToRefreshLayout.OnRefreshListener;
import tools.Tools;
import view.ListViewForGrab;
import view.PullableScrollView;
import adapter.CreditMallExchangeListAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
public class CreditMallExchangeListMain extends BaseActivity implements
		OnRefreshListener{

	private PullToRefreshLayout refresh;
	private PullableScrollView psv;
	private ListViewForGrab lv;
	private LinearLayout noRecord;
	private int count = 10, total = 0;
	private CreditMallExchangeListAdapter adapter;
	private ArrayList<HashMap<String, String>> list;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_mall_exchange_list);
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
		refresh = (PullToRefreshLayout) findViewById(R.id.credit_mall_exchange_refresh_view);
		psv = (PullableScrollView) findViewById(R.id.credit_mall_exchange_psv);
		lv = (ListViewForGrab) findViewById(R.id.credit_mall_exchange_lv);
		psv.smoothScrollTo(0, 0);
		refresh.setOnRefreshListener(this);
		noRecord = (LinearLayout) findViewById(R.id.credit_mall_exchange_norecord);

		list = new ArrayList<HashMap<String, String>>();
		adapter = new CreditMallExchangeListAdapter(this, list);
		lv.setAdapter(adapter);

		listHttp(new Tools().getUserId(this), "f");
	}

	private void listHttp(String uid, final String type){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("userid", uid);
			job.put("count", count + "");
			job.put("total", total + "");

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.CREDIT_MALL_EXCHANGE_RECORD_LIST_URL,
					se, new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("积分商城兑换记录列表接口返回  ---> "
										+ str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONArray jArray = data
												.getJSONArray("conversion");
										ArrayList<HashMap<String, String>> _list = new ArrayList<HashMap<String, String>>();
										for(int i = 0, j = jArray.length(); i < j; i++){
											JSONObject job = jArray
													.optJSONObject(i);
											HashMap<String, String> hashMap = new HashMap<String, String>();
											hashMap.put("id",
													job.getString("id"));
											hashMap.put("img",
													job.getString("img"));
											hashMap.put("name",
													job.getString("name"));
											hashMap.put("point",
													job.getString("point"));
											_list.add(hashMap);
										}
										if (type.equals("f")){
											list.clear();
											list.addAll(_list);
										}else{
											list.addAll(_list);
										}
										if(list.size() == 0){
											lv.setVisibility(View.GONE);
											noRecord.setVisibility(View.VISIBLE);
										}else{
											lv.setVisibility(View.VISIBLE);
											noRecord.setVisibility(View.GONE);
										}
										adapter.notifyDataSetChanged();
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
							Toast.makeText(CreditMallExchangeListMain.this,
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
				listHttp(
						new Tools().getUserId(CreditMallExchangeListMain.this),
						"f");
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
				listHttp(new Tools().getUserId(CreditMallExchangeListMain.this), "m");
				// 千万别忘了告诉控件加载完毕了哦！
				pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
			}
		}.sendEmptyMessageDelayed(0, 2000);
	}
}
