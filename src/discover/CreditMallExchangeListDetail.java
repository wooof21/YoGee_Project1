/**
 * 
 * @param
 */
package discover;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class CreditMallExchangeListDetail extends BaseActivity implements
		OnClickListener{

	private ImageView back, pic;
	private FrameLayout frame;
	private TextView title, credit, status, date, name, phone, address,
			backToMain;
	private String goodsId, id;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.credit_mall_exchange_list_detail);
		initView();
	}

	private void initView(){
		back = (ImageView) findViewById(R.id.back);
		pic = (ImageView) findViewById(R.id.credit_mall_exchange_list_detail_pic);
		frame = (FrameLayout) findViewById(R.id.credit_mall_exchange_list_detail_frame);
		title = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_title);
		credit = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_credit);
		status = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_status);
		date = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_date);
		name = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_name);
		phone = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_phone);
		address = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_address);
		backToMain = (TextView) findViewById(R.id.credit_mall_exchange_list_detail_backtomain);

		back.setOnClickListener(this);
		frame.setOnClickListener(this);
		backToMain.setOnClickListener(this);

		id = getIntent().getExtras().getString("id");
		System.out.println("goodsId ---> " + id);
		System.out.println("from ---> "
				+ getIntent().getExtras().getString("from", ""));
		if (getIntent().getExtras().getString("from", "").equals("")){
			detailHttp(id);
		}else{
			popDetailHttp(id, new Tools().getUserId(this));
		}
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					HashMap<String, String> hashMap = (HashMap<String, String>) msg.obj;
					ImageLoader.getInstance().displayImage(hashMap.get("img"),
							pic);
					title.setText(hashMap.get("name"));
					credit.setText(Html
							.fromHtml("<font size=\"14\" color=\"red\">"
									+ hashMap.get("point")
									+ "</font><font size=\"12\" color=\"grey\">积分</font>"));
					date.setText(hashMap.get("createDate"));
					name.setText(hashMap.get("addressName"));
					phone.setText(hashMap.get("phone"));
					address.setText(hashMap.get("addressArea"));
				break;

				default:
				break;
			}
		}

	};

	private void popDetailHttp(String id, String uid){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try{
			job.put("mallid", id);
			job.put("userid", uid);
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.CREDIT_MALL_EXCHANGE_POPUP_DETAIL_URL,
					se, new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out
										.println("积分商城商品兑换详情popup 接口返回  ---> "
												+ str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONObject job = data
												.getJSONObject("detail");
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("addressArea",
												job.getString("address"));
										hashMap.put("addressName",
												job.getString("addressName"));
										hashMap.put("createDate",
												job.getString("createDate"));
										hashMap.put("id", job.getString("id"));
										goodsId = CreditMallExchangeListDetail.this.id;
										hashMap.put("img", job.getString("img"));
										hashMap.put("name",
												job.getString("name"));
										hashMap.put("phone",
												job.getString("phone"));
										hashMap.put("point",
												job.getString("point"));

										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();
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
							Toast.makeText(CreditMallExchangeListDetail.this,
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

	private void detailHttp(String id){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("exchangeid", id);
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.CREDIT_MALL_EXCHANGE_LIST_DETAIL_URL,
					se, new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String str = new String(arg2);
								System.out.println("积分商城商品兑换记录详情接口返回  ---> "
										+ str);
								String message = "";
								try{
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")){
										JSONObject data = result
												.getJSONObject("data");
										JSONObject job = data
												.getJSONObject("detail");
										HashMap<String, String> hashMap = new HashMap<String, String>();
										hashMap.put("addressArea",
												job.getString("addressArea"));
										hashMap.put("addressName",
												job.getString("addressName"));
										hashMap.put("createDate",
												job.getString("createDate"));
										hashMap.put("id", job.getString("id"));
										hashMap.put("img", job.getString("img"));
										hashMap.put("integralmallId",
												job.getString("integralmallId"));
										goodsId = job.getString("integralmallId");
										hashMap.put("name",
												job.getString("name"));
										hashMap.put("phone",
												job.getString("phone"));
										hashMap.put("point",
												job.getString("point"));

										Message msg = handler.obtainMessage();
										msg.what = 1;
										msg.obj = hashMap;
										msg.sendToTarget();
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
							Toast.makeText(CreditMallExchangeListDetail.this,
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
	public void onClick(View v){
		// TODO Auto-generated method stub
		switch(v.getId()){
			case R.id.back:
				finish();
			break;
			case R.id.credit_mall_exchange_list_detail_frame:
				Intent intent = new Intent(CreditMallExchangeListDetail.this, CreditMallGoodsDetailActivity.class);
				intent.putExtra("id", goodsId);
				intent.putExtra("from", "list");
				startActivity(intent);
			break;
			case R.id.credit_mall_exchange_list_detail_backtomain:
				startActivity(new Intent(CreditMallExchangeListDetail.this,
						CreditMallActivity.class));
			break;
			default:
			break;
		}
	}
}
