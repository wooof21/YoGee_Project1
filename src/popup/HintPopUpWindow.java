/**
 * 
 * @param
 */
package popup;

import tools.Tools;

import com.youge.jobfinder.R;

import android.R.integer;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

/**
 * 
 * @param
 */
public class HintPopUpWindow extends PopupWindow{


	public HintPopUpWindow(Context context, final View view, String type,
			int x, int y, boolean isMain){
		View ll = View.inflate(context, R.layout.hint_view, null);
		final ImageView iv = (ImageView) ll.findViewById(R.id.hint_iv);

		if (type.equals("1") && isMain){ // song
			iv.setImageResource(R.drawable.zhu_hint);
		}else if (type.equals("2") && isMain){ // che
			iv.setImageResource(R.drawable.xiu_hint);
		}else if (type.equals("3") && isMain){ // xiu
			iv.setImageResource(R.drawable.zhi_hint);
		}else if (type.equals("4") && isMain){ // zhi
			iv.setImageResource(R.drawable.che_hint);
		}else if (type.equals("5") && isMain){ // zhu
			iv.setImageResource(R.drawable.song_hint);
		}else if (type.equals("9") && isMain){ // zhu
			iv.setImageResource(R.drawable.jia_hint);
		}else if (type.equals("8") && isMain){ // zhu
			iv.setImageResource(R.drawable.timeout_hint_right);
		}else if (type.equals("0") && isMain){ // xian xia
			iv.setImageResource(R.drawable.offline_hint);
		}else if (type.equals("6") && isMain){ // xian shang
			iv.setImageResource(R.drawable.online_hint);
		}else if (type.equals("1") && !isMain){ // 
			iv.setImageResource(R.drawable.zhu_hint_left);
		}else if (type.equals("2") && !isMain){ // 
			iv.setImageResource(R.drawable.xiu_hint_left);
		}else if (type.equals("3") && !isMain){ // 
			iv.setImageResource(R.drawable.zhi_hint_left);
		}else if (type.equals("4") && !isMain){ // 
			iv.setImageResource(R.drawable.che_hint_left);
		}else if (type.equals("5") && !isMain){ // 
			iv.setImageResource(R.drawable.song_hint_left);
		}else if (type.equals("0") && !isMain){ // 
			iv.setImageResource(R.drawable.offline_hint_left);
		}else if (type.equals("6") && !isMain){ // 
			iv.setImageResource(R.drawable.online_hint_left);
		}else if (type.equals("8") && !isMain){ // 
			iv.setImageResource(R.drawable.timeout_hint_left);
		}

		int[] location = new int[2];
		view.getLocationOnScreen(location);

		int curIndex = (int) (new Tools().getScreenWidth(context) - x)
				/ new Tools().dip2px(context, 20);
		System.out.println("curIndex ---> " + curIndex);
		setFocusable(true);
		setOutsideTouchable(true);
		setTouchable(true);
		setBackgroundDrawable(new BitmapDrawable());
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		if(isMain){
			iv.setPadding(0, 0, curIndex*new Tools().dip2px(context, 20), 0);
		}else{
			//iv.setPadding(0, 0, curIndex*new Tools().dip2px(context, 10), 0);
			iv.setPadding(0, 0, 0, 0);
		}
		setContentView(ll);

		// showAsDropDown(view);
		showAtLocation(view, Gravity.NO_GRAVITY, location[0],
				location[1] - new Tools().dip2px(context, 25));//
		update();
		
		autoDismiss();
	}
	
	private void autoDismiss(){
		new Handler().postDelayed(new Runnable(){
			
			@Override
			public void run(){
				// TODO Auto-generated method stub
				dismiss();
			}
		}, 1500);
	}
}
