/**
 * 
 *@param
 */
package view;

import com.youge.jobfinder.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * 
 *@param
 */
public class ExpandableLayout extends LinearLayout {

	private Context mContext;
	private LinearLayout mContentView;
	private ImageView mIconExpand;
	int mContentHeight = 0;
	int mTitleHeight = 0;
	private boolean isExpand = false;
	private Animation animationDown;
	private Animation animationUp;

	public ExpandableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (this.mContentHeight == 0) {
			this.mContentView.measure(widthMeasureSpec, 0);
			this.mContentHeight = this.mContentView.getMeasuredHeight();
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		//this.mContentView = (LinearLayout) this.findViewById(R.id.expand_value);
		this.mIconExpand = (ImageView) this.findViewById(R.id.my_order_lv_item_activityiv);

		this.mContentView.setOnClickListener(new ExpandListener());
		mContentView.setVisibility(View.VISIBLE);
	}

	private class ExpandListener implements View.OnClickListener {
		@Override
		public final void onClick(View paramView) {
			//clearAnimation是view的方法
			System.out.println("1111111111111111");
			clearAnimation();
			if (!isExpand) {
				if (animationDown == null) {
					animationDown = new DropDownAnim(mContentView,
							mContentHeight, true);
					animationDown.setDuration(200); // SUPPRESS CHECKSTYLE
				}
				startAnimation(animationDown);
				mContentView.startAnimation(AnimationUtils.loadAnimation(
						mContext, R.anim.animalpha));
				mIconExpand.setImageResource(R.drawable.arrow_up);
				isExpand = true;
			} else {
				isExpand = false;
				if (animationUp == null) {
					animationUp = new DropDownAnim(mContentView,
							mContentHeight, false);
					animationUp.setDuration(200); // SUPPRESS CHECKSTYLE
				}
				startAnimation(animationUp);
				mIconExpand.setImageResource(R.drawable.arrow_down);
			}
		}
	}

	class DropDownAnim extends Animation {
		/** 目标的高度 */
		private int targetHeight;
		/** 目标view */
		private View view;
		/** 是否向下展开 */
		private boolean down;

		/**
		 * 构造方法
		 * 
		 * @param targetview
		 *            需要被展现的view
		 * @param vieweight
		 *            目的高
		 * @param isdown
		 *            true:向下展开，false:收起
		 */
		public DropDownAnim(View targetview, int vieweight, boolean isdown) {
			this.view = targetview;
			this.targetHeight = vieweight;
			this.down = isdown;
		}
		//down的时候，interpolatedTime从0增长到1，这样newHeight也从0增长到targetHeight
		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			int newHeight;
			if (down) {
				newHeight = (int) (targetHeight * interpolatedTime);
			} else {
				newHeight = (int) (targetHeight * (1 - interpolatedTime));
			}
			view.getLayoutParams().height = newHeight;
			view.requestLayout();
			if (view.getVisibility() == View.GONE) {
				view.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
		}

		@Override
		public boolean willChangeBounds() {
			return true;
		}
	}
}

