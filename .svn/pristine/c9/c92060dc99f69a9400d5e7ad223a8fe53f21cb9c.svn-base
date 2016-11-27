package com.youge.jobfinder.activity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.OrderRecords;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.HttpClient;
import tools.PullToRefreshLayout;
import tools.PullToRefreshLayout.OnRefreshListener;
import tools.Tools;
import view.ListViewForBalacne;
import view.PullableScrollView;
import adapter.MainMybalacneAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.vip.WithdrawDepositActivity;

import fragment.MainGrabFragment;

public class MainMybalacneActivity extends BaseActivity implements
		OnClickListener, OnRefreshListener {

	private ListViewForBalacne listview;
	private MainMybalacneAdapter mAdapter;
	private ImageView back, no_balacne;
	private String total = "0";
	private String count = "10";
	private ArrayList<OrderRecords> listOrderRecords;
	private TextView balacne, withdraw_deposit;
	private LinearLayout balacneExplain;
	private PullToRefreshLayout refresh;
	private PullableScrollView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_mybalacne);
		findView();
		initView();
		orderRecordsHttpClient(new Tools().getUserId(this));
	}

	/**
	 * 根据ID查找控件
	 */
	private void findView() {
		total = "0";
		refresh = (PullToRefreshLayout) findViewById(R.id.refresh_view);
		no_balacne = (ImageView) findViewById(R.id.no_balacne);
		refresh.setOnRefreshListener(MainMybalacneActivity.this);
		listview = (ListViewForBalacne) findViewById(R.id.listview);
		listview.setFocusable(false);
		balacne = (TextView) findViewById(R.id.balacne);
		back = (ImageView) findViewById(R.id.back);
		withdraw_deposit = (TextView) findViewById(R.id.withdraw_deposit);
		balacneExplain = (LinearLayout) findViewById(R.id.balacneExplain);
		sv = (PullableScrollView) findViewById(R.id.sv);
		sv.smoothScrollTo(0, 0);
		balacneExplain.setOnClickListener(this);
		withdraw_deposit.setOnClickListener(this);
	}

	/**
	 * 初始化页面
	 */
	private void initView() {
		listOrderRecords = new ArrayList<OrderRecords>();
		mAdapter = new MainMybalacneAdapter(getApplicationContext(),
				listOrderRecords);
		listview.setAdapter(mAdapter);
		// balacneExplain.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	/**
	 * handler 处理waterdroplistview刷新加载
	 */
	private Handler wdlvHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (total == "0") {
					refresh.refreshFinish(PullToRefreshLayout.SUCCEED);
				} else {
					refresh.loadmoreFinish(PullToRefreshLayout.SUCCEED);
				}
				break;
			case 2:
				if (total == "0") {
					refresh.refreshFinish(PullToRefreshLayout.FAIL);
				} else {
					refresh.loadmoreFinish(PullToRefreshLayout.FAIL);
				}
				break;
			}

		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.balacneExplain:
			intent = new Intent(MainMybalacneActivity.this,
					BalacneExplainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		case R.id.withdraw_deposit:
			intent = new Intent(MainMybalacneActivity.this,
					WithdrawDepositActivity.class);
			intent.putExtra("balance", balacne.getText().toString());
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void orderRecordsHttpClient(String uid) {
		JSONObject job = new JSONObject();

		try {
			job.put("userid", uid);
			job.put("count", count);
			job.put("total", total);

			StringEntity se = new StringEntity(job.toString(), "utf-8");
			HttpClient.post(this, Config.ORDER_RECORDS, se,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							if (arg0 == 200) {
								String str = new String(arg2);
								System.out.println("绑定List接口返回 ---> " + str);
								String message = "";
								try {
									JSONObject result = new JSONObject(str);
									String state = result.getString("state");
									message = result.getString("msg");
									if (state.equals("success")) {
										JSONObject data = result
												.getJSONObject("data");
										balacne.setText(data
												.getString("userMoney"));
										// balacnetext.put("0",
										// data.getString("userMoney"));
										JSONArray bindingList = data
												.getJSONArray("list");
										for (int i = 0, j = bindingList
												.length(); i < j; i++) {
											OrderRecords orderRecords = new OrderRecords();
											JSONObject Data = bindingList
													.getJSONObject(i);
											orderRecords.setId(Data
													.getString("id"));
											orderRecords.setTitle(Data
													.getString("title"));
											orderRecords.setType(Data
													.getString("type"));
											orderRecords.setMoney(Data
													.getString("money"));
											orderRecords.setCreateDate(Data
													.getString("createDate"));
											listOrderRecords.add(orderRecords);
										}
										if (bindingList.length() == 0) {
											no_balacne
													.setVisibility(View.VISIBLE);
											Toast.makeText(
													MainMybalacneActivity.this,
													"无收支明细", Toast.LENGTH_SHORT)
													.show();
										} else {
											no_balacne.setVisibility(View.GONE);
										}
										Message msg = wdlvHandler
												.obtainMessage();
										msg.what = 1;
										msg.sendToTarget();
										mAdapter.notifyDataSetChanged();
									} else {
										Message msg = wdlvHandler
												.obtainMessage();
										msg.what = 2;
										msg.sendToTarget();
										Toast.makeText(
												MainMybalacneActivity.this,
												message, Toast.LENGTH_SHORT)
												.show();
									}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									Message msg = wdlvHandler.obtainMessage();
									msg.what = 2;
									msg.sendToTarget();
								}
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							// 网络断开时进行相关操作
							Message msg = wdlvHandler.obtainMessage();
							msg.what = 2;
							msg.sendToTarget();
							Toast.makeText(MainMybalacneActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = wdlvHandler.obtainMessage();
			msg.what = 2;
			msg.sendToTarget();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Message msg = wdlvHandler.obtainMessage();
			msg.what = 2;
			msg.sendToTarget();
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
		}
		return false;
	}

	@Override
	public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
		total = "0";
		listOrderRecords.clear();
		orderRecordsHttpClient(new Tools().getUserId(this));
	}

	@Override
	public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
		total = Integer.parseInt(total) + Integer.parseInt(count) + "";
		orderRecordsHttpClient(new Tools().getUserId(this));
	}
}
