/**
 * 
 */
package progressdialog;

import com.youge.jobfinder.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author
 * 
 * @param
 * @return
 */
public class NewCustomProgressDialog extends Dialog {

	private Context context = null;
	private static NewCustomProgressDialog customProgressDialog = null;

	/**
	 * @param context
	 */
	public NewCustomProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NewCustomProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	public static NewCustomProgressDialog createDialog(Context context) {
		customProgressDialog = new NewCustomProgressDialog(context,
				R.style.NewCustomProgressDialog);
		customProgressDialog.setContentView(R.layout.newcustomprogressdialog);
		customProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		customProgressDialog.setCanceledOnTouchOutside(false);
		customProgressDialog.show();
		return customProgressDialog;
	}

	public void onWindowFocusChanged(boolean hasFocus) {

		if (customProgressDialog == null) {
			return;
		}

		ImageView imageView = (ImageView) customProgressDialog
				.findViewById(R.id.loadingImageView);
//		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
//				.getBackground();
//		animationDrawable.start();
	}

	/**
	 * 
	 * [Summary] setTitile ����
	 * 
	 * @param strTitle
	 * @return
	 * 
	 */
	public NewCustomProgressDialog setTitile(String strTitle) {
		return customProgressDialog;
	}

	/**
	 * 
	 * [Summary] setMessage ��ʾ����
	 * 
	 * @param strMessage
	 * @return
	 * 
	 */
	public NewCustomProgressDialog setMessage(String strMessage) {
		TextView tvMsg = (TextView) customProgressDialog
				.findViewById(R.id.id_tv_loadingmsg);

		if (tvMsg != null) {
			tvMsg.setText(strMessage);
		}

		return customProgressDialog;
	}

	@Override
	public void setCancelable(boolean flag) {
		// TODO Auto-generated method stub
		super.setCancelable(flag);
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		// TODO Auto-generated method stub
		super.setCanceledOnTouchOutside(false);
	}

}
