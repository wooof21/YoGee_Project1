/**
 * 
 * @param
 */
package adapter;

import ivzoom.ImagePagerActivity;

import java.util.ArrayList;
import java.util.HashMap;

import popup.HintPopUpWindow;

import model.GrabOrderModel;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.GetOtherInfoActivity;
import com.youge.jobfinder.activity.MainOrderDetailActivity;
import com.youge.jobfinder.activity.OrderDetailActivity;

import tools.Tools;
import view.MGridView;
import view.RoundImageView;

import android.R.integer;
import android.app.Activity;
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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 
 * @param
 */
public class MainGrabLvAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<GrabOrderModel> list;
	private View parent;
	private LayoutInflater lInflater;

	private PopupWindow popup;
	private String type;

	/**
	 * @param context
	 * @param list
	 */
	public MainGrabLvAdapter(Context context, ArrayList<GrabOrderModel> list,
			View parent){
		super();
		this.context = context;
		this.list = list;
		this.parent = parent;
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
		ViewHolder vHolder;
		if (convertView == null){
			convertView = lInflater.inflate(R.layout.main_grab_order_list_item,
					null);
			vHolder = new ViewHolder();
			vHolder.head = (RoundImageView) convertView
					.findViewById(R.id.my_order_lv_item_head);
			vHolder.name = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_name);
			vHolder.title = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_title);
			vHolder.time = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_time);
			vHolder.content = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_content);
			vHolder.price = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_price);
			// vHolder.gv = ((MGridView) convertView
			// .findViewById(R.id.my_order_lv_item_gv));
			vHolder.address = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_address);
			vHolder.distance = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_distance);
			vHolder.hr = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_hr);
			vHolder.todo = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_todo);
			vHolder.label = (LinearLayout) convertView
					.findViewById(R.id.my_order_lv_item_label);
			vHolder.addrLL = (LinearLayout) convertView
					.findViewById(R.id.my_order_lv_item_addrll);
