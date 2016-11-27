/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import popup.ContentPopUpWindowSingleButton;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.R.id;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @param
 */
public class ToBidActivity extends BaseActivity implements OnClickListener{

	private ImageView back;
	private EditText price;
	private TextView commit;
	private RelativeLayout parent;

	private String id, uid;

	private ContentPopUpWindowSingleButton popup;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_bid);
		initView();
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

	private void initView(){
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		back = (ImageView) findViewById(R.id.back);
		price = (EditText) findViewById(R.id.to_bid_price);
		commit = (TextView) findViewById(R.id.to_bid_commit);
		parent = (RelativeLayout) findViewById(R.id.to_bid_parent);

		back.setOnClickListener(this);
		commit.setOnClickListener(this);

		id = getIntent().getExtras().getString("id");
		uid = getIntent().getExtras().getString("uid");
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
				setResult(RESULT_CANCELED);
				finish();
			break;
			case R.id.to_bid_commit:
				if (price.getText().toString().length() != 0){
					bidHttpClient(id, uid, price.getText().toString());
				}else{
					Toast.makeText(ToBidActivity.this, "请填写报价金额!",
							Toast.LENGTH_SHORT).show();
				}
			break;
			default:
			break;
		}
	}

	private void bidHttpClient(String id, String uid, String price){
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try{
			job.put("id", id);
			job.put("userid", uid);
			job.put("price", price);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(ToBidActivity.this, Config.USER_TO_BID_URL, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200){
								String message = "";
								String str = new String(arg2);
								System.out.println("我要报价接口返回 ---> " + str);

								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")){
										popup = new ContentPopUpWindowSingleButton(
												ToBidActivity.this, parent,
												"您已成功报价, 请耐心等待雇主回复!", "", "");
										JSONObject data = job
												.getJSONObject("data");
										JSONObject quote = data
												.getJSONObject("userQuote");
										final String id = quote.getString("id");
										final String uid = quote
												.getString("userid");
										final String img = quote
												.getString("img");
										final String price = quote
												.getString("price");
										popup.setOnDismissListener(new OnDismissListener(){

											@Override
											public void onDismiss(){
												// TODO Auto-generated method
												// stub
												Log.e("popup window",
														"on dismiss");
												Intent intent = getIntent();
												intent.putExtra("id", id);
												intent.putExtra("uid", uid);
												intent.putExtra("img", img);
												intent.putExtra("price", price);
												setResult(RESULT_OK, intent);
												finish();
											}
										});
									}else{
										Toast.makeText(ToBidActivity.this,
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
							Toast.makeText(ToBidActivity.this, "网络连接失败，请检测网络！",
									Toast.LENGTH_SHORT).show();
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

}
