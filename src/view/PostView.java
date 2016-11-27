/**
 * 
 * @param
 */
package view;

import tools.Tools;

import com.youge.jobfinder.R;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * @param
 */
public class PostView extends SurfaceView implements SurfaceHolder.Callback{

	private SurfaceHolder holder;
	private postThread pThread;
	
	private Canvas canvas = null;
	private Paint paint; //创建画笔
	private RectF rect;
	
	private int INSIDE_SPEED = 1, OUTSIDE_SPEED = 2, SLEEP = 200;
	private PointF pA, pB, pC, pD, pa, pb, pc, pd;
	private PointF bsOringal, cheOringal, gwcOringal, kpOringal, lbOringal, wjOringal, xsmOringal, yxOringal;
	private Bitmap banshou, che, gouwuche, kapian, luobo, wenjian, xueshumao, yaoxiang, post, inside, outside;
	
	private STATE bsState, cheState, gwcState, kpState, lbState, wjState, xsmState, yxState;
	private Options opts;
	
	public enum STATE{
		LEFT_DOWN,
		RIGHT_DOWN,
		RIGHT_UP,
		LEFT_UP
	}
//	/**
//	 * @param context
//	 */
//	public PostView(Context context, int left, int top, int right, int bottom){
//		super(context);
//		// TODO Auto-generated constructor stub
//		holder = this.getHolder();
//		holder.addCallback(this);
//		paint = new Paint();
//		paint.setAntiAlias(true);
//		rect = new RectF(0, 720, 720, 1280);
//		pA = new PointF(rect.centerX(), rect.centerY()-140);
//		pB = new PointF(rect.centerX()-140, rect.centerY());
//		pC = new PointF(rect.centerX(), rect.centerY()+140);
//		pD = new PointF(rect.centerX()+140, rect.centerY());
//		pa = new PointF(rect.centerX(), rect.centerY()-104);
//		pb = new PointF(rect.centerX()-104, rect.centerY());
//		pc = new PointF(rect.centerX(), rect.centerY()+104);
//		pd = new PointF(rect.centerX()+104, rect.centerY());
//		bsOringal = new PointF(pA.x-45, pA.y+3);
//		lbOringal = new PointF(pB.x+20, pB.y+31);
//		yxOringal = new PointF(pB.x+90, pB.y+95);
//		xsmOringal = new PointF(pC.x+50, pC.y-80);
//		kpOringal = new PointF(pD.x-64, pD.y-74);
//		wjOringal = new PointF(pa.x-75, pa.y+32);
//		cheOringal = new PointF(pc.x+2, pc.y-38);
//		gwcOringal = new PointF(pd.x-62, pd.y-62);
//		bsState = STATE.LEFT_DOWN;
//		lbState = STATE.RIGHT_DOWN;
//		yxState = STATE.RIGHT_DOWN;
//		xsmState = STATE.RIGHT_UP;
//		kpState = STATE.LEFT_UP;
//		wjState = STATE.LEFT_DOWN;
//		cheState = STATE.RIGHT_UP;
//		gwcState = STATE.LEFT_UP;
//		banshou = BitmapFactory.decodeResource(getResources(), R.drawable.banshou);
//		che = BitmapFactory.decodeResource(getResources(), R.drawable.cheche);
//		gouwuche = BitmapFactory.decodeResource(getResources(), R.drawable.gouwuche);
//		kapian = BitmapFactory.decodeResource(getResources(), R.drawable.kapian);
//		luobo = BitmapFactory.decodeResource(getResources(), R.drawable.luobo);
//		wenjian = BitmapFactory.decodeResource(getResources(), R.drawable.wenjian);
//		xueshumao = BitmapFactory.decodeResource(getResources(), R.drawable.xueshumao);
//		yaoxiang = BitmapFactory.decodeResource(getResources(), R.drawable.yaoxiang);
//		pThread = new postThread(holder);//创建一个绘图线程
//	}
	
	
	

	/**
	 * @param context
	 * @param attrs
	 */
	public PostView(Context context, AttributeSet attrs){
		super(context, attrs);
		// TODO Auto-generated constructor stub
		holder = this.getHolder();
		holder.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		init(context);
		pThread = new postThread(holder);//创建一个绘图线程
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility){
		// TODO Auto-generated method stub
		super.onWindowVisibilityChanged(visibility);
		if(visibility == VISIBLE){
			try{
				pThread.isRun = true;
			}catch(IllegalThreadStateException e){
			}
		}else{
			try{
				pThread.isRun = false;
			}catch(IllegalThreadStateException e){
			}
		}
	}

