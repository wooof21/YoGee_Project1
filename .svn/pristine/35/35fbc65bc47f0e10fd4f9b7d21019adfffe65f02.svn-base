package com.youge.jobfinder;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class AdvertisingDetailActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advertising_detail);
		String detailsImg = getIntent().getStringExtra("detailsImg");
		String title = getIntent().getStringExtra("title");
		ImageView img = (ImageView) findViewById(R.id.img);
		TextView title_title = (TextView) findViewById(R.id.title_title);
		ImageLoader.getInstance().displayImage(detailsImg, img);
		title_title.setText(title);
		ImageView back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
