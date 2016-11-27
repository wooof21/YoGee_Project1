package view;

import com.youge.jobfinder.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;

public class RoundImageView extends ImageView{

	// border_thickness ���ñ߿�Ŀ��
	// border_inside_color �����ڱ߿����ɫ
	// border_outside_color ������߿����ɫ
	// ����߿����ֻ����һ���Ļ�������Ϊֻ��ʾһ���߿�

	// ���� �����layout��
	// xmlns:android="http://schemas.android.com/apk/res/android"
	// xmlns:tools="http://schemas.android.com/tools"
	// xmlns:imagecontrol="http://schemas.android.com/apk/res-auto"

	/*
	 * <com.alan.roundimageview.RoundImageView
	 * android:id="@+id/roundImage_one_border" android:layout_width="80dp"
	 * android:layout_height="80dp" android:layout_marginLeft="10dp"
	 * android:layout_marginRight="10dp"
	 * imagecontrol:border_outside_color="#FFFF0000"
	 * imagecontrol:border_thickness="2dp"
	 * android:src="@drawable/default_portrait" />
	 */

	/** 
     * 图片的类型，圆形or圆角 
     */  
    private int type;  
    private static final int TYPE_CIRCLE = 0;  
    private static final int TYPE_ROUND = 1;  
  
    /** 
     * 圆角大小的默认值 
     */  
    private static final int BODER_RADIUS_DEFAULT = 10;  
    /** 
     * 圆角的大小 
     */  
    private int mBorderRadius;  
    /** 
     * 圆角的半径 
     */  
    private int mRadius;  
	private int mBorderThickness = 0;
	private Context mContext;
	private int defaultColor = 0xFFFFFFFF;
	// 如果只有其中一个有值，则只画一个圆形边框
	private int mBorderOutsideColor = 0;
	private int mBorderInsideColor = 0;
	// 控件默认长、宽
	private int defaultWidth = 0;
	private int defaultHeight = 0;

	public RoundImageView(Context context){
		super(context);
		mContext = context;
	}

	public RoundImageView(Context context, AttributeSet attrs){
		super(context, attrs);
		mContext = context;
		setCustomAttributes(attrs);
	}

	public RoundImageView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		mContext = context;
		setCustomAttributes(attrs);
	}

	private void setCustomAttributes(AttributeSet attrs){
		TypedArray a = mContext.obtainStyledAttributes(attrs,
				R.styleable.RoundImageView);
		mBorderThickness = a.getDimensionPixelSize(
				R.styleable.RoundImageView_border_thickness, 0);
		mBorderOutsideColor = a.getColor(
				R.styleable.RoundImageView_border_outside_color, defaultColor);
		mBorderInsideColor = a.getColor(
				R.styleable.RoundImageView_border_inside_color, defaultColor);
		
		mBorderRadius = a.getDimensionPixelSize(  
                R.styleable.RoundImageView_borderRadius, (int) TypedValue  
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,  
                                BODER_RADIUS_DEFAULT, getResources()  
                                        .getDisplayMetrics()));// 默认为10dp  
        type = a.getInt(R.styleable.RoundImageView_type, TYPE_CIRCLE);// 默认为Circle  
        a.recycle();
	}

	@Override
	protected void onDraw(Canvas canvas){
		Drawable drawable = getDrawable();
		if (drawable == null){
			return;
		}

		if (getWidth() == 0 || getHeight() == 0){
			return;
		}
		this.measure(0, 0);
		if (drawable.getClass() == NinePatchDrawable.class)
			return;
		Bitmap b = ((BitmapDrawable) drawable).getBitmap();
		Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
		if (defaultWidth == 0){
			defaultWidth = getWidth();

		}
		if (defaultHeight == 0){
			defaultHeight = getHeight();
		}
		// ��֤���¶�ȡͼƬ�󲻻���ΪͼƬ��С��ı�ؼ��?�ߵĴ�С����Կ?��Ϊwrap_content���ֵ�imageview�����ᵼ��margin��Ч��
		// if (defaultWidth != 0 && defaultHeight != 0) {
		// LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		// defaultWidth, defaultHeight);
		// setLayoutParams(params);
		// }
		if (mBorderInsideColor != defaultColor
				&& mBorderOutsideColor != defaultColor){// 定义画两个边框，分别为外圆边框和内圆边框
			mRadius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - 2 * mBorderThickness;
			// 画内圆
			drawCircleBorder(canvas, mRadius + mBorderThickness / 2,
					mBorderInsideColor);
			// 画外圆
			drawCircleBorder(canvas, mRadius + mBorderThickness
					+ mBorderThickness / 2, mBorderOutsideColor);
		}else if (mBorderInsideColor != defaultColor
				&& mBorderOutsideColor == defaultColor){// 定义画一个边框
			mRadius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, mRadius + mBorderThickness / 2,
					mBorderInsideColor);
		}else if (mBorderInsideColor == defaultColor
				&& mBorderOutsideColor != defaultColor){// 定义画一个边框
			mRadius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2 - mBorderThickness;
			drawCircleBorder(canvas, mRadius + mBorderThickness / 2,
					mBorderOutsideColor);
		}else{// 没有边框
			mRadius = (defaultWidth < defaultHeight ? defaultWidth
					: defaultHeight) / 2;
		}
		Bitmap roundBitmap = getCroppedRoundBitmap(bitmap, mRadius);
		canvas.drawBitmap(roundBitmap, defaultWidth / 2 - mRadius, defaultHeight
				/ 2 - mRadius, null);
	}
	
