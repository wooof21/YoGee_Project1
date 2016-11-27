package find;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import model.Quote;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.HttpClient;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.R.id;
import com.youge.jobfinder.R.layout;
import com.youge.jobfinder.activity.OrderDetailActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class IntegralDetailActivity extends BaseActivity implements
		OnClickListener {
	private ImageView back;
	private TextView integral_name, integral_price, integral_remarks, id;
	private ImageView integral_img;
	private String integralmallId;
	private LinearLayout integral_explain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_integral_detail);
		findView();
		initView();
		integralHttpClient();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		back = (ImageView) findViewById(R.id.back);
		id = (TextView) findViewById(R.id.id);
		integral_name = (TextView) findViewById(R.id.integral_name);
		integral_price = (TextView) findViewById(R.id.integral_price);
		integral_remarks = (TextView) findViewById(R.id.integral_remarks);
		integral_img = (ImageView) findViewById(R.id.integral_img);
		integral_explain = (LinearLayout) findViewById(R.id.integral_explain);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		integralmallId = getIntent().getStringExtra("integralmallId");
		integral_explain.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.integral_explain:
			intent = new Intent(IntegralDetailActivity.this,
					IntegralExplainActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void integralHttpClient() {

		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		try {
			// job.put("Id", integralmallId);
			job.put("id", "3");
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.SHANGPIN_DETAILS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							if (arg0 == 200) { // 成功
								String str = new String(arg2);
								System.out.println("商品详情json结果 ---> " + str);
								String message = "";
								try {
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									message = job.getString("msg");
									if (state.equals("success")) {
										JSONObject data = job
												.getJSONObject("data");
										JSONObject order = data
												.getJSONObject("Details");
										id.setText(order
												.getString("integralmallId"));
										integral_name.setText(order
												.getString("integralmallName"));
										integral_price.setText(order
												.getString("price"));
										integral_remarks.setText(order
												.getString("integralmallRemarks"));
										ImageLoader
												.getInstance()
												.displayImage(
														order.getString("integralmallImg"),
														integral_img);
										pd.dismiss();
									} else {// 提交失败
										pd.dismiss();
										Toast.makeText(
												IntegralDetailActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}

								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									pd.dismiss();
								}
							} else {
								pd.dismiss();
								Toast.makeText(IntegralDetailActivity.this,
										"请求失败,请重试！", Toast.LENGTH_SHORT).show();
								IntegralDetailActivity.this.finish();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(IntegralDetailActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			pd.dismiss();
		}

	}
}
