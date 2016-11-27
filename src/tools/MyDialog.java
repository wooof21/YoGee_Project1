package tools;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author 自定义对话框
 */
public class MyDialog extends Dialog {
	private static int default_width = 100;
	private static int default_hight = 50;

	public MyDialog(Context context, int layout, int style) {
		this(context, default_width, default_hight, layout, style);
	}

	public MyDialog(Context context, int width, int height, int layout,
			int style) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		getDensity(context);
		params.width = width;
		params.height = LayoutParams.WRAP_CONTENT;
		params.gravity = Gravity.CENTER;
		params.format = PixelFormat.RGBA_8888;
		window.setAttributes(params);

	}

	public MyDialog(Context context, int width, int height, int layout,
			int style, int num) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		getDensity(context);
		params.width = width;
		if (num > 10) {
			params.height = height;
		} else {
			params.height = LayoutParams.WRAP_CONTENT;
		}
		params.gravity = Gravity.CENTER;
		params.format = PixelFormat.RGBA_8888;
		window.setAttributes(params);

	}

	/**
	 * 获取当前手机密度
	 */
	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		return displayMetrics.density;
	}

}
