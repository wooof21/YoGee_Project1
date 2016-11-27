/**
 * 
 * @param
 */
package adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import view.SmoothImageView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.youge.jobfinder.R;

// 定义继承BaseAdapter的匹配器
public class ImageAdapter extends BaseAdapter{

	// Item的修饰背景
	int mGalleryItemBackground;

	// 上下文对象
	private Context mContext;
	private ArrayList<String> pic;
	private int width, height;

//	// 图片数组
//	private Integer[] mImageIds = { R.drawable.p1, R.drawable.p2,
//			R.drawable.p3, R.drawable.p4, R.drawable.p5, R.drawable.p6,
//			R.drawable.p7, R.drawable.p8, R.drawable.p9 };

	private SmoothImageView iv;

	// 构造方法
	public ImageAdapter(Context c, ArrayList<String> pics){
		mContext = c;
		this.pic = pics;
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		width = wm.getDefaultDisplay().getWidth();
		height = wm.getDefaultDisplay().getHeight();
	}

	// 返回项目数量
	@Override
	public int getCount(){
		return pic.size();
	}

	// 返回项目
	@Override
	public Object getItem(int position){
		return pic.get(position);
	}

	// 返回项目Id
	@Override
	public long getItemId(int position){
		return position;
	}

	// 返回视图
	@SuppressWarnings("deprecation")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent){

		if (convertView == null){
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.smooth_iv, null);
		}
		System.out.println("pic addr ---> " + pic.get(position));
		iv = (SmoothImageView) convertView.findViewById(R.id.smooth_iv);
		int[] location = new int[2];
		iv.getLocationOnScreen(location);
		iv.setOriginalInfo(iv.getWidth(), iv.getHeight(), 0,
				0);
		//iv.transformIn();
		iv.setLayoutParams(new Gallery.LayoutParams(width, height));
		iv.setScaleType(ScaleType.CENTER);

//		new Thread(){
//			public void run(){
//				Bitmap b = getURLBitmap(pic.get(position));
//				if (b == null){
//					System.out.println("111222");
//				}else{
//					System.out.println("222111");
//					Message msg = handler.obtainMessage();
//					msg.what = 1;
//					msg.obj = b;
//					msg.sendToTarget();
//				}
//			}
//		}.start();

		ImageLoader.getInstance().displayImage(pic.get(position), iv);
		// final SmoothImageView imageView = new SmoothImageView(mContext);
		// imageView.transformIn();
		// imageView.setLayoutParams(new Gallery.LayoutParams(
		// LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		// imageView.setScaleType(ScaleType.CENTER_INSIDE);
		// System.out.println("pic addr ---> " + pic.get(position));
		// ImageLoader.getInstance().displayImage(pic.get(position), imageView);
		// imageView.setImageResource(mImageIds[position]);
		// imageView.setBackgroundResource(mGalleryItemBackground);

		// ImageView iv = new ImageView(mContext);
		// iv.setImageResource(mImageIds[position]);
		// //给生成的ImageView设置Id，不设置的话Id都是-1
		// iv.setId(mImageIds[position]);
		// iv.setLayoutParams(new
		// Gallery.LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT));
		// iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		// iv.setBackgroundResource(mGalleryItemBackground);
		return convertView;//
	}

	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg){
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
				case 1:
					Bitmap b = resizeImage((Bitmap) msg.obj, width * 2 / 3,
							height * 2 / 3);
					iv.setImageBitmap(b);
				break;

				default:
				break;
			}

		}

	};

	public Bitmap getURLBitmap(String pic){
		URL imageUrl = null;
		Bitmap bitmap = null;
		try{

			imageUrl = new URL(pic);
		}catch(MalformedURLException e){
			e.printStackTrace();
		}
		try{

			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.connect();

			InputStream is = conn.getInputStream();

			bitmap = BitmapFactory.decodeStream(is);

			is.close();
		}catch(IOException e){
			e.printStackTrace();
		}
		return bitmap;
	}

	public Bitmap resizeImage(Bitmap bitmap, int w, int h){
		// load the origial Bitmap

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		int newWidth = w;
		int newHeight = h;

		Log.v("resizeImage", String.valueOf(width));
		Log.v("resizeImage", String.valueOf(height));

		Log.v("resizeImage", String.valueOf(newWidth));
		Log.v("resizeImage", String.valueOf(newHeight));
		// calculate the scale
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// create a matrix for the manipulation
		Matrix matrix = new Matrix();
		// resize the Bitmap
		matrix.postScale(scaleWidth, scaleHeight);
		// rotate the Bitmap
		matrix.postRotate(0);
		// recreate the new Bitmap
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;
	}

}
