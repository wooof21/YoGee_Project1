/**
 * 
 * @param
 */
package com.youge.jobfinder;

import tools.Exit;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * session的统计 正确集成如下代码，才能够保证获取正确的新增用户、活跃用户、启动次数、使用时长等基本数据。
 * 
 * @param
 */
public class BaseActivity extends Activity{

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Exit.getInstance().addActivity(this);
		MobclickAgent.setDebugMode(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			setTranslucentStatus(true);
		}

		// create our manager instance after the content view is set
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// enable status bar tint
		tintManager.setStatusBarTintEnabled(true);
		// enable navigation bar tint
		tintManager.setNavigationBarTintEnabled(true);

		// set a custom tint color for all system bars
		tintManager.setTintColor(Color.parseColor("#22b570"));
		// // set a custom navigation bar resource
		// tintManager.setNavigationBarTintResource(R.drawable.my_tint);
		// // set a custom status bar drawable
		// tintManager.setStatusBarTintDrawable(MyDrawable);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on){
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on){
			winParams.flags |= bits;
		}else{
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * @param
	 */
	@Override
	protected void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("onResume activity name", this.getClass().getSimpleName());
		MobclickAgent.onPageStart(this.getClass().getSimpleName());// 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
		MobclickAgent.onResume(this);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	protected void onPause(){
		// TODO Auto-generated method stub
		super.onPause();
		Log.e("onPause activity name", this.getClass().getSimpleName());
		MobclickAgent.onPageEnd(this.getClass().getSimpleName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
																	// onPageEnd
																	// 在onPause
																	// 之前调用,因为
																	// onPause
																	// 中会保存信息
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev){
		if (ev.getAction() == MotionEvent.ACTION_DOWN){
			View v = getCurrentFocus();
			if (isShouldHideKeyboard(v, ev)){
				hideKeyboard(v.getWindowToken());
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	private boolean isShouldHideKeyboard(View v, MotionEvent event){
		if (v != null && (v instanceof EditText)){
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom){
				// 点击EditText的事件，忽略它。
				return false;
			}else{
				return true;
			}
		}
		// 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
		return false;
	}

	/**
	 * 获取InputMethodManager，隐藏软键盘
	 * 
	 * @param token
	 */
	private void hideKeyboard(IBinder token){
		if (token != null){
			InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(token,
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
