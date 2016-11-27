package com.youge.jobfinder.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class PayAgreementActivity extends BaseActivity implements OnClickListener{

	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payagreement);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
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

	@Override
	public void onClick(View v){
		switch(v.getId()){
			case R.id.back:
				finish();
			break;

			default:
			break;
		}

	}
}
