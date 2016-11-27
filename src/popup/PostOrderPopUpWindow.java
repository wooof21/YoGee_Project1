/**
 * 
 * @param
 */
package popup;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.FillOrderMaintActivity;

/**
 * 
 * @param
 */
public class PostOrderPopUpWindow extends PopupWindow {

	private Context context;
	private String uid, lat, lon;

	public PostOrderPopUpWindow(final Context context, View parent, String uid,
			final String lat, final String lon) {
		View view = View.inflate(context, R.layout.popup_post_order, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.post_in));

		this.context = context;
		this.uid = uid;
		this.lat = lat;
		this.lon = lon;

		LinearLayout grab_order = (LinearLayout) view
				.findViewById(R.id.fragment_main_grab_order);
		grab_order.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.post_in));
		LinearLayout post_order = (LinearLayout) view
				.findViewById(R.id.fragment_main_post_order);
		post_order.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.post_in_2));

		ImageView grab = (ImageView) view.findViewById(R.id.grab);
		ImageView post = (ImageView) view.findViewById(R.id.post);
		ImageView post_popup_back = (ImageView) view
				.findViewById(R.id.post_popup_back);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setAnimationStyle(R.style.PostPopupAnimation);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		grab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						FillOrderMaintActivity.class);
				intent.putExtra("category", "0");
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				context.startActivity(intent);
				dismiss();
			}
		});

		post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,
						FillOrderMaintActivity.class);
				intent.putExtra("category", "1");
				intent.putExtra("lat", lat);
				intent.putExtra("lon", lon);
				context.startActivity(intent);
				dismiss();
			}
		});

		post_popup_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			dismiss();
		}
		return false;
	}

}
