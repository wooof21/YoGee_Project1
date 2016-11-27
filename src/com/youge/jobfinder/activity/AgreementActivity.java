package com.youge.jobfinder.activity;

import java.util.HashMap;
import java.util.Map;

import tools.Exit;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

public class AgreementActivity extends BaseActivity implements OnClickListener{

	private ImageView back;
	private long start, end;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		// // 透明状态栏
		// getWindow()
		// .addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		// // 透明导航栏
		// getWindow().addFlags(
		// WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		start = System.currentTimeMillis();
		Exit.getInstance().addActivity(this);
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
				statistics();
			break;

			default:
			break;
		}

	}
	
	/**
	 * 
	 *@param
	 */
	@Override
	public void onBackPressed(){
		// TODO Auto-generated method stub
		super.onBackPressed();
		statistics();
	}
	
	/**
	 * 统计用户协议阅读时间, 计算事件
	 * 
	 *@param
	 */
	private void statistics(){
		end = System.currentTimeMillis();
		int duration = (int) (end - start);
		Map<String, String> map = new HashMap<String, String>();
		map.put("type", "阅读时长");
		MobclickAgent.onEventValue(this, "agreement", map, duration);
		finish();
	}
}
