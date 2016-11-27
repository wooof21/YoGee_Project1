/**
 * 
 * @param
 */
package popup;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class StartImgPopUpWindow extends PopupWindow {

	private Context context;
	private String img;

	public StartImgPopUpWindow(final Context context, View parent, String img) {
		View view = View.inflate(context, R.layout.popup_startimg_order, null);

		this.context = context;
		this.img = img;
		ImageView imgView = (ImageView) view.findViewById(R.id.img);
		TextView backTextView = (TextView) view.findViewById(R.id.back);

		ImageLoader.getInstance().displayImage(img, imgView);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setAnimationStyle(R.style.PostPopupAnimation);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Message msg = handler.obtainMessage();
				msg.what = 1;
				msg.sendToTarget();
			}
		};
		timer.schedule(task, 3000); // 停留3秒钟.
		backTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sharedPre = context.getSharedPreferences(
						"user", Context.MODE_PRIVATE);
				Editor editor = sharedPre.edit();
				editor.putBoolean("isFromSplash", false);
				editor.commit();
				dismiss();
			}
		});

	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				SharedPreferences sharedPre = context.getSharedPreferences(
						"user", Context.MODE_PRIVATE);
				Editor editor = sharedPre.edit();
				editor.putBoolean("isFromSplash", false);
				editor.commit();
				dismiss();
				break;

			default:
				break;
			}

		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			SharedPreferences sharedPre = context.getSharedPreferences("user",
					Context.MODE_PRIVATE);
			Editor editor = sharedPre.edit();
			editor.putBoolean("isFromSplash", false);
			editor.commit();
			dismiss();
		}
		return false;
	}

}
