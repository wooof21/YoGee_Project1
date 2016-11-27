/**
 * 
 * @param
 */
package fragment;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.Quote;

import tools.Tools;
import view.AutoChangeLineLL;
import view.FixGridLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.ToBidActivity;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class OrderDetailToBidFragment extends Fragment{

	private FixGridLayout fgl;
	private LinearLayout click;
	private FrameLayout move;
	private TextView grab, count;
	private ImageView arrow;

	private int aclHeight;

	private int upDown = 1;

	/**
	 * 
	 * @param
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_to_bid, null);
		initView(view);

		ArrayList<Quote> quote = (ArrayList<Quote>) getArguments()
				.getSerializable("quote");
		count.setText("等共" + quote.size() + "人报价");
		setData(quote);

		new Handler().postDelayed(new Runnable(){

			@Override
			public void run(){
				// TODO Auto-generated method stub
				System.out.println("fgl line count ---> " + fgl.getLineCount());
				System.out.println("fgl 高 ---> " + fgl.getLineCount() * 60);

				int width = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				int height = View.MeasureSpec.makeMeasureSpec(0,
						View.MeasureSpec.UNSPECIFIED);
				move.measure(width, height);

				int h = (fgl.getLineCount() + 3) * 60;

				android.view.ViewGroup.LayoutParams lp = move.getLayoutParams();
				lp.height = getAclTotalHeight() + 60;
				// lp.height = h;
				System.out.println("lp height ---> " + lp.height);
				move.setLayoutParams(lp);
			}
		}, 1000);
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
		MobclickAgent.onPageStart("OrderDetailToBidFragment"); // 统计页面(仅有Activity的应用中SDK自动调用，不需要单独写)
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void onPause(){
		// TODO Auto-generated method stub
		MobclickAgent.onPageEnd("OrderDetailToBidFragment"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证
													// onPageEnd 在onPause
													// 之前调用,因为 onPause 中会保存信息
		super.onPause();
	}

	private void initView(View root){
		fgl = (FixGridLayout) root.findViewById(R.id.fragment_to_bid_container);
		click = (LinearLayout) root.findViewById(R.id.fragment_to_bid_click);
		move = (FrameLayout) root.findViewById(R.id.fragment_to_bid_move);
		grab = (TextView) root.findViewById(R.id.fragment_to_bid_grab);
		count = (TextView) root.findViewById(R.id.fragment_to_bid_tv);
		arrow = (ImageView) root.findViewById(R.id.fragment_to_bid_iv);

		final String id = getArguments().getString("id");
		final String uid = getArguments().getString("uid");
		final String rid = getArguments().getString("rid");
		boolean isBid = getArguments().getBoolean("isBid");
		if(isBid){
			grab.setVisibility(View.INVISIBLE);
		}
		click.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				// doAnimation(0, getAclTotalHeight());
				System.out.println("child count " + fgl.getChildCount());
				System.out.println("child line count" + fgl.getLineCount());
				if (fgl.getLineCount() != 0){
					if (upDown == 1){
						moveDown();
						upDown = 2;
					}else{
						moveUp();
						upDown = 1;
					}
				}
			}
		});

		grab.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (new Tools().getUserId(getActivity()).equals(rid)){
					Toast.makeText(getActivity(), "不能对自己发的单报价!",
							Toast.LENGTH_SHORT).show();
				}else{
					Intent intent = new Intent(getActivity(),
							ToBidActivity.class);
					intent.putExtra("id", id);
					intent.putExtra("uid", uid);
					getActivity().startActivityForResult(intent, 100);
				}

			}
		});
	}

	private void setData(ArrayList<Quote> quote){
		View view = null;
		// for(int i = 0, j = 14; i < j; i++){
		// view = LayoutInflater.from(getActivity()).inflate(
		// R.layout.order_detail_bid_by_whom_item, null);
		// ImageView iv = (ImageView) view
		// .findViewById(R.id.order_detail_bid_item_head);
		// TextView tv = (TextView) view
		// .findViewById(R.id.order_detail_bid_item_price);
		// iv.setImageResource(R.drawable.pic);
		// tv.setText("1111");
		//
		// fgl.addView(view);
		// }
		for(int i = 0, j = quote.size(); i < j; i++){
			view = LayoutInflater.from(getActivity()).inflate(
					R.layout.order_detail_bid_by_whom_item, null);
			ImageView iv = (ImageView) view
					.findViewById(R.id.order_detail_bid_item_head);
			TextView tv = (TextView) view
					.findViewById(R.id.order_detail_bid_item_price);
			ImageLoader.getInstance().displayImage(quote.get(i).getImg(), iv);
			tv.setText("出价" + quote.get(i).getPrice() + "元");
			fgl.addView(view);
		}
	}

	private int getAclTotalHeight(){
		return (fgl.getLineCount() + 3) * 60;
	}

	private void resetHeight(){
		android.view.ViewGroup.LayoutParams lp = move.getLayoutParams();
		lp.height = 180;
		move.setLayoutParams(lp);
	}

	private void moveDown(){
		arrow.setImageResource(R.drawable.arrow_up);
		TranslateAnimation ta = new TranslateAnimation(0, 0, 0,
				getAclTotalHeight());
		ta.setDuration(1000);
		// ta.setInterpolator(new CycleInterpolator(5));
		ta.setFillAfter(true);
		move.startAnimation(ta);
		// new Handler().postDelayed(new Runnable(){
		//
		// @Override
		// public void run(){
		// // TODO Auto-generated method stub
		// resetHeight();
		// }
		// }, 1000);
	}

	private void moveUp(){
		arrow.setImageResource(R.drawable.arrow_down);
		TranslateAnimation ta = new TranslateAnimation(0, 0,
				getAclTotalHeight(), 0);
		ta.setDuration(1000);
		// ta.setInterpolator(new CycleInterpolator(5));
		ta.setFillAfter(true);
		move.startAnimation(ta);

	}

}