//	@Override  
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)  
//    {  
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);  
//  
//        /** 
//         * 如果类型是圆形，则强制改变view的宽高一致，以小值为准 
//         */  
//        if (type == TYPE_CIRCLE)  
//        {  
//        	defaultWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());  
//            mRadius = defaultWidth / 2;  
//            setMeasuredDimension(defaultWidth, defaultWidth);  
//        }  
//  
//    } 

	/**
	 * 获取裁剪后的圆形图片
	 * 
	 * @param radius半径
	 *            
	 */
	public Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius){
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;

		// 为了防止宽高不相等，造成圆形图片变形，因此截取长方形中处于中间位置最大的正方形图片
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		int squareWidth = 0, squareHeight = 0;
		int x = 0, y = 0;
		Bitmap squareBitmap;
		if (bmpHeight > bmpWidth){// 高大于宽
			squareWidth = squareHeight = bmpWidth;
			x = 0;
			y = (bmpHeight - bmpWidth) / 2;
			// 截取正方形图片
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		}else if (bmpHeight < bmpWidth){// 宽大于高
			squareWidth = squareHeight = bmpHeight;
			x = (bmpWidth - bmpHeight) / 2;
			y = 0;
			squareBitmap = Bitmap.createBitmap(bmp, x, y, squareWidth,
					squareHeight);
		}else{
			squareBitmap = bmp;
		}

		if (squareBitmap.getWidth() != diameter
				|| squareBitmap.getHeight() != diameter){
			scaledSrcBmp = Bitmap.createScaledBitmap(squareBitmap, diameter,
					diameter, true);

		}else{
			scaledSrcBmp = squareBitmap;
		}
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		Paint paint = new Paint();
		Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);
		// bitmap����(recycle�����ڲ����ļ�XML������Ч��)
		// bmp.recycle();
		// squareBitmap.recycle();
		// scaledSrcBmp.recycle();
		bmp = null;
		squareBitmap = null;
		scaledSrcBmp = null;
		return output;
	}

	/**
	 * 边缘画圆
	 */
	private void drawCircleBorder(Canvas canvas, int radius, int color){
		Paint paint = new Paint();
		/* 去锯齿 */
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setColor(color);
		/* 设置paint的　style　为STROKE：空心 */
		paint.setStyle(Paint.Style.STROKE);
		/* 设置paint的外框宽度 */
		paint.setStrokeWidth(mBorderThickness);
		canvas.drawCircle(defaultWidth / 2, defaultHeight / 2, radius, paint);
	}

}
