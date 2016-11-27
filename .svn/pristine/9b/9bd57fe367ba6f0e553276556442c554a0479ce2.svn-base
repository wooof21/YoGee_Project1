/**
 * 
 * @param
 */
package popup;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.MainOrderDetailActivity;
import com.youge.jobfinder.activity.OrderListDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class ContentPopUpWindow extends PopupWindow{

	private Message msg;
	private String type;

	public ContentPopUpWindow(Context context, View parent, String content,
			final String what){
		View view = View.inflate(context, R.layout.popup_content, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView title = (TextView) view.findViewById(R.id.popup_content);
		TextView dismiss = (TextView) view
				.findViewById(R.id.popup_content_dismiss);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_content_commit);
		title.setText(content);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		dismiss.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (what.equals("grab")){
					msg = MainOrderDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 200;
				}else if (what.equals("bidcancel")){
					msg = MainOrderDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 100;
				}else if (what.equals("refund")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 100;
				}else if (what.equals("bidcancellist")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 1001;
				}else if (what.equals("finishOrder")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 500;
				}else if (what.equals("notify_leaving")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 5000;
				}else if (what.equals("notify_onroad")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 5001;
				}else if (what.equals("notify_finish")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 5002;
				}else if (what.equals("notify_confirm")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 4999;
				}else if (what.equals("notify_cancel")){
					msg = OrderListDetailActivity.instance.handler
							.obtainMessage();
					msg.what = 4998;
				}
				msg.sendToTarget();
				dismiss();
			}
		});

	}

	public ContentPopUpWindow(Context context, View parent, String content,
			final String what, String point, final Handler handler){
		View view = View.inflate(context, R.layout.popup_content, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView title = (TextView) view.findViewById(R.id.popup_content);
		TextView dismiss = (TextView) view
				.findViewById(R.id.popup_content_dismiss);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_content_commit);
		title.setText(Html
				.fromHtml("</font><font color=\"black\">此次兑换将使用</font>"
						+ "<font color=\"red\">" + point
						+ "</font><font color=\"black\">积分, 确定兑换么?</font>"));

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		dismiss.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Message msg = handler.obtainMessage();
				msg.what = 2;
				msg.sendToTarget();
				dismiss();
			}
		});
	}

	public ContentPopUpWindow(final Context context, View parent,
			String content, final String bPhone, final String sPhone,
			String what){
		View view = View.inflate(context, R.layout.popup_content2, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		final TextView title = (TextView) view
				.findViewById(R.id.popup_content2);
		final TextView contactb = (TextView) view
				.findViewById(R.id.popup_content_contactb);
		final TextView contacts = (TextView) view
				.findViewById(R.id.popup_content_contancs);

		title.setText(content);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();
		if (what.equals("refund")){
			type = "refund";
			contactb.setText("取消");
		}else{
			type = "null";
		}

		contactb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (type.equals("null")){
					title.setText(bPhone);
					contactb.setText("取消");
					contacts.setText("呼叫");
					type = "b";
				}else{
					dismiss();
				}
			}
		});

		contacts.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				if (type.equals("null")){
					title.setText(sPhone);
					contactb.setText("取消");
					contacts.setText("呼叫");
					type = "s";
				}else if (type.equals("b")){
					context.startActivity(new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + bPhone)));
					dismiss();
				}else if (type.equals("s") || type.equals("refund")){
					context.startActivity(new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + sPhone)));
					dismiss();
				}
			}
		});
	}

	public ContentPopUpWindow(Context context, View parent,
			final String status, final Handler handler){
		View view = View.inflate(context, R.layout.popup_content2, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		final TextView title = (TextView) view
				.findViewById(R.id.popup_content2);
		final TextView contactb = (TextView) view
				.findViewById(R.id.popup_content_contactb);
		final TextView contacts = (TextView) view
				.findViewById(R.id.popup_content_contancs);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		title.setText("即将开启百度地图导航" + "\n"
				+ "使用过程中可能产生的流量费用, 与本公司无关." + "\n" + "请确定是否开启百度导航.");

		contactb.setText("取消");
		contacts.setText("确定");
		contactb.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				dismiss();
			}
		});
		contacts.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				Message msg = handler.obtainMessage();
				if (status.equals("drive")){
					msg.what = 1;
				}else if (status.equals("walk")){
					msg.what = 2;
				}else{
					msg.what = 3;
				}
				msg.sendToTarget();
				dismiss();
			}
		});

	}
}
