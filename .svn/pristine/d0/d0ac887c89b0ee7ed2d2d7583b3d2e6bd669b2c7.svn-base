/**
 * 
 * @param
 */
package adapter;

import java.util.ArrayList;
import java.util.HashMap;

import popup.HintPopUpWindow;

import tools.Tools;
import view.RoundImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.GetOtherInfoActivity;

import android.R.raw;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

/**
 * 
 * @param
 */
public class MyOrderListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<HashMap<String, String>> list;
	private String type;
	private LayoutInflater lInflater;

	private PopupWindow popup;
	private String hint;

	/**
	 * @param context
	 * @param list
	 */
	public MyOrderListAdapter(Context context,
			ArrayList<HashMap<String, String>> list, String type){
		super();
		this.context = context;
		this.list = list;
		this.type = type;
		this.lInflater = LayoutInflater.from(context);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getCount(){
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public Object getItem(int position){
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public long getItemId(int position){
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){
		// TODO Auto-generated method stub
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.my_order_list_new_item,
					null);
		}
		final ViewHolder vHolder = new ViewHolder();
		vHolder.head = (RoundImageView) convertView
				.findViewById(R.id.my_order_list_head);
		vHolder.name = (TextView) convertView
				.findViewById(R.id.my_order_list_name);
		vHolder.title = (TextView) convertView
				.findViewById(R.id.my_order_item_title);
		vHolder.date = (TextView) convertView
				.findViewById(R.id.my_order_list_date);
		vHolder.content = (TextView) convertView
				.findViewById(R.id.my_order_list_content);
		vHolder.address = (TextView) convertView
				.findViewById(R.id.my_order_item_addr);//
		// vHolder.distance = (TextView) convertView
		// .findViewById(R.id.my_order_list_distance);
		vHolder.status = (TextView) convertView
				.findViewById(R.id.my_order_list_status);
		// vHolder.delete = (ImageView) convertView
		// .findViewById(R.id.my_order_item_delete);
		vHolder.label = (LinearLayout) convertView
				.findViewById(R.id.my_order_list_label);
		vHolder.adrrLL = (LinearLayout) convertView
				.findViewById(R.id.my_order_item_addrll);
		vHolder.online = (LinearLayout) convertView
				.findViewById(R.id.my_order_item_onlinell);
		vHolder.endDate = (TextView) convertView
				.findViewById(R.id.my_order_item_enddate);
		vHolder.what = (TextView) convertView
				.findViewById(R.id.my_order_item_what);
		vHolder.headll = (LinearLayout) convertView
				.findViewById(R.id.my_order_list_headll);
		vHolder.hide = (RelativeLayout) convertView
				.findViewById(R.id.my_order_list_inprocess_hide);

		if (list.get(position).get("method").equals("0")){// 线上
			vHolder.adrrLL.setVisibility(View.GONE);
			vHolder.online.setVisibility(View.VISIBLE);
		}else{
			vHolder.adrrLL.setVisibility(View.VISIBLE);
			vHolder.online.setVisibility(View.GONE);
			vHolder.address.setText(list.get(position).get("address"));
		}

		vHolder.label.removeAllViews();
		LayoutParams lp = new LayoutParams(new Tools().dip2px(context, 20),
				new Tools().dip2px(context, 20));
		lp.gravity = Gravity.CENTER_VERTICAL;
		if (list.get(position).get("type").equals("1")){
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
		}else if (list.get(position).get("type").equals("2")){
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
		}else if (list.get(position).get("type").equals("3")){
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
		}else if (list.get(position).get("type").equals("4")){
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
		}else if (list.get(position).get("type").equals("5")){
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
		}else if (list.get(position).get("type").equals("6")){
			final ImageView iv = new ImageView(context);
			iv.setLayoutParams(lp);
			iv.setImageResource(R.drawable.jia);
			vHolder.label.addView(iv);
			// iv.setOnTouchListener(new OnTouchListener(){
			//
			// @Override
			// public boolean onTouch(View v, MotionEvent event){
			// // TODO Auto-generated method stub
			// new HintPopUpWindow(context, v, "5", (int) event.getRawX(),
			// (int) event.getRawY(), false);
			// return true;
			// }
			// });
		}

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head)
				.showImageOnLoading(R.drawable.default_head).build();
		ImageLoader.getInstance().displayImage(
				list.get(position).get("userimg"), vHolder.head, options);
		vHolder.name.setText(list.get(position).get("username"));
		vHolder.title.setText(list.get(position).get("title"));
		vHolder.date.setText(list.get(position).get("createDate"));
		vHolder.content.setText(list.get(position).get("synopsis"));
		vHolder.address.setText(list.get(position).get("address"));
		vHolder.endDate.setText(list.get(position).get("endDate"));
		vHolder.head.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Intent intent = new Intent(context, GetOtherInfoActivity.class);
				intent.putExtra("otherUserId", list.get(position).get("userid"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
				context.startActivity(intent);
			}
		});//

		if (list.get(position).get("style").equals("0")){
			vHolder.what.setText("我的发单");
			vHolder.what.setTextColor(Color.rgb(255, 255, 255));
			vHolder.what.setBackgroundResource(R.drawable.corner_yellow);
		}else{
			vHolder.what.setText("我的抢单");
			vHolder.what.setTextColor(Color.rgb(255, 255, 255));
			vHolder.what.setBackgroundResource(R.drawable.corner_blue);
		}

		if (type.equals("undone") || type.equals("done")){ //
			vHolder.hide.setVisibility(View.VISIBLE);
			if (list.get(position).get("orderState").equals("0")){ // 待支付
				// vHolder.delete.setVisibility(View.VISIBLE);
				vHolder.status.setText("待支付");
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}else if (list.get(position).get("orderState").equals("1")){ // 已提交
				// vHolder.delete.setVisibility(View.VISIBLE);
				vHolder.status.setText("已提交");
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}else if (list.get(position).get("orderState").equals("2")){ // 报价没选中
				// vHolder.delete.setVisibility(View.INVISIBLE);
				if (list.get(position).get("category").equals("0")){
					vHolder.status.setText("待确认");
				}else{
					vHolder.status.setText("未选中");
				}
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}else if (list.get(position).get("orderState").equals("3")){ // 进行中
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("进行中");
				vHolder.status.setTextColor(Color.rgb(34, 181, 112));
			}else if (list.get(position).get("orderState").equals("4")){ // 完成
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("已完成");
				vHolder.status.setTextColor(Color.rgb(34, 181, 112));
			}else if (list.get(position).get("orderState").equals("5")){ // 订单取消
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("已取消");
				vHolder.status.setTextColor(Color.rgb(108, 108, 108));
			}else if (list.get(position).get("orderState").equals("6")){ // 未评价
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("待评价");
				vHolder.status.setTextColor(Color.rgb(34, 181, 112));
			}else if (list.get(position).get("orderState").equals("7")){ // 已超时
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("已超时");
				vHolder.status.setTextColor(Color.rgb(108, 108, 108));
			}else if (list.get(position).get("orderState").equals("8")){ // 已选中
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("待抢单");
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}else if (list.get(position).get("orderState").equals("9")){ // 待报价
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("待报价");
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}else if (list.get(position).get("orderState").equals("10")){ // 未被选
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("未被选");
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}else if (list.get(position).get("orderState").equals("11")){ // 已选择
				// vHolder.delete.setVisibility(View.INVISIBLE);
				vHolder.status.setText("已选择");
				vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			}

		}else{
			vHolder.hide.setVisibility(View.GONE);
			// vHolder.headll.setVisibility(View.GONE);
			// // vHolder.distance.setVisibility(View.INVISIBLE);
			// vHolder.title.setText(list.get(position).get("title"));
			// vHolder.date.setText(list.get(position).get("createDate"));
			// vHolder.content.setText(list.get(position).get("synopsis"));
			// vHolder.address.setText(list.get(position).get("address"));
			// vHolder.endDate.setText(list.get(position).get("endDate"));
			// if (list.get(position).get("orderState").equals("1")){ //
			// 1没抢（无人报价(待报价
			// // 待抢单)
			// // vHolder.delete.setVisibility(View.VISIBLE);
			// if (list.get(position).get("category").equals("0")){ // 0抢单
			// vHolder.status.setText("待抢单");
			// vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			// }else{
			// vHolder.status.setText("待报价");
			// vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			// }
			// }else if (list.get(position).get("orderState").equals("0")){// 0
			// // 订单(抢单)已提交,
			// // 未支付
			// // vHolder.delete.setVisibility(View.VISIBLE);
			// vHolder.status.setText("未支付");
			// vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			// }else if (list.get(position).get("orderState").equals("2")){ //
			// 2报价没选中
			// // vHolder.delete.setVisibility(View.INVISIBLE);
			// vHolder.status.setText("待选择");
			// vHolder.status.setTextColor(Color.rgb(255, 101, 48));
			// }else if (list.get(position).get("orderState").equals("4")){ //
			// 4完成
			// // vHolder.delete.setVisibility(View.INVISIBLE);
			// vHolder.status.setText("已完成");
			// vHolder.status.setTextColor(Color.rgb(34, 181, 112));
			// }else if (list.get(position).get("orderState").equals("5")){ //
			// 5订单取消
			// // vHolder.delete.setVisibility(View.INVISIBLE);
			// vHolder.status.setText("已取消");
			// vHolder.status.setTextColor(Color.rgb(108, 108, 108));
			// }else if (list.get(position).get("orderState").equals("6")){ //
			// 6待评价
			// // vHolder.delete.setVisibility(View.INVISIBLE);
			// vHolder.status.setText("待评价");
			// vHolder.status.setTextColor(Color.rgb(34, 181, 112));
			// }else if (list.get(position).get("orderState").equals("7")){ //
			// 已超时
			// // vHolder.delete.setVisibility(View.INVISIBLE);
			// vHolder.status.setText("已超时");
			// vHolder.status.setTextColor(Color.rgb(108, 108, 108));
			// }
		}
		return convertView;
	}

	class ViewHolder{
		RoundImageView head;
		TextView name, title, type, date, content, address, status, endDate,
				what;
		LinearLayout label;
		LinearLayout adrrLL, online, headll;
		RelativeLayout hide;
	}

}
