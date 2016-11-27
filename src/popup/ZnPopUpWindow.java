package popup;

import com.youge.jobfinder.R;

import fragment.MainGrabFragment;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class ZnPopUpWindow extends PopupWindow implements OnClickListener{

	private RelativeLayout distance, price, latest, share;
	private ImageView iv1, iv2, iv3, iv4;

	public ZnPopUpWindow(Context context, View parent, int width, String sort){
		View view = View.inflate(context, R.layout.popup_zhinengpaixu, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		distance = (RelativeLayout) view.findViewById(R.id.zn_distance);
		price = (RelativeLayout) view.findViewById(R.id.zn_price);
		latest = (RelativeLayout) view.findViewById(R.id.zn_latest);
		share = (RelativeLayout) view.findViewById(R.id.zn_share);
		iv1 = (ImageView) view.findViewById(R.id.zn_distance_select);
		iv2 = (ImageView) view.findViewById(R.id.zn_price_select);
		iv3 = (ImageView) view.findViewById(R.id.zn_latest_select);
		iv4 = (ImageView) view.findViewById(R.id.zn_share_select);

		setDefaultSelect(sort);

		distance.setOnClickListener(this);
		price.setOnClickListener(this);
		latest.setOnClickListener(this);
		share.setOnClickListener(this);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		// setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		// showAtLocation(parent, Gravity.CENTER, 0, 30);
		showAsDropDown(parent);
		update();
	}

	@Override
	public void onClick(View v){
		// TODO Auto-generated method stub
		String sort = "0";
		Message msg = MainGrabFragment.instance.handler.obtainMessage();
		msg.what = 5;
		switch(v.getId()){
			case R.id.zn_distance:
				selectById(R.id.zn_distance);
				sort = "1";
				dismiss();
			break;
			case R.id.zn_price:
				selectById(R.id.zn_price);
				sort = "2";
				dismiss();
			break;
			case R.id.zn_latest:
				selectById(R.id.zn_latest);
				sort = "3";
				dismiss();
			break;
			case R.id.zn_share:
				selectById(R.id.zn_share);
				sort = "4";
				dismiss();
			break;
			default:
			break;
		}
		msg.obj = sort;
		msg.sendToTarget();

	}

	private void setDefaultSelect(String sort){
		if (sort.equals("0")){
			iv1.setVisibility(View.VISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
		}else if (sort.equals("1")){
			iv1.setVisibility(View.VISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
		}else if (sort.equals("2")){
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.VISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
		}else if (sort.equals("3")){
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.VISIBLE);
			iv4.setVisibility(View.INVISIBLE);
		}else{
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.VISIBLE);
		}
	}

	private void selectById(int id){
		switch(id){
			case R.id.zn_distance:
				iv1.setVisibility(View.VISIBLE);
				iv2.setVisibility(View.INVISIBLE);
				iv3.setVisibility(View.INVISIBLE);
				iv4.setVisibility(View.INVISIBLE);
			break;
			case R.id.zn_price:
				iv1.setVisibility(View.INVISIBLE);
				iv2.setVisibility(View.VISIBLE);
				iv3.setVisibility(View.INVISIBLE);
				iv4.setVisibility(View.INVISIBLE);
			break;
			case R.id.zn_latest:
				iv1.setVisibility(View.INVISIBLE);
				iv2.setVisibility(View.INVISIBLE);
				iv3.setVisibility(View.VISIBLE);
				iv4.setVisibility(View.INVISIBLE);
			break;
			case R.id.zn_share:
				iv1.setVisibility(View.INVISIBLE);
				iv2.setVisibility(View.INVISIBLE);
				iv3.setVisibility(View.INVISIBLE);
				iv4.setVisibility(View.VISIBLE);
			break;
			default:
			break;
		}
	}

}
