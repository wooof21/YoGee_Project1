package view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class UpMarqueeImageView extends ImageView implements
		Animator.AnimatorListener {

	private static final int ANIMATION_DURATION = 500;
	private float height;
	private float count;
	private AnimatorSet mAnimatorStartSet;
	private AnimatorSet mAnimatorEndSet;
	private Drawable mDrawable;

	public UpMarqueeImageView(Context context) {
		super(context);
	}

	public UpMarqueeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		height = getHeight();// 确保view的高度
		count = height / 2;
		initStartAnimation(count);
	}

	/** --- 初始化向上脱离屏幕的动画效果 --- */
	private void initStartAnimation(float counts) {
		ObjectAnimator translate = ObjectAnimator.ofFloat(this, View.Y, counts,
				-height);
		ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f);
		mAnimatorStartSet = new AnimatorSet();
		mAnimatorStartSet.play(translate).with(alpha);
		mAnimatorStartSet.setDuration(ANIMATION_DURATION);
		mAnimatorStartSet.addListener(this);
	}

	/** --- 初始化从屏幕下面向上的动画效果 --- */
	private void initEndAnimation() {
		ObjectAnimator translate = ObjectAnimator.ofFloat(this, View.Y, height,
				count);
		ObjectAnimator alpha = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f);
		mAnimatorEndSet = new AnimatorSet();
		mAnimatorEndSet.play(translate).with(alpha);
		mAnimatorEndSet.setDuration(ANIMATION_DURATION);
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		// TODO Auto-generated method stub
		//super.setImageDrawable(drawable);
		mDrawable = drawable;
		if (null != mAnimatorStartSet) {
			mAnimatorStartSet.start();
		}
	}

	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		// TODO Auto-generated method stub
		super.setImageDrawable(mDrawable);
		if (null == mAnimatorEndSet) {
			initEndAnimation();
		}
		mAnimatorEndSet.start();
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub

	}

}
