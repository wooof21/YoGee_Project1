package popup;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.youge.jobfinder.R;
import com.youge.jobfinder.activity.LableGradActivity;

public class LabelPopUpWindow extends PopupWindow implements OnClickListener {

	private RelativeLayout label_zhu, label_xiu, label_zhi, label_che,
			label_song, label_jia;
	private ImageView iv1, iv2, iv3, iv4, iv5, iv6;
	private String label;

	public LabelPopUpWindow(Context context, View parent, String label) {
		View view = View.inflate(context, R.layout.popup_label, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		this.label = label;

		label_zhu = (RelativeLayout) view.findViewById(R.id.label_zhu);
		label_xiu = (RelativeLayout) view.findViewById(R.id.label_xiu);
		label_zhi = (RelativeLayout) view.findViewById(R.id.label_zhi);
		label_che = (RelativeLayout) view.findViewById(R.id.label_che);
		label_song = (RelativeLayout) view.findViewById(R.id.label_song);
		label_jia = (RelativeLayout) view.findViewById(R.id.label_jia);
		iv1 = (ImageView) view.findViewById(R.id.zhu_select);
		iv2 = (ImageView) view.findViewById(R.id.xiu_select);
		iv3 = (ImageView) view.findViewById(R.id.zhi_select);
		iv4 = (ImageView) view.findViewById(R.id.che_select);
		iv5 = (ImageView) view.findViewById(R.id.song_select);
		iv6 = (ImageView) view.findViewById(R.id.jia_select);

		setLabelSelect(label);
		label_zhu.setOnClickListener(this);
		label_xiu.setOnClickListener(this);
		label_zhi.setOnClickListener(this);
		label_che.setOnClickListener(this);
		label_song.setOnClickListener(this);
		label_jia.setOnClickListener(this);

		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.MATCH_PARENT);
		setBackgroundDrawable(new ColorDrawable(R.color.transparents));
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		// showAtLocation(parent, Gravity.BOTTOM, 0, 80);
		showAsDropDown(parent);
		update();
	}

	private void setLabelSelect(String label) {
		if (label.equals("1")) {
			iv1.setVisibility(View.VISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
			iv5.setVisibility(View.INVISIBLE);
			iv6.setVisibility(View.INVISIBLE);
		} else if (label.equals("2")) {
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.VISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
			iv5.setVisibility(View.INVISIBLE);
			iv6.setVisibility(View.INVISIBLE);
		} else if (label.equals("3")) {
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.VISIBLE);
			iv4.setVisibility(View.INVISIBLE);
			iv5.setVisibility(View.INVISIBLE);
			iv6.setVisibility(View.INVISIBLE);
		} else if (label.equals("4")) {
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.VISIBLE);
			iv5.setVisibility(View.INVISIBLE);
			iv6.setVisibility(View.INVISIBLE);
		} else if (label.equals("5")) {
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
			iv5.setVisibility(View.VISIBLE);
			iv6.setVisibility(View.INVISIBLE);
		} else if (label.equals("6")) {
			iv1.setVisibility(View.INVISIBLE);
			iv2.setVisibility(View.INVISIBLE);
			iv3.setVisibility(View.INVISIBLE);
			iv4.setVisibility(View.INVISIBLE);
			iv5.setVisibility(View.INVISIBLE);
			iv6.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		String label = "1";
		Message msg = LableGradActivity.instance.handler.obtainMessage();
		msg.what = 1;
		switch (v.getId()) {
		case R.id.label_zhu:
			setLabelSelect("1");
			label = "1";
			dismiss();
			break;
		case R.id.label_xiu:
			setLabelSelect("2");
			label = "2";
			dismiss();
			break;
		case R.id.label_zhi:
			setLabelSelect("3");
			label = "3";
			dismiss();
			break;
		case R.id.label_che:
			setLabelSelect("4");
			label = "4";
			dismiss();
			break;
		case R.id.label_song:
			setLabelSelect("5");
			label = "5";
			dismiss();
			break;
		case R.id.label_jia:
			setLabelSelect("6");
			label = "6";
			dismiss();
			break;
		default:
			break;
		}
		msg.obj = label;
		msg.sendToTarget();
	}
}
