package com.youge.jobfinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.PostChuActivity;
import com.youge.jobfinder.R;

public class SettingFingerPostActivity extends BaseActivity implements
		OnClickListener {

	private LinearLayout fa_chu, fa_bao, qiang_chu, qiang_bao;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting_finger_post);
		fa_chu = (LinearLayout) findViewById(R.id.fa_chu);
		fa_bao = (LinearLayout) findViewById(R.id.fa_bao);
		qiang_chu = (LinearLayout) findViewById(R.id.qiang_chu);
		qiang_bao = (LinearLayout) findViewById(R.id.qiang_bao);
		back = (ImageView) findViewById(R.id.back);
		fa_chu.setOnClickListener(this);
		fa_bao.setOnClickListener(this);
		qiang_chu.setOnClickListener(this);
		qiang_bao.setOnClickListener(this);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(this, PostChuActivity.class);
		;
		switch (v.getId()) {
		case R.id.fa_chu:
			intent.putExtra("isFrom", "1");
			startActivity(intent);
			break;
		case R.id.fa_bao:
			intent.putExtra("isFrom", "2");
			startActivity(intent);
			break;
		case R.id.qiang_chu:
			intent.putExtra("isFrom", "3");
			startActivity(intent);
			break;
		case R.id.qiang_bao:
			intent.putExtra("isFrom", "4");
			startActivity(intent);
			break;
		case R.id.back:
			finish();
			break;
		default:
			break;
		}

	}
}
