/**
 * 
 * @param
 */
package fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class MainBottomFragment extends Fragment implements OnClickListener {

	private LinearLayout main, order, discover, pcenter;
	private ImageView mainIv, orderIv, disIv, pcenterIv;
	private TextView mainTv, orderTv, disTv, pcenterTv;

	/**
	 * 
	 * @param
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.bottom_fragment, null);
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
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MainBottomFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("MainBottomFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
														// onPageEnd 在onPause
														// 之前调用,因为 onPause
														// 中会保存信息
		super.onPause();
	}

	private void initView(View root) {
		main = (LinearLayout) root.findViewById(R.id.bottom_frag_main_ll);
		order = (LinearLayout) root.findViewById(R.id.bottom_frag_order_ll);
		discover = (LinearLayout) root
				.findViewById(R.id.bottom_frag_discover_ll);
		pcenter = (LinearLayout) root.findViewById(R.id.bottom_frag_pcenter_ll);
		mainIv = (ImageView) root.findViewById(R.id.bottom_frag_main_iv);
		orderIv = (ImageView) root.findViewById(R.id.bottom_frag_order_iv);
		disIv = (ImageView) root.findViewById(R.id.bottom_frag_discover_iv);
		pcenterIv = (ImageView) root.findViewById(R.id.bottom_frag_pcenter_iv);
		mainTv = (TextView) root.findViewById(R.id.bottom_frag_main_tv);
		orderTv = (TextView) root.findViewById(R.id.bottom_frag_order_tv);
		disTv = (TextView) root.findViewById(R.id.bottom_frag_discover_tv);
		pcenterTv = (TextView) root.findViewById(R.id.bottom_frag_pcenter_tv);

		main.setOnClickListener(this);
		order.setOnClickListener(this);
		discover.setOnClickListener(this);
		pcenter.setOnClickListener(this);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onClick(View v) {
		// // TODO Auto-generated method stub
		// String className =
		// getActivity().getClass().getSimpleName().toString();
		// System.out.println("class name ---> " + className);
		// switch(v.getId()){
		// case R.id.bottom_frag_main_ll:
		// if (!className.equals("MainActivity")){
		// setBg(1);
		// startActivity(new Intent(getActivity(), MainActivity.class));
		// }
		// break;
		// case R.id.bottom_frag_order_ll:
		// if (!className.equals("OrderListMain")){
		// if (new Tools().isUserLogin(getActivity())){
		// setBg(2);
		// startActivity(new Intent(getActivity(),
		// OrderListMain.class));
		// }else{
		// new LoginPopUpwindow(getActivity(),
		// MainActivity.instance.parent);
		// }
		// }
		// break;
		// case R.id.bottom_frag_discover_ll:
		//
		// break;
		// case R.id.bottom_frag_pcenter_ll:
		// if (!className.equals("PersonalCenterMain")){
		// if (new Tools().isUserLogin(getActivity())){
		// setBg(4);
		// startActivity(new Intent(getActivity(),
		// PersonalCenterMain.class));
		// }else{
		// new LoginPopUpwindow(getActivity(),
		// MainActivity.instance.parent);
		// }
		//
		// }
		// break;
		// default:
		// break;
		// }
	}

	private void setBg(int id) {
		setAllToGrey();
		switch (id) {
		case 1: // 首页
			mainIv.setImageResource(R.drawable.main_green);
			mainTv.setTextColor(Color.rgb(34, 181, 112));
			break;
		case 2: // 订单
			orderIv.setImageResource(R.drawable.order_green);
			orderTv.setTextColor(Color.rgb(34, 181, 112));
			break;
		case 3: // 发现
			disIv.setImageResource(R.drawable.discover_green);
			disTv.setTextColor(Color.rgb(34, 181, 112));
			break;
		case 4: // 我的
			pcenterIv.setImageResource(R.drawable.pcenter_green);
			pcenterTv.setTextColor(Color.rgb(34, 181, 112));
			break;
		default:
			break;
		}
	}

	private void setAllToGrey() {
		mainIv.setImageResource(R.drawable.main_grey);
		mainTv.setTextColor(Color.rgb(51, 51, 51));
		orderIv.setImageResource(R.drawable.order_grey);
		orderTv.setTextColor(Color.rgb(51, 51, 51));
		disIv.setImageResource(R.drawable.discover_grey);
		disTv.setTextColor(Color.rgb(51, 51, 51));
		pcenterIv.setImageResource(R.drawable.pcenter_grey);
		pcenterTv.setTextColor(Color.rgb(51, 51, 51));
	}

}