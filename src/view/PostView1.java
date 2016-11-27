/**
 * 
 * @param
 */
package view;

import com.youge.jobfinder.R;

import tools.DensityUtil;
import tools.Tools;
import view.PostView.STATE;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationSet;

/**
 * 
 * @param
 */
public class PostView1 extends View{

	private Paint paint; // 创建画笔
	private RectF rect;

	private int INSIDE_SPEED = 1, OUTSIDE_SPEED = 1, SLEEP = 20;
	private PointF pA, pB, pC, pD, pa, pb, pc, pd;
	private PointF bsOringal, cheOringal, gwcOringal, kpOringal, lbOringal,
			wjOringal, xsmOringal, yxOringal;
	private Bitmap banshou, che, gouwuche, kapian, luobo, wenjian, xueshumao,
			yaoxiang, post, inside, outside;

	private STATE bsState, cheState, gwcState, kpState, lbState, wjState,
			xsmState, yxState;
	private Options opts;
	private static float SCALE;

	private AnimationSet set;

	private final static int POSTHALF = 65;
	private final static int INSIDEHALF = 176;
	private final static int OUTSIDEHALF = 238;
	private final static int BSHALF = 25;
	private final static int LBHALF = 25;
	private final static int YXHALF = 28;
	private final static int XSMHALF = 30;
	private final static int KPHALF = 23;
	private final static int WJHALF = 33;
	private final static int CHEHALF = 30;
	private final static int GWCHALF = 23;

	public enum STATE{
		LEFT_DOWN, RIGHT_DOWN, RIGHT_UP, LEFT_UP
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public PostView1(Context context, AttributeSet attrs){
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
		// thread = new postThread();
		// thread.start();
	}