//			vHolder.sex = (ImageView) convertView
//					.findViewById(R.id.my_order_lv_item_sex);
			vHolder.label1 = (ImageView) convertView
					.findViewById(R.id.my_order_lv_item_label1);
			vHolder.label2 = (ImageView) convertView
					.findViewById(R.id.my_order_lv_item_label2);
			vHolder.identity = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_identify);
			vHolder.identityPic = (ImageView) convertView
					.findViewById(R.id.my_order_lv_item_identify_pic);
			vHolder.online = (LinearLayout) convertView
					.findViewById(R.id.my_order_lv_item_online_addr);
			vHolder.picFrame = (FrameLayout) convertView
					.findViewById(R.id.my_order_lv_item_picframe);
			vHolder.pic = (ImageView) convertView
					.findViewById(R.id.my_order_lv_item_pic);
			vHolder.picTotal = (TextView) convertView
					.findViewById(R.id.my_order_lv_item_pic_total);
			convertView.setTag(vHolder);
		}else{
			vHolder = (ViewHolder) convertView.getTag();
		}

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head)
				.showImageOnLoading(R.drawable.default_head).build();
		vHolder.name.setText(list.get(position).getReleaseUserName());
		vHolder.title.setText(list.get(position).getTitle());
		vHolder.time.setText(list.get(position).getCreateDate());
		vHolder.content.setText(list.get(position).getSynopsis());
		vHolder.hr.setText(list.get(position).getStartDate());//
		vHolder.distance.setText(list.get(position).getDistance());
		System.out.println("adapter img list ---> " + list.get(position).getImgList().size());
		if (list.get(position).getImgList().size() != 0){
			vHolder.picFrame.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					list.get(position).getReleaseUserImg(), vHolder.head, options);
			System.out.println("list size ---> "
					+ list.get(position).getImgList().size());
			vHolder.picTotal.setText("共"
					+ list.get(position).getImgList().size() + "张");
			ImageLoader.getInstance().displayImage(
					list.get(position).getImgList().get(0), vHolder.pic);
			vHolder.pic.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v){
					// TODO Auto-generated method stub
					imageBrower(0, list.get(position).getImgList());
				}
			});
		}else{
			vHolder.picFrame.setVisibility(View.GONE);
		}
		if (list.get(position).getMethod().equals("0")){ // 线上
			vHolder.online.setVisibility(View.VISIBLE);
			vHolder.addrLL.setVisibility(View.GONE);
		}else{
			vHolder.online.setVisibility(View.GONE);
			vHolder.addrLL.setVisibility(View.VISIBLE);
			vHolder.address.setText(list.get(position).getAddress());
		}
		if (list.get(position).getCategory().equals("0")){ // 抢单
			vHolder.price.setText("报酬:" + list.get(position).getPrice() + "元");
			vHolder.todo.setText("抢单");
		}else{
			vHolder.price.setText("报酬:竞价中");
			vHolder.todo.setText("报价");
		}

		if (list.get(position).getSex().equals("1")){// 男
			vHolder.sex.setImageResource(R.drawable.male);
		}else{
			vHolder.sex.setImageResource(R.drawable.famale);
		}
		if (list.get(position).getLabel().equals("1")){
			vHolder.label1.setImageResource(R.drawable.zhu);
		}else if (list.get(position).getLabel().equals("2")){
			vHolder.label1.setImageResource(R.drawable.xiu);
		}else if (list.get(position).getLabel().equals("3")){
			vHolder.label1.setImageResource(R.drawable.zhi);
		}else if (list.get(position).getLabel().equals("4")){
			vHolder.label1.setImageResource(R.drawable.che);
		}else if (list.get(position).getLabel().equals("5")){
			vHolder.label1.setImageResource(R.drawable.song);
		}else if (list.get(position).getLabel().equals("6")){
			vHolder.label1.setImageResource(R.drawable.jia);
		}
		vHolder.label1.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event){
				// TODO Auto-generated method stub
				if (list.get(position).getLabel().equals("1")){
					new HintPopUpWindow(context, v, "1", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}else if (list.get(position).getLabel().equals("2")){
					new HintPopUpWindow(context, v, "2", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}else if (list.get(position).getLabel().equals("3")){
					new HintPopUpWindow(context, v, "3", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}else if (list.get(position).getLabel().equals("4")){
					new HintPopUpWindow(context, v, "4", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}else if (list.get(position).getLabel().equals("5")){
					new HintPopUpWindow(context, v, "5", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}else if (list.get(position).getLabel().equals("6")){
					new HintPopUpWindow(context, v, "9", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}
				return true;
			}
		});
		if (list.get(position).getTimeout().equals("1")){
			vHolder.label2.setVisibility(View.VISIBLE);
			vHolder.label2.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event){
					// TODO Auto-generated method stub
					new HintPopUpWindow(context, v, "8", (int) event.getRawX(),
							(int) event.getRawY(), true);
					return true;
				}
			});
		}else{
			vHolder.label2.setVisibility(View.GONE);
		}
		if (list.get(position).getIdentity().equals("1")){
			vHolder.identity.setText("已实名认证");
		}else{
			vHolder.identity.setText("未实名认证");
			vHolder.identity.setTextColor(Color.rgb(154, 154, 154));
			vHolder.identityPic.setImageResource(R.drawable.identity_grey);
		}

		convertView.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				System.out.println("category ---> "
						+ list.get(position).getCategory());//
				if (new Tools().isUserLogin(context)){
					Intent intent = new Intent(context,
							MainOrderDetailActivity.class);
					intent.putExtra("category", list.get(position)
							.getCategory());
					intent.putExtra("id", list.get(position).getId());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "您还没有登录哦!~", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		vHolder.head.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (new Tools().isUserLogin(context)){
					Intent intent = new Intent(context,
							GetOtherInfoActivity.class);
					intent.putExtra("otherUserId", list.get(position)
							.getReleaseUserId());
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "您还没有登录哦!~", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		vHolder.todo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				System.out.println("category ---> "
						+ list.get(position).getCategory());//
				if (new Tools().isUserLogin(context)){
					Intent intent = new Intent(context,
							MainOrderDetailActivity.class);
					intent.putExtra("category", list.get(position)
							.getCategory());
					intent.putExtra("id", list.get(position).getId());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				}else{
					Toast.makeText(context, "您还没有登录哦!~", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
		// if (list.get(position).getImgList().size() == 3){
		// vHolder.gv.setColumnWidth(new Tools().getScreenWidth(context) / 3);
		// vHolder.gv.setNumColumns(3);
		// }else if(list.get(position).getImgList().size() == 1){
		// android.view.ViewGroup.LayoutParams lp =
		// vHolder.gv.getLayoutParams();
		// lp.width = new Tools().getScreenWidth(context) / 2 - new
		// Tools().dip2px(context, 10);
		// vHolder.gv.setLayoutParams(lp);
		// vHolder.gv.setColumnWidth(new Tools().getScreenWidth(context) / 2);
		// vHolder.gv.setNumColumns(1);
		// }else if(list.get(position).getImgList().size() == 2){
		// vHolder.gv.setColumnWidth(new Tools().getScreenWidth(context) / 2);
		// vHolder.gv.setNumColumns(2);
		// }
		// mAdapter = new MainGrabGvInLvAdapter(context, list.get(position)
		// .getImgList(), this.parent);
		// vHolder.gv.setAdapter(mAdapter);
		return convertView;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2){
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	class ViewHolder{
		RoundImageView head;
		TextView name;
		TextView title;
		TextView time;
		TextView content;
		TextView price;
		TextView address;
		TextView distance;
		TextView hr;
		TextView todo;
		TextView method;
		LinearLayout label;
		ImageView sex;
		ImageView label1;
		ImageView label2;
		TextView identity;
		ImageView identityPic;
		LinearLayout addrLL;
		LinearLayout online;
		FrameLayout picFrame;
		ImageView pic;
		TextView picTotal;
	}

}
