/**
 * 
 * @param
 */
package popup;

import com.alipay.android.phone.mrpc.core.w;
import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.FillOrderMaintActivity;
import com.youge.jobfinder.activity.MainOrderDetailActivity;
import com.youge.jobfinder.activity.OrderListDetailActivity;
import com.youge.jobfinder.vip.ChangeUserResumeActivity;

import fragment.MainGrabFragment;

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
public class OrderPopUpWindow extends PopupWindow {

	public OrderPopUpWindow(Context context, View parent, String content,
			final String what) {
		View view = View.inflate(context, R.layout.popup_order, null);
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

		dismiss.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});

		commit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("1".equals(what)) {
					Message msg1 = FillOrderMaintActivity.instance.mHandler
							.obtainMessage();
					msg1.what = 10;
					FillOrderMaintActivity.instance.mHandler.sendMessage(msg1);
				}else if("2".equals(what)){
					Message msg2 = FillOrderMaintActivity.instance.mHandler
							.obtainMessage();
					msg2.what = 11;
					FillOrderMaintActivity.instance.mHandler.sendMessage(msg2);
				}else if("3".equals(what)){
					Message msg3 = ChangeUserResumeActivity.instance.mHandler
							.obtainMessage();
					msg3.what = 12;
					ChangeUserResumeActivity.instance.mHandler.sendMessage(msg3);
				}
			}
		});
	}
}
