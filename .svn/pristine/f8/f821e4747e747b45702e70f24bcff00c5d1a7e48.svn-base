/**
 * 
 * @param
 */
package popup;

import login.Login;

import com.youge.jobfinder.MainActivity;
import com.youge.jobfinder.R;

import android.content.Context;
import android.content.Intent;
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
public class LoginPopUpwindow extends PopupWindow {

	public LoginPopUpwindow(final Context context, View parent, final String from) {
		View view = View.inflate(context, R.layout.popup_login, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView dismiss = (TextView) view
				.findViewById(R.id.login_popup_dismiss);
		TextView login = (TextView) view.findViewById(R.id.login_popup_login);

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

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(from.equals("main")){
					dismiss();
					((MainActivity) context).startActivityForResult(new Intent(context, Login.class), 999);
				}
			}
		});

	}
}