	private void init(Context context){
		rect = new RectF(0, 0, new Tools().getScreenWidth(context), new Tools().dip2px(context, 280));
		opts = new BitmapFactory.Options();  
	    opts.inPreferredConfig = Bitmap.Config.RGB_565;  
		pA = new PointF(rect.centerX(), rect.centerY()-140);
		pB = new PointF(rect.centerX()-140, rect.centerY());
		pC = new PointF(rect.centerX(), rect.centerY()+140);
		pD = new PointF(rect.centerX()+140, rect.centerY());
		pa = new PointF(rect.centerX(), rect.centerY()-104);
		pb = new PointF(rect.centerX()-104, rect.centerY());
		pc = new PointF(rect.centerX(), rect.centerY()+104);
		pd = new PointF(rect.centerX()+104, rect.centerY());
		bsOringal = new PointF(pA.x-45, pA.y+3);
		lbOringal = new PointF(pB.x+20, pB.y+31);
		yxOringal = new PointF(pB.x+90, pB.y+95);
		xsmOringal = new PointF(pC.x+50, pC.y-80);
		kpOringal = new PointF(pD.x-64, pD.y-74);
		wjOringal = new PointF(pa.x-75, pa.y+32);
		cheOringal = new PointF(pc.x+2, pc.y-38);
		gwcOringal = new PointF(pd.x-62, pd.y-62);
		bsState = STATE.LEFT_DOWN;
		lbState = STATE.RIGHT_DOWN;
		yxState = STATE.RIGHT_DOWN;
		xsmState = STATE.RIGHT_UP;
		kpState = STATE.LEFT_UP;
		wjState = STATE.LEFT_DOWN;
		cheState = STATE.RIGHT_UP;
		gwcState = STATE.LEFT_UP;
		banshou = BitmapFactory.decodeResource(getResources(), R.drawable.banshou, opts);
		che = BitmapFactory.decodeResource(getResources(), R.drawable.cheche, opts);
		gouwuche = BitmapFactory.decodeResource(getResources(), R.drawable.gouwuche, opts);
		kapian = BitmapFactory.decodeResource(getResources(), R.drawable.kapian, opts);
		luobo = BitmapFactory.decodeResource(getResources(), R.drawable.luobo, opts);
		wenjian = BitmapFactory.decodeResource(getResources(), R.drawable.wenjian, opts);
		xueshumao = BitmapFactory.decodeResource(getResources(), R.drawable.xueshumao, opts);
		yaoxiang = BitmapFactory.decodeResource(getResources(), R.drawable.yaoxiang, opts);
		outside = BitmapFactory.decodeResource(getResources(), R.drawable.wai, opts);
		inside = BitmapFactory.decodeResource(getResources(), R.drawable.nei, opts);
		post = BitmapFactory.decodeResource(getResources(), R.drawable.fa, opts);
	}

	class postThread extends Thread{
		private SurfaceHolder holder;
		public boolean isRun;

		public postThread(SurfaceHolder holder){
			this.holder = holder;
			isRun = true;	
		}
		/**
		 * 
		 * @param
		 */
		@Override
		public void run(){
			// TODO Auto-generated method stub
			while(isRun){
				
				try{
					synchronized(holder){
						canvas = holder.lockCanvas();//锁定画布，一般在锁定后就可以通过其返回的画布对象Canvas，在其上面画图等操作了。
						drawPic();
						//Thread.sleep(SLEEP);
					}
				}catch(Exception e){

				}finally{
					if (canvas != null){
					holder.unlockCanvasAndPost(canvas);;//结束锁定画图，并提交改变。
					
				}
				}
				
			}
		}
	}

