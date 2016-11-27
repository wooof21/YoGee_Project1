package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import com.youge.jobfinder.R;

public class MyGifView extends View {

	// 表示开始播放gif图片的绝对时间
	private long movieStart = 0;
	// movie对象管理gif图片里面的多个帧
	private Movie movie;

	public MyGifView(Context context, AttributeSet attrs) {
		super(context, attrs);
		movie = Movie.decodeStream(context.getResources().openRawResource(
				R.drawable.order_post_pic));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		long currentTime = System.currentTimeMillis();
		// 第一次播放
		if (movieStart == 0) {
			movieStart = currentTime;
		}

		// 循环播放
		if (movie != null) {
			int duration = movie.duration();
			int relTime = (int) ((currentTime - movieStart) % duration);
			movie.setTime(relTime);
			movie.draw(canvas, 0, 0);
			// 强制重绘
			invalidate();
		}

		// 如果只想播放一次,只需判断currentTime-movieStart的值大于duration就不重绘即可

		super.onDraw(canvas);
	}
}
