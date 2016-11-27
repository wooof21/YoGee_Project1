/**
 * 
 * @param
 */
package popup;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import view.SmoothImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.youge.jobfinder.R;

import adapter.ImageAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.PopupWindow;
import android.widget.ImageView.ScaleType;

/**
 * 
 * @param
 */
public class GalleyPopUpWindow extends PopupWindow{

	public GalleyPopUpWindow(Context context, View parent,
			ArrayList<String> pics){
		View view = View.inflate(context, R.layout.popup_gallery, null);
		view.startAnimation(AnimationUtils.loadAnimation(context,
				R.anim.push_bottom_in_2));

		Gallery gallery = (Gallery) view.findViewById(R.id.popup_gallery);
		// 设置图片匹配器
		gallery.setAdapter(new ImageAdapter(context, pics));
		

		setWidth(LayoutParams.FILL_PARENT);
		setHeight(LayoutParams.FILL_PARENT);
		setBackgroundDrawable(new BitmapDrawable());
		setFocusable(true);
		setOutsideTouchable(true);
		setContentView(view);
		showAtLocation(parent, Gravity.CENTER, 0, 0);
		update();

	}


}
