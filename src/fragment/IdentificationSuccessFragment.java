/**
 * 
 *@param
 */
package fragment;

import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 
 *@param
 */
public class IdentificationSuccessFragment extends Fragment{

	private EditText name, idno;//
	private FrameLayout next;
	/**
	 * 
	 *@param
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.identity_certification_next, null);
		initView(view);
		
		return view;
	}
	
	/**
	 * 包含Activity、Fragment或View的应用 1. MobclickAgent.onResume()
	 * 和MobclickAgent.onPause() 方法是用来统计应用时长的(也就是Session时长,当然还包括一些其他功能) 2.
	 * MobclickAgent.onPageStart() 和 MobclickAgent.onPageEnd() 方法是用来统计页面跳转的
	 * 
	 * 在仅有Activity的程序中，SDK 自动帮助开发者调用了 2. 中的方法，并把Activity
	 * 类名作为页面名称统计。但是在包含fragment的程序中我们希望统计更详细的页面，所以需要自己调用方法做更详细的统计。
	 * 首先，需要在程序入口处，调用 MobclickAgent.openActivityDurationTrack(false)
	 * 禁止默认的页面统计方式，这样将不会再自动统计Activity。
	 * 
	 * @param
	 */
	@Override
	public void onCreate(Bundle savedInstanceState){
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onResume(){
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("IdentificationSuccessFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause(){
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("IdentificationSuccessFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		super.onPause();
	}
	
	private void initView(View root){
		name = (EditText) root.findViewById(R.id.identification_done_name);
		idno = (EditText) root.findViewById(R.id.identification_done_idno);
		next = (FrameLayout) root.findViewById(R.id.identification_done_next);
		
		next.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				
			}
		});
	}
}
