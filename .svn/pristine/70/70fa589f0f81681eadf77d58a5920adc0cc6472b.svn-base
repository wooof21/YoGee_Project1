/**
 * 
 * @param
 */
package view;

import com.youge.jobfinder.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;

/**
 * 
 * @param
 */
public class FixGridLayout extends LinearLayout{

	private int mWidth = 0;

	private int mWDivider = 0;
	private int mHDivider = 0;

	private int left_padding = 0;// 整个布局 左边第一个的padding值
	private int top_padding = 0;// 整个布局 顶部的padding值

	private int maxCount = 100;// 一行显示的最大控件个数

	private boolean mExpanded = true;// 是否展开
	private int cosLine = 2;
	private int mLineCount = 0;
	private int count;

	public int getLineCount(){
		return mLineCount;
	}

	public FixGridLayout(Context context){
		super(context);
	}

	public FixGridLayout(Context context, AttributeSet attrs){
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.FixLinerLayout, 0, 0);

		// left_padding = a.getInteger(R.styleable.FixLinerLayout_leftPadding,
		// 0);
		// top_padding = a.getInteger(R.styleable.FixLinerLayout_topPadding, 5);
		// mWDivider = a.getInteger(R.styleable.FixLinerLayout_mWDivider, 10);
		// mHDivider = a.getInteger(R.styleable.FixLinerLayout_mHDivider, 10);
		maxCount = a.getInteger(R.styleable.FixLinerLayout_maxCount, 100);

		a.recycle();

	}

	public boolean changeExpanded(){
		mExpanded = !mExpanded;
		postInvalidate();
		ViewGroup.LayoutParams params = getLayoutParams();
		setLayoutParams(params);
		return mExpanded;
	}

	public void setmWDivider(int mWDivider){
		this.mWDivider = mWDivider;
	}

	/**
	 * 控制子控件的换行
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b){
		mWidth = r - l;// 计算view 的宽度

		int x = left_padding;// 当前绘制控件的x坐标
		int y = top_padding;// 当前绘制控件的y坐标
		int count = getChildCount();
		int mCount = 0;// 当前的控件的个数
		for(int j = 0; j < count; j++){
			final View childView = getChildAt(j);
			measureView(childView);// 测量子控件
			int w = childView.getMeasuredWidth();// 子控件Child的宽高
			int h = childView.getMeasuredHeight();// 子控件Child的宽高
			// 当前x左边+默认间距+要加的子控件宽度>控件宽度 换行
			if (x + mWDivider + w + mWDivider > mWidth || mCount >= maxCount){
				mCount = 0;
				x = left_padding;// 坐标从坐标开始
				y = y + h + mHDivider;// y轴坐标为y+y轴间距+y轴控件高度
			}
			if (x != 0){// 要花的控件距离左边控件的距离
				x = x + mWDivider;
			}
			childView.layout(x, y, x + w, y + h);
			mCount += 1;
			x = x + w;// 花完之后 下一个控件x坐标
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		int x = left_padding;// 当前绘制控件的x坐标
		int y = top_padding;// 当前绘制控件的y坐标
		mLineCount = 0;
		count = getChildCount();
		int mCount = 0;// 当前的控件的个数
		for(int j = 0; j < count; j++){
			final View childView = getChildAt(j);
			measureView(childView);
			int w = childView.getMeasuredWidth();// 获取子控件Child的宽高
			int h = childView.getMeasuredHeight();// 获取子控件Child的宽高
			if (j == 0){// 这个事计算控件的高度 所以第一行也要加上控件高度+间距
				y = y + h + mHDivider;
			}
			// 当前x左边+默认间距+要加的子控件宽度>控件宽度 换行
			if (x + mWDivider + w + mWDivider > mWidth || mCount >= maxCount){
				mLineCount += 1;
				mCount = 0;
				x = left_padding;
				if (!mExpanded){
					if (mLineCount < cosLine){
						y = y + h + mHDivider;// y轴坐标为y+y轴间距+y轴控件高度
					}
				}else{
					y = y + h + mHDivider;// y轴坐标为y+y轴间距+y轴控件高度
				}
			}
			if (x != 0){// 要花的控件距离左边控件的距离
				x = x + mWDivider;
			}
			mCount += 1;// 当前行个数加1
			x = x + w;// 花完之后 下一个控件x坐标
		}
		setMeasuredDimension(widthMeasureSpec, y);
	}

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
	 * 
	 * @param pChild
	 * @date 2013-11-20 下午4:12:07
	 * @change JohnWatson
	 * @version 1.0
	 */
	public void measureView(View pChild){
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null){
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0){
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		}else{
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}

}
