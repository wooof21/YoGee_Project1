package tools;

import view.PullableScrollView;

public interface ScrollViewListener {

	void onScrollChanged(PullableScrollView scrollView, int x, int y, int oldx,
			int oldy);

}
