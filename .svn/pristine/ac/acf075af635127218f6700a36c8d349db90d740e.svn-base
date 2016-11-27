package com.youge.jobfinder;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tools.Config;
import tools.HttpClient;
import tools.Tools;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public class FirstStartActivity extends BaseActivity implements OnClickListener{
	private ViewPager mViewPager;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView guide1, guide2, guide3;
	private TextView entry;
	private Boolean misScrolled = true;

	@Override
	protected void onCreate(Bundle savedInstanceState){
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_start);
		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);

		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		View view1 = View.inflate(this, R.layout.first_login_guide1, null);
		View view2 = View.inflate(this, R.layout.first_login_guide2, null);
		View view3 = View.inflate(this, R.layout.first_login_entry, null);
		View view4 = View.inflate(this, R.layout.first_login_guide4, null);
		guide1 = (ImageView) view1.findViewById(R.id.guide1);
		guide2 = (ImageView) view2.findViewById(R.id.guide2);
		guide3 = (ImageView) view3.findViewById(R.id.guide3);

		guidpageHttpClient();

		ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);

		MyPagerAdapter mAdapter = new MyPagerAdapter(views);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);

		mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
		mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));

		if (new Tools().isUserLogin(this)){
			MobclickAgent.onProfileSignIn(new Tools().getUserId(this));
		}
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

	public class MyPagerAdapter extends PagerAdapter{

		public List<View> mViews;

		public MyPagerAdapter(List<View> mViews){
			this.mViews = mViews;
		}

		@Override
		public int getCount(){
			return mViews.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object){
			((ViewPager) container).removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1){
			return arg0 == (arg1);
		}

		@Override
		public Object instantiateItem(View container, int position){
			((ViewPager) container).addView(mViews.get(position), 0);
			return mViews.get(position);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0){
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2){

		}

		@Override
		public void onPageSelected(int arg0){
			switch(arg0){
				case 0:
					mPage0.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage1.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage2.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
				break;
				case 1:
					mPage0.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage1.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
					mPage2.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
				break;
				case 2:
					mPage0.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage1.setImageDrawable(getResources().getDrawable(
							R.drawable.page));
					mPage2.setImageDrawable(getResources().getDrawable(
							R.drawable.page_now));
				break;
				case 3:
					SharedPreferences sp = getSharedPreferences("user",
							Context.MODE_PRIVATE);
					Editor editor = sp.edit();
					editor.putBoolean("isFirstStart", false);
					editor.commit();
					startActivity(new Intent(FirstStartActivity.this,
							MainActivity.class));
					FirstStartActivity.this.finish();
				break;
				default:
				break;
			}
		}

	}

	/**
	 * 
	 * 
	 */
	private void guidpageHttpClient(){
		JSONObject job = new JSONObject();
		try{
			StringEntity se = new StringEntity(job.toString());
			HttpClient.post(this, Config.GUIDE_PAGE, se,
					new AsyncHttpResponseHandler(){

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2){

							// TODO Auto-generated method stub
							String msg = "";
							if (arg0 == 200){
								// 将byte 转换 String
								String str = new String(arg2);
								Log.e("rr", "str--" + str);

								try{
									JSONObject job = new JSONObject(str);
									String state = job.getString("state");
									msg = job.getString("msg");
									DisplayImageOptions options = new DisplayImageOptions.Builder()
									.showImageForEmptyUri(
											R.drawable.default_head)
									.showImageOnFail(
											R.drawable.default_head)
									.showImageOnLoading(
											R.drawable.default_head)
									.build();
									if (state.equals("success")){
										JSONObject data = job
												.getJSONObject("data");
										JSONArray guidePage = data
												.getJSONArray("guidePage");
										
										ImageLoader.getInstance().displayImage(
												guidePage.getString(0), guide1, options);
										ImageLoader.getInstance().displayImage(
												guidePage.getString(1), guide2, options);
										ImageLoader.getInstance().displayImage(
												guidePage.getString(2), guide3, options);
									}
								}catch(JSONException e){
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else{
								Intent intent = new Intent(
										FirstStartActivity.this,
										MainActivity.class);
								startActivity(intent);
								FirstStartActivity.this.finish();
								Toast.makeText(FirstStartActivity.this, msg,
										Toast.LENGTH_SHORT).show();
							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3){
							// TODO Auto-generated method stub
							Intent intent = new Intent(FirstStartActivity.this,
									MainActivity.class);
							startActivity(intent);
							FirstStartActivity.this.finish();
							Toast.makeText(FirstStartActivity.this,
									"网络连接失败，请检测网络！", Toast.LENGTH_SHORT).show();
						}
					});
		}catch(UnsupportedEncodingException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressLint("WorldWriteableFiles")
	@Override
	public void onClick(View v){
		SharedPreferences sp = getSharedPreferences("user",
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putBoolean("isFirstStart", false);
		editor.commit();
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