	private void drawPic(){
		canvas.drawColor(Color.WHITE);//设置画布背景颜色
//		canvas.drawRect(rect, paint);
		canvas.drawBitmap(post, rect.centerX()-44, rect.centerY()-44, paint);
		canvas.drawBitmap(inside, rect.centerX()-104, rect.centerY()-104, paint);
		canvas.drawBitmap(outside, rect.centerX()-140, rect.centerY()-140, paint);

		canvas.drawBitmap(banshou, bsOringal.x, bsOringal.y, paint);
		canvas.drawBitmap(luobo, lbOringal.x, lbOringal.y, paint);
		canvas.drawBitmap(yaoxiang, yxOringal.x, yxOringal.y, paint);
		canvas.drawBitmap(xueshumao, xsmOringal.x, xsmOringal.y, paint);
		canvas.drawBitmap(kapian, kpOringal.x, kpOringal.y, paint);
		canvas.drawBitmap(wenjian, wjOringal.x, wjOringal.y, paint);
		canvas.drawBitmap(che, cheOringal.x, cheOringal.y, paint);
		canvas.drawBitmap(gouwuche, gwcOringal.x, gwcOringal.y, paint);

		moveBS();
		moveLB();
		moveYX();
		moveXSM();
		moveKP();
		moveWJ();
		moveCHE();
		moveGWC();
//		

	}
	private void moveBS(){
		switch(bsState){
			case LEFT_DOWN:
				if(bsOringal.x > pB.x-17 && bsOringal.y < pB.y+17){
					bsOringal.x -= OUTSIDE_SPEED;
					bsOringal.y += OUTSIDE_SPEED;
				}else{
					bsOringal.x = pB.x - 17;
					bsOringal.y = pB.y - 17;
					bsState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if(bsOringal.x < pC.x-17 && bsOringal.y < pC.y-17){
					bsOringal.x += OUTSIDE_SPEED;
					bsOringal.y += OUTSIDE_SPEED;
				}else{
					bsOringal.x = pC.x - 17;
					bsOringal.y = pC.y - 17;
					bsState = STATE.RIGHT_UP;
				}
				break;
			case RIGHT_UP:
				if(bsOringal.x < pD.x+17 && bsOringal.y > pD.y-17){
					bsOringal.x += OUTSIDE_SPEED;
					bsOringal.y -= OUTSIDE_SPEED;
				}else{
					bsOringal.x = pD.x - 17;
					bsOringal.y = pD.y - 17;
					bsState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(bsOringal.x > pA.x-17 && bsOringal.y > pA.y-17){
					bsOringal.x -= OUTSIDE_SPEED;
					bsOringal.y -= OUTSIDE_SPEED;
				}else{
					bsOringal.x = pA.x - 17;
					bsOringal.y = pA.y - 17;
					bsState = STATE.LEFT_DOWN;
				}
				break;
			default:
			break;
		}
	}
	private void moveLB(){
		switch(yxState){
			case RIGHT_DOWN:
				if(lbOringal.x < pC.x-18 && lbOringal.y < pC.y-18){
					lbOringal.x += OUTSIDE_SPEED;
					lbOringal.y += OUTSIDE_SPEED;
				}else{
					lbOringal.x = pC.x - 18;
					lbOringal.y = pC.y - 18;
					yxState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if(lbOringal.x < pD.x+18 && lbOringal.y > pD.y-18){
					lbOringal.x += OUTSIDE_SPEED;
					lbOringal.y -= OUTSIDE_SPEED;
				}else{
					lbOringal.x = pD.x - 18;
					lbOringal.y = pD.y - 18;
					yxState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(lbOringal.x > pA.x-18 && lbOringal.y > pA.y-18){
					lbOringal.x -= OUTSIDE_SPEED;
					lbOringal.y -= OUTSIDE_SPEED;
				}else{
					lbOringal.x = pA.x - 18;
					lbOringal.y = pA.y - 18;
					yxState = STATE.LEFT_DOWN;
				}
				break;
			case LEFT_DOWN:
				if(lbOringal.x > pB.x-18 && lbOringal.y < pB.y+18){
					lbOringal.x -= OUTSIDE_SPEED;
					lbOringal.y += OUTSIDE_SPEED;
				}else{
					lbOringal.x = pB.x - 18;
					lbOringal.y = pB.y - 18;
					yxState = STATE.RIGHT_DOWN;
				}
			break;
			default:
			break;
		}
	}
	private void moveYX(){
		switch(lbState){
			case RIGHT_DOWN:
				if(yxOringal.x < pC.x-15 && yxOringal.y < pC.y-15){
					yxOringal.x += OUTSIDE_SPEED;
					yxOringal.y += OUTSIDE_SPEED;
				}else{
					yxOringal.x = pC.x - 15;
					yxOringal.y = pC. y -15;
					lbState = STATE.RIGHT_UP;
				}
			break;
			case RIGHT_UP:
				if(yxOringal.x < pD.x+15 && yxOringal.y > pD.y-15){
					yxOringal.x += OUTSIDE_SPEED;
					yxOringal.y -= OUTSIDE_SPEED;
				}else{
					yxOringal.x = pD.x - 15;
					yxOringal.y = pD. y -15;
					lbState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(yxOringal.x > pA.x-15 && yxOringal.y > pA.y-15){
					yxOringal.x -= OUTSIDE_SPEED;
					yxOringal.y -= OUTSIDE_SPEED;
				}else{
					yxOringal.x = pA.x - 15;
					yxOringal.y = pA. y -15;
					lbState = STATE.LEFT_DOWN;
				}
				break;
			case LEFT_DOWN:
				if(yxOringal.x > pB.x-15 && yxOringal.y < pB.y+15){
					yxOringal.x -= OUTSIDE_SPEED;
					yxOringal.y += OUTSIDE_SPEED;
				}else{
					yxOringal.x = pB.x - 15;
					yxOringal.y = pB. y -15;
					lbState = STATE.RIGHT_DOWN;
				}
			break;
			default:
			break;
		}
	}
	
	private void moveXSM(){
		switch(xsmState){
			case LEFT_DOWN:
				if(xsmOringal.x > pB.x-19 && xsmOringal.y < pB.y+19){
					xsmOringal.x -= OUTSIDE_SPEED;
					xsmOringal.y += OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pB.x - 19;
					xsmOringal.y = pB.y - 19;
					xsmState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if(xsmOringal.x < pC.x-19 && xsmOringal.y < pC.y-19){
					xsmOringal.x += OUTSIDE_SPEED;
					xsmOringal.y += OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pC.x - 19;
					xsmOringal.y = pC.y - 19;
					xsmState = STATE.RIGHT_UP;
				}
				break;
			case RIGHT_UP:
				if(xsmOringal.x < pD.x+19 && xsmOringal.y > pD.y-19){
					xsmOringal.x += OUTSIDE_SPEED;
					xsmOringal.y -= OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pD.x - 19;
					xsmOringal.y = pD.y - 19;
					xsmState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(xsmOringal.x > pA.x-19 && xsmOringal.y > pA.y-19){
					xsmOringal.x -= OUTSIDE_SPEED;
					xsmOringal.y -= OUTSIDE_SPEED;
				}else{
					xsmOringal.x = pA.x - 19;
					xsmOringal.y = pA.y - 19;
					xsmState = STATE.LEFT_DOWN;
				}
				break;
			default:
			break;
		}
	}
	private void moveKP(){
		switch(kpState){
			case LEFT_DOWN:
				if(kpOringal.x > pB.x-14 && kpOringal.y < pB.y+14){
					kpOringal.x -= OUTSIDE_SPEED;
					kpOringal.y += OUTSIDE_SPEED;
				}else{
					kpOringal.x = pB.x - 14;
					kpOringal.y = pB.y - 14;
					kpState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if(kpOringal.x < pC.x-14 && kpOringal.y < pC.y-14){
					kpOringal.x += OUTSIDE_SPEED;
					kpOringal.y += OUTSIDE_SPEED;
				}else{
					kpOringal.x = pC.x - 14;
					kpOringal.y = pC.y - 14;
					kpState = STATE.RIGHT_UP;
				}
				break;
			case RIGHT_UP:
				if(kpOringal.x < pD.x+14 && kpOringal.y > pD.y-14){
					kpOringal.x += OUTSIDE_SPEED;
					kpOringal.y -= OUTSIDE_SPEED;
				}else{
					kpOringal.x = pD.x - 14;
					kpOringal.y = pD.y - 14;
					kpState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(kpOringal.x > pA.x-14 && kpOringal.y > pA.y-14){
					kpOringal.x -= OUTSIDE_SPEED;
					kpOringal.y -= OUTSIDE_SPEED;
				}else{
					kpOringal.x = pA.x - 14;
					kpOringal.y = pA.y - 14;
					kpState = STATE.LEFT_DOWN;
				}
				break;
			default:
			break;
		}
	}
	private void moveWJ(){
		switch(wjState){
			case LEFT_DOWN:
				if(wjOringal.x > pb.x-18 && wjOringal.y < pb.y+18){
					wjOringal.x -= INSIDE_SPEED;
					wjOringal.y += INSIDE_SPEED;
				}else{
					wjOringal.x = pb.x - 18;
					wjOringal.y = pb.y - 18;
					wjState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if(wjOringal.x < pc.x-18 && wjOringal.y < pc.y-18){
					wjOringal.x += INSIDE_SPEED;
					wjOringal.y += INSIDE_SPEED;
				}else{
					wjOringal.x = pc.x - 18;
					wjOringal.y = pc.y - 18;
					wjState = STATE.RIGHT_UP;
				}
				break;
			case RIGHT_UP:
				if(wjOringal.x < pd.x+18 && wjOringal.y > pd.y-18){
					wjOringal.x += INSIDE_SPEED;
					wjOringal.y -= INSIDE_SPEED;
				}else{
					wjOringal.x = pd.x - 18;
					wjOringal.y = pd.y - 18;
					wjState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(wjOringal.x > pa.x-18 && wjOringal.y > pa.y-18){
					wjOringal.x -= INSIDE_SPEED;
					wjOringal.y -= INSIDE_SPEED;
				}else{
					wjOringal.x = pa.x - 18;
					wjOringal.y = pa.y - 18;
					wjState = STATE.LEFT_DOWN;
				}
				break;
			default:
			break;
		}
	}
	private void moveCHE(){
		switch(cheState){
			case LEFT_DOWN:
				if(cheOringal.x > pb.x-18 && cheOringal.y < pb.y+18){
					cheOringal.x -= INSIDE_SPEED;
					cheOringal.y += INSIDE_SPEED;
				}else{
					cheOringal.x = pb.x - 19;
					cheOringal.y = pb.y - 19;
					cheState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if(cheOringal.x < pc.x-18 && cheOringal.y < pc.y-18){
					cheOringal.x += INSIDE_SPEED;
					cheOringal.y += INSIDE_SPEED;
				}else{
					cheOringal.x = pc.x - 19;
					cheOringal.y = pc.y - 19;
					cheState = STATE.RIGHT_UP;
				}
				break;
			case RIGHT_UP:
				if(cheOringal.x < pd.x+18 && cheOringal.y > pd.y-18){
					cheOringal.x += INSIDE_SPEED;
					cheOringal.y -= INSIDE_SPEED;
				}else{
					cheOringal.x = pd.x - 19;
					cheOringal.y = pd.y - 19;
					cheState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(cheOringal.x > pa.x-18 && cheOringal.y > pa.y-18){
					cheOringal.x -= INSIDE_SPEED;
					cheOringal.y -= INSIDE_SPEED;
				}else{
					cheOringal.x = pa.x - 19;
					cheOringal.y = pa.y - 19;
					cheState = STATE.LEFT_DOWN;
				}
				break;
			default:
			break;
		}
	}
	private void moveGWC(){
		switch(gwcState){
			case LEFT_DOWN:
				if(gwcOringal.x > pb.x-12 && gwcOringal.y < pb.y+12){
					gwcOringal.x -= INSIDE_SPEED;
					gwcOringal.y += INSIDE_SPEED;
				}else{
					gwcOringal.x = pb.x - 12;
					gwcOringal.y = pb.y - 12;
					gwcState = STATE.RIGHT_DOWN;
				}
			break;
			case RIGHT_DOWN:
				if(gwcOringal.x < pc.x-12 && gwcOringal.y < pc.y-12){
					gwcOringal.x += INSIDE_SPEED;
					gwcOringal.y += INSIDE_SPEED;
				}else{
					gwcOringal.x = pc.x - 12;
					gwcOringal.y = pc.y - 12;
					gwcState = STATE.RIGHT_UP;
				}
				break;
			case RIGHT_UP:
				if(gwcOringal.x < pd.x+12 && gwcOringal.y > pd.y-12){
					gwcOringal.x += INSIDE_SPEED;
					gwcOringal.y -= INSIDE_SPEED;
				}else{
					gwcOringal.x = pd.x - 12;
					gwcOringal.y = pd.y - 12;
					gwcState = STATE.LEFT_UP;
				}
				break;
			case LEFT_UP:
				if(gwcOringal.x > pa.x-12 && gwcOringal.y > pa.y-12){
					gwcOringal.x -= INSIDE_SPEED;
					gwcOringal.y -= INSIDE_SPEED;
				}else{
					gwcOringal.x = pa.x - 12;
					gwcOringal.y = pa.y - 12;
					gwcState = STATE.LEFT_DOWN;
				}
				break;
			default:
			break;
		}
	}
	
	/**
	 * 
	 * @param
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder){
		// TODO Auto-generated method stub
		try{
			pThread.isRun = true;
			pThread.start();
		}catch(IllegalThreadStateException e){
			// TODO: handle exception
		}
	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height){
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder){
		// TODO Auto-generated method stub
		try{
			pThread.isRun = false;
		}catch(IllegalThreadStateException e){
			// TODO: handle exception
		}
	}

}
