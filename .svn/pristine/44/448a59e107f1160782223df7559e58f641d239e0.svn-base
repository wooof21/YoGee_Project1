/**
 * 
 * @param
 */
package com.youge.jobfinder.activity;

import java.util.ArrayList;
import java.util.HashMap;

import com.youge.jobfinder.BaseActivity;
import com.youge.jobfinder.R;

import adapter.CommentListItemAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

/**
 * 
 * @param
 */
public class CommentListActivity extends BaseActivity{

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_list_main);

		findViewById(R.id.back).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				finish();
			}
		});

		ListView lv = (ListView) findViewById(R.id.comment_list_lv);
		ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>) getIntent()
				.getExtras().getSerializable("list");

		CommentListItemAdapter adapter = new CommentListItemAdapter(this, list);
		lv.setAdapter(adapter);
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
