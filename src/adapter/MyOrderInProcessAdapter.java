/**
 * 
 *@param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import popup.HintPopUpWindow;

import tools.Tools;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.OrderListDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 *@param
 */
public class MyOrderInProcessAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private LayoutInflater lInflater;
	
	private PopupWindow popup;
	private String type;
	
	
	/**
	 * @param context
	 * @param list
	 */
	public MyOrderInProcessAdapter(Context context,
			ArrayList<HashMap<String, String>> list){
		super();
		this.context = context;
		this.list = list;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 *@param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if(convertView == null){
			convertView = lInflater.inflate(R.layout.my_order_inprocess_new_item, null);
		}
		final ViewHolder vHolder = new ViewHolder();
		vHolder.what = (TextView) convertView.findViewById(R.id.my_order_inprocess_item_what);
		vHolder.title = (TextView) convertView.findViewById(R.id.my_order_frag2_mypost_title);
		vHolder.label = (LinearLayout) convertView.findViewById(R.id.my_order_frag2_mypost_label);
		vHolder.content = (TextView) convertView.findViewById(R.id.my_order_frag2_mypost_content);
		vHolder.endDate = (TextView) convertView.findViewById(R.id.my_order_frag2_mypost_starttime);
		vHolder.address = (TextView) convertView.findViewById(R.id.my_order_frag2_mypost_address);
		vHolder.online = (LinearLayout) convertView.findViewById(R.id.my_order_frag2_mypost_onlinell);
		vHolder.addrLL = (LinearLayout) convertView.findViewById(R.id.my_order_inprocess_new_item_addrll);
		
		if(list.get(position).get("type").equals("1")){
			vHolder.what.setText("我的发单");
			vHolder.what.setTextColor(Color.rgb(45, 144, 255));
			vHolder.what.setBackgroundResource(R.drawable.corner_blue_stroke);
		}else{
			vHolder.what.setText("我的抢单");
			vHolder.what.setTextColor(Color.rgb(255, 55, 55));
			vHolder.what.setBackgroundResource(R.drawable.corner_red_stroke);
		}

		vHolder.endDate.setText(list.get(position).get("endDate"));
		vHolder.content.setText(list.get(position).get("synopsis"));
		vHolder.title.setText(list.get(position).get("title"));
		if(list.get(position).get("method").equals("0")){ //线上
			vHolder.online.setVisibility(View.VISIBLE);
			vHolder.addrLL.setVisibility(View.GONE);
		}else{
			vHolder.address.setText(list.get(position).get("address"));
		}
		vHolder.label.removeAllViews();
		LayoutParams lp = new LayoutParams(new Tools().dip2px(context, 20),
				new Tools().dip2px(context, 20));
		lp.gravity = Gravity.CENTER_VERTICAL;
		if (list.get(position).get("label").equals("1")){
			final ImageView iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.zhu);
			vHolder.label.addView(iv);
			iv.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event){
					// TODO Auto-generated method stub
					new HintPopUpWindow(context, v, "1", (int) event.getRawX(),
							(int) event.getRawY(), false);
					return true;
				}
			});
		}else if(list.get(position).get("label").equals("2")){
			final ImageView iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.xiu);
			vHolder.label.addView(iv);
			iv.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event){
					// TODO Auto-generated method stub
					new HintPopUpWindow(context, v, "2", (int) event.getRawX(),
							(int) event.getRawY(), false);
					return true;
				}
			});
		}else if(list.get(position).get("label").equals("3")){
			final ImageView iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.zhi);
			vHolder.label.addView(iv);
			iv.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event){
					// TODO Auto-generated method stub
					new HintPopUpWindow(context, v, "3", (int) event.getRawX(),
							(int) event.getRawY(), false);
					return true;
				}
			});
		}else if(list.get(position).get("label").equals("4")){
			final ImageView iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.che);
			vHolder.label.addView(iv);
			iv.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event){
					// TODO Auto-generated method stub
					new HintPopUpWindow(context, v, "4", (int) event.getRawX(),
							(int) event.getRawY(), false);
					return true;
				}
			});
		}else if(list.get(position).get("label").equals("5")){
			final ImageView iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.song);
			vHolder.label.addView(iv);
			iv.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event){
					// TODO Auto-generated method stub
					new HintPopUpWindow(context, v, "5", (int) event.getRawX(),
							(int) event.getRawY(), false);
					return true;
				}
			});
		}
		
