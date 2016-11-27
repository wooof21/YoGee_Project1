/**
 * 
 * @param
 */
package popup;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.youge.jobfinder.MainActivity;
import com.youge.jobfinder.R;

import discover.CreditMallExchangeListDetail;

/**
 * 
 * @param
 */
public class ContentPopUpWindowSingleButton extends PopupWindow{

	public ContentPopUpWindowSingleButton(final Context context, View parent,
			String content){
		View view = View.inflate(context, R.layout.popup_content_single_button,
				null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView title = (TextView) view
				.findViewById(R.id.popup_single_btn_content);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_single_btn_content_commit);

		title.setText(content);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				Intent intent = new Intent(context, MainActivity.class);
				context.startActivity(intent);
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	public ContentPopUpWindowSingleButton(final Context context, View parent,
			String content, final String what, final String goodsId){
		View view = View.inflate(context, R.layout.popup_content_single_button,
				null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		TextView title = (TextView) view
				.findViewById(R.id.popup_single_btn_content);
		TextView commit = (TextView) view
				.findViewById(R.id.popup_single_btn_content_commit);

		title.setText(content);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();
		
		commit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v){
				// TODO Auto-generated method stub
				dismiss();
				if(what.equals("creditmall_exchange")){
					Intent intent = new Intent(context, CreditMallExchangeListDetail.class);
					intent.putExtra("from", "popup");
					intent.putExtra("id", goodsId);
					context.startActivity(intent);
				}
			}
		});
	}
}
