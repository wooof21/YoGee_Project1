package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class ReportActivity extends BaseActivity implements OnClickListener {
	private String oid;
	private ImageView back;
	private TextView title_tv, report_notice;
	private EditText report_content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_report);
		oid = getIntent().getStringExtra("oid");
		back = (ImageView) findViewById(R.id.back);
		title_tv = (TextView) findViewById(R.id.title_tv);
		report_content = (EditText) findViewById(R.id.report_content);
		report_notice = (TextView) findViewById(R.id.report_notice);
		back.setOnClickListener(this);
		title_tv.setOnClickListener(this);
		report_notice.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.title_tv:
			if ("".equals(report_content.getText().toString())) {
				Toast.makeText(ReportActivity.this, "举报内容不能为空！",
						Toast.LENGTH_LONG).show();
			} else {
				reportHttpClient(new Tools().getUserId(this), oid,
						report_content.getText().toString());
			}
			break;
		case R.id.report_notice:
			Intent intent = new Intent(this, ReportNoticeActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 绑定账户
	 */
	private void reportHttpClient(String uid, String oid, String content) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("orderid", oid);
			job.put("content", "content");

			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.REPORT, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("举报接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										String results = data
												.getString("result");
										if ("0".equals(results)) {
											pd.dismiss();
											Toast.makeText(ReportActivity.this,
													"提交失败", Toast.LENGTH_LONG)
													.show();
											ReportActivity.this.finish();
										} else {
											pd.dismiss();
											Toast.makeText(ReportActivity.this,
													"提交成功", Toast.LENGTH_LONG)
													.show();
										}
									} else {
										pd.dismiss();
										Toast.makeText(ReportActivity.this,
												message, Toast.LENGTH_LONG)
												.show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							} else {
								pd.dismiss();
								Toast.makeText(ReportActivity.this,
										"请求失败,请重试！", Toast.LENGTH_SHORT).show();
								ReportActivity.this.finish();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// 网络断开时进行相关操作
							pd.dismiss();
							Toast.makeText(ReportActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
