package popup;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.LableGradActivity;

public class LabelXsPopUpWindow extends PopupWindow implements OnClickListener {

	private String method, orderType, timeout, onlinePay, distance; // 筛选方式默认值
	private String XS, labels;
	private JSONObject job;
	private TextView online, offline, grab, price, cancel, commit, xs_all,
			xs_type_all, xs_distance_three, xs_distance_five, xs_distance_ten;
	private ToggleButton overtime, pay;
	private ImageView che, song, zhu, xiu, zhi, jia;
	private CheckBox cb;

	private String state1, state2, state3, state4, state5, state6;

	public LabelXsPopUpWindow(Context context, View parent, String method,
			String orderType, String timeout, String onlinePay, String distance) {
		View view = View.inflate(context, R.layout.popup_shaixuanfangshi, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.method = method;
		this.orderType = orderType;
		this.timeout = timeout;
		this.onlinePay = onlinePay;
		this.distance = distance;

		labels = "0";
		state1 = state2 = state3 = state4 = state5 = state6 = "0";

		xs_all = (TextView) view.findViewById(R.id.xs_all);
		online = (TextView) view.findViewById(R.id.xs_online);
		offline = (TextView) view.findViewById(R.id.xs_offline);
		xs_type_all = (TextView) view.findViewById(R.id.xs_type_all);
		grab = (TextView) view.findViewById(R.id.xs_type_grab);
		price = (TextView) view.findViewById(R.id.xs_type_price);
		xs_distance_three = (TextView) view
				.findViewById(R.id.xs_distance_three);
		xs_distance_five = (TextView) view.findViewById(R.id.xs_distance_five);
		xs_distance_ten = (TextView) view.findViewById(R.id.xs_distance_ten);
		overtime = (ToggleButton) view.findViewById(R.id.xs_overtime);
		pay = (ToggleButton) view.findViewById(R.id.xs_online_pay);
		cancel = (TextView) view.findViewById(R.id.xs_cancel);
		commit = (TextView) view.findViewById(R.id.xs_commit);
		cb = (CheckBox) view.findViewById(R.id.xs_label_cb);
		song = (ImageView) view.findViewById(R.id.xs_song);
		che = (ImageView) view.findViewById(R.id.xs_che);
		xiu = (ImageView) view.findViewById(R.id.xs_xiu);
		zhi = (ImageView) view.findViewById(R.id.xs_zhi);
		zhu = (ImageView) view.findViewById(R.id.xs_zhu);
		jia = (ImageView) view.findViewById(R.id.xs_jia);

		setDefaultBg();// 开始设置4个选项的默认值,或上次选择的值

		xs_all.setOnClickListener(this);
		online.setOnClickListener(this);
		offline.setOnClickListener(this);
		xs_type_all.setOnClickListener(this);
		grab.setOnClickListener(this);
		price.setOnClickListener(this);
		cancel.setOnClickListener(this);
		commit.setOnClickListener(this);
		xs_distance_three.setOnClickListener(this);
		xs_distance_five.setOnClickListener(this);
		xs_distance_ten.setOnClickListener(this);

		overtime.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) { // 选择超时赔付
					LabelXsPopUpWindow.this.timeout = "1";
				} else {
					LabelXsPopUpWindow.this.timeout = "0";
				}
			}
		});
		pay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) { // 选择线上支付
					LabelXsPopUpWindow.this.onlinePay = "1";
				} else {
					LabelXsPopUpWindow.this.onlinePay = "0";
				}
			}
		});

		cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					select(true);
					labels = "1,2,3,4,5";
				} else {
					select(false);
					labels = "0";
				}
			}
		});

		zhu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb.setChecked(false);
				if (state1.equals("0")) {
					zhu.setImageResource(R.drawable.zhu);
					state1 = "1";
				} else {
					zhu.setImageResource(R.drawable.zhu_grey);
					state1 = "0";
				}
			}
		});
		xiu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb.setChecked(false);
				if (state2.equals("0")) {
					xiu.setImageResource(R.drawable.xiu);
					state2 = "1";
				} else {
					xiu.setImageResource(R.drawable.xiu_grey);
					state2 = "0";
				}
			}
		});
		zhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb.setChecked(false);
				if (state3.equals("0")) {
					zhi.setImageResource(R.drawable.zhi);
					state3 = "1";
				} else {
					zhi.setImageResource(R.drawable.zhi_grey);
					state3 = "0";
				}
			}
		});
		che.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb.setChecked(false);
				if (state4.equals("0")) {
					che.setImageResource(R.drawable.che);
					state4 = "1";
				} else {
					che.setImageResource(R.drawable.che_grey);
					state4 = "0";
				}
			}
		});
		song.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb.setChecked(false);
				if (state5.equals("0")) {
					song.setImageResource(R.drawable.song);
					state5 = "1";
				} else {
					song.setImageResource(R.drawable.song_grey);
					state5 = "0";
				}
			}
		});

		jia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				cb.setChecked(false);
				if (state6.equals("0")) {
					jia.setImageResource(R.drawable.jia);
					state6 = "1";
				} else {
					jia.setImageResource(R.drawable.jia_grey);
					state6 = "0";
				}
			}
		});

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		// setBackgroundDrawable(new ColorDrawable(R.color.transparents));
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		// showAtLocation(parent, Gravity.BOTTOM, 0, 80);
		showAsDropDown(parent);
		update();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.xs_all:
			setOnOffBg(2);
			break;
		case R.id.xs_online:
			setOnOffBg(0);
			break;
		case R.id.xs_offline:
			setOnOffBg(1);
			break;
		case R.id.xs_type_all:
			setOrderTypeBg(2);
			break;
		case R.id.xs_type_grab:
			setOrderTypeBg(0);
			break;
		case R.id.xs_type_price:
			setOrderTypeBg(1);
			break;
		case R.id.xs_distance_three:
			setDistance(3);
			break;
		case R.id.xs_distance_five:
			setDistance(5);
			break;
		case R.id.xs_distance_ten:
			setDistance(10);
			break;
		case R.id.xs_cancel:
			dismiss();
			Message msg = LableGradActivity.instance.handler.obtainMessage();
			msg.what = 0;
			LableGradActivity.instance.handler.sendMessage(msg);
			break;
		case R.id.xs_commit:
			sendMessage();
			dismiss();
			break;
		default:
			break;
		}
	}

	private void select(boolean yes) {
		if (yes) {
			zhu.setImageResource(R.drawable.zhu);
			xiu.setImageResource(R.drawable.xiu);
			zhi.setImageResource(R.drawable.zhi);
			che.setImageResource(R.drawable.che);
			song.setImageResource(R.drawable.song);
			jia.setImageResource(R.drawable.jia);
			state1 = state2 = state3 = state4 = state5 = state6 = "1";
		} else {
			zhu.setImageResource(R.drawable.zhu_grey);
			xiu.setImageResource(R.drawable.xiu_grey);
			zhi.setImageResource(R.drawable.zhi_grey);
			che.setImageResource(R.drawable.che_grey);
			song.setImageResource(R.drawable.song_grey);
			jia.setImageResource(R.drawable.jia_grey);
			state1 = state2 = state3 = state4 = state5 = state6 = "0";
		}
	}

	private String setLabels() {
		String label = "";
		if (state1.equals("1")) {
			label += "1,";
		}
		if (state2.equals("1")) {
			label += "2,";
		}
		if (state3.equals("1")) {
			label += "3,";
		}
		if (state4.equals("1")) {
			label += "4,";
		}
		if (state5.equals("1")) {
			label += "5,";
		}
		if (state6.equals("1")) {
			label += "6,";
		}
		if (state1.equals("0") && state2.equals("0") && state3.equals("0")
				&& state4.equals("0") && state5.equals("0")
				&& state6.equals("0")) {
			label = "0";
		}
		System.out.println("label ---> " + label);
		if (label.equals("0")) {
			return label;
		} else {
			return label.substring(0, label.length() - 1);
		}
	}

	/**
	 * 点击确认时, 发送消息
	 * 
	 * @return
	 */
	private void sendMessage() {
		Message msg1 = LableGradActivity.instance.handler.obtainMessage();
		msg1.what = 2;
		msg1.obj = method;
		LableGradActivity.instance.handler.sendMessage(msg1);

		Message msg2 = LableGradActivity.instance.handler.obtainMessage();
		msg2.what = 3;
		msg2.obj = orderType;
		LableGradActivity.instance.handler.sendMessage(msg2);

		Message msg3 = LableGradActivity.instance.handler.obtainMessage();
		msg3.what = 4;
		msg3.obj = timeout;
		LableGradActivity.instance.handler.sendMessage(msg3);

		Message msg4 = LableGradActivity.instance.handler.obtainMessage();
		msg4.what = 5;
		msg4.obj = onlinePay;
		LableGradActivity.instance.handler.sendMessage(msg4);

		Message msg5 = LableGradActivity.instance.handler.obtainMessage();
		msg5.what = 6;
		msg5.obj = distance;
		msg5.sendToTarget();

		Message msg6 = LableGradActivity.instance.handler.obtainMessage();
		msg6.what = 7;
		msg6.obj = setLabels();
		msg6.sendToTarget();

	}

	/**
	 * 设置各选项的默认值, 保存上次值
	 */
	private void setDefaultBg() {
		if (method.equals("2")) {
			xs_all.setBackgroundResource(R.drawable.square_green_one);
			xs_all.setTextColor(Color.rgb(255, 255, 255));
			online.setBackgroundResource(R.drawable.square_green_center_two);
			online.setTextColor(Color.rgb(34, 181, 112));
			offline.setBackgroundResource(R.drawable.square_green_stroke_two);
			offline.setTextColor(Color.rgb(34, 181, 112));
		} else if (method.equals("0")) {
			xs_all.setBackgroundResource(R.drawable.square_green_two);
			xs_all.setTextColor(Color.rgb(34, 181, 112));
			online.setBackgroundResource(R.drawable.square_green_center_one);
			online.setTextColor(Color.rgb(255, 255, 255));
			offline.setBackgroundResource(R.drawable.square_green_stroke_two);
			offline.setTextColor(Color.rgb(34, 181, 112));
		} else {
			xs_all.setBackgroundResource(R.drawable.square_green_two);
			xs_all.setTextColor(Color.rgb(34, 181, 112));
			online.setBackgroundResource(R.drawable.square_green_center_two);
			online.setTextColor(Color.rgb(34, 181, 112));
			offline.setBackgroundResource(R.drawable.square_green_stroke_one);
			offline.setTextColor(Color.rgb(255, 255, 255));

		}

		if (orderType.equals("2")) {
			xs_type_all.setBackgroundResource(R.drawable.square_green_one);
			xs_type_all.setTextColor(Color.rgb(255, 255, 255));
			grab.setBackgroundResource(R.drawable.square_green_center_two);
			grab.setTextColor(Color.rgb(34, 181, 112));
			price.setBackgroundResource(R.drawable.square_green_stroke_two);
			price.setTextColor(Color.rgb(34, 181, 112));
		} else if (orderType.equals("0")) {
			xs_type_all.setBackgroundResource(R.drawable.square_green_two);
			xs_type_all.setTextColor(Color.rgb(34, 181, 112));
			grab.setBackgroundResource(R.drawable.square_green_center_one);
			grab.setTextColor(Color.rgb(255, 255, 255));
			price.setBackgroundResource(R.drawable.square_green_stroke_two);
			price.setTextColor(Color.rgb(34, 181, 112));
		} else {
			xs_type_all.setBackgroundResource(R.drawable.square_green_two);
			xs_type_all.setTextColor(Color.rgb(34, 181, 112));
			grab.setBackgroundResource(R.drawable.square_green_center_two);
			grab.setTextColor(Color.rgb(34, 181, 112));
			price.setBackgroundResource(R.drawable.square_green_stroke_one);
			price.setTextColor(Color.rgb(255, 255, 255));
		}

		if (distance.equals("3")) {
			xs_distance_three
					.setBackgroundResource(R.drawable.square_green_one);
			xs_distance_three.setTextColor(Color.rgb(255, 255, 255));
			xs_distance_five
					.setBackgroundResource(R.drawable.square_green_center_two);
			xs_distance_five.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_ten
					.setBackgroundResource(R.drawable.square_green_stroke_two);
			xs_distance_ten.setTextColor(Color.rgb(34, 181, 112));
		} else if (distance.equals("5")) {
			xs_distance_three
					.setBackgroundResource(R.drawable.square_green_two);
			xs_distance_three.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_five
					.setBackgroundResource(R.drawable.square_green_center_one);
			xs_distance_five.setTextColor(Color.rgb(255, 255, 255));
			xs_distance_ten
					.setBackgroundResource(R.drawable.square_green_stroke_two);
			xs_distance_ten.setTextColor(Color.rgb(34, 181, 112));
		} else {
			xs_distance_three
					.setBackgroundResource(R.drawable.square_green_two);
			xs_distance_three.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_five
					.setBackgroundResource(R.drawable.square_green_center_two);
			xs_distance_five.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_ten
					.setBackgroundResource(R.drawable.square_green_stroke_one);
			xs_distance_ten.setTextColor(Color.rgb(255, 255, 255));
		}

		if (timeout.equals("0")) {
			overtime.setChecked(false);
		} else {
			overtime.setChecked(true);
		}

		if (onlinePay.equals("0")) {
			pay.setChecked(false);
		} else {
			pay.setChecked(true);
		}
	}

	private void setOnOffBg(int type) {
		if (type == 2) { // 线上
			xs_all.setBackgroundResource(R.drawable.square_green_one);
			xs_all.setTextColor(Color.rgb(255, 255, 255));
			online.setBackgroundResource(R.drawable.square_green_center_two);
			online.setTextColor(Color.rgb(34, 181, 112));
			offline.setBackgroundResource(R.drawable.square_green_stroke_two);
			offline.setTextColor(Color.rgb(34, 181, 112));
			method = "2";
		} else if (type == 0) {
			xs_all.setBackgroundResource(R.drawable.square_green_two);
			xs_all.setTextColor(Color.rgb(34, 181, 112));
			online.setBackgroundResource(R.drawable.square_green_center_one);
			online.setTextColor(Color.rgb(255, 255, 255));
			offline.setBackgroundResource(R.drawable.square_green_stroke_two);
			offline.setTextColor(Color.rgb(34, 181, 112));
			method = "0";
		} else {
			xs_all.setBackgroundResource(R.drawable.square_green_two);
			xs_all.setTextColor(Color.rgb(34, 181, 112));
			online.setBackgroundResource(R.drawable.square_green_center_two);
			online.setTextColor(Color.rgb(34, 181, 112));
			offline.setBackgroundResource(R.drawable.square_green_stroke_one);
			offline.setTextColor(Color.rgb(255, 255, 255));
			method = "1";
		}
	}

	private void setOrderTypeBg(int type) {
		if (type == 2) {
			xs_type_all.setBackgroundResource(R.drawable.square_green_one);
			xs_type_all.setTextColor(Color.rgb(255, 255, 255));
			grab.setBackgroundResource(R.drawable.square_green_center_two);
			grab.setTextColor(Color.rgb(34, 181, 112));
			price.setBackgroundResource(R.drawable.square_green_stroke_two);
			price.setTextColor(Color.rgb(34, 181, 112));
			orderType = "2";
		} else if (type == 0) {
			xs_type_all.setBackgroundResource(R.drawable.square_green_two);
			xs_type_all.setTextColor(Color.rgb(34, 181, 112));
			grab.setBackgroundResource(R.drawable.square_green_center_one);
			grab.setTextColor(Color.rgb(255, 255, 255));
			price.setBackgroundResource(R.drawable.square_green_stroke_two);
			price.setTextColor(Color.rgb(34, 181, 112));
			orderType = "0";
		} else {
			xs_type_all.setBackgroundResource(R.drawable.square_green_two);
			xs_type_all.setTextColor(Color.rgb(34, 181, 112));
			grab.setBackgroundResource(R.drawable.square_green_center_two);
			grab.setTextColor(Color.rgb(34, 181, 112));
			price.setBackgroundResource(R.drawable.square_green_stroke_one);
			price.setTextColor(Color.rgb(255, 255, 255));
			orderType = "1";
		}
	}

	private void setDistance(int type) {
		if (type == 3) {
			xs_distance_three
					.setBackgroundResource(R.drawable.square_green_one);
			xs_distance_three.setTextColor(Color.rgb(255, 255, 255));
			xs_distance_five
					.setBackgroundResource(R.drawable.square_green_center_two);
			xs_distance_five.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_ten
					.setBackgroundResource(R.drawable.square_green_stroke_two);
			xs_distance_ten.setTextColor(Color.rgb(34, 181, 112));
			distance = "3";
		} else if (type == 5) {
			xs_distance_three
					.setBackgroundResource(R.drawable.square_green_two);
			xs_distance_three.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_five
					.setBackgroundResource(R.drawable.square_green_center_one);
			xs_distance_five.setTextColor(Color.rgb(255, 255, 255));
			xs_distance_ten
					.setBackgroundResource(R.drawable.square_green_stroke_two);
			xs_distance_ten.setTextColor(Color.rgb(34, 181, 112));
			distance = "5";
		} else {
			xs_distance_three
					.setBackgroundResource(R.drawable.square_green_two);
			xs_distance_three.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_five
					.setBackgroundResource(R.drawable.square_green_center_two);
			xs_distance_five.setTextColor(Color.rgb(34, 181, 112));
			xs_distance_ten
					.setBackgroundResource(R.drawable.square_green_stroke_one);
			xs_distance_ten.setTextColor(Color.rgb(255, 255, 255));
			distance = "10";
		}
	}
}
