package com.youge.jobfinder.vip;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.R.drawable;
import com.youge.jobfinder.R.id;
import com.youge.jobfinder.R.layout;
import com.youge.jobfinder.activity.MainMybalacneActivity;

public class WithdrawDepositActivity extends BaseActivity implements
		OnClickListener {

	private Drawable payment_sel_mode;
	private Drawable payment_unsel_mode;
	private ImageView zhifubaotixian_cb, weixintixian_cb, back;
	private String type;
	private EditText amount_text;
	private String balance;
	private TextView amount, title_tv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_withdraw_deposit);
		findView();
		initView();
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		weixintixian_cb = (ImageView) findViewById(R.id.weixintixian_cb);
		zhifubaotixian_cb = (ImageView) findViewById(R.id.zhifubaotixian_cb);
		back = (ImageView) findViewById(R.id.back);
		amount_text = (EditText) findViewById(R.id.amount_text);
		amount = (TextView) findViewById(R.id.amount);
		title_tv = (TextView) findViewById(R.id.title_tv);
	};

	/**
	 * 
	 */
	private void initView() {
		balance = getIntent().getStringExtra("balance");
		amount.setText(balance);
		payment_sel_mode = getResources().getDrawable(
				R.drawable.withdraw_depostit_sel);
		payment_unsel_mode = getResources().getDrawable(
				R.drawable.withdraw_depostit_uns);
		changeColor(weixintixian_cb, zhifubaotixian_cb);
		type = "1";
		weixintixian_cb.setOnClickListener(this);
		zhifubaotixian_cb.setOnClickListener(this);
		title_tv.setOnClickListener(this);
		back.setOnClickListener(this);
		amount_text.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (amount_text.getText().toString().indexOf(".") >= 0) {
					if (amount_text
							.getText()
							.toString()
							.indexOf(
									".",
									amount_text.getText().toString()
											.indexOf(".") + 1) > 0) {
						amount_text.setText(amount_text
								.getText()
								.toString()
								.substring(
										0,
										amount_text.getText().toString()
												.length() - 1));
						amount_text.setSelection(amount_text.getText()
								.toString().length());
					}

				}

				if (amount_text.getText().toString().split("\\.").length == 2) {
					String end = amount_text.getText().toString().split("\\.")[1];
					if (end.length() == 3) {
						amount_text.setText(amount_text
								.getText()
								.toString()
								.substring(
										0,
										amount_text.getText().toString()
												.length() - 1));
						amount_text.setSelection(amount_text.getText()
								.toString().length());
					}

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	};

	private void changeColor(ImageView imgOne, ImageView imgOTwo) {
		imgOne.setBackgroundDrawable(payment_sel_mode);
		imgOTwo.setBackgroundDrawable(payment_unsel_mode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.weixintixian_cb:
			changeColor(weixintixian_cb, zhifubaotixian_cb);
			type = "1";
			break;
		case R.id.zhifubaotixian_cb:
			changeColor(zhifubaotixian_cb, weixintixian_cb);
			type = "2";
			break;
		case R.id.title_tv:
			if (validate()) {
				withdrawalsHttpClient(new Tools().getUserId(this));
			}
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		if (amount_text.getText().length() == 0) {
			Toast.makeText(this, "提现金额不能为0!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (strzhuanInt(amount_text.getText().toString()) == 0) {
			Toast.makeText(this, "提现金额不能为0!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (strzhuanInt(amount_text.getText().toString()) > strzhuanInt(amount
				.getText().toString())) {
			Toast.makeText(this, "提现金额大于账户余额!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void withdrawalsHttpClient(String uid) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("momey", strzhuanInt(amount_text.getText().toString()) + "");
			job.put("type", type);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.WITH_DRAWALS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("提现接口返回 ---> " + str);
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
										if ("1".equals(results)) {
											Intent intent = new Intent(
													WithdrawDepositActivity.this,
													MainMybalacneActivity.class);
											intent.putExtra("isFromTL", true);
											startActivity(intent);
											finish();
											Toast.makeText(
													WithdrawDepositActivity.this,
													"提现成功", Toast.LENGTH_SHORT)
													.show();
										} else {
											Toast.makeText(
													WithdrawDepositActivity.this,
													message, Toast.LENGTH_SHORT)
													.show();
										}
									} else {
										Toast.makeText(
												WithdrawDepositActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
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
							// 网络断开时进行相关操作
							Toast.makeText(WithdrawDepositActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
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

	private int strzhuanInt(String str) {
		if (str.split("\\.").length == 1) {
			return Integer.parseInt(str.split("\\.")[0]) * 100;
		} else if (str.split("\\.").length == 2) {
			if (str.split("\\.")[1].length() == 1) {
				return Integer.parseInt(str.split("\\.")[0]) * 100
						+ Integer.parseInt(str.split("\\.")[1]) * 10;
			} else {
				return Integer.parseInt(str.split("\\.")[0]) * 100
						+ Integer.parseInt(str.split("\\.")[1]);
			}
		} else {
			return 0;
		}
	}
}
