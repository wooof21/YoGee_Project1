/**
 * 
 * @param
 */
package com.youge.jobfinder.vip;

import tools.Exit;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class BalanceWithdrawActivity extends BaseActivity implements
		OnClickListener{

	private ImageView back;
	private TextView balance, next;
	private EditText amount;

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.balance_withdraw);
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
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		Exit.getInstance().addActivity(this);

		back = (ImageView) findViewById(R.id.back);
		balance = (TextView) findViewById(R.id.balance_withdraw_balacne);
		next = (TextView) findViewById(R.id.balance_withdraw_next);
		amount = (EditText) findViewById(R.id.balance_withdraw_amount);

		back.setOnClickListener(this);
		next.setOnClickListener(this);
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
			case R.id.balance_withdraw_next:

			break;
			default:
			break;
		}
	}

}
