/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.Address;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import popup.AddAddressPopUpWindow;
import progressdialog.CustomProgressDialog;
import tools.Config;
import tools.Exit;
import tools.HttpClient;
import tools.Tools;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.ChangeUserInfoActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class AddAddressActivity extends BaseActivity implements OnClickListener {

	private ImageView back, delete;
	private EditText name, phone, address;
	private RadioGroup rg;
	private RadioButton rb1, rb2;
	private TextView commit, add_address_address_area;
	private String isSelected;

	private String id, uName, phoneNo, sex, addr, province = "", city = "",
			district = "";
	private Address aModel;
	private ArrayList<Address> aList;
	private LinearLayout parent;
	public static AddAddressActivity instance;
	private String addressStr;
	private String addressStrtwo;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_address);
		initView();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void initView() {
		// //透明状态栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// //透明导航栏
		// getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);
		instance = this;
		back = (ImageView) findViewById(R.id.back);
		delete = (ImageView) findViewById(R.id.title_iv);
		name = (EditText) findViewById(R.id.add_address_name);
		phone = (EditText) findViewById(R.id.add_address_phone);
		address = (EditText) findViewById(R.id.add_address_address);
		rg = (RadioGroup) findViewById(R.id.add_address_radiogroup);
		rb1 = (RadioButton) findViewById(R.id.add_address_rb1);
		rb2 = (RadioButton) findViewById(R.id.add_address_rb2);
		commit = (TextView) findViewById(R.id.add_address_commit);
		add_address_address_area = (TextView) findViewById(R.id.add_address_address_area);
		parent = (LinearLayout) findViewById(R.id.parent);

		back.setOnClickListener(this);
		delete.setOnClickListener(this);
		commit.setOnClickListener(this);
		add_address_address_area.setOnClickListener(this);

		sex = getIntent().getExtras().getString("sex");
		id = getIntent().getExtras().getString("id");
		uName = getIntent().getExtras().getString("name");
		phoneNo = getIntent().getExtras().getString("phone");
		addr = getIntent().getExtras().getString("address");
		if (!TextUtils.isEmpty(addr)) {
			addressStr = addr.split("\\ ")[0] + " " + addr.split("\\ ")[1]
					+ " " + addr.split("\\ ")[2];
			addressStrtwo = addr.split("\\ ")[3];
			add_address_address_area.setText(addressStr);
			address.setText(addressStrtwo);
		}
		isSelected = getIntent().getExtras().getString("isSelected");

		if (sex.equals("1")) {
			rb1.isChecked();
		} else {
			rb2.isChecked();
		}
		if (!uName.equals("")) {
			name.setText(uName);
		}
		if (!phoneNo.equals("")) {
			phone.setText(phoneNo);
		}
		if (id.equals("0")) {
			delete.setVisibility(View.GONE);
		} else {
			delete.setVisibility(View.VISIBLE);
		}

		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == rb1.getId()) {
					sex = "1";
				} else {
					sex = "2";
				}
			}
		});
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.title_iv:
			addrDeleteHttpClient(id);
			break;
		case R.id.add_address_commit:
			if (validate()) {
				addrHttpClient(new Tools().getUserId(AddAddressActivity.this));
			}
			break;
		case R.id.add_address_address_area:
			if (!TextUtils.isEmpty(add_address_address_area.getText()
					.toString())) {
				province = add_address_address_area.getText().toString()
						.split("\\ ")[0];
				city = add_address_address_area.getText().toString()
						.split("\\ ")[1];
				district = add_address_address_area.getText().toString()
						.split("\\ ")[2];
			}
			new AddAddressPopUpWindow(AddAddressActivity.this, parent,
					province, city, district);
			break;
		default:
			break;
		}
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				addressStr = (String) msg.obj;
				add_address_address_area.setText(addressStr);
				break;
			default:
				break;
			}
		}

	};

	private boolean validate() {
		if (name.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写姓名!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (phone.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写电话!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (phone.getText().toString().length() != 11) {
			Toast.makeText(this, "请填写11位电话号码!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (add_address_address_area.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写省, 市, 区!", Toast.LENGTH_SHORT).show();
			return false;
		} else if (address.getText().toString().length() == 0) {
			Toast.makeText(this, "请填写详细地址!", Toast.LENGTH_SHORT).show();
			return false;
		} else {
			return true;
		}
	}

	private void addrDeleteHttpClient(String id) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		aList = new ArrayList<Address>();

		try {
			job.put("id", id);
			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.DELETE_USER_ADDRESS_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("删除地址接口返回  ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONArray list = data
												.getJSONArray("list");
										for (int i = 0, j = list.length(); i < j; i++) {
											JSONObject job = list
													.optJSONObject(i);
											aModel = new Address();
											aModel.setId(job.getString("id"));
											aModel.setAddress(job
													.getString("address"));
											aModel.setName(job
													.getString("name"));
											aModel.setPhone(job
													.getString("phone"));
											aModel.setSex(job.getString("sex"));
											aModel.setIsSelected(job
													.getString("isSelected"));

											aList.add(aModel);
										}
										if ("1".equals(isSelected)) {
											SharedPreferences sharedPre = getSharedPreferences(
													"user",
													Context.MODE_PRIVATE);
											Editor editor = sharedPre.edit();
											editor.putString(
													"addressMainAddress", "");
											editor.putString(
													"addressMainPhone", "");
											editor.putString("addressMainName",
													"");
											editor.putString("addressMainSex",
													"");
											editor.commit();
										}
										Intent intent = getIntent();
										intent.putExtra("list", aList);
										setResult(RESULT_OK, intent);
										AddAddressActivity.this.finish();
									} else {
										Toast.makeText(AddAddressActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								Toast.makeText(AddAddressActivity.this,
										"删除失败,请重试!", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(AddAddressActivity.this,
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

	private void addrHttpClient(String uid) {
		final CustomProgressDialog pd = CustomProgressDialog.createDialog(this);
		JSONObject job = new JSONObject();
		aList = new ArrayList<Address>();
		try {
			job.put("id", id);
			job.put("userid", uid);
			job.put("name", name.getText().toString());
			job.put("phone", phone.getText().toString());
			job.put("sex", sex);
			job.put("address", add_address_address_area.getText().toString()
					+ " " + address.getText().toString());

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.CHANGE_USER_ADDRESS_URL, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// TODO Auto-generated method stub
							pd.dismiss();
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("修改添加地址接口返回  ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										JSONArray list = data
												.getJSONArray("list");
										for (int i = 0, j = list.length(); i < j; i++) {
											JSONObject job = list
													.optJSONObject(i);
											aModel = new Address();
											aModel.setId(job.getString("id"));
											aModel.setAddress(job
													.getString("address"));
											aModel.setName(job
													.getString("name"));
											aModel.setPhone(job
													.getString("phone"));
											aModel.setSex(job.getString("sex"));
											aModel.setIsSelected(job
													.getString("isSelected"));

											aList.add(aModel);
										}
										Intent intent = getIntent();
										intent.putExtra("list", aList);
										setResult(RESULT_OK, intent);
										AddAddressActivity.this.finish();
									} else {
										Toast.makeText(AddAddressActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else {
								Toast.makeText(AddAddressActivity.this,
										"提交失败,请重试!", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							pd.dismiss();
							// 网络断开时进行相关操作
							Toast.makeText(AddAddressActivity.this,
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
