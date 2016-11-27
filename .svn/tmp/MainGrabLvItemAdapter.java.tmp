/**
 * 
 * @param
 */
package adapter;

import ivzoom.ImagePagerActivity;

import java.util.ArrayList;

import model.ActivityModel;
import model.GrabOrderModel;
import popup.HintPopUpWindow;
import tools.Tools;
import view.ListViewForGrab;
import view.ListViewForMainGrab;
import view.RoundImageView;
import view.ScrollListView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.GetOtherInfoActivity;
import com.youge.jobfinder.activity.MainOrderDetailActivity;

/**
 * 
 * @param
 */
public class MainGrabLvItemAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<GrabOrderModel> list;
	private View parent;
	private LayoutInflater lInflater;

	private String arrow;
	protected TranslateAnimation mShowAction;
	protected TranslateAnimation mHiddenAction;
	// private ArrayList<ActivityModel> mList;
	private ActivityLVItemAdapter adapter;

	/**
	 * @param context
	 * @param list
	 */
	public MainGrabLvItemAdapter(Context context,
			ArrayList<GrabOrderModel> list, View parent) {
		super();
		this.context = context;
		this.list = list;
		this.parent = parent;
		this.lInflater = LayoutInflater.from(context);
		this.arrow = "down";

		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_PARENT,
				-1.0f, Animation.RELATIVE_TO_PARENT, 0.0f);
		mShowAction.setDuration(300);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f);
		mHiddenAction.setDuration(300);
		// mList = new ArrayList<ActivityModel>();

	}

	/**
	 * 
	 * @param
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.main_grab_order_list_item,
					null);
		}

		final ViewHolder vHolder = new ViewHolder();
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
		// vHolder.todo = (TextView) convertView
		// .findViewById(R.id.my_order_lv_item_todo);
		vHolder.label = (LinearLayout) convertView
				.findViewById(R.id.my_order_lv_item_label);
		vHolder.addrLL = (LinearLayout) convertView
				.findViewById(R.id.my_order_lv_item_addrll);
		// vHolder.sex = (ImageView) convertView
		// .findViewById(R.id.my_order_lv_item_sex);
		vHolder.label1 = (ImageView) convertView
				.findViewById(R.id.my_order_lv_item_label1);
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
		vHolder.bottom = (LinearLayout) convertView
				.findViewById(R.id.my_order_lv_item_bottomll);
		vHolder.activityLL = (LinearLayout) convertView
				.findViewById(R.id.my_order_lv_item_activityll);
		vHolder.activityTv = (TextView) convertView
				.findViewById(R.id.my_order_lv_item_activitytv);
		vHolder.activityIv = (ImageView) convertView
				.findViewById(R.id.my_order_lv_item_activityiv);
		vHolder.activityLv = (ListViewForGrab) convertView
				.findViewById(R.id.my_order_lv_item_activitylv);

		vHolder.event_one_tv = (TextView) convertView
				.findViewById(R.id.my_order_lv_item_event_text_one);
		vHolder.event_one_Iv = (ImageView) convertView
				.findViewById(R.id.my_order_lv_item_event_img_one);
		vHolder.event_one_LL = (LinearLayout) convertView
				.findViewById(R.id.my_order_lv_item_event_one);
		vHolder.event_two_tv = (TextView) convertView
				.findViewById(R.id.my_order_lv_item_event_text_two);
		vHolder.event_two_Iv = (ImageView) convertView
				.findViewById(R.id.my_order_lv_item_event_img_two);
		vHolder.event_two_LL = (LinearLayout) convertView
				.findViewById(R.id.my_order_lv_item_event_two);

		vHolder.my_order_event_item = (LinearLayout) convertView
				.findViewById(R.id.my_order_event_item);
		convertView.setTag(vHolder);
		ArrayList<ActivityModel> amList = list.get(position).getActivityList();

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.default_head)
				.showImageOnFail(R.drawable.default_head)
				.showImageOnLoading(R.drawable.default_head).build();

		DisplayImageOptions riv = new DisplayImageOptions.Builder().displayer(
				new RoundedBitmapDisplayer(5)).build();

		DisplayImageOptions overtime = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.overtime_two)
				.showImageOnFail(R.drawable.overtime_two)
				.showImageOnLoading(R.drawable.overtime_two).build();
		ImageLoader.getInstance().displayImage(
				list.get(position).getReleaseUserImg(), vHolder.head, options);
		vHolder.name.setText(list.get(position).getReleaseUserName());
		vHolder.title.setText(list.get(position).getTitle());
		vHolder.time.setText(list.get(position).getCreateDate());
		vHolder.content.setText(list.get(position).getSynopsis());
		vHolder.hr.setText(list.get(position).getStartDate());//
		vHolder.distance.setText(list.get(position).getDistance());
		if (list.get(position).getImgList().size() != 0) {
			vHolder.picFrame.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					list.get(position).getReleaseUserImg(), vHolder.head,
					options);
			vHolder.picTotal.setText("共"
					+ list.get(position).getImgList().size() + "张");
			ImageLoader.getInstance().displayImage(
					list.get(position).getImgList().get(0), vHolder.pic, riv);
			vHolder.pic.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					imageBrower(0, list.get(position).getImgList());
				}
			});
		} else {
			vHolder.picFrame.setVisibility(View.GONE);
		}
		if (list.get(position).getMethod().equals("0")) { // 线上
			vHolder.online.setVisibility(View.VISIBLE);
			vHolder.addrLL.setVisibility(View.GONE);
		} else {
			vHolder.online.setVisibility(View.GONE);
			vHolder.addrLL.setVisibility(View.VISIBLE);
			vHolder.address.setText(list.get(position).getAddress());
		}
		if (list.get(position).getCategory().equals("0")) { // 抢单
			vHolder.price.setText(list.get(position).getPrice() + "元");
		} else {
			vHolder.price.setText("竞价中");
		}

		// if (list.get(position).getSex().equals("1")){// 男
		// vHolder.sex.setImageResource(R.drawable.male);
		// }else{
		// vHolder.sex.setImageResource(R.drawable.famale);
		// }
		if (list.get(position).getLabel().equals("1")) {
			vHolder.label1.setImageResource(R.drawable.zhu);
		} else if (list.get(position).getLabel().equals("2")) {
			vHolder.label1.setImageResource(R.drawable.xiu);
		} else if (list.get(position).getLabel().equals("3")) {
			vHolder.label1.setImageResource(R.drawable.zhi);
		} else if (list.get(position).getLabel().equals("4")) {
			vHolder.label1.setImageResource(R.drawable.che);
		} else if (list.get(position).getLabel().equals("5")) {
			vHolder.label1.setImageResource(R.drawable.song);
		} else if (list.get(position).getLabel().equals("6")) {
			vHolder.label1.setImageResource(R.drawable.jia);
		}
		vHolder.label1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (list.get(position).getLabel().equals("1")) {
					new HintPopUpWindow(context, v, "1", (int) event.getRawX(),
							(int) event.getRawY(), true);
				} else if (list.get(position).getLabel().equals("2")) {
					new HintPopUpWindow(context, v, "2", (int) event.getRawX(),
							(int) event.getRawY(), true);
				} else if (list.get(position).getLabel().equals("3")) {
					new HintPopUpWindow(context, v, "3", (int) event.getRawX(),
							(int) event.getRawY(), true);
				} else if (list.get(position).getLabel().equals("4")) {
					new HintPopUpWindow(context, v, "4", (int) event.getRawX(),
							(int) event.getRawY(), true);
				} else if (list.get(position).getLabel().equals("5")) {
					new HintPopUpWindow(context, v, "5", (int) event.getRawX(),
							(int) event.getRawY(), true);
				} else if (list.get(position).getLabel().equals("6")) {
					new HintPopUpWindow(context, v, "9", (int) event.getRawX(),
							(int) event.getRawY(), true);
				}
				return true;
			}
		});

		if (list.get(position).getTimeout().equals("0")) {
			vHolder.activityTv.setText(amList.size() + "个活动");
		} else {
			vHolder.event_one_LL.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage("", vHolder.event_one_Iv,
					overtime);
			// vHolder.event_one_Iv.setImageResource(R.drawable.overtime_two);
			vHolder.event_one_tv.setText("超时赔付");
			vHolder.activityTv.setText(amList.size() + 1 + "个活动");
		}

		// 赔 0活动
		if (list.get(position).getTimeout().equals("1") && amList.size() == 0) {
			vHolder.bottom.setVisibility(View.VISIBLE);
			vHolder.event_two_LL.setVisibility(View.GONE);
		} else if (list.get(position).getTimeout().equals("1")// 赔 1活动
				&& amList.size() == 1) {
			vHolder.bottom.setVisibility(View.VISIBLE);
			vHolder.event_two_LL.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					amList.get(0).getActivityImg(), vHolder.event_two_Iv);
			vHolder.event_two_tv.setText(amList.get(0).getActivityTitle());
		} else if (list.get(position).getTimeout().equals("0")// 不赔 0活动
				&& amList.size() == 0) {
			vHolder.bottom.setVisibility(View.GONE);
		} else if (list.get(position).getTimeout().equals("0")// 不赔 1活动
				&& amList.size() == 1) {
			vHolder.bottom.setVisibility(View.VISIBLE);
			vHolder.event_two_LL.setVisibility(View.GONE);
			ImageLoader.getInstance().displayImage(
					amList.get(0).getActivityImg(), vHolder.event_one_Iv);
			vHolder.event_one_tv.setText(amList.get(0).getActivityTitle());
		} else if (list.get(position).getTimeout().equals("0")// 不赔 2活动
				&& amList.size() == 2) {
			vHolder.bottom.setVisibility(View.VISIBLE);
			// vHolder.event_one_LL.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					amList.get(0).getActivityImg(), vHolder.event_one_Iv);
			vHolder.event_one_tv.setText(amList.get(0).getActivityTitle());
			// vHolder.event_two_LL.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(
					amList.get(1).getActivityImg(), vHolder.event_two_Iv);
			vHolder.event_two_tv.setText(amList.get(1).getActivityTitle());
		} else if (list.get(position).getTimeout().equals("0")// 不赔 2以上活动
				&& amList.size() > 2) {
			vHolder.bottom.setVisibility(View.VISIBLE);

			openEvents(vHolder, "1", amList, position);

		} else if (list.get(position).getTimeout().equals("1")// 赔 1以上活动
				&& amList.size() > 1) {
			vHolder.bottom.setVisibility(View.VISIBLE);

			openEvents(vHolder, "2", amList, position);
		}

		// if ("超时赔付".equals(vHolder.event_one_tv.getText())) {
		// vHolder.event_one_Iv = (ImageView) convertView
		// .findViewById(R.id.my_order_lv_item_event_img_one);
		// vHolder.event_one_Iv.setBackgroundDrawable(context.getResources()
		// .getDrawable(R.drawable.overtime_two));
		// }

		if (list.get(position).getIdentity().equals("1")) {
			vHolder.identity.setTextColor(Color.rgb(34, 181, 112));
			vHolder.identityPic.setImageResource(R.drawable.renzheng);
			vHolder.identity.setText("已认证");
		} else {
			vHolder.identity.setText("未认证");
			vHolder.identity.setTextColor(Color.rgb(154, 154, 154));
			vHolder.identityPic.setImageResource(R.drawable.identity_grey);
		}

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("category ---> "
						+ list.get(position).getCategory());//
				if (new Tools().isUserLogin(context)) {
					Intent intent = new Intent(context,
							MainOrderDetailActivity.class);
					intent.putExtra("category", list.get(position)
							.getCategory());
					intent.putExtra("id", list.get(position).getId());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "您还没有登录哦!~", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		vHolder.head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (new Tools().isUserLogin(context)) {
					Intent intent = new Intent(context,
							GetOtherInfoActivity.class);
					intent.putExtra("otherUserId", list.get(position)
							.getReleaseUserId());
					context.startActivity(intent);
				} else {
					Toast.makeText(context, "您还没有登录哦!~", Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		// if ("超时赔付".equals(vHolder.event_one_tv.getText())) {
		// vHolder.event_one_Iv.setImageResource(R.drawable.overtime_two);
		// }

		// vHolder.todo.setOnClickListener(new OnClickListener(){
		//
		// @Override
		// public void onClick(View v){
		// // TODO Auto-generated method stub
		// System.out.println("category ---> "
		// + list.get(position).getCategory());//
		// if (new Tools().isUserLogin(context)){
		// Intent intent = new Intent(context,
		// MainOrderDetailActivity.class);
		// intent.putExtra("category", list.get(position)
		// .getCategory());
		// intent.putExtra("id", list.get(position).getId());
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startActivity(intent);
		// }else{
		// Toast.makeText(context, "您还没有登录哦!~", Toast.LENGTH_SHORT)
		// .show();
		// }
		// }
		// });
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
	 * 加载活动
	 */
	private void openEvents(final ViewHolder vHolder, String type,
			ArrayList<ActivityModel> amList, final int position) {
		// mList.clear();
		// mList.addAll(reList(type, amList));
		vHolder.my_order_event_item.removeAllViews();
		for (int i = 0, j = amList.size(); i < j; i++) {
			if ("1".equals(type)) {
				if (i == 0) {
					// vHolder.event_one_LL.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(
							amList.get(i).getActivityImg(),
							vHolder.event_one_Iv);
					vHolder.event_one_tv.setText(amList.get(0)
							.getActivityTitle());
				} else if (i == 1) {
					// vHolder.event_two_LL.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(
							amList.get(i).getActivityImg(),
							vHolder.event_two_Iv);
					vHolder.event_two_tv.setText(amList.get(1)
							.getActivityTitle());
				} else {
					LinearLayout ll = new LinearLayout(context);
					View view = lInflater.inflate(R.layout.activity_lv_item,
							null);
					ImageView iv = (ImageView) view
							.findViewById(R.id.activity_lv_item_iv);
					ImageLoader.getInstance().displayImage(
							amList.get(i).getActivityImg(), iv);
					TextView tv = (TextView) view
							.findViewById(R.id.activity_lv_item_tv);
					tv.setText(amList.get(i).getActivityTitle());
					android.view.ViewGroup.LayoutParams cameraLp = vHolder.event_one_LL
							.getLayoutParams();
					LayoutParams lp = new LayoutParams(cameraLp.width,
							cameraLp.height);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(lp);
					ll.addView(view);
					vHolder.my_order_event_item.addView(ll);
				}
			} else if ("2".equals(type)) {
				if (i == 0) {
					// vHolder.event_two_LL.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(
							amList.get(0).getActivityImg(),
							vHolder.event_two_Iv);
					vHolder.event_two_tv.setText(amList.get(0)
							.getActivityTitle());
				} else {
					LinearLayout ll = new LinearLayout(context);
					View view = lInflater.inflate(R.layout.activity_lv_item,
							null);
					ImageView iv = (ImageView) view
							.findViewById(R.id.activity_lv_item_iv);
					ImageLoader.getInstance().displayImage(
							amList.get(i).getActivityImg(), iv);
					TextView tv = (TextView) view
							.findViewById(R.id.activity_lv_item_tv);
					tv.setText(amList.get(i).getActivityTitle());
					android.view.ViewGroup.LayoutParams cameraLp = vHolder.event_one_LL
							.getLayoutParams();
					LayoutParams lp = new LayoutParams(cameraLp.width,
							cameraLp.height);
					ll.setOrientation(LinearLayout.HORIZONTAL);
					ll.setLayoutParams(lp);
					ll.addView(view);
					vHolder.my_order_event_item.addView(ll);
				}
			}
		}
		vHolder.activityLL.setVisibility(View.VISIBLE);
		if (list.get(position).isShow() == false) {
			vHolder.my_order_event_item.setVisibility(View.GONE);
			// vHolder.activityLv.setVisibility(View.VISIBLE);
			vHolder.activityIv.setImageResource(R.drawable.arrow_down);
		} else {
			vHolder.my_order_event_item.setVisibility(View.VISIBLE);
			vHolder.activityIv.setImageResource(R.drawable.arrow_up);
		}

		vHolder.bottom.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (arrow.equals("down")) {
					arrow = "up";
					list.get(position).setShow(true);
					notifyDataSetChanged();
					// vHolder.activityIv.setImageResource(R.drawable.arrow_up);
					// // ((LinearLayout.LayoutParams) vHolder.activityLv
					// // .getLayoutParams()).bottomMargin = 0;
					// vHolder.my_order_event_item.setVisibility(View.VISIBLE);
				} else {
					arrow = "down";
					list.get(position).setShow(false);
					notifyDataSetChanged();
					// vHolder.activityIv.setImageResource(R.drawable.arrow_down);
					// vHolder.my_order_event_item.setVisibility(View.GONE);
					// ((LinearLayout.LayoutParams) vHolder.activityLv
					// .getLayoutParams()).bottomMargin = (-1)
					// * vHolder.activityLv.getMeasuredHeight();
				}
			}
		});

		// vHolder.my_order_event_item.addView(lp);
	}

	/**
	 * 加载活动
	 */
	private void openEvent(final ViewHolder vHolder, String type,
			ArrayList<ActivityModel> amList, final int position) {

		if (list.get(position).getTimeout().equals("0")) {
			vHolder.activityTv.setText(amList.size() + "个活动");
		} else {
			vHolder.activityTv.setText(amList.size() + 1 + "个活动");
		}
		amList.clear();
		amList.addAll(reList(type, amList));
		adapter = new ActivityLVItemAdapter(context, amList);
		vHolder.activityLv.setAdapter(adapter);
		ScrollListView.setListViewHeightBasedOnChildren(vHolder.activityLv);
		// vHolder.bottom.setVisibility(View.VISIBLE);
		vHolder.activityLL.setVisibility(View.VISIBLE);
		if (list.get(position).isShow() == false) {
			vHolder.activityLv.setVisibility(View.GONE);
			// vHolder.activityLv.setVisibility(View.VISIBLE);
			vHolder.activityIv.setImageResource(R.drawable.arrow_down);
		} else {
			vHolder.activityLv.setVisibility(View.VISIBLE);
			vHolder.activityIv.setImageResource(R.drawable.arrow_up);
		}
		vHolder.activityLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (arrow.equals("down")) {
					arrow = "up";
					vHolder.activityIv.setImageResource(R.drawable.arrow_up);
					((LinearLayout.LayoutParams) vHolder.activityLv
							.getLayoutParams()).bottomMargin = 0;
					vHolder.activityLv.setVisibility(View.VISIBLE);
				} else {
					arrow = "down";
					vHolder.activityIv.setImageResource(R.drawable.arrow_down);
					vHolder.activityLv.setVisibility(View.GONE);
					((LinearLayout.LayoutParams) vHolder.activityLv
							.getLayoutParams()).bottomMargin = (-1)
							* vHolder.activityLv.getMeasuredHeight();
				}
			}
		});

	}

	/**
	 * 重填list
	 */
	private ArrayList<ActivityModel> reList(String type,
			ArrayList<ActivityModel> amList) {
		ArrayList<ActivityModel> list = new ArrayList<ActivityModel>();
		list.addAll(amList);
		if ("1".equals(type)) {
			list.remove(1);
			list.remove(0);
		} else {
			list.remove(0);
		}
		return list;
	}

	/**
	 * 打开图片查看器
	 * 
	 * @param position
	 * @param urls2
	 */
	protected void imageBrower(int position, ArrayList<String> urls2) {
		Intent intent = new Intent(context, ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls2);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		context.startActivity(intent);
	}

	class ViewHolder {
		RoundImageView head;
		TextView name;
		TextView title;
		TextView time;
		TextView content;
		TextView price;
		TextView address;
		TextView distance;
		TextView hr;
		// TextView todo;
		TextView method;
		LinearLayout label;
		// ImageView sex;
		ImageView label1;
		TextView identity;
		ImageView identityPic;
		LinearLayout addrLL;
		LinearLayout online;
		FrameLayout picFrame;
		ImageView pic;
		TextView picTotal;
		LinearLayout bottom;
		LinearLayout activityLL;
		TextView activityTv;
		ImageView activityIv;

		TextView event_one_tv;
		ImageView event_one_Iv;
		LinearLayout event_one_LL;

		TextView event_two_tv;
		ImageView event_two_Iv;
		LinearLayout event_two_LL;

		ListViewForGrab activityLv;

		LinearLayout my_order_event_item;
	}

	public void updataView(ListViewForMainGrab listView) {
		View view = listView.getChildAt(0);
		ViewHolder holder = (ViewHolder) view.getTag();
		if ("超时赔付".equals(holder.event_one_tv.getText())) {
			holder.event_one_Iv.setImageResource(R.drawable.overtime_two);
		}
	}

}
