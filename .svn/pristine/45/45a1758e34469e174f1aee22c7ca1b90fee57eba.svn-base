package com.youge.jobfinder.activity;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.R.id;
import com.youge.jobfinder.R.layout;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class BalacneExplainActivity extends BaseActivity implements
		OnClickListener {

	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_balacne_explain);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		default:
			break;
		}
	}
}
