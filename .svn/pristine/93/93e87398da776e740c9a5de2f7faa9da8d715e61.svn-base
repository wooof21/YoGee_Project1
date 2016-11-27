package com.youge.jobfinder;

import java.util.ArrayList;
import java.util.List;

import com.youge.jobfinder.FirstStartActivity.MyOnPageChangeListener;
import com.youge.jobfinder.FirstStartActivity.MyPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PostChuActivity extends BaseActivity implements OnClickListener {

	private ViewPager mViewPager;
	private ImageView mPage0;
	private ImageView mPage1;
	private ImageView mPage2;
	private ImageView mPage3;
	private ImageView mPage4;
	private ImageView guide1, guide2, guide3, guide4, guide5;
	private String isFrom;
	private ImageView back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_chu);
		isFrom = getIntent().getStringExtra("isFrom");

		mViewPager = (ViewPager) findViewById(R.id.whatsnew_viewpager);
		mPage0 = (ImageView) findViewById(R.id.page0);
		mPage1 = (ImageView) findViewById(R.id.page1);
		mPage2 = (ImageView) findViewById(R.id.page2);
		mPage3 = (ImageView) findViewById(R.id.page3);
		mPage4 = (ImageView) findViewById(R.id.page4);
		back = (ImageView) findViewById(R.id.back);

		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		View view1 = View.inflate(this, R.layout.post_chu_one, null);
		View view2 = View.inflate(this, R.layout.post_chu_two, null);
		View view3 = View.inflate(this, R.layout.post_chu_three, null);
		View view4 = View.inflate(this, R.layout.post_chu_four, null);
		View view5 = View.inflate(this, R.layout.post_chu_five, null);

		guide1 = (ImageView) view1.findViewById(R.id.guide1);
		guide2 = (ImageView) view2.findViewById(R.id.guide2);
		guide3 = (ImageView) view3.findViewById(R.id.guide3);
		guide4 = (ImageView) view4.findViewById(R.id.guide4);
		guide5 = (ImageView) view5.findViewById(R.id.guide5);

		if ("1".equals(isFrom)) {
			guide1.setImageResource(R.drawable.post_chu_one);
			guide2.setImageResource(R.drawable.post_chu_two);
			guide3.setImageResource(R.drawable.post_chu_three);
			guide4.setImageResource(R.drawable.post_chu_four);
		} else if ("2".equals(isFrom)) {
			guide1.setImageResource(R.drawable.post_bao_one);
			guide2.setImageResource(R.drawable.post_bao_two);
			guide3.setImageResource(R.drawable.post_bao_three);
			guide4.setImageResource(R.drawable.post_bao_four);
			guide5.setImageResource(R.drawable.post_bao_five);
		} else if ("3".equals(isFrom)) {
			guide1.setImageResource(R.drawable.get_chu_one);
			guide2.setImageResource(R.drawable.get_chu_two);
			guide3.setImageResource(R.drawable.get_chu_three);
			guide4.setImageResource(R.drawable.get_chu_four);
		} else {
			guide1.setImageResource(R.drawable.get_bao_one);
			guide2.setImageResource(R.drawable.get_bao_two);
			guide3.setImageResource(R.drawable.get_bao_three);
			guide4.setImageResource(R.drawable.get_bao_four);
		}

		ArrayList<View> views = new ArrayList<View>();
		views.add(view1);
		views.add(view2);
		views.add(view3);
		views.add(view4);
		if ("2".equals(isFrom)) {
			views.add(view5);
		}

		MyPagerAdapter mAdapter = new MyPagerAdapter(views);
		mViewPager.setAdapter(mAdapter);
		mViewPager.setCurrentItem(0);

		mPage0.setImageDrawable(getResources().getDrawable(R.drawable.page_now));
		mPage1.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage2.setImageDrawable(getResources().getDrawable(R.drawable.page));
		mPage3.setImageDrawable(getResources().getDrawable(R.drawable.page));

		if ("2".equals(isFrom)) {
			mPage4.setVisibility(View.VISIBLE);
			mPage4.setImageDrawable(getResources().getDrawable(R.drawable.page));
		}
		back.setOnClickListener(this);
	}

	public class MyPagerAdapter extends PagerAdapter {

		public List<View> mViews;

		public MyPagerAdapter(List<View> mViews) {
			this.mViews = mViews;
		}

		@Override
		public int getCount() {
			return mViews.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mViews.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mViews.get(position), 0);
			return mViews.get(position);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 1:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 2:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 3:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				break;
			case 4:
				mPage0.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage1.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage2.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage3.setImageDrawable(getResources().getDrawable(
						R.drawable.page));
				mPage4.setImageDrawable(getResources().getDrawable(
						R.drawable.page_now));
				break;
			default:
				break;
			}
		}

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
