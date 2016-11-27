/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.util.ArrayList;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import adapter.ImageAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Gallery;

/**
 * 
 * @param
 */
public class ImageZoomActivity extends BaseActivity{

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.popup_gallery);

		Gallery gallery = (Gallery) findViewById(R.id.popup_gallery);
		ArrayList<String> pics = (ArrayList<String>) getIntent().getExtras()
				.getSerializable("list");

		gallery.setAdapter(new ImageAdapter(this, pics));
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
}