//		String[] labels = list.get(position).get("label").split(",");
//		ImageView onoff = new ImageView(context);
//		LayoutParams lp = new LayoutParams(new Tools().dip2px(context, 20), new Tools().dip2px(context, 20));
//		lp.gravity = Gravity.CENTER_VERTICAL;
//		onoff.setLayoutParams(lp);
//		if(list.get(position).get("method").equals("1")){
//			onoff.setImageResource(R.drawable.xianshang);
//			type = "6";
//		}else{
//			onoff.setImageResource(R.drawable.xianxia);
//			type = "0";
//		}
//		onoff.setOnTouchListener(new OnTouchListener(){
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event){
//				// TODO Auto-generated method stub
//				popup = new HintPopUpWindow(context, v, type,
//						(int) event.getRawX(), (int) event.getRawY(), false);
//				System.out.println("x raw ---> " + event.getRawX());
//				System.out.println("y raw ---> " + event.getRawY());
//				return true;
//			}
//		});
//		vHolder.label.removeAllViews();
//		vHolder.label.addView(onoff);
//		for(int i=0,j=labels.length;i<j;i++){
//			if(labels[i].equals("1")){
//				final ImageView iv = new ImageView(context);
//				iv.setLayoutParams(lp);
//				iv.setImageResource(R.drawable.song);
//				vHolder.label.addView(iv);
//				iv.setOnTouchListener(new OnTouchListener(){
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event){
//						// TODO Auto-generated method stub
//						popup = new HintPopUpWindow(context, iv, "1",
//								(int) event.getRawX(), (int) event.getRawY(), false);
//						System.out.println("x raw ---> " + event.getRawX());
//						System.out.println("y raw ---> " + event.getRawY());
//						return true;
//					}
//				});
//			}else if(labels[i].equals("2")){
//				final ImageView iv = new ImageView(context);
//				iv.setLayoutParams(lp);
//				iv.setImageResource(R.drawable.che);
//				vHolder.label.addView(iv);
//				iv.setOnTouchListener(new OnTouchListener(){
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event){
//						// TODO Auto-generated method stub
//						popup = new HintPopUpWindow(context, iv, "2",
//								(int) event.getRawX(), (int) event.getRawY(), false);
//						System.out.println("x raw ---> " + event.getRawX());
//						System.out.println("y raw ---> " + event.getRawY());
//						return true;
//					}
//				});
//			}else if(labels[i].equals("3")){
//				final ImageView iv = new ImageView(context);
//				iv.setLayoutParams(lp);
//				iv.setImageResource(R.drawable.xiu);
//				vHolder.label.addView(iv);
//				iv.setOnTouchListener(new OnTouchListener(){
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event){
//						// TODO Auto-generated method stub
//						popup = new HintPopUpWindow(context, iv, "3",
//								(int) event.getRawX(), (int) event.getRawY(), false);
//						System.out.println("x raw ---> " + event.getRawX());
//						System.out.println("y raw ---> " + event.getRawY());
//						return true;
//					}
//				});
//			}else if(labels[i].equals("4")){
//				final ImageView iv = new ImageView(context);
//				iv.setLayoutParams(lp);
//				iv.setImageResource(R.drawable.zhi);
//				vHolder.label.addView(iv);
//				iv.setOnTouchListener(new OnTouchListener(){
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event){
//						// TODO Auto-generated method stub
//						popup = new HintPopUpWindow(context, iv, "4",
//								(int) event.getRawX(), (int) event.getRawY(), false);
//						System.out.println("x raw ---> " + event.getRawX());
//						System.out.println("y raw ---> " + event.getRawY());
//						return true;
//					}
//				});
//			}else if(labels[i].equals("5")){
//				final ImageView iv = new ImageView(context);
//				iv.setLayoutParams(lp);
//				iv.setImageResource(R.drawable.zhu);
//				vHolder.label.addView(iv);
//				iv.setOnTouchListener(new OnTouchListener(){
//
//					@Override
//					public boolean onTouch(View v, MotionEvent event){
//						// TODO Auto-generated method stub
//						popup = new HintPopUpWindow(context, iv, "5",
//								(int) event.getRawX(), (int) event.getRawY(), false);
//						System.out.println("x raw ---> " + event.getRawX());
//						System.out.println("y raw ---> " + event.getRawY());
//						return true;
//					}
//				});
//			}
//		}
		
//		convertView.setOnClickListener(new OnClickListener(){
//			
//			@Override
//			public void onClick(View v){
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(context, OrderListDetailActivity.class);
////				if(list.get(position).get("type").equals("1")){
////					intent.putExtra("where", "inprocess_post");
////				}else{
////					intent.putExtra("where", "inprocess_grab");
////				}
//				intent.putExtra("category", list.get(position).get("category"));
//				intent.putExtra("oid", list.get(position).get("id"));
//				context.startActivity(intent);
//			}
//		});
		
		return convertView;
	}
	
	class ViewHolder{
		TextView what;
		TextView title;
		LinearLayout label;
		TextView content;
//		TextView startDate;
		TextView endDate;
		TextView address;
		LinearLayout online, addrLL;
	}

}