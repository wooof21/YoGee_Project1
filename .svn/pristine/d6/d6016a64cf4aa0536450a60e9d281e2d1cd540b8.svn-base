package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.SurfaceHolder.Callback;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private Button button1, button2;
	public static String button_str = "Himi_��SurfaceView����������ϰ";
	private int move_x = 2, x = 80;
	private Thread th;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint p;

	/*
	 * public MySurfaceView(Context context) { super(context); }//��ע1������һ��Ҫ����ע�⣬��ϸ�����ĶԱ�ע1�Ľ��� ��
	 */

	public MySurfaceView(Context context, AttributeSet attrs) {//��ע1
		// �����new�����Ĵ���ʵ��϶���û�����⣬��������Ϊ��������ʾSurfaceViewͬʱ��ʾ�����������԰��Զ����SurfaceViewҲ�������ע������main����xml�У�
		// ����������Ҫע�⣬����xml��ע��ľͱ���������ֳ�ʼ�������� ��ʼ����ʱ�������������
		// ��ʱ�������������һ����о�ʱ�䣬�����һ��Ⱥ�ѵİ����²ŷ����������������
		// ��ô�ڶ�������ָ���Զ���������һЩ����,���񳤿�һ������Ը��������,����ͨ����������ݵ�
		super(context, attrs);
		p = new Paint();
//		button1 = (Button) findViewById(R.id.button1);
//		button2 = (Button) findViewById(R.id.button2);
		p.setAntiAlias(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		th = new Thread(this);
		this.setKeepScreenOn(true);
		setFocusable(true);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		th.start(); 
	} 
	public void draw() {
		canvas = sfh.lockCanvas();
		canvas.drawColor(Color.WHITE);
		canvas.drawText(button_str, x + move_x, 100, p);
		sfh.unlockCanvasAndPost(canvas);
	}

	private void logic() { // �Լ�д�����ϰ�ߣ����������߼��ĺ���
		x += move_x;
		if (x > 200 || x < 80) {
			move_x = -move_x;
		}
	}

	@Override
	public boolean onKeyDown(int key, KeyEvent event) {

		return super.onKeyDown(key, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			draw();
			logic();
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
			}
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

}
