/**
 * 
 * @param
 */
package popup;

import tools.Tools;

import com.youge.jobfinder.MainActivity;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.GetOtherInfoActivity;
import com.youge.jobfinder.activity.MainOrderDetailActivity;
import com.youge.jobfinder.activity.OrderDetailActivity;
import com.youge.jobfinder.activity.OrderListDetailActivity;

import fragment.OrderDetailToGrabFragment;

import adapter.OrderStatusDetailLVAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextClock;
import android.widget.TextView;

/**
 * 
 * @param
 */
public class PushPopUpWindow extends PopupWindow{

	private Message msg;

	public PushPopUpWindow(final Context context, View parent, String content,
			final String notificationType, String className){
		View view = View.inflate(context, R.layout.popup_content_push, null);
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
		System.out.println("context name ---> "
				+ context.getClass().getSimpleName());
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
				dismiss();
				if ("1".equals(notificationType)){
					if (new Tools().getWhere(context).equals("g")){
						if (new Tools().getOrderState(context).equals("1")
								|| new Tools().getOrderState(context).equals(
										"2")){
							Intent intent = new Intent(context,
									MainOrderDetailActivity.class);
							intent.putExtra("category",
									new Tools().getCatefory(context));
							intent.putExtra("id", new Tools().getOid(context));
							if(context.getClass().getSimpleName()
									.equals("MainOrderDetailActivity")){
								Message msg = MainOrderDetailActivity.instance.handler.obtainMessage();
								msg.what = 300;
								msg.sendToTarget();
							}
							context.startActivity(intent);
						}else{
							Intent intent = new Intent(context,
									OrderListDetailActivity.class);
							intent.putExtra("where",
									new Tools().getWhere(context));
							intent.putExtra("category",
									new Tools().getCatefory(context));
							intent.putExtra("orderState",
									new Tools().getOrderState(context));
							intent.putExtra("oid", new Tools().getOid(context));
							if (context.getClass().getSimpleName()
									.equals("OrderListDetailActivity")){
								Message msg = OrderListDetailActivity.instance.handler
										.obtainMessage();
								msg.what = 300;
								msg.sendToTarget();
							}
							context.startActivity(intent);
						}
					}else{
						Intent intent = new Intent(context,
								OrderListDetailActivity.class);
						intent.putExtra("where",
								new Tools().getWhere(context));
						intent.putExtra("category",
								new Tools().getCatefory(context));
						intent.putExtra("orderState",
								new Tools().getOrderState(context));
						intent.putExtra("oid", new Tools().getOid(context));
						if (context.getClass().getSimpleName()
								.equals("OrderListDetailActivity")){
							Message msg = OrderListDetailActivity.instance.handler
									.obtainMessage();
							msg.what = 300;
							msg.sendToTarget();
						}
						context.startActivity(intent);
					}
					// Intent intent = new Intent(context, MainActivity.class);
					// intent.putExtra("notificationType", notificationType);
					// context.startActivity(intent);
				}else if ("2".equals(notificationType)){
					Intent intent = new Intent(context,
							GetOtherInfoActivity.class);
					intent.putExtra("otherUserId",
							new Tools().getNotificationId(context));
					context.startActivity(intent);
				}
			}
		});

	}
}