	private void init(Context context){
		paint = new Paint();
		paint.setAntiAlias(true);
		rect = new RectF(0, 0, new Tools().getScreenWidth(context),
				new Tools().dip2px(context, 310));
		opts = new BitmapFactory.Options();
		opts.inPreferredConfig = Bitmap.Config.RGB_565;
		//opts.inScaled = true;
		// opts.inTargetDensity = (int) new
		// DensityUtil(context).getDmDensityDpi();
		SCALE = (float) ((new DensityUtil(context).getDmDensityDpi() / 320.0)-0.0);
		System.out.println("scale ---> "
				+ SCALE);
		pA = new PointF(rect.centerX(), rect.centerY() - OUTSIDEHALF * SCALE);
		pB = new PointF(rect.centerX() - OUTSIDEHALF * SCALE, rect.centerY());
		pC = new PointF(rect.centerX(), rect.centerY() + OUTSIDEHALF * SCALE);
		pD = new PointF(rect.centerX() + OUTSIDEHALF * SCALE, rect.centerY());
		pa = new PointF(rect.centerX(), rect.centerY() - INSIDEHALF * SCALE);
		pb = new PointF(rect.centerX() - INSIDEHALF * SCALE, rect.centerY());
		pc = new PointF(rect.centerX(), rect.centerY() + INSIDEHALF * SCALE);
		pd = new PointF(rect.centerX() + INSIDEHALF * SCALE, rect.centerY());
		bsOringal = new PointF(pA.x - (BSHALF+60) * SCALE, pA.y + (BSHALF-30) * SCALE);
		lbOringal = new PointF(pB.x + (LBHALF-28) * SCALE, pB.y + (LBHALF-17) * SCALE);
		yxOringal = new PointF(pB.x + (YXHALF+85) * SCALE, pB.y + (YXHALF+105) * SCALE);
		xsmOringal = new PointF(pC.x + (XSMHALF+90) * SCALE, pC.y - (XSMHALF+150) * SCALE);
		kpOringal = new PointF(pD.x - (KPHALF+131) * SCALE, pD.y - (KPHALF+145) * SCALE);
		wjOringal = new PointF(pa.x - (WJHALF+134) * SCALE, pa.y + (WJHALF+41) * SCALE);
		cheOringal = new PointF(pc.x - (CHEHALF-25) * SCALE, pc.y - (CHEHALF+20) * SCALE);
		gwcOringal = new PointF(pd.x - 52 * SCALE, pd.y - 62 * SCALE);
		bsState = STATE.LEFT_DOWN;
		lbState = STATE.RIGHT_DOWN;
		yxState = STATE.RIGHT_DOWN;
		xsmState = STATE.RIGHT_UP;
		kpState = STATE.LEFT_UP;
		wjState = STATE.LEFT_DOWN;
		cheState = STATE.RIGHT_UP;
		gwcState = STATE.LEFT_UP;
		banshou = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.banshou, opts), SCALE);
		che = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.cheche, opts), SCALE);
		gouwuche = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.gouwuche, opts), SCALE);
		kapian = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.kapian, opts), SCALE);
		luobo = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.luobo, opts), SCALE);
		wenjian = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.wenjian, opts), SCALE);
		xueshumao = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.xueshumao, opts), SCALE);
		yaoxiang = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.yaoxiang, opts), SCALE);
		outside = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.wai, opts), SCALE);
		inside = big(BitmapFactory.decodeResource(getResources(),
				R.drawable.nei, opts), SCALE);
		post = big(BitmapFactory.decodeResource(getResources(), R.drawable.fa,
				opts), SCALE);
	}

	private static Bitmap big(Bitmap bitmap, float scale){
		Matrix matrix = new Matrix();
		matrix.postScale(1.0f * scale, 1.0f * scale); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private static Bitmap small(Bitmap bitmap){
		Matrix matrix = new Matrix();
		matrix.postScale(1.0f * SCALE, 1.0f * SCALE); // 长和宽放大缩小的比例
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
				bitmap.getHeight(), matrix, true);
		return resizeBmp;
	}

	private void drawPic(Canvas canvas){

		// canvas.drawRect(rect, paint);
		canvas.drawBitmap(post, rect.centerX() - POSTHALF * SCALE, rect.centerY()
				- POSTHALF * SCALE, paint);
		canvas.drawBitmap(inside, rect.centerX() - INSIDEHALF * SCALE, rect.centerY()
				- INSIDEHALF * SCALE, paint);
		canvas.drawBitmap(outside, rect.centerX() - OUTSIDEHALF * SCALE, rect.centerY()
				- OUTSIDEHALF * SCALE, paint);

		canvas.drawBitmap(banshou, bsOringal.x, bsOringal.y, paint);
		canvas.drawBitmap(luobo, lbOringal.x, lbOringal.y, paint);
		canvas.drawBitmap(yaoxiang, yxOringal.x, yxOringal.y, paint);
		canvas.drawBitmap(xueshumao, xsmOringal.x, xsmOringal.y, paint);
		canvas.drawBitmap(kapian, kpOringal.x, kpOringal.y, paint);
		canvas.drawBitmap(wenjian, wjOringal.x, wjOringal.y, paint);
		canvas.drawBitmap(che, cheOringal.x, cheOringal.y, paint);
		canvas.drawBitmap(gouwuche, gwcOringal.x, gwcOringal.y, paint);

		try{
			Thread.sleep(SLEEP);		
			moveBS();
			moveLB();
			moveYX();
			moveXSM();
			moveKP();
			moveWJ();
			moveCHE();
			moveGWC();
		}catch(InterruptedException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//

		// try{
		// invalidate((int)rect.left, (int)rect.top, (int)rect.right,
		// (int)rect.bottom);
		// postInvalidate((int)rect.left, (int)rect.top, (int)rect.right,
		// (int)rect.bottom);
		// Thread.sleep(SLEEP);
		// }catch(InterruptedException e){
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private void moveBS(){
		switch(bsState){
			case LEFT_DOWN:
				if (bsOringal.x > pB.x - BSHALF * SCALE && bsOringal.y < pB.y + BSHALF * SCALE){
					bsOringal.x -= OUTSIDE_SPEED;
					bsOringal.y += OUTSIDE_SPEED;
				}else{
					bsOringal.x = pB.x - BSHALF * SCALE;
					bsOringal.y = pB.y - BSHALF * SCALE;
					bsState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if (bsOringal.x < pC.x - BSHALF * SCALE && bsOringal.y < pC.y - BSHALF * SCALE){
					bsOringal.x += OUTSIDE_SPEED;
					bsOringal.y += OUTSIDE_SPEED;
				}else{
					bsOringal.x = pC.x - BSHALF * SCALE;
					bsOringal.y = pC.y - BSHALF * SCALE;
					bsState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (bsOringal.x < pD.x + BSHALF * SCALE && bsOringal.y > pD.y - BSHALF * SCALE){
					bsOringal.x += OUTSIDE_SPEED;
					bsOringal.y -= OUTSIDE_SPEED;
				}else{
					bsOringal.x = pD.x - BSHALF * SCALE;
					bsOringal.y = pD.y - BSHALF * SCALE;
					bsState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (bsOringal.x > pA.x - BSHALF * SCALE && bsOringal.y > pA.y - BSHALF * SCALE){
					bsOringal.x -= OUTSIDE_SPEED;
					bsOringal.y -= OUTSIDE_SPEED;
				}else{
					bsOringal.x = pA.x - BSHALF * SCALE;
					bsOringal.y = pA.y - BSHALF * SCALE;
					bsState = STATE.LEFT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveLB(){
		switch(yxState){
			case RIGHT_DOWN:
				if (lbOringal.x < pC.x - LBHALF * SCALE && lbOringal.y < pC.y - LBHALF * SCALE){
					lbOringal.x += OUTSIDE_SPEED;
					lbOringal.y += OUTSIDE_SPEED;
				}else{
					lbOringal.x = pC.x - LBHALF * SCALE;
					lbOringal.y = pC.y - LBHALF * SCALE;
					yxState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (lbOringal.x < pD.x + LBHALF * SCALE && lbOringal.y > pD.y - LBHALF * SCALE){
					lbOringal.x += OUTSIDE_SPEED;
					lbOringal.y -= OUTSIDE_SPEED;
				}else{
					lbOringal.x = pD.x - LBHALF * SCALE;
					lbOringal.y = pD.y - LBHALF * SCALE;
					yxState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (lbOringal.x > pA.x - LBHALF * SCALE && lbOringal.y > pA.y - LBHALF * SCALE){
					lbOringal.x -= OUTSIDE_SPEED;
					lbOringal.y -= OUTSIDE_SPEED;
				}else{
					lbOringal.x = pA.x - LBHALF * SCALE;
					lbOringal.y = pA.y - LBHALF * SCALE;
					yxState = STATE.LEFT_DOWN;
				}
			break;
			case LEFT_DOWN:
				if (lbOringal.x > pB.x - LBHALF * SCALE && lbOringal.y < pB.y + LBHALF * SCALE){
					lbOringal.x -= OUTSIDE_SPEED;
					lbOringal.y += OUTSIDE_SPEED;
				}else{
					lbOringal.x = pB.x - LBHALF * SCALE;
					lbOringal.y = pB.y - LBHALF * SCALE;
					yxState = STATE.RIGHT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveYX(){
		switch(lbState){
			case RIGHT_DOWN:
				if (yxOringal.x < pC.x - YXHALF * SCALE && yxOringal.y < pC.y - YXHALF * SCALE){
					yxOringal.x += OUTSIDE_SPEED;
					yxOringal.y += OUTSIDE_SPEED;
				}else{
					yxOringal.x = pC.x - YXHALF * SCALE;
					yxOringal.y = pC.y - YXHALF * SCALE;
					lbState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (yxOringal.x < pD.x + YXHALF * SCALE && yxOringal.y > pD.y - YXHALF * SCALE){
					yxOringal.x += OUTSIDE_SPEED;
					yxOringal.y -= OUTSIDE_SPEED;
				}else{
					yxOringal.x = pD.x - YXHALF * SCALE;
					yxOringal.y = pD.y - YXHALF * SCALE;
					lbState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (yxOringal.x > pA.x - YXHALF * SCALE && yxOringal.y > pA.y - YXHALF * SCALE){
					yxOringal.x -= OUTSIDE_SPEED;
					yxOringal.y -= OUTSIDE_SPEED;
				}else{
					yxOringal.x = pA.x - YXHALF * SCALE;
					yxOringal.y = pA.y - YXHALF * SCALE;
					lbState = STATE.LEFT_DOWN;
				}
			break;
			case LEFT_DOWN:
				if (yxOringal.x > pB.x - YXHALF * SCALE && yxOringal.y < pB.y + YXHALF * SCALE){
					yxOringal.x -= OUTSIDE_SPEED;
					yxOringal.y += OUTSIDE_SPEED;
				}else{
					yxOringal.x = pB.x - YXHALF * SCALE;
					yxOringal.y = pB.y - YXHALF * SCALE;
					lbState = STATE.RIGHT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveXSM(){
		switch(xsmState){
			case LEFT_DOWN:
				if (xsmOringal.x > pB.x - XSMHALF * SCALE
						&& xsmOringal.y < pB.y + XSMHALF * SCALE){
					xsmOringal.x -= OUTSIDE_SPEED;
					xsmOringal.y += OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pB.x - XSMHALF * SCALE;
					xsmOringal.y = pB.y - XSMHALF * SCALE;
					xsmState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if (xsmOringal.x < pC.x - XSMHALF * SCALE
						&& xsmOringal.y < pC.y - XSMHALF * SCALE){
					xsmOringal.x += OUTSIDE_SPEED;
					xsmOringal.y += OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pC.x - XSMHALF * SCALE;
					xsmOringal.y = pC.y - XSMHALF * SCALE;
					xsmState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (xsmOringal.x < pD.x + XSMHALF * SCALE
						&& xsmOringal.y > pD.y - XSMHALF * SCALE){
					xsmOringal.x += OUTSIDE_SPEED;
					xsmOringal.y -= OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pD.x - XSMHALF * SCALE;
					xsmOringal.y = pD.y - XSMHALF * SCALE;
					xsmState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (xsmOringal.x > pA.x - XSMHALF * SCALE
						&& xsmOringal.y > pA.y - XSMHALF * SCALE){
					xsmOringal.x -= OUTSIDE_SPEED;
					xsmOringal.y -= OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pA.x - XSMHALF * SCALE;
					xsmOringal.y = pA.y - XSMHALF * SCALE;
					xsmState = STATE.LEFT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveKP(){
		switch(kpState){
			case LEFT_DOWN:
				if (kpOringal.x > pB.x - KPHALF * SCALE && kpOringal.y < pB.y + KPHALF * SCALE){
					kpOringal.x -= OUTSIDE_SPEED;
					kpOringal.y += OUTSIDE_SPEED;
				}else{
					kpOringal.x = pB.x - KPHALF * SCALE;
					kpOringal.y = pB.y - KPHALF * SCALE;
					kpState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if (kpOringal.x < pC.x - KPHALF * SCALE && kpOringal.y < pC.y - KPHALF * SCALE){
					kpOringal.x += OUTSIDE_SPEED;
					kpOringal.y += OUTSIDE_SPEED;
				}else{
					kpOringal.x = pC.x - KPHALF * SCALE;
					kpOringal.y = pC.y - KPHALF * SCALE;
					kpState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (kpOringal.x < pD.x + KPHALF * SCALE && kpOringal.y > pD.y - KPHALF * SCALE){
					kpOringal.x += OUTSIDE_SPEED;
					kpOringal.y -= OUTSIDE_SPEED;
				}else{
					kpOringal.x = pD.x - KPHALF * SCALE;
					kpOringal.y = pD.y - KPHALF * SCALE;
					kpState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (kpOringal.x > pA.x - KPHALF * SCALE && kpOringal.y > pA.y - KPHALF * SCALE){
					kpOringal.x -= OUTSIDE_SPEED;
					kpOringal.y -= OUTSIDE_SPEED;
				}else{
					kpOringal.x = pA.x - KPHALF * SCALE;
					kpOringal.y = pA.y - KPHALF * SCALE;
					kpState = STATE.LEFT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveWJ(){
		switch(wjState){
			case LEFT_DOWN:
				if (wjOringal.x > pb.x - WJHALF * SCALE && wjOringal.y < pb.y + WJHALF * SCALE){
					wjOringal.x -= INSIDE_SPEED;
					wjOringal.y += INSIDE_SPEED;
				}else{
					wjOringal.x = pb.x - WJHALF * SCALE;
					wjOringal.y = pb.y - WJHALF * SCALE;
					wjState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if (wjOringal.x < pc.x - WJHALF * SCALE && wjOringal.y < pc.y - WJHALF * SCALE){
					wjOringal.x += INSIDE_SPEED;
					wjOringal.y += INSIDE_SPEED;
				}else{
					wjOringal.x = pc.x - WJHALF * SCALE;
					wjOringal.y = pc.y - WJHALF * SCALE;
					wjState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (wjOringal.x < pd.x + WJHALF * SCALE && wjOringal.y > pd.y - WJHALF * SCALE){
					wjOringal.x += INSIDE_SPEED;
					wjOringal.y -= INSIDE_SPEED;
				}else{
					wjOringal.x = pd.x - WJHALF * SCALE;
					wjOringal.y = pd.y - WJHALF * SCALE;
					wjState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (wjOringal.x > pa.x - WJHALF * SCALE && wjOringal.y > pa.y - WJHALF * SCALE){
					wjOringal.x -= INSIDE_SPEED;
					wjOringal.y -= INSIDE_SPEED;
				}else{
					wjOringal.x = pa.x - WJHALF * SCALE;
					wjOringal.y = pa.y - WJHALF * SCALE;
					wjState = STATE.LEFT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveCHE(){
		switch(cheState){
			case LEFT_DOWN:
				if (cheOringal.x > pb.x - CHEHALF * SCALE
						&& cheOringal.y < pb.y + CHEHALF * SCALE){
					cheOringal.x -= INSIDE_SPEED;
					cheOringal.y += INSIDE_SPEED;
				}else{
					cheOringal.x = pb.x - CHEHALF * SCALE;
					cheOringal.y = pb.y - CHEHALF * SCALE;
					cheState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if (cheOringal.x < pc.x - CHEHALF * SCALE
						&& cheOringal.y < pc.y - CHEHALF * SCALE){
					cheOringal.x += INSIDE_SPEED;
					cheOringal.y += INSIDE_SPEED;
				}else{
					cheOringal.x = pc.x - CHEHALF * SCALE;
					cheOringal.y = pc.y - CHEHALF * SCALE;
					cheState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (cheOringal.x < pd.x + CHEHALF * SCALE
						&& cheOringal.y > pd.y - CHEHALF * SCALE){
					cheOringal.x += INSIDE_SPEED;
					cheOringal.y -= INSIDE_SPEED;
				}else{
					cheOringal.x = pd.x - CHEHALF * SCALE;
					cheOringal.y = pd.y - CHEHALF * SCALE;
					cheState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (cheOringal.x > pa.x - CHEHALF * SCALE
						&& cheOringal.y > pa.y - CHEHALF * SCALE){
					cheOringal.x -= INSIDE_SPEED;
					cheOringal.y -= INSIDE_SPEED;
				}else{
					cheOringal.x = pa.x - CHEHALF * SCALE;
					cheOringal.y = pa.y - CHEHALF * SCALE;
					cheState = STATE.LEFT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	private void moveGWC(){
		switch(gwcState){
			case LEFT_DOWN:
				if (gwcOringal.x > pb.x - GWCHALF * SCALE
						&& gwcOringal.y < pb.y + GWCHALF * SCALE){
					gwcOringal.x -= INSIDE_SPEED;
					gwcOringal.y += INSIDE_SPEED;
				}else{
					gwcOringal.x = pb.x - GWCHALF * SCALE;
					gwcOringal.y = pb.y - GWCHALF * SCALE;
					gwcState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if (gwcOringal.x < pc.x - GWCHALF * SCALE
						&& gwcOringal.y < pc.y - GWCHALF * SCALE){
					gwcOringal.x += INSIDE_SPEED;
					gwcOringal.y += INSIDE_SPEED;
				}else{
					gwcOringal.x = pc.x - GWCHALF * SCALE;
					gwcOringal.y = pc.y - GWCHALF * SCALE;
					gwcState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if (gwcOringal.x < pd.x + GWCHALF * SCALE
						&& gwcOringal.y > pd.y - GWCHALF * SCALE){
					gwcOringal.x += INSIDE_SPEED;
					gwcOringal.y -= INSIDE_SPEED;
				}else{
					gwcOringal.x = pd.x - GWCHALF * SCALE;
					gwcOringal.y = pd.y - GWCHALF * SCALE;
					gwcState = STATE.LEFT_UP;
				}
			break;
			case LEFT_UP:
				if (gwcOringal.x > pa.x - GWCHALF * SCALE
						&& gwcOringal.y > pa.y - GWCHALF * SCALE){
					gwcOringal.x -= INSIDE_SPEED;
					gwcOringal.y -= INSIDE_SPEED;
				}else{
					gwcOringal.x = pa.x - GWCHALF * SCALE;
					gwcOringal.y = pa.y - GWCHALF * SCALE;
					gwcState = STATE.LEFT_DOWN;
				}
			break;
			default:
			break;
		}
		invalidate(); // 实现动画的关键点
	}

	@Override
	protected void onDraw(Canvas canvas){
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		drawPic(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
