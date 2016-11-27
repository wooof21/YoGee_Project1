/**
 * 
 * @param
 */
package popup;

import java.util.ArrayList;

import model.NowEvent;
import adapter.EventPopupAdapter;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.youge.jobfinder.R;

/**
 * 
 * @param
 */
public class EventPopUpWindow extends PopupWindow {

	private Context context;
	private ArrayList<NowEvent> evnetList;

	public EventPopUpWindow(final Context context, View parent,
			ArrayList<NowEvent> evnetList) {
		View view = View.inflate(context, R.layout.popup_event, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.post_in));

		this.context = context;
		this.evnetList = evnetList;

		EventPopupAdapter mAdapter = new EventPopupAdapter(context, evnetList);

		ListView listView = (ListView) view.findViewById(R.id.listView);

		listView.setAdapter(mAdapter);

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
